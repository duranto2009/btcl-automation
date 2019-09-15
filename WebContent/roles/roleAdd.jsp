<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.ROLE_MANAGEMENT) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.ROLE_ADD )>PermissionConstants.PERMISSION_READ ) ){
    	
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
  	request.setAttribute("menu","userAndRoleManagement");
	request.setAttribute("subMenu1","roleManagement");
  	request.setAttribute("subMenu2","roleAdd");
%>
<jsp:include page="../common/layout.jsp" flush="true">
<jsp:param name="title" value="Add Role" /> 
	<jsp:param name="body" value="../roles/roleAddBody.jsp" />
</jsp:include> 