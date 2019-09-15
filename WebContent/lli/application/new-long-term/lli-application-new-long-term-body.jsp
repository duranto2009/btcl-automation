<div id="btcl-application" v-cloak="true">
    <btcl-body title="New Long Term" subtitle="Application">
        <btcl-portlet>
            <btcl-field title="Client">
                <lli-client-search :client.sync="revise.client" :callback="getBW"></lli-client-search>
            </btcl-field>
            <btcl-info v-if="revise.bwData" title="Existing Active Bandwidth(Mbps)" :text.sync="revise.existingBW"></btcl-info>
            <btcl-info v-if="revise.bwData" title="Existing Long Term Bandwidth(Mbps)" :text.sync="revise.existingLT"></btcl-info>
            <btcl-input title="Bandwidth (Mbps)" :text.sync="revise.bandwidth"></btcl-input>
            <btcl-field title="Start Date">
                <btcl-datepicker :date.sync="revise.suggestedDate"></btcl-datepicker>
            </btcl-field>
            <btcl-input title="Description" :text.sync="revise.description"></btcl-input>
            <btcl-input title="Comment" :text.sync="revise.comment"></btcl-input>

            <div style="padding-top: 30px">
                <button  type="submit" class="btn green-haze btn-block" @click="submitData">Submit</button>
            </div>
        </btcl-portlet>
    </btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=new-long-term/lli-application-new-long-term.js></script>