<div id=btcl-application>
	<btcl-body title="Process Application" :subtitle="'Application ID: ' + application.applicationID  + ' (' + application.applicationType.label + ')'">
		<div class=row>
			<!-- Application View -->
			<div class=col-md-4>
				<btcl-portlet title="Application">
		       		<btcl-info title="Status" :text="application.status.label"></btcl-info>

		        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
		        	<btcl-info title="Contract ID" :text="application.contract.ID"></btcl-info>
		        	<btcl-info title="Bandwidth (Mbps)" :text="application.bandwidth"></btcl-info>
		        	<btcl-info title="Description" :text="application.description"></btcl-info>
		        	<btcl-info title="Comment" :text="application.comment"></btcl-info>
		        	
		        	<btcl-info title="Suggested Date" :text="application.suggestedDate" date></btcl-info>
		        </btcl-portlet>
			</div>
			<div class=col-md-8>
				<btcl-portlet title="Processing Window">
					Processing is complete.<br>
					Thank You!<br>
					Please Save and Finalize the application.<br>
		        </btcl-portlet>
			</div>
		</div>
		<button type=button class="btn btn-block btn-lg green-haze" @click="process('break-long-term-process')">Save</button>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=break-long-term/lli-application-break-long-term-process.js></script>


