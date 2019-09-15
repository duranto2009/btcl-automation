<%@page import="lli.connection.LLIConnectionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="java.util.ArrayList"%>
<%@page import="lli.LLIConnectionInstance"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@ page import="lli.Application.ownership.LLIOwnerShipChangeApplication" %>
<%
	String msg = null;
	String url = "lli/ownershipChange/search";
	String navigator = SessionConstants.NAV_LLI_OWNER_CHANGE;
	String context = "../../.." + request.getContextPath() + "/";
%>


<div id ="loading" class="row" style="text-align: center">
	<i class="fa fa-spinner fa-spin fa-5x"></i>
</div>

<jsp:include page="/includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
		<div class="table-responsive">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
				<tr>
					<th>Application ID</th>
					<th>Source Client</th>
					<th>Dest Client</th>
					<th>Application Type</th>
					<th>Status</th>
					<th>App Date</th>
					<th>Details</th>
				</tr>
				</thead>
				<tbody>
				<%
					ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_LLI_OWNER_CHANGE);
					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							LLIOwnerShipChangeApplication lliOwnerShipChangeApplication = (LLIOwnerShipChangeApplication) data.get(i);
				%>
				<tr>

					<td style="font-weight: bold">
						<%if (lliOwnerShipChangeApplication.isHasPermission()) {%>
						<a href="<%=context%>lli/ownershipChange/owner-change-view.do?id=<%=lliOwnerShipChangeApplication.getId() %>">
							<%=lliOwnerShipChangeApplication.getId()%>
						</a>
						<% } else { %>
						<p><%=lliOwnerShipChangeApplication.getId()%>
						</p>
						<%}%>

					</td>
					<%--<td>
						<a href="<%=context%>lli/ownershipChange/owner-change-view.do?id=<%=lliOwnerShipChangeApplication.getId() %>"><%=lliOwnerShipChangeApplication.getId()%>
						</a>
					</td>--%>
					<td>
						<a href="<%=context%>lli/client/board.do?id=<%=lliOwnerShipChangeApplication.getSrcClient()%>">
							<%=AllClientRepository.getInstance().getClientByClientID(lliOwnerShipChangeApplication.getSrcClient()).getLoginName()%>
						</a>
						<%--<%=AllClientRepository.getInstance().getClientByClientID(nixConnectionInstance.getClient()).getLoginName()%>--%>
					</td>
					<td>
						<a href="<%=context%>lli/client/board.do?id=<%=lliOwnerShipChangeApplication.getDstClient()%>">
							<%=AllClientRepository.getInstance().getClientByClientID(lliOwnerShipChangeApplication.getDstClient()).getLoginName()%>
						</a>
						<%--<%=AllClientRepository.getInstance().getClientByClientID(nixConnectionInstance.getClient()).getLoginName()%>--%>
					</td>
					<td><%=LLIConnectionConstants.applicationTypeNameMap.get(lliOwnerShipChangeApplication.getType())%>
					</td>
					<td style="color: <%=lliOwnerShipChangeApplication.getColor()%>;font-weight: bold">
						<%=lliOwnerShipChangeApplication.getStateDescription()%>
					</td>
					<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(
							lliOwnerShipChangeApplication.getSubmissionDate(), "dd-MM-YYYY")%>
					</td>
					<td>
						<a href="<%=context%>lli/ownershipChange/details.do?id=<%=lliOwnerShipChangeApplication.getId()%>">
							<%--<%=nixApplication.getId()%>--%>
							<span class="glyphicon glyphicon-list-alt"></span>
						</a>
					</td>
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

<script>
	var tid = setInterval( function () {
		if ( document.readyState !== 'complete' ) return;
		clearInterval( tid );
		$("#loading").hide();
	}, 200 );
</script>

