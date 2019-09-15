var vue = new Vue({
    el: "#btcl-upstream",
    data: {
        contractList: [],
        contract: {},
        // application.state.view
        application:{

            state: {
                view: "circuit-info"
            },

            linkInfoBSCCL: {},
            btclWOToProvider: {},
            letterOfbtclBackhaul: {},

            linkInfoBSCCLs: [{},],
            btclWOToProviders: [{},],
            letterOfbtclBackhauls: [{},],


            contractDetail: true,
        },

        typeOfBandwidthList: [],
        btclServiceLocationList: [],
        providerLocationList: [],
        providerList:[],
        mediaList: [],
        loading: true,
        fileUploadURL: '',
    },
    methods: {
        getContractByHistoryID: function(id){
            this.contract = this.contractList[id];
            this.setCircuitInfo();
        },

        getObjectValueFromObjectListByKey: function(objectList,key){
            return getObjectValueFromObjectListByKey(objectList,key)
        },

        // setCircuitInfo: function(){
        //
        //     let linkInfoBSCCL = this.contract.circuitInformationDTOs.find( c=> c.circuitInfoType  === LINK_INFO_OF_BSCCL);
        //     if(linkInfoBSCCL){this.application.linkInfoBSCCL = linkInfoBSCCL}
        //
        //     let btclWOToProvider = this.contract.circuitInformationDTOs.find( c=> c.circuitInfoType  === BTCLS_WORK_ORDER_TO_PROVIDER);
        //     if(btclWOToProvider){this.application.btclWOToProvider = btclWOToProvider}
        //
        //     let letterOfbtclBackhaul = this.contract.circuitInformationDTOs.find( c=> c.circuitInfoType  === LETTERS_OF_BTCL_BACKHAUL);
        //     if(letterOfbtclBackhaul){this.application.letterOfbtclBackhaul = letterOfbtclBackhaul}
        //
        // },
        setCircuitInfo: function(){

            let linkInfoBSCCLs = this.contract.circuitInformationDTOs.filter( c=> c.circuitInfoType  === LINK_INFO_OF_BSCCL);
            if(linkInfoBSCCLs){this.application.linkInfoBSCCLs = linkInfoBSCCLs}

            let btclWOToProviders = this.contract.circuitInformationDTOs.filter( c=> c.circuitInfoType  === BTCLS_WORK_ORDER_TO_PROVIDER);
            if(btclWOToProviders){this.application.btclWOToProviders = btclWOToProviders}

            let letterOfbtclBackhauls = this.contract.circuitInformationDTOs.filter( c=> c.circuitInfoType  === LETTERS_OF_BTCL_BACKHAUL);
            if(letterOfbtclBackhauls){this.application.letterOfbtclBackhauls = letterOfbtclBackhauls}

        },

        updateCircuitInformation: function(){
            let app = this.updateApplicationInformation();
            postObject({application: app}, context + 'upstream/'+'update-circuit-info'+'.do', '', this);
        },

        updateApplicationInformation: function(){
            let app = {
            };
            this.circuitInfoPopulate(app);

            return app;
        },

        // circuitInfoPopulate: function(app) {
        //     app['circuitInformationDTOs'] = [];
        //
        //     if(Object.keys(this.application.linkInfoBSCCL).length > 0){
        //         this.application.linkInfoBSCCL['circuitInfoType'] = "LINK_INFO_OF_BSCCL";
        //         this.application.linkInfoBSCCL['applicationId'] = this.contract.applicationId;
        //         app['circuitInformationDTOs'].push(this.application.linkInfoBSCCL);
        //     }
        //     if(Object.keys(this.application.btclWOToProvider).length > 0){
        //         this.application.btclWOToProvider['circuitInfoType'] = "BTCLS_WORK_ORDER_TO_PROVIDER";
        //         this.application.btclWOToProvider['applicationId'] = this.contract.applicationId;
        //         app['circuitInformationDTOs'].push(this.application.btclWOToProvider);
        //     }
        //     if(Object.keys(this.application.letterOfbtclBackhaul).length > 0){
        //         this.application.letterOfbtclBackhaul['applicationId'] = this.contract.applicationId;
        //         this.application.letterOfbtclBackhaul['circuitInfoType'] = "LETTERS_OF_BTCL_BACKHAUL";
        //         app['circuitInformationDTOs'].push(this.application.letterOfbtclBackhaul);
        //     }
        // },

        addEmptyObject: function(obj){
            this.application[obj].push({});
        },

        deleteFromRepeater: function (obj, property, Index) {
            return deleteFromRepeater(obj, property, Index);
        },

        circuitInfoPopulate: function(app) {
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


            for (let i = 0; i < this.application.linkInfoBSCCLs.length; i++){
                let linkInfoBSCCL = this.application.linkInfoBSCCLs[i];
                if(Object.keys(linkInfoBSCCL).length > 0){
                    linkInfoBSCCL['circuitInfoType'] = "LINK_INFO_OF_BSCCL";
                    // linkInfoBSCCL['applicationId'] = this.contract.applicationId;
                    linkInfoBSCCL['contractId'] = this.contract.contractId;
                    app['circuitInformationDTOs'].push(linkInfoBSCCL);
                }
            }

            for (let i = 0; i < this.application.btclWOToProviders.length; i++){
                let btclWOToProvider = this.application.btclWOToProviders[i];
                if(Object.keys(btclWOToProvider).length > 0){
                    btclWOToProvider['circuitInfoType'] = "BTCLS_WORK_ORDER_TO_PROVIDER";
                    // btclWOToProvider['applicationId'] = this.contract.applicationId;
                    btclWOToProvider['contractId'] = this.contract.contractId;
                    app['circuitInformationDTOs'].push(btclWOToProvider);
                }
            }

            for (let i = 0; i < this.application.letterOfbtclBackhauls.length; i++){
                let letterOfbtclBackhaul = this.application.letterOfbtclBackhauls[i];
                if(Object.keys(letterOfbtclBackhaul).length > 0){
                    letterOfbtclBackhaul['circuitInfoType'] = "LETTERS_OF_BTCL_BACKHAUL";
                    // letterOfbtclBackhaul['applicationId'] = this.contract.applicationId;
                    letterOfbtclBackhaul['contractId'] = this.contract.contractId;
                    app['circuitInformationDTOs'].push(letterOfbtclBackhaul);
                }
            }


        },

        showFormattedHistoryName: function(arr){
            arr = arr.split('_');
            let str = "";
            let modifiedarr = arr.map( string => {
                let low = string.toLowerCase();
                return low.charAt(0).toUpperCase() + low.slice(1);
            });
            let ans = modifiedarr.reduce((acc,val) => {
                return acc + " " + val;
            }, "");

            return ans;
        },

        convertMillisecondsToYearMonthDay: function(time){
            return convertMillisecondsToYearMonthDay(time)
        }



    },
    created() {
        this.loading = true;
        this.fileUploadURL = context + "file/upload.do";
        getNewRequestFormItems(this);
        Promise.all(
            [

                axios({method: 'GET', 'url': context + 'upstream/contract-data.do?historyId=' + historyId})
                    .then(result => {
                        vue.loading = false;
                        // debugger;
                        if (result.data.payload.hasOwnProperty("members")) {
                            // this.application = result.data.payload.members;
                            vue.contractList = Object.assign(vue.contractList, result.data.payload.members);
                        }
                        else {
                            // this.application = result.data.payload;
                            vue.contractList = Object.assign(vue.contractList, result.data.payload);
                        }
                        if(this.contractList.length == 0){
                            errorMessage("No contract found with the historyId - " + historyId)
                            return;
                        }
                        vue.contractList.reverse();
                        vue.contract = vue.contractList[0];

                        //set ckt info
                        vue.setCircuitInfo();

                    }, error => {
                    }),

            ]

        ).then(function (values) {
            // alert('all ajax call finished');
            vue.loading = false;
        });
    },
});

