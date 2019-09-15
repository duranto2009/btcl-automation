<%@page import="util.TimeConverter"%>
<%@page import="accounting.AccountType"%>
<%@page import="accounting.AccountingEntry"%>
<%@page import="accounting.AccountingIncident"%>
<%@ page import="java.math.BigDecimal" %>
<%
	AccountingIncident accountingIncident = (AccountingIncident) request.getAttribute("incident");

%>
<div id=btcl-application>
	<btcl-body title="Accounting" subtitle='Incident'>
		<table class="table table-bordered table-striped table-hover">
		<caption> Date: <%=TimeConverter.getTimeStringByDateFormat(accountingIncident.getDateOfRecord(), "dd/MM/yyyy") %> Incident ID: <%=accountingIncident.getID() %></caption>
			<thead>
				<tr>
					<th>Description</th>
					<th>Account Information</th>
				</tr>
			</thead>
			<tbody>
				<td><%=accountingIncident.getDescription() %>
				<td>
					<table class="table table-striped table-hover">
						<thead>
							<th>Account Name</th>
							<th>Dr/Cr</th>
							<th>Debit</th>
							<th>Credit</th>
						</thead>
						<tbody>
						<%for(AccountingEntry ae: accountingIncident.getAccountingEntries()) {%>
						
							<tr>
								<td><%=AccountType.getAccountTypeByAccountID(ae.getAccountID()).getName() %></td>
								<td><%=AccountType.getAccountTypeByAccountID(ae.getAccountID()).isCreditAccount() ? "Cr" : "Dr" %>
								<td><%=ae.getDebit() == 0.0 ? "-" : new BigDecimal(ae.getDebit()).toPlainString()%></td>
								<td><%=ae.getCredit() == 0.0 ? "-" : new BigDecimal(ae.getCredit()).toPlainString()%></td>
							</tr>
						
						<%} %>
						</tbody>
					</table>
				</td>
			</tbody>
		</table>		
	</btcl-body>
</div>

<script>
var vue = new Vue({
	el: "#btcl-application",
	data: {

		contextPath: context,
	},
	methods: {
		
	}
});
</script>