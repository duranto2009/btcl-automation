<%
request.setAttribute("menu","lliMenu");
request.setAttribute("subMenu1","lliLink");
request.setAttribute("subMenu2","lliLinkRequestIpAddressSubmenu2");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Request IP Address" /> 
	<jsp:param name="body" value="../lli/link/lliLinkIpaddressNewRequestBody.jsp" />
</jsp:include> 
