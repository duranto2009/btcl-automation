package client.bill;

import accounting.AccountType;
import accounting.AccountingEntryService;
import accounting.AccountingIncidentService;
import annotation.Transactional;
import application.Application;
import application.ApplicationConditionBuilder;
import application.ApplicationState;
import coLocation.CoLocationConstants;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationDTOConditionBuilder;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionDTOConditionBuilder;
import coLocation.inventory.CoLocationInventoryService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.*;
import common.payment.constants.PaymentConstants;
import flow.repository.FlowRepository;
import global.GlobalService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIConnectionConditionBuilder;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationConditionBuilder;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseDTOConditionBuilder;
import lli.Application.ownership.LLIOwnerChangeConstant;
import lli.Application.ownership.LLIOwnerShipChangeApplication;
import lli.Application.ownership.LLIOwnerShipChangeApplicationConditionBuilder;
import nix.application.NIXApplication;
import nix.application.NIXApplicationConditionBuilder;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionConditionBuilder;
import nix.revise.NIXReviseDTO;
import nix.revise.NIXReviseDTOConditionBuilder;
import requestMapping.Service;
import util.EnglishNumberToWords;
import util.KeyValuePair;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.application.VPNApplicationLinkConditionBuilder;
import vpn.application.VPNApplicationService;
import vpn.network.VPNNetworkLink;
import vpn.network.VPNNetworkLinkConditionBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static common.bill.BillService.round;

public class CommonBillService {

    @Service
    GlobalService globalService;
    @Service
    AccountingEntryService accountingEntryService;

    @Service
    LLIFlowConnectionService lliFlowConnectionService;

    @Service
    VPNApplicationService vpnApplicationService;


    @Service
    CoLocationInventoryService coLocationInventoryService;



    @Service
    AccountingIncidentService accountingIncidentService;

    public KeyValuePair<List<BillDTO>, String> getMultipleMonthlyBill(long clientId, int module, long fromDate, long toDate) throws Exception {
//        Date from = new Date(fromDate);
//        Date to = new Date(toDate);
//        Calendar fromCal = Calendar.getInstance();
//        Calendar toCal = Calendar.getInstance();
//        fromCal.setTime(from);
//        toCal.setTime(to);
//
//        int fromMonth = fromCal.get(Calendar.MONTH);
//        int fromYear = fromCal.get(Calendar.YEAR);
//
//        int toMonth = toCal.get(Calendar.MONTH);
//        int toYear = toCal.get(Calendar.YEAR);
//        LocalDate fromDt = LocalDate.of(fromYear, fromMonth + 1, 1);
//        LocalDate toDt = LocalDate.of(toYear, toMonth + 1, 1);
//        long monthBetween = ChronoUnit.MONTHS.between(fromDt, toDt);
//        int month = fromMonth;
//        int year = fromYear;
//        List<LLIMonthlyBillSummaryByClient> lliMonthlyBillSummaryByClients = new ArrayList<>();
        List<BillDTO> billDTOS = new ArrayList<>();

        int moduleID = module * 100 + 2;
        ArrayList<Integer> multipleType = new ArrayList<>();
        multipleType.add(BillConstants.NOT_PART_OF_MULTIPLE_BILL);
//
//        for (int i = 0; i <= monthBetween; i++) {
//            List<BillDTO> billDTO = globalService.getAllObjectListByCondition(BillDTO.class,
//                    new BillDTOConditionBuilder()
//                            .Where()
//                            .clientIDEquals(clientId)
//                            .billTypeEquals(BillConstants.MONTHLY_BILL)
//                            .monthEquals(month)
//                            .yearEquals(year)
//                            .isMultipleIn(multipleType)
//                            .paymentStatusEquals(PaymentConstants.PAYMENT_STATUS_UNPAID)
//                            .entityTypeIDEquals(moduleID)
//                            .getCondition()
//            );
//
//            billDTOS.addAll(billDTO);
////            if (lliMonthlyBillSummaryByClient != null) {
////
////                lliMonthlyBillSummaryByClients.add(lliMonthlyBillSummaryByClient);
////            }
//
//            month++;
//            if (month >= 12) {
//                month = 0;
//                year = year + 1;
//            }
//
//        }

        billDTOS=globalService.getAllObjectListByCondition(BillDTO.class,
                new BillDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .billTypeEquals(BillConstants.MONTHLY_BILL)
                        .generationTimeGreaterThan(fromDate)
                        .generationTimeLessThan(toDate)
                        .isMultipleIn(multipleType)
                        .paymentStatusEquals(PaymentConstants.PAYMENT_STATUS_UNPAID)
                        .entityTypeIDEquals(moduleID)
                        .getCondition()
        );

        String NumToWords = convertTotalToWords(billDTOS);
        return new KeyValuePair<>(billDTOS, NumToWords);
    }

