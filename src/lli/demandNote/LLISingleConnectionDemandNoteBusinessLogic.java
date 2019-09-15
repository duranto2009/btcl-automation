package lli.demandNote;

import accounting.*;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import lli.bill.LLIBillCommonBusinessLogic;
import requestMapping.Service;
import util.CurrentTimeFactory;

public class LLISingleConnectionDemandNoteBusinessLogic implements DemandNote, GenerateBill {

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
	
	private AccountingIncidentBuilder getAccountingIncidentBuilder(LLISingleConnectionCommonDemandNote lliSingleConnectionCommonDemandNote){
		
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(lliSingleConnectionCommonDemandNote)
				.debit(AccountType.DISCOUNT, lliSingleConnectionCommonDemandNote.getDiscount())
				.credit(AccountType.SECURITY, lliSingleConnectionCommonDemandNote.getSecurityMoney())
				.credit(AccountType.ADJUSTABLE, lliSingleConnectionCommonDemandNote.getBandwidthCharge()
						+lliSingleConnectionCommonDemandNote.getAdvancedAmount()
						+lliSingleConnectionCommonDemandNote.getCoreCharge())
				.credit(AccountType.DOWNGRADE_CHARGE, lliSingleConnectionCommonDemandNote.getDowngradeCharge())
				.credit(AccountType.PORT_CHARGE, lliSingleConnectionCommonDemandNote.getPortCharge())
				.credit(AccountType.FIBRE_OTC, lliSingleConnectionCommonDemandNote.getFibreOTC())
				//.credit(AccountType.CORE_CHARGE, lliSingleConnectionCommonDemandNote.getCoreCharge())
				.credit(AccountType.IP_COST, lliSingleConnectionCommonDemandNote.getFirstXIpCost()+lliSingleConnectionCommonDemandNote.getNextYIpCost())
				.credit(AccountType.SHIFT_COST, lliSingleConnectionCommonDemandNote.getShiftCharge())
				.credit(AccountType.OTHER_CHARGES, lliSingleConnectionCommonDemandNote.getTotalItemCost())
				.credit(AccountType.VAT_PAYABLE_TO_NBR, lliSingleConnectionCommonDemandNote.getVAT());
	
		return accountingIncidentBuilder;
	}
	
	@Override
	public void skip(BillDTO billDTO) throws Exception{
		LLISingleConnectionCommonDemandNote lliSingleConnectionCommonDemandNote = (LLISingleConnectionCommonDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliSingleConnectionCommonDemandNote);
		lliBillCommonBusinessLogic.skip(billDTO, accountingIncidentBuilder);
	}

	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception{
		LLISingleConnectionCommonDemandNote lliSingleConnectionCommonDemandNote = (LLISingleConnectionCommonDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliSingleConnectionCommonDemandNote);
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
		LLISingleConnectionCommonDemandNote lliSingleConnectionCommonDemandNote = (LLISingleConnectionCommonDemandNote)billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliSingleConnectionCommonDemandNote);
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
	public void generate(BillDTO billDTO) throws Exception {
		LLISingleConnectionCommonDemandNote bill = (LLISingleConnectionCommonDemandNote) billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(bill);
		AccountingIncident accountingIncident = accountingIncidentBuilder
				.description("Yearly IP Address Bill is Generated for Client " + bill.getClientID() + " Year (" + bill.getYear() + ")")
				.debit(AccountType.ACCOUNT_RECEIVABLE_TD, bill.getNetPayable())
				.createAccountingIncident();
		accountingIncidentService.insertAccountingIncident(accountingIncident);
	}
}
