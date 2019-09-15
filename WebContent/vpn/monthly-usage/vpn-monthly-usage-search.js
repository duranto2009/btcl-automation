var thirdTable = new Vue({
	
	el: '#monthly-usage-search',
	
	data: {
		monthStr:null,
		app: {},
		contextPath:context,
		monthlyUsage:{client:{}},
		result:false,
		isAdmin:false,
		mbpsBreakDownContent:{},
		client:{},
	},
	
	methods:{
		
		findUsageOfTheMonth : function() {
			this.monthlyUsage.suggestedDate = new Date($('input[name="month"]').val()).getTime();
			var clientId=-1;
			
			if(this.monthlyUsage.client.ID>0){
				clientId=this.monthlyUsage.client.ID;
			}
			
			var suggestedDate = this.monthlyUsage.suggestedDate;
			var newDate = new Date();
			
			if(!isAdmin && suggestedDate == null){
				//var month = monPaddingMap[newDate.getMonth()];
				//var year = newDate.getFullYear();
				suggestedDate = newDate.getTime();
				
			}
			
			axios({ method: 'GET', 'url': context + 'vpn/monthly-usage/view.do?id=' + clientId+'&date='+suggestedDate})
			.then(result => {
				
				if(result.data.responseCode == 1) {
					this.app = result.data.payload;
					if(this.app!=null){
						// this.linkBandwidthBreakDownsContent = JSON.parse(this.app.linkBandwidthBreakDownsContent);
						this.result= true;
						this.monthStr = monthmap[this.app.month];
						//fetch the client information
						// var clientId = this.app.clientId;
						if(clientId>0){
							axios({ method: 'GET', 'url': context + 'vpn/client/get-client-details.do?id=' + clientId})
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
					else {
						this.result =false;
						toastr.error('No data found', 'Failure');	

					}
				}
				else {
					this.result= false;
					toastr.error(result.data.msg, 'Failure');	
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
		getStringDate:function(date){
			 var t = new Date(date);
			 var year = t.getFullYear();
			 var month = t.getMonth();
			 var day = t.getDate();
			 return day+" "+monthmap[month];
		},
	    getLinkBandwidthBreakDownsContent:function(contents){
	    	var bdBreakDown= JSON.parse(contents);
	    	return bdBreakDown;
	    },
	    getLocalLoopBreakDownsContent:function(contents){
	    	var loopBreakDown = JSON.parse(contents);
	    	return loopBreakDown;

	    },
	    
	    getBWBreakDownsRemarks:function(bwBreakDowns){
	    	var remarks = [];
	    	for(var i = 0; i < bwBreakDowns.length; i++)
    		{
	    		if(bwBreakDowns[i].remark != null)
	    			remarks.push(bwBreakDowns[i].remark);
    		}
	    	return remarks;
	    }
		/*getConcateRemark:function(feeList){
			var remark="";
            for (var i=0; i< feeList.length;i++) {
            	if(i==feeList.length-1)remark = remark+feeList[i].remark;
            	else remark = remark+feeList[i].remark+",";
            }
            
			return remark;
		}*/
	},
	 computed: {
	   sortedLinks: function() {
	      function compareByName(a, b) {
	        if (a.name < b.name)
	          return -1;
	        if (a.name > b.name)
	          return 1;
	        return 0;
	      }
	      this.app.monthlyUsageByLinks = this.app.monthlyUsageByLinks.sort(compareByName);
	      function compareByType(a, b) {
		        if (a.type < b.type)
		          return 1;
		        if (a.type > b.type)
		          return -1;
		        return 0;
		      }
	       sortedArrayOfLink = this.app.monthlyUsageByLinks.sort(compareByType);
	       return sortedArrayOfLink;
	    }
	 },
	mounted(){
		this.isAdmin= isAdmin;
		if(!this.isAdmin)this.findUsageOfTheMonth();
	}
	
});