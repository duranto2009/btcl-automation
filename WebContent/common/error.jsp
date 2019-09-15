<%@ include file="../includes/checkLogin.jsp"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="sessionmanager.SessionConstants" %>
<%@ page isErrorPage="true" %>

<%
String message = "";
HttpSession sess  = request.getSession(true);
message = (String)sess.getAttribute(SessionConstants.FAILURE_MESSAGE);

if(message == null)
{
	message = "";
	if(exception != null){
		message = exception.getMessage();
	}
}else{
	sess.removeAttribute( SessionConstants.FAILURE_MESSAGE );
}

//System.out.println("Exception: " + exception.getMessage() );

if(message == null)message = "";

%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
    <!--<![endif]-->
    <!-- BEGIN HEAD -->
    <head>
       <html:base />
       <title>Failure..</title>
      <%@ include file="../skeleton/head.jsp"%>	
         </head>
    <!-- END HEAD -->

    <body class="page-container-bg-solid page-boxed">
        <!-- BEGIN HEADER -->
        <div class="page-header">
            <!-- BEGIN HEADER TOP -->
           <%@ include file="../skeleton/header.jsp"%>	
            <!-- END HEADER TOP -->
            <!-- BEGIN HEADER MENU -->
             <%@ include file="../skeleton/menu.jsp"%>	
            <!-- END HEADER MENU -->
        </div>
        <!-- END HEADER -->
        <!-- BEGIN CONTAINER -->
        <div class="page-container">
            <!-- BEGIN CONTENT -->
            <div class="page-content-wrapper">
                <!-- BEGIN CONTENT BODY -->
                <!-- BEGIN PAGE CONTENT BODY -->
                <div class="page-content">
                    <div class="container">
                        <!-- BEGIN PAGE BREADCRUMBS -->
                        <ul class="page-breadcrumb breadcrumb">
                            <li>
                                <a href="<%=request.getContextPath()%>">Home</a>
                                <i class="fa fa-circle"></i>
                            </li>
                            <li>
                                <span>Failure</span>
                            </li>
                        </ul>
                        <!-- END PAGE BREADCRUMBS -->
                        <!-- BEGIN PAGE CONTENT INNER -->
                        <div class="page-content-inner">
							<div class="row">
								<div class="portlet box red">
									<div class="portlet-title">
										<div class="caption">
											<i class="fa fa-gift"></i>Failure!!!
										</div>
									</div>
									<div class="portlet-body">
										<div class="note note-danger">
											 <table style = "text-align:left;margin-left: auto;margin-right: auto;" id="error_table">
					                            <tr>
					                                <td class="colLabel">Status Code:</td>
					                                <td class="colValue"><%=request.getAttribute("statusCode") %></td>
					                            </tr>
					                            <tr>
					                                <td class="colLabel">Servlet Name:</td>
					                                <td class="colValue"><%=request.getAttribute("servletName") %></td>
					                            </tr>
					                            <tr>
					                                <td class="colLabel">The request URI:</td>
					                                <td class="colValue"><%=request.getAttribute("reqUri") %></td>
					                            </tr> 
					                            <tr id="exceptionMessage">
					                                <td class="colLabel">The Exception Message:</td>
					                                <td class="colValue" style="color: red;">
					                                	<%=request.getAttribute("excpMsg") %><br>
					                                	<%=request.getAttribute("excepDesc") %>
					                                </td>
					                            </tr>
					                        </table>    
										</div>
									</div>
								</div>
						
							</div>
                        </div>
                        <!-- END PAGE CONTENT INNER -->
                    </div>
                </div>
                <!-- END PAGE CONTENT BODY -->
                <!-- END CONTENT BODY -->
            </div>
            <!-- END CONTENT -->
        </div>
        <!-- END CONTAINER -->
        <!-- BEGIN FOOTER -->
       	<%@ include file="../skeleton/footer.jsp"%>
        <!-- END FOOTER -->
       <%@ include file="../skeleton/includes.jsp"%>
    </body>

</html>