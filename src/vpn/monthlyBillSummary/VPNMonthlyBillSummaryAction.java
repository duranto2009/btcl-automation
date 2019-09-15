package vpn.monthlyBillSummary;

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

@ActionRequestMapping("vpn/monthly-bill-summary")
public class VPNMonthlyBillSummaryAction extends AnnotatedRequestMappingAction{

	static Logger logger = Logger.getLogger(VPNMonthlyBillSummaryAction.class);

	@Service
	VPNMonthlyBillSummaryService vpnMonthlyBillSummaryService;
	
	@RequestMapping(mapping="/search", requestMethod=RequestMethod.All) 
	public VPNMonthlyBillSummaryByClient getMonthlyBillView(
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

		VPNMonthlyBillSummaryByClient vpnMonthlyBillSummary = vpnMonthlyBillSummaryService.getVPNMonthlyBillSummary(clientId, month, year);

		return 	vpnMonthlyBillSummary;
	}

	@ForwardedAction
	@RequestMapping(mapping="/searchPage", requestMethod=RequestMethod.GET) 
	public String getMonthlyBillSummarySearch() throws Exception {		
		return "vpn-monthly-bill-summary";
	}

}
