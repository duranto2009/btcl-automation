<div id=btcl-application v-cloak="true">
	<btcl-body title="NIX Demand Note" subtitle='Connection' :loader="loading">
		<btcl-portlet>
			<btcl-info title="Application ID" :text='applicationID'></btcl-info>
			<btcl-info title="Client Name" :text="app.client.label"></btcl-info>

			<btcl-input title="Registration Charge" :text.sync="dn.registrationFee" readonly="true" required="true"></btcl-input>
			<btcl-input title="Port Charge" :text.sync="dn.nixPortCharge" required="true" readonly="true"></btcl-input>
			<btcl-input title="Discount % (on Port Charge)" :text.sync="dn.discountPercentage" ></btcl-input>
			<btcl-info title="Discount" :text.sync="discount" readonly="true"></btcl-info>
			<btcl-input title="Port Charge( -discount )" :text.sync="deductDiscount"  readonly="true"></btcl-input>

			<btcl-input v-if="dn.securityMoney" title="Security Charge ( -discount )" :text.sync="updatedSecurity" readonly="true" required="true"></btcl-input>
			<btcl-input v-else title="Security Charge ( Govt/Semi-Govt/Autonomous )" :text.sync="dn.securityMoney" readonly="true" required="true"></btcl-input>

			<btcl-input title="Local Loop Charge" :text.sync="dn.localLoopCharge" readonly="true" required="true"></btcl-input>
			<btcl-input title="Port Upgrade Charge" :text.sync="dn.nixPortUpgradeCharge" required="true" readonly="true"></btcl-input>
			<btcl-input title="Port Downgrade Charge" :text.sync="dn.nixPortDowngradeCharge" required="true" readonly="true"></btcl-input>
			<btcl-input title="Reconnect Charge" :text.sync="dn.reconnectCharge" required="true" readonly="true"></btcl-input>
			<btcl-input title="Closing Charge" :text.sync="dn.closingCharge" required="true" readonly="true"></btcl-input>



			<btcl-input title="Advance Adjustment" :text.sync="dn.advanceAdjustment"></btcl-input>
			<btcl-input title="Description" :text.sync="dn.description"></btcl-input>

			<btcl-info title="Sub Total" :text.sync="totalPayable" readonly="true"></btcl-info>
			<btcl-info title="VAT Calculable (without security)" :text.sync="vatCalculable" readonly="true"></btcl-info>
			<btcl-input title="VAT % " :text.sync="dn.VatPercentage" required="true"></btcl-input>
			<btcl-info title="VAT (without security)" :text.sync="vat" readonly="true"></btcl-info>
			<btcl-info title="Net payable (Sub Total + VAT)" :text.sync="netPayable" required="true" readonly="true"></btcl-info>

			<%--<btcl-info title="Total( - discount )" :text.sync="totalPayable" readonly="true"></btcl-info>--%>
			<%--<btcl-input title="VAT % " :text.sync="dn.VatPercentage" required="true"></btcl-input>--%>
			<%--<btcl-info title="VAT (without security)" :text.sync="vat" readonly="true"></btcl-info>--%>
			<%--<btcl-info title="Net payable ( with VAT )" :text.sync="netPayable" required="true" readonly="true"></btcl-info>--%>
			<button type=button class="btn green-haze btn-block btn-lg" @click="generateDN">Generate</button>
		</btcl-portlet>
	</btcl-body>

</div>
<script>
let appId = '<%=request.getParameter("id")%>';
</script>
<script src="../demand-note/nix-dn.js"></script>
<script src="../demand-note/nix-dn-connection.js"></script>