<%@page import="util.TimeConverter" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="lli.asn.ASN" %>
<%
	String msg = null;
	String url = "asn/search-asn";
	String navigator = SessionConstants.NAV_ASN;
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
					<th>ASN No</th>
					<th>Client</th>
					<th>Last Modified</th>
					<th>Edit</th>
				</tr>
				</thead>
				<tbody>
				<%
					List<ASN> data = ( List<ASN> )session.getAttribute(SessionConstants.VIEW_ASN);



					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							ASN asn = (ASN) data.get(i);
				%>
				<tr>
					<td style="font-weight: bold">
						<a href="<%=context%>asn/details-asn.do?id=<%=asn.getId() %>">
							<%=asn.getAsnNo()%>
						</a>
					</td>
					<td>
                        <%--//TODO Client Id--%>
							<%=
						AllClientRepository.getInstance().getClientByClientID(asn.getClient()).getLoginName()
						%>
					</td>

					<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(asn.getLastModifyTime(), "dd-MM-YYYY")%>
					<td style="font-weight: bold">
						<a href="<%=context%>asn/edit.do?id=<%=asn.getId() %>">Edit
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
