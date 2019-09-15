<div class="modal fade" id="ldgmModal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 95%;">
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    <p>Choose Vendors</p>
                </div>
            </div>
            <div class="modal-body">
                <%--LDGM--%>
                <btcl-portlet>
                    <div class="table-responsive">
                        <table class="table table-fit table-bordered" style="width: 100%;">
                            <tr>
                                <th style="width: 10% !important;"><p style="text-align: center;">Office</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">POP</p></th>
                                <th><p style="text-align: center;">Requested BW(Mbps)</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">FROM</p></th>
                                <th><p style="text-align: center;">From Address</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">To</p></th>
                                <th><p style="text-align: center;">To Address</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">Loop Provider</p></th>
                                <th><p style="text-align: center;">Select Vendor</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">OFC Type</p></th>

                            </tr>

                            <template v-for="(office,officeIndex) in content.officeList">
                                <tr v-for="(localLoop, localLoopIndex) in office.localLoopList">
                                    <%--<hr/>--%>
                                    <%--office start--%>
                                    <td>
                                        <select v-model="localLoop.officeid"
                                                class="form-control"
                                                name="divdata">
                                            <option disabled v-bind:value="0"> Select Office</option>
                                            <option v-bind:value="office.id" :value="office.id"
                                                    v-for="office in application.officeList">
                                                {{office.officeName}}
                                            </option>
                                        </select>
                                    </td>
                                    <%--office end--%>
                                    <td>
                                        <select v-model="localLoop.popID"
                                                class="form-control"
                                                name="divdata">
                                            <option disabled value="-1"> Select POP</option>
                                            <option :value="pop.popID"
                                                    v-for="pop in application.ifr"
                                                    v-if="pop.isSelected==1 && pop.officeID==localLoop.officeid">
                                                {{pop.popName}}
                                            </option>
                                        </select>

                                    </td>
                                    <td>
                                        <p style="text-align: center;padding-top:9px;">
                                            {{localLoop.bandwidth}}
                                        </p>

                                    </td>
                                    <td>
                                        <%--if ldp select previous ldp destination name--%>
                                        <select class="form-control"
                                                v-model="localLoop.src">
                                            <option value="-1">Select</option>
                                            <option value="1">BTCL POP</option>
                                            <option value="4">BTCL MUX</option>
                                            <option value="2">BTCL LDP</option>
                                        </select>
                                    </td>
                                    <td>
                                        <p v-if="localLoop.src==1">
                                            <%--BTCL POP--%>
                                            <select disabled v-model="localLoop.popID"
                                                    class="form-control" name="divdata"
                                                    id="divisiondropdownxx">
                                                <option value="-1"> Select POP</option>
                                                <option :value="pop.popID"
                                                        v-for="pop in application.ifr"
                                                        v-if="pop.isSelected==1">
                                                    {{pop.popName}}
                                                </option>
                                            </select>

                                            <%--{{pop.popName}}--%>

                                        </p>
                                        <p v-else-if="localLoop.src==2">
                                            <template
                                                    v-if="checkIfAlreadyToLDPSelected(localLoop,office.localLoopList,localLoopIndex)">
                                                <%--yah show previous thing--%>
                                                {{localLoop.srcName}}
                                            </template>
                                            <template v-else>
                                                <input type="text" required
                                                       class="form-control"
                                                       v-model="localLoop.srcName"
                                                       placeholder="Enter Name"/>
                                            </template>
                                        </p>
                                        <p v-else>
                                            <input type="text" required
                                                   class="form-control"
                                                   v-model="localLoop.srcName"
                                                   placeholder="Enter Name"/>
                                        </p>
                                    </td>
                                    <td>
                                        <select v-if="localLoop.src==4||localLoop.src==2"
                                                class="form-control"
                                                v-model="localLoop.destination">
                                            <option value="-1">Select</option>
                                            <option value="3">Customer End</option>
                                        </select>
                                        <select v-else class="form-control"
                                                v-model="localLoop.destination">
                                            <option value="-1">Select</option>
                                            <option value="2">BTCL LDP</option>
                                            <option value="4">BTCL MUX</option>
                                            <option value="3">Customer End</option>
                                        </select>
                                    </td>
                                    <td>
                                        <p v-if="localLoop.destination==3">
                                            <span v-for="office in application.officeList"
                                                  v-if="office.id==localLoop.officeid">
                                              {{office.officeAddress}}
                                            </span>
                                            <span>
                                                <input type="hidden" required
                                                       class="form-control"
                                                       v-model="localLoop.destinationName"
                                                       placeholder="Enter Name"/>
                                            </span>
                                        </p>
                                        <p v-else>
                                            <input type="text" required
                                                   class="form-control"
                                                   v-model="localLoop.destinationName"
                                                   placeholder="Enter Name"/>
                                        </p>
                                    </td>
                                    <td>
                                        <select class="form-control"
                                                v-model="localLoop.loopProvider">
                                            <option value="-1">Select</option>
                                            <option value="1">BTCL</option>
                                            <option value="2">Outsource Company</option>

                                        </select>
                                    </td>
                                    <td>
                                        <%--!!! -- fiber  -- !!!--%>
                                        <div v-if="localLoop.loopProvider==2">
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
                                        <div v-if="localLoop.loopProvider==1">
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
                                        <select class="form-control" v-model="localLoop.ofcType">
                                            <option value="0" disabled>Select OFC Type</option>
                                            <option value="1">Single</option>
                                            <option value="2">Double</option>
                                            <%--<option value="3">Customer End</option>--%>
                                        </select>
                                    </td>
                                    <td>
                                        <button type=button
                                                @click="deleteLocalLoop(content, officeIndex, localLoopIndex, $event)"
                                                class="btn red btn-outline"><span
                                                class="glyphicon glyphicon-remove"></span>
                                        </button>
                                    </td>
                                </tr>


                                <div>
                                    <button type=button @click="addLocalLoop(office,content.bandwidth, $event)"
                                            class="btn green-haze btn-info">
                                        <span class="glyphicon glyphicon-plus"></span>
                                    </button>
                                </div>
                                <br/>


                            </template>
                        </table>
                    </div>

                    <div align="right">
                    </div>
                </btcl-portlet>
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
                <button type="button" class="btn btn-success" @click="processRequestForEFR('new-connection-process')">
                    Save
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>

            </div>
        </div>


    </div>
