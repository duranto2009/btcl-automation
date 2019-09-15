<%@page import="common.CommonActionStatusDTO"%>
<%
request.setAttribute("menu","vpnMenu");
request.setAttribute("subMenu1","vpnConfigurationSubmenu1");
request.setAttribute("subMenu2","editInventorySubmenu");

new CommonActionStatusDTO().setWarningMessage("Currently <b>Add New Attributes</b> works. All the other actions are restricted.", true, request);
%>

<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Configure Inventory" /> 
	<jsp:param name="body" value="../vpn/inventory/editCategoryView.jsp" />
</jsp:include> 