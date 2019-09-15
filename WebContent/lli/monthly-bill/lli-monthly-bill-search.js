var thirdTable = new Vue({
	
	el: '#monthly-bill-search',
	
	data: {
		monthStr:null,
		app: {},
		contextPath:context,
		monthlybill:{client:{}},
		result:false,
		//isAdmin:false,
		billingRangeBreakDownContent:{},
		longTermContractBreakDownContent:{},
		mbpsBreakDownContent:{},
		client:{},
	},
	 
	methods:{
		findBillOfTheMonth : function() {
			this.monthlybill.suggestedDate = new Date($('input[name="month"]').val()).getTime();
			//var clientId=-1;
			
			//if(this.monthlybill.client.ID>0){
				clientId=this.monthlybill.client.ID;
			//}
			
			var suggestedDate = this.monthlybill.suggestedDate;
			var newDate = new Date();
			
			/*if(!isAdmin && suggestedDate == null){
				//var month = monPaddingMap[newDate.getMonth()];
				//var year = newDate.getFullYear();
				suggestedDate = newDate.getTime();
				
			}*/
			
			axios({ method: 'GET', 'url': context + 'lli/monthly-bill/search.do?id=' + clientId+'&date='+suggestedDate})
			.then(result => {
				
				if(result.data.responseCode == 1) {
					this.app = result.data.payload;
					if(this.app!=null){
						this.mbpsBreakDownContent = JSON.parse(this.app.mbpsBreakDownContent);
						this.billingRangeBreakDownContent = JSON.parse(this.app.billingRangeBreakDownContent);
						this.longTermContractBreakDownContent = JSON.parse(this.app.longTermContractBreakDownContent);
						this.result= true;
						this.monthStr = monthmap[this.app.month];
					
						//fetch the client information
						var clientId = this.app.clientId;
						if(clientId>0){
							axios({ method: 'GET', 'url': context + 'lli/client/get-client-details.do?id=' + clientId})
							.then(result => {
								if(result.data.responseCode == 1) {
									this.client = result.data.payload;
								}
								else{
									toastr.error('No Client found', 'Failure');	
								}
							}).catch( error => {
								console.log(error);
							});							
						}
						//client detail fetch ends here
				
					}
					else{
						this.result = false;
						toastr.error('No data found', 'Failure');	
					}
				}
				else {
					this.result= false;
					toastr.error(result.data.msg, 'Failure');	
					//window.location.href = context + 'lli/monthly-bill/searchPage.do'
				}
			
			}).catch( error => {
				console.log(error);
			});
		},
		
		getTotalFee:function(feeList){
		
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
		},
		getAmountInWords:function(amount){
			return "this is the amount in words";
		}
	},
	 computed: {
	    sortedConnections: function() {
	      function compareByName(a, b) {
	        if (a.name < b.name)
	          return -1;
	        if (a.name > b.name)
	          return 1;
	        return 0;
	      }
	      this.app.monthlyBillByConnections = this.app.monthlyBillByConnections.sort(compareByName);
	      function compareByType(a, b) {
		        if (a.type < b.type)
		          return 1;
		        if (a.type > b.type)
		          return -1;
		        return 0;
		      }
	       sortedArrayOfconnection = this.app.monthlyBillByConnections.sort(compareByType);
	       return sortedArrayOfconnection;
	    }
	 },
	/*mounted(){
		this.isAdmin= isAdmin;
		if(!this.isAdmin)this.findBillOfTheMonth();
	}*/
	
});