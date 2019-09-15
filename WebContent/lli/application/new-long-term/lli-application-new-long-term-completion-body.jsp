<div id=btcl-application>
	<btcl-body title="Complete Request" :subtitle="'Application ID: ' + application.applicationID">
		<btcl-form :action="contextPath + 'lli/application/new-long-term-complete.do'" :name="['application']" :form-data="[application]" :redirect="submitCallback">
        
        	<btcl-portlet title="Long Term Contract">
	        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
	        	<btcl-info title="Bandwidth (Mbps)" :text="application.bandwidth"></btcl-info>
	        	<btcl-info title="Contract Start Date" :text="application.suggestedDate" date></btcl-info>
	        	<btcl-info title="Description" :text="application.description"></btcl-info>        	
        	</btcl-portlet>
			
		</btcl-form>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=new-long-term/lli-application-new-long-term-completion.js></script>


