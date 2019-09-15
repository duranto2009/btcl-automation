<%@page import="user.UserDTO"%>
<%@page import="common.HierarchicalEntityService"%>
<%@page import="user.UserRepository"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="request.EntityActionGenerator"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="login.LoginDTO"%>
<%@page import="request.RequestStatus"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="java.util.*,java.text.*"%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>


<html:base />

			<%
			LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
				String msg = null;
				String url = "SearchRequest";
				String navigator = SessionConstants.NAV_REQUEST;
				String context = "../../.." + request.getContextPath() + "/";
			%>
			<jsp:include page="../includes/navRequest.jsp" flush="true">
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
			</jsp:include>

	
	<div class="portlet box portlet-btcl light">
		<div class="portlet-body">
			<html:form action="/DropClient" method="POST">				
					<div class="table-responsive">
						<table id="example1" class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									<th>Request Name</th>
									<th>Root Request</th>
									<th>Requested To</th>
									<th>Entity Name</th>									
									<th>Client</th>									
									<th>Request Date</th>
									<th>Status</th>					
								</tr>
							</thead>
							<tbody>
								<%
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_REQUEST);
		
									if (data != null) {
										int size = data.size();
 										for (int i = 0; i < size; i++) {
											CommonRequestDTO row = (CommonRequestDTO) data.get(i);
											ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getClientID());
											if (clientDTO == null)
												continue;
											String clientName = clientDTO.getLoginName();
											String requestDateString = TimeConverter.getMeridiemTime(row.getRequestTime());
											String requestedTo = "-";
											if(row.getRequestToAccountID() == null){
												requestedTo = "System";
											}
											else if(row.getRequestToAccountID() < 0){
												if(UserRepository.getInstance().getUserDTOByUserID(0-row.getRequestToAccountID()) != null){
													UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(0-row.getRequestToAccountID());
													String fullName = "";
													if(userDTO.getFullName().length() > 0)
													{
														fullName = "(" + userDTO.getFullName() + ")";
													}
													requestedTo =  userDTO.getUsername() + fullName;
												}
											}else{
												if(AllClientRepository.getInstance().getClientByClientID(row.getRequestToAccountID()) != null){
													requestedTo = AllClientRepository.getInstance().getClientByClientID(row.getRequestToAccountID()).getLoginName();
												}
											}
								%>
								<%try{ %>
								<tr>
									<%
									
									RequestActionStateRepository actionRepo = RequestActionStateRepository.getInstance();
									String rootRequestName = "";
									if(row.getRootReqID() == null)
									{
										
									}
									int rootTypeID = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(row.getRequestTypeID()).getRootActionTypeID();
									%>
									<td><%=RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(row.getRequestTypeID()).getDescription()%></td>									
									<td><%=RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(rootTypeID).getDescription()%></td>
									<td><%=requestedTo%></td>
									<td>
									<% 
									HierarchicalEntityService hierarchicalEntityService = new HierarchicalEntityService();
									if(hierarchicalEntityService.hasViewPage(row.getEntityID(), row.getEntityTypeID())){
									%>
									<a href="<%=context+ hierarchicalEntityService.getViewPageByEntityIDAndEntityTypeID(row.getEntityID(), row.getEntityTypeID(), row.getModuleIDFromThisDTO())%>">
									<%=row.getEntityName()%></a>
									<%}else{%> 
										<%= row.getEntityName()%>
									<%}%>
									</td>																											
									<td><%=clientName%></td>
									
									<td><%=requestDateString%></td>
									<%
									int status = row.getCompletionStatus();
									%>
									
									<td><%=RequestStatus.reqStatusMap.get(status)%></td>

								</tr>
								<%}catch(Exception ex ){ %>
									<tr><td colspan="10" class="text-center "><span class="font-red-mint font-lg bold uppercase">Something wrong in this record(id: <%=row.getReqID() %>)</span></td></tr>
								<%} %>

								<%
									}
								%>
							</tbody>
							<%
								}
							%>

						</table>
					</div>
			</html:form>
		</div>
	</div>