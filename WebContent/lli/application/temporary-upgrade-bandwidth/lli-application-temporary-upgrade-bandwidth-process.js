var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {},
		content: {}
	},
	mounted() {
		axios({ method: 'GET', 'url': context + 'lli/application/new-connection-get.do?id=' + applicationID})
		.then(result => {
			this.application = result.data.payload;
			if (this.application.content === ""){
				this.setUpFreshContent(this.application.connection.ID, this.application.applicationType.ID);
			} else {
				this.content = JSON.parse(result.data.payload.content);
			}
		}, error => {
		});
	},
	watch: {
		content: {
			deep: true,
			handler: function(val, oldVal){
				this.application.content = JSON.stringify(this.content);
			}
		}
	},
	methods: {
		addOffice: function(connection, event){
			connection.officeList.push({
				ID:0, name: '', address: '', localLoopList: [],
			});
		},
		addLocalLoop: function(office, event){
			office.localLoopList.push({
				ID:0, vlanID: 0, OCDistance: 0, btclDistance: 0, clientDistance: 0, OCID: 0
			});
		},
		deleteOffice: function(connection, officeIndex, event){
			connection.officeList.splice(officeIndex,1);
		},
		deleteLocalLoop: function(connection, officeIndex, localLoopIndex, event){
			connection.officeList[officeIndex].localLoopList.splice(localLoopIndex,1);
		},
		process: function(url){
			axios.post(context + 'lli/application/'+ url +'.do', {'application': this.application})
			.then(result => {
				if(result.data.responseCode == 2) {
					toastr.error(result.data.msg);
				}else if(result.data.responseCode == 1){
					toastr.success("Your request has been processed", "Success");
					window.location.href= context + 'lli/application/view.do?id='+this.application.applicationID;
				}
				else{
					toastr.error("Your request was not accepted", "Failure");
				}
			})
			.catch(function (error) {
			});
		},
		setUpFreshContent: function(connectionID,applicationType){
			axios({ method: 'GET', 'url': context + 'lli/connection/revise-connection-json.do?id=' + connectionID})
			.then(result => {
				this.content = result.data.payload;
				this.doApplicationSpecificTask(applicationType);
			}, error => {
			});
		},
		doApplicationSpecificTask: function(applicationType){
			switch(applicationType) {
			    case 2://Upgrade Connection
			    	this.content.bandwidth = parseInt(this.content.bandwidth) + parseInt(this.application.bandwidth);
			    	this.content.incident = {ID:2, label:'Upgrade Bandwidth'};
			    	this.content.CONTENTTYPE = 'connection';
			        break;
			    default:
			        goHome();
			} 
		}
	}
});