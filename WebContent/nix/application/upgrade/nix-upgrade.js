var vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        application: {
            client:{},
            existingPortType:null,
            exitingPortSelected:false,
        },
        portTypeDropDown:[],
        message: 'Location',
        newPortTypes: [],
        nixLocalLoops: [],
        nixOffices: [],
        nixConnections:[],
        nixExistingPorts:[],
        loading:false,
    },
    methods: {
        goView: function(result){
            if(result.data.responseCode == 2) {
                toastr.error(result.data.msg);
            }else if(result.data.responseCode == 1){
                toastr.success("Your request has been processed", "Success");
                window.location.href = context + 'nix/application/search.do';
            }
            else{
                toastr.error("Your request was not accepted", "Failure");
            }
        },

        clientSelectionCallback: function(clientID){
            if(clientID === undefined){
                this.connectionOptionListByClientID = [];
                this.application.connection = undefined;
            } else{
                return axios({ method: 'GET', 'url': context + 'nix/connection/get-active-connection-by-client.do?id=' + clientID})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            this.nixConnections = result.data.payload;
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    }, error => {
                    });
            }
        },
        connectionSelectionCallback: function(connection){
            if(connection === undefined){
                this.officeList = [];
                this.application.office = undefined;
            } else{
                axios({ method: 'GET', 'url': context + 'nix/connection/get-officeObjectList-by-connection-id.do?id=' + connection.ID})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            this.nixOffices = result.data.payload;
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    }, error => {
                    });
            }
        },

        officeSelectionCallback: function(office){
            if(office === undefined){
                this.officeList = [];
                this.application.office = undefined;
            } else{
                axios({ method: 'GET', 'url': context + 'nix/connection/get-portList-by-connection-id.do?id=' + office.ID})
                    .then(result => {
                        if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        } else if (result.data.responseCode == 1) {
                            this.nixExistingPorts = result.data.payload;
                        }
                        else {
                            toastr.error("Your request was not accepted", "Failure");
                        }
                    }, error => {
                    });
            }
        },
        portSelectionCallback: function(port){
            if(port === undefined){
                toastr.error("Your Port Is not found", "Failure");

            } else{
               this.exitingPortSelected = true;
               this.application.existingPortType = port.object.value;
            }
        },
        submitFormData: function(){
            var url1 = "upgrade-port";
            if(this.application.client.key == null){
                this.errorMessage("Client must be selected.");
                return;
            }

            if(this.application.connection == null){
                this.errorMessage("Connection must be selected.");
                return
            }
            if(this.application.office ==null){
                this.errorMessage("Office must be selected.");
                return
            }
            if(this.application.oldPort == null ){
                this.errorMessage("Port must be selected.");
                return
            }
            if (this.application.newPortType == null) {
                this.errorMessage("port type must be selected.");
                return;
            }
            // if (this.application.loopProvider == null||this.application.loopProvider == '') {
            //     this.errorMessage("loop provider must be selected.");
            //     return;
            // }
            if (this.application.comment == null) {
                this.errorMessage("Provide comments.");
                return;
            }
            if(this.application.suggestedDate == null ){
                this.errorMessage("Date must be selected.");
                return
            }
            this.loading =true;
            axios.post(context + 'nix/application/' + url1 + '.do', {'application': JSON.stringify(this.application)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                    this.loading = false;
                });
        },
        errorMessage: function (msg) {
            toastr.options.timeOut = 3000;
            toastr.error(msg);
            return;
        },
	},
    watch:{
        'application.existingPortType':function () {
            if(this.application.existingPortType == 'FE'){
                vue.portTypeDropDown =[{ID:2,label:'GE'},{ID:3,label:'10GE'}];
            }
            else if(this.application.existingPortType == 'GE'){
                vue.portTypeDropDown =[{ID:3,label:'10GE'}]
            }
            else {
                vue.portTypeDropDown =[{ID:-1,label:'No Upgradable port Type'}]
            }
        },
    }
});