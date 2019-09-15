<%
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lli-application");
	request.setAttribute("subMenu2","lli-reconnect");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Reconnect" /> 
	<jsp:param name="body" value="../lli/application/reconnect/lli-application-reconnect-body.jsp" />
</jsp:include>C