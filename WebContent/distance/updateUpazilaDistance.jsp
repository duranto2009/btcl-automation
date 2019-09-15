<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ include file="../../includes/checkLogin.jsp" %>

<%
boolean hasPermission = false;

if( loginDTO.getUserID()>0 )
{
	if((loginDTO.getMenuPermission(PermissionConstants.VPN_CONFIGURATION_DISTANCE) != -1  && (loginDTO.getMenuPermission( PermissionConstants.VPN_CONFIGURATION_DISTANCE )>=PermissionConstants.PERMISSION_MODIFY )) 
	    	|| (loginDTO.getMenuPermission(PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST) != -1  && (loginDTO.getMenuPermission( PermissionConstants.LLI_CONFIGURATION_OFC_INSTALL_COST)>=PermissionConstants.PERMISSION_MODIFY )))
	        hasPermission=true;    
}

if( !hasPermission ){	
	CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}
%>

<%
if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnConfigurationSubmenu1");
	request.setAttribute("subMenu2","vpnDistanceConfigSubmenu2");  
	request.setAttribute("subMenu3","upazilaDistanceSubmenu2");  
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliConfigurationSubmenu1");
	request.setAttribute("subMenu2","lliDistanceConfigSubmenu2");  
	request.setAttribute("subMenu3","upazilaDistanceSubmenu2");  
}
%>
<jsp:include page="../common/layout.jsp" flush="true">
<jsp:param name="title" value="Update Distance from District to Upazila" /> 
	<jsp:param name="body" value="../distance/updateUpazilaDistanceBody.jsp" />
</jsp:include> 


