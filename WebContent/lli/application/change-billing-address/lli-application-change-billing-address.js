var vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        application: {clientDetailsDTO:{},},

    },
    methods: {
        submitData: function () {
            var url = "change-billing-address";
            var path = context+'lli/revise/' + url + '.do';
            axios.post(path, {'application': JSON.stringify(this.application)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/revise/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });
        },

        clientSelectionCallback: function(clientID){
            if(clientID === undefined){
                this.connectionOptionListByClientID = [];
                this.application.connection = undefined;
            } else{
                this.getClientContactDetailsByClientId(clientID);
            }
        },
        getClientContactDetailsByClientId:function(clientID){
            axios({ method: 'GET', 'url': context + 'lli/revise/get-client-billing-address.do?id=' + clientID})
                .then(result => {
                    if(result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    }else if(result.data.responseCode == 1){
                        this.application.clientDetailsDTO = result.data.payload;
                        console.log("Your request has been processed", "Success");
                        //location.reload();
                    }
                    else{
                        console.log("Your request was not accepted", "Failure");
                    }
                }, error => {
                });
        },
        getClientContactDetails: function () {
            axios({ method: 'GET', 'url': context + 'lli/revise/get-client-billing-address.do?id=' + this.application.client.ID})
                .then(result => {
                    if(result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    }else if(result.data.responseCode == 1){
                        this.application.clientDetailsDTO = result.data.payload;
                        console.log("Your request has been processed", "Success");
                        //location.reload();
                    }
                    else{
                        console.log("Your request was not accepted", "Failure");
                    }
                }, error => {
                });
        }
    },
    mounted(){
        this.getClientContactDetails();
    }

});