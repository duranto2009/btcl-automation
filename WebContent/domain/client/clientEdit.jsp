<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","domainClient");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Client Edit" /> 
	<jsp:param name="body" value="../domain/client/clientEditBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
	<jsp:param name="helpers" value="mobileNumberHelper.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
</jsp:include> 