<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;

String idStr = request.getParameter("entityID");

if( loginDTO.getAccountID() > 0 && idStr != null && loginDTO.getAccountID() == Long.parseLong(idStr) ){
	
	hasPermission = true;
}
else if( loginDTO.getUserID()>0 )
{
    if((loginDTO.getMenuPermission(PermissionConstants.DOMAIN_CLIENT) !=-1)
    		
      &&(loginDTO.getMenuPermission(PermissionConstants.DOMAIN_CLIENT_SEARCH) >= PermissionConstants.PERMISSION_READ)) {
    	
        hasPermission=true;
    }
}
if( !hasPermission ){
	new CommonActionStatusDTO().setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}

%>

<%
request.setAttribute("menu","domainMenu");
request.setAttribute("subMenu1","domainClient");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Domain Client Preview" /> 
	<jsp:param name="body" value="../domain/client/clientPreviewBody.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
</jsp:include> 