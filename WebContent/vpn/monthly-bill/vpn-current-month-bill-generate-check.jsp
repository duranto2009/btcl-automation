<%@page import="org.apache.log4j.Logger"%>

<%
    request.setAttribute("menu","vpnMenu");
    request.setAttribute("subMenu1","vpn-monthly-Bill-summary");
    request.setAttribute("subMenu2","vpn-monthly-bill-check");
%>
<%
Logger logger = Logger.getLogger("Check page");
try{ %>
<jsp:include page="/layout/layout2018.jsp" flush="true">
        <jsp:param name="title" value="Monthly Bill Not Generated" />      
         <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />       
         <jsp:param name="body" value="/vpn/monthly-bill/vpn-current-month-bill-generate-check-body.jsp" />
</jsp:include>
<%}catch(Exception e){
	logger.fatal("exception in page ",e);
}
%>
