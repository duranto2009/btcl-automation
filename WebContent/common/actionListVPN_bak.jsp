<%@page import="common.note.CommonNote"%>
<%@page import="java.util.Iterator"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="util.SqlGenerator"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.RequestStatus"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="vpn.constants.VpnStateConstants"%>
<%@page import="vpn.constants.VpnRequestTypeConstants.REQUEST_NEW_CLIENT"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="vpn.constants.VpnRequestTypeConstants.REQUEST_NEW_LINK"%>
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
%>
<%try{%>

			<div class="row">
				<div class="col-md-12">
					<h3 >Actions</h3>
				<%
				
					
					ArrayList<ActionStateDTO> actionStateDTOs = new ArrayList<ActionStateDTO>();
					actionLogger.debug("actionStateDTOs " + actionStateDTOs);
					for (StateActionDTO stateActionDTO1 : stateActionDTOs) {
						String stateName = "";
						if(stateActionDTO1.getStateID() <= 0)
						{
							
						}
						else
						{
							stateName = StateRepository.getInstance().getStateDTOByStateID(stateActionDTO1.getStateID()).getName();
						}
						CommonRequestDTO row = stateActionDTO1.getCommonRequestDTO();	
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
								requestedBy = userdto.getUsername();
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
									requestedTo = userdto.getUsername();
								}
							}						
						}
						
						String adviceNoteLink = null;
						if( row.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE ){
							
							long entityTypeId = row.getEntityTypeID();
							long entityId = row.getEntityID();
							
							CommonNote adviceNote = CommonNote.getNote( entityTypeId, entityId );
							
							if( adviceNote != null )
								adviceNoteLink = request.getContextPath() + "/common/note/noteView.jsp?id=" + adviceNote.getId();
						}
							%> 
							
							
						<div class="table-responsive">
							<table class="table table-bordered table-striped">
								<tbody>
									<tr>
										<th scope="row">Status</th>
										<td><%=stateName %> <% if( adviceNoteLink != null ){%>  (<a href="<%=adviceNoteLink %>" target = "_blank" >View</a>) <%} %></td>
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
										<td><%=row.getDescription()%></td>
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
                            if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(stateActionDTO1.getCommonRequestDTO().getRequestTypeID()).isRevertable())
                            {
                            	
                            	revertable = true;
                            	
                            	/***** This block author - Alam ********/
                            	//If document type is Near end or far end, then check if demand note is generated.
                            	//If demand note is generated, then NE or FE can't be rolled back
                            	if( row.getEntityTypeID() == EntityTypeConstant.VPN_LINK_FAR_END || row.getEntityTypeID() == EntityTypeConstant.VPN_LINK_NEAR_END ){
	                            	
                            		DatabaseConnection databaseConnection = new DatabaseConnection();
	                            	
	                            	databaseConnection.dbOpen();
	                            
									CommonDAO commonDAO = new CommonDAO();
									
									//Get root Link for Near end or far end
	                            	Set<Long> rootSet = commonDAO.getMainRoots( row, false, true, databaseConnection );
	                            	
									boolean linkFound = false;
											
	                            	if( rootSet.size() == 1 ){
	                            		
	                            		long rootReqID = rootSet.iterator().next();
	                            		
	                            		CommonRequestDTO rootDTO = (CommonRequestDTO)SqlGenerator.getObjectByID( CommonRequestDTO.class, rootReqID, databaseConnection );
	                            		
	                            		//Get list of bottom request of root. This will also contain the child of link like Near end or far end/
	                            		Set<CommonRequestDTO> bottomOfRootDTOSet = (Set<CommonRequestDTO>)commonDAO.getBottomRequestDTOsByEntity( rootDTO, databaseConnection );
	                            		
	                            		Iterator bottomOfRootSetIterator = bottomOfRootDTOSet.iterator();
	                            		
	                            		//Itearate over bottom req list and find the bottom link. 
	                            		while( bottomOfRootSetIterator.hasNext() ){
	                            			
	                            			CommonRequestDTO bottomOfRootDTO = (CommonRequestDTO)bottomOfRootSetIterator.next();
	                            			
	                            			if( bottomOfRootDTO.getEntityTypeID() == EntityTypeConstant.VPN_LINK){
	                            				
	                            				linkFound = true;
	                            				//If bottom link demand note is generated. then Near end or far end can't be rolled back.
	                            				VpnLinkDTO bottomLink = (VpnLinkDTO)SqlGenerator.getObjectByID( VpnLinkDTO.class, bottomOfRootDTO.getEntityID(), databaseConnection );
	                            				
	                            				//Can't revert FR request if demand note has generated already or Link is active. that means latest status is negetive
	                            				if( bottomLink.getLatestStatus() >= VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_GENERATE_DEMAND_NOTE || bottomLink.getLatestStatus() < 0 ){
	                            					
	                            					revertable = false;
	                            				}
	                            			}
	                            		}
	                            		
	                            		//No link found means link is live and bottom requests only contains FR near and far requests
	                            		if( !linkFound )
	                            			revertable = false;
	                            	}
	                            	
	                            	databaseConnection.dbClose();
                            	}
                            }
                            %>
                            <%
                            actionLogger.debug("samePerson " + samePerson + " revertable " + revertable);
                            if(samePerson && revertable){
                            	CommonRequestDTO sourceCommonRequestDTO = stateActionDTO1.getCommonRequestDTO();
                            	sourceCommonRequestDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_CANCEL_REQUEST);
                            	request.setAttribute("commonRequestDTO", stateActionDTO1.getCommonRequestDTO());
                            %>
                            
                            <form action="<%=context%>CommonAction.do?mode=cancel" method="post" class="formSubmit">
                            
                            	<%@include file="../../common/elements/actionListHiddens.jsp"%>                            
                            	<button  class="btn btn-submit-btcl">Rollback this action</button>
                            	
                            </form>
                            <%
                            //break;
                            }%>                            
                           <!--  </div> -->

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
					/* for (int actionTypeID : stateActionDTO.getActionTypeIDs()) { */
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
						
					 	actionLogger.debug("commonRequestDTO " + commonRequestDTO);
					 	
						String [][] searchFieldInfo=VpnRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
					    if(searchFieldInfo == null)
					    {
					    	searchFieldInfo = new String[][]{{ "../../includes/elements/description.jsp", "description" }};
					    }
						request.setAttribute("commonRequestDTO", commonRequestDTO);
						request.setAttribute("stateActionDTO", stateActionDTO1);
						request.setAttribute("searchFieldInfo", searchFieldInfo);
						
						%>
					<%if(actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.UPDATE_APPLICATION){%>
					<%-- <%if(actionStateDTO.getType().equals("updateApp")){%> --%>
						<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<h4>${ action.description}</h4>
							<div class="form-group col-md-offset-5 col-md-2">
								<a class="btn green-jungle  btn-block uppercase" href="<%=editActionName%>">Edit</a>
							</div>
						</div>
					<%
					continue;
					
					}else if(actionStateDTO.getType().equals("selectPortReq")){												
						PermissionService permissionService = new PermissionService();
						CommonRequestDTO permissionCommonRequestDTO = new CommonRequestDTO();
						permissionCommonRequestDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_PORT_SELECT);
						int nextStateID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionStateDTO.getActionTypeID()).getNextStateID();
						actionLogger.debug("nextStateID " + nextStateID);
						permissionCommonRequestDTO.setState(nextStateID);
						ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(permissionCommonRequestDTO, loginDTO);
						request.setAttribute("adminList",userDTOs);
						
						actionLogger.debug("size: "+userDTOs);						
						%>
						
						<%-- <div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div> --%>		
					<%}else if(actionStateDTO.getType().equals("verifyApp")){ %>
						<%-- <div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div> --%>
					<%}else if(actionStateDTO.getType().equals("rejectApp")){%>
						<%-- <div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div> --%>
					<%}else if(actionStateDTO.getType().equals("frNearLoopReq")){														
						VpnLinkService linkService = new VpnLinkService();
						VpnLinkDTO linkDTO = linkService.getVpnLinkByVpnLinkID(commonRequestDTO.getEntityID());
						
						commonRequestDTO.setEntityID(linkDTO.getNearEndID());
						commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK_NEAR_END);
						
						CommonRequestDTO permissionCommonRequestDTO = new CommonRequestDTO();
						permissionCommonRequestDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_FR_RESPONSE_WITH_LOOP_CHECK_NEAR_END);
						int nextStateID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionStateDTO.getActionTypeID()).getNextStateID();
						permissionCommonRequestDTO.setState(nextStateID);
						PermissionService permissionService = new PermissionService();
						ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(permissionCommonRequestDTO, loginDTO);
						request.setAttribute("adminList",userDTOs);
						

						%>
						
						<%-- <div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div> --%>
				 <%}else if(actionStateDTO.getType().equals("frFarLoopReq")){							
						VpnLinkService linkService = new VpnLinkService();
						VpnLinkDTO linkDTO = linkService.getVpnLinkByVpnLinkID(commonRequestDTO.getEntityID());
						
						commonRequestDTO.setEntityID(linkDTO.getFarEndID());
						commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK_FAR_END);
						
						CommonRequestDTO permissionCommonRequestDTO = new CommonRequestDTO();
						permissionCommonRequestDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_FR_RESPONSE_WITH_LOOP_CHECK_FAR_END);
						int nextStateID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionStateDTO.getActionTypeID()).getNextStateID();
						permissionCommonRequestDTO.setState(nextStateID);
						PermissionService permissionService = new PermissionService();
						
						ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(permissionCommonRequestDTO, loginDTO);
						request.setAttribute("adminList",userDTOs);
						
						actionLogger.debug("size: "+userDTOs);						
						%>
						
						<%-- <div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div> --%>
					
				<%}else if(actionStateDTO.getType().equals("frNearLoopResponse")){									
						
						VpnLinkService linkService = new VpnLinkService();
						VpnLinkDTO linkDTO = linkService.getVpnLinkByVpnLinkID(commonRequestDTO.getEntityID());
						
						commonRequestDTO.setEntityID(linkDTO.getNearEndID());
						commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK_NEAR_END);
					
					    commonRequestDTO.setRequestToAccountID(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID());						
						%>
						<%-- <div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div> --%>
						
					<%}else if(actionStateDTO.getType().equals("frFarLoopResponse")){	
						
						VpnLinkService linkService = new VpnLinkService();
						VpnLinkDTO linkDTO = linkService.getVpnLinkByVpnLinkID(commonRequestDTO.getEntityID());
						
						commonRequestDTO.setEntityID(linkDTO.getFarEndID());
						commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK_FAR_END);
					
						PermissionService permissionService = new PermissionService();
						ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(commonRequestDTO);
						request.setAttribute("adminList",userDTOs);
						
						commonRequestDTO.setRequestToAccountID(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID());						
						%>
						<%-- <div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div> --%>
					
					<%}else if(actionStateDTO.getType().equals("conOwnershipChange")){%>
						<%-- <div class='box-body'>
							<%@include file="../../common/elements/commonActionList.jsp"%>
						</div> --%>
	
					<%}
					else if( actionStateDTO.getActionTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_REQUEST_ADVICE_NOTE ){
						
						//Added by alam for sending advice note, after bill has been paid for the new Vpn link
						//Code is mainly copied from Fr req send portion
						
						VpnLinkService linkService = new VpnLinkService();
						VpnLinkDTO linkDTO = linkService.getVpnLinkByVpnLinkID(commonRequestDTO.getEntityID());
						
						commonRequestDTO.setEntityID(linkDTO.getID());
						commonRequestDTO.setEntityTypeID(EntityTypeConstant.VPN_LINK);
						
						CommonRequestDTO permissionCommonRequestDTO = new CommonRequestDTO();
						permissionCommonRequestDTO.setRequestTypeID(VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_COMPLETE_SETUP);
						int nextStateID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionStateDTO.getActionTypeID()).getNextStateID();
						permissionCommonRequestDTO.setState(nextStateID);
						PermissionService permissionService = new PermissionService();
						
						ArrayList<UserDTO> userDTOs = permissionService.getUsersDTOHavingPermission(permissionCommonRequestDTO, loginDTO);
						request.setAttribute("adminList",userDTOs);
						
						actionLogger.debug("size: "+userDTOs);	
					}
					else if( actionStateDTO.getType().equals( "informSetupDone" ) ){
						
						//commonRequestDTO.setRequestToAccountID(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID());
					}
					else{
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