</div>


<div class="modal fade" id="efrForNewLocalLoop" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 95%;">
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    <p>Choose Vendors</p>
                </div>
            </div>
            <div class="modal-body">
                <%--LDGM--%>
                <btcl-portlet>
                    <div style="width: 100%!important;" class="table-responsive">
                        <table style="width: 100%" class="table table-fit table-bordered">
                            <tr>
                                <th style="width: 10% !important;"><p style="text-align: center;">Office</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">POP</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">FROM</p></th>
                                <th><p style="text-align: center;">From Address</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">To</p></th>
                                <th><p style="text-align: center;">To Address</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">Loop Provider</p></th>
                                <th><p style="text-align: center;">Select Vendor</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">OFC Type</p></th>

                            </tr>

                            <template v-for="(office,officeIndex) in content.newOfficeList">
                                <tr v-for="(localLoop, localLoopIndex) in office.localLoopList">
                                    <%--<hr/>--%>
                                    <%--office start--%>
                                    <td>
                                        <select v-model="localLoop.officeid"
                                                class="form-control"
                                                name="divdata">
                                            <option disabled v-bind:value="0"> Select Office</option>
                                            <option v-bind:value="office.id" :value="office.id"
                                                    v-for="office in application.newOfficeList">
                                                {{office.officeName}}
                                            </option>
                                        </select>
                                    </td>
                                    <%--office end--%>
                                    <td>
                                        <select v-model="localLoop.popID"
                                                class="form-control">
                                            <option disabled value="-1"> Select POP</option>
                                            <option :value="pop.popID"
                                                    v-for="pop in application.ifr"
                                                    v-if="pop.isSelected==1
                                                    && pop.officeID==localLoop.officeid"
                                            >
                                                {{pop.popName}}
                                            </option>
                                        </select>

                                    </td>
                                    <td>
                                        <%--if ldp select previous ldp destination name--%>
                                        <select class="form-control"
                                                v-model="localLoop.src">
                                            <option value="-1">Select</option>
                                            <option value="1">BTCL POP</option>
                                            <option value="4">BTCL MUX</option>
                                            <option value="2">BTCL LDP</option>
                                        </select>
                                    </td>
                                    <td>
                                        <p v-if="localLoop.src==1">
                                            <%--BTCL POP--%>
                                            <select disabled v-model="localLoop.popID"
                                                    class="form-control" name="divdata"
                                                    id="divisiondropdownxx">
                                                <option value="-1"> Select POP</option>
                                                <option :value="pop.popID"
                                                        v-for="pop in application.ifr"
                                                        v-if="pop.isSelected==1"
                                                >
                                                    {{pop.popName}}
                                                </option>
                                            </select>

                                            <%--{{pop.popName}}--%>

                                        </p>
                                        <p v-else-if="localLoop.src==2">
                                            <template
                                                    v-if="checkIfAlreadyToLDPSelected(localLoop,office.localLoopList,localLoopIndex)">
                                                <%--yah show previous thing--%>
                                                {{localLoop.srcName}}
                                            </template>
                                            <template v-else>
                                                <input type="text" required
                                                       class="form-control"
                                                       v-model="localLoop.srcName"
                                                       placeholder="Enter Name"/>
                                            </template>
                                        </p>


                                        <p v-else>
                                            <input type="text" required
                                                   class="form-control"
                                                   v-model="localLoop.srcName"
                                                   placeholder="Enter Name"/>
                                        </p>
                                    </td>
                                    <td>
                                        <select v-if="localLoop.src==4||localLoop.src==2"
                                                class="form-control"
                                                v-model="localLoop.destination">
                                            <option value="-1">Select</option>
                                            <option value="3">Customer End</option>
                                        </select>

                                        <select v-else class="form-control"
                                                v-model="localLoop.destination">
                                            <option value="-1">Select</option>
                                            <option value="2">BTCL LDP</option>
                                            <option value="4">BTCL MUX</option>
                                            <option value="3">Customer End</option>
                                        </select>
                                    </td>
                                    <td>
                                        <p v-if="localLoop.destination==3">
                                           <span v-for="office in application.newOfficeList"
                                                                  v-if="office.id==localLoop.officeid">
                                                              {{office.officeAddress}}
                                           </span>
                                            <span>
                                                                    <input type="hidden" required
                                                                           class="form-control"
                                                                           v-model="localLoop.destinationName"
                                                                           placeholder="Enter Name"/>
                                                            </span>

                                        </p>

                                        <p v-else>
                                            <input type="text" required class="form-control"
                                                   v-model="localLoop.destinationName"
                                                   placeholder="Enter Name"/>
                                        </p>
                                    </td>
                                    <td>
                                        <select class="form-control"
                                                v-model="localLoop.loopProvider">
                                            <option value="-1">Select</option>
                                            <option value="1">BTCL</option>
                                            <option value="2">Outsource Company</option>

                                        </select>
                                    </td>
                                    <td>
                                        <%--!!! -- fiber  -- !!!--%>
                                        <div v-if="localLoop.loopProvider==2">
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
                                        <div v-if="localLoop.loopProvider==1">
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
                                        <select class="form-control" v-model="localLoop.ofcType">
                                            <option value="0" disabled>Select OFC Type</option>
                                            <option value="1">Single</option>
                                            <option value="2">Double</option>
                                            <%--<option value="3">Customer End</option>--%>
                                        </select>
                                    </td>
                                    <td>
                                        <button type=button
                                                @click="deleteNewLocalLoop(content, officeIndex, localLoopIndex, $event)"
                                                class="btn red btn-outline"><span
                                                class="glyphicon glyphicon-remove"></span>
                                        </button>
                                    </td>
                                </tr>


                                <div>
                                    <button type=button @click="addLocalLoop(office,content.bandwidth, $event)"
                                            class="btn green-haze btn-info">
                                        <span class="glyphicon glyphicon-plus"></span>
                                    </button>
                                </div>
                                <br/>


                            </template>
                        </table>
                    </div>

                    <div align="right">
                    </div>
                </btcl-portlet>
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
                <button type="button" class="btn btn-success"
                        @click.once="processEFRRequestForNewLocalLoop('new-connection-process')">Save
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>

            </div>
        </div>


    </div>
