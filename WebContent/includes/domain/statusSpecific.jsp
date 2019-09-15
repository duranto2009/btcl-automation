<%@page import="request.RequestActionStateRepository"%>
<%@page import="request.StateDTO"%>
<%@page import="request.RequestStatus"%>
<%@page import="common.ModuleConstants"%>
<%@page import="request.StateRepository"%>
<%@page import="common.UtilityConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@ page language="java"%>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String statusSpecific = (String) session.getAttribute("statusSpecific");
	if (statusSpecific == null) {
		statusSpecific = "";
	}
	System.out.println("statusSpecific=" + statusSpecific);
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Specific Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
			<select class="form-control select2" size="1" name="statusSpecific" >
			<option  value="" <%if (statusSpecific.equals("") ){%> selected='selected' <%}%>>All</option>
			<%
			ArrayList<Integer> stateList = new ArrayList<Integer>(StateRepository.getInstance().getStatusListByModuleIDAndActivationStatus(ModuleConstants.Module_ID_DOMAIN, EntityTypeConstant.STATUS_ACTIVE));
			System.out.println("stateList " + stateList);
			List<Integer> stateList2 = StateRepository.getInstance().getStatusListByModuleIDAndActivationStatus(ModuleConstants.Module_ID_DOMAIN, EntityTypeConstant.STATUS_SEMI_ACTIVE);
			//System.out.println("stateList2 " + stateList2);
			stateList.addAll(stateList2);			
			stateList2 = StateRepository.getInstance().getStatusListByModuleIDAndActivationStatus(ModuleConstants.Module_ID_DOMAIN, EntityTypeConstant.STATUS_NOT_ACTIVE);
			//System.out.println("stateList2 " + stateList2);
			stateList.addAll(stateList2);
			%>
			<%			
			for(Integer stateID: stateList)
			{
				StateDTO stateDTO = StateRepository.getInstance().getStateDTOByStateID(stateID);
				String rootReqTypeStr = "";
				if(stateDTO.getRootRequestTypeID() > 0)
				{
					rootReqTypeStr = "(" + RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(stateDTO.getRootRequestTypeID()).getDescription() + ")";
				}
			%>		 	
			<option value="<%=stateDTO.getId()%>" <%if (statusSpecific.equals(""+stateDTO.getId()) ){%> selected <%}%>><%=stateDTO.getName()%> <%=rootReqTypeStr%></option>
			<%}%>
		</select>
	</div>
</div>