    public KeyValuePair<Long, List<BillDTO>> getFinalMonthlyBill(long clientId, int module, long lastDate) throws Exception {


        int entityTypeID = module * 100 + 2;
        KeyValuePair<Long, List<BillDTO>> billKeyValuePair = new KeyValuePair<>();
        List<BillDTO> billDTOS = new ArrayList<>();

        BillDTO finalBillDTO = checkIfFinalBillgenerated(clientId, entityTypeID);
        if (finalBillDTO == null) {

            ArrayList<Integer> billType = new ArrayList<Integer>();
            billType.add(BillConstants.MONTHLY_BILL);
            billType.add(BillConstants.MULTIPLE_MONTH_BILL);

            ArrayList<Integer> multipleType = new ArrayList<>();
            multipleType.add(BillConstants.NOT_PART_OF_MULTIPLE_BILL);
            multipleType.add(BillConstants.MULTIPLE_BILL_PARENT);
            billDTOS = globalService.getAllObjectListByCondition(BillDTO.class,
                    new BillDTOConditionBuilder()
                            .Where()
                            .clientIDEquals(clientId)
                            .billTypeIn(billType)
                            .paymentStatusEquals(PaymentConstants.PAYMENT_STATUS_UNPAID)
                            .isMultipleIn(multipleType)
                            .entityTypeIDEquals(entityTypeID)
                            .getCondition()
            );

            billKeyValuePair = generateFinalBill(billDTOS, lastDate, module,clientId);
        } else {
            List<MultipleBillMappingDTO> multipleBillMappingDTOS = globalService.getAllObjectListByCondition(MultipleBillMappingDTO.class,
                    new MultipleBillMappingDTOConditionBuilder()
                            .Where()
                            .commonBillIdEquals(finalBillDTO.getID())
                            .getCondition()
            );
            for (MultipleBillMappingDTO multipleBillMappingDTO : multipleBillMappingDTOS) {
                BillDTO billDTO1 = globalService.findByPK(BillDTO.class, multipleBillMappingDTO.getIndividualBillId());
                billDTOS.add(billDTO1);
            }
            billKeyValuePair.setKey(finalBillDTO.getID());
            billKeyValuePair.setValue(billDTOS);
        }
        return billKeyValuePair;
    }


    public BillDTO checkIfFinalBillgenerated(long clientId, int entityTypeId) throws Exception {

        boolean isGenerated = false;
        List<BillDTO> billDTO = globalService.getAllObjectListByCondition(BillDTO.class,
                new BillDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .entityTypeIDEquals(entityTypeId)
                        .billTypeEquals(BillConstants.FINAL_BILL)
                        .getCondition()
        );

        if (billDTO != null && billDTO.size() > 0) {
            if (billDTO.get(0).getPaymentStatus() == BillDTO.UNPAID) {
                return billDTO.get(0);
            } else if (billDTO.get(0).getPaymentStatus() == BillDTO.PAID_UNVERIFIED) {
                throw new RequestFailureException("Final Bill with Bill Id: " + billDTO.get(0).getID() + " is Already Paid by client and need verification");
            } else {
                throw new RequestFailureException("Final Bill with Bill Id: " + billDTO.get(0).getID() + " is Already Paid and verified");
            }
        } else {
            return null;
        }

    }

    public String convertTotalToWords(List<BillDTO> billDTOS) {

        double grandTotal = billDTOS
                .stream()
                .mapToDouble(BillDTO::getNetPayable)
                .sum();

        grandTotal = round(grandTotal, 2);


        String NumToWords = convertTotalToWords(grandTotal);

        return NumToWords;

    }


    public String convertTotalToWords(double grandTotal) {
        grandTotal = round(grandTotal, 2);
        String numberStr = Double.toString(grandTotal);
        String fractionalStr = numberStr.substring(numberStr.indexOf('.') + 1);
        long fractional = Long.valueOf(fractionalStr);
        String NumToWords = EnglishNumberToWords.convert((long) grandTotal) + " Taka " +
                EnglishNumberToWords.convert(fractional) + " Paisa Only ";

        return NumToWords;
    }


