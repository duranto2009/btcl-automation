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
			if (this.application.content === ""){
				this.setUpFreshContent();
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
		setUpFreshContent: function(){
			this.content = {
				ID: 0,
				name: '',
				client: this.application.client,
				bandwidth: this.application.bandwidth,
				connectionType: this.application.connectionType,
				officeList: [
					{
						ID: 0,
						name: '',
						address: this.application.address,
						localLoopList: []
					}
				],
				incident: {ID:1, label:'New Connection'},
				CONTENTTYPE: 'connection'
			};
		}
	}
});