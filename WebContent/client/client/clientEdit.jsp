<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;
String idStr = request.getParameter("entityID");
int moduleID = Integer.parseInt(request.getParameter("moduleID"));

System.out.println( idStr );

int clientMenu = (moduleID == ModuleConstants.Module_ID_LLI) ? (moduleID*1000 + 100) : (moduleID*1000 + 2);
int clientSearchMenu = (moduleID == ModuleConstants.Module_ID_LLI) ? (moduleID*1000 + 102) : (moduleID*1000 + 4);
if( loginDTO.getAccountID() > 0 && idStr != null && loginDTO.getAccountID() == Long.parseLong(idStr) ){
	
	hasPermission = true;
}
else if( loginDTO.getUserID()>0 )
{
    if((loginDTO.getMenuPermission(clientMenu) !=-1) &&(loginDTO.getMenuPermission(clientSearchMenu) >= PermissionConstants.PERMISSION_READ)){
        hasPermission=true;
    }
    permission = loginDTO.getMenuPermission( clientSearchMenu ) ;
	
}
if( !hasPermission ){
	CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}

%>

<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliClientMenu");
%>

<%
switch (moduleID){
case ModuleConstants.Module_ID_DOMAIN:
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","domainClientMenu");
	break;
case ModuleConstants.Module_ID_WEBHOSTING:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	break;
case ModuleConstants.Module_ID_DNSHOSTING:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	break;
case ModuleConstants.Module_ID_COLOCATION:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	break;
case ModuleConstants.Module_ID_IPADDRESS:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	break;
case ModuleConstants.Module_ID_VPN:
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnClient");
	break;
case ModuleConstants.Module_ID_LLI:
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliClientMenu");
	break;
case ModuleConstants.Module_ID_IIG:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	break;
case ModuleConstants.Module_ID_ADSL:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	break;
case ModuleConstants.Module_ID_NIX:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	break;
}
%>


<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Client Edit" /> 
	<jsp:param name="body" value="../client/client/clientEditBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="mobileNumberHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 