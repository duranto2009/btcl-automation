<btcl-portlet title="Provide Necessary Information"
              v-if="application.state.view === 'upload-document-for-gm-international'
<%--                  || application.state.name === 'UPSTREAM_APPROVED_VENDOR_AND_FORWARDED_TO_DGM_CORE_UPSTREAM'--%>
                "

>
    <btcl-input title="Bandwidth Price(USD)" :text.sync="application.bandwidthPrice" placeholder="Enter Bandwidth Price"
                :number="true"></btcl-input>
    <btcl-input title="OTC(USD)" :text.sync="application.otc" placeholder="Enter OTC" :number="true"></btcl-input>
    <btcl-input title="MRC(USD)" :text.sync="application.mrc" placeholder="Enter MRC" :number="true"></btcl-input>
    <%--    <btcl-input title="Contact Duration(Years)" :text.sync="application.contractDuration" placeholder="Enter Contact Duration" :number="true"></btcl-input>--%>

    <btcl-field title="Contract Duration"

                v-if="application.applicationObject.applicationType=='UPSTREAM_NEW_REQUEST'"

    >
        <div class="form-group">
            <div class="row">
                <div class="col-md-4">
                    <label class="col-md-3 control-label">Year </label>
                    <div class="col-md-5">
                        <input  type="number" class="form-control input-small input-inline" v-model.number ="application.year" value="0" placeholder="Enter Year"> </div>
                </div>

                <div class="col-md-4">
                    <label class="col-md-3 control-label">Month </label>
                    <div class="col-md-5">
                        <input type="number" class="form-control input-small input-inline" v-model.number ="application.month" value="0" placeholder="Enter Month"> </div>
                </div>

                <div class="col-md-4">
                    <label class="col-md-3 control-label">day </label>
                    <div class="col-md-5">
                        <input type="number" class="form-control input-small input-inline" v-model.number ="application.day" value="0" placeholder="Enter Day"> </div>
                </div>
            </div>

        </div>
<%--        <btcl-datepicker :date.sync="application.contractDuration" pattern="yyyy-MM-dd"></btcl-datepicker>--%>
    </btcl-field>






</btcl-portlet>

<btcl-portlet title="Documents" v-if="application.state.view === 'upload-document-for-gm-international'
                  || application.state.name === 'UPSTREAM_APPROVED_VENDOR_AND_FORWARDED_TO_DGM_CORE_UPSTREAM'">

    <btcl-file-upload-upstream :params="{
                                'moduleId': 5,
                                'applicationId': application.applicationId,
                                'fileUploadURL': fileUploadURL,
                                'isAllowed' : isAllowed
                            }">
    </btcl-file-upload-upstream>
</btcl-portlet>