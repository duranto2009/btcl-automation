package scheduler.entity;

import accounting.GenerateBill;
import annotation.AccountingLogic;
import annotation.CurrentTime;
import annotation.Transactional;
import common.ModuleConstants;
import common.RequestFailureException;
import common.StringUtils;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.bill.BillService;
import global.GlobalService;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationConditionBuilder;
import lli.connection.LLIConnectionConstants;
import lli.demandNote.LLISingleConnectionCommonDemandNote;
import lombok.extern.log4j.Log4j;
import officialLetter.OfficialLetterService;
import util.CurrentTimeFactory;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j
class IPTasks {

    private GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
    private BillService billService = ServiceDAOFactory.getService(BillService.class);
    private OfficialLetterService officialLetterService = ServiceDAOFactory.getService(OfficialLetterService.class);

    void generateYearlyBill() throws Exception {

        //find additional ip applications which are completed
        List<LLIApplication> lliApplicationList = getCompletedLLIAdditionalIPApps();

        //find all bill ids
        List<Long> billIds = getBillIdsFromApplications(lliApplicationList);

        // check the size; theoretically the size of the two list must be equal
        // i.e. completed apps must have demand note ids.
        checkEqualLength(billIds, lliApplicationList);

        // if empty nothing to do here. go back to your cave!
        if(billIds.isEmpty()) return;

        // filter the bills :: criteria : today is the "billing date" of the examined bill.
        // "billing date : 11 months/ 1 year less than 1 month."
        List<LLISingleConnectionCommonDemandNote> filteredBills = getFilteredBills(billIds);

        // mutate the bills : this bills need to be inserted as yearly bill; so several properties
        List<LLISingleConnectionCommonDemandNote> mutatedFilteredBills = getMutatedBills(filteredBills);

        // you are almost there. save these mutated bills, official letter, accounting incident in db.
        saveInDB(mutatedFilteredBills);

    }

    @Transactional
    void saveInDB(List<LLISingleConnectionCommonDemandNote> mutatedFilteredBills) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        for(LLISingleConnectionCommonDemandNote demandNote: mutatedFilteredBills) {
            billService.insertBillIndividualTransaction(demandNote);
            officialLetterService.saveDemandNoteAsOfficialLetter(LLISingleConnectionCommonDemandNote.class,
                    ModuleConstants.Module_ID_LLI, -1, demandNote.getClientID(), -1);
            AccountingLogic accountingLogic = demandNote.getClass().getDeclaredAnnotation(AccountingLogic.class);
            if(accountingLogic != null && accountingLogic.value().newInstance() instanceof GenerateBill) {
                GenerateBill generator = (GenerateBill) ServiceDAOFactory.getService(accountingLogic.value());
                generator.generate(demandNote);
            }
        }
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    private List<LLISingleConnectionCommonDemandNote> getMutatedBills(List<LLISingleConnectionCommonDemandNote> filteredBills) {
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        return filteredBills.stream()
                .peek(t-> {
                    t.setYear(now.getYear());
                    t.setMonth(now.getMonthValue()-1);
                    t.setPaymentID(null);
                    t.setIsMultiple(BillConstants.NOT_PART_OF_MULTIPLE_BILL);
                    t.setPaymentStatus(BillDTO.UNPAID);
                    t.setBillType(BillConstants.YEARLY_BILL);
                    t.setGenerationTime(System.currentTimeMillis());
                    t.setDescription("Yearly Bill :: IP Address :: " + t.getClientID());
                    t.setBaseBillId(t.getID());
                }).collect(Collectors.toList());
    }

    private List<LLISingleConnectionCommonDemandNote> getFilteredBills(List<Long> billIds) throws Exception {
        List<List<?>> lists =  globalService.join(
                BillDTO.class, LLISingleConnectionCommonDemandNote.class, "bill",
                "common", "blID", "lli_common_dn_parent_bill_id", "=",
                "JOIN", "WHERE bill.blID IN " + StringUtils.getCommaSeparatedString(billIds),
                Collections.singletonList(LLISingleConnectionCommonDemandNote.class)
        );
        List<LLISingleConnectionCommonDemandNote> bills = (List<LLISingleConnectionCommonDemandNote>)lists.get(0);
        return bills.stream()
                .filter(IPTasks::worthyBill)
                .collect(Collectors.toList());
    }

    private static boolean worthyBill(LLISingleConnectionCommonDemandNote lliSingleConnectionCommonDemandNote) {
        LocalDate nextBillDate = TimeConverter.getStandardNextYearDate(lliSingleConnectionCommonDemandNote.getGenerationTime());
        return nextBillDate.isEqual(LocalDate.now(ZoneId.systemDefault()));
    }


    private List<Long> getBillIdsFromApplications(List<LLIApplication> lliApplicationList) {
        return lliApplicationList.stream()
                .map(LLIApplication::getDemandNoteID)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    private List<LLIApplication> getCompletedLLIAdditionalIPApps() throws Exception {
        return globalService.getAllObjectListByCondition(LLIApplication.class,
                new LLIApplicationConditionBuilder()
                        .Where()
                        .applicationTypeEquals(LLIConnectionConstants.ADDITIONAL_IP)
                        .isServiceStarted(true)
                        .getCondition()
        );
    }

    private void checkEqualLength(List<Long> billIds, List<LLIApplication> lliApplicationList) {
        if(billIds.size() != lliApplicationList.size()) {
            log.fatal("bill Ids");
            billIds.forEach(log::info);
            log.fatal("app demand note ids");
            lliApplicationList.forEach(t->log.info(t.getDemandNoteID()));

            throw new RequestFailureException("Error in generating lli ip address yearly bill");

        }
    }
}
