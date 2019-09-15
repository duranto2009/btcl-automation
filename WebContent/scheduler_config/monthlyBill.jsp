<%
	String titleValue =( String ) request.getAttribute("title");
%>
<jsp:include page="../common/layout.jsp" flush="true">
	<jsp:param name="title" value="<%=titleValue %>" /> 
	<jsp:param name="body" value="../scheduler_config/monthlyBillBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include>