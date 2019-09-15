var vue = new Vue({
    el: "#btcl-vpn",
    data: {

        application: {
            client: null,
            comment: null,
            
            // description: null,
            skipPayment: false,
            state: {
                name: '',
            },
            applicationType: 'shift',
            localEnd:{
                useExistingLoop: {},
                office:{},
            },

            remoteEnds:[],

            remoteEnd:{
                useExistingLoop:{},
            },
        },
        moduleId: moduleID,
        loading: false,
        linkList: [],
        link: null,
        changedBandwidth: 0,

        officeList: [],


        connectionTypeList: [{id: 1, value: 'Regular'}, {id: 2, value: 'Special Connection'}],
        layerTypeList: [{id: 2, value: 'Layer 2'}, {id: 3, value: 'Layer 3'}],
        localEndTerminalDeviceProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        remoteEndTerminalDeviceProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        officeSelectionType: [{id: 1, value: 'Existing Office'}, {id: 2, value: 'Create New Office'}],
        useExistingLoopList: [{id: 1, value: 'Yes'}, {id: 2, value: 'No'}],
        officeList: [],
        localEndLoopList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        remoteEndLoopList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        loopProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],

        localEndEdit : false,
        remoteEndEdit: false,

        lEnd: null,
        rEnd: null,

        clientDataLoaded: false,


    },
    methods: {
        submitShiftForm: function () {
            if (vue.validate() == false) return;
            // this.loading = true;
            // submitBandwidthChangeForm(vue);

            Object.assign(this.application, applicationBuilder(vue));
            let postObject = {
                application: this.application,
            };

            let url1 = "shift";
            // debugger;
            this.loading = true;
            postRequestedDataToServer(url1,postObject, this.redirectURLBuilder());


        },
        validate: function () {
            // start: form validation
            if (this.application.client == null) {
                errorMessage("Client must be selected.");
                return false;
            }
            if (this.link == null) {
                errorMessage("Link Must be selected.");
                return false;
            }
            if((JSON.stringify(vue.application.remoteEnd) === JSON.stringify(vue.rEnd)) && (JSON.stringify(vue.application.localEnd) === JSON.stringify(vue.lEnd))){
                errorMessage("At least one input field of local end or remote end need to be changed.");
                return false;
            }

            if(this.makeSureToSelectLocalLoop(this.application.localEnd) == false)return false;
            if(this.makeSureToSelectLocalLoop(this.application.remoteEnds[0]) == false)return false;

            //if existing Office Type Selected



            // end: form validation'

            if (this.application.suggestedDate == null) {
                errorMessage("Please provide a suggested date");
                return false;
            }


            return true;
        },

        makeSureToSelectLocalLoop: function(office){
            if(office.officeSelectionType.id == 1){
                if(office.useExistingLoop.id == 2) {
                    errorMessage("For Existing Office Existing Local Loop must be selected.");
                    return false;
                }
            }

            return true;
        },

        redirectURLBuilder: function(){
            let redirect = 'vpn/link/' + 'search' + '.do';
            return context + redirect;
        },



        officeBuilder: function (end, office) {
            // this.application[end] = this.application.vpnApplicationLinks[linkIndex][office];
            this.$set(this.application, end, this.link[office]);

            //use existing office or not
            //this.$set(this.application[end], 'useExistingLoop', this.link[office][useExistingLoop]);

            //office selection type


            //new office provided by user
            if (this.link[office].localLoops[0].isCompleted == false) {
                // this.application[end].officeSelectionType = this.officeSelectionType[1]; // new office provided
                this.$set(this.application[end], 'officeSelectionType', this.officeSelectionType[1]);
            }
            //already existing office
            else {
                // this.application[end].officeSelectionType = this.officeSelectionType[0]; // existing office provided
                this.$set(this.application[end], 'officeSelectionType', this.officeSelectionType[0]);

                // if existing loop
                if(this.application[end].localLoops[0].isCompleted){
                    // this.application[end].useExistingLoop = this.useExistingLoopList[0];
                    this.$set(this.application[end], 'useExistingLoop', this.useExistingLoopList[0]);

                }else{
                    this.$set(this.application[end], 'useExistingLoop', this.useExistingLoopList[1]);
                    // this.application[end].useExistingLoop = this.useExistingLoopList[1];

                }

            }
            // this.application.end['loop'] =

            //loop
            //if no loop then create the loop
            // if(!this.application[end].hasOwnProperty('localLoops')){
            //    this.$set(this.application.end, 'localLoops', [{}]);
            // }
            if (this.application[end].localLoops[0].loopProvider == 1) { //loop provider btcl
                // this.application[end].loop = this.loopProviderList[0];
                this.$set(this.application[end], 'loop', this.loopProviderList[0]);
            } else {
                // this.application[end].loop = this.loopProviderList[1];
                this.$set(this.application[end], 'loop', this.loopProviderList[1]);

            }

            //terminal device provider
            if (this.link.remoteOfficeTerminalDeviceProvider == 1 && end == 'remoteEnd') { //terminal provider btcl
                this.$set(this.application[end], 'terminalDeviceProvider',  this.loopProviderList[0]);
            } else if (this.link.remoteOfficeTerminalDeviceProvider == 2 && end == 'remoteEnd') {
                this.$set(this.application[end], 'terminalDeviceProvider', this.loopProviderList[1]);

            }
            // debugger;
            //use existing loop

            // if (end == 'remoteEnd') { //terminal provider btcl
                // this.$set(this.application[end], 'terminalDeviceProvider',  this.loopProviderList[0]);
                this.$set(this.application[end], 'useExistingLoop', this.useExistingLoopList[0]);

            // }

            //
            // if (this.link.remoteEndOffice.useExistingLoop.id == 1 && end == 'remoteEnd') { //terminal provider btcl
            //     // this.$set(this.application[end], 'terminalDeviceProvider',  this.loopProviderList[0]);
            //     this.$set(this.application[end], 'useExistingLoop', this.useExistingLoopList[0]);
            //
            // } else if (this.link.remoteEndOffice.useExistingLoop.id == 2  && end == 'remoteEnd') {
            //     this.$set(this.application[end], 'useExistingLoop', this.useExistingLoopList[1]);
            //     // this.$set(this.application[end], 'useExistingLoop', this.link.remoteEndOffice.useExistingLoop);// this.application[end].bandwidth = this.link.linkBandwidth;
            //
            // }

            if (this.link.localOfficeTerminalDeviceProvider == 1 && end == 'localEnd') { //terminal provider btcl
                this.$set(this.application[end], 'terminalDeviceProvider', this.loopProviderList[0]);
            } else if (this.link.localOfficeTerminalDeviceProvider == 2 && end == 'localEnd') {
                this.$set(this.application[end],'terminalDeviceProvider',this.loopProviderList[1]);
            }
            // end of terminal device provider


            this.$set(this.application[end], 'office', this.officeList.find(office =>  office.id === this.application[end].localLoops[0].officeId));
            //this.application[end].office = this.officeList.find(office =>  office.id === this.application[end].localLoops[0].officeId);


            this.$set(this.application[end]['office'], 'localLoop', this.link[office]['office']['localLoops'][0]);


            //start bandwidth
            if (end == 'remoteEnd') {

                this.$set(this.application[end], 'bandwidth', this.link.linkBandwidth);// this.application[end].bandwidth = this.link.linkBandwidth;

                this.$set(this.application, 'remoteEnds', []);

                this.application.remoteEnds.push(this.application[end]);
            }



            Object.assign(this.application, this.application);
            this.$forceUpdate();

            //end bandwidth

        },

        toggleLocalEndEdit: function(){
            this.localEndEdit  = !this.localEndEdit;
        },
        toggleRemoteEndEdit: function(){
            this.remoteEndEdit = !this.remoteEndEdit
        },
        endValidationDataPopulate: function(){
            this.lEnd = Object.assign({},this.application.localEnd);
            this.rEnd = Object.assign({},this.application.remoteEnd);
        }


    },
    watch: {
        'application.client': function () {
            if(vue.clientDataLoaded === true)return;
            vue.loading = true;

            Promise.all([
            getVPNOfficeByClientId(this.application.client.key).then(result=>{
                this.officeList = result;
                // this.application[end].office = this.officeList.find(office =>  office.id === this.application[end].localLoops[0].officeId);
            })],[
            getVPNNetworkLinksByClientId(this.application.client.key).then(result => {
                this.linkList = result;
                // vue.loading = false;

                if(this.linkList.length == 0){
                    errorMessage("The client doesn't have any link.")
                }
            })]
            ).then(function(){
                vue.loading = false;
                vue.clientDataLoaded = true;
            });


        },
        'link' : function(){

            this.officeBuilder('localEnd', 'localEndOffice');
            this.officeBuilder('remoteEnd', 'remoteEndOffice');
            this.endValidationDataPopulate();
            this.$set(this.application, 'linkId', this.link.id);
        },



    },
    created(){

    }
});