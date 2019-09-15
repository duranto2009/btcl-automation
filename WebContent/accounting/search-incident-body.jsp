<%@page import="accounting.AccountType"%>
<%@page import="accounting.AccountingEntry"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="accounting.AccountingIncident"%>
<%@page import="java.util.List"%>
<%@ page import="common.repository.AllClientRepository" %>
<%
	String url = "accounting/incident/search";
	String context = "../" + request.getContextPath();
	String navigator = SessionConstants.NAV_ACC_INCIDENT;
	LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<jsp:include page="../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>
<div class="portlet box">
	<div class="portlet-body">
		<div class="table-responsive">
			<table id="tableData" class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th>Date</th>
						<th>Client</th>
						<th>Description</th>
						<th>Account Inforamtion</th>
						
					</tr>
				</thead>
				<tbody>
				<%
				
				List<AccountingIncident> data = (ArrayList<AccountingIncident>) session.getAttribute(SessionConstants.VIEW_ACC_INCIDENT);
				if(data != null){
					for(AccountingIncident accountingIncident : data){
						int totalAccEntries = accountingIncident.getAccountingEntries().size();
				%>
					<tr>
						<td>
							<%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(accountingIncident.getDateOfOccurance(), "dd/MM/yyyy")%>
						</td>
						<td>
							<%=AllClientRepository.getInstance().getClientByClientID(accountingIncident.getClientID()).getLoginName()%>
						</td>
						<td><a target="_blank" href=${context}accounting/incident/view.do?id=<%=accountingIncident.getID() %> >
							<%=accountingIncident.getDescription() %>
							</a>
						</td>
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
											<td><%=ae.getDebit() == 0.0 ? "-" : ae.getDebit()%></td>
											<td><%=ae.getCredit() == 0.0 ? "-" : ae.getCredit()%></td>
										</tr>
									
									<%} %>
								</tbody>
							</table>
						</td>
<!-- 						<td> -->
<!-- 							<table class="table table-striped table-hover"> -->
<!-- 								<tbody> -->
<%-- 									<%for(AccountingEntry ae: accountingIncident.getAccountingEntries()) {%> --%>
									
<!-- 										<tr> -->
<%-- 											<td><%=ae.getDebit() == 0.0 ? "-" : ae.getDebit()%></td> --%>
<!-- 										</tr> -->
									
<%-- 									<%} %> --%>
<!-- 								</tbody> -->
<!-- 							</table> -->
<!-- 						</td> -->
<!-- 						<td> -->
<!-- 							<table class="table table-striped table-hover"> -->
<!-- 								<tbody> -->
<%-- 									<%for(AccountingEntry ae: accountingIncident.getAccountingEntries()) {%> --%>
									
<!-- 										<tr> -->
<%-- 											<td><%=ae.getCredit() == 0.0 ? "-" : ae.getCredit()%></td> --%>
<!-- 										</tr> -->
									
<%-- 									<%} %> --%>
<!-- 								</tbody> -->
<!-- 							</table> -->
<!-- 						</td> -->
					</tr>
					<%			
					}
					%>
				</tbody>
				<%	
				}
				%>
			</table>
		</div>
	</div>
</div>