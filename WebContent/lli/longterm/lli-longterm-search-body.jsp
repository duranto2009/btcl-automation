<%@page import="common.repository.AllClientRepository"%>
<%@page import="lli.LLILongTermContract"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.TimeConverter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%
	String url = "lli/longterm/search";
	String context = "../" + request.getContextPath();
	String navigator = SessionConstants.NAV_LONG_TERM;
	LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>
<div class="portlet box">
	<div class="portlet-body">
		<div class="table-responsive">
			<table id="tableData" class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th>ID</th>
						<th>Client Name</th>
						<th>Contract Start Date</th>
						<th>Bandwidth</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
				<%
				
				List<LLILongTermContract> data = (ArrayList<LLILongTermContract>) session.getAttribute(SessionConstants.VIEW_LONG_TERM);
				if(data != null){
					for(LLILongTermContract lliLongTermContract : data){
				%>
					<tr>
						<td>
							<a href=<%=request.getContextPath() + "/lli/longterm/view.do?id=" + lliLongTermContract.getID()%>>
								<%=lliLongTermContract.getID()%>
							</a>
						</td>
						<td>
							<%= AllClientRepository.getInstance().getClientByClientID( lliLongTermContract.getClientID()).getLoginName() %>
						</td>
						<td>
							<%= TimeConverter.getDateTimeStringByMillisecAndDateFormat( lliLongTermContract.getContractStartDate(),"dd/MM/yyyy")%>
						</td>
						<td>
							<%=lliLongTermContract.getBandwidth() %>
						</td>
						<td>
							<%=LLILongTermContract.statusMap.get(lliLongTermContract.getStatus())%>
						</td>
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