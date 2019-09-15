<div id="btcl-application" v-cloak="true">
	<btcl-body title="LLI Demand Note" subtitle='Existing Connection' :loader="loading">
		<btcl-portlet>
			<btcl-info title="Application ID" :text.sync="applicationID"></btcl-info>
			<btcl-info title="Client Name" :text="app.client.label"></btcl-info>

			<btcl-input title="BW MRC (In Advance) (BDT)" :text.sync="dn.bandwidthCharge" readonly="true" required="true"></btcl-input>
			<btcl-input title="Discount %" :text.sync="dn.discountPercentage" required="true" :readonly="isCDGM==='false' ? true: false"></btcl-input>
			<btcl-info title="Discount" :text.sync="discount" readonly="true"></btcl-info>
			<btcl-input title="BW MRC after Discount (BDT)" :text.sync="deductDiscount"  readonly="true"></btcl-input>


			<btcl-input v-if="dn.securityMoney" title="Security Charge after Discount (BDT)" :text.sync="updatedSecurity" readonly="true" required="true"></btcl-input>
			<btcl-input v-else title="Security Charge (Govt/Semi-Govt/Autonomous)" :text.sync="dn.securityMoney" readonly="true" required="true"></btcl-input>


			<btcl-input title="Fiber OTC (BDT)" :text.sync="dn.fibreOTC" readonly="true"></btcl-input>
			<btcl-input title="Local Loop Charge (BDT)" :text.sync="dn.coreCharge" readonly="true"></btcl-input>

			<div align="center">
				<button type="button" class="btn btn-outline green-dark-stripe" @click="seeLocalLoopBreakdown"> See Local Loop Breakdown </button>
			</div>

			<btcl-input title="Downgrade Charge (BDT)" :text.sync="dn.downgradeCharge" readonly="true"></btcl-input>
			<btcl-input title="Port Charge (BDT)" :text.sync="dn.portCharge" readonly="true"></btcl-input>

			<btcl-input v-if = "app.applicationType.ID === 9" :title="firstIPTitle" :text.sync="dn.firstXIpCost" readonly="true"></btcl-input>
			<btcl-input v-else title="First X IP Charge (BDT)" :text.sync="dn.firstXIpCost" readonly="true"></btcl-input>
			<btcl-input v-if = "app.applicationType.ID === 9" :title="nextIPTitle" :text.sync="dn.nextYIpCost" readonly="true"></btcl-input>
			<btcl-input v-else title="Next Y IP Charge (BDT)" :text.sync="dn.nextYIpCost" readonly="true"></btcl-input>
			<btcl-input title="Shifting Charge (BDT)" :text.sync="dn.shiftCharge" readonly="true"></btcl-input>

			<btcl-info title="Others"></btcl-info>
			<btcl-field v-for="(otherItem, otherItemIndex) in otherItemList">
				<btcl-grid column=3>
					<btcl-input title="Item" :text.sync="otherItem.item" required="true"></btcl-input>
					<btcl-input title="Charge (BDT)" :text.sync="otherItem.cost" required="true"></btcl-input>
					<button type=button class="btn red btn-outline" @click="removeItem(otherItemIndex)">Remove Item</button>				
				</btcl-grid>
			</btcl-field>
			<btcl-field>
				<button type=button class="btn green-haze btn-outline" @click="addItem">Add Item</button>
			</btcl-field>

			<btcl-input title="Advanced Amount (BDT)" :text.sync="dn.advancedAmount"></btcl-input>
			<btcl-input title="Description" :text.sync="dn.description"></btcl-input>

			<btcl-info title="Sub Total (BDT)" :text.sync="totalPayable" readonly="true"></btcl-info>
			<%--<btcl-info title="VAT Calculable (without security)" :text.sync="vatCalculable" readonly="true"></btcl-info>--%>
			<btcl-input title="VAT % " :text.sync="dn.VatPercentage" required="true"></btcl-input>
			<btcl-info title="VAT (BDT)" :text.sync="vat" readonly="true"></btcl-info>
			<btcl-info title="Net payable (Sub Total + VAT) (BDT)" :text.sync="netPayable" required="true" readonly="true"></btcl-info>

			<button type=button class="btn green-haze btn-block btn-lg" @click="generateDN">Generate</button>
			
		</btcl-portlet>
		
	</btcl-body>
</div>
<script>
let appId = '<%=request.getParameter("id")%>';
let isCDGM = '<%=request.getAttribute("isCDGM")%>';
</script>
<script src="../demand-note/lli-dn.js"></script>
<script src="../demand-note/lli-dn-revise-connection.js"></script>
