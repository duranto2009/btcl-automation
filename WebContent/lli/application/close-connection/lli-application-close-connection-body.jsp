<div id="btcl-application" v-cloak="true">
	<btcl-body title="Close Connection" subtitle="Application">
		<btcl-form :action="contextPath + 'lli/application/close-connection.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-field title="Client">
					<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback"></lli-client-search>           		
           		</btcl-field>
<!--            		<btcl-field title="Connection"> -->
<!--            			<input type=number class="form-control" v-model=application.connectionID> -->
<!--            		</btcl-field>           		 -->
				<btcl-field title="Connection">
           			<multiselect v-model="application.connection" :options="connectionOptionListByClientID" 
			        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom" @select="getConnectionByID">
			        </multiselect>
           		</btcl-field>
				<btcl-info v-if="Object.keys(existingConnection).length !== 0" title="Existing Bandwidth" :text="existingConnection.bandwidth + ' Mbps'"></btcl-info>

				<btcl-field title="Closing Type">
					<multiselect
							v-model="application.policyType"
							:options="[{ID:1,label:'Close Including Policy'},{ID:2,label:'Instant Close'}]"
							track-by=ID
							label=label
							:allow-empty="false"
							:searchable=true
							open-direction="bottom">
					</multiselect>
				</btcl-field>

           		
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>

           		<btcl-field title="Suggested Date">
           			<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
           		</btcl-field>
           		
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=close-connection/lli-application-close-connection.js></script>
<%--<script src=lli-application-revise-connection.js></script>--%>



