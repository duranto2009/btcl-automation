<%
  	request.setAttribute("menu","lli");
  	request.setAttribute("subMenu1","lli-connection");
  	request.setAttribute("subMenu2","lli-application-existing-connection");
  	request.setAttribute("subMenu3","lli-change-ownership");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Change Ownership" /> 
	<jsp:param name="body" value="../lli/application/change-ownership/lli-application-change-ownership-view-body.jsp" />
</jsp:include>