<div id=btcl-application>

	<btcl-body title="Reconnect" subtitle="Application">

		<btcl-portlet>

			<btcl-field title="Client">
				<user-search-by-module
						:client.sync="revise.client"
						:module="6">Client
				</user-search-by-module>
			</btcl-field>

			<btcl-input title="Description"
						:text.sync="revise.description">
			</btcl-input>

			<btcl-input title="Comment"
						:text.sync="revise.comment">
			</btcl-input>

			<btcl-field title="Suggested Date">
				<btcl-datepicker :date.sync="revise.suggestedDate">
				</btcl-datepicker>
			</btcl-field>

			<btcl-field>
				<div align="right">
					<button type="submit"
							class="btn green-haze"
							v-on:click="submitData" >Submit
					</button>
				</div>
			</btcl-field>

		</btcl-portlet>

	</btcl-body>

</div>

<script>
	var vue = new Vue({
		el: "#btcl-application",
		data: {
			revise: {}
		},
		methods: {
			submitData: function () {
				var url = "request";
				axios.post(context + 'vpn/reconnect/' + url + '.do', {'data': JSON.stringify(this.revise)})
						.then(result => {
							if (result.data.responseCode == 2) {
								toastr.error(result.data.msg);
							} else if (result.data.responseCode == 1) {
								toastr.success("Your request has been processed", "Success");
								window.location.href = context + 'vpn/link/search.do';
							}
							else {
								toastr.error("Your request was not accepted", "Failure");
							}
						})
						.catch(function (error) {
						});
			},
		}
	});
</script>


