<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="inventory.InventoryService"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="vpn.link.VpnLinkShiftDTO"%>
<%@page import="vpn.link.request.VpnLinkShiftService"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="util.ServiceDAOFactory"%>
<%
	//Request Validation
	Long vpnLinkShiftRequestID = Long.parseLong(request.getParameter("id"));	
	if(vpnLinkShiftRequestID==null){
		response.sendRedirect(request.getContextPath());
	}
	
	//Services Initialization
	VpnLinkShiftService vpnLinkShiftService = ServiceDAOFactory.getService(VpnLinkShiftService.class);
	VpnLinkService vpnLinkService = ServiceDAOFactory.getService(VpnLinkService.class);
	InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
	
	//Shift Details Object for presentation
	VpnLinkShiftDTO vpnLinkShiftDTO = vpnLinkShiftService.getSpecificRequestDTOByPrimaryKey(vpnLinkShiftRequestID);
	VpnLinkDTO vpnLinkDTO = vpnLinkService.getVpnLinkByVpnLinkID(vpnLinkShiftDTO.getVpnLinkID());
	VpnNearEndDTO vpnNearEndDTO = vpnLinkService.getNearEndByNearEndID(vpnLinkDTO.getNearEndID());
	VpnEndPointDetailsDTO nearEndDetailsDTO = LinkUtils.getEndPointDTODetails(vpnNearEndDTO);
	VpnFarEndDTO vpnFarEndDTO = vpnLinkService.getFarEndByFarEndID(vpnLinkDTO.getFarEndID());
	VpnEndPointDetailsDTO farEndDetailsDTO = LinkUtils.getEndPointDTODetails(vpnFarEndDTO);
%>

