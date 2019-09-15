<div class="modal fade" id="forwardAdviceNote" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 55%;">
        <div class="modal-content">

            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;">Forward Advice Note
                </div>
                <div class="tools">
                    <a href="javascript:;" data-original-title="" title=""
                       class="collapse"></a>
                </div>
            </div>

            <div class="form-body">
                <div class="portlet light bordered">
                    <div class="portlet-body">
                        <div class=form-horizontal>
                            <div class=form-body>

                        <btcl-portlet>
                            <btcl-field title="Forward To">
                                <lli-user-search :client.sync="application.userList" :multiple="true">Client</lli-user-search>

                            </btcl-field>
                            <btcl-input :textarea="true" title="Comment" :text.sync="comment"></btcl-input>


                        </btcl-portlet>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" @click="forwardAdviceNote()">
                    Submit
                </button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>
    </div>
</div>