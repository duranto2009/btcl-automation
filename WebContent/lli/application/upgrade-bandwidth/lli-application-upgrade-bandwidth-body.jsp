<div id=btcl-application v-cloak="true">
	<btcl-body title="Upgrade Bandwidth" subtitle="Application" :loader="loading">
		<btcl-form :callback="validatebw" :action="contextPath + 'lli/application/upgrade-bandwidth.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-field title="Client" required="true">
           			<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback"></lli-client-search>
           		</btcl-field>
           		<btcl-field title="Connection" required="true">
           			<multiselect v-model="application.connection" :options="connectionOptionListByClientID"
			        	 label=label :allow-empty="false" :searchable=true open-direction="bottom"
			        	@select="getConnectionByID">
			        </multiselect>
           		</btcl-field>
				<btcl-field v-if="application.client.registrantType==1" title="Want's to Pay DN(Connection Charge)">
					<multiselect v-model="application.skipPayment" :options="[{ID:1,label:'First Month Bill'},{ID:0,label:'Before Upgradation'}]"
								 track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
					</multiselect>
				</btcl-field>
           		<btcl-info v-if="Object.keys(existingConnection).length !== 0" title="Existing Bandwidth" :text="existingConnection.bandwidth + ' Mbps'"></btcl-info>
           		<btcl-input title="Extra Bandwidth (Mbps)" :text.sync="application.bandwidth" required="true" :number="true"></btcl-input>
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
           		<btcl-field title="Suggested Date" required="true">
           			<btcl-datepicker :date.sync="application.suggestedDate" ></btcl-datepicker>
           		</btcl-field>
           	</btcl-portlet>

		</btcl-form>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=lli-application-revise-connection.js></script>


