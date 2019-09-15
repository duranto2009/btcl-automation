<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
<%@page contentType="text/html;charset=utf-8" %>
<%
	
	long nearEndID = Long.parseLong( request.getParameter( "nearEndID") );
	long farEndID = Long.parseLong( request.getParameter( "farEndID") );
	VpnLinkService vpnLinkService = new VpnLinkService();
	
	VpnNearEndDTO nearEndDTO = vpnLinkService.getNearEndByNearEndID( nearEndID );
	VpnFarEndDTO farEndDTO = vpnLinkService.getFarEndByFarEndID( farEndID );
	
	VpnEndPointDetailsDTO nearEndDetailsDTO=LinkUtils.getEndPointDTODetails( nearEndDTO );
	VpnEndPointDetailsDTO farEndDetailsDTO=LinkUtils.getEndPointDTODetails( farEndDTO );
%>

<table class='items billTable'>
	<thead>
		<tr class='title textcenter'>
			<td> End Point Details </td>
			<td>Near End</td>
			<td>Far End</td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></td>
			<td><%=nearEndDetailsDTO.getDistrictName() %></td>
			<td><%=farEndDetailsDTO.getDistrictName() %></td>
		</tr>
		<tr>
			<td><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></td>
			<td><%=nearEndDetailsDTO.getUpazilaName() %></td>
			<td><%=farEndDetailsDTO.getUpazilaName() %></td>
		</tr>
		<tr>
			<td><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></td>
			<td><%=nearEndDetailsDTO.getUnionName() %></td>
			<td><%=farEndDetailsDTO.getUnionName() %></td>
		</tr>
		<tr>
			<td><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %> Name</td>
			<td><%=nearEndDetailsDTO.getPopName() %></td>
			<td><%=farEndDetailsDTO.getPopName() %></td>
		</tr>
		<tr>
			<td>Address</td>
			<td><%=nearEndDetailsDTO.getAddress() %></td>
			<td><%=farEndDetailsDTO.getAddress() %></td>
		</tr>
		<tr>
			<td>Local Loop Distance</td>
			<td class="text-muted"> ${nearEnd.localLoopDistance }</td>
			<td class="text-muted"> ${farEnd.localLoopDistance }</td>
		</tr>
		<tr>
			<td>Port Type</td>
			<td><%=nearEndDetailsDTO.getPortCateogryTypeName() %></td>
			<td><%=farEndDetailsDTO.getPortCateogryTypeName() %></td>
		</tr>
		<tr>
			<td>Port ID</td>
			<td> <%=nearEndDetailsDTO.getPortID() %> </td>
			<td><%=farEndDetailsDTO.getPortID() %></td>
		</tr>
		<tr>
			<td>Distance (m)</td>
			<td><%=nearEndDetailsDTO.getDistanceFromNearestPopInMeter() %></td>
			<td><%=farEndDetailsDTO.getDistanceFromNearestPopInMeter() %></td>
		</tr>
		<tr>
			<td>Loop Provider</td>
			<td><%=EndPointConstants.providerOfOFC.get( nearEndDetailsDTO.getOfcProviderTypeID()) %></td>
			<td><%=EndPointConstants.providerOfOFC.get( farEndDetailsDTO.getOfcProviderTypeID() ) %></td>
		</tr>
	</tbody>
</table>