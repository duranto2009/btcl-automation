<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","configDomainMenu");
  request.setAttribute("subMenu2","packageTypeDomainMenu"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Package Type Add" /> 
	<jsp:param name="body" value="../domain/package/addPackageTypeBody.jsp" />
</jsp:include>