<div id=btcl-application>
	<btcl-body title="LLI Configuration" subtitle='Fixed Cost'>
		<btcl-portlet>
			<btcl-input title="Registration Charge" :text.sync="otc.registrationCharge"></btcl-input>
			<btcl-input title="Downgrade Charge" :text.sync="otc.downgradeCharge"></btcl-input>
			<btcl-input title="Port Charge" :text.sync="otc.portCharge"></btcl-input>
			<btcl-input title="Shifting Charge" :text.sync="otc.shiftingCharge"></btcl-input>
			<btcl-input title="Fiber Charge" :text.sync="otc.fiberCharge"></btcl-input>
			<btcl-input title="Reconnection Charge" :text.sync="otc.reconnectionCharge"></btcl-input>
			<btcl-input title="Instant Closing Charge" :text.sync="otc.instantClosingCharge"></btcl-input>
			<btcl-input title="Cache Service Flat Rate" :text.sync="otc.cacheServiceFlatRate"></btcl-input>
			
			<btcl-input title="Max. VAT %" :text.sync="otc.maximumVatPercentage"></btcl-input>
			<btcl-input title="Max. Discount %" :text.sync="otc.maximumDiscountPercentage"></btcl-input>
			<btcl-field title="Suggested Date">
        		<btcl-datepicker :date.sync="otc.activationDate"></btcl-datepicker>
      		</btcl-field>
			<button type=button class="btn green-haze btn-block btn-lg" @click="submit">Update</button>
		</btcl-portlet>
	</btcl-body>
</div>
<script>
var vue = new Vue({
	el: "#btcl-application",
	data: {
		contextPath: context,
		otc : {}
	},
	methods: {
		submit : function () {
			axios.post(context + "lli/configuration/fixed-cost/insert.do", {
				otc: this.otc,
			})
			.then(result => {
				if(result.data.responseCode == 1) {
					toastr.success('Fixed Cost updated', 'Success');
				}else {
					toastr.error(result.data.msg, 'Failure');
				}
			})
			.catch(function (error) {
				console.log(error);
			});
		},
		getLatestOTC : function () {
			axios.get(context + "lli/configuration/fixed-cost/latest/view.do")
			.then(result => {
				if(result.data.responseCode == 1) {
					this.otc = result.data.payload;
				}else {
					toastr.error(result.data.msg, 'Failure');
				}
			})
			.catch(function (error) {
				console.log(error);
			});
		}
	},
	mounted () {
		this.getLatestOTC();
	}
});
</script>

