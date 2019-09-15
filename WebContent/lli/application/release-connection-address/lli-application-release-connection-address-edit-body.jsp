<div id=btcl-application>
	<btcl-body title="Release Connection Address" subtitle="Application">
		
		<btcl-form :action="contextPath + 'lli/application/release-connection-address-edit.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-info title="Client" :text="application.client.label" @loaded="getConnectionListByClient(application.client, true)"></btcl-info>
           		<btcl-field title="Connection">
           			<multiselect v-model="application.connection" :options="connectionOptionListByClientID" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom"
			        	@select="getOfficeListByConnectionID" @input="clearOffice">
			        </multiselect>
           		</btcl-field>
           		<btcl-field title="Select Office">
           			<multiselect v-model="application.office" :options="officeOptionListByConnectionID" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
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


