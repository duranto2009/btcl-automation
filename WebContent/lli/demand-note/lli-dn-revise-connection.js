let vue = new Vue({
	el: "#btcl-application",
	data: {
		app: {
			client: {},
			applicationType : {

			},
		},

		applicationID : appId,
		contextPath: context,
		dn:{ },
		otherItemList : [],
		loading: false,
		isCDGM : isCDGM
	},
	
	methods: {
		seeLocalLoopBreakdown() {
			window.open(context + 'lli/dn/get-local-loop-breakdown.do?appId=' + this.applicationID, target="_blank");
		},
		generateDN : function(){
			this.dn.itemCosts = this.otherItemList;
            let url_string=location.href;
            let url = new URL(url_string);
            let c = url.searchParams.get("nextstate");
            this.loading=true;
            Promise.resolve(
				axios.post(this.contextPath + "lli/dn/common/generate.do", {
					dn: this.dn,
					appId: this.applicationID,
					nextState:c
				}).then(result=>{
					if (result.data.responseCode === 1){
						toastr.success('Demand Note has been generated', 'Success');
						// window.location.href = context + "lli/dn/view.do?id=" + result.data.payload;
						window.location.href = context + "lli/application/search.do";

					}else  {
						errorMessage(result.data.msg);
					}
				}).catch(function (error) {
					console.log(error);
				})
			).then(()=>this.loading=false);

		},
		
		addItem: function(){
			this.otherItemList.push({item: '', cost: ''});
		},
		removeItem: function(otherItemIndex){
			if(otherItemIndex > -1){
				this.otherItemList.splice(otherItemIndex, 1);	
			}
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
		getSecurityMoney : function () {
			return floatParse(this.dn.securityMoney);
		},
		getAdvancedAmount : function () {
			return floatParse (this.dn.advancedAmount);
		},
		getVatExclusive : function () {
			return this.getSecurityMoney() + this.getAdvancedAmount();
		},
		getBWCharge : function() {
			return floatParse(this.dn.bandwidthCharge);
		},
		getDowngradeCharge : function () {
			return floatParse(this.dn.downgradeCharge);
		},
		getPortCharge : function () {
			return floatParse(this.dn.portCharge);
		},
		getFibreOTC : function () {
			return floatParse(this.dn.fibreOTC);
		},
		getCoreCharge : function () {
			return floatParse(this.dn.coreCharge);
		},
		getOtherCost : function () {
			return this.otherItemList.reduce((accumulator, object) => {
				return accumulator + floatParse(object.cost);
			}, 0.0);
		},
		getFirstXIPCost : function () {
			return floatParse(this.dn.firstXIpCost);
		},
		getNextYIPCost : function () {
			return floatParse(this.dn.nextYIpCost);
		},
		getShiftCost: function () {
			return floatParse (this.dn.shiftCharge);
		},
		getOTC : function () {
			return this.getPortCharge() + this.getFibreOTC() + this.getCoreCharge(); 
		},
		getIPCost : function () {
			return this.getFirstXIPCost() + this.getNextYIPCost();
		},
		getVatCalculable : function () {
			return this.getOTC() + this.getOtherCost() + 
					this.getBWCharge() + this.getDowngradeCharge() + 
						this.getIPCost () + this.getShiftCost() + this.getAdvancedAmount();
		},
		getVatPercentage : function () {
			return floatParse(this.dn.VatPercentage) / 100.0;
		},
		getVat: function() {
			return Math.ceil((this.getVatCalculable() - this.getDiscount())* this.getVatPercentage())
		},
		getDiscountPercentage : function () {
			return floatParse(this.dn.discountPercentage) / 100.0;
		},
		getGrandTotal : function () {
			return this.getVatCalculable() + this.getSecurityMoney();
		},
		getDiscount : function () {
			return Math.floor(this.getBWCharge() * this.getDiscountPercentage())
		},
		getTotalPayable : function () {
			return this.getGrandTotal() - (this.getSecurityMoney() ? 2*this.getDiscount() : this.getDiscount());
		},
		getNetPayable : function () {
			return this.getTotalPayable()  + this.getVat();
		},
		fixedTwoDigit : (amount) => floatParse(amount).toFixed(2),
		
		
	},
	computed : {
		firstIPTitle() {
			return 'First ' + (this.app.ipCount<=4 ? this.app.ipCount : 4) + ' IP Charge (BDT)'
		},
		nextIPTitle(){
			return "Next " + (this.app.ipCount<=4 ? 0 : this.app.ipCount - 4) + " IP Charge (BDT)";
		},
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
			return this.getGrandTotal() - (this.getSecurityMoney() ? 2*this.getDiscount() : this.getDiscount());
		},
		vatCalculable() { return this.fixedTwoDigit(this.getVatCalculable() - this.getDiscount()) },
		deductDiscount() { return this.fixedTwoDigit(this.getBWCharge()- this.getDiscount()) },
		updatedSecurity() {
			return !this.getSecurityMoney() ? 0
				: this.getBWCharge() - this.getDiscount();
		}

	},
	mounted() {
		this.getData(
			this.contextPath + 'lli/application/new-connection-get.do?id=' + this.applicationID,
			context+ 'lli/dn/common/autofill.do?appId=' + this.applicationID
		);
	}

	
});
