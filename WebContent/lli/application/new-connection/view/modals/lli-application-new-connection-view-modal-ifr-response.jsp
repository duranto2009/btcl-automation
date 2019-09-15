<div class="modal fade" id="ifrrespondmodal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 85%">
        <div class="modal-content">
            <div>
                <div class="portlet-title">
                    <div class="caption" align="center"
                         style="background: #fab74d;padding: 10px;">Server Room Response
                    </div>
                </div>
                <div class="form-body">

                    <div class="table-responsive" style="overflow-x: inherit !important;">
                        <table class="table table-fit table-bordered" style="width: 100%;">
                            <thead>
                            <tr>
                                <th>Connection Office</th>
                                <th>Connection Office Address</th>
                                <th>Pop</th>
                                <th>Requested BW(Mbps)</th>
                                <th>Priority</th>
                                <th>Feasibility</th>
                                <th>Available BW(Mbps)</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(element,index) in modifiedIFR" :key="index">
                                <td>
                                    <multiselect v-model="element.office"
                                                 :options="officelist"
                                                 placeholder="Select Office"
                                                 label="officeName"
                                                 track-by="officeID"
                                                 :searchable="true" :allow-empty="false"
                                                 :close-on-select="true"
                                                 @select="forceUpdateDOM(element)"></multiselect>
                                </td>
                                <td>
                                    <span v-if="element.officeAddress!=null">{{element.officeAddress}}</span>
                                    <span v-else>{{element.office.officeAddress}}</span>
                                </td>

                                <td>
                                    <multiselect v-model="element.pop"
                                                 :options="application.popList"
                                                 placeholder="Select POP"
                                                 label="label"
                                    <%--track-by="ID"--%>
                                <%--track-by="ID"--%>
                                                 <%--:max-height="600"--%>
                                                 :searchable="true"
                                                 :close-on-select="true"
                                                 :show-labels="false"
                                                 placeholder="Pick a pop"
                                    <%--:allow-empty="false"--%>
                                                 @select="forceUpdateDOM"></multiselect>
                                </td>
                                <td>
                                    {{application.bandwidth}}
                                </td>


                                <td>
                                    <select class="form-control"
                                            v-model="element.priority">
                                        <option disabled value="0">Select Priority</option>
                                        <option value="1">High</option>
                                        <option value="2">Medium</option>
                                        <option value="3">Moderate</option>
                                    </select>
                                </td>

                                <td align="center">
                                    <select class="form-control" v-model="element.isSelected">
                                        <%--<option disabled selected hidden value="0">Select</option>--%>
                                        <option value="0">Not Possible</option>
                                        <option value="1">Possible</option>
                                        <%--<option  value="3">Moderate</option>--%>
                                    </select>
                                </td>
                                <td>
                                    <div v-if="element.isSelected==1">
                                        <input class="form-control" disabled="disabled"  v-model="element.availableBW=application.bandwidth"/>
                                    </div>
                                    <div v-else="">
                                        <input value="0" type="number"    class="form-control" required
                                               v-model="element.availableBW"/>
                                    </div>
                                </td>
                                <td
                                        v-if="(application.applicationType.ID==1) || (modifiedIFR.length > application.ifr.length )"
                                        class="col-md-1">
                                    <button v-if=" (index >= application.ifr.length )" type=button
                                            @click="serverRoomPOPDelete(index)"
                                            class="btn red btn-outline">
                                        <span aria-hidden="true" style="font-size:20px">×</span>
                                    </button>
                                </td>


                            </tr>
                            </tbody>
                        </table>
                    </div>


                    <%--for adding new --%>
                    <div align=right>
                        <button type=button
                                @click="serverRoomPOPAdd()"
                                class="btn green-haze btn-outline">
                            <span aria-hidden="true" style="font-size:20px">+</span>
                        </button>
                    </div>

                </div>
            </div>
            <br>
            <div class="modal-footer">
                <div class="form-group">
                    <div class="col-md-12">
                        <input type="text" v-model="comment" placeholder="Comment(*)"
                               class="form-control input-lg">
                    </div>
                </div>
                <br><br>
                <button type="button" class="btn btn-success" @click="serverRoomNextStep">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>
