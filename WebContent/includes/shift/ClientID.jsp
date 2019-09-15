
<%@page import="client.ClientDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="client.ClientRepository"%>
<%String clientID = (String)session.getAttribute("csClientID");
if(clientID == null) clientID="";
%>
<tr>
	<td bgcolor="#deede6"
		style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25">Client ID</TD>
	<td bgcolor="#deede6" align="left" height="25"><select size="1" name="csClientID">
			<option value="" <%=(clientID.equals("")?"selected=\"selected\"":"")%>>All</option>
			<%
			ClientRepository cr= ClientRepository.getInstance();
			ArrayList cList= cr.getClientList();
			for(int i=0;i<cList.size();i++){
				//if(((ClientDTO)cList.get(i)).getAccountStatus() != ClientDTO.CUSTOMER_STATUS_REMOVED){
			%>
			<option value="<%=Long.toString(((ClientDTO)cList.get(i)).getUniqueID()) %>" <%=clientID.equals(""+((ClientDTO)cList.get(i)).getUniqueID())?"selected=\"selected\"":"" %>><%=((ClientDTO)cList.get(i)).getUserName() %></option>
			<%
				//}
			}
			%>
	</select></td>
</tr>