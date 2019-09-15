package scheduler.entity;

import accounting.GenerateBill;
import annotation.AccountingLogic;
import annotation.Transactional;
import coLocation.CoLocationConstants;
import coLocation.accounts.VariableCost.VariableCostService;
import coLocation.accounts.commonCost.AllVariableUnitCharge;
import coLocation.accounts.commonCost.CommonCostDTO;
import coLocation.accounts.commonCost.CommonCostService;
import coLocation.connection.CoLocationConnectionDTO;
import coLocation.connection.CoLocationConnectionService;
import coLocation.demandNote.CoLocationDemandNote;
import coLocation.demandNote.CoLocationDemandNoteService;
import coLocation.demandNote.CoLocationYearlyDemandNote;
import coLocation.td.CoLocationProbableTDService;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.bill.BillService;
import common.pdf.AsyncPdfService;
import common.repository.AllClientRepository;
import lombok.extern.log4j.Log4j;
import officialLetter.*;
import user.UserDTO;
import user.UserService;
import util.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Log4j
public class CoLocationTasks {
    private final static String DASH = "---------------------------------";
    private final static String METHOD_ENTRY = "**********METHOD_ENTRY************";
    private final static String METHOD_EXIT = "~~~~~~~~~~~~~METHOD_EXIT~~~~~~~~~~~";
    private final static String TASK_ENTRY = "**********TASK_ENTRY************";
    private final static String TASK_EXIT = "~~~~~~~~~~~~~TASK_EXIT~~~~~~~~~~~";
    void calculateCoLocationYearlyBill() throws Exception {

        CoLocationConnectionService connectionService = ServiceDAOFactory.getService(CoLocationConnectionService.class);
        CoLocationDemandNoteService demandNoteService = ServiceDAOFactory.getService(CoLocationDemandNoteService.class);
        CommonCostService commonCostService = ServiceDAOFactory.getService(CommonCostService.class);
        VariableCostService variableCostService = ServiceDAOFactory.getService(VariableCostService.class);


        Map<Long, List<CoLocationConnectionDTO>> map = getAllCoLocationConnectionMappedByHistoryId(connectionService);
        Map<Long, KeyValuePair<List<CoLocationConnectionDTO>, KeyValuePair<LocalDate, Boolean>>> filteredMap = getFilteredMap(map, connectionService);

        filteredMap.forEach(((k, v) -> {
            try {
                print(TASK_ENTRY);
                log.info("For History ID: " + k);
                List<CoLocationConnectionDTO> connections = v.getKey();
                log.info("BILL WORTHY CONNECTION LIST SIZE: " + connections.size());
                connections.forEach(t->log.info(t.getHistoryID() + " ### " + t.getID()));
                KeyValuePair<LocalDate, Boolean> pairOfNextBillDateAndAdjustmentNeeded = v.getValue();
                LocalDate nextBillDate = pairOfNextBillDateAndAdjustmentNeeded.getKey();
                boolean isAdjustmentCalculationNeeded = pairOfNextBillDateAndAdjustmentNeeded.getValue();

                if(isTodayIsNotYearlyBillGenerationDay(nextBillDate)) {
                    log.info("Aborted for history id: " + k);
                    log.info("Reason: Today is not the bill date");
                    return;
                }
                CoLocationConnectionDTO latestHistory = connections.stream()
                        .reduce((f,s)->s)
                        .orElseThrow(()->new RequestFailureException("No latest history found"));
                log.info("Latest History: " + latestHistory.toString());
                if(latestHistory.getValidTo() != Long.MAX_VALUE){
                    // TD or Closed;
                    log.info("Aborted for history id: " + k);
                    log.info("Reason: CoLocation Connection Either in Closed / TD state");
                    return;
                }

                double adjustment;
                if(isAdjustmentCalculationNeeded) {
                    adjustment = calculateAdjustment(connections, demandNoteService);
                }else {
                    adjustment = 0;
                }

                CoLocationYearlyDemandNote demandNote = createYearlyDemandNote(adjustment, latestHistory, commonCostService, variableCostService);

                handleDatabaseWriteOps(demandNote, latestHistory);
                AsyncPdfService.getInstance().accept(demandNote);
                log.info("Delegated to Async Pdf service");
                print(TASK_EXIT);
            } catch (Exception e) {
                log.fatal(e.getMessage());
                throw new RequestFailureException(e.getMessage());
            }
        }));
    }

