<%@page import="common.ModuleConstants"%>
<%
request.setAttribute("menu","vpnMenu");
request.setAttribute("subMenu1","vpnClient");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="VPN Client View" /> 
	<jsp:param name="body" value="../vpn/client/clientViewBody.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="magnificPopupHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 