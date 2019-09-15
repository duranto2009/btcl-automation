<div class="modal fade" id="paymentVerifiedModal" role="dialog">
    <div class="modal-dialog modal-lg">

        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <%--!vendor!--%>
                <div>
                    <div class="portlet-title">
                        <div class="tools">
                            <a href="javascript:;" data-original-title="" title=""
                               class="collapse"></a>
                        </div>
                    </div>


                    <div class="form-body">
                        <div class="form-group"><label class="control-label col-md-3"
                                                       style="text-align: center;">
                            Comment

                        </label>
                            <div class="col-md-9"><input type="text" v-model="comment"
                                                         placeholder="Comment Here"
                                                         class="form-control input-lg">
                            </div>
                        </div>
                    </div>
                </div>


            </div>
            <div class="modal-footer">
                <%--<button type=button class="btn btn-success" @click="processRequestForEFR('new-connection-process')">Save</button>--%>
                <button type=button class="btn btn-success"
                        @click="paymentVerifiedNextStep">
                    Submit*
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>