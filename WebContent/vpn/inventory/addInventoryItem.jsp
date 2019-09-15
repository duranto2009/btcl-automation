<%@page import="common.ModuleConstants"%>
<%

String moduleIDStr = request.getParameter( "moduleID" );
Integer moduleID = null;

if( moduleIDStr == null ){
	
	moduleIDStr = (String)request.getAttribute( "moduleID" );
	
	if( moduleIDStr == null ){
		
		moduleIDStr = (String)request.getSession().getAttribute( "moduleID" );
	}
}

if( moduleIDStr != null ){
	
	moduleID = Integer.parseInt( moduleIDStr );
}

if( moduleID != null && moduleID == ModuleConstants.Module_ID_LLI ){
	
	request.setAttribute("menu", "lliMenu");
	request.setAttribute("subMenu1","inventorySubmenuLLI");
	request.setAttribute("subMenu2","addInventorySubmenuLLI");
	request.setAttribute( "moduleID", moduleID );
}
else{
  	
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","inventorySubmenu");
	request.setAttribute("subMenu2","addInventorySubmenu");
	
}
%>
<jsp:include page="/common/layout.jsp" flush="true">
<jsp:param name="title" value="Add Inventory Item" /> 
	<jsp:param name="body" value="../vpn/inventory/addInventoryItemBody.jsp" />
</jsp:include>