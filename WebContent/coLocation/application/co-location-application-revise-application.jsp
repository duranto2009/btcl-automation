<%@ page import="coLocation.CoLocationConstants" %>
<%@ page import="common.ModuleConstants" %>
<div id=btcl-co-location-newapplication>
    <btcl-body title="Co-Location" subtitle='Revise Application' v-cloak="true">
        <btcl-portlet>
            <btcl-field title="Client">
                <user-search-by-module :client.sync="newapplication.client" :module="moduleId"
                                       :callback="getConnectionByClientId">
                </user-search-by-module>
            </btcl-field>
            <template v-if="newapplication.client!==null">

                <btcl-field title="Connection">
                    <multiselect v-model="oldapplication" :options="connectionOptionListByClientID" label=name
                                 :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>
                </btcl-field>

                <template v-if="oldapplication!=null">
                    <%--<template>--%>
                    <%--<btcl-field title="Connection Type">--%>

                    <%--<multiselect placeholder="Connection Type" v-model="newapplication.connectionType" :options="connectionTypeList" label=value :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>--%>
                    <btcl-info title="Existing Connection Type"
                               :text="oldapplication.conTypeDescription.value"></btcl-info>

                    <%--</btcl-field>--%>
                    <%--<btcl-field title="Rack" v-if="oldapplication.rackNeeded == false">--%>
                    <%--<label class="radio-inline"><input type="radio" name="rackRadio" :value="true" checked v-model="newapplication.rackNeeded">Yes</label>--%>
                    <%--<label class="radio-inline"><input type="radio" name="rackRadio" :value="false" v-model="newapplication.rackNeeded">No</label>--%>
                    <%--</btcl-field>--%>
                    <template v-if="newapplication.rackNeeded">
                        <btcl-info title="Existing Rack Size" :text="oldapplication.rackSizeDescription"></btcl-info>
                        <%--<btcl-field title="Rack Size required to upgrade by">--%>
                        <%--<mounted-id-value-selection :model.sync="newapplication.rackSize" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SIZE%>'></mounted-id-value-selection>--%>
                        <%--</btcl-field>--%>
                        <btcl-info title="Existing Rack Space" :text="oldapplication.rackSpaceDescription"></btcl-info>
                        <btcl-field title="Change Rack Space to">


                            <%--<mounted-id-value-selection :model.sync="newapplication.rackSpace" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE%>'></mounted-id-value-selection>--%>


                            <multiselect v-model="newapplication.rackSpace" :options="rackSpaceList" label=description
                                         :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>

                        </btcl-field>
                    </template>
                    <btcl-field title="Power" v-if="oldapplication.powerNeeded == false">
                        <label class="radio-inline"><input type="radio" name="powerRadio" :value="true" checked
                                                           v-model="newapplication.powerNeeded">Yes</label>
                        <label class="radio-inline"><input type="radio" name="powerRadio" :value="false"
                                                           v-model="newapplication.powerNeeded">No</label>
                    </btcl-field>
                    <template v-if="newapplication.powerNeeded">
                        <btcl-info v-if="oldapplication.powerNeeded == true" title="Existing Power Type"
                                   :text="oldapplication.powerTypeDescription">

                        </btcl-info>
                        <%--<btcl-field title="Power Type required to upgrade by">--%>
                        <%--<multiselect v-model="newapplication.powerType" :options="powerTypeList" label=value :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>--%>
                        <%--</btcl-field>--%>
                        <btcl-info v-if="oldapplication.powerNeeded == true" title="Existing Power Supply"
                                   :text="oldapplication.powerAmount">

                        </btcl-info>
                        <%--<btcl-field title="Change Power Supply to">--%>
                        <%--<mounted-id-value-selection :model.sync="newapplication.powerAmount" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_POWER_AMOUNT%>'></mounted-id-value-selection>--%>

                        <btcl-field v-if="oldapplication.powerNeeded == false" title="Power Type" required="true">
                            <mounted-id-value-selection :model.sync="newapplication.powerType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_POWER_TYPE%>'>

                            </mounted-id-value-selection>
                        </btcl-field>

<%--                        <btcl-info v-if="oldapplication.powerNeeded == true" title="Existing Power Supply"--%>
<%--                                   :text="oldapplication.powerAmount">--%>