    @Transactional
    void handleDatabaseWriteOps(CoLocationYearlyDemandNote demandNote, CoLocationConnectionDTO latestHistory) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        insertDemandNote(demandNote);
        log.info("demand note insertion done");
        saveOfficialLetter(demandNote.getClientID(), -1);
        log.info("Official letter saved");
        saveAccountingIncident(demandNote);
        log.info("Accounting Incident saved");
        saveProbableTD(latestHistory);
        log.info("Probable TD List updated");
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional
    void saveProbableTD(CoLocationConnectionDTO latestHistory) throws Exception {
        ServiceDAOFactory.getService(CoLocationProbableTDService.class).saveByConnection(latestHistory);
    }

    @Transactional
    void saveAccountingIncident(CoLocationYearlyDemandNote demandNote) throws Exception {
        AccountingLogic accountingLogic =  demandNote.getClass().getAnnotation(AccountingLogic.class);
        if(accountingLogic !=null && accountingLogic.value().newInstance() instanceof GenerateBill) {
            GenerateBill generator = (GenerateBill) ServiceDAOFactory.getService(accountingLogic.value());
            generator.generate(demandNote);
        }
    }

    private boolean isTodayIsNotYearlyBillGenerationDay(LocalDate nextBillDate) {
        return !nextBillDate.isEqual(LocalDate.now(ZoneId.systemDefault()));
    }

    private CoLocationYearlyDemandNote createYearlyDemandNote(double adjustment,
                                                              CoLocationConnectionDTO latestHistory,
                                                              CommonCostService commonCostService,
                                                              VariableCostService variableCostService) throws Exception {
        print(METHOD_ENTRY);
        log.fatal("CALCULATING YEARLY DEMAND NOTE");
        CoLocationYearlyDemandNote demandNote = new CoLocationYearlyDemandNote();
        demandNote.setClientID(latestHistory.getClientID());

        CommonCostDTO coLocationCommonCostVat = commonCostService.getAllFixedCostOfApplication(CoLocationConstants.MAXIMUM_VAT_PERCENTAGE_TYPE);
        demandNote.setVatPercentage(coLocationCommonCostVat.getPrice());
        demandNote.setDiscountPercentage(0);
        demandNote.setDescription("CoLocation Yearly Demand Note for " + latestHistory.getName() + ", Connection Id " + latestHistory.getHistoryID() + " Generated. ");

        AllVariableUnitCharge charge = variableCostService.getAllVariableUnitChargeByCoLocation(latestHistory);

        double monthCount = 12.;
        if (latestHistory.isRackNeeded()) {
            demandNote.setRackCost(charge.getRackCharge().getPrice() * monthCount);
        }
        if (latestHistory.isPowerNeeded()) {
            demandNote.setPowerCost(charge.getPowerCharge().getPrice() * monthCount);
        }
        if (latestHistory.isFiberNeeded()) {
            demandNote.setOfcCost(charge.getFiberCharge().getPrice() * monthCount);
        }
        if(latestHistory.isFloorSpaceNeeded()) {
            demandNote.setFloorSpaceCost(charge.getFloorSpaceCharge().getPrice() * monthCount);
        }

        demandNote.setConnectionId(latestHistory.getID());
        demandNote.setYearlyAdjustment(adjustment);


        double vatCalculable = demandNote.getOfcCost()
                + demandNote.getPowerCost()
                + demandNote.getRackCost()
                + demandNote.getFloorSpaceCost()
                + demandNote.getAdvanceAdjustment()
                + demandNote.getClosingCost()
                + demandNote.getDowngradeCost()
                + demandNote.getReconnectCost()
                + demandNote.getUpgradeCost();

        double vatCalculableWithAdjustment = vatCalculable - demandNote.getYearlyAdjustment();


        double vat = vatCalculableWithAdjustment * demandNote.getVatPercentage() / 100.0;

        double grandTotal = vatCalculableWithAdjustment;

        double discount = 0 * demandNote.getDiscountPercentage() / 100.0;
        if(Math.abs(discount) == 0){
            discount = 0.0;
        }
        double totalPayable = grandTotal - discount;
        double netPayable = totalPayable + vat;

        if(netPayable < 0 ){
            netPayable = 0;
            vat = 0;
            /// automatic paid/// automatic verified
            demandNote.setPaymentStatus(BillDTO.PAID_VERIFIED);

            /// description change hobe
            String additionalMsg = " However, the yearly adjustment(w/o VAT) was(" + adjustment + "), but the total bill(w/o VAT) was(" + vatCalculable + "). Hence, the bill is automatically paid and verified.";
            demandNote.setDescription(demandNote.getDescription() + additionalMsg);
        }
        demandNote.setVAT(vat);
        demandNote.setDiscount(discount);
        demandNote.setGrandTotal(grandTotal);
        demandNote.setTotalPayable(totalPayable);
        demandNote.setNetPayable(netPayable);
        demandNote.setLastPaymentDate(TimeConverter.getLastDateBeforeNMonth(1));

        log.info("Demand Note: " + demandNote );
        print(METHOD_EXIT);
        return demandNote;
    }

