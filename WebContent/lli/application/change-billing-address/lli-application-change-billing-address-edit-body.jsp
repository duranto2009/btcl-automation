<div id=btcl-application>
	<btcl-body title="Change Billing Address" subtitle="Application">
		<btcl-form :action="contextPath + 'lli/application/change-billing-address-edit.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           	
           		<btcl-info title="Client" :text="application.client.label"></btcl-info>
           		
       			<btcl-input title="First Name" :text.sync="application.firstName"></btcl-input>	
       			<btcl-input title="Last Name" :text.sync="application.lastName"></btcl-input>	
       			<btcl-input title="E-Mail" :text.sync="application.email"></btcl-input>
       			<btcl-input title="Mobile Number" :text.sync="application.mobileNumber"></btcl-input>
       			<btcl-input title="Telephone Number" :text.sync="application.telephoneNumber"></btcl-input>
       			<btcl-input title="Fax Number" :text.sync="application.faxNumber"></btcl-input>
       			<btcl-input title="City" :text.sync="application.city"></btcl-input>
       			<btcl-input title="Post Code" :text.sync="application.postCode"></btcl-input>
       			<btcl-input title="Address" :text.sync="application.address"></btcl-input>
          		          		           	
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>	
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>	
           		<btcl-field title="Suggested Date"><btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker></btcl-field>

           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>
<script>var applicationID = '<%=request.getParameter("id")%>';</script>
<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=lli-application-edit.js></script>


