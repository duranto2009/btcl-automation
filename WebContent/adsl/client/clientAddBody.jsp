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
%>

<html:base />



<portlet-div data-title-icon="fa fa-user" data-title="ADSL Registration Information" data-action="/AddClient" data-form-id=fileupload>
	<%@include file="../../includes/vpnclients/client-info-copy.jsp" %>
	
	<div class="row form-group">
		<label class="control-label col-md-3 text-center"> <span  class="bold">Registration Type </span> </label>
		<div class="col-md-4">
			<div class="radio-list">
				<label class="radio-inline">
					 <input type="radio" name="accountType" value="1" autocomplete="off" data-no-uniform="true" checked />Individual
				</label>
				 <label class="radio-inline"> 
				 	<input type="radio" name="accountType" value="2" autocomplete="off"  data-no-uniform="true"  />Company
				</label>
			</div>
		</div>
	</div>
	
	
	<!-- Individual -->
	<div id="regIndividualInfo">
	<br><hr><br>
   		<h3 class="form-section">Person Info</h3>
   		<input type=hidden name="clientDetailsDTO.registrantType" value="0">
    	<input type=hidden name="clientDetailsDTO.regiCat" value="0">
		
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="First Name" data-required=true data-fullwidth=true>
					<input type=text name="registrantContactDetails.registrantsName" class="form-control individual regi">
				</default-form-group>
			</div>
         	<div class="col-md-6">
         		<default-form-group data-title="Last Name" data-required=true data-fullwidth=true>
					<input type=text name="registrantContactDetails.registrantsLastName" class="form-control individual regi">
				</default-form-group>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="Father Name" data-required=true data-fullwidth=true>
					<input type=text name="registrantContactDetails.fatherName" class="form-control">
				</default-form-group>
			</div>
			<div class="col-md-6">
				<default-form-group data-title="Mother Name" data-required=true data-fullwidth=true>
					<input type=text name="registrantContactDetails.motherName" class="form-control">
				</default-form-group>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="Gender" data-fullwidth=true>
					<select name="registrantContactDetails.gender" class="form-control">
						<option value="male">Male</option><option value="female">Female</option>
					</select>
				</default-form-group>
			</div>
			<div class="col-md-6">
				<default-form-group data-title="Email" data-required=true data-fullwidth=true>
					<input type=text name="registrantContactDetails.email" class="form-control individual regi">
				</default-form-group>
			</div>
		</div>
              
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="Mobile Number" data-required=true data-fullwidth=true>
					<input name="intlMobileNumber" class="phoneNumber form-control individual regi" type="tel"> 
					<span  class="hide valid-msg"> Mobile number is valid</span> 
					<span  class="hide error-msg"> Mobile number is invalid </span>
				</default-form-group>
			</div>
			<div class="col-md-6">
				<default-form-group data-title="Fax" data-fullwidth=true>
					<input type=text name="registrantContactDetails.faxNumber" class="phoneNumber form-control individual regi"> 
				</default-form-group>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="Date of Birth" data-fullwidth=true>
					<input type=text name="registrantContactDetails.dateOfBirth" class="form-control datepicker">
				</default-form-group>
			</div>
         	<div class="col-md-6">
         		<default-form-group data-title="Occupation" data-fullwidth=true>
					<input type=text name="registrantContactDetails.occupation" class="form-control">
				</default-form-group>
			</div>
		</div>
    </div>
	
	
	<!-- Company -->
	<div id="regCompanyInfo" class=hidden>
	<br><hr><br>
		<h3 class="form-section">Company Info</h3>
                   	 
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="Company Name" data-required=true data-fullwidth=true>
					<input type=text name="registrantContactDetails.registrantsName" class="form-control company">
				</default-form-group>
			</div>
			<div class="col-md-6">
				<default-form-group data-title="Web Address" data-fullwidth=true>
					<input type=text name="registrantContactDetails.webAddress" class="form-control">
				</default-form-group>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="Mobile Number" data-required=true data-fullwidth=true>
					<input name="intlMobileNumber" class="phoneNumber form-control company regi" type="tel"> 
					<span  class="hide valid-msg"> Mobile number is valid</span> 
					<span  class="hide error-msg"> Mobile number is invalid </span>
				</default-form-group>
			</div>
			<div class="col-md-6">
         		<default-form-group data-title="Email" data-required=true data-fullwidth=true>
					<input type=text name="registrantContactDetails.email" class="form-control company">
				</default-form-group>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="Telephone Number" data-fullwidth=true>
					<input name="intlMobileNumber" class="phoneNumber form-control company regi" type="tel"> 
				</default-form-group>
			</div>
			<div class="col-md-6">
				<default-form-group data-title="Fax" data-fullwidth=true>
					<input type=text name="registrantContactDetails.faxNumber" class="form-control company">
				</default-form-group>
			</div>
			<div class="col-md-6">
				<default-form-group data-title="Registrant Type" data-fullwidth=true>
					<select name="clientDetailsDTO.registrantType" class="form-control border-radius">
						<option value="0">Select</option>
						<%HashMap<Integer, String> companyMap = RegistrantTypeConstants.REGISTRANT_TYPE.get(ModuleConstants.Module_ID_ADSL);
						for (Integer key : companyMap.keySet()) {%>
						<option  value="<%=key%>"><%=companyMap.get(key) %></option>
						<%}%>
					</select>
				</default-form-group>
			</div>
		</div>
		
		<div class="row">
			<div class=col-sm-12>
				<label class="col-md-2 control-label">Registrant Category<span class="required" aria-required="true"> * </span></label>
				<div clas="col-md-10" style="padding-top:7px">
					<%HashMap<Integer, String> regMap = VpnRegistrantsConstants.vpnRegTypeMap;
					for (Integer reg : regMap.keySet()) {
					String re = reg + "";
					%>
					<div class="col-md-2 col-sm-6 col-xs-12 lr-no-padding">
						<input type=checkbox name=clientDetailsDTO.regiCategories value="<%=re%>">
					<label class=""><%=regMap.get(reg)%></label>
					</div>
					<%}%>
				</div>
			</div>
		</div>
	</div>
	
	
	
	<br><hr><br>
	<!-- Identification -->
	<h3 class="form-section">Identification</h3>
	<div class="row">
	
		<div class=form-group id="document_<%=FileTypeConstants.NID_SUFFIX%100%>">
			<div class=col-md-12>
				<div class="col-md-2 control-label">NID</div>
				<input type="hidden" data-type="nid" data-text="NID" name="clientDetailsDTO.identityType" 
				value="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_NID%>">
				<div class=col-md-7><input type=text name="clientDetailsDTO.identityNo" class="form-control"></div>
				<div class=col-md-3>
					<div id="nid" class="btn btn-default form-control fileinput-button">
						<i class="fa fa-upload"></i>
						<span>NID Documents</span>
						<input class="jFile" type="file" name="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_NID %>" >
					</div>
				</div>
			</div>
		</div>
		
		<div class=form-group id="document_<%=FileTypeConstants.PASSPORT_SUFFIX%100%>">
			<div class=col-md-12>
				<div class="col-md-2 control-label">Passport</div>
				<input type="hidden" data-type="passport" data-text="PASSPORT" name="clientDetailsDTO.identityType" 
				value="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_PASSPORT%>">
				<div class=col-md-7><input type=text name="clientDetailsDTO.identityNo" class="form-control"></div>
				<div class=col-md-3>
					<div id="passport" class="btn btn-default form-control fileinput-button">
						<i class="fa fa-upload"></i>
						<span>Passport Documents</span> 
						<input class="jFile" type="file" name="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_PASSPORT %>" >
					</div>
				</div>
			</div>
		</div>
		
		<div class=form-group id="document_<%=FileTypeConstants.TIN_SUFFIX%100%>">
			<div class=col-md-12>
				<div class="col-md-2 control-label">TIN</div>
				<input type="hidden" data-type="tin" data-text="TIN" 
					name="clientDetailsDTO.identityType"  value="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_TIN %>">
				<div class=col-md-7><input type=text name="clientDetailsDTO.identityNo" class="form-control"></div>
				<div class=col-md-3>
					<div id="tin" class="btn btn-default form-control fileinput-button">
						<i class="fa fa-upload"></i>
						<span>TIN Documents</span> 
						<input class="jFile" type="file" name="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_TIN %>" >
					</div>
				</div>
			</div>
		</div>
		
		<div class=form-group id="document_<%=FileTypeConstants.TRADE_SUFFIX%100%>">
			<div class=col-md-12>
				<div class="col-md-2 control-label">Trade License</div>
				<input type="hidden" data-type="trade" data-text="<%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_TRADE) %>" 
					name="clientDetailsDTO.identityType"  value="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_TRADE %>">
				<div class=col-md-7><input type=text name="clientDetailsDTO.identityNo" class="form-control"></div>
				<div class=col-md-3>
					<div id="trade" class="btn btn-default form-control fileinput-button">
						<i class="fa fa-upload"></i>
						<span>Trade License Documents</span> 
						<input class="jFile" type="file" name="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_TRADE %>" >
					</div>
				</div>
			</div>
		</div>
		
		<div class=form-group id="document_<%=FileTypeConstants.FORWARDING_LETTER_SUFFIX%100%>">
			<div class=col-md-12>
				<div class="col-md-2 control-label">Forwarding Letter</div>
				<input type="hidden"  data-type="forwardingLetterFile" data-text="Forwarding Letter"
					name="clientDetailsDTO.identityType" value="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_FORWARDING_LETTER%>">
				<div class=col-md-7><input type=text name="clientDetailsDTO.identityNo" class="form-control"></div>
				<div class=col-md-3>
					<div id="forwardingLetterFile" class="btn btn-default form-control fileinput-button">
						<i class="fa fa-upload"></i>
						<span>Forwarding Letter Documents</span> 
						<input class="jFile" type="file" name="<%=FileTypeConstants.ADSL_CLIENT_ADD.ADSL_CLIENT_ADD_FORWARDING_LETTER %>" >
					</div>
				</div>
			</div>
		</div>
		
	</div>
	
	
	<br><hr><br>
	<!-- Address -->
	<h3 class="form-section">Address</h3>
	<div class="row">
		<div class="col-md-6">
			<default-form-group data-title=Country data-required=true data-fullwidth=true>
				<select class="form-control select2" name=registrantContactDetails.country>
					<%String[] locales = Locale.getISOCountries();
					for (String countryCode : locales) {
						Locale obj = new Locale("", countryCode);
					%>
					<option value="<%= obj.getCountry()%>"><%=obj.getDisplayCountry()%></option>
					<%}%>
				</select>
			</default-form-group>
		</div>
		<div class="col-md-6">
			<default-form-group data-title=City data-required=true data-fullwidth=true>
				<input type=text name=registrantContactDetails.city class="form-control regi"
			</default-form-group>
		</div>
	</div>
		
	<div class="row">
		<div class="col-md-6">
			<default-form-group data-title="Post Code" data-required=false data-fullwidth=true>
				<input type=text name=registrantContactDetails.postCode class="form-control regi"
			</default-form-group>
		</div>
		<div class="col-md-6">
			<default-form-group data-title="Address" data-required=true data-fullwidth=true>
				<textarea name=registrantContactDetails.address class="form-control border-radius textarea regi"></textarea>
			</default-form-group>
		</div>
	</div>
	
	
	<br><hr><br>
	<!-- Login Information -->
	<%if((!(loginDTO.getAccountID()>0))){%>
	<div id="login-information" style="display:block">
		<h3 class="form-section">Login Info</h3>
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title=Username data-required=true data-fullwidth=true>
					<input name="clientDetailsDTO.loginName"  class="form-control" type="text" autocomplete="off">
				</default-form-group>
			</div>
			<div class="col-md-6">
				<default-form-group data-title=Password data-required=true data-fullwidth=true>
					<input name="clientDetailsDTO.loginPassword" id="password" class="form-control" type="password" autocomplete="off">
				</default-form-group>
			</div>
		</div>
                     
		<div class="row">
			<div class="col-md-6">
				<default-form-group data-title="Confirm Password" data-required=true data-fullwidth=true>
					<input name="confirmLoginPassword" id="cPassword" class="form-control" type="password" autocomplete="off">
				</default-form-group>
			</div>
		</div>
	</div>
	<%}%>
	
	<br><hr><br>
	
	<!-- Contact Details -->
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
	
	
	
	
	<input type=hidden id="clientCategoryType" name="clientDetailsDTO.clientCategoryType"  value="1"/>
 	<input type="hidden" name="clientDetailsDTO.moduleID" value="<%=ModuleConstants.Module_ID_ADSL%>">
   	<input type="hidden" name="registrantContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_REGISTRANT%>" />
	<input type="hidden" name="billingContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_BILLING%>" /> 
	<input type="hidden" name="adminContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_ADMIN%>" />
	<input type="hidden" name="technicalContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_TECHNICAL%>" />
   	<input type=hidden name="registrantContactDetails.phoneNumber"/>
   	
   	
   	
	<div class=row>
		<div class="col-md-12">
			<span class="fileupload-process"></span>
			<div class="col-lg-12 fileupload-progress fade">
				<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
					<div class="progress-bar progress-bar-success" style="width: 0%;"></div>
				</div>
				<div class="progress-extended">&nbsp;</div>
			</div>
		</div>
		<div class=col-md-12>
			<table role="presentation" class="table table-striped">
				<tbody class="files"></tbody>
			</table>
		</div>
	</div>	
	<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />	