    @Transactional
    public KeyValuePair<Long, List<BillDTO>> generateMultipleBill(List<BillDTO> billDTOS, long lastPayDate, long fromDate, long toDate) throws Exception {
        BillDTO multipleBillDTO = new BillDTO();
        List<BillDTO> filteredBillDTO = billDTOS
                .stream()
                .filter(t -> t.getIsMultiple() == 0)
                .collect(Collectors.toList());

        if (billDTOS != null
                && filteredBillDTO != null
                && billDTOS.size() > 0
                && billDTOS.size() == filteredBillDTO.size()) {

            if (billDTOS != null && billDTOS.size() > 0) {

                multipleBillDTO = prepareBillDTOForMultipleBill(billDTOS, lastPayDate, BillConstants.MULTIPLE_MONTH_BILL);
                multipleBillDTO.setActivationTimeFrom(fromDate);
                multipleBillDTO.setActivationTimeTo(toDate);
                saveBillWithMapping(multipleBillDTO, billDTOS);
            }


        } else {
            throw new RequestFailureException(" There Is already multiple bill for one or more Bill that you selected");
        }
        return new KeyValuePair<>(multipleBillDTO.getID(), billDTOS);
    }

    @Transactional
    public KeyValuePair<Long, List<BillDTO>> generateFinalBill(List<BillDTO> billDTOS, long lastPayDate, int module,long clientId) throws Exception {
        BillDTO multipleBillDTO = new BillDTO();
        int entityTypeID=module*100+2;
        if (billDTOS != null && billDTOS.size() > 0) {
            List<BillDTO> paidUnverifiedBillDTOList = globalService.getAllObjectListByCondition(BillDTO.class,
                    new BillDTOConditionBuilder()
                            .Where()
                            .clientIDEquals(billDTOS.get(0).getClientID())
                            .paymentStatusEquals(BillDTO.PAID_UNVERIFIED)
                            .entityTypeIDEquals(entityTypeID)
                            .getCondition()

            );
            if (paidUnverifiedBillDTOList != null && paidUnverifiedBillDTOList.size() > 0) {
                String unverifiedIdListBuilder = "";
                for (BillDTO billDTO : paidUnverifiedBillDTOList
                ) {
                    unverifiedIdListBuilder += billDTO.getID();
                    for (int i = 1; i < paidUnverifiedBillDTOList.size(); i++) {
                        unverifiedIdListBuilder += " , ";
                    }
                }
                throw new RequestFailureException("Please Verify the bills with Bill ID: " + unverifiedIdListBuilder + " Before Generating Final Bill");
            } else {

                multipleBillDTO = prepareBillDTOForMultipleBill(billDTOS, lastPayDate, BillConstants.FINAL_BILL);
                multipleBillDTO.setIsMultiple(BillConstants.FINAL_BILL_PARENT);
                double securityMoney = (accountingEntryService
                        .getBalanceByClientIDAndAccountID(multipleBillDTO.getClientID()
                                , AccountType.SECURITY.getID()));
                multipleBillDTO.setTotalPayable(multipleBillDTO.getTotalPayable() - securityMoney);
                multipleBillDTO.setNetPayable(multipleBillDTO.getNetPayable() - securityMoney);

                saveBillWithMapping(multipleBillDTO, billDTOS);
                dicontinueClientByModule(module,clientId);

                // close services

            }

        } else {


            dicontinueClientByModule(module,clientId);
            throw new RequestFailureException("Client Closed Without generating Final Bill as No Earlier pending Bill Found : ");

        }
        return new KeyValuePair<>(multipleBillDTO.getID(), billDTOS);

    }


    public void dicontinueClientByModule(int module,long clientId) throws Exception {
        if (module == ModuleConstants.Module_ID_LLI) {
            disContinueClientForLLI(clientId);
        }
        else if (module == ModuleConstants.Module_ID_VPN) {
            disContinueClientForVPN(clientId);
        }else if(module==ModuleConstants.Module_ID_NIX){
            disContinueClientForNix(clientId);
        }
        else if(module==ModuleConstants.Module_ID_COLOCATION){
            disContinueClientForCoLocation(clientId);
        }
    }


