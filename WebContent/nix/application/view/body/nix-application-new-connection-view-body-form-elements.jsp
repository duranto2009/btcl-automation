
<div v-if="noClientCorrectionNeeded">
    <btcl-portlet>

        <btcl-field v-for="(element,index) in application.formElements" :key="index">
            <div class="form-group">
                <div class="row">
                    <label class="col-sm-4"
                           style="text-align: left;">{{element.Label}}</label>
                    <div v-if="element.Label!='Status'" class="col-sm-6 text-center"
                         style="background:  #f2f1f1;padding:1%">
                        <template><span >{{element.Value}}</span></template>
                    </div>


                    <div v-else class="col-sm-6 text-center"  v-bind:style="{ background: application.color}">

                            <span style="color: white;font-weight: bold ;font-size: 18px!important;">{{element.Value}}</span>
                    </div>


                </div>
            </div>
        </btcl-field>


        <%--<div v-if="ccCheck">
            <btcl-field>
                <div class="form-group">
                    <div class="row">
                        <label class="col-sm-4 control-label" style="text-align: left;padding-left:4%">From
                            Date</label>
                        <div class="col-sm-6">
                            <btcl-datepicker :date.sync="application.fromDate"
                                             pattern="yyyy-MM-dd"></btcl-datepicker>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class=row>
                        <label class="col-sm-4 control-label" style="text-align: left;padding-left:4%">To
                            Date</label>
                        <div class="col-sm-6">
                            <btcl-datepicker :date.sync="application.toDate"
                                             pattern="yyyy-MM-dd"></btcl-datepicker>
                        </div>
                    </div>
                </div>
            </btcl-field>
        </div>--%>
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
    <btcl-portlet v-if="application.applicationType.ID == TYPE_FACTORY.NEW_CONNECTION">
        <%--<btcl-field title="Client"><lli-client-search :client.sync="application.client">Client</lli-client-search></btcl-field>--%>

            <div class="form-group">
                <div class=row>
                    <label class="col-sm-4 control-label" >{{Object.keys(application.Comments)[0]}}</label>
                    <div class=col-sm-6>
                        <p class="form-control">{{Object.values(application.Comments)[0]}}</p>
                    </div>
                </div>
            </div>
            <btcl-field title="Loop Provider">
                <multiselect v-model="application.loopProvider"
                             :options="loopProviderList"
                             label="label" track-by="label"  open-direction="bottom">
                </multiselect>
            </btcl-field>

        <btcl-field title="Zone">
            <multiselect v-model="application.zone" :options="zoneList" placeholder="Zone"
                         label="nameEng" track-by="nameEng" open-direction="bottom"></multiselect>
        </btcl-field>

        <btcl-field title="Suggested Date">
            <btcl-datepicker :date.sync="application.suggestedDate"
                             pattern="DD-MM-YYYY"></btcl-datepicker>
        </btcl-field>
            <btcl-field title="Port Type">
                <multiselect v-model="application.selectedPort" :options="[{ID:1,label:'FE'},{ID:2,label:'GE'},{ID:3,label:'10GE'}]"
                             track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
                </multiselect>
            </btcl-field>
            <btcl-input title="Port Count" :text.sync="application.portCount"></btcl-input>

            <btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
        <btcl-bounded v-for="(office,officeIndex) in application.officeList"
                      :title="'Office '+(officeIndex+1)"
                      :key="officeIndex">

            <btcl-input title="Office Name" :text.sync="office.name"></btcl-input>
            <btcl-input title="Office Address" :text.sync="office.address"></btcl-input>
            <div align="right" v-if="officeIndex!=0">
            </div>
        </btcl-bounded>
    </btcl-portlet>
    <btcl-portlet v-else>
        <%--<btcl-field title="Client"><lli-client-search :client.sync="application.client">Client</lli-client-search></btcl-field>--%>

        <div class="form-group">
            <div class=row>
                <label class="col-sm-4 control-label" >{{Object.keys(application.Comments)[0]}}</label>
                <div class=col-sm-6>
                    <p class="form-control">{{Object.values(application.Comments)[0]}}</p>
                </div>
            </div>
        </div>

        <%--<btcl-field title="Connection">
            <multiselect v-model="application.selectedConnection"
                         :options="nixConnections"
                         :track-by=ID
                         label=label
                         :allow-empty="false"
                         :searchable=true
                         open-direction="bottom"
                         @select="connectionSelectionCallback">
            </multiselect>
        </btcl-field>

        <btcl-field title="Office">
            <multiselect v-model="application.selectedOffice"
                         :options="nixOffices"
                         :track-by=ID
                         label=label
                         :allow-empty="false"
                         :searchable=true
                         open-direction="bottom"
                         @select="officeSelectionCallback">
            </multiselect>
        </btcl-field>

        &lt;%&ndash;todo a selection call back method for new port type selection&ndash;%&gt;
        <btcl-field title="Existing Ports">
            <multiselect v-model="application.selectedOldPort"
                         :options="nixExistingPorts"
                         :track-by=ID
                         label=label
                         :allow-empty="false"
                         :searchable=true
                         open-direction="bottom"
                         @select="portSelectionCallback">
            </multiselect>
        </btcl-field>--%>

        <btcl-info title="Existing Port Type" :text.sync="application.existingPortType">
        </btcl-info>

        <btcl-field title="New Port Type">
            <multiselect v-model="application.selectedNewPort"
                         :options="portTypeDropDown"
                         track-by=ID
                         label=label
                         :allow-empty="false"
                         :searchable=true
                         open-direction="bottom">
            </multiselect>
        </btcl-field>
<%--        <btcl-field v-if="application.applicationType.ID == 2" title="Loop Provider">--%>
<%--            <multiselect v-model="application.loopProvider"--%>
<%--                         :options="loopProviderList"--%>
<%--                         label="label" track-by="label"  open-direction="bottom">--%>
<%--            </multiselect>--%>
<%--        </btcl-field>--%>

<%--        <btcl-field title="Zone">--%>
<%--            <multiselect v-model="application.zone" :options="zoneList" placeholder="Zone"--%>
<%--                         label="nameEng" track-by="nameEng" open-direction="bottom"></multiselect>--%>
<%--        </btcl-field>--%>

        <btcl-field title="Suggested Date">
            <btcl-datepicker :date.sync="application.suggestedDate"
                             pattern="DD-MM-YYYY"></btcl-datepicker>
        </btcl-field>
        <btcl-input title="Comment" :text.sync="application.comment"></btcl-input>

    </btcl-portlet>

</div>
