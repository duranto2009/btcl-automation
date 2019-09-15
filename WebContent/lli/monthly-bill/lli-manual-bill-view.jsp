<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lliBillAndPayment");
  request.setAttribute("subMenu2","manual-bill");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Manual Bill" /> 
	<jsp:param name="body" value="/lli/monthly-bill/lli-manual-bill-view-body.jsp" />
</jsp:include>