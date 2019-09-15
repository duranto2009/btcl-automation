let vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
        loading: false,
		application: {
            state: 1,
            officeList: [
                {
                    officeName: '',
                    officeAddress: ''
                },
            ],
            //changed to array as it may be object or an array of object
            client:{},
            connectionTypes : [],
            connectionType:{},
            loopProvider : {},


        },
        message: 'Location',
        popOptions: [],
        divisions: [],
        districts: [],
		zones:[],
        selectedDiv: '-1',
        selectedDis: '',
		selectedZone:'',
	},
	methods: {
		goView: function(result){
			if(result.data.responseCode == 2) {
				toastr.error(result.data.msg);
			}else if(result.data.responseCode == 1){
				toastr.success("Your request has been processed", "Success");
				window.location.href = context + 'lli/application/search.do';
			}
			else{
				toastr.error("Your request was not accepted", "Failure");
			}
		},

        renderDistrict: function () {
            // alert('clicked ' + this.selected);
            axios({method: "GET", "url": context + "location/district.do?division=" + this.selectedDiv})
                .then(result => {
                    this.districts = result.data.payload;
                    this.selectedDis = -1;
                    console.log('=====================!!!!!!!!! ===============');
                    console.log(this.districts.elements);
                    console.log('=====================!!!!!!!!! ===============');
                }, error => {
                });
        },
        renderZone: function () {
            // alert('clicked ' + this.selected);
            axios({method: "GET", "url": context + "location/zone.do?district=" + this.selectedDis})
                .then(result => {
                    this.zones = result.data.payload;
                    this.selectedZone = -1;
                    console.log('=====================!!!!!!!!! ===============');
                    console.log(this.zones.elements);
                    console.log('=====================!!!!!!!!! ===============');
                }, error => {
                });
        },
        submitFormData: function(){
            if (this.validate() == false) return;
            this.loading = true;
            // let app = applicationBuilder(vue);
            let url = "new-connection";
            axios.post(context + 'lli/application/' + url + '.do', {
                application: this.application
            }).then(result => {
                if (result.data.responseCode == 2) {
                    toastr.error(result.data.msg);
                } else if (result.data.responseCode == 1) {
                    toastr.success("Your request has been processed", "Success");
                    toastr.options.timeOut = 2500;
                    window.location.href = context + 'lli/application/search.do';
                } else {
                    toastr.error("Your request was not accepted", "Failure");
                    window.location.href = context + 'lli/application/new-connection.do';
                    toastr.options.timeOut = 2500;

                }
            })
                .catch(function (error) {
                });
        },
        validate: function () {
            if (vue.application.client.ID == null) {
                toastr.error("Client must be selected.");
                return false;
            }


            if (vue.application.connectionType.ID == null) {
                toastr.error("Connection Type must be selected.");
                return false;
            }


            if(vue.application.client.registrantType==1 && vue.application.connectionType.ID!=2){
                if (vue.application.skipPayment == null) {
                    toastr.error("Demand Note payment type must be selected");
                    return false;
                }
            }
            if (vue.application.bandwidth == null) {
                toastr.error("Bandwidth must be provided.");
                return false;
            }
            if (vue.application.zone == null) {
                toastr.error("Zone must be selected.");
                return false;
            }
            if (vue.application.loopProvider.ID == null) {
                toastr.error("Loop Provider must be selected.");
                return false;
            }
            if (vue.application.suggestedDate == null) {
                toastr.error("Please provide a suggested date.");
                return false;
            }

            //remote office end validate
            let isOfficeNameNeeded = false;
            let isOfficeAddNeeded = false;
            for (i = 0; i < vue.application.officeList.length; i++) {
                if (vue.application.officeList[i]!=null) {
                    if (vue.application.officeList[i].officeName == null||vue.application.officeList[i].officeName.length==0) {
                        isOfficeNameNeeded = true;
                       break;
                    }
                    if (vue.application.officeList[i].officeAddress == null||vue.application.officeList[i].officeAddress.length ==0) {
                        isOfficeAddNeeded = true;
                        break;
                    }
                }
                else{
                    toastr.error("Office must be provided");
                    return false;
                }
            }
            if (isOfficeNameNeeded) {
                toastr.error("Office Name must be provided");
                return false;
            }
            if (isOfficeAddNeeded) {
                toastr.error("Office Address must be provided");
                return false;
            }
            return true;
        },
        addOffice: function (app, event) {
            app.officeList.push({
                officeName: '',
                officeAddress: ''
            });

        },
        deleteOffice: function (application, officeIndex, event) {
            //connection.officeList[officeIndex].localLoopList.splice(localLoopIndex, 1);
            if (application.officeList.length == 1) return;
            application.officeList.splice(officeIndex, 1);


        },
        skipPaymentPropertyDelete: function(){
		    delete this.application.skipPayment;
        }

	},
    mounted() {
	    this.loading = true;
        Promise.resolve(axios({method: "GET", "url": context + "location/division.do?id=" + 0})
            .then(result => {
                this.divisions = result.data.payload;

                // console.log(this.divisions.elements);

            }, error => {
            })
       ).then(()=>this.loading=false)

    },
    updated: function(){
        this.$nextTick(function(){
        });

    },
    watch: {
	    'application.client': function(newVal, oldVal){
	        // alert('client changed');
	        console.log('newval: ' + JSON.stringify(newVal));
	        console.log('oldVal: ' + JSON.stringify(oldVal));
	        if(newVal.registrantType!=1){
                this.skipPaymentPropertyDelete();
            }

            axios({ method: 'GET', 'url': context + 'lli/options/connection-type.do?id=' + this.application.client.ID})
                .then(result => {
                    this.application.connectionTypes = result.data.payload;
                }, error => {
                });
        }
    }

});