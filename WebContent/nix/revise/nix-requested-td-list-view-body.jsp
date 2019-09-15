<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="nix.revise.NIXReviseDTO"%>
<%@page import="nix.revise.NIXClientTDService"%>
<%@page import="nix.constants.NIXConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.List"%>
<%
	String url = "nix/revise/search";
	String context = request.getContextPath();
	String navigator = SessionConstants.NAV_NIX_CLIENT_REVISE;
	List<NIXReviseDTO> data = (ArrayList<NIXReviseDTO>) session.getAttribute(SessionConstants.VIEW_NIX_CLIENT_REVISE);
	if (data != null) {
		Collections.sort(data, new Comparator<NIXReviseDTO>() {
			public int compare(NIXReviseDTO o1, NIXReviseDTO o2) {
				return Boolean.compare(o1.isHasPermission(), o2.isHasPermission());
			}
		});
		Collections.reverse(data);
	}
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


				<th>Application ID.</th>
				<th>Client Name</th>
				<th>Application Type</th>
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
		%>
			<tr>

				<td>
					<%if (listItem.isHasPermission()) {%>
					<a href="<%=context%>/nix/revise/newview.do?id=<%=listItem.getId() %>"><%=listItem.getId()%></a>
					<% } else { %>
					<p><%=listItem.getId()%>
					</p>
					<%}%>

				</td>
				<td>
					<a href="<%=context%>/nix/client/board.do?id=<%=clientDTO.getClientID()%>">
						<%=clientName%>
					</a></td>
				<td><%=NIXConstants.nixapplicationTypeNameMap.get(listItem.getApplicationType())%>
				<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(listItem.getSuggestedDate(), "dd/MM/yyyy") %></td>

                <td>
                    <span style="color: <%=listItem.getColor()%>;font-weight: bold"><%=listItem.getStateDescription()%></span>
					<% if(listItem.getState() == NIXConstants.TD_AN_GENERATED
						||listItem.getState() == NIXConstants.RECONNECT_AN_GENERATED
						||listItem.getState() == NIXConstants.COMPLETE_RECONNECTION
						||listItem.getState() == NIXConstants.TD_COMPLETED
					) {%>
                        <%--&nbsp;&nbsp;<a target="_blank" href="<%=context%>/lli/pdf/advicenote.do?applicationid=<%= listItem.getId() %>&type=long-term">[Advice&nbsp;Note&nbsp;&#x2197;]</a>--%>
                        &nbsp;&nbsp;<a target="_blank" href="<%=context%>/pdf/view/advice-note.do?appId=<%= listItem.getId() %>&module=9">[Advice&nbsp;Note&nbsp;&#x2197;]</a>
                    <% } %>
                </td>

				<%--<td><%= ((listItem.getSuggestedDate() - System.currentTimeMillis()) / TimeConverter.MILLS_IN_A_DAY) %></td>--%>
				<%--<td><button type=button class='btn form-control' @click=td(<%=listItem.getClientID()%>)>TD</button></td>--%>
			</tr>
		<%}

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
