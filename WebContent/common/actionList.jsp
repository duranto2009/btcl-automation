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
	Logger logger1 = Logger.getLogger("actionlist_jsp");
%>
<%
String tabRadio="col-md-12";
String tabSubmit="col-md-12";
if("true".equals(request.getParameter("view-tab"))){
	tabRadio="col-md-4";
	tabSubmit="col-md-offset-4 col-md-4";
	
%>
<style>
.btn-block{
	width: 200px;
}
</style>
<% } %>

<script type="text/javascript">
    $(document).ready(function() {
	var pieces = window.location.href.split("/");
	var name = pieces[pieces.length - 1];
	$('input[type=radio]').attr('checked', false);
	$(".formSubmit").submit(function() {
	    $(this).find('.actionName').val(name);
	    return true;
	});

    })
</script>
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
						try{
						    stateName = StateRepository.getInstance().getStateDTOByStateID(stateActionDTO1.getStateID()).getName();
							}catch(Exception ex){
								logger1.debug("Error view page : "+ ex);
								stateName = "General Actions";
							}
							%> 
							<h4 class="text-center bold "> <%=stateName %></h4>
				<%
						for (int actionTypeID : stateActionDTO1.getActionTypeIDs()) {
							ActionStateDTO actionStateDTO1 =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
//							actionStateDTO.setStateID(stateActionDTO.getStateID());
							actionStateDTOs.add(actionStateDTO1);
							%>
						<div class="<%=tabRadio%>">
							<label> <input type="radio" class="rdo1"
								name="actionListRadio"
								value="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO1.getActionTypeID()%>"> <%=actionStateDTO1.getDescription()%>
							</label>
						</div>							
							<%
						}
						
						%>
				<div style="padding: 3px"></div>
				<%
					}					
					%>

			</div>
		</div>
		<hr>
		<div class="row radio-content" id="radioContent">
			<div class="col-md-12">
				<%
					
					for (StateActionDTO stateActionDTO : stateActionDTOs) {
						%>
				<%
						for (int actionTypeID : stateActionDTO.getActionTypeIDs()) {
							ActionStateDTO actionStateDTO =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
							request.setAttribute("action", actionStateDTO);
							if(actionStateDTO.getType() == null)
							{
								actionStateDTO.setType("");
							}
							logger1.debug("actionStateDTO.getType() " + actionStateDTO.getType());
							%>


				<%if(actionStateDTO.getType().equals("updateApp")){						
						%>

				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
					<h4>${ action.description}</h4>
					<div class="form-group col-md-offset-5 col-md-2">
					<a class="btn green-jungle  btn-block uppercase" href="<%=editActionName%>">Edit</a>
					</div>
				</div>
				<%}else if(actionStateDTO.getType().equals("verifyApp")){%>
				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
					<h4>${ action.description}</h4>
					<div class="form-group col-md-offset-5 col-md-3">
						<a class="btn green-jungle  btn-block uppercase" href="<%=detailsActionName%>">Verify Application</a>
					</div>
					<form role='form' method='post' action='${context}CommonAction.do?mode=${action.type}' class="formSubmit">
						<div class='box-body'>
							<input type='hidden' name='requestTypeID' value='${action.actionTypeID}'> <input type="hidden" name="actionName" value="" class="actionName">
							<input type='hidden' name='entityTypeID' value='${entityTypeID }'> <input type='hidden' name='entityID' value='${entityID }'> <input
								type='hidden' name='clientID' value='${clientID }'>
							<div class="form-group">
								<textarea name='description' class="form-control spinner" placeholder="Description" rows="2"></textarea>
							</div>
						</div>
						<div class="form-group col-md-offset-5 col-md-3">
							<button type='submit' class='btn green-jungle  btn-block uppercase'>Confirm Varification</button>
						</div>
					</form>
				</div>
				<%}else if(actionStateDTO.getType().equals("rejectApp")){%>
				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
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
				</div>
				<%}else if(actionStateDTO.getType().equals("frNearLoopReq")){							
							%>
				<c:set var="entityTypeID" value="${entityTypeID }"></c:set>
				<c:set var="entityID" value="${entityID }"></c:set>
				<%

							%>
				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
					<h4>${ action.description}</h4>
					<form role='form' method='post' action='${context}CommonAction.do?mode=${action.type}' class="formSubmit">
						<div class='box-body'>
							<%-- <input type='hidden' name='requestTypeID'
												value='${action.actionTypeID}'> <input type="hidden"
												name="actionName" value="" class="actionName"> <input
												type='hidden' name='entityTypeID' value='${entityTypeID }'> --%>
							<!-- Make it dynamic -->
							<%
							
							CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
							/* commonRequestDTO.setEntityTypeID((Integer) pageContext.getAttribute("entityTypeID")); */
							/*
							* Make it dynamic
							*/

							commonRequestDTO.setEntityTypeID((Integer) pageContext.getAttribute("entityTypeID"));
							commonRequestDTO.setEntityID((Long) pageContext.getAttribute("entityID"));
							
							CommonService comService = new CommonService();
/* 							CommonRequestDTO comDTOLocal = new CommonRequestDTO();
							comDTOLocal.setEntityID(id);
							comDTOLocal.setEntityTypeID(Integer.parseInt((String)request.getAttribute("entityTypeID"))); */
							logger1.debug("sending parameter " + commonRequestDTO);
							ArrayList<EntityTypeEntityDTO> entityTypeEntityDTOs = comService.getRelatedEntity(commonRequestDTO, true);
							logger1.debug("received entityTypeEntityDTOs " + entityTypeEntityDTOs);
							CommonRequestDTO nearEndCommonRequestDTO = commonRequestDTO;
							for (EntityTypeEntityDTO entityTypeEntityDTO : entityTypeEntityDTOs) {
								logger1.debug("*entityTypeEntityDTO= " + entityTypeEntityDTO);
								if (entityTypeEntityDTO.getEntityTypeID() == EntityTypeConstant.VPN_LINK_NEAR_END) {
									nearEndCommonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK_NEAR_END);
									nearEndCommonRequestDTO.setEntityID(entityTypeEntityDTO.getEntityID());
									break;
								}
							}
							
							commonRequestDTO.setEntityID(nearEndCommonRequestDTO.getEntityID());
							commonRequestDTO.setEntityTypeID(nearEndCommonRequestDTO.getEntityTypeID());
							commonRequestDTO.setRequestTypeID(actionTypeID);
							commonRequestDTO.setState(stateActionDTO.getStateID());
							PermissionService permissionService = new PermissionService();
							ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(commonRequestDTO);
							request.setAttribute("adminList",userDTOs);
							
							logger1.debug("nearEndCommonRequestDTO " + nearEndCommonRequestDTO);
							%>
							<input type='hidden' name='requestTypeID' value='${action.actionTypeID}'> <input type="hidden" name="actionName" value="" class="actionName">
							<input type='hidden' name='entityTypeID' value='<%=nearEndCommonRequestDTO.getEntityTypeID()%>'> <input type='hidden' name='entityID' value='<%=nearEndCommonRequestDTO.getEntityID()%>'>
							<input type='hidden' name='clientID' value='${clientID }'>
							<%-- <input type='hidden' name='state' value='<%=stateActionDTO.getStateID()%>'> --%>
							<div class="form-group">
								<select class="form-control select2" name="requestToAccountID" style="width: 100%">
									<c:forEach var="adminDTO" items="${adminList}">
										<option value='${-adminDTO.userID}'>${adminDTO.username}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<textarea name='description' class="form-control spinner" placeholder="Description" rows="2"></textarea>
							</div>
						</div>
						<div class="form-group col-md-offset-5 col-md-2">
							<button type='submit' class='btn green-jungle  btn-block uppercase '>Submit</button>
						</div>
					</form>
				</div>
				<%}else if(actionStateDTO.getType().equals("frFarLoopReq")){							
							%>
				<c:set var="entityTypeID" value="${entityTypeID }"></c:set>
				<c:set var="entityID" value="${entityID }"></c:set>
				<%


							%>
				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
					<h4>${ action.description}</h4>
					<form role='form' method='post' action='${context}CommonAction.do?mode=${action.type}' class="formSubmit">
						<div class='box-body'>
							<%-- <input type='hidden' name='requestTypeID'
												value='${action.actionTypeID}'> <input type="hidden"
												name="actionName" value="" class="actionName"> <input
												type='hidden' name='entityTypeID' value='${entityTypeID }'> --%>
							<!-- Make it dynamic -->
							<% 
							CommonService comService = new CommonService();
							CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
							/* commonRequestDTO.setEntityTypeID((Integer) pageContext.getAttribute("entityTypeID")); */
							/*
							* Make it dynamic
							*/
							commonRequestDTO.setEntityTypeID((Integer) pageContext.getAttribute("entityTypeID"));
							commonRequestDTO.setEntityID((Long) pageContext.getAttribute("entityID"));
							
							ArrayList<EntityTypeEntityDTO> entityTypeEntityDTOs = comService.getRelatedEntity(commonRequestDTO, true);
							CommonRequestDTO farEndCommonRequestDTO = commonRequestDTO;
							for (EntityTypeEntityDTO entityTypeEntityDTO : entityTypeEntityDTOs) {
								if (entityTypeEntityDTO.getEntityTypeID() == EntityTypeConstant.VPN_LINK_FAR_END) {
									farEndCommonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK_FAR_END);
									farEndCommonRequestDTO.setEntityID(entityTypeEntityDTO.getEntityID());
									break;
								}
							}		
							
							commonRequestDTO.setEntityID(farEndCommonRequestDTO.getEntityID());
							commonRequestDTO.setEntityTypeID(farEndCommonRequestDTO.getEntityTypeID());
							commonRequestDTO.setRequestTypeID(actionTypeID);
							commonRequestDTO.setState(stateActionDTO.getStateID());
							PermissionService permissionService = new PermissionService();
							ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(commonRequestDTO);
							request.setAttribute("adminList",userDTOs);
							%>
							<input type='hidden' name='requestTypeID' value='${action.actionTypeID}'> <input type="hidden" name="actionName" value="" class="actionName">
							<input type='hidden' name='entityTypeID' value='<%=EntityTypeConstant.VPN_LINK_FAR_END%>'> <input type='hidden' name='entityID' value='<%=farEndCommonRequestDTO.getEntityID()%>'>
							<input type='hidden' name='clientID' value='${clientID }'>
							<%-- <input type='hidden' name='state' value='<%=stateActionDTO.getStateID()%>'> --%>
							<div class="form-group">
								<select class="form-control select2" name="requestToAccountID" style="width: 100%">
									<c:forEach var="adminDTO" items="${adminList}">
										<option value='${-adminDTO.userID}'>${adminDTO.username}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<textarea name='description' class="form-control spinner" placeholder="Description" rows="2"></textarea>
							</div>
						</div>
						<div class="form-group col-md-offset-5 col-md-2">
							<button type='submit' class='btn green-jungle  btn-block uppercase'>Submit</button>
						</div>
					</form>
				</div>
				<%}else if(actionStateDTO.getType().equals("frNearLoopResponse")){							
							%>
				<c:set var="entityTypeID" value="${entityTypeID }"></c:set>
				<c:set var="entityID" value="${entityID }"></c:set>
				<%
							CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
							/* commonRequestDTO.setEntityTypeID((Integer) pageContext.getAttribute("entityTypeID")); */
							/*
							* Make it dynamic
							*/
							commonRequestDTO.setEntityTypeID((Integer) EntityTypeConstant.VPN_LINK_NEAR_END);
							commonRequestDTO.setEntityID((Long) pageContext.getAttribute("entityID"));
							commonRequestDTO.setRequestTypeID(actionTypeID);
							commonRequestDTO.setState(stateActionDTO.getStateID());
							PermissionService permissionService = new PermissionService();
							ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(commonRequestDTO);
							request.setAttribute("adminList",userDTOs);
							%>
				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
					<h4>${ action.description}</h4>
					<form role='form' method='post' action='${context}CommonAction.do?mode=${action.type}' class="formSubmit">
						<div class='box-body'>
							<%-- <input type='hidden' name='requestTypeID'
												value='${action.actionTypeID}'> <input type="hidden"
												name="actionName" value="" class="actionName"> <input
												type='hidden' name='entityTypeID' value='${entityTypeID }'> --%>
							<!-- Make it dynamic -->
							<input type='hidden' name='requestTypeID' value='${action.actionTypeID}'> <input type="hidden" name="actionName" value="" class="actionName">
							<input type='hidden' name='entityTypeID' value='<%=EntityTypeConstant.VPN_LINK_NEAR_END%>'> <input type='hidden' name='entityID' value='${entityID }'>
							<input type='hidden' name='clientID' value='${clientID }'>
							<%-- <input type='hidden' name='state' value='<%=stateActionDTO.getStateID()%>'> --%>
							
							<div class="form-group">
								<label for="dist" >Total Local loop distance</label>
								<input type="text" class="form-control" name="" value=""/>
							</div>
							<div class="form-group">
								<label for="dist" >Distance Covered By BTCL</label>
								<input type="text" class="form-control" name="" value=""/>
							</div>
							<div class="form-group">
								<label for="dist" >Distance Covered By O/C</label>
								<input type="text" class="form-control" name="" value=""/>
							</div>
							<div class="form-group">
								<label for="dist" >Distance Covered By Customer</label>
								<input type="text" class="form-control" name="" value=""/>
							</div>														
							<div class="form-group">
								<textarea name='description' class="form-control spinner" placeholder="Description" rows="2"></textarea>
							</div>
						</div>
						<div class="form-group col-md-offset-5 col-md-2">
							<button type='submit' class='btn green-jungle  btn-block uppercase '>Submit</button>
						</div>
					</form>
				</div>				
				<%}else if(actionStateDTO.getType().equals("frFarLoopResponse")){							
							%>
				<c:set var="entityTypeID" value="${entityTypeID }"></c:set>
				<c:set var="entityID" value="${entityID }"></c:set>
				<%
							CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
							/* commonRequestDTO.setEntityTypeID((Integer) pageContext.getAttribute("entityTypeID")); */
							/*
							* Make it dynamic
							*/
							commonRequestDTO.setEntityTypeID((Integer) EntityTypeConstant.VPN_LINK_FAR_END);
							commonRequestDTO.setEntityID((Long) pageContext.getAttribute("entityID"));
							commonRequestDTO.setRequestTypeID(actionTypeID);
							commonRequestDTO.setState(stateActionDTO.getStateID());
							PermissionService permissionService = new PermissionService();
							ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(commonRequestDTO);
							request.setAttribute("adminList",userDTOs);
							%>
				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
					<h4>${ action.description}</h4>
					<form role='form' method='post' action='${context}CommonAction.do?mode=${action.type}' class="formSubmit">
						<div class='box-body'>
							<%-- <input type='hidden' name='requestTypeID'
												value='${action.actionTypeID}'> <input type="hidden"
												name="actionName" value="" class="actionName"> <input
												type='hidden' name='entityTypeID' value='${entityTypeID }'> --%>
							<!-- Make it dynamic -->
							<input type='hidden' name='requestTypeID' value='${action.actionTypeID}'> <input type="hidden" name="actionName" value="" class="actionName">
							<input type='hidden' name='entityTypeID' value='<%=EntityTypeConstant.VPN_LINK_FAR_END%>'> <input type='hidden' name='entityID' value='${entityID }'>
							<input type='hidden' name='clientID' value='${clientID }'>
							<%-- <input type='hidden' name='state' value='<%=stateActionDTO.getStateID()%>'> --%>
							
							<div class="form-group">
								<label for="dist" >Total Local loop distance</label>
								<input type="text" class="form-control" name="" value=""/>
							</div>
							<div class="form-group">
								<label for="dist" >Distance Covered By BTCL</label>
								<input type="text" class="form-control" name="" value=""/>
							</div>
							<div class="form-group">
								<label for="dist" >Distance Covered By O/C</label>
								<input type="text" class="form-control" name="" value=""/>
							</div>
							<div class="form-group">
								<label for="dist" >Distance Covered By Customer</label>
								<input type="text" class="form-control" name="" value=""/>
							</div>														
							<div class="form-group">
								<textarea name='description' class="form-control spinner" placeholder="Description" rows="2"></textarea>
							</div>
						</div>
						<div class="form-group col-md-offset-5 col-md-2">
							<button type='submit' class='btn green-jungle  btn-block uppercase '>Submit</button>
						</div>
					</form>
				</div>								
				<%}else if(actionStateDTO.getType().equals("conOwnershipChange")){%>
				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
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
				</div>

				<%}else{%>
				<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
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
				</div>
				<%}%>
				<%}%>
				<%} %>
			</div>
		</div>
		</div>
	</div>
</div>

<%}catch(Exception ex){ 
logger1.debug("ex", ex);
}%>
<%@include file="../../common/addActionScript.jsp"%>
