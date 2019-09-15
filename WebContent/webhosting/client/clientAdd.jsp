<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="java.util.*"%>
<%@ include file="../../includes/checkLogin.jsp" %>


<%
boolean hasPermission = false;
int permission;
if(loginDTO.getUserID()>0){
	System.err.println(loginDTO.getMenuPermission(PermissionConstants.WEBHOSTING_CLIENT));
    if((loginDTO.getMenuPermission(PermissionConstants.WEBHOSTING_CLIENT) !=-1) &&(loginDTO.getMenuPermission(PermissionConstants.WEBHOSTING_CLIENT_ADD) == PermissionConstants.PERMISSION_FULL)){
        hasPermission=true;
    }
    permission = loginDTO.getMenuPermission( PermissionConstants.WEBHOSTING_CLIENT_ADD ) ;
}
if(loginDTO.getAccountID()>0){
	List<HashMap<String, String>> moduleListByClientID = AllClientRepository.getInstance().getModuleListByClientID(loginDTO.getAccountID());
	hasPermission=true;
	for(HashMap<String, String> module : moduleListByClientID){
		if(module.containsKey(ModuleConstants.Module_ID_WEBHOSTING+"")){
			hasPermission=false;
			break;
		}
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
  request.setAttribute("menu","webHostingMenu");
  request.setAttribute("subMenu1","webHostingClientMenu");
  request.setAttribute("subMenu2","addWebHostingClientMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Add Client" /> 
	<jsp:param name="body" value="../webhosting/client/clientAddBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="mobileNumberHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 