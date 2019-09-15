var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		
		application: {
			applicationTypeName : "",
			fields : []
		}
	},
	mounted() {
		axios({ method: "GET", "url": context + "lli/application/get-application-type.do?id=" + applicationTypeID}).then(result => {
			this.application.applicationID = result.data.payload.ID;
			this.application.applicationTypeName = result.data.payload.name;
            this.application.fields = JSON.parse(result.data.payload.fields);
        }, error => {
            window.reload();
        });
	}
});