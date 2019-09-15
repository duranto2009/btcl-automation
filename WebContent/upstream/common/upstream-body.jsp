<%@ page import="vpn.VPNConstants" %>
<%@ page import="common.ModuleConstants" %>
<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>
<%@ page import="upstream.UpstreamConstants" %><%--
  Created by IntelliJ IDEA.
  User: forhad
  Date: 12/4/18
  Time: 4:31 PM
--%>
<%LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);%>

<%
    request.setAttribute("menu","upstreamMenu");
    String url = (String)request.getAttribute(UpstreamConstants.UPSTREAM_SESSION_URL);
    String path = UpstreamConstants.urlMap.get(url).getUrl();
    String subMenu1 = UpstreamConstants.urlMap.get(url).getSubMenu1();
    String subMenu2 = UpstreamConstants.urlMap.get(url).getSubMenu2();
    System.out.println("========================START=================================");
    System.out.println("!! URL: " + url);
    System.out.println("!! Path: " + path);
    System.out.println("!! SubMenu1: " + subMenu1);
    System.out.println("!! SubMenu2: " + subMenu2);
    System.out.println("=========================END===================================");
    request.setAttribute(UpstreamConstants.subMenu1,subMenu1);
    request.setAttribute(UpstreamConstants.subMenu2,subMenu2);
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
    var applicationId = parseInt('<%=request.getParameter("id")%>');
    var loggedInUserID = '<%=loginDTO.getUserID()%>';
    var moduleID =<%=ModuleConstants.Module_ID_UPSTREAM%>;


    const TYPE_OF_BW ='<%=UpstreamConstants.INVENTORY_ITEM_TYPE.TYPE_OF_BW%>';
    const MEDIA ='<%=UpstreamConstants.INVENTORY_ITEM_TYPE.MEDIA%>';
    const BTCL_SERVICE_LOCATION ='<%=UpstreamConstants.INVENTORY_ITEM_TYPE.BTCL_SERVICE_LOCATION%>';
    const PROVIDER_LOCATION ='<%=UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER_LOCATION%>';
    const PROVIDER ='<%=UpstreamConstants.INVENTORY_ITEM_TYPE.PROVIDER%>';
</script>

<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Upstream" />
    <jsp:param name="body" value="<%=path%>" />
    <jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
    <jsp:param name="helpers" value="../common/datePickerHelper.jsp"/>
</jsp:include>

