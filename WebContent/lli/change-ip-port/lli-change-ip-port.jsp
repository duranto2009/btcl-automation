<%
  	request.setAttribute("menu","lliMenu");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-change-ip-port");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Change IP PORT" />
	<jsp:param name="body" value="../lli/change-ip-port/lli-change-ip-port-body.jsp" />
</jsp:include>