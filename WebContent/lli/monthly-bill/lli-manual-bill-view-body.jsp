<div id=btcl-application>
	<btcl-body title="LLI" subtitle='Manual Bill View'>
		<btcl-portlet>
			<btcl-info title="Client" :text="client.label" ></btcl-info>
			<btcl-info title="Connection" :text="connection.name"></btcl-info>
			<btcl-info title="Items"></btcl-info>
			<btcl-info v-for="item in bill.listOfFactors" :title="item.item" :text="item.cost"></btcl-info>
			<btcl-info title="Description" :text="bill.description"></btcl-info>
			<btcl-info title="VAT %" :text="bill.VatPercentage" required></btcl-info>
			<btcl-info title="VAT" :text="bill.VAT" required></btcl-info>
			
			<btcl-info title="Total" :text="bill.grandTotal" required></btcl-info>
			<btcl-info title="Net payable ( with VAT)" :text="bill.netPayable" required></btcl-info>
			<div class=row>
				<button type=button class="btn btn-block green-haze btn-md" @click="viewPDF">View PDF</button>
			</div>
		</btcl-portlet>
	</btcl-body>
</div>
<script>
var id = '<%=request.getParameter("id")%>';
var vue = new Vue({
	el: "#btcl-application",
	data: {
		billId : id,
		client : {},
		connection : {},
		bill : {},
		contextPath : context
	},
	methods: {
		viewPDF : function () {
			window.location.href = context + "pdf/view/manual-bill.do?id=" + this.billId;
		},
		getClient: function (clientId) {
			axios.get(context + 'lli/client/get-client-by-id.do?id='+clientId).then(res=>{
				if(res.data.responseCode==1){
					this.client = res.data.payload;
				}
			}).catch(err=>{
				toastr.error(res.data.msg, 'Failure');
			});
		},
		getBill : function () {
			axios.get(context + 'lli/bill/get-bill.do?id='+this.billId).then(res=>{
				if(res.data.responseCode==1){
					this.bill = res.data.payload;
					this.getClient(this.bill.clientID);
					this.getConnection(this.bill.connectionId);	
				}
			}).catch(err=>{
				toastr.error(res.data.msg, 'Failure');
			});
		},
		getConnection : function (connectionId) {
			if(typeof connectionId === 'undefined') this.connection = {ID: '', name : 'N/A'};
			else {
				axios.get(context + 'lli/connection/revise-connection-json.do?id='+connectionId).then(res=>{
					if(res.data.responseCode==1){
						this.connection = res.data.payload;
					}else {
						toastr.error(res.data.msg, 'Failure');
					}
				}).catch(err=>{
					toastr.error(res.data.msg, 'Failure');
				});	
			}
			
		}
	},
	mounted: function () {
		this.getBill();
	}
	
});
</script>