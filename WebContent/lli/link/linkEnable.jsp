<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","lliLinkEnable");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link Enable Request" /> 
	<jsp:param name="body" value="../lli/link/linkEnableBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 
