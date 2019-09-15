<%
  	request.setAttribute("menu","lliMenu");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-shift-pop");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Shift Pop" /> 
	<jsp:param name="body" value="../lli/application/shift-pop/lli-application-shift-pop-body.jsp" />
</jsp:include>