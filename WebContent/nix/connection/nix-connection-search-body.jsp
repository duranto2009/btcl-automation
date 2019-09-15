<%@page import="lli.connection.LLIConnectionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="java.util.ArrayList"%>
<%@page import="lli.LLIConnectionInstance"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@ page import="nix.connection.NIXConnection" %>
<%
	String msg = null;
	String url = "nix/connection/search";
	String navigator = SessionConstants.NAV_NIX_CONNECTION;
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
						<%--<th>Bandwidth (Mbps)</th>--%>
						<th>Status</th>
						<th>Last Worked On</th>
					</tr>
				</thead>
				<tbody>
					<%
					ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_NIX_CONNECTION);
					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							NIXConnection nixConnectionInstance = (NIXConnection) data.get(i);
					%>
					<tr>
						<td><%=nixConnectionInstance.getConnectionId()%></td>
						<td>
							<a href="<%=context%>nix/connection/view.do?id=<%=nixConnectionInstance.getConnectionId() %>"><%=nixConnectionInstance.getName()%></a>
						</td>
						<td>
							<a href="<%=context%>nix/client/board.do?id=<%=nixConnectionInstance.getClient()%>">
								<%=AllClientRepository.getInstance().getClientByClientID(nixConnectionInstance.getClient()).getLoginName()%>
							</a>
							<%--<%=AllClientRepository.getInstance().getClientByClientID(nixConnectionInstance.getClient()).getLoginName()%>--%>
						</td>
						<%--<td><%=nixConnectionInstance.getStatus()%></td>--%>
						<td><%=LLIConnectionConstants.connectionStatusMap.get(nixConnectionInstance.getStatus())%></td>
						<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(nixConnectionInstance.getActiveFrom(), "dd/MM/yyyy") %></td>
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
