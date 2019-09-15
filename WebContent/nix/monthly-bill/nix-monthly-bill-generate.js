var thirdTable = new Vue({
	
	el: '#monthly-bill-generate',
	
	data: {		
		contextPath:context,
		isGenerated:null,
		msg:null,
		clicked:false
	},
	methods:{
		findBillOfTheCurrentMonth : function() {
			this.clicked = true;
			toastr.info("Your bill is generating", "Please wait");
			if(!this.isGenerated){		
				axios({ method: 'GET', 'url': context + 'nix/monthly-bill/billGenerate.do'})
				.then(result => {
					if(result.data.responseCode == 1) {
						toastr.success("Your request has been processed", "Success");
						setTimeout(function() {
							window.location.href = context + 'nix/monthly-bill/goGenerate.do';
						}, 1000);
					}
					else {
						toastr.error(result.data.msg, 'Failure');	
					}
				
				}).catch( error => {
					console.log(error);
				});
			}
			
		},
		
		checkIsMontlyBillGenerated:function(){
			axios({ method: 'GET', 'url': context + 'nix/monthly-bill/checkIsGenerate.do'})
			.then(result => {
				
				if(result.data.responseCode == 1) {
					this.isGenerated = result.data.payload;
					if(this.isGenerated == false){
						this.msg="Monthly bill for current month has not been generated yet.";
					}
					else if(this.isGenerated == true) {
						this.msg="Monthly bill for current month has already been generated.";
					}
					else{
                        this.msg="An error Occurred.";
					}
				}
				else {
					toastr.error(result.data.msg, 'Failure');	
				}
			
			}).catch( error => {
				console.log(error);
			});
		}
		
	},
	mounted(){
		this.checkIsMontlyBillGenerated();
	}
	
});