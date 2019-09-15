<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;

if( loginDTO.getUserID()>0 )
{
    if((loginDTO.getMenuPermission(PermissionConstants.LLI_CLIENT) !=-1)
    		
      &&(loginDTO.getMenuPermission(PermissionConstants.LLI_CLIENT_MIGRATION) >= PermissionConstants.PERMISSION_FULL)) {
    	
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
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliMigration");
  request.setAttribute("subMenu2","lliClientMigration");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Update Migrated Client" /> 
	<jsp:param name="body" value="../lli/client/migratedClientUpdateBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 