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

<%@page import="java.text.NumberFormat" %>
<%@page import="java.util.Locale" %>
<%@page import="inventory.InventoryConstants" %>

<div id=monthly-bill-search>
    <btcl-body title="NIX monthly bill" subtitle="Monthly Bill">
        <btcl-portlet>
            <btcl-field title="Client Name">
                <nix-client-search :client.sync="monthlybill.client">Select Client</nix-client-search>
            </btcl-field>
            <btcl-field title="Month">
                <input class="form-control datepicker" name='month' data-format="yyyy-mm" type=text>
            </btcl-field>

            <button type=button class="btn green-haze btn-block btn-lg" @click="findBillOfTheMonth">Submit</button>
        </btcl-portlet>
    </btcl-body>

    <div v-if="result" class="container" style="background:white;">
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="row">
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
                                <span>{{client.clientType}}</span> <br>
                                <span>({{client.registrantType}})</span> <br>
                                <span>({{client.registrantCategory}})</span> <br>
                            </td>
                        </tr>

                    </table>
                </div>

                <div class="col-md-6 col-sm-6 col-xs-6" id="billPriorInfo">
                    <h1 style="font-size:14px;">Port Information</h1>
                    <table id="billPriorInfoTable"
                           class="table table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <th>Port Type</th>
                            <th>Port Count</th>
                            <th>Port Cost</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="port in feeByPortTypeForClients">

                            <td>{{port.portType}}</td>
                            <td>{{port.portCount}}</td>
                            <td>{{takaFormat(port.portCost)}}</td>

                        </tr>

                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12" id="monthLyBillTable"
                     v-if="app.monthlyBillByConnections.length>0">
                    <h1 style="font-size:14px;">Bill For Each Connection</h1>

                    <table id="thirdTable"
                           class="table table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <th>Connection Name</th>
                            <th>Port Type</th>
                            <th>Port Count</th>
                            <th>Port Rate</th>
                            <th>Port Cost</th>
                            <th>Loop Rate</th>
                            <th>Loop Cost</th>
                            <th>Discount Rate (%)</th>
                            <th>Discount</th>
                            <th>Total Cost (Without Vat)</th>
                            <th>Vat Rate (%)</th>
                            <th>Vat</th>
                            <th>Total Cost</th>
                            <th>Remarks</th>

                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="row in sortedConnections">
                            <td style="max-width:250px; word-wrap: break-word">{{row.name}}</td>

                            <td>{{row.portTypeName}}</td>
                            <td>{{row.portCount}}</td>
                            <td>{{takaFormat(row.portRate)}}</td>
                            <td>{{takaFormat(row.portCost)}}</td>
                            <td>{{takaFormat(row.loopRate)}}</td>
                            <td>
                                <span v-if="row.loopCost>0">{{takaFormat(row.loopCost)}}</span>
                                <span v-if="row.loopCost==0">-</span>
                            </td>
                            <td>{{takaFormat(row.discountRate)}}</td>
                            <td>{{takaFormat(row.discount)}}</td>
                            <td>
                                <span v-if="row.grandCost>0">{{takaFormat(row.grandCost)}}</span>
                                <span v-if="row.grandCost==0">-</span>
                            </td>

                            <td>
                                <span v-if="row.vatRate>0">{{takaFormat(row.vatRate)}}</span>
                                <span v-if="row.vatRate==0">-</span>
                            </td>
                            <td>
                                <span v-if="row.vat>0">{{takaFormat(row.vat)}}</span>
                                <span v-if="row.vat==0">-</span>
                            </td>

                            <td>
                            <span v-if="row.totalCost>0">
                                {{takaFormat(row.totalCost)}}
                            </span>
                                <span v-if="row.totalCost==0">-</span>
                            </td>
                            <td>{{row.remark}}</td>

                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="col-md-6  col-sm-6 col-xs-6">

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
                                <td>Total Payable</td>
                                <td>{{takaFormat(app.totalPayable)}}</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>VAT</td>
                                <td>{{takaFormat(app.VAT)}}</td>
                                <td>{{takaFormat(app.VatPercentage)}}<span>%</span></td>
                            </tr>

                            <tr>
                                <td>Net Payable</td>
                                <td>{{takaFormat(app.netPayable)}}</td>
                                <td></td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src=../../assets/month-map-int-str.js></script>

<script src=nix-monthly-bill-search.js></script>
       

