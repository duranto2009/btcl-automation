let vue = new Vue({
	el: "#btcl-application",
	data: {
		app: {
			client : {

			}
		},
		applicationID : appId,
		variousData : {},
		dn:{},
		localLoopCosts : {},
		contextPath: context,
		otherItemList : [],
		loading:false,
		isCDGM : isCDGM

	},
	methods: {
		seeLocalLoopBreakdown() {
			window.open(context + 'lli/dn/get-local-loop-breakdown.do?appId=' + this.applicationID, target="_blank");
		},
		generateDN : function(){

			let url_string=location.href;
            let url = new URL(url_string);
            let c = url.searchParams.get("nextstate");
            console.log(c);
			this.dn.itemCosts = this.otherItemList;
			this.loading = true;
			Promise.resolve(
				axios.post(context + "lli/dn/new-connection/generate.do", {
					dn: this.dn,
					appId : this.applicationID,
					nextState:c
				})
					.then(result => {
						if(result.data.responseCode === 1) {
							toastr.success('Demand Note generated', 'Success');
							setTimeout(()=> {
								window.location.href = context + "lli/application/search.do";
							}, 2000);
						}else {
							errorMessage(result.data.msg);
						}
					})
					.catch(function (error) {
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
		getOTC : function () {
			return floatParse(this.dn.fibreOTC) + floatParse(this.dn.localLoopCharge);
		},
		getOtherCost : function () {
			return this.otherItemList.reduce((accumulator, object) => {
				return accumulator + floatParse(object.cost);
			}, 0.0);
		},
		getRegFee : function () {
			return floatParse(this.dn.registrationFee);
		},
		getSecurity: function(){
			return floatParse(this.dn.securityMoney);
		},
		getBwMRC : function () {
			return floatParse(this.dn.bwMRC);
		}, 
		getSecurityAndAdvance : function () {
			return floatParse(this.dn.securityMoney) + floatParse(this.dn.advanceAdjustment);
		}, 
		getAdvance: function() {
			return floatParse(this.dn.advanceAdjustment);
		},
		getVatCalculable : function() {
			return this.getRegFee() + this.getBwMRC() + this.getOTC() + this.getOtherCost() + this.getAdvance();
		},
		getVatPercentage : function () {
			return floatParse(this.dn.VatPercentage) / 100.0;
		},
		getVat : function () {
			return Math.ceil((this.getVatCalculable() - this.getDiscount())* this.getVatPercentage())
		},
		getGrandTotal : function () {
			return this.getVatCalculable() + this.getSecurity();
		},
		getDiscountPercentage : function () {
			return floatParse(this.dn.discountPercentage) / 100.0;
		},
		getDiscount : function () {
			return Math.floor(this.getBwMRC() * this.getDiscountPercentage())
		},
		getTotalPayable : function () {
			return this.getGrandTotal() - (this.getSecurity() ? 2*this.getDiscount() : this.getDiscount());
		},
		getNetPayable : function() {
			return this.getTotalPayable() + this.getVat() ;
		},
		fixedTwoDigit : (amount) => floatParse(amount).toFixed(2),
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
			return this.getGrandTotal() - (this.getSecurity() ? 2*this.getDiscount() : this.getDiscount());
		},
		vatCalculable() { return this.fixedTwoDigit(this.getVatCalculable() - this.getDiscount()) },
		deductDiscount() { return this.fixedTwoDigit(this.getBwMRC()- this.getDiscount()) },
		updatedSecurity() {
			return !this.getSecurity() ? 0
				: this.getBwMRC() - this.getDiscount();
		}

	},
	mounted() {
		this.getData(
			context + 'lli/application/new-connection-get.do?id=' + this.applicationID,
			context+ 'lli/dn/new-connection/autofill.do?appId=' + this.applicationID
		);
	}
});
