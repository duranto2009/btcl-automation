<div id="btcl-application" v-cloak="true">
	<btcl-body title="Change Ownership" subtitle="Application" :loader="loading">

        	<btcl-portlet>
           		<btcl-field title="Source Client" :required=true>
					<user-search-by-module
							:client.sync="application.srcClient"
							:callback="clientSelectionCallback"
							:module="6">
					</user-search-by-module>
           		</btcl-field>
				<btcl-field title="DestinationClient Client" :required=true>
					<multiselect v-model="application.dstClient"
								 :options="destinationClientList"
								 <%--track-by=ID--%>
								 label=value
								 @search-change="searchDestinationClient"
								 :allow-empty="false"
								 :searchable=true
								 open-direction="bottom">
					</multiselect>
				</btcl-field>
				<div v-if="connectionOptionListByClientID.length>0">
					<btcl-field title ="Active Links" :required=true>
						<select class="form-control"
								v-model="connection"
								@change="connectionSelectionCallback(connection)"
								style="margin-top: 7px;">
							<option value="0">Select Connection
							</option>
							<option v-bind:value="connection"
									v-for="connection in connectionOptionListByClientID">
								{{connection.linkName}}
							</option>
						</select>
					</btcl-field>

				</div>

				<div v-if="application.selectedConnectionList.length>0">
					<btcl-field title ="Selected Links">

                            <div style="margin-top: 2px;border: 1px solid;"
                                 class="form-control"
                                 v-for="(selectedConnection,index) in application.selectedConnectionList">
                                <div class="row">
                                    <div class="col-sm-10">{{selectedConnection.linkName}}</div>
                                    <div class="col-sm-2">
                                        <button  class="pull-right btn-primary" @click="removeSelectedConnection(index)"
                                                 type="button">x</button>
                                    </div>
                                </div>
                            </div>
					</btcl-field>

				</div>


				<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
           		
           		<btcl-field title="Suggested Date">
           			<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
           		</btcl-field>


					<div align="center">
						<button type="submit" class="btn btn-block green-haze" @click="submitFormData" >Submit</button>
					</div>
           	</btcl-portlet>

		<%--</btcl-form>--%>
	</btcl-body>
</div>

<script>

 new Vue({
		el: "#btcl-application",
		data: {
			contextPath: context,
			connectionOptionListByClientID: [],
			destinationClientList:[],
			application: {
				srcClient:{},
				dstClient:{},
				selectedConnectionList:[],

			},
			module:6,
			loading : false,
			connection : {},
		},
		methods: {
			hasError() {
				let error = false;
				if(!this.application.srcClient.key){
					errorMessage("Please Select Source Client");
					error = true;
				}
				if(!this.application.dstClient.key) {
					errorMessage("Please Select Destination Client");
					error = true;
				}
				if(!this.application.selectedConnectionList.length){
					errorMessage("Please Select at least one connection to change ownership");
					error = true;
				}

				return error;
			},
			submitFormData:function () {
				if(this.hasError()) {
					return;
				}
				var url1 = "insert";
				this.loading = true;
				Promise.resolve(
				axios.post(context + 'vpn/ownerchange/' + url1 + '.do', {'application': JSON.stringify(this.application)})
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
						}) ).then(() => this.loading = false);
			},
			clearConnection: function(){
				this.connectionOptionListByClientID = [];
			},
			clientSelectionCallback: function(clientID){
				if(clientID === undefined){
					this.clearConnection();
				} else{
					//todo active link fetch by client id


                    this.clearConnection();


					return axios({ method: 'GET', 'url': context + 'vpn/network/get-all-by-client.do?id=' + clientID})
							.then(result => {
								if(result.data.responseCode === 1) {


									this.connectionOptionListByClientID = result.data.payload;


								}else {
									errorMessage(result.data.msg);

								}

							}, error => {
							});

				}
			},
			searchDestinationClient:function(query){
				axios({method: 'GET', 'url': context + 'Client/all.do?query=' + query +'&module=' + this.module})
				//todo vpn client get
				//axios({method: 'GET', 'url': context + 'lli/client/get-client.do?val=' + query})
						.then(result => {

							this.destinationClientList = result.data.payload;
						}, error => {
						});
			},
			connectionSelectionCallback:function (connection) {

				this.application.selectedConnectionList.push(connection);
			},
			removeSelectedConnection: function (index) {
				this.application.selectedConnectionList.splice(index, 1);
			},

		},
		watch: {
			'application.dstClient.key': function () {
				if(this.application.srcClient.key === this.application.dstClient.key){
					errorMessage("You can't transfer to same client");
					this.application.dstClient = null;
				}
			},
		}
	});

</script>

