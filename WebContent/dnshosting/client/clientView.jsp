<%@page import="common.ModuleConstants"%>
<%
request.setAttribute("menu","dnshostingMenu");
request.setAttribute("subMenu1","dnshostingClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="DNS Hosting Client View" /> 
	<jsp:param name="body" value="../dnshosting/client/clientViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="magnificPopupHelper.jsp" />
</jsp:include> 