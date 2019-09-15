<%@page import="org.apache.log4j.Logger"%>

<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lliBillAndPayment");

    request.setAttribute("subMenu2","monthly-bill");
    request.setAttribute("subMenu3","lli-monthly-bill-check");
%>
<%
Logger logger = Logger.getLogger("Check page");
try{ %>
<jsp:include page="/layout/layout2018.jsp" flush="true">
        <jsp:param name="title" value="Monthly Bill Not Generated" />      
         <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />       
         <jsp:param name="body" value="/lli/monthly-bill/lli-current-month-bill-generate-check-body.jsp" />
</jsp:include>
<%}catch(Exception e){
	logger.fatal("exception in page ",e);
}
%>
