<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.USER_MANAGEMENT) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.USER_SEARCH )>PermissionConstants.PERMISSION_READ ) )
    	
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
  	request.setAttribute("menu","userAndRoleManagement");
	request.setAttribute("subMenu1","userManagement");
  	request.setAttribute("subMenu2","userSearch");
%>
<jsp:include page="../common/layout.jsp" flush="true">
<jsp:param name="title" value="User Search" /> 
	<jsp:param name="body" value="../users/userBody.jsp" />
</jsp:include> 

