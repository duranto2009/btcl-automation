<%@page import="domain.DomainService"%>
<%@page import="login.PermissionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.RequestFailureException"%>
<%
/* 	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

	boolean hasPermission = false;
	
	if(loginDTO.getUserID()>0)
	{
	    if((loginDTO.getMenuPermission(PermissionConstants.DOMAIN_MARKET) !=-1)
	    		
	      &&(loginDTO.getMenuPermission(PermissionConstants.BUY_DOMAIN_SEARCH) >= PermissionConstants.PERMISSION_READ)){
	    	
	        hasPermission=true;
	    }
	}
	else if( loginDTO.getAccountID() > 0 ){
		
		long domainClientId = (Long)request.getAttribute( "clientID" );
		
		if( domainClientId == loginDTO.getAccountID() || new DomainService().isOwnershipRequestPending(loginDTO, domainDTO)){
			
			hasPermission = true;
		}
	}
	
	if( !hasPermission ){
		
		throw new RequestFailureException( "You don't have permission to see this page" );
	} */
%>

<%
	request.setAttribute("menu", "domainMenu");
	request.setAttribute("subMenu1", "domainAllMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain View" /> 
	<jsp:param name="body" value="../domain/domainSearch/domainViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 