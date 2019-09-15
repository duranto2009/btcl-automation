import {fixedTwoDigit, callAXIOS, floatParse} from "./vpn-dn.js";

const vue = new Vue({
	el: "#btcl-application",
	data: {
		loading : false,
		app: {},
		id : id,
		dn:{},
		contextPath: context,
		otherItemList : [],
	},
	
	methods: {
		generateDN : function(){
			this.loading=true;
			axios.post(context + "vpn/dn/generate.do", {
				dn: this.dn,
				id : this.id,
				nextState:new URL(location.href).searchParams.get("nextstate"),
				global: isGlobal
			})
			.then(result => {
				if(result.data.responseCode == 1) {
					toastr.success('Demand Note generated', 'Success');
					window.location.href = context + "vpn/link/search.do";
				}else {
					toastr.error(result.data.msg, 'Failure');
				}
				this.loading = false;
			})
			.catch((error)=> {
				console.log(error);
				this.loading = false;
			});
		},
		getDNAutoFilled () {
			callAXIOS(
				context + 'vpn/dn/autofill.do?id=' + this.id + '&global=' + isGlobal+"&appGroup="+appGroup,
				'GET',
				(data)=>{
					this.dn = data.payload;
					this.loading = false;
				},
				(data)=>{
					errorMessage(data.msg);
					this.loading = false;
				}
			)
		},

		getOTCLocalLoopBTCL() {return floatParse(this.dn.otcLocalLoopBTCL)},
		getRegFee () { return floatParse(this.dn.registrationCharge) },
		getSecurity(){ return floatParse(this.dn.securityCharge) },
		getLocalLoopCharge ( ) {return floatParse(this.dn.localLoopCharge)},
		getBWCharge() { return floatParse(this.dn.bandwidthCharge)},
		getDegradationCharge() { return floatParse(this.dn.degradationCharge)},
		getShiftingCharge() { return floatParse(this.dn.shiftingCharge)},
		getOwnershipChangeCharge() { return floatParse(this.dn.ownershipChangeCharge) },
		getOtherCharge() { return floatParse(this.dn.otherCharge)},
		getReconnectionCharge() { return floatParse(this.dn.reconnectCharge) },
		getClosingCharge() { return floatParse(this.dn.closingCharge) },
		getAdvance() { return floatParse(this.dn.advanceAdjustment) },
		getVatCalculable () {
			return this.getRegFee() +
				this.getOTCLocalLoopBTCL() +
				this.getBWCharge() +
				this.getLocalLoopCharge() +
				this.getDegradationCharge() +
				this.getShiftingCharge() +
				this.getOwnershipChangeCharge() +
				this.getOtherCharge() +
				this.getReconnectionCharge() +
				this.getClosingCharge() +
				this.getAdvance()
				;
		},
		getVatPercentage () { return floatParse(this.dn.VatPercentage) / 100.0 },
		getVat() {
			return Math.ceil((this.getVatCalculable() - this.getDiscount())* this.getVatPercentage())
			// return Math.ceil((this.getVatCalculable() - this.dn.discount)* this.getVatPercentage())
		},
		getGrandTotal () { return this.getVatCalculable() + this.getSecurity()  },
		getDiscountPercentage  () { return floatParse(this.dn.discountPercentage) / 100.0 },
		getDiscount () { return Math.floor(this.getBWCharge() * this.getDiscountPercentage()) },
		getTotalPayable () {
			return this.getGrandTotal() - (this.getSecurity() ? 2*this.getDiscount() : this.getDiscount());
		},
		getNetPayable () { return this.getTotalPayable() + this.getVat() }
		
	},
	mounted() {
		this.loading = true;
		this.getDNAutoFilled();

		// this.getApplication();
	},
	computed : {
		vat(){ return fixedTwoDigit(this.getVat()) },
		discount(){ return fixedTwoDigit(this.getDiscount()) },
		netPayable(){ return fixedTwoDigit(this.getNetPayable()) },
		grandTotal(){ return fixedTwoDigit(this.getGrandTotal()) },
		totalPayable(){ return fixedTwoDigit(this.getTotalPayable()) },
		deductDiscount() { return fixedTwoDigit(this.getBWCharge() - this.getDiscount()) },
		vatCalculable() { return fixedTwoDigit(this.getVatCalculable() - this.getDiscount()) },
		updatedSecurity() {
			return !this.getSecurity() ? 0
				: this.getBWCharge() - this.getDiscount();
		}
	}
});
