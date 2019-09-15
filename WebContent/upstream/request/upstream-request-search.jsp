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
<%@ page import="upstream.application.UpstreamApplication" %>
<%
	String msg = null;
	String url = "upstream/request-search";
	String navigator = SessionConstants.NAV_UPSTREAM_REQUEST;
	String context = "" + request.getContextPath() + "/";
%>

<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>"/>
	<jsp:param name="navigator" value="<%=navigator%>"/>
</jsp:include>
<%--<link--%>
		<%--href="${context}assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css"--%>
		<%--rel="stylesheet" type="text/css" />--%>
<%--<link--%>
		<%--href="${context}assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css"--%>
		<%--rel="stylesheet" type="text/css" />--%>
<%--<link--%>
		<%--href="${context}assets/global/plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css"--%>
		<%--rel="stylesheet" type="text/css" />--%>
<%--<link--%>
		<%--href="${context}assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"--%>
		<%--rel="stylesheet" type="text/css" />--%>
<%--<link href="${context}assets/global/plugins/clockface/css/clockface.css"--%>
	  <%--rel="stylesheet" type="text/css" />--%>


<%--<script src="${context}assets/global/plugins/moment.min.js"--%>
		<%--type="text/javascript"></script>--%>
<%--<script--%>
		<%--src="${context}assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.js"--%>
		<%--type="text/javascript"></script>--%>
<%--<script--%>
		<%--src="${context}assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"--%>
		<%--type="text/javascript"></script>--%>
<%--<script--%>
		<%--src="${context}assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"--%>
		<%--type="text/javascript"></script>--%>
<%--<script--%>
		<%--src="${context}assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"--%>
		<%--type="text/javascript"></script>--%>
<%--<script src="${context}assets/global/plugins/clockface/js/clockface.js"--%>
		<%--type="text/javascript"></script>--%>
<%--<script src="${context}assets/global/scripts/app.min.js"--%>
		<%--type="text/javascript"></script>--%>
<%--<script--%>
		<%--src="${context}assets/pages/scripts/components-date-time-pickers.min.js"--%>
		<%--type="text/javascript"></script>--%>

<div class="portlet box">
	<div class="portlet-body">
		<div class="table-responsive">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
				<tr>
					<th>Application ID</th>
					<%--<th>Client</th>--%>
					<th>Application Type</th>
					<th>Status</th>
					<th>Submitted Date</th>
					<%--<th>Suggested Date</th>--%>
					<%--<th>Destination Client</th>--%>
					<th>Details</th>
				</tr>
				</thead>
				<tbody>
				<%
					List<UpstreamApplication> data = ( List<UpstreamApplication> )session.getAttribute(SessionConstants.VIEW_UPSTREAM_REQUEST);

					if (data != null) {

						Collections.sort(data, new Comparator<UpstreamApplication>() {
							public int compare(UpstreamApplication o1, UpstreamApplication o2) {
								return Boolean.compare(o1.isHasPermission(), o2.isHasPermission());
							}
						});
						Collections.reverse(data);
					}


					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							UpstreamApplication upstreamApplication = (UpstreamApplication) data.get(i);
				%>
				<tr>
					<td style="font-weight: bold">
						<%if (
								upstreamApplication.isHasPermission()
//								true
						) {%>
						<a href="<%=context%>upstream/request-details.do?id=<%=upstreamApplication.getApplicationId() %>"><%=upstreamApplication.getApplicationId()%></a>
						<% } else { %>
						<p><%=upstreamApplication.getApplicationId()%>
						</p>
						<%}%>

					</td>
					<%--<td>--%>
						<%--&lt;%&ndash;<%=vpnApplication.getClientId()%>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;//TODO Client Id&ndash;%&gt;--%>
						<%--&lt;%&ndash;<a href="<%=context%>lli/client/board.do?id=<%=vpnApplication.getClientId()%>">&ndash;%&gt;--%>
							<%--<%=--%>
						<%--AllClientRepository.getInstance().getClientByClientID(upstreamApplication.getClientId()).getLoginName()--%>
						<%--%>--%>
						<%--&lt;%&ndash;</a>&ndash;%&gt;--%>
					<%--</td>--%>
					<td>
                       <%=upstreamApplication.getApplicationType().getApplicationTypeName()%>
					</td>
					<td
							<%--style="color: <%=upstreamApplication.getColor()%>;font-weight: bold"--%>
					><%=upstreamApplication.getApplicationStatus()%></td>
					<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamApplication.getApplicationDate(), "dd-MM-YYYY")%>
					<%--<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamApplication.getSuggestedDate(), "dd-MM-YYYY")%>--%>
					</td>
					<%--<%if(upstreamApplication.getSecondClient()>0){%>--%>
					<%--<td><%=AllClientRepository.getInstance().getClientByClientID(upstreamApplication.getSecondClient()).getLoginName()%></td>--%>
					<%--<%}else{%>--%>
					<%--<td>N/A</td>--%>
					<%--<%}%>--%>
					<td><a href="<%=context%>upstream/request-details.do?id=<%=upstreamApplication.getApplicationId() %>&type=details">
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
