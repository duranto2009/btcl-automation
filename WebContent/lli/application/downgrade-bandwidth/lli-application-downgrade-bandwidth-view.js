var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {}
	},
	methods: {
		previewApplicationContent(applicationID){
			window.location.href = context + 'lli/application/preview-content.do?id=' + applicationID;
		}
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
		}, error => {
		});
	}
});