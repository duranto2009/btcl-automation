<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","domainAllMenu");
  request.setAttribute("subMenu2",""); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain Renewal Preview"  /> 
	<jsp:param name="body" value="../domain/renewDomain/renewDomainPreviewBody.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
</jsp:include>