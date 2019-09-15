<%@page import="common.EntityTypeConstant"%>
<%@page import="request.StateRepository"%>
<%@page import="request.StateDTO"%>
<%@page import="domain.DomainDTO"%>
<%@page import="domain.DomainService"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;

String idStr = request.getParameter("entityID");
long domID = Long.parseLong( idStr );

DomainDTO domDTO = new DomainService().getDomainByID( domID );

if( loginDTO.getAccountID() > 0 && idStr != null && loginDTO.getAccountID() == domDTO.getDomainClientID() ){
	
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
	request.setAttribute("menu", "domainMenu");
	request.setAttribute("subMenu1", "domainAllMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Domain Preview" /> 
	<jsp:param name="body" value="../domain/domainSearch/domainPreviewBody.jsp" />
</jsp:include> 