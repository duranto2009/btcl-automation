<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>
<div id="btcl-nix-application" v-cloak="true">

    <%--<div class="row" v-if="loading" style="text-align: center">--%>
        <%--<i class="fa fa-spinner fa-spin fa-5x"></i>--%>
    <%--</div>--%>

    <div id="appBody">
        <btcl-body :title="application.applicationType.label + ' (' + application.applicationID +')'"
                   subtitle="Information" :loader="loading">

            <%--form elements--%>
            <jsp:include page="body/nix-application-new-connection-view-body-form-elements.jsp"/>

            <%--Requested POP Details--%>
            <%--IFR Responses--%>
            <jsp:include page="body/nix-application-new-connection-view-body-ifr.jsp"/>


            <%--EFR Response--%>
            <%--Work Order Details--%>

            <jsp:include page="body/nix-application-new-connection-view-body-efr.jsp"/>

            <%--View Demand Note--%>
            <%--View Work Order--%>
            <%--View Advice Note--%>
            <jsp:include page="body/nix-application-new-connection-view-body-documents.jsp"/>


            <%--form actions--%>
            <jsp:include page="body/nix-application-new-connection-view-body-form-actions.jsp"/>


            <%--modals--%>
            <jsp:include page="body/nix-application-new-connection-view-modals.jsp"/>


        </btcl-body>
    </div>

</div>


<%
    LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);
%>


<script>
    let applicationID = '<%=request.getParameter("id")%>';
    let loggedInUserID = '<%=loginDTO.getUserID()%>';
</script>

<script src="nix-application-components.js"></script>
<script src="view/nix-application-new-connection-view.js"></script>





