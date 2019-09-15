<btcl-body
        v-if="(link.state.view=='efr-details-info' || detailsPageLoading == true)&& checkLinkEFR(link)"
<%--v-if="vendorResponseAndDemandNote"--%>
        title="Feasibility Report"
>
    <btcl-portlet  title="Approve Vendor Collaboration">
        <div class="form-body">
            <div class="form-group"
                 style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                <div class="col-md-1"><p style="text-align: center;">Office Type</p></div>
                <div class="col-md-2"><p style="text-align: center;">Vendor Name</p></div>
                <div class="col-md-2"><p style="text-align: center;">From</p></div>
                <div class="col-md-2"><p style="text-align: center;">To</p></div>
                <div class="col-md-1"><p style="text-align: center;">Proposed Length</p></div>
                <div class="col-md-1"><p style="text-align: center;">Actual Length</p></div>
                <div class="col-md-2"><p style="text-align: center;">Contact</p></div>
                <div v-if="detailsPageLoading == false" class="col-md-1"><p style="text-align: center;">Response</p></div>

            </div>
            <%--local office--%>
            <template v-if="link.hasOwnProperty('localOffice')">
                <div class="form-group" v-for="element in link.localOffice.efrs">
                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            Local Office </p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.vendorName}}</p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.source}}</p>
                    </div>

                    <div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">
                        {{element.destination}}</p></div>

                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.proposedLoopDistance}} m</p>
                    </div>
                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.actualLoopDistance}} m</p>
                    </div>
                    <div class="col-md-2" align="center">
                        <%--<input v-if="element.isSelected==true" disabled style="margin-top:18%;" type="checkbox" :true-value="1" :false-value="0" v-model="element.workGiven">--%>
                        <%--<input--%>
                        <%--&lt;%&ndash;v-else &ndash;%&gt;--%>
                                <%--style="margin-top:18%;" type="checkbox"--%>
                        <%--&lt;%&ndash;:true-value="1" :false-value="0"&ndash;%&gt;--%>
                                <%--v-model="element.loopDistanceIsApproved">--%>
                            <p style="text-align: center;border: 1px solid;padding: 10px;">
                                {{element.contact}}</p>
                    </div>
                    <div v-if="detailsPageLoading == false" class="col-md-1">
                        <input
                                <%--v-if="element.isSelected==true" disabled--%>
                                style="margin-top:18%;" type="checkbox"
                                <%--:true-value="1" :false-value="0"--%>
                                v-model="element.isCollaborationApproved">
                        <%--<input--%>
                        <%--&lt;%&ndash;v-else &ndash;%&gt;--%>
                        <%--style="margin-top:18%;" type="checkbox"--%>
                        <%--&lt;%&ndash;:true-value="1" :false-value="0"&ndash;%&gt;--%>
                        <%--v-model="element.loopDistanceIsApproved">--%>
                    </div>
                    <p :value="element.quotationStatus=1"></p>
                </div>
            </template>

            <%--remote office--%>
            <template v-if="link.hasOwnProperty('remoteOffice')">
                <div class="form-group" v-for="element in link.remoteOffice.efrs">
                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            Remote Office </p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.vendorName}}</p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.source}}</p>
                    </div>

                    <div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">
                        {{element.destination}}</p></div>

                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.proposedLoopDistance}} m</p>
                    </div>
                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.actualLoopDistance}} m</p>
                    </div>

                    <div class="col-md-2" align="center">
                        <%--&lt;%&ndash;<input v-if="element.isSelected==true" disabled style="margin-top:18%;" type="checkbox" :true-value="1" :false-value="0" v-model="element.workGiven">&ndash;%&gt;--%>
                        <%--<input--%>
                        <%--&lt;%&ndash;v-else &ndash;%&gt;--%>
                                <%--style="margin-top:18%;" type="checkbox"--%>
                        <%--&lt;%&ndash;:true-value="1" :false-value="0"&ndash;%&gt;--%>
                                <%--v-model="element.loopDistanceIsApproved">--%>
                            <p style="text-align: center;border: 1px solid;padding: 10px;">
                                {{element.contact}}</p>
                    </div>
                    <div v-if="detailsPageLoading == false" class="col-md-1">
                        <input
                                <%--v-if="element.isSelected==true" disabled--%>
                               style="margin-top:18%;" type="checkbox"
                               <%--:true-value="1" :false-value="0"--%>
                               v-model="element.isCollaborationApproved">
                        <%--<input--%>
                        <%--&lt;%&ndash;v-else &ndash;%&gt;--%>
                        <%--style="margin-top:18%;" type="checkbox"--%>
                        <%--&lt;%&ndash;:true-value="1" :false-value="0"&ndash;%&gt;--%>
                        <%--v-model="element.loopDistanceIsApproved">--%>
                    </div>
                    <p :value="element.quotationStatus=1"></p>
                </div>
            </template>

        </div>
    </btcl-portlet>
