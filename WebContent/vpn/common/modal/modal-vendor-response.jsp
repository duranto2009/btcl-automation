<%--
  User: forhad
  Date: 2/17/19
  Time: 11:59 AM
--%>
<div class="modal fade" id="modal-vendor-response" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 95%;">
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    <p>Feasibility</p>
                </div>
            </div>
            <div class="modal-body">

                <div class="table-responsive">
                    <table class="table table-fit table-bordered">

                        <tr>
                            <th style="width: 10% !important;"><p style="text-align: center;">Office</p>
                            </th>
                            <th style="width: 10% !important;"><p style="text-align: center;">POP</p>
                            </th>
                            <th><p style="text-align: center;">Requested BW(Mbps)</p></th>
                            <th style="width: 10% !important;"><p style="text-align: center;">FROM</p>
                            </th>
                            <th><p style="text-align: center;">From Address</p></th>
                            <th style="width: 10% !important;"><p style="text-align: center;">To</p>
                            </th>
                            <th><p style="text-align: center;">To Address</p></th>
                            <th style="width: 10% !important;"><p style="text-align: center;">Loop
                                Provider</p>
                            </th>
                            <%--<th><p style="text-align: center;">Select Vendor</p></th>--%>
                            <th style="width: 10% !important;"><p style="text-align: center;">OFC
                                Type</p></th>
                            <th v-if="nextAction.param=='complete-work-order'"
                                style="width: 10% !important;"><p style="text-align: center;">Proposed
                                Loop Distance(m)</p></th>
                            <th v-if="nextAction.param=='complete-work-order'"
                                style="width: 10% !important;"><p style="text-align: center;">Actual
                                Loop Distance(m)</p></th>
                            <th v-if="nextAction.param=='efr-response' || picked.param=='efr-response'"><p style="text-align: center;">
                                Proposed Loop Distance(m)</p>
                            </th>
                            <th><p style="text-align: center;"></p></th>

                        </tr>
                        <%--local office end--%>
                        <template v-if="localOfficeExists()">
                            <template
                                    v-for="(link, linkIndex) in application.vpnApplicationLinks"
                                    v-if="link.hasOwnProperty('localOffice')"

                            >

                                <%--header: show the header only once--%>
                                <template v-if="link.localOffice.efrs.length>0">


                                </template>


                                <%--body--%>
                                <template
                                        v-for="(localLoop, localLoopIndex) in link.localOffice.efrs"
                                        v-if="linkIndex==0">
                                    <tr

                                    <%--&& localLoopIndex==0"--%>
                                    >
                                        <td><p>{{link.localOffice.officeName}}</p></td>
                                        <td>{{getPOPNameById(localLoop.popId)}}
                                        </td>
                                        <td><p style="text-align: center;padding-top:9px;">
                                            {{link.linkBandwidth}}
                                        </p></td>
                                        <td>
                                            <%--if ldp select previous ldp destination name--%>
                                            <select disabled class="form-control"
                                                    v-model.number="localLoop.sourceType">
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

                                            <p v-else>

                                                {{localLoop.source}}</p>
                                        </td>
                                        <td>
                                            <%--if destination is mux or ldp--%>
                                            <select v-if="localLoop.sourceType==4||localLoop.sourceType==2"
                                                    disabled
                                                    class="form-control"
                                                    v-model.number="localLoop.destinationType">
                                                <%--<option value="-1">Select</option>--%>
                                                <option v-bind:value="3">Customer End</option>
                                            </select>

                                            <select v-else class="form-control"
                                                    disabled
                                                    v-model.number="localLoop.destinationType">
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

                                                {{link.localOffice.officeAddress}}
                                            </p>
                                        </td>
                                        <td>
                                            <%--TODO: vendorType should be changed to vendorType if needed--%>

                                                <div v-if="localLoop.vendorType==2">
                                                    <select disabled
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
                                                    <select disabled
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



                                        <%--</td>--%>
                                        <td>
                                            <select class="form-control" v-model.number="localLoop.ofcType" disabled>
                                                <option v-bind:value="0" disabled>Select OFC Type</option>
                                                <option v-bind:value="1">Single</option>
                                                <option v-bind:value="2">Double</option>
                                                <%--<option value="3">Customer End</option>--%>
                                            </select>
                                        </td>
                                        <td v-if="nextAction.param=='complete-work-order'">
                                            {{localLoop.proposedLoopDistance}}
                                        </td>
                                        <td v-if="nextAction.param=='complete-work-order'">
                                            <input type="number"
                                                   style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                   v-model.number="localLoop.actualLoopDistance"
                                                   placeholder="Enter Actual Length">
                                        </td>


                                        <td v-if="nextAction.param=='efr-response' || picked.param=='efr-response'">
                                            <p v-if="localLoop.isReplied">
                                                {{localLoop.proposedLoopDistance}}
                                            </p>
                                            <input v-else type="number"
                                                   style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                   v-model.number="localLoop.proposedLoopDistance"
                                                   placeholder="Enter Length">
                                        </td>
                                        <td>

                                        </td>
                                    </tr>
                                </template>

                                <%--add more loop option--%>
                                <template v-if="linkIndex==0">

                                </template>
                                <%--<br/>--%>
                            </template>
                        </template>
                        <%--<br>--%>
                        <%--remote office end--%>
                        <template v-if="remoteOfficeExists()">
                            <template
                                    v-for="(link, linkIndex) in application.vpnApplicationLinks"
                                    v-if="localLinkIndex == -1 && link.hasOwnProperty('remoteOffice')"
                            >
                                <%--body--%>
                                <template v-for="(localLoop, localLoopIndex) in link.remoteOffice.efrs">
                                    <tr

                                    <%--&& localLoopIndex==0"--%>
                                    >

                                        <%--office start--%>
                                        <td>
                                            <p>{{link.remoteOffice.officeName}}</p>
                                        </td>
                                        <%--office end--%>
                                        <td>
                                            <%--{{localLoop.pop.label}}--%>
                                            {{getPOPNameById(localLoop.popId)}}


                                        </td>
                                        <td>
                                            <p style="text-align: center;padding-top:9px;">
                                                {{link.linkBandwidth}}
                                            </p>

                                        </td>
                                        <td>
                                            <%--if ldp select previous ldp destination name--%>
                                            <select disabled class="form-control"
                                                    v-model.number="localLoop.sourceType">
                                                <option v-bind:value="-1">Select</option>
                                                <option v-bind:value="1">BTCL POP</option>
                                                <option v-bind:value="4">BTCL MUX</option>
                                                <option v-bind:value="2">BTCL LDP</option>
                                            </select>
                                        </td>
                                        <td>
                                            <p>{{localLoop.source}}</p>
                                        </td>
                                        <td>
                                            <%--if destination is mux or ldp--%>
                                            <select disabled v-if="localLoop.sourceType==4||localLoop.sourceType==2"
                                                    class="form-control"
                                                    v-model.number="localLoop.destinationType">
                                                <%--<option value="-1">Select</option>--%>
                                                <option v-bind:value="3">Customer End</option>
                                            </select>

                                            <select disabled v-else class="form-control"
                                                    v-model.number="localLoop.destinationType">
                                                <%--<option v-bind:value="-1">Select</option>--%>
                                                <option v-bind:value="2">BTCL LDP</option>
                                                <option v-bind:value="4">BTCL MUX</option>
                                                <option v-bind:value="3">Customer End</option>
                                            </select>
                                        </td>
                                        <td>
                                            <%--if destination is client end--%>
                                            <p v-if="localLoop.destinationType==3">
                                                {{link.remoteOffice.officeAddress}}</p>

                                            <p v-else>{{localLoop.destination}}</p>
                                        </td>
                                        <td>
                                            <%--!!! -- fiber  -- !!!--%>
                                            <div v-if="localLoop.vendorType==2">
                                                <select disabled
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
                                                <select disabled
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
                                            <select class="form-control" v-model.number="localLoop.ofcType" disabled>
                                                <option v-bind:value="0" disabled>Select OFC Type</option>
                                                <option v-bind:value="1">Single</option>
                                                <option v-bind:value="2">Double</option>
                                                <%--<option value="3">Customer End</option>--%>
                                            </select>
                                        </td>
                                        <td v-if="nextAction.param=='complete-work-order'">
                                            {{localLoop.proposedLoopDistance}}
                                        </td>
                                        <td v-if="nextAction.param=='complete-work-order'">
                                            <input type="number"
                                                   style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                   v-model.number="localLoop.actualLoopDistance"
                                                   placeholder="Enter Actual Length">
                                        </td>

                                        <td v-if="nextAction.param=='efr-response' || picked.param=='efr-response'">
                                            <p v-if="localLoop.isReplied">
                                                {{localLoop.proposedLoopDistance}}
                                            </p>
                                            <input v-else type="number"
                                                   style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                   v-model.number="localLoop.proposedLoopDistance"
                                                   placeholder="Enter Length">
                                        </td>
                                        <td>
                                        </td>
                                    </tr>
                                </template>

                            </template>

                            <%--if specific link action selected--%>
                            <template
                                    v-else
                                    v-for="(link, linkIndex) in application.vpnApplicationLinks"
                                    v-if="localLinkIndex==linkIndex && link.hasOwnProperty('remoteOffice')"
                            >
                                <%--body--%>
                                <template v-for="(localLoop, localLoopIndex) in link.remoteOffice.efrs">

                                    <%--</tr>--%>
                                        <tr>

                                            <%--office start--%>
                                            <td>
                                                <p>{{link.remoteOffice.officeName}}</p>
                                            </td>
                                            <%--office end--%>
                                            <td>
                                                <%--{{localLoop.pop.label}}--%>
                                                {{getPOPNameById(localLoop.popId)}}


                                            </td>
                                            <td>
                                                <p style="text-align: center;padding-top:9px;">
                                                    {{link.linkBandwidth}}
                                                </p>

                                            </td>
                                            <td>
                                                <%--if ldp select previous ldp destination name--%>
                                                <select disabled class="form-control"
                                                        v-model.number="localLoop.sourceType">
                                                    <option v-bind:value="-1">Select</option>
                                                    <option v-bind:value="1">BTCL POP</option>
                                                    <option v-bind:value="4">BTCL MUX</option>
                                                    <option v-bind:value="2">BTCL LDP</option>
                                                </select>
                                            </td>
                                            <td>
                                                <p>{{localLoop.source}}</p>
                                            </td>
                                            <td>
                                                <%--if destination is mux or ldp--%>
                                                <select disabled v-if="localLoop.sourceType==4||localLoop.sourceType==2"
                                                        class="form-control"
                                                        v-model.number="localLoop.destinationType">
                                                    <%--<option value="-1">Select</option>--%>
                                                    <option v-bind:value="3">Customer End</option>
                                                </select>

                                                <select disabled v-else class="form-control"
                                                        v-model.number="localLoop.destinationType">
                                                    <%--<option v-bind:value="-1">Select</option>--%>
                                                    <option v-bind:value="2">BTCL LDP</option>
                                                    <option v-bind:value="4">BTCL MUX</option>
                                                    <option v-bind:value="3">Customer End</option>
                                                </select>
                                            </td>
                                            <td>
                                                <%--if destination is client end--%>
                                                <p v-if="localLoop.destinationType==3">
                                                    {{link.remoteOffice.officeAddress}}</p>

                                                <p v-else>{{localLoop.destination}}</p>
                                            </td>
                                            <td>
                                                <%--!!! -- fiber  -- !!!--%>
                                                <div v-if="localLoop.vendorType==2">
                                                    <select disabled
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
                                                    <select disabled
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
                                                <select class="form-control" v-model.number="localLoop.ofcType" disabled>
                                                    <option v-bind:value="0" disabled>Select OFC Type</option>
                                                    <option v-bind:value="1">Single</option>
                                                    <option v-bind:value="2">Double</option>
                                                    <%--<option value="3">Customer End</option>--%>
                                                </select>
                                            </td>
                                            <td v-if="nextAction.param=='complete-work-order'">
                                                {{localLoop.proposedLoopDistance}}
                                            </td>
                                            <td v-if="nextAction.param=='complete-work-order'">
                                                <input type="number"
                                                       style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                       v-model.number="localLoop.actualLoopDistance"
                                                       placeholder="Enter Actual Length">
                                            </td>

                                            <td v-if="nextAction.param=='efr-response' || picked.param=='efr-response'">
                                                <p v-if="localLoop.isReplied">
                                                    {{localLoop.proposedLoopDistance}}
                                                </p>
                                                <input v- type="number"
                                                       style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                                       v-model.number="localLoop.proposedLoopDistance"
                                                       placeholder="Enter Length">
                                            </td>
                                            <td>
                                                <%--<button type=button--%>
                                                <%--@click="deleteLocalLoop(content, officeIndex, localLoopIndex, $event)"--%>
                                                <%--class="btn red btn-outline"><span--%>
                                                <%--class="glyphicon glyphicon-remove"></span>--%>
                                                <%--</button>--%>
                                            </td>
                                        </tr>
                                </template>
                            </template>

                        </template>


                    </table>
                </div>
                <div align="center">
                <%--<input type="text" placeholder="Enter Contact Information">--%>

                <template v-if="nextAction.param=='complete-work-order'">
                    <btcl-portlet>
                    <btcl-input title="Enter Contact Number" :text.sync="vendorContact" placeholder="Write Contact Number"></btcl-input>
                    <%--<btcl-field title="Worked With Server Room">--%>
                    <br>
                        <input type="checkbox" id="checkbox" v-model="isCollaborated">
                    <label for="checkbox">Worked With Server Room?(if yes then check the tick)</label>
                    <%--</btcl-field>--%>
                    </btcl-portlet>
                </template>

                </div>
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