<%@page import="common.repository.AllClientRepository" %>
<%@page import="sessionmanager.SessionConstants" %>

<%@page import="vpn.network.VPNNetworkLink" %>
<%@ page import="java.util.List" %>
<%@ page import="util.TimeConverter" %>
<%
	String msg = null;
	String url = "vpn/network/search";
	String navigator = SessionConstants.NAV_VPN_NETWORK_LINK;
	String context = "../../.." + request.getContextPath() + "/";
%>

<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>"/>
	<jsp:param name="navigator" value="<%=navigator%>"/>
</jsp:include>


<div class="portlet box">
	<div class="portlet-body">
		<div class="table-responsive">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
				<tr>
					<th>Link ID</th>
					<th>Link Name</th>
					<th>Status</th>
					<th>Client</th>
					<th>BandWidth</th>
					<th>Activation Date</th>
					<%--<th>Status</th>--%>

				</tr>
				</thead>
				<tbody>
				<%
					List<VPNNetworkLink> data = ( List<VPNNetworkLink> )session.getAttribute(SessionConstants.VIEW_VPN_NETWORK_LINK);

					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							VPNNetworkLink vpnNetworkLink = data.get(i);
				%>
				<tr>
					<td style="font-weight: bold">

						<a href="<%=context%>vpn/network/details.do?id=<%=vpnNetworkLink.getId() %>"><%=vpnNetworkLink.getHistoryId()%>
						</a>


					</td>

					<td>
						<%--<%=vpnApplication.getClientId()%>--%>
						<%--//TODO Client Id--%>
						<%--<a href="<%=context%>lli/client/board.do?id=<%=vpnApplication.getClientId()%>">--%>
						<%=
						vpnNetworkLink.getLinkName()
						%>
						<%--</a>--%>
					</td>
					<td>
						<%--<%=vpnApplication.getClientId()%>--%>
						<%--//TODO Client Id--%>
						<%--<a href="<%=context%>lli/client/board.do?id=<%=vpnApplication.getClientId()%>">--%>
						<%=vpnNetworkLink.getLinkStatus()%>
						<%--</a>--%>
					</td>
					<td>
						<%--<%=vpnApplication.getClientId()%>--%>
                        <%--//TODO Client Id--%>
						<%--<a href="<%=context%>lli/client/board.do?id=<%=vpnApplication.getClientId()%>">--%>
							<%=
						AllClientRepository.getInstance().getClientByClientID(vpnNetworkLink.getClientId()).getLoginName()
						%>
						<%--</a>--%>
					</td>
					<td>
						<%--<%=vpnApplication.getClientId()%>--%>
						<%--//TODO Client Id--%>
						<%--<a href="<%=context%>lli/client/board.do?id=<%=vpnApplication.getClientId()%>">--%>
						<%=
						vpnNetworkLink.getLinkBandwidth()+" Mbps"
						%>
						<%--</a>--%>
					</td>
					<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnNetworkLink.getActiveFrom(), "dd/MM/yyyy")%></td>

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
