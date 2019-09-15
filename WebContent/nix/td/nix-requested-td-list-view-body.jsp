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
<%@ page import="nix.revise.NIXReviseDTO" %>
<%@ page import="nix.constants.NIXConstants" %>
<%
	String url = "lli/revise/requestedtdlist";
	String context = "../" + request.getContextPath();
	String navigator = SessionConstants.NAV_NIX_CLIENT_REVISE;
	List<NIXReviseDTO> data = (ArrayList<NIXReviseDTO>) session.getAttribute(SessionConstants.VIEW_NIX_CLIENT_REVISE);
	data = (data == null ? new ArrayList<NIXReviseDTO>() : data);
	
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
		<% for(NIXReviseDTO listItem : data) {
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
					<a href="<%=context%>revise/details.do?id=<%=listItem.getId() %>"><%=listItem.getId()%></a>
				</td>
				<td><%=clientName %></td>
				<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(listItem.getSuggestedDate(), "dd/MM/yyyy") %></td>
				<td style="color: <%=listItem.getColor()%>;font-weight: bold">
					<%--<%=listItem.getStateDescription()%>--%>
					<span style="color: <%=listItem.getColor()%>;font-weight: bold"><%=listItem.getStateDescription()%></span>
					<% if(listItem.getState() == NIXConstants.TD_AN_GENERATED||listItem.getState() == NIXConstants.RECONNECT_AN_GENERATED) {%>
					<%--&nbsp;&nbsp;<a target="_blank" href="<%=context%>/lli/pdf/advicenote.do?applicationid=<%= listItem.getId() %>&type=long-term">[Advice&nbsp;Note&nbsp;&#x2197;]</a>--%>
					&nbsp;&nbsp;<a target="_blank" href="<%=context%>/pdf/view/advice-note.do?appId=<%= listItem.getId() %>&module=9">[Advice&nbsp;Note&nbsp;&#x2197;]</a>
					<% } %>
				</td>
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
			axios.post(this.context + "nix/td/td.do", {
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
