<div id=btcl-application>
	<btcl-body title="Reconnect" subtitle="Application">
		<btcl-form :action="contextPath + 'lli/application/reconnect-edit.do'" :name="['application']" :form-data="[application]" :redirect="goView">
        	<btcl-portlet>
           		<btcl-info title="Client" :text="application.client.label"></btcl-info>
           		
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


