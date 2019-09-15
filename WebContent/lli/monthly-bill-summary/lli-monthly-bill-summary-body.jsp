<style type="text/css">
	#customarInfoTable,#billPriorInfoTable,#monthBillCalculated{
		/*  width:auto !important;*/
	}

</style>
<div id="monthly-bill-summary" v-cloak="true">
	<btcl-body title="monthly bill Summary" subtitle="Monthly Bill Summary">
        	<btcl-portlet>
           		<btcl-field title="Client ID" v-if ="isAdmin" >
					<lli-client-search :id_first=true :client.sync="monthlybillSummary.client"></lli-client-search>
           		</btcl-field>

				<btcl-field  title="Month">
					<input class="form-control datepicker" name='month' data-format="yyyy-mm" type=text > 
				</btcl-field>
				<!-- <btcl-field  title="Type">
					<Select>
						<option>Type1</option>
						<option>Type2</option>
					
					</Select>
				</btcl-field> -->
           		<button type=button class="btn green-haze btn-block btn-lg" @click="findSummaryOfTheMonth">Submit</button>           		         		 
           	</btcl-portlet>
	</btcl-body>
	 
     <div v-if="result" class="container" style= "background:white;" >

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
					<td >{{client.clientID}}</td>
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
		<div class="col-md-6 col-sm-6 col-xs-6" id="billPriorInfo">
			<h1 style="font-size:14px;">Connection Information</h1>
				<table id="billPriorInfoTable"
						class="table table-bordered table-striped table-hover"
						>
				   <tr>
						<td style="width:50%">Bill Issue Date</td>
						<td style="text-align:right;">{{billIssueDate}}</td>
					</tr>
					<tr>
						<td>Last Day Of Payment</td>
						<td style="text-align:right;">{{lastDayOfPayment}}</td>
					</tr>
					<tr>
						<td>Total Bandwidth</td>
						<td style="text-align:right;">{{mbpsBreakDownContent.value}} Mbps</td>
					</tr>
					<tr >
						<td>Applied Billing Range</td>
						<td style="text-align:right;">{{billingRangeBreakDownContent.fromValue}}
							<span>-</span>{{billingRangeBreakDownContent.toValue}} Mbps
						</td>
					</tr>
					<tr>
						<td>Applied MRC/Mbps</td>
						<td style="text-align:right;">{{billingRangeBreakDownContent.rate}} Tk</td>
					</tr>
					<tr>
						<td>Total Long Term Bandwidth</td>
						 <td style="text-align:right;">{{longTermContractBreakDownContent.value}} Mbps</td>
					</tr>
					<tr>
						<td>Applied MRC/Long Term Mbps</td>
						<td style="text-align:right;">{{longTermContractBreakDownContent.rate}} Tk</td>

					</tr>
					<tr >
						<td>Transmission Of Cache Bandwidth</td>
						<td style="text-align:right;">{{app.totalMbpsBreakDownForCache.value}} Mbps</td>

					</tr>
					<tr>
						<td>MRC/Cache Mbps</td>
						<td style="text-align:right;">{{app.billingRangeBreakDownForCache.rate}} Tk</td>
					</tr>
				</table>
		</div>
	</div>

		<div id="monthLyBillTable" class="col-md-12 col-sm-12 col-xs-12" v-if ="app.lliMonthlyBillSummaryByItems.length>0">

		<table id="thirdTable"
				class="table table-bordered table-striped table-hover">
			<thead style="background:lightblue;">
				<tr>
					<th  style="width:50%;">Particular</th>
					<th style="text-align:right;">Amount (BDT)</th>
				</tr>
			</thead>
		   <tbody>
			   <tr>
					<td>Bandwidth Charge</td>
					<td style="text-align:right;">{{takaFormat(regular)}} Tk</td>
			   </tr>
				<tr v-if="cache>0">
					<td>Transmission BW for Cache Charge</td>
					<td style="text-align:right;">{{takaFormat(cache)}} Tk</td>
			   </tr>
			   <tr>
					<td>Local Loop Charge</td>
					<td style="text-align:right;">{{takaFormat(localLoop)}} Tk</td>
			   </tr>
			   <tr v-if="adjustment != 0">
					<td>Adjustment</td>
					<td style="text-align:right;">{{takaFormat(adjustment)}} Tk</td>
			   </tr>
			   <tr v-if="dnAdjustment > 0">
					<td>Other Charges (Demand Note Transfer)</td>
					<td style="text-align:right;">{{takaFormat(dnAdjustment)}} Tk</td>
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
			<button type="button" class="btn green-haze btn-block" @click="viewMonthlyBillSummary"> View Monthly Bill Summary</button>
		 </div>

    </div>

</div>

<script src=../../assets/month-map-int-str.js></script>
	
<script src=../monthly-bill-summary/lli-monthly-bill-summary.js></script>
       

