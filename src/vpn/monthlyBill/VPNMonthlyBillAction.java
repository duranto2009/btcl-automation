package vpn.monthlyBill;


import annotation.ForwardedAction;
import annotation.JsonPost;
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
import vpn.VPNClientService;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ActionRequestMapping("vpn/monthly-bill")
public class VPNMonthlyBillAction extends AnnotatedRequestMappingAction {
    static Logger logger = Logger.getLogger(VPNMonthlyBillAction.class);

    @Service
    VPNMonthlyBillService vpnMonthlyBillService;

    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    // there may be more parameter (like month of the year etc)
    public VPNMonthlyBillByClient getMonthlyBillView(
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

        return vpnMonthlyBillService.getVPNMonthlyBillByClient(clientId, month, year);
    }


    @ForwardedAction
    @RequestMapping(mapping = "/check", requestMethod = RequestMethod.All)
    public String checkIsBillGenerateSearch(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_VPN_BILL_GENERATION_CHECK, request,
                vpnMonthlyBillService, SessionConstants.VIEW_VPN_BILL_GENERATION_CHECK, SessionConstants.SEARCH_VPN_BILL_GENERATION_CHECK);
        rnManager.doJob(loginDTO);
        return "vpn-monthly-bill-generation-check";
    }


    @ForwardedAction
    @RequestMapping(mapping = "/searchPage", requestMethod = RequestMethod.GET) // It will load the search page only
    public String getMonthlyBillSearch() throws Exception {
        return "vpn-monthly-bill-search-page";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/goGenerate", requestMethod = RequestMethod.GET) // It will load the search page only
    public String goMonthlyBillGeneratePage() throws Exception {
        return "vpn-monthly-bill-generate-page";
    }

    @RequestMapping(mapping = "/billGenerate", requestMethod = RequestMethod.GET)
    public void getMonthlyBillGenerate() throws Exception {
        vpnMonthlyBillService.generateCurrentVPNMonthlyBillForAll();
    }

    @RequestMapping(mapping = "/checkIsGenerate", requestMethod = RequestMethod.GET)
    public boolean checkMonthlyBillGenerate() throws Exception {
        return vpnMonthlyBillService.isCurrentMonthlyBillSummaryGenerated();
    }

    @JsonPost
    @RequestMapping(mapping = "/billGenerateByClient", requestMethod = RequestMethod.POST)
    public void getMonthlyBillGenerate(
            @RequestParameter(isJsonBody = true, value = "ids") List<Long> ids
    ) throws Exception {

        if (ids != null && ids.size() > 0)
            vpnMonthlyBillService.generateCurrentVPNMonthlyBill(ids);
    }
}
