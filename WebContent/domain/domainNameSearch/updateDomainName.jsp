<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;

if(loginDTO.getUserID()>0)
{
    if((loginDTO.getMenuPermission(PermissionConstants.FILTER_DOMAIN) !=-1) &&
    		(loginDTO.getMenuPermission(PermissionConstants.FILTER_DOMAIN_ADD) >= PermissionConstants.PERMISSION_MODIFY )) {
    	
        hasPermission=true;
    }
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
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","domainAllMenu");
  request.setAttribute("subMenu2","configPackage");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain Package Update" /> 
	<jsp:param name="body" value="../domain/domainNameSearch/updateDomainNameBody.jsp" />
</jsp:include>