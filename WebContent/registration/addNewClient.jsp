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

<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
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
<html:base />
<title>BTCL | Create New Client</title>
<%@ include file="../skeleton_btcl/head.jsp"%>
<link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
<link href="${context}assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
<link href="${context}assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet" type="text/css" />
<link href="${context}assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />

<%
	String[] cssStr = request.getParameterValues("css");
	for (int i = 0; ArrayUtils.isNotEmpty(cssStr) && i < cssStr.length; i++) {
%>
<link href="${context}<%=cssStr[i]%>" rel="stylesheet" type="text/css" />
<%
	}
%>

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
	.modal-overflow .modal-body {
	    max-height: 400px !important;
	    overflow-y: scroll;
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
	<!-- BEGIN HEADER -->
	<div class="page-header navbar navbar-fixed-top1 highlight-header">
		<!-- BEGIN HEADER INNER -->
		<%@ include file="../../skeleton_btcl/header2.jsp"%>
		<!-- END HEADER INNER -->
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
				<jsp:include page='../common/flushActionStatus.jsp' />
				<div class="portlet  box portlet-btcl">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa  fa-sign-in "></i>Client Registration
						</div>
						<div class="actions">
							<a href="<%=context%>" class="btn btn-warning-btcl" style="padding-left: 15px; padding-right: 15px;"> Login </a>
						</div>
					</div>
					<!-- /.box-header -->
					<div class="portlet-body form">
						<html:form styleId="fileupload" styleClass="form-horizontal" action="<%=actionName%>" enctype="multipart/form-data" method="POST">
							<html:hidden property="clientDetailsDTO.moduleIDs" styleId="selectedModuleID" value=""/>
							<div class="form-body">
								<div class="alert alert-danger display-hide">
									<button class="close" data-close="alert"></button>
									You have some form errors. Please check below.
								</div>
								<div class="alert alert-success display-hide">
									<button class="close" data-close="alert"></button>
									Your form validation is successful!
								</div>
								<div id="regIndividualInfo" class="hidden">
									<h3 class="form-section">Personal Info</h3>
									<html:hidden property="clientDetailsDTO.registrantType" value="0"></html:hidden>
									<html:hidden property="clientDetailsDTO.regiCat" value="0"></html:hidden>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">First Name<span class="required" aria-required="true"> * </span></label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.registrantsName" styleClass="form-control"></html:text>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group ">
												<label class="control-label col-md-3">Last Name</label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.registrantsLastName" styleClass="form-control"></html:text>
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<!--/row-->
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Father Name</label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.fatherName" styleClass="form-control"></html:text>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group ">
												<label class="control-label col-md-3">Mother Name</label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.motherName" styleClass="form-control"></html:text>
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Gender</label>
												<div class="col-md-9">
													<html:select property="registrantContactDetails.gender" styleClass="form-control">
														<option value="male">Male</option>
														<option value="female">Female</option>
													</html:select>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Email <span class="required" aria-required="true"> * </span></label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.email" styleClass="form-control"></html:text>
												</div>
												<!--/span-->
											</div>
										</div>
									</div>
									
									
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Mobile Number
													<span class="required" aria-required="true"> * </span>
												</label>
											 	<div class="col-md-9">
													<input name="intlMobileNumber" class="phoneNumber form-control" type="tel"> 
													<span  class="hide valid-msg"> Mobile number is valid</span> 
													<span  class="hide error-msg"> Mobile number is invalid </span>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Fax </label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.faxNumber" value="" styleClass="form-control">
													</html:text>
												</div>
												<!--/span-->
											</div>
										</div>
										<!--/span-->
									</div>
									<!--/row-->
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Date of Birth
													<span class="required" aria-required="true"> * </span>
												</label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.dateOfBirth" styleClass="form-control datepicker"></html:text>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Occupation</label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.occupation" value="" styleClass="form-control">
													</html:text>
												</div>
											</div>
										</div>
									</div>
									<!--/row-->
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Select <span class="required" aria-required="true"> * </span></label>
												<div class="col-md-9">
													<div class="radio-list">
														<label class="radio-inline">
															<input type="radio"
															data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_NID)%>"
															name="clientDetailsDTO.identityType"
															value="<%=FileTypeConstants.CLIENT.COMMON_NID%>"
															class="identityType identityTypeInd" checked="" /> <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_NID)%>
														</label> 
														<label class="radio-inline"> <input type="radio"
															data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_PASSPORT)%>"
															name="clientDetailsDTO.identityType"
															value="<%=FileTypeConstants.CLIENT.COMMON_PASSPORT%>"
															class="identityType identityTypeInd" /> <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_PASSPORT)%>
														</label>
													</div>
												</div>
												<!--/span-->
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"> 
													<span class="identityLabel"></span> No
												 	<span class="required" aria-required="true"> * </span></label>
												<div class="col-md-9">
													<input type=text name='clientDetailsDTO.identity' class=form-control>
												</div>
												<!--/span-->
											</div>
										</div>
										<!--/span-->
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Upload <span class="identityLabel"></span> Documents
													<span class="required" aria-required="true"> * </span>
												</label>
												<div class="col-md-9" >
													<div class="fileinput fileinput-new" data-provides="fileinput">
		                                                    <div class="input-group input-large">
		                                                        <div class="form-control uneditable-input input-fixed input-medium" data-trigger="fileinput">
		                                                            <i class="fa fa-file fileinput-exists"></i>&nbsp;
		                                                            <span class="fileinput-filename"> </span>
		                                                        </div>
		                                                        <span class="input-group-addon btn default btn-file">
		                                                            <span class="fileinput-new"> Select file </span>
		                                                            <span class="fileinput-exists"> Change </span>
		                                                            <input name="document" type="file" accept='image/jpg, image/jpeg, image/gif,image/png,application/pdf' required> 
		                                                            <input id="documentSize" name="documentSize" type="hidden"> 
		                                                         </span>
		                                                        <a href="javascript:;" class="input-group-addon btn red fileinput-exists" data-dismiss="fileinput"> Remove </a>
		                                                    </div>
		                                                </div>
		                                                <span class="help-block">  PDF (.pdf) or  Image(.png, .jpg and .jpeg) file is allowed to upload  only</span>
												</div>
											</div>
										</div>
									</div>
									<!--/row-->
								</div>
								<div id="regCompanyInfo" class="hidden">
									<h3 class="form-section">Registrant Info</h3>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"><%=LanguageConstants.COMPANY %> Name <span
													class="required" aria-required="true"> * </span>
												</label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.registrantsName" styleClass="form-control"></html:text>
													<span class="help-block"> Name of the <%=LanguageConstants.COMPANY_TYPE %> </span>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Web Address</label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.webAddress" styleClass="form-control"></html:text>
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<!--/row-->
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"><%=LanguageConstants.COMPANY %> Type <span
													class="required" aria-required="true"> * </span>
												</label>
												<div class="col-md-9">
													<html:select property="clientDetailsDTO.registrantType" styleClass="form-control border-radius">
														<html:option value="0">Select</html:option>
														<%
															HashMap<Integer, String> companyMap = RegistrantTypeConstants.REGISTRANT_TYPE.get(ModuleConstants.Module_ID_VPN);
																	for (Integer key : companyMap.keySet()) {
														%>
														<option value="<%=key%>"><%=companyMap.get(key)%></option>
														<%
															}
														%>
													</html:select>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Email <span class="required" aria-required="true"> * </span></label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.email" styleClass="form-control"></html:text>
												</div>
												<!--/span-->
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Mobile Number
													<span class="required" aria-required="true"> * </span>
												</label>
												<div class="col-md-9">
													<input name="intlMobileNumber" class="phoneNumber form-control" type="tel"> 
													<span  class="hide valid-msg"> Mobile number is valid</span> 
													<span  class="hide error-msg"> Mobile number is invalid </span>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Fax </label>
												<div class="col-md-9">
													<html:text property="registrantContactDetails.faxNumber" styleClass="form-control"></html:text>
												</div>
												<!--/span-->
											</div>
										</div>
										<!--/span-->
									</div>
									
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Select <span
													class="required" aria-required="true"> * </span>
												</label>
												<div class="col-md-9">
													<div class="radio-list">
														<label class="radio-inline">
															<input type="radio"
																data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_TIN)%>"
																name="clientDetailsDTO.identityType"
																value="<%=FileTypeConstants.CLIENT.COMMON_TIN%>"
																class="identityType identityTypeCom" checked="">
																<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_TIN)%>
														</label> 
														<label class="radio-inline">
															<input type="radio" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_TRADE)%>"
																name="clientDetailsDTO.identityType"
																value="<%=FileTypeConstants.CLIENT.COMMON_TRADE%>"
																class="identityType identityTypeCom">
																<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_TRADE)%>
														</label>
														<label class="radio-inline">
															<input id="forwardingLetter" type="radio" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_FORWARDING_LETTER)%>"
																name="clientDetailsDTO.identityType"
																value="<%=FileTypeConstants.CLIENT.COMMON_FORWARDING_LETTER%>"
																class="identityType identityTypeCom">
																<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.CLIENT.COMMON_FORWARDING_LETTER)%>
														</label>
													</div>
												</div>
												<!--/span-->
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"> 
													<span class="identityLabel"></span> No <span class="required" aria-required="true"> * </span></label>
												<div class="col-md-9">
													<input type=text class=form-control name=clientDetailsDTO.identityNo>
												</div>
												<!--/span-->
											</div>
										</div>
										<!--/span-->

									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">Upload <span class="identityLabel"></span> Documents
													<span class="required" aria-required="true"> * </span>
												</label>
												<div class="col-md-9" >
													<div class="fileinput fileinput-new" data-provides="fileinput">
		                                                    <div class="input-group input-large">
		                                                        <div class="form-control uneditable-input input-fixed input-medium" data-trigger="fileinput">
		                                                            <i class="fa fa-file fileinput-exists"></i>&nbsp;
		                                                            <span class="fileinput-filename"> </span>
		                                                        </div>
		                                                        <span class="input-group-addon btn default btn-file">
		                                                            <span class="fileinput-new"> Select file </span>
		                                                            <span class="fileinput-exists"> Change </span>
		                                                            <input name="document" type="file" accept='image/jpg, image/jpeg, image/gif,image/png,application/pdf' required> 
		                                                            <input id="documentSize" name="documentSize" type="hidden"> 
		                                                         </span>
		                                                        <a href="javascript:;" class="input-group-addon btn red fileinput-exists" data-dismiss="fileinput"> Remove </a>
		                                                    </div>
		                                                </div>
		                                                <span class="help-block">  PDF (.pdf) or  Image(.png, .jpg and .jpeg) file is allowed to upload  only</span>
												</div>
											</div>
										</div>
									</div>
									<!--/row-->
								</div>
								<h3 class="form-section">Registrant Address</h3>
								<!--/row-->
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">Country <span class="required" aria-required="true"> * </span></label>
											<div class="col-md-9">
												<html:select styleClass="form-control" property="registrantContactDetails.country">
													<%
														String[] locales = Locale.getISOCountries();
															for (String countryCode : locales) {
																Locale obj = new Locale("", countryCode);
															%>
															<html:option value="<%=obj.getCountry()%>"><%=obj.getDisplayCountry()%></html:option>
															<%
														}
													%>
												</html:select>
											</div>
										</div>
									</div>
									<!--/span-->
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">City <span class="required" aria-required="true"> * </span>
											</label>
											<div class="col-md-9">
												<html:text property="registrantContactDetails.city" styleClass="form-control "></html:text>
											</div>
											<!--/span-->
										</div>
									</div>
									<!--/span-->
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">Post Code </label>
											<div class="col-md-9">
												<html:text property="registrantContactDetails.postCode" styleClass="form-control"></html:text>
											</div>
											<!--/span-->
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">Address <span class="required" aria-required="true"> * </span></label>
											<div class="col-md-9">
												<html:textarea property="registrantContactDetails.address" styleClass="form-control border-radius textarea regi"></html:textarea>
											</div>
										</div>
									</div>
								</div>
								<h3 class="form-section">Login Info</h3>
								<!--/row-->
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">Username <span class="required" aria-required="true"> * </span></label>
											<div class="col-md-9">
												<input name="clientDetailsDTO.loginName"  class="form-control" type="text" autocomplete="off">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">Password <span class="required" aria-required="true"> * </span></label>
											<div class="col-md-9">
												<input name="clientDetailsDTO.loginPassword" id="password" class="form-control" type="password" autocomplete="off">
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">Captcha <span class="required" aria-required="true"> * </span></label>
											<div class="col-md-9 no-margin-padding" >
												<div class="col-xs-8 col-md-5" >
													<img class="img-thumbnail" style="width: 100%; max-height: 42px; padding: 2px;" id="captcha" src="<%=request.getContextPath()%>/simpleCaptcha.jpg" alt="loading captcha..."> 
												</div>
												<div class="col-xs-4 col-md-2" style="line-height: 43px;">
													 <i id="reloadCaptcha" title="Refresh Captcha" class="fa fa-refresh" aria-hidden="true"></i>
												</div>
												<div class="col-xs-12 col-md-5">
													<input class="form-control" type="text" name="answer" />
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"> Confirm Password  <span class="required" aria-required="true"> * </span>
											</label>
											<div class="col-md-9">
												<input name="confirmLoginPassword" id="cPassword" class="form-control" type="password" autocomplete="off">
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row" style="display: none;">
								<div class="col-md-4 col-sm-6">
									<%@include file="../vpn/client/clientAddBody_billingContact.jsp"%>
								</div>

								<div class="col-md-4 col-sm-6">
									<%@include file="../vpn/client/clientAddBody_AdminContact.jsp"%>
								</div>

								<div class="col-md-4 col-sm-6">
									<%@include file="../vpn/client/clientAddBody_TechnicalContact.jsp"%>
								</div>
							</div>
							
							<div class="form-actions">
								<div class="row">
									<div class="col-md-offset-5 col-md-7">
										<input type="hidden" name="csrfPreventionSalt" value="${csrfPreventionSalt}"/>
										<html:hidden property="newClient" value="true" />
										<html:hidden property="clientDetailsDTO.clientCategoryType" styleId="clientCategoryType"/>
										
										<html:hidden property="registrantContactDetails.detailsType" value="<%=new Integer(ClientConstants.DETAILS_TYPE_REGISTRANT).toString()%>" />
										<html:hidden property="billingContactDetails.detailsType" value="<%=new Integer(ClientConstants.DETAILS_TYPE_BILLING).toString()%>" /> 
										<html:hidden property="adminContactDetails.detailsType" value="<%=new Integer(ClientConstants.DETAILS_TYPE_ADMIN).toString()%>" />
										<html:hidden property="technicalContactDetails.detailsType" value="<%=new Integer(ClientConstants.DETAILS_TYPE_TECHNICAL).toString()%>" />
	                         			<html:hidden property="registrantContactDetails.phoneNumber"/>
	                         			
	                         			<input type="hidden" value="-1" name="clientDetailsDTO.existingClientID">	 
	
										<button type="reset" class="btn btn-reset-btcl">Reset</button>
										<button type="submit" class="btn btn-submit-btcl">Register</button>
									</div>
								</div>
							</div>
						</html:form>
					</div>
				</div>
				<!-- /.box-body -->

				<!-- /.box-footer -->

				<!-- End Inner-->
			</div>
		</div>
		<!-- END CONTENT -->
		<!-- Modal -->
		<div id="static2" class="modal container fade" tabindex="-1" data-backdrop="static" data-keyboard="false" data-attention-animation="false">
			<div class="modal-header">
				<h2 class="text-center">BTCL Client Registration</h2>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="form-group ">
						<label class="control-label col-md-2 bold"> Service Type <span class="required" aria-required="true"> * </span></label>
						<div class="col-md-10">
							<div class="radio-list">
							<%
							List<ObjectPair<Integer, String>> activeModuleMap = moduleService.getActiceModuleList();
							for(ObjectPair<Integer, String> module : activeModuleMap) {%>
								<label class="radio-inline"> 
								<input type="radio" name="moduleID" value="<%=module.key%>" <%=(module.key==1)?"checked":""%>/> <%=module.value%></label>
							<%
							}
							%>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="form-group ">
						<label class="control-label col-md-2 bold"> Registrant Type <span class="required" aria-required="true"> * </span></label>
						<div class="col-md-10">
							<div class="radio-list">
								<label class="radio-inline account indAccount"> 
									<input type="radio" name="accountType" value="1" autocomplete="off" checked />Individual
								</label>
								<label class="radio-inline account comAccount">
									<input type="radio" name="accountType" value="2" autocomplete="off" /><%=LanguageConstants.COMPANY_TYPE %>
								</label>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h4>Terms & Conditions and Privacy Policy</h4>
				<hr>
				<div>
					Please read our terms and conditions 
					<a id="domainTerms" class="terms" target="_blank" href="terms-and-conditions.jsp">from here.</a>
					<a id="vpnTerms" style="display: none" class="terms " target="_blank" href="${context}assets/static/VPN_TERMS.pdf">from here.</a>
				</div>
			</div>
			<div class="modal-footer">
				<div class="row">
					<div class="col-md-8 text-left">
						<input type="checkbox" name="checkbox" value="check" id="regAgree" />
						<label for="regAgree">I have read and agree to the Terms and Conditions and Privacy Policy</label>
					</div>
					<div class="col-md-4 ">
						<button type="button" class="btn btn-cancel-btcl" id="regBack">Back</button>
						<button type="button" class="btn btn-submit-btcl" id="regContinue">Continue</button>
					</div>
				</div>

			</div>
		</div>
	</div>
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<%@ include file="../skeleton_btcl/footer.jsp"%>
	<!-- END FOOTER -->
	<%@ include file="../skeleton_btcl/includes.jsp"%>
	<%@ include file="../../common/mobileNumberHelper.jsp"%>

	<%
		String[] jsStr = request.getParameterValues("js");
		for (int i = 0; ArrayUtils.isNotEmpty(jsStr) && i < jsStr.length; i++) {
	%>
	<script src="${context}<%=jsStr[i]%>" type="text/javascript"></script>
	<%
		}
	%>
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="${context}assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
	<script src="${context}assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
	<script src="${context}assets/global/plugins/moment.min.js" type="text/javascript"></script>
	<script src="${context}assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>


	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN THEME GLOBAL SCRIPTS -->
	<script src="${context}assets/global/scripts/app.min.js" type="text/javascript"></script>
	<script src="${context}assets/scripts/client/client-add-validation.js" type="text/javascript"></script>
	<script src="${context}assets/scripts/client/client-guidelines.js" type="text/javascript"></script>
	<script type="text/javascript">
	    $(document).ready(function(){
			
			var accountType="<%=ClientForm.CLIENT_TYPE_INDIVIDUAL%>";
			var countryDefault="BD";

			$("input[name='accountType']").change(function() {
				accountType=$(this).val();
				$("#clientCategoryType").val(accountType);
			})
			$("input[name=moduleID]").change(function() {
				//alert($("input[name=accountType]:checked").val());
				$('.terms').hide();
				$("#selectedModuleID").val($(this).val());
				if($(this).val()==CONFIG.get('module','vpn')){
					$('.account.indAccount').hide();
					//$('.account.comAccount').parent().addClass('checked');
					$('#vpnTerms').show();
				}else{
					$('.account').show();
					$('#domainTerms').show();
				}
				$("input[name='accountType']").attr("checked", false);
				$("input[name='accountType']").parent('span').removeClass('checked');
				
				
			});
			
			$('#regBack').click(function() {
				window.location = context;
			});

			function showAccountInfo(type) {
				if (type == 1) {
					$("#regIndividualInfo").removeClass('hidden');
					$("#regCompanyInfo").remove();

					$(".identityTypeInd").eq(0).prop('checked', true);
					$.uniform.update(".identityTypeInd");

				} else {
					$("#regCompanyInfo").removeClass('hidden');
					$("#regIndividualInfo").remove();

					$(".identityTypeCom").eq(0).prop('checked', true);
					$.uniform.update(".identityTypeCom");
				}
				$('#forwardingLetter').closest('.radio-inline').hide();
			}
			$('#regContinue').click(function() {
				$("#clientCategoryType").val(accountType);
				console.log(accountType);
				if(typeof $("input[name=accountType]:checked").val() =='undefined'){
					toastr.error('Please Select Client Type');
					return;
				}
				if ($("#regAgree:checked").val()) {
					$('#static2').modal('hide');
					showAccountInfo(accountType);
				} else {
					toastr.error("Please read all the terms and conditions");
				}
			});

			$('.identityType').change(function() {
				var label=$(this).closest('label').text();
				$('.identityLabel').html(label);
				$('input[name="clientDetailsDTO.identityNo"]').val('');
			});

			if ($("#clientCategoryType").val() == 0) {
				$('#static2').modal('show');
			} else {
				showAccountInfo($("#clientCategoryType").val());
			}

			function init() {
				$('.datepicker').datepicker({
					orientation : "bottom",
					autoclose : true
				});

				var countrySelector=$('select[name="registrantContactDetails.country"]')
				countrySelector.val(countryDefault);
				sortSelectBox(countrySelector);

				$(".bill").removeClass('bill-required');
				$(".admin").removeClass('admin-required');
				$(".technical").removeClass('technical-required');
			}
			$("#reloadCaptcha").click(function() {
				$("#captcha").attr('src', $("#captcha").attr('src') + '?' + Math.random());
			});
			
			$('input[name=document]').bind('change', function() {
			  	//this.files[0].size gets the size of your file.
			  	$('#documentSize').val(this.files[0].size);
			  	//alert(this.files[0].size);
			});

			init();
			
		});
	</script>
	
	<script>
	//d
	$(document).ready(function(){
		$("#fileupload").on("submit", function(event){
			event.preventDefault();
			
			var inputsModuleIDs = $("input[type=radio][name=moduleID]");
			$.each(inputsModuleIDs, function(index, value){
				if($(this).parent().hasClass("checked")){
					$("#selectedModuleID").val($(this).val());
				}
			});
			
			$(this).unbind('submit').submit();
		});
	});
				
	</script>
</body>
</html>