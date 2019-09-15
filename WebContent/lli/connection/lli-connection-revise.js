Vue.component("loop-distance", {
	props: ['client', 'btcl', 'oc'],
	template: `
		<div>
			<div style="display:grid; padding:10px; grid-gap:10px; grid-template-columns:15% 25% 25% 25% 10%;">
				<label class='control-label'>Distance</label>
				<div><input type=text class='form-control' v-model=clientDistance><div align=center><label>(Client)</label></div></div>
				<div><input type=text class='form-control' v-model=btclDistance><div align=center><label>(BTCL)</label></div></div>
				<div><input type=text class='form-control' v-model=ocDistance><div align=center><label>(O/C)</label></div></div>
				<div></div>
			</div>
		</div>
	`,
	computed:{
		clientDistance : {
			get() {	return this.client; },
	        set(val) { this.$emit('update:client', val);}
		},
		btclDistance : {
			get() {	return this.btcl; },
	        set(val) { this.$emit('update:btcl', val);}
		},
		ocDistance : {
			get() {	return this.oc; },
	        set(val) { this.$emit('update:oc', val);}
		}
	}
});

Vue.component("oc", {
	props: ['oc'],
	template: `
		<div style="display:grid; padding:10px; grid-gap:10px; grid-template-columns:15% 20% 15% 50%;">
			<label class='control-label'>OC ID</label>
			<input type=text class='form-control' v-model=OCID>
			<button type=button class='form-control' @click=fetchOcDetails>Check</button>
			<p class='form-control'>{{details}}</p>
		</div>
	`,
	data: function(){
		return {
			details: ""
		}
	},
	computed:{
		OCID : {
			get() {
	        	return this.oc;
	        },
	        set(val) {
	        	this.$emit('update:oc', val);
	        }
		}
	},
	methods: {
		fetchOcDetails: function(){
			axios({ method: "GET", "url": context + "lli/connection/oc-details.do?id="+ this.oc }).then(result => {
	            this.details = result.data.payload;
	        }, error => {
	            toastr.error("An error occurred.");
	        });
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
	data: function(){
		return {
			details: ""
		}
	},
	computed:{
		vlanID : {
			get() {
	        	return this.vlan;
	        },
	        set(val) {
	        	this.$emit('update:vlan', val);
	        }
		}
	},
	methods: {
		fetchVlanDetails: function(){
			axios({ method: "GET", "url": context + "lli/connection/vlan-details.do?id="+ this.vlan }).then(result => {
	            this.details = result.data.payload;
	        }, error => {
	            toastr.error("An error occurred.");
	        });
		}
	}
});


var vue = new Vue({
	el: "#btcl-application",
	data: {
		connection: {},
		contextPath: context,
		selectedClient: null,
		selectedConnection: null,
		clientConnectionList : []
	},
	methods: {
		addOffice: function(connection, event){
			connection.officeList.push({
				ID:0, name: '', address: '', localLoopList: [],
			});
		},
		addLocalLoop: function(office, event){
			office.localLoopList.push({
				ID:0, vlanID: 0, OCDistance: 0, btclDistance: 0, clientDistance: 0, OCID: 0
			});
		},
		deleteOffice: function(connection, officeIndex, event){
			connection.officeList.splice(officeIndex,1);
		},
		deleteLocalLoop: function(connection, officeIndex, localLoopIndex, event){
			connection.officeList[officeIndex].localLoopList.splice(localLoopIndex,1);
		},
		clientSelectionCallback: function(selectedClientID){
			selectedConnection = null;
			clientConnectionList = [];
			axios({ method: "GET", "url": context + "lli/connection/get-connection-by-client.do?id="+ selectedClientID }).then(result => {
				this.clientConnectionList = result.data.payload;
	        }, error => {});
		},
		connectionSelectionCallback: function(selectedConnection, id){
			axios({ method: "GET", "url": context + "lli/connection/revise-connection-json.do?id="+ selectedConnection.ID }).then(result => {
	            this.connection = result.data.payload;
	        }, error => {
	        });
		}
		
	}
});