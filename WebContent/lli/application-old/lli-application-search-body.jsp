<%@page import="util.TimeConverter"%>
<%@page import="lli.LLIConnectionApplicationService"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="lli.LLIApplicationInstance"%>
<%@page import="java.util.ArrayList"%>
<%@page import="sessionmanager.SessionConstants"%>
<%
	String msg = null;
	String url = "lli/application/search";
	String navigator = SessionConstants.NAV_LLI_APPLICATION;
	String context = "../../.." + request.getContextPath() + "/";
%>
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
		<div class="table-responsive">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>ID</th>
						<th>Client</th>
						<th>Application Type</th>
						<th>Date</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
					<%
					ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_LLI_APPLICATION);
					if (data != null) {
						for (int i = 0; i < data.size(); i++) {
							LLIApplicationInstance lliApplicationInstance = (LLIApplicationInstance) data.get(i);
					%>
					<tr>
						<td>
							<a href="<%=context%>lli/application/view.do?id=<%=lliApplicationInstance.getID()%>"><%=lliApplicationInstance.getID()%></a>
						</td>
						<td>
							<%=AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(lliApplicationInstance.getClientID(), ModuleConstants.Module_ID_LLI).getLoginName() %>
						</td>
						<td>
							<%=ServiceDAOFactory.getService(LLIConnectionApplicationService.class).getApplicationTypeByID(lliApplicationInstance.getApplicationID()).getName()%>
						</td>
						<td>
							<%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(lliApplicationInstance.getApplicationDate(), "dd/MM/yyyy hh:mm a") %>
						</td>
						<td>
							Status
						</td>
					</tr>
					<% 	
					}
					%>
				</tbody>
				<% } %>
			</table>
		</div>
	</div>
</div>
