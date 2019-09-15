<%
  	request.setAttribute("menu","dnshostingMenu");
	request.setAttribute("subMenu1","dnshostingDomainMenu");
  	request.setAttribute("subMenu2","searchDnshostingDomainMenu");
%> 

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Search" /> 
	<jsp:param name="body" value="../dnshosting/domain/searchDNSDomainBody.jsp" />
</jsp:include>