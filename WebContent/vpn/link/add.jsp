<div id="btcl-vpn">


    <btcl-body title="VPN" subtitle='New Link' v-cloak="true" :loader="loading">
        <btcl-portlet >

            <btcl-info v-if="application.state.name=='CLIENT_CORRECTION'" title="Client"
                       :text=application.client.value></btcl-info>
            <%--<btcl-info v-if="application.state.name=='CLIENT_CORRECTION'" title="Client" :text=application.client.value></btcl-info>--%>
            <btcl-field v-else title="Client Name" required = "true">
                <user-search-by-module :client.sync="application.client" :module="moduleId">Client
                </user-search-by-module>
            </btcl-field>
            <btcl-field title="VPN Layer Type" required = "true">
                <multiselect placeholder="Select VPN Layer Type" v-model="application.layerType"
                             :options="layerTypeList" label=value :allow-empty="false" :searchable=true
                             open-direction="bottom"></multiselect>
            </btcl-field>

            <%--<btcl-field title="Connection Type">--%>
            <%--<multiselect placeholder="Select Connection Type" v-model="application.connectionType" :options="connectionTypeList" label=value :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>--%>
            <%--</btcl-field>--%>
            <template v-if="application.client!=null">
                <btcl-field v-if="application.client.registrantType==1" title="Want's to Pay DN(Connection Charge)">
                    <multiselect v-model="application.skipPayment"
                                 :options="[{id:1,value:'First Month Bill'},{id:0,value:'Before Connection'}]"
                                 label=value :allow-empty="false" :searchable=true open-direction="bottom">
                    </multiselect>
                </btcl-field>
            </template>

            <%--<btcl-input title="Description" :text.sync="application.description" placeholder="Write Description"></btcl-input>--%>
            <btcl-input title="Comment" :text.sync="application.comment" placeholder="Write Comment"></btcl-input>
            <btcl-field title="Suggested Date" required = "true">
                <btcl-datepicker :date.sync="application.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>
            </btcl-field>

            <%--<btcl-input title="Link" :text.sync="application.link" placeholder="Write link "></btcl-input>--%>

            <br/>
            <br/>
            <%--<button type=button class="btn green-haze btn-block btn-lg" @click="submitNewConnection">Submit</button>--%>
        </btcl-portlet>

        <%--start: office infromation add--%>
        <btcl-portlet title="Office Information">
            <jsp:include page="../common/view/view-link-office-information-add.jsp"/>
            <button type=button class="btn green-haze btn-block btn-lg" @click="submitNewConnection">Submit</button>
        </btcl-portlet>
        <%--start: office infromation add--%>


    </btcl-body>

</div>
<script src="../script/vpn-common.js" type="text/javascript"></script>
<script src="../script/vpn-add.js" type="text/javascript"></script>
