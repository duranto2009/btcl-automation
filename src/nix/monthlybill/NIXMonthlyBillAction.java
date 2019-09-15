package nix.monthlybill;

import annotation.ForwardedAction;
import annotation.JsonPost;
import common.ClientDTO;
import common.repository.AllClientRepository;
import login.LoginDTO;
import nix.connection.NIXConnection;
import nix.connection.NIXConnectionService;
import nix.monthlybillsummary.NIXMonthlyBillSummaryByClient;
import nix.monthlybillsummary.NIXMonthlyBillSummaryService;
import nix.monthlyusage.NIXMonthlyUsageByClient;
import nix.monthlyusage.NIXMonthlyUsageByClientService;
import nix.monthlyusage.NIXMonthlyUsageService;
import nix.outsourcebill.NIXMonthlyOutsourceBill;
import nix.outsourcebill.NIXMonthlyOutsourceBillByConnection;
import nix.outsourcebill.NIXMonthlyOutsourceBillByConnectionService;
import nix.outsourcebill.NIXMonthlyOutsourceBillService;
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
import java.util.List;

import org.apache.log4j.Logger;
import util.RecordNavigationManager;

@ActionRequestMapping("nix/monthly-bill")
public class NIXMonthlyBillAction extends AnnotatedRequestMappingAction {


    static Logger logger = Logger.getLogger(nix.monthlybill.NIXMonthlyBillAction.class);

    @Service
    NIXMonthlyBillByClientService nixMonthlyBillByClientService;

    @Service
    NIXMonthlyBillService nixMonthlyBillService;

    @Service
    NIXMonthlyUsageService nixMonthlyUsageService;

    @Service
    NIXMonthlyBillSummaryService nixMonthlyBillSummaryService;

    @Service
    NIXMonthlyOutsourceBillService nixMonthlyOutsourceBillService;

    @Service
    NIXMonthlyOutsourceBillByConnectionService nixMonthlyOutsourceBillByConnectionService;

    @Service
    NIXConnectionService nixConnectionService;


    @ForwardedAction
    @RequestMapping(mapping = "/searchPage", requestMethod = RequestMethod.GET) // It will load the search page only
    public String getMonthlyBillSearch() throws Exception {
        return "nix-monthly-bill-search-page";
    }


    @ForwardedAction
    @RequestMapping(mapping = "/goGenerate", requestMethod = RequestMethod.GET) // It will load the search page only
    public String goMonthlyBillGeneratePage() throws Exception {
        return "nix-monthly-bill-generate-page";
    }


    @RequestMapping(mapping = "/billGenerate", requestMethod = RequestMethod.GET) // It will load the search page only
    public void getMonthlyBillGenerate() throws Exception {
        nixMonthlyBillService.generateCurrentNIXMonthlyBillForAll();
    }


    @RequestMapping(mapping = "/checkIsGenerate", requestMethod = RequestMethod.GET)
    // It will load the search page only
    public boolean checkMonthlyBillGenerate() throws Exception {
        return nixMonthlyBillService.isCurrentMonthlyBillSummaryGenerated();
    }


    @JsonPost
    @RequestMapping(mapping = "/billGenerateByClient", requestMethod = RequestMethod.POST)
    // It will load the search page only
    public void getMonthlyBillGenerate(
            @RequestParameter(isJsonBody = true, value = "ids") List<Long> ids
    ) throws Exception {
        if (ids != null && ids.size() > 0) {
            nixMonthlyBillService.generateCurrentNIXMonthlyBill(ids);
        }
    }


    @ForwardedAction
    @RequestMapping(mapping = "/check", requestMethod = RequestMethod.All)
    public String checkIsBillGenerateSearch(LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        RecordNavigationManager rnManager = new RecordNavigationManager(SessionConstants.NAV_NIX_BILL_GENERATION_CHECK, request,
                nixMonthlyBillService, SessionConstants.VIEW_NIX_BILL_GENERATION_CHECK, SessionConstants.SEARCH_NIX_BILL_GENERATION_CHECK);
        rnManager.doJob(loginDTO);
        return "nix-monthly-bill-generation-check";
    }


    @ForwardedAction
    @RequestMapping(mapping = "/monthly-bill-summary-searchPage", requestMethod = RequestMethod.GET)
    public String getMonthlyBillSummary() throws Exception {
        return "nix-monthly-bill-summary";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/monthly-bill-usage-searchPage", requestMethod = RequestMethod.GET)
    // It will load the search page only
    public String getMonthlyUsageSearchPage() throws Exception {
        return "nix-monthly-usage-search-page";
    }

    @ForwardedAction
    @RequestMapping(mapping = "/monthly-outsource-bill-search", requestMethod = RequestMethod.All)
    public String getMonthlyOutSourceBillSummaryPage() {
        return "nix-monthly-outsourcing-bill-page";
    }


    @RequestMapping(mapping = "/monthly-outsource-bill-summary", requestMethod = RequestMethod.GET)
    public NIXMonthlyOutsourceBill getMonthlyOutSourceBillSummary(
            @RequestParameter("vendor") long vendor,
            @RequestParameter("month") int month,
            @RequestParameter("year") int year) throws Exception {

        return nixMonthlyOutsourceBillService.getByVendorIdByMonthByYear(vendor, month, year);
    }

    @RequestMapping(mapping = "/monthly-outsource-bill-details", requestMethod = RequestMethod.GET)
    public List<NIXMonthlyOutsourceBillByConnection> getMonthlyOutSourceBillDetails(
            @RequestParameter("vendor") long vendor,
            @RequestParameter("month") int month,
            @RequestParameter("year") int year) throws Exception {

        NIXMonthlyOutsourceBill nixMonthlyOutsourceBill = nixMonthlyOutsourceBillService.getByVendorIdByMonthByYear(vendor, month, year);
        long outSourceBillId = nixMonthlyOutsourceBill.getId();
        List<NIXMonthlyOutsourceBillByConnection> nixMonthlyOutsourceBillByConnections =
                nixMonthlyOutsourceBillByConnectionService.getByOutsourceBillId(outSourceBillId);


        for (NIXMonthlyOutsourceBillByConnection nixMonthlyOutsourceBillByConnection : nixMonthlyOutsourceBillByConnections) {
            long conId = nixMonthlyOutsourceBillByConnection.getConnectionId();
            NIXConnection nixConnection = nixConnectionService.getConnnectionByConnectionId(conId);

            long clientId = nixConnection.getClient();
            ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(clientId);
            nixMonthlyOutsourceBillByConnection.setClientName(clientDTO.getName());
            nixMonthlyOutsourceBillByConnection.setActiveFrom(nixConnection.getActiveFrom());
        }
        return nixMonthlyOutsourceBillByConnections;
    }


    @RequestMapping(mapping = "/monthly-bill-usage-search", requestMethod = RequestMethod.All)
    public NIXMonthlyUsageByClient getMonthlyUsage(
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

        return nixMonthlyUsageService.getNIXMonthlyUsageByClient(clientId, month, year);
    }


    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public NIXMonthlyBillByClient getMonthlyBillView(
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

        return nixMonthlyBillByClientService.getByClientIdAndDateRange(clientId, month, year);
    }


    @RequestMapping(mapping = "/monthly-bill-summary-search", requestMethod = RequestMethod.All)
    public NIXMonthlyBillSummaryByClient getMonthlyBillSummaryView(
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

        NIXMonthlyBillSummaryByClient summary = nixMonthlyBillSummaryService.getNIXMonthlyBillSummary(clientId, month, year);

        return summary;
    }
}
