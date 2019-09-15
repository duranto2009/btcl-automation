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

        providerList: [],

        application: {
            typeOfBandwidth: null,
            btclServiceLocation: null,
            providerLocation: null,
            media: null,
            bandwidthCapacity: null,
            comment: null,
            suggestedDate: null,
            provider: null,
            // description: null,
            // bandwidth: null,
        },

        moduleId: moduleID,
        loading: false,
    },
    methods: {
        submitNewConnection: function () {
            if(!this.validate())return;
            let app = newUpstreamRequestApplicationBuilder(vue);
            postObject({application: app}, context + 'upstream/submit-new-request.do', context+'upstream/request-search.do', this);

        },
        validate: function () {
            // start: form validation
            if (this.application.typeOfBandwidth == null) {
                errorMessage("Type of Bandwidth must be selected.");
                return false;
            }
            if (this.application.bandwidthCapacity == null) {
                errorMessage("Bandwidth Capacity must be entered.");
                return false;
            }

            if (this.application.btclServiceLocation== null) {
                errorMessage("BTCL Service Location must be selected.");
                return false;
            }

            if (this.application.providerLocation== null) {
                errorMessage("BTCL Provider Location must be selected.");
                return false;
            }

            if (this.application.media == null) {
                errorMessage("Media Type must be selected.");
                return false;
            }


            // if (this.application.suggestedDate == null) {
            //     errorMessage("Provide a suggested date.");
            //     return false;
            // }
            // end: form validation'
            return true;
        },

    },
    created() {
        getNewRequestFormItems(this);
    },
    watch: {
        'application.client': function () {

            getVPNOfficeByClientId(this.application.client.key).then(result => {
                this.officeList = result;
            });
        }
    }
});

