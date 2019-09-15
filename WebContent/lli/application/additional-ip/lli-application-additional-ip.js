let vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		application: {},
        connectionOptionListByClientID:[],
        loading:false,
	},
	methods:{
        submitData: function () {
            let url = "additional-ip";
            if(this.application.client== null){
                toastr.error("Please select a client.");
                return;
            }
            if(this.application.connection== null){
                toastr.error("Please select a connection.");
                return;
            }
            if(!this.application.ipCount || this.application.ipCount <= 0){
                toastr.error("IP count should be greater than zero.");
                return;
            }

            if(this.application.suggestedDate == null){
                toastr.error("Date should be selected.");
                return;
            }
            if(this.application.comment == null){
                toastr.error("Please provide a comment.");
                return;
            }
            let path = context+'lli/application/' + url + '.do';
            this.loading=true;
            Promise.resolve(axios.post(path, {'application': JSON.stringify(this.application)})
                .then(result => {
                    if (result.data.responseCode === 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode === 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        errorMessage("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                   console.log(error);
                })).then(()=>this.loading=false);
        },
        clientSelectionCallback: function(clientID){
            if(clientID === undefined){
                this.connectionOptionListByClientID = [];
                this.application.connection = undefined;
            } else{
                axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
                    .then(result => {
                        this.connectionOptionListByClientID = result.data.payload;
                    }, error => {
                    });
            }
        },
        
	},
});