<div id=btcl-application>
	<btcl-body title="Downgrade Bandwidth" subtitle="Application">
		<btcl-form :callback="validatebw" :action="contextPath + 'lli/application/downgrade-bandwidth.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-field title="Client" required="true">
           			<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback"></lli-client-search>
           		</btcl-field>


           		<btcl-field title="Connection" required="true">
           			<multiselect v-model="application.connection" :options="connectionOptionListByClientID" 
			        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom"
			       		@select="getConnectionByID">
			        </multiselect>
           		</btcl-field>


				<btcl-field title="Downgrade Type">
					<multiselect
							v-model="application.policyType"
							:options="[{ID:1,label:'Downgrade Including Policy'},{ID:2,label:'Instant Downgrade'}]"
							track-by=ID
							label=label
							:allow-empty="false"
							:searchable=true
							open-direction="bottom">
					</multiselect>
				</btcl-field>

				<template v-if="application.client && application.policyType">
					<btcl-field v-if="application.client.registrantType==1 && application.policyType.ID==2"
					title="Want's to Pay DN(Connection Charge)"
					required="true">
					<multiselect v-model="application.skipPayment"
								 :options="[{ID:1,label:'First Month Bill'},{ID:0,label:'Before Connection'}]"
								 track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
					</multiselect>
					</btcl-field>
				</template>



				<btcl-info v-if="Object.keys(existingConnection).length !== 0" title="Existing Bandwidth" :text="existingConnection.bandwidth + ' Mbps'"></btcl-info>
           		<btcl-input required="true" title="Bandwidth to be decreased (Mbps)" :text.sync="application.bandwidth" :number="true"></btcl-input>
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
           		<btcl-field title="Suggested Date" required="true">
           			<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
           		</btcl-field>
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>

<script>
    var vue = new Vue({
        el: "#btcl-application",
        data: {
            contextPath: context,
            connectionOptionListByClientID: [],
            existingConnection: {},
            application: {}
        },
        methods: {
            clientSelectionCallback: function(clientID){
                if(clientID === undefined){
                    this.connectionOptionListByClientID = [];
                    this.application.connection = undefined;
                } else{
                    axios({ method: 'GET', 'url': context + 'lli/connection/get-active-connection-by-client.do?id=' + clientID})
                        .then(result => {
                            this.connectionOptionListByClientID = result.data.payload;
                        }, error => {
                        });
                }
            },
			validatebw: function(){
				if(this.application.client == null){
					toastr.error("Select Client", "Failure"); return false;

				}
				if(this.application.connection == null){
					toastr.error("Select Connection", "Failure"); return false;

				}
				if(this.application.bandwidth == null){
					toastr.error("Provide bandwidth", "Failure"); return false;

				}
				if(this.application.bandwidth <=0){
					toastr.error("Bandwidth must be positive", "Failure"); return false;

				}
				return true;
			},
            getConnectionByID: function(selectedOption, id){
                this.existingConnection = {};
                axios({ method: 'GET', 'url': context + 'lli/connection/revise-connection-json.do?id=' + selectedOption.ID})
                    .then(result => {
                        this.existingConnection = result.data.payload;
                    }, error => {
                    });
            },
            goView: function(result){
                if(result.data.responseCode == 2) {
                    toastr.error(result.data.msg);
                }else if(result.data.responseCode == 1){
                    toastr.success("Your request has been processed", "Success");
                    window.location.href = context + 'lli/application/search.do';
                    // window.location.href = context + 'lli/application/view.do?id=' + result.data.payload;
                }
                else{
                    toastr.error("Your request was not accepted", "Failure");
                }
            }
        },

        watch: {
          'application.bandwidth' : function(){
              if(this.application.bandwidth>this.existingConnection.bandwidth){
                  this.application.bandwidth = 0;
                  errorMessage("Downgrade bandwidth cannot be greater than existing bandwidth.");
                  this.$forceUpdate();
              }
              if(this.application.bandwidth < 0){
                  this.application.bandwidth = 0;
                  errorMessage("Downgrade bandwidth cannot be less than 0.");
                  this.$forceUpdate();
              }
          }
        },
    });
</script>