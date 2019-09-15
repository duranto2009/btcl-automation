<div id="bill-check" v-cloak="true">
	<btcl-body title="Bill" subtitle="Bill Check">
        	<btcl-portlet>
           		<btcl-field title="Client">
					<%--<lli-client-search :client.sync="application.client"></lli-client-search>--%>
						<user-search-by-module :client.sync="application.client" :module="moduleId">Client
						</user-search-by-module>
				</btcl-field>
				<btcl-field title="From Date">
					<btcl-datepicker :date.sync="application.fromDate"></btcl-datepicker>
				</btcl-field>
				<btcl-field title="To Date">
					<btcl-datepicker :date.sync="application.toDate"></btcl-datepicker>
				</btcl-field>
				<button type=button class="btn green-haze btn-block btn-lg" @click="checkBill">Submit</button>
           	</btcl-portlet>
	</btcl-body>
</div>
<script>
	let moduleID = <%=request.getParameter("moduleId")%>;
</script>
<script src=../bill-check.js></script>


