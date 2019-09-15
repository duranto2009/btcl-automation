var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {},
		loading : false,
	},
	mounted() {
		this.loading= true;
		Promise.resolve(
			axios({ method: 'GET', 'url': context + 'lli/application/new-connection-get.do?id=' + applicationID})
			.then(result => {
				this.application = result.data.payload;
			}, error => {
			})).then(()=>this.loading=false)
	}
});