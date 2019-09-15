var thirdTable = new Vue({
	
	el: '#monthly-bill-summary',
	
	data: {
		monthStr:null,
		app: {},
		contextPath:context,
		monthlybillSummary:{client:{}},
		result:false,
		isAdmin:false,
		billingRangeBreakDownContent:{},
		longTermContractBreakDownContent:{},
		mbpsBreakDownContent:{},
		billIssueDate:null,
		lastDayOfPayment:null,
		client:{},
	},
	 
	methods:{
		findSummaryOfTheMonth : function() {
			this.monthlybillSummary.suggestedDate = new Date($('input[name="month"]').val()).getTime();
			var clientId=-1;
			
			if(this.monthlybillSummary.client.ID>0){
				clientId=this.monthlybillSummary.client.ID;
			}
			
			var suggestedDate = this.monthlybillSummary.suggestedDate;
			var newDate = new Date();
			
			if(!isAdmin && suggestedDate == null){
				//var month = monPaddingMap[newDate.getMonth()];
				//var year = newDate.getFullYear();
				suggestedDate = newDate.getTime();
				
			}
			
			axios({ method: 'GET', 'url': context + 'lli/monthly-bill-summary/search.do?id=' + clientId+'&date='+suggestedDate})
			.then(result => {
				
				if(result.data.responseCode == 1) {
					this.app = result.data.payload;
					if(this.app!=null){
						this.mbpsBreakDownContent = JSON.parse(this.app.mbpsBreakDownContent);
						this.billingRangeBreakDownContent = JSON.parse(this.app.billingRangeBreakDownContent);
						this.longTermContractBreakDownContent = JSON.parse(this.app.longTermContractBreakDownContent);
						this.result= true;
						this.billIssueDate = new Date(this.app.createdDate).toDateString();
						this.lastDayOfPayment = new Date(this.app.lastPaymentDate).toDateString();
						//this.monthStr = monthmap[this.app.month];
						
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
		

		
		getConcateRemark:function(feeList){
			var remark="";
            for (var i=0; i< feeList.length;i++) {
            	if(i==feeList.length-1)remark = remark+feeList[i].remark;
            	else remark = remark+feeList[i].remark+",";
            }
            
			return remark;
		}
	},
	 computed: {
	    sortedLinks: function() {
	     /* function compareByDate(a, b) {
	        if (a.createdDate < b.createdDate)
	          return -1;
	        if (a.createdDate > b.createdDate)
	          return 1;
	        return 0;
	      }
	      this.app.monthlyBillByConnections = this.app.lliMonthlyBillSummaryByConnections.sort(compareByDate);*/
	      function compareByType(a, b) {
		        if (a.type < b.type)
		          return -1;
		        if (a.type > b.type)
		          return 1;
		        return 0;
		      }
	       sortedArrayOfconnection = this.app.lliMonthlyBillSummaryByConnections.sort(compareByType);
	       return sortedArrayOfconnection;
	    }
	 },
	mounted(){
		this.isAdmin= isAdmin;
		if(!this.isAdmin)this.findSummaryOfTheMonth();
	}
	
});