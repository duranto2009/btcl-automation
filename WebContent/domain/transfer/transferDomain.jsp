<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ include file="../../includes/checkLogin.jsp" %>
<%
boolean hasPermission = false;
CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
if(loginDTO.getUserID()>0) 
{
    if((loginDTO.getMenuPermission(PermissionConstants.DOMAIN_TRANSFER) !=-1)
    		
      &&(loginDTO.getMenuPermission(PermissionConstants.DOMAIN_TRANSFER) >= PermissionConstants.PERMISSION_MODIFY)){
    	
        hasPermission=true;
    }
}

if( !hasPermission ){
	new CommonActionStatusDTO().setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}
%>
<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","domainAllMenu");
  request.setAttribute("subMenu2","transferDomainMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Transfer Domain" /> 
	<jsp:param name="body" value="../domain/transfer/transferDomainBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
</jsp:include>