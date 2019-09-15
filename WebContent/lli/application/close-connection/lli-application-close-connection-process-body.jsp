<div id=btcl-application>
	<btcl-body title="Process Application" :subtitle="'Application ID: ' + application.applicationID">
		<div class=row>
			<!-- Application View -->
			<div class=col-md-4>
				<btcl-portlet title="Application">
		       		<btcl-info title="Status" :text="application.status.label"></btcl-info>
		       		<btcl-info title="Application Type" :text="application.applicationType.label"></btcl-info>
		        	<btcl-info title="Application ID" :text="application.applicationID"></btcl-info><br/>
		        	
		        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
		        	<btcl-info title="Connection" :text="application.connection.label"></btcl-info>
		        	
		        	<btcl-info title="Comment" :text="application.comment"></btcl-info>
		        	<btcl-info title="Description" :text="application.description"></btcl-info>
		        	<btcl-info title="Suggested Date" :text="application.suggestedDate" date></btcl-info>
		        </btcl-portlet>
			</div>
			<div class=col-md-8>
				<btcl-portlet title="Processing Window">
		       		
		       		The processing of this application has finished.
		       		<br>
		       		Thank you.
		       		<br> 
		       		You may save and finalize the application.
		        </btcl-portlet>
			</div>
		</div>
		<button type=button class="btn btn-block btn-lg green-haze" @click="process('close-connection-process')">Save</button>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=close-connection/lli-application-close-connection-process.js></script>


