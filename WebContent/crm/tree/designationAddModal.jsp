<style>
	
	.permission{
		
		border-left: rgb(96, 191, 80) 3px solid;
		overflow: hidden;
	}
</style>

<div class="modal fade" id="designationAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"> Add Designation </h4>
			</div>
			
			<form action="<%=request.getContextPath()%>/CrmDesignation/create_designation.do" id="designationAddForm" class="designationAddForm" method="post"> 
				
				<input type="hidden" id="parentCatagoryTypeID" name="parentCatagoryTypeID" value="" />
				<input type="hidden" id="designationTypeID_des" name="designationTypeID_des" value="" />
				<input type="hidden" name="editForm_des" id="editForm_des" value="false" />
				<input type="hidden" name="divID_des" value="" id="divID_des" />
				
				<div class="modal-body">
				
					<div class="form-body" style="overflow: hidden">
						
						<h4>Information:</h4>
						
<!-- 						<div class="form-group" style="overflow:hidden" id="dept_name_block"> -->
<!-- 							<label for="dept_name" class="col-sm-5 control-label">Department Name</label> -->
<!-- 							<div class="col-sm-6"> -->
<!-- 								<input class="form-control simple-input regi" name="dept_name" id="dept_name" /> -->
<!-- 							</div> -->
<!-- 						</div> -->
						
						<div class="form-group" style="overflow:hidden; display:none;" id="dept_code_block">
							<label for="dept_code" class="col-sm-5 control-label">Department Code</label>
							<div class="col-sm-6">
								<input class="form-control simple-input regi" name="dept_code" id="dept_code" />
							</div>
						</div>
						
<!-- 						<div class="form-group" style="overflow:hidden" id="des_noc_block"> -->
<!-- 							<label for="isNOC" class="col-sm-5 control-label">Is NOC</label> -->
<!-- 							<div class="col-sm-6"> -->
<!-- 								<input type="checkbox" class="simple-input regi" name="isNOC" id="des_noc"/> -->
<!-- 							</div> -->
<!-- 						</div> -->
						
						<div class="clearfix"></div>
						
						<div class="form-group">
							<label for="name" class="col-sm-5 control-label">Designation Name</label>
							<div class="col-sm-6">
								<input class="form-control simple-input regi" name="name" id="name" />
							</div>
						</div>
						
						<div class="clearfix"></div>
						
						<h4>Permissions:</h4>
						
						<div class="form-group">
							
							<label for="selectAll_des" class="col-sm-5 control-label"> <b>Select All</b> </label>
							<div class="col-sm-6">
								<input type="checkbox" class="simple-input regi" id="selectAll_des"/>
							</div>
						</div>
						<br/>
						<br/>
						
						<div class="clearfix"></div>
							
						<!-- Permission Container Start -->
						
						<div id="permission-container-designation">
							
							<div class="form-group permission">
								
								<label for="hasPermissionToChangeStatus" class="col-sm-5 control-label"> Can Change Status </label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToChangeStatus" id="hasPermissionToChangeStatus_des"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassStatusChangingPermission" class="col-sm-5 control-label"> Can Pass Status Change </label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToPassStatusChangingPermission" id="hasPermissionToPassStatusChangingPermission_des" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToForwardComplain" class="col-sm-5 control-label"> Can Forward Complain </label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToForwardComplain" id="hasPermissionToForwardComplain_des" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassComplainForwardingPermission" class="col-sm-5 control-label"> Can Pass Complain Forward </label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToPassComplainForwardingPermission" id="hasPermissionToPassComplainForwardingPermission_des" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassComplain" class="col-sm-5 control-label"> Can Pass Complain </label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToPassComplain" id="hasPermissionToPassComplain_des"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToPassComplainPassingPermission" class="col-sm-5 control-label"> Can Pass Complain Pass</label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToPassComplainPassingPermission" id="hasPermissionToPassComplainPassingPermission_des"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
								
								<label for="hasPermissionToAssignComplain" class="col-sm-5 control-label"> Can Assign Complain </label>
								<div class="col-sm-6">
									<input type="checkbox" class="simple-input regi" name="hasPermissionToAssignComplain" id="hasPermissionToAssignComplain_des" />
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group permission">
							
								<label for="hasPermissionToPassComplainAssigningPermission" class="col-sm-5 control-label"> Can Pass Complain Assign </label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassComplainAssigningPermission" id="hasPermissionToPassComplainAssigningPermission_des"/>
								</div>
							</div>
						
						
						<!-- New Permissions -->
						
							<div class="clearfix"></div>
							<div class="form-group permission">
							
								<label for="hasPermissionToAddComplain" class="col-sm-5 control-label"> Can Add Complain </label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToAddComplain" id="hasPermissionToAddComplain_des"/>
								</div>
							</div>
							
							<!-- New Permissions -->
							
							<div class="clearfix"></div>
							<div class="form-group permission">
							
								<label for="hasPermissionToAddComplain" class="col-sm-5 control-label"> Can Add Complain </label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPremissionToAddComplain" id="hasPremissionToAddComplain_des"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToPassAddComplainPermission" class="col-sm-5 control-label"> Can Pass Complain Add Permission</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassAddComplainPermission" id="hasPermissionToPassAddComplainPermission_des"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToChangePriority" class="col-sm-5 control-label"> Can Change Priority</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToChangePriority" id="hasPermissionToChangePriority_des"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToPassChangePriorityPermission" class="col-sm-5 control-label"> Can Pass Priority Change Permission</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassChangePriorityPermission" id="hasPermissionToPassChangePriorityPermission_des"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToPassComplainToOtherDepartment" class="col-sm-5 control-label"> Can Pass Complain To Other Department</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassComplainToOtherDepartment" id="hasPermissionToPassComplainToOtherDepartment_des"/>
								</div>
							</div>
							
							<div class="clearfix"></div>
							
							<div class="form-group permission">
							
								<label for="hasPermissionToPassPassComplainPermissionToOtherDepartment" class="col-sm-5 control-label"> Can Pass Complain Permission To Other Department</label>
								<div class="col-sm-6">
									<input type="checkbox" name="hasPermissionToPassPassComplainPermissionToOtherDepartment" id="hasPermissionToPassPassComplainPermissionToOtherDepartment_des"/>
								</div>
							</div>
						</div>
						
						<!-- Permission Container End -->
						
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