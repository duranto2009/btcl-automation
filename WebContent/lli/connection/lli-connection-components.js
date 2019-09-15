Vue.component("inventory-autocomplete", {
    props: {
        itemid: Number,
        vlanid: Number,
        fixed: Boolean,
        readonly: Boolean
    },
    template: `
		<div>
			<btcl-info v-if="fixed || readonly" title="PoP" :text="selectedPop.label"></btcl-info>
			<btcl-field title="PoP" v-else >
				<multiselect v-model="selectedPop" :options="popOptions" 
		        	:track-by=ID label=label :allow-empty="false" :searchable=true :loading=popLoading
		        	:id="{index : 0, categoryID: 4}" :internal-search=true :options-limit="500" :limit="15" 
		    		open-direction="bottom" @select="selectInventory">
		        </multiselect>
			</btcl-field>
			<btcl-info v-if="readonly" title="Switch/Router" :text="selectedRouter.label"></btcl-info>
			<btcl-field v-else title="Switch/Router">
				<multiselect v-model="selectedRouter" :options="routerOptions" 
		        	:track-by=ID label=label :allow-empty="false" :searchable=true :loading=routerLoading
		        	:id="{index : 1, categoryID: 5}" :internal-search=true :options-limit="500" :limit="15" 
		    		open-direction="bottom" @select="selectInventory">
		        </multiselect>
			</btcl-field>
			<btcl-info v-if="readonly" title="Port" :text="selectedPort.label"></btcl-info>
			<btcl-field v-else title="Port">
				<multiselect v-model="selectedPort" :options="portOptions" 
		        	:track-by=ID label=label :allow-empty="false" :searchable=true :loading=portLoading
		        	:id="{index : 2, categoryID: 99}" :internal-search=true :options-limit="500" :limit="15" 
		    		open-direction="bottom" @select="selectInventory">
		        </multiselect>
			</btcl-field>
			<btcl-info v-if="readonly" title="VLAN" :text="selectedVLAN.label"></btcl-info>
			<btcl-field v-else title="VLAN">
				<multiselect v-model="selectedVLAN" :options="vlanOptions" 
		        	:track-by=ID label=label :allow-empty="false" :searchable=true :loading=vlanLoading
		        	:id="{index : 3, categoryID: 6}" :internal-search=true :options-limit="500" :limit="15" 
		    		open-direction="bottom" @select="selectInventory">
		        </multiselect>
			</btcl-field>
			
<!--			<btcl-info v-if="readonly" title="Global VLAN" :text="selectedVLAN.label"></btcl-info>-->
<!--			<btcl-field v-else title="GLOBAL VLAN">-->
<!--				<multiselect v-model="selectedVLAN" :options="vlanOptions" -->
<!--		        	:track-by=ID label=label :allow-empty="false" :searchable=true :loading=vlanLoading-->
<!--		        	:id="{index : 4, categoryID: 101}" :internal-search=true :options-limit="500" :limit="15" -->
<!--		    		open-direction="bottom" @select="selectInventory">-->
<!--		        </multiselect>-->
<!--			</btcl-field>-->
		</div>
		
	`,
    data: function () {
        return {
            categoryConstants: [
                {category: 4, label: "PoP"},
                {category: 5, label: "Router/Switch"},
                {category: 99, label: "Port"},
                {category: 6, label: "VLAN"},
                {category: 101, label: "GLOBAL VLAN"},
            ],
            popOptions: [], routerOptions: [], portOptions: [], vlanOptions: [],
            selectedPop: {}, selectedRouter: {}, selectedPort: {}, selectedVLAN: {},
            popLoading: false, routerLoading: false, portLoading: false, vlanLoading: false
        }
    },
    methods: {
        searchInventory: function (searchQuery, id) {
        },
        selectInventory: function (selectedOption, id, keepChildren) {
            // debugger;
            var childCategoryID = id.index !== 3 ? this.categoryConstants[id.index + 1].category : false;
            if (childCategoryID) {
                switch (childCategoryID) {
                    case 5:
                        this.routerLoading = true;
                        break;
                    case 99:
                        this.portLoading = true;
                        break;
                    case 6:
                        this.vlanLoading = true;
                        break;
                    case 101:
                        this.vlanLoading = true;
                        break;
                }
                axios({
                    method: "GET",
                    "url": context + "lli/inventory/get-inventory-options.do?categoryID=" + childCategoryID + "&parentID=" + selectedOption.ID
                })
                    .then(result => {
                        switch (childCategoryID) {
                            case 5:
                                this.routerOptions = result.data.payload;
                                if (!keepChildren) {
                                    this.portOptions = [];
                                    this.vlanOptions = [];
                                    this.selectedRouter = undefined;
                                    this.selectedPort = undefined;
                                    this.selectedVLAN = undefined;
                                }
                                this.routerLoading = false;
                                break;
                            case 99:
                                this.portOptions = result.data.payload;
                                if (!keepChildren) {
                                    this.vlanOptions = [];
                                    this.selectedPort = undefined;
                                    this.selectedVLAN = undefined;
                                }
                                this.portLoading = false;
                                break;
                            case 6:
                                this.vlanOptions = result.data.payload;
                                if (!keepChildren) {
                                    this.selectedVLAN = undefined;
                                }
                                this.vlanLoading = false;
                                break;
                            case 101:
                                this.vlanOptions = result.data.payload;
                                if (!keepChildren) {
                                    this.selectedVLAN = undefined;
                                }
                                this.vlanLoading = false;
                                break;
                        }
                        if (!keepChildren) {
                            this.$emit('update:itemid', selectedOption.ID);
                        }
                    }, error => {
                    });
            } else {
                this.$emit('update:itemid', selectedOption.ID);
            }
        },
        refreshInventoryPath: function () {
            axios({
                method: "GET",
                "url": context + "lli/inventory/get-inventory-details-upto-pop.do?inventoryID=" + this.itemid
            })
                .then(result => {
                    var itemsUptoPop = result.data.payload;

                    if (typeof itemsUptoPop['4'] !== 'undefined') {/*pop*/
                        this.selectInventory({ID: itemsUptoPop['4'].ID, label: itemsUptoPop['4'].name}, {
                            index: 0,
                            categoryID: 4
                        }, true);
                        this.selectedPop = {ID: itemsUptoPop['4'].ID, label: itemsUptoPop['4'].name};
                    }
                    if (typeof itemsUptoPop['5'] !== 'undefined') {/*router*/
                        this.selectInventory({ID: itemsUptoPop['5'].ID, label: itemsUptoPop['5'].name}, {
                            index: 1,
                            categoryID: 5
                        }, true);
                        this.selectedRouter = {ID: itemsUptoPop['5'].ID, label: itemsUptoPop['5'].name};
                    }
                    if (typeof itemsUptoPop['99'] !== 'undefined') {/*port*/
                        this.selectInventory({ID: itemsUptoPop['99'].ID, label: itemsUptoPop['99'].name}, {
                            index: 2,
                            categoryID: 99
                        }, true);
                        this.selectedPort = {ID: itemsUptoPop['99'].ID, label: itemsUptoPop['99'].name};
                    }
                }, error => {
                });
        },
        refreshVlanPath: function () {
            axios({
                method: "GET",
                "url": context + "lli/inventory/get-inventory-details-upto-pop.do?inventoryID=" + this.vlanid
            })
                .then(result => {
                    var itemsUptoPop = result.data.payload;
                   // debugger;
                    if (typeof itemsUptoPop['6'] !== 'undefined') {/*vlan*/
                        this.selectInventory({ID: itemsUptoPop['6'].ID, label: itemsUptoPop['6'].name}, {
                            index: 3,
                            categoryID: 6
                        }, true);
                        this.selectedVLAN = {ID: itemsUptoPop['6'].ID, label: itemsUptoPop['6'].name};
                    }else if (typeof itemsUptoPop['101'] !== 'undefined') {/*globla-vlan*/
                        this.selectInventory({ID: itemsUptoPop['101'].ID, label: itemsUptoPop['101'].name}, {
                            index: 3,
                            categoryID: 101
                        }, true);
                        this.selectedVLAN = {ID: itemsUptoPop['101'].ID, label: itemsUptoPop['101'].name};
                    }
                }, error => {
                });
        }
    },
    mounted() {
        axios({method: "GET", "url": context + "lli/inventory/get-inventory-options.do?categoryID=" + 4})
            .then(result => {
                this.popOptions = result.data.payload;
            }, error => {
            });
        if (typeof this.itemid !== 'undefined') {
            this.refreshInventoryPath();
            this.refreshVlanPath();
        }
    },
    watch: {
        itemid: function () {
            if (this.readonly) {
                this.refreshInventoryPath();
            }
        }
    }
});

