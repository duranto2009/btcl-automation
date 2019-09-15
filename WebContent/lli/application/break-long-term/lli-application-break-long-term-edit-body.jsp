<div id=btcl-application>
	<btcl-body title="Break Long Term" subtitle="Application">
		<btcl-form :action="contextPath + 'lli/application/break-long-term-edit.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
        		<btcl-info title="Client" :text="application.client.label" @loaded="getLongTermContractListByClientID(application.client)"></btcl-info>
           		<btcl-field title="Contract">
           			<multiselect v-model="application.contract" :options="contractOptionListByClientID" 
			        	:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
           		</btcl-field>            		        	
           		<btcl-field title="Bandwidth (Mbps)">
           			<input type=number class="form-control" v-model=application.bandwidth>
           		</btcl-field>
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>
           		<btcl-field title="Date"><btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker></btcl-field>
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>
<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=lli-application-edit.js></script>


