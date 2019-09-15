<style type="text/css">
    #customarInfoTable, #billPriorInfoTable, #monthBillCalculated {
        /*  width:auto !important;*/
    }

</style>

<div id=monthly-bill-summary>
    <btcl-body title="NIX monthly bill Summary" subtitle="Monthly Bill Summary">
        <btcl-portlet>
            <btcl-field title="Client" v-if="isAdmin">
                <nix-client-search :client.sync="monthlybillSummary.client">Select Client</nix-client-search>
            </btcl-field>

            <btcl-field title="Month">
                <input class="form-control datepicker" name='month' data-format="yyyy-mm" type=text>
            </btcl-field>

            <button type=button class="btn green-haze btn-block btn-lg" @click="findSummaryOfTheMonth">Submit</button>
        </btcl-portlet>
    </btcl-body>

    <div v-if="result" class="container" style="background:white;">

        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="col-md-6 col-sm-6 col-xs-6">
                <h1 style="font-size:14px;">Customer Information</h1>
                <table id="customarInfoTable"
                       class="table table-bordered table-striped table-hover">
                    <tr>
                        <td style="width:50%;">Customer Name</td>
                        <td>{{client.loginName}}</td>
                    </tr>

                    <tr>
                        <td>Customer Id</td>
                        <td>{{client.clientID}}</td>
                    </tr>

                    <tr>
                        <td>Customer Type</td>
                        <td><span>{{client.clientType }}</span><span> ({{client.registrantType}})</span></td>
                    </tr>
                    <tr>
                        <td>Customer Category</td>
                        <td><span>{{client.registrantCategory}}</span></td>
                    </tr>

                </table>
            </div>

        </div>

        <div id="monthLyBillTable" class="col-md-12 col-sm-12 col-xs-12"
             v-if="app.nixMonthlyBillSummaryByItems.length>0">

            <table id="thirdTable"
                   class="table table-bordered table-striped table-hover">
                <thead style="background:lightblue;">
                <tr>
                    <th style="width:50%;">Particular</th>
                    <th style="text-align:right;">Amount (BDT)</th>
                </tr>
                </thead>
                <tbody>


                <tr v-for="row in particulars">

                    <td>{{row.billType}}</td>
                    <td style="text-align:right;">{{takaFormat(row.grandCost)}}</td>

                </tr>

                <tr v-if="adjustmentAmount > 0">
                    <td>Other </td>
                    <td style="text-align:right;">{{takaFormat(adjustmentAmount)}} Tk</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="col-md-6 col-sm-6 col-xs-6"></div>
            <div class="col-md-6 col-sm-6 col-xs-6"
                 id="monthBillCalculated">
                <table id="monthBillCalculatedTable"
                       class="table table-bordered table-striped table-hover"
                       style="text-align:right;">
                    <tr>
                        <td style="width:50%;">Sub Total Amount</td>
                        <td>{{takaFormat(app.grandTotal)}} Tk</td>
                    </tr>
                    <tr>
                        <td>Adjustable Amount (-)</td>
                        <td>{{takaFormat(app.adjustmentAmount)}} Tk</td>
                    </tr>
                    <tr>
                        <td>Total Payable</td>
                        <td>{{takaFormat(app.totalPayable)}} Tk</td>
                    </tr>
                    <tr>
                        <td>VAT (+)</td>
                        <td>{{takaFormat(app.VAT)}} Tk</td>
                    </tr>


                    <tr>
                        <td>Net Payable</td>
                        <td>{{takaFormat(app.netPayable)}} Tk</td>
                    </tr>

                </table>
            </div>
        </div>
        <div class="text-center">
            <button type="button" class="btn green-haze btn-block" @click="viewMonthlyBillSummary"> View Monthly Bill
                Summary
            </button>
        </div>

    </div>

</div>

<script src=../../assets/month-map-int-str.js></script>

<script src=../monthly-bill-summary/nix-monthly-bill-summary.js></script>
       

