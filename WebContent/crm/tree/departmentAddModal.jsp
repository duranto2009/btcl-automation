

<%@page import="crm.CrmDepartmentDTO"%>
<% 

	CrmDepartmentDTO crmDeptDTO = (CrmDepartmentDTO) request.getAttribute("department"); 
	String districtName = (crmDeptDTO.getDistrictName() == null)? "All": crmDeptDTO.getDistrictName();
	String upazilaName = (crmDeptDTO.getUpazilaName() == null) ? "All": crmDeptDTO.getUpazilaName();
	String unionName = (crmDeptDTO.getUnionName() == null)? "All":crmDeptDTO.getUnionName();
%>

<div class="modal fade" id="departmentAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Edit Department</h4>
			</div>
			<form class="form-horizontal" id="tableForm">
				<div class="modal-body">
				<div class="form-body">
					<div class="form-group">
						<label class="col-sm-3 control-label">Mark As NOC</label>
						<div class="col-sm-6">
							<input  type="checkbox" name="isNOC" id="isNOC"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">District</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
							name="districtName" value="<%=districtName %>" id="districtNameDept" required>
							<input type="hidden" id="districtID" name="districtID" value="<%=crmDeptDTO.getDistrictID()%>">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Upazila</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="upazilaNameDept"
							name="upazilaName" value = "<%=upazilaName %>" >
							<input type="hidden" id= "upazilaID" name="upazilaID" value="<%=crmDeptDTO.getUpazilaID()%>">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Union</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
							name="unionName" id="unionNameDept" value = "<%=unionName%>" >
							<input type="hidden" id="unionID" name="unionID" value="<%=crmDeptDTO.getUnionID()%>">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Department</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
							name="departmentName" value="<%=crmDeptDTO.getDepartmentName() %>" id="departmentName" required >
							<input type="hidden" id="departmentID" name="ID" value="<%=crmDeptDTO.getID()%>">
						</div>
					</div>
					
					
					<div class="form-actions text-center">
						<button class="btn btn-reset-btcl" type="reset" id="resetBtn">Reset</button>
						<button class="btn btn-submit-btcl" type="submit" id="submitBtnDept">Submit</button>
					</div>
				</div>
				</div>
				
			</form>	
		</div>
	</div>
</div>

<script src="../../assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../../scripts/util.js"></script>
<script>
$(document).ready(function(){
	<%
		if(crmDeptDTO.isNOC()){
	%>
			$('#isNOC').attr('checked', true);
	<%		
		}
	%>
	$("#submitBtnDept").click(function(){
		var formData = $("#tableForm").serialize();
		LOG(formData);
		var url = "../../CrmDesignation/department/editDepartment.do";
		callAjax(url,formData, function(data){
			if(data['responseCode'] == 1 ){
				toastr.success(data['msg']);
				$("#departmentAddModal").modal('hide');
				setTimeout(function(){
					location.reload();
				}, 1000);
			}else {
				toastr.error(data['msg']);
			}
		}, "POST");
		return false;
	});

	$("#resetBtn").click(function(){
		$("#tableForm").trigger("reset");
	});
	
	
	function processList(url, formData, callback, type) {
		$.ajax({
			type : typeof type != 'undefined' ? type
					: "POST",
			url : url,
			data : formData,
			dataType : 'JSON',
			success : function(data) {
				callback(data.payload);
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
	
	var districtName= $("#districtNameDept");
	var upazilaName= $("#upazilaNameDept");
	var unionName= $("#unionNameDept");
	var districtID= $("#districtID");
	var upazilaID= $("#upazilaID");
	var unionID= $("#unionID");
	districtName.change(function(){
		if(	$.trim(	districtName.val()	).length ==0	){
			districtOnChangeEdit();
		}
	});
	upazilaName.change(function(){
		if(	$.trim(	upazilaName.val()	).length ==0	){
			upazilaOnChangeEdit();
		}
	});
	unionName.change(function(){
		if(	$.trim(	unionName.val()	).length ==0	){
			unionOnChangeEdit();
		}
	});
	function districtOnChangeEdit(){
		districtID.val('');
		upazilaName.val('');
		upazilaOnChangeEdit();
	};
	
	function upazilaOnChangeEdit(){
		upazilaID.val('');
		unionName.val('');
		unionOnChangeEdit();
	}
	function unionOnChangeEdit(){
		unionID.val('');
	}
	districtName.autocomplete({
		source : function(request, response) {
			var url = '../../CrmDesignation/department/autoComplete.do';
			var formData={};
			districtOnChangeEdit();
			formData.name = request.term;
			formData.categoryType = 1;
			processList(url,formData,response,"POST");
		},
		minLength : 1,
		select : function(e, ui) {
			districtName.val(ui.item.name);
			districtID.val(ui.item.ID);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, data) {
		return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
	};
	
	upazilaName.autocomplete({
		source : function(request, response) {
			var url = '../../CrmDesignation/department/autoComplete.do';
			var formData={};
			upazilaOnChangeEdit();
			formData.name = request.term;
			formData.categoryType=2;
			formData.parentID = districtID.val();
			processList(url,formData,response,"POST");
		},
		minLength : 1,
		select : function(e, ui) {
			upazilaName.val(ui.item.name);
			upazilaID.val(ui.item.ID);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, data) {
		return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
	};
	
	unionName.autocomplete({
		source : function(request, response) {
			var url = '../../CrmDesignation/department/autoComplete.do';
			var formData={};
			unionOnChangeEdit();
			formData.name = request.term;
			formData.categoryType=3;
			formData.parentID = upazilaID.val();
			processList(url,formData,response,"POST");
		},
		minLength : 1,
		select : function(e, ui) {
			unionName.val(ui.item.name);
			unionID.val(ui.item.ID);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, data) {
		return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
	};
});
</script>