<div class="modal fade" id="ifrrespondmodalFORLocalLoop" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 65%">
        <div class="modal-content">
            <div>
                <div class="portlet-title">
                    <div class="caption" align="center"
                         style="background: #fab74d;padding: 10px;">Server Room Response
                    </div>
                </div>
                <div class="form-body">
                    <div class="form-group col-md-12 ">
                        <label class="control-label col-md-4 col-xs-12" style="text-align: center;">Connection
                            Office</label>
                        <label class="control-label col-md-3 col-xs-12" style="text-align: center;">POP</label>
                        <div class="control-label col-md-3"><p style="text-align: center;">Feasibility</p></div>
                        <div class="control-label col-md-1"><p style="text-align: center;"></p></div>
                    </div>

                    <div class="form-group col-md-12" v-for="(element,index) in modifiedIFRForNewLocalLoop"
                         :key="index">
                        <label class="control-label col-md-4" style="text-align: center;">
                            <multiselect v-model="element.office"
                                         :options="application.newOfficeList"
                                         placeholder="Select Office"
                                         label="officeName"
                                         track-by="officeID"></multiselect>

                        </label>

                        <label class="control-label col-md-3" style="text-align: center;">
                            <multiselect v-model="element.pop"
                                         :options="popList"
                                         placeholder="Select POP"
                                         label="label"
                                         track-by="ID"
                                         @select="forceUpdateDOM"
<%--                                         @click="forceUpdateDOM"--%>
                                         :searchable="true"
                                         :close-on-select="true"
                                         :show-labels="false"
                            >

                            </multiselect>

                        </label>
                        <div class="control-label col-md-3" align="center">
                            <%--<input type="checkbox" :true-value="1" v-bind:false-value="0" v-model="element.isSelected">--%>

                            <select class="form-control" v-model="element.isSelected">
                                <%--<option disabled selected hidden value="0">Select</option>--%>
                                <option value="0">Not Possible</option>
                                <option value="1">Possible</option>
                                <%--<option  value="3">Moderate</option>--%>
                            </select>


                        </div>

                        <div
                                v-if="!isItNewLocalLoop || (modifiedIFRForNewLocalLoop.length > application.ifr.length )"
                                class="col-md-1">
                            <button v-if=" (index >= application.ifr.length )" type=button
                                    @click="serverRoomPOPDeleteForNewLocalLoop(index)"
                                    class="btn red btn-outline">
                                <span aria-hidden="true" style="font-size:20px">×</span>
                            </button>
                        </div>
                    </div>


                    <%--for adding new --%>
                    <div align=right>
                        <button type=button
                                @click="serverRoomPOPAddForNewLocalLoop()"
                                class="btn green-haze btn-outline">
                            <span aria-hidden="true" style="font-size:20px">+</span>
                        </button>
                    </div>

                </div>
            </div>

            <btcl-field>
                <div class="col-md-12" title="Port Count">
                    <div>
                        <h3>Port Count : {{application.portCount}}</h3>
                    </div>
                </div>
            </btcl-field>
            <br>
            <div class="modal-footer">
                <div class="form-group">
                    <div class="col-md-12">
                        <input type="text" v-model="comment" placeholder="Comment(*)"
                               class="form-control input-lg">
                    </div>
                </div>
                <br><br>
                <button type="button" class="btn btn-success" @click.once="serverRoomNextStepNewLocalLoop">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>
<div class="modal fade" id="ifrrespondmodalAdditionalIP" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 65%">
        <div class="modal-content">
            <div>
                <div class="portlet-title">
                    <div class="caption" align="center"
                         style="background: #fab74d;padding: 10px;">Server Room Response
                    </div>
                </div>

                <div class="form-body">

                    <div class="table-responsive" style="overflow-x: inherit !important;">
                        <table class="table table-fit table-bordered" style="width: 100%;">
                            <thead>
                            <tr>
                                <th style="text-align: center">IP Count</th>
                                <th style="text-align: center">Feasibility</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td style="text-align: center">
                                    {{application.ipCount}}
                                </td>
                                <td style="text-align: center">
                                    <select class="form-control" v-model="application.isSelected">
                                        <option value="0">Not Possible</option>
                                        <option value="1">Possible</option>
                                    </select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <%--</div>--%>

                </div>
            </div>

            <br>
            <div class="modal-footer">
                <div class="form-group">
                    <div class="col-md-12">
                        <input type="text" v-model="comment" placeholder="Comment Here"
                               class="form-control input-lg">
                    </div>
                </div>
                <br><br>
                <button type="button" class="btn btn-success" @click="serverRoomNextStepAdditoinalIP">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>