package report;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import common.ModuleConstants;
import common.RequestFailureException;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
@ActionRequestMapping("report/")
@Deprecated
public class ReportAction extends AnnotatedRequestMappingAction {
	@Service
	ReportService reportService;

	@RequestMapping(mapping="GetServiceEntityReport", requestMethod = RequestMethod.GET)
	public ActionForward getNewReport(ActionMapping mapping){
		return mapping.findForward("GetServiceEntityReport");
	}



	@RequestMapping(mapping="generateReport", requestMethod = RequestMethod.POST)
	public void generateReport(){

	}

//	@SuppressWarnings("unused")
//	@RequestMapping(mapping="GetServiceEntityReportTableData", requestMethod = RequestMethod.All)
//	public List<List<Object>>  getReportResult(HttpServletRequest request) throws Exception{
//
//		Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
//
//		List<List<Object>> rows = null;
//
//		Integer recordPerPage = Integer.parseInt(request.getParameter("RECORDS_PER_PAGE"));
//		Integer pageNo = Integer.parseInt(request.getParameter("pageno"));
//		int offset = (pageNo-1)*recordPerPage;
//
//
//		if(moduleID == null){
//			throw new RequestFailureException("Invalid module ID");
//		}else if(moduleID == ModuleConstants.Module_ID_DOMAIN){
//			rows = reportService.generateReportForDomain(request,recordPerPage,offset);
//		}else if(moduleID == ModuleConstants.Module_ID_VPN){
//			rows = reportService.generateReportForVPN(request,recordPerPage,offset);
//		}else if(moduleID == ModuleConstants.Module_ID_LLI){
//			rows = reportService.generateReportForLLI(request,recordPerPage,offset);
//		}else if(moduleID == ModuleConstants.Module_ID_COLOCATION){
//			rows = reportService.generateReportForColocation(request,recordPerPage,offset);
//		}else if(moduleID == ModuleConstants.Module_ID_CRM){
//			rows = reportService.generateReportForCRM(request,recordPerPage,offset);
//		}
//		return rows;
//	}

//	@SuppressWarnings("unused")
//	@RequestMapping(mapping="GetServiceEntityTotalResultCount", requestMethod = RequestMethod.All)
//	public Integer getTotalResultCount(HttpServletRequest request) throws Exception{
//
//		Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
//		Integer totalCount = null;
//
//		if(moduleID == null){
//			throw new RequestFailureException("Invalid module ID");
//		}else if(moduleID == ModuleConstants.Module_ID_DOMAIN){
//			totalCount = reportService.getTotalReportCountForDomain(request);
//		}else if(moduleID == ModuleConstants.Module_ID_VPN){
//			totalCount = reportService.getTotalReportCountForVPN(request);
//		}else if(moduleID == ModuleConstants.Module_ID_LLI){
//			totalCount = reportService.getTotalReportCountForForLLI(request);
//		}else if(moduleID == ModuleConstants.Module_ID_COLOCATION){
//			totalCount = reportService.getTotalReportCountForForColocation(request);
//		}else if(moduleID == ModuleConstants.Module_ID_CRM){
//			totalCount = reportService.getTotalReportCountForCRM(request);
//		}
//		return totalCount;
//
//
//	}







}
