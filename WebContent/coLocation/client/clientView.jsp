<%@page import="common.ModuleConstants"%>
<%
request.setAttribute("menu","colocationMenu");
request.setAttribute("subMenu1","colocationClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Co Location Client View" /> 
	<jsp:param name="body" value="../coLocation/client/clientViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="magnificPopupHelper.jsp" />
</jsp:include> 