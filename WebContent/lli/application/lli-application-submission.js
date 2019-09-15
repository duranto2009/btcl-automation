let vue = new Vue({
	el: "#btcl-application",
	data: {
		loading: false,
		contextPath: context,
		//Client-Dependent
		connectionOptionListByClientID: [],
		dstconnectionOptionListByClientID:[],
		contractOptionListByClientID: [],
		//Connection-Dependent
		officeOptionListByConnectionID: [],
		popOptionListByOfficeID: [],
		//Objects Initialization
		application: {
			sourceConnection :{},
			destinationConnection : {},
			client: {},
			type: {
				ID: 0,
				label: '',
			},
            officeList: [
                {
                    officeName: '',
                    officeAddress: ''
                },
            ],
			state: 1,
			skipPayment : {},
			connectionTypes : [],

		},
		deepConnection: {},

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
		validate() {
			let error = true;
			if(!this.application.client.ID) {
				errorMessage("Please Select a Client");
				error = false;
			}
			if(!this.application.sourceConnection.ID) {
				errorMessage("Please Select a source Connection");
				error = false;
			}
			return error;

		},
		goView: function(result){
			if(result.data.responseCode === 2) {
				errorMessage(result.data.msg);
			}else if(result.data.responseCode === 1){
				toastr.success("Your request has been processed", "Success");
				window.location.href = context + 'lli/application/search.do';
			}
			else{
				errorMessage("Your request was not accepted", "Failure");
			}
		},
		clientSelectionCallback: function(clientID){
			if(clientID === undefined){
				this.clearConnection();this.clearOffice();this.clearPop();
			} else{
				this.clearConnection();
				this.application.sourceConnection = {};
				this.application.destinationConnection = {};
				axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
				.then(result => {
					// debugger;
					this.clearConnection();this.clearOffice();this.clearPop();
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
			let tempPopOptions = [];
			this.deepConnection.officeList.forEach(function(currentOffice, officeIndex, officeArray){
				if(selectedOffice.ID === currentOffice.ID){
					currentOffice.localLoopList.forEach(function(currentLocalLoop, localLoopIndex, localLoopArray){
						tempPopOptions.push({ID: currentLocalLoop.pop.ID, label: currentLocalLoop.pop.label});
					}, this);
				}
			}, this);
			this.popOptionListByOfficeID = tempPopOptions;
		},
        addOffice: function (app, event) {
            app.officeList.push({
                officeName: '',
                officeAddress: ''
            });

        },
        deleteOffice: function (application, officeIndex, event) {
            //connection.officeList[officeIndex].localLoopList.splice(localLoopIndex, 1);
            if (application.officeList.length == 1) return;
            application.officeList.splice(officeIndex, 1);


        },
	},
	mounted() {
		if (typeof applicationID !== 'undefined'){
			this.loading=true;
			Promise.resolve(
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
			})).then(()=>this.loading=false);
		}
	},
    watch: {

        'application.sourceConnection': function (val) {
			if(!this.application.sourceConnection.ID) return;
           this.dstconnectionOptionListByClientID = this.connectionOptionListByClientID.filter(t => t.ID!= val.ID);
           if(this.dstconnectionOptionListByClientID.length == 0 && this.application.type.ID ==1){
           	 errorMessage("No Destination Connection Found.");
		   }

			axios({ method: 'GET', 'url': context + 'lli/options/connection-type.do?id=' + this.application.client.ID})
				.then(result => {
					this.application.connectionTypes = result.data.payload;
				}, error => {
				});
        }


    }
});