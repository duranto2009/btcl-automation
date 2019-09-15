<%

request.setAttribute("menu","vpnMenu");
request.setAttribute("subMenu1","inventorySubmenu");
request.setAttribute("subMenu2","addInventorySubmenu");

%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="View Inventory Item" /> 
	<jsp:param name="body" value="../vpn/inventory/viewInventoryItemBody.jsp" />
</jsp:include>