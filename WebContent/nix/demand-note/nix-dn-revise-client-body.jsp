<div id=btcl-application v-cloak="true">
    <btcl-body title="Demand Note" subtitle='Reconnect Connection' :loader="loading">
        <btcl-portlet>
            <btcl-info title="Application ID" :text="applicationID"></btcl-info>
            <btcl-info title="Client" :text="app.client.label"></btcl-info>

            <btcl-input title="Reconnection Charge" :text.sync="dn.reconnectCharge" required="true" readonly="true"></btcl-input>

            <btcl-info title="Total" :text.sync="totalPayable" readonly="true"></btcl-info>
            <btcl-input title="VAT %" :text.sync="dn.VatPercentage" ></btcl-input>
            <btcl-info title="VAT" :text="vat" readonly="true"></btcl-info>
            <btcl-info title="Net payable (with VAT)" :text.sync="netPayable" readonly="true"></btcl-info>

            <button type=button class="btn green-haze btn-block btn-lg" @click="generateDN">Generate</button>
        </btcl-portlet>
    </btcl-body>
</div>
<script>
    let appId = '<%=request.getParameter("id")%>';
</script>
<script src="../demand-note/nix-dn.js"></script>
<script src="../demand-note/nix-dn-revise-client.js"></script>
