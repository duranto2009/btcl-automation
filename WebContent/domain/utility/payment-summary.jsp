<%
	request.setAttribute("menu", "doaminMenu");
	request.setAttribute("subMenu1", "doaminReportMenu");
	request.setAttribute("subMenu2", "summary-report");
	
	
%>

<jsp:include page="/layout/layout2018.jsp" flush="true">
	<jsp:param name="title" value="Domain | Payment Summary" /> 
	<jsp:param name="body" value="/domain/utility/payment-summary-body.jsp" />
</jsp:include>