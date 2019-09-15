<div class="modal fade" id="vendormodal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 85%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <%--!vendor!--%>
                <div class="portlet-title">
                    <div class="caption"
                         align="center"
                         style="background: #fab74d;padding: 10px;">EFR Response
                    </div>
                    <div class="tools">
                        <a href="javascript:;"
                           data-original-title=""
                           title=""
                           class="collapse">
                        </a>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-fit table-bordered" style="width: 100%;">

                        <thead>
                        <tr>
                            <th>From</th>
                            <th>To</th>
                            <th>OFC Type</th>
                            <th>Quotation Status</th>
                            <th>Length(Meter)</th>
                            <th></th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr v-for="element in application.incompleteEFR">
                            <td>
                                <p style="text-align: center;border: 1px solid;padding: 10px;">
                                    {{element.source}}
                                    <label v-if="element.sourceType==2"> (BTCL LDP)</label>
                                    <label v-if="element.sourceType==1"> (BTCL POP)</label>
                                    <label v-if="element.sourceType==3"> (Customer Address)</label>
                                </p>
                            </td>
                            <td>
                                <p style="text-align: center;border: 1px solid;padding: 10px;">
                                    {{element.destination}}
                                    <label v-if="element.destinationType==2"> (BTCL LDP)</label>
                                    <label v-if="element.destinationType==1"> (BTCL POP)</label>
                                    <label v-if="element.destinationType==3"> (Customer Address)</label>
                                </p>
                            </td>
                            <td>
                                <select disabled class="form-control"
                                        v-model="element.ofcType"
                                        style="margin-top: 7px;">
                                    <option value="0">Select OFC Type
                                    </option>
                                    <option value="1">Single</option>
                                    <option value="2">Double</option>
                                </select>
                            </td>
                            <td >
                                <select v-model="element.quotationStatus"
                                        class="form-control"
                                        style="margin-top: 7px;">
                                    <option value="1">Possible</option>
                                    <option value="3">Not Possible</option>
                                </select>
                            </td>

                            <td>
                                <input type="number"
                                       style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"
                                       v-model="element.proposedDistance"
                                       placeholder="Enter Length">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <div class="form-group">
                    <div class="col-md-12">
                        <input type="text"
                               v-model="comment"
                               placeholder="Comment Here"
                               class="form-control input-lg">
                    </div>
                </div>
                <br><br>
                <button type="button"
                        class="btn btn-success"
                        @click="vendorNextStep">
                    Submit
                </button>
                <button type="button"
                        class="btn btn-danger"
                        data-dismiss="modal">Close
                </button>
            </div>
        </div>
    </div>
</div>


