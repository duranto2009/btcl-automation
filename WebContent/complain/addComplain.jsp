<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;

if(loginDTO.getUserID()>0)
{
    if((loginDTO.getMenuPermission(PermissionConstants.COMPLAIN) !=-1) &&(loginDTO.getMenuPermission(PermissionConstants.COMPLAIN_ADD) == PermissionConstants.PERMISSION_FULL))
    {
        hasPermission=true;
    }
}
if( loginDTO.getAccountID() > 0 ){
	
	hasPermission = true;
}

if( !hasPermission )
{	
	CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();	
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}

%>

<%
if((ModuleConstants.Module_ID_DOMAIN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","complainMenu"+ModuleConstants.Module_ID_DOMAIN);
	request.setAttribute("subMenu2","complainAddMenu"+ModuleConstants.Module_ID_DOMAIN);
}else if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","complainMenu"+ModuleConstants.Module_ID_VPN);
	request.setAttribute("subMenu2","complainAddMenu"+ModuleConstants.Module_ID_VPN);
}
%>
<jsp:include page='../common/layout.jsp' flush="true">
<jsp:param name="title" value="Add New Complain" /> 
	<jsp:param name="body" value="../complain/addComplainBody.jsp" />
</jsp:include> 