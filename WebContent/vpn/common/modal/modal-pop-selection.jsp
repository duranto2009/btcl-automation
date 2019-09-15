<div class="modal fade" id="modal-pop-selection" role="dialog">
    <div class="modal-dialog modal-lg" style="width:80%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center" style="background: #fab74d;padding: 10px;">
                    Choose POP
                </div>
                <div class="tools">
                    <a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                </div>
            </div>

            <div class="modal-body">
                <div class="portlet-body">
                    <btcl-portlet>
                        <table class="table">
                            <thead>
                            <tr>
                                <th style="text-align: center;">Office Type</th>
                                <th style="text-align: center;">Office</th>
                                <th style="text-align: center;">Office Address</th>
                                <th style="text-align: center;">POP</th>
                                <th style="text-align: center;">Bandwidth(Mbps)</th>
                                <th v-if="nextAction.param=='feasibility'" style="text-align: center;">Feasibility</th>
                                <th v-if="application.applicationType=='VPN_UPGRADE_CONNECTION'" style="text-align: center;">Change POP</th>
                            </tr>
                            </thead>
                            <template v-for="(link, linkIndex) in application.vpnApplicationLinks">
                                <tbody>
                                <%--local end office--%>
                                <template v-if="link.hasOwnProperty('localOffice')">
                                    <template v-if="localLoopIndex == 0 && linkIndex == 0"
                                              v-for="(localLoop, localLoopIndex) in link.localOffice.localLoops"
                                              :title="''">
                                        <tr>
                                            <td align="center"><p>Local Office</p></td>
                                            <td align="center"><p>{{link.localOffice.officeName}}</p></td>
                                            <td align="center"><p>{{link.localOffice.officeAddress}}</p></td>
                                            <td align="center">
                                                <template v-if="localLoop.isCompleted&&localLoop.changePOP==false">
                                                    <p>{{localLoop.popName}}</p>
                                                </template>
                                                <template v-else>
                                                    <multiselect v-model="localEndPop"
                                                                 :options="popList"
                                                                 placeholder="Select POP"
                                                                 label="label"
                                                    ></multiselect>
                                                </template>
                                            </td>
                                            <td align="center">
                                                <input type="text" class="form-control"
                                                       <%--v-model="link.linkBandwidth"--%>
                                                        value="N/A"
                                                       readonly/>
                                            </td>
                                            <td v-if="nextAction.param=='feasibility'" align="center">
                                                <select class="form-control" v-model.number="localEndFeasibility">
                                                    <option v-bind:value="false">Not Possible</option>
                                                    <option v-bind:value="true">Possible</option>
                                                </select>
                                            </td>
                                            <td v-if="application.applicationType=='VPN_UPGRADE_CONNECTION'" align="center">
                                                <input style="margin-top:5%;" type="checkbox" v-model="localLoop.changePOP">
                                            </td>
                                        </tr>
                                    </template>
                                </template>

                                <%--remote end office--%>
                                <template v-if="link.hasOwnProperty('remoteOffice')">

                                    <%--if global link choosen--%>
                                    <template v-if="localLinkIndex==-1">
                                        <template v-for="(localLoop, localLoopIndex) in link.remoteOffice.localLoops"
                                                  :title="''">
                                            <tr>
                                                <td align="center">
                                                    <p>Remote Office</p>
                                                </td>
                                                <td align="center">
                                                    <p>{{link.remoteOffice.officeName}}</p>
                                                </td>
                                                <td align="center">
                                                    <p>{{link.remoteOffice.officeAddress}}</p>
                                                </td>
                                                <td align="center">
                                                    <template v-if="localLoop.isCompleted&&localLoop.changePOP==false">
                                                        <p>{{localLoop.popName}}</p>
                                                    </template>
                                                    <template v-else>
                                                        <multiselect v-model="localLoop.pop"
                                                                     :options="popList"
                                                                     placeholder="Select POP"
                                                                     label="label"
                                                        ></multiselect>
                                                    </template>
                                                </td>
                                                <td align="center">
                                                    <input type="text" class="form-control"
                                                           v-model="link.linkBandwidth"
                                                           readonly/>
                                                </td>
                                                <td v-if="nextAction.param=='feasibility'" align="center">
                                                    <select class="form-control"
                                                            v-model.number="localLoop.ifrFeasibility" type="number">
                                                        <option v-bind:value="false">Not Possible</option>
                                                        <option v-bind:value="true">Possible</option>
                                                    </select>
                                                </td>
                                                <td v-if="application.applicationType=='VPN_UPGRADE_CONNECTION'" align="center">
                                                    <input style="margin-top:5%;" type="checkbox" v-model="localLoop.changePOP">
                                                </td>
                                            </tr>
                                        </template>
                                    </template>

                                    <%-- if local link choosen--%>
                                    <template v-else>
                                        <template v-for="(localLoop, localLoopIndex) in link.remoteOffice.localLoops"
                                                  :title="''">
                                            <tr v-if="localLinkIndex == linkIndex">
                                                <td align="center">
                                                    <p>Remote Office</p>
                                                </td>
                                                <td align="center">
                                                    <p>{{link.remoteOffice.officeName}}</p>
                                                </td>
                                                <td align="center">
                                                    <p>{{link.remoteOffice.officeAddress}}</p>
                                                </td>
                                                <td align="center">
                                                    <template v-if="localLoop.isCompleted&&localLoop.changePOP==false">
                                                        <p>{{localLoop.popName}}</p>
                                                    </template>
                                                    <template v-else>
                                                        <multiselect v-model="localLoop.pop"
                                                                     :options="popList"
                                                                     placeholder="Select POP"
                                                                     label="label"
                                                        ></multiselect>
                                                    </template>
                                                </td>
                                                <td align="center">
                                                    <input type="text" class="form-control"
                                                           v-model="link.linkBandwidth"
                                                           readonly/>
                                                </td>
                                                <td v-if="nextAction.param=='feasibility'" align="center">
                                                    <select class="form-control"
                                                            v-model.number="localLoop.ifrFeasibility" type="number">
                                                        <option v-bind:value="false">Not Possible</option>
                                                        <option v-bind:value="true">Possible</option>
                                                    </select>
                                                </td>
                                                <td v-if="application.applicationType=='VPN_UPGRADE_CONNECTION'" align="center">
                                                    <input style="margin-top:5%;" type="checkbox" v-model="localLoop.changePOP">
                                                </td>
                                            </tr>
                                        </template>
                                    </template>

                                </template>
                                </tbody>
                            </template>
                        </table>
                    </btcl-portlet>
                    Comment <input type=text title="Comment" required="true" class="form-control" v-model="comment"/>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success"
                        @click="ifrPostRequest()"
                >Save
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>