<%@page import="common.ModuleConstants"%>
<%
request.setAttribute("menu","nixMenu");
request.setAttribute("subMenu1","nixClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="NIX Client View" /> 
	<jsp:param name="body" value="../nix/client/clientViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="magnificPopupHelper.jsp" />
</jsp:include> 