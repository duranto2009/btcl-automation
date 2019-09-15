<div id="btcl-application">
    <btcl-body title="Accounting" subtitle='Subscriber Ledger'
               v-cloak="true"
               :loader="loading"
    >
        <btcl-field title="Module">
            <multiselect v-model="selectedModule" :options="[{id: 6, name:'VPN'}, {id: 7, name:'LLI'}, {id:9, name: 'NIX'}, {id:4, name:'CoLocation'}]"
                         track-by=name label=name :allow-empty="false" :searchable=true :loading=this.isLoading
                         id=ajax :internal-search=false :options-limit="500" :limit="15"
                         open-direction="bottom">
            </multiselect>
        </btcl-field>

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
        <btcl-download-btn v-if="false"
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
            selectedModule: {},
            headings: ['Date', 'Invoice Id', 'BillType /Month /Year',
                'BTCL Amount(Debit)', 'VAT(Debit)', 'Total Amount(Debit)',
                'BTCL Amount(Credit)', 'VAT(Credit)', 'Total Amount(Credit)',
                'BTCL Amount(Balance)', 'VAT(Balance)', 'Total Amount(Balance)',
                'Dr/Cr',
                'Bank', 'Branch'],
            client:{},
            ledger : {},
            list : [],
            sum: {},
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
                            toDate : this.ledger.toDate,
                            moduleId  : this.selectedModule.id
                        }).then(res=>{
                            if(res.data.responseCode === 1) {
                                if(res.data.payload.length===0){
                                    errorMessage("Nothing Found")
                                }else {
                                    this.list = res.data.payload;

                                    this.sum['btcl'] = {
                                        'debit' :  0,
                                        'credit' : 0,
                                        'balance': 0
                                    };
                                    this.sum['vat'] = {
                                        'debit' : 0,
                                        'credit' : 0,
                                        'balance' : 0,
                                    };
                                    this.sum['total'] = {
                                        'debit' : 0,
                                        'credit' : 0,
                                        'balance' : 0,
                                    }
                                    for (let i = 0;i<this.list.length;i++) {
                                        let item = this.list[i];

                                            this.sum.btcl.debit += item.btclAmountDr === '-'? 0: floatParse(item.btclAmountDr);
                                            this.sum.vat.debit += item.vatDr === '-' ?0 : floatParse(item.vatDr);
                                            this.sum.total.debit += item.totalAmountDr === '-' ?0 : floatParse(item.totalAmountDr);
    
                                            this.sum.btcl.credit += item.btclAmountCr === '-'? 0: floatParse(item.btclAmountCr);
                                            this.sum.vat.credit += item.vatCr === '-' ?0 : floatParse(item.vatCr);
                                            this.sum.total.credit += item.totalAmountCr === '-' ?0 : floatParse(item.totalAmountCr);
                                            //
                                            // this.sum.btcl.balance += item.btclAmountBl === '-'? 0: item.btclAmountBl;
                                            // this.sum.vat.balance += item.vatBl === '-' ?0 : item.vatBl;
                                            // this.sum.total.balance += item.totalAmountBl === '-' ?0 : item.totalAmountBl;
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
                    arr.push([item.generationOrPaymentDate,
                        item.invoiceId,
                        item.billTypeMonthYear,
                        fixedTwoDigit(item.btclAmountDr),
                        fixedTwoDigit(item.vatDr),
                        fixedTwoDigit(item.totalAmountDr),
                        fixedTwoDigit(item.btclAmountCr),
                        fixedTwoDigit(item.vatCr),
                        fixedTwoDigit(item.totalAmountCr),
                        fixedTwoDigit(item.btclAmountBl),
                        fixedTwoDigit(item.vatBl),
                        fixedTwoDigit(item.totalAmountBl),
                        item.drOrCr,
                        item.bankName,
                        item.branchName,

                    ]);
                });
                return arr;
            },
            createFooter () {
                let lastItem = this.list[this.list.length-1];

                return [
                    '',
                    '',
                    'Total',
                    fixedTwoDigit(this.sum.btcl.debit),
                    fixedTwoDigit(this.sum.vat.debit),
                    fixedTwoDigit(this.sum.total.debit),
                    fixedTwoDigit(this.sum.btcl.credit),
                    fixedTwoDigit(this.sum.vat.credit),
                    fixedTwoDigit(this.sum.total.credit),
                    fixedTwoDigit(lastItem.btclAmountBl),
                    fixedTwoDigit(lastItem.vatBl),
                    fixedTwoDigit(lastItem.totalAmountBl),
                    lastItem.drOrCr,
                    "-",
                    "-"
                ]
            }
        },
        components : {
            "btcl-download-btn" : BTCLDownloadButton,
            "btcl-table" : BTCLTable
        }

    });
</script>