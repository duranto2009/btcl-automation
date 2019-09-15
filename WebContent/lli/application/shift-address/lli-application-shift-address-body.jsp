<div id=btcl-application>
	<btcl-body title="Shift Address" subtitle="Application">
		<btcl-form :action="contextPath + 'lli/application/shift-address.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-field title="Client">
					<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback">Client</lli-client-search>           		
           		</btcl-field>
           		<btcl-field title="Connection">
           			<multiselect v-model="application.connection" :options="connectionOptionListByClientID" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom"
			        	@select="connectionSelectionCallback">
			        </multiselect>
           		</btcl-field>
           		<btcl-field title="Select Connection Office">
           			<multiselect v-model="application.office" :options="officeOptionListByConnectionID" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
           		</btcl-field>
           		<btcl-input title="New Address" :text.sync="application.address"></btcl-input>           		
           		<btcl-field title="Loop Provider">
           			<multiselect v-model="application.loopProvider" :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
           		</btcl-field>
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
           		<btcl-field title="Suggested Date"><btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker></btcl-field>
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=shift-address/lli-application-shift-address.js></script>


