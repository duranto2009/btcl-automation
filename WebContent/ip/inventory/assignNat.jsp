<%
    request.setAttribute("menu","ip-management");
    request.setAttribute("subMenu1","ip-management-inventory");
    request.setAttribute("subMenu2","inventory-nat");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="IP Management" />
    <jsp:param name="body" value="/ip/inventory/assignNatBody.jsp" />
    <jsp:param name="css" value="components/modal.css"/>
    <jsp:param name="js" value="/ip/js/ip-utility.js"/>
</jsp:include>