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
	String url = "asn/search";
	String navigator = SessionConstants.NAV_ASN_APP;
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
					<th>Details</th>
				</tr>
				</thead>
				<tbody>
				<%
					List<Application> data = ( List<Application> )session.getAttribute(SessionConstants.VIEW_ASN_APP);

					if (data != null) {

						Collections.sort(data, new Comparator<Application>() {
							public int compare(Application o1, Application o2) {
								return Boolean.compare(o1.isHasPermission(), o2.isHasPermission());
							}
						});
						Collections.reverse(data);
					}


					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							Application asnApplication = (Application) data.get(i);
				%>
				<tr>
					<td style="font-weight: bold">
						<%if (asnApplication.isHasPermission()) {%>
						<a href="<%=context%>asn/details.do?id=<%=asnApplication.getApplicationId() %>"><%=asnApplication.getApplicationId()%>
						</a>
						<% } else { %>
						<p><%=asnApplication.getApplicationId()%>
						</p>
						<%}%>

					</td>
					<td>
                        <%--//TODO Client Id--%>
							<%=
						AllClientRepository.getInstance().getClientByClientID(asnApplication.getClientId()).getLoginName()
						%>
					</td>
					<td>
                       <%=asnApplication.getApplicationType().getApplicationTypeName()%>
					</td>
					<td style="color: <%=asnApplication.getColor()%>;font-weight: bold"><%=asnApplication.getStateDescription()%>
					</td>
					<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(asnApplication.getSubmissionDate(), "dd-MM-YYYY")%>
					<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(asnApplication.getSuggestedDate(), "dd-MM-YYYY")%>
					</td>
					<td><a href="<%=context%>asn/details-view.do?id=<%=asnApplication.getApplicationId() %>&type=details">
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
