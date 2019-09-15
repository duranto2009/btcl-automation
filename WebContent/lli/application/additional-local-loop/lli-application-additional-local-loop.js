var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		connectionOptionListByClientID: [],
		officeList: [],
		application: {
            addedOfficeList: [],
			selectedOfficeList:[],
			selectedLoopList :[],
			selectedLoopProvider:null,
			selectedOfficeIndex:{},
			selectedloopIndex:[],
			client:[],
		},
		isOld:true,
		isReuse:1,
		localloops:[],
        loopProviderOptionList:[
			{
				ID:1,
                label:"BTCL"
            },
			{
				ID:2,
				label:"Client"
			}
		],
		loading:false,
	},
	methods: {
		clientSelectionCallback: function(clientID){
			if(clientID === undefined){
				this.connectionOptionListByClientID = [];
				this.application.connection = undefined;
			} else{
				axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
				.then(result => {
					this.connectionOptionListByClientID = result.data.payload;
				}, error => {
				});
			}
		},
		connectionSelectionCallback: function(connection){
			if(connection === undefined){
				this.officeList = [];
				this.application.office = undefined;
			} else{
				axios({ method: 'GET', 'url': context + 'lli/connection/get-officeObjectList-by-connection-id.do?id=' + connection.ID})
				.then(result => {
					this.officeList = result.data.payload;
				}, error => {
				});
			}
		},

        addOffice: function () {
            this.application.addedOfficeList.push({
                officeName: '',
                officeAddress: ''
            });

        },
        deleteOffice: function (officeIndex) {
            this.application.addedOfficeList.splice(officeIndex, 1);


        },
        submitData: function () {
			if(this.application.client == null ||this.application.client.length == 0){
				toastr.error ("select a Client");
				return;
			}
			if(this.application.loopProvider == null ){
				toastr.error ("select a loop provider first");
				return;
            }

			if(this.application.connection == null ){
				toastr.error ("select a Connection");
				return;
			}
			//2 = add new office
            if(this.application.selectOld.ID==2){
            	if(this.application.addedOfficeList.length == 0) {
					toastr.error("Add a Office Name", "Failure");
					return;
				}
            	else{
            		for(let i=0;i<this.application.addedOfficeList.length;i++){
            			if(this.application.addedOfficeList[i].officeName=='' ||this.application.addedOfficeList[i].officeName==null){
							toastr.error("Add a Office Name", "Failure");
							return;
						}
            			if(this.application.addedOfficeList[i].officeAddress==''||this.application.addedOfficeList[i].officeAddress==null){
							toastr.error("Add a Office Address", "Failure");
							return;
						}
					}
				}
			}
			if(this.application.portCount == 0 || this.application.portCount== null){
				toastr.error ("Port Count must be greater than zero");
				return;
			}

			if(this.application.suggestedDate == null ){
				toastr.error ("select a Date");
				return;
			}
			this.application.selectedLoopList = [];
			for(var i=0;i<this.application.selectedloopIndex.length;i++){
				var index = this.application.selectedloopIndex[i];
				var el = this.localloops[index];
				this.application.selectedLoopList.push(el);
			}
           // var item = this.application.selectedOfficeList;
			var portCount = this.application.portCount;
			var suggestedDate = this.application.suggestedDate;
			if(this.application.isReuse!= null && this.application.isReuse.ID != null && this.application.isReuse.ID !=2){
				if(this.application.selectedLoopList.length == 0){
					toastr.error ("Loop must be selected.");
					return;
				}
			}
          /*  if (item == null|| item.length == 0) {
                this.errorMessage("Office must be selected.");
                return;
            }*/
            if(portCount == 0 || portCount==null){
                this.errorMessage("Port Count must be greater than zero");
                return;
			}
            if(suggestedDate == 0 || suggestedDate==null){
                this.errorMessage("Date Must be selected");
                return;
            }
            var url = "additional-local-loop";
            var path = context+'lli/application/' + url + '.do';
            this.loading = true;
            axios.post(path, {'application': JSON.stringify(this.application)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    this.loading = false;
                })
                .catch(function (error) {
                	this.loading = false;
                });
        },
        errorMessage: function (msg) {
            toastr.options.timeOut = 3000;
            toastr.error(msg);
            return;
        },
	},
	watch:{
		'application.selectOld':function () {
			if(this.application.selectOld.ID == 1)this.isOld = true;
			else this.isOld = false;
		},

		'application.isReuse':function () {
			if(this.application.isReuse.ID == 1)this.isReuse = 1;
			else if(this.application.isReuse.ID == 2)this.isReuse = 2;
			else this.isReuse = 3;
		},
		'application.selectedOfficeIndex':function () {
			if(this.application.selectedOfficeIndex.ID!=null){
				axios({ method: 'GET', 'url': context + 'lli/connection/get-local-loops.do?officeId=' +this.application.selectedOfficeIndex.ID })
					.then(result => {
						this.localloops = result.data.payload;
					}, error => {
					});
			}
		}
	}

});