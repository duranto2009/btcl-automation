<%@page import="crm.repository.CrmAllDesignationRepository"%>
<%@page import="crm.repository.CrmDepartmentRepository"%>
<%@page import="crm.repository.CrmAllEmployeeRepository"%>
<%@page import="crm.*" %>
<%@page import="java.util.*" %>
<%@page import="crm.inventory.*" %>

<div class="form-group">



	<label class="control-label col-md-4 col-sm-4 col-xs-4">Previous Employee</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<input type="text" class = "form-control" id="prevEmployee">
		<input type="hidden"  name="previousEmployee" id="prevEmployeeID">
	</div>
	
</div>
<script>
$(document).ready(function(){
	var prevEmployee = $('#prevEmployee');
	var currEmployee = $('#currEmployee');
	
	var prevEmpID = $("#prevEmployeeID");
	var currEmpID = $("#currEmployeeID");
	
	prevEmployee.autocomplete({ 
		source : function(request, response) {
			prevEmpID.val(-1);
			var term = request.term;
			var url = context + 'CrmActivityLog/getAllCrmEmployee.do';
			var param = {};
			param.partialName = term;
			processList(url, param, response, "GET");
		},
		minLength : 1,
		select : function(e, ui) {
			var row = ui.item.employeeName + "[" + ui.item.designationName + "]" + "[" + ui.item.departmentName + "]";
			prevEmployee.val(row);
			prevEmpID.val(ui.item.employeeID);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		var row = item.employeeName + "[" + item.designationName + "]" + "[" + item.departmentName + "]";
		return $("<li>").append(
				"<a>" + 
					row + 
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
						toastr.error("Nothing found!");
					} else {
						LOG(data);
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