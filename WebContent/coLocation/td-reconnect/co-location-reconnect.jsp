<%@ page import="coLocation.CoLocationConstants" %>
<%@ page import="common.ModuleConstants" %>
<div id=btcl-co-location-newapplication>
    <btcl-body title="Co-Location" subtitle='Reconnect Connection' v-cloak="true">
        <btcl-portlet>
            <btcl-field title="Client">
                <user-search-by-module :client.sync="newapplication.client" :module="moduleId" :callback="getConnectionByClientId">
                </user-search-by-module>
            </btcl-field>
            <template v-if="newapplication.client!==null">

                <btcl-field title="Connection" v-if="connectionOptionListByClientID.length>0">
                    <multiselect v-model="oldapplication" :options="connectionOptionListByClientID" label=name :allow-empty="false" :searchable=true open-direction="bottom"></multiselect>
                </btcl-field>
                <template v-if="oldapplication!=null">
                    <btcl-info title="Existing Connection Type" :text="oldapplication.conTypeDescription.value"></btcl-info>
                    <template v-if="newapplication.rackNeeded">
                        <btcl-info title="Existing Rack Size" :text="oldapplication.rackSizeDescription"></btcl-info>
                        <btcl-info title="Existing Rack Space" :text="oldapplication.rackSpaceDescription"></btcl-info>
                    </template>
                    <btcl-field title="Power" v-if="oldapplication.powerNeeded == false">
                        <label class="radio-inline"><input type="radio" name="powerRadio" :value="true" checked v-model="newapplication.powerNeeded">Yes</label>
                        <label class="radio-inline"><input type="radio" name="powerRadio" :value="false" v-model="newapplication.powerNeeded">No</label>
                    </btcl-field>
                    <template v-if="newapplication.powerNeeded">
                        <btcl-info title="Existing Power Type" :text="oldapplication.powerTypeDescription"></btcl-info>
                        <btcl-info title="Existing Power Supply" :text="oldapplication.powerAmount"></btcl-info>
                    </template>
                    <btcl-field title="Fiber" v-if="oldapplication.fiberNeeded == false">
                        <label class="radio-inline"><input type="radio" name="fiberRadio" :value="true" checked v-model="newapplication.fiberNeeded">Yes</label>
                        <label class="radio-inline"><input type="radio" name="fiberRadio" :value="false" v-model="newapplication.fiberNeeded">No</label>
                    </btcl-field>
                    <template v-if="newapplication.fiberNeeded">
                        <btcl-info title="Existing Fiber Type" :text="oldapplication.ofcTypeDescription"></btcl-info>
                        <btcl-info title="Existing Fiber Core" :text="oldapplication.fiberCore"></btcl-info>
                    </template>
                    <btcl-input :textarea="true" title="Description" :text.sync="newapplication.description" placeholder="Write Description."></btcl-input>

                    <btcl-input :textarea="true" title="Comment" :text.sync="newapplication.comment" placeholder="Write your comment here."></btcl-input>
                    <btcl-field title="Suggested Date">
                        <btcl-datepicker :date.sync="newapplication.suggestedDate" pattern="DD-MM-YYYY"></btcl-datepicker>
                    </btcl-field>
                    <btcl-field title="Connection Details" :vertical="true">
                        <a class="form-control" v-on:click="showConnectionDetails">Click Here</a>
                    </btcl-field>
                    <button type=button class="btn green-haze btn-block btn-lg" @click="submitData">Submit
                    </button>
                    <div class="modal fade" id="connectionDetailsModal" role="dialog">
                        <div class="modal-dialog modal-lg" style="width: 50%;">
                            <div class="modal-content">
                                <div class="modal-body">
                                    <div>
                                        <div class="form-body">
                                            <btcl-portlet title=Connection>
                                                <btcl-info title="Client" :text=connection.client.value></btcl-info>
                                                <btcl-info title="Connection Name" :text=connection.name></btcl-info>
                                                <btcl-info title="Connection Type" :text="connection.conTypeDescription.value"></btcl-info>
                                                <btcl-info title="Active From" :text="connection.activeFrom" v-bind:is-date="true"></btcl-info>
                                                <btcl-info title="Discount Rate" :text="connection.discountRate"></btcl-info>
                                            </btcl-portlet>
                                            <btcl-portlet title="Inventory Information">
                                                <btcl-bounded title="">
                                                    <table class="table">
                                                        <tr  style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                                                            <th ><p style="text-align: center;">Name</p></th>
                                                            <th ><p style="text-align: center;">Model</p></th>
                                                            <th ><p style="text-align: center;">Type</p></th>
                                                            <th ><p style="text-align: center;">Amount</p></th>
                                                        </tr>
                                                        <template v-for="(inventory, index) in connection.modifiedCoLocationInventoryDTOList">
                                                            <tr >
                                                                <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.name}}</p></td>
                                                                <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.model}}</p></td>
                                                                <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.type}}</p></td>
                                                                <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{inventory.amount}}</p></td>
                                                            </tr>
                                                        </template>
                                                    </table>
                                                </btcl-bounded>
                                            </btcl-portlet>

                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
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
                fiberType: null,
                fiberCore: 0,
                comment: null,
                suggestedDate: null,
                description: null,
            },
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
            submitData: function () {
                this.newapplication['connection_id'] = this.newapplication.ID;
                axios.post(context + "co-location/reconnect.do", {
                    // application: this.revise
                    'application': JSON.stringify(this.newapplication)
                })
                    .then(function(response){
                        if(response.data.responseCode == 2) {
                            toastr.error(response.data.msg); // application not successful
                        }else {
                            toastr.success(response.data.msg);
                            window.location.href = context + 'co-location/search.do';
                        }
                    })
                    .catch(function(error){
                        console.log(error);
                    });
            },
            getConnectionByClientId: function (clientID) {
                if (clientID === undefined) {
                    this.connectionOptionListByClientID = [];
                    this.connection = undefined;
                } else {
                    return axios.get(context + 'co-location/get-reconnect-connection-list-by-client.do?clientID=' + clientID)
                        .then(result => {
                            if(result.data.responseCode === 2) {
                                toastr.options.timeOut = 2500;
                                toastr.error(result.data.msg);
                                this.connectionOptionListByClientID=[];
                                this.oldapplication = null;
                            }
                            else {
                                this.connectionOptionListByClientID = result.data.payload;
                            }

                        }).catch(error => {
                        console.log(error)
                    });
                }
            },
            createModifiedCoLocationInventoryDTOList: function(){
                // debugger;
                this.connection.modifiedCoLocationInventoryDTOList = this.connection.coLocationInventoryDTOList.map(function(data){
                    if(data.templateID == vue.connection.powerType){
                        data['type'] = vue.connection.powerTypeDescription;
                        data['amount'] = vue.connection.powerAmount;
                    }
                    else if(data.templateID == vue.connection.rackSize){
                        data['type'] = vue.connection.rackSizeDescription;
                        data['amount'] = vue.connection.rackSpaceDescription;
                    }
                    else if(data.templateID == vue.connection.fiberType){
                        data['type'] = vue.connection.ofcTypeDescription;
                        data['amount'] = vue.connection.fiberCore;
                    }
                    return data;
                });
            },
            showConnectionDetails: function(){
                // alert('called');
                $('#connectionDetailsModal').modal({show: true});
            }
        },
        created() {
        },
        mounted(){
            axios({method: 'GET', 'url': context + 'co-location/get-inventory-template.do?id=<%=CoLocationConstants.INVENTORY_CATEGORY_RACK_SPACE%>'}).then(result => {this.rackSpaceList = result.data.payload;}, error => {});
        },
        watch: {
            oldapplication : function(val)
            {
                //setup new application with old application value
                Object.assign(this.newapplication,val);
                Object.assign(this.connection,val);

                // for rackspace multiselect list need to make rackspace object
                this.newapplication.rackSpace=this.rackSpaceList.find(x=>x.id==this.newapplication.rackSpace);
                //for connection details
                axios({
                    method: "GET",
                    "url": context + "co-location/history-list-json.do?id=" + this.newapplication.historyID
                }).then(result => {
                    this.historyList = result.data.payload;
                    if(this.historyList.length>0){
                        this.connection = this.historyList[0];
                        this.createModifiedCoLocationInventoryDTOList();
                    }
                }, error => {
                });

            }

        },
    });
</script>