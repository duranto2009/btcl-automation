<%@ page import="common.ModuleConstants" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%
	int module = Integer.parseInt(request.getParameter("module"));
%>
<%
	if(module == ModuleConstants.Module_ID_VPN) {

		request.setAttribute("menu", "vpnMenu");
		request.setAttribute("subMenu1","vpnConfigurationSubmenu1");
		request.setAttribute("subMenu2","districtOfcInstallationCostSubmenu2");
	}else if (module == ModuleConstants.Module_ID_LLI) {
		request.setAttribute("menu", "lliMenu");
		request.setAttribute("subMenu1","lli-configuration");
		request.setAttribute("subMenu2","districtOfcInstallationCostSubmenu2LLI");
	}
%>
<jsp:include page="../common/layout.jsp" flush="true">
	<jsp:param name="title" value="LLI OFC Installation Cost" />
	<jsp:param name="body" value="../ofcinstallation/districtOfcInstallationCostBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 