    @Transactional
    public List<BillDTO> getIndividualBillFromSummedBill(BillDTO billDTO) throws Exception {

        List<BillDTO> individualDTOList = new ArrayList<>();
        List<BillDTO> multipleBillDToList = new ArrayList<>();

        List<MultipleBillMappingDTO> multipleBillMappingDTOS = globalService.getAllObjectListByCondition(MultipleBillMappingDTO.class,
                new MultipleBillMappingDTOConditionBuilder()
                        .Where()
                        .commonBillIdEquals(billDTO.getID())
                        .getCondition()
        );
        List<Long> billIds = new ArrayList<>();
        for (MultipleBillMappingDTO multipleBillMappingDTO : multipleBillMappingDTOS
        ) {
            billIds.add(multipleBillMappingDTO.getIndividualBillId());

        }

        List<BillDTO> allBillDTO = globalService.getAllObjectListByCondition(BillDTO.class,
                new BillDTOConditionBuilder()
                        .Where()
                        .IDIn(billIds)
                        .getCondition()
        );

        individualDTOList = allBillDTO
                .stream()
                .filter(t -> t.getBillType() == BillConstants.MONTHLY_BILL)
                .collect(Collectors.toList());
        multipleBillDToList = allBillDTO
                .stream()
                .filter(t -> t.getBillType() == BillConstants.MULTIPLE_MONTH_BILL)
                .collect(Collectors.toList());

        List<Long> multipleBillMappingIds = new ArrayList<>();
        if (multipleBillDToList != null && multipleBillDToList.size() > 0) {
            for (BillDTO billDTO1 : multipleBillDToList) {

                multipleBillMappingIds.add(billDTO1.getID());
            }


            List<MultipleBillMappingDTO> multipleBillMappingDTOS2 = globalService.getAllObjectListByCondition(MultipleBillMappingDTO.class,
                    new MultipleBillMappingDTOConditionBuilder()
                            .Where()
                            .commonBillIdIn(multipleBillMappingIds)
                            .getCondition()
            );

            List<Long> billIdsFromMultipleBill = new ArrayList<>();

            for (MultipleBillMappingDTO multipleBillMappingDTO : multipleBillMappingDTOS2
            ) {
                billIdsFromMultipleBill.add(multipleBillMappingDTO.getIndividualBillId());

            }

            List<BillDTO> allBillDTOFromMultipleBill = globalService.getAllObjectListByCondition(BillDTO.class,
                    new BillDTOConditionBuilder()
                            .Where()
                            .IDIn(billIdsFromMultipleBill)
                            .getCondition()
            );
            individualDTOList.addAll(allBillDTOFromMultipleBill);
        }


        return individualDTOList;

    }


    private void saveBillWithMapping(BillDTO multipleBillDTO, List<BillDTO> billDTOS) throws Exception {
        globalService.save(multipleBillDTO);
        for (BillDTO billDTO : billDTOS
        ) {

            MultipleBillMappingDTO multipleBillMappingDTO = new MultipleBillMappingDTO();
            multipleBillMappingDTO.setCommonBillId(multipleBillDTO.getID());
            multipleBillMappingDTO.setIndividualBillId(billDTO.getID());
            globalService.save(multipleBillMappingDTO);
//            if(billDTO.getIsMultiple()==BillConstants.NOT_PART_OF_MULTIPLE_BILL) {
            billDTO.setIsMultiple(BillConstants.MULTIPLE_BILL_CHILD);
//            }
            globalService.update(billDTO);
        }

    }


    private BillDTO prepareBillDTOForMultipleBill(List<BillDTO> billDTOS, long lastPayDate, int billType) {
        BillDTO multipleBillDTO = new BillDTO();

        double grandTotal = billDTOS
                .stream()
                .mapToDouble(BillDTO::getGrandTotal)
                .sum();


        double totalPayable = billDTOS
                .stream()
                .mapToDouble(BillDTO::getTotalPayable)
                .sum();
        double totalVat = billDTOS
                .stream()
                .mapToDouble(BillDTO::getVAT)
                .sum();

        double netPayable = billDTOS
                .stream()
                .mapToDouble(BillDTO::getNetPayable)
                .sum();


        double totalAdjust = billDTOS
                .stream()
                .mapToDouble(BillDTO::getAdjustmentAmount)
                .sum();

        multipleBillDTO.setBillType(billType);
        multipleBillDTO.setVatPercentage(billDTOS.get(0).getVatPercentage());
        multipleBillDTO.setDiscountPercentage(billDTOS.get(0).getDiscountPercentage());
        multipleBillDTO.setClientID(billDTOS.get(0).getClientID());
        multipleBillDTO.setGrandTotal(grandTotal); // previously it was set as totalPayable ; now why would  anyone save totalPayable as grandTotal ?
        multipleBillDTO.setTotalPayable(totalPayable);
        multipleBillDTO.setVAT(totalVat);
        multipleBillDTO.setDescription(billDTOS.get(0).getDescription());
        multipleBillDTO.setGenerationTime(System.currentTimeMillis());
        multipleBillDTO.setDeleted(billDTOS.get(0).isDeleted());
        multipleBillDTO.setLastModificationTime(System.currentTimeMillis());
        multipleBillDTO.setEntityID(billDTOS.get(0).getEntityID());
        multipleBillDTO.setEntityTypeID(billDTOS.get(0).getEntityTypeID());
        multipleBillDTO.setNetPayable(netPayable);
        multipleBillDTO.setReqID(billDTOS.get(0).getReqID());
//        multipleBillDTO.setClassName(billDTOS.get(0).getClassName());
        multipleBillDTO.setPaymentID(billDTOS.get(0).getPaymentID());
        multipleBillDTO.setDiscount(billDTOS.get(0).getDiscount());
        multipleBillDTO.setActivationTimeFrom(System.currentTimeMillis());
        multipleBillDTO.setActivationTimeTo(billDTOS.get(0).getActivationTimeTo());
        multipleBillDTO.setBillFilePath(billDTOS.get(0).getBillFilePath());
        multipleBillDTO.setBillReqType(billDTOS.get(0).getBillReqType());
        multipleBillDTO.setLateFee(billDTOS.get(0).getLateFee());
        multipleBillDTO.setPaymentStatus(billDTOS.get(0).getPaymentStatus());
        multipleBillDTO.setPaymentPortal(billDTOS.get(0).getPaymentPortal());
        multipleBillDTO.setLastPaymentDate(lastPayDate);
        multipleBillDTO.setAdjustmentAmount(totalAdjust);
        multipleBillDTO.setIsMultiple(BillConstants.MULTIPLE_BILL_PARENT);
        return multipleBillDTO;

    }


