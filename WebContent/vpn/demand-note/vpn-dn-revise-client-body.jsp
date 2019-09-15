<div id=btcl-application>
    <btcl-body title="Demand Note" subtitle='Client Revision'>
        <btcl-portlet>
            <btcl-info title="Application ID" :text="applicationID"></btcl-info>
            <btcl-info title="Client" :text="app.client.label"></btcl-info>

            <btcl-input title="Reconnection Charge" :text.sync="dn.reconnectCharge"></btcl-input>
            <btcl-input title="Others" :text.sync="dn.otherCost"></btcl-input>
            <btcl-info title="Total" :text.sync="grandTotal" required></btcl-info>

            <btcl-input title="Discount %" :text.sync="dn.discountPercentage" required></btcl-input>
            <btcl-info title="Discount" :text="discount"></btcl-info>

            <btcl-info title="Total( - discount )" :text.sync="totalPayable" required></btcl-info>
            <btcl-input title="VAT %" :text.sync="dn.VatPercentage" required></btcl-input>
            <btcl-info title="VAT" :text="vat" required></btcl-info>
            <btcl-info title="Net payable (with VAT)" :text.sync="netPayable" required></btcl-info>

            <button type=button class="btn green-haze btn-block btn-lg" @click="generateDN">Generate</button>
        </btcl-portlet>
    </btcl-body>
</div>
<script>
    var appId = '<%=request.getParameter("id")%>';
</script>
<script src="../demand-note/vpn-dn.js"></script>
<script src="../demand-note/vpn-dn-revise-client.js"></script>
