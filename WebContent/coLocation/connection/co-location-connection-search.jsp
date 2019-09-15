<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="coLocation.CoLocationConstants"%>
<%@page import="coLocation.connection.CoLocationConnectionDTO"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="lli.client.td.LLIClientTDService"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%
	String url = "co-location/connection-search";
	String context = request.getContextPath();
	String navigator = SessionConstants.NAV_COLOCATION_CONNECTION;
	List<CoLocationConnectionDTO> data = (ArrayList<CoLocationConnectionDTO>) session.getAttribute(SessionConstants.VIEW_COLOCATION_CONENCTION);

//	sorting the data
//	if (data != null) {
//		Collections.sort(data, new Comparator<CoLocationApplicationDTO>() {
//			public int compare(CoLocationApplicationDTO o1, CoLocationApplicationDTO o2) {
////								if(o1.getHasPermission() == o2.getHasPermission()) return 0;
////								return o1.degree < o2.degree ? -1 : 1;
//				return Boolean.compare(o1.isHasPermission(), o2.isHasPermission());
//			}
//		});
//		Collections.reverse(data);
//	}
//
//
//	data = (data == null ? new ArrayList<CoLocationApplicationDTO>() : data);
//
%>
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<div id=btcl-application>
	<btcl-body>
		<btcl-portlet>
			<table class="table table-hover table-striped table-bordered">
				<thead>
				<tr>
					<th>Connection ID.</th>
					<th>Connection Name</th>
					<th>Connection Type</th>
					<th>Client Name</th>
					<th>Status</th>
					<th>Incident</th>
					<th>Active From</th>
				</tr>
				</thead>
				<tbody>
				<% for(CoLocationConnectionDTO listItem : data) {
					String clientName = "";
					ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(listItem.getClientID());
					if(clientDTO != null) {
						clientName = clientDTO.getLoginName();
					}
//			boolean isAlreadyTemporarilyDisconneced = ServiceDAOFactory.getService(LLIClientTDService.class).isClientTemporarilyDisconnected(listItem.getClientID());
//			if(!isAlreadyTemporarilyDisconneced){
				%>
					<tr>
						<td>
							<a href=
									   "${context}co-location/connection-details.do?id=<%=listItem.getID()%>&historyID=<%=listItem.getHistoryID()%>">
								<%=listItem.getID()%>
							</a>
						</td>
						<td><%=listItem.getName() %></td>
						<td><%=CoLocationConstants.connectionTypeNameMap.get(listItem.getConnectionType()) %></td>

						<td><%=clientName %></td>
						<td><%=CoLocationConstants.connectionStatusNameMap.get(listItem.getStatus())%></td>
						<td><%=CoLocationConstants.applicationTypeNameMap.get(listItem.getIncident())%></td>
						<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(listItem.getActiveFrom(), "dd/MM/yyyy") %></td>
					</tr>
				<%}%>
				</tbody>
			</table>
		</btcl-portlet>
	</btcl-body>
</div>

<script>
	 new Vue({
		 el: '#btcl-application',
	 });
</script>
