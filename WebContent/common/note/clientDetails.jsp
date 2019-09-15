<%@page import="common.note.CommonNoteDAO"%>
<%@page import="common.note.CommonNote"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%
	String id = request.getParameter( "id" );
	
	if( id!=null ){
		
		long commonNoteID = Long.parseLong( "id" );
		
		CommonNote note = CommonNoteDAO.getById( commonNoteID );
	}
	String clientIDStr = request.getParameter( "clientID" );
	long clientID = Long.parseLong( clientIDStr );
	
	ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID( clientID );
%>
<table class="items">
	<tbody>
		<tr>
			<td width="100%">

				<div class="addressbox">

					<strong>Adviced To</strong>
					<br>
					Customer ID : <%=clientID %>
					<br>
					Name : <%=clientDTO.getName() %>
					<br>
					Customer User Name: <%=clientDTO.getLoginName() %>
				</div>
			</td>
		</tr>
	</tbody>
</table>