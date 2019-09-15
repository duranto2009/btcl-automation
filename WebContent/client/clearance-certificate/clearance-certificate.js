import BTCLDownloadButton from '../../vue-components/btclDownloadBtn.js';
import BTCLTable from '../../vue-components/btclTable.js';


var thirdTable = new Vue({

    components : {
        "btcl-download-btn" : BTCLDownloadButton,
        "btcl-table" : BTCLTable
    },


    el: '#clearance-certificate-search',

    data: {
        headings: ["Sl no.", "Month", "Year", "Amount"],
        contextPath: context,
        result: false,
        client: {},
        params: {},

        fromDate: '',
        toDate: '',
        moduleId: '',
        amountInWords: '',
        letterNumber: '',

        modules: [],
        dueBills: {},
        totalDue: 0,
        clientDetails: {},
        monthmap: {

            "0": "Jan",
            "1": "Feb",

            "2": "Mar",
            "3": "Apr",

            "4": "May",
            "5": "Jun",

            "6": "Jul",
            "7": "Aug",

            "8": "Sep",
            "9": "Oct",

            "10": "Nov",
            "11": "Dec"

        }
    },

    methods: {
        findDueBillsOfClient: function () {
            let clientId = this.client.key;

            axios({
                method: 'GET',
                'url': context + 'request-letter/search.do?clientId=' + clientId + '&fromDate=' + this.fromDate
                    + '&toDate=' + this.toDate + "&moduleId=" + this.moduleId
            })
                .then(result => {

                    if (result.data.responseCode == 1) {
                        this.dueBills = result.data.payload;
                        if (this.dueBills != null) {

                            this.result = true;
                            this.calculateTotalDue();
                            this.getAmountInWords();
                            //fetch the client information
                            if (clientId > 0) {
                                axios({
                                    method: 'GET', 'url': context + 'Client/get-client-details.do?clientId=' + clientId
                                        + "&moduleId=" + this.moduleId
                                })
                                    .then(result => {
                                        if (result.data.responseCode == 1) {
                                            this.clientDetails = result.data.payload;


                                            if(this.letterNumber==''){
                                                this.letterNumber='N/A';
                                            }

                                            this.params = {
                                                "letterNumber": this.letterNumber,
                                                "dueBills": this.dueBills,
                                                "clientId": clientId,
                                                "moduleId": this.moduleId,
                                                "amountInWords": this.amountInWords,
                                                "totalDue": this.totalDue,
                                                "clientDetails": this.clientDetails,
                                                "fromDateMillis": this.fromDate,
                                                "toDateMillis": this.toDate,
                                            }

                                        } else {
                                            toastr.error('No Client found', 'Failure');
                                        }
                                    }).catch(error => {
                                    console.log(error);
                                });
                            }
                            //client detail fetch ends here
                        } else {
                            this.result = false;
                            toastr.error('No data found', 'Failure');
                        }
                    } else {
                        this.result = false;
                        toastr.error(result.data.msg, 'Failure');
                    }

                }).catch(error => {
                console.log(error);
            });
        },

        takaFormat: function (amount) {
            return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
        },

        getModules: function () {
            axios.get(context + "ClientType/getAllModules.do").then(res => {
                if (res.data.responseCode === 1) {
                    this.modules = res.data.payload;
                } else {
                    toastr.error("Error Loading modules", "Failure");
                }
            });
        },

        calculateTotalDue() {
            var i;
            for (i = 0; i < this.dueBills.length; i++) {
                this.totalDue += this.dueBills[i].value.value;
            }

        },

        getAmountInWords() {
            axios.get(context + "request-letter/get-amount-in-words.do?amount=" + this.totalDue).then(res => {
                if (res.data.responseCode === 1) {
                    this.amountInWords = res.data.payload;
                } else {
                    toastr.error("Error getting amount in words", "Failure");
                }
            });
        },

        viewRequestLetter() {

        },

        createRow() {
            let rows = [];

            this.dueBills.forEach((dueBill, index) => {
                rows.push([index + 1, this.monthmap[dueBill.value.key.toString()], dueBill.key, this.takaFormat(dueBill.value.value)]);

            });


            return rows;
        },

        createFooter() {
            return [
                "",
                "Month Count: " + this.dueBills.length,
                "Total Amount: " + this.takaFormat(this.totalDue),
                "In Words: " + this.amountInWords
            ]
        }

    },
    mounted() {
        this.getModules();
    },
    watch: {
        moduleId() {
            this.client = {};
            this.dueBills = {};
            this.clientDetails = {};
            this.result = false;
            this.totalDue = 0;
            this.amountInWords = '';
        },

        client() {
            this.dueBills = {};
            this.clientDetails = {};
            this.result = false;
            this.totalDue = 0;
            this.amountInWords = '';
        },

        fromDate(){
            this.result = false;
            this.dueBills = {};
            this.totalDue = 0;
            this.amountInWords = '';
        },
        toDate(){
            this.result = false;
            this.dueBills = {};
            this.totalDue = 0;
            this.amountInWords = '';
        },
    },

});