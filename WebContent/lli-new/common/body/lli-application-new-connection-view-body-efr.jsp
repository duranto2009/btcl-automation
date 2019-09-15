<btcl-body  v-if="link.state.view=='view-vendor-selection'" title="Feasibility Report">
    <btcl-portlet title="EFR Responses">
        <div class="form-body">
            <div class="form-group"
                 style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                <div class="col-md-2"><p style="text-align: center;">Vendor Name</p></div>
                <div class="col-md-2"><p style="text-align: center;">From</p></div>
                <div class="col-md-2"><p style="text-align: center;">To</p></div>
                <div class="col-md-1"><p style="text-align: center;">Length(m)</p></div>
                <div class="col-md-2"><p style="text-align: center;">OFC Type</p></div>
                <div class="col-md-2"><p style="text-align: center;">Response</p></div>
                <div class="col-md-1"><p style="text-align: center;">Select</p></div>
            </div>
            <template v-for="office in link.officeList">
                <div class="form-group" v-for="element in office.efrs">
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.vendorName}}</p>
                    </div>
                    <div class="col-md-2">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.source}}</p>
                    </div>


                    <div class="col-md-2"><p style="text-align: center;border: 1px solid;padding: 10px;">
                        {{element.source}}</p></div>

                    <div class="col-md-1">
                        <p style="text-align: center;border: 1px solid;padding: 10px;">
                            {{element.proposedLoopDistance}}</p>
                    </div>
                    <div class="col-md-2">
                        <select disabled class="form-control" v-model="element.ofcType"
                                style="margin-top: 7px;">
                            <option value="1">Single</option>f
                            <option value="2">Double</option>
                        </select>
                    </div>
                    <div class="col-md-2" align="center">
                        <button class="btn-success" align="center"
                                style="width:100%;margin-top:5%;background-color:green!important;"
                                v-if="element.isReplied==true">Responded
                        </button>
                    </div>
                    <div class="col-md-1" align="center">
                        <input
                                style="margin-top:18%;" type="checkbox"
                                v-model="element.isSelected">
                    </div>
                    <p :value="element.quotationStatus=1"></p>
                </div>
            </template>
        </div>
    </btcl-portlet>
</btcl-body>
