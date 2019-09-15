<%
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lli-search");
	request.setAttribute("subMenu2","lli-search-application");
	request.setAttribute("subMenu3","lli-change-ownership-search");

%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Change Ownership" /> 
	<jsp:param name="body" value="../lli/application/change-ownership/search/lli-application-change-ownership-search-body.jsp" />
</jsp:include>