</div>

<div class="modal fade" id="efrForNewLocalLoopForUpgrade" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 95%;">
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">
                    <p>Choose Vendors</p>
                </div>
            </div>
            <div class="modal-body">
                <%--LDGM--%>
                <btcl-portlet>
                    <div class="table-responsive">
                        <table style="width: 100% !important;" class="table table-fit table-bordered">
                            <tr>
                                <th style="width: 10% !important;"><p style="text-align: center;">Office</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">POP</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">FROM</p></th>
                                <th><p style="text-align: center;">From Address</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">To</p></th>
                                <th><p style="text-align: center;">To Address</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">Loop Provider</p></th>
                                <th><p style="text-align: center;">Select Vendor</p></th>
                                <th style="width: 10% !important;"><p style="text-align: center;">OFC Type</p></th>

                            </tr>

                            <template v-for="(office,officeIndex) in content.newOfficeList">
                                <tr v-for="(localLoop, localLoopIndex) in office.localLoopList">
                                    <%--<hr/>--%>
                                    <%--office start--%>
                                    <td>
                                        <select v-model="localLoop.officeid"
                                                class="form-control">
                                            <option disabled v-bind:value="0"> Select Office</option>
                                            <option v-bind:value="office.id" :value="office.id"
                                                    v-for="office in application.newOfficeList">
                                                {{office.officeName}}
                                            </option>
                                        </select>
                                    </td>
                                    <%--office end--%>
                                    <td>
                                        <select v-model="localLoop.popID"
                                                class="form-control">
                                            <option disabled value="-1"> Select POP</option>
                                            <option :value="pop.popID"
                                                    v-for="pop in application.ifr"
                                                    v-if="pop.isSelected==1
                                                    && pop.popID==localLoop.pop.ID"
                                            >
                                                {{pop.popName}}
                                            </option>
                                        </select>

                                    </td>
                                    <td>
                                        <%--if ldp select previous ldp destination name--%>
                                        <select class="form-control"
                                                v-model="localLoop.src">
                                            <option value="-1">Select</option>
                                            <option value="1">BTCL POP</option>
                                            <option value="4">BTCL MUX</option>
                                            <option value="2">BTCL LDP</option>
                                        </select>
                                    </td>
                                    <td>
                                        <p v-if="localLoop.src==1">
                                            <%--BTCL POP--%>
                                            <select disabled v-model="localLoop.popID"
                                                    class="form-control" name="divdata"
                                                    id="divisiondropdownxx">
                                                <option value="-1"> Select POP</option>
                                                <option :value="pop.popID"
                                                        v-for="pop in application.ifr"
                                                        v-if="pop.isSelected==1">
                                                    {{pop.popName}}
                                                </option>
                                            </select>

                                            <%--{{pop.popName}}--%>

                                        </p>
                                        <p v-else-if="localLoop.src==2">
                                            <template
                                                    v-if="checkIfAlreadyToLDPSelected(localLoop,office.localLoopList,localLoopIndex)">
                                                <%--yah show previous thing--%>
                                                {{localLoop.srcName}}
                                            </template>
                                            <template v-else>
                                                <input type="text" required
                                                       class="form-control"
                                                       v-model="localLoop.srcName"
                                                       placeholder="Enter Name"/>
                                            </template>
                                        </p>


                                        <p v-else>
                                            <input type="text" required
                                                   class="form-control"
                                                   v-model="localLoop.srcName"
                                                   placeholder="Enter Name"/>
                                        </p>
                                    </td>
                                    <td>
                                        <select v-if="localLoop.src==4||localLoop.src==2"
                                                class="form-control"
                                                v-model="localLoop.destination">
                                            <option value="-1">Select</option>
                                            <option value="3">Customer End</option>
                                        </select>

                                        <select v-else class="form-control"
                                                v-model="localLoop.destination">
                                            <option value="-1">Select</option>
                                            <option value="2">BTCL LDP</option>
                                            <option value="4">BTCL MUX</option>
                                            <option value="3">Customer End</option>
                                        </select>
                                    </td>
                                    <td>
                                        <p v-if="localLoop.destination==3">
                                                            <span v-for="office in application.newOfficeList"
                                                                  v-if="office.id==localLoop.officeid">
                                                              {{office.officeAddress}}
                                                            </span>
                                            <span>
                                                                    <input type="hidden" required
                                                                           class="form-control"
                                                                           v-model="localLoop.destinationName"
                                                                           placeholder="Enter Name"/>
                                                            </span>

                                        </p>

                                        <p v-else>
                                            <input type="text" required class="form-control"
                                                   v-model="localLoop.destinationName"
                                                   placeholder="Enter Name"/>
                                        </p>
                                    </td>
                                    <td>
                                        <select class="form-control"
                                                v-model="localLoop.loopProvider">
                                            <option value="-1">Select</option>
                                            <option value="1">BTCL</option>
                                            <option value="2">Outsource Company</option>

                                        </select>
                                    </td>
                                    <td>
                                        <%--!!! -- fiber  -- !!!--%>
                                        <div v-if="localLoop.loopProvider==2">
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
                                        <div v-if="localLoop.loopProvider==1">
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
                                        <select class="form-control" v-model="localLoop.ofcType">
                                            <option value="0" disabled>Select OFC Type</option>
                                            <option value="1">Single</option>
                                            <option value="2">Double</option>
                                            <%--<option value="3">Customer End</option>--%>
                                        </select>
                                    </td>
                                    <td>
                                        <button type=button
                                                @click="deleteNewLocalLoop(content, officeIndex, localLoopIndex, $event)"
                                                class="btn red btn-outline"><span
                                                class="glyphicon glyphicon-remove"></span>
                                        </button>
                                    </td>
                                </tr>


                                <div>
                                    <button type=button @click="addLocalLoop(office,content.bandwidth, $event)"
                                            class="btn green-haze btn-info">
                                        <span class="glyphicon glyphicon-plus"></span>
                                    </button>
                                </div>
                                <br/>


                            </template>
                        </table>
                    </div>

                    <div align="right">
                    </div>
                </btcl-portlet>
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
                <button type="button" class="btn btn-success"
                        @click.once="processEFRRequestForNewLocalLoop('new-connection-process')">Save
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>

            </div>
        </div>


    </div>
</div>
