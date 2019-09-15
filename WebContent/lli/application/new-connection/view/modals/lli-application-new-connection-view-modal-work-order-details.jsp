<div class="modal fade" id="workOrderDetails" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 85%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <%--!vendor!--%>
                <div class="portlet-title">
                    <div class="caption" align="center"
                         style="background: #fab74d;padding: 10px;">Work Order Details
                    </div>
                    <div class="tools">
                        <a href="javascript:;" data-original-title="" title=""
                           class="collapse"></a>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-fit table-bordered" style="width: 100%;">
                            <thead>
                                <tr>
                                    <th>From</th>
                                    <th>To</th>
                                    <th>Length(Meter)</th>
                                    <th>OFC Type</th>

                                    <th v-if="application.applicationType.ID!=7">Bandwidth(Mbps)</th>
                                    <th>Deadlines</th>
                                    <th>Actual Length(Meter)</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="element in application.completeEFR">

                                    <td>{{element.source}}</td>

                                    <td>{{element.destination}}</td>

                                    <td>
                                            {{element.proposedLoopDistance}}
                                    </td>
                                    <td>
                                        <select disabled v-model="element.ofcType"
                                                style="margin-top: 7px;">
                                            <option v-bind:value="1">Single</option>
                                            <option v-bind:value="2">Double</option>
                                        </select>
                                    </td>

                                    <td v-if="application.applicationType.ID!=7" align="center">
                                            {{element.bandwidth}}Mbps
                                    </td>
                                    <td class="col-md-2" align="center">
                                             {{qDeadLine(element)}}
                                    </td>
                                    <td  align="center">
                                             <input style="text-align: center;border: 1px solid;padding: 10px; margin-top:2%" type="number" v-model.number="element.actualLoopDistance"
                                                   placeholder="Give Actual Length"
                                                   class="form-control input-lg">
                                    </td>
                                    <td  align="center">

                                        <%--<p v-bind:value="element.quotationStatus=1"></p>--%></td>

                                </tr>
                            </tbody>
                        </table>
                </div>
            </div>
            <div class="modal-footer">
                <div class="form-group">
                    <%--<label class="control-label col-md-3" style="text-align: center;">Comment</label>--%>
                    <div class="col-md-12"><input type="text" v-model="comment"
                                                  placeholder="Comment Here"
                                                  class="form-control input-lg">
                    </div>
                </div>
                <br><br>
                <button type="button" class="btn btn-success" @click="workOrderJobDoneNextState(application.completeEFR)">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>
    </div>
</div>