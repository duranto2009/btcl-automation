<%@page import="inventory.InventoryConstants"%>
<%@page import="inventory.InventoryService"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="vpn.link.request.VpnLinkBandwidthChangeRequestService"%>
<%@page import="vpn.link.VpnBandWidthChangeRequestDTO"%>
<%
	Long vpnLinkBandwidthChangeRequestID = Long.parseLong(request.getParameter("id"));	
	if(vpnLinkBandwidthChangeRequestID==null){
		response.sendRedirect(request.getContextPath());
	}
	VpnLinkBandwidthChangeRequestService vpnLinkBandwidthChangeRequestService = ServiceDAOFactory.getService(VpnLinkBandwidthChangeRequestService.class);
	VpnBandWidthChangeRequestDTO vpnBandWidthChangeRequestDTO = vpnLinkBandwidthChangeRequestService.getRequestDTOByPrimaryKey(vpnLinkBandwidthChangeRequestID);
	VpnLinkDTO vpnLinkDTO = new VpnLinkService().getVpnLinkByVpnLinkID(vpnBandWidthChangeRequestDTO.getLinkID());
%>

<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Link Upgrade/Degrade Request
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
								<th class="text-center" colspan="4"><h3>Link Upgrade/Degrade </h3></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th scope="row">Client Name</th>
								<td class="text-muted" colspan="2"><%= AllClientRepository.getInstance().getClientByClientID(vpnLinkDTO.getClientID()).getName()%></td>
							</tr>
							<tr>
								<th scope="row">Link Name</th>
								<td class="text-muted" colspan="2"><%=vpnLinkDTO.getName()%></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<tbody>
							<tr>
								<th scope="row"></th>
								<th scope="row">Current  </th>
								<th scope="row">New </th>
							</tr>
							<tr>
								<th scope="row">Bandwidth</th>
								<td scope="row"><%=vpnLinkDTO.getVpnBandwidth()%></td>
								<td scope="row"><%= vpnBandWidthChangeRequestDTO.getNewBandwidth()%> </td>
							</tr>
							<tr>
								<th scope="row">Bandwidth Type</th>
								<td scope="row"><%=vpnLinkDTO.getVpnBandwidthType()==1?"Mb":"Gb"%></td>
								<td scope="row"><%= vpnBandWidthChangeRequestDTO.getNewBandwidthType()==1?"Mb":"Gb"%> </td>
							</tr>
							<%-- <%
							VpnLinkService vpnLinkService = new VpnLinkService();
							VpnNearEndDTO vpnNearEndDTO = vpnLinkService.getNearEndByNearEndID(vpnLinkDTO.getNearEndID());
							VpnFarEndDTO vpnFarEndDTO = vpnLinkService.getFarEndByFarEndID(vpnLinkDTO.getFarEndID());
							InventoryService inventoryService = new InventoryService();
							String oldNearEndPort =  inventoryService.getInventoryItemByItemID(vpnNearEndDTO.getPortID()).getName();
							String oldNearEndPortType =  vpnNearEndDTO.getPortCategoryType();
							String oldFarEndPort = inventoryService.getInventoryItemByItemID(vpnFarEndDTO.getPortID()).getName();
							String oldFarEndPortType =  vpnFarEndDTO.getPortCategoryType();
							%>
							<tr>
								<th scope="row">Near End Port</th>
								<td scope="row"><%=oldNearEndPort%></td>
								<td scope="row"><%=vpnBandWidthChangeRequestDTO.getNewNearPortID() != null ? vpnBandWidthChangeRequestDTO.getNewNearPortID() : "n/a"%></td>
							</tr>
							<tr>
								<th scope="row">Near End Port Type</th>
								<td scope="row"><%=oldNearEndPortType%></td>
								<td scope="row"><%=vpnBandWidthChangeRequestDTO.getNewNearPortType() != null ? vpnBandWidthChangeRequestDTO.getNewNearPortType() : "n/a"%></td>
							</tr>
							
							<tr>
								<th scope="row">Far End Port</th>
								<td scope="row"><%=oldFarEndPort%></td>
								<td scope="row"><%=vpnBandWidthChangeRequestDTO.getNewFarPortID() != null ? vpnBandWidthChangeRequestDTO.getNewFarPortID() : "n/a"%></td>
							</tr>
							<tr>
								<th scope="row">Far End Port Type</th>
								<td scope="row"><%=oldFarEndPortType%></td>
								<td scope="row"><%=vpnBandWidthChangeRequestDTO.getNewFarPortType() != null ? vpnBandWidthChangeRequestDTO.getNewFarPortType() : "n/a"%></td>
							</tr> --%> 
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

