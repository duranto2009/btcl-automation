var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {}
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