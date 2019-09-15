let vue = new Vue({
    el: "#btcl-application",
    data: {
        contextPath: context,
        connectionOptionListByClientID: [],
        application: {
            connectionInfo:{},
            },
        ports: [],
        connection:null,
        vlans: [],
        ipRegionList: [],
        regionID: 0,
        ipVersion: 0,
        ipType: 0,
        ipBlockSize: 0,
        ipFreeBlockList: [],
        ipBlockID: 0,
        ipRoutingInfo: '',
        ipAvailableRangleList: '',
        ipAvailableIPID: 0,
        ipAvailableSelected:[],
        isConnectionInfo:false,
        comment: '',
        selectedOffice:'',
        selectedloops:[],
        selectedPop:'',
        routerSwitch:[],
        switchId:'',
        portId:'',
        vlanId:'',
        ipRegionList:[],
        loopId:'',
        usageId:'',
        loading : false,
        inventoryData : {}

    },
    methods: {
        clientSelectionCallback: function(clientID){
            this.application.connectionInfo =null;
            this.isConnectionInfo =false;
            this.connection=null;
            if(clientID === undefined){
                this.connectionOptionListByClientID = [];
                this.application.connection = undefined;
            } else{
                axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
                    .then(result => {
                        if(result.data.responseCode == 2) {
                            toastr.error(result.data.msg);
                        }else if(result.data.responseCode == 1){

                            toastr.options.timeOut = 3000;
                            //toastr.success("Client Fetch success");
                            this.connectionOptionListByClientID = result.data.payload;
                        }
                    }, error => {
                    });
            }
        },
        getConnectionInfo:function(){
           if(this.connection == null) toastr.error("No connection is found or selected");

            let connectionID = this.connection.ID;
            this.loading = true;
            Promise.resolve(
            axios({ method: 'GET', 'url': context + 'lli/changeIPPort/get-connection.do?connectionID=' + connectionID})
                .then(result => {
                    if(result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    }else if(result.data.responseCode == 1){
                        this.isConnectionInfo = true;
                        if (result.data.payload.hasOwnProperty("members")) {

                            // this.application = result.data.payload.members;
                            this.application.connectionInfo = result.data.payload.members;
                        }
                        else {
                            // this.application = result.data.payload;
                            this.application.connectionInfo = result.data.payload;

                        }

                        this.$forceUpdate();
                        console.log("Your request has been processed", "Success");
                    }
                    else{
                        console.log("Your request was not accepted", "Failure");
                    }
                }, error => {
                })).then(()=>this.loading = false);
        },
        getRegion:function () {
            let url = 'ip/region/getRegions.do';
            axios({method: 'GET', 'url': context + url})
                .then(result => {
                    this.ipRegionList = result.data.payload;
                }, error => {
                });

        },

        searchIPBlock: function (data) {
            // this.loading = true;
            let regid = this.regionID;
            let ver = this.ipVersion;
            let typ = this.ipType;
            let blksz = this.ipBlockSize;
            if (regid == 0 || ver == 0 || typ == 0 || blksz == 0) {
                errorMessage("Please Provide all the information.");
                return;
            }
            let info = {
                "regionId": regid,
                "version": ver,
                "type": typ,
                "blockSize": parseInt(blksz),
                "moduleId" :CONFIG.get("module","lli")
            };

            // let url = "get-free-block";
            let url = "inventory/suggestion";
            axios.post(context + 'ip/' + url + '.do', {'params': info})
                .then(result => {
                    // debugger;
                    this.ipBlockID = 0;
                    if (result.data.responseCode == 2) {
                        this.ipFreeBlockList = [];
                        this.ipRoutingInfo = '';
                        this.ipAvailableRangleList = '';
                        errorMessage(result.data.msg);

                    }
                    else {
                        this.ipFreeBlockList = result.data.payload;
                        this.ipRoutingInfo = '';
                        this.ipAvailableRangleList = '';

                    }

                    // this.loading = false;


                })
                .catch(function (error) {
                });


        },
        onchangeIPBlockRange: function (data) {
            // this.loading = true;
            // let fromIP = data.key;
            // let toIP = data.value;
            let blockSize = this.ipBlockSize;
            let postData = {
                "block": data.key,
                "size": blockSize
            };
            // let url = "routing-info/suggestion";
            let url = "get-free-block";
            axios.post(context + 'ip/' + url + '.do', {
                "block": data.key,
                "size": blockSize
            })
                .then(result => {
                    this.ipAvailableRangleList = result.data.payload;
                    this.ipAvailableIPID = 0;
                    // debugger;
                    // this.loading = false;
                })
                .catch(function (error) {
                });
        },

        onchangeAvailableIP: function (data) {
            //if same element clicked twice the don't select the element
            let isPresent = this.ipAvailableSelected.some(t=> (t.fromIP === data.fromIP && t.toIP === data.toIP));
            if(isPresent){
                return;
            }
            this.ipAvailableSelected.push(
                {
                    "fromIP":data.fromIP,
                    "toIP":data.toIP,
                    "version": this.ipVersion,
                    "type" : this.ipType,
                    "regionId": this.regionID,
                }
            );
        },

        getPOPwithType: function (address, type) {
            if (type == 1) {
                return address + '(BTCL POP)';
            }
            else if (type == 2) {
                return address + '(BTCL LDP)';
            }
            else if (type == 3) {
                return address + '(Customer End)';

            }
            else if (type == 4) {
                return address + '(BTCL MUX)';
            }
        },
        getSwitchByPop:function(){
            return axios({ method: 'GET', 'url': context + 'lli/changeIPPort/get-switches.do?popID=' + this.selectedPop})
                .then(result => {
                    if(result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    }else if(result.data.responseCode == 1){
                        if (result.data.payload.hasOwnProperty("elements")) {
                            this.routerSwitch =  result.data.payload.elements;

                        }
                         else this.routerSwitch = result.data.payload;
                    }
                    else{
                    }
                }, error => {
                });

        },
        onChangeSwitch:function (rsId) {
            console.log(rsId);
            this.ports = this.inventoryData["ports"].filter((item)=>{
                return item.parent === rsId;
            });
            this.vlans = this.inventoryData["vlans"].filter((item)=>{
                return item.hasOwnProperty("parent") ? item.parent === rsId : true;
            });

            if(!this.ports.length) {
                errorMessage("No Port found under selected router switch");
            }


        },

        setInventoryValues:function () {

            for(let i =0 ;i<this.selectedloops.length;i++){
                if(this.selectedPop == this.selectedloops[i].popID){
                    this.switchId = this.selectedloops[i].router_switchID;
                    this.portId = this.selectedloops[i].portID;
                    this.vlanId = this.selectedloops[i].VLANID;
                    return;
                }
            }
        },
        updatePortInfo:function () {

            let portInfo = {
                popId: this.selectedPop,
                portId: this.portId,
                switchId: this.switchId,
                vlanId: this.vlanId,
                localLoopId:this.loopId,
                connectionId: this.application.connectionInfo.ID,


            };

            if (portInfo.popId === "") {
                errorMessage("Please select a pop.");
                return;

            }
            if (portInfo.switchId === "") {
                errorMessage("Please select a switch.");
                return;

            }

            if (portInfo.portId === "") {
                errorMessage("Please select a Port.");
                return;

            }
            let url = "updatelocalloop";
            axios.post(context + 'lli/changeIPPort/' + url + '.do', {'data': JSON.stringify(portInfo)})
                .then(result => {
                    console.log(result);

                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        this.closePortAssignModal();
                        toastr.success("Your request for port update has been processed", "Success");
                        this.getConnectionInfo();
                        //window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Please Try Again.", "Failure");
                    }
                    // this.loading = false;
                })
                .catch(function (error) {
                });
        },
        assignNewIPAndReleaseOld: function () {
            let ipInfo = {
                ip:  this.ipAvailableSelected,
                connectionId: this.application.connectionInfo.ID,
                usageId:this.usageId
            };

            let url1 = "updateIP";
            // this.loading = true;
            toastr.options.timeOut = 3000;
            toastr.info("Please Wait");
            axios.post(context + 'lli/changeIPPort/' + url1 + '.do', {'data': JSON.stringify(ipInfo)})
                .then(result => {
                    if (result.data.responseCode == 2) {
                        toastr.error(result.data.msg);
                    } else if (result.data.responseCode == 1) {
                        this.closeIpAssignModal();
                        toastr.success("Your request for change IP has been processed", "Success");
                        this.getConnectionInfo();

                        //window.location.href = context + 'lli/application/search.do';
                    }
                    else {
                        toastr.error("Your request was not accepted", "Failure");
                    }
                    // this.loading = false;
                })
                .catch(function (error) {
                });
        },
        showAssignModal:function (ip) {
            this.usageId = ip.usageId;
            this.ipType = ip.type;
            this.ipBlockSize = this.ip2int(ip.toIP)-this.ip2int(ip.fromIP)+1; //not found
            this.regionID = ip.regionId;
            this.ipVersion = ip.version;
            $('#ipAssignModal').modal({show: true});
        },
        ip2int:function(ip) {
            return ip.split('.').reduce(function(ipInt, octet) { return (ipInt<<8) + parseInt(octet, 10)}, 0) >>> 0;
        },
        ipAvailableDelete: function (index) {
            this.ipAvailableSelected.splice(index, 1);
        },
        closeIpAssignModal:function () {
            $('#ipAssignModal').modal('hide');

        },
        async showLoopEditModal (loop) {
            this.selectedPop = loop.popID;
            this.portId =loop.portID;
            this.switchId = loop.router_switchID;
            this.loopId = loop.historyID;
            this.vlanId =loop.VLANID;
            // await this.getSwitchByPop()
            //     .then(this.onChangeSwitch())
            //     .then(this.onChangePort());

            await this.getAllInventoryData();


            // this.onChangeSwitch();
            // this.onChangePort();
            $('#portAssignModal').modal({show: true});
        },
        closePortAssignModal:function () {
            $('#portAssignModal').modal('hide');


        },
        async getAllInventoryData() {
            return axios.get(context + "lli/changeIPPort/get-inventory-data.do?popId=" + this.selectedPop)
                .then(res=>{
                    if(res.data.responseCode === 1 ) {
                        this.inventoryData = res.data.payload;
                        this.routerSwitch = this.inventoryData["rs"];
                        this.ports = this.inventoryData["ports"];
                        this.vlans = this.inventoryData["vlans"];
                    }else {
                        errorMessage(res.data.msg);
                    }
                }).catch(err=>console.log(err));
        }
    },
    mounted(){
       this.getRegion();
    },

});