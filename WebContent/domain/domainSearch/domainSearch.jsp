<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","domainAllMenu");
  request.setAttribute("subMenu2","searchBuyDomainMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain Search" /> 
	<jsp:param name="body" value="../domain/domainSearch/domainSearchBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 