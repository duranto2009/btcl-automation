<div id="btcl-vpn-application" v-cloak>

    <%--Application Details--%>
    <btcl-body title="VPN" subtitle='Network Link Details'>
        <%--{{}}--%>

        <div class="row">
            <div class="col-md-8">
                <btcl-portlet title=Connection>
                    <%--<btcl-info title="Client" :text=connection.client.value></btcl-info>--%>
                    <btcl-info title="Link Name" :text=link.linkName></btcl-info>
                    <btcl-info title="Bandwidth" :text="link.linkBandwidth + ' Mbps'"></btcl-info>
                    <%--<btcl-info title="Active From" :text="connection.activeFrom" v-bind:is-date="true"></btcl-info>--%>
                    <%--<btcl-info title="Discount Rate" :text="connection.discountRate"></btcl-info>--%>
                    <%--<btcl-info title="Status" :text="connection.status.label"></btcl-info>--%>

                    <div class=row>
                        <%--local end office information--%>
                        <div class="col-sm-6">
                            <btcl-portlet title="Local End" v-if="link.hasOwnProperty('localEndOffice')">
                                <btcl-info title="Office Name" :text.sync="link.localEndOffice.officeName"></btcl-info>
                                <btcl-info title="Office Address"
                                           :text.sync="link.localEndOffice.officeAddress"></btcl-info>
                                <hr>
                                <btcl-bounded :title="'Local Office Loop ' + (localLoopIndex+1)"  v-for="(localLoop,localLoopIndex) in link.localEndOffice.localLoops">
                                    <btcl-info title="POP Name" :text.sync="localLoop.popName"></btcl-info>
                                    <btcl-info title="Router or Switch Name" :text.sync="localLoop.rsName"></btcl-info>
                                    <btcl-info v-if="localLoop.loopProvider == 1" title="Loop Provider" text="BTCL"></btcl-info>
                                    <btcl-info v-else title="Loop Provider" text="Client"></btcl-info>
                                    <btcl-info title="OC Distance(m)" :text.sync="localLoop.ocDistance"></btcl-info>
                                    <btcl-info title="Port Name" :text.sync="localLoop.portName"></btcl-info>
                                    <btcl-info title="VLAN Name" :text.sync="localLoop.vlanName"></btcl-info>

                                    <btcl-info v-if="localLoop.vlanType==1" title="VLAN Type" text = "Regular VLAN"></btcl-info>
                                    <btcl-info v-else title="VLAN Type" text = "Global VLAN"></btcl-info>

                                </btcl-bounded>

                            </btcl-portlet>
                        </div>
                        <%--remote end office information--%>
                        <div class="col-sm-6">
                            <btcl-portlet title="Remote End" v-if="link.hasOwnProperty('remoteEndOffice')">
                                <btcl-info title="Office Name" :text.sync="link.remoteEndOffice.officeName"></btcl-info>
                                <btcl-info title="Office Address"
                                           :text.sync="link.remoteEndOffice.officeAddress"></btcl-info>
                                <hr>
                                <btcl-bounded :title="'Remote Office Loop ' + (localLoopIndex+1)" v-for="(localLoop, localLoopIndex) in link.remoteEndOffice.localLoops">
                                    <btcl-info title="POP Name" :text.sync="localLoop.popName"></btcl-info>
                                    <btcl-info title="Router or Switch Name" :text.sync="localLoop.rsName"></btcl-info>
                                    <btcl-info v-if="localLoop.loopProvider == 1" title="Loop Provider" text="BTCL"></btcl-info>
                                    <btcl-info v-else title="Loop Provider" text="Client"></btcl-info>
                                    <btcl-info title="OC Distance(m)" :text.sync="localLoop.ocDistance"></btcl-info>
                                    <btcl-info title="Port Name" :text.sync="localLoop.portName"></btcl-info>
                                    <btcl-info title="VLAN Name" :text.sync="localLoop.vlanName"></btcl-info>
                                    <%--<btcl-info title="Vendor Name" :text.sync="localLoop.popName"></btcl-info>--%>

                                    <btcl-info v-if="localLoop.vlanType==1" title="VLAN Type" text = "Regular VLAN"></btcl-info>
                                    <btcl-info v-else title="VLAN Type" text = "Global VLAN"></btcl-info>


                                </btcl-bounded>

                            </btcl-portlet>
                        </div>
                    </div>

                </btcl-portlet>
                <%--<btcl-portlet title="Inventory Information">--%>
                <%--<btcl-bounded title="">--%>
                <%--&lt;%&ndash;<table class="table">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<tr  style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th ><p style="text-align: center;">Name</p></th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th ><p style="text-align: center;">Model</p></th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;&lt;%&ndash;<th ><p style="text-align: center;">Total Amount</p></th>&ndash;%&gt;&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th ><p style="text-align: center;">Type</p></th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th ><p style="text-align: center;">Amount</p></th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;&lt;%&ndash;<th ><p style="text-align: center;"></p></th>&ndash;%&gt;&ndash;%&gt;--%>
                <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<template v-for="(inventory, index) in connection.modifiedCoLocationInventoryDTOList">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<tr >&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td >&ndash;%&gt;--%>
                <%--&lt;%&ndash;<p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.name}}</p>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.model}}</p></td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td ><p style="text-align: center;border: 1px solid;padding: 10px;">&ndash;%&gt;--%>
                <%--&lt;%&ndash;&lt;%&ndash;{{new Date(parseInt(ip.activeFrom)).toLocaleDateString("ca-ES")}}&ndash;%&gt;&ndash;%&gt;--%>
                <%--&lt;%&ndash;{{inventory.type}}&ndash;%&gt;--%>
                <%--&lt;%&ndash;</p></td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.amount}}</p></td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;&lt;%&ndash;<td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.isUsed}}</p></td>&ndash;%&gt;&ndash;%&gt;--%>
                <%--&lt;%&ndash;&lt;%&ndash;<td ><p style="text-align: center;border: 1px solid;padding: 10px;"></p></td>&ndash;%&gt;&ndash;%&gt;--%>
                <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</template>&ndash;%&gt;--%>

                <%--&lt;%&ndash;&lt;%&ndash;<template v-if="connection.powerNeeded"></template>&ndash;%&gt;&ndash;%&gt;--%>
                <%--&lt;%&ndash;&lt;%&ndash;<template v-if="connection.fiberNeeded"></template>&ndash;%&gt;&ndash;%&gt;--%>
                <%--&lt;%&ndash;&lt;%&ndash;<template v-if="connection.rackNeeded"></template>&ndash;%&gt;&ndash;%&gt;--%>


                <%--&lt;%&ndash;</table>&ndash;%&gt;--%>
                <%--</btcl-bounded>--%>
                <%--</btcl-portlet>--%>


            </div>
            <div class="col-md-4">
                <btcl-portlet title=History>
                    <div>
                        <button type="button" class="btn btn-default btn-block"
                                v-for="(history, historyIndex) in this.linkList"
                        @click="getConnectionByHistoryID(historyIndex)"
                        >
                            <%--XXX--%>
                            <div align="left">
                                <b>{{history.incidentType}}</b>
                                <br/>
                                {{new Date(history.activeFrom).toLocaleDateString("ca-ES")}}
                            </div>
                        </button>
                    </div>
                </btcl-portlet>
            </div>
        </div>
    </btcl-body>
    <%--{{bla}}--%>
</div>

<script src="../script/vpn-network-details.js" type="text/javascript"></script>
