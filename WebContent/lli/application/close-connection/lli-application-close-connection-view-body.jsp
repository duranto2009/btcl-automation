<div id=btcl-application>
	<btcl-body :title="application.applicationType.label + ' (' + application.applicationID +')'" subtitle="Information">
       	<btcl-portlet>
       		<btcl-info title="Status" :text="application.status.label"></btcl-info>
       		<btcl-field><forward-button title="Preview Processed Content" :url="'lli/application/preview-content.do?id='+application.applicationID"></forward-button></btcl-field>
       		<btcl-info v-if="application.status.ID == 50" title="Correction Reason" :text="application.requestForCorrectionComment"></btcl-info>
       		<btcl-info v-if="application.status.ID == 51" title="Rejection Comment" :text="application.rejectionComment"></btcl-info>
       		<br>
        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
        	<btcl-info title="Connection" :text="application.connection.label"></btcl-info>        	        	
        	<btcl-info title="Suggested Date" :text="application.suggestedDate" date></btcl-info>
        	<btcl-info title="Description" :text="application.description"></btcl-info>
        	<application-actions appid="<%=request.getParameter("id")%>"></application-actions>    	
        </btcl-portlet>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=lli-application-components.js></script>
<script src=close-connection/lli-application-close-connection-view.js></script>


