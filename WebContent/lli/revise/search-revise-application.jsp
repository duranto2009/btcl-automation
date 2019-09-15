<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lli-search");
    request.setAttribute("subMenu2","lli-search-application");
    request.setAttribute("subMenu3","lli-search-application-client");

%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Revise" />
	<jsp:param name="body" value="../lli/revise/search-revise-application-body.jsp" />
	<jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>