package lli.demandNote;

import accounting.AccountType;
import accounting.AccountingIncidentBuilder;
import accounting.AccountingIncidentService;
import accounting.DemandNote;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import lli.bill.LLIBillCommonBusinessLogic;
import lli.demandNote.adjustment.DNAdjustStatus;
import lli.demandNote.adjustment.LLIDemandNoteAdjustmentService;
import requestMapping.Service;
import util.CurrentTimeFactory;

public class LLINewConnectionDemandNoteBusinessLogic implements DemandNote {
	
	@Service
	AccountingIncidentService accountingIncidentService;
	@Service
	LLIBillCommonBusinessLogic lliBillCommonBusinessLogic;
	@Service
    LLIDemandNoteAdjustmentService lliDemandNoteAdjustmentService;
	
	private AccountingIncidentBuilder createBaseIncidentBuilder(LLINewConnectionDemandNote lliNewConnectionDemandNote){
		return new AccountingIncidentBuilder()
				.clientID(lliNewConnectionDemandNote.getClientID())
				.dateOfRecord(CurrentTimeFactory.getCurrentTime())
				.moduleID(ModuleConstants.Module_ID_LLI)
				.dateOfOccurance(CurrentTimeFactory.getCurrentTime());
				
	}
	
	private AccountingIncidentBuilder getAccountingIncidentBuilder(LLINewConnectionDemandNote lliNewConnectionDemandNote){
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(lliNewConnectionDemandNote)
				.debit(AccountType.DISCOUNT, lliNewConnectionDemandNote.getDiscount())
				.credit(AccountType.SECURITY, lliNewConnectionDemandNote.getSecurityMoney())
				.credit(AccountType.NEW_CONNECTION_CHARGE, lliNewConnectionDemandNote.getRegistrationFee())
				
				/*.credit(AccountType.ADJUSTABLE,
						lliNewConnectionDemandNote.getTransferCharge() +
						lliNewConnectionDemandNote.getBwMRC() +
						lliNewConnectionDemandNote.getLocalLoopCharge())*/
				
				.credit(AccountType.ADJUSTABLE, lliNewConnectionDemandNote.getAdvanceAdjustment())
				.credit(AccountType.BANDWIDTH_COST, lliNewConnectionDemandNote.getBwMRC())
				.credit(AccountType.LOCAL_LOOP_CHARGE, lliNewConnectionDemandNote.getLocalLoopCharge())
				.credit(AccountType.OTHER_CHARGES, lliNewConnectionDemandNote.getTotalItemCost())
				.credit(AccountType.FIBRE_OTC, lliNewConnectionDemandNote.getFibreOTC())
				.credit(AccountType.VAT_PAYABLE_TO_NBR, lliNewConnectionDemandNote.getVAT()); 
		
		return accountingIncidentBuilder;
	}
	
	@Override
	public void skip(BillDTO billDTO) throws Exception{
		
		LLINewConnectionDemandNote lliNewConnectionDemandNote = (LLINewConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliNewConnectionDemandNote);
		lliBillCommonBusinessLogic.skip(billDTO, accountingIncidentBuilder);

        lliDemandNoteAdjustmentService.setStatusByBillId(billDTO.getID(), DNAdjustStatus.ACTIVE);
	}

	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception{
		LLINewConnectionDemandNote lliNewConnectionDemandNote = (LLINewConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliNewConnectionDemandNote);
		lliBillCommonBusinessLogic.verifyPayment(billDTO, accountingIncidentBuilder);

        lliDemandNoteAdjustmentService.setStatusByBillId(billDTO.getID(), DNAdjustStatus.ACTIVE);
	}

	@Override
	public void cancelBill(BillDTO billDTO) throws Exception{
		if(billDTO.getPaymentStatus() != BillDTO.UNPAID){
			throw new RequestFailureException("Demand note with invoice ID "
						+billDTO.getID()+" can not be cancelled.");
		}
		lliBillCommonBusinessLogic.cancelBill(billDTO);
	}

	@Override
	public void unskip(BillDTO billDTO) throws Exception {
		LLINewConnectionDemandNote lliNewConnectionDemandNote = (LLINewConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliNewConnectionDemandNote);
		lliBillCommonBusinessLogic.unskip(billDTO, accountingIncidentBuilder);
	}
	
	@Override
	public boolean isSkipable(BillDTO billDTO) throws Exception {
		return lliBillCommonBusinessLogic.isSkipable(billDTO, ModuleConstants.Module_ID_LLI);
	}

	@Override
	public boolean isUnskipable(BillDTO billDTO) throws Exception {
		return lliBillCommonBusinessLogic.isUnskipable(billDTO, ModuleConstants.Module_ID_LLI);
	}
}
