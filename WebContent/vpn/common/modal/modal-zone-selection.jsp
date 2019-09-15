<div class="modal fade" id="efrRequestToLocalDGMModal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 50%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <div>
                    <%--local end zone --%>
                    <template v-if="application.vpnApplicationLinks.length>0">
                        <template v-if="application.vpnApplicationLinks[0].hasOwnProperty('localOffice')">
                            <template v-if="application.vpnApplicationLinks[0].localOffice.localLoops">
                                <template v-if="application.vpnApplicationLinks[0].localOffice.localLoops.length>0">
                                    <%--is btcl is loop provider then select zone--%>
                                    <template
                                            v-if="application.vpnApplicationLinks[0].localOffice.localLoops[0].loopProvider==1&&application.vpnApplicationLinks[0].localOffice.localLoops[0].isCompleted!=true">
                                        <btcl-portlet>
                                            <btcl-field title="Local End Zone Selection">
                                                <%--<lli-zone-search :client.sync="localEndZone">Zone</lli-zone-search>--%>
                                                <template v-if="localEndZone">
                                                    <p>{{localEndZone.nameEng}}</p>
                                                </template>
                                                <template v-else>
                                                    <multiselect v-model="localEndZone"
                                                                 :options="zoneList"
                                                                 placeholder="Select Zone"
                                                                 label="nameEng"
                                                    <%--track-by="ID"--%>
                                                    ></multiselect>
                                                </template>


                                                <%--<multiselect v-model="localEndZone"--%>
                                                <%--:options="zoneList"--%>
                                                <%--placeholder="Select Zone"--%>
                                                <%--label="nameEng"--%>
                                                <%--&lt;%&ndash;track-by="ID"&ndash;%&gt;--%>
                                                <%--></multiselect>--%>
                                            </btcl-field>
                                        </btcl-portlet>
                                    </template>
                                </template>
                            </template>
                        </template>

                        <%--<btcl-input title="Comment" :text.sync="comment"></btcl-input>--%>
                    </template>
                    <%--remote end zone--%>
                    <template v-if="remoteOfficeExists()">
                        <%--if global--%>
                        <template v-if="localLinkIndex==-1">
                            <template
                                    v-for="(link, linkIndex) in application.vpnApplicationLinks"
                                    v-if="link.hasOwnProperty('remoteOffice')"

                            >
                                <btcl-field
                                        v-for="(localLoop, localLoopIndex) in link.remoteOffice.localLoops"
                                        :title="'Remote End Zone Selection (id:' + link.id + ')'"
                                        v-if="localLoop.loopProvider==1&&localLoop.isCompleted!=true"
                                >
                                    <%--<lli-zone-search :client.sync="localLoop.zone">Zone</lli-zone-search>--%>
                                    <template v-if="localLoop.zone">
                                        <p>{{localLoop.zone.nameEng}}</p>
                                    </template>
                                    <template v-else>
                                        <multiselect v-model="localLoop.zone"
                                                     :options="zoneList"
                                                     placeholder="Select Zone"
                                                     label="nameEng"
                                        <%--track-by="ID"--%>
                                        ></multiselect>
                                    </template>

                                </btcl-field>
                                <%--<btcl-input title="Comment" :text.sync="comment"></btcl-input>--%>
                            </template>
                        </template>

                        <%--if specific link --%>
                        <template v-else>
                            <template
                                    v-for="(link, linkIndex) in application.vpnApplicationLinks"
                            >
                                <btcl-field
                                        :title="'Remote End Zone Selection (id:' + link.id + ')'"
                                        v-for="(localLoop, localLoopIndex) in link.remoteOffice.localLoops"
                                        v-if="(linkIndex==localLinkIndex)&&(localLoop.loopProvider==1&&localLoop.isCompleted!=true)"
                                <%--v-if="localLoop.loopProvider==1&&localLoop.isCompleted!=true"--%>

                                >
                                    <%--<lli-zone-search :client.sync="link.zone">Zone</lli-zone-search>--%>
                                    <template v-if="localLoop.zone">
                                        <p>{{localLoop.zone.nameEng}}</p>
                                    </template>
                                    <template v-else>
                                        <multiselect v-model="localLoop.zone"
                                                     :options="zoneList"
                                                     placeholder="Select Zone"
                                                     label="nameEng"
                                        <%--track-by="ID"--%>
                                        ></multiselect>
                                    </template>
                                </btcl-field>
                                <%--<btcl-input title="Comment" :text.sync="comment"></btcl-input>--%>
                            </template>
                        </template>


                        <btcl-input title="Comment" :text.sync="comment"></btcl-input>
                    </template>

                </div>


            </div>
            <div class="modal-footer">
                <%--<button type=button class="btn btn-success" @click="processRequestForEFR('new-connection-process')">Save</button>--%>
                <button type=button class="btn btn-success"
                        @click="zoneSelectionModalSubmit()">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>



