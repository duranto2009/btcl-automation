let vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		connectionOptionListByClientID: [],
		existingConnection: {},
		application: {
			client:{},
		},
		loading:false,
	},
	methods: {
		clientSelectionCallback: function(clientID){
			if(clientID === undefined){
				this.connectionOptionListByClientID = [];
				this.application.connection = undefined;
			} else{
				axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
				.then(result => {
					this.connectionOptionListByClientID = result.data.payload;
				}, error => {
				});
			}
		},
		getConnectionByID: function(selectedOption, id){
			this.existingConnection = {};
			axios({ method: 'GET', 'url': context + 'lli/connection/revise-connection-json.do?id=' + selectedOption.ID})
			.then(result => {
				this.existingConnection = result.data.payload;
			}, error => {
			});
		},
		goView: function(result){
			if(result.data.responseCode == 2) {
				toastr.error(result.data.msg);
			}else if(result.data.responseCode == 1){
				toastr.success("Your request has been processed", "Success");
                window.location.href = context + 'lli/application/search.do';
                // window.location.href = context + 'lli/application/view.do?id=' + result.data.payload;
			}
			else{
				toastr.error("Your request was not accepted", "Failure");
			}
		},

		validatebw: function(){
			if(this.application.bandwidth == null){
				toastr.error("Provide bandwidth", "Failure"); return false;

			}
			if(this.application.bandwidth < 0){
				toastr.error("Bandwidth must be positive", "Failure"); return false;

			}
			return true;
		}

	},
	mounted() {
		this.loading = false;
	},
	created() {
		this.loading = true;
	}
});