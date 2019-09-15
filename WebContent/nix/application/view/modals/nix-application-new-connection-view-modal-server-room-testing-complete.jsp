<div class="modal fade" id="serverRoomTestingComplete" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 50%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <div>
                    <div class="portlet-title">
                        <div class="caption" align="center"
                             style="background: #fab74d;padding: 10px;">Local Loops.
                        </div>
                        <div class="tools">
                            <a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                        </div>
                    </div>
                    <div class="form-body">
                        <%--start: port, vlan, router switch assign--%>
                        <div v-for="(element,index) in application.localloops" :key="index">
                            <btcl-portlet v-if="element.workCompleted == 1 || application.loopProvider.ID==2">
<%--                            <btcl-portlet >--%>
                                <btcl-field>
                                    <div class="form-group">
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">
                                                POP {{index+1}}
                                            </label>
                                            <div class=col-sm-6>
                                                <p class="form-control" style="background:  #f2f1f1;text-align: center;">
                                                    <%--<span  style="background-color:#f99a2f;color:white;">{{element.Value}}</span>--%>
                                                    <%--<span v-else>{{element.Value}}</span>--%>
                                                    {{element.popName}}
                                                </p>
                                            </div>
                                        </div>
                                        <%--check if global or regular vlan--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">VLAN Type</label>
                                            <div class=col-sm-6>
                                                <select class="form-control" v-model="element.vlanType"
                                                        v-on:change="onChangeVlanType(element)"
                                                        style="margin-top: 7px;">
                                                    <option selected hidden disabled value="0" >Select VLAN Type</option>
                                                    <option value=1>Regular VLAN</option>
                                                    <option value=2>Global VLAN</option>
                                                </select>
                                            </div>
                                        </div>
                                        <%--<div v-if="parseInt(element.vlanType)==1">--%>
                                            <div class=row>
                                                <label class="col-sm-4 control-label"
                                                       style="text-align: left;">Switch/Router</label>
                                                <div class=col-sm-6>
                                                    <select class="form-control"
                                                            v-model="element.routerSwitchId"
                                                            v-on:change="onChangeSwitchRouter(element)"
                                                            style="margin-top: 7px;">
                                                        <option v-bind:value="rs.ID"
                                                                :key="rs.ID"
                                                                v-for="rs in element.router_switch">
                                                            {{rs.name}}
                                                        </option>

                                                    </select>


                                                </div>

                                                <%--<i>Select Switch/Router</i>--%>
                                            </div>
                                            <div class=row>
                                                <label class="col-sm-4 control-label"
                                                       style="text-align: left;">Port</label>
                                                <div class=col-sm-6>
                                                    <select class="form-control"
                                                            v-model="element.portID"
                                                            <%--v-on:change="onchangePort(application,index)"--%>
                                                            style="margin-top: 7px;">
                                                        <option v-bind:value="p.ID"
                                                                v-for="p in element.filteredPorts"
                                                                :key="p.ID">
                                                            {{p.name}}
                                                            <template v-if="p.isUsed"> (used)</template>
                                                            <template v-else> (not used)</template>
                                                        </option>

                                                    </select>
                                                </div>
                                                <%--<i>Select Port</i>--%>
                                            </div>
                                        <%--</div>--%>
                                            <div class=row>
                                                <label class="col-sm-4 control-label"
                                                       style="text-align: left;">VLAN</label>
                                                <div class=col-sm-6>
                                                    <select class="form-control"
                                                            v-model:value="element.VLANID">
                                                        <option v-bind:value="v.ID"
                                                                v-for="v in element.filteredVlans">
                                                            {{v.name}}
                                                            <template v-if="v.isUsed"> (used)</template>
                                                            <template v-else> (not used)</template>

                                                        </option>
                                                    </select>
                                                </div>
                                            </div>

                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">Client Distance(m)</label>
                                            <div class=col-sm-6>
                                                <p class="form-control" style="background:  #f2f1f1;text-align: center;">
                                                    {{element.clientDistance}}
                                                </p>


                                            </div>
                                        </div>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">BTCL Distance(m)</label>
                                            <div class=col-sm-6>
                                                <p class="form-control"
                                                   style="background:  #f2f1f1;text-align: center;">
                                                    {{element.btclDistance}}
                                                </p>


                                            </div>
                                        </div>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">O/C Distance(m)</label>
                                            <div class=col-sm-6>
                                                <p class="form-control"
                                                   style="background:  #f2f1f1;text-align: center;">
                                                    {{element.ocdDistance}}
                                                </p>


                                            </div>
                                        </div>
                                        <%
                                            //todo :loop provider siginicance check for commented line below
                                        %>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">OFC Type</label>
                                            <div class=col-sm-6>
                                                <select disabled
                                                        class="form-control"
                                                        v-model="element.ofcType"
                                                        style="margin-top: 7px;">
                                                    <option value="1">Single</option>
                                                    <option value="2">Double</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </btcl-field>
                            </btcl-portlet>
                            <br/><br/>
                        </div>
                        <%--end: port, vlan, router switch assign--%>
                        <%--start: IP Assign--%>
                        <div v-if="application.applicationType.ID==TYPE_FACTORY.NEW_CONNECTION">
                            <%--    assign ip address--%>
                            <div class="caption" align="center"
                                 style="padding: 10px;font-weight: bold;font-size: 15px      ;">
                                IP Assign
                            </div>
                            <btcl-portlet>
                                <btcl-field>
                                    <div class="form-group">
                                        <%--select ip region--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">Select Region</label>
                                            <div class=col-sm-8>
                                                <select class="form-control" v-model="regionID" style="margin-top: 7px;">
                                                    <option value="0" disabled>Select Region</option>
                                                    <option v-bind:value="region.key" v-for="region in ipRegionList">{{region.value}}
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                        <%--slect ip version--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">IP Version</label>
                                            <div class=col-sm-8>
                                                <select class="form-control" v-model="ipVersion" style="margin-top: 7px;">
                                                    <option value="0" disabled>Select IP Version</option>
                                                    <option value="IPv4">IPv4</option>
                                                    <option value="IPv6">IPv6</option>
                                                </select>
                                            </div>
                                        </div>
                                        <%--select ip type--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">IP Type</label>
                                            <div class=col-sm-8>
                                                <select class="form-control" v-model="ipType" style="margin-top: 7px;">
                                                    <option value="0" disabled>Select Type
                                                    </option>
                                                    <option value="PUBLIC">Public</option>
                                                    <option value="PRIVATE">Private</option>
                                                </select>
                                            </div>
                                        </div>

                                        <%--select blocksize--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">Block Size</label>
                                            <div class=col-sm-8>
                                                <select class="form-control"
                                                        v-model="ipBlockSize"
                                                        style="margin-top: 7px;">
                                                    <option value="0" disabled>Select Block Size</option>
                                                    <option value="2">2</option>
                                                    <option value="4">4</option>
                                                    <option value="8">8</option>
                                                    <option value="16">16</option>
                                                    <option value="32">32</option>
                                                    <option value="64">64</option>
                                                    <option value="128">128</option>
                                                    <option value="256">256</option>
                                                    <%--<option value="512">256</option>--%>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type=button class="btn btn-success"
                                                    @click="searchIPBlock">
                                                Search IP Block
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
                                                        <option v-bind:value="ipBlock"
                                                                v-for="ipBlock in ipFreeBlockList">
                                                            {{ipBlock.key.fromIP}} -
                                                            {{ipBlock.key.toIP}}
                                                            <template v-if="ipBlock.value!==''">({{ipBlock.value}})</template>
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
                                                        <div style="margin-top: 2px;border: 1px solid;" <%--class="form-control"--%> v-for="(ip,index) in ipAvailableSelected">
                                                            <div class="row">
                                                                <div class="col-sm-10">{{ip.fromIP}} - {{ip.toIP}} </div>
                                                                <div class="col-sm-2"><button  @click="ipAvailableDelete(index)" type="button" style="color:red" >x</button></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </btcl-field>
                            </btcl-portlet>
                        </div>
                        <%--end: IP Assign--%>
                        <%--start: connection name, comment--%>
                        <div v-if="picked==STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION  || STATE_FACTORY.COMPLETE_TESTING"
                             class="form-group">
                           <label class="control-label col-md-3"
                                   style="text-align: center;">
                                Connection Name
                            </label>
                            <div v-if="application.connection" class="col-md-9">
                                <p>{{application.connection.name}}</p>
                            </div>
                            <div v-else class="col-md-9">
                                <input type="text"
                                       v-model="application.connectionName"
                                       placeholder="Provide a Connection Name"
                                       class="form-control input-lg">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3"
                                   style="text-align: center;">
                                Comment
                            </label>
                            <div class="col-md-9">
                                <input type="text"
                                       v-model="comment"
                                       placeholder="Comment Here"
                                       class="form-control input-lg">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type=button
                        class="btn btn-success"
                        @click="testingCompleteNextStep">
                    Submit
                </button>
                <button type="button"
                        class="btn btn-danger"
                        data-dismiss="modal">Close
                </button>
            </div>
        </div>
    </div>
