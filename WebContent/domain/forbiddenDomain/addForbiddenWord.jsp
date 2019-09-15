<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.FORBIDDEN_WORD) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.FORBIDDEN_WORD_ADD )>PermissionConstants.PERMISSION_READ ) )
    	
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
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","configDomainMenu");
  request.setAttribute("subMenu2","forbiddenDomainnMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Add Forbidden Word" /> 
	<jsp:param name="body" value="../domain/forbiddenDomain/addForbiddenWordBody.jsp" />
</jsp:include>