</portlet-div>
		
	                     	 		 
	        
	                         	
	                         	
<script src="${context}/assets/scripts/client/client-add-validation.js" type="text/javascript"></script>
<script src="${context}/assets/scripts/client/client-contact-copy.js" type="text/javascript"></script>

<script type="text/javascript">

function showAccountInfo(type){
   $("#clientCategoryType").val(type);
   if(type==1){
		$("#regIndividualInfo").removeClass('hidden');
		$("#regCompanyInfo").addClass('hidden');
		
		$("#regCompanyInfo").find("input, select").attr("disabled", true);
		$("#regIndividualInfo").find("input, select").attr("disabled", false);
		
		$(".individual").addClass("regi");
		$(".company").removeClass("regi");
	}else{
   	 	$("#regCompanyInfo").removeClass('hidden');
   	 	$("#regIndividualInfo").addClass('hidden');
   	 	
   	 	$("#regIndividualInfo").find("input, select").attr("disabled", true);
   	 	$("#regCompanyInfo").find("input, select").attr("disabled", false);
   	 	
   	 	$(".individual").removeClass("regi");
		$(".company").addClass("regi");
	}
}


$(document).ready(function() {
	$("input[name='accountType']").change(function(){
	    showAccountInfo($("input[name='accountType']:checked").val());
	});
	
	/*
	$('select[name="clientDetailsDTO.registrantType"]').change(function(){
    	$.uniform.update($('.identityTypeCom').attr('checked',false));
    	if(($(this).val()==govtCompanyType)|| ($(this).val()==militaryCompanyType) || ($(this).val()==foreignCompanyType)){
    		$('#regCompanyInfo input[name="clientDetailsDTO.identityNo"]').closest('.form-group').hide();
    		$('.identityTypeCom').closest('.radio-inline').hide();
    		$('#forwardingLetter').closest('.radio-inline').show();
    		$.uniform.update($('#forwardingLetter').attr('checked',true));
    	}else{
    		$('#regCompanyInfo input[name="clientDetailsDTO.identityNo"]').closest('.form-group').show();
    		$('.identityTypeCom').closest('.radio-inline').show();
    		$('#forwardingLetter').closest('.radio-inline').hide();
    		$.uniform.update($('#forwardingLetter').attr('checked', false));
    		$('.identityLabel').html('');
    	}
    	
    });
	*/

	function setDefaultCountry(){
		var countrySelector=$('select[name="registrantContactDetails.country"]')
		countrySelector.val("BD");
		sortSelectBox(countrySelector);
	};
	
	setDefaultCountry();
	$('select[name="clientDetailsDTO.registrantType"]').trigger("change");
	 	
});
</script>

<script src="../../assets/scripts/common/client-info-copy.js"></script>
<script src="../../adsl/client/clientMandatoryDocuments.js" type="text/javascript"></script>


