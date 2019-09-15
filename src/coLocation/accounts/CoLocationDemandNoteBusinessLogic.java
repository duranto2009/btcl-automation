package coLocation.accounts;

import accounting.*;
import coLocation.application.CoLocationApplicationDTO;
import coLocation.application.CoLocationApplicationService;
import coLocation.demandNote.CoLocationDemandNote;
import coLocation.demandNote.CoLocationYearlyDemandNote;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import demandNote.DemandNoteCommonBusinessLogic;
import requestMapping.Service;
import util.CurrentTimeFactory;

public class CoLocationDemandNoteBusinessLogic implements DemandNote {

    @Service
    AccountingIncidentService accountingIncidentService;
    @Service
    DemandNoteCommonBusinessLogic demandNoteCommonBusingessLogic;
    @Service
    CoLocationApplicationService coLocationApplicationService;

    private AccountingIncidentBuilder createBaseIncidentBuilder(BillDTO demandNote){
        return new AccountingIncidentBuilder()
                .clientID(demandNote.getClientID())
                .dateOfRecord(CurrentTimeFactory.getCurrentTime())
                .moduleID(ModuleConstants.Module_ID_COLOCATION)
                .dateOfOccurance(CurrentTimeFactory.getCurrentTime());

    }

    private AccountingIncidentBuilder getAccountingIncidentBuilder(CoLocationDemandNote coLocationDemandNote){
        AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(coLocationDemandNote)
                .debit(AccountType.COLOCATION_DISCOUNT, coLocationDemandNote.getDiscount())
                .credit(AccountType.COLOCATION_ADJUSTABLE, coLocationDemandNote.getAdvanceAdjustment())
                .credit(AccountType.COLOCATION_POWER_COST, coLocationDemandNote.getPowerCost())
                .credit(AccountType.COLOCATION_OFC_COST, coLocationDemandNote.getOfcCost())
                .credit(AccountType.COLOCATION_RACK_COST, coLocationDemandNote.getRackCost())
                .credit(AccountType.COLOCATION_FLOOR_SPACE_COST, coLocationDemandNote.getFloorSpaceCost())
                .credit(AccountType.COLOCATION_VAT_PAYABLE_TO_NBR, coLocationDemandNote.getVAT());

        return accountingIncidentBuilder;
    }

    @Override
    public void cancelBill(BillDTO billDTO) throws Exception {
        if(billDTO.getPaymentStatus() != BillDTO.UNPAID){
            throw new RequestFailureException("Demand note with invoice ID "
                    +billDTO.getID()+" can not be cancelled.");
        }
        CoLocationApplicationDTO application = coLocationApplicationService.getColocationApplicationByDemandNoteId(billDTO.getID());
        // null checking not necessary as common layer will throw will exception should the application is not found.
        application.setDemandNoteID(null);
        coLocationApplicationService.updateApplicaton(application);

    }

    @Override
    public void skip(BillDTO billDTO) throws Exception {
        CoLocationDemandNote demandNote = (CoLocationDemandNote) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(demandNote);

        String description = "Invoice id "+billDTO.getID()+" has been skipped";
        AccountingIncident accountingIncident = accountingIncidentBuilder
                .description(description)
                .debit(AccountType.COLOCATION_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                .createAccountingIncident();
        accountingIncidentService.insertAccountingIncident(accountingIncident);
    }

    @Override
    public void unskip(BillDTO billDTO) throws Exception {
        CoLocationDemandNote demandNote = (CoLocationDemandNote) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(demandNote);

        String description = "Invoice id "+billDTO.getID()+" has been unskipped";
        AccountingIncident accountingIncident = accountingIncidentBuilder
                .description(description)
                .debit(AccountType.COLOCATION_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                .reverse()
                .createAccountingIncident();

        accountingIncidentService.insertAccountingIncident(accountingIncident);
    }

    @Override
    public boolean isSkipable(BillDTO billDTO) throws Exception {
        return demandNoteCommonBusingessLogic.isSkipable(billDTO, ModuleConstants.Module_ID_COLOCATION);
    }

    @Override
    public boolean isUnskipable(BillDTO billDTO) throws Exception {
        return demandNoteCommonBusingessLogic.isUnskipable(billDTO, ModuleConstants.Module_ID_COLOCATION);
    }

    @Override
    //TODO raihan remove / refactor duplicate code.
    public void verifyPayment(BillDTO billDTO) throws Exception {
        CoLocationDemandNote demandNote = (CoLocationDemandNote)billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(demandNote);
        //lliBillCommonBusinessLogic.verifyPayment(billDTO, accountingIncidentBuilder);
        AccountingIncident accountingIncident =null;

        String description = "Payment of invoice id "+billDTO.getID()+" has been verified" ;

        if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED_FROM_SKIPPED){
            //skipped flow
            accountingIncident = accountingIncidentBuilder
                    .clearAccountingEntryList()
                    .description(description + "; the invoice previously was skipped")
                    .debit(AccountType.COLOCATION_CASH, billDTO.getNetPayable())
                    .credit(AccountType.COLOCATION_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                    .createAccountingIncident();
        }else if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED){
            // first paid
            //normal flow
            accountingIncident = accountingIncidentBuilder
                    .description(description)
                    .debit(AccountType.COLOCATION_CASH, billDTO.getNetPayable())
                    .createAccountingIncident();
        }
        accountingIncidentService.insertAccountingIncident(accountingIncident);
    }


}
