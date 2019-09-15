<%--done--%>

<btcl-body v-if="efrRequested" title="Feasibility Report">

    <btcl-portlet title="EFR Responses">
        <div class="table-responsive">
            <table class="table table-fit table-bordered" style="width: 100%;">
                <thead>
                <tr>
                    <th>Office</th>
                    <th>From</th>
                    <th>To</th>
                    <th>Source Type</th>
                    <th>Destination Type</th>
                    <th>Deadline</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="element in application.incompleteEFR">
                    <td>{{getOfficeWithID(element.officeID)}}</td>
                    <td>{{getPOPwithType(element.source,element.sourceType)}}</td>

                    <td>{{getPOPwithType(element.destination,element.destinationType)}}</td>
                    <td>
                        <select disabled v-model="element.sourceType"
                                style="margin-top: 7px;">
                            <option value="1">BTCL POP</option>
                            <option value="2">BTCL LDP</option>
                            <option value="4">BTCL MUX</option>
                            <option value="3">Customer End</option>
                        </select>
                    </td>


                    <td>
                        <select disabled v-model="element.destinationType"
                                style="margin-top: 7px;">
                            <option value="1">BTCL POP</option>
                            <option value="2">BTCL LDP</option>
                            <option value="4">BTCL MUX</option>
                            <option value="3">Customer End</option>
                        </select>
                    </td>
                    <td>{{qDeadLine(element)}}</td>
                    <td align="center">

                        <%-- <p :value="element.quotationStatus=1"></p>  --%> </td>


                </tr>
                </tbody>
            </table>
        </div>
        File Upload
        <btcl-file-upload :params="{
                                'moduleId': 7,
                                'applicationId': application.applicationID,
                                'componentId':application.applicationType.ID,
                                'stateId':application.state,
                                'fileUploadURL': fileUploadURL
                            }">
        </btcl-file-upload>
    </btcl-portlet>

</btcl-body>

<%--EFR RESPONDED--%>
<%--<div v-if="application.action.length>0">--%>
<%--done--%>
<btcl-body v-if="vendorResponseAndDemandNote" title="Feasibility Report">
    <btcl-portlet title="EFR Responses">
        <div class="table-responsive">
            <table class="table table-fit table-bordered" style="width: 100%;">
                <thead>
                <tr>
                    <th>Vendor Name</th>
                    <th>From</th>
                    <th>To</th>
                    <th>Length(Meter)</th>
                    <th>OFC Type</th>
                    <th>Response</th>
                    <th>Select</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="element in application.completeEFR">
                    <td>{{element.vendorName}}</td>
                    <td>{{element.source}}</td>

                    <td>{{element.destination}}</td>
                    <td>{{element.proposedLoopDistance}}</td>
                    <td>
                        <select disabled v-model="element.ofcType"
                                style="margin-top: 7px;">
                            <option value="1">Single</option>
                            <option value="2">Double</option>
                        </select>
                    </td>
                    <td align="center">
                        <button class="btn-success" align="center"
                                style="width:100%;margin-top:5%;background-color:green!important;"
                                v-if="element.quotationStatus==1">Responded
                        </button>
                        <button style="width:100%;margin-top:5%; background-color:darkred!important;" class="btn-danger"
                                v-if="element.quotationStatus==2">Expired
                        </button>
                        <button style="width:100%;margin-top:5%;background-color:darkorange!important;"
                                class="btn-warning" v-if="element.quotationStatus==0">Waiting
                        </button>
                        <button style="width:100%;margin-top:5%; background-color:darkred!important;" class="btn-danger"
                                v-if="element.quotationStatus==3">Ignored
                        </button>

                    </td>
                    <td  align="center">
                        <input v-if="element.workGiven==1 || element.quotationStatus !=1 ||application.state ==STATE_FACTORY.ONLY_LOOP_FORWARDED_LDGM_FORWARD_CDGM"
                               disabled
                               style="margin-top:18%;"
                               type="checkbox"
                               :true-value="1"
                               :false-value="0" v-model="element.workGiven">
                        <%--<input v-else-if="element.workGiven!=1 "--%>
                               <%--disabled--%>
                               <%--style="margin-top:18%;"--%>
                               <%--type="checkbox"--%>
                               <%--:true-value="1"--%>
                               <%--:false-value="0"--%>
                               <%--v-model="element.workGiven">--%>
                        <input v-else
                               style="margin-top:18%;"
                               type="checkbox"
                               :true-value="1"
                               :false-value="0"
                               v-model="element.workGiven">
                    </td>
                    <td align="center">

                        <%--<p :value="element.quotationStatus=1"></p>--%> </td>

                </tr>
                </tbody>
            </table>
        </div>
    </btcl-portlet>
