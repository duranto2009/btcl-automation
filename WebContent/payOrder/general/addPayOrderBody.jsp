<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="java.util.Map"%>
<%@page import="payOrder.PayOrderDTO"%>
<div class="row">
	<div class="col-md-12">
		<div class="portlet box portlet-btcl">

			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-search-plus"></i> Add Pay Order
				</div>
				<div class="tools">
					<a class="collapse" href="javascript:;" data-original-title=""
						title=""> </a>
				</div>
			</div>

			<div class="portlet-body form">
				<form class="form-horizontal" id=""
					action="<%=request.getContextPath()%>/PayOrder/AddNewPayOrder.do"
					method="post">
					<div class="form-body">
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">Total Amount<span class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<input id="" type="text" class="form-control"
									placeholder="" name="payDraftTotalAmount" required> 
							</div>
						</div>
						<div class="form-group">
							<label for="" class="control-label col-sm-2">Draft Type<span
								class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<select name="payDraftType" class="form-control border-radius ">
									<%
										for (Map.Entry<Integer, String> entry : PayOrderDTO.MapOfDraftTypeToDraftTypeID.entrySet()) {
									%>
									<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
									<%
										}
									%>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="cnName" class="col-sm-2 control-label">Payment Date<span class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<input type="text" name="paymentDateStr"
									class="form-control datepicker" required />
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">Draft Number<span class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<input id="" type="text" class="form-control"
									placeholder="" name=payDraftNumber> 
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">Bank Name<span class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<input id="payDraftBankName" type="text" class="form-control"
									placeholder="" name="bankName"> 
								<input id ="payDraftBankID" type="hidden" name="payDraftBankID">
							</div>
						</div>
						
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">Branch Name</label>
							<div class="col-sm-10">
								<input id="" type="text" class="form-control"
									placeholder="" name="payDraftBranchName"> 
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">Module Type<span
								class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<select class="form-control select" id="payDraftModuleID"
									name="payDraftModuleID" style="width: 100%;">
									<%
										for (Map.Entry<Integer, String> entry : ModuleConstants.ModuleMap.entrySet()) {
									%>
									<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
									<%
										}
									%>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">Client Name<span class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<input id="clientName" type="text" class="form-control"
									placeholder="" name="clientName"> 
									<input id ="payDraftClientID" type="hidden" name="payDraftClientID">
							</div>
						</div>
					</div>
					<div class="form-actions right">
						<button class="btn btn-reset-btcl" type="reset">Reset</button>
						<button class="btn btn-submit-btcl" type="submit">Submit</button>
					</div>
				</form>
			</div>
		</div>
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

$(document).ready(function (){
	var moduleID = -1;
	$("#clientName").autocomplete({
	    source: function (request, response) {
	        $("#payDraftClientID").val(-1);
	        var term = request.term;
	        var url = context + 'AutoComplete.do?moduleID=' + moduleID + '&need=client&status=active';
	        var formData = {};
	        formData['name'] = term;
	        if (term.length >= 2) {
	            callAjax(url, formData, response, "GET");
	        } else {
	            delay(function () {
	                toastr.info("Your search name should be at lest 2 characters");
	            }, systemConfig.getTypingDelay());
	        }
	    },
	    minLength: 1,
	    select: function (e, ui) {
	        $('#clientName').val(ui.item.data);
	        $('#payDraftClientID').val(ui.item.id);
	        return false;
	    },
	}).autocomplete("instance")._renderItem = function (ul, item) {
	    return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);
	};
	
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
	
})
</script>