<div class="modal fade" id="efrRequestToLocalDGMModalSelectZone" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 50%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <div>
                    <%--local end zone --%>
                    <template v-if="application.vpnApplicationLinks.length>0">
                        <template v-if="application.vpnApplicationLinks[0].hasOwnProperty('localOffice')">
                            <template v-if="application.vpnApplicationLinks[0].localOffice.localLoops">
                                <template v-if="application.vpnApplicationLinks[0].localOffice.localLoops.length>0">
                                    <%--is btcl is loop provider then select zone--%>
                                    <template
                                            v-if="application.vpnApplicationLinks[0].localOffice.localLoops[0].loopProvider==1
                                            &&application.vpnApplicationLinks[0].localOffice.localLoops[0].isCompleted!=true
                                            &&application.vpnApplicationLinks[0].localOffice.localLoops[0].ifrFeasibility==true"
                                    >
                                        <btcl-portlet>
                                            <btcl-field title="Local End Zone Selection">
                                                <%--<lli-zone-search :client.sync="localEndZone">Zone</lli-zone-search>--%>

                                                    <multiselect v-model="localEndZone"
                                                                 :options="zoneList"
                                                                 placeholder="Select Zone"
                                                                 label="nameEng"
                                                    <%--track-by="ID"--%>
                                                    ></multiselect>



                                                <%--<multiselect v-model="localEndZone"--%>
                                                <%--:options="zoneList"--%>
                                                <%--placeholder="Select Zone"--%>
                                                <%--label="nameEng"--%>
                                                <%--&lt;%&ndash;track-by="ID"&ndash;%&gt;--%>
                                                <%--></multiselect>--%>
                                            </btcl-field>
                                        </btcl-portlet>
                                    </template>
                                </template>
                            </template>
                        </template>

                        <%--<btcl-input title="Comment" :text.sync="comment"></btcl-input>--%>
                    </template>
                    <%--remote end zone--%>
                    <template v-if="remoteOfficeExists()">
                        <%--if global--%>
                        <template v-if="localLinkIndex==-1">
                            <template
                                    v-for="(link, linkIndex) in application.vpnApplicationLinks"
                                    v-if="link.hasOwnProperty('remoteOffice')">
                                <btcl-field
                                        v-for="(localLoop, localLoopIndex) in link.remoteOffice.localLoops"
                                        :title="'Remote End Zone Selection (id:' + link.id + ')'"
                                        v-if="localLoop.loopProvider==1
                                        &&localLoop.isCompleted!=true
                                        &&localLoop.ifrFeasibility==true">
                                    <%--<lli-zone-search :client.sync="localLoop.zone">Zone</lli-zone-search>--%>


                                        <multiselect v-model="localLoop.zone"
                                                     :options="zoneList"
                                                     placeholder="Select Zone"
                                                     label="nameEng"
                                        <%--track-by="ID"--%>
                                        ></multiselect>


                                </btcl-field>
                                <%--<btcl-input title="Comment" :text.sync="comment"></btcl-input>--%>
                            </template>
                        </template>

                        <%--if specific link --%>
                        <template v-else>
                            <template
                                    v-for="(link, linkIndex) in application.vpnApplicationLinks"
                            >
                                <btcl-field
                                        :title="'Remote End Zone Selection (id:' + link.id + ')'"
                                        v-for="(localLoop, localLoopIndex) in link.remoteOffice.localLoops"
                                        v-if="(linkIndex==localLinkIndex)&&(
                                        localLoop.loopProvider==1
                                        &&localLoop.isCompleted!=true
                                        &&localLoop.ifrFeasibility==true
                                        )"
                                <%--v-if="localLoop.loopProvider==1&&localLoop.isCompleted!=true"--%>
                                >
                                    <%--<lli-zone-search :client.sync="link.zone">Zone</lli-zone-search>--%>

                                        <multiselect v-model="localLoop.zone"
                                                     :options="zoneList"
                                                     placeholder="Select Zone"
                                                     label="nameEng"
                                        <%--track-by="ID"--%>
                                        ></multiselect>

                                </btcl-field>
                                <%--<btcl-input title="Comment" :text.sync="comment"></btcl-input>--%>
                            </template>
                        </template>


                        <btcl-input title="Comment" :text.sync="comment"></btcl-input>


                    </template>

                </div>


            </div>
            <div class="modal-footer">
                <%--<button type=button class="btn btn-success" @click="processRequestForEFR('new-connection-process')">Save</button>--%>
                <button type=button class="btn btn-success"
                        @click="zoneSelectionModalSubmit()">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>