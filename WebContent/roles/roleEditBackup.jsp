<%@page import="login.PermissionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.RequestFailureException"%>

<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

	boolean hasPermission = false;
	
	if(loginDTO.getUserID()>0)
	{
	    if((loginDTO.getMenuPermission(PermissionConstants.ROLE_MANAGEMENT) !=-1)
	    		
	      &&(loginDTO.getMenuPermission(PermissionConstants.ROLE_ADD) >= PermissionConstants.PERMISSION_MODIFY)){
	    	
	        hasPermission=true;
	    }
	}
	
	if( !hasPermission ){
		
		throw new RequestFailureException( "You don't have permission to see this page" );
	}
%>

<%
  	request.setAttribute("menu","userAndRoleManagement");
	request.setAttribute("subMenu1","roleManagement");
  	request.setAttribute("subMenu2","roleSearch");
%>
<jsp:include page="../common/layout.jsp" flush="true">
<jsp:param name="title" value="Edit Role" /> 
	<jsp:param name="body" value="../roles/roleEditBodyBackup.jsp" />
</jsp:include> 