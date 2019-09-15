<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="distance.DistanceService"%>
<%@page import="util.TimeConverter"%>
<%@page import="language.VpnLanguageConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="permission.StateActionDTO"%>
<%@page import="vpn.VpnConnectionDTO"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="permission.ActionStateDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="login.LoginDTO"%>
<%@page import="request.RequestStatus"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="vpn.client.ClientDetailsDTO,vpn.link.VpnLinkService"%>
<%@page import="java.util.*,java.text.*"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="date" class="java.util.Date" />
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<script src="${context}/scripts/bootstrap-datepicker.js"></script>
<%
	Logger logger = Logger.getLogger("link_view_jsp");
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	String context = "" + request.getContextPath() + "/";
	Long id = (Long) request.getAttribute("entityID");
	if (id == null || id == 0) {
		id = Long.parseLong(request.getParameter("entityID"));
	}
	VpnLinkDTO vpnLinkDTO = (VpnLinkDTO) request.getAttribute("vpnLink");
	
	ArrayList<StateActionDTO> stateActionDTOs = (ArrayList<StateActionDTO>) request.getAttribute("stateActions");
	logger.debug("stateActions " + stateActionDTOs);
	//ArrayList<ActionStateDTO> actionStateDTOs = (ArrayList<ActionStateDTO>) request.getAttribute("actions");

	String action = "VpnLinkAction";
	String actionName = "../../VpnLinkAction.do?entityID=" + id+ "&entityTypeID="+EntityTypeConstant.VPN_LINK;
	String detailsActionName = actionName + "&getMode=details";
	String editActionName = actionName + "&getMode=edit";
	request.setAttribute("entityTypeID", EntityTypeConstant.VPN_LINK);
	request.setAttribute("entityID", id);
	VpnConnectionDTO vpnConnection = (VpnConnectionDTO) request.getAttribute("vpnConnection");
	
	if(vpnLinkDTO==null){
		vpnLinkDTO =new VpnLinkService().getVpnLinkByVpnLinkID(id);		
	}
	request.setAttribute("rootEntityID", vpnLinkDTO.getID());	
	request.setAttribute("rootEntityTypeID", EntityTypeConstant.VPN_LINK);
	
	ClientDTO clientVPNDTO = AllClientRepository.getInstance().getClientByClientID(vpnLinkDTO.getClientID());
	// AllClientRepository.getInstance().getVpnClientByClientID(vpnLinkDTO.getClientID(), ModuleConstants.Module_ID_VPN);
	String tabActivated = "1";
	if (request.getParameter("currentTab") != null) {
		tabActivated = request.getParameter("currentTab");
	}
	//String currentStatusStr= new StatusHistoryService().getActivationStatus(EntityTypeConstant.VPN_LINK, id);
	CommonDAO commonDAO = new CommonDAO();
	String currentStatusStr = commonDAO.getActivationStatusName(vpnLinkDTO.getCurrentStatus());
	String latestStatusStr = StateRepository.getInstance().getStateDTOByStateID(vpnLinkDTO.getLatestStatus()).getName();
	
	DistanceService distanceService = ServiceDAOFactory.getService(DistanceService.class);
	VpnLinkService vpnLinkService = ServiceDAOFactory.getService(VpnLinkService.class);
	VpnNearEndDTO vpnNearEndDTO = vpnLinkService.getNearEndByNearEndID(vpnLinkDTO.getNearEndID());
	VpnFarEndDTO vpnFarEndDTO = vpnLinkService.getFarEndByFarEndID(vpnLinkDTO.getFarEndID());
