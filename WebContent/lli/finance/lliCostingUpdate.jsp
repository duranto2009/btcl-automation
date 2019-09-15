<%@page import="common.*"%>

<%
  
	int moduleID = Integer.parseInt(request.getParameter("moduleID"));	
	switch (moduleID){
		case ModuleConstants.Module_ID_LLI:
			request.setAttribute("menu","lliMenu");
			request.setAttribute("subMenu1", "lli-configuration");
		  	request.setAttribute("subMenu2", "lliCostConfigMenu");
			break;
		
		default:
	}
 %> 
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Cost Configuration" /> 
	<jsp:param name="body" value="../lli/finance/lliCostConfigPage.jsp" />
</jsp:include>