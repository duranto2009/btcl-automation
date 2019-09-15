<div id=btcl-application>
	<btcl-body title="Complete Request" :subtitle="'Application ID: ' + application.applicationID">
		<btcl-form :action="contextPath + 'lli/application/shift-bandwidth-complete.do'" :name="['application']" :form-data="[application]" :redirect="submitCallback">
        
        
	        <lli-connection-view v-if="content.CONTENTTYPE == 'connection'" :lliconnection.sync=content pop-fixed></lli-connection-view>
			<div class=row v-else-if="content.CONTENTTYPE == 'two-connection'">
	        	<div class=col-md-6>
	        		<lli-connection-view :lliconnection.sync=content.sourceConnection pop-fixed></lli-connection-view>
	        	</div>
	        	<div class=col-md-6>
	        		<lli-connection-view :lliconnection.sync=content.destinationConnection pop-fixed></lli-connection-view>
	        	</div>
	        </div>
			
			<btcl-portlet>
	       		<btcl-field title="Active From"><btcl-datepicker :date.sync="activeFrom"></btcl-datepicker></btcl-field>
	       	</btcl-portlet>

		</btcl-form>
	</btcl-body>
</div>
<script>
	var applicationID = '<%=request.getParameter("id")%>';
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-application-components.js></script>
<script src=shift-bandwidth/lli-application-shift-bandwidth-completion.js></script>


