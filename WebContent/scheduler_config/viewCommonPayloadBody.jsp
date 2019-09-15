<%@page import="vpn.action.VpnMonthlyBillAction"%>
<style>
#output{white-space: pre}
</style>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
	</div>
	<div class="portlet-body">
		<div class="row">
			<label class="col-sm-3">Returned Object</label>
			<div id="output" class="col-sm-6">
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
	var url = context + "SchedulerVpn/viewPayload.do";
	var formData = {};
	
	callAjax(url, formData, function(data){
		if(data.responseCode == 1){
			toastr.success(data.msg);
			var jsonObj = data.payload;
			var jsonPretty = document.createTextNode(JSON.stringify(jsonObj,null,"\t"));  
			$('#output').html(jsonPretty);	
		}else {
			toastr.error(data.msg);
		}
				
	}, "GET");
	
});
	
</script>