<div id=btcl-application>
	<btcl-body title="Complete Request" :subtitle="'Application ID: ' + application.applicationID">
		<btcl-form :action="contextPath + 'lli/application/close-connection-complete.do'" :name="['application']" :form-data="[application]" :redirect="submitCallback">
        
	        <btcl-portlet :title="'LLI : '+connection.name">
				<btcl-portlet title=Connection>
					<btcl-info title="Client" :text=connection.client.label></btcl-info>
					<btcl-info title="Connection Name" :text=connection.name></btcl-info>
		       		<btcl-info title="Bandwidth (Mbps)" :text="connection.bandwidth"></btcl-info>
		       		<btcl-info title="Connection Type" :text="connection.connectionType.label"></btcl-info>
		            <btcl-info title="Status" :text="connection.status.label"></btcl-info>
		   		</btcl-portlet>
		          		
		       	<btcl-portlet :title="'Office '+(officeIndex+1)" v-for="(office,officeIndex) in connection.officeList">
		       		<btcl-info title="Office Name" :text=office.name></btcl-info>
		       		<btcl-info title="Address" :text="office.address"></btcl-info>
		         		
		       		<btcl-field :title="'Local Loop '+(localLoopIndex+1)" v-for="(localLoop, localLoopIndex) in office.localLoopList">
		       			<btcl-portlet>
		       				<inventory-autocomplete :itemid=localLoop.vlanID readonly></inventory-autocomplete>
		       				<btcl-info title="O/C" :text="localLoop.OC ? localLoop.OC.label : 'N/A'"></btcl-info>
		       				<btcl-info title="Client Distance" :text=localLoop.clientDistance></btcl-info>
		       				<btcl-info title="BTCL Distance" :text=localLoop.btclDistance></btcl-info>
		       				<btcl-info title="O/C Distance" :text=localLoop.OCDistance></btcl-info>
		       			</btcl-portlet>
		       		</btcl-field>
		       	</btcl-portlet>
			</btcl-portlet>
		
		</btcl-form>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=close-connection/lli-application-close-connection-completion.js></script>


