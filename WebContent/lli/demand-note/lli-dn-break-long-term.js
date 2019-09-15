let vue = new Vue({
    el: "#btcl-application",
    data: {
        applicationID: appId,
        contextPath: context,
        dn: {},
        app: {
            client: {}
        },
        loading :false,
        isCDGM : isCDGM
    },

    methods: {
        generateDN: function () {
            this.loading=true;
            Promise.resolve(
                axios.post(context + "lli/dn/short-bill/generate.do", {
                    dn: this.dn,
                    appId: this.applicationID,
                }).then(result => {
                    if (result.data.responseCode === 1) {
                        toastr.success('Demand Note has been generated', 'Success');
                        window.location.replace(context + "lli/revise/search.do");
                    } else {
                        errorMessage(result.data.msg);
                    }
                }).catch(function (error) {
                    console.log(error);
                })
            ).then(()=>this.loading=false)

        },

        getData (urlApp, urlDN) {
            this.loading = true;
            Promise.all(
                [
                    axios({ method: 'GET', 'url': urlApp})
                        .then(result => {
                            if(result.data.responseCode === 1) {
                                if(result.data.payload.hasOwnProperty("members")){
                                    this.app = result.data.payload.members;
                                }
                                else{
                                    this.app = result.data.payload;
                                }
                            }else {
                                errorMessage(result.data.msg);
                            }
                        }).catch( error => {
                        console.log(error);
                    }),

                    axios({ method: 'GET', 'url': urlDN})
                        .then(result => {
                            if(result.data.responseCode === 1) {
                                this.dn = result.data.payload;
                            }else {
                                errorMessage(result.data.msg);
                            }
                        }).catch( error => {
                        console.log(error);
                    })
                ]).then(()=>this.loading=false);
        },
        getContractBreakingFine: function () {
            return floatParse(this.dn.contractBreakingFine);
        },
        getDiscountPercentage: function () {
            return floatParse(this.dn.discountPercentage) / 100.0;
        },
        getOtherCost: function () {
            return floatParse(this.dn.otherCost);
        },
        getVatCalculable: function () {
            return this.getContractBreakingFine() + this.getOtherCost();
        },
        getVatPercentage: function () {
            return floatParse(this.dn.VatPercentage) / 100.0;
        },
        getVat: function () {
            return this.getVatCalculable() * this.getVatPercentage();
        },
        getDiscount: function () {
            return this.getGrandTotal() * this.getDiscountPercentage();
        },
        getGrandTotal: function () {
            return this.getVatCalculable();
        },
        getTotalPayable: function () {
            return this.getGrandTotal() - this.getDiscount();
        },
        getNetPayable: function () {
            return this.getTotalPayable() + this.getVat();
        },

    },
    mounted() {
        this.getData(
            context + 'lli/revise/revise-client-get-flow.do?id=' + this.applicationID,
            context + 'lli/dn/break-long-term/autofill.do?appId=' + this.applicationID
        );
    },
    computed: {
        vat: function () {
            return parseFloat(this.getVat().toFixed(2));
        },
        discount: function () {
            return parseFloat(this.getDiscount().toFixed(2));
        },
        netPayable: function () {
            return parseFloat(this.getNetPayable().toFixed(2));
        },
        grandTotal: function () {
            return parseFloat(this.getGrandTotal().toFixed(2));
        },
        totalPayable: function () {
            return parseFloat(this.getTotalPayable().toFixed(2));
        }
    }
});