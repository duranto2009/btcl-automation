var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		
		application: {
			fields : []
		}
	},
	mounted() {
		axios({ method: "GET", "url": context + "lli/application/get-application-instance.do?id=" + applicationInstanceID}).then(result => {
			this.application.ID = result.data.payload.ID;
			this.application.clientID = result.data.payload.clientID;
			this.application.applicationDate = result.data.payload.applicationDate;
			this.application.applicationTypeName = result.data.payload.applicationID;
            this.application.fields = JSON.parse(result.data.payload.fields);
        }, error => {
        });
	}
});