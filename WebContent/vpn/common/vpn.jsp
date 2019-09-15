<%@ page import="vpn.VPNConstants" %>
<%@ page import="common.ModuleConstants" %>
<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %><%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 12/4/18
  Time: 4:31 PM
--%>
<%LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);%>

<%
    //set up the menu
    request.setAttribute("menu","vpnMenu");
//    request.setAttribute("subMenu1","vpnLink");
//    request.setAttribute("subMenu2","addVpnLink");

    //request.setAttribute("subMenu3","lli-application-new-" + request.getParameter("id"));

    //which url to choose
    String url = (String)request.getAttribute(VPNConstants.VPN_SESSION_URL);
//
//    //choose content for the url
//    String path = "../vpn/link/add.jsp";
//    if(VPNConstants.LINK_ADD.equals(url)){
////        request.setAttribute("subMenu2","new-connection");
//        path = "../vpn/link/add.jsp";
//    }
//    else if(VPNConstants.LINK_SEARCH.equals(url)){
//        path = "../vpn/link/search.jsp";
//
//    }

    String path = VPNConstants.urlMap.get(url).getUrl();
    String subMenu1 = VPNConstants.urlMap.get(url).getSubMenu1();
    String subMenu2 = VPNConstants.urlMap.get(url).getSubMenu2();
    System.out.println("!! URL: " + request.getContextPath() +  url + " " + "!! Path: " + path + " " + "!! SubMenu1: " + subMenu1 + " " +"!! SubMenu2: " + subMenu2);
    request.setAttribute(VPNConstants.subMenu1,subMenu1);
    request.setAttribute(VPNConstants.subMenu2,subMenu2);
%>
<style>

    .table-fit {
        /*white-space: nowrap;*/
        width: 1px;
    }
    tbody tr th:first-child {
        width: 15%;!important: ;
    }
</style>
<script>
    var applicationID = parseInt('<%=request.getParameter("id")%>');
    var loggedInUserID = '<%=loginDTO.getUserID()%>';
    var moduleID = <%=ModuleConstants.Module_ID_VPN%>;
</script>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="VPN" />
    <jsp:param name="body" value="<%=path%>" />
    <jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>