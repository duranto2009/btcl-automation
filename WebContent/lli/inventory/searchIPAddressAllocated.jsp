<%
  	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","inventorySubmenuLLI");
  	request.setAttribute("subMenu2","ipAddressLLI");
  	request.setAttribute("subMenu3","search-allocated-ip");
  	String title = "Allocated IP Search";
%>



<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" /> 
	<jsp:param name="body" value="../lli/inventory/searchIPAddressAllocatedBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 