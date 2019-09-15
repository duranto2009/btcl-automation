<%
  	request.setAttribute("menu","lli");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-application-new");
  	request.setAttribute("subMenu3","lli-application-new-" + request.getParameter("id"));
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Shift Address" /> 
	<jsp:param name="body" value="../lli/application/shift-address/lli-application-shift-address-body.jsp" />
</jsp:include>