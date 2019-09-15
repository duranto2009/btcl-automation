<%@page import="request.RequestUtilService"%>
<%@page import="request.RequestUtilDAO"%>
<%@page import="java.util.List"%>
<%@page import="common.note.CommonNoteService"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="common.CommonDAO"%>
<%@page import="java.util.Iterator"%>
<%@page import="util.SqlGenerator"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="common.note.CommonNote"%>
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
	CommonRequestDTO row = new CommonRequestDTO();
	CommonService commonService = new CommonService();
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
							logger.debug("stateActionDTO1.getStateID() " + stateActionDTO1.getStateID());
							stateName = StateRepository.getInstance().getStateDTOByStateID(stateActionDTO1.getStateID()).getName();
					
							row= stateActionDTO1.getCommonRequestDTO();	
							ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getClientID());
							if (clientDTO == null)
								continue;
							String clientName = clientDTO.getLoginName();
							Date requestDate = new Date(row.getRequestTime());
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
							String requestDateString = df.format(requestDate).toString();
							int status = row.getCompletionStatus();
							if(loginDTO.getAccountID() > 0){
								if(status == RequestStatus.SEMI_PROCESSED)
									status = RequestStatus.PENDING;
							
							} 
							UserDTO userdto = null;
							String requestedBy = "";
							logger.debug("row.getRequestByAccountID() " + row.getRequestByAccountID());
							if(row.getRequestByAccountID() > 0)
							{
								clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getRequestByAccountID());
								if (clientDTO != null)
								{
									requestedBy = clientDTO.getLoginName();
								}
							    
							}						
							else if(row.getRequestByAccountID() < 0)
							{
								userdto = UserRepository.getInstance().getUserDTOByUserID(Math.abs(row.getRequestByAccountID()));
								if(userdto != null)
								{
									String fullName = userdto.getFullName();
									if(!fullName.isEmpty())
									{
										fullName = " ("+fullName+")";
									}
									requestedBy =  userdto.getUsername() + fullName;
								}
							}
							else
							{
								requestedBy = "system";
							}
							String requestedTo = "";
							if(row.getRequestToAccountID() != null)
							{
								if(row.getRequestToAccountID() > 0)
								{
									clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getRequestToAccountID());
									if (clientDTO != null)
									{
										requestedTo = clientDTO.getLoginName();
									}
								    
								}
								else if(row.getRequestToAccountID() < 0)
								{
									userdto = UserRepository.getInstance().getUserDTOByUserID(Math.abs(row.getRequestToAccountID()));
									if(userdto != null)
									{
										String fullName = userdto.getFullName();
										if(!fullName.isEmpty())
										{
											fullName = " ("+fullName+")";
										}
										requestedTo = userdto.getUsername() + fullName;
									}
								}						
							}
							ActionStateDTO bottomActionStateDTO = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(row.getRequestTypeID());
							int rootReqType = bottomActionStateDTO.getRootActionTypeID();
							String rootReqTypeStr = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(rootReqType).getDescription();
							
							
							String adviceNoteLink = null;
							if( row.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE ){
								
								long entityTypeId = row.getEntityTypeID();
								long entityId = row.getEntityID();
								
								CommonNote adviceNote = CommonNoteService.getNote( entityTypeId, entityId, row.getReqID() );
								
								if( adviceNote != null )
									adviceNoteLink = request.getContextPath() + "/common/note/noteView.jsp?id=" + adviceNote.getId();
							}
							
								%> 
								
								
								<div class="table-responsive">
									<table class="table table-bordered table-striped">
										<tbody>
											<tr>
											<th scope="row">Root Request</th>
												<th>
													<a href="<%=context%><%=new VpnLinkService().generateRequestPreviewURL(row.getRootReqID()==null?row.getReqID():row.getRootReqID(), row.getRequestTypeID())%>" target="_blank"><%=rootReqTypeStr%></a>
												</th>
											</tr>
											<tr>																								
												<th scope="row">Status</th>
												<td><%=stateName%>&nbsp;
												<%if(row.getRequestTypeID()% 100 == 11){
												Long rootReqID = row.getRootReqID();
												if(rootReqID == null)
												{
													rootReqID = row.getReqID();
												}
												%>
												
												<a target="_blank" href="<%=context%>GetPdfBill.do?method=getPdf&reqID=<%=row.getReqID()%>">VIEW</a>
												<%}%>
												
												<% if( adviceNoteLink != null ){%>  (<a href="<%=adviceNoteLink %>" target = "_blank" >View</a>) <%} %>
																																			
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
												<td><b>
													<%=row.getDescription()%>
													<%
					    								String noteLink = RequestUtilService.getNoteLink( request.getContextPath(), row.getRequestTypeID(), row.getReqID(), row.getEntityTypeID(), row.getEntityID() );
					        						%>
					        						
					        						<% if( noteLink != null ){%><%=noteLink %><%} %>	
					        						</b>
												</td>
											</tr>
											
											<%
											String typeOfRequest = "Request";
											if((Math.abs(row.getRequestTypeID()) % 100) == 11){
												typeOfRequest = "Payment";	
											}%>
											<%if(row.getRequestToAccountID() != null && row.getRequestToAccountID() > 0){%>
											<tr>
												<th scope="row"><%=typeOfRequest%> Expiration Date</th>
												<td style="color:red"><%=df.format(row.getExpireTime())%></td>
											</tr>
											 <%}%>
										</tbody>
									</table>
								</div>
								<%-- <jsp:include page="../../common/fileListHelper.jsp" flush="true">
									<jsp:param name="entityTypeID" value='<%=row.getRequestTypeID()%>' />
									<jsp:param name="entityID" value='<%=row.getReqID() %>' />
								</jsp:include> --%>
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
	                            if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(stateActionDTO1.getCommonRequestDTO().getRequestTypeID()).isRevertable())
	                            {
	                            	revertable = true;
	                            }
	                            %>
	                            <%
	                            actionLogger.debug("samePerson " + samePerson + " revertable " + revertable);
	                            if(samePerson && revertable){
	                            	CommonRequestDTO sourceCommonRequestDTO = stateActionDTO1.getCommonRequestDTO();
	                            	sourceCommonRequestDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_CANCEL_REQUEST);
	                            	request.setAttribute("commonRequestDTO", stateActionDTO1.getCommonRequestDTO());
	                            %>
	                            
	                            <form action="<%=context%>CommonAction.do?mode=cancel" method="Post" class="formSubmit" style="margin-bottom:40px;margin-top: -10px">
	                            <%@include file="../../common/elements/actionListHiddens.jsp"%>          
	                            <div style="text-align: center;">                  
	                            	<button class="btn btn-reset-btcl" >Rollback this action</button>
	                            </div>
	                            </form>
	                            <%}%>                            
							<%}%>
							
							<%
							for (int actionTypeID : stateActionDTO1.getActionTypeIDs()) {
								ActionStateDTO actionStateDTO1 =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
	//							actionStateDTO.setStateID(stateActionDTO.getStateID());
								actionStateDTOs.add(actionStateDTO1);
								
								ActionStateDTO actionStateDTO =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
								String [][] searchFieldInfo=VpnRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
								
								if( actionTypeID == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE ||
										actionTypeID == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_DEMAND_NOTE ||
										actionTypeID == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_MIGRATION_MRC ||
										actionTypeID == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_GENERATE_DEMAND_NOTE){
									
									%> 
						
									

							<div class="">
								<label> 
									<input type="radio" class="rdo1" name="actionListRadio" value="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO1.getActionTypeID()%>"> <%=actionStateDTO1.getDescription()%>
								</label>
							</div>
								<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">										
									<jsp:include page="<%=searchFieldInfo[0][0]%>" flush="true"/> 
								</div>	
									<%
									continue;
								}
																
								%>
							<div class="">
								<label> 
									<input type="radio" class="rdo1" name="actionListRadio" value="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO1.getActionTypeID()%>"> <%=actionStateDTO1.getDescription()%>
								</label>
							</div>							
								
						
				<%
					/* for (int actionTypeID : stateActionDTO.getActionTypeIDs()) { */
						
						request.setAttribute("action", actionStateDTO);
						if(actionStateDTO.getType() == null){
							actionStateDTO.setType("");
						}
						actionLogger.debug("actionStateDTO.getType() " + actionStateDTO.getType());
						actionLogger.debug("actionStateDTO.getType() " +actionStateDTO.getActionTypeID());
						
						
					 	
					 	CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
					 	SqlGenerator.populateObjectFromOtherObject(commonRequestDTO, CommonRequestDTO.class, row, CommonRequestDTO.class);	
					 	commonRequestDTO.setRequestTypeID(actionTypeID);
					 	commonRequestDTO.setRequestToAccountID(null);
					 	actionLogger.debug("stateActionDTO1.getCommonRequestDTO().getRequestTypeID() " + stateActionDTO1.getCommonRequestDTO().getRequestTypeID());
					 	int requestTypeRemainder = (Math.abs(stateActionDTO1.getCommonRequestDTO().getRequestTypeID()) % EntityTypeConstant.MULTIPLIER2);
						if(requestTypeRemainder == 04)
						{
							commonRequestDTO.setRequestToAccountID((Long) request.getAttribute("clientID"));
						}
						else if(VpnRequestTypeConstants.remainderSetOfResponseBack.contains(requestTypeRemainder))
						{
							//commonRequestDTO.setRequestToAccountID(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID());
						}
					 	actionLogger.debug("commonRequestDTO " + commonRequestDTO);
					 	
					    if(searchFieldInfo == null)
					    {
					    	searchFieldInfo = new String[][]{{ "../../includes/elements/description.jsp", "description" }};
					    }
						request.setAttribute("commonRequestDTO", commonRequestDTO);
						request.setAttribute("stateActionDTO", stateActionDTO1);
						request.setAttribute("searchFieldInfo", searchFieldInfo);
						boolean goToInternalFRPage = false;
						String extraParameter = "";
						if(actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE)
						{
							actionLogger.debug("goToInternalFRPage: "+ goToInternalFRPage);
							actionLogger.debug(commonRequestDTO);
						}
						
						%>
					<%if(actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.UPDATE_APPLICATION){ %>					
						<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<br>
							<a class="btn btn-sm green-jungle  uppercase" href="<%=searchFieldInfo[0][0]+"entityID="+request.getAttribute("entityID")+"&entityTypeID="+ EntityTypeConstant.VPN_LINK +"&getMode=edit"%>"><%=searchFieldInfo[0][1] %></a>
						</div>
					<%
					continue;
					
					}else if(actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.UPDATE_APPLICATION
						|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR
						|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR
						|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR
						|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.GET_PREPARED_TO_GENERATE_DEMAND_NOTE
						|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_POP_CHANGE.GET_PREPARED_TO_GENERATE_DEMAND_NOTE
						|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR
						){
						
						String url=searchFieldInfo[0][0]+"entityID="+request.getAttribute("entityID")+"&entityTypeID="+EntityTypeConstant.VPN_LINK;
						/* url+="&sourceRequestID="+row.getReqID(); */
						url+="&requestTypeID="+actionStateDTO.getActionTypeID();
						url+="&requestTo="+row.getRequestByAccountID();
						url+=extraParameter;
					
		
						%>
						<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<br>
							<a class="btn btn-sm green-jungle uppercase" href="<%=url%>"><%=searchFieldInfo[0][1] %></a>
						</div>
					<%
					continue;
					
					}else if(actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END
					|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END
					|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXTERNAL_FR_OF_NEAR_END
					|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END){
					    //commonRequestDTO.setRequestToAccountID(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID());						
						%>						
					<%}else if(actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END
					|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END
					|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXTERNAL_FR_OF_FAR_END
					|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END){	
						//commonRequestDTO.setRequestToAccountID(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID());						
						%>
					
					<%}
					else if( actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE 
							|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_DISABLE
							|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_DISABLE_LINK.SYSTEM_REQUEST_ADVICE_NOTE_FOR_ENABLE
							|| actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_LINK_CLOSE.SYSTEM_REQUEST_ADVICE_NOTE
							){						
					}
					else if( actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_INFORM_TESTING_DONE ){						
						commonRequestDTO.setRequestToAccountID(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID());		
					}else if(actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_REQUEST_WITH_INTERNAL_FR){																						
					} else{
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
