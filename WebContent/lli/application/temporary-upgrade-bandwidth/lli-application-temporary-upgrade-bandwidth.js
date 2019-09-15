var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		connectionOptionListByClientID: [],
		application: {
		}
	},
	methods: {
		clientSelectionCallback: function(clientID){
			if(clientID === undefined){
				this.connectionOptionListByClientID = [];
				this.application.connection = undefined;
			} else{
				axios({ method: 'GET', 'url': context + 'lli/connection/get-connection-by-client.do?id=' + clientID})
				.then(result => {
					this.connectionOptionListByClientID = result.data.payload;
				}, error => {
				});
			}
		},
		goView: function(result){
			if(result.data.responseCode == 2) {
				toastr.error(result.data.msg);
			}else if(result.data.responseCode == 1){
				toastr.success("Your request has been processed", "Success");
				window.location.href = context + 'lli/application/view.do?id=' + result.data.payload;
			}
			else{
				toastr.error("Your request was not accepted", "Failure");
			}
		}
	}
});