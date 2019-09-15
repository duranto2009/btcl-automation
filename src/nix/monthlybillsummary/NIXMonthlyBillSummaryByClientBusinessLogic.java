package nix.monthlybillsummary;

import accounting.*;
import common.ModuleConstants;
import common.Month;
import common.bill.BillConstants;
import common.bill.BillDTO;
import lli.bill.LLIBillCommonBusinessLogic;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClient;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByItem;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import util.CurrentTimeFactory;
import util.KeyValuePair;

public class NIXMonthlyBillSummaryByClientBusinessLogic implements LLIMonthlyBill{

	private static final Logger log = LoggerFactory.getLogger(LLIBillCommonBusinessLogic.class);

	@Service
	AccountingIncidentService accountingIncidentService;
	
	
	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception {
		NIXMonthlyBillSummaryByClient bill = (NIXMonthlyBillSummaryByClient) billDTO;
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

		if(billDTO.getBillType() == BillConstants.DEMAND_NOTE) {
			try{
				// todo :set payment clear for DN
//				lliApplicationService.setApplicationAsPaymentClearedByDemandNoteID(billDTO.getID());
			}catch(Exception ex){
//				reviseService.setApplicationAsPaymentClearedByDemandNoteID(billDTO.getID());
			}
		}
	}

	

	@Override
	public void cancelBill(BillDTO billDTO) throws Exception {
		
	}

	@Override
	public void generate(BillDTO billDTO) throws Exception {
		NIXMonthlyBillSummaryByClient bill = (NIXMonthlyBillSummaryByClient) billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuidler(bill);
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
	
	private AccountingIncidentBuilder getAccountingIncidentBuidler(NIXMonthlyBillSummaryByClient bill) {
		
		KeyValuePair<KeyValuePair<Double, Double>, KeyValuePair<Double, Double> > pairOfDiscountBWWithLoopCache = getPair(bill); 
		
		double totalAdjustableDeduct = bill.getAdjustmentAmount();
		double totalLoopCharge = pairOfDiscountBWWithLoopCache.key.value;
		double totalPortCharge = pairOfDiscountBWWithLoopCache.value.key;
//		double totalDiscount = pairOfDiscountBWWithLoopCache.key.key;
		double totalDiscount = 0;

		
		
		//TODO what to do if negetive value
		
		/*if(totalBW < 0)
			totalAdjustableDeduct -= totalBW;
		
		if(totalLoopCharge < 0)
			totalAdjustableDeduct -= totalLoopCharge;*/
		
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
		

//		if(totalBW < 0)
//			accountingIncidentBuilder.debit(AccountType.BANDWIDTH_COST, Math.abs(totalBW));
//		else
//			accountingIncidentBuilder.credit(AccountType.BANDWIDTH_COST, totalBW);
		
		if(totalLoopCharge < 0)
			accountingIncidentBuilder.debit(AccountType.NIX_LOCAL_LOOP, Math.abs(totalLoopCharge));
		else
			accountingIncidentBuilder.credit(AccountType.NIX_LOCAL_LOOP, totalLoopCharge);

		if(totalPortCharge < 0)
			accountingIncidentBuilder.debit(AccountType.NIX_PORT, Math.abs(totalPortCharge));
		else
			accountingIncidentBuilder.credit(AccountType.NIX_PORT, totalPortCharge);
		

		/*
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(bill)
				.debit(AccountType.ADJUSTABLE, bill.getAdjustmentAmount())
				.debit(AccountType.DISCOUNT, pairOfDiscountBWWithLoopCache.key.key)
				.credit(AccountType.BANDWIDTH_COST, pairOfDiscountBWWithLoopCache.key.value)
				.credit(AccountType.LOCAL_LOOP_CHARGE, pairOfDiscountBWWithLoopCache.value.key)
				.credit(AccountType.CACHE_COST, pairOfDiscountBWWithLoopCache.value.value)
				.credit(AccountType.VAT_PAYABLE_TO_NBR, bill.getVAT()) ;
		*/
		
		return accountingIncidentBuilder;
	}

	private AccountingIncidentBuilder createBaseIncidentBuilder(NIXMonthlyBillSummaryByClient bill) {
		return new AccountingIncidentBuilder()
				.clientID(bill.getClientID())
				.dateOfOccurance(CurrentTimeFactory.getCurrentTime())
				.dateOfRecord(CurrentTimeFactory.getCurrentTime())
				.moduleID(ModuleConstants.Module_ID_NIX);
	}

	private KeyValuePair<KeyValuePair<Double, Double>, KeyValuePair<Double, Double>> getPair(NIXMonthlyBillSummaryByClient bill) {
		double discount = 0;
		double loopCost = 0;
		double portCost = 0;
		
		discount = bill.getDiscount();

		for(NIXMonthlyBillSummaryByItem item : bill.getNixMonthlyBillSummaryByItems() ) {
			
			switch(item.getType()) {
//				case NIXMonthlyBillSummaryType.REGULAR:
//				case NIXMonthlyBillSummaryType.REGULAR_ADJUSTMENT:
//					bwCost += item.getGrandCost();
//					break;
//
					
				case NIXMonthlyBillSummaryType.PORT:
				case NIXMonthlyBillSummaryType.PORT_ADJUSTMENT:
					portCost += item.getGrandCost();
					break;
					
					
				case NIXMonthlyBillSummaryType.LOCAL_LOOP:
				case NIXMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT:
					loopCost += item.getGrandCost();
					break;
					
					
				default: break;
			}
		}
		return new KeyValuePair<>(
//				new KeyValuePair<Double, Double>(discount, bwCost),
				new KeyValuePair<Double, Double>(loopCost, loopCost),
				new KeyValuePair<Double, Double>(portCost, portCost)
			);
	}
}
