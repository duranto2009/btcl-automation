<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lli-configuration");
    request.setAttribute("subMenu2","lli-configuration-category");
%>

<style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }
    th, td {
        padding: 5px;
        text-align: left;
    }
    
        #categoryEdit{
            top: 30%;
            margin-left: 30%;
            left: 0;
            width: 60%;
            height: 50%;
            z-index: 10;
            background-color: whitesmoke;
            position: fixed;
            border-style:groove;
        }
    
</style>





<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Add New Category" />
    <jsp:param name="body" value="../lli/configuration/lli-configuration-new-category-body-new.jsp" />
</jsp:include>