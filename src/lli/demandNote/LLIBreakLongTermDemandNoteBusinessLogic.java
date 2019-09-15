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

public class LLIBreakLongTermDemandNoteBusinessLogic implements DemandNote {

	@Service
	AccountingIncidentService accountingIncidentService;
	@Service
	LLIBillCommonBusinessLogic lliBillCommonBusinessLogic;
	
	private AccountingIncidentBuilder createBaseIncidentBuilder(BillDTO billDTO){
		return new AccountingIncidentBuilder()
				.clientID(billDTO.getClientID())
				.dateOfRecord(CurrentTimeFactory.getCurrentTime())
				.moduleID(ModuleConstants.Module_ID_LLI)
				.dateOfOccurance(CurrentTimeFactory.getCurrentTime());
				
	}
	
	private AccountingIncidentBuilder getAccountingIncidentBuilder(LLIBreakLongTermDemandNote lliBreakLongTermDemandNote){
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(lliBreakLongTermDemandNote)
				.debit(AccountType.DISCOUNT, lliBreakLongTermDemandNote.getDiscount())
				.credit(AccountType.VAT_PAYABLE_TO_NBR, lliBreakLongTermDemandNote.getVAT())
				.credit(AccountType.OTHER_CHARGES, lliBreakLongTermDemandNote.getOtherCost())
				.credit(AccountType.LONG_TERM_BREAK_FINE, lliBreakLongTermDemandNote.getContractBreakingFine()); 
		
		return accountingIncidentBuilder;
	}
	
	@Override
	public void skip(BillDTO billDTO) throws Exception{
		
		LLIBreakLongTermDemandNote lliBreakLongTermDemandNote = (LLIBreakLongTermDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliBreakLongTermDemandNote);
		lliBillCommonBusinessLogic.skip(billDTO, accountingIncidentBuilder);
	}

	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception{
		LLIBreakLongTermDemandNote lliBreakLongTermDemandNote = (LLIBreakLongTermDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliBreakLongTermDemandNote);
		lliBillCommonBusinessLogic.verifyPayment(billDTO, accountingIncidentBuilder);
	}

	@Override
	public void cancelBill(BillDTO billDTO) throws Exception{
		if(billDTO.getPaymentStatus() != BillDTO.UNPAID){
			throw new RequestFailureException("Short Bill with invoice ID "
						+billDTO.getID()+" can not be cancelled.");
		}
		lliBillCommonBusinessLogic.cancelBill(billDTO);
	}

	@Override
	public void unskip(BillDTO billDTO) throws Exception {
		LLIBreakLongTermDemandNote lliBreakLongTermDemandNote = (LLIBreakLongTermDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliBreakLongTermDemandNote);
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
