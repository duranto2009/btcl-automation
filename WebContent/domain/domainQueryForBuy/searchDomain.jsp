<!DOCTYPE html>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="java.util.List"%>
<%@page import="vpn.client.ClientDAO"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="common.CommonDAO"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="domain.DomainUtil"%>
<%@page import="login.LoginDTO"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="domain.DomainDTO"%>
<%@page import="domain.DomainService"%>
<%@page import="java.util.Locale"%>
<%@page import="common.ClientConstants"%>
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
	Logger logger=Logger.getLogger(this.getClass());
	String context = "../../.." + request.getContextPath() + "/";
	request.setAttribute("context", context);
	
	//String actionName = "/AddClient";
	DomainService domainService = new DomainService();
	DomainDTO domainDTO = (DomainDTO)request.getAttribute("domainDTO");
	String domainAddress = "";
	String domainExt="";
	String secondLevelDom="";
	String domainName="";
	if(domainDTO != null)
	{
		domainAddress = domainDTO.getDomainAddress();
	}else if(domainDTO == null && request.getAttribute("domainName") != null){
		domainAddress = (String)request.getAttribute("domainName");
	}
	
	domainName=DomainUtil.getDomainName(domainAddress);
	domainExt=DomainUtil.getTopLevelDomain(domainAddress);
	secondLevelDom=DomainUtil.getSecondLevelDomain(domainAddress);
	domainName+=(StringUtils.isBlank(secondLevelDom))?"":("."+secondLevelDom);
	if(StringUtils.isBlank(domainAddress)){
		domainExt=DomainDTO.TLD_TYPE_BD;
	}else{
		domainExt="."+domainExt;
	}
	
	//Boolean isFileRequired = (Boolean) request.getAttribute("isFileRequired");
	String searchResult = (String) request.getAttribute("searchResult");
