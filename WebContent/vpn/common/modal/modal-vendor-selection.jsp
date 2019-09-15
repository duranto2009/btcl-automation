<%--
  User: forhad
  Date: 2/5/19
  Time: 11:59 AM
--%>
<div class="modal fade" id="modal-vendor-selection" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 95%;">
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    <p>Choose Vendors</p>
                </div>
            </div>
            <div class="modal-body">
                <%--local office end--%>
                <template>

                    <div class="table-responsive">
                        <table class="table table-fit table-bordered">

                            <tr>
                                <th style="width: 10% !important;"><p style="text-align: center;">Office</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">POP</p></th>
                                <th><p style="text-align: center;">Requested BW(Mbps)</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">FROM</p></th>
                                <th><p style="text-align: center;">From Address</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">To</p></th>
                                <th><p style="text-align: center;">To Address</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">Loop Provider</p>
                                </th>
                                <th><p style="text-align: center;">Select Vendor</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">OFC Type</p></th>
                                <th v-if="nextAction.param=='efr-response'"><p style="text-align: center;">Proposed Loop
                                    Distance</p></th>
                                <th><p style="text-align: center;"></p></th>

                            </tr>

                            <template v-if="localOfficeExists()">
                                <template
                                        v-for="(link, linkIndex) in application.vpnApplicationLinks"
                                        v-if="link.hasOwnProperty('localOffice')">
                                    <%--body--%>
                                    <tr
                                            v-for="(localLoop, localLoopIndex) in link.localOffice.efrs"
                                            v-if="linkIndex==0"
                                    <%--&& localLoopIndex==0"--%>
                                    >
                                        <td><p>{{link.localOffice.officeName}}</p></td>
                                        <td>{{getPOPNameById(localLoop.popId)}}</td>
                                        <td><p style="text-align: center;padding-top:9px;">
                                            {{link.linkBandwidth}}
                                        </p></td>
                                        <td>
                                            <%--if ldp select previous ldp destination name--%>
                                            <select class="form-control"
                                                    v-model.number="localLoop.sourceType"
                                                    @change="onChangeSourceType($event,linkIndex,'localOffice',localLoopIndex)"
                                            >
                                                <option v-bind:value="-1">Select</option>
                                                <option v-bind:value="1">BTCL POP</option>
                                                <option v-bind:value="4">BTCL MUX</option>
                                                <option v-bind:value="2">BTCL LDP</option>
                                            </select>
                                        </td>
                                        <td>
                                            <p v-if="localLoop.sourceType==1">
                                                {{getPOPNameById(localLoop.popId)}}

                                            </p>
                                            <p v-else-if="localLoop.sourceType==2">
                                                <template
                                                        v-if="checkIfAlreadyToLDPSelected(linkIndex,'localOffice',localLoopIndex)"

                                                >
                                                    <%--yah show previous thing--%>
                                                    {{localLoop.source}}
                                                </template>
                                                <template v-else>
                                                    <input type="text" required
                                                           class="form-control"
                                                           v-model="localLoop.source"
                                                           placeholder="Enter Name"
                                                    />
                                                </template>
                                            </p>
                                            <p v-else>
                                                <input type="text" required
                                                       class="form-control"
                                                       v-model="localLoop.source"
                                                       placeholder="Enter Name"/>
                                            </p>
                                        </td>
                                        <td>
                                            <%--if destination is mux or ldp--%>
                                            <select v-if="localLoop.sourceType==4||localLoop.sourceType==2"
                                                    class="form-control"
                                                    v-model.number="localLoop.destinationType"
                                                    @change="onChangeDestinationType($event,linkIndex,'localOffice',localLoopIndex)"

                                            >
                                                <%--<option value="-1">Select</option>--%>
                                                <option v-bind:value="3">Customer End</option>
                                            </select>

                                            <select v-else class="form-control"
                                                    v-model.number="localLoop.destinationType"
                                                    @change="onChangeDestinationType($event,linkIndex,'localOffice',localLoopIndex)"

                                            >
                                                <%--<option v-bind:value="-1">Select</option>--%>
                                                <option v-bind:value="2">BTCL LDP</option>
                                                <option v-bind:value="4">BTCL MUX</option>
                                                <option v-bind:value="3">Customer End</option>
                                            </select>
                                        </td>
                                        <td>
                                            <%--if destination is client end--%>
                                            <p v-if="localLoop.destinationType==3">
                                                {{link.localOffice.officeAddress}}

                                            </p>

                                            <p v-else>
                                                <input type="text" required class="form-control"
                                                       v-model="localLoop.destination"
                                                       placeholder="Enter Name"/>
                                            </p>
                                        </td>
                                        <td>
                                            <%--TODO: vendorType should be changed to vendorType if needed--%>
                                            <select class="form-control"
                                                    v-model.number="localLoop.vendorType">
                                                <option v-bind:value="-1">Select</option>
                                                <option v-bind:value="1">BTCL</option>
                                                <option v-bind:value="2">Outsource Company</option>

                                            </select>
                                        </td>
                                        <td>
                                            <%--!!! -- fiber  -- !!!--%>
                                            <div v-if="localLoop.vendorType==2">
                                                <select
                                                        v-model="localLoop.vendorID"
                                                        class="form-control" name="divdata"
                                                >
                                                    <option disabled value="-1"> Select Vendor
                                                    </option>
                                                    <option :value="vendor.ID"
                                                            v-for="vendor in application.vendors">
                                                        {{vendor.label}}
                                                    </option>

                                                </select>
                                            </div>
                                            <div v-if="localLoop.vendorType==1">
                                                <select
                                                        v-model="localLoop.vendorID"
                                                        class="form-control" name="divdata"
                                                >
                                                    <option disabled value="-1"> Select Vendor
                                                    </option>
                                                    <option :value="fiberdivision.ID"
                                                            v-for="fiberdivision in application.fiberdivisions">
                                                        {{fiberdivision.label}}
                                                    </option>

                                                </select>
                                            </div>


                                        </td>
                                        <td>
                                            <select class="form-control" v-model.number="localLoop.ofcType">
                                                <option v-bind:value="0" disabled>Select OFC Type</option>
                                                <option v-bind:value="1">Single</option>
                                                <option v-bind:value="2">Double</option>
                                                <%--<option value="3">Customer End</option>--%>
                                            </select>
                                        </td>
                                        <td v-if="nextAction.param=='efr-response'">
                                            <input type="number"
                                                   style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                   v-model.number="localLoop.proposedLoopDistance"
                                                   placeholder="Enter Length">
                                        </td>
                                        <td>
                                            <button
                                                    v-if="link.localOffice.efrs.length>1"
                                                    type=button
                                            <%--@click="deleteLocalLoop(content, officeIndex, localLoopIndex, $event)"--%>
                                                    @click="deleteFromRepeater(link.localOffice, 'efrs', localLoopIndex)"
                                                    class="btn red btn-outline">
                                                <span class="glyphicon glyphicon-remove"></span>
                                            </button>
                                        </td>
                                    </tr>

                                    <%--add more loop option--%>
                                    <tr v-if="linkIndex==0&&link.localOffice.efrs.length>0">
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td v-if="nextAction.param=='efr-response'"></td>
                                        <td>
                                            <button type=button @click="addLoopToLocalEnd(linkIndex)"
                                                    class="btn green-haze btn-info">
                                                <span class="glyphicon glyphicon-plus"></span>
                                            </button>
                                        </td>
                                    </tr>
                                    <br/>
                                </template>
                            </template>

                            <template v-if="remoteOfficeExists()">
                                <%--if global link action selected--%>
                                <template v-if="localLinkIndex == -1 ">
                                    <template
                                            v-for="(link, linkIndex) in application.vpnApplicationLinks"
                                            v-if="link.hasOwnProperty('remoteOffice')">
                                        <tr
                                                v-for="(localLoop, localLoopIndex) in link.remoteOffice.efrs"
                                        <%--&& localLoopIndex==0"--%>
                                        >

                                            <%--office start--%>
                                            <td>
                                                <p>{{link.remoteOffice.officeName}}</p>
                                            </td>
                                            <%--office end--%>
                                            <td>{{getPOPNameById(localLoop.popId)}}</td>

                                            <td>
                                                <p style="text-align: center;padding-top:9px;">
                                                    {{link.linkBandwidth}}
                                                </p>

                                            </td>
                                            <td>
                                                <%--if ldp select previous ldp destination name--%>
                                                <select class="form-control"
                                                        v-model.number="localLoop.sourceType"
                                                        @change="onChangeSourceType($event,linkIndex,'remoteOffice',localLoopIndex)"
                                                >
                                                    <option v-bind:value="-1">Select</option>
                                                    <option v-bind:value="1">BTCL POP</option>
                                                    <option v-bind:value="4">BTCL MUX</option>
                                                    <option v-bind:value="2">BTCL LDP</option>
                                                </select>
                                            </td>
                                            <td>
                                                <p v-if="localLoop.sourceType==1">
                                                    <%--BTCL POP--%>
                                                    <%--<select disabled v-model="localLoop.popID"--%>
                                                    <%--class="form-control" name="divdata"--%>
                                                    <%-->--%>
                                                    <%--<option value="-1"> Select POP</option>--%>
                                                    <%--<option :value="pop.popID"--%>
                                                    <%--v-for="pop in application.ifr"--%>
                                                    <%--v-if="pop.isSelected==1">--%>
                                                    <%--{{pop.popName}}--%>
                                                    <%--</option>--%>
                                                    <%--</select>--%>

                                                    <%--{{pop.popName}}--%>
                                                    {{getPOPNameById(localLoop.popId)}}


                                                </p>
                                                <p v-else-if="localLoop.sourceType==2">
                                                    <template
                                                            v-if="checkIfAlreadyToLDPSelected(linkIndex,'remoteOffice',localLoopIndex)"
                                                    >
                                                        <%--yah show previous thing--%>
                                                        {{localLoop.source}}
                                                    </template>
                                                    <template v-else>
                                                        <input type="text" required
                                                               class="form-control"
                                                               v-model="localLoop.source"
                                                               placeholder="Enter Name"
                                                        />
                                                    </template>
                                                </p>
                                                <p v-else>
                                                    <input type="text" required
                                                           class="form-control"
                                                           v-model="localLoop.source"
                                                           placeholder="Enter Name"/>
                                                </p>
                                            </td>
                                            <td>
                                                <%--if destination is mux or ldp--%>
                                                <select v-if="localLoop.sourceType==4||localLoop.sourceType==2"
                                                        class="form-control"
                                                        v-model.number="localLoop.destinationType"
                                                        @change="onChangeDestinationType($event,linkIndex,'remoteOffice',localLoopIndex)"
                                                >
                                                    <%--<option value="-1">Select</option>--%>
                                                    <option v-bind:value="3">Customer End</option>
                                                </select>

                                                <select v-else class="form-control"
                                                        v-model.number="localLoop.destinationType"
                                                        @change="onChangeDestinationType($event,linkIndex,'remoteOffice',localLoopIndex)"
                                                >
                                                    <%--<option v-bind:value="-1">Select</option>--%>
                                                    <option v-bind:value="2">BTCL LDP</option>
                                                    <option v-bind:value="4">BTCL MUX</option>
                                                    <option v-bind:value="3">Customer End</option>
                                                </select>
                                            </td>
                                            <td>
                                                <%--if destination is client end--%>
                                                <p v-if="localLoop.destinationType==3">
                                                    {{link.remoteOffice.officeAddress}}
                                                    <%--<span v-for="office in application.offices"--%>
                                                    <%--v-if="office.id==localLoop.officeid">--%>
                                                    <%--{{office.officeAddress}}--%>
                                                    <%--</span>--%>
                                                    <%--<span v-else>--%>
                                                    <%--<input type="hidden" required--%>
                                                    <%--class="form-control"--%>
                                                    <%--v-model="localLoop.destination"--%>
                                                    <%--placeholder="Enter Name"/>--%>
                                                    <%--</span>--%>

                                                </p>

                                                <p v-else>
                                                    <input type="text" required class="form-control"
                                                           v-model="localLoop.destination"
                                                           placeholder="Enter Name"/>
                                                </p>
                                            </td>
                                            <td>
                                                <select class="form-control"
                                                        v-model.number="localLoop.vendorType">
                                                    <option v-bind:value="-1">Select</option>
                                                    <option v-bind:value="1">BTCL</option>
                                                    <option v-bind:value="2">Outsource Company</option>

                                                </select>
                                            </td>
                                            <td>
                                                <%--!!! -- fiber  -- !!!--%>
                                                <div v-if="localLoop.vendorType==2">
                                                    <select
                                                            v-model="localLoop.vendorID"
                                                            class="form-control" name="divdata"
                                                    >
                                                        <option disabled value="-1"> Select Vendor
                                                        </option>
                                                        <option :value="vendor.ID"
                                                                v-for="vendor in application.vendors">
                                                            {{vendor.label}}
                                                        </option>

                                                    </select>
                                                </div>
                                                <div v-if="localLoop.vendorType==1">
                                                    <select
                                                            v-model="localLoop.vendorID"
                                                            class="form-control" name="divdata"
                                                    >
                                                        <option disabled value="-1"> Select Vendor
                                                        </option>
                                                        <option :value="fiberdivision.ID"
                                                                v-for="fiberdivision in application.fiberdivisions">
                                                            {{fiberdivision.label}}
                                                        </option>

                                                    </select>
                                                </div>


                                            </td>
                                            <td>
                                                <select class="form-control" v-model.number="localLoop.ofcType">
                                                    <option v-bind:value="0" disabled>Select OFC Type</option>
                                                    <option v-bind:value="1">Single</option>
                                                    <option v-bind:value="2">Double</option>
                                                    <%--<option value="3">Customer End</option>--%>
                                                </select>
                                            </td>
                                            <td v-if="nextAction.param=='efr-response'">
                                                <input type="number"
                                                       style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                       v-model.number="localLoop.proposedLoopDistance"
                                                       placeholder="Enter Length">
                                            </td>
                                            <td>
                                                <button
                                                        v-if="link.remoteOffice.efrs.length>1"
                                                        type=button
                                                <%--@click="deleteLocalLoop(content, officeIndex, localLoopIndex, $event)"--%>
                                                        @click="deleteFromRepeater(link.remoteOffice, 'efrs', localLoopIndex)"
                                                        class="btn red btn-outline">
                                                    <span class="glyphicon glyphicon-remove"></span>
                                                </button>
                                            </td>
                                        </tr>

                                        <%--add more loop option--%>
                                        <tr
                                        <%--v-if="linkIndex==0"--%>
                                                v-if="link.remoteOffice.efrs.length>0"
                                        >
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td v-if="nextAction.param=='efr-response'"></td>
                                            <td></td>
                                            <td>
                                                <button type=button @click="addLoopToRemoteEnd(linkIndex)"
                                                        class="btn green-haze btn-info">
                                                    <span class="glyphicon glyphicon-plus"></span>
                                                </button>
                                            </td>
                                        </tr>
                                        <br/>
                                    </template>
                                </template>

                                <%--if specific link action selected--%>
                                <template v-else>
                                    <template

                                            v-for="(link, linkIndex) in application.vpnApplicationLinks"
                                            v-if="localLinkIndex==linkIndex && link.hasOwnProperty('remoteOffice')"
                                    >
                                        <%--body--%>
                                        <tr
                                                v-for="(localLoop, localLoopIndex) in link.remoteOffice.efrs"
                                        <%--&& localLoopIndex==0"--%>
                                        >

                                            <%--office start--%>
                                            <td>
                                                <p>{{link.remoteOffice.officeName}}</p>
                                            </td>
                                            <%--office end--%>
                                            <td>
                                                <%--{{localLoop.pop.label}}--%>
                                                {{link.remoteOffice.officeName}} POP

                                            </td>
                                            <td>
                                                <p style="text-align: center;padding-top:9px;">
                                                    {{link.linkBandwidth}}
                                                </p>

                                            </td>
                                            <td>
                                                <%--if ldp select previous ldp destination name--%>
                                                <select class="form-control"
                                                        v-model.number="localLoop.sourceType"
                                                        @change="onChangeSourceType($event,linkIndex,'remoteOffice',localLoopIndex)"
                                                >
                                                    <option v-bind:value="-1">Select</option>
                                                    <option v-bind:value="1">BTCL POP</option>
                                                    <option v-bind:value="4">BTCL MUX</option>
                                                    <option v-bind:value="2">BTCL LDP</option>
                                                </select>
                                            </td>
                                            <td>
                                                <p v-if="localLoop.sourceType==1">
                                                    <%--BTCL POP--%>
                                                    <%--<select disabled v-model="localLoop.popID"--%>
                                                    <%--class="form-control" name="divdata"--%>
                                                    <%-->--%>
                                                    <%--<option value="-1"> Select POP</option>--%>
                                                    <%--<option :value="pop.popID"--%>
                                                    <%--v-for="pop in application.ifr"--%>
                                                    <%--v-if="pop.isSelected==1">--%>
                                                    <%--{{pop.popName}}--%>
                                                    <%--</option>--%>
                                                    <%--</select>--%>

                                                    <%--{{pop.popName}}--%>
                                                    {{link.remoteOffice.officeName}} POP

                                                </p>
                                                <p v-else-if="localLoop.sourceType==2">
                                                    <template
                                                            v-if="checkIfAlreadyToLDPSelected(linkIndex,'remoteOffice',localLoopIndex)"
                                                    >
                                                        <%--yah show previous thing--%>
                                                        {{localLoop.source}}
                                                    </template>
                                                    <template v-else>
                                                        <input type="text" required
                                                               class="form-control"
                                                               v-model="localLoop.source"
                                                               placeholder="Enter Name"
                                                        />
                                                    </template>
                                                </p>
                                                <p v-else>
                                                    <input type="text" required
                                                           class="form-control"
                                                           v-model="localLoop.source"
                                                           placeholder="Enter Name"/>
                                                </p>
                                            </td>
                                            <td>
                                                <%--if destination is mux or ldp--%>
                                                <select v-if="localLoop.sourceType==4||localLoop.sourceType==2"
                                                        class="form-control"
                                                        v-model.number="localLoop.destinationType"
                                                        @change="onChangeDestinationType($event,linkIndex,'remoteOffice',localLoopIndex)"
                                                >
                                                    <%--<option value="-1">Select</option>--%>
                                                    <option v-bind:value="3">Customer End</option>
                                                </select>

                                                <select v-else class="form-control"
                                                        v-model.number="localLoop.destinationType"
                                                        @change="onChangeDestinationType($event,linkIndex,'remoteOffice',localLoopIndex)"
                                                >
                                                    <%--<option v-bind:value="-1">Select</option>--%>
                                                    <option v-bind:value="2">BTCL LDP</option>
                                                    <option v-bind:value="4">BTCL MUX</option>
                                                    <option v-bind:value="3">Customer End</option>
                                                </select>
                                            </td>
                                            <td>
                                                <%--if destination is client end--%>
                                                <p v-if="localLoop.destinationType==3">
                                                    {{link.remoteOffice.officeAddress}}
                                                    <%--<span v-for="office in application.offices"--%>
                                                    <%--v-if="office.id==localLoop.officeid">--%>
                                                    <%--{{office.officeAddress}}--%>
                                                    <%--</span>--%>
                                                    <%--<span v-else>--%>
                                                    <%--<input type="hidden" required--%>
                                                    <%--class="form-control"--%>
                                                    <%--v-model="localLoop.destination"--%>
                                                    <%--placeholder="Enter Name"/>--%>
                                                    <%--</span>--%>

                                                </p>

                                                <p v-else>
                                                    <input type="text" required class="form-control"
                                                           v-model="localLoop.destination"
                                                           placeholder="Enter Name"/>
                                                </p>
                                            </td>
                                            <td>
                                                <select class="form-control"
                                                        v-model.number="localLoop.vendorType">
                                                    <option v-bind:value="-1">Select</option>
                                                    <option v-bind:value="1">BTCL</option>
                                                    <option v-bind:value="2">Outsource Company</option>

                                                </select>
                                            </td>
                                            <td>
                                                <%--!!! -- fiber  -- !!!--%>
                                                <div v-if="localLoop.vendorType==2">
                                                    <select
                                                            v-model="localLoop.vendorID"
                                                            class="form-control" name="divdata"
                                                    >
                                                        <option disabled value="-1"> Select Vendor
                                                        </option>
                                                        <option :value="vendor.ID"
                                                                v-for="vendor in application.vendors">
                                                            {{vendor.label}}
                                                        </option>

                                                    </select>
                                                </div>
                                                <div v-if="localLoop.vendorType==1">
                                                    <select
                                                            v-model="localLoop.vendorID"
                                                            class="form-control" name="divdata"
                                                    >
                                                        <option disabled value="-1"> Select Vendor
                                                        </option>
                                                        <option :value="fiberdivision.ID"
                                                                v-for="fiberdivision in application.fiberdivisions">
                                                            {{fiberdivision.label}}
                                                        </option>

                                                    </select>
                                                </div>


                                            </td>
                                            <td>
                                                <select class="form-control" v-model.number="localLoop.ofcType">
                                                    <option v-bind:value="0" disabled>Select OFC Type</option>
                                                    <option v-bind:value="1">Single</option>
                                                    <option v-bind:value="2">Double</option>
                                                    <%--<option value="3">Customer End</option>--%>
                                                </select>
                                            </td>
                                            <td v-if="nextAction.param=='efr-response'">
                                                <input type="number"
                                                       style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                       v-model.number="localLoop.proposedLoopDistance"
                                                       placeholder="Enter Length">
                                            </td>
                                            <td>
                                                <button
                                                        v-if="link.remoteOffice.efrs.length>1"
                                                        type=button
                                                <%--@click="deleteLocalLoop(content, officeIndex, localLoopIndex, $event)"--%>
                                                        @click="deleteFromRepeater(link.remoteOffice, 'efrs', localLoopIndex)"
                                                        class="btn red btn-outline">
                                                    <span class="glyphicon glyphicon-remove"></span>
                                                </button>
                                            </td>
                                        </tr>

                                        <%--add more loop option--%>
                                        <tr
                                                v-if="link.remoteOffice.efrs.length>0"
                                        <%--v-if="linkIndex==0"--%>
                                        >
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td v-if="nextAction.param=='efr-response'"></td>

                                            <td>
                                                <button type=button @click="addLoopToRemoteEnd(linkIndex)"
                                                        class="btn green-haze btn-info">
                                                    <span class="glyphicon glyphicon-plus"></span>
                                                </button>
                                            </td>
                                        </tr>
                                        <br/>
                                    </template>
                                </template>
                            </template>
                            <br>


                        </table>
                    </div>

                    <div align="right"></div>
                    <%--</btcl-portlet>--%>
                    <%--</template>--%>
                    <%--</template>--%>
                    <%--</template>--%>
                </template>
            </div>
            <div class="modal-footer">
                <div class="form-group">
                    <div class="col-md-12"><input type="text"
                                                  v-model="comment"
                                                  placeholder="Comment Here"
                                                  class="form-control input-lg">
                    </div>
                </div>
                <br><br>
                <button type="button" class="btn btn-success" @click="postRequest()">Save<!--.once-->
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>

            </div>
        </div>
    </div>
</div>