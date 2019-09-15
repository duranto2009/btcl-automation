<div id=btcl-application>
	<btcl-body title="LLI" subtitle="New Connection">
		<btcl-form :action="contextPath + 'lli/application/build.do'" :name="['application']" :form-data="[form]">
			<application-builder :localform=form></application-builder>
		</btcl-form>
	</btcl-body>
</div>

<script src=lli-application-build.js></script>


