let vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        connectionOptionListByClientID: [],
        destinationClientList:[],
        application: {
            srcClient:{},
            dstClient:{},
            selectedConnectionList:[],
            comment : ''
        },
        loading:false,
        connection: {

        },
        disabled : false,
    },
    methods: {

        validate() {
            let result = true;
            if(!this.application.srcClient.ID){
                errorMessage("Please Select Source Client");
                result = false;
            }
            if(!this.application.dstClient.ID) {
                errorMessage("Please Select Destination Client");
                result = false;
            }
            if(!this.application.selectedConnectionList.length){
                errorMessage("Please Select at least one connection to change ownership");
                result = false;
            }

            return result;
        },
        submitFormData:function () {
            if(!this.validate()) {
                return;
            }
            let url1 = "application-insert";
            this.loading = true;
            this.disabled = true;
            Promise.resolve(
                axios.post(context + 'lli/ownershipChange/' + url1 + '.do', {'application': JSON.stringify(this.application)})
                    .then(result => {
                        if (result.data.responseCode === 2) {
                            errorMessage(result.data.msg);
                        } else if (result.data.responseCode === 1) {
                            toastr.success("Your request has been processed", "Success");
                            window.location.href = context + 'lli/ownershipChange/search.do';
                        }
                    })
                    .catch(function (error) {
                        console.log(error);
                    })
            ).then(() => this.loading = false);
        },
        clearConnection: function(){
            this.connectionOptionListByClientID = []
        },
        clientSelectionCallback: function(clientID){
            this.application.selectedConnectionList = [];
            if(clientID === undefined){
                this.clearConnection();
            } else{
                axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
                    .then(result => {
                        if(result.data.responseCode === 1) {
                            this.clearConnection();
                            this.connectionOptionListByClientID = result.data.payload;
                            if(!this.connectionOptionListByClientID.length){
                                errorMessage("No Connection Found to change ownership");
                            }
                        }else {
                            errorMessage(result.data.msg);
                        }
                    }, error => {
                    });

            }
        },
        searchDestinationClient:function(query){
            axios({method: 'GET', 'url': context + 'lli/client/get-dest-client.do?val=' + query})
                .then(result => {
                    result.data.responseCode === 1 ? this.destinationClientList = result.data.payload : errorMessage(result.data.msg);

                }, error => console.log(error)
                );
        },
        connectionSelectionCallback:function (connection) {

            this.application.selectedConnectionList.push(connection);
        },
        removeSelectedConnection: function (index) {
            this.application.selectedConnectionList.splice(index, 1);
        },

    },
    watch: {
        'application.dstClient.ID': function () {
            if(this.application.srcClient.ID == this.application.dstClient.ID){
                errorMessage("You can't transfer to same client");
                this.application.dstClient = null;
            }
        },
    }
});