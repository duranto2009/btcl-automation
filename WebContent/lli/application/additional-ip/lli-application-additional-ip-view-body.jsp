<div id=btcl-application>
	<btcl-body title="Additional Port" subtitle="Information">
       	<btcl-portlet>
       		<btcl-info title="Status" :text="application.status.label"></btcl-info>
       		<btcl-info title="Application Type" text="New Connection"></btcl-info>
        	<btcl-info title="Application ID" :text="application.applicationID"></btcl-info><br/>
        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
        	<btcl-info title="Connection" :text="application.connection.label"></btcl-info>
        	<btcl-info title="Office" :text="application.officeID"></btcl-info>
        	<btcl-info title="Port Count" :text="application.portCount"></btcl-info>        	
        	<btcl-info title="Loop Provider" :text="application.loopProvider.label"></btcl-info>
        	<btcl-info title="Description" :text="application.description"></btcl-info>
        	<btcl-info title="Suggested Date" :text="application.suggestedDate"></btcl-info>
        	<application-actions appid="<%=request.getParameter("id")%>"></application-actions>    	
        </btcl-portlet>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=lli-application-components.js></script>
<script src=lli-application-additional-port-view.js></script>


