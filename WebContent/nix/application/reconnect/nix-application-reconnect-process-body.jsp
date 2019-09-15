<div id=btcl-application>
	<btcl-body title="Process Application" :subtitle="'Application ID: ' + application.applicationID  + ' (' + application.applicationType.label + ')'">
		<div class=row>
			<!-- Application View -->
			<div class=col-md-4>
				<btcl-portlet title="Application">
		        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
		        	<btcl-info title="Description" :text="application.description"></btcl-info>
		        	<btcl-info title="Comment" :text="application.comment"></btcl-info>
		        	<btcl-info title="Date" :text="application.suggestedDate" date></btcl-info>
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
		<button type=button class="btn btn-block btn-lg green-haze" @click="process('reconnect-process')">Save</button>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=nix-application-components.js></script>
<script src=reconnect/nix-application-reconnect-process.js></script>


