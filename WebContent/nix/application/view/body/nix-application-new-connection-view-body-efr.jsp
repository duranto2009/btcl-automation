<%--done--%>

<btcl-body v-if="efrRequested" title="Feasibility Report">
    <btcl-portlet title="EFR Responses">
        <div class="form-body">
            <div class="form-group"
                 style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">

                <div class="col-md-3"><p style="text-align: center;">From</p>
                </div>
                <div class="col-md-3"><p style="text-align: center;">To</p>
                </div>
                <div class="col-md-2"><p style="text-align: center;">Source</p></div>
                <div class="col-md-2"><p style="text-align: center;">Destination</p></div>
            </div>
            <div class="form-group"
                 v-for="element in application.incompleteEFR">
                <div class="col-md-3">
                    <p style="text-align: center;border: 1px solid;padding: 10px;">
                        {{getPOPwithType(element.source,element.sourceType)}}</p>
                </div>

                <div class="col-md-3"><p style="text-align: center;border: 1px solid;padding: 10px;">
                    {{getPOPwithType(element.destination,element.destinationType)}}</p></div>
                <div class="col-md-2">
                    <select disabled class="form-control" v-model="element.sourceType"
                            style="margin-top: 7px;">
                        <option value="1">BTCL POP</option>
                        <option value="2">BTCL LDP</option>
                        <option value="4">BTCL MUX</option>
                        <option value="3">Customer End</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select disabled class="form-control" v-model="element.destinationType"
                            style="margin-top: 7px;">
                        <option value="1">BTCL POP</option>
                        <option value="2">BTCL LDP</option>
                        <option value="4">BTCL MUX</option>
                        <option value="3">Customer End</option>
                    </select>
                </div>
            </div>
            <br>

        </div>
    </btcl-portlet>

</btcl-body>

<%--EFR RESPONDED--%>
<%--<div v-if="application.action.length>0">--%>
<%--done--%>
<btcl-body v-if="vendorResponseAndDemandNote" title="Feasibility Report">
    <btcl-portlet title="EFR Responses">
        <div class="table-responsive">
            <table class="table table-fit table-bordered">
                <thead>
                <tr
                        style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                    <th><p style="text-align: center;">Vendor Name</p></th>
                    <th><p style="text-align: center;">From</p></th>
                    <th><p style="text-align: center;">To</p></th>
                    <th><p style="text-align: center;">Length</p></th>
                    <th><p style="text-align: center;">OFC Type</p></th>
                    <th><p style="text-align: center;">Response</p></th>
                    <th><p style="text-align: center;">Select</p></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="element in application.completeEFR">
                    <td>
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.vendorName}}</p>
                    </td>
                    <td>
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.source}}
                            <label v-if="element.sourceType==2"> (BTCL LDP)</label>
                            <label v-if="element.sourceType==1"> (BTCL POP)</label>
                            <label v-if="element.sourceType==3"> (Customer Address)</label>

                        </p>
                    </td>

                    <td><p style="text-align: center;border: 1px solid;padding: 10px;">

                        {{element.destination}}
                        <label v-if="element.destinationType==2"> (BTCL LDP)</label>
                        <label v-if="element.destinationType==1"> (BTCL POP)</label>
                        <label v-if="element.destinationType==3"> (Customer Address)</label>

                    </p></td>

                    <td>
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.proposedDistance}} m</p>
                    </td>
                    <td>
                        <select disabled class="form-control" v-model="element.ofcType"
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
                        <button class="btn-danger" style="width:100%;margin-top:5%;background-color:red!important;"
                                v-if="element.quotationStatus==2">Expired
                        </button>
                        <button class="btn-danger" style="width:100%;margin-top:5%;background-color:red!important;"
                                v-if="element.quotationStatus==3">Not Possible
                        </button>
                        <button class="btn-warning" style="width:100%;margin-top:5%;background-color:yellow!important;"
                                v-if="element.quotationStatus==0">Waiting
                        </button>
                    </td>
                    <td align="center">

                        <input v-if="element.workGiven==1 || element.quotationStatus !=1 ||application.state ==STATE_FACTORY.EFR_RESPONSE_FORWARD_TO_CDGM"
                               disabled
                               style="margin-top:18%;"
                               type="checkbox"
                               :true-value="1"
                               :false-value="0" v-model="element.workGiven">
                        <input v-else
                               style="margin-top:18%;"
                               type="checkbox"
                               :true-value="1"
                               :false-value="0"
                               v-model="element.workGiven">
                    </td>
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
        <div class="table-responsive">
            <table class="table table-fit table-bordered"
                   style=" margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">

                <thead>
                <tr>

                    <th><p style="text-align: center;">From</p></th>
                    <th><p style="text-align: center;">To</p></th>
                    <th><p style="text-align: center;">Length</p></th>
                    <th><p style="text-align: center;">OFC Type</p></th>
                </tr>
                </thead>


                <tbody>

                <tr v-if="element.workGiven==1" v-for="element in application.completeEFR">

                    <td>
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.source}}
                            <label v-if="element.sourceType==2"> (BTCL LDP)</label>
                            <label v-if="element.sourceType==1"> (BTCL POP)</label>
                            <label v-if="element.sourceType==3"> (Customer Address)</label>
                        </p>
                    </td>

                    <td><p style="text-align: center;border: 1px solid;padding: 10px;">
                        {{element.destination}}
                        <label v-if="element.destinationType==2"> (BTCL LDP)</label>
                        <label v-if="element.destinationType==1"> (BTCL POP)</label>
                        <label v-if="element.destinationType==3"> (Customer Address)</label>
                    </p></td>

                    <td>
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.proposedDistance}} m</p>
                    </td>
                    <td>
                        <select disabled class="form-control" v-model="element.ofcType"
                                style="margin-top: 7px;">
                            <option value="1">Single</option>
                            <option value="2">Double</option>
                        </select>
                    </td>

                </tr>
                </tbody>
            </table>

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