</btcl-body>


<%--ldgm/cdgm work order(completedEFR)--%>
<%--check--%>
<btcl-body title="Work Order Details" v-if="workOrderGenerate">
    <btcl-portlet>
        <div class="form-body">
            <div class="table-responsive">
                <table class="table table-fit table-bordered" style="width:100%">

                    <thead>
                    <tr>
                        <th>From</th>
                        <th>To</th>
                        <th>Length(Meter)</th>
                        <th>OFC Type</th>

                        <th>Deadlines</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="element in application.completeEFR">

                        <td>{{element.source}}</td>
                        <td>{{element.destination}}</td>
                        <td>{{element.proposedLoopDistance}}</td>
                        <td>
                            <select disabled v-model="element.ofcType"
                                    style="margin-top: 7px;">
                                <option value="1">Single</option>
                                <option value="2">Double</option>
                            </select>
                        </td>
                        <td align="center">
                            {{qDeadLine(element)}}
                        </td>
                        <td align="center">
                            <%--<p v-bind:value="element.quotationStatus=1"></p>--%>
                        </td>


                    </tr>
                    </tbody>
                </table>
            </div>
            File Upload
            <btcl-file-upload :params="{
                                'moduleId': 7,
                                'applicationId': application.applicationID,
                                'componentId':application.applicationType.ID,
                                'stateId':application.state,
                                'fileUploadURL': fileUploadURL
                            }">
            </btcl-file-upload>

        </div>
    </btcl-portlet>

</btcl-body>

<btcl-body title="Approve Loop Distances" v-if="workOrderComplete">
    <btcl-portlet>
        <div class="table-responsive">
            <table class="table table-fit table-bordered" style="width: 100%;">
                <thead>
                <tr>
                    <th>From</th>
                    <th>To</th>
                    <th>Vendor Name</th>
                    <th>Length(Meter)</th>
                    <th>OFC Type</th>
                    <th>Deadlines</th>
                    <th>Actual Length(Meter)</th>
                    <th>Approve</th>
                    <th></th>
                </tr>
                </thead>

                <tbody>
                <tr v-for="element in application.completeEFR">

                    <template v-if="element.quotationStatus==1 &&element.workGiven==1 && element.workCompleted==1">

                        <td>{{element.source}}</td>

                        <td>{{element.destination}}</td>
                        <td>{{element.vendorName}}</td>

                        <td>{{element.proposedLoopDistance}}</td>
                        <td>
                            <select disabled v-model="element.ofcType"
                                    style="margin-top: 7px;">
                                <option v-bind:value="1">Single</option>
                                <option v-bind:value="2">Double</option>
                            </select>
                        </td>

                        <td align="center">
                            {{qDeadLine(element)}}
                        </td>
                        <td class="col-md-1" align="center">
                            {{element.actualLoopDistance}}
                        </td>
                        <td align="center">
                            <input style="margin-top:18%;" type="checkbox" :true-value="1" v-bind:false-value="0"
                                   v-model="element.loopDistanceIsApproved">
                        </td>

                        <td><%--<p v-bind:value="element.quotationStatus=1"></p>--%></td>
                    </template>

                </tr>
                </tbody>
            </table>
        </div>
    </btcl-portlet>

</btcl-body>