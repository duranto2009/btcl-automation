<%@page import="lli.link.LliLinkService"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ClientRepository"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>


	<%
		String msg = null;

		String url = "LliLinkSearch";
		String navigator = SessionConstants.NAV_LLI_LINK;
		String context = "../../.."  + request.getContextPath() + "/";
	%>
	<jsp:include page="../../includes/nav.jsp" flush="true">
		<jsp:param name="url" value="<%=url%>" />
		<jsp:param name="navigator" value="<%=navigator%>" />
	</jsp:include>



		<div class="portlet box portlet-btcl light">
		<div class="portlet-body">
		<html:form action="/DropClient" method="POST">
			<div class="table-responsive">
					<table id="example1" class="table table-bordered table-striped table-hover">
						<thead>
							<tr>
								<th> Connection Name </th>
								<th> Client </th>
								<th> Bandwidth </th>
								<th> Activation Date </th>
								<th> Status </th>
							</tr>
						</thead>
						<tbody>
							<%
								CommonDAO commonDAO = new CommonDAO();
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_LLI_LINK);
								LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
									if (data != null) {
										int size = data.size();
										for (int i = 0; i < size; i++) {
											LliLinkDTO row = (LliLinkDTO) data.get(i);
											long clientID = row.getClientID();
											String clientName = AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName();
											String activationDateString="N/A";
											Long activationDateInLong = row.getActivationDate();
											if(activationDateInLong != null && activationDateInLong>0){
												activationDateString=TimeConverter.getMeridiemTime(activationDateInLong);
											}
											request.setAttribute("status", commonDAO.getActivationStatusName(row.getCurrentStatus()));
							%>
							<tr>
								<td><a href="<%=context%>LliLinkAction.do?entityID=<%=row.getID()%>&entityTypeID=<%=EntityTypeConstant.LLI_LINK%>"><%=row.getLinkName()%></a></td>
								<td><a href="<%=context%>GetClientForView.do?entityID=<%=clientID%>&entityTypeID=<%=EntityTypeConstant.LLI_CLIENT%>&moduleID=<%=ModuleConstants.Module_ID_LLI%>"><%=clientName %></a></td>
								<td><%=row.getLliBandwidth() %> <%=EntityTypeConstant.linkBandwidthTypeMap.get(row.getLliBandwidthType())%></td>	
								<td><%=activationDateString %></td>
								<td>${status}</td>
							</tr>
							<% } %>
						</tbody>
						<%
							}
						%>

					</table>
				
			</div>
		</html:form>
		</div>
	</div>