Vue.component("inventory-vlan", {
    props: ['vlan'],
    template: `
		<div style="display:grid; padding:10px; grid-gap:10px; grid-template-columns:15% 20% 15% 50%;">
			<label class='control-label'>VLAN ID</label>
			<input type=text class='form-control' v-model=vlanID>
			<button type=button class='form-control' @click=fetchVlanDetails>Check</button>
			<p class='form-control'>{{details}}</p>
		</div>
	`,
    data: function () {
        return {
            details: ""
        }
    },
    computed: {
        vlanID: {
            get() {
                return this.vlan;
            },
            set(val) {
                this.$emit('update:vlan', val);
            }
        }
    },
    methods: {
        fetchVlanDetails: function () {
            axios({method: "GET", "url": context + "lli/connection/vlan-details.do?id=" + this.vlan}).then(result => {
                this.details = result.data.payload;
            }, error => {
                toastr.error("An error occurred.");
            });
        }
    }
});
Vue.component("loop-distance", {
    props: ['client', 'btcl', 'oc'],
    template: `
		<div>
			<btcl-input title="Client Distance(m)" :text.sync="clientDistance"></btcl-input>
			<btcl-input title="BTCL Distance(m)" :text.sync="btclDistance"></btcl-input>
			<btcl-input title="O/C Distance(m)" :text.sync="ocDistance"></btcl-input>
		</div>
	`,
    computed: {
        clientDistance: {
            get() {
                return this.client;
            },
            set(val) {
                if (val.length == 0) val = 0;
                this.$emit('update:client', val);
            }
        },
        btclDistance: {
            get() {
                return this.btcl;
            },
            set(val) {
                if (val.length == 0) val = 0;
                this.$emit('update:btcl', val);
            }
        },
        ocDistance: {
            get() {
                return this.oc;
            },
            set(val) {
                if (val.length == 0) val = 0;
                this.$emit('update:oc', val);
            }
        }
    }
});

