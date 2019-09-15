var vue = new Vue({
    el: "#btcl-vpn",
    data: {

        divShow : true,
        // typeOfBandwidthList: [{id: 1, value: 'IP Transit'}, {id: 2, value: 'Peering'}, {id: 3, value: 'IPLC'}],
        typeOfBandwidthList: [],
        // btclServiceLocationList: [{id: 1, value: 'Mog'}, {id: 2, value: 'Ctg'}],
        btclServiceLocationList: [],
        // providerLocationList: [{id: 1, value: 'Singa'}, {id: 2, value: 'Pal'}],
        providerLocationList: [],
        // mediaList: [{id: 1, value: 'SMW4'}, {id: 2, value: 'SMW5'}, {id: 3, value: 'ITC'}],
        mediaList: [],

        application: {
            typeOfBandwidth: null,
            btclServiceLocation: null,
            providerLocation: null,
            media: null,
            bandwidthCapacity: null,
            comment: null,
            suggestedDate: null,
            // description: null,
            // bandwidth: null,
        },

        moduleId: moduleID,
        loading: false,
        loader: false,


        contractList: [],
        contract: null,

        contractExtensionTo: null,
    },
    methods: {
        submitNewConnection: function () {
            if(!this.validate())return;
            let app =this.contract;
            //set the comment
            if(this.application.comment){
                app['comment'] = this.application.comment;
            }

            let duration = this.checkForContractDuration();
            // if(duration >0){
            //     app["contractDuration"] = duration;
            // }
            this.contract.contractDuration = duration;
            postObject({application: app}, context + 'upstream/submit-contract-extension-request.do', context+'upstream/request-search.do', this);

        },
        validate: function () {
            // if (this.contractExtensionTo == null) {
            //     errorMessage("Extension field must be filled.");
            //     return false;
            // }
            //
            // if (this.contractExtensionTo <= this.contract.contractDuration) {
            //     errorMessage("Extension field must be greater than the original contract duration.");
            //     return false;
            // }

            return true;
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

        getObjectValueFromObjectListByKey: function(objectList,key){
            return getObjectValueFromObjectListByKey(objectList,key)
        },

        convertMillisecondsToYearMonthDay: function(time){
            return convertMillisecondsToYearMonthDay(time)
        },
        showSearchField: function(){
            document.getElementById("blah").style.display = "block";
            this.divShow = false;
        },
        getContractByContractId: function(contractId){
            let item_url =  context + "upstream/contract-data-by-id.do?contractId="+contractId;
            this.loader = true;
            this.loading = true;
            getDataListByURL(item_url).then(result =>{
                // vue.contractList = result;
                this.contract = result;
                vue.loader = false;
                vue.loading = false;
            });

        }


    },
    created() {
        // this.loader = true;
        getNewRequestFormItems(this);
        // let item_url = context + 'upstream/get-all-contracts.do';
        // getDataListByURL(item_url).then(result =>{
        //     vue.contractList = result;
        //     vue.loader = false;
        // });

    },
    mounted(){
        this.divShow = false;
    }

});

