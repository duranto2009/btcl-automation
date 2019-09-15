<div id="btcl-application" v-cloak="true">
    <btcl-body title="IP Management" subtitle="NAT assignment">
        <btcl-portlet>
            <btcl-field title="Region" bare>
                <div class="col-sm-3">
                    <select v-model="selectedRegion" class="form-control" @change="onChangeRegion">
                        <option :value="-1" disabled hidden>Select Region</option>
                        <option :value="region.id" v-for="region in axiosData[0]">{{region.name_en}}</option>
                    </select>
                </div>
            </btcl-field>


            <btcl-field title="Sub Region" bare>
                <div class="col-sm-3">
                    <select v-model="selectedSubRegion" class="form-control" @change="onChangeSubRegion">
                        <option :value="-1" hidden disabled>Select Sub Region</option>
                        <option :value="sub_region.id" v-for="sub_region in sub_regions">{{sub_region.subRegionName}}
                        </option>
                    </select>
                </div>
            </btcl-field>

            <%--<btcl-field title="Module" bare>--%>
            <%--<div class="col-sm-3">--%>
            <%--<select v-model="selectedModule"  class="form-control" @change="onChangeModule">--%>
            <%--<option :value="-1" disabled hidden>Select Module</option>--%>
            <%--<option :value="item.key" v-for="item in axiosData[1]">{{item.value}}</option>--%>
            <%--</select>--%>
            <%--</div>--%>
            <%--</btcl-field>--%>
            <hr>


            <modal v-if="showPrivateIPModal">
                <div class="portlet-title" slot="header">
                    <div class="caption" align="center">New Private IP Range</div>
                </div>
                <btcl-portlet slot="body">
                    <btcl-input title="From IP" :text.sync="pvtFromIP" required="true"></btcl-input>
                    <btcl-input title="to IP" :text.sync="pvtToIP" required="true"></btcl-input>
                    <btcl-input title="remarks" :text.sync="pvtRemarks"></btcl-input>
                </btcl-portlet>

                <button slot="footer" class="btn green-haze " @click="savePrivateIP">Submit</button>
                <button slot="footer" class="btn red-haze " @click="showPrivateIPModal = false">Close</button>
            </modal>
            <modal v-if="showPublicIPModal">
                <div class="portlet-title" slot="header">
                    <div class="caption" align="center">New Public IP Range</div>
                </div>
                <btcl-portlet slot="body">
                    <btcl-field title="Select Range" bare>
                        <div class="col-sm-5">
                            <select v-model="selectedPublicIP" class="form-control" @change="onChangePublicIPRange">
                                <option :value="-1" disabled hidden>Select Range</option>
                                <option :value="item.key" v-for="item in publicIPRange">{{item.key.fromIP}} -
                                    {{item.key.toIP}}
                                </option>
                            </select>
                        </div>
                    </btcl-field>
                    <btcl-field title="Select IP" bare>
                        <div class="col-sm-5">
                            <select v-model="selectedIndividualPublicIP" class="form-control">
                                <option :value="-1" disabled hidden>Select IP</option>
                                <option :value="item.fromIP" v-for="item in freeIPs">{{item.fromIP}}</option>
                            </select>
                        </div>
                    </btcl-field>

                </btcl-portlet>
                <button slot="footer" class="btn green-haze " @click="savePublicIP">Submit</button>
                <button slot="footer" class="btn red-haze " @click="showPublicIPModal = false">Close</button>
            </modal>
        </btcl-portlet>
    </btcl-body>


    <div class="col-sm-6">
        <btcl-portlet title="Private IP List">

            <div v-if="privateInventoryList.length!==0">
                <label class="control-label col-sm-offset-1 col-sm-4">
                    From IP
                </label>
                <label class="control-label  col-sm-4">
                    To IP
                </label>
                <br></br>
            </div>


            <div class="row" v-for="item in privateInventoryList">
                <label class="control-label col-sm-offset-1 col-sm-4">
                    {{item.fromIP}}
                </label>
                <label class="control-label col-sm-4">
                    {{item.toIP}}
                </label>

                <div class="col-sm-3">
                    <button type="button" class="btn green-haze" @click="deletePrivateIp(item)">Delete</button>
                </div>
                <br></br>
            </div>
            <hr>
            <button type="button" class="btn green-haze btn-block" @click="enablePrivateIPAddModal"> Add</button>
        </btcl-portlet>
    </div>

    <div class="col-sm-6">

        <btcl-portlet title="Public IP Usage for NAT Purpose">
            <div v-if="publicUsageList.length!==0">
                <label class="control-label col-sm-offset-3 col-sm-4">
                    IP
                </label>
                <br></br>
            </div>

            <div class="row" v-for="item in publicUsageList">
                <label class="control-label col-sm-offset-3 col-sm-4">
                    {{item.fromIP}}
                </label>
                <div class="col-sm-3">
                    <button type="button" class="btn green-haze" @click="deletePublicIp(item)">Delete</button>
                </div>
                <br></br>
            </div>
            <button type="button" class="btn green-haze btn-block" @click="enablePublicIPAddModal"> Add</button>
        </btcl-portlet>
    </div>
