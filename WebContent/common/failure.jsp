<!DOCTYPE html>
<%@ page import="sessionmanager.SessionConstants" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html;charset=utf-8" %>

<%
	String context = "../../.."  + request.getContextPath() + "/";
    LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
    request.setAttribute("context", context);
    request.setAttribute("loginDTO",loginDTO);
    
%>
<%
String message = "";
HttpSession sess  = request.getSession(true);
message = (String)sess.getAttribute(SessionConstants.FAILURE_MESSAGE);


if(message == null)message = "";

%>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<html:base />
<title>BTCL |  Failure</title>
<%@ include file="../skeleton_btcl/head.jsp"%>
<script type="text/javascript">
 var context = '${context}';
</script>
</head>
<!-- END HEAD -->
<%
String  fullMenu="'"+  request.getAttribute("menu")+"','"+ request.getAttribute("subMenu1")+"','"+ request.getAttribute("subMenu2")+"'";
%>

<body class="page-container-bg-solid page-header-fixed page-sidebar-closed-hide-logo"  onload="activateMenu(<%=fullMenu%>)">
	<!-- BEGIN HEADER -->
	<div class="page-header navbar navbar-fixed-top">
		<!-- BEGIN HEADER INNER -->
		<%@ include file="../skeleton_btcl/header.jsp"%>
		<!-- END HEADER INNER -->
	</div>
	<!-- END HEADER -->
	<!-- BEGIN HEADER & CONTENT DIVIDER -->
	<div class="clearfix"></div>
	<!-- END HEADER & CONTENT DIVIDER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<%if(loginDTO.getUserID() > 0){%>
			<%@ include file="../skeleton_btcl/menu.jsp"%>
		<%}else{%>
		<%@ include file="../skeleton_btcl/menu_client.jsp"%>
		<%}%>
		<!-- END SIDEBAR -->
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<div class="page-content">
                    <!-- BEGIN PAGE CONTENT INNER -->
                    <div class="page-content-inner">
						<div class="row">
							<div class="portlet box red">
								<div class="portlet-title">
									<div class="caption">
										<i class="icon-shield"></i>Something wrong
									</div>
								</div>
								<div class="portlet-body">
									<div class="row">
										<h2><%=message %></h2>
									</div>
									<p>
										<%
											String referer=(String)request.getHeader("referer");
											if(null!=referer){
										%>
										<a class="btn btn-submit-btcl" href="<%=referer%>">Back to previous page</a>
										<%}else{ %>
											<a class="btn btn-submit-btcl" href="<%=context%>">Back to Home</a>
										<%} %>
									</p>
									<div class="note note-danger">
										<p> <c:out value="${failureMsg}"/>  </p>
									</div>
								</div>
							</div>
					
						</div>
                    </div>
                       <!-- END PAGE CONTENT INNER -->
				<!-- END PAGE BASE CONTENT -->
			</div>
			<!-- END CONTENT BODY -->
		</div>
		<!-- END CONTENT -->
		<div class="modal"></div>
	</div>

	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<%@ include file="../skeleton_btcl/footer.jsp"%>
	<!-- END FOOTER -->
	<%@ include file="../skeleton_btcl/includes.jsp"%>
</body>
</html>