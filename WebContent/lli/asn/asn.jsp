<%@ page import="common.ModuleConstants" %>
<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>
<%@ page import="lli.asn.ASNConstant" %><%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 12/4/18
  Time: 4:31 PM
--%>
<%LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);%>

<%
    request.setAttribute("menu","lliMenu");

    String url = (String)request.getAttribute(ASNConstant.ASN_SESSION_URL);

    String path = ASNConstant.urlMap.get(url).getUrl();
    String subMenu1 = ASNConstant.urlMap.get(url).getSubMenu1();
    String subMenu2 = ASNConstant.urlMap.get(url).getSubMenu2();
    System.out.println("!! URL: " + request.getContextPath() +  url + " " + "!! Path: " + path + " " + "!! SubMenu1: " + subMenu1 + " " +"!! SubMenu2: " + subMenu2);
    request.setAttribute(ASNConstant.subMenu1,subMenu1);
    request.setAttribute(ASNConstant.subMenu2,subMenu2);
%>
<style>
    .table-fit {
        width: 1px;
    }
    tbody tr th:first-child {
        width: 15%;!important: ;
    }
</style>
<script>
    var applicationID = parseInt('<%=request.getParameter("id")%>');
    var loggedInUserID = '<%=loginDTO.getUserID()%>';
    var moduleID =<%=ModuleConstants.Module_ID_LLI%>;
</script>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="ASN" />
    <jsp:param name="body" value="<%=path%>" />
    <jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>