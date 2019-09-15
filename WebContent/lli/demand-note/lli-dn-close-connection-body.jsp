<div id="btcl-application">
	<btcl-body title="LLI Demand Note" subtitle='Close Connection' :loader="loading">
		<btcl-portlet>
			<btcl-info title="Application ID" :text="applicationID"></btcl-info>
			<btcl-info title="Closing Date" :text="app.suggestedDate" date="trueN"></btcl-info>
			<btcl-info title="Client Name" :text="app.client.label"></btcl-info>
			<btcl-input title="Closing OTC" :text.sync="dn.closingOTC" required="true"></btcl-input>
			<btcl-input title="Description" :text.sync="dn.description" required="true"></btcl-input>
			<btcl-input title="Other Cost" :text.sync="dn.otherCost"></btcl-input>
			<btcl-info title="Total" :text.sync="grandTotal" required="true"></btcl-info>
<!-- 			<btcl-input title="Discount %" :text.sync="dn.discountPercentage" required="true" :readonly="isCDGM==='false' ? true: false"></btcl-input> -->
<!-- 			<btcl-info title="Discount" :text.sync="discount" required="true"></btcl-info> -->			
			<btcl-info title="Total( - discount )" :text.sync="totalPayable" required="true"></btcl-info>
			<btcl-input title="VAT %" :text.sync="dn.VatPercentage" required="true"></btcl-input>
			<btcl-info title="VAT" :text.sync="vat" required="true"></btcl-info>
			<btcl-info title="Net payable ( with VAT )" :text.sync="netPayable" required="true"></btcl-info>
			<button type=button class="btn green-haze btn-block btn-lg" @click="generateDN">Generate</button>
		</btcl-portlet>
	</btcl-body>
</div>
<script>
let appId = '<%=request.getParameter("id")%>';
let isCDGM = '<%=request.getAttribute("isCDGM")%>';
</script>
<script src="../demand-note/lli-dn.js"></script>
<script src="../demand-note/lli-dn-close-connection.js"></script>