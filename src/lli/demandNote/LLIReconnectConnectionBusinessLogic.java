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

public class LLIReconnectConnectionBusinessLogic implements DemandNote {
	@Service
	LLIBillCommonBusinessLogic lliBillCommonBusinessLogic;
	
	@Service
	AccountingIncidentService accountingIncidentService;
	
	private AccountingIncidentBuilder createBaseIncidentBuilder(
			LLIReconnectConnectionDemandNote lliReconnectConnectionDemandNote) {
		return new AccountingIncidentBuilder()
				.clientID(lliReconnectConnectionDemandNote.getClientID())
				.dateOfRecord(CurrentTimeFactory.getCurrentTime())
				.moduleID(ModuleConstants.Module_ID_LLI)
				.dateOfOccurance(CurrentTimeFactory.getCurrentTime());
	}
	
	private AccountingIncidentBuilder getAccountingIncidentBuilder(
			LLIReconnectConnectionDemandNote lliReconnectConnectionDemandNote) {
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(lliReconnectConnectionDemandNote)
				.debit(AccountType.DISCOUNT, lliReconnectConnectionDemandNote.getDiscount())
				.credit(AccountType.RECONNECT, lliReconnectConnectionDemandNote.getReconnectionCharge())
				.credit(AccountType.OTHER_CHARGES, lliReconnectConnectionDemandNote.getOtherCost())
				.credit(AccountType.VAT_PAYABLE_TO_NBR, lliReconnectConnectionDemandNote.getVAT());
	
		return accountingIncidentBuilder;
	}



	@Override
	public void skip(BillDTO billDTO) throws Exception {
		LLIReconnectConnectionDemandNote lliReconnectConnectionDemandNote = (LLIReconnectConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliReconnectConnectionDemandNote);
		lliBillCommonBusinessLogic.skip(billDTO, accountingIncidentBuilder);
	}

	
	@Override
	public void unskip(BillDTO billDTO) throws Exception {
		LLIReconnectConnectionDemandNote lliReconnectConnectionDemandNote = (LLIReconnectConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliReconnectConnectionDemandNote);
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

	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception {
		LLIReconnectConnectionDemandNote lliReconnectConnectionDemandNote = (LLIReconnectConnectionDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliReconnectConnectionDemandNote);
		lliBillCommonBusinessLogic.verifyPayment(billDTO, accountingIncidentBuilder);
	}

	@Override
	public void cancelBill(BillDTO billDTO) throws Exception {
		if(billDTO.getPaymentStatus() != BillDTO.UNPAID){
			throw new RequestFailureException("Demand note with invoice ID "
						+billDTO.getID()+" can not be cancelled.");
		}
		lliBillCommonBusinessLogic.cancelBill(billDTO);
	}

}
