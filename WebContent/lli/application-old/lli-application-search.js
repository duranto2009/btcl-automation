var vue = new Vue({
	el: "#btcl-application",
	data: {
		applicationTypeList: [],
		applicationType: "",
		
		contextPath: context,
		
		application: {
			fields: []
		}
	},
	mounted() {
		axios({ method: "GET", "url": context + "lli/application/get-application-types.do"}).then(result => {
            this.applicationTypeList = result.data.payload;
        }, error => {
            window.reload();
        });
	},
	methods: {
		getApplicationFields: function(selectedOption, id){
			axios({ method: "GET", "url": context + "lli/application/get-application-fields.do?id=" + selectedOption.ID}).then(result => {
				this.application.applicationID = selectedOption.ID;
	            this.application.fields = JSON.parse(result.data.payload);
	        }, error => {
	            window.reload();
	        });
		}
	}
});