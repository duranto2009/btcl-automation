<div id="btcl-application" v-cloak="true">
    <btcl-body title="Terminate Long Term" subtitle="Application">
        <btcl-portlet>
            <btcl-field title="Client">
                <lli-client-search :client.sync="revise.client" :callback="clientSelectionCallback">
                </lli-client-search>
            </btcl-field>

            <btcl-field title="Contract">
                <multiselect :options="revise.contractOptionListByClientID" v-model="revise.contract"
                             track-by=ID label=label :allow-empty="false" :searchable=true
                             open-direction="bottom" @select="getLongTermContractByID">
                </multiselect>
            </btcl-field>

            <btcl-info v-if="Object.keys(revise.existingContract).length !== 0" title="Existing Bandwidth"
                       :text.sync="revise.existingContract.bandwidth + ' Mbps'"></btcl-info>

            <btcl-input title="Bandwidth (Mbps)" :text.sync="revise.bandwidth"></btcl-input>

            <btcl-field title="Date">
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
<script src=break-long-term/lli-application-break-long-term.js></script>


