let vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {},
		content: {},
		activeFrom: 0,
		loading : false,
	},
	mounted() {
		this.loading = true;
		Promise.resolve(
		axios({ method: 'GET', 'url': context + 'lli/application/new-connection-get.do?id=' + applicationID})
		.then(result => {
			this.application = result.data.payload;
			this.content = JSON.parse(result.data.payload.content);
			this.activeFrom = this.application.suggestedDate;
		}, error => {
		})).then(()=>this.loading = false);
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
		},
		activeFrom: function(val){
			this.content.sourceConnection.activeFrom = val;
			this.content.destinationConnection.activeFrom = val;
		}
	}
});