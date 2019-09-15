var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {},
		connection: {}
	},
	mounted() {
		axios({ method: 'GET', 'url': context + 'lli/application/new-connection-get.do?id=' + applicationID})
		.then(result => {
			this.application = result.data.payload;
			
			axios({ method: "GET", "url": context + "lli/connection/view-connection-json.do?id="+ this.application.connection.ID })
			.then(result => {
	            this.connection = result.data.payload;
	        }, error => {
	        });
			
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
});