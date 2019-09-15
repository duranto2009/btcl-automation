<%@ page import="common.ModuleConstants" %><%
	int moduleID = Integer.parseInt(request.getParameter("module"));
	if(moduleID == ModuleConstants.Module_ID_VPN){
		request.setAttribute("menu", "vpnMenu");
		request.setAttribute("subMenu1","vpnReportMenu");
		request.setAttribute("subMenu2","vpnReportMenuNetworkLink");

	}
	else if(moduleID == ModuleConstants.Module_ID_LLI){
		request.setAttribute("menu", "lliMenu");
		request.setAttribute("subMenu1","lliReportMenu");
		request.setAttribute("subMenu2","lliConnectionReportMenu");

	}
	else if(moduleID == ModuleConstants.Module_ID_COLOCATION){
		request.setAttribute("menu", "colocationMenu");
		request.setAttribute("subMenu1","colocationReportMenu");
		request.setAttribute("subMenu2","colocationConnectionReportMenu");

	}
	else if(moduleID == ModuleConstants.Module_ID_NIX){
		request.setAttribute("menu", "nixMenu");
		request.setAttribute("subMenu1","nixReportMenu");
		request.setAttribute("subMenu2","nixConnectionReportMenu");

	}
	else if(moduleID == ModuleConstants.Module_ID_UPSTREAM){
		request.setAttribute("menu", "upstreamMenu");
		request.setAttribute("subMenu1","upStreamReportMenu");
		request.setAttribute("subMenu2","upStreamConnectionReportMenu");

	}



%>

<jsp:include page="/layout/layout2018.jsp" flush="true">
	<jsp:param name="title" value="Report"/>
	<jsp:param name="css" value="assets/pages/css/profile.min.css" />
	<jsp:param name="css" value="assets/global/plugins/datatables/datatables.min.css" />
	<jsp:param name="body" value="../report/connection/report-body.jsp"/>
</jsp:include>
