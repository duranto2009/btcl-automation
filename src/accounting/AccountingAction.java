
package accounting;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.JsonObject;
import common.bill.BillDTO;
import common.bill.BillService;
import common.payment.PaymentDTO;
import lli.LLIDropdownPair;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.*;

@ActionRequestMapping("accounting/")
public class AccountingAction extends AnnotatedRequestMappingAction{

	@Service private AccountingIncidentService accountingIncidentService;
	@Service private AccountingEntryService accountingEntryService;
	@Service private BillService billService;
	
	@RequestMapping(mapping="incident/view", requestMethod=RequestMethod.GET)
	@ForwardedAction
	public String getAccountingIncidentByID(@RequestParameter("id") long ID, HttpServletRequest request) throws Exception {
		AccountingIncident accountingIncident = accountingIncidentService.getAccountingIncidentByIncidentID(ID);
		request.setAttribute("incident", accountingIncident);
		return "view-incident";
	}
	@RequestMapping(mapping="incident/search", requestMethod=RequestMethod.All) 
	@ForwardedAction
	public String searchAccountingIncidents(HttpServletRequest request) throws Exception {
		
		LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        RecordNavigationManager rnManager = new RecordNavigationManager(
        			SessionConstants.NAV_ACC_INCIDENT, request, accountingIncidentService,
        			SessionConstants.VIEW_ACC_INCIDENT,
        			SessionConstants.SEARCH_ACC_INCIDENT
        );
        
        rnManager.doJob(loginDTO);
        return "search-incident";
	}

	@RequestMapping(mapping="get-all-accounts", requestMethod=RequestMethod.GET)
	public List<LLIDropdownPair> getAllAccounts() throws Exception {
		List<LLIDropdownPair> accounts = new ArrayList<>();
		for(AccountType accountType: AccountType.values()) {
			accounts.add(new LLIDropdownPair(accountType.getID(), accountType.getName()));
		}
		return accounts;
	}
	@JsonPost
	@RequestMapping(mapping="ledger/search", requestMethod=RequestMethod.POST)
	public List<CumulativeAccountingEntry> searchAccountForLedgerPOST(
			@RequestParameter(isJsonBody=true, value="clientID")Long clientID,
			@RequestParameter(isJsonBody=true, value="accountID")Integer accountID,
			@RequestParameter(isJsonBody=true, value="fromDate")Long fromDate,
			@RequestParameter(isJsonBody=true, value="toDate")Long toDate,
			LoginDTO loginDTO) throws Exception {

		if(toDate != null){
			toDate = toDate + TimeConverter.MILLS_IN_A_DAY - 1;
		}
		return  accountingEntryService.getAccountingEntryByAccountIDAndDateRange(accountID, fromDate, toDate, clientID,loginDTO);
	}
	@RequestMapping(mapping="ledger/search", requestMethod=RequestMethod.GET)
	@ForwardedAction
	public String searchAccountForLedgerGET(@RequestParameter(value="accountID") Integer accountID , 
			@RequestParameter(value="clientID") Long clientID, 
			@RequestParameter(value="toDate") Long fromDate,
			@RequestParameter(value="fromDate") Long toDate, 
			HttpServletRequest request
			) throws Exception {
		
//		List<CumulativeAccountingEntry>accountingEntries = accountingEntryService
//				.getAccountingEntryByAccountIDAndDateRange(accountID, fromDate, toDate, clientID);
//		
//		request.setAttribute("cumulative_account_entry", accountingEntries);
        return "search-account-for-ledger";
        
	}

	@ForwardedAction
	@RequestMapping(mapping = "ledger/subscriber", requestMethod = RequestMethod.GET)
	public String getSubscriberLedgerPage() {
		return "subscriber-ledger";
	}

	@JsonPost
	@RequestMapping(mapping= "ledger/subscriber/report", requestMethod = RequestMethod.POST)
	public List<BillPaymentDTOForLedger> getSubscriberLedgerReport (@RequestParameter(isJsonBody=true, value="clientID")long clientID,
													   @RequestParameter(isJsonBody=true, value="fromDate")Long fromDate,
													   @RequestParameter(isJsonBody=true, value="toDate")Long toDate,
														@RequestParameter(isJsonBody = true, value = "moduleId") int moduleId
													) throws Exception {
		if(toDate != null){
			toDate = toDate + TimeConverter.MILLS_IN_A_DAY - 1;
		}

		return billService.getSubscriberLedgerReport(clientID, fromDate, toDate, moduleId);


	}

}
