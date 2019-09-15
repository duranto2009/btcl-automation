<div class="modal fade" id="ifrrespondmodal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 75%">
        <div class="modal-content">
            <div>
                <div class="portlet-title">
                    <div class="caption" align="center"
                         style="background: #fab74d;padding: 10px;">Server Room Response
                    </div>
                </div>
                <div class="form-body">
                    <div class="form-group col-md-12 ">
                        <label class=" col-md-4 col col-xs-12" style="text-align: center;">Office</label>
                        <label class=" col-md-4 col col-xs-12" style="text-align: center;">POP</label>
                        <div class="col-md-2"><p style="text-align: center;">Feasibility</p></div>
                        <div class="col-md-1"><p style="text-align: center;"></p></div>
                    </div>


                    <div v-if="element.replied==0" class="form-group col-md-12" v-for="(element,index) in modifiedIFR"
                         :key="index">
                        <label class=" col-md-4" style="text-align: center;">
                            <multiselect v-model="element.office"
                                         :options="officelist"
                                         placeholder="Select Office"
                                         label="name"
                                         track-by="office"></multiselect>

                        </label>

                        <label class=" col-md-4" style="text-align: center;">
                            <multiselect v-model="element.pop"
                                         :options="popList"
                                         placeholder="Select POP"
                                         label="label"
                                         track-by="ID"></multiselect>

                        </label>
                        <div class="col-md-2" align="center">
                            <%--<input type="checkbox" :true-value="1" v-bind:false-value="0" v-model="element.isSelected">--%>

                            <select class="form-control" v-model="element.selected">
                                <%--<option disabled selected hidden value="0">Select</option>--%>
                                <option  value="0">Not Possible</option>
                                <option  value="1">Possible</option>
                                <%--<option  value="3">Moderate</option>--%>
                            </select>



                        </div>

                        <div
                                v-if="(application.applicationType.ID==1) || (modifiedIFR.length > application.ifr.length )"
                                class="col-md-1">
                            <button <%--v-if=" (index >= application.ifr.length )"--%> type=button
                                    @click="serverRoomPOPDelete(index)"
                                    class="btn red btn-outline">
                                <span aria-hidden="true" style="font-size:20px">×</span>
                            </button>
                        </div>


                    </div>

                    <%--for adding new --%>
                    <div  align=right>
                        <button type=button
                                @click="serverRoomPOPAdd()"
                                class="btn green-haze btn-outline">
                            <span aria-hidden="true" style="font-size:20px">+</span>
                        </button>
                    </div>

                </div>
            </div>
            <br>

            <btcl-bounded>
                <div class="col-md-6" style="text-align: left">
                    <label>Port Type:</label>
                    <label>{{application.portTypeString}}</label>
                </div>
                <div class="col-md-6" style="text-align: left">
                    <label>Port Count:</label>
                    <label>{{application.portCount}}</label>
                </div>
            </btcl-bounded>


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
<div v-if="isUpgradeLoaded" class="modal fade" id="ifrrespondmodalUpgrade" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 75%">
        <div class="modal-content">
            <div>
                <div class="portlet-title">
                    <div class="caption" align="center"
                         style="background: #fab74d;padding: 10px;">Server Room Response!!!
                    </div>
                </div>
                <div class="form-body">
                    <div class="form-group col-md-12 ">
                        <label class=" col-md-4 col col-xs-12" style="text-align: center;">Office</label>
                        <label class=" col-md-4 col col-xs-12" style="text-align: center;">POP</label>
                        <div class="col-md-2"><p style="text-align: center;">Feasibility</p></div>
                        <div class="col-md-1"><p style="text-align: center;"></p></div>
                    </div>


                    <div class="form-group col-md-12" v-for="(element,index) in modifiedIFR"
                         :key="index">
                        <label class=" col-md-4" style="text-align: center;">
                            <multiselect disabled v-model="element.office"
                                         :options="officelist"
                                         placeholder="Select Office"
                                         label="name"
                                         track-by="office"></multiselect>

                        </label>

                        <label class=" col-md-4" style="text-align: center;">
                            <multiselect disabled v-model="element.pop"
                                         :options="popList"
                                         placeholder="Select POP"
                                         label="label"
                                         track-by="ID"></multiselect>

                        </label>
                        <div class="col-md-2" align="center">
                            <%--<input type="checkbox" :true-value="1" v-bind:false-value="0" v-model="element.isSelected">--%>

                            <select class="form-control" v-model="element.selected">
                                <%--<option disabled selected hidden value="0">Select</option>--%>
                                <option  value="0">Not Possible</option>
                                <option  value="1">Possible</option>
                                <%--<option  value="3">Moderate</option>--%>
                            </select>



                        </div>

                        <div
                                v-if="(modifiedIFR.length > application.ifr.length )"
                                class="col-md-1">
                            <button type=button
                                    @click="serverRoomPOPDelete(index)"
                                    class="btn red btn-outline">
                                <span aria-hidden="true" style="font-size:20px">×</span>
                            </button>
                        </div>


                    </div>

                    <%--for adding new --%>
<%--                    <div align=right>--%>
<%--                        <button type=button--%>
<%--                                @click="serverRoomPOPAdd()"--%>
<%--                                class="btn green-haze btn-outline">--%>
<%--                            <span aria-hidden="true" style="font-size:20px">+</span>--%>
<%--                        </button>--%>
<%--                    </div>--%>

                </div>
            </div>
            <br>
            <br>

            <btcl-bounded>
                <div class="col-md-6" style="text-align: left">
                    <label>Old Port Type:</label>
                    <label>{{application.oldPortInfo.value}}</label>
                </div>
                <div class="col-md-6" style="text-align: left">
                    <label>New Port Type:</label>
                    <label>{{application.portTypeString}}</label>
                </div>
            </btcl-bounded>


            <div class="modal-footer">
                <div class="form-group">
                    <div class="col-md-12">
                        <input type="text" v-model="comment" placeholder="Comment(*)"
                               class="form-control input-lg">
                    </div>
                </div>
                <br><br>
                <button type="button" class="btn btn-success" @click="serverRoomNextStepUpgrade">Submit</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>