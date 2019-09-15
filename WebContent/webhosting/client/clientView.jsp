<%@page import="common.ModuleConstants"%>
<%
request.setAttribute("menu","webHostingMenu");
request.setAttribute("subMenu1","webHostingClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Web Hosting Client View" /> 
	<jsp:param name="body" value="../webhosting/client/clientViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="magnificPopupHelper.jsp" />
</jsp:include> 