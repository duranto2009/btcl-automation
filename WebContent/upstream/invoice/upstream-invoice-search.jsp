<%@page import="util.TimeConverter" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="java.util.ArrayList" %>
<%@page import="lli.LLIConnectionInstance" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="vpn.application.VPNApplication" %>
<%@ page import="application.Application" %>
<%@ page import="java.util.List" %>
<%@ page import="upstream.application.UpstreamApplication" %>
<%@ page import="upstream.contract.UpstreamContract" %>
<%@ page import="upstream.inventory.UpstreamInventoryService" %>
<%@ page import="util.ServiceDAOFactory" %>
<%@ page import="global.GlobalService" %>
<%@ page import="upstream.inventory.UpstreamInventoryItem" %>
<%
    String msg = null;
    String url = "upstream/invoice-search";
    String navigator = SessionConstants.NAV_UPSTREAM_CONTRACT;
    String context = "" + request.getContextPath() + "/";
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
                    <th>SL No</th>
                    <th>Contract Id</th>

                    <th>Type of Bandwidth</th>
                    <th>Bandwidth Capacity (Gbps)</th>
                    <th>Media</th>
                    <th>BTCL Service Location</th>
                    <th>Provider Location</th>
                    <th>Active From</th>
                    <th>Contract Duration To</th>
                    <%--<th>Suggested Date</th>--%>
                    <%--<th>Destination Client</th>--%>
<%--                    <th>Details</th>--%>
                </tr>
                </thead>
                <tbody>

                <%
                    UpstreamInventoryService inventoryService = ServiceDAOFactory.getService(UpstreamInventoryService.class);
                    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
                    List<UpstreamContract> data = (List<UpstreamContract>) session.getAttribute(SessionConstants.VIEW_UPSTREAM_CONTRACT);
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            UpstreamContract upstreamContract = (UpstreamContract) data.get(i);
                %>

                <tr>
                    <td>
                        <%= i+1 %>
                    </td>
                    <td style="font-weight: bold">
                        <a href="<%=context%>upstream/contract-details.do?id=<%=upstreamContract.getContractId() %>&historyId=<%=upstreamContract.getContractHistoryId() %>">
                            <%=upstreamContract.getContractId()%>
                        </a>

                    </td>




                    <td>
                        <%=globalService.findByPK(UpstreamInventoryItem.class, upstreamContract.getTypeOfBandwidthId()).getItemName()%>
                    </td>

                    <td>
                    <%=upstreamContract.getBandwidthCapacity()%>
                    </td>


                    <td>
                        <%=globalService.findByPK(UpstreamInventoryItem.class, upstreamContract.getMediaId()).getItemName()%>
                    </td>


                    <td>
                        <%=globalService.findByPK(UpstreamInventoryItem.class, upstreamContract.getBtclServiceLocationId()).getItemName()%>
                    </td>


                    <td>
                        <%=globalService.findByPK(UpstreamInventoryItem.class, upstreamContract.getProviderLocationId()).getItemName()%>
                    </td>

                    <td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamContract.getActiveFrom(), "dd-MM-YYYY")%>
                        <%--<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamApplication.getSuggestedDate(), "dd-MM-YYYY")%>--%>
                    </td>

                    <td>
                        <%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamContract.getActiveFrom() + Double.valueOf(upstreamContract.getContractDuration()).longValue(), "dd-MM-YYYY")%>
                        <%--                        <%=upstreamContract.getContractDuration()%>--%>
                    </td>

<%--                    <td>--%>
<%--                        <a href="<%=context%>upstream/contract-details.do?id=<%=upstreamContract.getApplicationId() %>&type=details">--%>
<%--                            <span class="glyphicon glyphicon-list-alt"></span>--%>
<%--                        </a></td>--%>
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
