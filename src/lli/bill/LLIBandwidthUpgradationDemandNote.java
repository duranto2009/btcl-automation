package lli.bill;

import accounting.AccountType;
import accounting.AccountingIncident;
import accounting.AccountingIncidentBuilder;
import accounting.AccountingIncidentService;
import common.ModuleConstants;
import common.bill.BillDTO;
import connection.DatabaseConnection;
import util.CurrentTimeFactory;
import util.ServiceDAOFactory;

public class LLIBandwidthUpgradationDemandNote extends BillDTO{

	double securityMoney;
	double advancePayment;
	double discount;
	double upgradationCharge;
	double fibreOTC;
	boolean isSkipped;
	
	
	
	private AccountingIncidentBuilder createAccountingIncidentForDemandNoteSkipping(){
		
		String description = "";
		
		AccountingIncidentBuilder accountingIncidentBuilder = new AccountingIncidentBuilder()
															.description(description)
															.moduleID(ModuleConstants.Module_ID_LLI)
															.clientID(getClientID())
															.dateOfRecord(CurrentTimeFactory.getCurrentTime())
															.dateOfOccurance(CurrentTimeFactory.getCurrentTime())
															
															.debit(AccountType.ACCOUNT_RECEIVABLE_TD, getTotalPayable())
															.debit(AccountType.DISCOUNT, discount)
															
															.credit(AccountType.SECURITY, securityMoney)
															.credit(AccountType.ADJUSTABLE, advancePayment)
															.credit(AccountType.UPGRADATION_CHARGE, upgradationCharge)
															.credit(AccountType.FIBRE_OTC, fibreOTC)
															.credit(AccountType.VAT_PAYABLE_TO_NBR, getVAT());
		
		return accountingIncidentBuilder;
	}
	
	
	public void skipDemandNote() throws Exception{
		
		// check the demand note is valid and not skipped and not paid
		
		
		AccountingIncident accountingIncident = createAccountingIncidentForDemandNoteSkipping()
				.createAccountingIncident();
		
		AccountingIncidentService accountingIncidentService = ServiceDAOFactory.getService(AccountingIncidentService.class);
		accountingIncidentService.insertAccountingIncident(accountingIncident);
	}
	
	@Override
	public void payBill(DatabaseConnection databaseConnection, Object...objects ) throws Exception{
		// check if the demand note is cancelled or paid or skipped
		
		if(isSkipped){
			
			String description = "";
			
			AccountingIncident accountingIncident = new AccountingIncidentBuilder(description, ModuleConstants.Module_ID_LLI,
					getClientID(), CurrentTimeFactory.getCurrentTime(), CurrentTimeFactory.getCurrentTime())
					.debit(AccountType.UNVERIFIED_CASH, getTotalPayable())
					.credit(AccountType.ACCOUNT_RECEIVABLE_TD, getTotalPayable())
					.createAccountingIncident();
			
			
		}else{

			AccountingIncident accountingIncident = createAccountingIncidentForDemandNoteSkipping()
					.replaceAccountType(AccountType.ACCOUNT_RECEIVABLE_TD, AccountType.CASH)
					.createAccountingIncident();
			ServiceDAOFactory.getService(AccountingIncidentService.class).insertAccountingIncident(accountingIncident);
		}
		
		
	}
	@Override
	public void cancelBill() throws Exception{
		// if the deman note is valid
		if(getPaymentStatus() == BillDTO.PAID_VERIFIED){
			
		}
		
		if(getPaymentStatus() == BillDTO.PAID_UNVERIFIED){
			
		}
	}
	@Override
	public void verifyPayment() throws Exception{
		
	}

	
}