%>
<html>
<head>
<html:base />
<title>BTCL | Domain Checker</title>
<%@ include file="../../skeleton_btcl/head.jsp"%>
 <link href="${context}assets/styles/no-login.css" rel="stylesheet" type="text/css" />
 <link href="domain-query-buy.css" rel="stylesheet" type="text/css"/>

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
	<div class="page-container highlight-header highlight-header">
		<!-- BEGIN SIDEBAR -->

		<!-- END SIDEBAR -->
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<div class="page-content">
				<!-- Start Inner -->
					<!-- <div class="box-body">			 -->
					<div class="portlet  box portlet-btcl">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-search-plus" aria-hidden="true"></i> Domain Checker
							</div>
							<div class="actions">
								<a href="<%=request.getContextPath() %>/" class="btn btn-warning-btcl" style="padding-left: 15px; padding-right: 15px; "> Home </a>
		 					</div>
						</div>
						<!-- /.box-header -->
						<div class="portlet-body" style="padding-top: 30px;">
							<!-- Domain Checker Body -->
							<div class="row">
								<!-- Domain Search -->
									<div class="search-bar bordered">
											<div class="col-md-offset-2 col-md-8" style=" padding-top:20px;padding-bottom:20px;">
												<form method="post" action="../../DomainChecker.do?mode=checkDomain" id="submit_for_check">
													<input type="hidden" name="csrfPreventionSalt" value="${csrfPreventionSalt}"/>
													<div class="note note-info " >
				                 						<div class="domain-validation-message has-error" style="position: relative;top:0px;">
				                 							Please write a domain name without www.	
				                 	 					</div>
				            					 	</div>
													<div class="input-group  my-group">
														<input class="form-control" id="domainName" type="text" name="domainName" value="<%=domainName%>"style="width: 80%;"
															data-placement="bottom" data-toggle="popover" data-container="body" data-html="true"
															placeholder="Search for Domain" title="Please use bangla keyboard and write a domain name without www.">
															<select id="domainExt" name="domainExt" style="width: 20%;" class="selectpicker form-control" data-live-search="true" title="Please select extension">
																<option value="1" <%if(domainExt.equals(DomainDTO.TLD_TYPE_BD)){ %> selected <%} %> >.bd</option>
																<option value="2" <%if(domainExt.equals(DomainDTO.TLD_TYPE_BANGLA)){ %> selected <%} %>>.বাংলা</option>
															</select> 
															<span class="input-group-btn">
															<button class="btn blue uppercase bold  my-group-button searchRequest" id="checkDomain" type="submit">Search</button>
														</span>
													</div>
												</form>
												<br>
												<hr>
											</div>
											<br>
											<br>
									</div>
								<div id="popover-content" class="hide" class="col-md-12 bangla-keyboard">
									<%@ include file="../../keyboard/amarBangla.jsp"%>
								</div>
							</div>
							<!-- ./Domain Checker Body -->
							<%if(domainDTO!=null){ %>
								<div class="row">
									<div class="col-md-offset-1 col-md-10">
										<%if(domainDTO.isAvailable()){ %>
										<div style="padding-top: 5px; ">
											<div class="note note-success" style="text-align: center;">
												<h4 class="block">Congratulation! <strong><%=domainAddress %></strong> domain is available.</h4>
											</div>
											<div style="text-align: center;padding-top:10px;padding-bottom:20px;">
												<h4 class="block">Please <strong><a href="<%=request.getContextPath() %>/">Login</a></strong> or <strong><a href="<%=request.getContextPath() %>/registration/addNewClient.jsp">Sign up</a></strong> to continue</h4>
											</div>
										</div>
										<%}else if(!domainDTO.isAvailable()){ %>
											<div style="padding-top: 5px;">
												<div class="note note-danger" style="text-align: center;">
													<h4 class="block"><%=request.getAttribute("message") %>
												</h4>
												</div>
											</div>
										<%} %>	
										<%if(domainDTO!=null && domainDTO.getDomainClientID()>0){ %>
										 <%
										 CommonDAO commonDAO = new CommonDAO();
										 ClientService clientService= new ClientService();
										 List<ClientContactDetailsDTO> contactList=clientService.getVpnContactDetailsListByClientID( AllClientRepository.getInstance().getVpnClientByClientID(  domainDTO.getDomainClientID(),ModuleConstants.Module_ID_DOMAIN).getId());
										 %>
											<div class="table-responsive">
												<table class="table table-bordered table-striped table-hover">
													<thead>
														<tr>
															<th class="text-center" colspan="2"><h3><b><%=domainDTO.getDomainAddress() %></b>'s Information</h3></th>
														</tr>
													</thead>
													<tbody>
															<tr>
																<th scope="row">Domain</th>
																<td><%=domainDTO.getDomainAddress() %></td>
															</tr>
															
															<%if(contactList!=null && contactList.size()>0){ %>
																<tr>
																	<th scope="row">Registrant's Name</th>
																	<td><%=contactList.get(0).getRegistrantsName() +"&nbsp;"+contactList.get(0).getRegistrantsLastName() %></td>
																</tr>
																<tr>
																	<th scope="row">Email</th>
																	<td><%=contactList.get(0).getEmail() %></td>
																</tr>
															<%} %>										
															<tr>
																<th scope="row">Domain Status</th>
																<td><%=commonDAO.getActivationStatusName(domainDTO.getCurrentStatus()) %> </td>
															</tr>
															<tr>
																<th scope="row">Activation Date</th>
																<td><%if(domainDTO.getActivationDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getActivationDate()) %><%}else{ %>N/A<%} %></td>
															</tr>
															<tr>
																<th scope="row">Expire Date</th>
																<td><%if(domainDTO.getExpiryDate()>0){%><%=TimeConverter.getTimeStringFromLong(domainDTO.getExpiryDate()) %><%}else{ %>N/A<%} %></td>
															</tr>
														</tbody>
													</table>
													<!--table class="table table-bordered table-striped">
														<thead>
															<tr>
																<th class="text-center" colspan="4"><h3>Server Info</h3></th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<th scope="row">Primary DNS</th>
																<td><%=domainDTO.getPrimaryDNS() %></td>
																<th scope="row">Primary IP</th>
																<td><%=domainDTO.getPrimaryDnsIP() %></td>
															</tr>
															<tr>
																<th scope="row">Secondary DNS</th>
																<td><%=domainDTO.getSecondaryDNS() %></td>
																<th scope="row">Secondary IP</th>
																<td><%=domainDTO.getSecondaryDnsIP() %></td>
															</tr>
															<tr>
																<th scope="row">Tertiary DNS</th>
																<td><%=domainDTO.getTertiaryDNS() %></td>
																<th scope="row">Tertiary  IP</th>
																<td><%=domainDTO.getTertiaryDnsIP() %></td>
															</tr>
													</tbody>
												</table-->
											</div>
										<%} %>
										</div>
									</div>
								<%} %>
							<!-- General Rules -->
							<%if(DomainDTO.isDotBdAvailable) {%>
							<div class="row">
								<div class="col-md-offset-1 col-md-10">
									<table class="table table-bordered table-striped table-hover"
												role="grid">
												<thead>
													<tr>
														<th>Domain Extension</th>
														<th>Description</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<th><strong>.com.bd</strong></th>
														<td>Intended for Commercial entities and purposes</td>
													</tr>
													<tr>
														<th><strong>.org.bd</strong></th>
														<td>Intended for Not-for-profit entities</td>
													</tr>
													<tr>
														<th><strong>.info.bd</strong></th>
														<td>Intended for personal names, only individuals can
															register</td>
													</tr>
													<tr>
														<th><strong>.net.bd</strong></th>
														<td>Restricted to Bangladeshi Internet Service Provider's
															infrastructure</td>
													</tr>
													<tr>
														<th><strong>.edu.bd</strong></th>
														<td>Restricted to Bangladeshi Educational institute,
															college and universities</td>
													</tr>
													<tr>
														<th><strong>.ac.bd</strong></th>
														<td>Restricted to Bangladeshi Academic institute, schools
															and coaching center</td>
													</tr>
													<tr>
														<th><strong>.gov.bd</strong></th>
														<td>Restricted to Government</td>
													</tr>
												</tbody>
											</table>
								</div>
							</div>
							<%} %>
							<!-- General Rules -->
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
	
	<%
			String[] jsStr = request.getParameterValues("js");
			for (int i = 0; ArrayUtils.isNotEmpty(jsStr) && i < jsStr.length; i++) {
		%>
	<script src="${context}<%=jsStr[i]%>" type="text/javascript"></script>
	<%
		}
	%>

   <!-- END PAGE LEVEL PLUGINS -->
   <!-- BEGIN THEME GLOBAL SCRIPTS -->
   <script src="${context}assets/global/scripts/app.min.js" type="text/javascript"></script>
   <script src="domain-buy-validation.js" type="text/javascript"></script>
</body>
</html>