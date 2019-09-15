<div id=btcl-application>
	<btcl-body title="LLI" subtitle="New Long Term">
		<btcl-form :action="contextPath + 'lli/longterm/new.do'" :name="['contract']" :form-data="[contract]">
			<btcl-field title="Client">
				<lli-client-search :client.sync="contract.client">Client</lli-client-search>        		
       		</btcl-field>
       		<btcl-input title="Bandwidth" :text.sync="contract.bandwidth"></btcl-input>
       		<btcl-field title="Contract Start Date">
       			<btcl-datepicker :date.sync="contract.contractStartDate"></btcl-datepicker>
       		</btcl-field>
		</btcl-form>
	</btcl-body>
</div>

<script src=lli-longterm-new.js></script>


