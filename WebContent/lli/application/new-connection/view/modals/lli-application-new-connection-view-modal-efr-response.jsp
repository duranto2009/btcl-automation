<div class="modal fade" id="vendormodal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 85%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <%--!vendor!--%>
                <div class="portlet-title">
                    <div class="caption" align="center"
                         style="background: #fab74d;padding: 10px;">EFR Response
                    </div>
                    <div class="tools">
                        <a href="javascript:void(0);" data-original-title="" title=""
                           class="collapse"></a>
                    </div>
                </div>
                    <%--
            <thead>--%>
                <div class="table-responsive">
                    <table class="table table-fit table-bordered" style="width: 100%;">
                        <thead>
                            <tr>
                                <th>From</th>
                                <th>To</th>
                                <th>Length(Meter)</th>
                                <th>OFC Type</th>
                                <th>Quotation Status</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="element in application.incompleteEFR">
                                <td>{{element.source}}</td>
                                <td>{{element.destination}}</td>
                                <td>
                                    <input type="number"
                                           <%--style="width: 100%;padding: 2%;padding-top: 3%;margin-top: 1%;"--%>
                                           v-model="element.proposedLoopDistance"
                                           placeholder="Enter Length">
                                </td>
                                <td>
                                    <select disabled v-model="element.ofcType"
                                            class="form-control"
                                            style="margin-top: 7px;">
                                        <option value="1">Single</option>
                                        <option value="2">Double</option>
                                    </select>
                                </td>
                                <td>
                                    <select v-model="element.quotationStatus"
                                            class="form-control"
                                            style="margin-top: 7px;">
                                        <option value="1">Possible</option>
                                        <option value="3">Not Possible</option>
                                    </select>
                                </td>
<%--
                                <td><p v-bind:value="element.quotationStatus=1"></p></td>
--%>
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
                <button type="button" class="btn btn-success" @click="vendorNextStep">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>
    </div>
</div>