</div>
<script>
    var vue = new Vue({
        el: "#btcl-application",
        data: {
            showPrivateIPModal: false,
            showPublicIPModal: false,
            regions: [],
            selectedRegion: -1,
            sub_regions: [],
            selectedSubRegion: -1,
            axiosData: [],
            axiosDataPromises: [],
            selectedModule: 7,
            pvtFromIP: '',
            pvtToIP: '',
            pvtRemarks: '',
            publicIPRange: [],
            selectedPublicIP: -1,
            freeIPs: [],
            selectedIndividualPublicIP: -1,
            publicUsageList: [],
            privateInventoryList: [],
        },
        methods: {
            deletePrivateIp: function (item) {
                axios.get(context + "ip/privateIP/deletePrivateIPsById.do?id=" + item.id)
                    .then(res => {
                        if (res.data.responseCode == 1) {
                            this.reloadPrivateIPList();
                            // alert("deleted");
                        } else {
                            toastr.error("error deleting private ip");
                        }
                    })
                    .catch(err => console.log(err));
            },

            deletePublicIp: function (item) {
                axios.get(context + "ip/publicIP/deletePublicIpUsageById.do?id=" + item.id)
                    .then(res => {
                        if (res.data.responseCode == 1) {
                            this.reloadPublicIPList();
                            // alert("deleted");
                        }
                        else {
                            toastr.eror("error deleting public ip");
                        }
                    })
                    .catch(
                        err => console.log(err)
                    );
            },
            onChangeSubRegion() {
                this.reloadPublicIPList();
                this.reloadPrivateIPList();
            },
            onChangePublicIPRange() {
                axios.post(context + "ip/get-free-block.do", {
                    "block": {
                        "fromIP": this.selectedPublicIP.fromIP,
                        "toIP": this.selectedPublicIP.toIP,
                    },
                    "size": 1
                }).then(res => {
                    if (res.data.responseCode == 1) {
                        this.freeIPs = res.data.payload;
                    } else {
                        toastr.error("Error Retrieving Free IP Block [" + res.data.msg + "]", "Failure");
                    }
                })
                    .catch(err => console.log(err));
            },
            savePublicIP() {
                axios.post(context + "ip/save/usage.do", {
                    'block': {
                        'fromIP': this.selectedIndividualPublicIP,
                        'toIP': this.selectedIndividualPublicIP,
                        'version': 'IPv4',
                        'type': 'PUBLIC',
                        'purpose': 'NAT',
                        'regionId': this.selectedRegion,
                        'subRegionId': this.selectedSubRegion
                    }
                }).then(res => {
                    if (res.data.responseCode == 1) {
                        toastr.success("Your Request has been processed", "Success");
                        this.selectedIndividualPublicIP = -1;
                        this.selectedPublicIP = -1;
                        this.reloadPublicIPList();
                    } else {
                        toastr.failure(res.data.msg, "Failure");
                    }
                })
            },
            checkModalViewEligibility() {
                toastr.options.timeOut = 2500;
                if (this.selectedRegion === -1) {
                    toastr.info("Provide a Region");
                    return false;
                }
                if (this.selectedSubRegion === -1) {
                    toastr.info("Provide a Sub Region");
                    return false;
                }
                if (this.selectedModule === -1) {
                    toastr.info("Provide a Module");
                    return false;
                }
                return true;

            },
            enablePublicIPAddModal() {
                if (this.checkModalViewEligibility()) {
                    this.getPublicIPs();
                    this.showPublicIPModal = true;
                }
            },
            getPublicIPs() {
                axios.post(context + "ip/inventory/suggestion.do", {
                    "params": {
                        "regionId": this.selectedRegion,
                        "version": "IPv4", // default need to handle v6 later
                        "moduleId": this.selectedModule,
                        "type": "PUBLIC",
                        "blockSize": 1 // default
                        // no support for sub region.
                    }
                })
                    .then(res => {
                        if (res.data.responseCode == 1) {
                            this.publicIPRange = res.data.payload;
                        } else {
                            toastr.error("error retrieving public ip range [" + res.data.msg + "]", "Failure");
                        }
                    })
                    .catch(err => console.log(err));
            },

            enablePrivateIPAddModal() {
                if (this.checkModalViewEligibility()) {
                    this.showPrivateIPModal = true;
                }
            },
            savePrivateIP() {
                if (!(validate(this.pvtFromIP, this.pvtToIP, 'IPv4'))) {
                    return;
                }
                if (!checkIsPrivate(this.pvtFromIP, this.pvtToIP)) {
                    return;
                }

                axios.post(context + "ip/privateIP/add.do", {
                    "block": {
                        "subRegionId": this.selectedSubRegion,
                        "moduleId": this.selectedModule,
                        "fromIP": this.pvtFromIP,
                        "toIP": this.pvtToIP,
                        "remarks": this.pvtRemarks
                    }
                }).then(res => {
                    if (res.data.responseCode == 1) {
                        toastr.success("Your Request has been processed", "Success");
                        this.pvtFromIP = '';
                        this.pvtToIP = '';
                        this.pvtRemarks = '';
                        this.reloadPrivateIPList();
                    } else {
                        toastr.error(res.data.msg, "Failure");
                    }
                })
                    .catch(err => console.log(err));
            },
            getData() {
                let urls = [
                    {
                        "name": "regions",
                        "url": context + "ip/region/getAllRegions.do?query="
                    },
                    {
                        "name": "modules",
                        "url": context + "ip/module/getModules.do"
                    },
                ];
                this.axiosDataPromises = urls.map(t => {
                    return axios.get(t.url)
                        .then(res => {

                            if (res.data.responseCode == 1) {
                                return res.data.payload;
                            } else {

                                toastr.error("error retrieving " + t.name, "Failure");
                            }
                        })
                        .catch(err => {
                            console.log(err);
                        });
                });
                Promise.all(this.axiosDataPromises).then(resultArray => {
                    this.axiosData = resultArray;
                });

            },
            onChangeRegion() {
                axios.get(context + "ip/region/getAllSubRegionsByRegionId.do?regionId=" + this.selectedRegion)
                    .then(res => {
                        if (res.data.responseCode == 1) {
                            this.sub_regions = res.data.payload;
                        } else {
                            toastr.error("error loading sub regions[" + res.data.msg + "]", "Failure");
                        }
                    })
                    .catch(err => {
                        console.log(err);
                    });
            },
            reloadPrivateIPList() {
                axios.get(context + "ip/privateIP/getPrivateIPsBySubRegionIdAndModuleId.do?subRegionId=" + this.selectedSubRegion + "&moduleId=" + this.selectedModule)
                    .then(res => {
                        if (res.data.responseCode == 1) {
                            this.privateInventoryList = res.data.payload;
                        } else {
                            toastr.error("Error reloading private ip inventory");
                        }
                    }).catch(err => {
                    console.log(err);
                })
            }
            ,
            reloadPublicIPList() {
                let searchCriteria = {
                    'Purpose': 'NAT',
                };
                if (this.selectedRegion !== -1) {
                    searchCriteria.Region = this.selectedRegion.toString(10);
                }
                if (this.selectedSubRegion !== -1) {
                    searchCriteria.SubRegion = this.selectedSubRegion.toString(10);
                }

                axios
                    .post(context + "ip/get-block-usage.do", {
                        "param": JSON.stringify(searchCriteria)
                    })
                    .then(res => {
                        if (res.data.responseCode == 1) {
                            this.publicUsageList = res.data.payload;
                        } else {
                            toastr.error("error loading Public Usage List for NAT[" + res.data.msg + "]", "Failure");
                        }
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
        },
        mounted() {
            this.getData();
        }
    });

</script>
<script src="../../components/modal.js"></script>