<btcl-body title="Work Order Completed" v-if="workOrderComplete">
    <btcl-portlet>
        <div class="table-responsive">
            <table class="table table-fit table-bordered"
            <%--style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px"--%>>

                <thead>
                <tr>
                    <th ><p style="text-align: center;">From</p></th>
                    <th><p style="text-align: center;">To</p></th>
                    <th ><p style="text-align: center;">Length</p></th>
                    <th ><p style="text-align: center;">OFC Type</p></th>
                    <th ><p style="text-align: center;">Actual Length(m)</p></th>
                    <th ><p style="text-align: center;">Approve?</p></th>
                    <%--<div class="col-md-1"><p style="text-align: center;">Approve</p></div>--%>

                </tr>
                </thead>
                <tbody>


                <tr v-for="element in application.completeEFR">
                    <template v-if="element.workCompleted==1">
                        <td >
                            <p style="text-align: center;border: 1px solid;padding: 10px;">
                                {{element.source}}</p>
                        </td>

                        <td><p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.destination}}</p></td>

                        <td >
                            <p style="text-align: center;border: 1px solid;padding: 10px;">
                                {{element.proposedDistance}}</p>
                        </td>
                        <td >
                            <select disabled class="form-control" v-model="element.ofcType"
                                    style="margin-top: 7px;">
                                <option v-bind:value="1">Single</option>
                                <option v-bind:value="2">Double</option>
                            </select>
                        </td>
                        <td align="center">
                            <p style="text-align: center;border: 1px solid;padding: 10px;">
                                {{element.actualDistance}}</p>
                        </td>
                        <td align="center">

                            <input v-if="element.workGiven==1
                             && element.workCompleted==1
                             &&element.quotationStatus ==1
                             && element.approvedDistance==1
                             && application.state==STATE_FACTORY.WO_RESPONSE_FORWARD_TO_CDGM"
                                   disabled
                                   style="margin-top:18%;"
                                   type="checkbox"
                                   :true-value="1"
                                   :false-value="0" v-model="element.approvedDistance">


                            <input v-else style="margin-top:18%;" type="checkbox"
                                   :true-value="1"
                                   :false-value="0"
                                   v-model="element.approvedDistance">
                        </td>


                    </template>
                </tr>
                </tbody>
            </table>

        </div>
    </btcl-portlet>

</btcl-body>