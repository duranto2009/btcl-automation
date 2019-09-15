<%@page import="ip.ipUsage.IPBlockUsage"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%
    String url = "ip/usage/search";
    String navigator = SessionConstants.NAV_IP_USAGE;
    List<IPBlockUsage> data = (ArrayList<IPBlockUsage>) session.getAttribute(SessionConstants.VIEW_IP_USAGE);
    data = (data == null ? new ArrayList<>() : data);

%>
<jsp:include page="/includes/nav.jsp" flush="true">
    <jsp:param name="url" value="<%=url%>" />
    <jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>

<%
    Map<Long, String> regionMap = (Map<Long, String>) request.getAttribute("regionMap");
%>
<div id="btcl-application">
    <btcl-body title="Search" subtitle="IP Usage">
        <btcl-portlet>
            <table class="table table-hover table-striped table-bordered">
                <thead>
                <tr>
                    <th>From</th>
                    <th>To</th>
                    <th>Version</th>
                    <th>Purpose</th>
                    <th>Region</th>
                    <th>Allocation Date</th>


                </tr>
                </thead>
                <tbody>
                <% for(IPBlockUsage block : data) {%>
                <tr>
                    <td><%=block.getFromIP() %></td>
                    <td><%=block.getToIP()%></td>

                    <td><%=block.getVersion()%></td>
                    <td><%=block.getPurpose()%></td>
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
