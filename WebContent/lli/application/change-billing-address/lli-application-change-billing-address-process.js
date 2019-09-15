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
		}
	}
});