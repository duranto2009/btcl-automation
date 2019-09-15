<div id=btcl-application>
	<btcl-body title="Change Billing Address" subtitle="Application">
<%--
		<btcl-form :action="contextPath + 'lli/application/change-billing-address.do'" :name="['application']" :form-data="[application]" :redirect="goView">
--%>
        	<btcl-portlet>
           	
           		<btcl-field title="Client">
					<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback">Client</lli-client-search>
				</btcl-field>
       			<btcl-input title="First Name" :text.sync="application.clientDetailsDTO.registrantsName"></btcl-input>
       			<btcl-input title="Last Name" :text.sync="application.clientDetailsDTO.registrantsLastName"></btcl-input>
       			<btcl-input title="E-Mail" :text.sync="application.clientDetailsDTO.email"></btcl-input>
       			<btcl-input title="Mobile Number" :text.sync="application.clientDetailsDTO.phoneNumber"></btcl-input>
       			<btcl-input title="Telephone Number" :text.sync="application.clientDetailsDTO.landlineNumber"></btcl-input>
       			<btcl-input title="Fax Number" :text.sync="application.clientDetailsDTO.faxNumber"></btcl-input>
       			<btcl-input title="City" :text.sync="application.clientDetailsDTO.city"></btcl-input>
       			<btcl-input title="Post Code" :text.sync="application.clientDetailsDTO.postCode"></btcl-input>
       			<btcl-input title="Address" :text.sync="application.clientDetailsDTO.address"></btcl-input>
          		          		           	
           		<btcl-input title="Description" :text.sync="application.description"></btcl-input>
           		<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>	
           		<btcl-field title="Suggested Date"><btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker></btcl-field>
				<btcl-field>
					<div align="right">
						<button type="submit" class="btn green-haze" v-on:click="submitData" >Submit</button>
					</div>
				</btcl-field>

			</btcl-portlet>


<%--
		</btcl-form>
--%>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src="../application/lli-application-components.js"></script>
<script src="../application/change-billing-address/lli-application-change-billing-address.js"></script>


