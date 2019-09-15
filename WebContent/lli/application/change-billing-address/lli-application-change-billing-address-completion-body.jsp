<div id=btcl-application>
	<btcl-body title="Complete Request" :subtitle="'Application ID: ' + application.applicationID">
		<btcl-form :action="contextPath + 'lli/application/change-billing-address-complete.do'" :name="['application']" :form-data="[application]" :redirect="submitCallback">
        
        	<btcl-portlet title="New Billing Address">
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
        	</btcl-portlet>
			
		</btcl-form>
	</btcl-body>
</div>
<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=change-billing-address/lli-application-change-billing-address-completion.js></script>


