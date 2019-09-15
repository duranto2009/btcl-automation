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
            contextPath: context,
            monthlybillSummary: {client: {}},
            result: false,
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
            fromDate: '',
            toDate:'',
            lastDate: "",
            lastPayDate: "",
            selectedElement: [],

            moduleId: '',
            modules: [],

            client: {},
            showDownload:false,
            clientDetails: {},
            invoiceId:0,

            fromDateString: '',
            toDateString:'',
            dateRange : [],



        },
        computed: {
            fromDateNew () {
                return new Date(this.dateRange[0]).getTime();
            },
            toDateNew () {
                let prev =  new Date(this.dateRange[1]);
                console.log(prev);
                return  prev.setMonth(prev.getMonth() + 1) -1;

            }
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

            takaFormat: function (amount) {
                return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            },
            findSummaryOfMultipleMonth: function () {
                // this.monthlybillSummary.suggestedDate = new Date($('input[name="from"]').val()).getTime();

                // debugger;
                let module = this.moduleId;


                this.lastDate = new Date(this.lastPayDate).toDateString();
                this.fromDateString = new Date(this.fromDateNew).toDateString();
                this.toDateString = new Date(this.toDateNew).toDateString();

                // var clientId=-1;
                var clientId = this.client.key;

                // if(this.monthlybillSummary.client.ID>0){
                // 	clientId=this.monthlybillSummary.client.ID;
                // }

                var suggestedDate = this.monthlybillSummary.suggestedDate;
                var newDate = new Date();
                //
                // if(!isAdmin && suggestedDate == null){
                // 	//var month = monPaddingMap[newDate.getMonth()];
                // 	//var year = newDate.getFullYear();
                // 	suggestedDate = newDate.getTime();
                //
                // }

                axios({
                    method: 'GET',
                    'url': context + 'common/bill/search-multiple.do?id=' + clientId + '&module=' + module + '&from=' + this.fromDateNew
                        + '&to=' + this.toDateNew
                })
                    .then(result => {

                        if (result.data.responseCode == 1) {
                            this.app = result.data.payload.key;

                            // debugger;
                            this.grandTotalAmount = result.data.payload.value;
                            // debugger;
                            if (this.app != null) {
                                if (this.app.length > 0) {
                                    var items = this.app.lliMonthlyBillSummaryByItems;
                                    // this.setValues(items);
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

                                                } else {
                                                    toastr.error('No Client found', 'Failure');
                                                    toastr.options.timeOut = 3000;

                                                }
                                            }).catch(error => {
                                            console.log(error);
                                        });
                                    }

                                    // debugger;
                                    this.setCalculatedValue();
                                }
                                //client detail fetch ends here
                                else {
                                    toastr.error('No data found for the specified range', 'Failure');
                                    toastr.options.timeOut = 3000;

                                }

                            } else {
                                this.result = false;
                                toastr.error('No data found', 'Failure');
                                toastr.options.timeOut = 3000;

                            }


                        } else {
                            this.result = false;
                            toastr.error(result.data.msg, 'Failure');
                            toastr.options.timeOut = 3000;
                        }

                    }).catch(error => {
                    console.log(error);
                });
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

            }
            ,
            generateSelected: function () {
                // debugger;
                if(!(this.selectedElement.length>0)){
                    toastr.error('At least one bill should be selected for multiple bill');
                    return;
                }


                this.app = this.selectedElement;
                this.setCalculatedValue();
                axios({method: 'GET', 'url': context + 'common/bill/convert-total.do?amount=' + this.grandTotal+'&final='+0+'&client='+this.client.key})
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

                axios.post(context + 'common/bill/generate-bill.do', {
                    'data': this.app,
                    'lastPayDate': this.lastPayDate,
                    'from' : this.fromDateNew,
                    'to' : this.toDateNew
                })
                    .then(result => {

                        if (result.data.responseCode == 1) {
                            if(this.selectedElement.length>0){
                                this.showDownload=true;
                            }
                            // debugger;
                            this.app = result.data.payload.value;
                            this.invoiceId = result.data.payload.key;
                            toastr.success("Bill Generated Successfully");
                            toastr.options.timeOut = 3000;

                            //Touhid: start
                            this.params = {
                                "dueBills": this.app,
                                "netPayable": this.grandTotal,
                                "amountInWords": this.grandTotalAmount,
                                "clientId": this.client.key,
                                "lastPaymentDate": this.lastDate,
                                "fromDate": this.fromDateNew,
                                "toDate": this.toDateNew,
                                "type": 5,
                                "moduleId": this.moduleId,
                                "invoiceId": this.invoiceId,
                            }

                            //Touhid: end


                        } else if (result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                            toastr.options.timeOut = 3000;
                        }


                        // this.loading = false;
                    })
                    .catch(function (error) {
                    });
            }
            ,
            selectAllElement: function () {
                this.selectedElement = this.app;
                ;
            }
        },
        mounted() {
            this.isAdmin = isAdmin;
            if (!this.isAdmin) this.findSummaryOfTheMonth();
            this.getModules();
        }

    })
;