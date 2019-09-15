<%@ page import="upstream.UpstreamConstants" %>







<%@page import="util.TimeConverter" %>
<%@page import="common.repository.AllClientRepository" %>
<%@page import="lli.LLIConnectionInstance" %>
<%@page import="sessionmanager.SessionConstants" %>
<%@ page import="vpn.application.VPNApplication" %>
<%@ page import="application.Application" %>
<%@ page import="upstream.application.UpstreamApplication" %>
<%@ page import="upstream.contract.UpstreamContract" %>
<%@ page import="upstream.inventory.UpstreamInventoryService" %>
<%@ page import="util.ServiceDAOFactory" %>
<%@ page import="global.GlobalService" %>
<%@ page import="upstream.inventory.UpstreamInventoryItem" %>
<%@ page import="java.util.*" %>
<%
    String msg = null;
    String url = "upstream/contract-close";
    String navigator = SessionConstants.NAV_UPSTREAM_CONTRACT;
    String context = "" + request.getContextPath() + "/";
%>

<btcl-loader :loader = "loader">

    <div id="blah">
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
                            <th>Contract Id</th>

                            <th>Type of Bandwidth</th>
                            <th>Bandwidth Capacity (Gbps)</th>
                            <th>Media</th>
                            <th>BTCL Service Location</th>
                            <th>Provider Location</th>
                            <th>BW Price(USD)</th>
                            <th>OTC (USD)</th>
                            <th>MRC (USD)</th>
                            <th>Active From</th>
                            <th>Contract Duration To</th>
                            <%--                    <th>Application Date</th>--%>
                            <%--<th>Destination Client</th>--%>
                            <th>Close Contract</th>
                        </tr>
                        </thead>
                        <tbody>

                        <%
                            UpstreamInventoryService inventoryService = ServiceDAOFactory.getService(UpstreamInventoryService.class);
                            GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);

                            Map<Long, UpstreamInventoryItem> upstreamInventoryItemsMap = null;
                            try {
                                upstreamInventoryItemsMap = inventoryService.getAllUpstreamItemsMapById();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            List<UpstreamContract> data = (List<UpstreamContract>) session.getAttribute(SessionConstants.VIEW_UPSTREAM_CONTRACT);
                            if (data != null) {
                                for (int i = 0; i < data.size(); i++) {
                                    UpstreamContract upstreamContract = (UpstreamContract) data.get(i);
                        %>

                        <tr>
                            <td style="font-weight: bold">
                                <%--                        <a href="<%=context%>upstream/contract-details.do?id=<%=upstreamContract.getContractId() %>&historyId=<%=upstreamContract.getContractHistoryId() %>">--%>
                                <%=upstreamContract.getContractId()%>
                                <%--                        </a>--%>

                            </td>


                            <td>
                                <%=upstreamInventoryItemsMap.get(upstreamContract.getTypeOfBandwidthId()).getItemName()%>
                            </td>

                            <td>
                                <%=upstreamContract.getBandwidthCapacity()%>
                            </td>


                            <td>
                                <%--                        <%=globalService.findByPK(UpstreamInventoryItem.class, upstreamContract.getMediaId()).getItemName()%>--%>
                                <%=upstreamInventoryItemsMap.get(upstreamContract.getMediaId()).getItemName()%>

                            </td>


                            <td>
                                <%--                        <%=globalService.findByPK(UpstreamInventoryItem.class, upstreamContract.getBtclServiceLocationId()).getItemName()%>--%>
                                <%=upstreamInventoryItemsMap.get(upstreamContract.getBtclServiceLocationId()).getItemName()%>

                            </td>


                            <td>
                                <%--                        <%=globalService.findByPK(UpstreamInventoryItem.class, upstreamContract.getProviderLocationId()).getItemName()%>--%>
                                <%=upstreamInventoryItemsMap.get(upstreamContract.getProviderLocationId()).getItemName()%>

                            </td>

                            <td><%=upstreamContract.getBandwidthPrice()%>
                            </td>
                            <td><%=upstreamContract.getOtc()%>
                            </td>
                            <td><%=upstreamContract.getMrc()%>
                            </td>
                            <%--                    <td><%=upstreamContract.get%></td>--%>

                            <td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamContract.getActiveFrom(), "dd-MM-YYYY")%>
                                <%--<td><%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamApplication.getSuggestedDate(), "dd-MM-YYYY")%>--%>
                            </td>

                            <td>
                                <%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(upstreamContract.getActiveFrom() + Double.valueOf(upstreamContract.getContractDuration()).longValue(), "dd-MM-YYYY")%>
                                <%--                        <%=upstreamContract.getContractDuration()%>--%>
                            </td>

                            <td style="text-align: center;">
                                <a class="btn btn-danger"  onclick="get_contract_info(<%=upstreamContract.getContractId() %>); return false;" href="#">
                                    <span class="glyphicon glyphicon-plus"></span>
                                </a></td>
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
    </div>

</btcl-loader>











<div id="btcl-vpn" v-if="divShow">
    <template v-if="loading">
        <div class="row" style="text-align: center;margin-top:10%">
            <i class="fa fa-spinner fa-spin fa-5x"></i>
        </div>
    </template>
    <template v-else>
        <btcl-body title="Upstream" subtitle='Close Contract' v-cloak="true">
            <btcl-portlet>
                    <btcl-field title="Select Contract">
                        <div class="row">
                            <div class="col-md-8">
                                <multiselect placeholder="Select Contract" v-model="contract"
                                             :options="contractList" label=contractName :allow-empty="false"
                                             :searchable=true
                                             open-direction="bottom">

                                </multiselect>
                            </div>
                            <div class="col-md-4">
                                <button class="btn btn-primary" @click="showSearchField"> Show Search Options</button>
                            </div>
                        </div>

                    </btcl-field>

                <hr>
                <template v-if="contract">
                    <jsp:include page="../common/views/upstream-contract-information-view.jsp"/>
                    <btcl-input title="Comment" :text.sync="application.comment"
                                placeholder="Write Comment"></btcl-input>

                    <br/>
                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitNewConnection">Submit</button>
                </template>
            </btcl-portlet>


        </btcl-body>
    </template>
</div>
<script>
    function get_contract_info(id) {
        document.getElementById("blah").style.display = "none";
        vue.divShow = true;
        vue.getContractByContractId(id);
    }
</script>

<script src="../upstream/scripts/upstream-common.js" type="text/javascript"></script>
<script src="../upstream/scripts/upstream-contract-close-application.js" type="text/javascript"></script>
