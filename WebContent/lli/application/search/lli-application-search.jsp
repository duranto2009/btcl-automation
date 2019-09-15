<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lli-search");
  request.setAttribute("subMenu2","lli-search-application");
  request.setAttribute("subMenu3","lli-search-application-connection");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Application" /> 
	<jsp:param name="body" value="../lli/application/search/lli-application-search-body.jsp" />
    <jsp:param name="helpers" value="../common/datePickerHelper.jsp"/>
</jsp:include>