Vue.component("ofc-type", {
    props: ['type'],
    template: `
		<btcl-field title="OFC Type">
			<multiselect v-model="ofcTypeObject" :options="this.options" 
	        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
	        </multiselect>
		</btcl-field>
	`,
    data: function () {
        return {
            options: [
                {ID: 1, label: 'Single'},
                {ID: 2, label: 'Dual'}
            ]
        }
    },
    computed: {
        ofcTypeObject: {
            get() {
                return this.type;
            },
            set(val) {
                this.$emit('update:type', val);
            }
        }
    }
});


Vue.component("oc", {
    props: ['oc'],
    template: `
		<btcl-field title="Loop Provider">
			<multiselect v-model="ocObject" :options="ocList" 
	        	:track-by=ID label=label :allow-empty="true" :searchable=true 
	        	:id="ajax" :internal-search=true :options-limit="500" :limit="15" 
	    		open-direction="bottom">
	        </multiselect>
		</btcl-field>
	`,
    data: function () {
        return {
            ocList: []
        }
    },
    computed: {
        ocObject: {
            get() {
                return this.oc;
            },
            set(val) {
                this.$emit('update:oc', val);
            }
        }
    },
    mounted() {
        axios({method: "GET", "url": context + "lli/client/get-loop-provider.do"})
            .then(result => {
                this.ocList = result.data.payload;
            }, error => {
            });
    }
});
Vue.component('lli-connection-view', {
    template: `
		<div>
			<div v-if="typeof this.connection !== undefined">
				<btcl-portlet title=Connection>
					<btcl-info title="Client" :text=this.connection.client.label></btcl-info>
					<btcl-info title="Connection Name" :text=this.connection.name></btcl-info>
					<btcl-info title="Connection Type" :text="this.connection.connectionType.label"></btcl-info>
					<btcl-info title="Bandwidth (Mbps)" :text="this.connection.bandwidth"></btcl-info>
				</btcl-portlet>
				<btcl-portlet :title="'Office '+(officeIndex+1)" v-for="(office,officeIndex) in this.connection.officeList">
		       		<btcl-info title="Office Name" :text=office.name></btcl-info>
		       		<btcl-info title="Address" :text="office.address"></btcl-info>
		         		
		         		
		         		
		         	<btcl-bounded v-for="(localLoop, localLoopIndex) in office.localLoopList" :title="'Local Loop '+(localLoopIndex+1)">
           				<inventory-autocomplete :itemid.sync="localLoop.vlanID" :fixed="popFixed" :readonly="readonly"></inventory-autocomplete>
           				<btcl-info title="Client Distance" :text=localLoop.clientDistance></btcl-info>
	       				<btcl-info title="BTCL Distance" :text=localLoop.btclDistance></btcl-info>
	       				<btcl-info title="O/C Distance" :text=localLoop.OCDistance></btcl-info>
           				<btcl-info title="O/C" :text="localLoop.OC ? localLoop.OC.label : 'N/A'"></btcl-info>
	       				<btcl-info title="OFC Type" :text=localLoop.ofcType.label></btcl-info>
	           		</btcl-bounded>	
		       	</btcl-portlet>
			</div>
		</div>
	`,
    props: {
        lliconnection: Object,
        popFixed: Boolean,
        readonly: Boolean
    },
    computed: {
        connection: {
            get() {
                return this.lliconnection;
            },
            set(val) {
                this.$emit('update:lliconnection', val);
            }
        }
    }
});

