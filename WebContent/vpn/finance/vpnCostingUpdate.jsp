<%@page import="common.*"%>

<%
  
	int moduleID = Integer.parseInt(request.getParameter("moduleID"));	
	switch (moduleID){
		case ModuleConstants.Module_ID_VPN:
			request.setAttribute("menu","vpnMenu");
			request.setAttribute("subMenu1", "vpnConfigurationSubmenu1");
		  	request.setAttribute("subMenu2", "vpnCostConfigSubmenu2");
		  	request.setAttribute("subMenu3", "vpnCostConfigMenu");
			break;
		case ModuleConstants.Module_ID_LLI:
			request.setAttribute("menu","lliMenu");
			request.setAttribute("subMenu1", "lliConfigurationSubmenu1");
		  	request.setAttribute("subMenu2", "lliCostConfigSubmenu2");
			break;
		
		
		default:
				
			
	}
  	
  	
 %> 
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Cost Configuration" /> 
	<jsp:param name="body" value="../vpn/finance/vpnCostConfigPage.jsp" />
</jsp:include>