<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Link Shift Request
		</div>
		<div class="actions">
			<input type="button" id="printBtn" class="btn btn-default " style="padding-left: 15px; padding-right: 15px;" value="Print" />   
		</div>
	</div>
	<div class="portlet-body" id="printContent">
		<div class="row">
			<div class="col-md-12">
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th class="text-center" colspan="4"><h3>Link Shift</h3></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th>Client Name</th>
								<td class="text-muted" colspan="2"><%= AllClientRepository.getInstance().getClientByClientID(vpnLinkDTO.getClientID()).getName()%></td>
							</tr>
							<tr>
								<th>Link Name</th>
								<td class="text-muted" colspan="2"><%=vpnLinkDTO.getName()%></td>
							</tr>
						</tbody>
					</table>
				</div>
				<% if(vpnLinkShiftDTO.getNeShiftMode() != 1) {%>
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th class="text-center" colspan="4"><h3>Near End</h3></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th></th>
								<th>Current  </th>
								<th>New </th>
							</tr>
							
							<tr>
								<th>Office Name</th>
								<td><%=nearEndDetailsDTO.getVepName()%></td>
								<td><%=vpnLinkShiftDTO.getNeName()%></td>
							</tr>
							<tr>
								<th>District</th>
								<td><%=nearEndDetailsDTO.getDistrictName()%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getNeDistrictID()).getName()%></td>
							</tr>
							<tr>
								<th>Upazila</th>
								<td><%=nearEndDetailsDTO.getUpazilaName()%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getNeUpazilaID()).getName()%></td>
							</tr>
							<tr>
								<th>Union</th>
								<td><%=nearEndDetailsDTO.getUnionName()%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getNeUnionID()).getName()%></td>
							</tr>
							<tr>
								<th>Pop</th>
								<td><%=nearEndDetailsDTO.getPopName()%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getNePopID()).getName()%></td>
							</tr>
							<tr>
								<th>Port Type</th>
								<td><%=nearEndDetailsDTO.getPortCategoryType()%></td>
								<td><%=vpnLinkShiftDTO.getNePortType()%></td>
							</tr>
							<tr>
								<th>Office Address</th>
								<td><%=nearEndDetailsDTO.getAddress()%></td>
								<td><%=vpnLinkShiftDTO.getNeAddress()%></td>
							</tr>
							<tr>
								<th>OFC Type</th>
								<td><%=EndPointConstants.coreTypeMap.get(nearEndDetailsDTO.getCoreType())%></td>
								<td><%=EndPointConstants.coreTypeMap.get(vpnLinkShiftDTO.getNeCoreType())%></td>
							</tr>
							<tr>
								<th>Local Loop Connectivity</th>
								<td><%=EndPointConstants.providerOfOFC.get(nearEndDetailsDTO.getOfcProviderID())%></td>
								<td><%=EndPointConstants.providerOfOFC.get(vpnLinkShiftDTO.getNeOfcProvider())%></td>
							</tr>
							<tr>
								<th>Terminal Device Provider</th>
								<td><%=EndPointConstants.terminalDeviceProvider.get(nearEndDetailsDTO.getTerminalDeviceProvider())%></td>
								<td><%=EndPointConstants.terminalDeviceProvider.get(vpnLinkShiftDTO.getNeTerminalDeviceProvider())%></td>
							</tr>
							
							<% if (!String.valueOf(vpnLinkShiftDTO.getNeRouterID()).toLowerCase().equals("null")) { %>
							<tr>
								<th>Router</th>
								<td><%=nearEndDetailsDTO.getRouterName() + " (" + nearEndDetailsDTO.getRouterID() + ")"%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getNeRouterID()).getName() + " (" + vpnLinkShiftDTO.getNeRouterID() + ")"%></td>
							</tr>
							<tr>
								<th>Port</th>
								<td><%=nearEndDetailsDTO.getPortName() + " (" + nearEndDetailsDTO.getPortID() + ")"%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getNePortID()).getName() + " (" + vpnLinkShiftDTO.getNePortID() + ")"%></td>
							</tr>
							<tr>
								<th>Mandatory VLAN</th>
								<td><%=nearEndDetailsDTO.getMandatoryVlanName() + " (" + nearEndDetailsDTO.getMandatoryVlanID() + ")"%></td>
								<td><%=inventoryService.getInventoryItemByItemID(Long.parseLong(vpnLinkShiftDTO.getNeMandatoryVLanID())).getName() + " (" + vpnLinkShiftDTO.getNeMandatoryVLanID() + ")"%></td>
							</tr>
							
							
							<% 
							Integer existingNearEndAdditionalVLANCount = nearEndDetailsDTO.getAdditionalVlanIDs() != null ? nearEndDetailsDTO.getAdditionalVlanIDs().split(",").length : 0;
							Integer shiftedNearEndAdditionalVLANCount = vpnLinkShiftDTO.getNeAdditionalVLanID() != null ? vpnLinkShiftDTO.getNeAdditionalVLanID().split(",").length : 0;
							Integer rowSpan = existingNearEndAdditionalVLANCount >= shiftedNearEndAdditionalVLANCount ? existingNearEndAdditionalVLANCount : shiftedNearEndAdditionalVLANCount;
							%>
							
							<% for(int i=0;i<rowSpan;i++){ %>
								<% if(i==0){%>
								<tr>
									<th rowspan="<%=rowSpan%>">Additional VLAN</th>
									<td><%=nearEndDetailsDTO.getAdditionalVlanIDs() != null ? nearEndDetailsDTO.getAdditionalVlanIDs().split(",")[0] : ""%></td>
									<td><%=vpnLinkShiftDTO.getNeAdditionalVLanID()  != null ? vpnLinkShiftDTO.getNeAdditionalVLanID().split(",")[0] : ""%></td>
								</tr>
								<%} else {%>
								<tr>
									<td><%=nearEndDetailsDTO.getAdditionalVlanIDs() != null && nearEndDetailsDTO.getAdditionalVlanIDs().split(",").length >= i ? nearEndDetailsDTO.getAdditionalVlanIDs().split(",")[i] : ""%></td>
									<td><%=vpnLinkShiftDTO.getNeAdditionalVLanID() != null && vpnLinkShiftDTO.getNeAdditionalVLanID().split(",").length >= i ? vpnLinkShiftDTO.getNeAdditionalVLanID().split(",")[i] : ""%></td>
								</tr>
								<% } %>
							<% } %>
							<% } %>
						</tbody>
					</table>
				</div>
				<% } %>
				
				<% if(vpnLinkShiftDTO.getFeShiftMode() != 1) { %>
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th class="text-center" colspan="4"><h3>Far End</h3></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th></th>
								<th>Current  </th>
								<th>New </th>
							</tr>
							
							<tr>
								<th>Office Name</th>
								<td><%=farEndDetailsDTO.getVepName()%></td>
								<td><%=vpnLinkShiftDTO.getFeName()%></td>
							</tr>
							<tr>
								<th>District</th>
								<td><%=farEndDetailsDTO.getDistrictName()%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getFeDistrictID()).getName()%></td>
							</tr>
							<tr>
								<th>Upazila</th>
								<td><%=farEndDetailsDTO.getUpazilaName()%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getFeUpazilaID()).getName()%></td>
							</tr>
							<tr>
								<th>Union</th>
								<td><%=farEndDetailsDTO.getUnionName()%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getFeUnionID()).getName()%></td>
							</tr>
							<tr>
								<th>Pop</th>
								<td><%=farEndDetailsDTO.getPopName()%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getFePopID()).getName()%></td>
							</tr>
							<tr>
								<th>Port Type</th>
								<td><%=farEndDetailsDTO.getPortCategoryType()%></td>
								<td><%=vpnLinkShiftDTO.getFePortType()%></td>
							</tr>
							<tr>
								<th>Office Address</th>
								<td><%=farEndDetailsDTO.getAddress()%></td>
								<td><%=vpnLinkShiftDTO.getFeAddress()%></td>
							</tr>
							<tr>
								<th>OFC Type</th>
								<td><%=EndPointConstants.coreTypeMap.get(farEndDetailsDTO.getCoreType())%></td>
								<td><%=EndPointConstants.coreTypeMap.get(vpnLinkShiftDTO.getFeCoreType())%></td>
							</tr>
							<tr>
								<th>Local Loop Connectivity</th>
								<td><%=EndPointConstants.providerOfOFC.get(farEndDetailsDTO.getOfcProviderID())%></td>
								<td><%=EndPointConstants.providerOfOFC.get(vpnLinkShiftDTO.getFeOfcProvider())%></td>
							</tr>
							<tr>
								<th>Terminal Device Provider</th>
								<td><%=EndPointConstants.terminalDeviceProvider.get(farEndDetailsDTO.getTerminalDeviceProvider())%></td>
								<td><%=EndPointConstants.terminalDeviceProvider.get(vpnLinkShiftDTO.getFeTerminalDeviceProvider())%></td>
							</tr>
							
							<% if (!String.valueOf(vpnLinkShiftDTO.getFeRouterID()).toLowerCase().equals("null")) { %>
							<tr>
								<th>Router</th>
								<td><%=farEndDetailsDTO.getRouterName() + " (" + farEndDetailsDTO.getRouterID() + ")"%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getFeRouterID()).getName() + " (" + vpnLinkShiftDTO.getFeRouterID() + ")"%></td>
							</tr>
							<tr>
								<th>Port</th>
								<td><%=farEndDetailsDTO.getPortName() + " (" + farEndDetailsDTO.getPortID() + ")"%></td>
								<td><%=inventoryService.getInventoryItemByItemID(vpnLinkShiftDTO.getFePortID()).getName() + " (" + vpnLinkShiftDTO.getFePortID() + ")"%></td>
							</tr>
							<tr>
								<th>Mandatory VLAN</th>
								<td><%=farEndDetailsDTO.getMandatoryVlanName() + " (" + farEndDetailsDTO.getMandatoryVlanID() + ")"%></td>
								<td><%=inventoryService.getInventoryItemByItemID(Long.parseLong(vpnLinkShiftDTO.getFeMandatoryVLanID())).getName() + " (" + vpnLinkShiftDTO.getFeMandatoryVLanID() + ")"%></td>
							</tr>
							
							
							<% 
							Integer existingFarEndAdditionalVLANCount = farEndDetailsDTO.getAdditionalVlanIDs() != null ? farEndDetailsDTO.getAdditionalVlanIDs().split(",").length : 0;
							Integer shiftedFarEndAdditionalVLANCount = vpnLinkShiftDTO.getFeAdditionalVLanID() != null ? vpnLinkShiftDTO.getFeAdditionalVLanID().split(",").length : 0;
							Integer rowSpan = existingFarEndAdditionalVLANCount >= shiftedFarEndAdditionalVLANCount ? existingFarEndAdditionalVLANCount : shiftedFarEndAdditionalVLANCount;
							%>
							
							<% for(int i=0;i<rowSpan;i++){ %>
								<% if(i==0){%>
								<tr>
									<th rowspan="<%=rowSpan%>">Additional VLAN</th>
									<td><%=farEndDetailsDTO.getAdditionalVlanIDs() != null ? farEndDetailsDTO.getAdditionalVlanIDs().split(",")[0] : ""%></td>
									<td><%=vpnLinkShiftDTO.getFeAdditionalVLanID()  != null ? vpnLinkShiftDTO.getFeAdditionalVLanID().split(",")[0] : ""%></td>
								</tr>
								<%} else {%>
								<tr>
									<td><%=farEndDetailsDTO.getAdditionalVlanIDs() != null && farEndDetailsDTO.getAdditionalVlanIDs().split(",").length >= i ? farEndDetailsDTO.getAdditionalVlanIDs().split(",")[i] : ""%></td>
									<td><%=vpnLinkShiftDTO.getFeAdditionalVLanID() != null && vpnLinkShiftDTO.getFeAdditionalVLanID().split(",").length >= i ? vpnLinkShiftDTO.getFeAdditionalVLanID().split(",")[i] : ""%></td>
								</tr>
								<% } %>
							<% } %>
							<% } %>
						</tbody>
					</table>
				</div>
				<% } %>
			</div>
		</div>
	</div>
</div>