    public KeyValuePair<Boolean, JsonObject> getExistingInfo(long clientId, int moduleId) throws Exception {

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        Boolean key = false;
        BillDTO billDTO = checkIfFinalBillgenerated(clientId, moduleId * 100 + 2);
        if (billDTO != null) {
            key = true;

        }

        if (key == false) {

            if (moduleId == ModuleConstants.Module_ID_LLI) {

                HashMap<String, List<?>> lliData = getLLIRelatedData(clientId);
                for (Map.Entry<String, List<?>> entry : lliData.entrySet()) {
                    jsonObject.add(entry.getKey(), gson.toJsonTree(entry.getValue()));
                }


            } else if (moduleId == ModuleConstants.Module_ID_VPN) {
                //todo fetch vpn related data


                HashMap<String, List<?>> vpnData =  getVPNRelatedData(clientId);
                for (Map.Entry<String, List<?>> entry : vpnData.entrySet()) {
                    jsonObject.add(entry.getKey(), gson.toJsonTree(entry.getValue()));
                }
            }
            else if (moduleId == ModuleConstants.Module_ID_NIX) {
                //todo fetch vpn related data


                HashMap<String, List<?>> nixData =  getNIXRelatedData(clientId);
                for (Map.Entry<String, List<?>> entry : nixData.entrySet()) {
                    jsonObject.add(entry.getKey(), gson.toJsonTree(entry.getValue()));
                }
            }

            else if (moduleId == ModuleConstants.Module_ID_COLOCATION) {
                HashMap<String, List<?>> coLocData =  getCoLocationRelatedData(clientId);
                for (Map.Entry<String, List<?>> entry : coLocData.entrySet()) {
                    jsonObject.add(entry.getKey(), gson.toJsonTree(entry.getValue()));
                }
            }
        }

        return new KeyValuePair<>(key, jsonObject);
    }

    private HashMap<String, List<?>> getVPNRelatedData(long clientId) throws Exception {

        HashMap<String, List<?>> vpnDataMap = new HashMap<>();
        List<VPNNetworkLink> vpnNetworkLinks = globalService.getAllObjectListByCondition(VPNNetworkLink.class,
                new VPNNetworkLinkConditionBuilder()
                        .Where()
                        .clientIdEquals(clientId)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition()
        );


        List<Application> applications = globalService.getAllObjectListByCondition(Application.class,
                new ApplicationConditionBuilder()
                        .Where()
                        .clientIdEquals(clientId)
                        .isServiceStartedEquals(0)
                        .getCondition()
        );
//

        List<VPNApplication> allVpnAppWithLink=new ArrayList<>();
        for (Application application:applications
             ) {
            VPNApplication vpnApplication = vpnApplicationService.getVPNApplicationByApplicationIdWithoutVPNLinks(application.getApplicationId());
            List<VPNApplicationLink> vpnApplicationLinkList=globalService.getAllObjectListByCondition(VPNApplicationLink.class,
                    new VPNApplicationLinkConditionBuilder()
                    .Where()
                    .vpnApplicationIdEquals(vpnApplication.getVpnApplicationId())
                    .isServiceStarted(false)
                    .getCondition()
            );
            if (vpnApplicationLinkList!=null && vpnApplicationLinkList.size()>0){
                vpnApplication.setVpnApplicationLinks(vpnApplicationLinkList);
                allVpnAppWithLink.add(vpnApplication);
            }



        }



        vpnDataMap.put(CommonBillKeyConstants.VPN_LINK_KEY_NAME_FOR_MAP, vpnNetworkLinks);
        vpnDataMap.put(CommonBillKeyConstants.VPN_APPLICATION_KEY_NAME_FOR_MAP, allVpnAppWithLink);


        return vpnDataMap;
    }

