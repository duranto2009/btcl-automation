<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.ROLE_MANAGEMENT) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.ROLE_SEARCH )>=PermissionConstants.PERMISSION_READ ) )
    	
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
	request.setAttribute("subMenu1","roleManagement");
  	request.setAttribute("subMenu2","roleSearch");
%>
<jsp:include page="../common/layout.jsp" flush="true">
<jsp:param name="title" value="Role Search" /> 
	<jsp:param name="body" value="../roles/roleSearchBody.jsp" />
</jsp:include>
