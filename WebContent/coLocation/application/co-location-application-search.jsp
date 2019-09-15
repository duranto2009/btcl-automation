<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<%@ page import="lli.connection.LLIConnectionConstants" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Collections" %>
<%@ page import="coLocation.application.CoLocationApplicationDTO" %>
<%@ page import="coLocation.CoLocationConstants" %>
<%@ page import="coLocation.ifr.CoLocationApplicationIFRDTO" %>
<%@ page import="coLocation.ifr.CoLocationApplicationIFRService" %>
<%
	String url = "co-location/search";
	String context = request.getContextPath();
	String navigator = SessionConstants.NAV_COLOCATION_APPLICATION;
	List<CoLocationApplicationDTO> data = (ArrayList<CoLocationApplicationDTO>) session.getAttribute(SessionConstants.VIEW_COLOCATION_APPLICATION);



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
				<th>Details</th>
			</tr>
		</thead>
		<tbody>
		<% for(CoLocationApplicationDTO listItem : data) {
			String clientName = "";
			ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(listItem.getClientID());
			if(clientDTO != null) {
				clientName = clientDTO.getLoginName();
			}
//			boolean isAlreadyTemporarilyDisconneced = ServiceDAOFactory.getService(LLIClientTDService.class).isClientTemporarilyDisconnected(listItem.getClientID());
//			if(!isAlreadyTemporarilyDisconneced){
            LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
            boolean hasIFRAccess= true;

            boolean hasPermission = listItem.isHasPermission();

            if(
                    loginDTO.getRoleID() == CoLocationConstants.SPACE_OFC_ROLE ||
                    loginDTO.getRoleID() == CoLocationConstants.POWER_ROLE ||
                    loginDTO.getRoleID() == CoLocationConstants.DGM_BROADBAND
            ) {
                try {
                    hasIFRAccess = ServiceDAOFactory.getService(CoLocationApplicationIFRService.class).hasIFRAccess(listItem, loginDTO.getRoleID());
				}catch (Exception e){
                    e.getMessage();
				}
            }

            //check ifr request for power or space_ofc




		%>
			<tr>

				<td>
					<%if (hasPermission && hasIFRAccess) {%>
					<a href="<%=context%>/co-location/new-connection-application-details.do?id=<%=listItem.getApplicationID() %>"><%=listItem.getApplicationID()%></a>
					<% } else { %>
					<p><%=listItem.getApplicationID()%>
					</p>
					<%}%>

				</td>
				<td> <a href="<%=context%>/GetClientForView.do?moduleID=4&entityID=<%=clientDTO.getClientID()%>"><%=clientName %></a></td>
				<%--<td><%=clientName %></td>--%>
				<td><%=CoLocationConstants.applicationTypeNameMap.get(listItem.getApplicationType())%>
				<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(listItem.getSuggestedDate(), "dd/MM/yyyy") %></td>

                <td>
                    <span style="color: <%=listItem.getColor()%>;font-weight: bold"><%=listItem.getStateDescription()%></span>
                    <%--<span style="color: black;font-weight: bold"><%=listItem.getStateDescription()%></span>--%>
                    <%--<% if(listItem.isCompleted()) {%>--%>
                        <%--&nbsp;&nbsp;<a target="_blank" href="<%=context%>/lli/pdf/advicenote.do?applicationid=<%= listItem.getId() %>&type=long-term">[Advice&nbsp;Note&nbsp;&#x2197;]</a>--%>
                        <%--&nbsp;&nbsp;<a target="_blank" href="<%=context%>/lli/pdf/view.do?id=<%= listItem.getApplicationID() %>&type=advice-note">[Advice&nbsp;Note&nbsp;&#x2197;]</a>--%>
                    <%--<% } %>--%>
                </td>

				<td><a href="<%=context%>/co-location/new-connection-application-details-full.do?id=<%=listItem.getApplicationID() %>">
					<%--<%=lliApplication.getApplicationID()%>--%>
					<span class="glyphicon glyphicon-list-alt"></span>
				</a></td>

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
