let vue = new Vue({
	el: "#btcl-application",
	data: {
		applicationID : appId,
		contextPath: context,
		dn:{},
		app : {
			client : {}
		},
		isCDGM : isCDGM,
		loading:false
	},
	methods: {
		generateDN : function(){

            let url_string=location.href;
            let url = new URL(url_string);
            let c = url.searchParams.get("nextstate");

			this.dn.itemCosts = this.otherItemList;
			this.loading = true;
			Promise.resolve(
				axios.post(context + "lli/dn/close/generate.do", {
					dn: this.dn,
					appId : this.applicationID,
					nextState:c
				})
					.then(result => {
						if(result.data.responseCode === 1) {
							toastr.success('Demand Note has been generated', 'Success');
							window.location.href = context + "lli/application/search.do";
							// window.location.href = context + "lli/dn/view.do?id=" + result.data.payload;
						}else {
							errorMessage(result.data.msg);
						}
					})
					.catch(function (error) {
						console.log(error);
					})
			).then(()=>this.loading=false);

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

		getClosingOTC : function () {
			return floatParse(this.dn.closingOTC);
		},
		getOtherCost : function () {
			return floatParse(this.dn.otherCost);
		},
		getVatCalculable : function () {
			return this.getClosingOTC() + this.getOtherCost();
		},
		getVatPercentage : function () {
			return floatParse(this.dn.VatPercentage) / 100.0;
		},
		getVat : function () {
			return this.getVatCalculable() * this.getVatPercentage(); 
		},
		getGrandTotal : function () {
			return this.getVatCalculable();
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
			return this.getTotalPayable() + this.getVat();
		}
	},
	computed : {
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
	mounted() {
		this.getData(
			context + 'lli/application/new-connection-get.do?id=' + this.applicationID,
			context+ 'lli/dn/close/autofill.do?appId=' + this.applicationID
		);
	}
});