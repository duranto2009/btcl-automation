<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","searchLliLink");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Lli Link Search" /> 
	<jsp:param name="body" value="../lli/link/linkSearchBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 