</btcl-body>

<btcl-body
        v-if="link.state.view=='view-loop-distance-approve'"
<%--v-if="vendorResponseAndDemandNote"--%>
        title="Feasibility Report"
>
    <btcl-portlet title="Approve Loop distance">
        <div class="form-body">
            <div class="form-group"
                 style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                <div class="col-md-1"><p style="text-align: center;">Office Type</p></div>
                <div class="col-md-1"><p style="text-align: center;">Vendor Name</p></div>
                <div class="col-md-2"><p style="text-align: center;">From</p></div>
                <div class="col-md-2"><p style="text-align: center;">To</p></div>
                <div class="col-md-2"><p style="text-align: center;">Proposed Length</p></div>
                <div class="col-md-2"><p style="text-align: center;">Actual Length</p></div>
                <div class="col-md-2"><p style="text-align: center;">Response</p></div>
                <%--<div class="col-md-2"><p style="text-align: center;">Contact</p></div>--%>
            </div>
            <%--local office--%>
            <template v-if="link.hasOwnProperty('localOffice')">
                <div class="form-group" v-for="element in link.localOffice.efrs">
                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            Local Office </p>
                    </div>
                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.vendorName}}</p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.source}}</p>
                    </div>

                    <div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">
                        {{element.destination}}</p></div>

                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.proposedLoopDistance}}</p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.actualLoopDistance}}</p>
                    </div>
                    <div class="col-md-2" align="center">
                        <%--<input v-if="element.isSelected==true" disabled style="margin-top:18%;" type="checkbox" :true-value="1" :false-value="0" v-model="element.workGiven">--%>
                        <input
                        <%--v-else --%>
                        style="margin-top:18%;" type="checkbox"
                        <%--:true-value="1" :false-value="0"--%>
                        v-model="element.loopDistanceIsApproved">
                        <%--<p style="text-align: center;border: 1px solid;padding: 10px;">{{element.contact}}</p>--%>
                    </div>
                    <p :value="element.quotationStatus=1"></p>
                </div>
            </template>

            <%--remote office--%>
            <template v-if="link.hasOwnProperty('remoteOffice')">
                <div class="form-group" v-for="element in link.remoteOffice.efrs">
                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            Remote Office </p>
                    </div>
                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.vendorName}}</p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.source}}</p>
                    </div>

                    <div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">
                        {{element.destination}}</p></div>

                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.proposedLoopDistance}}</p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.actualLoopDistance}}</p>
                    </div>

                    <div class="col-md-2" align="center">
                        <%--<input v-if="element.isSelected==true" disabled style="margin-top:18%;" type="checkbox" :true-value="1" :false-value="0" v-model="element.workGiven">--%>
                        <input
                        <%--v-else --%>
                        style="margin-top:18%;" type="checkbox"
                        <%--:true-value="1" :false-value="0"--%>
                        v-model="element.loopDistanceIsApproved">
                        <%--<p style="text-align: center;border: 1px solid;padding: 10px;">{{element.contact}}</p>--%>
                    </div>
                    <p :value="element.quotationStatus=1"></p>
                </div>
            </template>

        </div>
    </btcl-portlet>
</btcl-body>