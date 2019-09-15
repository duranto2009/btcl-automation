<%
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lli-application");
	request.setAttribute("subMenu2","lli-change-ownership");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Change Ownership" /> 
	<jsp:param name="body" value="../lli/application/change-ownership/lli-application-change-ownership-body.jsp" />
</jsp:include>