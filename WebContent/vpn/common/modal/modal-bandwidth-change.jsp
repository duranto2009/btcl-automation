<div class="modal fade" id="modal-bandwidth-change" role="dialog">
    <div class="modal-dialog modal-lg" style="width:80%;">
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center" style="background: #fab74d;padding: 10px;">
                    Bandwidth Change Correction
                </div>
                <div class="tools">
                    <a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                </div>
            </div>

            <div class="modal-body">
                <div class="portlet-body">
                    <btcl-body title="VPN" subtitle='Bandwidth Change' v-cloak="true">
                        <btcl-portlet>
                            <btcl-field title="Client Name">
                                <user-search-by-module :client.sync="application.client" :module="moduleId">Client</user-search-by-module>
                            </btcl-field>

                            <template v-if="application.client!=null">
                                <btcl-field v-if="application.client.registrantType==1"
                                            title="Want's to Pay DN(Connection Charge)">
                                    <multiselect v-model="application.skipPayment"
                                                 :options="[{id:1,value:'Monthly Bill'},{id:0,value:'Instantly'}]"
                                                 label=value :allow-empty="false" :searchable=true
                                                 open-direction="bottom">
                                    </multiselect>
                                </btcl-field>
                                <template v-if="linkList.length>0">
                                    <btcl-field title="Links">
                                        <multiselect placeholder="Select Link" v-model="link"
                                                     :options="linkList" label=linkName :allow-empty="false"
                                                     :searchable=true
                                                     open-direction="bottom">
                                        </multiselect>
                                    </btcl-field>
                                    <template v-if="link!=null">
                                        <btcl-info title="Bandwidth(Mbps)" :text="link.linkBandwidth"></btcl-info>
                                        <btcl-input title="Enter Changed Bandwidth(Mbps)" :text.sync="changedBandwidth"
                                                    placeholder="Enter Bandwidth"
                                                    :number="true"></btcl-input>
                                    </template>


                                </template>

                            </template>

                            <%--<btcl-input title="Description" :text.sync="application.description" placeholder="Write Description"></btcl-input>--%>
                            <btcl-input title="Comment" :text.sync="application.comment"
                                        placeholder="Write Comment"></btcl-input>
                            <btcl-field title="Suggested Date">
                                <btcl-datepicker :date.sync="application.suggestedDate"
                                                 pattern="yyyy-MM-dd"></btcl-datepicker>
                            </btcl-field>
                            <br>

                            <%--<button type=button class="btn green-haze btn-block btn-lg" @click="submitBandwidthChangeForm">Submit</button>--%>
                            <button type=button class="btn green-haze btn-block btn-lg" @click="submitBandwidthChangeForm">Submit</button>

                        </btcl-portlet>
                    </btcl-body>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success"
                        @click="postRequest()"
                >Save
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>