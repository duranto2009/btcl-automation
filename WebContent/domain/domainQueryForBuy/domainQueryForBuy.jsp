<%@ page contentType="text/html;charset=utf-8" %>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.DOMAIN_MARKET) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.BUY_DOMAIN )>PermissionConstants.PERMISSION_READ ) )
    	
        hasPermission=true;
    
}

if( loginDTO.getAccountID() > 0 )
	hasPermission = true;

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
  request.setAttribute("subMenu2","buyDomainMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Domain Checker" /> 
	<jsp:param name="body" value="../domain/domainQueryForBuy/domainQueryForBuyBody.jsp" />
	
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
</jsp:include>