
<div id=btcl-application v-cloak="true">
    
    <btcl-body title="VPN Demand Note" :loader="loading">
        <btcl-portlet >


                <%--<btcl-info title="Application ID" :text='applicationID'></btcl-info>--%>
                <%--<btcl-info title="Client Name" :text="app.client.label"></btcl-info>--%>
                <btcl-input title="Registration Charge" :text.sync="dn.registrationCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="OTC Local Loop Charge (For BTCL Loop Provider)" :text.sync="dn.otcLocalLoopBTCL" readonly="true" required="true"></btcl-input>
                <btcl-input title="Bandwidth Charge" :text.sync="dn.bandwidthCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="Discount % (on BW)" :text.sync="dn.discountPercentage" required="true"></btcl-input>
                <btcl-info title="Discount" :text.sync="discount" required="true"></btcl-info>
                <btcl-input title="Bandwidth Charge ( -discount )" :text.sync="deductDiscount" readonly="true" required="true"></btcl-input>
                <btcl-input v-if="dn.securityCharge" title="Security Charge ( - discount )" :text.sync="updatedSecurity" readonly="true" required="true"></btcl-input>
                <btcl-input v-else title="Security Charge ( Govt/Semi-Govt/Autonomous )" :text.sync="dn.securityCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="Local Loop Charge" :text.sync="dn.localLoopCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="Instant Degradation Charge" :text.sync="dn.degradationCharge"  readonly="true" required="true"></btcl-input>
                <btcl-input title="Reconnect Charge" :text.sync="dn.reconnectCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="Closing Charge" :text.sync="dn.closingCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="Shifting Charge" :text.sync="dn.shiftingCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="Ownership Change Charge" :text.sync="dn.ownershipChangeCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="Other Charge" :text.sync="dn.otherCharge" readonly="true" required="true"></btcl-input>
                <btcl-input title="Advance" :text.sync="dn.advance" readonly="true" required="true"></btcl-input>
                <btcl-input title="Description" :text.sync="dn.description"></btcl-input>

                <btcl-info title="Sub Total" :text.sync="totalPayable" required></btcl-info>
                <btcl-info title="VAT Calculable (without security)" :text.sync="vatCalculable" required></btcl-info>
                <btcl-input title="VAT % " :text.sync="dn.VatPercentage" required></btcl-input>
                <btcl-info title="VAT (without security)" :text.sync="vat" required></btcl-info>


                <btcl-info title="Net payable (Sub Total + VAT)" :text.sync="netPayable" required></btcl-info>
                <button type=button class="btn green-haze btn-block btn-lg" @click="generateDN">Generate</button>




        </btcl-portlet>
    </btcl-body>
</div>
<script>
    const id = '<%=request.getParameter("id")%>';
    const isGlobal = '<%=request.getParameter("global")%>';
    const appGroup = '<%=request.getParameter("appGroup")%>';
</script>
<script src="../demand-note/vpn-dn-connection.js" type="module"></script>