<%
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lli-search");
	request.setAttribute("subMenu2", "lli-long-term-search");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Long Term Contract Search" /> 
	<jsp:param name="body" value="../lli/longterm/lli-longterm-search-body.jsp" />
	<jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>