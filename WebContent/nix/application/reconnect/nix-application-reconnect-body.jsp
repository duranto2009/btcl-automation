<div id=btcl-application>
	<div class="row" v-if="loading" style="text-align: center">
		<i class="fa fa-spinner fa-spin fa-5x"></i>
	</div>
	<div v-else>
		<btcl-body title="Reconnect" subtitle="Application">
			<%--<btcl-form :action="contextPath + 'lli/application/reconnect.do'" :name="['application']" :form-data="[application]" :redirect="goView">--%>
			<btcl-portlet>
				<btcl-field title="Client">
					<multiselect v-model="revise.client"
								 :options="tdClientList"
								 :track-by=ID
								 label=label
								 :allow-empty="false"
								 :searchable=true
								 open-direction="bottom">
					</multiselect>
					<%--<user-search-by-module
							:client.sync="revise.client"
							:module="9"
							:callback="clientSelectionCallback">Client
					</user-search-by-module>--%>
				</btcl-field>

				<btcl-input title="Description" :text.sync="revise.description"></btcl-input>
				<btcl-input title="Comment" :text.sync="revise.comment"></btcl-input>
				<btcl-field title="Suggested Date"><btcl-datepicker :date.sync="revise.suggestedDate"></btcl-datepicker></btcl-field>
				<btcl-field>
					<div align="right">
						<button type="submit" class="btn green-haze" v-on:click="submitData" >Submit</button>
					</div>
				</btcl-field>
			</btcl-portlet>

			<%--</btcl-form>--%>
		</btcl-body>
	</div>
</div>
<script src=nix-application-components.js></script>
<script>
	var vue = new Vue({
		el: "#btcl-application",
		data: {
			revise: {},
			loading:false,
			tdClientList:[],
		},
		methods: {
			submitData: function () {
				var url = "reconnectrequest";
				this.loading = true;
				axios.post(context + 'nix/revise/' + url + '.do', {'data': JSON.stringify(this.revise)})
						.then(result => {
							if (result.data.responseCode == 2) {
								toastr.error(result.data.msg);
							} else if (result.data.responseCode == 1) {
								toastr.success("Your request has been processed", "Success");
								window.location.href = context + 'nix/revise/search.do';
							}
							else {
								toastr.error("Your request was not accepted", "Failure");
							}
							this.loading = false;
						})
						.catch(function (error) {
							this.loading = false;
						});
			},
		},
		created() {
			axios({method: 'GET', 'url': context + 'nix/revise/get-all-td.do'})
					.then(result => {
						if (result.data.responseCode == 1) {
							vue.tdClientList = result.data.payload;
						}
						else {
							toastr.error(result.data.msg,"Failure");
						}
					},
					error => {
						console.log(error);
					});
		},
	});
</script>


