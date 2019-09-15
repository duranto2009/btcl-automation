<%
request.setAttribute("menu","nixMenu");
request.setAttribute("subMenu1","nixClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="NIX Client Summary" /> 
	<jsp:param name="body" value="../nix/client/clientPreviewBody.jsp" />
</jsp:include> 