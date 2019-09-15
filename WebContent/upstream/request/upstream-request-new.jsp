<%@ page import="upstream.UpstreamConstants" %>
<div id="btcl-vpn">
    <template v-if="loading">
        <div class="row" style="text-align: center;margin-top:10%">
            <i class="fa fa-spinner fa-spin fa-5x"></i>
        </div>
    </template>
    <template v-else>
        <btcl-body title="Upstream" subtitle='New Request' v-cloak="true">
            <btcl-portlet>
                <%--<btcl-field title="Client Name">--%>
                <%--<user-search-by-module :client.sync="application.client" :module="moduleId">Client--%>
                <%--</user-search-by-module>--%>
                <%--</btcl-field>--%>
                <jsp:include page="../common/views/upstream-new-request-form-view.jsp"/>


                <%--<btcl-input title="Description" :text.sync="application.description" placeholder="Write Description"></btcl-input>--%>
                <btcl-input title="Comment" :text.sync="application.comment" placeholder="Write Comment"></btcl-input>

                <%--<btcl-field title="Suggested Date">--%>
                <%--<btcl-datepicker :date.sync="application.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>--%>
                <%--</btcl-field>--%>


                <br/>
                <button type=button class="btn green-haze btn-block btn-lg" @click="submitNewConnection">Submit</button>
            </btcl-portlet>

        </btcl-body>
    </template>
</div>
<script>
    <%--var applicationType = '<%=UpstreamConstants.APPLICATION_TYPE.NEW%>';--%>
</script>

<script src="../upstream/scripts/upstream-common.js" type="text/javascript"></script>
<script src="../upstream/scripts/upstream-request-new.js" type="text/javascript"></script>