    private HashMap<String, List<?>> getLLIRelatedData(long clientId) throws Exception {

        HashMap<String, List<?>> lliDataMap = new HashMap<>();
        List<LLIConnection> lliConnections = globalService.getAllObjectListByCondition(LLIConnection.class,
                new LLIConnectionConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition()
        );


        List<LLIApplication> lliApplications = globalService.getAllObjectListByCondition(LLIApplication.class,
                new LLIApplicationConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .isServiceStarted(false)
                        .getCondition()
        );
        if(lliApplications!=null) {
            for (LLIApplication lliApplication : lliApplications
            ) {
                lliApplication.setStateDescription(FlowRepository.getInstance().getFlowStateByFlowStateId(lliApplication.getState()).getViewDescription());
            }
        }


        List<ReviseDTO> reviseDTOS = globalService.getAllObjectListByCondition(ReviseDTO.class,
                new ReviseDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .isCompleted(false)
                        .getCondition()
        );

        if(reviseDTOS!=null) {
            for (ReviseDTO reviseDTO : reviseDTOS
            ) {
                reviseDTO.setStateDescription(FlowRepository.getInstance().getFlowStateByFlowStateId((int) reviseDTO.getState()).getViewDescription());
            }
        }


        List<LLIOwnerShipChangeApplication> summedLLLIOwnerShipChangeApplications = new ArrayList<>();
        List<LLIOwnerShipChangeApplication> lliOwnerShipChangeApplications = globalService.getAllObjectListByCondition(LLIOwnerShipChangeApplication.class,
                new LLIOwnerShipChangeApplicationConditionBuilder()
                        .Where()
                        .srcClientEquals(clientId)
                        .getCondition()
        )
                .stream()
                .filter(t -> t.getStatus() != LLIOwnerChangeConstant.APPLICATION_COMPLETED)
                .collect(Collectors.toList());

        summedLLLIOwnerShipChangeApplications.addAll(lliOwnerShipChangeApplications);

        List<LLIOwnerShipChangeApplication> lliOwnerShipChangeApplications2 = globalService.getAllObjectListByCondition(LLIOwnerShipChangeApplication.class,
                new LLIOwnerShipChangeApplicationConditionBuilder()
                        .Where()
                        .dstClientEquals(clientId)
                        .getCondition()
        )
                .stream()
                .filter(t -> t.getStatus() != LLIOwnerChangeConstant.APPLICATION_COMPLETED)
                .collect(Collectors.toList());

        summedLLLIOwnerShipChangeApplications.addAll(lliOwnerShipChangeApplications2);


        if(summedLLLIOwnerShipChangeApplications!=null) {
            for (LLIOwnerShipChangeApplication lliOwnerShipChangeApplication : summedLLLIOwnerShipChangeApplications
            ) {
                lliOwnerShipChangeApplication.setStateDescription(FlowRepository.getInstance().getFlowStateByFlowStateId((int) lliOwnerShipChangeApplication.getState()).getViewDescription());
            }
        }

        lliDataMap.put(CommonBillKeyConstants.LLI_CONNECTION_KEY_NAME_FOR_MAP, lliConnections);
        lliDataMap.put(CommonBillKeyConstants.LLI_APPLICATION_KEY_NAME_FOR_MAP, lliApplications);
        lliDataMap.put(CommonBillKeyConstants.LLI_REVISE_APPLICATION_KEY_NAME_FOR_MAP, reviseDTOS);
        lliDataMap.put(CommonBillKeyConstants.LLI_OWNER_CHANGE_APPLICATION_KEY_NAME_FOR_MAP, summedLLLIOwnerShipChangeApplications);

        return lliDataMap;

    }

