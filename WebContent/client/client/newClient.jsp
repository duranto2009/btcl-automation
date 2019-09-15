<!DOCTYPE html>
<%@page import="common.ObjectPair"%>
<%@page import="java.util.List"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="module.ModuleService"%>
<%@page import="common.LanguageConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="util.Country2Phone"%>
<%@page import="config.GlobalConfigurationRepository"%>
<%@page import="java.util.Locale"%>
<%@page import="common.ClientConstants"%>
<%@page import="util.SOP,org.apache.log4j.*"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="sessionmanager.SessionConstants"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@page contentType="text/html;charset=utf-8"%>

<%
	Logger logger = Logger.getLogger(getClass());
	String context = "../../.." + request.getContextPath() + "/";
	request.setAttribute("context", context);

	String actionName = "/AddClient";
	ModuleService moduleService = ServiceDAOFactory.getService(ModuleService.class);
%>
<html>
<head>
<title>BTCL | Create New Client</title>
<%@ include file="../../skeleton_btcl/head.jsp"%>
<link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
<link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
<link href="${context}assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet" type="text/css" />
<link href="${context}assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />

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
		.modal-body, .modal-overflow .modal-body{
		  	max-height: 250px !important;
	    	overflow-y: scroll;
	    	overflow: auto !important;
		}
	}
</style>
</head>
<body
	class="page-container-bg-solid page-header-fixed1 page-sidebar-closed-hide-logo">
	<div class="page-header navbar navbar-fixed-top1 highlight-header">
		<%@ include file="../../skeleton_btcl/header2.jsp"%>
	</div>
	<div class="clearfix"></div>
	<div class="page-container highlight-header">
		<div class="page-content-wrapper">
			<div class="page-content highlight-header">
				<jsp:include page='../../common/flushActionStatus.jsp' />
				<div class="portlet  box portlet-btcl">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa  fa-sign-in "></i>Client Registration
						</div>
					</div>
					<div class="portlet-body form" id="regHolder">
						<%@ include file="newClientBody_Step1.jsp"%>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="../../skeleton_btcl/footer.jsp"%>
	<%@ include file="../../skeleton_btcl/includes.jsp"%>
	

	<script src="${context}assets/global/plugins/moment.min.js" type="text/javascript"></script>
	<script src="${context}assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>

	<script src="${context}assets/global/scripts/app.min.js" type="text/javascript"></script>
	<script src="${context}assets/scripts/client/client-add-validation.js" type="text/javascript"></script>
</body>
</html>


<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/js/utils.js"></script>



