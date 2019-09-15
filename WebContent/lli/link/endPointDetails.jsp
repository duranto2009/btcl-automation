<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="lli.link.LliFarEndDTO"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="lli.constants.EndPointConstants"%>
<%@page import="lli.link.LinkUtils"%>
<%@page import="lli.link.LliEndPointDetailsDTO"%>
<%@page contentType="text/html;charset=utf-8" %>
<%
	
	long nearEndID = Long.parseLong( request.getParameter( "nearEndID") );
	long farEndID = Long.parseLong( request.getParameter( "farEndID") );
	LliLinkService lliLinkService = new LliLinkService();
	
	LliFarEndDTO farEndDTO = lliLinkService.getFarEndByFarEndID( farEndID );
	
	LliEndPointDetailsDTO farEndDetailsDTO=LinkUtils.getEndPointDTODetails( farEndDTO );
%>

<table class='items billTable'>
	<thead>
		<tr class='title textcenter'>
			<td> End Point Details </td>
			<td>Far End</td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></td>
			<td><%=farEndDetailsDTO.getDistrictName() %></td>
		</tr>
		<tr>
			<td><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></td>
			<td><%=farEndDetailsDTO.getUpazilaName() %></td>
		</tr>
		<tr>
			<td><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></td>
			<td><%=farEndDetailsDTO.getUnionName() %></td>
		</tr>
		<tr>
			<td><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %> Name</td>
			<td><%=farEndDetailsDTO.getPopName() %></td>
		</tr>
		<tr>
			<td>Address</td>
			<td><%=farEndDetailsDTO.getAddress() %></td>
		</tr>
		<tr>
			<td>Local Loop Distance</td>
			<td class="text-muted"> ${nearEnd.localLoopDistance }</td>
			<td class="text-muted"> ${farEnd.localLoopDistance }</td>
		</tr>
		<tr>
			<td>Port Type</td>
			<td><%=farEndDetailsDTO.getPortCateogryTypeName() %></td>
		</tr>
		<tr>
			<td>Port ID</td>
			<td><%=farEndDetailsDTO.getPortID() %></td>
		</tr>
		<tr>
			<td>Distance (m)</td>
			<td><%=farEndDetailsDTO.getDistanceFromNearestPopInMeter() %></td>
		</tr>
		<tr>
			<td>Loop Provider</td>
			<td><%=EndPointConstants.providerOfOFC.get( farEndDetailsDTO.getOfcProviderTypeID() ) %></td>
		</tr>
	</tbody>
</table>