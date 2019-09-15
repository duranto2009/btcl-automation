<%@page import="common.CommonDAO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="lli.client.td.LLIClientTDService"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="vpn.client.ClientForm"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%@ page import="client.RegistrantTypeConstants" %>
<%@ page import="client.RegistrantCategoryConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.stream.Collector" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.*" %>
<%
	String msg = null;
	String url = "SearchClient";
	String navigator = SessionConstants.NAV_VPN_CLIENT;
	String context = "../../.." + request.getContextPath() + "/";
%>
<jsp:include page="/includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>
<%
	ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_VPN_CLIENT);
	List<Long> clientIds = new ArrayList<>();
	if(data!=null) {
		data.forEach(t->{
			clientIds.add(((ClientDetailsDTO)t).getClientID());
		});
	}



	int moduleID = Integer.parseInt(request.getParameter("moduleID"));
	Map<Long, String> map = new HashMap<>();
	if(moduleID == ModuleConstants.Module_ID_LLI) {
		map = ServiceDAOFactory.getService(LLIClientTDService.class).getClientTDStatusByClientIDs(clientIds);
	}
%>


<div class="portlet box">
	<div class="portlet-body">
		<html:form action="/DropClient" method="POST">
			<div class="table-responsive">
				<table id="example1" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>Name</th>
							<th>Username</th>
							<th>Client ID</th>
							<%--<th>Registration Type</th>--%>
							<th>Registrant Type</th>
							<th>Registrant Category</th>
							<th>Last Modification Time</th>
							<th>Client Status</th>
							<th>Mobile Number</th>
							<th>E-mail</th>
							<th>Edit</th>
						</tr>
					</thead>
					<tbody>

						<%

							if (data != null) {
								int size = data.size();
								CommonDAO commonDAO = new CommonDAO();
								
								for (int i = 0; i < size; i++) {
									try{
									ClientDetailsDTO row = (ClientDetailsDTO) data.get(i);
									
									request.setAttribute("firstName",!row.getVpnContactDetails().isEmpty()?row.getVpnContactDetails().get(0).getRegistrantsName():"");
									request.setAttribute("clientType",ClientForm.CLIENT_TYPE_STR.get(row.getClientCategoryType()));
									request.setAttribute("username",row.getLoginName());
									request.setAttribute("usernameURL",row.getLoginName());
//									if(row.getModuleID() == ModuleConstants.Module_ID_LLI && ServiceDAOFactory.getService(LLIClientTDService.class).getClientTDStatus(row.getClientID()).equals("TD") ){
									if(row.getModuleID() == ModuleConstants.Module_ID_LLI){
										String td = map.get(row.getClientID());
										if(td!=null && td.equalsIgnoreCase("TD")) {
											request.setAttribute("clientStatus","<label class='label label-danger'>TD</label>");
										}else {
											request.setAttribute("clientStatus",commonDAO.getActivationStatusName(row.getCurrentStatus()));
										}
									}else{
										request.setAttribute("clientStatus",commonDAO.getActivationStatusName(row.getCurrentStatus()));
									}
									request.setAttribute("mobile",!row.getVpnContactDetails().isEmpty()?row.vpnContactDetails.get(0).getPhoneNumber():"");
									request.setAttribute("email",!row.getVpnContactDetails().isEmpty()?row.vpnContactDetails.get(0).getEmail():"");
									
							%>
						<tr>
							<%if(row.getVpnContactDetails().size()>0) {%>
								<td><%=row.getVpnContactDetails().get(0).getRegistrantsName() %></td>
							<%}else{ %>
								<td></td>
							<%} %>
							
							<td>
								<a href="<%=context%>GetClientForView.do?moduleID=<%=row.getModuleID() %>&entityID=<%=row.getClientID()%>">${username }</a>
							</td>
							<%--<td><%=ClientForm.CLIENT_TYPE_STR.get(row.getClientCategoryType()) %></td>--%>
							<td><%=row.getClientID() %></td>
							<td><%=RegistrantTypeConstants.RegistrantTypeName.get(row.getRegistrantType()) %></td>
							<td><%=RegistrantCategoryConstants.RegistrantCategoryName.get(row.getRegistrantCategory())%></td>
							<td><%=new Date(row.getLastModificationTime())%></td>
							<td>${clientStatus }</td>
							<td>${mobile }</td>
							<td>${email }</td>
							<td><a href="<%=context%>GetClientForEdit.do?moduleID=<%=row.getModuleID() %>&entityID=<%=row.getClientID()%>&edit">Edit</a></td>
						</tr>
						<% 	
								}catch(Exception ex){
									ex.printStackTrace();
								}
								
								}
								 
						%>
					</tbody>
					<% } %>
				</table>
			</div>
		</html:form>
	</div>
</div>