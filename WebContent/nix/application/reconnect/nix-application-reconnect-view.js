var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {}
	},
	mounted() {
		axios({ method: 'GET', 'url': context + 'nix/application/new-connection-get.do?id=' + applicationID})
		.then(result => {
			this.application = result.data.payload;
		}, error => {
		});
	}
});