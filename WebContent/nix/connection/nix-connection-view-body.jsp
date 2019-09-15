<div id="btcl-application" v-cloak="true">
	<btcl-body :title="'NIX : '+connection.name">
		<div class="row">
                <div class="col-md-8">
                    <btcl-portlet title=Connection>
                        <btcl-info title="Client" :text=connection.client></btcl-info>
                        <btcl-info title="Connection Name" :text=connection.name></btcl-info>
                        <btcl-info title="Connection Status" :text="statusString"></btcl-info>
                        <btcl-info title="Active From" :text="connection.activeFrom" v-bind:is-date="true"></btcl-info>
                    </btcl-portlet>
                    <btcl-portlet title="Office Information">

                        <btcl-bounded :title="office.name" v-for="office in connection.nixOffices">
                            <btcl-info title="Address" :text=office.nixApplicationOffice.address></btcl-info>
                            <btcl-info title="Created at" :text=office.nixApplicationOffice.created :is-date="true"></btcl-info>
                            <h4>Loop Information</h4>
                            <table class="table">
                                <tr  style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
                                    <th ><p style="text-align: center;">POP Name</p></th>
                                    <th ><p style="text-align: center;">Port</p></th>
                                    <th ><p style="text-align: center;">Port Type</p></th>
                                    <th ><p style="text-align: center;">Route Switch Id</p></th>
                                    <th ><p style="text-align: center;">VLAN Id</p></th>
                                    <th ><p style="text-align: center;">BTCL Distance</p></th>
                                    <th ><p style="text-align: center;">Client Distance</p></th>
                                    <th ><p style="text-align: center;">OC Distance</p></th>
                                    <th ><p style="text-align: center;">OFC Type</p></th>
                                </tr>
                                <template v-for="(loop, index) in office.nixApplicationOffice.loops">
                                    <tr >
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{loop.popName}}</p></td>
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{loop.portName}}</p></td>
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{loop.portTypeString}}</p></td>
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{loop.switchName}}</p></td>
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{loop.vlanName}}</p></td>
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{loop.btclDistance}}</p></td>
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{loop.clientDistance}}</p></td>
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;">{{loop.ocdDistance}}</p></td>
                                        <td ><p style="text-align: center;border: 1px solid;padding: 10px;" v-if="loop.ofcType == 1">Single</p>
                                            <p style="text-align: center;border: 1px solid;padding: 10px;" v-else-if="loop.ofcType == 2">Double</p>
                                            <p style="text-align: center;border: 1px solid;padding: 10px;" v-else>N/A</p>
                                        </td>
                                    </tr>
                                </template>
                            </table>
                        </btcl-bounded>
                    </btcl-portlet>
			</div>
			<div class=col-md-4>
                <btcl-portlet title=History>
                    <div>
                        <button type="button" class="btn btn-default btn-block" v-for="(history, historyIndex) in this.connectionList" @click="getConnectionByHistoryID(historyIndex)">
                            <div align="left">
                                <b>{{getIncidentHistoryNameByID(history.incidentId)}}</b>
                                <br/>
                                {{new Date(history.activeFrom).toLocaleDateString("ca-ES")}}
                            </div>
                        </button>
                    </div>
                </btcl-portlet>
			</div>
		</div>
	</btcl-body>
	
</div>

<script>
	var requestID = "<%=request.getParameter("id")%>";
	if(requestID =="null") {window.location.href=context;}
</script>
<script>
    Vue.component('connection-history', {
        template: `
		<div>
			<button type=button class="btn btn-default btn-block" v-for="(history, historyIndex) in this.historyList" @click=getConnectionByHistoryID(history.historyID)>
				<div align=left>
				<b>{{history.incident.label}}</b>
				<br/>
				{{new Date(history.activeFrom).toLocaleDateString("ca-ES")}}
				</div>
			</button>
		</div>
	`,
        data: function(){
            return {
                historyList: []
            }
        },
        mounted() {
            axios({ method: "GET", "url": context + "lli/connection/history-list-json.do?id="+ requestID }).then(result => {
                this.historyList = result.data.payload;
            }, error => {
            });
        },
        methods: {
            getConnectionByHistoryID: function(historyID){
                axios({ method: "GET", "url": context + "lli/connection/connection-by-history-id.do?id="+ historyID }).then(result => {
                    vue.connection = result.data.payload;
                }, error => {
                });
            },
        }
    });

    var vue = new Vue({
        el: "#btcl-application",
        data: {
            connection: {
                name: "",
            },
            statusString:"",
            connectionList: null,
            ipList:'',
        },
        mounted() {
            axios({ method: "GET", "url": context + "nix/connection/get-connection-by-history-id.do?id="+ requestID }).then(result => {
                if(result.data.responseCode == 1){
                    this.connectionList = result.data.payload;
                    this.connection = this.connectionList[0];
                    if(this.connection.status == 1){
                        this.statusString ="Active";
                    }
                    else{
                        this.statusString = "Inactive"
                    }
                }
            }, error => {
            });

            // axios({ method: "GET", "url": context + "ip/connection/details.do?id="+ requestID }).then(result => {
            //     if(result.data.payload.hasOwnProperty("elements"))
            //         this.ipList = result.data.payload.elements;
            //     else this.ipList = result.data.payload;
            // }, error => {
            // });
        },
        methods: {
            getConnectionByHistoryID: function (id) {
                this.connection = this.connectionList[id];
                // this.createModifiedCoLocationInventoryDTOList();
            },
            getIncidentHistoryNameByID:function (id) {
                if(id == 1)return "New Connection";
                else if(id == 2) return "Upgrade";
                else if(id == 3) return "Downgrade";

            },
        }
    });
</script>


