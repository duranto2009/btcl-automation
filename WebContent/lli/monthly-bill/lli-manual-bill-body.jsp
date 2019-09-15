<div id="btcl-application" v-cloak="true">
	<btcl-body title="LLI" subtitle='Manual Bill'>
		<btcl-portlet>
		<btcl-field title="Client" required>
			<lli-client-search :client.sync="client" :callback="getConnectionByClientId"></lli-client-search>
        </btcl-field>
		<btcl-field title="Connection">
			<multiselect v-model="connection" :options="connectionOptionListByClientID"
						 :track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			</multiselect>
		</btcl-field>
		<btcl-info title="Items"></btcl-info>
		<btcl-field v-for="(otherItem, otherItemIndex) in otherItemList">
			<btcl-grid column=3>
				<btcl-input title="Item" :text.sync="otherItem.item"></btcl-input>
				<btcl-input title="Cost" :text.sync="otherItem.cost"></btcl-input>
				<button type=button class="btn red btn-outline" @click="removeItem(otherItemIndex)">Remove Item</button>				
			</btcl-grid>
		</btcl-field>
		<btcl-field>
			<button type=button class="btn green-haze btn-outline" @click="addItem">Add Item</button>
		</btcl-field>
		<btcl-input title="Description" :text.sync="bill.description"></btcl-input>
		<btcl-input title="VAT %" :text.sync="bill.VatPercentage" required></btcl-input>
		<btcl-info title="VAT" :text.sync="vat" required></btcl-info>
		
		<btcl-info title="Total" :text.sync="grandTotal" required></btcl-info>
		<btcl-info title="Net payable ( with VAT)" :text.sync="netPayable" required></btcl-info>
		<button type=button class="btn green-haze btn-block btn-lg" @click="generateManualBill">Generate</button>
		</btcl-portlet>
	</btcl-body>
</div>
<script src="../demand-note/lli-dn.js"></script>
<script>
var vue = new Vue({
	el: "#btcl-application",
	data: {
		bill:{
			VatPercentage : '15'
		},
		contextPath: context,
		connectionOptionListByClientID : [],
		otherItemList : [],
		client : {},
		connection : {}
		
	},
	methods: {
        getConnectionByClientId: function(clientID){
            if(clientID === undefined){
                this.connectionOptionListByClientID = [];
                this.connection = undefined;
            } else{
                axios.get( context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID)
				.then(result => {
					this.connectionOptionListByClientID = result.data.payload;
            	}).catch(error => {
            	    console.log(error)
                });
            }
        },
        generateManualBill : function () {
        	this.bill.listOfFactors = this.otherItemList;
        	this.bill.clientID = this.client.ID;
        	this.bill.connectionId = this.connection.ID;
        	
        	axios.post(context + "lli/bill/manual.do", {
        		bill : this.bill
        	}).then(result=>{
        		if(result.data.responseCode == 1) {
        			toastr.success('Bill is generated', 'Success');
        		}else {
        			toastr.error(result.data.msg, 'Failure');
        		}
        	}).catch(error=>{
        		console.log(error);
        	})
        },
        addItem: function(){
			this.otherItemList.push({item: '', cost: ''});
		},
		removeItem: function(otherItemIndex){
			if(otherItemIndex > -1){
				this.otherItemList.splice(otherItemIndex, 1);	
			}
		}, 
		getTotalCost : function () {
			return this.otherItemList.reduce((accumulator, object) => {
				return accumulator + floatParse(object.cost);
			}, 0.0);
		},
		getVatCalculable : function() {
			return this.getTotalCost();
		},
		getVatPercentage : function () {
			return floatParse(this.bill.VatPercentage) / 100.0;
		},
		getVat : function () {
			return this.getVatCalculable() * this.getVatPercentage(); 
		},
		getGrandTotal : function () {
			return this.getVatCalculable();
		},
		getNetPayable : function() {
			return this.getGrandTotal() + this.getVat() ;
		}
		
	},
	computed : {
		vat : function () {
			return parseFloat(this.getVat().toFixed(2));
		},
		netPayable : function () {
			return parseFloat(this.getNetPayable().toFixed(2));
		}, 
		grandTotal : function() {
			return parseFloat(this.getGrandTotal().toFixed(2));
		},
	}
	
});
</script>