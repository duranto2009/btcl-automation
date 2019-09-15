<div id=btcl-application>
	<btcl-body :title="application.applicationType.label" :subtitle="'Application ID: ' + application.applicationID">
       	<btcl-portlet>
       		<btcl-info title="Status" :text="application.status.label"></btcl-info>
       		<btcl-field><forward-button title="Preview Processed Content" :url="'lli/application/preview-content.do?id='+application.applicationID"></forward-button></btcl-field>
        	<btcl-info v-if="application.status.ID == 50" title="Correction Reason" :text="application.requestForCorrectionComment"></btcl-info>
       		<btcl-info v-if="application.status.ID == 51" title="Rejection Comment" :text="application.rejectionComment"></btcl-info>
	       	<br>
        	<btcl-info title="Client Name" :text="application.client.label"></btcl-info>
        	
        	<btcl-info title="First Name" :text="application.firstName"></btcl-info>	
   			<btcl-info title="Last Name" :text="application.lastName"></btcl-info>	
   			<btcl-info title="E-Mail" :text="application.email"></btcl-info>
   			<btcl-info title="Mobile Number" :text="application.mobileNumber"></btcl-info>
   			<btcl-info title="Telephone Number" :text="application.telephoneNumber"></btcl-info>
   			<btcl-info title="Fax Number" :text="application.faxNumber"></btcl-info>
   			<btcl-info title="City" :text="application.city"></btcl-info>
   			<btcl-info title="Post Code" :text="application.postCode"></btcl-info>
   			<btcl-info title="Address" :text="application.address"></btcl-info>
        	
        	<btcl-info title="Description" :text="application.description"></btcl-info>
        	<btcl-info title="Comment" :text="application.comment"></btcl-info>
        	<btcl-info title="Suggested Date" :text="application.suggestedDate" date></btcl-info>
        	<application-actions appid="<%=request.getParameter("id")%>"></application-actions>    	
        </btcl-portlet>
	</btcl-body>
</div>
<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src=lli-application-components.js></script>
<script src=change-billing-address/lli-application-change-billing-address-view.js></script>


