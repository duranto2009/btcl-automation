<%
	request.setAttribute("menu","lliMenu");

%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Revise" />
	<jsp:param name="body" value="../lli/revise/view-body.jsp" />
</jsp:include>