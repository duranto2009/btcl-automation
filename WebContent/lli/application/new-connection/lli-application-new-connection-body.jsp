<div id="btcl-application" v-cloak="true">
    <btcl-body title="New Connection" subtitle="Application" :loader="loading">
        <%--<btcl-form :action="contextPath + 'lli/application/new-connection.do'" :name="['application']"
                   :form-data="[application]" :redirect="goView">--%>

        <%--<form>--%>
        <btcl-portlet>
            <btcl-field title="Client" required="true">
                <lli-client-search :client.sync="application.client"></lli-client-search>
            </btcl-field>
            <btcl-field title="Connection Type" required="true">
                <select class="form-control" v-model="application.connectionType">
                    <option v-for="type in application.connectionTypes" :value="type">
                        {{type.label}}
                    </option>
                </select>
            </btcl-field>
            <btcl-field v-if="application.client.registrantType==1
<%--                && application.connectionType.ID!=2--%>
"
                        title="Want's to Pay DN(Connection Charge)"
                        required="true">
                <multiselect v-model="application.skipPayment"
                             :options="[{ID:1,label:'First Month Bill'},{ID:0,label:'Before Connection'}]"
                             :track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
                </multiselect>
            </btcl-field>
            <btcl-input title="Bandwidth (Mbps)" required="true" :text.sync="application.bandwidth" :number="true"
                        placeholder="Enter Bandwidth"></btcl-input>
            <btcl-field title="Zone" required="true">
                <lli-zone-search :client.sync="application.zone"></lli-zone-search>
            </btcl-field>
            <btcl-field title="Loop Provider" required="true">
                <multiselect v-model="application.loopProvider"
                             :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]"
                             label=label :allow-empty="false" :searchable=true open-direction="bottom">
                </multiselect>
            </btcl-field>
            <btcl-field v-if="application.connectionType && application.connectionType.ID==2"
                        title="Duration (Days)" required="true">
                <input type=number class="form-control" v-model=application.duration>
            </btcl-field>
            <btcl-input title="Description" :text.sync="application.description"
                        placeholder="Write Description"></btcl-input>
            <btcl-input title="Comment" :text.sync="application.comment" placeholder="Write Comment"></btcl-input>
            <btcl-field title="Suggested Date" required="true">
                <btcl-datepicker :date.sync="application.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>
            </btcl-field>
            <btcl-bounded v-for="(office,officeIndex) in application.officeList" :title="'Connection Office '+(officeIndex+1)"
                          :key="officeIndex">
                <div align="right" v-if="officeIndex!=0">
                    <button type="button"
                    <%--@click="deleteLocalLoop(content, officeIndex, officeIndex, $event)"--%>
                            @click="deleteOffice(application,officeIndex, $event)"
                            class="btn red btn-outline">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
                <btcl-input title="Connection Office Name" required="true" :text.sync="office.officeName"
                            placeholder="Enter Office Name"></btcl-input>
                <btcl-input title="Connection Office Address" required="true" :text.sync="office.officeAddress"
                            placeholder="Enter Office Address"></btcl-input>

            </btcl-bounded>
            <div align="center">

<%--                disabled by bony--%>
<%--                <button type=button--%>
<%--                &lt;%&ndash;@click="addLocalLoop(office,content.bandwidth, $event)" &ndash;%&gt;--%>
<%--                        @click="addOffice(application,  $event)"--%>
<%--                        class="btn green-haze btn-outline">--%>
<%--                    Add More Connection Office--%>
<%--                </button>--%>
            </div>
            <btcl-bounded>
                <button type=button class="btn green-haze btn-block btn-lg" @click="submitFormData">Submit</button>
            </btcl-bounded>

        </btcl-portlet>

        <%--</btcl-form>--%>

    </btcl-body>


</div>


<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=new-connection/lli-application-new-connection.js></script>



