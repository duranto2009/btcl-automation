package lli.monthlyBill;

import accounting.AccountType;
import accounting.AccountingIncidentBuilder;
import accounting.AccountingIncidentService;
import accounting.LLIMonthlyBill;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import lli.bill.LLIBillCommonBusinessLogic;
import requestMapping.Service;
import util.CurrentTimeFactory;

public class LLIManualBillBusinessLogic implements LLIMonthlyBill{
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
	
	private AccountingIncidentBuilder getAccountingIncidentBuilder(LLIManualBill manualBill){
		
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(manualBill)
				.debit(AccountType.DISCOUNT, manualBill.getDiscount())
				.credit(AccountType.OTHER_CHARGES, manualBill.getTotalCost()) // severe problem; all the money is going in a single account!
				.credit(AccountType.VAT_PAYABLE_TO_NBR, manualBill.getVAT());
	
		return accountingIncidentBuilder;
	}
	@Override
	public void generate(BillDTO billDTO) throws Exception {
		LLIManualBill lliManualBill = (LLIManualBill)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliManualBill);
		lliBillCommonBusinessLogic.generateBill(billDTO, accountingIncidentBuilder);
	}

	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception {
		LLIManualBill lliManualBill = (LLIManualBill)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliManualBill);
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
