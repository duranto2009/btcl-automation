package lli.monthlyBillSummary;

import accounting.AccountType;
import accounting.AccountingIncidentBuilder;
import accounting.AccountingIncidentService;
import accounting.LLIMonthlyBill;
import common.ModuleConstants;
import common.bill.BillDTO;
import lli.bill.LLIBillCommonBusinessLogic;
import requestMapping.Service;
import util.CurrentTimeFactory;
import util.KeyValuePair;

public class LLIMonthlyBillSummaryByClientBusinessLogic  implements LLIMonthlyBill{

	@Service
	AccountingIncidentService accountingIncidentService;
	@Service
	LLIBillCommonBusinessLogic lliBillCommonBusinessLogic;
	
	
	@Override
	public void verifyPayment(BillDTO billDTO) throws Exception {
		LLIMonthlyBillSummaryByClient bill = (LLIMonthlyBillSummaryByClient) billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(bill)
				.credit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable());
		lliBillCommonBusinessLogic.verifyPayment(billDTO, accountingIncidentBuilder);
	}

	

	@Override
	public void cancelBill(BillDTO billDTO) throws Exception {
		
	}

	@Override
	public void generate(BillDTO billDTO) throws Exception {
		LLIMonthlyBillSummaryByClient bill = (LLIMonthlyBillSummaryByClient) billDTO;
		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuidler(bill);
		lliBillCommonBusinessLogic.generateBill(billDTO, accountingIncidentBuilder);
	}
	
	private AccountingIncidentBuilder getAccountingIncidentBuidler(LLIMonthlyBillSummaryByClient bill) {
		
		KeyValuePair<KeyValuePair<Double, Double>, KeyValuePair<Double, Double> > pairOfDiscountBWWithLoopCache = getPair(bill); 
		
		double totalAdjustableDeduct = bill.getAdjustmentAmount();
		double totalBW = pairOfDiscountBWWithLoopCache.key.value + pairOfDiscountBWWithLoopCache.value.value;
		double totalLoopCharge = pairOfDiscountBWWithLoopCache.value.key;
		double totalDiscount = pairOfDiscountBWWithLoopCache.key.key;
		
		
		
		//TODO what to do if negetive value
		
		/*if(totalBW < 0)
			totalAdjustableDeduct -= totalBW;
		
		if(totalLoopCharge < 0)
			totalAdjustableDeduct -= totalLoopCharge;*/
		
		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(bill)
				.credit(AccountType.VAT_PAYABLE_TO_NBR, bill.getVAT());
		
		if(totalAdjustableDeduct < 0)
			accountingIncidentBuilder.credit(AccountType.ADJUSTABLE, Math.abs(totalAdjustableDeduct));
		else
			accountingIncidentBuilder.debit(AccountType.ADJUSTABLE, totalAdjustableDeduct);
		
		if(totalDiscount < 0)
			accountingIncidentBuilder.credit(AccountType.DISCOUNT, Math.abs(totalDiscount));
		else
			accountingIncidentBuilder.debit(AccountType.DISCOUNT, totalDiscount);
		

		if(totalBW < 0)
			accountingIncidentBuilder.debit(AccountType.BANDWIDTH_COST, Math.abs(totalBW));
		else
			accountingIncidentBuilder.credit(AccountType.BANDWIDTH_COST, totalBW);
		
		if(totalLoopCharge < 0)
			accountingIncidentBuilder.debit(AccountType.LOCAL_LOOP_CHARGE, Math.abs(totalLoopCharge));
		else
			accountingIncidentBuilder.credit(AccountType.LOCAL_LOOP_CHARGE, totalLoopCharge);
		
		
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

	private AccountingIncidentBuilder createBaseIncidentBuilder(LLIMonthlyBillSummaryByClient bill) {
		return new AccountingIncidentBuilder()
				.clientID(bill.getClientID())
				.dateOfOccurance(CurrentTimeFactory.getCurrentTime())
				.dateOfRecord(CurrentTimeFactory.getCurrentTime())
				.moduleID(ModuleConstants.Module_ID_LLI);
	}

	private KeyValuePair<KeyValuePair<Double, Double>, KeyValuePair<Double, Double>> getPair(LLIMonthlyBillSummaryByClient bill) {
		double discount = 0;
		double bwCost = 0;
		double loopCost = 0;
		double cacheCost = 0;
		
		discount = bill.getDiscount();

		for(LLIMonthlyBillSummaryByItem item : bill.getLliMonthlyBillSummaryByItems() ) {
			
			switch(item.getType()) {
				case LLIMonthlyBillSummaryType.REGULAR:
				case LLIMonthlyBillSummaryType.REGULAR_ADJUSTMENT:
					bwCost += item.getGrandCost();
					break;
					
					
				case LLIMonthlyBillSummaryType.CACHE:
				case LLIMonthlyBillSummaryType.CACHE_ADJUSTMENT:
					cacheCost += item.getGrandCost();
					break;
					
					
				case LLIMonthlyBillSummaryType.LOCAL_LOOP:
				case LLIMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT:
					loopCost += item.getGrandCost();
					break;
					
					
				default: break;
			}
		}
		return new KeyValuePair<>(
				new KeyValuePair<Double, Double>(discount, bwCost), 
				new KeyValuePair<Double, Double>(loopCost, cacheCost) 
			);
	}
}
