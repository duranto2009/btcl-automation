<%@page import="common.EntityTypeConstant"%>
<%@page import="request.StateRepository"%>
<%@page import="request.StateDTO"%>
<%@page import="domain.DomainDTO"%>
<%@page import="domain.DomainService"%>
<%@page import="login.PermissionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.RequestFailureException"%>

<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

	boolean hasPermission = false;
	
	if(loginDTO.getUserID()>0)
	{
	    if((loginDTO.getMenuPermission(PermissionConstants.BUY_DOMAIN) !=-1)
	    		
	      &&(loginDTO.getMenuPermission(PermissionConstants.BUY_DOMAIN) >= PermissionConstants.PERMISSION_MODIFY)){
	    	
	        hasPermission=true;
	    }
	}
	else if( loginDTO.getAccountID() > 0 ){

		//If status not active, user won't have permission
		int id = Integer.parseInt(request.getParameter("entityID"));
		DomainDTO domainDTO = new DomainService().getDomainByID( id );
		
		int status = domainDTO.getCurrentStatus();

		StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID( status );
		
		if( stateDTO.getActivationStatus() == EntityTypeConstant.STATUS_NOT_ACTIVE ){
			
			hasPermission = false;
		}
		
		else{
		
			if( domainDTO.getDomainClientID() == loginDTO.getAccountID() ){
				hasPermission = true;
			}
		}
	}
	
	if( !hasPermission ){
		
		throw new RequestFailureException( "You don't have permission to see this page" );
	}
%>

<%
	request.setAttribute("menu", "domainMenu");
	request.setAttribute("subMenu1", "domainAllMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Domain Edit" /> 
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
	<jsp:param name="body" value="../domain/domainSearch/domainEditBody.jsp" />
</jsp:include> 