<%@page import="domain.constants.DomainRequestTypeConstants"%>
<%@page import="vpn.constants.VpnStateConstants"%>
<%@page import="common.EntityTypeEntityDTO"%>
<%@page import="common.CommonService"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="request.StateRepository"%>
<%@page import="java.util.Set"%>
<%@page import="request.RequestStateActionRepository"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="permission.StateActionDTO"%>
<%@page import="user.UserDTO"%>
<%@page import="permission.PermissionService"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="user.UserRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="permission.ActionStateDTO"%>
<%	
	Logger actionLogger = Logger.getLogger("actionlist_domain_jsp");
%>
<%try{%>
 <div class="panel panel-btcl light">
	<div class="panel-heading">
		<div class="caption">
			<i class="fa fa-tasks"></i> Action List
		</div>
	</div>
	<%-- /.box-header --%>
	<div class="panel-body form">
		<div class="form-body">
			<div class="row">
				<div class="col-md-12">
					<%-- <c:forEach var="action" items="${ actions }">
						<div class="<%=tabRadio%>">
							<label> <input type="radio" class="rdo1"
								name="actionListRadio"
								value="link_action_${action.actionTypeID }"> ${ action.description}
							</label>
						</div>
					</c:forEach> --%>
				<%
					ArrayList<ActionStateDTO> actionStateDTOs = new ArrayList<ActionStateDTO>();
					for (StateActionDTO stateActionDTO1 : stateActionDTOs) {
						String stateName = "";
						if(stateActionDTO1.getStateID() == -1)
						{
							stateName = "General Actions";
						}
						else if(stateActionDTO1.getStateID() == -2)
						{
							stateName = "Basic Actions";
						}
						else
						{
							stateName = StateRepository.getInstance().getStateDTOByStateID(stateActionDTO1.getStateID()).getName();
						}
							%> 
							<h4 class="text-center bold "> <%=stateName %></h4>
							<%
							for (int actionTypeID : stateActionDTO1.getActionTypeIDs()) {
								ActionStateDTO actionStateDTO1 =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
	//							actionStateDTO.setStateID(stateActionDTO.getStateID());
								actionStateDTOs.add(actionStateDTO1);
								%>
							<div class="">
								<label> 
									<input type="radio" class="rdo1" name="actionListRadio" value="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO1.getActionTypeID()%>"> <%=actionStateDTO1.getDescription()%>
								</label>
							</div>							
								<%
							}
							%>
						<div style="padding: 3px"></div>
					<% } %>

			</div>
		</div>
		<hr>
		<div class="row radio-content" id="radioContent">
			<div class="col-md-12">
			<%
				for (StateActionDTO stateActionDTO : stateActionDTOs) { %>
						
				<%
					for (int actionTypeID : stateActionDTO.getActionTypeIDs()) {
						ActionStateDTO actionStateDTO =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
						request.setAttribute("action", actionStateDTO);
						if(actionStateDTO.getType() == null){
							actionStateDTO.setType("");
						}
						actionLogger.debug("actionStateDTO.getType() " + actionStateDTO.getType());
						actionLogger.debug("actionStateDTO.getType() " +actionStateDTO.getActionTypeID());
						%>

					<%if(actionStateDTO.getType().equals("updateApp")){%>
						<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<h4>${ action.description}</h4>
							<div class="form-group col-md-offset-5 col-md-2">
								<a class="btn green-jungle  btn-block uppercase" href="<%=editActionName%>">Edit</a>
							</div>
						</div>
					<%}else if(actionStateDTO.getType().equals("selectPortReq")){%>
						
					<%}else if(actionStateDTO.getType().equals("verifyApp")){ %>
						<!-- div class="form-group ">
							<a class=" pull-right uppercase" href="<%=detailsActionName%>">Verify Application</a>
						</div-->
						<% 
						
							CommonRequestDTO commonRequestDTO = new CommonRequestDTO((Integer) request.getAttribute("entityTypeID"),
								(Long) request.getAttribute("entityID"),
								actionTypeID,
								stateActionDTO.getStateID(),
								(Long) request.getAttribute("clientID"));
						
						
							
							String [][] searchFieldInfo=DomainRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
							
							request.setAttribute("commonRequestDTO", commonRequestDTO);
							request.setAttribute("stateActionDTO", stateActionDTO);
							request.setAttribute("searchFieldInfo", searchFieldInfo);
						%>
						<div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div>
					<%}else if(actionStateDTO.getType().equals("rejectApp")){%>
						<% 
							CommonRequestDTO commonRequestDTO = new CommonRequestDTO((Integer) request.getAttribute("entityTypeID"),
								(Long) request.getAttribute("entityID"),
								actionTypeID,
								stateActionDTO.getStateID(),
								(Long) request.getAttribute("clientID"));
						
						
							String [][] searchFieldInfo=DomainRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
							
							request.setAttribute("commonRequestDTO", commonRequestDTO);
							request.setAttribute("stateActionDTO", stateActionDTO);
							request.setAttribute("searchFieldInfo", searchFieldInfo);
						%>
						<div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div>
					<%}else if(actionStateDTO.getType().equals("frNearLoopReq")){%>								

				 <%}else if(actionStateDTO.getType().equals("frFarLoopReq")){	%>						
	
					
					<%}else if(actionStateDTO.getType().equals("conOwnershipChange")){%>
						<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<h4>${ action.description} </h4>
							<form role='form' method='post' action='${context}CommonAction.do?mode=${action.type}' class="formSubmit">
								<div class='box-body'>
									<input type='hidden' name='requestTypeID' value='${action.actionTypeID}'> <input type="hidden" name="actionName" value="" class="actionName">
									<input type='hidden' name='entityTypeID' value='${entityTypeID }'> <input type='hidden' name='entityID' value='${entityID }'> <input
										type='hidden' name='clientID' value='${clientID }'>
									<div class="form-group">
										<textarea name='description' class="form-control spinner" placeholder="Description" rows="2"></textarea>
									</div>
								</div>
								<div class="form-group col-md-offset-5 col-md-2">
									<button type='submit' class='btn green-jungle  btn-block uppercase'>Submit</button>
								</div>
							</form>
						</div>
	
					<%}else{
					    CommonRequestDTO commonRequestDTO = new CommonRequestDTO((Integer) request.getAttribute("entityTypeID"),
						(Long) request.getAttribute("entityID"),
						actionTypeID,
						stateActionDTO.getStateID(),
						(Long) request.getAttribute("clientID"));
					    commonRequestDTO.setReqID(stateActionDTO.getCommonRequestDTO().getReqID());
					    String [][] searchFieldInfo=DomainRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
					    if(searchFieldInfo == null)
					    {
					    	searchFieldInfo = new String[][]{{ "../../includes/elements/description.jsp", "description" }};
					    }
						request.setAttribute("commonRequestDTO", commonRequestDTO);
						request.setAttribute("stateActionDTO", stateActionDTO);
						request.setAttribute("searchFieldInfo", searchFieldInfo);
					%>
						<div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div>					
<%-- 						<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<h4>${ action.description}</h4>
							<form role='form' method='post' action='${context}CommonAction.do?mode=${action.type}' class="formSubmit">
								<div class='box-body'>
									<input type='hidden' name='requestTypeID' value='${action.actionTypeID}'> <input type="hidden" name="actionName" value="" class="actionName">
									<input type='hidden' name='entityTypeID' value='${entityTypeID }'> <input type='hidden' name='entityID' value='${entityID }'> <input
										type='hidden' name='clientID' value='${clientID }'>
									<div class="form-group">
										<textarea name='description' class="form-control spinner" placeholder="Description" rows="2"></textarea>
									</div>
								</div>
								<div class="form-group col-md-offset-5 col-md-2">
									<button type='submit' class='btn green-jungle  btn-block uppercase'>Submit</button>
								</div>
							</form>
						</div> --%>
					<%}%>
				  <%}%>
				<%} %>
			</div>
		</div>
		</div>
	</div>
</div>

<%}catch(Exception ex){ 
	actionLogger.debug("ex", ex);
}%>
<%@include file="../../common/addActionScript.jsp"%>