<%--                        </btcl-info>--%>
                        <btcl-input title="Change Power Supply to" :text.sync="newapplication.powerAmount"
                                    :number="true" placeholder="Enter New Power Amount."></btcl-input>
                        <%--</btcl-field>--%>
                    </template>
                    <btcl-field title="Fiber" v-if="oldapplication.fiberNeeded == false">
                        <label class="radio-inline"><input type="radio" name="fiberRadio" :value="true" checked
                                                           v-model="newapplication.fiberNeeded">Yes</label>
                        <label class="radio-inline"><input type="radio" name="fiberRadio" :value="false"
                                                           v-model="newapplication.fiberNeeded">No</label>
                    </btcl-field>
                    <template v-if="newapplication.fiberNeeded">
                        <btcl-info  v-if="oldapplication.fiberNeeded == true" title="Existing Fiber Type" :text="oldapplication.ofcTypeDescription"></btcl-info>
                        <%--<btcl-field title="Fiber Type required to upgrade by">--%>
                        <%--<mounted-id-value-selection :model.sync="newapplication.fiberType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FIBER_TYPE%>'></mounted-id-value-selection>--%>
                        <%--</btcl-field>--%>
                        <btcl-info v-if="oldapplication.fiberNeeded == true " title="Existing No. of Fiber Core" :text="oldapplication.fiberCore"></btcl-info>
                        <btcl-field v-if="oldapplication.fiberNeeded == false"  title="Fiber Type required to upgrade by">
                        <mounted-id-value-selection :model.sync="newapplication.fiberType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FIBER_TYPE%>'></mounted-id-value-selection>
                        </btcl-field>


                        <btcl-input title="Change No. of Fiber Core to" :text.sync="newapplication.fiberCore"
                                    :number="true" placeholder="Enter New No. of Fiber Core."></btcl-input>
                    </template>


                    <!--

                        <btcl-field title="Floor Space">
                            <label class="radio-inline"><input type="radio" name="floorSpaceRadio" :value="true" checked v-model="application.floorSpaceNeeded">Yes</label>
                            <label class="radio-inline"><input type="radio" name="floorSpaceRadio" :value="false" v-model="application.floorSpaceNeeded">No</label>
                        </btcl-field>
                        <template v-if="application.floorSpaceNeeded">
                            <btcl-field title="Floor Space Type">
                                <mounted-id-value-selection :model.sync="application.floorSpaceType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE%>'></mounted-id-value-selection>
                            </btcl-field>
                            <btcl-input title="Floor Space Amount(ft)" :text.sync="application.floorSpaceAmount" :number="true" placeholder="Enter Floor Space Amount."></btcl-input>
                        </template>


                        -->

                        <btcl-field title="Floor Space" v-if="oldapplication.floorSpaceNeeded == false">
                            <label class="radio-inline"><input type="radio" name="floorSpaceRadio" :value="true" checked
                                                               v-model="newapplication.floorSpaceNeeded">Yes</label>
                            <label class="radio-inline"><input type="radio" name="floorSpaceRadio" :value="false"
                                                               v-model="newapplication.floorSpaceNeeded">No</label>
                        </btcl-field>
                        <template v-if="newapplication.floorSpaceNeeded">
<%--                            <btcl-info  v-if="newapplication.floorSpaceNeeded == true" title="Existing Floor Space Type"--%>
<%--                                        :text="newapplication.floorSpaceTypeDescription"></btcl-info>--%>
                            <btcl-field title="Floor Space Type" required="true">
                                <mounted-id-value-selection :model.sync="newapplication.floorSpaceType" url='co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_FLOOR_SPACE%>'></mounted-id-value-selection>
                            </btcl-field>
                            <btcl-info v-if="oldapplication.floorSpaceNeeded == true " title="Existing Floor Space(ft.)"
                                       :text="newapplication.floorSpaceAmount"></btcl-info>
                            <btcl-input v-if="oldapplication.floorSpaceNeeded == false "
                                        title="Change Floor Space Amount(ft.) to"
                                        :text.sync="newapplication.floorSpaceAmount" :number="true"
                                        placeholder="Enter Floor Space Amount."></btcl-input>


<%--                            <btcl-input title="Change No. of Fiber Core to" :text.sync="newapplication.fiberCore"--%>
<%--                                        :number="true" placeholder="Enter New No. of Fiber Core."></btcl-input>--%>
                        </template>





