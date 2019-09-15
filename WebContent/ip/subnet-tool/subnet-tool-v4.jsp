<%
  request.setAttribute("menu","ip-management");
  request.setAttribute("subMenu1","ip-management-subnet");
  request.setAttribute("subMenu2","subnet-v4");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="IP Management" /> 
	<jsp:param name="body" value="/ip/subnet-tool/subnet-tool-v4-body.jsp" />
</jsp:include>