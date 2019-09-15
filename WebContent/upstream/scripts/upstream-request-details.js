var vue = new Vue({
    el: "#btcl-upstream",
    data: {
        // typeOfBandwidthList: [{id: 1, value: 'IP Transit'}, {id: 2, value: 'Peering'}, {id: 3, value: 'IPLC'}],
        typeOfBandwidthList: [],
        // btclServiceLocationList: [{id: 1, value: 'Mog'}, {id: 2, value: 'Ctg'}],
        btclServiceLocationList: [],
        // providerLocationList: [{id: 1, value: 'Singa'}, {id: 2, value: 'Pal'}],
        providerLocationList: [],
        // mediaList: [{id: 1, value: 'SMW4'}, {id: 2, value: 'SMW5'}, {id: 3, value: 'ITC'}],
        mediaList: [],
        providerList:[],
        application: {
            typeOfBandwidth: null,
            btclServiceLocation: null,
            providerLocation: null,
            media: null,
            bandwidthCapacity: null,
            comment: null,
            suggestedDate: null,
            action: [],
            formElements: [],
            // description: null,
            // bandwidth: null,
            state:{},
            applicationObject: {},

            linkInfoBSCCL: {},
            btclWOToProvider: {},
            letterOfbtclBackhaul: {},

            linkInfoBSCCLs: [{},],
            btclWOToProviders: [{},],
            letterOfbtclBackhauls: [{},],

            otc: 0,
            mrc: 0,
            bandwidthPrice: 0,

        },
        moduleId: moduleID,
        loading: false,
        picked: null,
        comment: '',
        fileUploadURL: '',
        isAllowed: true,


        showAction: true,

        // year: 0,
        // month: 0,
        // day: 0,
    },
    methods: {
        nextStep: function () {
            if (!this.picked) {
                errorMessage("Please Select an Action.");
                return;
            }

            let commentTitle = " Comment on " + this.picked.description;
            if (this.picked.modal != "") {
                $(this.picked.modal).modal({show: true});
            } else {
                this.loadSweetAlert(commentTitle);
            }
        },

        updateApplicationInformation: function () {
            let app = this.application.applicationObject;
            app['applicationStatus'] = this.picked.name;
            app['state'] = this.picked.id;
            app['comment'] = this.comment;

            //gm international form data
            // if(this.application.hasOwnProperty("bandwidthPrice")){app["bandwidthPrice"] = this.application["bandwidthPrice"];}
            // if(this.application.hasOwnProperty("otc")){app["otc"] = this.application["otc"];}
            // if(this.application.hasOwnProperty("mrc")){app["mrc"] = this.application["mrc"];}
            // if(this.application.hasOwnProperty("contractDuration")){app["contractDuration"] = this.application["contractDuration"];}

            this.gmInternationalFormDataPopulate(app);


            //circuit info
            // app['circuitInformationDTOs'] = [];
            //
            // if(Object.keys(this.application.linkInfoBSCCL).length > 0){
            //     this.application.linkInfoBSCCL['circuitInfoType'] = "LINK_INFO_OF_BSCCL";
            //     this.application.linkInfoBSCCL['applicationId'] = applicationId;
            //     app['circuitInformationDTOs'].push(this.application.linkInfoBSCCL);
            // }
            // if(Object.keys(this.application.btclWOToProvider).length > 0){
            //     this.application.btclWOToProvider['circuitInfoType'] = "BTCLS_WORK_ORDER_TO_PROVIDER";
            //     this.application.btclWOToProvider['applicationId'] = applicationId;
            //     app['circuitInformationDTOs'].push(this.application.btclWOToProvider);
            // }
            // if(Object.keys(this.application.letterOfbtclBackhaul).length > 0){
            //     this.application.letterOfbtclBackhaul['applicationId'] = applicationId;
            //     this.application.letterOfbtclBackhaul['circuitInfoType'] = "LETTERS_OF_BTCL_BACKHAUL";
            //     app['circuitInformationDTOs'].push(this.application.letterOfbtclBackhaul);
            // }

            this.circuitInfoPopulate(app);

            return app;
        },

        gmInternationalFormDataPopulate: function (app) {
            if (this.application.hasOwnProperty("bandwidthPrice") && this.application["bandwidthPrice"] > 0) {
                app["bandwidthPrice"] = this.application["bandwidthPrice"];
            }
            if (this.application.hasOwnProperty("otc") && this.application["otc"] > 0) {
                app["otc"] = this.application["otc"];
            }
            if (this.application.hasOwnProperty("mrc") && this.application["mrc"] > 0) {
                app["mrc"] = this.application["mrc"];
            }
            // if (this.application.hasOwnProperty("contractDuration")) {
            //    app["contractDuration"] = this.application["contractDuration"];
            // }

            let duration = this.checkForContractDuration();
            if(duration >0){
                app["contractDuration"] = duration;
            }

            //for correction step set necessary data
            if (this.application.bandwidthCapacity) {
                app["bandwidthCapacity"] = this.application["bandwidthCapacity"];
            }
            if (this.application.typeOfBandwidth) {
                app["typeOfBandwidthId"] = this.application["typeOfBandwidth"].key;
            }
            if (this.application.btclServiceLocation) {
                app["btclServiceLocationId"] = this.application["btclServiceLocation"].key;
            }
            if (this.application.providerLocation) {
                app["providerLocationId"] = this.application["providerLocation"].key;
            }
            if (this.application.media) {
                app["mediaId"] = this.application["media"].key;
            }

            if (this.application.srfDate) {
                app["srfDate"] = this.application["srfDate"];
            }


        },

        checkForContractDuration: function(){
            let duration = 0;
            if(this.application.hasOwnProperty("year")){
                duration = duration + this.application.year * 365 * 24 * 60 * 60 * 1000;
            }
            if(this.application.hasOwnProperty("month")){
                duration = duration + this.application.month * 30 * 24 * 60 * 60 * 1000;

            }
            if(this.application.hasOwnProperty("day")){
                duration = duration + this.application.day * 24 * 60 * 60 * 1000;
            }
            return duration;

        },


        // circuitInfoPopulate: function(app) {
        //     app['circuitInformationDTOs'] = [];
        //
        //     if(Object.keys(this.application.linkInfoBSCCL).length > 0){
        //         this.application.linkInfoBSCCL['circuitInfoType'] = "LINK_INFO_OF_BSCCL";
        //         this.application.linkInfoBSCCL['applicationId'] = applicationId;
        //         app['circuitInformationDTOs'].push(this.application.linkInfoBSCCL);
        //     }
        //     if(Object.keys(this.application.btclWOToProvider).length > 0){
        //         this.application.btclWOToProvider['circuitInfoType'] = "BTCLS_WORK_ORDER_TO_PROVIDER";
        //         this.application.btclWOToProvider['applicationId'] = applicationId;
        //         app['circuitInformationDTOs'].push(this.application.btclWOToProvider);
        //     }
        //     if(Object.keys(this.application.letterOfbtclBackhaul).length > 0){
        //         this.application.letterOfbtclBackhaul['applicationId'] = applicationId;
        //         this.application.letterOfbtclBackhaul['circuitInfoType'] = "LETTERS_OF_BTCL_BACKHAUL";
        //         app['circuitInformationDTOs'].push(this.application.letterOfbtclBackhaul);
        //     }
        // },


        circuitInfoPopulate: function (app) {
            app['circuitInformationDTOs'] = [];

            // if(Object.keys(this.application.linkInfoBSCCL).length > 0){
            //     this.application.linkInfoBSCCL['circuitInfoType'] = "LINK_INFO_OF_BSCCL";
            //     this.application.linkInfoBSCCL['applicationId'] = applicationId;
            //     app['circuitInformationDTOs'].push(this.application.linkInfoBSCCL);
            // }
            // if(Object.keys(this.application.btclWOToProvider).length > 0){
            //     this.application.btclWOToProvider['circuitInfoType'] = "BTCLS_WORK_ORDER_TO_PROVIDER";
            //     this.application.btclWOToProvider['applicationId'] = applicationId;
            //     app['circuitInformationDTOs'].push(this.application.btclWOToProvider);
            // }
            // if(Object.keys(this.application.letterOfbtclBackhaul).length > 0){
            //     this.application.letterOfbtclBackhaul['applicationId'] = applicationId;
            //     this.application.letterOfbtclBackhaul['circuitInfoType'] = "LETTERS_OF_BTCL_BACKHAUL";
            //     app['circuitInformationDTOs'].push(this.application.letterOfbtclBackhaul);
            // }


            for (let i = 0; i < this.application.linkInfoBSCCLs.length; i++) {
                let linkInfoBSCCL = this.application.linkInfoBSCCLs[i];
                if (Object.keys(linkInfoBSCCL).length > 0) {
                    linkInfoBSCCL['circuitInfoType'] = "LINK_INFO_OF_BSCCL";
                    linkInfoBSCCL['applicationId'] = applicationId;
                    app['circuitInformationDTOs'].push(linkInfoBSCCL);
                }
            }

            for (let i = 0; i < this.application.btclWOToProviders.length; i++) {
                let btclWOToProvider = this.application.btclWOToProviders[i];
                if (Object.keys(btclWOToProvider).length > 0) {
                    btclWOToProvider['circuitInfoType'] = "BTCLS_WORK_ORDER_TO_PROVIDER";
                    btclWOToProvider['applicationId'] = applicationId;
                    app['circuitInformationDTOs'].push(btclWOToProvider);
                }
            }

            for (let i = 0; i < this.application.letterOfbtclBackhauls.length; i++) {
                let letterOfbtclBackhaul = this.application.letterOfbtclBackhauls[i];
                if (Object.keys(letterOfbtclBackhaul).length > 0) {
                    letterOfbtclBackhaul['circuitInfoType'] = "LETTERS_OF_BTCL_BACKHAUL";
                    letterOfbtclBackhaul['applicationId'] = applicationId;
                    app['circuitInformationDTOs'].push(letterOfbtclBackhaul);
                }
            }

            //for upstream contract extension
            if (app['circuitInformationDTOs'].length == 0) {
                //circuit info insertion
                if (this.application.hasOwnProperty("contractObject")) {
                    app["circuitInformationDTOs"] = this.application["contractObject"]["circuitInformationDTOs"];
                }
            }


        },


        processApplication: function () {
            let app = this.updateApplicationInformation();
            postObject({application: app}, context + 'upstream/' + this.picked.url + '.do', context + 'upstream/request-search.do', this);
        },

        loadSweetAlert: function (commentTitle) {
            swal(commentTitle, {
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
                        this.comment = value + '';
                        // if (this.picked.name == "VPN_PRECHECK_DONE") {
                        //     Object.assign(this.application, applicationBuilder(vue));
                        // }
                        this.processApplication();
                        // debugger;
                    }
                });
        },

        //repeater related necessary method
        addEmptyObject: function (obj) {
            this.application[obj].push({});
            // addEmptyObject(this.application,obj);
        },

        deleteFromRepeater: function (obj, property, Index) {
            return deleteFromRepeater(obj, property, Index);
        },

        setCircuitInfo: function () {

            let linkInfoBSCCLs = this.application.contractObject.circuitInformationDTOs.filter(c => c.circuitInfoType === LINK_INFO_OF_BSCCL);
            if (linkInfoBSCCLs) {
                this.application.linkInfoBSCCLs = linkInfoBSCCLs
            }

            let btclWOToProviders = this.application.contractObject.circuitInformationDTOs.filter(c => c.circuitInfoType === BTCLS_WORK_ORDER_TO_PROVIDER);
            if (btclWOToProviders) {
                this.application.btclWOToProviders = btclWOToProviders
            }

            let letterOfbtclBackhauls = this.application.contractObject.circuitInformationDTOs.filter(c => c.circuitInfoType === LETTERS_OF_BTCL_BACKHAUL);
            if (letterOfbtclBackhauls) {
                this.application.letterOfbtclBackhauls = letterOfbtclBackhauls
            }

        },

        msToTime: function (duration) {
            var milliseconds = parseInt((duration % 1000) / 100)
                , seconds = parseInt((duration / 1000) % 60)
                , minutes = parseInt((duration / (1000 * 60)) % 60)
                , hours = parseInt((duration / (1000 * 60 * 60)) % 24);

            hours = (hours < 10) ? "0" + hours : hours;
            minutes = (minutes < 10) ? "0" + minutes : minutes;
            seconds = (seconds < 10) ? "0" + seconds : seconds;

            return hours + ":" + minutes + ":" + seconds; // + "." + milliseconds;
        },

        convertMillisecondsToYearMonthDay: function(time){
            return convertMillisecondsToYearMonthDay(time)
        }


    },
    created() {
        // getNewRequestFormItems(this);
        this.loading = true;
        let flowURL = 'upstream/application-data.do?id=' + applicationId;
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
                        this.application.applicationId = applicationId;

                        if (this.application.state.name === 'UPSTREAM_APPROVED_VENDOR_AND_FORWARDED_TO_DGM_CORE_UPSTREAM') {
                            this.isAllowed = false;
                        }
                        this.fileUploadURL = context + "file/upload.do";


                        this.application.action.unshift({}); // to resolve radio button related issue
                    }, error => {
                    }),
                getNewRequestFormItems(this), // render necessary data for correction step
            ]
        ).then(function (values) {
            vue.loading = false;

            //set values for correction
            vue.application.bandwidthCapacity = vue.application.applicationObject.bandwidthCapacity;

            vue.application.typeOfBandwidth = vue.typeOfBandwidthList.find(t => t.key == vue.application.applicationObject.typeOfBandwidthId);

            vue.application.btclServiceLocation = vue.btclServiceLocationList.find(t => t.key == vue.application.applicationObject.btclServiceLocationId);

            vue.application.providerLocation = vue.providerLocationList.find(t => t.key == vue.application.applicationObject.providerLocationId);

            vue.application.provider = vue.providerList.find(t => t.key == vue.application.applicationObject.selectedProviderId);

            vue.application.media = vue.mediaList.find(t => t.key == vue.application.applicationObject.mediaId);


            //set circuit info's
            if (vue.application.hasOwnProperty("contractObject")) vue.setCircuitInfo();


        });


        if((new URLSearchParams(window.location.search)).get("type") === "details"){
            this.showAction = false;
        }

    },
});

