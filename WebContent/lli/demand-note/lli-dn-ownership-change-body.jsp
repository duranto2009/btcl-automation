<div id="btcl-application" v-cloak="true">
	<btcl-body title="Demand Note" subtitle='Ownership Change' :loader="loading">
		<btcl-portlet>
			<btcl-info title="Application ID" :text="applicationID"></btcl-info>
			<btcl-info title="From Client" :text="app.client.value"></btcl-info>
			<btcl-info title="To Client" :text="app.clientDst.value"></btcl-info>

			<btcl-input title="Ownership Change Charge" :text.sync="dn.ownerShipChangeCharge" readonly="true"></btcl-input>
			<%--<btcl-info title="Total" :text.sync="grandTotal" required="true"></btcl-info>--%>
			<!-- 			<btcl-input title="Discount %" :text.sync="dn.discountPercentage" required="true" :readonly="isCDGM==='false' ? true: false"></btcl-input> -->
			<!-- 		 	<btcl-info title="Discount" :text="discount"></btcl-info> -->
			<btcl-info title="Total" :text.sync="totalPayable" required="true"></btcl-info>
			<btcl-input title="VAT %" :text.sync="dn.VatPercentage" required="true"></btcl-input>
			<btcl-info title="VAT" :text="vat" required="true"></btcl-info>
			<btcl-info title="Net payable (with VAT)" :text.sync="netPayable" required="true"></btcl-info>

			<button type=button class="btn green-haze btn-block" @click="generateDN">Generate</button>
		</btcl-portlet>
	</btcl-body>
</div>
<script>
	let appId = '<%=request.getParameter("applicationID")%>';
    let nextState = '<%=request.getParameter("nextState")%>';
	let isCDGM = '<%=request.getAttribute("isCDGM")%>';
</script>
<script src="../demand-note/lli-dn.js"></script>
<script src="../demand-note/lli-dn-ownership-change.js"></script>
