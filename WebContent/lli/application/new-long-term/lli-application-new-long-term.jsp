<%
	request.setAttribute("menu","lliMenu");
	request.setAttribute("subMenu1","lli-application");
	request.setAttribute("subMenu2","lli-new-long-term");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="New Long Term" /> 
	<jsp:param name="body" value="../lli/application/new-long-term/lli-application-new-long-term-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>