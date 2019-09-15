<%@page import="coLocation.constants.ColocationConstants"%>
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

<%@page import="coLocation.ColocationRequestTypeConstants"%>
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
		ArrayList<ActionStateDTO> actionStateDTOs = new ArrayList<ActionStateDTO>();
		for (StateActionDTO stateActionDTO1 : stateActionDTOs) {
			String stateName = "";
			if(stateActionDTO1.getStateID() <= 0){							
			}
			else{
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
				if(row.getRequestByAccountID() > 0){
					clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getRequestByAccountID());
					if (clientDTO != null){
						requestedBy = clientDTO.getLoginName();
					}
				}						
				else if(row.getRequestByAccountID() < 0){
					userdto = UserRepository.getInstance().getUserDTOByUserID(Math.abs(row.getRequestByAccountID()));
					if(userdto != null){
						String fullName = userdto.getFullName();
						if(!fullName.isEmpty()){
							fullName = " ("+fullName+")";
						}
						requestedBy =  userdto.getUsername() + fullName;
					}
				}
				else{
					requestedBy = "system";
				}
				
				String requestedTo = "";
				if(row.getRequestToAccountID() != null){
					if(row.getRequestToAccountID() > 0){
						clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getRequestToAccountID());
						if (clientDTO != null){
							requestedTo = clientDTO.getLoginName();
						}
					}
					else if(row.getRequestToAccountID() < 0){
						userdto = UserRepository.getInstance().getUserDTOByUserID(Math.abs(row.getRequestToAccountID()));
						if(userdto != null){
							String fullName = userdto.getFullName();
							if(!fullName.isEmpty()){
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
				if( row.getRequestTypeID() == ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.ADVICE_NOTE_TO_COLOCATION_SERVER_ROOM ){
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
								<th><%=rootReqTypeStr%>
							</tr>
							<tr>																								
								<th scope="row">Status</th>
								<td><%=stateName%>&nbsp;
								<%if(row.getRequestTypeID()% 100 == 11){
								Long rootReqID = row.getRootReqID();
								if(rootReqID == null){
									rootReqID = row.getReqID();
								}
								%>
								<a target="_blank" href="<%=context%>GetPdfBill.do?method=getPdf&reqID=<%=rootReqID%>">VIEW</a>
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
								<td><%=row.getDescription()%></td>
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
				<jsp:include page="../../common/fileListHelper.jsp" flush="true">
					<jsp:param name="entityTypeID" value='<%=row.getRequestTypeID()%>' />
					<jsp:param name="entityID" value='<%=row.getReqID() %>' />
				</jsp:include>
					
					
	              <%boolean samePerson = false;
					boolean revertable = false;
					if(loginDTO.getUserID() > 0){
						if(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID() == -loginDTO.getUserID()){
							samePerson = true;
						}
					}
					else if(loginDTO.getAccountID() > 0){
						if(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID() == loginDTO.getAccountID()){
							samePerson = true;
						}
					}%>
					
	              <%if(RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(stateActionDTO1.getCommonRequestDTO().getRequestTypeID()).isRevertable()){
	              	revertable = true;
	              	}%>
	              	
	              <%actionLogger.debug("samePerson " + samePerson + " revertable " + revertable);
	              	if(samePerson && revertable){
		              	CommonRequestDTO sourceCommonRequestDTO = stateActionDTO1.getCommonRequestDTO();
		              	sourceCommonRequestDTO.setRequestTypeID(ColocationRequestTypeConstants.REQUEST_NEW_COLOCATION.SYSTEM_CANCEL_REQUEST);
		              	request.setAttribute("commonRequestDTO", stateActionDTO1.getCommonRequestDTO());
	              	%>
	              
		              <form action="<%=context%>CommonAction.do?mode=cancel" method="Post" class="formSubmit">
		              <%@include file="../../common/elements/actionListHiddens.jsp"%>                           
		              <button  class="btn btn-submit-btcl">Rollback this action</button>
		              </form>
		          	<%}%>                            
			<%}%>
				
				
				
			<!-- Action List begins -->
			<%for (int actionTypeID : stateActionDTO1.getActionTypeIDs()) {
					ActionStateDTO actionStateDTO1 =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
					actionStateDTOs.add(actionStateDTO1);
					
					ActionStateDTO actionStateDTO =  RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(actionTypeID);
					String [][] searchFieldInfo = ColocationRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
					
					if( actionTypeID == ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.SYSTEM_GENERATE_DEMAND_NOTE){%> 
						
						<div class="">
							<label> 
								<input type="radio" class="rdo1" name="actionListRadio" value="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO1.getActionTypeID()%>"><%=actionStateDTO1.getDescription()%>
							</label>
						</div>
						<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">										
							<jsp:include page="<%=searchFieldInfo[0][0]%>" flush="true"/> 
						</div>
							
					<%continue;
					}%>
					
					<div class="">
						<label> 
							<input type="radio" class="rdo1" name="actionListRadio" value="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO1.getActionTypeID()%>"><%=actionStateDTO1.getDescription()%>
						</label>
					</div>							
			
				<%request.setAttribute("action", actionStateDTO);
					if(actionStateDTO.getType() == null){
						actionStateDTO.setType("");
					}
					actionLogger.debug("actionStateDTO.getType() " + actionStateDTO.getType());
					actionLogger.debug("actionStateDTO.getType() " +actionStateDTO.getActionTypeID());
					
					
				 	CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
				 	SqlGenerator.populateObjectFromOtherObject(commonRequestDTO, CommonRequestDTO.class, row, CommonRequestDTO.class);	
				 	commonRequestDTO.setRequestTypeID(actionTypeID);
				 	actionLogger.debug("stateActionDTO1.getCommonRequestDTO().getRequestTypeID() " + stateActionDTO1.getCommonRequestDTO().getRequestTypeID());
				 	int requestTypeRemainder = (Math.abs(stateActionDTO1.getCommonRequestDTO().getRequestTypeID()) % EntityTypeConstant.MULTIPLIER2);
					if(requestTypeRemainder == 04){
						commonRequestDTO.setRequestToAccountID((Long) request.getAttribute("clientID"));
					}
				 	actionLogger.debug("commonRequestDTO " + commonRequestDTO);
				 	
				    if(searchFieldInfo == null){
				    	searchFieldInfo = new String[][]{{ "../../includes/elements/description.jsp", "description" }};
				    }
					request.setAttribute("commonRequestDTO", commonRequestDTO);
					request.setAttribute("stateActionDTO", stateActionDTO1);
					request.setAttribute("searchFieldInfo", searchFieldInfo);
					boolean goToInternalFRPage = false;
					String extraParameter = "";
					if(actionStateDTO.getActionTypeID() == ColocationRequestTypeConstants.REQUEST_NEW_COLOCATION.GET_PREPARED_TO_GENERATE_DEMAND_NOTE){
						actionLogger.debug("goToInternalFRPage: "+ goToInternalFRPage);
						actionLogger.debug(commonRequestDTO);
					}%>
					
					
					
				<!-- checking -->
				<%if(actionStateDTO.getActionTypeID() == ColocationRequestTypeConstants.REQUEST_NEW_COLOCATION.UPDATE_APPLICATION){%>					
					<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;"><br>
						<a class="btn btn-sm green-jungle  uppercase" href="<%="../../ColocationEdit.do?colocationID="+request.getAttribute("entityID")%>">Go to Colocation Update Page</a>
					</div>
					<%continue;
				}
				
				else if(actionStateDTO.getActionTypeID() == ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.FORWARD_TO_DIRECTOR_INTERNATIONAL){
					commonRequestDTO.setRequestToAccountID(-ColocationConstants.COLOCATION_DIRECTOR_INTERNATIONAL_ID);
				}
				else if(actionStateDTO.getActionTypeID() == ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.FORWARD_TO_CORRESPONDING_DE){
					commonRequestDTO.setRequestToAccountID(-ColocationConstants.COLOCATION_DE_ID);
				}
				else if(actionStateDTO.getActionTypeID() == ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.DE_REPORT_BACK_TO_DIRECTOR_INTERNATIONAL){ //Internal FR
					//should work automatically but doesn't
					commonRequestDTO.setRequestToAccountID(-ColocationConstants.COLOCATION_DIRECTOR_INTERNATIONAL_ID);%>
				
					<div class="portlet" id="link_action_<%=stateActionDTO1.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;"><br>
						<a class="btn btn-sm green-jungle  uppercase" href="<%="../../ColocationInternalFR.do?colocationID="+request.getAttribute("entityID")%>">Submit Feasibility Report</a>
					</div>
				<%}
				else if(actionStateDTO.getActionTypeID() == ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.DIRECTOR_INTERNATIONAL_PASS_REPORT_TO_DE){ //Initial DE
					//Get ID of initial DE
					commonRequestDTO.setRequestToAccountID(commonService.getRequestedByAccountID(stateActionDTO1.getCommonRequestDTO().getRootReqID(), ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.FORWARD_TO_DIRECTOR_INTERNATIONAL));
				}
				else if(actionStateDTO.getActionTypeID() == ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.ADVICE_NOTE_TO_COLOCATION_SERVER_ROOM){
					commonRequestDTO.setRequestToAccountID(-ColocationConstants.COLOCATION_SERVER_ROOM_ID);
				}
				else if(actionStateDTO.getActionTypeID() == ColocationRequestTypeConstants.REQUEST_COLOCATION_FRESH.SETUP_DONE){
					//should work automatically but doesn't
					commonRequestDTO.setRequestToAccountID(stateActionDTO1.getCommonRequestDTO().getRequestByAccountID());
				}
				
				else{
					actionLogger.debug("in else part");					    
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

<%@include file="../../common/addActionScriptColocation.jsp"%>
