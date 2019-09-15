package lli.monthlyBillSummary;


import annotation.ForwardedAction;
import global.GlobalService;
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

@ActionRequestMapping("lli/monthly-bill-summary")
public class LLIMonthlyBillSummaryAction extends AnnotatedRequestMappingAction {

    static Logger logger = Logger.getLogger(LLIMonthlyBillSummaryAction.class);

    @Service
    LLIMonthlyBillSummaryService lliMonthlyBillSummaryService;

    @Service
    GlobalService globalService;

    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public LLIMonthlyBillSummaryByClient getMonthlyBillView(
            HttpServletRequest request,
            @RequestParameter("id") long clientId,
            @RequestParameter("date") long date
    ) throws Exception {

        login.LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

        if (clientId == -1) {
            logger.debug("clientId is before : " + clientId);
            clientId = loginDTO.getAccountID();
            logger.debug("clientId is after: " + clientId);
        }
        Date d = new Date(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        logger.debug("the month is: " + month + " and the year is: " + year);
        LLIMonthlyBillSummaryByClient lliMonthlyBillSummary = lliMonthlyBillSummaryService.getLLIMonthlyBillSummary(clientId, month, year);
        return lliMonthlyBillSummary;
    }

	
/*	//this is method for reteurning dummy data 
	private LLIMonthlyBillSummaryByClient getLLIMonthlyBillSummaryByClient(long clientId, int month, int year) {
		// TODO Auto-generated method stub
		LLIMonthlyBillSummaryByClient lliMonthlyBillSummaryByClient = new LLIMonthlyBillSummaryByClient();
		
		lliMonthlyBillSummaryByClient.setID((long) 12);
		lliMonthlyBillSummaryByClient.setClientID(clientId);
		//lliMonthUsageByClient.adjustmentAmount =500;

		return lliMonthlyBillSummaryByClient;
		
	}*/

    @ForwardedAction
    @RequestMapping(mapping = "/searchPage", requestMethod = RequestMethod.GET)
    public String getMonthlyBillSummarySearch() throws Exception {
        return "lli-monthly-bill-summary";
    }






}
