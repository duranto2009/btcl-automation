<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ include file="../../includes/checkLogin.jsp" %>


<%
  request.setAttribute("menu","domainMenu");
  request.setAttribute("subMenu1","domainBillAndPayment");
  request.setAttribute("subMenu2","manualDemandNote"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Domain Package Add" /> 
	<jsp:param name="body" value="../domain/NIXDemandNote/manualDemandNoteBody.jsp" />
</jsp:include>