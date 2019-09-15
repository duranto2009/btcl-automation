<%
	String titleValue = "View Payload";
%>
<jsp:include page="../common/layout.jsp" flush="true">
	<jsp:param name="title" value="<%=titleValue %>" /> 
	<jsp:param name="body" value="../scheduler_config/viewCommonPayloadBody.jsp" />
</jsp:include>