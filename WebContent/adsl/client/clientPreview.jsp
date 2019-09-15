<%
request.setAttribute("menu","adslMenu");
request.setAttribute("subMenu1","adslClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="ADSL Client Summary" /> 
	<jsp:param name="body" value="../adsl/client/clientPreviewBody.jsp" />
</jsp:include> 