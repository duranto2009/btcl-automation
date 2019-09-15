<%
  	request.setAttribute("menu","lli");
	request.setAttribute("subMenu1","lliBillAndPayment");
  	request.setAttribute("subMenu2","accounting-incident-search");
  
%>
<jsp:include page="../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Accounting Incident Search" /> 
	<jsp:param name="body" value="../accounting/search-incident-body.jsp" />
	<jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>