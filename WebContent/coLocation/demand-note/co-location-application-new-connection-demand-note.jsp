<%@ page import="common.ModuleConstants" %>
<div id="btcl-application">
	<btcl-body title="Demand Note" subtitle='New Connection'>
		<btcl-portlet>
			<btcl-info title="Application ID" :text='applicationID'></btcl-info>
	        <btcl-input title="Fiber Cost" :text.sync="dn.ofcCost" required></btcl-input>
	        <btcl-input title="Power Cost" :text.sync="dn.powerCost" required></btcl-input>
	        <btcl-input title="Space Cost" :text.sync="dn.rackCost" required></btcl-input>
	        <btcl-input title="Floor Space Cost" :text.sync="dn.floorSpaceCost" required></btcl-input>
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

const billID = '<%=request.getParameter("id")%>';
const applicationID = '<%=request.getParameter("applicationID")%>';
const nextState = '<%=request.getParameter("nextState")%>';
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


new Vue({
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
    	fixedTwoDigit : (amount) => floatParse(amount).toFixed(2),
        getApplication : function () {
            axios({method: 'GET', 'url': context + "co-location/dn/new-connection/autofill.do?appId=" + applicationID})
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
						//fetch client
                        axios({method: 'GET', 'url': context + 'Client/get-by-id-module.do?id=' + this.dn.clientID +'&module='+ <%=ModuleConstants.Module_ID_COLOCATION%>}).then(result => {
                                this.app.client = result.data.payload;
                            }, error => {});
                        //end of fetching client
                    }else {
                    	errorMessage(result.data.msg);
                    }
                }).catch(error=>{
                console.log(error);
            })
        },
        generateDN : function(){

            let url_string=location.href;
            let url = new URL(url_string);
            // var c = url.searchParams.get("nextState");
            let c = nextState;
            this.dn.itemCosts = this.otherItemList;
            // debugger;
            axios.post(context + "co-location/dn/new-connection/generate.do", {
                dn: this.dn,
                appId : this.applicationID,
                nextState:c
            })
                .then(result => {
                    if(result.data.responseCode == 1) {
                        toastr.success('Demand Note generated', 'Success');
                        window.location.href = context + "co-location/search.do";
                    }else {
                		errorMessage(result.data.msg);
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        getDNAutoFilled : function () {
            axios({ method: 'GET', 'url': context+ 'lli/dn/new-connection/autofill.do?appId=' + this.applicationID})
                .then(result => {
                    if(result.data.responseCode == 1) {
                        this.dn = result.data.payload;
                    }else {
                        toastr.error(result.data.msg, 'Failure');
                    }
                }).catch( error => {
                console.log(error);
            });
        },
        getOfcCost() { return floatParse(this.dn.ofcCost)},
		getRackCost() { return floatParse(this.dn.rackCost)},
		getPowerCost() { return floatParse(this.dn.powerCost)},
		getAdvance () { return floatParse(this.dn.advanceAdjustment)},
		getFloorSpaceCost() {return floatParse(this.dn.floorSpaceCost)},
        getVatCalculable : function() {
           return this.getOfcCost()
					+ this.getPowerCost()
					+ this.getRackCost()
					+ this.getFloorSpaceCost()
					+ this.getAdvance();
        },
        getVatPercentage : function () {
            return floatParse((this.dn.VatPercentage) / 100.0);
        },
        getVat : function () {
            return floatParse(this.getVatCalculable() * this.getVatPercentage());
        },
        getGrandTotal : function () {
            return floatParse(this.getVatCalculable())
        },
        getDiscountPercentage : function () {
            return floatParse((this.dn.discountPercentage) / 100.0);
        },
        getDiscount : function () {
            return floatParse(this.getGrandTotal() * this.getDiscountPercentage());
        },
        getTotalPayable : function () {
            return floatParse(this.getGrandTotal() - this.getDiscount());
        },
        getNetPayable : function() {
            return floatParse(this.getTotalPayable() + this.getVat()) ;
        }

    },
    computed: {

        vat : function () {
            return this.fixedTwoDigit(this.getVat());
        },
        discount: function () {
            return this.fixedTwoDigit(this.getDiscount());
        },
        netPayable : function () {
            return this.fixedTwoDigit(Math.ceil(this.getNetPayable()));
        },
        grandTotal : function() {
            return this.fixedTwoDigit(this.getGrandTotal());
        },
        totalPayable : function () {
            return this.fixedTwoDigit(this.getTotalPayable());
        }
    },
    mounted () {
        this.getApplication();
    }
});
</script>
