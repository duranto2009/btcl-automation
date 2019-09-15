<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="lli.Application.ReviseClient.ReviseDTO"%>
<%@page import="lli.connection.LLIConnectionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%
	String url = "lli/revise/search";
	String context = request.getContextPath();
	String navigator = SessionConstants.NAV_LLI_CLIENT_REVISE;
	List<ReviseDTO> data = (ArrayList<ReviseDTO>) session.getAttribute(SessionConstants.VIEW_LLI_CLIENT_REVISE);
	data = (data == null ? new ArrayList<>() : data);
	
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
				<th>Application Type</th>
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
//			boolean isAlreadyTemporarilyDisconneced = ServiceDAOFactory.getService(LLIClientTDService.class).isClientTemporarilyDisconnected(listItem.getClientID());
//			if(!isAlreadyTemporarilyDisconneced){
		%>
			<tr>

				<td>
					<%if (listItem.isHasPermission()) {%>
					<a href="<%=context%>/lli/revise/newview.do?id=<%=listItem.getId() %>"><%=listItem.getId()%></a>
					<% } else { %>
					<p><%=listItem.getId()%>
					</p>
					<%}%>

				</td>
				<td><%=clientName %></td>
				<td><%=LLIConnectionConstants.applicationTypeNameMap.get(listItem.getApplicationType())%>
				<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(listItem.getSuggestedDate(), "dd/MM/yyyy") %></td>

                <td>
                    <span style="color: <%=listItem.getColor()%>;font-weight: bold"><%=listItem.getStateDescription()%></span>
                    <% if(listItem.getAdviceNoteId()>0) {%>
                        <%--&nbsp;&nbsp;<a target="_blank" href="<%=context%>/lli/pdf/advicenote.do?applicationid=<%= listItem.getId() %>&type=long-term">[Advice&nbsp;Note&nbsp;&#x2197;]</a>--%>
                        &nbsp;&nbsp;<a target="_blank" href="<%=context%>/pdf/view/advice-note.do?appId=<%= listItem.getId() %>&module=7">[Advice&nbsp;Note&nbsp;&#x2197;]</a>
                    <% } %>
                </td>

				<%--<td><%= ((listItem.getSuggestedDate() - System.currentTimeMillis()) / TimeConverter.MILLS_IN_A_DAY) %></td>--%>
				<%--<td><button type=button class='btn form-control' @click=td(<%=listItem.getClientID()%>)>TD</button></td>--%>
			</tr>
		<%}
//		}
		%>
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
