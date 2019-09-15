<%@page import="util.ServiceDAOFactory" %>
<%@ page import="client.temporaryClient.TemporaryClient" %>
<%@ page import="global.GlobalService" %>
<jsp:useBean id="date" class="java.util.Date"/>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@page contentType="text/html;charset=utf-8" %>

<%

    long clientId = (Long) request.getAttribute("clientId");

    TemporaryClient client = ServiceDAOFactory.getService(
            GlobalService.class
    ).findByPK(TemporaryClient.class, clientId);

%>

<%
    String context = "../../.." + request.getContextPath() + "/";
    request.setAttribute("context", context);
%>
<html>

<link href="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/css/intlTelInput.css"
      rel="stylesheet"/>


<head>
    <title>BTCL | Create New Client</title>
    <%@ include file="../../skeleton_btcl/head.jsp" %>
    <link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet"
          type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet"
          type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet"
          type="text/css"/>
    <link href="${context}assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet"
          type="text/css"/>

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
<div class="portlet box portlet-btcl">
    <div class="portlet-body">
        <div class="tab-content">
            <div id="tab_5_1" class="tab-pane active">
                <h3>Information</h3>
                <div class1="well well-lg">
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped">
                            <tbody>
                            <tr>
                                <th scope="row">Email</th>
                                <td>
                                    <%=client.getEmailId()%>
                                    <%if (client.isEmailVerified() == false) { %>
                                    <button class="btn btn-xs btn-warning-btcl" title="Please verify your email"
                                            id="sendVerificationMail"> verify
                                    </button>
                                    <%} else { %>
                                    <label id="emailVerified" class="badge badge-success" title="Email is verified">
                                        <i class="fa fa-check"></i> Verified </label>
                                    <%} %>

                                    <label style="visibility: hidden;" id="emailVerified" class="badge badge-success"
                                           title="Email is verified">
                                        <i class="fa fa-check"></i> Verified </label>
                                </td>
                            </tr>

                            <tr>
                                <th scope="row">Mobile</th>
                                <td>
                                    <%=client.getMobileNumber()%>
                                    <%if (client.getMobileNumber().equals("")) { %>
                                    N/A
                                    <%} else if (client.isMobileNumberVerified() == false) { %>
                                    <button class="btn btn-xs btn-warning-btcl" title="Please verify your Phone No"
                                            id="sendVerificationSMS"> Verify
                                    </button>
                                    <%} else { %>

                                    <label class="badge badge-success" title="Phone no is verified"> <i
                                            class="fa fa-check"></i> Verified </label>
                                    <%} %>
                                    <label class="badge badge-success" title="Phone no is verified" style="display:none"
                                           id="phoneVerified"> <i class="fa fa-check"></i> Verified </label>

                                    <div style="display: none;" id="OTPForm">
                                        <input type="text" style="width:30%; display: inline;" class="form-control"
                                               id="otp"/> &nbsp
                                        <button class="btn btn-sm btn-primary" id="otpSubmit"> Submit</button>
                                        <button class="btn btn-sm btn-danger" id="resendOTP"> Resend Code</button>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>

                        <%
                            String url = context + "Client/get-reg-form.do";
                        %>
                        <form action="<%=url%>" method="post">
                            <input type="hidden" name="email" value="<%=client.getEmailId()%>">
                            <input type="hidden" name="mobile" value="<%=client.getMobileNumber()%>">
                            <input type="hidden" name="tempClientId" value="<%=client.getId()%>">
                            <%if (client.isEmailVerified() && client.isMobileNumberVerified()) {%>
                            <input type="submit" value="Next Page" class="btn btn-default"></a>
                            <%}%>
                        </form>


                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../../skeleton_btcl/footer.jsp" %>
<%@ include file="../../skeleton_btcl/includes.jsp" %>


<script src="${context}assets/global/plugins/moment.min.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"
        type="text/javascript"></script>

<script src="${context}assets/global/scripts/app.min.js" type="text/javascript"></script>
<script src="${context}assets/scripts/client/client-add-validation.js" type="text/javascript"></script>

</body>

</html>

<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/js/utils.js"></script>

