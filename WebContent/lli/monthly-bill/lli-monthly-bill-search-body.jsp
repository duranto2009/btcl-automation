<style type="text/css">
	table{
		width:auto !important;
	}
	#thirdTable,#thirdTable > tbody > tr > td{
		border: 1px solid black;
	}
	#thirdTable > thead > tr > th{
		border: 1px solid black;
	}
</style>

<div id="monthly-bill-search" v-cloak="true">
	<btcl-body title="monthly bill" subtitle="Monthly Bill">
        	<btcl-portlet>
           		<btcl-field title="Client ID">
					<lli-client-search :id_first=true :client.sync="monthlybill.client"></lli-client-search>
           		</btcl-field>
<!--            		<btcl-field title="Select Month"> -->
<!--            			<btcl-monthpicker :date.sync="monthlybill.suggestedDate"></btcl-monthpicker> -->
				
<!--            		</btcl-field> -->
				<btcl-field  title="Month">
					<input class="form-control datepicker" name='month' data-format="yyyy-mm"  type=text > 
				</btcl-field>
				
           		<button type=button class="btn green-haze btn-block btn-lg" @click="findBillOfTheMonth">Submit</button>           		         		 
           	</btcl-portlet>
	</btcl-body>
	 
    <div v-if="result" class="container" style= "background:white;" >
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
	                	<td><span>{{client.clientType}}</span><span>({{client.registrantType}})</span><span>({{client.registrantCategory}})</span></td>
	                </tr>
	              
	            </table>
			 </div>
	        <div class="col-md-6 col-sm-6 col-xs-6" id="billPriorInfo">
	        	<h1 style="font-size:14px;">Connection Information</h1>
	            <table id="billPriorInfoTable"
	            		class="table table-bordered table-striped table-hover">
	                <tr>
	                    <td>Bill Of Month</td>
	                    <td>{{monthStr}}</td>
	                	<td></td>
	                </tr>
	                
	                <tr>
	                    <td>Total Mbps</td>
	                    <td>{{mbpsBreakDownContent.value}}</td>
	                	<td></td>
	                </tr>
	                
	                <tr>
	                    <td>Mbps in Billing Range</td>
	                    <td>{{billingRangeBreakDownContent.fromValue}}<span>-</span>{{billingRangeBreakDownContent.toValue}}</td>
	                	<td>{{billingRangeBreakDownContent.rate}} Per Mbps</td>
	                </tr>
	                
	                 <tr>
	                    <td>Total Cache Mbps</td>
	                    <td>{{app.totalMbpsBreakDownForCache.value}}</td>
	                	<td>{{app.billingRangeBreakDownForCache.rate}} Per Mbps</td>
	                </tr>
	               
	                <tr v-if ="longTermContractBreakDownContent.value>0"> <!-- the condition need to implement here  -->
	                    <td>Long Term Contract</td>
	                    <td>{{longTermContractBreakDownContent.value}}</td>
	                    <td>{{longTermContractBreakDownContent.rate}} Per Mbps</td>
	                </tr>
	            </table>

        	</div>
		</div>
        <div id="monthLyBillTable" v-if ="app.monthlyBillByConnections.length>0">
           <h1 style="font-size:14px;">Bill For Each Connection</h1>
        
            <table id="thirdTable"
            		class="table table-bordered table-striped table-hover">
                <thead>
                    <tr>
                        <th>Connection Name</th>
                        <th>Connection Type</th>
                        <th>Connection(Mbps)</th>
                        <th>MRC/Mbps</th>
                        <th>Mbps Cost</th>
                        <th>Discount Rate (%)</th>
                        <th>Discount </th>
                        <th>Total Mbps Cost</th>
                        <th>Loop Cost</th>
                        <th>Total Cost (Without Vat)</th>
                        <th>Vat Rate (%)</th>
                        <th>Vat</th>
                        <th>Total Cost</th>

                    </tr>
                </thead>
                <tbody>
                    <tr v-for="row in sortedConnections">
                        <td style="max-width:250px; word-wrap: break-word">{{row.name}}
                        	<!-- <span v-if="row.mbpsCost>0">{{row.mbpsCost}}</span>
                        	 <span v-if="row.mbpsCost==0">-</span>-->
                        </td>
                        <td>{{row.connectionType}}
<!--                         	<span v-if="row.mbpsCost>0">{{row.mbpsCost}}</span> -->
<!--                         	<span v-if="row.mbpsCost==0">-</span> -->
                        </td>
                        <td>
                        <span v-if="row.totalMbps>0">{{row.totalMbps}}</span>
                        	<span v-if="row.totalMbps==0">-</span>
                        </td>                        
                        <td>
                        	<span v-if="row.mbpsRate>0">{{takaFormat(row.mbpsRate)}}</span>
                        	<span v-if="row.mbpsRate==0">-</span>
                        </td>
                        <td>
                        	<span v-if="row.mbpsCost>0">{{takaFormat(row.mbpsCost)}}</span>
                        	<span v-if="row.mbpsCost==0">-</span>
                        </td>                      
                        <td>
                        	<span v-if="row.discountRate>0">{{takaFormat(row.discountRate)}}</span>
                        	<span v-if="row.discountRate==0">-</span>
                        </td>
                        <td>
                        	<span v-if="row.discount>0">{{takaFormat(row.discount)}}</span>
                        	<span v-if="row.discount==0">-</span>
                        </td>
                        <td>
                        	<span v-if="(row.mbpsCost - row.discount)>0">{{takaFormat(row.mbpsCost - row.discount)}}</span>
                        	<span v-else>-</span>
                        </td>          
                        <td>
                        	<span v-if="row.loopCost>0">{{takaFormat(row.loopCost)}}</span>
                        	<span v-if="row.loopCost==0">-</span>
                        </td>
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
                        	<span v-if="row.totalCost>0">{{takaFormat(row.totalCost)}}</span>
                        	<span v-if="row.totalCost==0">-</span>
                        </td>
                        
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
	                    <td>LTC Adjustment</td>
	                    <td>-{{takaFormat(app.longTermContructAdjustment)}}</td>
	                    <td></td>
	                </tr>
	                <tr>
	                    <td>Grand Total</td>
	                    <td>{{takaFormat(app.grandTotal)}}</td>
	                    <td></td>
	                </tr>
	             <!--    <tr>
	                    <td>Discount</td>
	                    <td>-{{app.discount}}</td>
	                    <td>{{app.discountPercentage}}<span>%</span></td>
	                </tr> -->
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
	                <!--<tr>
	                    <td>Late FeeByPortTypeForClient</td>
	                    <td>{{app.lateFee}}</td>
	                	<td></td>
	                </tr>
	                <tr>
	                    <td>Adjustment Amount</td>
	                    <td>-{{app.adjustmentAmount}}</td>
	               		<td></td>
	                </tr>-->
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
<script src=../../assets/month-map-int-str.js></script>
	
<script src=../monthly-bill/lli-monthly-bill-search.js></script>
       

