package vpn.monthlyUsage;


import annotation.ForwardedAction;
import org.apache.log4j.Logger;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@ActionRequestMapping("vpn/monthly-usage")
public class VPNMonthlyUsageAction extends AnnotatedRequestMappingAction{

	static Logger logger = Logger.getLogger(VPNMonthlyUsageAction.class);

	@Service
	VPNMonthlyUsageService vpnMonthlyUsageService;
	
	//monthly bill search view
	@RequestMapping(mapping="/view", requestMethod=RequestMethod.All) // there may be more parameter (like month of the year etc)
	public VPNMonthlyUsageByClient getMonthlyUsageByClient(
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

		return vpnMonthlyUsageService.getVPNMonthlyUsageByClient(clientId, month, year);
	}

	@ForwardedAction
	@RequestMapping(mapping="/search", requestMethod=RequestMethod.GET) // It will load the search page only 
	public String getMonthlyUsageSearchPage() throws Exception {		
		return "vpn-monthly-usage-search-page";
	}
}
