<%@page import="lli.connection.LLIConnectionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="java.util.ArrayList"%>
<%@page import="lli.LLIConnectionInstance"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@ page import="com.google.gson.Gson" %>
<%
	String msg = null;
	String url = "lli/connection/search";
	String navigator = SessionConstants.NAV_LLI_CONNECTION;
	String context = "../../.." + request.getContextPath() + "/";
	long clientId = (long)request.getAttribute("clientId");
	String clientName = new Gson().toJson(AllClientRepository.getInstance().getClientByClientID(clientId).getLoginName());
%>



<div id="app">
	<btcl-body :title="'Connections of Client (' + client + ')'">
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
					ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_LLI_CONNECTION);
					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							LLIConnectionInstance lliConnectionInstance = (LLIConnectionInstance) data.get(i);
							if(lliConnectionInstance.getClientID()!=clientId)
							    continue;
					%>
					<tr>
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
	</btcl-body>
</div>

<script>
	let client = JSON.parse('<%=clientName%>');
	new Vue({
		el: "#app",
		data :{
			client
		}
	});
</script>
