<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("payDraftBankID");
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
%>
<style>
.btn:not(.btn-sm):not(.btn-lg) {
    line-height: 1.30;
}
</style>
<div class="form-group ">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Bank Name</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<div class="input-group " style="width: 100%;">
			<input name="payDraftBankName" id="payDraftBankName" class="form-control"
				autocomplete="off" type="text" value="">
				<input id ="payDraftBankID" type="hidden" name="payDraftBankID">
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