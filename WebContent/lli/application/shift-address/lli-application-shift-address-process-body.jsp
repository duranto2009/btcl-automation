<div id=btcl-application>
	<btcl-body title="Process Application" :subtitle="'Application ID: ' + application.applicationID  + ' (' + application.applicationType.label + ')'">
		<div class=row>
			<!-- Application View -->
			<div class=col-md-4>
				<btcl-portlet title="Application">
		       		<btcl-info title="Status" :text="application.status.label"></btcl-info>
		        	<br>
		        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
		        	<btcl-info title="Connection" :text="application.connection.label"></btcl-info>
		        	<btcl-info title="Old Office" :text="application.office.label"></btcl-info>
		        	<btcl-info title="New Address" :text="application.address"></btcl-info>
		        	<btcl-info title="Loop Provider" :text="application.loopProvider.label"></btcl-info>
		        	<btcl-info title="Description" :text="application.description"></btcl-info>
		        	<btcl-info title="Comment" :text="application.comment"></btcl-info>
		        	<btcl-info title="Suggested Date" :text="application.suggestedDate" date></btcl-info>
		        </btcl-portlet>
		        <button type=button class="btn btn-block red btn-outline" @click="setUpFreshContent(application.connection.ID, application.applicationType.ID)">Process from Scratch</button>
			</div>
			<div class=col-md-8>
				<btcl-portlet title="Processing Window">
					<btcl-portlet>
						<btcl-info title="Client" :text=content.client.label></btcl-info>
						<btcl-info title="Connection Name" :text="content.name"></btcl-info>
						<btcl-info title="Bandwidth" :text="content.bandwidth"></btcl-info>
						<btcl-info title="Connection Type" :text="content.connectionType.label"></btcl-info>
						<btcl-info title="Status" :text="content.status.label"></btcl-info>
					</btcl-portlet>
					
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
					<div align=right>
						<button type=button @click="addOffice(content, $event)" class="btn green-haze btn-outline">Add Office</button>
					</div>
		        </btcl-portlet>
			</div>
		</div>
		<button type=button class="btn btn-block btn-lg green-haze" @click="process('shift-address-process')">Save</button>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=lli-application-revise-connection-process.js></script>


