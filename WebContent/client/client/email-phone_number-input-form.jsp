<!DOCTYPE html>
<%@page import="util.ServiceDAOFactory" %>
<%@page import="vpn.client.ClientService" %>
<%@page import="java.util.List" %>
<%@ page import="client.temporaryClient.TemporaryClientService" %>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@page contentType="text/html;charset=utf-8" %>

<%
    String context = "../../.." + request.getContextPath() + "/";
    request.setAttribute("context", context);

    ServiceDAOFactory.getService(TemporaryClientService.class).deleteTemporaryClientsOlderThanAnHour();

    List<String> allEmails = ServiceDAOFactory.getService(ClientService.class).getAllUsedEmails();
    List<String> allMobileNumbers = ServiceDAOFactory.getService(ClientService.class).getAllUsedMobileNumbers();

    Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
    Integer accountType = Integer.parseInt(request.getParameter("accountType"));

    request.getSession().setAttribute("moduleId", moduleID);
    request.getSession().setAttribute("accountType", accountType);
%>
<html>

<link href="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/css/intlTelInput.css"
      rel="stylesheet"/>

<head>
    <title>BTCL | Registration</title>
    <%@ include file="../../skeleton_btcl/head.jsp" %>
    <link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css"
          rel="stylesheet" type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css"
          rel="stylesheet" type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css"
          rel="stylesheet" type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css"
          rel="stylesheet" type="text/css"/>

    <script type="text/javascript">
        var context = '${context}';
    </script>

    <!-- END HEAD -->
    <style type="text/css">
        .page-content-wrapper .page-content {
            margin-left: 15px !important;
            margin-right: 15px !important;
        }

        .lr-no-padding {
            padding: 0 !important;
        }

        @media screen and (max-width: 480px) {
            .modal-body, .modal-overflow .modal-body {
                max-height: 250px !important;
                overflow-y: scroll;
                overflow: auto !important;
            }
        }
    </style>
</head>

<body class="page-container-bg-solid page-header-fixed1 page-sidebar-closed-hide-logo">
<div id="fakeLoader"></div>
<div class="page-header navbar navbar-fixed-top1 highlight-header">
    <%@ include file="../../skeleton_btcl/header2.jsp" %>
</div>
<div class="clearfix"></div>
<div class="page-container highlight-header">
    <div class="page-content-wrapper">
        <div class="page-content highlight-header">
            <jsp:include page='../../common/flushActionStatus.jsp'/>

            <form name="submitForm" onsubmit="return validateFormData()" class="form-horizontal"
                  action="<%=request.getContextPath()%>/Client/temporaryAdd.do" method="POST"
                  id="fileupload">

                <div class="portlet  box portlet-btcl">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa  fa-sign-in "></i>Client Registration
                        </div>
                    </div>
                    <div class="portlet-body form" id="regHolder">

                        <h3 class="form-section">Registrant Info</h3>

                        <div class="row">
                            <div class="col-md-6">
                                <div class=form-group>
                                    <label class="col-sm-4 control-label">
                                        Email<span class=required aria-required=true>*</span></label>
                                    <div class=col-sm-8>
                                        <input type=text name="email"
                                               class="form-control company regi"/>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <br/>

                        <div class="row">
                            <div class="col-md-6">
                                <div class=form-group>
                                    <label class="col-sm-4 control-label">Mobile Number<span class=required
                                                                                             aria-required=true>*</span></label>
                                    <div class=col-sm-8>
                                        <input name="intlMobileNumber" class="phoneNumber form-control company regi"
                                               type="tel">
                                        <span class="hide valid-msg"> Mobile number is valid</span>
                                        <span class="hide error-msg"> Mobile number is invalid </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br/>

                        <div class="row" align="right">
                            <div class="col-md-12">
                                <div class=form-group>
                                    <div class=col-sm-6>
                                        <input type="submit" value="Go to Verify Page"/>
                                    </div>
                                </div>
                            </div>
                        </div>


                    </div>
                </div>

            </form>
            <p><font color="black" size="6"> ***Once verified, the email and the phone number can not be changed</font></p>
            <p><font color="black" size="6"> ***You must complete the registration process within 1 (one) hour. After that,
                you will have to start from the beginning again.</font></p>

        </div>
    </div>
</div>
<script>
    function validateFormData() {
        let email = document.submitForm.email.value;
        let code = document.getElementsByClassName("selected-dial-code")[0].innerText;
        let mobile = document.submitForm.intlMobileNumber.value;

        if (email === "") {
            toastr.error('email must not be empty');
            return false;
        }

        if (code === "") {
            toastr.error("please, select a country code");
            return false;
        }

        if (mobile === "") {
            toastr.error("please, provide your mobile number");
            return false;
        }

        if (checkifEmailIsAlreadyUsed(email)) {
            toastr.error("email id already in use");
            return false;
        }

        if (mobile.charAt(0) == '0') {
            mobile = mobile.slice(1, mobile.length);
        }


        document.submitForm.intlMobileNumber.value = code + mobile;


        if (checkifMobileIsAlreadyUsed(document.submitForm.intlMobileNumber.value)) {
            toastr.error("Phone Number already in use");
            document.submitForm.intlMobileNumber.value=mobile;
            return false;
        }

        return true;
    }

    function checkifEmailIsAlreadyUsed(email) {

        var list = new Array();
        let i;

        <%
            String mail="";
            for(int i=0; i< allEmails.size();i++){
                mail+="'" + allEmails.get(i) + "'"+",";
            }

        %>
        var list = [<%=mail%>];


        for (i = 0; i < list.length; i++) {
            if (list[i].trim() === email)
                return true;
        }

        return false;
    }

    function checkifMobileIsAlreadyUsed(mobileNumber) {
        var list = new Array();
        let i;

        <%
            String mobile="";
            for(int i=0; i< allMobileNumbers.size();i++){
                mobile+="'" + allMobileNumbers.get(i) + "'"+",";
            }

        %>
        var list = [<%=mobile%>];


        for (i = 0; i < list.length; i++) {
            if (list[i].trim() === mobileNumber)
                return true;
        }

        return false;
    }

</script>

<%@ include file="../../skeleton_btcl/footer.jsp" %>
<%@ include file="../../skeleton_btcl/includes.jsp" %>


<script
src = "${context}assets/global/plugins/moment.min.js"
type = "text/javascript" ></script>
<script
src = "${context}assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"
type = "text/javascript" ></script>

<script src="${context}assets/global/scripts/app.min.js" type="text/javascript"></script>
</body>
</html>

<script src="<%=request.getContextPath()%>/assets/scripts/client/client-account-type-utils.js"></script>
<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/js/intlTelInput.js"></script>
<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/examples/js/isValidNumber.js"></script>






