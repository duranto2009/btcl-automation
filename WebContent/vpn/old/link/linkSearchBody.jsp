<%@page import="util.ServiceDAOFactory"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
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


<%
boolean showTerminationOption = true;
String msg = null;

String url = "VpnLinkSearch";
String navigator = SessionConstants.NAV_VPN_LINK;
String context = "" + request.getContextPath() + "/";

VpnLinkService vpnLinkService = ServiceDAOFactory.getService(VpnLinkService.class);
String editActionName = "";
%>
<jsp:include page="../../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box portlet-btcl light">
	<div class="portlet-body">
		<form action="<%= request.getContextPath() %>/TerminateVpnLink.do" method="POST">
			<div class="table-responsive">
				<table id="example1" class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>Link Name</th>
							<th>Client</th>
							<th>Bandwidth</th>
							<th>Activation Date</th>
							<th>Status</th>
							<%if( showTerminationOption ){ %>
								<th width="5%"> <button class="btn btn-sm btn-primary"> Terminate </button></th>
							<%} %>
						</tr>
					</thead>
					<tbody>
						<%
						CommonDAO commonDAO = new CommonDAO();
						ArrayList<VpnLinkDTO> data = (ArrayList<VpnLinkDTO>) session.getAttribute(SessionConstants.VIEW_VPN_LINK);

							if (data != null) {
								int size = data.size();
								for (int i = 0; i < size; i++) {
									VpnLinkDTO row = (VpnLinkDTO) data.get(i);
									long connectionID = row.getVpnConnectionID();
									long clientID = row.getClientID();
									String clientName = AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName();
									String activationDateString="N/A";
									Long activationDateInLong = row.getActivationDate();
									if(activationDateInLong != null && activationDateInLong>0){
										activationDateString=TimeConverter.getTimeWithTimeStringFromLong(activationDateInLong);
									}
									request.setAttribute("status", commonDAO.getActivationStatusName(row.getCurrentStatus()));
									editActionName = (vpnLinkService.isLinkVerified(row.getEntityID())) ? "../../VPN/Link/Update/Link.do?id=" + row.getEntityID()
											: "../../VpnLinkAction.do?entityID=" + row.getEntityID() + "&entityTypeID="+EntityTypeConstant.VPN_LINK+"&getMode=edit";
						%>
						<tr <%=(!row.isNearEndReused()) ? "style='background-color:#daf5da ;'" : "" %>>
							<td ><a href="<%=context%>VpnLinkAction.do?entityID=<%=row.getID()%>&entityTypeID=<%=EntityTypeConstant.VPN_LINK%>"><%=row.getLinkName()%></a></td>
<%-- 							<td ><a href="<%=context%>VpnLinkAction.do?entityID=<%=row.getID()%>&entityTypeID=<%=EntityTypeConstant.VPN_LINK%>"><%=row.getLinkName()%></a></td> --%>
							<td><a href="<%=context%>GetClientForView.do?entityID=<%=clientID%>&entityTypeID=<%=EntityTypeConstant.VPN_LINK%>&moduleID=<%=ModuleConstants.Module_ID_VPN%>"><%=clientName %></a></td>
							<td><%=row.getVpnBandwidth() %> <%=EntityTypeConstant.linkBandwidthTypeMap.get(row.getVpnBandwidthType())%></td>	
							<td><%=activationDateString %></td>
							<td>${status}</td>
							
							<%if(showTerminationOption) {%>
								<td align="center"><input type="checkbox" name="terminateLinkID" value="<%=row.getEntityID() %>" /></td>	
							<%} %>
						</tr>
						<%		}%>
					</tbody>
					<%		}%>
				</table>
			</div>
		</form>
	</div>
</div>