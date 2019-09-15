<%@page import="common.repository.AllClientRepository"%>
<%@page import="util.TimeConverter"%>
<%@page import="complain.ComplainConstants"%>
<%@page import="user.UserRepository"%>
<%@page import="common.ModuleConstants"%>
<%@page import="complain.ComplainHistoryDTO"%>
<%@page import="complain.ComplainDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="vpn.VpnConnectionDTO"%>
<%@page import="vpn.VpnConnectionRepository"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ page language="java"%>

<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>



<!-- <div class="row"> -->

<%
String msg = null;
String url = "ComplainSearch";
String navigator = SessionConstants.NAV_COMPLAIN;
String context = "../.."  + request.getContextPath() + "/";
System.out.println(url + " "+ navigator + " "+ context);
String r_moduleID =request.getParameter("moduleID");
%>
<jsp:include page="../includes/navBill.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



		<div class="portlet box">
		<div class="portlet-body">
		<html:form action="/Complain"  method="POST">
			<div class="table-responsive">
					<table id="example1" class="table table-bordered table-striped table-hover">
						<thead>
							<tr>
								<th>Token</th>
								<th>Client Name</th>
								<th>Status</th>
								<th>Submission Date</th>
								<th>Priority</th>	
							</tr>
						</thead>
						<tbody>
						
						
							<%
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_COMPLAIN);
								System.out.println(data);
									if (data != null && data.size()>0) {
										int size = data.size();
										System.out.println("data size: "+data.size());
										for (int i = 0; i < size; i++) {
											String clientName = "";
											String moduleName = "";
											String priority = "";
											String status = "";
											ComplainDTO row = (ComplainDTO) data.get(i);
											long clientID = 0;
											clientID = row.getClientID();
											
											if(clientID>=0){
												if(AllClientRepository.getInstance().getClientByClientID(clientID) != null){
													clientName = AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName();//ClientRepository.getInstance().getClient(clientID).getLoginName();
												}else{
													clientName = "Client Data Error";
												}
											
											}
											Long complainTimeInLong = row.getCreationTime();
									
											int moduleID = row.getModuleID();
											
											if(ModuleConstants.ModuleMap.containsKey(moduleID)){
												moduleName = ModuleConstants.ModuleMap.get(moduleID);
											}
											if(ComplainConstants.PRIORITY_MAP.containsKey(row.getPriority())){
												priority = ComplainConstants.PRIORITY_MAP.get(row.getPriority());
											}		
											if(ComplainConstants.STATUS_MAP.containsKey(row.getStatus())){
												status = ComplainConstants.STATUS_MAP.get(row.getStatus());
											}
											
												
							%>
							<tr>
								<td><a href="<%=context%>Complain.do?moduleID=<%=r_moduleID %>&id=<%=row.getID()%>"><%=row.getID() %></a></td>
								<td><a href="<%=context%>GetClientForView.do?moduleID=<%=r_moduleID %>&entityID=<%=clientID%>"><%=clientName %></a></td>
								<td><%=status%></td>
								<td><%=TimeConverter.getTimeStringFromLong(row.getCreationTime()  ) %></td>
								<td><%=priority%></td>	
								
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
		</html:form>
		</div>
	</div>