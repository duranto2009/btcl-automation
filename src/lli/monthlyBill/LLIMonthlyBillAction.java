package lli.monthlyBill;


import annotation.ForwardedAction;
import annotation.JsonPost;
import lli.monthlyBillSummary.LLIMonthlyBillSummaryService;
import login.LoginDTO;
import org.apache.log4j.Logger;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ActionRequestMapping("lli/monthly-bill")
public class LLIMonthlyBillAction  extends AnnotatedRequestMappingAction{
	static Logger logger = Logger.getLogger(LLIMonthlyBillAction.class);

	@Service
	LLIMonthlyBillService  lliMonthlyBillService;
	@Service
	LLIMonthlyBillSummaryService  lliMonthlyBillSummaryService;



	//monthly bill search view
	@RequestMapping(mapping="/search-multiple", requestMethod=RequestMethod.All) // there may be more parameter (like month of the year etc)
	public List<LLIMonthlyBillByClient> getMultipleMonthlyBillView(
			HttpServletRequest request,
			@RequestParameter("id")long clientId,
			@RequestParameter("from")long fromDate,
			@RequestParameter("to")long toDate
	)throws Exception {

		login.LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		if(clientId==-1) {
			logger.debug("clientId is before : "+clientId);
			clientId= loginDTO.getAccountID();
			logger.debug("clientId is after: "+clientId);
		}
		Date from = new Date(fromDate);
		Date to = new Date(toDate);
		Calendar fromCal = Calendar.getInstance();
		Calendar toCal = Calendar.getInstance();
		fromCal.setTime(from);
		toCal.setTime(to);

		int fromMonth = fromCal.get(Calendar.MONTH);
		int fromYear = fromCal.get(Calendar.YEAR);

		int toMonth = toCal.get(Calendar.MONTH);
		int toYear = toCal.get(Calendar.YEAR);
		LocalDate fromDt=LocalDate.of(fromYear,fromMonth,1);
		LocalDate toDt=LocalDate.of(toYear,toMonth,1);
		long monthBetween= ChronoUnit.MONTHS.between(fromDt,toDt);
		int month=fromMonth;
		int year=fromYear;
		List<LLIMonthlyBillByClient> lliMonthlyBillByClients=new ArrayList<>();

		for(int i=0;i<monthBetween;i++){
			 LLIMonthlyBillByClient lliMonthlyBillByClient=lliMonthlyBillService.getMultipleLLIMonthlyBillByClient(clientId, month, year);
			 if(lliMonthlyBillByClient!=null){

				 lliMonthlyBillByClients.add(lliMonthlyBillByClient);
			 }
			 month++;
			 if(month>12){
			 	month=1;
			 	year=year+1;
			 }

		}


		return lliMonthlyBillByClients;
	}


	//monthly bill search view
	@RequestMapping(mapping="/search", requestMethod=RequestMethod.All) // there may be more parameter (like month of the year etc)
	public LLIMonthlyBillByClient getMonthlyBillView(
			HttpServletRequest request,
			@RequestParameter("id")long clientId,
			@RequestParameter("date")long date
			)throws Exception {		
		
	    login.LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

		if(clientId==-1) {
			logger.debug("clientId is before : "+clientId);
			clientId= loginDTO.getAccountID();
			logger.debug("clientId is after: "+clientId);
		}
		Date d = new Date(date);
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(d);  
		int month = cal.get(Calendar.MONTH); 
		int year = cal.get(Calendar.YEAR); 
		logger.debug("the month is: "+month+" and the year is: "+year);
		
		return lliMonthlyBillService.getLLIMonthlyBillByClient(clientId, month, year);		
	}
	@ForwardedAction
	@RequestMapping(mapping="/check", requestMethod=RequestMethod.All)
	public String checkIsBillGenerateSearch(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
		RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_LLI_BILL_GENERATION_CHECK, request,
				lliMonthlyBillService, SessionConstants.VIEW_LLI_BILL_GENERATION_CHECK, SessionConstants.SEARCH_LLI_BILL_GENERATION_CHECK);
		rnManager.doJob(loginDTO);
		return "lli-monthly-bill-generation-check";
	}
	
	@ForwardedAction
	@RequestMapping(mapping="/searchPage", requestMethod=RequestMethod.GET) // It will load the search page only 
	public String getMonthlyBillSearch() throws Exception {		
		return "lli-monthly-bill-search-page";		
	}
	// end monthly bill search view
	
	//  current month bill generate page, generate action and checkings
	 
	@ForwardedAction
	@RequestMapping(mapping="/goGenerate", requestMethod=RequestMethod.GET) // It will load the search page only 
	public String goMonthlyBillGeneratePage() throws Exception {		
		return "lli-monthly-bill-generate-page";		
	}
	
	
	@RequestMapping(mapping="/billGenerate", requestMethod=RequestMethod.GET) // It will load the search page only 
	public void getMonthlyBillGenerate() throws Exception {		
		 lliMonthlyBillService.generateCurrentLLIMonthlyBillForAll();		
	}
	
	@RequestMapping(mapping="/checkIsGenerate", requestMethod=RequestMethod.GET) // It will load the search page only 
	public boolean checkMonthlyBillGenerate() throws Exception {		
		return lliMonthlyBillService.isCurrentMonthlyBillSummaryGenerated()	;	
	}
	
	@JsonPost
	@RequestMapping(mapping="/billGenerateByClient", requestMethod=RequestMethod.POST) // It will load the search page only 
	public void getMonthlyBillGenerate(
			@RequestParameter(isJsonBody=true ,value ="ids")List<Long> ids 
		) throws Exception {		
		
		
		if(ids!=null && ids.size() > 0) 
		{

			lliMonthlyBillService.generateCurrentLLIMonthlyBill(ids);

			//LLIMonthlyBillSummaryByClient clientSummary = lliMonthlyBillSummaryService.getCurrentLLIMonthlyBillSummary(408002L);

		}
	}


	// current month bill generate ends
}