    private double calculateAdjustment(List<CoLocationConnectionDTO> connections, CoLocationDemandNoteService demandNoteService) throws Exception {
        //TESTED PARTIALLY : OK
        print(METHOD_ENTRY);
        log.fatal("CALCULATING ADJUSTMENT");
        List<Long> connectionIds = connections.stream()
                .map(CoLocationConnectionDTO::getID)
                .collect(Collectors.toList());
        log.info("Connection Ids");
        connectionIds.forEach(log::warn);
        double adjustment = demandNoteService.getTotalAdjustmentAmountByConnectionIds(connectionIds);
        log.warn("Adjustment: " + adjustment);
        print(METHOD_EXIT);
        return adjustment;
    }

    /***
     *
     * @param map History Id, List of Connection (All)
     * @param connectionService
     * @return map: key : History Id, value: KeyValuePair : first : List Filtered Connection
     *                                                      second : KeyValuePair first: LocalDate NextBillDate
     *                                                                            second: Boolean adjustmentCalculationNeeded or not
     */
    private Map<Long,
                KeyValuePair<
                        List<CoLocationConnectionDTO>,
                        KeyValuePair<LocalDate, Boolean>
                        >
            > getFilteredMap(Map<Long, List<CoLocationConnectionDTO>> map, CoLocationConnectionService connectionService) {
        // TESTED OK
        print(METHOD_ENTRY);
        log.fatal("CALCULATING FILTERED MAP : ONLY ELIGIBLE CONNECTIONS FOR BILLING GROUP BY HISTORY");
        Map<Long, KeyValuePair<List<CoLocationConnectionDTO>, KeyValuePair<LocalDate, Boolean>>> filteredMap =  map.entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey, entry -> {

//                                print(DASH);
                                log.info("For history id " + entry.getKey());
//                                log.info("Bill Worthy List for entry history: " + entry.getKey());

                                KeyValuePair<List<CoLocationConnectionDTO>, KeyValuePair<LocalDate, Boolean>> pair =  connectionService
                                        .getActiveYearlyBillWorthyConnectionsAndNextBillDate(entry.getKey(), entry.getValue());
                                log.info("Bill Date: "+pair.getValue().getKey());
                                log.info("Adjustment needed? " + pair.getValue().getValue());
                                return pair;

                        })
                );
//        printMap(filteredMap);
        print(METHOD_EXIT);
        return filteredMap;
    }

    @Transactional
    void saveOfficialLetter(long clientId, long applicationId) throws Exception {
        ServiceDAOFactory.getService(OfficialLetterService.class).saveDemandNoteAsOfficialLetter(
                CoLocationDemandNote.class,
                ModuleConstants.Module_ID_COLOCATION,
                applicationId,
                clientId,
                -1
        );

    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    void insertDemandNote(CoLocationYearlyDemandNote demandNote) throws Exception {
        demandNote.setYearlyDemandNote(true);
        ServiceDAOFactory.getService(BillService.class).insertBill(demandNote);
    }

    private Map<Long, List<CoLocationConnectionDTO>> getAllCoLocationConnectionMappedByHistoryId(CoLocationConnectionService connectionService) throws Exception {
        //TESTED OK
        print(METHOD_ENTRY);
        log.fatal("CALCULATING COLOCATION CONNECTIONS GROUP BY HISTORY");
        Map<Long, List<CoLocationConnectionDTO>> map = connectionService.getAllCoLocationConnectionMappedByHistory();
        printMap(map);
        print(METHOD_EXIT);

        return map;
    }

    private void printMap(Map<Long, List<CoLocationConnectionDTO>>map){
        log.info("RAW Map");
        map.forEach((k, v)->{
            log.warn("For History Id : " + k);
            v.forEach(t->{
                log.warn(t);
                print(DASH);
            });
            print(DASH);
        });
    }

    public static void print(String s) {log.info(s);}
}
