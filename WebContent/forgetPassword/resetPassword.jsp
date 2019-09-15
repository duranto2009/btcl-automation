<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	/* Message from Alam
	*********************************************************************************
	Question may arise what if user access this page directly without access token	
	Answer is it will be blocked by LoginFilter
	User can only access this page through ForgetPasword action as that action is ignored by filter
	***********************************************************************************/

	String loginURL = request.getRequestURL().toString();
	String context = request.getContextPath() + "/";
%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
<meta charset="utf-8" />
<title>Welcome to BTCL Bangladesh Limited | Reset Password</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link
	href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/simple-line-icons/simple-line-icons.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/uniform/css/uniform.default.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css"
	rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/select2/css/select2.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/select2/css/select2-bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN THEME GLOBAL STYLES -->
<link
	href="<%=request.getContextPath()%>/assets/global/css/components.min.css"
	rel="stylesheet" id="style_components" type="text/css" />
<link
	href="<%=request.getContextPath()%>/assets/global/css/plugins.min.css"
	rel="stylesheet" type="text/css" />
<!-- END THEME GLOBAL STYLES -->

<!-- BEGIN PAGE LEVEL STYLES -->
<link href="<%=request.getContextPath()%>/assets/pages/css/login.css"
	rel="stylesheet" type="text/css" />

<link href="<%=request.getContextPath() %>/assets/pages/css/profile.min.css"

<!-- END PAGE LEVEL STYLES -->

<link rel="shortcut icon" href="favicon.ico" />
<style type="text/css">
.login {
	background-color: #fff !important;
}

.login .copyright {
	color: black !important;
}

.login .content h3 {
	color: black !important;
}
</style>

</head>
<!-- END HEAD -->

<body class=" login">

	<div class="menu-toggler sidebar-toggler"></div>
	<!-- END SIDEBAR TOGGLER BUTTON -->

	<!-- BEGIN LOGO -->
	<div class="logo">
		<a href="<%=request.getContextPath()%>"> <img
			src="<%=request.getContextPath()%>/assets/images/company-name.png"
			alt="" />
		</a>
	</div>
	<!-- END LOGO -->

	<!-- BEGIN LOGIN -->
	<div class="content">

		<form action="<%=request.getContextPath() %>/ForgetPassword.do">

			<h3 class="form-title font-purple">Reset Password</h3>
			<div class="note note-warning custom-alert">
				<p>Password length should be minimum 4 with a digit and letter</p>
			</div>

			<input type="hidden" name="method" value="resetPassword" /> 
			
			<input type="hidden" name="formSubmitted" value="" />
			
			<input type="hidden" name="identifierGiven" value="<%=request.getParameter( "identifierGiven" ) %>" />
			<input type="hidden" name="username" value="<%=request.getParameter("username")%>" />
			<input type="hidden" name="token" value="<%=request.getParameter("token")%>" />

			<!-- This will show error or success message  -->
			<jsp:include page='../common/flushActionStatus.jsp' />

			<div class="form-group">
				<label class="control-label">New Password <span class="required"> * </span> </label>
				
				<input name="password" type="password" id="password" class="form-control" />

				<div style="margin-top: 5px;" class="pwstrength_viewport_progress"></div>
			</div>
			
			<div class="form-group">
				<label class="control-label">Confirm New Password <span class="required"> * </span></label>
				
				<input name="rePassword" type="password" class="form-control" />
			</div>

			<div class="margin-top-10">
				<input name="type" value="password" type="hidden">
				<input type="reset" class="btn btn-danger" value="Reset"> 
				<input type="submit" class="btn green-jungle uppercase" value=" Change Password" />
			</div>
			
		</form>
	</div>

	<div class="copyright">2016 @ BTCL Bangladesh Limited</div>
	<!--[if lt IE 9]>
			<script src="<%=request.getContextPath()%>/assetsglobal/plugins/respond.min.js"></script>
			<script src="<%=request.getContextPath()%>/assetsglobal/plugins/excanvas.min.js"></script> 
		<![endif]-->
	<!-- BEGIN CORE PLUGINS -->

	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/jquery.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/bootstrap/js/bootstrap.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/js.cookie.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/jquery.blockui.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/uniform/jquery.uniform.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js"
		type="text/javascript"></script>
	<!-- END CORE PLUGINS -->

	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/jquery-validation/js/jquery.validate.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/jquery-validation/js/additional-methods.min.js"
		type="text/javascript"></script>
	<script
		src="<%=request.getContextPath()%>/assets/global/plugins/select2/js/select2.full.min.js"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL PLUGINS -->

	<!-- BEGIN THEME GLOBAL SCRIPTS -->
	<script
		src="<%=request.getContextPath()%>/assets/global/scripts/app.min.js"
		type="text/javascript"></script>
	<!-- END THEME GLOBAL SCRIPTS -->

	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script
		src="<%=request.getContextPath()%>/assets/pages/scripts/login.min.js"
		type="text/javascript"></script>
		
	<script src="<%=request.getContextPath() %>/assets/global/plugins/jquery.sparkline.min.js" type="text/javascript"/>
	<script src="<%=request.getContextPath() %>/assets/pages/scripts/profile.min.js" type="text/javascript"/>
	<script src="<%=request.getContextPath() %>/assets/global/plugins/pwstrength.js" type="text/javascript"/>
	
	
	<!-- END PAGE LEVEL SCRIPTS -->

	<script type="text/javascript">
		$(document).ready(function() {
			 "use strict";
			var options = {};
			
			options.ui = {
			   	container: "#pwd-container",
			   	showVerdictsInsideProgressBar: true,
			   	showStatus: true,
			   	viewports: {
			   	    progress: ".pwstrength_viewport_progress"
			   	},
			   	progressBarExtraCssClasses: "progress-bar-striped active",
			   	showPopover: true,
				showErrors: true,
				showProgressBar: true
			};
			
			options.rules = {
				activated: {
				   wordTwoCharacterClasses: true,
				   wordRepetitions: true
				}
			};
			
			$('#password').pwstrength(options);
		})
		</script>
</body>

</html>