<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;

String idStr = request.getParameter("entityID");

System.out.println( idStr );
if( loginDTO.getAccountID() > 0 && idStr != null && loginDTO.getAccountID() == Long.parseLong(idStr) ){
	
	hasPermission = true;
}
else if( loginDTO.getUserID()>0 )
{
    if((loginDTO.getMenuPermission(PermissionConstants.COLOCATION_CLIENT) !=-1)
      &&(loginDTO.getMenuPermission(PermissionConstants.COLOCATION_CLIENT_SEARCH) >= PermissionConstants.PERMISSION_FULL)) {
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
  request.setAttribute("menu","colocationMenu");
  request.setAttribute("subMenu1","colocationClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Client Edit" /> 
	<jsp:param name="body" value="../coLocation/client/clientEditBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="mobileNumberHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 