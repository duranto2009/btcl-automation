<div id=btcl-application>
	<btcl-body title="Complete Request" :subtitle="'Application ID: ' + application.applicationID">
		<btcl-form :action="contextPath + 'lli/application/reconnect-complete.do'" :name="['application']" :form-data="[application]" :redirect="submitCallback">
        
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
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=reconnect/lli-application-reconnect-completion.js></script>