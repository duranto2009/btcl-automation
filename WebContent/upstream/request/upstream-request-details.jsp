<style>
    .portlet > .portlet-title > .tools > a.collapse {
        background-image: url(blah) !important;
        width: 14px;
        visibility: visible;
    }

    .portlet > .portlet-title > .tools > a.expand {
        background-image: url(blah) !important;
        width: 14px;
        visibility: visible;
    }

</style>
<div id="btcl-upstream">

<%--    <template v-if="loading">--%>
<%--        <btcl-body>--%>

<%--            <div class="row" style="text-align: center;margin-top:10%">--%>
<%--                <i class="fa fa-spinner fa-spin fa-5x"></i>--%>
<%--            </div>--%>
<%--        </btcl-body>--%>
<%--    </template>--%>
    <template
<%--            v-else--%>
    >
        <%--Application Details--%>
        <btcl-body title="Upstream" subtitle='Application Details' :loader="loading">

            <%--if client correction needed--%>
            <%--<btcl-portlet v-if="application.state.name=='VPN_REQUESTED_CLIENT_FOR_CORRECTION'">--%>
            <%--<jsp:include page="../../coLocation/application/co-location-application-form.jsp"/>--%>
            <%--</btcl-portlet>--%>

            <%--show application form details--%>
            <template
            <%--v-else--%>
            >
                <btcl-portlet>
                    <div class=row>
                        <%--left column--%>
                        <div class="col-sm-6" style="padding-left:2%">
                            <template v-for="(element,index) in application.formElements"
                                      v-if="index<application.formElements.length/2">
                                <div class="form-group">
                                    <div class="row">
                                        <label class="col-sm-4"
                                               style="text-align: left;"><b>{{element.Label}}</b></label>
                                        <div v-if="element.Label!='Application Type'" class="col-sm-6 text-center"
                                             style="background:  #f2f1f1;padding:1%">
                                            <template><span>{{element.Value}}</span></template>
                                        </div>
                                        <div v-else class="col-sm-6 text-center"
                                             v-bind:style="{ background: application.color, padding: '1%'}">
                                            <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                        </div>
                                    </div>
                                </div>
                            </template>
                        </div>
                        <%--right column--%>
                        <div class="col-sm-6" style="padding-left: 2%">
                            <template v-for="(element,index) in application.formElements"
                                      v-if="index>=application.formElements.length/2">
                                <div class="form-group">
                                    <div class="row">
                                        <label class="col-sm-4"
                                               style="text-align: left;"><b>{{element.Label}}</b></label>
                                        <div v-if="element.Label!='Application Type'" class="col-sm-6 text-center"
                                             style="background:  #f2f1f1;padding:1%">
                                            <template><span>{{element.Value}}</span></template>
                                        </div>
                                        <div v-else class="col-sm-6 text-center"
                                             v-bind:style="{ background: application.color, padding: '1%'}">
                                            <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                        </div>
                                    </div>
                                </div>
                            </template>
                        </div>
                    </div>

                </btcl-portlet>
                <%--comments--%>
                <%--<btcl-portlet>--%>
                <%--<div class="row">--%>
                <%--<div class="col-sm-12" style="color: forestgreen;" @click="showCommentModal(link.Comments)"><a>Click--%>
                <%--Here to See All Comments on this Link</a></div>--%>
                <%--</div>--%>
                <%--</btcl-portlet>--%>

                <%--documents--%>
                <template v-if="application.applicationObject.applicationType !== 'UPSTREAM_CONTRACT_CLOSE_REQUEST'">
                    <jsp:include page="../common/views/upstream-gm-doc-upload.jsp"/>
                </template>
            </template>
        </btcl-body>

        <%--circuit info--%>
        <jsp:include page="../common/views/upstream-circuit-info-view.jsp"/>

        <btcl-body title="SRF Date"
                   v-if="application.applicationObject.applicationType==='UPSTREAM_NEW_REQUEST' && application.state.view === 'circuit-info'">
            <%--                <btcl-portlet title="Enter SRF Date">--%>
            <btcl-field title="SRF Date">
                <btcl-datepicker :date.sync="application.srfDate" pattern="DD-MM-YYYY"></btcl-datepicker>
            </btcl-field>
            </btcl-portlet>
        </btcl-body>

        <%--Documents--%>
        <%--<jsp:include page="../common/view/view-documents.jsp"/>--%>

        <%--Add correction form if correction state--%>
        <btcl-body v-if="application.state.view == 'update-form-view' && application.applicationObject.applicationType==='UPSTREAM_NEW_REQUEST'" title="Upstream" subtitle='Application Correction'>
            <btcl-portlet>
                <jsp:include page="../common/views/upstream-new-request-form-view.jsp"/>
            </btcl-portlet>
        </btcl-body>

            <btcl-body v-if="application.state.view == 'update-form-view' && (application.applicationObject.applicationType==='UPSTREAM_DOWNGRADE'  || application.applicationObject.applicationType==='UPSTREAM_UPGRADE')" title="Upstream" subtitle='Application Correction'>
                <btcl-portlet>
                    <btcl-input title="Enter Modified Bandwidth Capacity(GB)" :text.sync="application.bandwidthCapacity"
                                placeholder="Enter Modified Bandwidth Capactiy Amount" :number="true"></btcl-input>

                </btcl-portlet>
            </btcl-body>

            <btcl-body v-if="application.state.view == 'update-form-view' && application.applicationObject.applicationType==='UPSTREAM_CONTRACT_EXTENSION_REQUEST'" title="Upstream" subtitle='Application Correction'>
                <btcl-portlet>
                    <btcl-field title="Contract Extension To Date">
                        <btcl-datepicker :date.sync="application.applicationObject.contractDuration" pattern="DD-MM-YYYY"></btcl-datepicker>
                    </btcl-field>
                </btcl-portlet>
            </btcl-body>



         <btcl-body title="Comments" v-if="application.hasOwnProperty('comments') && application.comments.length>0">
             <btcl-portlet v-for="comment in application.comments">
                 <p >{{comment.commentProviderName}} on {{new Date(parseInt(comment.submissionDate)).toLocaleDateString("ca-ES")}} <!--{{msToTime(comment.submissionDate)}}--> : {{comment.comment}}</p>
             </btcl-portlet>
         </btcl-body>

        <%--action forward--%>
        <btcl-body title="Available Global Actions" v-if="application.action.length>1 && showAction">
            <btcl-portlet>
                <div>
                    <ul style="list-style-type:none">
                        <li v-for="(element,elementIndex) in application.action" :key="elementIndex"
                            v-if="elementIndex>0">
                            <label>
                                <span><input type="radio"
                                             name="radioselect"
                                <%--@click="deleteAllLocalActionPicked()"--%>
                                             v-model="picked" :value="element"> {{element.description}}</span>
                            </label>
                        </li>
                    </ul>
                </div>
                <hr>
                <button type="submit" class="btn green-haze btn-block" @click="nextStep">Submit</button>
            </btcl-portlet>
        </btcl-body>

        <%--necessary modals--%>

    </template>


</div>
<script src="<%=request.getContextPath()%>/upstream/scripts/upstream-common.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/upstream/scripts/upstream-request-details.js" type="text/javascript"></script>
