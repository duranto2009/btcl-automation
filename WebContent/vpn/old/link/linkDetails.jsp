<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.SqlGenerator"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="common.CommonDAO"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnFRResponseExternalDTO"%>
<%@page import="vpn.link.api.APIUtil"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page contentType="text/html;charset=utf-8" %>
<%
	
	long id = Long.parseLong( request.getParameter( "id" ) );
	
	VpnLinkService vpnLinkService = new VpnLinkService();	

	VpnLinkDTO vpnLinkDTO = vpnLinkService.getVpnLinkByVpnLinkID( id );
	
	VpnNearEndDTO nearEndDTO = vpnLinkService.getNearEndByNearEndID( vpnLinkDTO.getNearEndID() );
	VpnFarEndDTO farEndDTO = vpnLinkService.getFarEndByFarEndID( vpnLinkDTO.getFarEndID() );
	
	VpnFRResponseExternalDTO nearEndResponse = null;
	VpnFRResponseExternalDTO farEndResponse = null;
	
	DatabaseConnection databaseConnection = new DatabaseConnection();
	
	databaseConnection.dbOpen();
	
	String conditionString = " where vresfrexEntityTypeID = " + EntityTypeConstant.VPN_LINK_NEAR_END + " and vresfrexNearOrFarEndpointID = " + nearEndDTO.getID();
	ArrayList<VpnFRResponseExternalDTO> nearEndResponseList = (ArrayList<VpnFRResponseExternalDTO>)SqlGenerator.getAllObjectList( VpnFRResponseExternalDTO.class, databaseConnection, conditionString);
	
	if( nearEndResponseList != null && nearEndResponseList.size() != 0 ){
		
		nearEndResponse = nearEndResponseList.get(0);
	}
	
	conditionString = " where vresfrexEntityTypeID = " + EntityTypeConstant.VPN_LINK_FAR_END + " and vresfrexNearOrFarEndpointID = " + farEndDTO.getID();
	ArrayList<VpnFRResponseExternalDTO> farEndResponseList = (ArrayList<VpnFRResponseExternalDTO>)SqlGenerator.getAllObjectList( VpnFRResponseExternalDTO.class, databaseConnection, conditionString);
	
	if( farEndResponseList != null && farEndResponseList.size() != 0 ){
		
		farEndResponse = farEndResponseList.get(0);
	}
	
	databaseConnection.dbClose();
	if( vpnLinkDTO != null ){
		
%>
	<table class='items billTable'>
		<thead>
			<tr class='title textcenter'>
				<td colspan="2"> Vpn Link Details </td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width='5%'>Link Name</td>
				<td width='5%'> <%=vpnLinkDTO.getLinkName() %> </td>
			</tr>
			<tr>
				<td width='5%'>Description</td>
				<td width='5%'> <%=vpnLinkDTO.getLinkDescription()  %> </td>
			</tr>
			<tr>	
				<td width='5%'>Bandwidth</td>
				<td width='5%'> <%=vpnLinkDTO.getVpnBandwidth() %> </td>
			</tr>	
			<tr>	
				<td width='5%'>VPN Distance (Km)</td>
				<td width='5%'> <%= ((APIUtil)ServiceDAOFactory.getService(APIUtil.class)).getDistanceByLinkID( vpnLinkDTO.getID() ).getDistance() %> </td>
			</tr>	
			<tr>	
				<td width='5%'>Local Loop Near End (m)</td>
				<td width='5%'> <%=nearEndResponse.getDistanceTotal() %> </td>
			</tr>	
			<tr>	
				<td width='5%'>Local Loop Remote End (m)</td>
				<td width='5%'> <%=farEndResponse.getDistanceTotal() %> </td>
			</tr>
		</tbody>
	</table>
<%}%>