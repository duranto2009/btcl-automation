<%@page import="crm.*" %>
<div class="form-group">
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Current Employee</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<input class="form-control" type="text"  id="currEmployee">
		<input type="hidden" name="currentEmployee" id="currEmployeeID">
	</div>
	
</div>
<script>
$(document).ready(function(){
	var currEmployee = $('#currEmployee');
	var currEmpID = $("#currEmployeeID");
	
	currEmployee.autocomplete({ 
		source : function(request, response) {
			currEmpID.val(-1);
			var term = request.term;
			var url = context + 'CrmActivityLog/getAllCrmEmployee.do';
			var param = {};
			param.partialName = term;
			processList(url, param, response, "GET");
		},
		minLength : 1,
		select : function(e, ui) {
			var row = ui.item.employeeName + "[" + ui.item.designationName + "]" + "[" + ui.item.departmentName + "]";
			currEmployee.val(row);
			currEmpID.val(ui.item.employeeID);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
			var row = item.employeeName + "[" + item.designationName + "]" + "[" + item.departmentName + "]";
			return $("<li>").append(
				"<a>" + 
					row+ 
				"</a>").appendTo(ul);
	};
	function processList(url, formData, callback, type) {
		$.ajax({
			type : typeof type != 'undefined' ? type
					: "POST",
			url : url,
			data : formData,
			dataType : 'JSON',
			success : function(data) {
				if (data.responseCode == 2) {
					toastr.error(data.msg);
				} else {
					if (data.payload.length == 0) {
						toastr
								.error("Nothing found!");
					} else {
						callback(data.payload);
					}
				}
			},
			error : function(jqXHR, textStatus,
					errorThrown) {
				toastr.error("Error Code: "
						+ jqXHR.status + ", Type:"
						+ textStatus
						+ ", Message: "
						+ errorThrown);
			},
			failure : function(response) {
				toastr.error(response);
			}
		});
	}
});
</script>