<style>
	.ui-autocomplete{
	
		z-index : 999999 !important;
	}
</style>

<div class="modal fade" id="employeeAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="employeeModalTitle"> Add Employee </h4>
			</div>
			
			<form action="<%=request.getContextPath()%>/CrmEmployee/createEmployee.do" id="employeeAddForm" class="employeeAddForm" method="post">
				
				<div class="modal-body">
					
					<input type="hidden" name="parentID" id="parentID" value="" />
					<input type="hidden" name="parentDesignationCategoryTypeID" id="parentDesignationCategoryTypeID" value="" />
					<input type="hidden" name="crmEmployeeID" id="crmEmployeeID" value="" />
					<input type="hidden" name="editForm" id="editForm" value="false" />
					<input type="hidden" name="divID" value="" id="divID" />
					<input type="hidden" name="ID" value="" id="ID" />
					
					<div class="form-body" style="overflow: hidden">
						
						<h4>Information:</h4>
						
						<div class="form-group" style="overflow: hidden">
							<label for="name" class="col-sm-5 control-label">Designation Name</label>
							
							<div class="col-sm-6">
								<select name="inventoryCatagoryTypeID" id="designationTypeID" class="form-control" style="width:100%" ></select>
							</div>
						</div>
						
						<div class="clearfix"></div>
						
						<div class="form-group" style="overflow: hidden">
							<label for="name" class="col-sm-5 control-label">Employee Name</label>
							<div class="col-sm-6">
								<input class="form-control simple-input regi" name="name" id="userName" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						
						<div class="form-group" style="overflow: hidden">
							<label for="name" class="col-sm-5 control-label">Username</label>
							<div class="col-sm-6">
								<input type="hidden" name="userID" id="userID" value=""/>
								<input class="form-control simple-input regi" name="username" id="username" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						
						<h4>Permissions:</h4>
						
						<div class="form-group">
							
							<label for="selectAll" class="col-sm-5 control-label"> <b>Select All</b> </label>
							<div class="col-sm-6">
								<input type="checkbox" class="simple-input regi" id="selectAll"/>
							</div>
						</div>
						<br/>
						<br/>
						
						<!-- Permission Container Start -->
						<div id="permission-container-employee">
						
							<div class="form-group permission">
								
								<label for="hasPermissionToChangeStatus" class="col-sm-5 control-label"> Can Change Status </label>
								<div class="col-sm-6">
									<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToChangeStatus" id="hasPermissionToChangeStatus" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassStatusChangingPermission" class="col-sm-5 control-label"> Can Pass Status Change </label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToPassStatusChangingPermission" id="hasPermissionToPassStatusChangingPermission" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToForwardComplain" class="col-sm-5 control-label"> Can Forward Complain </label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToForwardComplain" id="hasPermissionToForwardComplain" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassComplainForwardingPermission" class="col-sm-5 control-label"> Can Pass Complain Forward </label>
								<div class="col-sm-6">
									<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassComplainForwardingPermission" id="hasPermissionToPassComplainForwardingPermission" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassComplain" class="col-sm-5 control-label"> Can Pass Complain </label>
								<div class="col-sm-6">
									<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassComplain" id="hasPermissionToPassComplain" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassComplainPassingPermission" class="col-sm-5 control-label"> Can Pass Complain Pass</label>
								<div class="col-sm-6">
									<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassComplainPassingPermission" id="hasPermissionToPassComplainPassingPermission" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToAssignComplain" class="col-sm-5 control-label"> Can Assign Complain</label>
								<div class="col-sm-6">
									<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToAssignComplain" id="hasPermissionToAssignComplain" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassComplainAssigningPermission" class="col-sm-5 control-label"> Can Pass Complain Assign</label>
								<div class="col-sm-6">
									<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassComplainAssigningPermission" id="hasPermissionToPassComplainAssigningPermission" />
								</div>
							</div>
							
							<!-- New Permissions -->
							
							<div class="clearfix"></div>
							<div class="form-group permission">
							
								<label for="hasPremissionToAddComplain" class="col-sm-5 control-label"> Can Add Complain </label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPremissionToAddComplain" id="hasPremissionToAddComplain"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
							
								<label for="hasPermissionToPassAddComplainPermission" class="col-sm-5 control-label"> Can Pass Complain Add Permission</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassAddComplainPermission" id="hasPermissionToPassAddComplainPermission"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToChangePriority" class="col-sm-5 control-label"> Can Change Priority</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToChangePriority" id="hasPermissionToChangePriority"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToPassChangePriorityPermission" class="col-sm-5 control-label"> Can Pass Priority Change Permission</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassChangePriorityPermission" id="hasPermissionToPassChangePriorityPermission"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToPassComplainToOtherDepartment" class="col-sm-5 control-label"> Can Pass Complain To Other Department</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassComplainToOtherDepartment" id="hasPermissionToPassComplainToOtherDepartment"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToPassPassComplainPermissionToOtherDepartment" class="col-sm-5 control-label"> Can Pass Complain Permission To Other Department</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassPassComplainPermissionToOtherDepartment" id="hasPermissionToPassPassComplainPermissionToOtherDepartment"/>
								</div>
							</div>
						</div>
						
						<!-- Permission Container Ended -->
						
					</div>
					
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary"> Save </button>
				</div>
			</form>
			
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready( function() {
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
	
	$("#username").autocomplete({
		source : function(request, response) {
			$("#userID")
					.val("");
			var term = request.term;
			var url = context
					+ 'CrmEmployee/GetUserByPartialName.do?';
			var param = {};
			param['username'] = $(
					"#username")
					.val();

			processList(url, param,
					response, "GET");

		},
		minLength : 1,
		select : function(e, ui) {
			$('#username').val(
					ui.item.username);
			$('#userID').val(
					ui.item.userID);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, data) {
		return $("<li>").append("<a>" + data.username + "</a>").appendTo(ul);
	};
})

</script>
