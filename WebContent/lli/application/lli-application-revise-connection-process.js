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
			// this.application = result.data.payload.members;
            if(result.data.payload.hasOwnProperty("members")){

                this.application = result.data.payload.members;
            }
            else{
                this.application = result.data.payload;
            }
			if (typeof this.application.content == 'undefined' || this.application.content === ""){
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
				ID:0, vlanID: 0, OCDistance: 0, btclDistance: 0, clientDistance: 0, OC: undefined, ofcType: undefined
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
				this.content.CONTENTTYPE = 'connection';
				this.doApplicationSpecificTask(applicationType);
			}, error => {
			});
		},
		doApplicationSpecificTask: function(applicationType){
			switch(applicationType) {
			    case 2://Upgrade Bandwidth
			    	this.content.bandwidth = parseInt(this.content.bandwidth) + parseInt(this.application.bandwidth);
			    	this.content.incident = {ID:2, label:'Upgrade Bandwidth'};
			        break;
			    case 3://Downgrade Bandwidth
			    	this.content.bandwidth = parseInt(this.content.bandwidth) - parseInt(this.application.bandwidth);
			    	this.content.incident = {ID:3, label:'Downgrade Bandwidth'};
			        break;
			    case 4://Temporary Upgrade Bandwidth
			    	this.content.bandwidth = parseInt(this.content.bandwidth) + parseInt(this.application.bandwidth);
			    	this.content.incident = {ID:4, label:'Temporary Upgrade Bandwidth'};
			        break;
			    case 5://Additional Port
			    	this.content.officeList.forEach(function(office, officeIndex, array){
			    		if(office.ID == this.application.office.ID){
			    			for(var i = 0;i<this.application.portCount;i++){
			    				office.localLoopList.push({
			    					ID:0, vlanID: 0, OCDistance: 0, btclDistance: 0, clientDistance: 0, OC: undefined, ofcType: undefined
			    				});
			    			}
			    		}
			    	}, this);
			    	this.content.incident = {ID:5, label:'Additional Port'};
			    	break;
			    case 6://Release Port
			    	//Manual
			    	break;
			    case 7://Additional Local Loop
			    	//Manual
			    	break;
			    case 8://Release Local Loop
			    	//Manual
			    	break;
			    case 9://Additional IP
			    	//Manual
			    	break;
			    case 10://Release IP
			    	//Manual
			    	break;
			    case 11://Additional Connection Address
			    	this.content.officeList.push({ID:0,name:"",address:this.application.address,localLoopList:[]});
			    	break;
			    case 12://Shift Connection Address
			    	this.content.officeList.forEach(function(office, officeIndex, array){
			    		if(office.ID == this.application.office.ID){
			    			office.address = this.application.address;
			    		}
			    	}, this);
			    	break;
			    case 13://Release Connection Address
			    	//manual
			    	//backend validation
			    	break;
			    case 14://Shift Pop
			    	//bola jachche na
			    	break;
			    default:
			        //goHome();
			        break;
			} 
		}
	}
});