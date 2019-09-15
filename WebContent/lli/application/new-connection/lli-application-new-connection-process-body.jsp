<div id=btcl-application>
	<btcl-body title="Process Application" :subtitle="'Application ID: ' + application.applicationID">
		<div class=row>
			<!-- Application View -->
			<div class=col-md-4>
				<btcl-portlet title="Application">
		       		<btcl-info title="Status" :text="application.status.label"></btcl-info>
		       		<btcl-info title="Application Type" text="New Connection"></btcl-info>
		        	<btcl-info title="Application ID" :text="application.applicationID"></btcl-info><br/>
		        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
		        	<btcl-info title="Bandwidth (Mbps)" :text="application.bandwidth"></btcl-info>
		        	<btcl-info title="Connection Type" :text="application.connectionType.label"></btcl-info>
		        	<btcl-info v-if="application.connectionType.ID===2" title="Duration (Days)" :text="application.duration"></btcl-info>
		        	<btcl-info title="Description" :text="application.description"></btcl-info>
		        	<btcl-info title="Connection Address" :text="application.address"></btcl-info>
		        	<btcl-info title="Loop Provider" :text="application.loopProvider.label"></btcl-info>
		        	<btcl-info title="Comment" :text="application.comment"></btcl-info>
		        	<btcl-info title="Suggested Date" :text="application.suggestedDate" date></btcl-info>
		        </btcl-portlet>
			</div>
			<div class=col-md-8>
				<btcl-portlet title="New Connection">
		       		<btcl-input title="Connection Name" required :text.sync=content.name></btcl-input>
		       		<btcl-portlet :title="'Office '+(officeIndex+1)" v-for="(office,officeIndex) in content.officeList">
		           		<btcl-input title="Office Name" required :text.sync=office.name></btcl-input>
		           		<btcl-input title="Address" required :text.sync="office.address"></btcl-input>
		           		
		           		<btcl-bounded v-for="(localLoop, localLoopIndex) in office.localLoopList" :title="'Local Loop '+(localLoopIndex+1)">
	           				<inventory-autocomplete :itemid.sync=localLoop.vlanID></inventory-autocomplete>
	           				<loop-distance :client.sync=localLoop.clientDistance
	           						:btcl.sync=localLoop.btclDistance
	           						:oc.sync=localLoop.OCDistance>
	           				</loop-distance>
	           				<oc :oc.sync=localLoop.OC></oc>
	           				<ofc-type :type.sync="localLoop.ofcType"></ofc-type>
	           				<div align=right>
	           					<button type=button @click="deleteLocalLoop(content, officeIndex, localLoopIndex, $event)" class="btn red btn-outline">Remove this Local Loop</button>
	           				</div>
		           		</btcl-bounded>
		           		<div align=right>
			           		<button type=button @click="deleteOffice(content, officeIndex, $event)" class="btn red btn-outline">Remove this Office</button>
		           			<button type=button @click="addLocalLoop(office, $event)" class="btn green-haze btn-outline">Add Local Loop to this Office</button>
		           		</div>
		          	</btcl-portlet>
		        </btcl-portlet>
			</div>
		</div>
		<button type=button class="btn btn-block btn-lg green-haze" @click="process('new-connection-process')">Save</button>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=new-connection/lli-application-new-connection-process.js></script>


