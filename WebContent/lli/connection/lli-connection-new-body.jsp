<div id=btcl-application>
	<btcl-body title="LLI" subtitle="New Connection">
		<btcl-form :action="contextPath + 'lli/connection/new.do'" :name="['connection']" :form-data="[connection]">
			<btcl-portlet title=Connection>

				<btcl-field title="Client" required>
           			<lli-client-search :client.sync="connection.client"></lli-client-search>
            	</btcl-field>
           		<btcl-input title="Connection Name" required :text.sync=connection.name></btcl-input>
           		
           		<btcl-input title="Bandwidth" required :text.sync=connection.bandwidth></btcl-input>
           		
            	<btcl-field title="Connection Type" required>
	            	<multiselect v-model="connection.connectionType" :options="connection.options.connectionType" 
	            	:track-by=ID label=label :allow-empty="false" :searchable=true>
	            	</multiselect>
            	</btcl-field>
	
				<btcl-field title="Activation Date" required>
	            	<input type=text class="form-control" v-model="connection.activeFrom">
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
		</btcl-form>
	</btcl-body>
</div>

<script src=lli-connection-new.js></script>


