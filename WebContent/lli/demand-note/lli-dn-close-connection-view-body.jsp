<div id=btcl-application>
	<btcl-body title="LLI Demand Note View" subtitle='Close Connection'>
		<btcl-info title="Client" :text="app.client.label"></btcl-info>
		<btcl-info title="Connection" :text="appContent.name"></btcl-info>
		
		<btcl-info title="Closing OTC" :text="billDTO.closingOTC"></btcl-info>
		<btcl-info title="Discount %" :text="billDTO.discountPercentage"></btcl-info>
		<btcl-info title="Discount" :text="billDTO.discount"></btcl-info>
		<btcl-info title="Other" :text="billDTO.otherCost"></btcl-info>
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
	</btcl-body>
</div>

<script>
var billID = '<%=request.getParameter("id")%>';	
</script>
<script src="../demand-note/lli-dn.js"></script>
<script src="../demand-note/lli-dn-close-connection-view.js"></script>