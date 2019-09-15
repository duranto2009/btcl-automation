<%@page import="util.TimeConverter" %>
<%@page import="lli.connection.LLIConnectionConstants" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="java.util.ArrayList" %>
<%@page import="lli.LLIConnectionInstance" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="vpn.application.VPNApplication" %>
<%@ page import="application.Application" %>
<%@ page import="java.util.List" %>
<%
	String msg = null;
	String url = "vpn/link/search";
	String navigator = SessionConstants.NAV_VPN_LINK;
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
					<th>Application ID</th>
					<th>Client</th>
					<th>Application Type</th>
					<th>Status</th>
					<th>Submitted Date</th>
					<th>Suggested Date</th>
					<th>Destination Client</th>
					<th>Details</th>
				</tr>
				</thead>
				<tbody>
				<%
					List<Application> data = ( List<Application> )session.getAttribute(SessionConstants.VIEW_VPN_LINK);


					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							Application vpnApplication = (Application) data.get(i);
				%>
				<tr>
					<td style="font-weight: bold">
						<%if (vpnApplication.isHasPermission()) {%>
						<a href="<%=context%>vpn/link/details.do?id=<%=vpnApplication.getApplicationId() %>"><%=vpnApplication.getApplicationId()%>
						</a>
						<% } else { %>
						<p><%=vpnApplication.getApplicationId()%>
						</p>
						<%}%>

					</td>
					<td>
						<%--<%=vpnApplication.getClientId()%>--%>
                        <%--//TODO Client Id--%>
						<%--<a href="<%=context%>lli/client/board.do?id=<%=vpnApplication.getClientId()%>">--%>
							<%=
						AllClientRepository.getInstance().getClientByClientID(vpnApplication.getClientId()).getLoginName()
						%>
						<%--</a>--%>
					</td>
					<td>
                       <%=vpnApplication.getApplicationType().getApplicationTypeName()%>
                        <%--<%=LLIConnectionConstants.applicationTypeNameMap.get(vpnApplication.getApplicationType())%>--%>
					</td>
					<td style="color: <%=vpnApplication.getColor()%>;font-weight: bold"><%=vpnApplication.getStateDescription()%>
					</td>
					<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnApplication.getSubmissionDate(), "dd-MM-YYYY")%>
					<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(vpnApplication.getSuggestedDate(), "dd-MM-YYYY")%>
					</td>
					<%if(vpnApplication.getSecondClient()>0){%>
					<td><%=
					AllClientRepository.getInstance().getClientByClientID(vpnApplication.getSecondClient()).getLoginName()
					%></td>
					<%}else{%>
					<td>N/A</td>
					<%}%>
					<td><a href="<%=context%>vpn/link/details.do?id=<%=vpnApplication.getApplicationId() %>&type=details">
						<%--<%=vpnApplication.getApplicationId()%>--%>
						<%--<%=lliApplication.getApplicationID()%>--%>
						<span class="glyphicon glyphicon-list-alt"></span>
					</a></td>
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
