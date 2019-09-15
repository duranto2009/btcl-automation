package vpn.monthlyBillSummary;

import accounting.AccountType;
import accounting.AccountingEntryService;
import accounting.GenerateBill;
import annotation.AccountingLogic;
import annotation.Transactional;
import lli.demandNote.adjustment.DNAdjustStatus;
import org.apache.log4j.Logger;
import requestMapping.Service;
import util.DateUtils;
import util.NumberUtils;
import util.ServiceDAOFactory;
import util.TransactionType;
import vpn.demandNote.VPNDemandNoteAdjustment;
import vpn.demandNote.VPNDemandNoteAdjustmentService;
import vpn.monthlyBill.VPNMonthlyBillByClient;
import vpn.monthlyBill.VPNMonthlyBillByLink;
import vpn.monthlyUsage.VPNMonthlyUsageByClient;
import vpn.monthlyUsage.VPNMonthlyUsageByLink;
import vpn.td.VPNProbableTDClientService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class VPNMonthlyBillSummaryGenerator {

    static Logger logger = Logger.getLogger(VPNMonthlyBillSummaryGenerator.class);

    @Service
    VPNDemandNoteAdjustmentService vpnDemandNoteAdjustmentService;
    @Service
    AccountingEntryService accountingEntryService;
    @Service
    VPNMonthlyBillSummaryByClientService vpnMonthlyBillSummaryByClientService;
    @Service
    VPNMonthlyBillSummaryByItemService vpnMonthlyBillSummaryByItemService;
    @Service
    VPNProbableTDClientService vpnProbableTDClientService;

    List<VPNDemandNoteAdjustment> demandNoteAdjustments;


    public List<VPNMonthlyBillSummaryByClient> generateSummary(
            List<VPNMonthlyBillByClient> vpnMonthlyBillByClients,
            List<VPNMonthlyUsageByClient> vpnMonthlyUsageByClients,
            List<VPNMonthlyBillSummaryByClient> lastMonthSummaries) {

        demandNoteAdjustments = vpnDemandNoteAdjustmentService.getAllByStatus(DNAdjustStatus.ACTIVE);

        List<VPNMonthlyBillSummaryByClient> list = new ArrayList<>();

        for (VPNMonthlyBillByClient client : vpnMonthlyBillByClients) {

            VPNMonthlyUsageByClient clientUsage = vpnMonthlyUsageByClients
                    .stream()
                    .filter(x -> x.getClientId() == client.getClientId())
                    .findFirst()
                    .orElse(new VPNMonthlyUsageByClient());

            VPNMonthlyBillSummaryByClient lastMonthClientSummary = lastMonthSummaries
                    .stream()
                    .filter(x -> x.getClientId() == client.getClientId())
                    .findFirst()
                    .orElse(new VPNMonthlyBillSummaryByClient());

            try {
                VPNMonthlyBillSummaryByClient clientsummery = generateSummary(client, clientUsage, lastMonthClientSummary);

                save(clientsummery);
                list.add(clientsummery);

            } catch (Exception ex) {
                logger.error("Exception while generating VPNMonthlyBillSummaryByClient for client id = " + client.getClientId(), ex);
            }
        }

        return list;
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    private void save(VPNMonthlyBillSummaryByClient clientsummery) throws Exception {

        vpnMonthlyBillSummaryByClientService.save(clientsummery);
        if (clientsummery.getId() == null)
            return;

        for (VPNMonthlyBillSummaryByItem item : clientsummery.getVpnMonthlyBillSummaryByItems()) {
            item.setMonthlyBillSummaryByClientId(clientsummery.getId());
            vpnMonthlyBillSummaryByItemService.save(item);
        }

        calculateTDDate(clientsummery);
        updateDemandNoteAdjustmentAsCalculated(clientsummery.getClientID());
        insertIntoAccounting(clientsummery);

    }

    @Transactional
    private void insertIntoAccounting(VPNMonthlyBillSummaryByClient vpnMonthlyBillByClient) throws Exception {
        AccountingLogic accountingLogic = vpnMonthlyBillByClient.getClass().getAnnotation(AccountingLogic.class);

        if (accountingLogic != null && accountingLogic.value().newInstance() instanceof GenerateBill) {
            GenerateBill generateBill = (GenerateBill) ServiceDAOFactory.getService(accountingLogic.value());

            double tempNetBill = vpnMonthlyBillByClient.getNetPayable();
            double tempVat = vpnMonthlyBillByClient.getVAT();


            VPNMonthlyBillSummaryByItem itemForDemandNote = vpnMonthlyBillByClient.getVpnMonthlyBillSummaryByItems()
                    .stream()
                    .filter(x -> x.getType() == VPNMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT)
                    .findFirst()
                    .orElse(null);

            if (itemForDemandNote != null) {
                //for generating monthly bill incedient without demand note
                vpnMonthlyBillByClient.setNetPayable(NumberUtils.formattedValue(vpnMonthlyBillByClient.getNetPayable() - itemForDemandNote.getNetCost()));
                vpnMonthlyBillByClient.setVAT(NumberUtils.formattedValue(vpnMonthlyBillByClient.getVAT() - itemForDemandNote.getVat()));

                if (vpnMonthlyBillByClient.getNetPayable() < 0)
                    vpnMonthlyBillByClient.setNetPayable(0);
                if (vpnMonthlyBillByClient.getVAT() < 0)
                    vpnMonthlyBillByClient.setVAT(0);

            }

            generateBill.generate(vpnMonthlyBillByClient);

            vpnMonthlyBillByClient.setNetPayable(tempNetBill);
            vpnMonthlyBillByClient.setVAT(tempVat);
        }
    }


    @Transactional
    private void updateDemandNoteAdjustmentAsCalculated(long clientId) throws Exception {
        List<VPNDemandNoteAdjustment> listOfDemandNoteAdjustment = demandNoteAdjustments
                .stream()
                .filter(d -> d.getClientId() == clientId)
                .collect(Collectors.toList());

        if (listOfDemandNoteAdjustment.size() > 0) {
            for (VPNDemandNoteAdjustment demandNoteAdjustment : listOfDemandNoteAdjustment) {
                demandNoteAdjustment.setStatus(DNAdjustStatus.COMPLETED);
                vpnDemandNoteAdjustmentService.save(demandNoteAdjustment);
            }
        }
    }


    @Transactional
    private void calculateTDDate(VPNMonthlyBillSummaryByClient clientBillSummary) throws Exception {
        //get security money balance info from table
        double securityAmount = accountingEntryService.getBalanceByClientIDAndAccountID(
                clientBillSummary.getClientId(),
                AccountType.SECURITY.getID());

        //get adjustment balance info from table
        double balance = accountingEntryService.getBalanceByClientIDAndAccountID(
                clientBillSummary.getClientId(),
                AccountType.ADJUSTABLE.getID());

        //get total due info from table
        double receivableAmount = accountingEntryService.getBalanceByClientIDAndAccountID(
                clientBillSummary.getClientId(),
                AccountType.ACCOUNT_RECEIVABLE_TD.getID());


        double availableAmount = securityAmount + balance - receivableAmount;

        double totalBWCost = clientBillSummary.getVpnMonthlyBillSummaryByItems()
                .stream()
                .filter(x -> x.type == VPNMonthlyBillSummaryType.BANDWIDTH)
                .mapToDouble(x -> x.getGrandCost())
                .sum();

        double perDayBWCost = totalBWCost / 30;
        int remainingDays = perDayBWCost <= 0 ? 0 : (int) (availableAmount / perDayBWCost);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, remainingDays);
        long tdDate = cal.getTime().getTime();

        vpnProbableTDClientService.updateTDDate(clientBillSummary.getClientID(), tdDate);

    }


    private VPNMonthlyBillSummaryByClient generateSummary(
            VPNMonthlyBillByClient clientBill,
            VPNMonthlyUsageByClient clientUsage,
            VPNMonthlyBillSummaryByClient lastMonthClientSummary) throws Exception {

        List<VPNMonthlyBillSummaryByItem> itemSummaries = new ArrayList<>();

        calculateCurrentMonthBill(itemSummaries, clientBill);
        calculateAdjustmentOfLastMonth(itemSummaries, clientUsage, lastMonthClientSummary);

        VPNMonthlyBillSummaryByClient clientSummary = new VPNMonthlyBillSummaryByClient();
        clientSummary.setVpnMonthlyBillSummaryByItems(itemSummaries);

        clientSummary.setClientId(clientBill.getClientId());
//        clientSummary.setTotalMbpsBreakDown(clientBill.getTotalMbpsBreakDown());


//        clientSummary.setBillingRangeBreakDown(clientBill.getBillingRangeBreakDown());
        clientSummary.setVatPercentage(clientBill.getVatPercentage());

        calculateBill(clientSummary);

        return clientSummary;
    }


    private void calculateBill(VPNMonthlyBillSummaryByClient clientBillSummary) throws Exception {
        double totalDiscount = 0, subTotal = 0, totalVat = 0;
        VPNMonthlyBillSummaryByItem demandNoteAdjustmentSummary = null;

        for (VPNMonthlyBillSummaryByItem sc : clientBillSummary.getVpnMonthlyBillSummaryByItems()) {
            sc.setType();
            sc.setGrandCost(NumberUtils.formattedValue(sc.getGrandCost()));
            sc.setTotalCost(NumberUtils.formattedValue(sc.getGrandCost() - sc.getDiscount()));

            if (sc.getType() == VPNMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT) {
                demandNoteAdjustmentSummary = sc;
                sc.setVat(NumberUtils.formattedValue(sc.getVat()));
            } else {
                sc.setVat(NumberUtils.formattedValue(sc.getTotalCost() * sc.getVatRate() / 100));
            }

            sc.setNetCost(NumberUtils.formattedValue(sc.getTotalCost() + sc.getVat()));

            subTotal += sc.getTotalCost();
            totalVat += sc.getVat();
            totalDiscount += sc.getDiscount();

        }
        //TODO :need to get yearly fee for additional ip

        subTotal = NumberUtils.formattedValue(subTotal);
        //get adjustment balance info from table
        double balance = accountingEntryService.getBalanceByClientIDAndAccountID(clientBillSummary.getClientId(), AccountType.ADJUSTABLE.getID());
        double adjustedAmount = 0.0;
        if (demandNoteAdjustmentSummary != null) {
            double subTotalWithoutDN = NumberUtils.formattedValue(subTotal - demandNoteAdjustmentSummary.getTotalCost());
            //adjustedAmount = subTotalWithoutDN <= 0 ? 0 : Math.min(subTotalWithoutDN, balance);
            adjustedAmount = Math.min(subTotalWithoutDN, balance);
        } else
            //adjustedAmount = subTotal <= 0 ? 0 : Math.min(subTotal, balance);
            adjustedAmount = Math.min(subTotal, balance);

        clientBillSummary.setAdjustmentAmount(NumberUtils.formattedValue(adjustedAmount));
        clientBillSummary.setGrandTotal(NumberUtils.formattedValue(subTotal));

        clientBillSummary.setTotalPayable(NumberUtils.formattedValue(clientBillSummary.getGrandTotal() - clientBillSummary.getAdjustmentAmount()));
        //if(clientBillSummary.getTotalPayable() < 0)
        //	clientBillSummary.setTotalPayable(0);
        /********/
        //not using calculated total vat cause there arises a issue while vat changed and totalpayable is zero
        clientBillSummary.setVAT(Math.ceil(clientBillSummary.getTotalPayable() * clientBillSummary.getVatPercentage() / 100));
        clientBillSummary.setNetPayable(NumberUtils.formattedValue(clientBillSummary.getTotalPayable() + clientBillSummary.getVAT()));

        clientBillSummary.setCreatedDate(DateUtils.getCurrentTime());

        //bill dto property set
        clientBillSummary.setGenerationTime(DateUtils.getCurrentTime());
        clientBillSummary.setActivationTimeFrom(DateUtils.getFirstDayOfMonth(0).getTime());
        clientBillSummary.setActivationTimeTo(DateUtils.getLastDayOfMonth(0).getTime());
        clientBillSummary.setLastPaymentDate(DateUtils.getLastDayOfMonth(0).getTime());
        clientBillSummary.setDeleted(false);
        clientBillSummary.setMonth(DateUtils.getMonthFromDate(DateUtils.getCurrentTime()));
        clientBillSummary.setYear(DateUtils.getYearFromDate(DateUtils.getCurrentTime()));


        clientBillSummary.setDiscountPercentage(0);
        clientBillSummary.setDiscount(NumberUtils.formattedValue(totalDiscount));
    }


    private void calculateAdjustmentOfLastMonth
            (
                    List<VPNMonthlyBillSummaryByItem> itemSummaries,
                    VPNMonthlyUsageByClient clientUsage,
                    VPNMonthlyBillSummaryByClient lastMonthClientSummary
            ) {

        VPNMonthlyBillSummaryByItem bandwidthAdjustmentSummary = new VPNMonthlyBillSummaryByItem();
        VPNMonthlyBillSummaryByItem loopAdjustmentSummary = new VPNMonthlyBillSummaryByItem();

        for (VPNMonthlyUsageByLink usageByLink : clientUsage.getMonthlyUsageByLinks()) {
            bandwidthAdjustmentSummary.setGrandCost(bandwidthAdjustmentSummary.getGrandCost() + usageByLink.getMbpsCost());// - connection.getDiscount());
            bandwidthAdjustmentSummary.setDiscount(bandwidthAdjustmentSummary.getDiscount() + usageByLink.getDiscount());

            loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() + usageByLink.getTotalLoopCost());
            //no discount for loop cost
        }

        bandwidthAdjustmentSummary.setType(VPNMonthlyBillSummaryType.BANDWIDTH_ADJUSTMENT);
        bandwidthAdjustmentSummary.setGrandCost(NumberUtils.formattedValue(bandwidthAdjustmentSummary.getGrandCost()));
        bandwidthAdjustmentSummary.setDiscount(bandwidthAdjustmentSummary.getDiscount());
        bandwidthAdjustmentSummary.setVatRate(clientUsage.getVatPercentage());

        loopAdjustmentSummary.setType(VPNMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT);
        loopAdjustmentSummary.setVatRate(clientUsage.getVatPercentage());

        for (VPNMonthlyBillSummaryByItem lastMonthItem : lastMonthClientSummary.getVpnMonthlyBillSummaryByItems()) {
            if (lastMonthItem.getType() == VPNMonthlyBillSummaryType.BANDWIDTH) {
                bandwidthAdjustmentSummary.setGrandCost(bandwidthAdjustmentSummary.getGrandCost() - lastMonthItem.getGrandCost());
                bandwidthAdjustmentSummary.setDiscount(bandwidthAdjustmentSummary.getDiscount() - lastMonthItem.getDiscount());
            } else if (lastMonthItem.getType() == VPNMonthlyBillSummaryType.LOCAL_LOOP) {
                loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() - lastMonthItem.getGrandCost());
            }

        }

        //adjust from demand note if exists
        List<VPNDemandNoteAdjustment> listOfDemandNoteAdjustment = demandNoteAdjustments
                .stream()
                .filter(d -> d.getClientId() == clientUsage.getClientId())
                .collect(Collectors.toList());

        if (demandNoteAdjustments.size() > 0) {
            VPNMonthlyBillSummaryByItem demandNoteAdjustmentSummary = new VPNMonthlyBillSummaryByItem();
            demandNoteAdjustmentSummary.setType(VPNMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT);

            for (VPNDemandNoteAdjustment demandNoteAdjustment : listOfDemandNoteAdjustment) {

                bandwidthAdjustmentSummary.setGrandCost(bandwidthAdjustmentSummary.getGrandCost() - demandNoteAdjustment.getBandWidthCharge());
                bandwidthAdjustmentSummary.setDiscount(bandwidthAdjustmentSummary.getDiscount() - demandNoteAdjustment.getBandWidthDiscount());

                loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() - demandNoteAdjustment.getLoopCharge());

                if (demandNoteAdjustment.getTotalDue() > 0) {
                    demandNoteAdjustmentSummary.setGrandCost(demandNoteAdjustmentSummary.getGrandCost() + demandNoteAdjustment.getTotalDue());
                    // to handle change in vat rate we are summing up demand note vat here
                    demandNoteAdjustmentSummary.setVat(demandNoteAdjustmentSummary.getVat() + demandNoteAdjustment.getVat());

                    //we will not add discount into adjustment
                    //cause TotalDue of demandNoteAdjustment is after discounted amount.
                    //Besides discount already included as BandwidthDiscount which has already been calculated above
                    //Though we are adding in grandCost of demandNoteAdjustmentSummary
                }
            }

            if (demandNoteAdjustmentSummary.getGrandCost() > 0)
                itemSummaries.add(demandNoteAdjustmentSummary);

        }

        itemSummaries.add(bandwidthAdjustmentSummary);
        itemSummaries.add(loopAdjustmentSummary);
    }


    private void calculateCurrentMonthBill(
            List<VPNMonthlyBillSummaryByItem> itemSummaries,
            VPNMonthlyBillByClient clientBill) {
        VPNMonthlyBillSummaryByItem bandwidthSummary = new VPNMonthlyBillSummaryByItem();
        VPNMonthlyBillSummaryByItem loopSummary = new VPNMonthlyBillSummaryByItem();

        for (VPNMonthlyBillByLink billByLink : clientBill.getMonthlyBillByLinks()) {

            bandwidthSummary.setGrandCost(bandwidthSummary.getGrandCost() + billByLink.getMbpsCost());// - connection.getDiscount());
            bandwidthSummary.setDiscount(bandwidthSummary.getDiscount() + billByLink.getDiscount());

            loopSummary.setGrandCost(loopSummary.getGrandCost() + billByLink.getTotalLoopCost());
            //no discount for local loop
        }

        bandwidthSummary.setType(VPNMonthlyBillSummaryType.BANDWIDTH);
        bandwidthSummary.setGrandCost(NumberUtils.formattedValue(bandwidthSummary.getGrandCost()));
        bandwidthSummary.setDiscount(bandwidthSummary.getDiscount());
        bandwidthSummary.setVatRate(clientBill.getVatPercentage());

        loopSummary.setType(VPNMonthlyBillSummaryType.LOCAL_LOOP);
        loopSummary.setVatRate(clientBill.getVatPercentage());

        itemSummaries.add(bandwidthSummary);
        itemSummaries.add(loopSummary);
    }


}
