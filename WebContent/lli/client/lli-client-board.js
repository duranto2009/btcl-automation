var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		connectionList: [],
		contractList: [],
		tdHistoryList: [],
		clientTDDate: 0,
		accountList: [],
		registrantContactDetails: {},
		billingContactDetails: {},
		adminContactDetails: {},
		technicalContactDetails: {},
		clientDetails: {}
	},
	mounted() {
		//Get Client Summary
		axios({ method: 'GET', 'url': context + 'lli/client/get-client-details.do?id=' + clientID})
		.then(result => {
			this.clientDetails = result.data.payload;
		}, error => {
		});
		//Get Contact Details
		axios({ method: 'GET', 'url': context + 'lli/client/get-contact-details-list.do?id=' + clientID})
		.then(result => {
			result.data.payload.forEach(function(val,index,array){
				switch(val.detailsType){
				case 0:
					this.registrantContactDetails = val;
					break;
				case 1:
					this.billingContactDetails = val;
					break;
				case 2:
					this.adminContactDetails = val;
					break;
				case 3:
					this.technicalContactDetails = val;
					break;
				}
			}, this);
		}, error => {
		});
		//Get Connection List
		axios({ method: 'GET', 'url': context + 'lli/client/get-connection-list.do?id=' + clientID})
		.then(result => {
			this.connectionList = result.data.payload;
		}, error => {
		});
		//Get Long Term Contract List
		axios({ method: 'GET', 'url': context + 'lli/client/get-contract-list.do?id=' + clientID})
		.then(result => {
			this.contractList = result.data.payload;
		}, error => {
		});
		//Get TD History List
		axios({ method: 'GET', 'url': context + 'lli/td/get-client-td-history.do?id=' + clientID})
		.then(result => {
			this.tdHistoryList = result.data.payload;
		}, error => {
		});
		//Get Probable TD Date
		axios({ method: 'GET', 'url': context + 'lli/td/get-probable-td-date.do?id=' + clientID})
		.then(result => {
			this.clientTDDate = result.data.payload;
		}, error => {
		});
		//Get Balance
		axios({ method: 'GET', 'url': context + 'lli/client/get-account-list.do?id=' + clientID})
		.then(result => {
			this.accountList = result.data.payload;
		}, error => {
		});
	}
});