<!-- /.row -->
<script src="<%=request.getContextPath() %>/assets/scripts/client/phoneVerificationHandler.js"></script>
<script type="text/javascript">
    <%--function wait(ms){--%>
    <%--var start = new Date().getTime();--%>
    <%--var end = start;--%>
    <%--while(end < start + ms) {--%>
    <%--end = new Date().getTime();--%>
    <%--}--%>
    <%--}--%>
    <%--function checkIfEmailVerified(){--%>
    <%--//action method--%>

    <%--// every 20 sec--%>
    <%--var isVerified = false;--%>
    <%--var i = 0;--%>
    <%--while(true){--%>
    <%--//call ajax--%>
    <%--i++;--%>
    <%--console.log("isVerified: " + isVerified + ", count: " + i);--%>

    <%--var clientId = '<%=client.getId() %>';--%>
    <%--var email = '<%=client.getEmailId()%>';--%>
    <%--var url = contextPath + "/api/client.do";--%>

    <%--// if (email.length == 0) {--%>
    <%--//--%>
    <%--//     bootbox.alert("Invalid email address");--%>
    <%--//     return false;--%>
    <%--// }--%>

    <%--var param = {};--%>
    <%--param['method'] = "isEmailVerified";--%>
    <%--param['id'] = clientId;--%>
    <%--// param['email'] = email;--%>


    <%--$.ajax({--%>
    <%--url: url,--%>
    <%--dataType: 'json',--%>
    <%--data: param,--%>
    <%--success: function (data) {--%>
    <%--debugger;--%>
    <%--if(data["message"]=="true")isVerified = true;--%>
    <%--console.log(data);--%>
    <%--}--%>
    <%--});--%>
    <%--if(isVerified)break;--%>

    <%--wait(20000); //wait 20 seconds--%>
    <%--}--%>
    <%--//true ->--%>
    <%--document.getElementById("sendVerificationMail").style.display = 'none';--%>
    <%--document.getElementById("emailVerified").style.display = 'block';--%>
    <%--}--%>

    function call(url, params, successCallback) {

        $.ajax({
            url: url,
            dataType: 'json',
            data: params,
            success: function (data) {

                console.log(data);
                if (data['statusCode'] == '200') {

                    toastr.success(data['message'], "Success");

                    if (successCallback) {

                        console.log("Success block executing");

                        successCallback();
                    }

                } else if (data['statusCode'] == '400') {

                    toastr.error(data['message'], "Error");
                }
            },
            error: function () {
                alert('error');
            }
        });
    }

    var contextPath = '<%=request.getContextPath() %>';
    var resendButton = $("#resendOTP");

    $("#sendVerificationMail").on("click", function () {

        var clientId = '<%=client.getId() %>';
        var email = '<%=client.getEmailId()%>';
        var url = contextPath + "/api/client.do";

        if (email.length == 0) {

            bootbox.alert("Invalid email address");
            return false;
        }

        var param = {};
        param['method'] = "sendVerificationMailTemporaryClient";
        param['id'] = clientId;
        param['email'] = email;

        call(url, param, null);
    });

    $("#sendVerificationSMS, #resendOTP").on("click", function () {

        var clientId = '<%=client.getId() %>';
        var phoneNo = '<%=client.getMobileNumber()%>';

        var otp = new OTP();

        otp.setClientID(clientId);
        otp.setPhoneNo(phoneNo);
        otp.setContextPath(contextPath);
        otp.setResendButton(resendButton);
        otp.setMethod("sendVerificationSMSTemporaryClient");

        otp.sendOTP();

    });


    //Handles OTP submit event. Verifies if user submitted OTP correctly
    $("#otpSubmit").on("click", function () {

        var clientId = '<%=client.getId() %>';
        var phoneNo = '<%=client.getMobileNumber()%>';
        var url = contextPath + "/api/client.do";
        var token = $("#otp").val();
        var form = $("#OTPForm");

        var otp = new OTP();

        otp.setClientID(clientId);
        otp.setPhoneNo(phoneNo);
        otp.setContextPath(contextPath);
        otp.setResendButton(resendButton);
        otp.setToken(token);
        otp.setOTPForm(form);
        otp.setMethod("verifySMS");
        otp.setPhoneVerifiedIcon($("#phoneVerified"));
        otp.setVerifyPhoneIcon($("#sendVerificationSMS"));

        otp.verifyOTP();

    });

</script>

