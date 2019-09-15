<%@ page import="coLocation.CoLocationConstants" %>
<%@ page import="common.ModuleConstants" %>
<div id=btcl-co-location-newapplication>
    <btcl-body title="Co-Location" subtitle='Downgrade Application' v-cloak="true">
        <btcl-portlet>
            <btcl-field title="Client">
                <user-search-by-module :client.sync="newapplication.client" :module="moduleId" :callback="getConnectionByClientId">
                </user-search-by-module>
            </btcl-field>
            <template v-if="newapplication.client!==null">

                <btcl-field title="Connection">
                    <multiselect v-model="oldapplication" :options="connectionOptionListByClientID" label=name :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>
                </btcl-field>

                <template v-if="oldapplication!=null">
                    <%--<template>--%>
                    <%--<btcl-field title="Connection Type">--%>

                        <%--<multiselect placeholder="Connection Type" v-model="newapplication.connectionType" :options="connectionTypeList" label=value :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>--%>
                        <btcl-info title="Existing Connection Type" :text="oldapplication.conTypeDescription"></btcl-info>

                    <%--</btcl-field>--%>
                    <btcl-field title="Rack">
                        <label class="radio-inline"><input type="radio" name="rackRadio" :value="true" checked v-model="newapplication.rackNeeded">Yes</label>
                        <label class="radio-inline"><input type="radio" name="rackRadio" :value="false" v-model="newapplication.rackNeeded">No</label>
                    </btcl-field>
                    <template v-if="newapplication.rackNeeded">


                        <div class="form-group">
                            <div class=row>
                                <label class="col-sm-4 control-label" >Existing Rack Size</label>
                                <div class=col-sm-6>
                                    <p class="form-control">{{oldapplication.rackSizeDescription}} ({{oldapplication.rackSpaceDescription}})</p>
                                </div>
                            </div>
                        </div>



                        <%--<btcl-info title="Existing Rack Size" :text="oldapplication.rackSizeDescription"></btcl-info>--%>


                        <btcl-field title="Rack Size required to downgrade by">
                            <mounted-id-value-selection :model.sync="newapplication.rackSize" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SIZE%>'></mounted-id-value-selection>
                        </btcl-field>
                        <%--<btcl-info title="Existing Rack Space" :text="oldapplication.rackSpaceDescription"></btcl-info>--%>
                        <%--<btcl-field title="Rack Space required to downgrade by">--%>
                            <%--<mounted-id-value-selection :model.sync="newapplication.rackSpace" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE%>'></mounted-id-value-selection>--%>
                        <%--</btcl-field>--%>
                    </template>
                    <btcl-field title="Power">
                        <label class="radio-inline"><input type="radio" name="powerRadio" :value="true" checked v-model="newapplication.powerNeeded">Yes</label>
                        <label class="radio-inline"><input type="radio" name="powerRadio" :value="false" v-model="newapplication.powerNeeded">No</label>
                    </btcl-field>
                    <template v-if="newapplication.powerNeeded">
                        <btcl-info title="Existing Power Type" :text="oldapplication.powerTypeDescription"></btcl-info>
                        <btcl-field title="Power Type required to downgrade by">
                            <multiselect v-model="newapplication.powerType" :options="powerTypeList" label=value :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>
                        </btcl-field>
                        <btcl-info title="Existing Power Supply" :text="oldapplication.powerAmount"></btcl-info>
                        <%--<btcl-field title="Power Supply required to downgrade by">--%>
                            <%--<mounted-id-value-selection :model.sync="newapplication.powerAmount" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_POWER_AMOUNT%>'></mounted-id-value-selection>--%>
                        <%--</btcl-field>--%>

                        <btcl-input title="Power Supply required to downgrade by" :text.sync="newapplication.powerAmount" placeholder="Write your comment here." number="true"></btcl-input>
                    </template>
                    <btcl-field title="Fiber">
                        <label class="radio-inline"><input type="radio" name="fiberRadio" :value="true" checked v-model="newapplication.fiberNeeded">Yes</label>
                        <label class="radio-inline"><input type="radio" name="fiberRadio" :value="false" v-model="newapplication.fiberNeeded">No</label>
                    </btcl-field>
                    <template v-if="newapplication.fiberNeeded">
                        <btcl-info title="Existing Fiber Type" :text="oldapplication.ofcTypeDescription"></btcl-info>
                        <btcl-field title="Fiber Type required to downgrade by">
                            <mounted-id-value-selection :model.sync="newapplication.fiberType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FIBER_TYPE%>'></mounted-id-value-selection>
                        </btcl-field>
                        <btcl-info title="Existing Fiber Core" :text="oldapplication.fiberCore"></btcl-info>
                        <btcl-input title="Fiber Core required to downgrade by" :text.sync="newapplication.fiberCore" :number="true"></btcl-input>
                    </template>
                    <btcl-input title="Comment" :text.sync="newapplication.comment" placeholder="Write your comment here."></btcl-input>
                    <btcl-field title="Suggested Date">
                        <btcl-datepicker :date.sync="newapplication.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>
                    </btcl-field>
                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitNewConnection">Submit
                    </button>

                </template>


            </template>
        </btcl-portlet>
    </btcl-body>
