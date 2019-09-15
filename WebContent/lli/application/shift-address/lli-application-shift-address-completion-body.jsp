<div id=btcl-application>
	<btcl-body title="Complete Request" :subtitle="'Application ID: ' + application.applicationID">
		<btcl-form :action="contextPath + 'lli/application/shift-address-complete.do'" :name="['application']" :form-data="[application]" :redirect="submitCallback">
        
        
        <lli-connection-view v-if="content.CONTENTTYPE == 'connection'" :lliconnection.sync=content pop-fixed></lli-connection-view>
		
		<btcl-portlet>
       		<btcl-field title="Active From"><btcl-datepicker :date.sync="content.activeFrom"></btcl-datepicker></btcl-field>
       	</btcl-portlet>
	
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=shift-address/lli-application-shift-address-completion.js></script>


