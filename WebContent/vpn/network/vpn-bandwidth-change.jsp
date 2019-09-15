<div id="btcl-vpn">
    <template v-if="loading">
        <div class="row" style="text-align: center;margin-top:10%">
            <i class="fa fa-spinner fa-spin fa-5x"></i>
        </div>
    </template>
    <template v-else>
    <btcl-body title="VPN" subtitle='Bandwidth Change' v-cloak="true">
        <btcl-portlet>
            <btcl-field title="Client Name" required = "true">
                <user-search-by-module :client.sync="application.client" :module="moduleId">Client</user-search-by-module>
            </btcl-field>

            <template v-if="application.client!=null">
                <btcl-field v-if="application.client.registrantType==1" title="Want's to Pay DN(Connection Charge)" required = "true">
                    <multiselect v-model="application.skipPayment"
                                 :options="[{id:1,value:'Monthly Bill'},{id:0,value:'Instantly'}]"
                                 label=value :allow-empty="false" :searchable=true open-direction="bottom">
                    </multiselect>
                </btcl-field>
                <template v-if="linkList.length>0">
                    <btcl-field title="Links" required = "true">
                        <multiselect placeholder="Select Link" v-model="link"
                                     :options="linkList" label=linkName :allow-empty="false" :searchable=true
                                     open-direction="bottom"></multiselect>
                    </btcl-field>
                    <template v-if="link!=null">
                        <btcl-info title="Bandwidth(Mbps)" :text="link.linkBandwidth"></btcl-info>
                        <btcl-input title="Enter Changed Bandwidth(Mbps)" :text.sync="changedBandwidth" placeholder="Enter Bandwidth"
                                    :number="true" required = "true"></btcl-input>
                    </template>


                </template>

            </template>

            <%--<btcl-input title="Description" :text.sync="application.description" placeholder="Write Description"></btcl-input>--%>
            <btcl-input title="Comment" :text.sync="application.comment" placeholder="Write Comment"></btcl-input>
            <btcl-field title="Suggested Date" required = "true">
                <btcl-datepicker :date.sync="application.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>
            </btcl-field>
            <br>

            <button type=button class="btn green-haze btn-block btn-lg" @click="submitBandwidthChangeForm">Submit</button>
        </btcl-portlet>
    </btcl-body>
    </template>
</div>
<script src="../script/vpn-common.js" type="text/javascript"></script>
<script src="../script/vpn-bandwidth-change.js" type="text/javascript"></script>
