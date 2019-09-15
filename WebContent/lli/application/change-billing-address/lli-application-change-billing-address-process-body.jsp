<div id=btcl-application>
	<btcl-body title="Process Application" :subtitle="'Application ID: ' + application.applicationID  + ' (' + application.applicationType.label + ')'">
		<div class=row>
			<!-- Application View -->
			<div class=col-md-4>
				<btcl-portlet title="Application">
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
			</div>
			<div class=col-md-8>
				<btcl-portlet title="Processing Window">
					Processing is complete.<br>
					Thank You!<br>
					Please Save and Finalize the application.<br>
		        </btcl-portlet>
			</div>
		</div>
		<button type=button class="btn btn-block btn-lg green-haze" @click="process('change-billing-address-process')">Save</button>
	</btcl-body>
</div>
<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=change-billing-address/lli-application-change-billing-address-process.js></script>


