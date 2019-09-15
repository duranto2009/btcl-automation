<!-- Modal -->
<div class="modal fade" id="employeeAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" > Add Employee </h4>
			</div>
			
			<form action="<%=request.getContextPath()%>/CrmEmployee/createEmployee.do" id="employeeAddForm" method="post">
				
				<div class="modal-body">
					
					<input type="hidden" name="parentID" id="parentID" value="" />
					<input type="hidden" name="parentDesignationCategoryTypeID" id="parentDesignationCategoryTypeID" value="" />
					<input type="hidden" name="userID" id="userID" value="4127" />
					<input type="hidden" name="crmEmployeeID" id="crmEmployeeID" value="" />
					<input type="hidden" name="editForm" id="editForm" value="false" />
					
					<div class="form-body" style="overflow: hidden">
						
						<h4>Information:</h4>
						
						<div class="form-group" style="overflow: hidden">
							<label for="name" class="col-sm-5 control-label">Designation Name</label>
							
							<div class="col-sm-6">
								<select name="inventoryCatagoryTypeID" id="designationTypeID" class="form-control" style="width:50%" ></select>
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
						
						<h4>Permissions:</h4>
						
						<div class="form-group">
							
							<label for="hasPermissionToChangeStatus" class="col-sm-5 control-label"> Can Change Status </label>
							<div class="col-sm-6">
								<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToChangeStatus" id="hasPermissionToChangeStatus" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						<div class="form-group">
							
							<label for="hasPermissionToPassStatusChangingPermission" class="col-sm-5 control-label"> Can Pass Status Change </label>
							<div class="col-sm-6">
								<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassStatusChangingPermission" id="hasPermissionToPassStatusChangingPermission" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						<div class="form-group">
							
							<label for="hasPermissionToForwardComplain" class="col-sm-5 control-label"> Can Forward Complain </label>
							<div class="col-sm-6">
								<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToForwardComplain" id="hasPermissionToForwardComplain" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						<div class="form-group">
							
							<label for="hasPermissionToPassComplainForwardingPermission" class="col-sm-5 control-label"> Can Pass Complain Forward </label>
							<div class="col-sm-6">
								<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassComplainForwardingPermission" id="hasPermissionToPassComplainForwardingPermission" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						<div class="form-group">
							
							<label for="hasPermissionToPassComplain" class="col-sm-5 control-label"> Can Pass Complain </label>
							<div class="col-sm-6">
								<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassComplain" id="hasPermissionToPassComplain" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						<div class="form-group">
							
							<label for="hasPermissionToPassComplainPassingPermission" class="col-sm-5 control-label"> Can Pass Complain Pass</label>
							<div class="col-sm-6">
								<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassComplainPassingPermission" id="hasPermissionToPassComplainPassingPermission" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						<div class="form-group">
							
							<label for="hasPermissionToAssignComplain" class="col-sm-5 control-label"> Can Assign Complain</label>
							<div class="col-sm-6">
								<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToAssignComplain" id="hasPermissionToAssignComplain" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						<div class="form-group">
							
							<label for="hasPermissionToPassComplainAssigningPermission" class="col-sm-5 control-label"> Can Pass Complain Assign</label>
							<div class="col-sm-6">
								<input type="checkbox" class="form-control simple-input regi" name="hasPermissionToPassComplainAssigningPermission" id="hasPermissionToPassComplainAssigningPermission" />
							</div>
						</div>
						
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
