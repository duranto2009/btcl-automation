<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","domainReportMenu");
  request.setAttribute("subMenu2","domainSearchLogReportMenu"); 
%>

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain Search Log" /> 
	<jsp:param name="body" value="../domain/searchLog/searchLogBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 