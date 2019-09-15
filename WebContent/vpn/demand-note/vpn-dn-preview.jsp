<%@ page import="common.ApplicationGroupType" %>
<%
    String renderJSP = "";
    String title = "";
    try{
        int type = Integer.parseInt(request.getParameter("appGroup"));

        ApplicationGroupType applicationGroupType = ApplicationGroupType.getAppGroupTypeByOrdinal(type);

        title = "VPN Link";
        renderJSP = "/vpn/demand-note/vpn-dn-link-body.jsp";
//        if(applicationGroupType == ApplicationGroupType.VPN_LINK_APPLICATION
//                ||applicationGroupType == ApplicationGroupType.VPN_LINK_CLIENT_APPLICATION
//        ){
//
//        }else if(applicationGroupType == ApplicationGroupType.VPN_CLIENT_APPLICATION){
//            title = "VPN Revise Client";
//            renderJSP = "/vpn/demand-note/vpn-dn-revise-client-body.jsp";
//        }

    }catch (Exception e){
        e.printStackTrace();
    }

%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="<%=title%>" />
    <jsp:param name="body" value="<%=renderJSP%>"/>
</jsp:include>