<div id=btcl-application>
    <btcl-body title="VPN" subtitle='Outsourcing Bill'>
        <btcl-portlet>
            <btcl-field title="Vendor" required>
                <multiselect v-model="client" :options="clientList"
                             track-by=key label=value :allow-empty="false" :searchable=true
                             id=ajax :internal-search=false :options-limit="500" :limit="15"
                             @search-change="searchClient"
                             open-direction="bottom"
                <%--@select="selectClient"--%>
                >
                </multiselect>
            </btcl-field>


            <btcl-field  title="Month">
                <input class="form-control datepicker" name='month' data-format="yyyy-mm" type=text >
            </btcl-field>

            <btcl-field title="Report Type">
                <label class="radio-inline"><input type="radio" name="fiberRadio" :value="true" checked v-model="reportType">Details</label>
                <label class="radio-inline"><input type="radio" name="fiberRadio" :value="false" v-model="reportType">Summary</label>
            </btcl-field>
            <button type=button class="btn green-haze btn-block btn-lg" @click="getOCBill">Show</button>
        </btcl-portlet>

        <btcl-portlet v-if="summary" title="Summary Bill">
            <btcl-info title="Bill Generation Date" :text="summary.lastModificationTime" :date="true"></btcl-info>
            <btcl-info title="Loop Distance(Single)" :text="summary.loopLengthSingle"></btcl-info>
            <btcl-info title="Loop Distance(Dual)"  :text="summary.loopLengthDouble"></btcl-info>
            <btcl-info title="Total Payable(BDT)" :text="summary.totalPayable"></btcl-info>
            <%--<btcl-info title="Amount in Words" :text="bill.description"></btcl-info>--%>

        </btcl-portlet>

        <%--<hr>--%>
        <btcl-portlet v-if="details" title="Details Bill">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th ><p style="text-align: center;"></p></th>
                            <th ><p style="text-align: center;">SI</p></th>
                            <%--<th ><p style="text-align: center;">Advice Note No</p></th>--%>
                            <%--<th ><p style="text-align: center;">Work Order No</p></th>--%>
                            <th ><p style="text-align: center;">Client Name</p></th>
                            <th ><p style="text-align: center;">Link Activation Date</p></th>
                            <th ><p style="text-align: center;">Loop Distance(Single)(m)</p></th>
                            <th ><p style="text-align: center;">Loop Distance(Dual)(m)</p></th>
                            <th ><p style="text-align: center;">BTCL Distance(m)</p></th>
                            <th ><p style="text-align: center;">Vendor Distance(m)</p></th>
                            <th ><p style="text-align: center;">Subtotal Payable(BDT)</p></th>
                            <th ><p style="text-align: center;"></p></th>

                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(element,index) in details">
                            <td ><p style="text-align: center;padding: 10px;"></p></td>
                            <td ><p style="text-align: center;padding: 10px;">{{index + 1}}</p></td>
                            <%--<td ><p style="text-align: center;padding: 10px;">{{element.adviceNoteNo}}</p></td>--%>
                            <%--<td ><p style="text-align: center;padding: 10px;">{{element.demandNoteNo}}</p></td>--%>
                            <td ><p style="text-align: center;padding: 10px;">{{element.clientName}}</p></td>
                            <td ><p style="text-align: center;padding: 10px;">  {{new Date(parseInt(element.activeFrom)).toLocaleDateString("ca-ES")}}</p></td>
                            <td ><p style="text-align: center;padding: 10px;">{{element.loopLengthSingle}}</p></td>
                            <td ><p style="text-align: center;padding: 10px;">{{element.loopLengthDouble}}</p></td>
                            <td ><p style="text-align: center;padding: 10px;">{{element.btclLength}}</p></td>
                            <td ><p style="text-align: center;padding: 10px;">{{element.vendorLength}}</p></td>
                            <td ><p style="text-align: center;padding: 10px;">{{takaFormat(element.totalPayable)}}</p></td>
                            <td ><p style="text-align: center;padding: 10px;"></p></td>


                        </tr>
                    </tbody>

                </table>
                <br>
            </div>
        </btcl-portlet>




    </btcl-body>
</div>
<script src="../demand-note/vpn-dn.js"></script>
<script>
    var vue = new Vue({
        el: "#btcl-application",
        data: {
            bill: {
                VatPercentage: '15'
            },
            contextPath: context,
            linkOptionListByClientID: [],
            otherItemList: [],
            client: {},
            link: {},
            reportType: true,
            monthList: [],

            clientList: [],
            month: '',
            year: '',
            required: true,
            summary: null,
            details: null,
            time:'',

        },
        methods: {
            searchClient: function (query) {
                // /Client/all.do?query=a&module=4
                axios({method: 'GET', 'url': context + 'get-user-by-role.do?id=' + 16020})
                    .then(result => {
                        this.clientList = result.data.payload;
                    }, error => {
                    });
            },
            takaFormat: function (amount) {
                return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
            },
            getLinkByClientId: function (clientID) {
                if (clientID === undefined) {
                    this.linkOptionListByClientID = [];
                    this.link = undefined;
                } else {
                    axios.get(context + 'vpn/link/get-all-active-links-of-client.do?id=' + clientID)
                        .then(result => {
                            this.linkOptionListByClientID = result.data.payload;
                        }).catch(error => {
                        console.log(error)
                    });
                }
            },
            getOCBill: function () {
                this.time = new Date($('input[name="month"]').val()).getTime();
                this.time = new Date(this.time);
                // alert(this.time);
                this.month = this.time.getMonth();
                this.year = this.time.getFullYear();
                // alert(this.month + ': '  +this.year);

                this.summary = null;
                this.details = null;
                if(this.reportType == true){
                axios.get(context + 'vpn/monthly-outsource-bill/details.do?vendor='+this.client.key + '&month='+this.month+'&year='+this.year)
                    .then(result => {
                        this.details = result.data.payload;
                    }).catch(error => {
                    console.log(error)
                });
                }
                else if(this.reportType == false){
                    axios.get(context + 'vpn/monthly-outsource-bill/summary.do?vendor='+this.client.key + '&month='+this.month+'&year='+this.year)
                        .then(result => {
                            this.summary = result.data.payload;
                        }).catch(error => {
                        console.log(error)
                    });
                }
            },


        },
        computed: {
            vat: function () {
                return parseFloat(this.getVat().toFixed(2));
            },
            netPayable: function () {
                return parseFloat(this.getNetPayable().toFixed(2));
            },
            grandTotal: function () {
                return parseFloat(this.getGrandTotal().toFixed(2));
            },
        }

    });

</script>
<script src=../../assets/month-map-int-str.js></script>