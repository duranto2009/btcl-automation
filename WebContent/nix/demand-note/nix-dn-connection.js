let vue = new Vue({
	el: "#btcl-application",
	data: {
		app: {
			client: {

			}
		},
		applicationID : appId,
		dn:{},
		contextPath: context,
		otherItemList : [],
		loading:false
	},
	
	methods: {
		fixedTwoDigit : (amount) => floatParse(amount).toFixed(2),
		generateDN : function(){
			this.loading=true;
			let url_string=location.href;
            let url = new URL(url_string);
            let c = url.searchParams.get("nextstate");
			this.dn.itemCosts = this.otherItemList;
			axios.post(context + "nix/dn/generate.do", {
				dn: this.dn,
				appId : this.applicationID,
				nextState:c,
				appGroup: 4
			})
			.then(result => {
				if(result.data.responseCode === 1) {
					toastr.success('Demand Note generated', 'Success');
					window.location.href = context + "nix/application/search.do";
				}else {
					errorMessage(result.data.msg);
				}
				this.loading=false;
			})
			.catch((error)=> {
				console.log(error);
				this.loading=false;

			});
		},
		


		getRegFee : function () {
			return floatParse(this.dn.registrationFee);
		},
		getSecurity: function(){
			return floatParse(this.dn.securityMoney);
		},
		getPortCharge() {
			return floatParse(this.dn.nixPortCharge);
		},
		getPortUpgradeCharge() {
			return floatParse(this.dn.nixPortUpgradeCharge);
		},
		getPortDowngradeCharge() {
			return floatParse(this.dn.nixPortDowngradeCharge);
		},
		getLocalLoopCharge() {
			return floatParse(this.dn.localLoopCharge);
		},
		getReconnectionCharge() {
			return floatParse(this.dn.reconnectCharge);
		},
		getClosingCharge() {
			return floatParse(this.dn.closingCharge);
		},
		getAdvance: function() {
			return floatParse(this.dn.advanceAdjustment);
		},
		getVatCalculable : function() {
			return this.getRegFee() +
				this.getPortCharge() +
				this.getLocalLoopCharge() +
				this.getPortUpgradeCharge() +
				this.getPortDowngradeCharge() +
				this.getReconnectionCharge() +
				this.getClosingCharge() +
				this.getAdvance()
				;
		},
		getVatPercentage : function () {
			return floatParse(this.dn.VatPercentage) / 100.0;
		},
		getVat : function () {
			// return Math.ceil(this.getVatCalculable() * this.getVatPercentage());
			return Math.ceil((this.getVatCalculable() - this.getDiscount())* this.getVatPercentage())
		},
		getGrandTotal : function () {
			return this.getVatCalculable() + this.getSecurity();
		},
		getDiscountPercentage : function () {
			return floatParse(this.dn.discountPercentage) / 100.0;
		},
		getDiscount : function () {
			return Math.floor(this.getPortCharge() * this.getDiscountPercentage())
		},
		getTotalPayable : function () {
			// return this.getGrandTotal() - this.getDiscount();
			return this.getGrandTotal() - (this.getSecurity() ? 2*this.getDiscount() : this.getDiscount());
		},
		getNetPayable : function() {
			return this.getTotalPayable() + this.getVat() ;
		},
		getData() {
			this.loading=true;
			Promise.all(
				[
					axios({ method: 'GET', 'url': context + 'nix/application/new-connection-get.do?id=' + this.applicationID})
						.then(result => {
							result.data.responseCode === 1 ?
								(this.app = result.data.payload.hasOwnProperty("members") ?
									result.data.payload.members : this.app = result.data.payload)
								:
								errorMessage(result.data.msg);
						}).catch( error => {
						console.log(error);
					}),

					axios({ method: 'GET', 'url': context+ 'nix/dn/autofill.do?appId=' + this.applicationID + '&appGroup=4'})
						.then(result => {
							result.data.responseCode === 1 ?
								this.dn = result.data.payload :
									errorMessage(result.data.msg)
						}).catch( error => {
						console.log(error);
					})
				]
			).then((values)=>this.loading=false);

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
			// return parseFloat(this.getTotalPayable().toFixed(2));
			return this.getGrandTotal() - (this.getSecurity() ? 2*this.getDiscount() : this.getDiscount());
		},
		deductDiscount() { return this.fixedTwoDigit(this.getPortCharge() - this.getDiscount()) },
		vatCalculable() { return this.fixedTwoDigit(this.getVatCalculable() - this.getDiscount()) },
		updatedSecurity() {
			return !this.getSecurity() ? 0
				: this.getPortCharge() - this.getDiscount();
		}
		
	},
	created() {
		this.getData();
	}
});
