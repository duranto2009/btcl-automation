<div id=btcl-application v-cloak="true">
	<btcl-body title="Shift Pop" subtitle="Application" :loader="loading">
		<btcl-form :action="contextPath + 'lli/application/shift-pop.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-field title="Client">
					<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback"></lli-client-search>
           		</btcl-field>
           		<btcl-field title="Connection">
           			<multiselect v-model="application.connection" :options="connectionOptionListByClientID" 
			        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom"
			        	@select="getDeepConnectionDetails">
			        </multiselect>
           		</btcl-field>
           		<btcl-field title="Select Existing Connection Office">
           			<multiselect v-model="application.office" :options="officeOptionListByConnectionID" 
			        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom"
			        	@select="setPopOptions">
			        </multiselect>
           		</btcl-field>
           		<btcl-field title="Select Existing PoP">
           			<multiselect v-model="application.oldpop" :options="popOptionListByOfficeID" 
			        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
           		</btcl-field>
           		<btcl-field title="New PoP">
           			<mounted-selection :model.sync="application.newpop" url='lli/inventory/get-inventory-options.do?categoryID=4'></mounted-selection>
           		</btcl-field>
           		<btcl-field title="Loop Provider">
           			<multiselect v-model="application.loopProvider" :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]" 
			        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
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
<script src=lli-application-submission.js></script>