<%--                    <template v-if="newapplication.floorSpaceNeeded">--%>
<%--                        <btcl-info title="Existing Floor Space Type"--%>
<%--                                   :text="oldapplication.floorSpaceTypeDescription"></btcl-info>--%>
<%--                        <btcl-info title="Existing Floor Space(ft.)"--%>
<%--                                   :text="oldapplication.floorSpaceAmount"></btcl-info>--%>
<%--                        <btcl-input title="Change Floor Space Amount(ft.) to"--%>
<%--                                    :text.sync="newapplication.floorSpaceAmount" :number="true"--%>
<%--                                    placeholder="Enter Floor Space Amount."></btcl-input>--%>
<%--                    </template>--%>


                    <btcl-input title="Description" :text.sync="newapplication.description"
                                placeholder="Write Description."></btcl-input>

                    <btcl-input title="Comment" :text.sync="newapplication.comment"
                                placeholder="Write your comment here."></btcl-input>
                    <btcl-field title="Suggested Date">
                        <btcl-datepicker :date.sync="newapplication.suggestedDate"
                                         pattern="DD-MM-YYYY"></btcl-datepicker>
                    </btcl-field>
                    <btcl-field title="Connection Details" :vertical="true">
                        <%--<btcl-datepicker :date.sync="newapplication.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>--%>
                        <a class="form-control" v-on:click="showConnectionDetails">Click Here</a>
                    </btcl-field>
                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitNewConnection">Submit
                    </button>


                    <div class="modal fade" id="connectionDetailsModal" role="dialog">
                        <div class="modal-dialog modal-lg" style="width: 50%;">

                            <!-- Modal local loop selection-->
                            <div class="modal-content">
                                <div class="modal-body">
                                    <div>
                                        <div class="form-body">

                                            <%--<div class="form-group">--%>
                                            <%--<div class="row">--%>
                                            <%--<label class="control-label col-md-3" style="text-align: center;">Connection Name</label>--%>
                                            <%--<div v-if="application.connection" class="col-md-9">--%>
                                            <%--&lt;%&ndash;<input type="text"&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;v-model="application.connectionName"&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;placeholder="Provide a Connection Name"&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;class="form-control input-lg">&ndash;%&gt;--%>
                                            <%--<p>{{application.connection.name}}</p>--%>
                                            <%--</div>--%>
                                            <%--<div v-else class="col-md-9"><input type="text"--%>
                                            <%--v-model="application.connectionName"--%>
                                            <%--placeholder="Provide a Connection Name"--%>
                                            <%--class="form-control input-lg">--%>
                                            <%--</div>--%>
                                            <%--</div>--%>
                                            <%--</div>--%>


                                            <%--<div class="form-group">--%>
                                            <%--<div class="row">--%>
                                            <%--<label class="control-label col-md-3" style="text-align: center;">Comment</label>--%>
                                            <%--<div class="col-md-9"><input type="text" v-model="comment"--%>
                                            <%--placeholder="Comment Here"--%>
                                            <%--class="form-control input-lg">--%>
                                            <%--</div>--%>
                                            <%--</div>--%>
                                            <%--</div>--%>
                                            <btcl-portlet title=Connection>
                                                <btcl-info title="Client" :text=connection.client.value></btcl-info>
                                                <btcl-info title="Connection Name" :text=connection.name></btcl-info>
                                                <btcl-info title="Connection Type"
                                                           :text="connection.conTypeDescription.value"></btcl-info>
                                                <btcl-info title="Active From" :text="connection.activeFrom"
                                                           v-bind:is-date="true"></btcl-info>
                                                <btcl-info title="Discount Rate"
                                                           :text="connection.discountRate"></btcl-info>
                                                <%--<btcl-info title="Status" :text="connection.status.label"></btcl-info>--%>
                                            </btcl-portlet>
                                            <btcl-portlet title="Inventory Information">
                                                <btcl-bounded title="">
                                                    <table class="table">
                                                        <tr style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                                                            <th><p style="text-align: center;">Name</p></th>
                                                            <th><p style="text-align: center;">Model</p></th>
                                                            <%--<th ><p style="text-align: center;">Total Amount</p></th>--%>
                                                            <th><p style="text-align: center;">Type</p></th>
                                                            <th><p style="text-align: center;">Amount</p></th>
                                                            <%--<th ><p style="text-align: center;"></p></th>--%>
                                                        </tr>
                                                        <template
                                                                v-for="(inventory, index) in connection.modifiedCoLocationInventoryDTOList">
                                                            <tr>
                                                                <td>
                                                                    <p style="text-align: center;border: 1px solid;padding: 10px;">
                                                                        {{(inventory.name === "" ? "N/A" :
                                                                        inventory.name )}}</p>
                                                                </td>
                                                                <td>
                                                                    <p style="text-align: center;border: 1px solid;padding: 10px;">
                                                                        {{inventory.model}}</p></td>
                                                                <td>
                                                                    <p style="text-align: center;border: 1px solid;padding: 10px;">
                                                                        <%--{{new Date(parseInt(ip.activeFrom)).toLocaleDateString("ca-ES")}}--%>
                                                                        {{inventory.type}}
                                                                    </p></td>
                                                                <td>
                                                                    <p style="text-align: center;border: 1px solid;padding: 10px;">
                                                                        {{inventory.amount}}</p></td>
                                                                <%--<td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.isUsed}}</p></td>--%>
                                                                <%--<td ><p style="text-align: center;border: 1px solid;padding: 10px;"></p></td>--%>
                                                            </tr>
                                                        </template>

                                                        <%--<template v-if="connection.powerNeeded"></template>--%>
                                                        <%--<template v-if="connection.fiberNeeded"></template>--%>
                                                        <%--<template v-if="connection.rackNeeded"></template>--%>


                                                    </table>
                                                </btcl-bounded>
                                            </btcl-portlet>

                                        </div>
                                    </div>


                                </div>
                                <div class="modal-footer">
                                    <%--<button type=button class="btn btn-success" @click="processRequestForEFR('new-connection-process')">Save</button>--%>
                                    <%--<button type=button class="btn btn-success"--%>
                                    <%--@click="connectionCompletionModal">--%>
                                    <%--Submit--%>
                                    <%--</button>--%>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                                </div>
                            </div>

                        </div>
                    </div>


                </template>


            </template>
        </btcl-portlet>
    </btcl-body>


