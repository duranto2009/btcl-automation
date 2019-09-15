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
<div id=monthly-bill-summary>
	<btcl-body title="monthly bill Summary" subtitle="Monthly Bill Summary">
        	<btcl-portlet>
           		<btcl-field title="Client" v-if ="isAdmin" >
					<lli-client-search :client.sync="monthlybillSummary.client">Select Client</lli-client-search>           		
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
	                    <td>Customer Name</td>
	                    <td>{{client.loginName}}</td>
	                </tr>
	                
	                <tr>
	                    <td>Customer Id</td>
	                    <td >{{client.clientID}}</td>
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
		                    <td>Bill Issue Date</td>
		                    <td>{{billIssueDate}}</td>
		                </tr>
		                <tr>
		                    <td>Last Day Of Payment</td>
		                	<td>{{lastDayOfPayment}}</td>
		                </tr>
		                <tr>
		                    <td>Total Bandwidth</td>
		                    <td>{{mbpsBreakDownContent.value}} Mbps</td>
		                </tr>
		                <tr > 
		                    <td>Applied Billing Range</td>
		                    <td>{{billingRangeBreakDownContent.fromValue}}<span>-</span>{{billingRangeBreakDownContent.toValue}}  Mbps</td>
		                </tr>
		                <tr>
		                    <td>Applied MRC/Mbps</td>
		                	<td>{{billingRangeBreakDownContent.rate}} Per Mbps</td>
		                </tr>
		                <tr>
		                    <td>Total Long Term Bandwidth</td>
		                	 <td>{{longTermContractBreakDownContent.value}} Mbps</td>
		                </tr>
		                <tr>
		                    <td>Applied MRC/Long Term Mbps</td>
		                    <td>{{longTermContractBreakDownContent.rate}} Per Mbps</td>
		
		                </tr>
		                <tr > 
		                    <td>Transmission Of Cache Bandwidth</td>
		                	<td>{{app.totalMbpsBreakDownForCache.value}} Mbps</td>
		
		                </tr>
		                <tr>
		                	<td>MRC/Cache Mbps</td>
		                	<td>{{app.billingRangeBreakDownForCache.rate}} Per Mbps</td>
		                </tr>
		            </table>
			</div>
        </div>
		
		<div id="customerInfo">
			<div>
           		<!-- <span>Customar Type:</span>
           		<span></span> -->
           		<span>Customer ID:</span>
           		<span>{{app.clientID}}</span>
           	</div>
		</div>
		
        <div id="monthLyBillTable" v-if ="app.lliMonthlyBillSummaryByConnections.length>0">
           <h1 style="font-size:14px;">Bill For Each Connection</h1>
        
            <table id="thirdTable"
            		class="table table-bordered table-striped table-hover">
                <thead>
                	
                    <tr>
                        <th>Bill Period</th>
                        <th>Connection Type</th>
                        <th>Mbps Used</th>
                        <th>MRC/Mbps</th>
                        <th>Total Charge</th>
                        <th>Discount Rate(%)</th>
                        <th>Discount</th>
                        <th>BW Bill </th>
                        <th>Local Loop Meter</th>
                        <th>Local Loop MRC/Meter</th>
                        <th>Used Core</th>
                        <th>Local Loop Bill Amount</th>    
                        <th>Total Bill(Without Vat)</th>                    
                        <th>Vat Rate (%)</th>
                        <th>Vat</th>
                        <th>Total Bill </th>         

                    </tr>
                </thead>
                <tbody>
         		   <tr v-for="row in sortedConnections">
                        <td>
                        	<span >{{row.period}}</span>
<!--                         	<span v-if="row.period==0">-</span>
 -->                        </td>
                        <td>
	                        <span>{{row.billType}}</span>
<!-- 	                        <span v-if="row.billType==0">-</span>
 -->	                    </td>
                        <td>
                        	<span v-if="row.totalMbps==0">-</span>
                        	<span v-else>{{row.totalMbps}}</span>
                        </td>                        
                        <td>
                        	<span v-if="row.mbpsRate==0">-</span>
                        	<span v-else>{{row.mbpsRate}}</span>
                        </td>
                        <td>
                        	<span v-if="row.mbpsCost==0">-</span>
                        	<span v-else>{{row.mbpsCost}}</span>
                        </td>
                        <td>
                        	<span v-if="row.discountRate==0">-</span>
                        	<span v-else>{{row.discountRate}}</span>
                        </td>                        
                        <td>
                        	<span v-if="row.discount==0">-</span>
                        	<span v-else>{{row.discount}}</span>
                        </td>
                        <td>                        	
                        	<span v-if="row.totalMbpsCost==0">-</span>                        
                        	<span v-else>{{row.totalMbpsCost}}</span>
                        </td>   
                        <td>
                        	<span v-if="row.loopLength==0">-</span>  
                        	<span v-else>{{row.loopLength}}</span>
                        </td>     
                        <td>
                        	<span v-if="row.loopRate==0">-</span>  
                        	<span v-else>{{row.loopRate}}</span>
                        </td>  
                        <td>
                        	<span v-if="row.ofcType>0">{{row.ofcType}}</span>
                        	<span v-if="row.ofcType==0">-</span>                        
                        </td> 
                        <td>
                        	<span v-if="row.loopCost==0">-</span>                        
                        	<span v-else>{{row.loopCost}}</span>
                        </td>
                        <td>
                        	<span v-if="row.grandCost==0">-</span>
                            <span v-else>{{row.grandCost}}</span>
                        </td>
                        <td>
                        	<span v-if="row.vatRate==0">-</span>
                            <span v-else>{{row.vatRate}}</span>
                        </td>
                        <td>
                        	<span v-if="row.vat==0">-</span>
                            <span v-else>{{row.vat}}</span>
                        </td>
                        <td>
                            <span v-if="row.totalCost==0">-</span>
                            <span v-else>{{row.totalCost}}</span>
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
				                    <td>Total Amount(Without Vat)</td>
				                    <td>{{app.grandTotal}}</td>
				                </tr>
				                <tr>
				                    <td>VAT</td>
				                    <td>{{app.VAT}}</td>
				                </tr>
				                <tr>
				                    <td>Payable Amount</td>
				                    <td>{{app.totalPayable}}</td>
				                </tr>
				                <tr>
				                    <td>Less Adjustable Amount</td>
				                    <td>-{{app.adjustmentAmount}}</td>
				                </tr>
				                <tr>
				                    <td>Net Payable</td>
				                    <td>{{app.netPayable}}</td>
				                </tr>
				               
				        </table>
		        </div>
        </div>
    </div>
</div>
<script src=../../assets/month-map-int-str.js></script>
	
<script src=../monthly-bill-summary/lli-monthly-bill-summary.js></script>
       

