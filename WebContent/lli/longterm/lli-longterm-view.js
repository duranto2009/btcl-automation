Vue.component('longterm-history', {
	template: `
		<div>
			<button type=button class="btn btn-default btn-block" v-for="(history, historyIndex) in this.historyList" @click=getContractByHistoryID(history.historyID)>
				<div align=left>
				<b>{{history.status.label}}</b>
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
		axios({ method: "GET", "url": context + "lli/longterm/get-contract-history-list-by-contract-id.do?id="+ id }).then(result => {
            this.historyList = result.data.payload;
        }, error => {
        });
	},
	methods: {
		getContractByHistoryID: function(historyID){
			axios({ method: "GET", "url": context + "lli/longterm/get-contract-by-history-id.do?id="+ historyID }).then(result => {
	            vue.contract = result.data.payload;
	        }, error => {
	        });
		}
	}
});


var vue = new Vue({
	el: "#btcl-application",
	data: {
		longTermContractID: id,
		contextPath: context,
		contract: {}
	},
	mounted() {
		axios({ method: 'GET', 'url': context + 'lli/longterm/get-longterm.do?id=' + this.longTermContractID})
		.then(result => {
			this.contract = result.data.payload;
		}, error => {
		});
	}
});