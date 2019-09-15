<!DOCTYPE html>
<%@page import="util.Country2Phone"%>
<%@page import="config.GlobalConfigurationRepository"%>
<%@page import="java.util.Locale"%>
<%@page import="common.ClientConstants, common.CompanyTypeConstants"%>
<%@page import="util.SOP,org.apache.log4j.*"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ page import="sessionmanager.SessionConstants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html;charset=utf-8"%>

<%
	Logger logger=Logger.getLogger(getClass());
	String context = "../../.." + request.getContextPath() + "/";
	request.setAttribute("context", context);
%>
<html>
<head>
<html:base />
<title>BTCL | Payment Steps</title>
<%@ include file="../../skeleton_btcl/head.jsp"%>
<script type="text/javascript">
	var context = '${context}';
</script>
<!-- END HEAD -->
</head>
<body
	class="page-container-bg-solid page-header-fixed1 page-sidebar-closed-hide-logo">
	<!-- BEGIN HEADER -->
	<div class="page-header navbar navbar-fixed-top1 highlight-header">
		<%@ include file="../../skeleton_btcl/header2.jsp"%>
	</div>
	<!-- END HEADER -->
	<!-- BEGIN HEADER & CONTENT DIVIDER -->
	<div class="clearfix"></div>
	<!-- END HEADER & CONTENT DIVIDER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container highlight-header">
		<!-- BEGIN SIDEBAR -->
		<!-- END SIDEBAR -->
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<div class="page-content highlight-header">
				<!-- Start Inner -->
					<!-- <div class="box-body">			 -->
					<div class="portlet  box portlet-btcl">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa  fa-money "></i>Payment Steps
							</div>
						</div>
						<!-- /.box-header -->
						<div class="portlet-body form">
							<div class="row">
                        <div class="col-md-12">
                            <div class="portlet light portlet-fit bordered">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class=" icon-layers font-green"></i>
                                        <span class="caption-subject font-green bold uppercase">Payment Steps using Teletalk</span>
                                    </div>
                                </div>
                               <div class="portlet-body">
                                    <div class="mt-element-step">
                                        <div class="row step-default">
                                            <div class="col-md-4 bg-grey mt-step-col ">
                                                <div class="mt-step-number bg-white font-grey ">1</div>
                                                <div class="mt-step-title uppercase font-grey-cascade">Recharge</div>
                                                <div class="mt-step-content font-grey-cascade">Make sure whether you have sufficient balance in your Prepaid Teletalk Mobile.  <br> <br></div>
                                            </div>
                                            <div class="col-md-4 bg-grey mt-step-col active">
                                                <div class="mt-step-number bg-white font-grey">2</div>
                                                <div class="mt-step-title uppercase font-grey-cascade">SMS & PIN</div>
                                                <div class="mt-step-content font-grey-cascade">Go to your SMS option and  type <b> BTCL&ltspace&gtINVOICE_ID </b>. Send to <b>16222</b>. <br>You will get a <b>PIN_NUMBER</b> in return message.</div>
                                            </div>
                                            <div class="col-md-4 bg-grey mt-step-col error">
                                                <div class="mt-step-number bg-white font-grey">3</div>
                                                <div class="mt-step-title uppercase font-grey-cascade">Payment</div>
                                                <div class="mt-step-content font-grey-cascade">To confirm payment, In SMS type <b> BTCL&ltspace&gtYES&ltspace&gtPIN_NUMBER</b> <br><br></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
						</div>
					</div>
					<!-- /.box-body -->
					<!-- /.box-footer -->
				<!-- End Inner-->
			</div>
		</div>
		<!-- END CONTENT -->
	</div>
		<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<%@ include file="../../skeleton_btcl/footer.jsp"%>
	<!-- END FOOTER -->
	<%@ include file="../../skeleton_btcl/includes.jsp"%>
</body>
</html>