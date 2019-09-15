var vue = new Vue({
	el: "#btcl-connection",
	data: {
		contextPath: context,
		connection: {
            activeFrom:0,
            loopProvider:{
                ID:0,
                label:""
            },
            officeList: [
                {
                    officeName: '',
                    officeAddress: '',
                    loop:'',
                    btclLength:'',
                    ocLength:'',
                    ofcType:1,
                },
            ],
            //changed to array as it may be object or an array of object
            client:null,
            connectionType:{},
            // comment: '',
            // suggestedDate: 0,

        },
        message: 'Location',
        popOptions: [],
        divisions: [],
        districts: [],
		zones:[],
        selectedDiv: '-1',
        selectedDis: '',
		selectedZone:'',

        connectionList: [

            {
                officeList:  [{
                    officeAddress: "",
                    officeName: "",
                    loop:"",
                    btclLength:0,

                    ocLength:0,
                    oc:{
                        ID:0,
                        label:""
                    },

                }]
            }
        ],
        officeList: [],
        connectionTypeOptions:[],
        officeSelectionTypeList: [{id: 1, value: 'Existing Office'}, {id: 2, value: 'Create New Office'}],
        loopProviderList: [{id: 1, value: 'BTCL'}, {id: 2, value: 'Client'}],

        moduleId: 7,


    },
	methods: {
		goView: function(result){
			if(result.data.responseCode == 2) {
				toastr.error(result.data.msg);
			}else if(result.data.responseCode == 1){
				toastr.success("Your request has been processed", "Success");
				window.location.href = context + 'lli/connection/search.do';
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
            let url = "add-new-connection";

            //todo -> build connection object
            //todo -> post the object
            let con = this.buildFormDataObject();

            axios.post(context + 'lli-migration/' + url + '.do', {'lliConnections': con})
            // axios.post(context + 'lli/connection/' + url1 + '.do', {'ifr':JSON.stringify( this.content.officeList[0])})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        toastr.success("Your request has been processed", "Success");
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                })
                .catch(function (error) {});
        },
        addOffice: function (con, event) {
            con.officeList.push({
                officeName: '',
                officeAddress: '',
                loop:'',
                btclLength:'',

                ocLength:""
            });

        },
        deleteOffice: function (connection, officeIndex, event) {
            //connection.officeList[officeIndex].localLoopList.splice(localLoopIndex, 1);
            if (connection.officeList.length == 1) return;
            connection.officeList.splice(officeIndex, 1);


        },
        skipPaymentPropertyDelete: function(){
		    delete this.connection.skipPayment;
        },



        addNewconnection: function(){
		    this.connectionList.push({
                officeList:  [{
                    officeAddress: "",
                    officeName: "",
                    loop:"",
                    btclLength:"",

                    ocLength:""
                }]
            });
        },
        getConnectionTypeOptions: function(clientID){
            axios({ method: 'GET', 'url': context + 'lli/options/connection-type.do?id=' + this.connection.client.ID})
                .then(result => {
                    // debugger;
                    this.connectionTypeOptions = result.data.payload;
                }, error => {
                });
        },

        buildFormDataObject: function(){
		    return connectionBuilder(this);
        },
        validate: function(){
            if (this.connection.client == null) {
                errorMessage("Client must be selected.");
                return false;
            }
            // if (this.connection.layerType == null) {
            //     errorMessage("Layer Type must be selected.");return false;
            // }
            return true;
        }

	},
    mounted() {},
    updated: function(){
        this.$nextTick(function(){});

    },
    watch: {
	    'connection.client': function(newVal, oldVal){

	        // alert('client changed');
	        console.log('newval: ' + JSON.stringify(newVal));
	        console.log('oldVal: ' + JSON.stringify(oldVal));




	        if(newVal.registrantType!=1){
                this.skipPaymentPropertyDelete();
            }


	        //get all the offices from vpn office (global office)
            getOfficeByClientId(newVal.ID).then(result => {
                this.officeList = result;
            });

            this.getConnectionTypeOptions(newVal.ID);




        }
    },
    created: function(){
	    // this.addNewconnection();

	  // this.addNewOffice(0);
    },

});