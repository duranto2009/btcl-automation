<%@page import="org.apache.log4j.Logger"%>

<%
    request.setAttribute("menu","nixMenu");
    request.setAttribute("subMenu1","nix-monthly-Bill-summary");
    request.setAttribute("subMenu2","nix-monthly-bill-check");
%>
<%
Logger logger = Logger.getLogger("Check page");
try{ %>
<jsp:include page="/layout/layout2018.jsp" flush="true">
        <jsp:param name="title" value="Monthly Bill Not Generated" />      
         <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />       
         <jsp:param name="body" value="/nix/monthly-bill/nix-current-month-bill-generate-check-body.jsp" />
</jsp:include>
<%}catch(Exception e){
	logger.fatal("exception in page ",e);
}
%>
