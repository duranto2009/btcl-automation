package lli.demandNote;

import accounting.AccountType;
import accounting.AccountingIncidentBuilder;
import accounting.AccountingIncidentService;
import accounting.DemandNote;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import lli.bill.LLIBillCommonBusinessLogic;
import requestMapping.Service;
import util.CurrentTimeFactory;

public class LLICloseConnectionDemandNoteBusinessLogic implements DemandNote {

	@Service
	AccountingIncidentService accountingIncidentService;
	@Service
	LLIBillCommonBusinessLogic lliBillCommonBusinessLogic;
	
	private AccountingIncidentBuilder createBaseIncidentBuilder(BillDTO parentDemandNote){
		return new AccountingIncidentBuilder()
				.clientID(parentDemandNote.getClientID())
				.dateOfRecord(CurrentTimeFactory.getCurrentTime())
				.moduleID(ModuleConstants.Module_ID_LLI)
				.dateOfOccurance(CurrentTimeFactory.getCurrentTime());
				
	}
	
	private AccountingIncidentBuilder getAccountingIncidentBuilder(LLICloseConnectionDemandNote lliCloseConnectionDemandNote){
		
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(lliCloseConnectionDemandNote)
				.debit(AccountType.DISCOUNT, lliCloseConnectionDemandNote.getDiscount())
				.credit(AccountType.VAT_PAYABLE_TO_NBR, lliCloseConnectionDemandNote.getVAT())
				.credit(AccountType.CLOSING_OTC, lliCloseConnectionDemandNote.getClosingOTC())
				.credit(AccountType.OTHER_CHARGES, lliCloseConnectionDemandNote.getOtherCost());
				
							
		return accountingIncidentBuilder;
	}
	
	@Override
	public void skip(BillDTO billDTO) throws Exception{
		LLICloseConnectionDemandNote lliCloseConnectionDemandNote = (LLICloseConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliCloseConnectionDemandNote);
		lliBillCommonBusinessLogic.skip(billDTO, accountingIncidentBuilder);
	}

	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception{
		LLICloseConnectionDemandNote lliCloseConnectionDemandNote = (LLICloseConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliCloseConnectionDemandNote);
		lliBillCommonBusinessLogic.verifyPayment(billDTO, accountingIncidentBuilder);
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
		LLICloseConnectionDemandNote lliCloseConnectionDemandNote = (LLICloseConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliCloseConnectionDemandNote);
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
