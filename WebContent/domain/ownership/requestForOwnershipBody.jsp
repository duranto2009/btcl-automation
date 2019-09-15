<%@page import="request.StateDTO"%>
<%@page import="request.StateRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="vpn.clientContactDetails.ClientContactDetailsDTO"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="domain.constants.DomainRequestTypeConstants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="util.SOP"%>
<%@page import="util.TimeConverter"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="domain.DomainDTO"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@page import="file.FileTypeConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="domain.DomainPackage"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackageDetails"%>
<%@page import="domain.DomainService"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<link href="<%=request.getContextPath() %>/assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath() %>/assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
<%
LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
Boolean isFileRequired = true;
String domainExt = null;
String isAvailable = request.getAttribute("available")+"";
Long domainClientID = (Long) request.getAttribute("domainClientID");
DomainDTO domainDTO = (DomainDTO)request.getAttribute("domainDTO");
String domainName= (String)request.getAttribute("domainName");
String fullDomainAddress= (String)request.getAttribute("fullDomainAddress");
String tldType= request.getAttribute("tldType")+"";
String commonText="Please write a domain name without www.";
if("true".equals(request.getAttribute("invalidSearch"))){
	commonText="Invalid Domain Search";
}

String errorMessage=(String)request.getAttribute("msg");
if(null==errorMessage){
	errorMessage=", Ownership can not be changed";
}

boolean inAFlow = false;
boolean currentStatusActive = true;
boolean latestStatusActive = true;
boolean activeDomain = false;
if(domainDTO != null){
	StateDTO latestStateDTO = StateRepository.getInstance().getStateDTOByStateID(domainDTO.getLatestStatus());
	StateDTO currentStateDTO = StateRepository.getInstance().getStateDTOByStateID(domainDTO.getCurrentStatus()); 
	latestStatusActive = (latestStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE);		
	currentStatusActive = (currentStateDTO.getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE);
	activeDomain = StateRepository.getInstance().getStateDTOByStateID(domainDTO.getCurrentStatus()).getActivationStatus() == EntityTypeConstant.STATUS_ACTIVE;
}
inAFlow = (!currentStatusActive || (currentStatusActive && !latestStatusActive));  


