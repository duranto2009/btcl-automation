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
<div id="btcl-vpn-application" v-cloak>


    <%--Application Details--%>
    <btcl-body title="VPN" subtitle='Application Details' :loader="loading">

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
                                    <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                         style="background:  #f2f1f1;padding:1%">
                                        <template><span>{{element.Value}}</span></template>
                                    </div>
                                    <div v-else class="col-sm-8 text-center"
                                         v-bind:style="{ background: application.color}">
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
                                    <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                         style="background:  #f2f1f1;padding:1%">
                                        <template><span>{{element.Value}}</span></template>
                                    </div>
                                    <div v-else class="col-sm-6 text-center"
                                         v-bind:style="{ background: application.color}">
                                        <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </div>
                </div>
            </btcl-portlet>
        </template>

        <%--VPN Link Information--%>
        <template v-for="(link, linkIndex) in application.vpnApplicationLinks">
            <%--if client correction needed--%>
            <template v-if="link.state.name=='VPN_REQUESTED_CLIENT_FOR_CORRECTION'
                ||link.state.name=='VPN_WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION'">
                <template v-if="linkIndex==0">
                    <btcl-portlet>
                        <div class="row">
                            <div align="center" class="col-sm-6" style="color: forestgreen;"
                                 @click="showCommentModal(link.Comments)"><a>Click Here to See All Comments on
                                this Link</a></div>
                            <%--                                <div align="center" class="col-sm-6" style="color: green;"--%>
                            <%--                                     @click="addCommentModal(link)"><a>Click Here to Add a Comment on this Link</a>--%>
                            <%--                                </div>--%>
                        </div>
                    </btcl-portlet>
                    <jsp:include page="../common/view/view-link-office-information-add.jsp"/>
                    <btcl-portlet title="Available Actions on this Link" v-if="!isSamePayment()">
                        <div>
                            <ul style="list-style-type:none">
                                <li v-for="(element,elementIndex) in link.action" :key="elementIndex">
                                    <%--<label v-if="demandNoteGeneratedSkip(element)"></label>--%>
                                    <label
                                    <%--v-else--%>
                                    >
                                    <span><input type="radio"
                                    <%--:name="'linkActionForwards'+linkIndex"--%>
                                                 name="radioselect"
                                                 v-model="picked"
                                                 :value="element"> {{element.description}}</span>
                                    </label>
                                </li>
                            </ul>
                        </div>
                        <hr>
                        <br>
                        <div align="center">
                            <button type="submit" class="btn green-haze" @click="nextStep(linkIndex)">Submit</button>
                        </div>
                    </btcl-portlet>
                </template>
                <template v-else></template>
            </template>
            <%--Link Details Information--%>
            <template v-else>
                <btcl-portlet :title="getLinkTitle(link.id,link.linkName)" :collapse="collapse">
                    <div class="form-group">
                        <btcl-portlet>
                            <div class=row>
                                <div class="col-sm-6" style="padding-left:2%">
                                    <template v-for="(element,index) in link.formElements"
                                              v-if="index<link.formElements.length/2">
                                        <div class="form-group">
                                            <div class="row">
                                                <label class="col-sm-4"
                                                       style="text-align: left;"><b>{{element.Label}}</b></label>
                                                <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                                     style="background:  #f2f1f1;padding:1%">
                                                    <template><span>{{element.Value}}</span></template>
                                                </div>
                                                <div v-else class="col-sm-6 text-center"
                                                     v-bind:style="{ background: application.color}">
                                                    <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </template>
                                </div>

                                <div class="col-sm-6" style="padding-left:2%">
                                    <template v-for="(element,index) in link.formElements"
                                              v-if="index>=link.formElements.length/2">
                                        <div class="form-group">
                                            <div class="row">
                                                <label class="col-sm-4"
                                                       style="text-align: left;"><b>{{element.Label}}</b></label>
                                                <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                                                     style="background:  #f2f1f1;padding:1%">
                                                    <template><span>{{element.Value}}</span></template>
                                                </div>
                                                <div v-else class="col-sm-6 text-center"
                                                     v-bind:style="{ background: application.color}">
                                                    <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                                                </div>

                                            </div>
                                        </div>
                                    </template>
                                </div>
                            </div>
                        </btcl-portlet>
                        <btcl-portlet>
                            <div class="row">
                                <div align="center" class="col-sm-6" style="color: forestgreen;"
                                     @click="showCommentModal(link.Comments)"><a>Click Here to See All Comments on
                                    this Link</a></div>
                                <div align="center" class="col-sm-6" style="color: green;"
                                     @click="addCommentModal(link)"><a>Click Here to Add a Comment on this Link</a>
                                </div>
                            </div>
                        </btcl-portlet>

                        <div class=row>
                            <div class="col-sm-6">
                                <btcl-portlet title="Local End" v-if="link.hasOwnProperty('localOffice')">
                                    <btcl-info title="Office Name" :text.sync="link.localOffice.officeName"></btcl-info>
                                    <btcl-info title="Office Address"
                                               :text.sync="link.localOffice.officeAddress"></btcl-info>
                                </btcl-portlet>
                            </div>

                            <div class="col-sm-6">
                                <btcl-portlet title="Remote End" v-if="link.hasOwnProperty('remoteOffice')">
                                    <btcl-info title="Office Name"
                                               :text.sync="link.remoteOffice.officeName"></btcl-info>
                                    <btcl-info title="Office Address"
                                               :text.sync="link.remoteOffice.officeAddress"></btcl-info>
                                </btcl-portlet>
                            </div>
                        </div>


                        <%--views--%>
                        <jsp:include page="../common/view/view-efr.jsp"/>
                        <jsp:include page="../common/view/view-ifr-details.jsp"/>
                        <jsp:include page="../common/view/view-loop-distance-approve.jsp"/>

                        <%--link wise advice note--%>

                        <btcl-portlet
                                v-if="application.vpnApplicationLinks[linkIndex].state.view==='ADVICE_NOTE'">
                            <div align="center">
                                <button type="submit" class="btn green-haze"
                                        @click="redirect('advicenote', application.vpnApplicationLinks[linkIndex].id)">
                                    View Advice Note
                                </button>
                            </div>

                        </btcl-portlet>

                        <%--link wise demand note--%>

                        <btcl-portlet

                                v-if="application.vpnApplicationLinks[linkIndex].state.view==='DEMAND_NOTE'"
                        >
                            <div align="center">
                                <button type="submit" class="btn green-haze"
                                        @click="redirect('demandnote', application.vpnApplicationLinks[linkIndex])">View
                                    Demand Note
                                </button>
                            </div>

                        </btcl-portlet>

                        <btcl-portlet

                                v-if="application.vpnApplicationLinks[linkIndex].state.view==='WORK_ORDER'"
                        >
                            <div align="center">
                                <button type="submit" class="btn green-haze"
                                        @click="redirect('workorder', application.vpnApplicationLinks[linkIndex])">View
                                    Work Order
                                </button>
                            </div>

                        </btcl-portlet>

                        <template v-if="link.hasOwnProperty('action')">
                            <btcl-portlet title="Available Actions on this Link"
                                          v-if="!isSamePayment()"
                            >
                                <div>
                                    <ul style="list-style-type:none">
                                        <li v-for="(element,elementIndex) in link.action" :key="elementIndex">
                                            <%--<label v-if="demandNoteGeneratedSkip(element)"></label>--%>
                                            <label
                                            <%--v-else--%>
                                            >
                                    <span><input type="radio"
                                                 :name="'linkActionForwards'+linkIndex"
                                    <%--name="radioselect"--%>
                                                 @click="localLinkActionPick(linkIndex,elementIndex)"
                                                 v-model="link.picked"
                                                 :value="element"> {{element.description}}</span>
                                            </label>
                                        </li>
                                    </ul>
                                </div>
                                <hr>
                                <br>
                                <div align="center">
                                    <button type="submit" class="btn green-haze" @click="nextStep(linkIndex)">Submit
                                    </button>
                                </div>
                            </btcl-portlet>
                        </template>
                    </div>
                </btcl-portlet>
            </template>


        </template>

    </btcl-body>

    <%--Documents--%>
    <jsp:include page="../common/view/view-documents.jsp"/>


    <%--action forward--%>
    <btcl-body title="Available Global Actions"
               v-if="application.action.length>1 && checkPaymentCondition() && loading == false && !details">
        <btcl-portlet>
            <div>
                <ul style="list-style-type:none">
                    <li v-for="(element,elementIndex) in application.action" :key="elementIndex"
                        v-if="elementIndex>0">
                        <label>
                                <span><input type="radio"
                                             name="radioselect"
                                             @click="deleteAllLocalActionPicked()"
                                             v-model="picked" :value="element"> {{element.description}}</span>
                        </label>
                    </li>
                </ul>
            </div>
            <hr>
            <button type="submit" class="btn green-haze btn-block" @click="nextStep">Submit</button>
        </btcl-portlet>
    </btcl-body>

    <%--server room testing complete--%>
    <jsp:include page="../common/modal/modal-server-room-testing-complete.jsp"/>

    <%--connection completion--%>
    <%--<jsp:include page="../../lli/application/new-connection/view/modals/lli-application-new-connection-view-modal-connection-complete.jsp"/>--%>
    <jsp:include
            page="../../lli/application/new-connection/view/modals/lli-application-new-connection-view-modal-advice-note.jsp"/>


    <%-- pop selection modal--%>
    <jsp:include page="../common/modal/modal-pop-selection.jsp"/>
    <jsp:include page="../common/modal/modal-vendor-selection.jsp"/>
    <jsp:include page="../common/modal/modal-vendor-response.jsp"/>

    <%--Comments modal--%>
    <jsp:include page="../common/modal/modal-comments.jsp"/>

    <%--zone selection modal--%>
    <jsp:include page="../common/modal/modal-zone-selection.jsp"/>

    <%--modal client correction for bandwidth change--%>
    <jsp:include page="../common/modal/modal-bandwidth-change.jsp"/>


</div>
<script src="../script/vpn-common.js" type="text/javascript"></script>
<script src="../script/vpn-link-details.js" type="text/javascript"></script>
