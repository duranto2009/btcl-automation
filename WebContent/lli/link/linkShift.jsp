<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","shiftLliLink");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Link Shift Details" /> 
	<jsp:param name="body" value="../lli/link/linkShiftBodyCompact.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
</jsp:include> 