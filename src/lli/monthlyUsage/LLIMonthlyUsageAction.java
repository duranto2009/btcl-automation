package lli.monthlyUsage;


import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import annotation.ForwardedAction;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;

@ActionRequestMapping("lli/monthly-usage")
public class LLIMonthlyUsageAction  extends AnnotatedRequestMappingAction{
	
	static Logger logger = Logger.getLogger(LLIMonthlyUsageAction.class);
	

	@Service
	LLIMonthlyUsageService lliMonthlyUsageService;
	
	//monthly bill search view
	@RequestMapping(mapping="/view", requestMethod=RequestMethod.All) // there may be more parameter (like month of the year etc)
	public LLIMonthlyUsageByClient getMonthlyUsageByClient(
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
		
		//lliMonthlyUsageService.generateLastMonthLLIMonthlyUsageForAll();
		
		
		return lliMonthlyUsageService.getLLIMonthlyUsageByClient(clientId, month, year);
	}
	
	
	@ForwardedAction
	@RequestMapping(mapping="/search", requestMethod=RequestMethod.GET) // It will load the search page only 
	public String getMonthlyUsageSearchPage() throws Exception {		
		return "lli-monthly-usage-search-page";		
	}
	//end monthly bill view break down
	
	
	/*public LLIMonthlyUsageByClient getMonthlyUsageDummyData(long clientId, int month, int year) {
		LLIMonthlyUsageByClient lliMonthUsageByClient = new LLIMonthlyUsageByClient();
		
		lliMonthUsageByClient.id = (long) 12;
		lliMonthUsageByClient.clientId = clientId;
		//lliMonthUsageByClient.adjustmentAmount =500;
		lliMonthUsageByClient.createdDate =15000000l;
		lliMonthUsageByClient.discount =500;
		lliMonthUsageByClient.description ="Dummy connection description";
		lliMonthUsageByClient.discountPercentage =15;
		lliMonthUsageByClient.grandTotal =6000;
		lliMonthUsageByClient.lateFee =1000;
		lliMonthUsageByClient.longTermContructAdjustment =90;
		lliMonthUsageByClient.year =year;
		lliMonthUsageByClient.VAT =1500;
		lliMonthUsageByClient.VatPercentage =15;
		lliMonthUsageByClient.month = 4;
		return lliMonthUsageByClient;
	}*/
}
