var thirdTable = new Vue({
	
	el: '#monthly-bill-summary',
	
	data: {
		monthStr:null,
		app: {},
		contextPath:context,
		monthlybillSummary:{client:{}},
		result:false,
		isAdmin:false,
		regular:null,
		cache:null,
		localLoop:null,
		adjustment:null,
		dnAdjustment:null,
		billIssueDate:null,
		lastDayOfPayment:null,
		client:{},
		billingRangeBreakDownContent:{},
		longTermContractBreakDownContent:{},
		mbpsBreakDownContent:{},
	},
	 
	methods:{
		viewMonthlyBillSummary () {
			window.open(context + "pdf/view/monthly-bill.do?billId="+this.app.ID + "&module=7", "_blank" );
		},
        takaFormat: function (amount) {
            return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
        },
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
						var items = this.app.lliMonthlyBillSummaryByItems;
						this.setValues(items);
						this.result= true;
						this.billIssueDate = new Date(this.app.createdDate).toDateString();
						this.lastDayOfPayment = new Date(this.app.lastPaymentDate).toDateString();
						this.mbpsBreakDownContent = JSON.parse(this.app.mbpsBreakDownContent);
						this.billingRangeBreakDownContent = JSON.parse(this.app.billingRangeBreakDownContent);
						this.longTermContractBreakDownContent = JSON.parse(this.app.longTermContractBreakDownContent);
						
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
		setValues:function(items){
			this.adjustment = 0;
			this.regular = 0;
			this.cache = 0;
			this.localLoop = 0;
			this.dnAdjustment = 0;
			
			for(var i=0;i<items.length;i++){
				
				if(items[i].type>=200 && items[i].type<300){
					this.adjustment += items[i].totalCost;
				}else if(items[i].type==301){
					this.dnAdjustment = items[i].totalCost;
				}else if(items[i].type==101){
					this.regular = items[i].totalCost;
				}else if(items[i].type==111){
					this.cache = items[i].totalCost;
				}else{
					this.localLoop = items[i].totalCost;
				}
			}
			
		},
	},
	mounted(){
		this.isAdmin= isAdmin;
		if(!this.isAdmin)this.findSummaryOfTheMonth();
	}
	
});