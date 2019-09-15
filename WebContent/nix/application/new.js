var vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        submitDisable:false,
        loading:false,
        application: {
            state: 5001,
            officeList: [
                {
                    officeName: '',
                    officeAddress: ''
                },
            ],
            client:{},
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
                window.location.href = context + 'nix/application/search.do';
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

            var url1 = "new-connection";
            if(this.application.client.key == null){
                this.errorMessage("Client must be selected.");
                return;
            }

            if (this.application.loopProvider == null||this.application.loopProvider == '') {
                this.errorMessage("loop provider must be selected.");
                return;
            }
            if (this.application.zone == null) {
                this.errorMessage("Zone must be selected.");
                return;
            }
            if (this.application.portType == null) {
                this.errorMessage("port type must be selected.");
                return;
            }

            if (this.application.portCount == null ||this.application.portCount ==0) {
                this.errorMessage("port count must be given.");
                return;
            }
            if (this.application.comment == null) {
                errorMessage("Please provide a Comment.");
                return;
            }
            if (this.application.suggestedDate == null) {


                errorMessage("Please provide a suggested date.");
                return;
            }

            
            if(this.application.suggestedDate!=null){

                if(this.application.suggestedDate< new Date().getTime()){

                    errorMessage(" Suggested date should be less than system date");
                    return;

                }

            }

            if(this.application.officeList != null ||this.application.officeList.length > 0){
                for(var o=0;o<this.application.officeList.length;o++){
                    var ofc = this.application.officeList[o];
                    if(ofc!=null) {
                        if (ofc.officeName == ''||ofc.officeName==null) {
                            this.errorMessage("office Name must be added.");
                            return;
                        }
                        if (ofc.officeAddress == ''||ofc.officeAddress==null) {
                            this.errorMessage("office address must be provided.");
                            return;

                        }
                    }
                }
            }
            else {
                this.errorMessage("office  must be provided.");
                return;
            }
            this.submitDisable=true;
            this.loading=true;
            Promise.resolve(
                axios.post(context + 'nix/application/' + url1 + '.do', {'application': JSON.stringify(this.application)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);

                        setTimeout(()=>{
                            location.reload();
                        },5000);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                        window.location.href = context + 'nix/application/search.do';
                    }

                })
                .catch(function (error) {
                }))
                .then(()=>{
                    this.loading=false;
                    this.submitDisable=false;
                });

        },
        errorMessage: function (msg) {
            toastr.options.timeOut = 3000;
            toastr.error(msg);
            return;
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
        axios({method: "GET", "url": context + "location/division.do?id=" + 0})
            .then(result => {
                this.divisions = result.data.payload;

                console.log(this.divisions.elements);

            }, error => {
            });
        if (typeof this.itemid !== 'undefined') {
            // this.refreshInventoryPath();
            alert('error!!');
        }
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
        }
    }

});