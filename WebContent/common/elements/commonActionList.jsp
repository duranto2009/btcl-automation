<%@page import="user.UserDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="permission.PermissionService"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="file.FileTypeConstants.VPN_CLIENT_ADD"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="request.CommonRequestDTO, permission.ActionStateDTO, permission.StateActionDTO"%>

<%
ActionStateDTO actionStateDTONew = (ActionStateDTO) request.getAttribute("action");
StateActionDTO stateActionDTONew = (StateActionDTO) request.getAttribute("stateActionDTO");
   
PermissionService permissionServiceCommon = new PermissionService();
CommonRequestDTO permissionCommonRequestDTO = new CommonRequestDTO();
if(actionStateDTONew.getNextPreferableActionTypeID() != 0 && actionStateDTONew.isRootAction() == false) {
	permissionCommonRequestDTO.setRequestTypeID(actionStateDTONew.getNextPreferableActionTypeID());
	int nextStateID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionStateDTONew.getActionTypeID()).getNextStateID();
	permissionCommonRequestDTO.setState(nextStateID);
	ArrayList<UserDTO> userDTOs = permissionServiceCommon.getUsersDTOHavingPermission(permissionCommonRequestDTO, loginDTO);
	actionLogger.debug("userDTOs " + userDTOs);
	request.setAttribute("adminList",userDTOs);
}

String [][] searchFieldInfoNew = (String [][]) request.getAttribute("searchFieldInfo");    
%>

<div class="portlet" id="link_action_<%=stateActionDTONew.getStateID()%>_<%=actionStateDTONew.getActionTypeID() %>" style="display: none; padding-bottom: 20px;">
<%
	String currentActionTypeID=actionStateDTONew.getActionTypeID()+"";
	request.setAttribute("fileRequestID",currentActionTypeID);
%>
	<form id="fileupload<%=currentActionTypeID%>" role='form' method='post' action='<%=context%>CommonAction.do?mode=<%=actionStateDTONew.getType() %>' 
	class="formSubmit form-horizontal action-list-form">
		<%@include file="../../common/elements/actionListHiddens.jsp"%>
		<%
		if(searchFieldInfoNew != null && searchFieldInfoNew.length > 0)	{
			for(int i = 0; i < searchFieldInfoNew.length;i++) {
				if (searchFieldInfoNew[i][0].endsWith(".jsp") ) { %>
					<jsp:include page="<%=searchFieldInfoNew[i][0]%>" flush="true"/>
				<%}
			}
		} %>
		<div class="form-group pull-left">
			<div class="col-md-4">
				<button type='submit' class='btn btn-submit-btcl'>Submit</button>
			</div>
		</div>
	</form>
	<br>
</div>
