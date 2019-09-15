<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="java.util.*"%>
<%

int moduleID = Integer.parseInt(request.getParameter("moduleID"));


boolean hasPermission = false;
int permission;
if(loginDTO.getUserID()>0){
	int clientMenu = moduleID == ModuleConstants.Module_ID_LLI ? moduleID*1000 + 100 : moduleID *1000 + 2;
	int clientAddMenu = moduleID == ModuleConstants.Module_ID_LLI ? moduleID*1000 + 101 : moduleID*1000 + 3;
    if((loginDTO.getMenuPermission(clientMenu) !=-1) &&(loginDTO.getMenuPermission(clientAddMenu) == PermissionConstants.PERMISSION_FULL)){
        hasPermission=true;
    }
    permission = loginDTO.getMenuPermission( clientAddMenu ) ;
}
if(loginDTO.getAccountID()>0){
	List<HashMap<String, String>> moduleListByClientID = AllClientRepository.getInstance().getModuleListByClientID(loginDTO.getAccountID());
	hasPermission=true;
	for(HashMap<String, String> module : moduleListByClientID){
		if(module.containsKey(moduleID+"")){
			hasPermission = false;
			break;
		}
	}
}hasPermission=true;
if( !hasPermission ){	
	CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();	
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , false, request );
	response.sendRedirect("../");
	return;
}
%>


<%

switch (moduleID){
case ModuleConstants.Module_ID_DOMAIN:
	request.setAttribute("menu","domainMenu");
	request.setAttribute("subMenu1","domainClientMenu");
	request.setAttribute("subMenu2","addDomainClientMenu"); 
	break;
case ModuleConstants.Module_ID_WEBHOSTING:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_DNSHOSTING:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_COLOCATION:
	request.setAttribute("menu","colocationMenu");
	request.setAttribute("subMenu1","colocationClientMenu");
	request.setAttribute("subMenu2","addColocationClientMenu");
	break;
case ModuleConstants.Module_ID_IPADDRESS:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_VPN:
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnClient");
	request.setAttribute("subMenu2","addVpnClient");
	break;
case ModuleConstants.Module_ID_LLI:
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lliClientMenu");
	request.setAttribute("subMenu2","addLliClientMenu"); 
	break;
case ModuleConstants.Module_ID_IIG:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu");
	break;
case ModuleConstants.Module_ID_ADSL:
	request.setAttribute("menu","adslMenu");
	request.setAttribute("subMenu1","adslClientMenu");
	request.setAttribute("subMenu2","addAdslClientMenu"); 
	break;
case ModuleConstants.Module_ID_NIX:
	request.setAttribute("menu","nixMenu");
	request.setAttribute("subMenu1","nixClientMenu");
	request.setAttribute("subMenu2","addNixClientMenu");
	break;
}
%>
<%--<jsp:param name="body" value="../client-new/admin-client-add.jsp" />--%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Add Client" />


	<jsp:param name="body" value="../client/client/clientAddBody2.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="mobileNumberHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 