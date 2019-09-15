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
<%@ page import="coLocation.inventory.CoLocationInventoryDTO" %>
<%
	String url = "co-location/inventory-search";
	String context = request.getContextPath();
	String navigator = SessionConstants.NAV_COLOCATION_INVENTORY;
	List<CoLocationInventoryDTO> data = (ArrayList<CoLocationInventoryDTO>) session.getAttribute(SessionConstants.VIEW_COLOCATION_INVENOTRY);

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


	data = (data == null ? new ArrayList<CoLocationInventoryDTO>() : data);

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


				<th>Category </th>
				<th>Type</th>
				<th>Amount</th>
				<th>Brand</th>
				<th>Model</th>
				<%--<th>TD</th>--%>
			</tr>
			</thead>
			<tbody>
			<% for(CoLocationInventoryDTO listItem : data) {
				String clientName = "";
//				ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(listItem.getCatagoryID());
//				if(clientDTO != null) {
//					clientName = clientDTO.getLoginName();
//				}
//				boolean isAlreadyTemporarilyDisconneced = ServiceDAOFactory.getService(LLIClientTDService.class).isClientTemporarilyDisconnected(listItem.getClientID());
//			if(!isAlreadyTemporarilyDisconneced){
			%>
			<tr>

				<td>
					<%--<%if (listItem.isHasPermission()) {%>--%>
					<%--<a href="<%=context%>/co-location/new-connection-application-details.do?id=<%=listItem.getApplicationID() %>"><%=listItem.getApplicationID()%></a>--%>
					<%--<% } else { %>--%>
					<p><%=CoLocationConstants.categoryNameMap.get(listItem.getCatagoryID().intValue())%></p>
					<%--<%}%>--%>

				</td>
				<td><%=listItem.getType() %></td>
				<td><%=listItem.getAvailableAmount()%>
				<td><%=listItem.getName() %></td>
				<td><%=listItem.getModel() %></td>

				<%--<td>--%>
					<%--<span style="color: black;font-weight: bold"><%=listItem.getModel()%></span>--%>
					<%--&lt;%&ndash;<span style="color: black;font-weight: bold"><%=listItem.getStateDescription()%></span>&ndash;%&gt;--%>
					<%--&lt;%&ndash;<% if(listItem.isCompleted()) {%>&ndash;%&gt;--%>
					<%--&lt;%&ndash;&nbsp;&nbsp;<a target="_blank" href="<%=context%>/lli/pdf/advicenote.do?applicationid=<%= listItem.getId() %>&type=long-term">[Advice&nbsp;Note&nbsp;&#x2197;]</a>&ndash;%&gt;--%>
					<%--&lt;%&ndash;&nbsp;&nbsp;<a target="_blank" href="<%=context%>/lli/pdf/view.do?id=<%= listItem.getApplicationID() %>&type=advice-note">[Advice&nbsp;Note&nbsp;&#x2197;]</a>&ndash;%&gt;--%>
					<%--&lt;%&ndash;<% } %>&ndash;%&gt;--%>
				<%--</td>--%>

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
