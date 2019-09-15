<%@page import="common.RequestFailureException"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="client.ClientService"%>
<%@page import="common.ObjectPair"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="client.ClientTypeConstants"%>
<%@page import="common.RegistrantTypeConstants"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="common.ClientConstants"%>
<%@page import="util.Country2Phone"%>
<%@page import="java.util.Locale"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="util.SOP"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="common.ModuleConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
<%@ page import="sessionmanager.SessionConstants" %>

<%
    login.LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	int id = -1;
	String context = "../../.." + request.getContextPath() + "/";
	int moduleID = Integer.parseInt(request.getParameter("moduleID"));
	
	ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
	if (!clientService.isIndividualClientAccepted(moduleID) && !clientService.isCompanyClientAccepted(moduleID)) {
		throw new RequestFailureException("No valid client can be registered in this module.");
	}
%>

<div class="portlet box portlet-btcl form">
	<div class=portlet-title><div class=caption><i class="fa fa-user" aria-hidden="true"></i>Client Registration Information</div></div>
	<div class="portlet-body form">
		<form class="form-horizontal" id=clientRegistrationForm method=post action="../../AddClient.do">
			<div class="form-body">

				<%@include file="../../includes/vpnclients/client-info-copy.jsp" %>
	
				<div class="row form-group">
					<label class="control-label col-md-3 text-center"> <span class="bold">Registration Type </span> </label>
					<div class="col-md-4">
						<div class="radio-list">
							<%if(clientService.isIndividualClientAccepted(moduleID)) {%>
							<label class="radio-inline">
								 <input type="radio" name="accountType" value="1" autocomplete="off" data-no-uniform="true" checked />Individual
							</label>
							<%} 
							if(clientService.isCompanyClientAccepted(moduleID)) {%>
							<label class="radio-inline"> 
							 	<input type="radio" name="accountType" value="2" autocomplete="off"  data-no-uniform="true"  />Company
							</label>
							<%} %>
						</div>
					</div>
				</div>
	
				
				<!-- Individual -->
				<%if(clientService.isIndividualClientAccepted(moduleID)) {%>
				<%@include file="individual-client-form.jsp"%>
				<%} %>
	
				<!-- Company -->
				<%if(clientService.isCompanyClientAccepted(moduleID)) {%>
				<%@include file="company-client-form.jsp"%>
				<%}%>

				<!-- Identification -->
				<div class=row id=identification></div>
				
				<!-- Address -->
				<%@include file="client-address-form.jsp"%>
	
				<!-- Login Information -->
				<%if((!(loginDTO.getAccountID()>0))){%>
				<jsp:include page="client-login-info-form.jsp" flush="true">
					<jsp:param name="loginNameEditability" value="true"/>
					<jsp:param name="captcha" value="false"/> 
				</jsp:include>
				<%}%>
	
				<!-- Contact Details -->
				<div>
					<br><hr><br>
					<h3 class="form-section">Contact Information</h3>
					<div class="row">
						<div class="col-md-4 col-sm-6 contact-info">
							<%@include file="../client/clientAddBody_billingContact.jsp"%>
						</div>
				
						<div class="col-md-4 col-sm-6 contact-info">
							<%@include file="../client/clientAddBody_AdminContact.jsp"%>
						</div>
				
						<div class="col-md-4 col-sm-6 contact-info">
							<%@include file="../client/clientAddBody_TechnicalContact.jsp"%>
						</div>
					</div>
				</div>
			

				<input type=hidden id="clientCategoryType" name="clientDetailsDTO.clientCategoryType"  value="1"/>
			 	<input type="hidden" name="clientDetailsDTO.moduleID" value="<%=moduleID%>">
			   	<input type="hidden" name="registrantContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_REGISTRANT%>" />
				<input type="hidden" name="billingContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_BILLING%>" /> 
				<input type="hidden" name="adminContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_ADMIN%>" />
				<input type="hidden" name="technicalContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_TECHNICAL%>" />
			   	<input type=hidden name="registrantContactDetails.phoneNumber"/>
   	
				<div id=identification-upload></div>

				<div class=row>
					<div class="col-md-12">
						<span class="fileupload-process"></span>
						<div class="col-md-12 fileupload-progress fade">
							<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
								<div class="progress-bar progress-bar-success" style="width: 0%;"></div>
							</div>
							<div class="progress-extended">&nbsp;</div>
						</div>
					</div>
					<table role="presentation" class="table table-striped margin-top-10">
						<tbody class="files"></tbody>
					</table>
				</div>

			</div>
			<div class="form-actions text-center"><button class="btn btn-reset-btcl" type="reset" >Cancel</button><button class="btn btn-submit-btcl" type="submit">Submit</button></div>			
		</form>		
	</div>
</div>

<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />

<script>var moduleID = <%=moduleID%>;</script>
<script src="${context}/assets/scripts/client/client-add-validation.js" type="text/javascript"></script>
<script src="${context}/assets/scripts/client/client-contact-copy.js" type="text/javascript"></script>
<script src="../../assets/scripts/client/client-account-type-utils.js"></script>
<script src="../../assets/scripts/common/client-info-copy.js"></script>
