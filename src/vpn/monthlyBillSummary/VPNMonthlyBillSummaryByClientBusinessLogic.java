package vpn.monthlyBillSummary;

import accounting.*;
import common.ModuleConstants;
import common.Month;
import common.bill.BillConstants;
import common.bill.BillDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import util.CurrentTimeFactory;
import util.KeyValuePair;
import util.ServiceDAOFactory;

public class VPNMonthlyBillSummaryByClientBusinessLogic implements LLIMonthlyBill {

    @Service
    AccountingIncidentService accountingIncidentService;

    @Override
    public void verifyPayment(BillDTO billDTO) throws Exception {
        VPNMonthlyBillSummaryByClient bill = (VPNMonthlyBillSummaryByClient) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(bill)
                .credit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable());

        verifyPayment(billDTO, accountingIncidentBuilder);
    }


    public void verifyPayment(BillDTO billDTO,
                              AccountingIncidentBuilder accountingIncidentBuilder)
            throws Exception {
        AccountingIncident accountingIncident = null;

        String description = "invoice id " + billDTO.getID() + " payment verified. ";

        if (billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED_FROM_SKIPPED) {
            //skipped flow
            accountingIncident = accountingIncidentBuilder
                    .clearAccountingEntryList()
                    .description(description)
                    .debit(AccountType.CASH, billDTO.getNetPayable())
                    .credit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                    .createAccountingIncident();
        } else if (billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED) {
            // first paid
            //normal flow
            accountingIncident = accountingIncidentBuilder
                    .description(description)
                    .debit(AccountType.CASH, billDTO.getNetPayable())
                    .createAccountingIncident();
        }

        try {
            accountingIncidentService.insertAccountingIncident(accountingIncident);
        } catch (Exception ex) {
            //
        }

        if (billDTO.getBillType() == BillConstants.DEMAND_NOTE) {
            try {
                // todo :set payment clear for DN
//                lliApplicationService.setApplicationAsPaymentClearedByDemandNoteID(billDTO.getID());
            } catch (Exception ex) {
//                    reviseService.setApplicationAsPaymentClearedByDemandNoteID(billDTO.getID());
            }
        }
    }

    @Override
    public void cancelBill(BillDTO billDTO) throws Exception {

    }

    @Override
    public void generate(BillDTO billDTO) throws Exception {
        VPNMonthlyBillSummaryByClient bill = (VPNMonthlyBillSummaryByClient) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuidler(bill);
        generateBill(billDTO, accountingIncidentBuilder);
    }

    public void generateBill(BillDTO billDTO, AccountingIncidentBuilder accountingIncidentBuilder) throws Exception {
        String description = "";
        if (billDTO instanceof VPNMonthlyBillSummaryByClient) {
            description = "Monthly Bill Generated For Client " + billDTO.getClientID()
                    + " For " + Month.getMonthNameById(billDTO.getMonth())
                    + "," + billDTO.getYear();
        }
        AccountingIncident accountingIncident = accountingIncidentBuilder.description(description)
                .debit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                .createAccountingIncident();
        accountingIncidentService.insertAccountingIncident(accountingIncident);
    }

    private AccountingIncidentBuilder getAccountingIncidentBuidler(VPNMonthlyBillSummaryByClient bill) {

        KeyValuePair<KeyValuePair<Double, Double>, KeyValuePair<Double, Double>> pairOfDiscountBWWithLoopCache = getPair(bill);

        double totalAdjustableDeduct = bill.getAdjustmentAmount();
        double totalBW = pairOfDiscountBWWithLoopCache.key.value + pairOfDiscountBWWithLoopCache.value.value;
        double totalLoopCharge = pairOfDiscountBWWithLoopCache.value.key;
        double totalDiscount = pairOfDiscountBWWithLoopCache.key.key;


        //TODO what to do if negetive value
		
		/*if(totalBW < 0)
			totalAdjustableDeduct -= totalBW;
		
		if(totalLoopCharge < 0)
			totalAdjustableDeduct -= totalLoopCharge;*/

        AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(bill)
                .credit(AccountType.VAT_PAYABLE_TO_NBR, bill.getVAT());

        if (totalAdjustableDeduct < 0)
            accountingIncidentBuilder.credit(AccountType.ADJUSTABLE, Math.abs(totalAdjustableDeduct));
        else
            accountingIncidentBuilder.debit(AccountType.ADJUSTABLE, totalAdjustableDeduct);

        if (totalDiscount < 0)
            accountingIncidentBuilder.credit(AccountType.DISCOUNT, Math.abs(totalDiscount));
        else
            accountingIncidentBuilder.debit(AccountType.DISCOUNT, totalDiscount);


        if (totalBW < 0)
            accountingIncidentBuilder.debit(AccountType.BANDWIDTH_COST, Math.abs(totalBW));
        else
            accountingIncidentBuilder.credit(AccountType.BANDWIDTH_COST, totalBW);

        if (totalLoopCharge < 0)
            accountingIncidentBuilder.debit(AccountType.LOCAL_LOOP_CHARGE, Math.abs(totalLoopCharge));
        else
            accountingIncidentBuilder.credit(AccountType.LOCAL_LOOP_CHARGE, totalLoopCharge);
		
		
		/*
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(bill)
				.debit(AccountType.ADJUSTABLE, bill.getAdjustmentAmount())
				.debit(AccountType.DISCOUNT, pairOfDiscountBWWithLoopCache.key.key)
				.credit(AccountType.BANDWIDTH_COST, pairOfDiscountBWWithLoopCache.key.value)
				.credit(AccountType.LOCAL_LOOP_CHARGE, pairOfDiscountBWWithLoopCache.value.key)
				.credit(AccountType.CACHE_COST, pairOfDiscountBWWithLoopCache.value.value)
				.credit(AccountType.VAT_PAYABLE_TO_NBR, bill.getVAT()) ;
		*/

        return accountingIncidentBuilder;
    }

    private AccountingIncidentBuilder createBaseIncidentBuilder(VPNMonthlyBillSummaryByClient bill) {
        return new AccountingIncidentBuilder()
                .clientID(bill.getClientID())
                .dateOfOccurance(CurrentTimeFactory.getCurrentTime())
                .dateOfRecord(CurrentTimeFactory.getCurrentTime())
                .moduleID(ModuleConstants.Module_ID_VPN);
    }

    private KeyValuePair<KeyValuePair<Double, Double>, KeyValuePair<Double, Double>> getPair(VPNMonthlyBillSummaryByClient bill) {
        double discount = 0;
        double bwCost = 0;
        double loopCost = 0;

        discount = bill.getDiscount();

        for (VPNMonthlyBillSummaryByItem item : bill.getVpnMonthlyBillSummaryByItems()) {

            switch (item.getType()) {
                case VPNMonthlyBillSummaryType.BANDWIDTH:
                case VPNMonthlyBillSummaryType.BANDWIDTH_ADJUSTMENT:
                    bwCost += item.getGrandCost();
                    break;

                case VPNMonthlyBillSummaryType.LOCAL_LOOP:
                case VPNMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT:
                    loopCost += item.getGrandCost();
                    break;

                default:
                    break;
            }
        }
        return new KeyValuePair<>(
                new KeyValuePair<Double, Double>(discount, bwCost),
                new KeyValuePair<Double, Double>(loopCost, 0.0)
        );
    }
}