</div>
<script>
    var historyID = "<%=request.getParameter("historyID")%>";
    var vue = new Vue({
        el: "#btcl-co-location-newapplication",
        data: {
            connectionTypeList: [{id: 1, value: 'Regular'}, {id: 2, value: 'Special Connection'}],
            powerTypeList: [{id: 1, value: 'AC'}, {id: 2, value: 'DC'}],
            rackSpaceList: [],
            newapplication: {
                client: null,
                connectionType: null,
                rackNeeded: true,
                rackSize: null,
                rackSpace: null,
                powerNeeded: true,
                powerAmount: 0,
                powerType: null,
                fiberNeeded: true,
                floorSpaceNeeded: true,
                fiberType: null,
                floorSpaceType: null,
                floorSpaceAmount: 0,
                fiberCore: 0,
                comment: null,
                suggestedDate: null,
                description: null,
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

            connection: {
                client: {},
                modifiedCoLocationInventoryDTOList: null,
            },
            historyList: [],
        },
        methods: {
            submitNewConnection: function () {
                // start: form validation
                if (this.newapplication.client == null) {
                    errorMessage("Client must be selected.");
                    return;
                }
                if (this.newapplication.suggestedDate == null) {
                    errorMessage("Provide a suggested date.");
                    return;
                }

                if (this.newapplication.suggestedDate == Date.now()) {
                    errorMessage("Suggested Date Invalid.");
                    return;
                }
                // end: form validation'
                this.newapplication.fiberCore = parseInt(this.newapplication.fiberCore);
                this.newapplication.client.ID = this.newapplication.client.key;

                //set application id
                this.newapplication.connectionID = this.oldapplication.ID;
                this.newapplication.connectionType = this.oldapplication.conTypeDescription;

                let url = "revise-connection-application";
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
                        } else {
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
            createModifiedCoLocationInventoryDTOList: function () {
                // debugger;
                this.connection.modifiedCoLocationInventoryDTOList = this.connection.coLocationInventoryDTOList.map(function (data) {
                    if (data.templateID == vue.connection.powerType) {
                        data['type'] = vue.connection.powerTypeDescription;
                        data['amount'] = vue.connection.powerAmount;
                    } else if (data.templateID == vue.connection.rackSize) {
                        data['type'] = vue.connection.rackSizeDescription;
                        data['amount'] = vue.connection.rackSpaceDescription;
                    } else if (data.templateID == vue.connection.fiberType) {
                        data['type'] = vue.connection.ofcTypeDescription;
                        data['amount'] = vue.connection.fiberCore;
                    } else if (data.templateID == vue.connection.floorSpaceType) {
                        data['type'] = vue.connection.floorSpaceTypeDescription;
                        data['amount'] = vue.connection.floorSpaceAmount;
                    }
                    return data;
                });
            },
            showConnectionDetails: function () {
                // alert('called');
                $('#connectionDetailsModal').modal({show: true});
            }
        },
        created() {
        },
        mounted() {
            axios({
                method: 'GET',
                'url': context + 'co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE%>'
            }).then(result => {
                this.rackSpaceList = result.data.payload;
            }, error => {
            });
        },
        watch: {
            oldapplication: function (val) {
                //setup new application with old application value
                Object.assign(this.newapplication, val);
                Object.assign(this.connection, val);

                // for rackspace multiselect list need to make rackspace object
                var x = this.rackSpaceList.find(x => x.id == this.newapplication.rackSpace);
                this.newapplication.rackSpace = x;
                // this.newapplication.powerAmount = 0;
                // this.newapplication.fiberCore = 0;
                // this.newapplication.rackSpace = 0;


                //for connection details
                axios({
                    method: "GET",
                    "url": context + "co-location/history-list-json.do?id=" + this.newapplication.historyID
                }).then(result => {
                    this.historyList = result.data.payload;
                    if (this.historyList.length > 0) {
                        this.connection = this.historyList[0];
                        this.createModifiedCoLocationInventoryDTOList();

                    }
                }, error => {
                });

            }

        },
    });
</script>