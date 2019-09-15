<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="java.util.*"%>
<%
boolean hasPermission = false;
int permission;
if(loginDTO.getUserID()>0){
    if((loginDTO.getMenuPermission(PermissionConstants.ADSL_CLIENT) !=-1) &&(loginDTO.getMenuPermission(PermissionConstants.ADSL_CLIENT_ADD) == PermissionConstants.PERMISSION_FULL)){
        hasPermission=true;
    }
    permission = loginDTO.getMenuPermission( PermissionConstants.ADSL_CLIENT_ADD ) ;
}
if(loginDTO.getAccountID()>0){
	List<HashMap<String, String>> moduleListByClientID = AllClientRepository.getInstance().getModuleListByClientID(loginDTO.getAccountID());
	hasPermission=true;
	for(HashMap<String, String> module : moduleListByClientID){
		if(module.containsKey(ModuleConstants.Module_ID_ADSL+"")){
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
  request.setAttribute("menu","adslMenu");
  request.setAttribute("subMenu1","adslClientMenu");
  request.setAttribute("subMenu2","addAdslClientMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Add Client" /> 
	<jsp:param name="body" value="../adsl/client/clientAddBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="mobileNumberHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 