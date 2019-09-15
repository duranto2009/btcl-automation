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
		(async () => {
			await this.getHistoryList()
		} )().catch(err=>console.log(err));

	},
	methods: {
		getHistoryList() {
			return axios({ method: "GET", "url": context + "lli/connection/history-list-json.do?id="+ requestID }).then(result => {
				this.historyList = result.data.payload;
			}, error => {
			});
		},
		getConnectionByHistoryID: function(historyID){
			axios({ method: "GET", "url": context + "lli/connection/connection-by-history-id.do?id="+ historyID }).then(result => {
	            vue.connection = result.data.payload;
	        }, error => {
	        });
		}
	}
});

var vue = new Vue({
	el: "#btcl-application",
	data: {
		connection: {
			client: {

			},
			status : {

			},
			connectionType : {

			}
		},
		ipList:'',
		loading : false,
	},
	methods : {
		getConnectionData() {
			return axios({ method: "GET", "url": context + "lli/connection/view-connection-json.do?id="+ requestID }).then(result => {
				this.connection = result.data.payload;
			}, error => {
			});
		},
		getIPInfo() {
			return axios({ method: "GET", "url": context + "ip/connection/details.do?id="+ requestID }).then(result => {
				if(result.data.payload.hasOwnProperty("elements"))
					this.ipList = result.data.payload.elements;
				else this.ipList = result.data.payload;
			}, error => {
			});
		},


	},
	mounted() {
		this.loading = true;
		let makeAsyncCalls = async() => {
			await this.getConnectionData();
			await this.getIPInfo();
		};
		makeAsyncCalls()
			.then(()=>this.loading=false)
			.catch(err=>console.log(err));

	}
});