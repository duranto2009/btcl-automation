<%@page import="ipaddress.IpBlock"%>
<%
  	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","inventorySubmenuLLI");
  	request.setAttribute("subMenu2","ipAddressLLI");
  	IpBlock ipBlock = (IpBlock) request.getAttribute("ipBlock");
  	String title = "";
  	if(ipBlock == null){
  		title  = "Add Original IP Block";
  	}else {
  		title = "Edit Original IP Block";
  	}
%>


<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" /> 
	<jsp:param name="body" value="../lli/inventory/addIPAddressBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 