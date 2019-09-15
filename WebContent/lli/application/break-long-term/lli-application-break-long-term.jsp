<%
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lli-application");
	request.setAttribute("subMenu2", "lli-terminate-long-term");
%>

<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Terminate Long Term" />
	<jsp:param name="body" value="../lli/application/break-long-term/lli-application-break-long-term-body.jsp" />
</jsp:include>