</div>
<div v-if="isUpgradeLoaded" class="modal fade" id="serverRoomTestingCompleteUpgrade" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 50%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <div>
                    <div class="portlet-title">
                        <div class="caption" align="center"
                             style="background: #fab74d;padding: 10px;">Local Loops.
                        </div>
                        <div class="tools">
                            <a href="javascript:;" data-original-title="" title="" class="collapse"></a>
                        </div>
                    </div>
                    <div class="form-body">
                        <div>
                            <btcl-portlet>
                                <btcl-info title="Old Port Type" :text="application.oldPortInfo.value"></btcl-info>
                                <btcl-info title="New Port Type" :text="application.portTypeString"></btcl-info>
                            </btcl-portlet>
                        </div>
                        <%--start: port, vlan, router switch assign--%>
                        <div v-for="(element,index) in application.localloops" :key="index">
                            <btcl-portlet>
                                <btcl-field>
                                    <div class="form-group">
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">
                                                POP {{index+1}}
                                            </label>
                                            <div class=col-sm-6>
                                                <p class="form-control" style="background:  #f2f1f1;text-align: center;">
                                                    {{element.popName}}
                                                </p>
                                            </div>
                                        </div>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">VLAN Type</label>
                                            <div class=col-sm-6>
                                                <select class="form-control" v-model="element.vlanType"
                                                v-on:change="onChangeVlanType(element)"
                                                        style="margin-top: 7px;">
                                                    <option selected hidden disabled value="0" >Select VLAN Type</option>
                                                    <option value=1>Regular VLAN</option>
                                                    <option value=2>Global VLAN</option>
                                                </select>
                                            </div>
                                        </div>
                                        <%--<div v-if="parseInt(element.vlanType)==1">--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">Switch/Router</label>
                                            <div class=col-sm-6>
                                                <select class="form-control"
                                                        v-model="element.routerSwitchId"
                                                        v-on:change="onChangeSwitchRouter(element)"
                                                        style="margin-top: 7px;">
                                                    <option v-bind:value="rs.ID"
                                                            :key="rs.ID"
                                                            v-for="rs in element.router_switch">
                                                        {{rs.name}}
                                                    </option>
                                                </select>
                                            </div>
                                            <%--<i>Select Switch/Router</i>--%>
                                        </div>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">
                                                    Old Port
                                            </label>
                                            <div class=col-sm-6>
                                                <p class="form-control" style="background:  #f2f1f1;text-align: center;">
                                                    <%--<span  style="background-color:#f99a2f;color:white;">{{element.Value}}</span>--%>
                                                    <%--<span v-else>{{element.Value}}</span>--%>
                                                    {{element.oldPortName}}
                                                </p>
                                            </div>
                                        </div>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">Port</label>
                                            <div class=col-sm-6>
                                                <select class="form-control"
                                                        v-model="element.portID"
                                                <%--v-on:change="onchangePort(application,index)"--%>
                                                        style="margin-top: 7px;">
                                                    <option v-bind:value="p.ID"
                                                            v-for="p in element.filteredPorts"
                                                            :key="p.ID">
                                                        {{p.name}}
                                                        <template v-if="p.isUsed"> (used)</template>
                                                        <template v-else> (not used)</template>
                                                    </option>

                                                </select>
                                            </div>
                                            <%--<i>Select Port</i>--%>
                                        </div>
                                        <%--</div>--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">VLAN</label>
                                            <div class=col-sm-6>
                                                <select class="form-control"
                                                        v-model:value="element.vlanId">
                                                    <option v-bind:value="v.ID"
                                                            v-for="v in element.filteredVlans">
                                                        {{v.name}}
                                                        <template v-if="v.isUsed"> (used)</template>
                                                        <template v-else> (not used)</template>

                                                    </option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">Client Distance(m)</label>
                                            <div class=col-sm-6>
                                                <p class="form-control" style="background:  #f2f1f1;text-align: center;">
                                                    {{element.clientDistance}}
                                                </p>
                                            </div>
                                        </div>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">BTCL Distance(m)</label>
                                            <div class=col-sm-6>
                                                <p class="form-control"
                                                   style="background:  #f2f1f1;text-align: center;">
                                                    {{element.btclDistance}}
                                                </p>


                                            </div>
                                        </div>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">O/C Distance(m)</label>
                                            <div class=col-sm-6>
                                                <p class="form-control"
                                                   style="background:  #f2f1f1;text-align: center;">
                                                    {{element.ocdDistance}}
                                                </p>
                                            </div>
                                        </div>
                                        <%
                                            //todo :loop provider siginicance check for commented line below
                                        %>
                                        <div class=row>
                                            <label class="col-sm-4 control-label"
                                                   style="text-align: left;">OFC Type</label>
                                            <div class=col-sm-6>
                                                <select disabled
                                                        class="form-control"
                                                        v-model="element.ofcType"
                                                        style="margin-top: 7px;">
                                                    <option value="1">Single</option>
                                                    <option value="2">Double</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </btcl-field>
                            </btcl-portlet>
                            <br/><br/>
                        </div>
                        <%--start: IP Assign--%>
                        <div v-if="
                        application.applicationType.ID==TYPE_FACTORY.NEW_CONNECTION">
                            <%--    assign ip address--%>
                            <div class="caption" align="center"
                                 style="padding: 10px;font-weight: bold;font-size: 15px      ;">
                                IP Assign
                            </div>
                            <btcl-portlet>
                                <btcl-field>
                                    <div class="form-group">
                                        <%--select ip region--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">Select Region</label>
                                            <div class=col-sm-8>
                                                <select class="form-control" v-model="regionID" style="margin-top: 7px;">
                                                    <option value="0" disabled>Select Region</option>
                                                    <option v-bind:value="region.key" v-for="region in ipRegionList">{{region.value}}
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                        <%--slect ip version--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">IP Version</label>
                                            <div class=col-sm-8>
                                                <select class="form-control" v-model="ipVersion" style="margin-top: 7px;">
                                                    <option value="0" disabled>Select IP Version</option>
                                                    <option value="IPv4">IPv4</option>
                                                    <option value="IPv6">IPv6</option>
                                                </select>
                                            </div>
                                        </div>
                                        <%--select ip type--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">IP Type</label>
                                            <div class=col-sm-8>
                                                <select class="form-control" v-model="ipType" style="margin-top: 7px;">
                                                    <option value="0" disabled>Select Type
                                                    </option>
                                                    <option value="PUBLIC">Public</option>
                                                    <option value="PRIVATE">Private</option>
                                                </select>
                                            </div>
                                        </div>

                                        <%--select blocksize--%>
                                        <div class=row>
                                            <label class="col-sm-4 control-label" style="text-align: left;">Block Size</label>
                                            <div class=col-sm-8>
                                                <select class="form-control"
                                                        v-model="ipBlockSize"
                                                        style="margin-top: 7px;">
                                                    <option value="0" disabled>Select Block Size</option>
                                                    <option value="2">2</option>
                                                    <option value="4">4</option>
                                                    <option value="8">8</option>
                                                    <option value="16">16</option>
                                                    <option value="32">32</option>
                                                    <option value="64">64</option>
                                                    <option value="128">128</option>
                                                    <option value="256">256</option>
                                                    <%--<option value="512">256</option>--%>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type=button class="btn btn-success"
                                                    @click="searchIPBlock">
                                                Search IP Block
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
                                                        <option v-bind:value="ipBlock"
                                                                v-for="ipBlock in ipFreeBlockList">
                                                            {{ipBlock.key.fromIP}} -
                                                            {{ipBlock.key.toIP}}
                                                            <template v-if="ipBlock.value!==''">({{ipBlock.value}})</template>
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
                                                        <div style="margin-top: 2px;border: 1px solid;" <%--class="form-control"--%> v-for="(ip,index) in ipAvailableSelected">
                                                            <div class="row">
                                                                <div class="col-sm-10">{{ip.fromIP}} - {{ip.toIP}} </div>
                                                                <div class="col-sm-2"><button  @click="ipAvailableDelete(index)" type="button" style="color:red" >x</button></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </btcl-field>
                            </btcl-portlet>
                        </div>
                        <%--end: IP Assign--%>
                        <div v-if="picked==STATE_FACTORY.WITHOUT_LOOP_COMPLETE_TESTING_AND_CREATE_CONNECTION  || STATE_FACTORY.COMPLETE_TESTING"
                             class="form-group">
                            <label class="control-label col-md-3"
                                   style="text-align: center;">
                                Connection Name
                            </label>
                            <div v-if="application.connection" class="col-md-9">
                                <p>{{application.connection.name}}</p>
                            </div>
                            <div v-else class="col-md-9">
                                <input type="text"
                                       v-model="application.connectionName"
                                       placeholder="Provide a Connection Name"
                                       class="form-control input-lg">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3"
                                   style="text-align: center;">
                                Comment
                            </label>
                            <div class="col-md-9">
                                <input type="text"
                                       v-model="comment"
                                       placeholder="Comment Here"
                                       class="form-control input-lg">
                            </div>
                        </div>
                        <%--end: connection name, comment--%>
                        <%--<p  v-for="p in ports" :key="p.ID">{{p.name}}</p>--%>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <%--<button type=button class="btn btn-success" @click="processRequestForEFR('new-connection-process')">Save</button>--%>
                <button type=button
                        class="btn btn-success"
                        @click="testingCompleteNextStepUpgrade">
                    Submit
                </button>
                <button type="button"
                        class="btn btn-danger"
                        data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>