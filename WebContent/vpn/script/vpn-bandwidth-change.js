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
        },
        moduleId: moduleID,
        loading: false,
        linkList: [],
        link: null,
        changedBandwidth: 0,
    },
    methods: {
        submitBandwidthChangeForm: function () {
            if (vue.validate() == false) return;
            // this.loading = true;
            submitBandwidthChangeForm(vue);
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
            if (this.changedBandwidth == 0) {
                errorMessage("Change the Bandwidth.");
                return false;
            }
            
            if (this.application.suggestedDate == null){
                errorMessage("Please select a suggested date.");
                return false;
            }

            // end: form validation'
            return true;
        },
    },
    watch: {
        'application.client': function () {
            getVPNNetworkLinksByClientId(this.application.client.key).then(result => {
                this.linkList = result;
                if(this.linkList.length == 0){
                    errorMessage("The client doesn't have any link.")
                }
            });
        }
    }
});