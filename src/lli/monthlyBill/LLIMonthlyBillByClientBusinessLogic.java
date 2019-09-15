//package lli.monthlyBill;
//
//import accounting.AccountType;
//import accounting.AccountingIncidentBuilder;
//import accounting.AccountingIncidentService;
//import accounting.LLIMonthlyBill;
//import common.ModuleConstants;
//import common.bill.BillDTO;
//import lli.bill.LLIBillCommonBusinessLogic;
//import lli.connection.LLIConnectionConstants;
//import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClient;
//import requestMapping.Service;
//import util.CurrentTimeFactory;
//
//public class LLIMonthlyBillByClientBusinessLogic implements LLIMonthlyBill{
//
//	@Service
//	AccountingIncidentService accountingIncidentService;
//	@Service
//	LLIBillCommonBusinessLogic lliBillCommonBusinessLogic;
//	
//	@Override
//	public void generateMonthlyBillSummary(LLIMonthlyBillSummaryByClient lliMonthlyBillSummaryByClient)
//			throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//	
//	@Override
//	public void generateMonthlyBill(BillDTO billDTO) throws Exception {
//		LLIMonthlyBillByClient lliMonthlyBillByClient = (LLIMonthlyBillByClient)billDTO;
//		AccountingIncidentBuilder accountingIncidentBuilder = getAccountingIncidentBuilder(lliMonthlyBillByClient);
//		lliBillCommonBusinessLogic.generateMonthlyBill(billDTO, accountingIncidentBuilder);
//	}
//	
//	@Override
//	public void verifyPayment(BillDTO billDTO) throws Exception {
//		LLIMonthlyBillByClient lliMonthlyBillByClient = (LLIMonthlyBillByClient)billDTO;
//		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(lliMonthlyBillByClient);
//		lliBillCommonBusinessLogic.verifyPayment(billDTO, accountingIncidentBuilder);
//	}
//
//	private AccountingIncidentBuilder getAccountingIncidentBuilder(LLIMonthlyBillByClient lliMonthlyBillByClient) {
//		double loopCost = 0;
//		double mbpsCost = 0;
//		double cacheCost = 0;
//		
//		for(LLIMonthlyBillByConnection lliConnection : lliMonthlyBillByClient.getMonthlyBillByConnections()) {
//			
//			loopCost += lliConnection.getLoopCost();
//			
//			if(lliConnection.getType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR) {
//				mbpsCost += lliConnection.getMbpsCost();
//			}else if(lliConnection.getType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE) {
//				cacheCost += lliConnection.getMbpsCost();
//			}
//		}
//		
//		AccountingIncidentBuilder accountingIncidentBuilder = createBaseIncidentBuilder(lliMonthlyBillByClient)
//				.debit(AccountType.ADJUSTABLE, lliMonthlyBillByClient.getAdjustmentAmount())
//				.debit(AccountType.DISCOUNT, lliMonthlyBillByClient.getDiscount())
//				.credit(AccountType.BANDWIDTH_COST,mbpsCost-lliMonthlyBillByClient.getLongTermContructAdjustment())
//				.credit(AccountType.LOCAL_LOOP_CHARGE, loopCost)
//				.credit(AccountType.CACHE_COST, cacheCost)
//				.credit(AccountType.VAT_PAYABLE_TO_NBR, lliMonthlyBillByClient.getVAT()) ;
//		
//		return accountingIncidentBuilder;
//	}
//
//	private AccountingIncidentBuilder createBaseIncidentBuilder(LLIMonthlyBillByClient lliMonthlyBillByClient) {
//		return new AccountingIncidentBuilder()
//				.clientID(lliMonthlyBillByClient.getClientID())
//				.dateOfRecord(CurrentTimeFactory.getCurrentTime())
//				.moduleID(ModuleConstants.Module_ID_LLI)
//				.dateOfOccurance(CurrentTimeFactory.getCurrentTime());
//	}
//
//	@Override
//	public void cancelBill(BillDTO billDTO) throws Exception {
//		
//		
//	}
//
//	
//}
