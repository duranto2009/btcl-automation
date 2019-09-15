<style type="text/css">
    table > tr > td {
        width: auto !important;
        white-space: nowrap;
    }

    #thirdTable, #thirdTable > tbody > tr > td {
        border: 1px solid black;
    }

    #thirdTable > thead > tr > th {
        border: 1px solid black;
    }
</style>

<div id=monthly-usage-search>
    <btcl-body title="monthly Usage" subtitle="NIX Monthly Usage">
        <btcl-portlet>
            <btcl-field title="Client" v-if="isAdmin">
                <nix-client-search :client.sync="monthlyUsage.client">Client</nix-client-search>
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
                        <td>
                            <span>{{client.clientType}}</span><br>
                            <span>({{client.registrantType}})</span><br>
                            <span>({{client.registrantCategory}})</span><br>
                        </td>
                    </tr>

                </table>
            </div>
        </div>
        <div id="monthLyBillTable"><!--  --> <%--v-if="app.monthlyUsageByConnections.length>0--%>
            <h1 style="font-size:14px;">Bill For Each Connection</h1>

            <table id="thirdTable"
                   class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                    <th>Connection Name</th>
                    <th>Port Type</th>
                    <th>Port Break Down</th>
                    <th>Cost</th>
                    <th>Discount Rate (%)</th>
                    <th>Discount</th>
                    <th>Local Loop BreakDown</th>
                    <th>Local Loop Cost</th>
                    <th>Total Cost (Without Vat)</th>
                    <th>Vat Rate (%)</th>
                    <th>Vat</th>
                    <th>Total Cost</th>
                    <th>Remarks</th>

                </tr>
                </thead>
                <tbody>
                <tr v-for="(row, rowIndex) in sortedConnections">

                    <td style="max-width:180px; word-wrap: break-word">{{row.name}}</td>

                    <td>
                        <p v-for="(port, portIndex) in JSON.parse(row.portBreakDownsContent)">
                            {{port.name}}
                        </p>
                    </td>
                    <td>
                        <p v-for="(port, portIndex) in JSON.parse(row.portBreakDownsContent)">
                            {{port.name}} ({{new Date(port.fromDate).toLocaleDateString()}} -- {{new
                            Date(port.toDate).toLocaleDateString()}})
                        </p>

                    </td>
                    <td>
                        <p v-for="(port, portIndex) in JSON.parse(row.portBreakDownsContent)">
                            {{takaFormat(port.cost)}}
                        </p>

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
                        <p v-for="(LL, LLIndex) in JSON.parse(row.localLoopBreakDownsContent)">
                            {{LL.id}} ({{new Date(LL.fromDate).toLocaleDateString()}} -- {{new
                            Date(LL.toDate).toLocaleDateString()}})
                        </p>
                    </td>

                    <td>
                        <p v-for="(LL, LLIndex) in JSON.parse(row.localLoopBreakDownsContent)">
                            {{parseFloat(LL.cost).toFixed(2)}}
                        </p>
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

                    <td>
                        {{row.remarks}}
                    </td>

                    <%--</tr>--%>
                </tr>
                </tbody>
            </table>
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
                    </tr>
                    <tr>
                        <td>Total Bill</td>
                        <td>{{takaFormat(app.totalPayable)}}</td>
                    </tr>
                    <tr>
                        <td>VAT</td>
                        <td>{{takaFormat(app.VAT)}}</td>
                    </tr>

                    <tr>
                        <td>Net Bill</td>
                        <td>{{takaFormat(app.netPayable)}}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script src=../../assets/month-map-int-str.js></script>

<script src=../monthly-usage/nix-monthly-usage-search.js></script>
       

