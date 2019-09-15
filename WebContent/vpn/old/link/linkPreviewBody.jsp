<%@page import="distance.DistanceService"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="common.CategoryConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="language.VpnLanguageConstants"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="file.FileDTO"%>
<%@page import="util.SOP"%>
<%@page import="file.FileDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%

VpnLinkService vpnLinkService = ServiceDAOFactory.getService(VpnLinkService.class);

int id = Integer.parseInt(request.getParameter("entityID"));
String context = "" + request.getContextPath() + "/";
LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

String back  = "../../VpnLinkAction.do?entityID=" + id+"&entityTypeID="+EntityTypeConstant.VPN_LINK;

VpnNearEndDTO nearEndDTO=(VpnNearEndDTO)request.getAttribute("vpnNE");
VpnEndPointDetailsDTO nearEndDetailsDTO=LinkUtils.getEndPointDTODetails(nearEndDTO);

VpnFarEndDTO farEndDTO=(VpnFarEndDTO)request.getAttribute("vpnFE");
VpnEndPointDetailsDTO farEndDetailsDTO=LinkUtils.getEndPointDTODetails(farEndDTO);

request.setAttribute("nearEnd", nearEndDetailsDTO);
request.setAttribute("farEnd", farEndDetailsDTO);

VpnLinkDTO vpnLinkDTO= (VpnLinkDTO)request.getAttribute("vpnLink");

DistanceService distanceService = ServiceDAOFactory.getService(DistanceService.class);

String actionName = vpnLinkService.isLinkVerified(vpnLinkDTO.getID()) ? "../../VPN/Link/Update/Link.do?id="+vpnLinkDTO.getID()
		: "../../VpnLinkAction.do?entityID=" + id+"&entityTypeID="+EntityTypeConstant.VPN_LINK+"&getMode=edit";
%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Link View
		</div>
		<div class="actions">
			<a  href="<%=back%>" class="btn btn-back-btcl" style="padding-left: 15px; padding-right: 15px;"> Back </a>
			<input type="button" id="printBtn" class="btn btn-default " style="padding-left: 15px; padding-right: 15px;" value="Print" /> 
			<%if (vpnLinkService.processLinkUpdatePermission(id, loginDTO, request)){%>
				<a href="<%=actionName%>&edit" class="btn btn-edit-btcl" style="padding-left: 15px; padding-right: 15px;"> Edit </a>
			<%}%>			
		</div>
	</div>
	<div class="portlet-body" id="printContent">
		<div class="row">
			<div class="col-md-12">
				<div class="table-responsive">
					<table class="table table-bordered  table-striped">
						<thead>
							<tr>
								<th colspan="3" class="text-center">
									<h1> Link Details </h1>
								</th>
							</tr>
						</thead>
						<tbody>
							
							<tr>
								<th scope="row">Client Name</th>
								<td colspan="2">${clientName}</td>
							</tr>
							<tr>
								<th scope="row">Link Name</th>
								<td colspan="2">${vpnLink.linkName}</td>
							</tr>
							<%if(loginDTO.getIsAdmin()){ %>
								<tr>
									<th scope="row">Service Purpose</th>
									<td colspan="2"><%if(vpnLinkDTO.getServicePurpose()==1){ %> YES <%}else{ %>NO <%} %> </td>
								</tr>
							<%} %>
							<tr>
								<th scope="row">Bandwidth</th>
								<td colspan="2">${vpnLink.vpnBandwidth} &nbsp; <%=EntityTypeConstant.linkBandwidthTypeMap.get(vpnLinkDTO.getVpnBandwidthType()) %>&nbsp; </td>
							</tr>
							<tr>
								<th scope="row">Pop to Pop Distance</th>
								<td colspan="2">
									<%= vpnLinkDTO.getPopToPopDistance() <= 0 ? 
											"Suggested: " + distanceService.getDistanceBetweenTwoLocation(nearEndDTO.getUnionID(), farEndDTO.getUnionID())
											: vpnLinkDTO.getPopToPopDistance()
									%> KM
								</td>
							</tr>
							<tr>
								<th scope="row">Connection Type</th>
								<td colspan="2"><%=EndPointConstants.connectionType.get(vpnLinkDTO.getConnectionType()) %></td>
							</tr> 
							<tr>
								<th scope="row">Layer Type</th>
								<td colspan="2">
									<%=EndPointConstants.layerTypeMap.get(vpnLinkDTO.getLayerType()) %>
									<%if(vpnLinkDTO.getLayerType()==EndPointConstants.LAYER_TYPE_2 && StringUtils.isNotBlank(vpnLinkDTO.getLanIdNumbers())){ %>
										&nbsp;<%=vpnLinkDTO.getLanIdNumbers() %>
									<%} %>
								</td>
							</tr> 
							<tr>
								<th scope="row">Description</th>
								<td colspan="2">${vpnLink.linkDescription}</td>
							</tr> 
							<!-- tr>
								<th scope="row">Distance (km)</th>
								<td colspan="2">${vpnLink.vpnLoopDistance}</td>
							</tr-->
						</tbody>
					</table>
				</div>	
				<div class="row">
					<div class="col-md-6">
						<div class="table-responsive">
							<table class="table table-bordered table-striped">
								<thead>
									<tr>
										<th colspan="2" class="text-center">
											<h4> <%=VpnLanguageConstants.LOCAL_END%> Details </h4>
										</th>
									</tr>
								</thead>
						
								<tbody>
									<tr>
										<th scope="row"><%=VpnLanguageConstants.END_NAME %></th>
										<td class="text-muted">${nearEnd.vepName}</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_DISTRICT).getName() %></th>
										<td class="text-muted">${nearEnd.districtName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_UPAZILA).getName() %></th>
										<td class="text-muted">${nearEnd.upazilaName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_UNION).getName() %></th>
										<td class="text-muted">${nearEnd.unionName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_POP).getName() %></th>
										<td class="text-muted">${nearEnd.popName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_ROUTER).getName() %></th>
										<td class="text-muted">${nearEnd.routerName }</td>
									</tr>
