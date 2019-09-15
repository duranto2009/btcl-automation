var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		//Client-Dependent
		connectionOptionListByClientID: [],
		contractOptionListByClientID: [],
		//Connection-Dependent
		officeOptionListByConnectionID: [],
		popOptionListByOfficeID: [],
		//Objects Initialization
		application: {},
		deepConnection: {}
	},
	methods: {
		clearConnection: function(){
			this.connectionOptionListByClientID = [];
			this.application.connection = undefined;
			this.deepConnection = {};
		},
		clearOffice: function(){
			this.officeOptionListByConnectionID = [];
			this.application.office = undefined;
		},
		clearPop: function(){
			this.popOptionListByOfficeID = [];
			this.application.oldpop = undefined;
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
		},
		clientSelectionCallback: function(clientID){
			if(clientID === undefined){
				this.clearConnection();this.clearOffice();this.clearPop();
			} else{
				axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
				.then(result => {
					this.clearConnection();this.clearOffice();this.clearPop();
					this.connectionOptionListByClientID = result.data.payload;
				}, error => {
				});
			}
		},
		getConnectionListByClient: function(client, preLoaded){
			if(client === undefined){
				this.clearConnection();this.clearOffice();this.clearPop();
			} else{
				axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
				.then(result => {
					if(!preLoaded){
						this.clearConnection();this.clearOffice();this.clearPop();
					}
					this.connectionOptionListByClientID = result.data.payload;
				}, error => {
				});
			}
		},
		getLongTermContractListByClientID: function(client){
			if(client.ID === undefined){
				this.contractOptionListByClientID = [];
				this.application.contract = undefined;
			} else{
				axios({ method: 'GET', 'url': context + 'lli/longterm/get-longterm-by-client-id.do?id=' + client.ID})
				.then(result => {
					this.contractOptionListByClientID = result.data.payload;
				}, error => {
				});
			}
		},
		getOfficeListByConnectionID: function(connection){
			if(connection === undefined){
				this.clearOffice();this.clearPop();
			} else{
				axios({ method: 'GET', 'url': context + 'lli/connection/get-office-by-connection-id.do?id=' + connection.ID})
				.then(result => {
					this.officeOptionListByConnectionID = result.data.payload;
				}, error => {
				});
			}
		},
		getDeepConnectionDetails: function(connection){
			if(connection === undefined){
				this.clearOffice();this.clearPop();
			} else{
				axios({ method: 'GET', 'url': context + 'lli/connection/get-deep-connection-details.do?id=' + connection.ID})
				.then(result => {
					this.clearOffice();this.clearPop();
					this.deepConnection = result.data;
					this.generateOfficeListUsingThisDeepConnection();
				}, error => {
				});
			}
		},
		generateOfficeListUsingThisDeepConnection: function(){
			this.deepConnection.officeList.forEach(function(currentValue, index, array){
				this.officeOptionListByConnectionID.push({ID: currentValue.ID, label: currentValue.name});
			}, this);
		},
		setPopOptions: function(selectedOffice, id){
			this.clearPop();
			var tempPopOptions = [];
			this.deepConnection.officeList.forEach(function(currentOffice, officeIndex, officeArray){
				if(selectedOffice.ID === currentOffice.ID){
					currentOffice.localLoopList.forEach(function(currentLocalLoop, localLoopIndex, localLoopArray){
						tempPopOptions.push({ID: currentLocalLoop.pop.ID, label: currentLocalLoop.pop.label});
					}, this);
				}
			}, this);
			this.popOptionListByOfficeID = tempPopOptions;
		}
	},
	mounted() {
		if (typeof applicationID !== 'undefined'){
			axios({ method: 'GET', 'url': context + 'lli/application/new-connection-get.do?id=' + applicationID})
			.then(result => {
				// this.application = result.data.payload.members;
                if(result.data.payload.hasOwnProperty("members")){

                    this.application = result.data.payload.members;
                }
                else{
                    this.application = result.data.payload;
                }
			}, error => {
			});
		}
	}
});