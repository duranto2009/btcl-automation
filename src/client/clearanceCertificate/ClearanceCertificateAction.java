package client.clearanceCertificate;

import annotation.ForwardedAction;
import common.bill.BillCheckService;
import global.GlobalService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.KeyValuePair;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@ActionRequestMapping("clearance-certificate")
public class ClearanceCertificateAction extends AnnotatedRequestMappingAction {

    @Service
    private GlobalService globalService;

    @Service
    private BillCheckService billCheckService;

    @ForwardedAction
    @RequestMapping(mapping = "/get-search-page", requestMethod = RequestMethod.GET)
    public String getClearanceCertificateSearchPage() {
        return "clearance-certificate-search";
    }

    @RequestMapping(mapping = "/search", requestMethod = RequestMethod.All)
    public List<KeyValuePair<Integer, KeyValuePair<Integer, Double>>>
    getAllDueBillsOfClient(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParameter("clientId") long clientId,
                           @RequestParameter("fromDate") long fromDate,
                           @RequestParameter("toDate") long toDate,
                           @RequestParameter("moduleId") int moduleId) throws Exception {

        System.out.println(clientId + " " + fromDate + " " + toDate + " " + moduleId);
        return billCheckService.getMonthWiseDues(clientId, fromDate, toDate, moduleId);

    }
}
