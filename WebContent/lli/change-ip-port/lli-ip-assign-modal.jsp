<div class="modal fade" id="ipAssignModal" role="dialog">
    <div class="modal-dialog modal-lg" style="width:80%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    Assign IP
                </div>
                <div class="tools">
                    <a href="javascript:;" data-original-title="" title=""
                       class="collapse"></a>
                </div>
            </div>

            <div class="modal-body">
                <div class="portlet-body">
                    <btcl-portlet>
                        <btcl-field>
                            <div class="form-group">
                                <div class=row>
                                    <label class="col-sm-4 control-label"
                                           style="text-align: left;">Select
                                        Region</label>
                                    <div class=col-sm-8>
                                        <select class="form-control"
                                                v-model="regionID"
                                                style="margin-top: 7px;">
                                            <option value="0" disabled>Select
                                                Region
                                            </option>
                                            <option v-bind:value="region.key"
                                                    v-for="region in ipRegionList">
                                                {{region.value}}
                                            </option>

                                        </select>
                                    </div>
                                </div>
                                <div class=row>
                                    <label class="col-sm-4 control-label"
                                           style="text-align: left;">IP
                                        Version</label>
                                    <div class=col-sm-8>
                                        <select class="form-control"
                                                v-model="ipVersion"
                                                style="margin-top: 7px;">
                                            <option value="0" disabled>Select IP
                                                Version
                                            </option>
                                            <option value="IPv4">IPv4</option>
                                            <option value="IPv6">IPv6</option>

                                        </select>
                                    </div>

                                </div>
                                <div class=row>
                                    <label class="col-sm-4 control-label"
                                           style="text-align: left;">IP Type</label>
                                    <div class=col-sm-8>
                                        <select class="form-control"
                                                v-model="ipType"
                                                style="margin-top: 7px;">
                                            <option value="0" disabled>Select Type
                                            </option>
                                            <option value="PUBLIC">Public</option>
                                            <option value="PRIVATE">Private</option>

                                        </select>
                                    </div>
                                </div>
                                <div class=row>
                                    <label class="col-sm-4 control-label"
                                           style="text-align: left;">Block
                                        Size</label>
                                    <div class=col-sm-8>

                                        <select class="form-control"
                                                v-model="ipBlockSize"
                                                style="margin-top: 7px;">
                                            <option value="0" disabled>Select Block
                                                Size
                                            </option>
                                            <option value="2">2</option>
                                            <option value="4">4</option>
                                            <option value="8">8</option>
                                            <option value="16">16</option>
                                            <option value="32">32</option>
                                            <option value="64">64</option>
                                            <option value="128">128</option>
                                            <option value="256">256</option>
                                        </select>


                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type=button class="btn btn-success"
                                            @click="searchIPBlock">
                                        Search IP Block To Assign
                                    </button>
                                </div>
                                <div v-if="ipFreeBlockList.length>0">
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">IP Block Range</label>
                                            <div class=col-sm-8>
                                                <select class="form-control"
                                                        v-model="ipBlockID"
                                                        v-on:change="onchangeIPBlockRange(ipBlockID)"
                                                        style="margin-top: 7px;">
                                                    <option value="0">Select IP Block Range
                                                    </option>
                                                    <option v-bind:value="region"
                                                            v-for="region in ipFreeBlockList">
                                                        {{region.key.fromIP}} -
                                                        {{region.key.toIP}}
                                                    </option>
                                                </select>
                                            </div>
                                        </div>

                                        <div v-if="ipAvailableRangleList!=''">
                                            <div class="row">
                                                <label class="col-sm-4 control-label"
                                                       style="text-align: left;">Available IP's</label>
                                                <div class=col-sm-8>
                                                    <select class="form-control"
                                                            v-model="ipAvailableIPID"
                                                            v-on:change="onchangeAvailableIP(ipAvailableIPID)"
                                                            style="margin-top: 7px;">
                                                        <option value="0">Select IP
                                                        </option>
                                                        <option v-bind:value="ip"
                                                                v-for="ip in ipAvailableRangleList">
                                                            {{ip.fromIP}} -
                                                            {{ip.toIP}}
                                                        </option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>

                                        <div v-if="ipAvailableSelected.length>0">
                                            <div class="row">
                                                <label class="col-sm-4 control-label"
                                                       style="text-align: left;">Selected IP's</label>
                                                <div class=col-sm-8>
                                                    <div style="margin-top: 2px;border: 1px solid;" class="form-control" v-for="(ip,index) in ipAvailableSelected">
                                                        <div class="row">
                                                            <div class="col-sm-10">{{ip.fromIP}} - {{ip.toIP}} </div>
                                                            <div class="col-sm-2">
                                                                <button  @click="ipAvailableDelete(index)" type="button" style="color:red" >x</button></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                            </div>
                        </btcl-field>

                        <%--<btcl-field>
                            <div align="right">
                                <button type="submit" class="btn green-haze" v-on:click="assignNewIPAndReleaseOld" >Assign New IP</button>
                            </div>
                        </btcl-field>--%>
                    </btcl-portlet>
                    <input type=text class="form-control" v-model="comment"
                           placeholder="Comment"/>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success"
                        @click="assignNewIPAndReleaseOld">Assign
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal" @click="closeIpAssignModal">Close
                </button>
            </div>
        </div>

    </div>
</div>
