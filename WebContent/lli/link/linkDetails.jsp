<%@page import="util.ServiceDAOFactory"%>
<%@page import="util.SqlGenerator"%>
<%@page import="lli.link.LliFarEndDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="connection.DatabaseConnection"%>
<%@page import="common.CommonDAO"%>
<%@page import="lli.link.LinkUtils"%>
<%@page import="lli.link.LliFRResponseExternalDTO"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page contentType="text/html;charset=utf-8" %>
<%
	
	long id = Long.parseLong( request.getParameter( "id" ) );
	
	LliLinkService lliLinkService = new LliLinkService();	

	LliLinkDTO lliLinkDTO = lliLinkService.getLliLinkByLliLinkID( id );
	
	LliFarEndDTO farEndDTO = lliLinkService.getFarEndByFarEndID( lliLinkDTO.getFarEndID() );
	
	LliFRResponseExternalDTO nearEndResponse = null;
	LliFRResponseExternalDTO farEndResponse = null;
	
	DatabaseConnection databaseConnection = new DatabaseConnection();
	
	databaseConnection.dbOpen();
	
	String conditionString = " where vresfrexEntityTypeID = " + EntityTypeConstant.LLI_LINK_FAR_END + " and vresfrexNearOrFarEndpointID = " + farEndDTO.getID();
	ArrayList<LliFRResponseExternalDTO> farEndResponseList = (ArrayList<LliFRResponseExternalDTO>)SqlGenerator.getAllObjectList( LliFRResponseExternalDTO.class, databaseConnection, conditionString);
	
	if( farEndResponseList != null && farEndResponseList.size() != 0 ){
		
		farEndResponse = farEndResponseList.get(0);
	}
	
	databaseConnection.dbClose();
	if( lliLinkDTO != null ){
		
%>
	<table class='items billTable'>
		<thead>
			<tr class='title textcenter'>
				<td colspan="2"> Lli Connection Details </td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width='5%'>Connection Name</td>
				<td width='5%'> <%=lliLinkDTO.getLinkName() %> </td>
			</tr>
			<tr>
				<td width='5%'>Description</td>
				<td width='5%'> <%=lliLinkDTO.getLinkDescription()  %> </td>
			</tr>
			<tr>	
				<td width='5%'>Bandwidth</td>
				<td width='5%'> <%=lliLinkDTO.getLliBandwidth() %> </td>
			</tr>		
			<tr>	
				<td width='5%'>Local Loop Remote End (m)</td>
				<td width='5%'> <%=farEndResponse.getDistanceTotal() %> </td>
			</tr>
		</tbody>
	</table>
<%}%>