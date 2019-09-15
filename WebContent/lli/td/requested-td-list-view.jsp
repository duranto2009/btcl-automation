<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-connection");
  request.setAttribute("subMenu2","lli-existing-connection");
  request.setAttribute("subMenu3", "lli-td-reconnect-connection");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/td/requested-td-list-view-body.jsp" />
	<jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>