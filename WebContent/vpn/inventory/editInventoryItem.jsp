<%@page import="common.PermissionHandler"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>

<%PermissionHandler.handleMenuPermission(request, response, PermissionConstants.VPN_INVENTORY, PermissionConstants.VPN_INVENTORY_ADD, PermissionConstants.PERMISSION_FULL);%>

<%
request.setAttribute("menu","vpnMenu");
request.setAttribute("subMenu1","inventorySubmenu");
request.setAttribute("subMenu2","addInventorySubmenu");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Edit Inventory Item" /> 
	<jsp:param name="body" value="../vpn/inventory/editInventoryItemBody.jsp" />
</jsp:include>