<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg" style="width:80%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    Select POP
                </div>
                <div class="tools">
                    <a href="javascript:;" data-original-title="" title=""
                       class="collapse"></a>
                </div>
            </div>

            <div class="modal-body">
                <div class="portlet-body">
                    <btcl-portlet>
                        <div class="col-md-10">
                            <div class="col-md-5" align="center">
                                Office
                            </div>
                            <div class="col-md-5" align="center">
                                POP
                            </div>
                        </div>
                        <div :title="application.officeAddress"
                             v-for="(office,officeIndex) in content.officeList"
                             :key="officeIndex">
                            <btcl-bounded
                                    v-for="(localLoop, localLoopIndex) in office.localLoopList"
                                    :title="''"
                                    :key="localLoopIndex">
                                <div>
                                    <div class="col-md-10">
                                        <div class="row">
                                            <div class="col-md-5">
                                                <select class="form-control"
                                                        v-model="localLoop.officeid">
                                                    <option disabled selected hidden value="0">
                                                        Select Office
                                                    </option>

                                                    <option
                                                            :value="office.id"
                                                            v-for="office in application.officeList">
                                                        {{office.name}}
                                                    </option>
                                                </select>
                                            </div>
                                            <div class="col-md-5">
                                                <multiselect v-model="localLoop.pop"
                                                             :options="popList"
                                                             placeholder="Select POP"
                                                             label="label"
                                                             track-by="ID"></multiselect>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-2" align="right">
                                        <button type="button"
                                                @click="deleteLocalLoop(content, officeIndex, localLoopIndex)"
                                                class="btn red btn-outline">Remove
                                        </button>
                                    </div>
                                </div>


                            </btcl-bounded>
                            <div align="right">
                                <button type="button"
                                        @click="addLocalLoop(office,content.bandwidth)"
                                        class="btn green-haze btn-outline">
                                    Add
                                </button>
                            </div>
                        </div>
                    </btcl-portlet>
                    <%--<btcl-portlet>--%>
                      <btcl-bounded>
                          <div class="col-md-6 col-xs-6" style="text-align: left">
                            <label>Port Type:</label>
                            <label>{{application.portTypeString}}</label>
                        </div>
                        <div class="col-md-6 col-xs-6" style="text-align: left">
                            <label>Port Count:</label>
                            <label>{{application.portCount}}</label>
                        </div>
                      </btcl-bounded>
                <%--</btcl-portlet>--%>
                    <input type=text class="form-control" v-model="comment"
                           placeholder="Comment(*)"/>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" @click="process('new-connection-process')">Save</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>

<div v-if ="isUpgradeLoaded" class="modal fade" id="upgradeIFRModal" role="dialog">
    <div class="modal-dialog modal-lg" style="width:80%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    Pop Info
                </div>
                <div class="tools">
                    <a href="javascript:;" data-original-title="" title=""
                       class="collapse"></a>
                </div>
            </div>

            <div class="modal-body">
                <div class="portlet-body">
                    <btcl-portlet>
                        <div class="row">
                            <div class="col-md-5" align="center">
                                Office
                            </div>
                            <div class="col-md-5" align="center">
                                POP
                            </div>
                        </div>
                        <div :title="application.officeAddress"
                             v-for="(office,officeIndex) in content.officeList"
                             :key="officeIndex">
                            <btcl-bounded
                                    v-for="(localLoop, localLoopIndex) in office.localLoopList"
                                    :title="''"
                                    :key="localLoopIndex">
                                <div>
                                    <div>
                                        <div class="row">
                                            <div class="col-md-5">
                                                <select disabled class="form-control"
                                                        v-model="localLoop.officeid">
                                                    <option disabled selected hidden value="0">
                                                        Select Office
                                                    </option>

                                                    <option :value="office.id"
                                                            v-for="office in application.officeList">
                                                        {{office.name}}
                                                    </option>
                                                </select>
                                            </div>
                                            <div class="col-md-5">
                                                <multiselect disabled v-model="localLoop.pop"
                                                             :options="popList"
                                                             placeholder="Select POP"
                                                             label="label"
                                                             track-by="ID"></multiselect>
                                            </div>
                                        </div>
                                    </div>
                                   <%-- <div align="right">
                                        <button type="button"
                                                @click="deleteLocalLoop(content, officeIndex, localLoopIndex)"
                                                class="btn red btn-outline">Remove
                                        </button>
                                    </div>--%>
                                </div>


                            </btcl-bounded>
                           <%-- <div align="right">
                                <button type="button"
                                        @click="addLocalLoop(office,content.bandwidth)"
                                        class="btn green-haze btn-outline">
                                    Add
                                </button>
                            </div>--%>
                        </div>
                    </btcl-portlet>
                    <btcl-portlet>
                        <div style="text-align: left">
                            <label>Old Port Type::</label>
                            <label>{{application.oldPortInfo.value}}</label>
                        </div>
                        <div style="text-align: left">
                            <label>New Port Type::</label>
                            <label>{{application.portTypeString}}</label>
                        </div>
                    </btcl-portlet>
                    <input type=text class="form-control" v-model="comment"
                           placeholder="Comment(*)"/>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" @click="process('new-connection-process')">Save</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>