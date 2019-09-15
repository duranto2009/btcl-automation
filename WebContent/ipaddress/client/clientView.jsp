<%@page import="common.ModuleConstants"%>
<%
request.setAttribute("menu","ipaddressMenu");
request.setAttribute("subMenu1","ipaddressClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="IP Address Client View" /> 
	<jsp:param name="body" value="../ipaddress/client/clientViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="magnificPopupHelper.jsp" />
</jsp:include> 