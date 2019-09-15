<div class="modal fade" id="showOfficeDetails" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 50%;">
        <div class="modal-content">
            <div class="portlet-title">
                <div class="caption" align="center"
                     style="background: #fab74d;padding: 10px;"><p>Office Details</p>
                </div>
                <div class="tools">
                    <a href="javascript:;" data-original-title="" title=""
                       class="collapse"></a>
                </div>
                <div class="modal-body">
                    <div>
                        <btcl-portlet>
                            <ul class="list-group" v-for="(office,officeIndex) in application.officeList">
                                <li class="list-group-item active"
                                    style="background-color: black"><b font-size="20px">{{office.name}},
                                    {{office.address}}</b></li>
                                <li class="list-group-item" v-for="(loop, loopIndex) in office.loops">
                                    POP {{loopIndex+1}} : {{loop.popName}}
                                </li>
                            </ul>
                        </btcl-portlet>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                    </button>
                </div>
            </div>

        </div>
    </div>
</div>