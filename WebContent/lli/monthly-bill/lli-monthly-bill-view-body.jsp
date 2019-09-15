<div id=btcl-application>
	<btcl-body title="LLI" subtitle='Monthly Bill View'>
		<btcl-portlet>
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
	},
	methods: {
		viewPDF : function () {
			window.location.href = context + "lli/pdf/view.do?type=monthly-bill&id=" + this.billId;
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
	},
	mounted: function () {
		this.getBill();
	}
});
</script>
	