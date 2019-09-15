<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliBillAndPayment");

  request.setAttribute("subMenu2","accounting-ledger-search");
  
%>
<jsp:include page="../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Ledger" /> 
	<jsp:param name="body" value="../accounting/search-ledger-body.jsp" />
	<jsp:param name="helpers" value="../common/datePickerHelper.jsp"/>
</jsp:include>