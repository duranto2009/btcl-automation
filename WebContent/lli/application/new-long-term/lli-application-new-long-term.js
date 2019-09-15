var vue = new Vue({
    el: "#btcl-application",
    data: {
        revise: {
            contextPath: context,
            existingBW: '',
            existingLT: '',
            bwData : false,
        }
    },
    methods: {
        submitData: function () {
            if (isNaN(this.revise.bandwidth)) {
                toastr.error("Bandwidth must be a number", "Invalid input");
                return;
            }

            if (Number(this.revise.bandwidth) <= 0) {
                toastr.error("Bandwidth must be more than 0", "Invalid input");
                return;
            }

            if (this.revise.bandwidth > this.revise.existingBW) {
                toastr.error("Requested bandwidth must be less than Existing Active bandwidth.", "Invalid input");
                return;
            }




            var url = "newlongterm";
            axios.post(context + 'lli/revise/' + url + '.do', {'data': JSON.stringify(this.revise)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/revise/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {
                });
        },
        goView: function (result) {
            if (result.data.responseCode == 2) {
                toastr.error(result.data.msg);
            } else if (result.data.responseCode == 1) {
                toastr.success("Your request has been processed", "Success");
                window.location.href = context + 'lli/application/view.do?id=' + result.data.payload;
            }
            else {
                toastr.error("Your request was not accepted", "Failure");
            }
        },
        getTotalBW: function (clientID) {
            let self = this;
            axios({method: 'GET', 'url': context + 'lli/connection/total-active-bw.do?clientId=' + clientID})
                .then(result => {
                    if (result.data.responseCode == 1) self.revise.existingBW = result.data.payload;
                    else {
                        toastr.error(result.data.msg, 'Failure');
                    }
                }, error => {
                });
        },
        getTotalLT: function (clientID) {
            let self = this;
            axios({method: 'GET', 'url': context + 'lli/longterm/total.do?clientId=' + clientID})
                .then(result => {
                    if (result.data.responseCode == 1) self.revise.existingLT = result.data.payload;
                    else {
                        toastr.error(result.data.msg, 'Failure');
                    }
                }, error => {
                });
        },
        getBW: function (clientID) {
            if (clientID === undefined) {
                this.revise.existingBW = '';
                this.revise.existingLT = '';
            } else {
                Promise.all(
                    [
                        this.getTotalBW(clientID),
                        this.getTotalLT(clientID)
                ]).then(() => this.revise.bwData = true);
            }
        }
    }
});