<%@page import="common.payment.constants.PaymentConstants"%>

<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String context = "../../.." + request.getContextPath() + "/";
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption">
			<i class="fa fa-link" aria-hidden="true"></i> Bank Payment
		</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<html:form styleId="fileupload" styleClass="form-horizontal" method="post" enctype="multipart/form-data" action="/BankPayment">
			<div class="form-body">

				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Select Client <span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<input id="clientIdStr" class="form-control ui-autocomplete-input" name="clientIdStr" autocomplete="off" type="text"> 
						<input id="clientId" class="form-control" name="clientID" type="hidden" >
					</div>
				</div>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Select 	Bill<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<html:select styleId="clientBill" styleClass="form-control pull-right" property="billIDs" >
							<option value="0" selected="">Select Bill</option>
						</html:select>
					</div>
					<div id="detailsView" class="col-md-2">
						
					</div>
				</div>

				
				
				<div class="form-group ">
					<label class="col-md-3 control-label" for="form_control_1">Payment
						Type<span class="required" aria-required="true"> * </span></label>
					<div class="col-md-6">
						<div class="md-radio-inline">
							<div class="md-radio">
								<input id="payment_type_check" name="paymentType"
									value="<%=PaymentConstants.PAYMENT_TYPE_CHECK%>"
									class="md-radiobtn" type="radio"> <label
									for="payment_type_check"> <span class="inc"></span> <span
									class="check"></span> <span class="box"></span> Cheque
								</label>
							</div>
							<div class="md-radio">
								<input id="payment_type_pay_order" name="paymentType"
									value="<%=PaymentConstants.PAYMENT_TYPE_PAYORDER%>"
									class="md-radiobtn" type="radio"> <label
									for="payment_type_pay_order"> <span class="inc"></span>
									<span class="check"></span> <span class="box"></span> Pay Order
								</label>
							</div>
							
							<div class="md-radio">
								<input id="payment_type_receipt" name="paymentType"
									value="<%=PaymentConstants.PAYMENT_TYPE_RECEIPT%>"
									class="md-radiobtn" type="radio"> <label
									for="payment_type_receipt"> <span class="inc"></span>
									<span class="check"></span> <span class="box"></span> Receipt
								</label>
							</div>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-md-3 control-label payOrCheckText">Pay Draft/Cheque/Receipt No<span class="required" aria-required="true"> * </span></label>
					<div class="col-md-6">
						<input name="orderNo" type="text" class="form-control payDraftNumber" placeholder="Type here to find pay draft/cheque/receipt..." disabled/>
						<input name="payDraftID" type="hidden" class="payDraftID"/>
					</div>
				</div>

				
				<div class='form-group'>
						<label class='col-md-3 control-label'>Bank Name
						<span class="required" aria-required="true"> * </span>
						</label>
						<div class='col-md-6 pBankName'
							>
							<input id="payDraftBankName" type="text" class="form-control"
									placeholder="" name="bankName" required> 
								<input id ="payDraftBankID" type="hidden" name="payDraftBankID">
							</div>
				</div>
				
				<div class='form-group'>
						<label class='col-md-3 control-label'>Branch Name</label>
						<div class='col-md-6 pBranchName'
							>
							<input id="" type="text" class="form-control"
									placeholder="" name="bankBranchName"> 
						</div>
					</div>


				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Payment Date
					<span class="required" aria-required="true"> * </span>
					</label>
					<div class="col-sm-6">
						<input type="text" name="paymentDateStr"  class="form-control datepicker" value=""/>
					</div>
				</div>
				

				<div class="form-group">
					<label for="description" class="col-sm-3 control-label">Description</label>

					<div class="col-sm-6">
						<html:textarea property="description" styleClass="form-control" rows="3" ></html:textarea>
					</div>
				</div>

			</div>
			<div class="form-actions text-center">
				<input type="hidden" name="moduleID" id="moduleID" value="<%=request.getParameter("moduleID")%>">
				<button class="btn btn-reset-btcl" type="reset">Reset</button>
				<button class="btn btn-submit-btcl" name="action" id="action" value="bank" type="submit">Pay</button>
			</div>
		</html:form>
	</div>
</div>
<script type="text/javascript">

function processList(url, formData, callback, type) {
    $.ajax({
        type: typeof type != 'undefined' ? type : "POST",
        url: url,
        data: formData,
        dataType: 'JSON',
        success: function (data) {
            if (data.responseCode == 2) {
                toastr.error(data.msg);
            } else {
                if (data.payload.length == 0) {
                    toastr.error("No data found!");
                } else {
                    callback(data.payload);
                }
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus + ", Message: " + errorThrown);
        },
        failure: function (response) {
            toastr.error(response);
        }
    });
}

