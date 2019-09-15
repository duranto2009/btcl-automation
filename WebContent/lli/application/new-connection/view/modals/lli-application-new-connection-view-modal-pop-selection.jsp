<!-- Modal local loop selection -->
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
                        <div class="row">
                            <div class="col-md-3" align="center">
                                Connection Office
                            </div>
                            <div class="col-md-3" align="center">
                                POP
                            </div>
                            <div class="col-md-3" align="center">
                                Bandwidth(Mbps)
                            </div>
                            <div class="col-md-3" align="center">
                                Priority
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
                                            <div class="col-md-3">
                                                <select class="form-control"
                                                        v-model="localLoop.officeid">
                                                    <option disabled selected hidden value="0">
                                                        Select Connection Office
                                                    </option>

                                                    <option
                                                            :value="office.id"
                                                            v-for="office in application.officeList">
                                                        {{office.officeName}}
                                                    </option>
                                                </select>
                                            </div>
                                            <div class="col-md-3">
                                                <multiselect v-model="localLoop.pop"
                                                             :options="popList"
                                                             placeholder="Select POP"
                                                             label="label"
                                                             track-by="ID"></multiselect>
                                            </div>
                                            <div class="col-md-3">
                                                <input type="text" class="form-control"
                                                       v-model="localLoop.bandwidth"
                                                       readonly/>
                                            </div>
                                            <div class="col-md-3">
                                                <select class="form-control"
                                                        v-model="localLoop.priority">
                                                    <option selected hidden disabled value="0">Select
                                                        Priority
                                                    </option>
                                                    <option value="1">
                                                        High
                                                    </option>
                                                    <option value="2">
                                                        Medium
                                                    </option>
                                                    <option value="3">
                                                        Moderate
                                                    </option>
                                                    <option value="4">
                                                        Not Applicable
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div align="right">
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
                    <input type=text class="form-control" v-model="comment"
                           placeholder="Comment(*)"/>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success"
                        @click="process('new-connection-process')">Save
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>

<!-- Modal local loop selection -->
<div class="modal fade" id="newLocalLoopModal" role="dialog">
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
                        <div class="row">
                            <div class="col-md-5" align="center">
                                Connection Office
                            </div>
                            <div class="col-md-5" align="center">
                                POP
                            </div>
                        </div>
                        <div :title="application.officeAddress"
                             v-for="(newOffice,newOfficeIndex) in content.newOfficeList"
                             :key="newOfficeIndex">
                            <btcl-bounded
                                    v-for="(localLoop, newlocalLoopIndex) in newOffice.localLoopList"
                                    :title="''"
                                    :key="newlocalLoopIndex">
                                <div>
                                    <div>
                                        <div class="row">
                                            <div class="col-md-5">
                                                <select class="form-control"
                                                        v-model="localLoop.officeid">
                                                    <option disabled selected hidden value="0">
                                                        Select Connection Office
                                                    </option>

                                                    <option
                                                            :value="newOffice.id"
                                                            v-for="newOffice in application.newOfficeList">
                                                        {{newOffice.officeName}}
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
                                            <div class="col-md-2" align="right">
                                                <button type="button"
                                                        @click="deleteNewLocalLoop(content, newOfficeIndex, newlocalLoopIndex)"
                                                        class="btn red btn-outline">Remove
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </btcl-bounded>
                            <div align="right">
                                <button type="button"
                                        @click="addLocalLoop(newOffice,content.bandwidth)"
                                        class="btn green-haze btn-outline">
                                    Add
                                </button>
                            </div>

                        </div>

                        <div title="Port Count">
                            <div>
                                <p>Port Count : {{application.portCount}}</p>
                            </div>

                        </div>
                    </btcl-portlet>
                    <input type=text class="form-control" v-model="comment"
                           placeholder="Comment(*)"/>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success"
                        @click="processLocalLoopIFRRequest('new-local-loop-process')">Save
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>

<div class="modal fade" id="additoinalIPModal" role="dialog">
    <div class="modal-dialog modal-lg" style="width:80%;">

        <!--Modal For IP Request Forward-->
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    Forward The IP Request
                </div>
                <div class="tools">
                    <a href="javascript:;" data-original-title="" title=""
                       class="collapse"></a>
                </div>
            </div>

            <div class="modal-body">
                <div class="portlet-body">

                    <div class="col-md-5" align="center">
                        Number of IP Requsted
                    </div>
                    <div class="col-md-5" align="center" >
                        <input type="text" class="form-control"
                               v-model="application.ipCount"
                               readonly/>
                    </div>

                    <input type=text class="form-control" v-model="comment"
                           placeholder="Comment(*)"/>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success"
                        @click="processIFRRequestAdditionalIP('additional-ip-process')">Save
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>