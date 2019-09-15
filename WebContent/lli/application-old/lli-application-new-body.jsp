<div id=btcl-application>
	<btcl-body title="LLI Application" :subtitle="application.applicationTypeName">
		<btcl-form :action="contextPath + 'lli/application/new.do'" :name="['application']" :form-data="[application]">
           	<btcl-portlet>
           		<btcl-field title="Client">
					<btcl-autocomplete :model.sync="application.client" :url="contextPath + 'lli/client/get-client.do'">Client</btcl-autocomplete>           		
           		</btcl-field>
           	</btcl-portlet>
           	<btcl-portlet>
           		<btcl-input v-for="(field, fieldIndex) in application.fields" :title="field.title" :text.sync="field.value"></btcl-input>
           	</btcl-portlet>
		</btcl-form>
	</btcl-body>
</div>

<script>
	var applicationTypeID = "<%=request.getParameter("id")%>";
</script>
<script src=lli-application-new.js></script>


