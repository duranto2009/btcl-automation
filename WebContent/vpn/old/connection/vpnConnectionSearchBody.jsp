<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.ClientRepository"%>
<%@page import="vpn.VpnConnectionDTO"%>
<%@page import="vpn.VpnConnectionRepository"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>



<!-- <div class="row"> -->

			<%
				String msg = null;
				/* if ((msg = (String) session.getAttribute(util.ConformationMessage.CLIENT_EDIT)) != null) {
					session.removeAttribute(util.ConformationMessage.CLIENT_EDIT);
				} */

				String url = "VpnConnectionSearch";
				String navigator = SessionConstants.NAV_VPN_CONNECTION;
				String context = "" + request.getContextPath() + "/";
			%>
			<jsp:include page="../../../includes/nav.jsp" flush="true">
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
			</jsp:include>



		<div class="portlet box">
		<div class="portlet-body">
		<html:form action="/DropClient" method="POST">
			<div class="table-responsive">
					<table id="example1" class="table table-bordered table-striped">
						<thead>
							<tr>
																
																<th>Connection Name</th>																
																<th>Client Username</th>
																<th>Description</th>
																<th>Application Date</th>
																
															<th>Expire Date</th>
					
								
							</tr>
						</thead>
						<tbody>
						
						
							<%
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_VPN_CONNECTION);

									if (data != null) {
										int size = data.size();
										for (int i = 0; i < size; i++) {
											VpnConnectionDTO row = (VpnConnectionDTO) data.get(i);
											String aplicationDateString ="";
											String expireDateString ="";
											DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										if(row.getApplicationDate()>0){
											Date applicationDate =new Date(row.getApplicationDate());
											
										
											 aplicationDateString= df.format(applicationDate).toString();
										}
										if(row.getExpireDate()>0){
											Date expireDate =new Date(row.getExpireDate());
											
											
											expireDateString= df.format(expireDate).toString();
										}
										long clientID = row.getServiceClientID();
										String clientName = AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName();
										
											
							%>
							<tr>
							
															<td><a href="<%=context%>VpnConnectionAction.do?id=<%=row.getID()%>"><%=row.getCnName() %></a></td>
														<td><a href="<%=context%>GetClientForView.do?id=<%=clientID%>"><%=clientName %></a></td>
															<td><%=row.getCnDescription()%></td>
															
															
														<td><%=aplicationDateString %></td>	
														
													<td><%=expireDateString %></td>
												
												
								
								
								
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