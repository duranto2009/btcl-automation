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
                    <table class="table table-fit table-bordered"
                           style=";margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                        <thead>
                        <tr>
                            <th><p style="text-align: center;">From</p></th>
                            <th><p style="text-align: center;">To</p></th>
                            <th><p style="text-align: center;">Length</p></th>
                            <th><p style="text-align: center;">OFC Type</p></th>
                            <th><p style="text-align: center;">Actual Length(m)</p></th>
                        </tr>
                        </thead>


                        <tbody>
                        <tr v-if="element.workGiven==1" v-for="element in application.completeEFR">

                            <td >
                                <p style="text-align: center;border: 1px solid;padding: 10px;">
                                    {{element.source}}
                                    <label v-if="element.sourceType==2"> (BTCL LDP)</label>
                                    <label v-if="element.sourceType==1"> (BTCL POP)</label>
                                    <label v-if="element.sourceType==3"> (Customer Address)</label>
                                </p>
                            </td>

                            <td ><p style="text-align: center;border: 1px solid;padding: 10px;">
                                {{element.destination}}
                                <label v-if="element.destinationType==2"> (BTCL LDP)</label>
                                <label v-if="element.destinationType==1"> (BTCL POP)</label>
                                <label v-if="element.destinationType==3"> (Customer Address)</label>
                            </p></td>

                            <td >
                                <%--<input readonly type="number" style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;" v-model="element.proposedLoopDistance">--%>
                                <p style="text-align: center;border: 1px solid;padding: 10px;">
                                    {{element.proposedDistance}} m</p>
                            </td>
                            <td>
                                <select disabled class="form-control" v-model="element.ofcType"
                                        style="margin-top: 7px;">
                                    <option v-bind:value="1">Single</option>
                                    <option v-bind:value="2">Double</option>
                                </select>
                            </td>


                            <td  align="center">
                                <input style="text-align: center;border: 1px solid;padding: 10px; margin-top:2%"
                                       type="number" v-model.number="element.actualDistance"
                                       placeholder="Give Actual Length"
                                       class="form-control input-lg">
                            </td>


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
                <button type="button" class="btn btn-success"
                        @click="workOrderJobDoneNextState(application.completeEFR)">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>
    </div>
</div>