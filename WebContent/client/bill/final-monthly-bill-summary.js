import BTCLDownloadButton from '../../vue-components/btclDownloadBtn.js';
import BTCLTable from '../../vue-components/btclTable.js';


var thirdTable = new Vue({

    components: {
        "btcl-download-btn": BTCLDownloadButton,
        "btcl-table": BTCLTable
    },


    el: '#monthly-bill-summary',

    data: {
        params: {},
        monthStr: null,
        app: {},

        existingInfo: {},
        existingConnections: [],
        existingApplications: [],
        existingOwnerApplications:[],
        existingReviseApplications:[],
        existingVPNLinks:[],
        existingVPNAppLinks:[],

        existingNixConnections: [],
        existingNixApplications: [],
        existingNixReviseApplications:[],


        existingCoLocationConnections: [],
        existingCoLocationApplications: [],

        contextPath: context,
        monthlybillSummary: {client: {}},
        result: false,
        finalGenerated:false,
        finalVpnGenerated:false,
        finalNixGenerated:false,
        finalColocGenerated:false,
        isAdmin: false,
        regular: null,
        cache: null,
        localLoop: null,
        adjustment: null,
        dnAdjustment: null,
        billIssueDate: null,
        lastDayOfPayment: null,
        grandBtclAmount: 0,
        grandTotalAmount: "",
        grandVat: 0,
        grandTotal: 0,
        dateFrom: "",
        dateTo: "",
        lastDate: "",
        selectedElement: [],

        securityDeposit: 0,

        lastPayDate: "",


        client: {},
        clientDetails: {},

        moduleId: '',
        modules: [],

        submitClicked: false,

        billingRangeBreakDownContent: {},
        longTermContractBreakDownContent: {},
        mbpsBreakDownContent: {},
        invoiceId: 0

    },

    methods: {

        getModules: function () {
            axios.get(context + "ClientType/getAllModules.do").then(res => {
                if (res.data.responseCode === 1) {
                    this.modules = res.data.payload;
                } else {
                    toastr.error("Error Loading modules", "Failure");
                }
            });
        },
        viewMonthlyBillSummary() {
            window.open(context + "pdf/view/monthly-bill.do?billId=" + this.app.ID + "&module=7", "_blank");
        },
        takaFormat: function (amount) {
            return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
        },
        findSummaryOfMultipleMonth: function () {
            this.monthlybillSummary.suggestedDate = new Date($('input[name="from"]').val()).getTime();

            // debugger;

            var clientId = this.client.key;


            var module = this.moduleId;

            // if(this.monthlybillSummary.client.ID>0){
            // 	clientId=this.monthlybillSummary.client.ID;
            //
            this.submitClicked = true;

            this.lastPayDate = new Date($('input[name="lastDate"]').val()).getTime();
            this.lastDate = new Date($('input[name="lastDate"]').val()).toDateString();

            Promise.resolve(
                axios({
                    method: 'GET',
                    'url': context + 'common/bill/search-final.do?id=' + clientId + '&module=' + module + '&lastPayDate=' + this.lastPayDate
                })
                    .then(result => {

                        if (result.data.responseCode == 1) {
                            // debugger;
                            // $('#detailinfomodal').modal({hide: true});
                            this.app = result.data.payload.value;
                            this.invoiceId = result.data.payload.key;
                            this.setCalculatedValue();
                            this.getSecurity();
                            if(this.securityDeposit<this.grandTotal) {
                                this.convertTotal(this.grandTotal - this.securityDeposit);
                            }else{
                                this.convertTotal(0);
                            }


                            // debugger;y
                            //load the modal

                            if (this.app != null) {
                                if (this.app.length > 0) {
                                    this.result = true;
                                    this.billIssueDate = new Date(this.app[0].createdDate).toDateString();
                                    this.lastDayOfPayment = new Date(this.app[0].lastPaymentDate).toDateString();
                                    //fetch the client information
                                    var clientId = this.client.key;
                                    if (clientId > 0) {
                                        axios({
                                            method: 'GET',
                                            'url': context + 'Client/get-client-details.do?clientId=' + clientId
                                                + "&moduleId=" + this.moduleId
                                        })
                                            .then(result => {
                                                if (result.data.responseCode == 1) {
                                                    // debugger;
                                                    this.clientDetails = result.data.payload;


                                                    //Touhid: start

                                                    this.params = {
                                                        "dueBills": this.app,
                                                        "netPayable": this.grandTotal,
                                                        "amountInWords": this.grandTotalAmount,
                                                        "clientId": clientId,
                                                        "lastPaymentDate": this.lastDate,
                                                        "type": 4,
                                                        "moduleId": module,
                                                        "invoiceId": this.invoiceId,
                                                    }

                                                    //Touhid: end

                                                } else {
                                                    toastr.error('No Client found', 'Failure');
                                                }
                                            }).catch(error => {
                                            console.log(error);
                                        });
                                    }

                                    // debugger;
                                    //client detail fetch ends here
                                } else {
                                    toastr.error('No data found for Final Bill', 'Failure');
                                }

                            } else {
                                this.result = false;
                                toastr.error('No data found', 'Failure');
                            }


                        } else {
                            this.result = false;
                            toastr.error(result.data.msg, 'Failure');
                            //window.location.href = context + 'lli/monthly-bill/searchPage.do'
                        }

                    })
                    .catch(error => console.log(error))
            )
                .then(() => this.submitClicked = false);

        },

        getAllInfo: function () {

            var clientId = this.client.key;


            var module = this.moduleId;

            Promise.resolve(
                axios({method: 'GET', 'url': context + 'common/bill/get-info.do?id=' + clientId + '&module=' + module})
                    .then(result => {

                        if (result.data.responseCode == 1) {
                            // debugger;

                            if(module==7 || module==6||module==9||module==4) {
                                this.existingInfo = result.data.payload.value;


                                var isFinal = result.data.payload.key;

                                if (!isFinal) {
                                    if (this.existingInfo != null
                                    ) {
                                        // debugger;

                                        this.existingConnections = this.existingInfo.connections;
                                        this.existingApplications = this.existingInfo.applications;
                                        this.existingOwnerApplications = this.existingInfo.ownerchange;
                                        this.existingReviseApplications = this.existingInfo.revise;
                                        this.existingVPNLinks = this.existingInfo.links;
                                        this.existingVPNAppLinks = this.existingInfo.vpnapplications;


                                        this.existingNixConnections = this.existingInfo.nixconnections;
                                        this.existingNixApplications = this.existingInfo.nixapplications;
                                        this.existingNixReviseApplications = this.existingInfo.nixrevise;


                                        this.existingCoLocationConnections=this.existingInfo.colocationconnections;
                                        this.existingCoLocationApplications=this.existingInfo.colocationapplications;

                                        if(module==7){
                                            this.finalGenerated = true;
                                            $('#detailinfomodal').modal({show: true});
                                        }else if(module==6){
                                            this.finalVpnGenerated = true;
                                            $('#detailvpninfomodal').modal({show: true});
                                        }else if(module==9){
                                            this.finalNixGenerated = true;
                                            $('#detailnixinfomodal').modal({show: true});
                                        }else if(module==4){

                                            this.finalColocGenerated=true
                                            $('#detailcolocinfomodal').modal({show: true});
                                        }


                                    }
                                }
                                else {
                                    this.findSummaryOfMultipleMonth();

                                }
                            }else {
                                this.findSummaryOfMultipleMonth();
                            }


                        } else {

                            this.result = false;
                            toastr.error(result.data.msg, 'Failure');
                            //window.location.href = context + 'lli/monthly-bill/searchPage.do'
                        }

                    })
                    .catch(error => console.log(error))
            )
                .then(() => this.submitClicked = false);

        },

        setCalculatedValue: function () {

            var grandBTCL = 0;
            var grandVat = 0;
            var grandTotal = 0;

            for (var i = 0; i < this.app.length; i++) {
                grandBTCL += this.app[i].totalPayable;
                grandVat += this.app[i].VAT;
                grandTotal += this.app[i].netPayable;

            }

            this.grandBtclAmount = grandBTCL;
            this.grandVat = grandVat;
            this.grandTotal = grandTotal;

        },

        convertTotal: function (value) {
            // debugger;

            axios({
                method: 'GET',
                'url': context + 'common/bill/convert-total.do?amount=' + value + '&final=' + 1 + '&client=' + this.client.key
            })
                .then(result => {
                    if (result.data.responseCode == 1) {

                        this.grandTotalAmount = result.data.payload;

                    } else {
                        this.result = false;
                        toastr.error(result.data.msg, 'Failure');
                        toastr.options.timeOut = 3000;

                        //window.location.href = context + 'lli/monthly-bill/searchPage.do'
                    }


                }).catch(error => {
                console.log(error);
            });

        },

        getSecurity: function () {

            axios({method: 'GET', 'url': context + 'common/bill/get-security-amount.do?clientId=' + this.client.key})
                .then(result => {
                    if (result.data.responseCode == 1) {

                        this.securityDeposit = result.data.payload;

                    } else {
                        this.result = false;
                        toastr.error(result.data.msg, 'Failure');
                        toastr.options.timeOut = 3000;

                        //window.location.href = context + 'lli/monthly-bill/searchPage.do'
                    }


                }).catch(error => {
                console.log(error);
            });

        },

    },
    mounted() {
        this.isAdmin = isAdmin;
        if (!this.isAdmin) this.findSummaryOfTheMonth();
        this.getModules();
    }

});