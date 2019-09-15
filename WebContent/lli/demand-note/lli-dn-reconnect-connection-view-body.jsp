<div id=btcl-application>
	<btcl-body title="Demand Note View" subtitle='Reconnect Connection'>
	<btcl-portlet>
		<btcl-info title="Application ID" :text="app.applicationID"></btcl-info>
		<btcl-info title="Client" :text="app.client.label"></btcl-info>

		<btcl-info title="Reconnection Charge" :text="billDTO.reconnectionCharge"></btcl-info>
		<btcl-info title="Others" :text="billDTO.otherCost"></btcl-info>
		
		<btcl-info title="Total" :text="billDTO.grandTotal"></btcl-info>
		<btcl-info title="Discount %" :text="billDTO.discountPercentage"></btcl-info>
		<btcl-info title="Discount" :text="billDTO.discount"></btcl-info>
		<btcl-info title="Total (-discount)" :text="billDTO.totalPayable"></btcl-info>
		<btcl-info title="VAT %" :text="billDTO.VatPercentage"></btcl-info>
		<btcl-info title="VAT" :text="billDTO.VAT"></btcl-info>
		<btcl-info title="Net Payable (with VAT)" :text="billDTO.netPayable"></btcl-info>
		<btcl-info title="Status" :text="paymentStatus"></btcl-info>
		<div class=row>
			<div class="col-md-3 col-md-offset-1" v-for="btn in btnList">
				<button type=button class="btn btn-block green-haze" @click="submit(btn)">{{btn.title}}</button>
			</div>
		</div>
		<br>
		<div class=row>
			<button type=button class="btn btn-block green-haze btn-md" @click="viewPDF">View PDF</button>
		</div>	
		</btcl-portlet>	
	</btcl-body>
</div>
<script>
var billID = '<%=request.getParameter("id")%>';
</script>
<script src="../demand-note/lli-dn.js"></script>
<script src="../demand-note/lli-dn-reconnect-connection-view.js"></script>
