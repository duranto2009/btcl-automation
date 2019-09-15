<div id=btcl-application>
	<btcl-body title="LLI Demand Note View" subtitle='New Connection'>
		<btcl-portlet>
			<btcl-info title="Client" :text="app.client.label"></btcl-info>
			<btcl-info title="Connection" :text="appContent.name"></btcl-info>
			
			<btcl-info title="Security Deposit" :text="billDTO.securityMoney"></btcl-info>
			<btcl-info title="Registration Charge" :text="billDTO.registrationFee"></btcl-info>
			<btcl-info title="Bandwidth Charge" :text="billDTO.bwMRC"></btcl-info>
			<btcl-info title="Fiber OTC" :text="billDTO.fibreOTC"></btcl-info>
			<btcl-info title="Local Loop Charge" :text="billDTO.localLoopCharge"></btcl-info>
			<btcl-info v-for="item in billDTO.itemCosts" :title="item.item" :text="item.cost"></btcl-info>
			<btcl-info title="Advance Adjustment" :text="billDTO.advanceAdjustment"></btcl-info>
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
<script src="../demand-note/lli-dn-new-connection-view.js"></script>