<%@page import="login.PermissionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.RequestFailureException"%>

<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	long id = Long.parseLong(request.getAttribute("id").toString());
	boolean permissionForAddPage = false, permissionForEditPage = false, isAddPage = false;
	if(id == -1){
		isAddPage = true;
		permissionForAddPage = ( loginDTO.getMenuPermission( PermissionConstants.ROLE_ADD )>PermissionConstants.PERMISSION_READ );
	}else {
		permissionForEditPage = (loginDTO.getMenuPermission(PermissionConstants.ROLE_ADD) >= PermissionConstants.PERMISSION_MODIFY);
	}
	boolean hasPermission = false;
	
	if(loginDTO.getUserID()>0)
	{
	    if((loginDTO.getMenuPermission(PermissionConstants.ROLE_MANAGEMENT) !=-1)
	    		
	      && (isAddPage ==true ? permissionForAddPage : permissionForEditPage)
	      
	    ){
	    	
	        hasPermission=true;
	    }
	}
	
	if( !hasPermission ){
		
		throw new RequestFailureException( "You don't have permission to see this page" );
	}

  	request.setAttribute("menu","userAndRoleManagement");
	request.setAttribute("subMenu1","roleManagement");
	request.setAttribute("subMenu2", isAddPage ? "roleAdd" : "roleSearch");
	String title = isAddPage?"Add Role": "Edit Role";
%>
<jsp:include page="../common/layout.jsp" flush="true">
<jsp:param name="title" value="<%=title%>"/> 
	<jsp:param name="body" value="../roles/roleEditBody.jsp" />
</jsp:include> 