<%@page import="lli.connection.LLIConnectionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="java.util.ArrayList"%>
<%@page import="lli.LLIConnectionInstance"%>
<%@page import="sessionmanager.SessionConstants"%>
<%
	String msg = null;
	String url = "lli/connection/search";
	String navigator = SessionConstants.NAV_LLI_CONNECTION;
	String context = "../../.." + request.getContextPath() + "/";
%>

<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
		<div class="table-responsive">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>Connection ID</th>
						<th>Connection Name</th>
						<th>Client</th>
						<th>Bandwidth (Mbps)</th>
						<th>Status</th>
						<th>Last Worked On</th>
					</tr>
				</thead>
				<tbody>
					<%
					ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_LLI_CONNECTION);
					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							LLIConnectionInstance lliConnectionInstance = (LLIConnectionInstance) data.get(i);
					%>
					<tr>
						<td><%=lliConnectionInstance.getID()%></td>
						<td>
							<a href="<%=context%>lli/connection/view.do?id=<%=lliConnectionInstance.getID() %>"><%=lliConnectionInstance.getName()%></a>
						</td>
						<td><%=AllClientRepository.getInstance().getClientByClientID(lliConnectionInstance.getClientID()).getLoginName()%></td>
						<td><%=lliConnectionInstance.getBandwidth()%></td>
						<td><%=LLIConnectionConstants.connectionStatusMap.get(lliConnectionInstance.getStatus())%></td>
						<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliConnectionInstance.getActiveFrom(), "dd/MM/yyyy") %></td>
					</tr>
					<% 	
					}
					%>
				</tbody>
				<% } %>
			</table>
		</div>
	</div>
</div>
