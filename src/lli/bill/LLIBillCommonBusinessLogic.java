package lli.bill;

import accounting.AccountType;
import accounting.AccountingIncident;
import accounting.AccountingIncidentBuilder;
import accounting.AccountingIncidentService;
import client.RegistrantTypeConstants;
import common.Month;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.bill.BillDTO;
import common.repository.AllClientRepository;
import lli.Application.LLIApplication;
import lli.Application.LLIApplicationService;
import lli.Application.ReviseClient.ReviseService;
import lli.Application.ownership.LLIOwnerChangeService;
import lli.connection.LLIConnectionConstants;
import lli.monthlyBill.LLIManualBill;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryByClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.Service;
import util.ServiceDAOFactory;
import vpn.client.ClientDetailsDTO;

public class LLIBillCommonBusinessLogic {

	private static final Logger log = LoggerFactory.getLogger(LLIBillCommonBusinessLogic.class);

	@Service
	AccountingIncidentService accountingIncidentService;
	
	@Service
	LLIApplicationService lliApplicationService;

	@Service
	ReviseService reviseService;
	
	
	public void skip(BillDTO billDTO,AccountingIncidentBuilder accountingIncidentBuilder) throws Exception{
		String description = "invoice id "+billDTO.getID()+" skipped. ";
		AccountingIncident accountingIncident = accountingIncidentBuilder
				.description(description)
				.debit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
				.createAccountingIncident();
		accountingIncidentService.insertAccountingIncident(accountingIncident);
		lliApplicationService.setApplicationAsPaymentClearedByDemandNoteID(billDTO.getID());
	}
	public void verifyPayment(BillDTO billDTO,AccountingIncidentBuilder accountingIncidentBuilder) throws Exception{
		AccountingIncident accountingIncident =null;
		String description = "Demand Note of Invoice ID "+billDTO.getID()+" payment verified." ;
		if(billDTO.getBillType() == BillConstants.YEARLY_BILL){
			description = "Yearly Bill of Invoice ID " + billDTO.getID() + " payment verified.";
		}
		
		if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED_FROM_SKIPPED){
			//skipped flow
			accountingIncident = accountingIncidentBuilder
					.clearAccountingEntryList()
					.description(description)
					.debit(AccountType.CASH, billDTO.getNetPayable())
					.credit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
					.createAccountingIncident();
		}else if(billDTO.getPaymentStatus() == BillDTO.PAID_UNVERIFIED){
			// first paid
			//normal flow
			accountingIncident = accountingIncidentBuilder
					.description(description)
					.debit(AccountType.CASH, billDTO.getNetPayable())
					.createAccountingIncident();
		}

		try{
			accountingIncidentService.insertAccountingIncident(accountingIncident);
		}catch(Exception ex){
			log.error(ex.toString(), ex);
		}

		if(billDTO.getBillType() == BillConstants.DEMAND_NOTE) {
			try{
				lliApplicationService.setApplicationAsPaymentClearedByDemandNoteID(billDTO.getID());
			}catch(Exception ex){
				try{
					reviseService.setApplicationAsPaymentClearedByDemandNoteID(billDTO.getID());
				}catch (Exception e){
					ServiceDAOFactory.getService(LLIOwnerChangeService.class).setApplicationAsPaymentClearedByDemandNoteID(billDTO.getID());
				}
			}
		}
	}
	public void unskip(BillDTO billDTO , AccountingIncidentBuilder accountingIncidentBuilder) throws Exception{

		LLIApplication lliApplication = lliApplicationService.getNewFlowLLIApplicationByDemandNoteID(billDTO.getID());
		
		if(lliApplication != null && lliApplication.getStatus()==LLIConnectionConstants.STATUS_COMPLETED) {
			throw new RequestFailureException("This demand note with invoice ID "+billDTO.getID()
			+" can not be unskipped. The application is already completed.");
		}
		
		
		String description = "Bill id "+billDTO.getID()+" unskipped";
		AccountingIncident accountingIncident = accountingIncidentBuilder
				.description(description)
				.debit(AccountType.ACCOUNT_RECEIVABLE_TD, billDTO.getNetPayable())
				.reverse()
				.createAccountingIncident();
		
		accountingIncidentService.insertAccountingIncident(accountingIncident);


		
		lliApplicationService.setApplicationAsDemandNoteGeneratedByDemandNoteID(billDTO.getID());

	}
	
	public boolean isSkipable(BillDTO billDTO, int moduleId) throws Exception{
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(billDTO.getClientID(), moduleId);
		if(clientDetailsDTO==null) {
			throw new RequestFailureException ("No client Details found for the corresponding invoice");
		}
		return billDTO.getPaymentStatus() == BillDTO.UNPAID && clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.GOVT;
	}
	public boolean isUnskipable(BillDTO billDTO, int moduleId) throws Exception{
		ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(billDTO.getClientID(), moduleId);
		if(clientDetailsDTO==null) {
			throw new RequestFailureException ("No client Details found for the corresponding invoice");
		}
		//condition is wrong here need more test case  should be || ; raihan //TODO raihan
		if(billDTO.getPaymentStatus() != BillDTO.SKIPPED && clientDetailsDTO.getRegistrantType() != RegistrantTypeConstants.GOVT){
			return false;
		}
		
		LLIApplication lliApplication = lliApplicationService.getNewFlowLLIApplicationByDemandNoteID(billDTO.getID());
		
		if(lliApplication!=null && lliApplication.isServiceStarted()){
			return false;
		}
		
		return true;
	}
	
	
	public void cancelBill(BillDTO billDTO) throws Exception{
		LLIApplication lliApplication = lliApplicationService.getNewFlowLLIApplicationByDemandNoteID(billDTO.getID());
		if(lliApplication!=null ){
			lliApplication.setDemandNoteID(null);
			lliApplication.setStatus(LLIConnectionConstants.STATUS_FINALIZED);
			lliApplicationService.updateApplicaton(lliApplication);
		}
	}
	
	public void generateBill(BillDTO billDTO, AccountingIncidentBuilder accountingIncidentBuilder) throws Exception {
		String description = "";
		if(billDTO instanceof LLIMonthlyBillSummaryByClient) {
			description = "Monthly Bill Generated For Client " + billDTO.getClientID() 
						+ " For " + Month.getMonthNameById(billDTO.getMonth())
						+ "," + billDTO.getYear();
		}else if (billDTO instanceof LLIManualBill) {
			description = "Manual Bill Generated For Client " + billDTO.getClientID();
		}
		AccountingIncident accountingIncident = accountingIncidentBuilder.description(description)
				.debit(AccountType.ACCOUNT_RECEIVABLE_TD,billDTO.getNetPayable() )
				.createAccountingIncident();
		accountingIncidentService.insertAccountingIncident(accountingIncident);
	}
}
