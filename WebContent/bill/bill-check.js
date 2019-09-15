var vue = new Vue({
	el: '#bill-check',
	data: {
		contextPath:context,
		application:{client:[]},
		app:{},
		moduleId:moduleID,
	},
	methods:{
		checkBill : function() {
			let clietnId = this.application.client.key;

			axios({ method: 'GET', 'url': context + 'bill/check/clearance-certificate.do?client=' +clietnId
								+'&from='+this.application.fromDate
								+'&to='+this.application.toDate
								+'&module='+this.moduleId})
			.then(result => {

				if(result.data.responseCode == 1) {
					this.app = result.data.payload;
				}
				else {
					toastr.error(result.data.msg, 'Failure');
				}

			}).catch( error => {
				console.log(error);
			});
		},
		/*getTotalFee:function(feeList){
			var total=0;
			for (var i=0; i< feeList.length;i++) {
				total = total + feeList[i].cost;
            }
            return total;
		},
        takaFormat: function (amount) {
            return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
        },
		getConcateRemark:function(feeList){
			var remark="";
            for (var i=0; i< feeList.length;i++) {
            	if(i==feeList.length-1)remark = remark+feeList[i].remark;
            	else remark = remark+feeList[i].remark+",";
            }
			return remark;
		},*/
	},
});