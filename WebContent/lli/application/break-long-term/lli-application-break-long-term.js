var vue = new Vue({
    el: "#btcl-application",
    data: {
        revise: {
            contextPath: context,
            contractOptionListByClientID: [],
            application: {},
            existingContract: {}
        }
    },
    methods: {
        clientSelectionCallback: function (clientID) {
            if (clientID === undefined) {
                this.revise.contractOptionListByClientID = [];
                this.revise.existingContract = {};
                this.revise.contract = undefined;
            } else {
                axios({
                    method: 'GET',
                    url: context + 'lli/longterm/get-longterm-by-client-id.do?id=' + clientID
                }).then(result => {
                    this.revise.existingContract = {};
                    this.revise.contract = undefined;
                    this.revise.contractOptionListByClientID = result.data.payload;
                }, error => {
                });
            }
        },
        getLongTermContractByID: function (selectedOption, id) {
            this.existingContract = {};
            axios({
                method: 'GET',
                url: context + 'lli/longterm/get-longterm.do?id=' + selectedOption.ID
            }).then(result => {
                this.revise.existingContract = result.data.payload;
            }, error => {
            });
        },
        submitData: function () {
            if (isNaN(this.revise.bandwidth)) {
                toastr.error("Bandwidth must be a number", "Invalid input");
                return;
            }

            if (Number(this.revise.bandwidth) <= 0) {
                toastr.error("Bandwidth must be more than 0", "Invalid input");
                return;
            }

            if (this.revise.bandwidth > this.revise.existingContract.bandwidth) {
                toastr.error("Can not break more B/W than existing", "Invalid input");
                return;
            }

            var url = context + "lli/revise/break-longterm.do";
            axios.post(url, {'data': JSON.stringify(this.revise)}).then(result => {
                if (result.data.responseCode == 2) {
                    toastr.error(result.data.msg);
                } else if (result.data.responseCode == 1) {
                    toastr.success("Your request has been processed", "Success");
                    window.location.href = context + 'lli/revise/search.do';
                }
                else {
                    toastr.error("Your request was not accepted", "Failure");
                }
            }).catch(function (error) {
            });
        }
    }
});