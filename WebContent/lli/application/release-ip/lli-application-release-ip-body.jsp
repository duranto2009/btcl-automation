<div id=btcl-application>
	<btcl-body title="Release Port" subtitle="Application">
		<btcl-form :action="contextPath + 'lli/application/release-port.do'" :name="['application','content']" :form-data="[application,content]">
        	<btcl-portlet>
           		<btcl-field title="Client">
					<lli-client-search :client.sync="application.client">Client</lli-client-search>           		
           		</btcl-field>
           		<btcl-field title="Connection">
           			<input type=number class="form-control" v-model=application.connectionID>
           		</btcl-field>
           		<btcl-field title="Select Office">
           			<input type=number class="form-control" v-model=application.officeID>
           		</btcl-field>
           		<btcl-field title="Port Count">
           			<input type=number class="form-control" v-model=application.portCount>
           		</btcl-field>           		
           		<btcl-field title="Suggested Date">
           			<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
           		</btcl-field>
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=release-port/lli-application-release-port.js></script>


