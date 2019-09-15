<%
request.setAttribute("menu","lliMenu");
request.setAttribute("subMenu1","lliClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="LLI Client Summary" /> 
	<jsp:param name="body" value="../lli/client/clientPreviewBody.jsp" />
</jsp:include> 