Vue.component('connection-type', {
    template: `
		<div>
			<multiselect  v-if="typeof this.client !== 'undefined' && Object.keys(this.client).length > 0" v-model="connectionTypeObject" 
				:options="this.connectionTypeOptions" 
	        	 label=label :allow-empty="false" :searchable=true open-direction="bottom">
	        </multiselect>
	        <p v-else class=form-control>Select Client First</p>
	    </div>
	`,
    props: ['data', 'client'],
    data: function () {
        return {
            connectionTypeOptions: [],
        }
    },
    watch: {
        client: {
            deep: true,
            handler: function (value) {
                this.$emit('update:data', undefined);
                if (typeof value !== 'undefined' && Object.keys(value).length > 0 && typeof value.ID !== 'undefined') {
                    this.getConnectionTypeOptions(value.ID);
                } else {
                    this.connectionTypeOptions = [];
                }

            }
        }
    },
    computed: {
        connectionTypeObject: {
            get() {
                return this.data;
            },
            set(val) {
                this.$emit('update:data', val);
            }
        }
    },
    methods: {
        getConnectionTypeOptions: function (clientID) {
            axios({method: 'GET', 'url': context + 'lli/options/connection-type.do?id=' + clientID})
                .then(result => {
                    this.connectionTypeOptions = result.data.payload;
                }, error => {
                });
        }
    },
    mounted() {
        if (typeof this.client !== 'undefined' && Object.keys(this.client).length > 0) {
            this.getConnectionTypeOptions(this.client.ID);
        }
    }
});