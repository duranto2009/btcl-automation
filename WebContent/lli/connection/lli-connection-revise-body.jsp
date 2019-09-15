<div id=btcl-application>
	
	<btcl-body title="LLI">
		<btcl-form :action="contextPath + 'lli/connection/revise.do'" :name="['connection']" :form-data="[connection]">
			
			<btcl-portlet title="Revise Connection">
	           	<btcl-field title="Client">
	           		<btcl-autocomplete :model.sync="selectedClient" url="lli/client/get-client.do" :selectcallback="clientSelectionCallback"></btcl-autocomplete>
	           	</btcl-field>
	           	<btcl-field title="Connection">
	           		<multiselect v-model="selectedConnection" :options="clientConnectionList" 
	            	:track-by=ID label=label :allow-empty="false" :searchable=true
	            	@select="connectionSelectionCallback">
	            	</multiselect>
	           	</btcl-field>
       		</btcl-portlet>
			<div v-if="Object.keys(connection).length !== 0">
				<btcl-portlet title=Connection>
					<btcl-info title="Status" :text="connection.status.label"></btcl-info>
					<btcl-field title="Incident" required>
		            	<multiselect v-model="connection.incident" :options="connection.options.incidentType" 
		            	:track-by=ID label=label :allow-empty="false" :searchable=true>
		            	</multiselect>
		           	</btcl-field>
				
	           		<btcl-input title="Connection Name" required :text.sync=connection.name></btcl-input>
	           		
					<btcl-field title="Bandwidth" required bare>
						<div class=col-sm-3><input type=text class='form-control' v-model="connection.gbBandwidth"></div>
						<div class=col-sm-1><label class='control-label'>Gb</label></div>
						<div class=col-sm-3><input type=text class='form-control' v-model="connection.mbBandwidth"></div>
						<div class=col-sm-1><label class='control-label'>Mb</label></div>
	            	</btcl-field>		 
	            	           	
	            	<btcl-field title="Activation Date" required>
		            	<input type=text class="form-control" v-model="connection.activeFrom">
	            	</btcl-field>
			            	
	            	<btcl-field title="Connection Type" required>
		            	<multiselect v-model="connection.connectionType" :options="connection.options.connectionType" 
		            	:track-by=ID label=label :allow-empty="false" :searchable=true>
		            	</multiselect>
		           	</btcl-field>
	       		</btcl-portlet>
		           		
	          	<btcl-portlet :title="'Office '+(officeIndex+1)" v-for="(office,officeIndex) in connection.officeList">
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
		           		<button type=button @click="deleteOffice(connection, officeIndex, $event)" class="btn red btn-outline">Remove this Office</button>
	           			<button type=button @click="addLocalLoop(office, $event)" class="btn green-haze btn-outline">Add Local Loop to this Office</button>
	           		</div>
	          	</btcl-portlet>
				<div align=right>
					<button type=button @click="addOffice(connection, $event)" class="btn green-haze btn-outline">Add Office</button>
				</div>
			</div>
		</btcl-form>
	</btcl-body>
</div>
<script src=lli-connection-revise.js></script>


