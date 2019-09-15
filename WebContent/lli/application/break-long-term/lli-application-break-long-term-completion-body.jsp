<div id=btcl-application>
	<btcl-body title="Complete Request" :subtitle="'Application ID: ' + application.applicationID">
		<btcl-form :action="contextPath + 'lli/application/break-long-term-complete.do'" :name="['application']" :form-data="[application]" :redirect="submitCallback">
        
        	<btcl-portlet title="Long Term Contract">
	        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
	        	<btcl-info title="Contract ID" :text="application.contract.ID"></btcl-info>
	        	<btcl-info title="Contract Details" :text="application.contract.label"></btcl-info>
	        	<btcl-info title="Bandwidth to be broken (Mbps)" :text="application.bandwidth"></btcl-info>
	        	<btcl-info title="Contract Breaking Date" :text="application.suggestedDate" date></btcl-info>
	        	<btcl-info title="Description" :text="application.description"></btcl-info>        	
	        	<btcl-info title="Comment" :text="application.comment"></btcl-info>        	
        	</btcl-portlet>
			
		</btcl-form>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=break-long-term/lli-application-break-long-term-completion.js></script>




