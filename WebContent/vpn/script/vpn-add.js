var vue = new Vue({
    el: "#btcl-vpn",
    data: {
        connectionTypeList: [{id: 1, value: 'Regular'}, {id: 2, value: 'Special Connection'}],
        layerTypeList: [{id: 2, value: 'Layer 2'}, {id: 3, value: 'Layer 3'}],
        localEndTerminalDeviceProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        remoteEndTerminalDeviceProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        officeSelectionType: [{id: 1, value: 'Existing Office'}, {id: 2, value: 'Create New Office'}],
        useExistingLoopList: [{id: 1, value: 'Yes'}, {id: 2, value: 'No'}],
        officeList: [],
        localEndLoopList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        remoteEndLoopList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],


        application: {
            client: null,
            connectionType: null,
            layerType: null,

            comment: null,
            // suggestedDate: null,
            // description: null,
            // bandwidth: null,
            // link: null,
            skipPayment: false,
            state: {
                name: '',
            },
            localEnd: {
                officeName: null,
                officeAddress: null,
                loop: null,
                terminalDeviceProvider: {id: 2, value: 'Client'},
                officeSelectionType: {id: 2, value: 'Create New Office'},

            },
            remoteEnds:
                [{
                    officeName: null,
                    officeAddress: null,
                    loop: null,
                    terminalDeviceProvider: {id: 2, value: 'Client'},
                    officeSelectionType: {id: 2, value: 'Create New Office'},

                }],

        },
        moduleId: moduleID,

        loading: false,
    },
    methods: {
        submitNewConnection: function () {
            if (this.validate() == false) return;
            this.loading = true;

            let app = applicationBuilder(vue);
            // let app = this.applicationBuilder();
            // debugger;
            let url = "add";
            // <%--<%System.out.println("before axios call: " + "vpn/link/add.do");%>--%>
            axios.post(context + 'vpn/link/' + url + '.do', {
                // 'application': JSON.stringify(this.application)
                application: app
            }).then(result => {
                // debugger;
                if (result.data.responseCode == 2) {
                    toastr.error(result.data.msg);
                } else if (result.data.responseCode == 1) {
                    toastr.success("Your request has been processed", "Success");
                    toastr.options.timeOut = 2500;
                    //redirect to specific page
                    window.location.href = context + 'vpn/link/search.do';
                } else {
                    toastr.error("Your request was not accepted", "Failure");
                    window.location.href = context + 'vpn/link/add.do';
                    toastr.options.timeOut = 2500;

                }
                // this.loading = false;
                // <%--<%System.out.println("after axios call: " + "vpn/link/add.do");%>--%>
            })
                .catch(function (error) {
                });
        },
        validate: function () {
            

            return validateVPNNewLinkApplication(vue);
            // // start: form validation
            // if (this.application.client == null) {
            //     errorMessage("Client must be selected.");
            //     return false;
            // }
            // if (this.application.layerType == null) {
            //     errorMessage("Layer Type must be selected.");return false;
            // }
            // // if (this.application.suggestedDate == null) {
            // //     errorMessage("Provide a suggested date.");
            // //     return false;
            // // }
            // //loop validation
            // if(this.application.localEnd.loop == null || this.application.remoteEnds.some(r => r.loop == null)){
            //     errorMessage("Loop Provider must be selected.");return false;
            // }
            // // end: form validation'
            // return true;
        },
        addRemoteEnd: function () {
            this.application.remoteEnds.push({
                officeName: null,
                officeAddress: null,
                loop: null,
                terminalDeviceProvider: {id: 2, value: 'Client'},
                officeSelectionType: {id: 2, value: 'Create New Office'},

            });
        },

        deleteFromRepeater: function (obj, property, Index) {
            return deleteFromRepeater(obj, property, Index);
        },

    },

    watch: {
        'application.client': function () {
            // axios.get(context + 'vpn/link/get-office-by-client.do?clientId=' + this.application.client.key)
            //     .then(result => {
            //         if (result.data.responseCode == 2) {
            //             // toastr.error(result.data.msg);
            //         } else if (result.data.responseCode == 1) {
            //             this.officeList = result.data.payload;
            //         }
            //         // debugger;
            //     })
            //     .catch(function (error) {
            //     });

            getVPNOfficeByClientId(this.application.client.key).then(result => {
                this.officeList = result;
            });
        }
    }
});