<div class="modal fade" id="portAssignModal" role="dialog">
    <div class="modal-dialog modal-lg" style="width:80%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    Assign Port Vlan and Switch
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
                            <div class=row>
                                <label class="col-sm-4 control-label"
                                       style="text-align: left;">Switch/Router</label>
                                <div class=col-sm-6>
                                    <select class="form-control"
                                            v-model="switchId"
                                            v-on:change="onChangeSwitch(switchId)"
                                            style="margin-top: 7px;">
                                        <option v-bind:value="rs.id"
                                                :key="rs.id"
                                                v-for="rs in routerSwitch">
                                            {{rs.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                        </btcl-field>
                        <btcl-field>
                            <div class=row>
                                <label class="col-sm-4 control-label"
                                       style="text-align: left;">Port</label>
                                <div class=col-sm-6>
                                    <select class="form-control"
                                            v-model="portId"
                                            style="margin-top: 7px;">
                                        <option v-bind:value="p.id"
                                                v-for="p in ports"
                                                :key="p.id">
                                             {{p.name}} ({{p.type}})
                                            <template v-if="p.used">(Used)</template>
                                            <template v-else>(Not Used)</template>

                                        </option>

                                    </select>
                                </div>
                            </div>
                        </btcl-field>

                        <btcl-field>
                            <div class=row>
                                <label class="col-sm-4 control-label"
                                       style="text-align: left;">VLAN</label>
                                <div class=col-sm-6>

                                    <select class="form-control"
                                            v-model:value="vlanId">

                                        <option v-bind:value="v.id"
                                                v-for="v in vlans">
                                            {{v.name}}
                                            <template v-if="v.global">(Global)</template>
                                            <template v-else>(Regular)</template>
                                            <template v-if="v.used">(Used)</template>
                                            <template v-else>(Not Used)</template>
                                        </option>
                                    </select>
                                </div>
                            </div>
                        </btcl-field>

                    </btcl-portlet>
                    <input type=text class="form-control" v-model="comment"
                           placeholder="Comment"/>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success"
                        @click="updatePortInfo">Update
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal" @click="closePortAssignModal">Close
                </button>
            </div>
        </div>

    </div>
</div>
