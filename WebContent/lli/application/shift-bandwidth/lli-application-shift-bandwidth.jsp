<%
  	request.setAttribute("menu","lliMenu");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-shift-bandwidth");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Shift Bandwidth" /> 
	<jsp:param name="body" value="../lli/application/shift-bandwidth/lli-application-shift-bandwidth-body.jsp" />
</jsp:include>