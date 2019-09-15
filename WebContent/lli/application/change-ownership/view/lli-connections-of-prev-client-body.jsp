<%@page import="common.repository.AllClientRepository"%>
<%@page import="lli.Application.FlowConnectionManager.LLIConnection"%>
<%@page import="lli.connection.LLIConnectionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%
	String msg = null;
	String url = "lli/connection/search";
	String navigator = SessionConstants.NAV_LLI_CONNECTION;
	String context = "../../.." + request.getContextPath() + "/";
	long clientId = (long)request.getAttribute("clientId");
	List<LLIConnection> connections = (List<LLIConnection>)request.getAttribute("connections");
	String clientName = new Gson().toJson(
			AllClientRepository.getInstance().getClientByClientID(clientId).getLoginName());

%>


<div id="app">

	<btcl-body :title="'Connections of Client (' + clientName + ':' + clientId +')'" v-cloak="true">
		<div class="table-responsive">
			<table id="example1" class="table table-bordered table-striped">
				<thead >
					<tr>
						<th>Connection Name</th>
						<th>Client</th>
						<th>Bandwidth (Mbps)</th>
						<th>Status</th>
						<th>Last Worked On</th>
					</tr>
				</thead>
				<tbody>
					<%
					ArrayList data = (ArrayList) connections;
					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							LLIConnection lliConnection = (LLIConnection) data.get(i);
							if(lliConnection.getClientID()!=clientId)
							    continue;
					%>
					<tr>
						<td>
							<a href="<%=context%>lli/connection/view.do?id=<%=lliConnection.getID() %>"><%=lliConnection.getName()%></a>
						</td>
						<td><%=AllClientRepository.getInstance().getClientByClientID(lliConnection.getClientID()).getLoginName()%></td>
						<td><%=lliConnection.getBandwidth()%></td>
						<td><%=LLIConnectionConstants.connectionStatusMap.get(lliConnection.getStatus())%></td>
						<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliConnection.getActiveFrom(), "dd/MM/yyyy") %></td>
					</tr>
					<% 	
					}
					%>
				</tbody>
				<% } %>
			</table>
		</div>
	</btcl-body>
</div>
<script>
	let clientName = JSON.parse('<%=clientName%>');
	new Vue({
		el: "#app",
		data: {
			clientName,
			clientId : '<%=clientId%>'
		}
	});
</script>