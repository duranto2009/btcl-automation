<div v-if="noClientCorrectionNeeded">
    <btcl-portlet>

        <btcl-field v-for="(element,index) in application.formElements" :key="index">
            <div class="form-group">
                <div class="row">
                    <label class="col-sm-4"
                           style="text-align: left;">{{element.Label}}</label>
                    <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                         style="background:  #f2f1f1;padding:1%">
                        <template><span>{{element.Value}}</span></template>
                    </div>


                    <div v-else class="col-sm-6 text-center" v-bind:style="{ background: application.color}">

                        <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                    </div>


                </div>
            </div>
        </btcl-field>

        <btcl-field>
            <div class="form-group">
                <div class="row">
                    <label class="col-sm-4 control-label"
                           style="text-align: left;">Connection Address</label>
                    <div class="col-sm-6" style="padding: 0px !important">
                        <a class="form-control"
                           style="background:  #f2f1f1;text-align: center;color:blue"
                           v-on:click="showOfficeDetails">
                            Click Here for More
                        </a>
                    </div>
                </div>
            </div>
        </btcl-field>
    </btcl-portlet>
</div>

<%-- REQUEST FOR CLIENT CORRECTION STEP--%>
<div v-else>
    <btcl-portlet v-if="
    application.applicationType.ID == TYPE_FACTORY.ADDITIONAL_LOCAL_LOOP
    ||application.applicationType.ID == TYPE_FACTORY.ADDITIONAL_PORT
