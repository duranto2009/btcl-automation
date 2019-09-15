<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliBillAndPayment");
    request.setAttribute("subMenu2","monthly-bill");
    request.setAttribute("subMenu3","lli-monthly-outsource-Bill-search");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="/lli/outsourcing-bill/lli-oc-bill-body.jsp" />
    <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>