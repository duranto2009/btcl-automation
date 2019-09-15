<%@ page import="common.ModuleConstants" %>
<div id="btcl-application">
	<btcl-body title="Demand Note" subtitle='New Connection'>
		<btcl-portlet>
			<btcl-info title="Application ID" :text='applicationID'></btcl-info>
			<btcl-info title="Client Name" :text="app.client.value"></btcl-info>
			<btcl-input title="Advance Adjustment" :text.sync="dn.advanceAdjustment"></btcl-input>
			<btcl-input title="Description" :text.sync="dn.description"></btcl-input>
			<btcl-info title="Total" :text.sync="grandTotal" required></btcl-info>
			<btcl-input title="Discount % " :text.sync="dn.discountPercentage" required></btcl-input>
			<btcl-info title="Discount" :text.sync="discount" required></btcl-info>
			<btcl-info title="Total( - discount )" :text.sync="totalPayable" required></btcl-info>
			<btcl-input title="VAT %" :text.sync="dn.VatPercentage" required></btcl-input>
			<btcl-info title="VAT" :text.sync="vat" required></btcl-info>
			<btcl-info title="Net payable ( with VAT )" :text.sync="netPayable" required></btcl-info>
			<button type=button class="btn green-haze btn-block btn-lg" @click="generateDN">Generate</button>
		</btcl-portlet>
	</btcl-body>
</div>
<script>
var appId = '<%=request.getParameter("id")%>';
var billID = '<%=request.getParameter("id")%>';
var applicationID = '<%=request.getParameter("applicationID")%>';
var nextState = '<%=request.getParameter("nextState")%>';
const floatParse = (data) => {
    if(!isNumeric(data)) {
        return 0.0;
    }else {
        return parseFloat(data);
    }
};
const isNumeric = (data) => {
    return !isNaN(parseFloat(data)) && isFinite(data);
};

var vue = new Vue({
    el: "#btcl-application",
    data: {
        id : billID,
        billDTO : {},
        app : {
            client: {
                label: '',
			}
		},
        appContent: {},
        contextPath: context,
        btnList : [],
		applicationID: applicationID,
		dn: {}
    },
    methods: {
        getApplication : function () {
            axios({method: 'GET', 'url': context + "lli/dn/owner-change/autofill.do?appId=" + applicationID})
                .then(result=>{
                    if(result.data.responseCode == 1) {
                        // this.app = result.data.payload;
                        // this.app = result.data.payload.members;
                        if(result.data.payload.hasOwnProperty("members")){

                            this.dn = result.data.payload.members;
                        }
                        else{
                            this.dn = result.data.payload;
                        }
                        // this.appContent = JSON.parse(this.app.content);
						axios({method: 'GET', 'url': context + 'Client/get-by-id-module.do?id=' + this.dn.clientID +'&module='+ <%=ModuleConstants.Module_ID_LLI%>}).then(result => {
                                this.app.client = result.data.payload;
                            }, error => {});

                    }else {
                        toastr.error(result.data.msg, "Failure");
                    }
                }).catch(error=>{
                console.log(error);
            })
        },
        generateDN : function(){
            var url_string=location.href;
            var url = new URL(url_string);
            // var c = url.searchParams.get("nextState");
            var c = nextState;
            console.log(c);
            this.dn.itemCosts = this.otherItemList;
            // debugger;
            axios.post(context + "lli/dn/owner-change/generate.do", {
                dn: this.dn,
                appId : this.applicationID,
                nextState:c
            })
                .then(result => {
                    if(result.data.responseCode == 1) {
                        toastr.success('Demand Note generated', 'Success');
                        window.location.href = context + "lli/ownershipChange/search.do";
                    }else {
                        toastr.error(result.data.msg, 'Failure');
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        },

        addItem: function(){
            this.otherItemList.push({item: '', cost: ''});
        },
        removeItem: function(otherItemIndex){
            if(otherItemIndex > -1){
                this.otherItemList.splice(otherItemIndex, 1);
            }
        },
        getOfcCost() { return floatParse(this.dn.ofcCost)},
		getRackCost() { return floatParse(this.dn.rackCost)},
		getPowerCost() { return floatParse(this.dn.powerCost)},
		getAdvance () { return floatParse(this.dn.advanceAdjustment)},
        getVatCalculable : function() {
            return this.getOfcCost()
					+ this.getPowerCost()
					+ this.getRackCost()
					+ this.getAdvance()
        },
        getVatPercentage : function () {
            return floatParse(this.dn.VatPercentage) / 100.0;
        },
        getVat : function () {
            return Math.ceil(this.getVatCalculable() * this.getVatPercentage());
        },
        getGrandTotal : function () {
            return this.getVatCalculable()
        },
        getDiscountPercentage : function () {
            return floatParse(this.dn.discountPercentage) / 100.0;
        },
        getDiscount : function () {
            return this.getGrandTotal() * this.getDiscountPercentage();
        },
        getTotalPayable : function () {
            return this.getGrandTotal() - this.getDiscount();
        },
        getNetPayable : function() {
            return this.getTotalPayable() + this.getVat() ;
        }

    },
    computed: {
        vat : function () {
            return parseFloat(this.getVat().toFixed(2));
        },
        discount: function () {
            return parseFloat(this.getDiscount().toFixed(2));
        },
        netPayable : function () {
            return parseFloat(this.getNetPayable().toFixed(2));
        },
        grandTotal : function() {
            return parseFloat(this.getGrandTotal().toFixed(2));
        },
        totalPayable : function () {
            return parseFloat(this.getTotalPayable().toFixed(2));
        }
    },
    mounted () {
       // debugger;
        this.getApplication();
    }
});
</script>
