<div id=btcl-application>
	<btcl-body title="Domain Payment" subtitle='Summary'>
		<btcl-portlet>
		
		<btcl-field v-for="(dateRange, dateRangeIndex) in summary.dateRangeList">
				<btcl-field title="From Date" required>
					<btcl-datepicker :date.sync="dateRange.fromDate"></btcl-datepicker>
				</btcl-field>
				<btcl-field title="To Date" required>
					<btcl-datepicker :date.sync="dateRange.toDate"></btcl-datepicker>
				</btcl-field>
				<button type=button class="btn btn-block red btn-outline" @click="removeItem(dateRangeIndex)">Remove</button>				
		</btcl-field>
		<btcl-field>
			<button type=button class="btn btn-block green-haze btn-outline" @click="addItem">Add</button>
		</btcl-field>
		<button type=button class="btn green-haze btn-block btn-lg" @click="generate">Generate</button>
		</btcl-portlet>
	</btcl-body>
</div>

<script>
var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		summary : {
			dateRangeList : [],
		},
		result : []
	},
	methods: {
		addItem: function(){
			this.summary.dateRangeList.push({fromDate: '', toDate: ''});
		},
		removeItem: function(index){
			if(index > -1){
				this.summary.dateRangeList.splice(index, 1);	
			}
		}, 
		generate : function() {
			axios.post(context + 'domain/report/payment/summary', {
				params : this.summary
			}).
			then(result=>{
				if(result.data.responseCode == 1) {
					this.result = result.data.payload;
					toastr.success(result.data.msg);
				}else {
					toastr.error(result.data.msg);
				}
			}).
			catch(error=>{
				console.log(error);
			})
			
		}
	}
});

</script>