<%@page import="ipaddress.IpBlock"%>
<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","inventorySubmenuLLI");
	request.setAttribute("subMenu2","ipAddressLLI");
  	IpBlock ipBlock = (IpBlock) request.getAttribute("ipBlock");
  	String title = "Add IP Block Tester";
  	
%>


<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" /> 
	<jsp:param name="body" value="../lli/inventory/viewIPBlockTesterBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 