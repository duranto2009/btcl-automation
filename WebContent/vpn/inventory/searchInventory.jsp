<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","inventorySubmenu");
  request.setAttribute("subMenu2","searchInventorySubmenu");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Search Inventory Items" /> 
	<jsp:param name="body" value="../vpn/inventory/searchInventoryBody.jsp" />
</jsp:include> 