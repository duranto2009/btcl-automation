<style type="text/css">
    table {
        width: auto !important;
    }

    #thirdTable, #thirdTable > tbody > tr > td {
        border: 1px solid black;
    }

    #thirdTable > thead > tr > th {
        border: 1px solid black;
    }
</style>
<div id=monthly-usage-search>
    <btcl-body title="monthly Usage" subtitle="Monthly Usage">
        <btcl-portlet>
            <btcl-field title="Client" v-if="isAdmin">
                <vpn-client-search :client.sync="monthlyUsage.client">Client</vpn-client-search>
            </btcl-field>
            <btcl-field title="Month">
                <input type=text class="form-control datepicker" data-format="yyyy-mm" data-min-month="-1m"
                       name="month">
            </btcl-field>
            <button type=button class="btn green-haze btn-block btn-lg" @click="findUsageOfTheMonth">Submit</button>
        </btcl-portlet>
    </btcl-body>

    <div v-if="result" class="container" style="background:white;">
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="col-md-6 col-sm-6 col-xs-6">
                <h1 style="font-size:14px;">Customer Information</h1>
                <table id="customarInfoTable"
                       class="table table-bordered table-striped table-hover">
                    <tr>
                        <td>Customer Name</td>
                        <td>{{client.loginName}}</td>
                    </tr>

                    <tr>
                        <td>Customer Id</td>
                        <td>{{client.clientID}}</td>
                    </tr>

                    <tr>
                        <td>Customer Type</td>
                        <td><span>{{client.clientType}}</span>
                            <span>({{client.registrantType}})</span>
                            <span>({{client.registrantCategory}})</span>
                        </td>
                    </tr>

                </table>
            </div>


            <div class="col-md-6 col-sm-6 col-xs-6" id="billPriorInfo">
                <h1 style="font-size:14px;">Link Information</h1>
                <table id="billPriorInfoTable"
                       class="table table-bordered table-striped table-hover">
                    <tr>
                        <td>Usage Of Last Month</td>
                        <td>{{monthStr}}</td>
                        <td></td>
                    </tr>

                </table>

            </div>
        </div>
        <div id="monthLyBillTable" v-if="app.monthlyUsageByLinks.length>0"><!--  -->
            <h1 style="font-size:14px;">Usage and Bill For Each Link</h1>
            <div style="overflow-x:auto;">
            <table id="thirdTable"
                   class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                    <th>Link Id</th>
                    <th>Link Name</th>
                    <th>Link Type</th>
                    <th>Mbps BreakDown</th>
                    <th>Mbps Cost</th>
                    <th>Discount Rate (%)</th>
                    <th>Discount</th>
                    <th>Total Mbps Cost</th>
                    <th>Loop BreakDown</th>
                    <th>Loop Cost (Local End)</th>
                    <th>Loop Cost (Remote End)</th>
                    <th>Total Loop Cost</th>
                    <th>Total Cost (Without Vat)</th>
                    <th>Vat Rate (%)</th>
                    <th>Vat</th>
                    <th>Total Cost</th>
                    <%--<th>Remarks</th>--%>

                </tr>
                </thead>
                <tbody>
                <tr v-for="row in sortedLinks">

                    <td>{{row.linkId}}</td>
                    <td style="max-width:180px; word-wrap: break-word">{{row.linkName}}</td>

                    <td>{{row.linkType}}</td>

                    <td>
                        <div v-if="row.linkBandwidthBreakDowns.length>0" style="min-width:100px">
                            <li v-for="bdBreakDown in getLinkBandwidthBreakDownsContent(row.linkBandwidthBreakDownsContent)">
                                {{bdBreakDown.value}} ({{getStringDate(bdBreakDown.fromDate)}} -
                                {{getStringDate(bdBreakDown.toDate)}})
                            </li>
                        </div>
                        <div v-else>-</div>
                    </td>
                    <td>
                        <span v-if="row.mbpsCost>0">{{takaFormat(row.mbpsCost)}}</span>
                        <span v-else>-</span>
                    </td>
                    <td>
                        <span v-if="row.discountRate>0">{{takaFormat(row.discountRate)}}</span>
                        <span v-else>-</span>
                    </td>
                    <td>
                        <span v-if="row.discount>0">{{takaFormat(row.discount)}}</span>
                        <span v-else>-</span>
                    </td>

                    <td>
                        <span v-if="(row.mbpsCost - row.discount)>0">{{takaFormat(row.mbpsCost - row.discount)}}</span>
                        <span v-else>-</span>
                    </td>

                    <td>
                        <div v-if="row.localLoopBreakDowns.length>0" style="min-width:120px">
                            <li v-for="loopBreakDown in getLocalLoopBreakDownsContent(row.localLoopBreakDownsContent)">
                                {{loopBreakDown.value}} ({{getStringDate(loopBreakDown.fromDate)}} -
                                {{getStringDate(loopBreakDown.toDate)}})
                            </li>
                        </div>
                        <div v-else>-</div>
                    </td>

                    <td>
                        <span v-if="row.localEndLoopCost>0">{{takaFormat(row.localEndLoopCost)}}</span>
                        <span v-else>-</span>
                    </td>

                    <td>
                        <span v-if="row.remoteEndLoopCost>0">{{takaFormat(row.remoteEndLoopCost)}}</span>
                        <span v-else>-</span>
                    </td>

                    <td>
                        <span v-if="row.totalLoopCost>0">{{takaFormat(row.totalLoopCost)}}</span>
                        <span v-else>-</span>
                    </td>
                    <td>
                        <span v-if="row.grandCost>0">{{takaFormat(row.grandCost)}}</span>
                        <span v-else>-</span>
                    </td>
                    <td>
                        <span v-if="row.vatRate>0">{{takaFormat(row.vatRate)}}</span>
                        <span v-else>-</span>
                    </td>
                    <td>
                        <span v-if="row.vat>0">{{takaFormat(row.vat)}}</span>
                        <span v-else>-</span>
                    </td>
                    <td>
                        <span v-if="row.totalCost>0">{{takaFormat(row.totalCost)}}</span>
                        <span v-else>-</span>
                    </td>

                    <%--<td>--%>
                    <%--<div v-if="row.linkBandwidthBreakDowns.length>0" style="min-width:80px">--%>
                    <%--<li v-for="remark in getBWBreakDownsRemarks(row.linkBandwidthBreakDowns)">--%>
                    <%--{{remark}}--%>
                    <%--</li>--%>
                    <%--</div>--%>

                    <%--</td>--%>

                </tr>
                </tbody>
            </table>
            </div>
        </div>

        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="col-md-6  col-sm-6 col-xs-6">
                <!-- <span>Total Amount in Words:</span>
                <span>{{getAmountInWords(app.netPayable)}}</span> -->
            </div>
            <div class="col-md-6 col-sm-6 col-xs-6"
                 id="monthBillCalculated">
                <h1 style="font-size:14px;">Bill Calculation</h1>

                <table id="monthBillCalculatedTable"
                       class="table table-bordered table-striped table-hover">
                    <tr>
                        <td>Grand Total</td>
                        <td>{{takaFormat(app.grandTotal)}}</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>Discount</td>
                        <td>-{{takaFormat(app.discount)}}</td>
                        <td>({{takaFormat(app.discountPercentage)}}<span>%)</span></td>
                    </tr>
                    <tr>
                        <td>Total Bill</td>
                        <td>{{takaFormat(app.totalPayable)}}</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>VAT</td>
                        <td>{{app.VAT}}</td>
                        <td>({{takaFormat(app.VatPercentage)}}<span>%)</span></td>
                    </tr>

                    <tr>
                        <td>Net Bill</td>
                        <td>{{takaFormat(app.netPayable)}}</td>
                        <td></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script src=../../assets/month-map-int-str.js></script>

<script src=vpn-monthly-usage-search.js></script>
       

