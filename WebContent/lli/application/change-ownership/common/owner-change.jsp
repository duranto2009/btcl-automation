<%@ page import="lli.Application.ownership.LLIOwnerChangeConstant" %>
<%
    //which url to choose
    String url = (String)request.getAttribute(LLIOwnerChangeConstant.OWNER_CHANGE_SESSION_URL);

    //choose content for the url
    String path = "/lli/application/change-ownership/lli-application-change-ownership-view.jsp";
    if(LLIOwnerChangeConstant.APPLICATION_SEARCH.equals(url)){
        path = "/lli/application/change-ownership/search/lli-application-change-ownership-search.jsp";
    }
    else if(LLIOwnerChangeConstant.APPLICATION_DEMAND_NOTE.equals(url)){
//        path = "../lli/application/change-ownership/demandnote/owner-change-demand-note.jsp";
        path = "../lli/demand-note/lli-dn-ownership-change-body.jsp";
//        E:\BTCL\btcl-automation\WebContent\lli\application\change-ownership\demandnote\owner-change-demand-note.jsp
    }

%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Owner Change" />
    <jsp:param name="body" value="<%=path%>" />
    <jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>