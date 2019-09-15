var thirdTable = new Vue({
	
	el: '#monthlybillGenerateCheckBody',
	
	data: {
		contextPath:context,
		clicked : false
	
	},
	
	methods:{
		
		genrateBillOfTheMonth : function() {
			toastr.info("Generating Monhtly Bill", "Please Wait");
			this.clicked = true;
			var ids = this.getCheckedValues();
			if(ids.length == 0) {
				alert("No client is selected");
				return;
			}
			axios.post(context + 'vpn/monthly-bill/billGenerateByClient.do', {
				ids : ids
			})
			.then(result => {
				
				if(result.data.responseCode == 1) {
					toastr.success("Your request has been processed", "Success");
					setTimeout(function () {
						window.location.href = context + 'vpn/monthly-bill/check.do';
					}, 1000);
					
				}
				else {
					toastr.error(result.data.msg, 'Failure');	
				}
			
			}).catch( error => {
				console.log(error);
			});
		},
		getCheckedValues: function (){
			var checkBoxes = $("input[name='cIDs']");
			console.log("lenght: "+checkBoxes.length);
			var ids=[];
			for(var i=0;i<checkBoxes.length;i++){
				var t = checkBoxes[i];
				if(t.checked == true){
					ids.push(t.value);
				}
			}
			return ids;
			
		},
		isCheckALl:function (){
			var allCheck = $("input[name='checkAll']");
			if(allCheck[0].checked ==true){
				$('input[name=cIDs]').click(); 

			}
		}
	}
	
});