<%-- 									
									<tr> 
										<th scope="row">Mandatory <%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_VLAN).getName() %></th>
										<td class="text-muted">${nearEnd.mandatoryVlanName }</td>
									</tr> 
									<tr> 
										<th scope="row">Additional <%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_VLAN).getName() %></th>
										<td class="text-muted">${nearEnd.additionalVlanNames }</td>
									</tr> 
								
 --%>									<tr>
										<th scope="row">Port Type</th>
										<td class="text-muted">${nearEnd.portCateogryTypeName }</td>
									</tr>
									<%if(loginDTO.getIsAdmin()){ %>
									<tr>
										<th scope="row">Port</th>
										<td class="text-muted">${nearEnd.portName}</td>
									</tr>
									<%} %>
									
									<tr>
										<th scope="row">Address</th>
										<td class="text-muted">${nearEnd.address }</td>
									</tr>
									<tr>
										<th scope="row">Local Loop Distance</th>
										<td class="text-muted"> ${nearEnd.localLoopDistance }</td>
									</tr>
									
									<tr>
										<th scope="row">OFC Type</th>
										<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(nearEndDetailsDTO.getCoreType()) %></td>
									</tr> 
									<tr>
										<th scope="row"><%=VpnLanguageConstants.LOOP_PROVIDER %></th>
										<td class="text-muted"><%=EndPointConstants.providerOfOFC.get(nearEndDTO.getOfcProviderTypeID()) %></td>
									</tr>
									<tr>
										<th scope="row">Terminal Device Provider</th>
										<td class="text-muted"><%=EndPointConstants.terminalDeviceProvider.get(nearEndDTO.getTerminalDeviceProvider()) %></td>
									</tr>
									
								</tbody>
							</table>
						</div>
					</div>
					<div class="col-md-6">
						<div class="table-responsive">
							<table class="table table-bordered table-striped">
								<thead>
									<tr>
										<th colspan="2" class="text-center">
											<h4> <%=VpnLanguageConstants.REMOTE_END%> Details </h4>
										</th>
									</tr>
								</thead>
						
								<tbody>
									<tr>
										<th scope="row"><%=VpnLanguageConstants.END_NAME %></th>
										<td class="text-muted">${farEnd.vepName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></th>
										<td class="text-muted">${farEnd.districtName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></th>
										<td class="text-muted">${farEnd.upazilaName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></th>
										<td class="text-muted">${farEnd.unionName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></th>
										<td class="text-muted">${farEnd.popName }</td>
									</tr>
									<tr>
										<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_ROUTER).getName() %></th>
										<td class="text-muted">${farEnd.routerName }</td>
									</tr>
									<%-- 
									<tr>
										<th scope="row">Mandatory <%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_VLAN).getName() %></th>
										<td class="text-muted">${farEnd.mandatoryVlanName }</td>
									</tr>
									<tr>
										<th scope="row">Additional <%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_VLAN).getName() %></th>
										<td class="text-muted">${farEnd.additionalVlanNames }</td>
									</tr>
									 --%>
									<tr>
										<th scope="row">Port Type</th>
										<td class="text-muted">${farEnd.portCateogryTypeName }</td>
									</tr>
									<%if(loginDTO.getIsAdmin()){ %>
									<tr>
										<th scope="row">Port</th>
										<td class="text-muted">${farEnd.portName }</td>
									</tr>
									<%} %>
									<tr>
										<th scope="row">Address</th>
										<td class="text-muted">${farEnd.address }</td>
									</tr>
									<tr>
										<th scope="row">Local Loop Distance</th>
										<td class="text-muted"> ${farEnd.localLoopDistance }</td>
									</tr>
									<tr>
										<th scope="row">OFC Type</th>
										<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(farEndDetailsDTO.getCoreType()) %></td>
									</tr> 
									<tr>
										<th scope="row"><%=VpnLanguageConstants.LOOP_PROVIDER %></th>
										<td class="text-muted"> <%=EndPointConstants.providerOfOFC.get(farEndDTO.getOfcProviderTypeID()) %></td>
									</tr>
									<tr>
										<th scope="row">Terminal Device Provider</th>
										<td class="text-muted"> <%=EndPointConstants.terminalDeviceProvider.get(farEndDTO.getTerminalDeviceProvider()) %></td>
									</tr>
									
								</tbody>
							</table>
						</div>
					</div>
				</div>
				
			</div>
		</div>
		<div class="row">
			<jsp:include page="../../../common/fileListHelper.jsp" flush="true">
				<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.VPN_LINK %>" />
				<jsp:param name="entityID" value="<%=id %>" />
			</jsp:include>
		</div>
	</div>
</div>
<script src="${context}assets/scripts/printThis.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
	    $("#printBtn").click(function(){
			$("#printContent").printThis();
	    })
	})
</script>
