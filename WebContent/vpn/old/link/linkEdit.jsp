<%@page import="vpn.constants.VpnRequestTypeConstants"%>

<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","addVpnLink");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Link Edit Details" /> 
	<jsp:param name="body" value="../vpn/link/linkEditBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
</jsp:include> 