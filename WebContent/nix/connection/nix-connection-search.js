Vue.component('search-nav', {
	data: function(){
		return {
			nav: [
				{name: "connectionID", title: "Connection ID"},
				{name: "connectionName", title: "Connection Name"},
				{name: "clientName", title: "Client Name"},
				{name: "connectionStatus", title: "Connection Status"}
			],
			form: {}
		}
	},
	template: `
	<div>
		<input type=text class=form-control v-model="this.form['nav[0].name']">
		{{this.form}}
	</div>
	`
});


var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context
	},
	mounted() {
		axios({ method: "GET", "url": context + "lli/connection/new-connection-json.do"}).then(result => {
        }, error => {
        });
	},
});