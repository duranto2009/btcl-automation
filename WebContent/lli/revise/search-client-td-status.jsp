<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lli-search");
    request.setAttribute("subMenu2","lli-td-client-search");

%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Probable TD Clients" />
	<jsp:param name="body" value="../lli/revise/search-client-td-status-body.jsp" />
	<jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>