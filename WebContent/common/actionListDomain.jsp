<%@page import="domain.DomainService"%>
<%@page import="request.OwnerShipChangeRequest"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="domain.constants.DomainRequestTypeConstants"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.RequestStatus"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
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
					for (StateActionDTO stateActionDTO1 : stateActionDTOs) {
						String stateName = "";
						if(stateActionDTO1.getStateID() <= 0)
						{
							
						}
						else
						{
							stateName = StateRepository.getInstance().getStateDTOByStateID(stateActionDTO1.getStateID()).getName();
					
							CommonRequestDTO bottomRequestDTO = stateActionDTO1.getCommonRequestDTO();	
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
										requestedBy =  userdto.getUsername() + (!userdto.getFullName().isEmpty() ? " ("+userdto.getFullName()+")" : "");
									}
								}
							}
							else
							{
								requestedBy = "System";
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
											requestedTo =  userdto.getUserID() +" ("+userdto.getFullName()+")";
										}
									}
								}						
							}
							ActionStateDTO bottomActionStateDTO = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(bottomRequestDTO.getRequestTypeID());
							int rootReqType = bottomActionStateDTO.getRootActionTypeID();
							ActionStateDTO rootActionStateDTO = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(rootReqType);
							String rootReqTypeStr = rootActionStateDTO.getDescription();
								%> 

								
								
								<div class="table-responsive">
									<table class="table table-bordered table-striped">
										<tbody>
											<tr>
											<th scope="row">Root Request</th>
											<th>
											<%
											String hyperlinkOfRootRequest = new DomainService().generateRequestPreviewURL(bottomRequestDTO);
											if(hyperlinkOfRootRequest == null)
											{%>
												<%=rootReqTypeStr%>	
											<%}
											else
											{%>
												<a href="<%=context%><%=new DomainService().generateRequestPreviewURL(bottomRequestDTO)%>" target="_blank"><%=rootReqTypeStr%></a>
											<%}%>
											
											</th>
											</tr>
											<tr>																								
												<th scope="row">Status</th>
												<td><%=stateName%>&nbsp;
												<%if(bottomRequestDTO.getRequestTypeID()% 100 == 11){
												Long rootReqID = bottomRequestDTO.getRootReqID();
												if(rootReqID == null)
												{
													rootReqID = bottomRequestDTO.getReqID();
												}
												%>												
													<a target="_blank" href="<%=context%>GetPdfBill.do?method=getDomainPdf&reqID=<%=bottomRequestDTO.getReqID()%>">VIEW</a>
												<%}%>																				
												</td>												
											</tr>
											<tr>
												<th scope="row">Time</th>
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
											
											<%
											String typeOfRequest = "Request";
											if((Math.abs(bottomRequestDTO.getRequestTypeID()) % 100) == 11){
												typeOfRequest = "Payment";	
												//bottomRequestDTO.setExpireTime(bottomRequestDTO.getRequestTime() + (15 * 86400000L));
											}%>
											<%if(bottomRequestDTO.getRequestToAccountID() != null && bottomRequestDTO.getRequestToAccountID() > 0){%>
											<tr>
												<th scope="row"><%=typeOfRequest%> Expiration Date</th>
												<td style="color:red"><%=df.format(bottomRequestDTO.getExpireTime())%></td>
											</tr>
											 <%}%>
										</tbody>
									</table>
								</div>
								<jsp:include page="../../common/fileListHelper.jsp" flush="true">
									<jsp:param name="entityTypeID" value='<%=bottomRequestDTO.getRequestTypeID()%>' />
									<jsp:param name="entityID" value='<%=bottomRequestDTO.getReqID() %>' />
								</jsp:include>
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
	                            	sourceCommonRequestDTO.setRequestTypeID(DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.SYSTEM_CANCEL_REQUEST);
	                            	request.setAttribute("commonRequestDTO", stateActionDTO1.getCommonRequestDTO());
	                            %>
	                            
	                            <form action="<%=context%>CommonAction.do?mode=cancel" method="Post" class="formSubmit">
	                            <%@include file="../../common/elements/actionListHiddens.jsp"%>                            
	                            <button  class="btn btn-submit-btcl">Rollback this action</button>
	                            </form>
	                            <%
	                            //break;
	                            }%>                            
							<%}%>
							
							<%
							for (int actionTypeID : stateActionDTO1.getActionTypeIDs()) {
								ActionStateDTO actionStateDTO1 =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
	//							actionStateDTO.setStateID(stateActionDTO.getStateID());
								actionStateDTOs.add(actionStateDTO1);
																
								%>
							<div class="">
								<label> 
									<input type="radio"  name="actionListRadio" value="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO1.getActionTypeID()%>"> <%=actionStateDTO1.getDescription()%>
								</label>
							</div>							
								
						
				<%
					
						ActionStateDTO actionStateDTO =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
						request.setAttribute("action", actionStateDTO);
						if(actionStateDTO.getType() == null){
							actionStateDTO.setType("");
						}
						actionLogger.debug("actionStateDTO.getType() " + actionStateDTO.getType());
						actionLogger.debug("actionStateDTO.getType() " +actionStateDTO.getActionTypeID());
						
						
					 	CommonRequestDTO commonRequestDTO = new CommonRequestDTO((Integer) request.getAttribute("entityTypeID"),
							(Long) request.getAttribute("entityID"),
							actionTypeID,
							stateActionDTO1.getStateID(),
							(Long) request.getAttribute("clientID"));
					 	commonRequestDTO.setReqID(stateActionDTO1.getCommonRequestDTO().getReqID());
					 	actionLogger.debug("stateActionDTO1.getCommonRequestDTO().getRequestTypeID() " + stateActionDTO1.getCommonRequestDTO().getRequestTypeID());
					 	int requestTypeRemainder = (Math.abs(stateActionDTO1.getCommonRequestDTO().getRequestTypeID()) % EntityTypeConstant.MULTIPLIER2);
						if((requestTypeRemainder % EntityTypeConstant.MULTIPLIER2) == 04)
						{
							commonRequestDTO.setRequestToAccountID((Long) request.getAttribute("clientID"));
						}
					 	actionLogger.debug("commonRequestDTO " + commonRequestDTO);
					 	
						String [][] searchFieldInfo=DomainRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
					    if(searchFieldInfo == null)
					    {
					    	searchFieldInfo = new String[][]{{ "../../includes/elements/description.jsp", "description" }};
					    }
						request.setAttribute("commonRequestDTO", commonRequestDTO);
						request.setAttribute("stateActionDTO", stateActionDTO1);
						request.setAttribute("searchFieldInfo", searchFieldInfo);
						
						%>
					<%if(actionStateDTO.getActionTypeID() == DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.UPDATE_APPLICATION){ %>					
						<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<br>
							<a class="btn btn-sm green-jungle  uppercase" href="<%=context+searchFieldInfo[0][0]+"?entityID="+request.getAttribute("entityID")%>"><%=searchFieldInfo[0][1] %></a>
						</div>
					<%
					continue;
					
					}else{
						actionLogger.debug("in else part");					    

					%>
					<%}%>
					
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