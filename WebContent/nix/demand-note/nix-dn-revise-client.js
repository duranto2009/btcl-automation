let vue = new Vue({
    el: "#btcl-application",
    data: {
        applicationID: appId,
        contextPath: context,
        dn: {},
        app: {
            client : {
                
            }
        },
        loading: false
    },
    methods: {
        getDNAutoFilled: function () {
            axios({
                method: 'GET',
                url: context + 'nix/dn/autofill.do?appId=' + this.applicationID + '&appGroup=5'
            }).then(result => {
                if (result.data.responseCode === 1) {
                    this.dn = result.data.payload;
                } else {
                    errorMessage(result.data.msg);
                }
            }).catch(error => {
                console.log(error);
            });
        },
        generateDN: function () {
            this.loading=true;
            let url_string=location.href;
            let url = new URL(url_string);
            let c = url.searchParams.get("nextstate");
            Promise.resolve(
                axios.post(context + "nix/dn/generate.do", {
                    dn: this.dn,
                    appId: this.applicationID,
                    nextState:c,
                    appGroup: 5
                }).then(result => {
                    if (result.data.responseCode === 1) {
                        toastr.success('Demand Note has been generated', 'Success');
                        // window.location.href = context + "nix/pdf/view.do?type=demand-note&id=" + result.data.payload;
                        window.location.href = context + "nix/revise/search.do";
                    } else {
                        errorMessage(result.data.msg);
                    }
                }).catch(function (error) {
                    console.log(error);
                })
            ).then(()=>this.loading=false);

        },

        getApplication: function () {
            axios({
                method: 'GET',
                url: context + 'nix/revise/revise-client-get-flow.do?id=' + this.applicationID
            }).then(result => {
                if (result.data.responseCode === 1) {
                    if (result.data.payload.hasOwnProperty("members")) {
                        this.app = result.data.payload.members;
                    }
                    else {
                        this.app = result.data.payload;
                    }
                } else {
                    errorMessage(result.data.msg);
                }
            }).catch(error => {
                console.log(error);
            });
        },
        getRegFee : function () {
            return floatParse(this.dn.registrationFee);
        },
        getSecurity: function(){
            return floatParse(this.dn.securityMoney);
        },
        getPortCharge() {
            return floatParse(this.dn.nixPortCharge);
        },
        getPortUpgradeCharge() {
            return floatParse(this.dn.nixPortUpgradeCharge);
        },
        getPortDowngradeCharge() {
            return floatParse(this.dn.nixPortDowngradeCharge);
        },
        getLocalLoopCharge() {
            return floatParse(this.dn.localLoopCharge);
        },
        getReconnectionCharge() {
            return floatParse(this.dn.reconnectCharge);
        },
        getClosingCharge() {
            return floatParse(this.dn.closingCharge);
        },
        getAdvance: function() {
            return floatParse(this.dn.advanceAdjustment);
        },
        getVatCalculable : function() {
            return this.getRegFee() +
                this.getPortCharge() +
                this.getLocalLoopCharge() +
                this.getPortUpgradeCharge() +
                this.getPortDowngradeCharge() +
                this.getReconnectionCharge() +
                this.getClosingCharge() +
                this.getAdvance()


                ;
        },
        getVatPercentage : function () {
            return floatParse(this.dn.VatPercentage) / 100.0;
        },
        getVat : function () {
            return Math.ceil(this.getVatCalculable() * this.getVatPercentage());
        },
        getGrandTotal : function () {
            return this.getVatCalculable() + this.getSecurity();
        },
        getDiscountPercentage : function () {
            return floatParse(this.dn.discountPercentage) / 100.0;
        },
        getDiscount : function () {
            return 0 * this.getDiscountPercentage(); // TODO
        },
        getTotalPayable : function () {
            return this.getGrandTotal() - this.getDiscount();
        },
        getNetPayable : function() {
            return this.getTotalPayable() + this.getVat() ;
        }



    },
    mounted() {
        this.loading  = true;
        Promise.all([
            this.getApplication(),
            this.getDNAutoFilled()
        ]).then(()=>this.loading = false);

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