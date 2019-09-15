<%@page import="common.ModuleConstants"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>



<%
int moduleID = Integer.parseInt(request.getParameter("moduleID"));
if(moduleID == ModuleConstants.Module_ID_DOMAIN){
	request.setAttribute("menu", "domainMenu");
	request.setAttribute("subMenu1","domainReportMenu");
}else if(moduleID == ModuleConstants.Module_ID_VPN){
	request.setAttribute("menu", "vpnMenu");
	request.setAttribute("subMenu1","vpnReportMenu");
}else if(moduleID == ModuleConstants.Module_ID_LLI){
	request.setAttribute("menu", "lliMenu");
	request.setAttribute("subMenu1","lliReportMenu");
}else if(moduleID == ModuleConstants.Module_ID_COLOCATION){
	request.setAttribute("menu", "colocationMenu");
	request.setAttribute("subMenu1","colocationReportMenu");
}
%> 

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Service Entity Report" /> 
	<jsp:param name="body" value="../report/serviceEntity/reportBody.jsp" />
	<jsp:param name="css" value="assets/pages/css/profile.min.css" />
	<jsp:param name="css" value="report/report.css" />
	<jsp:param name="js" value="assets/report/jquery.dataTables.min.js" />
	<jsp:param name="js" value="assets/report/dataTables.buttons.min.js" />
	<jsp:param name="js" value="assets/report/jszip.min.js" />
	<jsp:param name="js" value="assets/report/pdfmake.min.js" />
	<jsp:param name="js" value="assets/report/vfs_fonts.js" />
	<jsp:param name="js" value="assets/report/buttons.html5.min.js" />
	
	<jsp:param name="css" value="assets/global/plugins/datatables/datatables.min.css" />
	<jsp:param name="js" value="assets/global/scripts/datatable.js" />
	<jsp:param name="js" value="assets/pages/scripts/table-datatables-scroller.min.js" />
	<jsp:param name="js" value="report/report.js" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include>