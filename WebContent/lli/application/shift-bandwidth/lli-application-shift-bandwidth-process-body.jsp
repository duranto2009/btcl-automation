<div id=btcl-application>
	<btcl-body title="Process Application" :subtitle="'Application ID: ' + application.applicationID  + ' (' + application.applicationType.label + ')'">
		<!-- Application View -->
		<btcl-portlet :title="'Application (' + application.status.label + ')'">
        	<btcl-info title="Client" :text="application.client.label"></btcl-info>
        	<btcl-info title="Bandwidth to be Shifted (Mbps)" :text="application.bandwidth"></btcl-info>
        	<btcl-info title="Description" :text="application.description"></btcl-info>
        	<btcl-info title="Comment" :text="application.comment"></btcl-info>
        	<btcl-info title="Suggested Date" :text="application.suggestedDate" date></btcl-info>
        	<button type=button class="btn red btn-outline" @click="setUpFreshContent(application.sourceConnection.ID, application.destinationConnection.ID)">Process from Scratch</button>
        </btcl-portlet>
		<br>
		<btcl-portlet title="Processing Window">
		<div class=row>
			<div class=col-md-6>
				<btcl-bounded title="Source Connection">
					<btcl-portlet>
						<btcl-info title="Connection Name" :text="content.sourceConnection.name"></btcl-info>
						<btcl-info title="Bandwidth" :text="content.sourceConnection.bandwidth"></btcl-info>
						<btcl-info title="Connection Type" :text="content.sourceConnection.connectionType.label"></btcl-info>
						<btcl-info title="Status" :text="content.sourceConnection.status.label"></btcl-info>
					</btcl-portlet>
					<btcl-portlet :title="'Office '+(officeIndex+1)" v-for="(office,officeIndex) in content.sourceConnection.officeList">
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
	          					<button type=button @click="deleteLocalLoop(content.sourceConnection, officeIndex, localLoopIndex, $event)" class="btn red btn-outline">Remove this Local Loop</button>
	          				</div>
		           		</btcl-bounded>
		           		<div align=right>
			           		<button type=button @click="deleteOffice(content.sourceConnection, officeIndex, $event)" class="btn red btn-outline">Remove this Office</button>
		           			<button type=button @click="addLocalLoop(office, $event)" class="btn green-haze btn-outline">Add Local Loop to this Office</button>
		           		</div>
		          	</btcl-portlet>
					<div align=right>
						<button type=button @click="addOffice(content.sourceConnection, $event)" class="btn green-haze btn-outline">Add Office</button>
					</div>
				</btcl-bounded>
			</div>
			<div class=col-md-6>
				<btcl-bounded title="Destination Connection">
					<btcl-portlet>
						<btcl-info title="Connection Name" :text="content.destinationConnection.name"></btcl-info>
						<btcl-info title="Bandwidth" :text="content.destinationConnection.bandwidth"></btcl-info>
						<btcl-info title="Connection Type" :text="content.destinationConnection.connectionType.label"></btcl-info>
						<btcl-info title="Status" :text="content.destinationConnection.status.label"></btcl-info>
					</btcl-portlet>
					<btcl-portlet :title="'Office '+(officeIndex+1)" v-for="(office,officeIndex) in content.destinationConnection.officeList">
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
	          					<button type=button @click="deleteLocalLoop(content.destinationConnection, officeIndex, localLoopIndex, $event)" class="btn red btn-outline">Remove this Local Loop</button>
	          				</div>
		           		</btcl-bounded>
		           		<div align=right>
			           		<button type=button @click="deleteOffice(content.destinationConnection, officeIndex, $event)" class="btn red btn-outline">Remove this Office</button>
		           			<button type=button @click="addLocalLoop(office, $event)" class="btn green-haze btn-outline">Add Local Loop to this Office</button>
		           		</div>
		          	</btcl-portlet>
					<div align=right>
						<button type=button @click="addOffice(content.destinationConnection, $event)" class="btn green-haze btn-outline">Add Office</button>
					</div>
				</btcl-bounded>
			</div>
		</div>
			
			
       		
        </btcl-portlet>
		<button type=button class="btn btn-block btn-lg green-haze" @click="process('shift-bandwidth-process')">Save</button>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=shift-bandwidth/lli-application-shift-bandwidth-process.js></script>