    private HashMap<String, List<?>> getNIXRelatedData(long clientId) throws Exception {

        HashMap<String, List<?>> nixDataMap = new HashMap<>();
        List<NIXConnection> nixConnections = globalService.getAllObjectListByCondition(NIXConnection.class,
                new NIXConnectionConditionBuilder()
                        .Where()
                        .clientEquals(clientId)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition()
        );


        List<NIXApplication> nixApplications = globalService.getAllObjectListByCondition(NIXApplication.class,
                new NIXApplicationConditionBuilder()
                        .Where()
                        .clientEquals(clientId)
                        .isServiceStartedEquals(0)
                        .getCondition()
        );
        if(nixApplications!=null) {
            for (NIXApplication nixApplication : nixApplications
            ) {
                nixApplication.setStateDescription(FlowRepository.getInstance().getFlowStateByFlowStateId(nixApplication.getState()).getViewDescription());
            }
        }


        List<NIXReviseDTO> reviseDTOS = globalService.getAllObjectListByCondition(NIXReviseDTO.class,
                new NIXReviseDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .isCompleted(false)
                        .getCondition()
        );

        if(reviseDTOS!=null) {
            for (NIXReviseDTO reviseDTO : reviseDTOS
            ) {
                reviseDTO.setStateDescription(FlowRepository.getInstance().getFlowStateByFlowStateId((int) reviseDTO.getState()).getViewDescription());
            }
        }



        nixDataMap.put(CommonBillKeyConstants.NIX_CONNECTION_KEY_NAME_FOR_MAP, nixConnections);
        nixDataMap.put(CommonBillKeyConstants.NIX_APPLICATION_KEY_NAME_FOR_MAP, nixApplications);
        nixDataMap.put(CommonBillKeyConstants.NIX_REVISE_APPLICATION_KEY_NAME_FOR_MAP, reviseDTOS);

        return nixDataMap;

    }
    private HashMap<String, List<?>> getCoLocationRelatedData(long clientId) throws Exception {

        HashMap<String, List<?>> colocDataMap = new HashMap<>();
        List<CoLocationConnectionDTO> coLocationConnectionDTOS = globalService.getAllObjectListByCondition(CoLocationConnectionDTO.class,
                new CoLocationConnectionDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition()
        );


        List<CoLocationApplicationDTO> coLocationApplicationDTOS = globalService.getAllObjectListByCondition(CoLocationApplicationDTO.class,
                new CoLocationApplicationDTOConditionBuilder()
                        .Where()
                        .clientIDEquals(clientId)
                        .isServiceStarted(false)
                        .getCondition()
        );
        if(coLocationApplicationDTOS!=null) {
            for (CoLocationApplicationDTO locationApplicationDTO : coLocationApplicationDTOS
            ) {
                locationApplicationDTO.setStateDescription(FlowRepository.getInstance().getFlowStateByFlowStateId(locationApplicationDTO.getState()).getViewDescription());
            }
        }




        colocDataMap.put(CommonBillKeyConstants.COLOCATION_CONNECTION_KEY_NAME_FOR_MAP, coLocationConnectionDTOS);
        colocDataMap.put(CommonBillKeyConstants.COLOCATION_APPLICATION_KEY_NAME_FOR_MAP, coLocationApplicationDTOS);

        return colocDataMap;

    }

    private void disContinueClientForLLI(long clientId) throws Exception {

        HashMap<String, List<?>> lliDataMap = getLLIRelatedData(clientId);
        int state = ApplicationState.APPLICATION_CLOSED_DUE_TO_FINAL_BILL_GENERATION.getState();
        for (Map.Entry<String, List<?>> entry : lliDataMap.entrySet()) {
            String key = entry.getKey();
            if (key.equals(CommonBillKeyConstants.LLI_CONNECTION_KEY_NAME_FOR_MAP)) {
                List<LLIConnection> lliConnections = (List<LLIConnection>) entry.getValue();
                for (LLIConnection lliConnection : lliConnections
                ) {
                    lliFlowConnectionService.closeConnectionDeallocateInventoryAndIP(lliConnection);
                }
            } else if (key.equals(CommonBillKeyConstants.LLI_APPLICATION_KEY_NAME_FOR_MAP)) {
                List<LLIApplication> lliApplications = (List<LLIApplication>) entry.getValue();
                for (LLIApplication lliApplication : lliApplications
                ) {
                    lliApplication.setState(state);
                    globalService.update(lliApplication);


                }
            } else if (key.equals(CommonBillKeyConstants.LLI_REVISE_APPLICATION_KEY_NAME_FOR_MAP)) {

                List<ReviseDTO> reviseDTOS = (List<ReviseDTO>) entry.getValue();
                for (ReviseDTO reviseDTO : reviseDTOS
                ) {
                    reviseDTO.setState(state);
                    globalService.update(reviseDTO);
                }

            } else if (key.equals(CommonBillKeyConstants.LLI_OWNER_CHANGE_APPLICATION_KEY_NAME_FOR_MAP)) {

                List<LLIOwnerShipChangeApplication> lliOwnerShipChangeApplications = (List<LLIOwnerShipChangeApplication>) entry.getValue();
                for (LLIOwnerShipChangeApplication lliOwnerShipChangeApplication : lliOwnerShipChangeApplications
                ) {
                    lliOwnerShipChangeApplication.setState(state);
                    globalService.update(lliOwnerShipChangeApplication);
                }

            }
        }

    }

