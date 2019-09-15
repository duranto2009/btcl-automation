<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","lliLinkCloseSubmenu2");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link Close Request" /> 
	<jsp:param name="body" value="../lli/link/linkCloseBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 
