<%@page import="util.ServiceDAOFactory"%>
<%@page import="lli.client.td.LLIClientTDService"%>
<%@page import="lli.LLIClientService"%>
<%@page import="org.joda.time.JodaTimePermission"%>
<%@page import="lli.client.td.LLIProbableTDClient"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@ page import="lli.Application.ReviseClient.ReviseDTO" %>
<%
	String url = "lli/revise/requestedtdlist";
	String context = "../" + request.getContextPath();
	String navigator = SessionConstants.NAV_LLI_CLIENT_REVISE;
	List<ReviseDTO> data = (ArrayList<ReviseDTO>) session.getAttribute(SessionConstants.VIEW_LLI_CLIENT_REVISE);
	data = (data == null ? new ArrayList<ReviseDTO>() : data);
	
%>
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<div id=btcl-application>
<btcl-body>
	<table class="table table-hover table-striped table-bordered">
		<thead>
			<tr>


				<th>Application ID</th>
				<th>Client Name</th>
				<th>Advised Date</th>
				<th>State</th>
				<%--<th>TD</th>--%>
			</tr>
		</thead>
		<tbody>
		<% for(ReviseDTO listItem : data) {
			String clientName = "";
			ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(listItem.getClientID());
			if(clientDTO != null) {
				clientName = clientDTO.getLoginName();
			}
			boolean isAlreadyTemporarilyDisconneced = ServiceDAOFactory.getService(LLIClientTDService.class).isClientTemporarilyDisconnected(listItem.getClientID());
			if(!isAlreadyTemporarilyDisconneced){
		%>
			<tr>

				<td>
					<%--<%if (lliApplication.getHasPermission()) {%>--%>
					<a href="<%=context%>revise/details.do?id=<%=listItem.getId() %>"><%=listItem.getId()%></a>
					<%--<% } else { %>--%>
					<%--<p><%=lliApplication.getApplicationID()%>--%>
					<%--</p>--%>
					<%--<%}%>--%>

				</td>
				<td><%=clientName %></td>
				<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(listItem.getSuggestedDate(), "dd/MM/yyyy") %></td>
				<td style="color: <%=listItem.getColor()%>;font-weight: bold"><%=listItem.getStateDescription()%></td>
				<%--<td><%= ((listItem.getSuggestedDate() - System.currentTimeMillis()) / TimeConverter.MILLS_IN_A_DAY) %></td>--%>
				<%--<td><button type=button class='btn form-control' @click=td(<%=listItem.getClientID()%>)>TD</button></td>--%>
			</tr>
		<%}
		}%>
		</tbody>
	</table>
</btcl-body>
	
</div>
<script>
var vue = new Vue({
	el : '#btcl-application',
	
	data : {
		context : context,	
		
	},
	methods : {
		td: function (id) {
			axios.post(this.context + "lli/td/td.do", {
				id : id,
			})
			.then(function(response){
				if(response.data.responseCode == 2) {
					toastr.error(response.data.msg);
				}else {
					toastr.success(response.data.msg);
				}
			})
			.catch(function(error){
				console.log(error);
			});
		}
	}
});
</script>
