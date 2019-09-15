<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-connection");
  request.setAttribute("subMenu2","lli-change-lli.Application.ownership");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Demand Note" /> 
	<jsp:param name="body" value="../lli/demand-note/lli-dn-lli.Application.ownership-change-view-body.jsp" />
</jsp:include>