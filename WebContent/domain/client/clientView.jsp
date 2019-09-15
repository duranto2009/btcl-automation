<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.RequestFailureException"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%
String idStr = request.getParameter("entityID");
boolean hasPermission = false;

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
	
	CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
	
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}
%>

<%
request.setAttribute("menu","domainMenu");
request.setAttribute("subMenu1","domainClient");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain Client View" /> 
	<jsp:param name="body" value="../domain/client/clientViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 