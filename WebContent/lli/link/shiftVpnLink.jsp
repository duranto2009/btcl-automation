<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","shiftLLILink");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Shfit LLI Link " /> 
	<jsp:param name="body" value="../lli/link/shiftLliLinkBody.jsp" />
</jsp:include> 
