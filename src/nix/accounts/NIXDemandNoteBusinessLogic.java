package nix.accounts;

import accounting.*;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import demandNote.DemandNoteCommonBusinessLogic;
import nix.application.NIXApplication;
import nix.demandnote.NIXDemandNote;
import nix.application.NIXApplicationService;
import requestMapping.Service;
import util.CurrentTimeFactory;

public class NIXDemandNoteBusinessLogic implements DemandNote {

    @Service private NIXApplicationService nixNIXApplicationService;
    @Service private AccountingIncidentService accountingIncidentService;
    @Service private DemandNoteCommonBusinessLogic demandNoteCommonBusinessLogic;


    private AccountingIncidentBuilder createBaseIncidentBuilder(BillDTO nixDemandNote){
        return new AccountingIncidentBuilder()
                .clientID(nixDemandNote.getClientID())
                .dateOfRecord(CurrentTimeFactory.getCurrentTime())
                .moduleID(ModuleConstants.Module_ID_NIX)
                .dateOfOccurance(CurrentTimeFactory.getCurrentTime());

    }

    private AccountingIncidentBuilder getAccountingIncidentBuilder(NIXDemandNote nixNIXDemandNote){

        return createBaseIncidentBuilder(nixNIXDemandNote)
                .debit(AccountType.NIX_DISCOUNT, nixNIXDemandNote.getDiscount())
                .credit(AccountType.NIX_SECURITY, nixNIXDemandNote.getSecurityMoney())
                .credit(AccountType.NIX_NEW_CONNECTION_CHARGE, nixNIXDemandNote.getRegistrationFee())
                .credit(AccountType.NIX_ADJUSTABLE, nixNIXDemandNote.getAdvanceAdjustment())
                .credit(AccountType.NIX_PORT, nixNIXDemandNote.getNixPortCharge())
                .credit(AccountType.NIX_PORT_UPGRADE, nixNIXDemandNote.getNixPortUpgradeCharge())
                .credit(AccountType.NIX_PORT_DOWNGRADE, nixNIXDemandNote.getNixPortDowngradeCharge())
                .credit(AccountType.NIX_INSTANT_DEGRADATION, nixNIXDemandNote.getInstantDegradationCharge())
                .credit(AccountType.NIX_RECONNECT, nixNIXDemandNote.getReconnectCharge())
                .credit(AccountType.NIX_CLOSING_OTC, nixNIXDemandNote.getClosingCharge())
                .credit(AccountType.NIX_LOCAL_LOOP, nixNIXDemandNote.getLocalLoopCharge())
                .credit(AccountType.NIX_VAT_PAYABLE_TO_NBR, nixNIXDemandNote.getVAT());
    }

    @Override
    public void cancelBill(BillDTO billDTO) throws Exception {
        demandNoteCommonBusinessLogic.canBeCancelled(billDTO);

        NIXApplication application = nixNIXApplicationService.getNIXApplicationByDemandNoteId(billDTO.getID());
        //no need to check null as common layer will throw exception should there be a null occurrence
        application.setDemandNote(null);
        nixNIXApplicationService.updateApplicaton(application);
    }

    @Override
    public void skip(BillDTO billDTO) throws Exception {
        NIXDemandNote NIXDemandNote = (NIXDemandNote) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(NIXDemandNote);
        String description = "Invoice id "+billDTO.getID()+" has been skipped.";
        AccountingIncident accountingIncident = accountingIncidentBuilder
                .description(description)
                .debit(AccountType.NIX_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                .createAccountingIncident();
        accountingIncidentService.insertAccountingIncident(accountingIncident);
    }

    @Override
    public void unskip(BillDTO billDTO) throws Exception {
        NIXDemandNote NIXDemandNote = (NIXDemandNote) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(NIXDemandNote);

        // Application completed check ; TODO raihan-> see LLICommonBusinessLogic

        String description = "Bill id "+billDTO.getID()+" unskipped.";
        AccountingIncident accountingIncident = accountingIncidentBuilder
                .description(description)
                //TD or NOT_TD ; TODO raihan
                .debit(AccountType.NIX_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                .reverse()
                .createAccountingIncident();

        accountingIncidentService.insertAccountingIncident(accountingIncident);

    }

    @Override
    public boolean isSkipable(BillDTO billDTO) throws Exception {
       return demandNoteCommonBusinessLogic.isSkipable(billDTO, ModuleConstants.Module_ID_NIX);
    }

    @Override
    public boolean isUnskipable(BillDTO billDTO) throws Exception {
       return demandNoteCommonBusinessLogic.isUnskipable(billDTO, ModuleConstants.Module_ID_NIX);
    }

    @Override
    public void verifyPayment(BillDTO billDTO) throws Exception {
        NIXDemandNote NIXDemandNote = (NIXDemandNote) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(NIXDemandNote);

        AccountingIncident accountingIncident =null;
        String description = "Payment of invoice id "+billDTO.getID()+" has been verified" ;

        if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED_FROM_SKIPPED){
            //skipped flow
            accountingIncident = accountingIncidentBuilder
                    .clearAccountingEntryList()
                    .description(description + "; the invoice previously was skipped")
                    .debit(AccountType.NIX_CASH, billDTO.getNetPayable())
                    .credit(AccountType.NIX_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                    .createAccountingIncident();
        }else if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED){
            // first paid
            //normal flow
            accountingIncident = accountingIncidentBuilder
                    .description(description)
                    .debit(AccountType.NIX_CASH, billDTO.getNetPayable())
                    .createAccountingIncident();
        }
        accountingIncidentService.insertAccountingIncident(accountingIncident);

    }
}
