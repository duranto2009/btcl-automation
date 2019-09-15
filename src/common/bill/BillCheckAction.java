package common.bill;

import annotation.ForwardedAction;
import common.RequestFailureException;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import java.util.List;

@ActionRequestMapping("bill/check")
public class BillCheckAction extends AnnotatedRequestMappingAction {

    @Service
    BillCheckService billCheckService;

    @ForwardedAction
    @RequestMapping(mapping="/clearance", requestMethod= RequestMethod.GET)
    public String getBillCheckClearancPage() throws Exception {
        return "check-bill-clearance";
    }

    @RequestMapping(mapping="/clearance-certificate", requestMethod= RequestMethod.GET)
    public List<BillDTO> getBillCheckClearancCertificate(
            @RequestParameter("client") long clientId,
            @RequestParameter("from") long fromDate,
            @RequestParameter("to") long toDate,
            @RequestParameter("module") int moduleId) throws Exception {

        return billCheckService.checkBillByClientByModule (clientId,fromDate,toDate,moduleId);
    }
}
