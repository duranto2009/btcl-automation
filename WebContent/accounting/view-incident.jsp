<%
  request.setAttribute("menu","lliBillAndPayment");
  request.setAttribute("subMenu1","accounting");
  request.setAttribute("subMenu2","incident");
  
%>
<jsp:include page="../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Accounting Incident" /> 
	<jsp:param name="body" value="../accounting/view-incident-body.jsp" />
</jsp:include>