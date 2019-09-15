var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {},
		content: {},
	},
	mounted() {
		axios({ method: 'GET', 'url': context + 'lli/application/new-connection-get.do?id=' + applicationID})
		.then(result => {
			this.application = result.data.payload.members;
			this.content = JSON.parse(result.data.payload.content);
		}, error => {
		});
	},
	methods: {
		submitCallback: function(result){
			if(result.data.responseCode == 2) {
				toastr.error(result.data.msg);
			}else if(result.data.responseCode == 1){
				toastr.success("Your request has been processed", "Success");
				window.location.href = context + 'lli/application/search.do';
			}
			else{
				toastr.error("Your request was not accepted", "Failure");
			}
		}
	},
	watch: {
		content: {
			deep: true,
			handler(value){
				this.application.content = JSON.stringify(value);
			}
		}
	}
});