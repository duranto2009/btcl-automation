<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","searchLliLink");
%>
<!-- BEGIN PAGE LEVEL STYLES -->
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="LLI Link View" /> 
	<jsp:param name="body" value="../lli/link/linkViewBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
</jsp:include> 

