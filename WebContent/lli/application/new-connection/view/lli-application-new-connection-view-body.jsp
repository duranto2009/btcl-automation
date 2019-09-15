<%@ page import="login.LoginDTO" %>
<%@ page import="sessionmanager.SessionConstants" %>
<style>

    .table-fit {
        /*white-space: nowrap;*/
        width: 1px;
    }
    tbody tr th:first-child {
        width: 15%;!important: ;
    }
</style>

<div id="btcl-application" v-cloak>




        <btcl-body :title="application.applicationType.label + ' (' + application.applicationID +')'"
                   subtitle="Information" :loader="loading">

            <%--form elements--%>
            <jsp:include page="body/lli-application-new-connection-view-body-form-elements.jsp"/>

            <%--Requested POP Details--%>
            <%--IFR Responses--%>
            <jsp:include page="body/lli-application-new-connection-view-body-ifr.jsp"/>


            <%--EFR Response--%>
            <%--Work Order Details--%>

            <jsp:include page="body/lli-application-new-connection-view-body-efr.jsp"/>

            <%--View Demand Note--%>
            <%--View Work Order--%>
            <%--View Advice Note--%>
            <jsp:include page="body/lli-application-new-connection-view-body-documents.jsp"/>


            <%--form actions--%>
            <jsp:include page="body/lli-application-new-connection-view-body-form-actions.jsp"/>


            <%--modals--%>
            <jsp:include page="body/lli-application-new-connection-view-modals.jsp"/>


            <btcl-portlet v-if="isPageLoaded && !efrRequested && !workOrderGenerate">
                <btcl-uploaded-file-view :params="{
                                'moduleId': 7,
                                'applicationId': application.applicationID,
                                'componentId':application.applicationType.ID,
                                'stateId':application.state
                            }">
                </btcl-uploaded-file-view>
            </btcl-portlet>

        </btcl-body>


</div>


<%
    LoginDTO loginDTO = (LoginDTO) session.getAttribute(SessionConstants.USER_LOGIN);
%>


<script>
    var applicationID = '<%=request.getParameter("id")%>';
    var loggedInUserID = '<%=loginDTO.getUserID()%>';
</script>


<script src="../connection/lli-connection-components.js"></script>
<script src="lli-application-components.js"></script>
<script src="new-connection/view/lli-application-new-connection-view.js" type="module"></script>





