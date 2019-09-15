<%@page import="login.PermissionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.RequestFailureException"%>

<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

	boolean hasPermission = false;
	
	if(loginDTO.getUserID()>0)
	{
	    if((loginDTO.getMenuPermission(PermissionConstants.COMMON_CHARGE) !=-1)
	    		
	      &&( loginDTO.getMenuPermission(PermissionConstants.COMMON_CHARGE) >= PermissionConstants.PERMISSION_FULL )){
	    	
	        hasPermission=true;
	    }
	}
	
	if( !hasPermission ){
		
		throw new RequestFailureException( "You don't have permission to see this page" );
	}
%>

<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","configDomainMenu");
  request.setAttribute("subMenu2","commonChargeDomainMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain Common Config" /> 
	<jsp:param name="body" value="../domain/package/domainCommonChargeCofigBody.jsp" />
</jsp:include>