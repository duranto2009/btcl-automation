var vue = new Vue({
    el: "#btcl-vpn",
    data: {

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

        divShow: true,
    },
    methods: {
        submitNewConnection: function () {
            if(!this.validate())return;
            // let app =this.contract;
            this.contract.bandwidthCapacity = this.application.bandwidthCapacity;
            let app =this.contract;
            
            //set the comment
            if(this.application.comment){
                app['comment'] = this.application.comment;
            }

            postObject({application: app}, context + 'upstream/submit-contract-bandwidth-change-request.do', context+'upstream/request-search.do', this);

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
        getNewRequestFormItems(this);
        let item_url = context + 'upstream/get-all-contracts.do';
        // getDataListByURL(item_url).then(result =>{vue.contractList = result;});

    },
    mounted(){
        this.divShow = false;
    }

});

