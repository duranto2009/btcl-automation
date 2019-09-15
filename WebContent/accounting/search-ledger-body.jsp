
<div id=btcl-application>

	<btcl-body title="Accounting" subtitle='Ledger' v-cloak="true">
		<btcl-field title="Client" required>
        		<lli-client-search :client.sync="client"></lli-client-search>
       	</btcl-field>
       	<btcl-field title="Account Name" required>
        		<multiselect v-model="account" :options="accounts" 
	        	track-by="ID" label="label" :allow-empty="false" :searchable=true>
	        </multiselect>
       	</btcl-field>
       	<btcl-field title="From Date" required>
			<btcl-datepicker :date.sync="ledger.fromDate"></btcl-datepicker>
		</btcl-field>
		
		<btcl-field title="To Date" required>
			<btcl-datepicker :date.sync="ledger.toDate"></btcl-datepicker>
		</btcl-field>
		<button type=button class="btn green-haze btn-block btn-lg" @click="search">Search</button>
		<btcl-table v-for="(group, index) in groups"
				:caption="accountDictionary[index]"
				:columns="headings"
				:rows="createRow(group)"
				:footer = createFooter(group)

		>

		</btcl-table>
	</btcl-body>
</div>

<script type="module">
import BTCLTable from '${context}vue-components/btclTable.js';
new Vue({
	components : {
		"btcl-table" : BTCLTable
	},

	el: "#btcl-application",
	data: {

		// heading
		headings: ['Incident ID', 'Date', 'Debit', 'Credit', 'Balance'],
		accountDictionary : {},

		client:{},
		account:{},
		ledger: {},
		contextPath: context,
		accountingEntries : [],
		accounts : [
		],
		groups : {}
	},
	methods: {
		createRow(group) {
			let rows = [];
			group.forEach((t)=>{
				rows.push([t.incidentID, this.convertDate(t.dateOfRecord), t.debit, t.credit, t.balance]);
			});
			return rows;
		},
		createFooter(group) {
			let t = group[group.length-1];
			return ['', 'Total', t.cumulativeDebit, t.cumulativeCredit, t.balance];
		},
		getAccounts : function () {
			axios.get(this.contextPath + "accounting/get-all-accounts.do")
			.then(response => {
				if(response.data.responseCode === 1){

					this.accounts = response.data.payload;

					this.accounts.forEach(item=>{
						this.accountDictionary[item.ID] = item.label;
					})
				}
			})
			.catch(error => {
				console.log(error);
			})
		},
		convertDate(t) {
			const time = new Date(t);
			const date = time.getDate();
			const month = time.getMonth() + 1;
			const year = time.getFullYear();
			return date + "/" + month + "/" + year;
		},
		search : function () {
			axios.post(this.contextPath + "accounting/ledger/search.do", this.ledger)
			.then(response => {
				// console.log(response);
				if(response.data.responseCode == 2) {
					toastr.error(response.data.msg);
				}else if(response.data.responseCode == 1){
					this.accountingEntries = response.data.payload;
					this.groups = this.accountingEntries.reduce(function(rv, x) {
						(rv[x.accountID] = rv[x.accountID] || []).push(x);
						return rv;
					}, {});


					// console.log(this.accountingEntries);
					if(this.accountingEntries.length==0) {
						toastr.success("No accounting entry found");
					}else {
						toastr.success("Success");
					}
				}
			})
			.catch(error=> {
				console.log(error);
			});	
		},
	},
	watch: {
	    client: function (val) {
	      this.ledger.clientID = val.ID;
	    },
		account : function(val) {
			this.ledger.accountID = val.ID;
		}
  	},
  	mounted() {
  		this.search();
  		this.getAccounts();
  		
  	}
    
		

});
</script>