<%@page import="ip.ipUsage.IPBlockUsage"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="ip.IPInventory.IPBlockInventory" %>
<%
    String url = "ip/inventory/search";
    String navigator = SessionConstants.NAV_IP_INVENTORY;
    List<IPBlockInventory> data = (ArrayList<IPBlockInventory>) session.getAttribute(SessionConstants.VIEW_IP_INVENTORY);
    data = (data == null ? new ArrayList<IPBlockInventory>() : data);

%>
<jsp:include page="/includes/nav.jsp" flush="true">
    <jsp:param name="url" value="<%=url%>" />
    <jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<%
    Map<Long, String> regionMap = (Map<Long, String>) request.getAttribute("regionMap");
%>
<div id="btcl-application">
    <btcl-body title="Search" subtitle="IP Inventory">
        <btcl-portlet>
            <table class="table table-hover table-striped table-bordered">
                <thead>
                <tr>
                    <th>From</th>
                    <th>To</th>
                    <th>Version</th>
                    <th>Type</th>
                    <th>Region</th>
                    <th>Allocation Date</th>


                </tr>
                </thead>
                <tbody>
                <% for(IPBlockInventory block : data) {%>
                <tr>
                    <td><%=block.getFromIP() %></td>
                    <td><%=block.getToIP()%></td>

                    <td><%=block.getVersion()%></td>
                    <td><%=block.getType()%></td>
                    <td><%=regionMap.get(block.getRegionId())%></td>
                    <td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(block.getActiveFrom(), "dd/MM/yyyy")%></td>
                </tr>
                <%}%>

                </tbody>
            </table>
        </btcl-portlet>

    </btcl-body>

</div>
<script>
    new Vue({
        el:"#btcl-application",
        data: {

        }
    });

</script>
