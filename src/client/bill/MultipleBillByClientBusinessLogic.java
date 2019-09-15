package client.bill;

import accounting.AccountType;
import accounting.AccountingIncident;
import accounting.AccountingIncidentBuilder;
import accounting.AccountingIncidentService;
import common.Month;
import common.bill.BillDTO;
import common.bill.MultipleBillMappingDTO;
import lli.bill.LLIBillCommonBusinessLogic;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import util.CurrentTimeFactory;

import java.util.ArrayList;
import java.util.List;

public class MultipleBillByClientBusinessLogic implements MultipleBill{

	private static final Logger log = LoggerFactory.getLogger(LLIBillCommonBusinessLogic.class);

	@Service
	AccountingIncidentService accountingIncidentService;
	
	
	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception {
		BillDTO bill =  billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(bill)
				.credit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable());
		verifyPayment(billDTO, accountingIncidentBuilder);
	}



	public void verifyPayment(BillDTO billDTO,AccountingIncidentBuilder accountingIncidentBuilder) throws Exception{
		AccountingIncident accountingIncident =null;

		String description = "invoice id "+billDTO.getID()+" payment verified. " ;

		if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED_FROM_SKIPPED){
			//skipped flow
			accountingIncident = accountingIncidentBuilder
					.clearAccountingEntryList()
					.description(description)
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

		try{
			accountingIncidentService.insertAccountingIncident(accountingIncident);
		}catch(Exception ex){
			log.error(ex.toString(), ex);
		}


	}

	

	@Override
	public void cancelBill(BillDTO billDTO) throws Exception {
		
	}

	@Override
	public void generate(BillDTO billDTO) throws Exception {
		BillDTO bill =  billDTO;
		int module=bill.getEntityTypeID()%100;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuidler(bill,module);
		generateBill(billDTO, accountingIncidentBuilder);
	}

	public void generateBill(BillDTO billDTO, AccountingIncidentBuilder accountingIncidentBuilder) throws Exception {
		String description = "";
		if(billDTO instanceof NIXMonthlyBillSummaryByClient) {
			description = "Monthly Bill Generated For Client " + billDTO.getClientID()
					+ " For " + Month.getMonthNameById(billDTO.getMonth())
					+ "," + billDTO.getYear();
		}
//		else if (billDTO instanceof LLIManualBill) {
//			description = "Manual Bill Generated For Client " + billDTO.getClientID();
//		}
		AccountingIncident accountingIncident = accountingIncidentBuilder.description(description)
				.debit(AccountType.NIX_ACCOUNT_RECEIVABLE_TD,billDTO.getNetPayable() )
				.createAccountingIncident();
		accountingIncidentService.insertAccountingIncident(accountingIncident);
	}
	
	private AccountingIncidentBuilder getAccountingIncidentBuidler(BillDTO bill,int module) {
		

		double totalAdjustableDeduct = bill.getAdjustmentAmount();
		double totalDiscount = 0;
		
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(bill)
				.credit(AccountType.NIX_VAT_PAYABLE_TO_NBR, bill.getVAT());
		
		if(totalAdjustableDeduct < 0)
			accountingIncidentBuilder.credit(AccountType.NIX_ADJUSTABLE, Math.abs(totalAdjustableDeduct));
		else
			accountingIncidentBuilder.debit(AccountType.NIX_ADJUSTABLE, totalAdjustableDeduct);
		
		if(totalDiscount < 0)
			accountingIncidentBuilder.credit(AccountType.NIX_DISCOUNT, Math.abs(totalDiscount));
		else
			accountingIncidentBuilder.debit(AccountType.NIX_DISCOUNT, totalDiscount);
		return accountingIncidentBuilder;
	}

	private AccountingIncidentBuilder createBaseIncidentBuilder(BillDTO bill) {
		int module=bill.getEntityTypeID()%100;
		return new AccountingIncidentBuilder()
				.clientID(bill.getClientID())
				.dateOfOccurance(CurrentTimeFactory.getCurrentTime())
				.dateOfRecord(CurrentTimeFactory.getCurrentTime())
				.moduleID(module);
	}

}
