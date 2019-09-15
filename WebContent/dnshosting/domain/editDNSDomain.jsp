<%
  	request.setAttribute("menu","dnshostingMenu");
	request.setAttribute("subMenu1","dnshostingDomainMenu");
  	request.setAttribute("subMenu2","addDnshostingDomainMenu");
%> 

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Edit DNS Domain" /> 
	<jsp:param name="body" value="../dnshosting/domain/editDNSDomainBody.jsp" />
</jsp:include>