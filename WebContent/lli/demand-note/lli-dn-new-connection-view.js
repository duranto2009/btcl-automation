var vue = new Vue({
	el: "#btcl-application",
	data: {
		id : billID,
		billDTO : {},
		app : {},
		appContent: {},
		contextPath: context,
		btnList : []
	},
	methods: {
		viewPDF : function () {
			window.location.href = context + "lli/pdf/view.do?type=demand-note&id=" + this.id;
		},
		submit : function (action) {
			if(!action.isForward){
				axios({ method: 'POST', 'url': context + action.url, data:{billID: this.id}})
				.then(result => {
					if(result.data.responseCode == 1){
						toastr.success("Your request has been processed", "Success");
						if(action.url.indexOf('cancel') !=-1){
							window.location.href = context + 'SearchBill.do?moduleID=7';
						}else {
							location.reload();
						}
					}if(result.data.responseCode == 2) {
						toastr.error(result.data.msg, "Failure");
					}
				}, error => {
				});
			}
		},
		checkBtnStatus : function () {
			axios.get(context + "lli/dn/get-actions.do?id="+this.id)
			.then(response=> {
				if(response.data.responseCode == 1){
					this.btnList = response.data.payload;				
				}else {
					console.log(response.data);
				}
			})
			.catch(function (error) {
				console.log(error);
			});
		},
		getBillDTOJSON : function () {
			axios.get(context+ "lli/dn/getBill.do?id="+this.id)
			.then(response=> {
				if(response.data.responseCode == 1){
					this.billDTO = response.data.payload;				
				}else {
					toastr.error(response.data.msg, "Failure");
					window.location.href = context + 'SearchBill.do?moduleID=7';
				}
			})
			.catch(function (error) {
				console.log(error);
			});
		},
		getApplication : function () {
			axios.get(context + "lli/application/get-app.do?dnID=" + this.id)
			.then(result=>{
				if(result.data.responseCode == 1) {
					// this.app = result.data.payload;
					// this.app = result.data.payload.members;
                    if(result.data.payload.hasOwnProperty("members")){

                        this.app = result.data.payload.members;
                    }
                    else{
                        this.app = result.data.payload;
                    }
					this.appContent = JSON.parse(this.app.content);
				}else {
					toastr.error(result.data.msg, "Failure");
				}
			}).catch(error=>{
				console.log(error);
			})
		}
	},
	computed: {
		paymentStatus : function () {
			const paymentStatus = this.billDTO.paymentStatus;
			return paymentStatus === 0 ? "UNPAID" : 
				paymentStatus === 1 ? "PAID UNVERIFIED":
					paymentStatus === 2 ? "PAID VERIFIED" :
						paymentStatus === 4 ? "SKIPPED" : "";
		}
	},
	mounted () {
		this.getApplication();
		this.getBillDTOJSON();
		this.checkBtnStatus();
	}
});
