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

    <template v-if="loading">
        <btcl-body>

            <div class="row" style="text-align: center;margin-top:10%">
                <i class="fa fa-spinner fa-spin fa-5x"></i>
            </div>
        </btcl-body>
    </template>
    <template v-else>
        <btcl-body title="Upstream" subtitle='Contract Details'>
            <%--{{}}--%>

            <div class="row">
                <div class="col-md-8">
                    <jsp:include page="../common/views/upstream-contract-information-view.jsp"/>

<%--                    documents show--%>
                    <btcl-portlet title="Documents" >

                        <btcl-file-upload-upstream :params="{
                                'moduleId': 5,
                                'applicationId': contract.applicationId,
                                'fileUploadURL': fileUploadURL,
                                'isAllowed' : false
                            }">
                        </btcl-file-upload-upstream>
                    </btcl-portlet>

                    <%--ckt info show below--%>
                    <jsp:include page="../common/views/upstream-circuit-info-view.jsp"/>


                </div>
                <div class="col-md-4">
                    <btcl-portlet title=History>
                        <div>
                            <button type="button" class="btn btn-default btn-block"
                                    v-for="(history, historyIndex) in this.contractList"
                                    @click="getContractByHistoryID(historyIndex)"
                            >
                                <%--XXX--%>
                                <div align="left">
                                    <b>{{showFormattedHistoryName(history.applicationType)}}</b>
                                    <br/>
                                    {{new Date(history.activeFrom).toLocaleDateString("ca-ES")}}
                                </div>
                            </button>
                        </div>
                    </btcl-portlet>
                </div>
            </div>
        </btcl-body>
    </template>
    <%--{{bla}}--%>

</div>
<script>
    var historyId = parseInt((new URL(window.location.href)).searchParams.get("historyId"));
</script>
<script src="<%=request.getContextPath()%>/upstream/scripts/upstream-common.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/upstream/scripts/upstream-contract-details.js"
        type="text/javascript"></script>
