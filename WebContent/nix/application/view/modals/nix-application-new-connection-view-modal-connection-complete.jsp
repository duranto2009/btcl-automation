<div class="modal fade" id="connectionCompletionModal" role="dialog">
<div class="modal-dialog modal-lg" style="width: 50%;">

    <!-- Modal local loop selection-->
    <div class="modal-content">
        <div class="modal-body">
            <div>
                <div class="form-body">


                    <div class="form-group"><label class="control-label col-md-3"
                                                   style="text-align: center;">
                        Connection Name

                    </label>
                        <div v-if="application.connection" class="col-md-9">
                            <%--<input type="text"--%>
                            <%--v-model="application.connectionName"--%>
                            <%--placeholder="Provide a Connection Name"--%>
                            <%--class="form-control input-lg">--%>
                            <p>{{application.connection.name}}</p>
                        </div>
                        <div v-else class="col-md-9"><input type="text"
                                                            v-model="application.connectionName"
                                                            placeholder="Provide a Connection Name"
                                                            class="form-control input-lg">
                        </div>
                    </div>


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
                    @click="connectionCompletionModal">
                Submit
            </button>
            <button type="button" class="btn btn-danger" data-dismiss="modal">Close
            </button>
        </div>
    </div>

</div>
</div>