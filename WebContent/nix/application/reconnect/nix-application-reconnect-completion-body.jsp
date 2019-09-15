<div id=btcl-application>
	<btcl-body title="Complete Request" :subtitle="'Application ID: ' + application.applicationID">
		<btcl-form :action="contextPath + 'nix/application/reconnect-complete.do'" :name="['application']" :form-data="[application]" :redirect="submitCallback">
        
        	<btcl-portlet :title="application.applicationType.label">
	        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
	        	
	        	<btcl-info title="Description" :text="application.description"></btcl-info>
	        	<btcl-info title="Comment" :text="application.comment"></btcl-info>
	        	<btcl-info title="Date" :text="application.suggestedDate" date></btcl-info>        	
        	</btcl-portlet>
			
		</btcl-form>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=nix-application-components.js></script>
<script src=reconnect/nix-application-reconnect-completion.js></script>