    private void disContinueClientForVPN(long clientId) throws Exception {

        HashMap<String, List<?>> vpnDataMap = getVPNRelatedData(clientId);
        ApplicationState applicationState= ApplicationState.APPLICATION_CLOSED_DUE_TO_FINAL_BILL_GENERATION;
        for (Map.Entry<String, List<?>> entry : vpnDataMap.entrySet()) {
            String key = entry.getKey();
            if (key.equals(CommonBillKeyConstants.VPN_LINK_KEY_NAME_FOR_MAP)) {
                List<VPNNetworkLink> vpnNetworkLinks = (List<VPNNetworkLink>) entry.getValue();
                for (VPNNetworkLink vpnNetworkLink : vpnNetworkLinks
                ) {

                    vpnApplicationService.closeLink(vpnNetworkLink,clientId);
                }
            } else if (key.equals(CommonBillKeyConstants.VPN_APPLICATION_KEY_NAME_FOR_MAP)) {
                List<VPNApplication> vpnApplications = (List<VPNApplication>) entry.getValue();
                for (VPNApplication vpnApplication : vpnApplications
                ) {

                    for (VPNApplicationLink vpnApplicationLink:vpnApplication.getVpnApplicationLinks()
                         ) {

                        vpnApplicationLink.setLinkState(applicationState);
                        globalService.update(vpnApplicationLink);

                    }



                }
            }
        }

    }

    private void disContinueClientForNix(long clientId) throws Exception {

        HashMap<String, List<?>> nixData = getLLIRelatedData(clientId);
        int state = ApplicationState.APPLICATION_CLOSED_DUE_TO_FINAL_BILL_GENERATION.getState();
        for (Map.Entry<String, List<?>> entry : nixData.entrySet()) {
            String key = entry.getKey();
            if (key.equals(CommonBillKeyConstants.NIX_CONNECTION_KEY_NAME_FOR_MAP)) {
                List<NIXConnection> nixConnections = (List<NIXConnection>) entry.getValue();
                for (NIXConnection nixConnection : nixConnections
                ) {
//                    closeConnectionDeallocateInventoryAndIP(nixConnection);
                }
            }
            else if (key.equals(CommonBillKeyConstants.NIX_APPLICATION_KEY_NAME_FOR_MAP)) {
                List<NIXApplication> nixApplications = (List<NIXApplication>) entry.getValue();
                for (NIXApplication nixApplication : nixApplications
                ) {
                    nixApplication.setState(state);
                    globalService.update(nixApplication);


                }
            }
            else if (key.equals(CommonBillKeyConstants.NIX_REVISE_APPLICATION_KEY_NAME_FOR_MAP)) {

                List<NIXReviseDTO> reviseDTOS = (List<NIXReviseDTO>) entry.getValue();
                for (NIXReviseDTO reviseDTO : reviseDTOS
                ) {
                    reviseDTO.setState(state);
                    globalService.update(reviseDTO);
                }

            }
        }

    }




    private void disContinueClientForCoLocation(long clientId) throws Exception {

        HashMap<String, List<?>> ColocDataMap = getCoLocationRelatedData(clientId);
        int state = ApplicationState.APPLICATION_CLOSED_DUE_TO_FINAL_BILL_GENERATION.getState();
        for (Map.Entry<String, List<?>> entry : ColocDataMap.entrySet()) {
            String key = entry.getKey();
            if (key.equals(CommonBillKeyConstants.COLOCATION_CONNECTION_KEY_NAME_FOR_MAP)) {
                List<CoLocationConnectionDTO> coLocationConnectionDTOS = (List<CoLocationConnectionDTO>) entry.getValue();
                for (CoLocationConnectionDTO coLocationConnectionDTO : coLocationConnectionDTOS
                ) {

                    coLocationConnectionDTO.setStatus(CoLocationConstants.STATUS_CLOSED);
                    coLocationConnectionDTO.setValidTo(System.currentTimeMillis());
                    coLocationConnectionDTO.setIncident(CoLocationConstants.CLOSE);
                    coLocationInventoryService.deallocateInventory(coLocationConnectionDTO);
                }
            }
            else if (key.equals(CommonBillKeyConstants.COLOCATION_APPLICATION_KEY_NAME_FOR_MAP)) {
                List<CoLocationApplicationDTO> coLocationApplicationDTOS = (List<CoLocationApplicationDTO>) entry.getValue();
                for (CoLocationApplicationDTO locationApplicationDTO : coLocationApplicationDTOS
                ) {
                    locationApplicationDTO.setState(state);
                    globalService.update(coLocationApplicationDTOS);


                }
            }

        }

    }
}
