<div id=btcl-application>
	<btcl-body title="Temporary Upgrade Bandwidth" subtitle="Application">
		<btcl-form :action="contextPath + 'lli/application/temporary-upgrade-bandwidth.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-field title="Client">
           			<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback">Client</lli-client-search>
           		</btcl-field>
           		<btcl-field title="Connection">
           			<multiselect v-model="application.connection" :options="connectionOptionListByClientID" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom"
			        	@select="getConnectionByID">
			        </multiselect>
           		</btcl-field>
           		<btcl-info v-if="Object.keys(existingConnection).length !== 0" title="Existing Bandwidth" :text="existingConnection.bandwidth + ' Mbps'"></btcl-info>
           		<btcl-input title="Extra Bandwidth (Mbps)" :text.sync="application.bandwidth"></btcl-input>
           		<btcl-input title="Duration (days)" :text.sync="application.duration"></btcl-input>
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
           		<btcl-field title="Suggested Date">
           			<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
           		</btcl-field>
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=lli-application-revise-connection.js></script>


