var vue = new Vue({
    el: "#btcl-vpn-application",

    data: {
        //start region: necessary lists for client correction
        connectionTypeList: [{id: 1, value: 'Regular'}, {id: 2, value: 'Special Connection'}],
        layerTypeList: [{id: 2, value: 'Layer 2'}, {id: 3, value: 'Layer 3'}],
        localEndTerminalDeviceProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        remoteEndTerminalDeviceProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        terminalDeviceProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        officeSelectionType: [{id: 1, value: 'Existing Office'}, {id: 2, value: 'Create New Office'}],
        useExistingLoopList: [{id: 1, value: 'Yes'}, {id: 2, value: 'No'}],
        officeList: [],
        localEndLoopList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        loopProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],
        remoteEndLoopList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],

        //endregion: necessary lists for client correction
        detailsPageLoading:false,
        application: {
            client: null,
            connectionType: null,
            suggestedDate: null,
            description: null,
            formElements: [
                {Label: '', Value: ''}, // TODO need to be uncommented when real data available
            ],
            action: [
                {Label: '', Value: ''},
            ],
            offices: [
                {
                    loops: [
                        {}
                    ]
                }
            ],
            color: "blue",
            comment: '',
            state: '',
            connection: null,


            vpnApplicationLinks: [
                {
                    localOffice: {
                        efrs : [],
                    },
                    remoteOffice:{
                        localLoops:[
                            {
                                pop: {},
                            }
                        ],
                        efrs: []
                    },
                    state :{

                    },
                    formElements : [
                        {Label: '', Value: ''},
                    ]
                }
                ],

            //start: for client correction
            localEnd: {
                officeName: "",
                officeAddress: "",
                loop: {},
                terminalDeviceProvider: {},
                officeSelectionType: {
                    id: "",
                    value: "",
                },

            },
            remoteEnds:
                [

                        // {
                        //     officeName: "",
                        //     officeAddress: "",
                        //     loop: {},
                        //     terminalDeviceProvider: {},
                        // }
                ],
            //end: for client correction


        },
        an:null,
        dn:null,
        wo:null,
        vendorContact: null,
        isCollaborated: false,
        globalActionPicked: null,
        localActionPicked: null,
        details:false,

        picked: {},
        NO_ACTION_PICKED: {},
        nextAction: {
            param: '',
        },

        comment: '',
        moduleId: moduleID,

        popList: [],

        localEnd: {},
        remoteEnd: {},

        localEndLoop: {},
        remoteEndLoop: {},

        localLinkIndex: -1,

        localEndPop: null,
        localEndFeasibility: false,

        localEndZone: null,

        zoneList: [],
        loading: true,

        //    start: IP assign
        regionID: null,

        ipRegionList: [],
        ipVersion: null,
        ipType: null,
        ipBlockSize: null,
        ipFreeBlockList: [],
        ipBlockID: null,
        ipAvailableRangleList: [],
        ipAvailableSelected: [],
        ipRoutingInfo: null,
        ipAvailableIPID: null,
        collapse: true,

        linkList: [],
        link: null,
        changedBandwidth: 0,

        commentsList: null,



        //    end: IP assign
    },
    methods: {

        nextStep: function (...arg) {
            if (Object.keys(this.picked).length === 0) {
                errorMessage("Please Select an Action.");
                return;
            }

            // if user want to process single link then the 'localLinkIndex variable value should be the index of application.applicationLinks

            // else change the localLinkIndex to -1
            let nextAction = this.picked; // this.application.action.find(obj => obj.id == this.picked);

            this.localLinkIndex = -1;
            if (arg.length == 1) {
                if (Number.isInteger(arg[0])) {
                    this.localLinkIndex = arg[0];
                    //change the next state of localLinkIndex
                    for (let i = 0; i < this.application.vpnApplicationLinks.length; i++) {
                        if (i == this.localLinkIndex) {
                            this.application.vpnApplicationLinks[i].linkState = nextAction.name;
                        }
                    }
                }
                //else change the next state of all the links
                else {
                    for (let i = 0; i < this.application.vpnApplicationLinks.length; i++) {
                        // if(i == this.localLinkIndex){
                        this.application.vpnApplicationLinks[i].linkState = nextAction.name;
                        // }
                    }
                }

            }
            //for testing complete load ipList
            if(this.picked.param == 'complete-testing'){
                this.getIPRegionList();
            }
            this.nextAction = nextAction;
            if (nextAction) var commentTitle = " Comment on " + nextAction.description;
            if (Object.keys(this.picked).length === 0) {
                errorMessage("Please Select an Action.");
                return;
            }
            //if connection compelete step then take load modal to take connection name as input
            // {/*<%--if(this.picked == <%=CoLocationConstants.STATE.COMPLETE_CONNECTION.getValue()%>)  $('#connectionCompletionModal').modal({show: true});--%>*/}
            //   {/*<%--else if(this.picked ==  <%=CoLocationConstants.STATE.ADVICE_NOTE_PUBLISH.getValue()%>)    $('#forwardAdviceNote').modal({show: true});--%>*/}
            else if (nextAction.modal != "") {
                $(nextAction.modal).modal({show: true});
            }
            //load sweet alert elsewhere
            else swal(commentTitle, {
                    content: "input",
                    showCancelButton: true,
                    buttons: true,
                })
                    .then((value) => {
                        if (value === false) return false;
                        if (value === "") {
                            swal("Warning!", "You you need to write something!", "warning");
                            return false;
                        }
                        if (value) {
                            this.loading = true;
                            this.comment = value + '';
                            if (this.nextAction.name == "VPN_PRECHECK_DONE") {
                                Object.assign(this.application, applicationBuilder(vue));
                            }
                            this.postRequest();
                            // debugger;
                        }
                    });
        },
        ifrPostRequest:function(){
            

            this.postRequest();
        },
        postRequest: function () {




            // this.$refs['vendor-response'].toggle('#response-id');


            $('#modal-vendor-response').modal('hide');
            $('#modal-vendor-selection').modal('hide');
            $('#modal-pop-selection').modal('hide');
            $('#modal-bandwidth-change').modal('hide');
            $('#serverRoomTestingComplete').modal('hide');
            $('#efrRequestToLocalDGMModal').modal('hide');
            $('#efrRequestToLocalDGMModalSelectZone').modal('hide');


            let apps = this.application;
            //set the comment
            this.application['comment'] = this.comment;
            for (let i = 0; i < this.application.vpnApplicationLinks.length; i++) {

                //copy the local office to every link

                //for local office

                if (this.application.vpnApplicationLinks[i].hasOwnProperty("localOffice")) {

                    //set ifr feasibility

                    //set zone

                    for (let j = 0; j < this.application.vpnApplicationLinks[i].localOffice.localLoops.length; j++) {

                        //set pop in local office end

                        if (this.localEndPop != null) this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["popId"] = this.localEndPop.ID;
                        // if(i==0){
                        //     if(this.application.vpnApplicationLinks[i].localOffice.localLoops[j].hasOwnProperty("pop"))
                        //         this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["popId"] = this.application.vpnApplicationLinks[i].localOffice.localLoops[j].pop.ID;
                        // }else{
                        //     this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["popId"] = this.application.vpnApplicationLinks[i].localOffice.localLoops[j].popId;
                        //     //set feasiblity of local office
                        //set ifr feasibility in local office end
                        // debugger;
                        // if (this.localEndFeasibility != false){

                            this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["ifrFeasibility"] = this.localEndFeasibility;

                        // }
                        // }

                        //set the zone of the local office end of the link,

                        //set isDistributed true for this case

                        if (this.localEndZone != null
                            && this.application.vpnApplicationLinks[i].localOffice.localLoops[j].isCompleted != true
                        ) {
                            this.application.vpnApplicationLinks[i].localOffice.localLoops[j].zoneId = this.localEndZone.id;
                            // todo : hardcode needed if necessary for efr request & work order
                            //this.application.vpnApplicationLinks[i].localOffice.localLoops[j].isDistributed = true;
                        }
                    }

                    //    if efr exists and proposed loop distance is provided then make isReplied to true

                    if (this.application.vpnApplicationLinks[i].localOffice.hasOwnProperty("efrs")) {
                        for (let j = 0; j < this.application.vpnApplicationLinks[i].localOffice.efrs.length; j++) {
                            if (this.application.vpnApplicationLinks[i].localOffice.efrs[j].hasOwnProperty("proposedLoopDistance")){
                                if (this.application.vpnApplicationLinks[i].localOffice.efrs[j].proposedLoopDistance > 0) {
                                    this.application.vpnApplicationLinks[i].localOffice.efrs[j].isReplied = true;
                                }
                            }
                            //set collaborated if needed
                            if(this.isCollaborated){
                                this.application.vpnApplicationLinks[i].localOffice.efrs[j]['isCollaborated'] = true;
                            }
                            //set contact number if exist
                            if(this.vendorContact!=null){
                                this.application.vpnApplicationLinks[i].localOffice.efrs[j]['contact'] = this.vendorContact;

                            }
                        }
                    }
                }

                //for remote office

                // {
                if (this.application.vpnApplicationLinks[i].hasOwnProperty("remoteOffice")) {
                    for (let j = 0; j < this.application.vpnApplicationLinks[i].remoteOffice.localLoops.length; j++) {
                        if (this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].hasOwnProperty("pop"))
                            this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j]["popId"] = this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].pop.ID;

                        //    set zone for remote ends

                        if (
                            this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].hasOwnProperty("zone")
                            && this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].isCompleted != true

                        ) {
                            this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j]["zoneId"] = this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].zone.id;

                            // todo : hardcode needed if necessary for efr request & work order
                            // this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].isDistributed = true;
                        }

                    }

                    //    if efr exists and proposed loop distance is provided then make isReplied to true
                    if (this.application.vpnApplicationLinks[i].remoteOffice.hasOwnProperty("efrs")) {
                        for (let j = 0; j < this.application.vpnApplicationLinks[i].remoteOffice.efrs.length; j++) {
                            if (this.application.vpnApplicationLinks[i].remoteOffice.efrs[j].hasOwnProperty("proposedLoopDistance")) {
                                if (this.application.vpnApplicationLinks[i].remoteOffice.efrs[j].proposedLoopDistance > 0) {
                                    this.application.vpnApplicationLinks[i].remoteOffice.efrs[j].isReplied = true;
                                }
                            }
                            if(this.isCollaborated){
                                this.application.vpnApplicationLinks[i].remoteOffice.efrs[j]['isCollaborated'] = true;
                            }
                            //set contact number if exist
                            if(this.vendorContact!=null){
                                this.application.vpnApplicationLinks[i].remoteOffice.efrs[j]['contact'] = this.vendorContact;

                            }
                        }
                    }

                }

            }

            //copy localOffice of first vpnApplication link to all other links

            for (let i = 1; i < this.application.vpnApplicationLinks.length; i++) {
                if (this.application.vpnApplicationLinks[i].hasOwnProperty("localOffice")) {
                    let localOffIndex = 0;
                    if(this.localLinkIndex != -1){
                        localOffIndex = this.localLinkIndex;
                    }
                    this.application.vpnApplicationLinks[i].localOffice = this.application.vpnApplicationLinks[localOffIndex].localOffice;
                }
            }

            // if global action not selected then send only that link

            if (this.localLinkIndex != -1) {
                let localVpnApplicationLinks = this.application.vpnApplicationLinks[this.localLinkIndex];
                delete (this.application.vpnApplicationLinks);
                this.application.vpnApplicationLinks = [];
                this.application.vpnApplicationLinks.push(
                    localVpnApplicationLinks,
                );
            }

            var url1 = this.nextAction.url;

            let postObject = {
                application: this.application,
            };
            // debugger;
            let ccList = null;
            if (this.application.userList) {
                ccList = this.application.userList;
            }
            (this.nextAction.name === "VPN_ADVICE_NOTE_PUBLISH"
                || this.nextAction.name === "VPN_WITHOUT_LOOP_ADVICE_NOTE_PUBLISH"
                || this.nextAction.name === "VPN_TD_APPLICATION_ADVICE_NOTE_PUBLISH"
                || this.nextAction.name === "VPN_RECONNECT_APPLICATION_ADVICE_NOTE_PUBLISH"
                || this.nextAction.name === "VPN_OC_ADVICE_NOTE_PUBLISH"
                || this.nextAction.name === "VPN_DOWNGRADE_APPLICATION_ADVICE_NOTE_PUBLISH"
                || this.nextAction.name === "CLOSE_ADVICE_NOTE"
            )

                ? postObject.ccList = JSON.stringify(ccList) : void (0);

            // debugger;


            this.postRequestedDataToServer(url1,postObject, this.redirectURLBuilder());

        },

        postRequestedDataToServer: function(url, postObject, redirect){
            this.loading = true;
            axios.post(context + 'vpn/link/' + url + '.do', postObject)
                .then(result => {
                    // vue.loading = false;
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg)
                        setTimeout(()=>{
                            location.reload(1);
                        },5000);



                        // sleep(5000);
                        // window.location.reload();
                    } else if (result.data.responseCode == 1) {
                        // let redirect = this.redirectURLBuilder();
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = redirect;
                        // window.location.href = context + redirect;
                    } else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    // this.loading = false;
                }).catch(function (error) {
            });
        },

        /**
         * return the redirect url if nextAction url is not empty
         * @returns {string|*}
         */
        redirectURLBuilder: function(){
            var redirect = "";
            if (this.nextAction.redirect != "") {
                if (this.nextAction.param != "") {
                    var paramArray = this.nextAction.param.split('_');

                    var paramVal = "";
                    for (var i = 0; i < paramArray.length; i++) {

                        if (paramArray[i] == "billIDs") {
                            if(this.localLinkIndex == -1){  // global action selected
                                paramVal += paramArray[i] + "=";
                                paramVal += this.application.vpnApplicationLinks.map(t=>t.demandNoteId).join("&" + paramArray[i] +"=");
                            }else{
                                paramVal += paramArray[i] + "=" + this.application.vpnApplicationLinks[0].demandNoteId;

                            }
                        } else if (paramArray[i] == "paymentID") {

                            if(this.localLinkIndex == -1){  // global action selected
                                paramVal += paramArray[i] + "=" + this.application.bill.paymentID;

                            }else{
                                paramVal += paramArray[i] + "=" + this.application.vpnApplicationLinks[0].bill.paymentID;

                            }

                            // paramVal += paramArray[i] + "=" + this.application.bill.paymentID;

                        } else if (paramArray[i] == "moduleID") {

                            paramVal += paramArray[i] + "=" + this.application.moduleID;
                        } else if (paramArray[i] == "applicationID") {

                            paramVal += paramArray[i] + "=" + this.application.applicationId;
                        } else if (paramArray[i] == "id") {
                            //set link id if link wise else application id
                            if(this.localLinkIndex == -1){  // global action selected
                                paramVal += paramArray[i] + "=" + this.application.applicationId;

                            }else{
                                paramVal += paramArray[i] + "=" + this.application.vpnApplicationLinks[0].id;

                            }

                        } else if (paramArray[i] == "nextstate") {

                            paramVal += paramArray[i] + "=" + this.nextAction.id;
                        } else if (paramArray[i] == "appGroup") {
                            let appGrp = (this.application.applicationType == "VPN_OWNER_CHANGE" ? 8 : ((this.application.applicationType == "VPN_TD")
                                                                    ||(this.application.applicationType == "VPN_RECONNECT"))?7: 6)
                            //todo hard code appgropu need to be replaced
                            paramVal += paramArray[i] + "=" + appGrp;
                        }else if (paramArray[i] == "global") {
                            // global link or local link
                            if(this.localLinkIndex == -1) {  // global action selected
                                paramVal += paramArray[i] + "=" + true;
                            }else{
                                paramVal += paramArray[i] + "=" + false;

                            }
                        }

                        if (i == paramArray.length - 1) {

                        } else {

                            paramVal += "&"
                        }
                    }

                    redirect = this.nextAction.redirect + "?" + paramVal
                } else {
                    redirect = this.nextAction.redirect;
                }
            } else {
                redirect = 'vpn/link/search.do';
            }

            return context + redirect;
        },
        viewAN: function () {
            let location = '';
            if (arguments[1]) {
                location = context + 'pdf/view/link/advice-note.do?linkId=' + arguments[1];
            } else {
                location = context + 'pdf/view/advice-note.do?appId=' + applicationID + "&module=6";
            }
            window.open(
                location,
                '_blank'
            );
        },
        viewDN: function () {
            let location = '';
            if(arguments[1] && arguments[1].hasOwnProperty('bill')){

                location = context + 'pdf/view/demand-note.do?billId=' + arguments[1].bill.ID;
            }else {
                //global case every demand note id should be same;
                location = context + 'pdf/view/demand-note.do?billId=' + this.application.vpnApplicationLinks[0].demandNoteId;
            }
            window.open(
                location,
                '_blank'
            );
        },
        viewWO: function (o) {
            // link+=request.getContextPath()+"/lli/pdf/workorder.do?applicationid="+ol.getApplicationId() +"&vendorid="+oc.getRecipientId();
            let location = context + 'pdf/view/work-order.do?appId='+o.appId +'&module=7&vendorId='+o.recipientId;
            window.open(
                location,
                '_blank'
            );
        },
        redirect: function (url) {
            let location;
            // http://localhost:8082/lli/pdf/view.do?type=demand-note&id=299009

            if (url === 'demandnote') {
                if(arguments[1] && arguments[1].hasOwnProperty('bill')){

                    location = context + 'pdf/view/demand-note.do?billId=' + arguments[1].bill.ID;
                }else {
                    //global case every demand note id should be same;
                    location = context + 'pdf/view/demand-note.do?billId=' + this.application.vpnApplicationLinks[0].demandNoteId;
                }
            }
            if (url === 'advicenote') {
                if (arguments[1]) {
                    location = context + 'pdf/view/link/advice-note.do?linkId=' + arguments[1];
                } else {
                    location = context + 'pdf/view/advice-note.do?appId=' + applicationID + "&module=6";
                }
            }
            if (url === 'workorder') location = context + 'pdf/view/work-order.do?appId=' + applicationID + "&module=6&vendorId=" + loggedInUserID;

            // window.location.href = location;
            window.open(
                location,
                '_blank'
            );
        },

        deleteAllLocalActionPicked: function(){
            for(let i=0;i < this.application.vpnApplicationLinks.length; i++){
                if(this.application.vpnApplicationLinks[i].hasOwnProperty("picked")){
                    delete(this.application.vpnApplicationLinks[i].picked);
                }
            }
        },
        // globalActionPicked: function(){
        //     this.deleteAllLocalActionPicked();
        //     debugger;
        // },
        localLinkActionPick: function(linkIndex,actionIndex){
            this.deleteAllLocalActionPicked();
            this.picked = this.application.vpnApplicationLinks[linkIndex].action[actionIndex];
            // debugger;
        },

        //set isDistributed=true on zone selection modal submit
        zoneSelectionModalSubmit: function(){
            for (let i = 0; i < this.application.vpnApplicationLinks.length; i++) {
                //for local office
                if (this.application.vpnApplicationLinks[i].hasOwnProperty("localOffice")) {
                    for (let j = 0; j < this.application.vpnApplicationLinks[i].localOffice.localLoops.length; j++) {
                        if (this.localEndZone != null
                            && this.application.vpnApplicationLinks[i].localOffice.localLoops[j].isCompleted != true
                        ) {
                            this.application.vpnApplicationLinks[i].localOffice.localLoops[j].isDistributed = true;
                        }
                    }
                }

                //for remote office
                if (this.application.vpnApplicationLinks[i].hasOwnProperty("remoteOffice")) {
                    for (let j = 0; j < this.application.vpnApplicationLinks[i].remoteOffice.localLoops.length; j++) {
                        if (
                            this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].hasOwnProperty("zone")
                            && this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].isCompleted != true

                        ) {
                            this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].isDistributed = true;
                        }
                    }
                }

            }
            this.postRequest();
        },

        forwardAdviceNote: function () {
            this.postRequest();
        },

        forceUpdateDOM: function () {
            this.$forceUpdate();
        },

        //add loop for efr
        addLoopToLocalEnd: function (idx) {
            // this.localEnd.loops.push(this.localEndLoop);
            // I can access the first efr as I am sure that
            // this method will be fired when there must be at least one element inside the efr array
            let popId = this.application.vpnApplicationLinks[idx].localOffice.efrs[0].popId;
            this.application.vpnApplicationLinks[idx].localOffice.efrs.push({
                // popId :this.application.vpnApplicationLinks[idx].localOffice.localLoops[0].popId,
                officeId: this.application.vpnApplicationLinks[idx].localOffice.id,
                popId: popId,
                applicationId: this.application.applicationId,
                moduleId: this.moduleId,

            });
        },
        addLoopToRemoteEnd: function (idx) {
            let popId = this.application.vpnApplicationLinks[idx].remoteOffice.efrs[0].popId;
            // this.remoteEnd.loops.push(this.remoteEndLoop);
            this.application.vpnApplicationLinks[idx].remoteOffice.efrs.push({
                // popId :this.application.vpnApplicationLinks[idx].localOffice.localLoops[0].popId,
                officeId: this.application.vpnApplicationLinks[idx].remoteOffice.id,
                popId: popId,
                applicationId: this.application.applicationId,
                moduleId: this.moduleId,
            });
        },


        //if any local office exists
        localOfficeExists: function () {
            let isExists = false;
            for (let i = 0; i < this.application.vpnApplicationLinks.length; i++) {
                if (this.application.vpnApplicationLinks[i].hasOwnProperty("localOffice")) {
                    isExists = true;
                    break;
                }
            }
            return isExists;
        },
        remoteOfficeExists: function () {
            let isExists = false;
            for (let i = 0; i < this.application.vpnApplicationLinks.length; i++) {
                if (this.application.vpnApplicationLinks[i].hasOwnProperty("remoteOffice")) {
                    isExists = true;
                    break;
                }
            }
            return isExists;
        },

        // connectionCompletionModal: function () {
        //     if (this.picked == this.NO_ACTION_PICKED) {
        //         errorMessage("Please Select an Action.");
        //         return;
        //     }
        //     ;
        //     const nextAction = this.application.action.find(obj => obj.id == this.picked);
        //     // this.postData(this.comment, nextAction);
        // },

        //  switch, port, router selection
        //In testing complete step, server room can select switch/router then related port and related vlan, here is the onchange for those fields
        onChangeSwitchRouter: function (localLoop) {
            localLoop.filteredPorts = this.getFilteredPorts(localLoop);
            localLoop.filteredVlans = this.getFilteredVlans(localLoop);
            localLoop.vlanId = 0;
            localLoop.portId = 0;
        },

        getFilteredPorts(localLoop) {
            return localLoop.ports.filter(t=> t.parentID === localLoop.routerOrSwitchId);
        },

        getFilteredVlans(localLoop) {
            if(localLoop.vlanType === 2) {
                return localLoop.globalVlans;
            }
            return localLoop.vlans.filter( t=> t.parentID === localLoop.routerOrSwitchId);
        },

        onChangeVlanType: function(localLoop){
            localLoop.filteredVlans = this.getFilteredVlans(localLoop);
            localLoop.vlanId = 0;
        },


        //IP Assign
        getIPRegionList: function(){
            var url = 'ip/region/getRegions.do';
            axios({method: 'GET', 'url': context + url})
                .then(result => {
                    this.ipRegionList = result.data.payload;
                }, error => {
                });
        },
        searchIPBlock: function (data) {
            // this.loading = true;
            var regid = this.regionID;
            var ver = this.ipVersion;
            var typ = this.ipType;
            var blksz = this.ipBlockSize;
            if (regid == 0 || ver == 0 || typ == 0 || blksz == 0) {
                this.errorMessage("Please Provide all the information.");
                return;
            }
            var data = {
                "regionId": regid,
                "version": ver,
                "type": typ,
                "blockSize": parseInt(blksz),
                "moduleId": CONFIG.get("module", "lli")
            };

            // var url = "get-free-block";
            var url = "inventory/suggestion";
            axios.post(context + 'ip/' + url + '.do', {'params': data})
                .then(result => {
                    // debugger;
                    this.ipBlockID = 0;
                    if (result.data.responseCode == 2) {
                        this.ipFreeBlockList = [];
                        this.ipRoutingInfo = '';
                        this.ipAvailableRangleList = '';
                        errorMessage(result.data.msg);

                    } else {
                        this.ipFreeBlockList = result.data.payload;
                        this.ipRoutingInfo = '';
                        this.ipAvailableRangleList = '';

                    }

                    // this.loading = false;


                })
                .catch(function (error) {
                });


        },
        onchangeIPBlockRange: function (data) {
            var blockSize = this.ipBlockSize;
            var postData = {
                "block": data,
                "size": blockSize
            };
            // var url = "routing-info/suggestion";
            var url = "get-free-block";
            axios.post(context + 'ip/' + url + '.do', {
                "block": data,
                "size": blockSize
            })
                .then(result => {
                    this.ipAvailableRangleList = result.data.payload;
                    this.ipAvailableIPID = 0;
                    // debugger;
                    // this.loading = false;
                })
                .catch(function (error) {
                });
        },
        onchangeAvailableIP: function (data) {
            //if same element clicked twice the don't select the element
            let isPresent = this.ipAvailableSelected.some(t => (t.fromIP === data.fromIP && t.toIP === data.toIP));
            if (isPresent) {
                return;
            }
            this.ipAvailableSelected.push(
                {
                    "fromIP": data.fromIP,
                    "toIP": data.toIP,
                    "version": this.ipVersion,
                    "type": this.ipType,
                    "regionId": this.regionID,
                }
            );
        },
        ipAvailableDelete: function (index) {
            this.ipAvailableSelected.splice(index, 1);
        },


        // Vendor Selection Conditions
        onChangeSourceType: function (event, linkIndex, office, efrIndex) {
            // if pop selected then set the office address as source address
            if (event.target.value == 1) {
                this.application.vpnApplicationLinks[linkIndex][office].efrs[efrIndex].source = this.getPOPNameById(this.application.vpnApplicationLinks[linkIndex][office].efrs[efrIndex].popId);
            } else {
                this.application.vpnApplicationLinks[linkIndex][office].efrs[efrIndex].source = '';
            }
        },
        onChangeDestinationType: function (event, linkIndex, office, efrIndex) {
            // if customer end selected then set the office address as source address
            if (event.target.value == 3) {
                this.application.vpnApplicationLinks[linkIndex][office].efrs[efrIndex].destination = this.application.vpnApplicationLinks[linkIndex][office].officeAddress; //efrs[efrIndex].popId);
            } else {
                this.application.vpnApplicationLinks[linkIndex][office].efrs[efrIndex].destination = '';
            }
        },

        //Check if already ldp selected
        checkIfAlreadyToLDPSelected: function (linkIndex, office, efrIndex) {
            let returnValue = false;
            if(efrIndex>0){
                //if ldp selected previously
                if(this.application.vpnApplicationLinks[linkIndex].hasOwnProperty("efrs")){
                if( this.application.vpnApplicationLinks[linkIndex][office].efrs[efrIndex-1].destinationType == 2){
                    this.application.vpnApicationLinks[linkIndex][office].efrs[efrIndex].source = this.application.vpnApplicationLinks[linkIndex][office].efrs[efrIndex-1].destination;
                    returnValue = true;
                }
                }
            }
            return returnValue;
        },

        //get POP Name by Id
        getPOPNameById: function (popId) {
            let pop = this.popList.find(obj => {
                return obj.ID === popId
            });
            if (pop) return pop.label;
            else return null;
        },


        globalAdviceNote() {
            return (this.application.vpnApplicationLinks.length>1)
            &&
            (this.application.vpnApplicationLinks.every((val, i, arr)=>val.hasOwnProperty("adviceNoteId") && val.adviceNoteId === arr[0].adviceNoteId))
            &&
            this.application.vpnApplicationLinks.every((val)=>val.state.view==='ADVICE_NOTE');
        },
        globalDemandNote() {
            return (this.application.vpnApplicationLinks.length>1)
                &&
                this.application.vpnApplicationLinks.every((val, i, arr)=>val.hasOwnProperty("demandNoteId") && val.demandNoteId === arr[0].demandNoteId)
                && this.application.vpnApplicationLinks.every((val)=>val.state.view==='DEMAND_NOTE')
                ;
        },
        globalWorkOrder() {
            return ((this.application.vpnApplicationLinks.length>1)
            &&
            this.application.vpnApplicationLinks.every(val=>val.state.view==='WORK_ORDER'));
        },


        //officeBuilder for Client Correction
        officeBuilder: function (end, office, linkIndex) {
            // this.application[end] = this.application.vpnApplicationLinks[linkIndex][office];
            this.$set(this.application, end, this.application.vpnApplicationLinks[linkIndex][office]);

            //office selection type

            //new office provided by user
            if (this.application[end].localLoops[0].isCompleted == false) {
                // this.application[end].officeSelectionType = this.officeSelectionType[1]; // new office provided
                this.$set(this.application[end], 'officeSelectionType', this.officeSelectionType[1]);
            }
            //already existing office
            else {
                // this.application[end].officeSelectionType = this.officeSelectionType[0]; // existing office provided
                this.$set(this.application[end], 'officeSelectionType', this.officeSelectionType[0]);


                if(this.application.vpnApplicationLinks[linkIndex].state.name=='VPN_REQUESTED_CLIENT_FOR_CORRECTION'||
                    this.application.vpnApplicationLinks[linkIndex].state.name== "VPN_WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION"){
                    getVPNOfficeByClientId(this.application.clientId).then(result=>{
                        this.officeList = result;
                        // this.application[end].office = this.officeList.find(office =>  office.id === this.application[end].localLoops[0].officeId);
                        this.$set(this.application[end], 'office', this.officeList.find(office =>  office.id === this.application[end].localLoops[0].officeId));

                    });
                }

                // if existing loop
                if(this.application[end].localLoops[0].isCompleted){
                    // this.application[end].useExistingLoop = this.useExistingLoopList[0];
                    this.$set(this.application[end], 'useExistingLoop', this.useExistingLoopList[0]);

                }else{
                    // this.application[end].useExistingLoop = this.useExistingLoopList[1];
                    this.$set(this.application[end], 'useExistingLoop', this.useExistingLoopList[1]);


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
            if (this.application.vpnApplicationLinks[linkIndex].remoteOfficeTerminalDeviceProvider == 1 && end == 'remoteEnd') { //terminal provider btcl
                this.$set(this.application[end], 'terminalDeviceProvider',  this.loopProviderList[0]);
            } else if (this.application.vpnApplicationLinks[linkIndex].remoteOfficeTerminalDeviceProvider == 2 && end == 'remoteEnd') {
                this.$set(this.application[end], 'terminalDeviceProvider', this.loopProviderList[1]);

            }

            if (this.application.vpnApplicationLinks[linkIndex].localOfficeTerminalDeviceProvider == 1 && end == 'localEnd') { //terminal provider btcl
                this.$set(this.application[end], 'terminalDeviceProvider', this.loopProviderList[0]);
            } else if (this.application.vpnApplicationLinks[linkIndex].localOfficeTerminalDeviceProvider == 2 && end == 'localEnd') {
                this.$set(this.application[end],'terminalDeviceProvider',this.loopProviderList[1]);
            }
            // end of terminal device provider

            //start bandwidth
            if (end == 'remoteEnd') {
                this.application[end].bandwidth = this.application.vpnApplicationLinks[linkIndex].linkBandwidth;
            }
            Object.assign(this.application, this.application);
            this.forceUpdateDOM();

            //end bandwidth

        },
        addRemoteEnd: function () {
            this.application.remoteEnds.push({
                officeName: "",
                officeAddress: null,
                loop: null,
                terminalDeviceProvider: null,
            });
        },

        //Comments Modal
        showCommentModal: function(comments){
            this.commentsList = comments;
            //if(this.commentsList.length>1){

            //}
            $("#comment-modal").modal({show: true});
        },

        //Comments Modal
        addCommentModal: function(link){
            this.link = link;
            //if(this.commentsList.length>1){

            //}
            $("#add-comment-modal").modal({show: true});
        },

        addCommentOnThisLink: function(){
            let app = {
                comment: vue.comment,
                vpnApplicationLinks: [this.link],
            };
            this.postRequestedDataToServer("add-comment",{application: app},window.location.href);

        },
        //on create necessary data builder
        necessaryModificationOnApplicationGet: function(){
            //update the pops of application office localloop

            // let modifiedOffice = ;
            // for(let i=0;i<this.application.offices.length;i++){
            //     // var loops = modifiedOffice[i].loops;
            //     for(let j=0;j<this.application.offices[i].loops.length;j++){
            //         let popId = this.application.offices[i].loops[j].popId;
            //         // delete(modifiedOffice[i].loops[j].popId);
            //         this.application.offices[i].loops[j]["pop"] =this.popList.find(obj => {
            //             return obj.ID === popId
            //         });
            //     }
            // }

            // assign localEnd, globalEnd, & related loop
            for (let i = 0; i < this.application.vpnApplicationLinks.length; i++) {
                //copy the local office to every link
                //for local office
                if (this.application.vpnApplicationLinks[i].hasOwnProperty("localOffice")) {
                    // this.application.localEnd = this.application.vpnApplicationLinks[i].localOffice; // needed for client correction step
                    this.officeBuilder('localEnd', 'localOffice', i);


                    for (let j = 0; j < this.application.vpnApplicationLinks[i].localOffice.localLoops.length; j++) {
                        // if(i==0){
                        //     if(this.application.vpnApplicationLinks[i].localOffice.localLoops[j].hasOwnProperty("pop"))
                        //         this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["popId"] = this.application.vpnApplicationLinks[i].localOffice.localLoops[j].pop.ID;
                        // }else{
                        //     this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["popId"] = this.application.vpnApplicationLinks[i].localOffice.localLoops[j].popId;
                        //     //set feasiblity of local office
                        //     this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["ifrFeasibility"] = this.application.vpnApplicationLinks[i].localOffice.localLoops[0].ifrFeasibility;
                        // }

                        //set local end feasibility
                        // debugger;


                        this.localEndFeasibility = this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["ifrFeasibility"];


                        //if pop exists then find the pop details
                        if (this.application.vpnApplicationLinks[i].localOffice.localLoops[j].popId != 0 && this.localEndPop == null) {

                            // debugger;

                            //set efr if efr length is zero && for specific state
                            if (
                                this.application.vpnApplicationLinks[i].state.name == "VPN_FORWARD_LDGM_FOR_LOOP" ||
                                this.application.vpnApplicationLinks[i].state.name == "VPN_IFR_RESPONDED"
                            ) {
                                if (
                                    this.application.vpnApplicationLinks[i].localOffice.efrs.length == 0
                                    && this.application.vpnApplicationLinks[i].localOffice.localLoops[j].isCompleted != true

                                ) {
                                    if (this.application.vpnApplicationLinks[i].localOffice.localLoops[j].loopProvider == 1) {
                                        this.application.vpnApplicationLinks[i].localOffice.efrs.push({
                                            officeId: this.application.vpnApplicationLinks[i].localOffice.id,
                                            popId: this.application.vpnApplicationLinks[i].localOffice.localLoops[j].popId,
                                            applicationId: this.application.applicationId,
                                            moduleId: this.moduleId,
                                            loopProvider: this.application.vpnApplicationLinks[i].localOffice.localLoops[j].loopProvider,
                                        });
                                    }

                                }
                            }

                            //set the pop information
                            this.localEndPop = this.popList.find(obj => {
                                return this.application.vpnApplicationLinks[i].localOffice.localLoops[j].popId == obj.ID
                            });
                            this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["pop"] = this.popList.find(obj => {
                                return this.application.vpnApplicationLinks[i].localOffice.localLoops[j].popId == obj.ID
                            });

                            //set zone information
                            if (this.application.vpnApplicationLinks[i].localOffice.localLoops[j].zoneId != 0) {
                                this.localEndZone = this.zoneList.find(obj => {
                                    return this.application.vpnApplicationLinks[i].localOffice.localLoops[j].zoneId == obj.id
                                });
                                this.application.vpnApplicationLinks[i].localOffice.localLoops[j]["zone"] = this.zoneList.find(obj => {
                                    return this.application.vpnApplicationLinks[i].localOffice.localLoops[j].zoneId == obj.id
                                });

                            }



                            if(!this.application.vpnApplicationLinks[i].localOffice.localLoops[j].ifrFeasibility
                                || this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].ifrFeasibility
                            ){
                                continue;
                            }

                            // if (this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].zoneId != 0) {
                            //     this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j]["zone"] = this.zoneList.find(obj => {
                            //         return this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].zoneId == obj.id
                            //     });
                            // }

                        }

                    }
                }
                //for remote office

                // {
                if (this.application.vpnApplicationLinks[i].hasOwnProperty("remoteOffice")) {

                    // this.application.remoteEnds.push( this.application.vpnApplicationLinks[i].remoteOffice); //needed for client correction step
                    this.officeBuilder('remoteEnd', 'remoteOffice', i);
                    this.application.remoteEnds.push(this.application.remoteEnd);

                    for (let j = 0; j < this.application.vpnApplicationLinks[i].remoteOffice.localLoops.length; j++) {
                        //if pop exists
                        if (this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].popId != 0) {
                            //set efr if efr length is zero
                            if (
                                this.application.vpnApplicationLinks[i].state.name == "VPN_FORWARD_LDGM_FOR_LOOP" ||
                                this.application.vpnApplicationLinks[i].state.name == "VPN_IFR_RESPONDED"
                            ) {
                                if(this.application.vpnApplicationLinks[i].remoteOffice.hasOwnProperty("efrs")) {
                                    if (this.application.vpnApplicationLinks[i].remoteOffice.efrs.length == 0) {
                                        if (this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].loopProvider == 1
                                            && this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].isCompleted != true
                                            && this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].ifrFeasibility == true
                                        ) {
                                            this.application.vpnApplicationLinks[i].remoteOffice.efrs.push({
                                                officeId: this.application.vpnApplicationLinks[i].remoteOffice.id,
                                                popId: this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].popId,
                                                applicationId: this.application.applicationId,
                                                moduleId: this.moduleId,
                                                loopProvider: this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].loopProvider,

                                            });
                                        }
                                    }
                                }
                            }


                            //set the pop information
                            // this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j]["pop"] = this.popList.find(obj => {
                            //     return this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].popId == obj.ID
                            // });
                            let currentOfficePOP = this.popList.find(obj => {
                                return this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].popId == obj.ID
                            });
                            this.$set(this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j],"pop",currentOfficePOP)
                            // this.$set(this.application[end], 'terminalDeviceProvider', this.loopProviderList[1]);


                            //set the zone information
                            if (this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].zoneId != 0) {
                                this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j]["zone"] = this.zoneList.find(obj => {
                                    return this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].zoneId == obj.id
                                });
                            }
                        }
                    }
                }
                // if(this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].hasOwnProperty("pop"))
                //     this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j]["popId"] = this.application.vpnApplicationLinks[i].remoteOffice.localLoops[j].pop.ID;
                // }

                //if client correction step then call the officeList



            }
        },
        getLinkTitle: function(linkId,linkName)
        {

            return 'Link ID - ' + linkId+ " (" +linkName + ")";

        },
        deleteFromRepeater: function(obj, property, Index){
            return deleteFromRepeater(obj, property,Index);
        },
        checkLinkEFR:function (link) {
            if(link.hasOwnProperty("localOffice")||link.hasOwnProperty("remoteOffice")) {
                if (link.hasOwnProperty("localOffice")) {
                    if (link.localOffice.efrs != null && link.localOffice.efrs.length > 0) {
                        return true;
                    }
                }
                if (link.hasOwnProperty("remoteOffice")) {
                    if (link.remoteOffice.efrs != null && link.remoteOffice.efrs.length > 0) {
                        return true;
                    }
                }
            }
            else{
                return false;
            }
        }
        ,
        checkPaymentCondition() {
            if(!   (this.application.action.length == 2 && this.isNextStatePaymentVerified(this.application.action[1].name))   ) {
                return true;
            }else {
                return this.isSamePayment()
            }
        },
        isSamePayment: function(){
            return this.application.vpnApplicationLinks.length > 1
                && this.application.vpnApplicationLinks.every((val, i, arr) =>{
                if(val.hasOwnProperty("bill") && val.bill.hasOwnProperty("paymentID")){
                    return val.bill.paymentID === arr[0].bill.paymentID
                    && this.inPaymentDoneState(val.state.name)
                    ;

                }
                return false;
            });
        },
        inPaymentDoneState(stateName) {
            return stateName === 'VPN_PAYMENT_DONE'
            || stateName === 'VPN_WITHOUT_LOOP_PAYMENT_DONE'
            || stateName === 'VPN_RECONNECT_APPLICATION_PAYMENT_DONE'
            ;
        },
        isNextStatePaymentVerified(stateName) {
            return stateName === 'VPN_PAYMENT_VERIFIED'
            || stateName === 'VPN_RECONNECT_APPLICATION_PAYMENT_VERIFIED'
        },


        checkIfVPNBandwidthChangeApplicationThenLoadNecessaryData: function(picked){
            if(picked.name =="VPN_DOWNGRADE_APPLICATION_CLIENT_CORRECTION_SUBMITTED"){
                getVPNNetworkLinksByClientId(this.application.client.key).then(result => {
                    //render the link list
                    vue.linkList = result;
                    //render the selected list
                    vue.link = vue.linkList.find( l => {
                        return l.id === vue.application.vpnApplicationLinks[0].networkLinkId
                    });
                    this.changedBandwidth = vue.application.vpnApplicationLinks[0].linkBandwidth;
                    if(vue.linkList.length == 0){
                        errorMessage("The client doesn't have any link.")
                    }
                });
            }
        },

        submitBandwidthChangeForm: function () {
            // if (vue.validate() == false) return;
            // this.loading = true;
            submitBandwidthChangeForm(vue);
        },
        getUrlVars: function() {
            var vars = {};
            var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
                function(m,key,value) {
                    vars[key] = value;
                });
            return vars;
        }

    },
    computed: {},
    mounted() {

    },
    created() {
        let url_string=location.href;
        let url = new URL(url_string);
        let c = url.searchParams.get("type");
        if(c!=null && c=="details"){
            this.detailsPageLoading = true;
            this.details=true;
        }
        else{
            this.detailsPageLoading = false;
        }
        this.loading = true;

        let flowURL  = 'vpn/link/flow-data.do?id=' + applicationID;
        if(this.getUrlVars()["type"]){
            flowURL = 'vpn/link/flow-view-data.do?id=' + applicationID;
        }
        Promise.all(
            [
                axios({method: 'GET', 'url': context + flowURL})
                    .then(result => {
                        // debugger;
                        if (result.data.payload.hasOwnProperty("members")) {
                            // this.application = result.data.payload.members;
                            this.application = Object.assign(this.application, result.data.payload.members);
                        } else {
                            // this.application = result.data.payload;
                            this.application = Object.assign(this.application, result.data.payload);
                        }

                        this.application.applicationID = applicationID;

                        this.application.action.unshift({}); // to resolve radio button related issue

                        //jami for official letter information
                        if(this.application.officialLetterInfo!=null
                            &&this.application.officialLetterInfo.length>0
                            && this.detailsPageLoading) {

                            this.an = this.application.officialLetterInfo.find(i => (i.type === "ADVICE_NOTE" && i.referType === "TO"));
                            this.dn = this.application.officialLetterInfo.find(i => (i.type === "DEMAND_NOTE" && i.referType === "TO"));
                            this.wo = this.application.officialLetterInfo.filter(i => (
                                    i.type === "WORK_ORDER"
                                    && i.referType === "TO"
                                    &&
                                    (i.recipientType == "VENDOR"
                                        || i.recipientType == "BTCL_OFFICIAL"
                                    )
                                    && loggedInAccount.ID == i.recipientId
                                )
                            );
                        }

                        //end

                        // generate DN & skip remove if not govt. client
                        if (loggedInAccount.registrantType !== 1) {
                            let DEMAND_NOTE_GENERATED_SKIP = 6019;
                            this.application.action = this.application.action.filter(a => a.id != DEMAND_NOTE_GENERATED_SKIP);
                        }

                        // populate the popList
                        axios({
                            method: "GET",
                            "url": context + "lli/inventory/get-inventory-options.do?categoryID=" + 4
                        })
                            .then(result => {
                                if (result.data.payload.hasOwnProperty("members")) {
                                    this.popList = result.data.payload.members;
                                } else {

                                    this.popList = result.data.payload;
                                }

                                //populate the zoneList
                                axios({method: 'GET', 'url': context + 'location/allzonesearch.do'})
                                    .then(result => {
                                        if (result.data.payload.hasOwnProperty("members")) {

                                            this.zoneList = result.data.payload.members;
                                        } else {

                                            this.zoneList = result.data.payload;
                                        }
                                        this.necessaryModificationOnApplicationGet();
                                        this.forceUpdateDOM();

                                        //if only one link exists
                                        if(vue.application.vpnApplicationLinks.length==1){
                                            //if single action for correction & change bandwidth correction step
                                            if(vue.application.vpnApplicationLinks[0].hasOwnProperty("action")){
                                            if(vue.application.vpnApplicationLinks[0].action.length == 1){
                                                this.checkIfVPNBandwidthChangeApplicationThenLoadNecessaryData(vue.application.vpnApplicationLinks[0].action[0]);
                                            }
                                            }
                                            vue.application.action = [];
                                            vue.collapse = false;
                                        }
                                    }, error => {
                                    });

                            });
                    }, error => {
                    })]
        ).then( ()=> {
            // alert('all ajax call finished');
            this.loading = false;
        });


    },
});