%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link" aria-hidden="true"></i> VPN Link View
		</div>
		<ul class="nav nav-tabs ">
			<li class="<%if (tabActivated.equals("1")) {%> <%="active"%><%}%>">
				<a href="../../VpnLinkAction.do?entityID=<%=id%>&entityTypeID=<%=EntityTypeConstant.VPN_LINK%>&currentTab=1" aria-expanded="true"> Summary </a>
			</li>
			<li class="<%if (tabActivated.equals("2")) {%> <%="active"%><%}%>">
				<a
				href="../../VpnLinkAction.do?entityID=<%=id%>&entityTypeID=<%=EntityTypeConstant.VPN_LINK%>&currentTab=2"
				aria-expanded="true"> Comments </a>
			</li>
			<li class="<%if (tabActivated.equals("3")) {%> <%="active"%><%}%>">
				<a
				href="../../VpnLinkAction.do?entityID=<%=id%>&entityTypeID=<%=EntityTypeConstant.VPN_LINK%>&currentTab=3"
				aria-expanded="true"> History </a>
			</li>
		</ul>
	</div>
	<div class="portlet-body">
		<div class="tab-content">
			<%
				if (tabActivated.equals("1")) {
			%>
			<div id="tab_5_1" class="tab-pane <%if (tabActivated.equals("1")) {%> <%="active"%><%}%>">
				<h3 class="">Information</h3>
				<!-- /.box-header -->
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<tbody>
							<tr>
								<th scope="row">Client Name</th>
								<td colspan="2">
									<a href="../../GetClientForView.do?entityID=<%=clientVPNDTO.getClientID()%>&entityTypeID=<%=EntityTypeConstant.VPN_CLIENT%>&moduleID=<%=ModuleConstants.Module_ID_VPN%>">
										<%=clientVPNDTO.getName()%>
									</a>
								</td>
							</tr>
							<tr>
								<th scope="row">Link Name</th>
								<td colspan="2">
									<a href="<%=detailsActionName%>">
										${vpnLink.linkName}
									</a>
								</td>
							</tr>
							<tr>
								<th scope="row">Description</th>
								<td colspan="2">${vpnLink.linkDescription}</td>
							</tr> 
							<tr>
								<th scope="row">Link Status</th>
								<td><%=currentStatusStr %></td>
							</tr>							
							<tr>
								<th scope="row">Bandwidth</th>
								<td colspan="2">${vpnLink.vpnBandwidth} <%=EntityTypeConstant.linkBandwidthTypeMap.get(vpnLinkDTO.getVpnBandwidthType()) %>&nbsp;</td>
							</tr>
							<tr>
								<th scope="row">Pop to Pop Distance</th>
								<td colspan="2">
									<%= (vpnLinkDTO.getPopToPopDistance() <= 0 && (vpnNearEndDTO.getUnionID() > 0 && vpnFarEndDTO.getUnionID() > 0)) ? 
											"Suggested: " + distanceService.getDistanceBetweenTwoLocation(vpnNearEndDTO.getUnionID(), vpnFarEndDTO.getUnionID())
											: vpnLinkDTO.getPopToPopDistance()
									%> KM
								</td>
							</tr>
							<tr>
								<th scope="row"><%=VpnLanguageConstants.LOCAL_END %> <%=VpnLanguageConstants.END_NAME %></th>
								<td class="text-muted"><a href='${context}vpn/link/endPointPreview.jsp?nearEndPointID=${nearEnd.ID}' target="_blank">${nearEnd.vepName}</a> </td>
							</tr>
							<tr>
								<th scope="row"><%=VpnLanguageConstants.REMOTE_END %> <%=VpnLanguageConstants.END_NAME %></th>
								<td class="text-muted"> <a href='${context}vpn/link/endPointPreview.jsp?farEndPointID=${farEnd.ID}' target="_blank" >${farEnd.vepName } </a></td>
							</tr>
							<tr>
								<th scope="row">Activation Date</th>
								<td><b><%if(vpnLinkDTO.getActivationDate()>0){%><%=TimeConverter.getTimeStringFromLong(vpnLinkDTO.getActivationDate()) %><%}else{ %>N/A<%} %></b></td>
							</tr>
						</tbody>
					</table>
				</div>
		
				
				<!-- Action List -->
				<%@include file="../../../common/actionListVPN.jsp"%>
				<!-- /Action List -->
			</div>
			<%
				}
			%>
			<%
				if (tabActivated.equals("2")) {
			%>
			<div id="tab_5_2" class="tab-pane <%if (tabActivated.equals("2")) {%> <%="active"%><%}%>">
				<%
					String url = "VpnLinkAction";
					String navigator = SessionConstants.NAV_COMMENT;
					String entityID = request.getParameter("entityID");
					String currentTab = request.getParameter("currentTab");
				%>
				<jsp:include page="../../../common/comment_column.jsp" flush="true">
					<jsp:param name="moduleID" value="<%=ModuleConstants.Module_ID_VPN%>" />
					<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.VPN_LINK%>" />
					<jsp:param name="entityID" value="<%=entityID%>" />
					<jsp:param name="currentTab" value="<%=currentTab%>" />
				</jsp:include>
			</div>

			<jsp:include page="../../../common/ajaxfileUploadTemplate.jsp" />

			<jsp:include page="../../../includes/navComment.jsp" flush="true">
				<jsp:param name="moduleID"
					value="<%=ModuleConstants.Module_ID_VPN%>" />
				<jsp:param name="entityTypeID"
					value="<%=EntityTypeConstant.VPN_LINK%>" />
				<jsp:param name="entityID" value="<%=entityID%>" />
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
				<jsp:param name="currentTab" value="<%=currentTab%>" />
			</jsp:include>


			<%
				}
			%>
			<%
				if (tabActivated.equals("3")) {
			%>
			<div id="tab_5_3"
				class="tab-pane <%if (tabActivated.equals("3")) {%> <%="active"%><%}%> ">
				<%
					String url = "VpnLinkAction";
						String navigator = SessionConstants.NAV_COMMON_REQUEST;
						String entityID = request.getParameter("entityID");
						String currentTab = request.getParameter("currentTab");
				%>
				<jsp:include page="../../../includes/navCommonRequestTab.jsp"
					flush="true">
					<jsp:param name="moduleID"
						value="<%=ModuleConstants.Module_ID_VPN%>" />
					<jsp:param name="entityTypeID"
						value="<%=EntityTypeConstant.VPN_LINK%>" />
					<jsp:param name="entityID" value="<%=entityID%>" />
					<jsp:param name="url" value="<%=url%>" />
					<jsp:param name="navigator" value="<%=navigator%>" />
					<jsp:param name="currentTab" value="<%=currentTab%>" />
				</jsp:include>

			</div>


			<%
				}
			%>
		</div>
	</div>
</div>