function getBank(bankDTO){
	$(".pBankName").html(bankDTO.bankName);
	console.log("bankname: "+bankDTO.bankName);
}

$(document).ready(function(){
	
	$('.datepicker').datepicker({
	 	format: 'dd/mm/yyyy',
        endDate: '+0d',
        autoclose: true
   });
	
	$("input[name=paymentType]").change(function(){
		var checkedValue = $("input[name=paymentType]:checked").val();
		if(checkedValue == 1){
			$(".payOrCheckText").html("Cheque No.");
			$("input[name=orderNo]").prop( "disabled", false );
		}else if(checkedValue == 2){
			$(".payOrCheckText").html("Pay Order No.");
			$("input[name=orderNo]").prop( "disabled", false );
		}else if(checkedValue == 3){
			$(".payOrCheckText").html("Receipt No.");
			$("input[name=orderNo]").prop( "disabled", false );
		}
	});
	
	$("#payDraftBankName").autocomplete({
	    source: function (request, response) {
	        $("#payDraftBankID").val(-1);
	        var term = request.term;
	        var url = context + 'Bank/Banks.do?';
	        var param = {};
	        param['payDraftBankName'] = $("#payDraftBankName").val();
	        processList(url, param, response, "GET");
	    },
	    minLength: 1,
	    select: function (e, ui) {
	        $('#payDraftBankName').val(ui.item.bankName);
	        $('#payDraftBankID').val(ui.item.ID);
	        return false;
	    },
	}).autocomplete("instance")._renderItem = function (ul, data) {
	    return $("<li>").append("<a>" + data.bankName + "</a>").appendTo(ul);
	};
	
	$(".payDraftNumber").on( "change", function(){
		$('.payDraftID').val($(this).val());
	});
	
	$("#clientIdStr").autocomplete({
	    source: function(request, response) {
		 $("#clientId").val(-1);
	       var term = request.term;
	       var url = '../../AutoComplete.do?need=client&moduleID='+$("#moduleID").val();
	       var formData = {};
	       formData['name']=term;
	       if (term.length >= 3) {
				callAjax(url, formData, response, "GET");
			} else {
				delay(function() {
					toastr.info("Your search name should be at lest 3 characters");
				}, systemConfig.getTypingDelay());
			}
	    },
	    minLength: 1,
	    select: function(e, ui) {
	       $('#clientIdStr').val(ui.item.data);
	       $('#clientId').val(ui.item.id);
	       loadBills(ui.item.id);
	       return false;
	    },
	 }).autocomplete("instance")._renderItem = function(ul, item) {
	    /* console.log(item); */
	    return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	 };
	 
	 var afterBillLoad=function(data){
	     console.log(data);
	     var options="";
	     if(data.length>0){
		 	options="<option value='-1' disabled selected>Select a bill</option>"
		     $.each(data,function(index, item){
			 	options+="<option value='"+item['ID']+"'>"+"#"+item['ID']+", "+item['billName']+"("+item['entityName']+")"+"</option>"
		     });
	     }else{
		 	options="<option value='-1' selected disabled>No Bill Found</option>"
	     }
	     $("#clientBill").html(options);
	    
	 }
	 function loadBills(clientID){
	     var formData = {};
	     var url= '../../AutoComplete.do?need=bill&moduleID='+$("#moduleID").val()+"&clientID="+clientID;
	     
         callAjax(url, formData, afterBillLoad, "GET");
         $("#detailsView").html("");
	 }
	 $("#clientBill").change(function(){
	     $("#detailsView").html("<a target='_blank' href='../bill/billView.jsp?id="+$(this).val()+"' >View</a>");
	 })
	 $('#fileupload').submit(function(){
		 if($("#clientId").val()==''){
			 toastr.error("Please search and select a client");
			 return false;
		 }
		 if($("#clientBill").val()<=0 ||  $("#clientBill").val()=='') {
			 toastr.error("Please select a valid bill");
			 return false;
		 }
		 return true;
	 });
	 
	 $("#action").click(function( event ) {
		  event.preventDefault();

		  var bankName = $("input[name=bankName]").val();
		  var bankID = $("input[name=payDraftBankID]").val();
		  var date = $("input[name=paymentDateStr]").val();
		  if(bankName == ""){
			  alert("Please select a bank name");
		  }else if(bankID == ""){
			  alert("Please select a valid bank");
		  }else if(date == ""){
			  alert("Select a valid date");
		  }else{
			  $("#fileupload" ).submit(); 
		  }
		  
		  
	});
});

</script>

<%-- <script src="${context}assets/scripts/common/bank-payment-validation.js" type="text/javascript"></script> --%>