</div>
<script>
    var vue = new Vue({
        el: "#btcl-co-location-newapplication",
        data: {
            connectionTypeList: [{id: 1, value: 'Regular'}, {id: 2, value: 'Special Connection'}],
            powerTypeList: [{id: 1, value: 'AC'}, {id: 2, value: 'DC'}],
            newapplication: {
                client: null,
                connectionType: null,
                rackNeeded: true,
                rackSize: null,
                rackSpace: null,
                powerNeeded: true,
                powerAmount: null,
                powerType: null,
                fiberNeeded: true,
                fiberType: null,
                fiberCore: 0,
                comment: null,
                suggestedDate: null,
            },
            // oldapplication: {
            //     rackSize: 0,
            //     rackSpace: 0,
            //     powerType: 0,
            //     powerSupply: 0,
            //     fiberType: 0,
            //     fiberCore: 0,
            // },
            moduleId: <%=ModuleConstants.Module_ID_COLOCATION%>,
            connectionOptionListByClientID: [],
            oldapplication: null,
        },
        watch: {
        },
        methods: {
            submitNewConnection: function () {
                // start: form validation
                if (this.newapplication.client == null) {
                    errorMessage("Client must be selected.");
                    return;
                }
                // if (this.newapplication.connectionType == null) {
                //     errorMessage("Connection Type must be selected.");
                //     return;
                // }
                if (this.newapplication.powerNeeded == false && this.newapplication.rackNeeded == false && this.newapplication.fiberNeeded == false) {
                    errorMessage("Power or Rack or Fiber shouldn't be empty.");
                    return;
                }
                if (this.newapplication.rackNeeded) {
                    if (this.newapplication.rackSize == null) {
                        errorMessage("Rack Size must be selected.");
                        return;
                    }
                    if (this.newapplication.rackSpace == null) {
                        errorMessage("Rack Space must be selected.");
                        return;
                    }
                }
                if (this.newapplication.powerNeeded) {
                    if (this.newapplication.powerType == null) {
                        errorMessage("Power Type must be selected.");
                        return;
                    }
                    if (this.newapplication.powerAmount == null) {
                        errorMessage("Power Supply must be selected.");
                        return;
                    }
                }
                if (this.newapplication.fiberNeeded) {
                    if (this.newapplication.fiberType == null) {
                        errorMessage("OFC Type must be selected.");
                        return;
                    }
                }
                if (this.newapplication.suggestedDate == null) {
                    errorMessage("Provide a suggested date.");
                    return;
                }
                // end: form validation'
                this.newapplication.fiberCore = parseInt(this.newapplication.fiberCore);
                this.newapplication.client.ID = this.newapplication.client.key;

                //set application id
                this.newapplication.connectionID =  this.oldapplication.ID;
                this.newapplication.connectionType =  this.oldapplication.connectionType;

                let url = "downgrade-connection-application";
                axios.post(context + 'co-location/' + url + '.do', {'application': JSON.stringify(this.newapplication)})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            toastr.options.timeOut = 2500;
                        } else if (result.data.responseCode == 1) {
                            toastr.success("Your request has been processed", "Success");
                            toastr.options.timeOut = 2500;
                            //redirect to specific page
                            window.location.href = context + 'co-location/search.do';
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                            toastr.options.timeOut = 2500;
                        }
                    })
                    .catch(function (error) {
                    });
            },
            getConnectionByClientId: function (clientID) {
                if (clientID === undefined) {
                    this.connectionOptionListByClientID = [];
                    this.connection = undefined;
                } else {
                   return axios.get(context + 'co-location/get-connection-by-client.do?clientID=' + clientID)
                        .then(result => {
                            this.connectionOptionListByClientID = result.data.payload;
                        }).catch(error => {
                        console.log(error)
                    });
                }
            },
        },
        created() {
            axios({method: 'GET', 'url': context + this.url}).then(result => {this.modelOptions = result.data.payload;}, error => {});
        },
    });
</script>