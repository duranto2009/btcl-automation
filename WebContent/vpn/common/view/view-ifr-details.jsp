<btcl-body
        v-if="link.state.view=='IFR_DETAILS_VIEW'"
<%--v-if="vendorResponseAndDemandNote"--%>
        title="Feasibility Report"
>
    <btcl-portlet title="IFR Feasibility">
        <div class="form-body">
            <div class="form-group"
                 style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                <div class="col-md-4"><p style="text-align: center;">Office Name</p></div>
                <div class="col-md-4"><p style="text-align: center;">POP Name</p></div>
                <div class="col-md-4"><p style="text-align: center;">Feasibility</p></div>
                <%--<div class="col-md-2"><p style="text-align: center;">To</p></div>--%>
                <%--<div class="col-md-1"><p style="text-align: center;">Length</p></div>--%>
                <%--<div class="col-md-2"><p style="text-align: center;">OFC Type</p></div>--%>
                <%--<div class="col-md-2"><p style="text-align: center;">Response</p></div>--%>
                <%--<div class="col-md-1"><p style="text-align: center;">Select</p></div>--%>
            </div>
            <%--local office--%>
            <template v-if="link.hasOwnProperty('localOffice')">
                <div class="form-group" v-for="element in link.localOffice.localLoops">
                    <div class="col-md-4">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">{{link.localOffice.officeName}}</p>
                    </div>
                    <div class="col-md-4">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.popName}}</p>
                    </div>
                    <div class="col-md-4">
                        <p style="text-align: center;border: 1px solid;padding: 10px;" v-if="element.ifrFeasibility">Possible</p>
                        <p style="text-align: center;border: 1px solid;padding: 10px;" v-else >Not Possible</p>
                    </div>

                    <%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">--%>
                        <%--{{element.destinationType}}(name)</p></div>--%>

                    <%--<div class="col-md-1">--%>
                        <%--<p style="text-align: center;border: 1px solid;padding: 10px;">--%>
                            <%--{{element.proposedLoopDistance}}</p>--%>
                    <%--</div>--%>
                    <%--<div class="col-md-2">--%>
                        <%--<select disabled class="form-control" v-model="element.ofcType"--%>
                                <%--style="margin-top: 7px;">--%>
                            <%--<option value="1">Single</option>--%>
                            <%--<option value="2">Double</option>--%>
                        <%--</select>--%>
                    <%--</div>--%>
                    <%--<div class="col-md-2" align="center">--%>
                        <%--<button class="btn-success" align="center"--%>
                                <%--style="width:100%;margin-top:5%;background-color:green!important;"--%>
                                <%--v-if="element.isReplied==true">Responded--%>
                        <%--</button>--%>
                        <%--&lt;%&ndash;<button class="btn-danger" v-if="element.quotationStatus==2">Expired</button>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<button class="btn-warning" v-if="element.quotationStatus==0">Waiting</button>&ndash;%&gt;--%>
                    <%--</div>--%>
                    <%--<div class="col-md-1" align="center">--%>
                        <%--&lt;%&ndash;<input v-if="element.isSelected==true" disabled style="margin-top:18%;" type="checkbox" :true-value="1" :false-value="0" v-model="element.workGiven">&ndash;%&gt;--%>
                        <%--<input--%>
                        <%--&lt;%&ndash;v-else &ndash;%&gt;--%>
                                <%--style="margin-top:18%;" type="checkbox"--%>
                        <%--&lt;%&ndash;:true-value="1" :false-value="0"&ndash;%&gt;--%>
                                <%--v-model="element.isSelected">--%>
                    <%--</div>--%>
                    <%--<p :value="element.quotationStatus=1"></p>--%>
                </div>
            </template>

            <%--remote office--%>
            <template v-if="link.hasOwnProperty('remoteOffice')">
                <div class="form-group" v-for="element in link.remoteOffice.localLoops">
                    <div class="col-md-4">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">{{link.remoteOffice.officeName}}</p>
                    </div>
                    <div class="col-md-4">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.popName}}</p>
                    </div>
                    <div class="col-md-4">
                        <p style="text-align: center;border: 1px solid;padding: 10px;" v-if="element.ifrFeasibility">Possible</p>
                        <p style="text-align: center;border: 1px solid;padding: 10px;" v-else >Not Possible</p>
                    </div>

                    <%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">--%>
                    <%--{{element.destinationType}}(name)</p></div>--%>

                    <%--<div class="col-md-1">--%>
                    <%--<p style="text-align: center;border: 1px solid;padding: 10px;">--%>
                    <%--{{element.proposedLoopDistance}}</p>--%>
                    <%--</div>--%>
                    <%--<div class="col-md-2">--%>
                    <%--<select disabled class="form-control" v-model="element.ofcType"--%>
                    <%--style="margin-top: 7px;">--%>
                    <%--<option value="1">Single</option>--%>
                    <%--<option value="2">Double</option>--%>
                    <%--</select>--%>
                    <%--</div>--%>
                    <%--<div class="col-md-2" align="center">--%>
                    <%--<button class="btn-success" align="center"--%>
                    <%--style="width:100%;margin-top:5%;background-color:green!important;"--%>
                    <%--v-if="element.isReplied==true">Responded--%>
                    <%--</button>--%>
                    <%--&lt;%&ndash;<button class="btn-danger" v-if="element.quotationStatus==2">Expired</button>&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<button class="btn-warning" v-if="element.quotationStatus==0">Waiting</button>&ndash;%&gt;--%>
                    <%--</div>--%>
                    <%--<div class="col-md-1" align="center">--%>
                    <%--&lt;%&ndash;<input v-if="element.isSelected==true" disabled style="margin-top:18%;" type="checkbox" :true-value="1" :false-value="0" v-model="element.workGiven">&ndash;%&gt;--%>
                    <%--<input--%>
                    <%--&lt;%&ndash;v-else &ndash;%&gt;--%>
                    <%--style="margin-top:18%;" type="checkbox"--%>
                    <%--&lt;%&ndash;:true-value="1" :false-value="0"&ndash;%&gt;--%>
                    <%--v-model="element.isSelected">--%>
                    <%--</div>--%>
                    <%--<p :value="element.quotationStatus=1"></p>--%>
                </div>
            </template>

        </div>
    </btcl-portlet>
</btcl-body>
