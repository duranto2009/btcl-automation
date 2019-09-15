package reportnew;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import common.RequestFailureException;
import login.LoginDTO;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ActionRequestMapping("report/")
public class ReportAction extends AnnotatedRequestMappingAction {

    @Service private ReportCheckBoxBuilder reportCheckBoxBuilder;
    @Service private ReportService reportService;

    //region Application Reports
    @ForwardedAction
    @RequestMapping(mapping = "application", requestMethod = RequestMethod.GET)
    public String getReportApplication(@RequestParameter("module") int module) throws Exception {
        return "report-application";
    }

    @RequestMapping(mapping="client", requestMethod = RequestMethod.GET)
    public ActionForward getClientReport(ActionMapping mapping){
        return mapping.findForward("report-client");
    }

    @RequestMapping(mapping="bill", requestMethod = RequestMethod.GET)
    public ActionForward getBillReport(ActionMapping mapping){
        return mapping.findForward("report-bill");
    }

    @RequestMapping(mapping = "payment", requestMethod = RequestMethod.GET)
    public ActionForward getPaymentReport(ActionMapping mapping) {
        return mapping.findForward("report-payment");
    }

    @RequestMapping(mapping = "get-application-check-boxes", requestMethod = RequestMethod.All)
    public JsonObject getCheckBoxesApplication(@RequestParameter("module") int module, LoginDTO loginDTO) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject = reportCheckBoxBuilder.getCheckBoxesApplication(module, jsonObject, loginDTO);
        return jsonObject;
    }

    @JsonPost
    @RequestMapping(mapping = "fetch-application-report", requestMethod = RequestMethod.POST)
    public Report getReportDataApplication(
            @RequestParameter(isJsonBody = true, value = "application") PostEntity postEntity,
            @RequestParameter(isJsonBody = true, value="module") int module,
            LoginDTO loginDTO) throws Exception {
        return reportService.getReportBatchOperationApplication(postEntity,module,loginDTO);
    }
    //endregion

    //region connection Report
    @ForwardedAction
    @RequestMapping(mapping = "connection", requestMethod = RequestMethod.GET)
    public String getReportConnection(@RequestParameter("module") int module) throws Exception {
        return "report-connection";
    }
    @RequestMapping(mapping = "get-connection-check-boxes", requestMethod = RequestMethod.All)
    public JsonObject getCheckBoxesConnection(@RequestParameter("module") int module, LoginDTO loginDTO) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject = reportCheckBoxBuilder.getCheckBoxesConnection(module, jsonObject, loginDTO);
        return jsonObject;
    }

    @JsonPost
    @RequestMapping(mapping = "fetch-connection-report", requestMethod = RequestMethod.POST)
    public Report getReportDataConnection(
            @RequestParameter(isJsonBody = true, value = "application") PostEntity postEntity,
            @RequestParameter(isJsonBody = true, value = "module") int module,
            LoginDTO loginDTO) throws Exception {
        return reportService.getReportBatchOperationConnection(postEntity,module,loginDTO);
    }

    @SuppressWarnings("unused")
    @RequestMapping(mapping="GetClientReportTableData", requestMethod = RequestMethod.All)
    public List<List<Object>>  getClientReportResult(HttpServletRequest request) throws Exception{

        Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));

        List<List<Object>> rows = null;

        Integer recordPerPage = Integer.parseInt(request.getParameter("RECORDS_PER_PAGE"));
        Integer pageNo = Integer.parseInt(request.getParameter("pageno"));
        int offset = (pageNo-1)*recordPerPage;


        if(moduleID == null){
            throw new RequestFailureException("Invalid module ID");
        }else {
            rows = reportService.generateReportForClient(moduleID,request,recordPerPage,offset);
        }
        return rows;
    }

    @SuppressWarnings("unused")
    @RequestMapping(mapping="GetClientTotalResultCount", requestMethod = RequestMethod.All)
    public Integer getCleintTotalResultCount(HttpServletRequest request) throws Exception{

        Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
        Integer totalCount = null;

        if(moduleID == null){
            throw new RequestFailureException("Invalid module ID");
        }else {
            totalCount = reportService.getClientTotalReportCount(moduleID,request);
        }
        return totalCount;

    }


    // endregion
    @RequestMapping(mapping="GetBillReportTableData", requestMethod = RequestMethod.All)
    public List<List<Object>>  getBillReportResult(HttpServletRequest request) throws Exception{

        Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));

        List<List<Object>> rows = null;

        Integer recordPerPage = Integer.parseInt(request.getParameter("RECORDS_PER_PAGE"));
        Integer pageNo = Integer.parseInt(request.getParameter("pageno"));
        int offset = (pageNo-1)*recordPerPage;


        if(moduleID == null){
            throw new RequestFailureException("Invalid module ID");
        }else {
            rows = reportService.generateReportForBill(moduleID,request,recordPerPage,offset);
        }
        return rows;
    }

    @RequestMapping(mapping = "GetPaymentReportTableData", requestMethod = RequestMethod.All)
    public List<List<Object>> getPaymentReportResult(HttpServletRequest request) throws Exception {

        //Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
        List<List<Object>> rows = null;

        Integer recordPerPage = Integer.parseInt(request.getParameter("RECORDS_PER_PAGE"));
        Integer pageNo = Integer.parseInt(request.getParameter("pageno"));
        int offset = (pageNo - 1) * recordPerPage;

        rows = reportService.generateReportForPayment(request, recordPerPage, offset);
        return rows;
    }

    @RequestMapping(mapping = "GetPaymentTotalResultCount", requestMethod = RequestMethod.All)
    public Integer getPaymentTotalResultCount(HttpServletRequest request) throws Exception {

        //Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
        Integer totalCount = null;

        totalCount = reportService.getTotalReportCountForPayment(request);

        return totalCount;

    }

    @SuppressWarnings("unused")
    @RequestMapping(mapping="GetBillTotalResultCount", requestMethod = RequestMethod.All)
    public Integer getBillTotalResultCount(HttpServletRequest request) throws Exception{

        Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
        Integer totalCount = null;

        if(moduleID == null){
            throw new RequestFailureException("Invalid module ID");
        }else {
            totalCount = reportService.getBillTotalReportCount(moduleID,request);
        }
        return totalCount;

    }



}
