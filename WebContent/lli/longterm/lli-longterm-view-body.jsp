<div id=btcl-application>
	<btcl-body title="LLI Long Term" subtitle="Information">
		<div class=row>
			<div class=col-md-8>
				<btcl-portlet title=Connection>
					<btcl-info title="Contract ID" :text="contract.ID"></btcl-info>
					<btcl-info title="Status" :text="contract.status.label"></btcl-info><br>
		       		<btcl-info title="Client" :text="contract.client.label"></btcl-info>
		       		<btcl-info title="Bandwidth (Mbps)" :text="contract.bandwidth"></btcl-info>
					<btcl-info title="Contract Start Date" :text="contract.contractStartDate" date="true"></btcl-info>
					<btcl-info title="Contract End Date" :text="contract.contractEndDate" date="true"></btcl-info>
		   		</btcl-portlet>
			</div>
			<div class=col-md-4>
				<btcl-portlet title=History>
					<longterm-history></longterm-history>
		   		</btcl-portlet>
			</div>
		</div>
	</btcl-body>
</div>
<script>var id = '<%=request.getParameter("id")%>';</script>
<script src=lli-longterm-view.js></script>


