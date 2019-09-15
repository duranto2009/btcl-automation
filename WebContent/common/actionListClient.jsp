<%@page import="common.ClientDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="login.LoginDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="permission.ActionStateDTO"%>
<%@page import="permission.PermissionService"%>
<%@page import="permission.StateActionDTO"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="request.RequestStatus"%>
<%@page import="request.StateRepository"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="user.UserDTO"%>
<%@page import="user.UserRepository"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%
	Logger actionLogger = Logger.getLogger("actionlist_jsp");
	PermissionService permissionService = new PermissionService();
%>
<%try{%>
			
			<div class="row">
				<div class="col-md-12">
					<h3 >Actions</h3>
				<%
				
/* 					int entityTypeIDActionScript = (Integer)(request.getAttribute("entityTypeID"));
					String actionName = EntityTypeConstant.entityStrutsActionMapForEdit.get(entityTypeIDActionScript); */
					ArrayList<ActionStateDTO> actionStateDTOs = new ArrayList<ActionStateDTO>();
					actionLogger.debug("stateActionDTOs " + stateActionDTOs);
					for (StateActionDTO stateActionDTO1 : stateActionDTOs) {
						String stateName = "";
						if(stateActionDTO1.getStateID() <= 0)
						{							
						}						
						else
						{
							stateName = StateRepository.getInstance().getStateDTOByStateID(stateActionDTO1.getStateID()).getName();
							actionLogger.debug("stateName " + stateName);
							CommonRequestDTO bottomRequestDTO = stateActionDTO1.getCommonRequestDTO();
							actionLogger.debug("bottomRequestDTO " + bottomRequestDTO);
							ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(bottomRequestDTO.getClientID());
							if (clientDTO == null)
								continue;
							String clientName = clientDTO.getLoginName();
							Date requestDate = new Date(bottomRequestDTO.getRequestTime());
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
							String requestDateString = df.format(requestDate).toString();
							int status = bottomRequestDTO.getCompletionStatus();
							if(loginDTO.getAccountID() > 0){
								if(status == RequestStatus.SEMI_PROCESSED)
									status = RequestStatus.PENDING;
							
							} 
							UserDTO userdto = null;
							String requestedBy = "";
							logger.debug("bottomRequestDTO.getRequestByAccountID() " + bottomRequestDTO.getRequestByAccountID());
							if(bottomRequestDTO.getRequestByAccountID() > 0)
							{
								clientDTO = AllClientRepository.getInstance().getClientByClientID(bottomRequestDTO.getRequestByAccountID());
								if (clientDTO != null)
								{
									requestedBy = clientDTO.getLoginName();
								}
							    
							}						
							else if(bottomRequestDTO.getRequestByAccountID() < 0)
							{
								userdto = UserRepository.getInstance().getUserDTOByUserID(Math.abs(bottomRequestDTO.getRequestByAccountID()));
								if(userdto != null)
								{
									if(loginDTO.getAccountID() > 0)
									{
										requestedBy = "System";
									}
									else
									{
										requestedBy = userdto.getUsername();
									}
									
								}
							}
							else
							{
								requestedBy = "system";
							}
							String requestedTo = "";
							if(bottomRequestDTO.getRequestToAccountID() != null)
							{
								if(bottomRequestDTO.getRequestToAccountID() > 0)
								{
									clientDTO = AllClientRepository.getInstance().getClientByClientID(bottomRequestDTO.getRequestToAccountID());
									if (clientDTO != null)
									{
										requestedTo = clientDTO.getLoginName();
									}
								    
								}
								else if(bottomRequestDTO.getRequestToAccountID() < 0)
								{
									userdto = UserRepository.getInstance().getUserDTOByUserID(Math.abs(bottomRequestDTO.getRequestToAccountID()));
									if(userdto != null)
									{
										if(loginDTO.getAccountID() > 0)
										{
											requestedTo = "System";
										}
										else
										{
											requestedTo = userdto.getUsername();
										}
									}
								}						
							}
								%> 
								
								
							<div class="table-responsive">
								<table class="table table-bordered table-striped">
									<tbody>
										<%-- <tr>
											<th scope="row">Status</th>
											<td><%=stateName %></td>
										</tr> --%>
										<tr>
											<th width="25%" scope="row">Time</th>
											<td><%=requestDateString%></td>
										</tr>
										<tr>
											<th scope="row">Requested From</th>
											<td><%=requestedBy%></td>
										</tr>
										<%if(requestedTo.length() > 0){%>
										<tr>
											<th scope="row"> Requested To</th>
											<td><%=requestedTo%></td>
										</tr>
										<%} %>
										<tr>
											<th scope="row">Description</th>
											<td><%=bottomRequestDTO.getDescription()%></td>
										</tr> 
									</tbody>
								</table>
							</div>
							
	                            <%
	                            boolean samePerson = false;
	                            boolean revertable = false;
	                            if(loginDTO.getUserID() > 0)
								{
									if(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID() == -loginDTO.getUserID())
									{
										samePerson = true;
									}
								}
	                            else if(loginDTO.getAccountID() > 0)
	                            {
	                            	if(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID() == loginDTO.getAccountID())
	                            	{
	                            		samePerson = true;
	                            	}
	                            }
	                            %>
	                            <%
	                            
	                            if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(bottomRequestDTO.getRequestTypeID()).isRevertable())
	                            {
	                            	revertable = true;	
	                            }
	                            boolean hadPermissionToTakePreviousAction = false;
	                            if(permissionService.hasPermission(bottomRequestDTO.getRequestTypeID(), loginDTO))
	                            {
	                            	hadPermissionToTakePreviousAction = true;
	                            }
	                            %>
	                            <%
	                            actionLogger.debug("samePerson " + samePerson + " revertable " + revertable);
	                            if(revertable && (samePerson || hadPermissionToTakePreviousAction)){
	                            	CommonRequestDTO sourceCommonRequestDTO = stateActionDTO1.getCommonRequestDTO();
	                            	int moduleIDLocal = sourceCommonRequestDTO.getEntityTypeID() / EntityTypeConstant.MULTIPLIER2;
	                            	int rollbackRequestTypeID = -(moduleIDLocal * ModuleConstants.MULTIPLIER + 199);
	                            	sourceCommonRequestDTO.setRequestTypeID(rollbackRequestTypeID);
	                            	//sourceCommonRequestDTO.setRequestTypeID(DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.SYSTEM_CANCEL_REQUEST);
	                            	request.setAttribute("commonRequestDTO", stateActionDTO1.getCommonRequestDTO());
	                            %>
	                            
	                            <%--<form action="<%=context%>CommonAction.do?mode=cancel" method="Post" class="formSubmit">--%>
	                            <%--<%@include file="../../common/elements/actionListHiddens.jsp"%>--%>
	                            <%--<button  class="btn btn-submit-btcl">Rollback this action</button>--%>
	                            <%--</form>--%>
	                            <%
	                            //break;
	                            }%>                            
	                           <!--  </div> -->

							<%} %>
							
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
					
						ActionStateDTO actionStateDTO =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
						request.setAttribute("action", actionStateDTO);
						actionLogger.debug("actionStateDTO.getActionTypeID() " +actionStateDTO.getActionTypeID());
						
						int entityTypeIDInActionList = (Integer) request.getAttribute("entityTypeID");
					 	CommonRequestDTO commonRequestDTO = new CommonRequestDTO(entityTypeIDInActionList,
							(Long) request.getAttribute("entityID"),
							actionTypeID,
							stateActionDTO1.getStateID(),
							(Long) request.getAttribute("clientID"));
					 	commonRequestDTO.setReqID(stateActionDTO1.getCommonRequestDTO().getReqID());
					 	actionLogger.debug("stateActionDTO1.getCommonRequestDTO().getRequestTypeID() " + stateActionDTO1.getCommonRequestDTO().getRequestTypeID());
						if((actionStateDTO.getActionTypeID() % EntityTypeConstant.MULTIPLIER2) == 04)//request for correction
						{
							commonRequestDTO.setRequestToAccountID((Long) request.getAttribute("clientID"));
						}						
					 	actionLogger.debug("commonRequestDTO " + commonRequestDTO);
					 	actionLogger.debug("entityTypeIDInActionList " + entityTypeIDInActionList);
						String [][] searchFieldInfo = null;
						if(entityTypeIDInActionList == EntityTypeConstant.VPN_CLIENT)
						{
							searchFieldInfo = VpnRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
						}
//						else if(entityTypeIDInActionList == EntityTypeConstant.DOMAIN_CLIENT)
//						{
//							searchFieldInfo = DomainRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
//						}
					    if(searchFieldInfo == null)
					    {
					    	searchFieldInfo = new String[][]{{ "../../includes/elements/description.jsp", "description" }};
					    }
						request.setAttribute("commonRequestDTO", commonRequestDTO);
						request.setAttribute("stateActionDTO", stateActionDTO1);
						request.setAttribute("searchFieldInfo", searchFieldInfo);
						
						%>
					
					<%
					int actionTypeIDRemainder = Math.abs(actionStateDTO.getActionTypeID()) % 100;
					if( actionTypeIDRemainder == 02 || actionTypeIDRemainder == 06){%>
						<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<br>
							<a class="btn btn-sm green-jungle uppercase" href="<%=editActionName%>">Go to Client Update Page</a>
						</div>
					<%
					continue;
					
					}%>
					
						<div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div>					
					
				  <%}%>
				<%} %>
			</div>
		</div>
		

<%}catch(Exception ex){ 
	actionLogger.debug("ex", ex);
}%>
<%@include file="../../common/addActionScript.jsp"%>
