<div id=btcl-application>
	<btcl-body title="LLI Application" :subtitle="application.applicationTypeName">
           	<btcl-portlet>
           		<btcl-info title="Client" :text=application.clientID></btcl-info>
           		<btcl-info title="Application Date" :text=application.applicationDate></btcl-info>
           	</btcl-portlet>
           	<btcl-portlet>
           		<btcl-info v-for="(field, fieldIndex) in application.fields" :title="field.title" :text="field.value"></btcl-info>
           	</btcl-portlet>
	</btcl-body>
</div>

<script>
	var applicationInstanceID = "<%=request.getParameter("id")%>";
</script>
<script src=lli-application-view.js></script>


