<div class="modal fade" id="requestToLocalDGMModal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 50%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <div>
                    <btcl-portlet>
                        <btcl-field title="Zone">
                            <lli-zone-search :client.sync="application.zone">Zone
                            </lli-zone-search>
                        </btcl-field>
                        <btcl-input title="Comment" :text.sync="comment"></btcl-input>
                    </btcl-portlet>
                </div>


            </div>
            <div class="modal-footer">
                <%--<button type=button class="btn btn-success" @click="processRequestForEFR('new-connection-process')">Save</button>--%>
                <button type=button class="btn btn-success"
                        @click="requestToLocalDGMModalSubmit">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>