<%@page import="util.ServiceDAOFactory"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="util.DatabaseConnectionFactory"%>
<%@page import="common.EntityDTO"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="crm.repository.CrmAllEmployeeRepository"%>
<%@page import="util.TimeConverter"%>
<%@page import="user.UserRepository"%>
<%@page import="crm.inventory.repository.CRMInventoryRepository"%>
<%@page import="crm.inventory.CRMInventoryAttributeName"%>
<%@page import="crm.repository.CrmInventoryItemRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="crm.*, java.util.*" %>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%
	String msg = (String)request.getAttribute("msg");
	String url = "CrmActivityLog/getCrmActivityLogSearch";
	String navigator = SessionConstants.NAV_CRM_ACTIVITY;
	String context = "../../.." + request.getContextPath();
	LoginDTO localLoginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	CommonDAO commonDAO = ServiceDAOFactory.getDAO(CommonDAO.class);
	
	
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
	<% if(msg != null){%>
		<div class="text-center" id="msgDiv">
			<p style="color: #5cb85c"><%=msg %></p>
		</div>
	<%} %>
		<form id="tableForm">
			<div class="table-responsive">
				<table id="tableData" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>Complain ID</th>
							<th>Client</th>
							<th>Previous Employee</th>
							<th>Previous User</th>
							<th>Current Employee</th>
							<th>Current User</th>
							<th>Action Type</th>
							<th>Description</th>
							<th>From Time to take Action</th>
							<th>Time Taken</th>
						</tr>
					</thead>
					<tbody>
					<%
					
					ArrayList<CrmActivityLog> data = (ArrayList<CrmActivityLog>) session.getAttribute(SessionConstants.VIEW_CRM_ACTIVITY);
					
					if(data != null){
						for(CrmActivityLog log: data){
					%>
							<tr>
								
								<td>
								<a href="${context }crm/complain/viewCrmComplain.jsp?complainID=
									<%=log.getCrmComplainID() %>" target="_blank">
									<%=log.getCrmComplainID() %>
								</a>
								
								</td>
								
								<td>
									<%
										if(log.getClientID() == null){
									%><%="N/A"%>
									<%		
										}else {
											%><%=AllClientRepository.getInstance().getClientByClientID(log.getClientID()).getLoginName() %>
									<%
										}
									%>
								</td>

								<td>
								<a href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=log.getPreviousEmployeeID() %>"
								target="_blank">
								<%try{ %>
									<%=CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(log.getPreviousEmployeeID()).getName() %>
								<%}catch(Exception e){} %>
								</a>
								
								</td>
								<!-- <td><%=log.getPreviousUserID() %></td> -->
								<td>
									<%try{ %>
									<%=UserRepository.getInstance().getUserDTOByUserID(log.getPreviousUserID()).getUsername()  %>
									<%}catch(Exception e){} %>
								</td>
								<td>
									<a href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=log.getCurrentEmployeeID() %>"
										target="_blank">
										<%try{ %>
										<%=CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByEmployeeID(log.getCurrentEmployeeID()).getName() %>
										<%}catch(Exception e){} %>
									</a>
									
								</td>
								<td>
								<!-- <%=log.getCurrentUserID() %> -->
								<%try{ %>
								<%=UserRepository.getInstance().getUserDTOByUserID(log.getCurrentUserID()).getUsername() %>
<%-- 								<%=log.getCurrentUserID() %> --%>
								<%}catch(Exception e){} %>
								</td>
								<!-- <td><%=log.getTakenActionType() %></td> -->
								<td><%=EntityTypeConstant.mapOfActionTypeStrToActionTypeCrm.get(
											log.getTakenActionType()
										) %></td>
								<td><%=log.getDescription()==null ?"N/A": log.getDescription() %></td>
								<td><%=TimeConverter.getMeridiemTime(log.getFromTimeForEmployeeToTakeAction())%></td>
								<td><%=log.getTimeOfTakenAction() == null ? "N/A" : TimeConverter.getMeridiemTime(log.getTimeOfTakenAction())%></td>
							</tr>
						<%}%>
					</tbody>
					<%}%>
				</table>
			</div>
		</form>
	</div>
</div>
