let vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		connectionOptionListByClientID: [],
		application: {
		},
        existingConnection: {},
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
				window.location.href = context + 'lli/application/search.do';
			}
			else{
				toastr.error("Your request was not accepted", "Failure");
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
	}
});