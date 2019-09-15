<%@page import="domain.constants.DomainRequestTypeConstants"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.RequestStatus"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
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

			<div class="row">
				<div class="col-md-12">
					<h3 class="header-btcl">Actions</h3>
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
						CommonRequestDTO row = stateActionDTO1.getCommonRequestDTO();	
						ClientDTO clientDTO = ClientRepository.getInstance().getClient(row.getClientID());
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
							clientDTO = ClientRepository.getInstance().getClient(row.getRequestByAccountID());
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
							clientDTO = ClientRepository.getInstance().getClient(row.getRequestToAccountID());
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
							%> 
							
							
							<div class="note note-info">
                                        <h4 class="block"><%=stateName %></h4>
                                        
                                        
                              <p><strong>Time:</strong> <%=requestDateString%></p>
                              <p><strong>From:</strong> <%=requestedBy%></p>
                              <%if(requestedTo.length() > 0){%>
                              <p><strong>To:</strong> <%=requestedTo%></p>
                              <%} %>
                              <p><strong>Description:</strong><br><%=row.getDescription()%></p>
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
                            	sourceCommonRequestDTO.setRequestTypeID(DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.SYSTEM_CANCEL_REQUEST);
                            	request.setAttribute("commonRequestDTO", stateActionDTO1.getCommonRequestDTO());
                            %>
                            
                            <form action="<%=context%>CommonAction.do?mode=cancel" method="Post" class="formSubmit">
                            <%@include file="../../common/elements/actionListHiddens.jsp"%>                            
                            <button>Rollback this action</button>
                            </form>
                            <%
                            //break;
                            }%>                            
                            </div>

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
						
						
					 	CommonRequestDTO commonRequestDTO = new CommonRequestDTO((Integer) request.getAttribute("entityTypeID"),
							(Long) request.getAttribute("entityID"),
							actionTypeID,
							stateActionDTO.getStateID(),
							(Long) request.getAttribute("clientID"));
					 	commonRequestDTO.setReqID(stateActionDTO.getCommonRequestDTO().getReqID());
						
					 	actionLogger.debug("commonRequestDTO " + commonRequestDTO);
					 	
						String [][] searchFieldInfo=DomainRequestTypeConstants.requestTypToSearchCriteria.get(actionStateDTO.getActionTypeID());
					    if(searchFieldInfo == null)
					    {
					    	searchFieldInfo = new String[][]{{ "../../includes/elements/description.jsp", "description" }};
					    }
						request.setAttribute("commonRequestDTO", commonRequestDTO);
						request.setAttribute("stateActionDTO", stateActionDTO);
						request.setAttribute("searchFieldInfo", searchFieldInfo);
						 
						%>
					<%if(actionStateDTO.getActionTypeID() == DomainRequestTypeConstants.REQUEST_NEW_DOMAIN.UPDATE_APPLICATION){%>
					<%-- <%if(actionStateDTO.getType().equals("updateApp")){%> --%>
						<div class="portlet" id="link_action_<%=stateActionDTO.getStateID()%>_<%=actionStateDTO.getActionTypeID() %>" style="display: none;">
							<h4>${ action.description}</h4>
							<div class="form-group col-md-offset-5 col-md-2">
								<a class="btn green-jungle  btn-block uppercase" href="<%=editActionName%>">Edit</a>
							</div>
						</div>
					<%
					continue;
					}else if(actionStateDTO.getType().equals("verifyApp")){ %>
					<%}else if(actionStateDTO.getType().equals("rejectApp")){%>
					<%}else if(actionStateDTO.getType().equals("conOwnershipChange")){%>	
					<%}else{
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
<%@include file="../../common/addActionScript.jsp"%>
