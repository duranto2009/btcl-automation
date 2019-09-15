<%@page import="note.NoteTypeConstants"%>
<%@page import="common.UtilityConstants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="vpn.constants.*"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page import="java.util.ArrayList,java.util.*" %>
<%String serviceTypeStr = (String)session.getAttribute("arRequestTypeID");
int serviceType = UtilityConstants.GLOBAL_DEFAULT_VAL;
if(StringUtils.isNotBlank(serviceTypeStr)){
	serviceType= Integer.parseInt(serviceTypeStr);
}else{
	serviceTypeStr=UtilityConstants.GLOBAL_SELECT_ALL_OPTION_VAL;
}
String moduleIdStr=request.getParameter("moduleID");
String entityTypeIdStr=request.getParameter("entityTypeID");

String[] checkList = { "Advice Note", "External FR", "Internal FR", "Negative FR" };
%>
<div class="form-group">
	<label for="inputPassword" class="control-label col-md-4 col-sm-4 col-xs-4">Request Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control select2" size="1" name="arRequestTypeID" >
		 	<option  value="<%=UtilityConstants.GLOBAL_SELECT_ALL_OPTION_VAL %>" <%if (serviceTypeStr.equals(UtilityConstants.GLOBAL_SELECT_ALL_OPTION_VAL) ){%> selected='selected' <%}%>>All</option>
			 <%
			 CommonRequestDTO comDTO = new CommonRequestDTO();
			 try{
				 comDTO.setModuleID(Integer.parseInt(moduleIdStr));
			 }catch(Exception ex){
				 
			 }
			 try{
				 comDTO.setEntityTypeID(Integer.parseInt(entityTypeIdStr));
			 }catch(Exception ex){
				 
			 }
			 comDTO.setRootActionsOnly(false);
			 comDTO.setVisibleOnly(false);
			 LoginDTO loginDTO = (LoginDTO)request.getAttribute("loginDTO");
			 Set<Integer> actionTypeIDSet = RequestActionStateRepository.getInstance().getAllActions(comDTO, loginDTO);
			 %>
			 
			<%for(int reqType:actionTypeIDSet ){ 
				String rootReqTypeStr = "";
				int rootReqType = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(reqType).getRootActionTypeID();
				rootReqTypeStr += RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(rootReqType).getDescription();
				String noteName = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(reqType).getDescription();
				
				for(Map.Entry<Integer, String> entry : NoteTypeConstants.mapOfNoteTypeToNoteTypeIdExtension.entrySet()){
					if(noteName.contains(entry.getValue())){
			%>
						<option value="<%=reqType%>" <%=((serviceType== reqType)?"selected=\"selected\"":"")%>><%=(noteName +" ("+rootReqTypeStr +")")%></option> 
			<%			break;
					}
				}
			}%>
		</select>
	</div>
</div>

