<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ include file="../../includes/checkLogin.jsp" %>
<%
boolean hasPermission = false;
int permission;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.DOMAIN_OWNERSHIP_CHANGE) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.DOMAIN_OWNERSHIP_CHANGE )>=PermissionConstants.PERMISSION_MODIFY ) )
    	
        hasPermission=true;
    
}

if( loginDTO.getAccountID() > 0 ){
	hasPermission = true;
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
  request.setAttribute("subMenu1","domainAllMenu");
  request.setAttribute("subMenu2","requestDomainOwnershipMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain Ownership Change" /> 
	<jsp:param name="body" value="../domain/ownership/requestForOwnershipBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
</jsp:include>