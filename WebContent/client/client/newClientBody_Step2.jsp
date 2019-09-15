<%@page import="client.temporaryClient.TemporaryClient"%>
<%@ page import="global.GlobalService" %>
<%
Integer moduleID = (Integer) request.getSession().getAttribute("moduleId");
Integer accountType = (Integer) request.getSession().getAttribute("accountType");

Long tempClientId = -1L;

if(moduleID==null || accountType==null){
	//this is the case of temporary client
	tempClientId = (Long) request.getAttribute("tempClientId");

	TemporaryClient temporaryClient = ServiceDAOFactory.getService(
			GlobalService.class
	).findByPK(TemporaryClient.class, tempClientId);

	moduleID = temporaryClient.getModuleId();
	accountType = temporaryClient.getClientType();

}

%>

<link href="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/css/intlTelInput.css" rel="stylesheet"/>


<form class="form-horizontal" action="<%=request.getContextPath()%>/Client/Add.do" method="POST" id="fileupload">

	<div class="form-body">
	
		<input type=hidden name=clientDetailsDTO.moduleID value=<%=moduleID%>>
		<input type=hidden name=clientDetailsDTO.clientCategoryType id=clientCategoryType value=<%=accountType%>>
		
		<% if(accountType.equals(1)){ %>
			<%@include file="individual-client-form.jsp"%>
		<% } else { %>
			<%@include file="company-client-form.jsp"%>		
		<% } %>
		
		<div class=row id=identification></div>
		
		<%@include file="client-address-form.jsp"%>	
		
		<jsp:include page="client-login-info-form.jsp" flush="true">
			<jsp:param name="loginNameEditability" value="true"/>
			<jsp:param name="captcha" value="true"/> 
		</jsp:include>	

		<div class="row" style="display: none;">
			<div class="col-md-4 col-sm-6">
				<%@include file="clientAddBody_billingContact.jsp"%>
			</div>

			<div class="col-md-4 col-sm-6">
				<%@include file="clientAddBody_AdminContact.jsp"%>
			</div>

			<div class="col-md-4 col-sm-6">
				<%@include file="clientAddBody_TechnicalContact.jsp"%>
			</div>
		</div>
	</div>
				
	<div class="form-actions">
		<div class="row">
			<div class="col-md-offset-5 col-md-7">
				<input type="hidden" name="csrfPreventionSalt" value="${csrfPreventionSalt}"/>
				<input type=hidden name="newClient" value="true" />
				
				<input type=hidden name="registrantContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_REGISTRANT%>" />
				<input type=hidden name="registrantContactDetails.phoneNumber"/>
                
                <input type="hidden" value="-1" name="clientDetailsDTO.existingClientID">
                <input type="hidden" value="1" name="registrantContactDetails.isEmailVerified">
                <input type="hidden" value="1" name="registrantContactDetails.isPhoneNumberVerified">
                <input type="hidden" value="<%=request.getAttribute("email")%>" name="registrantContactDetails.email">


				<button type="reset" class="btn btn-reset-btcl">Reset</button>
				<button type="button" class="btn btn-submit-btcl" id="modalShowButton">Submit</button>
			</div>
		</div>
	</div>
	
	
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog modal-sm">
			<h4 style="text-align:center;color:#1BBC9B;font-weight: bolder;">Documents Agreement</h4>
			<p>You will need to submit the documents listed below to complete your submission.</p>
			<ol type="1" id="document-list"></ol>
			<p>If you agree, click on "Agree". Otherwise, "Cancel" your registration.</p>
			<div align="right">
				<a href="<%=request.getContextPath()%>" class="btn btn-reset-btcl">Cancel Registration</a>
				<button style="margin-left:10px" type="button" class="btn btn-reset-btcl" id=modalHideButton>Back</button>
				<button style="margin-left:10px" type="button" class="btn btn-submit-btcl" id=formSubmitButton>Agree</button>
			</div>
		</div>
	</div>
	
	
</form>

<script>
	$("#modalShowButton").on("click", function(event){
		$(".bill-required").each(function(){$(this).removeClass("bill-required");});
		$(".admin-required").each(function(){$(this).removeClass("admin-required");});
		$(".technical-required").each(function(){$(this).removeClass("technical-required");});
		
		if($("#fileupload").valid()) {
			$("#myModal").modal();
		}
	});
	$("#modalHideButton").on("click", function(event){
		$("#myModal").modal('hide');
	});
	
	function submitCallback(data){
		window.location.href = data.location;
	}
	
	$("#formSubmitButton").click(function(event){
		ajax($("#fileupload").attr("action"), $("#fileupload").serialize(), submitCallback, "POST", []);
		$("#myModal").modal('hide');
	});
	
</script>

<script>

    var globalVariableToStoreSelectedRegistrantSubCategory = [];
    var btrcLicenseDate = "";

</script>

<script>var moduleID = <%=moduleID%>;var accountType= <%=accountType%>;</script>
<script src="<%=request.getContextPath()%>/assets/scripts/client/client-account-type-utils.js"></script>


<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/build/js/intlTelInput.js"></script>
<script src="<%=request.getContextPath()%>/assets/global/plugins/intl-tel-input/examples/js/isValidNumber.js"></script>
<script src="<%=request.getContextPath()%>/assets/scripts/client/client-add-validation.js"></script>
	