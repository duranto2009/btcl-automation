package vpn.demandNote;

import accounting.*;
import common.ModuleConstants;
import common.ServiceDAO;
import common.bill.BillDTO;
import demandNote.DemandNoteCommonBusinessLogic;
import global.GlobalService;
import requestMapping.Service;
import util.CurrentTimeFactory;
import util.ServiceDAOFactory;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationService;

import java.util.List;

public class VPNDemandNoteBusinessLogic implements DemandNote {

    @Service private DemandNoteCommonBusinessLogic demandNoteCommonBusinessLogic;
    @Service private VPNApplicationService vpnApplicationService;
    @Service private AccountingIncidentService accountingIncidentService;

    @Override
    public void cancelBill(BillDTO billDTO) throws Exception {
        demandNoteCommonBusinessLogic.canBeCancelled(billDTO);

        VPNApplication application = vpnApplicationService.getVPNApplicationByDemandNoteIdWithoutVPNLinks(billDTO.getID());
        application.setDemandNoteId(null);
        vpnApplicationService.updateApplicaton(application);
    }

    @Override
    public void skip(BillDTO billDTO) throws Exception {
        VPNDemandNote demandNote = (VPNDemandNote) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(demandNote);
        String description = "Invoice id "+billDTO.getID()+" has been skipped.";
        AccountingIncident accountingIncident = accountingIncidentBuilder
                .description(description)
                .debit(AccountType.VPN_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                .createAccountingIncident();
        accountingIncidentService.insertAccountingIncident(accountingIncident);
    }

    private void updateEligibility(long invoiceId) throws Exception {
        List<VPNLoopChargeDiscountEligibility> list = vpnApplicationService.getLocalLoopDiscountEligibilityByInvoiceId(invoiceId);
        GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
        for(VPNLoopChargeDiscountEligibility eligibility : list) {
            eligibility.setVerified(true);
            globalService.update(eligibility);
        }
    }

    private AccountingIncidentBuilder createBaseIncidentBuilder(BillDTO demandNote){
        return new AccountingIncidentBuilder()
                .clientID(demandNote.getClientID())
                .dateOfRecord(CurrentTimeFactory.getCurrentTime())
                .moduleID(ModuleConstants.Module_ID_VPN)
                .dateOfOccurance(CurrentTimeFactory.getCurrentTime());

    }
    private AccountingIncidentBuilder getAccountingIncidentBuilder(VPNDemandNote demandNote) {
        return createBaseIncidentBuilder(demandNote)
                .debit(AccountType.VPN_DISCOUNT, demandNote.getDiscount())
                .credit(AccountType.VPN_NEW_NETWORK_COST, demandNote.getRegistrationCharge())
                .credit(AccountType.VPN_OTC_LOCAL_LOOP_BTCL, demandNote.getOtcLocalLoopBTCL())
                .credit(AccountType.VPN_BANDWIDTH_COST, demandNote.getBandwidthCharge())
                .credit(AccountType.VPN_SECURITY, demandNote.getSecurityCharge())
                .credit(AccountType.VPN_LOCAL_LOOP_COST, demandNote.getLocalLoopCharge())
                .credit(AccountType.VPN_ADJUSTABLE_COST, demandNote.getAdvance())
                .credit(AccountType.VPN_OWNERSHIP_CHANGE_COST, demandNote.getOwnershipChangeCharge())
                .credit(AccountType.VPN_OTHER_COST, demandNote.getOtherCharge())
                .credit(AccountType.VPN_SHIFTING_COST, demandNote.getShiftingCharge())
                .credit(AccountType.VPN_DEGRADATION_COST, demandNote.getDegradationCharge())
                .credit(AccountType.VPN_RECONNECT_COST, demandNote.getReconnectCharge())
                .credit(AccountType.VPN_CLOSING_COST, demandNote.getClosingCharge())
                .credit(AccountType.VPN_VAT_PAYABLE_TO_NBR, demandNote.getVAT());
    }

    @Override
    public void unskip(BillDTO billDTO) throws Exception {
        VPNDemandNote demandNote = (VPNDemandNote) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(demandNote);

        // Application completed check ; TODO raihan-> see LLICommonBusinessLogic

        String description = "Bill id "+billDTO.getID()+" unskipped.";
        AccountingIncident accountingIncident = accountingIncidentBuilder
                .description(description)
                .debit(AccountType.VPN_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                .reverse()
                .createAccountingIncident();

        accountingIncidentService.insertAccountingIncident(accountingIncident);
    }

    @Override
    public boolean isSkipable(BillDTO billDTO) throws Exception {
        return demandNoteCommonBusinessLogic.isSkipable(billDTO, ModuleConstants.Module_ID_VPN);
    }

    @Override
    public boolean isUnskipable(BillDTO billDTO) throws Exception {
        return demandNoteCommonBusinessLogic.isUnskipable(billDTO, ModuleConstants.Module_ID_VPN);
    }

    @Override
    public void verifyPayment(BillDTO billDTO) throws Exception {
        VPNDemandNote demandNote = (VPNDemandNote) billDTO;
        AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(demandNote);

        AccountingIncident accountingIncident =null;
        String description = "Payment of invoice id "+billDTO.getID()+" has been verified" ;

        if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED_FROM_SKIPPED){
            //skipped flow
            accountingIncident = accountingIncidentBuilder
                    .clearAccountingEntryList()
                    .description(description + "; the invoice previously was skipped")
                    .debit(AccountType.VPN_CASH, billDTO.getNetPayable())
                    .credit(AccountType.VPN_ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
                    .createAccountingIncident();
        }else if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED){
            // first paid
            //normal flow
            accountingIncident = accountingIncidentBuilder
                    .description(description)
                    .debit(AccountType.VPN_CASH, billDTO.getNetPayable())
                    .createAccountingIncident();
        }
        accountingIncidentService.insertAccountingIncident(accountingIncident);
        updateEligibility(billDTO.getID());
    }
}