">
        <%--<btcl-field title="Client"><lli-client-search :client.sync="application.client">Client</lli-client-search></btcl-field>--%>

        <div class="form-group">
            <div class=row>
                <label class="col-sm-4 control-label">{{Object.keys(application.Comments)[0]}}</label>
                <div class=col-sm-6>
                    <p class="form-control">{{Object.values(application.Comments)[0]}}</p>
                </div>
            </div>
        </div>

        <btcl-field title="Loop Provider">
            <multiselect v-model="application.loopProvider"
                         :options="loopProviderList"
                         label="label" track-by="label" open-direction="bottom">
            </multiselect>
        </btcl-field>


        <btcl-field title="Port Count" required="true">
            <input type=number class="form-control" v-model=application.portCount>
        </btcl-field>

        <btcl-input title="Description" :text.sync="application.description"></btcl-input>

        <btcl-input title="Comment" :text.sync="application.comment"></btcl-input>

        <btcl-field title="Suggested Date">
            <btcl-datepicker :date.sync="application.suggestedDate"
                             pattern="DD-MM-YYYY"></btcl-datepicker>
        </btcl-field>

    </btcl-portlet>

    <btcl-portlet v-else-if="application.applicationType.ID == TYPE_FACTORY.ADDITIONAL_IP">
        <%--<btcl-field title="Client"><lli-client-search :client.sync="application.client">Client</lli-client-search></btcl-field>--%>

        <div class="form-group">
            <div class=row>
                <label class="col-sm-4 control-label">{{Object.keys(application.Comments)[0]}}</label>
                <div class=col-sm-6>
                    <p class="form-control">{{Object.values(application.Comments)[0]}}</p>
                </div>
            </div>
        </div>


        <btcl-field title="IP Count" required="true">
            <input type=number class="form-control" v-model=application.ipCount>
        </btcl-field>

        <btcl-input title="Description" :text.sync="application.description"></btcl-input>

        <btcl-input title="Comment" :text.sync="application.comment"></btcl-input>

        <btcl-field title="Suggested Date">
            <btcl-datepicker :date.sync="application.suggestedDate"
                             pattern="DD-MM-YYYY"></btcl-datepicker>
        </btcl-field>

    </btcl-portlet>


    <btcl-portlet v-else>
        <%--<btcl-field title="Client"><lli-client-search :client.sync="application.client">Client</lli-client-search></btcl-field>--%>

        <div class="form-group">
            <div class=row>
                <label class="col-sm-4 control-label">{{Object.keys(application.Comments)[0]}}</label>
                <div class=col-sm-6>
                    <p class="form-control">{{Object.values(application.Comments)[0]}}</p>
                </div>
            </div>
        </div>


        <template v-if="application.connectionID>0">
            <div class="form-group">
                <div class=row>
                    <label class="col-sm-4 control-label">Connection Name</label>
                    <div class=col-sm-6>
                        <p class="form-control">{{application.connection.name}}</p>
                    </div>
                </div>
            </div>


            <div class="form-group">
                <div class=row>
                    <label class="col-sm-4 control-label">Existing Bandwidth</label>
                    <div class=col-sm-6>
                        <p class="form-control">{{application.connection.bandwidth}} Mbps</p>
                    </div>
                </div>
            </div>
        </template>


        <btcl-input title="Bandwidth (Mbps)" :text.sync="application.bandwidth"></btcl-input>


        <template v-if="application.applicationType.ID == TYPE_FACTORY.NEW_CONNECTION
                ||application.applicationType.ID == TYPE_FACTORY.NEW_CONNECTION_FROM_SHIFTING_BW">

            <btcl-field title="Connection Type">
                <connection-type :data.sync=application.connectionType :client="application.client"></connection-type>
            </btcl-field>

            <btcl-field title="Loop Provider">
                <multiselect v-model="application.loopProvider"
                             :options="loopProviderList"
                             label="label" track-by="label" open-direction="bottom">
                </multiselect>
            </btcl-field>


            <btcl-field title="Zone" v-if="application.zone.id>0">
                <multiselect v-model="application.zone" :options="zoneList" placeholder="Zone"
                             label="nameEng" track-by="nameEng" open-direction="bottom"></multiselect>
            </btcl-field>

        </template>

        <btcl-field v-if="application.applicationType.ID == TYPE_FACTORY.NEW_CONNECTION && application.connectionType.ID==2"
                    title="Duration (Days)">
            <input type=number class="form-control" v-model=application.duration>
        </btcl-field>
        <btcl-input title="Description" :text.sync="application.description"></btcl-input>
        <btcl-field title="Suggested Date">
            <btcl-datepicker :date.sync="application.suggestedDate"
                             pattern="DD-MM-YYYY"></btcl-datepicker>
        </btcl-field>
        <btcl-input title="Comment" :text.sync="application.comment"></btcl-input>

        <template v-if="application.applicationType.ID == TYPE_FACTORY.NEW_CONNECTION
                ||application.applicationType.ID == TYPE_FACTORY.NEW_CONNECTION_FROM_SHIFTING_BW">
            <btcl-bounded v-for="(office,officeIndex) in application.officeList"
                          :title="'Office '+(officeIndex+1)"
                          :key="officeIndex">

                <btcl-input title="Connection Office Name" :text.sync="office.officeName"></btcl-input>
                <btcl-input title="Connection Office Address" :text.sync="office.officeAddress"></btcl-input>
                <div align="right" v-if="officeIndex!=0">
                    <%--<button type="button"--%>
                    <%--@click="deleteOffice(application,officeIndex, $event)"--%>
                    <%--class="btn red btn-outline">--%>
                    <%--Remove this Office--%>
                    <%--</button>--%>
                </div>
            </btcl-bounded>
        </template>


        <template v-if="application.connectionID>0">
            <btcl-bounded v-for="(office,officeIndex) in application.officeList"
                          :title="'Office '+(officeIndex+1)"
                          :key="officeIndex">

                <btcl-info title="Connection Office Name" :text.sync="office.officeName"></btcl-info>
                <btcl-info title="Connection Office Address" :text.sync="office.officeAddress"></btcl-info>
                <div align="right" v-if="officeIndex!=0">
                    <%--<button type="button"--%>
                    <%--@click="deleteOffice(application,officeIndex, $event)"--%>
                    <%--class="btn red btn-outline">--%>
                    <%--Remove this Office--%>
                    <%--</button>--%>
                </div>
            </btcl-bounded>
        </template>
    </btcl-portlet>

</div>