%>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">  <i class="fa  fa-exchange"> </i>Ownership change</div>
	</div>
	<!-- Main Body -->
	<div class="portlet-body" style="min-height: 450px;">
		<div class="row">
			<%if("true".equals(request.getAttribute("invalidSearch"))){ %>
			<div class="domainBuy" style="padding-top: 5px; ">		
				<div class="note note-danger" style="text-align: center;">
					<h4 class="block">Invalid Domain Search</h4>
				</div>
			</div>
			<%} %>
			<!-- Domain Search -->
			<div class="col-md-12">
				<div class="search-bar bordered">
					<div class="row">
						<div class="col-md-offset-1 col-md-10" style="padding-top: 20px; padding-bottom: 20px;">
							<form method="post" action="../../DomainAction.do?mode=checkOwnership" id="submit_for_check">
							<% if (loginDTO.getIsAdmin()) { %>
							<table class="table table-bordered table-checkable no-footer">
								<%
									if(domainClientID != null)
									{
										ClientDetailsDTO clientDetailsDTO =  AllClientRepository.getInstance().getVpnClientByClientID(domainClientID, ModuleConstants.Module_ID_DOMAIN);
								%>
									<tr>
										<td><label class="control-label" style="padding-top: 3px;">New Client Name<span class="required" aria-required="true"> * </span>
										</label></td>
										<td>
											<div class="form-group" style="margin-bottom: 0px;">
												<input id="clientIdStr" type="text" class="form-control"
													placeholder="Type to search client" name="clientIdStr" value="<%=clientDetailsDTO.getLoginName() %>">
												<input id="clientId" type="hidden" class="form-control"
													name="domainClientID" value="<%=domainClientID %>">
											</div>
										</td>
									</tr>
								<%
									}else{
								%>
								<tr>
									<td>
										<label class="control-label" style="padding-top: 3px;">
											New Client Name
											<span class="required" aria-required="true"> * </span>
										</label>
									</td>
									<td>
										<div class="form-group" style="margin-bottom: 0px;">
											<input id="clientIdStr" type="text" class="form-control" placeholder="Type to search client" name="clientIdStr">
											<input id="clientId" type="hidden" class="form-control" name="domainClientID" value="<%=domainClientID%>">
										</div>
									</td>
								</tr>
								<% } %>
							</table>
							<% } %>								
						<div class="" style="padding-bottom: 5px; padding-left: 5px;">
							<div class="domain-validation-message has-error" style="position: relative; top: 0px;">Please write a domain name without www.</div>
							</div>
								<div class="input-group  my-group">
									<input id="domainName" type="text" name="domainName" value="<%=StringUtils.trimToEmpty(domainName) %>"
										class="form-control" style="width: 80%;"
										data-placement="bottom" data-toggle="popover"
										data-container="body" data-html="true"
										placeholder="Search for Domain"
										title="Please use bangla keyboard and write a domain name without www.">

									<select id="domainExt" name="domainExt" style="width: 20%;" class="selectpicker form-control" data-live-search="true" title="Please select extension">
										<option value="1" <%if("1".equals(tldType)){ %> selected<%} %>>.bd</option>
										<option value="2" <%if("2".equals(tldType)){ %> selected<%} %>>.বাংলা</option>
									</select> 
									<span class="input-group-btn">
										<button class="btn blue uppercase bold  my-group-button" id="checkDomain" type="submit">Search</button>
									</span>
								</div>
							</form>
							<br>
						</div>
					</div>
				</div>
			</div>
			<div id="popover-content" class="col-md-12 bangla-keyboard hide">
				<%@ include file="../../keyboard/amarBangla.jsp"%>
			</div>
		</div>
		<!-- /Domain Search -->
		<!-- Congratulation message -->
		
		<%if("false".equals(isAvailable) && StringUtils.isNotBlank(domainName)){%>
			<div class="domainBuy" style="padding-top: 5px; ">		
				<div class="note note-danger" style="text-align: center;">
					<h4 class="block"><strong><%=domainName %></strong> <%=errorMessage %></h4>
				</div>
			</div>
		<%} %>
		<%if("true".equals(isAvailable) && domainDTO!=null && domainDTO.getDomainClientID()>0 ){
			 CommonDAO commonDAO = new CommonDAO();
	 		 ClientService clientService= new ClientService();
			 List<ClientContactDetailsDTO> contactList=clientService.getVpnContactDetailsListByClientID( AllClientRepository.getInstance().getVpnClientByClientID(  domainDTO.getDomainClientID(),ModuleConstants.Module_ID_DOMAIN).getId());

		%>
			<div class="table-responsive">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th class="text-center" colspan="4"><h3><b><%=domainDTO.getDomainAddress() %></b>'s Information</h3></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row">Domain</th>
							<td><%=domainDTO.getDomainAddress() %></td>
						</tr>
						<tr>
							<th scope="row">Username</th>
							<td><%=AllClientRepository.getInstance().getClientByClientID(domainDTO.getDomainClientID()).getLoginName() %></td>
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
				<table class="table table-bordered table-striped">
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
			</table>
		</div>
		<%if(!inAFlow && activeDomain){ %>
		<div class="row" style="border: 1px solid #e6e9ec !important; padding: 15px !important;">
			<!-- Domain Buy Form -->
			<form method="post" action="../../DomainAction.do?mode=ownershipRequest" id="fileupload" enctype="multipart/form-data">
				<input type="hidden" name="domainAddress" value="<%=fullDomainAddress%>">
				<input id="clientId" type="hidden" class="form-control" name="domainClientID" value="<%=domainClientID %>" >	
				<!-- Domain Buy : General Information -->
			
				<div class="portlet-body form">
				
					<%if((isFileRequired != null && isFileRequired) || loginDTO.getIsAdmin()){ %>
					<div class="row"> 
						<div class="col-md-12">
							<div class="table">
								<table class="table table-striped table-bordered table-checkable dataTable no-footer">
									<tbody>
										<tr>
										<td colspan="1" width="25%"><label class="control-label">Upload Required Documents<span class="required" aria-required="true"> * </span></label></td>
										<td colspan="1" width="75%">
											<!-- File upload -->
											<div class="row">
												<div class="col-md-12">
													 	<div class="col-md-3" style="padding: 0px;">
															<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
															<span class="btn btn-warning-btcl  fileinput-button">
																<i class="fa fa-upload"></i>
																<span> Upload here </span> 
																<input class="jFile" id="doc" type="file" name="<%=FileTypeConstants.DOMAIN_BUY.DOMAIN_OWNERSHIP_CHANGE%>">
															</span>
														</div>
														<div class="col-md-9 has-error" id="fileRequired">
														</div>
							
													<div class="col-md-9">
														 <!-- The global file processing state -->
														 <span class="fileupload-process"></span>
												   		  <!-- The global progress state -->
												          <div class="col-lg-12 fileupload-progress fade">
												              <!-- The global progress bar -->
												              <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
												                  <div class="progress-bar progress-bar-success" style="width:0%;"></div>
												              </div>
												              <!-- The extended global progress state -->
												              <div class="progress-extended">&nbsp;</div>
												          </div>
													</div>
													<!-- The table listing the files available for upload/download -->
													<table role="presentation" class="table table-striped margin-top-10">
														<tbody class="files"></tbody>
													</table>
												</div>
										 		<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
											</div>
											<!-- /File upload -->
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<%}%>
				<div class="row">
					<div class="col-md-12 text-left">
						<input type="checkbox" name="checkTerms" value="check" id="checkTerms" required/>
						<label for="checkTerms">I have read and agree to the  <a target="_blank" href="${context}registration/terms-and-conditions.jsp">Terms and Conditions and Privacy Policy </a></label>
					</div>
				</div>
				
				<!-- /Domain Buy : General Information -->
				<div class="form-actions">
					<div class="row">
	                     <div class="col-md-offset-4 col-md-8">
	                         	<a href="${context}domain/ownership/requestForOwnership.jsp" class="btn btn-reset-btcl" style="padding-left: 15px; padding-right: 15px;"> Reset </a>
	                         	
	                         	<input id="requetOwnershipBtn" type="submit" class="btn btn-submit-btcl" value="Request For Ownership"/>
	                         	
	                     </div>
	                 </div>
	            </div>
	           
	            </div>
			</form>
			<!-- /Domain Buy Form -->
		</div>
		 <%}else{%>
		 	<div class="note note-danger" style="text-align: center;">
				<h4 class="block">This domain <%=domainDTO.getDomainAddress()%> is in processing state. Please wait until ownership service is available for this domain. </h4>
			</div>
		 <%} %>
	<%} %>
	</div>
	<!-- Main Body -->
</div>

<link href="../domainQueryForBuy/domain-query-buy.css" rel="stylesheet" type="text/css"/>

<script src="${context}assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
<script src="${context}domain/client/domainClient.js" type="text/javascript"></script>
<script src="${context}assets/scripts/domain/commonDomainValidation.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	var bdDomainExt=1;
	var banglaDomainExt=2;
	
	$('#backToLogin').click(function(){
		window.location=context+'domain/ownership/requestForOwnership.jsp';
	});
	
	$("#requetOwnershipBtn").click(function(){
	    //$('#termsModal').modal('show');
	    if($("#checkTerms:checked").val()){
	    	$('.jFile').attr('disabled', true);
			$("#fileupload").submit();
	    }else{
			toastr.error("Please read all the terms and conditions");
	    }
	})
	
});
</script>
	
