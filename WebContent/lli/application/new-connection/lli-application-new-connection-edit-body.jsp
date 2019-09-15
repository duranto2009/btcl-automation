<div id=btcl-application>
	<btcl-body title="Edit Application" :subtitle="application.applicationType.label">
		<btcl-form :action="contextPath + 'lli/application/new-connection-edit.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
        		<btcl-info title="Client" :text="application.client.label"></btcl-info>
           		
           		<btcl-input title="Bandwidth (Mbps)" :text.sync="application.bandwidth"></btcl-input>
           		<btcl-field title="Connection Type">
           			<connection-type :data.sync=application.connectionType :client="application.client"></connection-type>
           		</btcl-field>
           		<btcl-input title="Connection Address" :text.sync="application.address"></btcl-input>
           		<btcl-field title="Loop Provider">
           			<multiselect v-model="application.loopProvider" :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
           		</btcl-field>
           		<btcl-field v-if="application.connectionType && application.connectionType.ID==2" title="Duration (Days)">
           			<input type=number class="form-control" v-model=application.duration>
           		</btcl-field>
           		
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
           		<btcl-field title="Suggested Date"><btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker></btcl-field>
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>
<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=lli-application-edit.js></script>
