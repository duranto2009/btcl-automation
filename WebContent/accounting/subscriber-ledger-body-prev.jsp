<div id="btcl-application">
    <btcl-body title="Accounting" subtitle='Subscriber Ledger'
               v-cloak="true"
               :loader="loading"
    >
        <btcl-field title="Client" required>
            <lli-client-search :client.sync="client"></lli-client-search>
        </btcl-field>

        <btcl-field title="From Date" required>
            <btcl-datepicker :date.sync="ledger.fromDate"></btcl-datepicker>
        </btcl-field>

        <btcl-field title="To Date" required>
            <btcl-datepicker :date.sync="ledger.toDate"></btcl-datepicker>
        </btcl-field>
        <button type=button class="btn green-haze btn-block" @click="search">Search</button>

        <br>
        <btcl-download-btn v-if="list.length>0"
                           alignment="center"
                           class_list="btn btn-primary"
                           btn_text="Download"
                           url_method="POST"
                           :url='context + "pdf/view/misc-document.do"'
                           :url_param="{type: 100, params: {clientId : client.ID, list}}"
                           :file_name='"subscriber-ledger-report-clientId-"+ client.ID + ".pdf"'
                           @download="handleDownloadEvent"
        >
        </btcl-download-btn>
        <btcl-table v-if="list.length>0 && !listLoading"
                    :columns="headings"
                    :rows="createRow()"
                    :footer="createFooter()"
        >

            <%--</btcl-table>--%>
    </btcl-body>
</div>
<script type="module">
    import {fixedTwoDigit, floatParse} from '${context}vpn/demand-note/vpn-dn.js';
    import BTCLDownloadButton from '${context}vue-components/btclDownloadBtn.js';
    import BTCLTable from '${context}vue-components/btclTable.js';
    new Vue({
        el: "#btcl-application",
        data: {
            context,
            headings: ['Sl.', 'Invoice Id', 'Month Year', 'BTCL Amount', 'VAT', 'Total Amount', 'Bank', 'Branch', 'Payment Date', 'Paid Amount'],
            client:{},
            ledger : {},
            list : [],
            tBTCL : 0,
            tVAT : 0,
            tTotal : 0,
            tPaid : 0,
            loading : false,
            listLoading : false,

        },
        watch: {
            client() {
                this.list = [];
            }
        },
        methods: {
            handleDownloadEvent(value) {
                console.log("value: " + value);
                this.listLoading = value;
            },
            search() {

                if(!this.client.ID){
                    errorMessage("Please Select Client");

                }else {
                    this.loading = true;
                    Promise.resolve(
                        axios.post(context + "accounting/ledger/subscriber/report.do", {
                            clientID : this.client.ID,
                            fromDate : this.ledger.fromDate,
                            toDate : this.ledger.toDate
                        }).then(res=>{
                            if(res.data.responseCode === 1) {
                                if(res.data.payload.length===0){
                                    errorMessage("Nothing Found")
                                }else {
                                    this.list = res.data.payload;
                                    this.tBTCL = 0; this.tVAT = 0; this.tTotal = 0; this.tPaid = 0;

                                    for (let i = 0;i<this.list.length;i++) {
                                        let item = this.list[i];
                                        this.tBTCL += floatParse(item.btclAmount);
                                        this.tVAT += floatParse(item.vat);
                                        this.tTotal += floatParse(item.totalAmount);
                                        this.tPaid += floatParse(item.paidAmount);
                                    }
                                }

                            } else {
                                errorMessage(res.data.msg);
                            }

                        }).catch(err=>console.log(err))
                    ).then(()=>this.loading=false);
                }

            },
            createRow (){
                let arr = [];
                // return arr;

                this.list.forEach((item)=> {
                    arr.push([item.serial,
                        item.invoiceId,
                        item.monthYear,
                        fixedTwoDigit(item.btclAmount),
                        fixedTwoDigit(item.vat),
                        fixedTwoDigit(item.totalAmount),
                        item.bankName,
                        item.branchName,
                        item.paymentDate,
                        fixedTwoDigit(item.paidAmount)
                    ]);
                });
                return arr;
            },
            createFooter () {

                return [
                    '',
                    '',
                    '',
                    fixedTwoDigit(this.tBTCL),
                    fixedTwoDigit(this.tVAT),
                    fixedTwoDigit(this.tTotal),
                    '',
                    '',
                    '',
                    fixedTwoDigit(this.tPaid),
                ]
            }
        },
        components : {
            "btcl-download-btn" : BTCLDownloadButton,
            "btcl-table" : BTCLTable
        }

    });
</script>