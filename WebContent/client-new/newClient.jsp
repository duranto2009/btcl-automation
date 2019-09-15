<!DOCTYPE html>
<%@page contentType="text/html;charset=utf-8" %>
<%
    String context = "../../.." + request.getContextPath() + "/";
    request.setAttribute("context", context);
    String actionName = "/AddClient";

%>
<html>
<head>
    <title>BTCL | Create New Client</title>
    <%@ include file="../skeleton_btcl/head.jsp" %>
    <link href="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/css/intlTelInput.css"
          rel="stylesheet"/>
    <link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet"
          type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet"
          type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet"
          type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet"
          type="text/css"/>
    <link rel="shortcut icon" href="${context}favicon.ico"/>
    <script src="https://unpkg.com/vue-form-wizard/dist/vue-form-wizard.js"></script>
    <link rel="stylesheet" href="https://unpkg.com/vue-form-wizard/dist/vue-form-wizard.min.css">
    <link rel="stylesheet" href="${context}dashboard/sweet-modal.css"/>
    <script src="${context}dashboard/sweet-modal.js"></script>
    <script type="text/javascript">
        var context = '${context}';
    </script>

    <!-- END HEAD -->
    <style type="text/css">
        .view-height {
            height: 50vh;
        }
        [v-cloak] {
            display: none;
        }
        hr {
            display: block;
            height: 1px;
            border: 0;
            border-top: 1px solid #ccc;
            margin: 1em 0;
            padding: 0;
        }
    </style>
</head>
<body class="page-container-bg-solid page-header-fixed1 page-sidebar-closed-hide-logo">

<div class="page-header navbar navbar-fixed-top1 highlight-header">
    <div class="page-header-inner">
        <div class="row text-center" style="max-height: 150px;">
            <a href="${context}">
                <img style="height: 80px;display: inline-block; "
                     src="${context}assets/images/company-name.png"
                     class="img-responsive" alt="company-name">
            </a>
        </div>
    </div>
</div>
<%--<div class="page-header navbar navbar-fixed-top1 highlight-header">--%>
<%--<%@ include file="../skeleton_btcl/header2.jsp"%>--%>
<%--</div>--%>
<div id="btcl-app" v-cloak="true">
    <btcl-body title="Client Registration"
               subtitle="for Data and Internet Services" >
        <btcl-portlet>
            <form-wizard title="" subtitle=""
                         step-size="sm"
                         color="#659dbd"
                         error-color="red"
                         :start-index="index"
                         @on-complete="finishTasks"
            >
                <template v-if="!redirected">
                    <jsp:include page="service-type.jsp"/>
                </template>

                <jsp:include page="authentication.jsp"/>
                <jsp:include page="registrant-information.jsp"/>
                <jsp:include page="login-information.jsp"/>

            </form-wizard>
        </btcl-portlet>
    </btcl-body>

</div>


<%@ include file="../skeleton_btcl/footer.jsp" %>
<%@ include file="../skeleton_btcl/includes.jsp" %>


<script src="${context}assets/global/plugins/moment.min.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"
        type="text/javascript"></script>

<script src="${context}assets/global/scripts/app.min.js" type="text/javascript"></script>
<script src="${context}assets/scripts/client/client-add-validation.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/js/utils.js"></script>


<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/js/intlTelInput.js"></script>
<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/examples/js/isValidNumber.js"></script>


<!-- vue btcl -->
<script src="${context}assets/global/scripts/btcl-vue.js"></script>
<script src="${context}assets/global/plugins/axios/axios.min.js" type="text/javascript"></script>
<script src="${context}client-new/client-registration.js" type="module"></script>

<script>

    let temporaryClientId = '<%=request.getAttribute("temporaryClientId")%>';

</script>


</body>
</html>



