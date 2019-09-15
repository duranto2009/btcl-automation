<%@page import="crm.CrmDesignationDTO"%>
<%@page import="distance.form.DistrictDistanceDTO"%>
<%@page import="crm.service.CrmDepartmentService"%>
<%@page import="crm.repository.CrmDepartmentRepository"%>
<%@page import="crm.CrmDepartmentDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.*, java.util.*"%>
<style>
	.control-label{
		text-align: center !important;
	}
</style>
<% 
	LoginDTO loginDTO = (LoginDTO)request.getSession().getAttribute( SessionConstants.USER_LOGIN ); 
	CrmDepartmentDTO crmDepartmentDTO = (CrmDepartmentDTO)request.getAttribute("department");
	
	
%>
<jsp:include page="employeeAddModal.jsp"></jsp:include>
<jsp:include page="designationAddModal.jsp"></jsp:include>
<jsp:include page="departmentAddModal.jsp"></jsp:include>
<jsp:include page="previewModal.jsp"></jsp:include>

<div id="loading" style="width:100%; height:100%; z-index: 999999; position:absolute; left:0px; top:0px; text-align: center; margin-top:10%; display: none" >
	<img src='<%=request.getContextPath()%>/assets/images/loading_green.gif' />
</div>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-search-plus"></i> Department</div>
		 <div class="tools">
            <a class="collapse" href="javascript:;" data-original-title="" title=""> </a>
        </div>
	</div>
	
	<div class="portlet-body">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="table">
					<table class="table table-bordered table-striped" id="deptTable">
						<tbody>
							<tr>
								<td><label class="control-label">Is NOC</label></td>
								<td><label class="control-label"><%=crmDepartmentDTO.isNOC()==true?"Yes":"No" %></label></td>
								
							</tr>
							<tr>
								<td><label class="control-label">District</label></td>
								<td><label class="control-label"><%=crmDepartmentDTO.getDistrictName()==null?"All":crmDepartmentDTO.getDistrictName() %></label></td>
							</tr>
							<tr>
								<td><label class="control-label">Upazila</label></td>
								<td><label class="control-label"><%=crmDepartmentDTO.getUpazilaName()==null?"All" : crmDepartmentDTO.getUpazilaName()%></label></td>
							</tr>
							<tr>
								<td><label class="control-label">Union</label></td>
								<td><label class="control-label"><%=crmDepartmentDTO.getUnionName()==null?"All" : crmDepartmentDTO.getUnionName()%></label></td>
							</tr>
							<tr>
								<td><label class="control-label">Department</label></td>
								<td><label class="control-label"><%=crmDepartmentDTO.getDepartmentName()%></label></td>
							</tr>
						</tbody>
					</table>
					<div class="form-actions text-center">
						<button class="btn btn-submit-btcl" type="button" id="editBtn">Edit</button>
					</div>			
				</div><!-- table -->
			</div> <!-- column -->
		</div> <!-- row -->
		
		<div class="row">
			<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
				<div class="portlet box portlet-btcl">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-search-plus"></i> Department Organogram </div>
						 <div class="tools">
				            <a class="collapse" href="javascript:;" data-original-title="" title=""> </a>
				        </div>
					</div>
					<div class="portlet-body" style="overflow: hidden;">
						<div id="desCon" class="treeContainer">
						</div>
						<div class="clearfix"></div>
						<%if(crmDepartmentDTO.getRootDesignationID() == null){ %>
						<div>
						
							<div class="row">
								<div class="form-group">
									<label class="control-label col-md-4 text-left"> <span
										class="bold">Choose Option </span>
									</label>
									<div class="col-md-8">
										<div class="radio-list">
											<label class="radio-inline"> <input type="radio"
												name="treeOption" value="1" autocomplete="off"
												data-no-uniform="true" >Add New Tree
											</label> <label class="radio-inline"> <input type="radio"
												name="treeOption" value="2" autocomplete="off"
												data-no-uniform="true" />Copy Tree
											</label>
										</div>
									</div>
								</div><!-- formgroup -->
							</div>
							<div id="wrapper-copy-dept" class="form" style="display:none">
								<form id="copy-dept-form" class="form-horizontal">
									<div class="row">
										<div class="form-group">
											<label class="col-md-4 control-label">District</label>
											<div class="col-md-8">
												<input type="text" class="form-control" id="districtName-copy"
												name="districtName-copy" required>
												<input type="hidden" id= "districtID-copy" name="districtID-copy" value="">
											</div>
										</div>
									</div>
									<div class="row">	
										<div class="form-group">
											<label class="col-md-4 control-label">Upazila</label>
											<div class="col-md-8">
												<input type="text" class="form-control" id="upazilaName-copy"
												name="upazilaName-copy" required>
												<input type="hidden" id= "upazilaID-copy" name="upazilaID-copy" value="">
											</div>
										</div>
									</div>
									<div class="row">	
										<div class="form-group">
											<label class="col-md-4 control-label">Union</label>
											<div class="col-md-8">
												<input type="text" class="form-control" id="unionName-copy"
												name="unionName-copy" required>
												<input type="hidden" id= "unionID-copy" name="unionID-copy" value="">
											</div>
										</div>
									</div>
									<div class="row">	
										<div class="form-group">
											<label class="col-md-4 control-label">Department</label>
											<div class="col-md-8">
												<input type="text" class="form-control" id="deptName-copy"
												name="deptName-copy" required>
												<input type="hidden" id= "deptID-copy" name="deptID-copy" value="">
											</div>
										</div>
									</div>
									<div class="text-center">
										<button type="button" id="reset-copy" class="btn btn-reset-btcl">Reset</button>
										<button type="button" id="find-preview" class="btn btn-submit-btcl">Find</button>
									</div>
								</form>
								
							</div><!-- wrapper -->
						</div><!-- form div -->
						<%} %>
						
					</div>
				</div>
			</div>
			<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
				<div class="portlet box portlet-btcl">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-search-plus"></i> Employee Hierarchy </div>
						 <div class="tools">
				            <a class="collapse" href="javascript:;" data-original-title="" title=""> </a>
				        </div>
					</div>
					<div class="portlet-body" style="overflow: hidden;">
						<div id="con" class="treeContainer">
						</div>
						<div class="clearfix"></div>
						<%if(crmDepartmentDTO.getRootDesignationID() != null && !CrmDepartmentService.hasAnyEmployee(crmDepartmentDTO.getID())){ %>
						<div class="text-center">
							<button class="btn btn-submit-btcl treeAddBtn" id="newEmployeeTree" ><i class="fa fa-plus"></i> Add</button>
						</div>
						<%} %>
						<br/>
					</div>
				</div>
			</div>
		</div>
		
		
	</div><!-- portlet-body -->
</div><!-- portlet-box end-->
<script>
			
	var permissions = ["hasPermissionToChangeStatus",
	                   "hasPermissionToForwardComplain",
	                   //"hasPermissionToPassComplainForwardingPermission",
	                   "hasPermissionToPassComplain",
	                   //"hasPermissionToPassComplainPassingPermission",
	                   "hasPermissionToAssignComplain",
	                   //"hasPermissionToPassComplainAssigningPermission",
	                   "hasPermissionToAddComplain",
	                   //"hasPermissionToPassAddComplainPermission",
	                   "hasPermissionToChangePriority",
	                   //"hasPermissionToPassChangePriorityPermission",
	                   "hasPermissionToPassComplainToOtherDepartment",
	                   //"hasPermissionToPassPassComplainPermissionToOtherDepartment"
	                   ];
	
	var permissionLabel = {
			
		"hasPermissionToChangeStatus":"Can Change Status",
		"hasPermissionToPassStatusChangingPermission":"Can Pass Status Change",
		"hasPermissionToForwardComplain" : "Can Forward Complain",
		"hasPermissionToPassComplainForwardingPermission":"Can Pass Complain Forward",
		"hasPermissionToPassComplain" : "Can Pass Complain",
		"hasPermissionToPassComplainPassingPermission":"Can Pass Complain Passing permission",
		"hasPermissionToAssignComplain" : "Can Assign Complain",
		"hasPermissionToPassComplainAssigningPermission" : "Can Pass Complain Assign Permission",
		"hasPermissionToAddComplain" : "Can Add Complain",
		"hasPermissionToPassAddComplainPermission":"Can Pass Complain Add Permission",
		"hasPermissionToChangePriority":"Can Change Priority",
		"hasPermissionToPassChangePriorityPermission" : "Can Pass Priority Change Permission",
		"hasPermissionToPassComplainToOtherDepartment" : " Can Pass Complain To Other Department ",
		"hasPermissionToPassPassComplainPermissionToOtherDepartment" : "Can Pass Complain Permission To Other Department"
	}
	
	$(document).ready( function(){
		$("#reset-copy").click(function(){
			$('#copy-dept-form').trigger('reset');
		});
		
		$('#find-preview').click(function(){
			var formData = $('#copy-dept-form').serialize();
			var deptID = $('#deptID-copy').val();
			$('#previewModalDiv').remove();
			$('#parentPreview').append("<div id='previewModalDiv'></div>");
			$('#previewModal').modal('show');
			var treeBuilder = new TreeBuilder();
			treeBuilder.setDepartmentID(deptID);
			treeBuilder.setDivID("previewModalDiv");
			treeBuilder.setAllDesignationRootURL( context + "/CrmDesignation/getDesignations.do?departmentID="+deptID);
			treeBuilder.buildPreviewTree();
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
		function autocompleteDept(){
			$("#deptName-copy").autocomplete({
	    		source : function(request, response) {
	    			$('#deptID-copy').val('');
	    			var url = context+"/CrmDesignation/department/autocompleteDept.do";
	    			var formData={};
	    			formData.deptName = request.term;
	    			formData.districtID = $("input[name='districtID-copy']").val();
	    			formData.upazilaID = $("input[name='upazilaID-copy']").val();
	    			formData.unionID = $("input[name='unionID-copy']").val();
	    			
	    			processList(url,formData,response,"POST");
	    		},
	    		minLength : 1,
	    		select : function(e, ui) {
	    			$('#deptName-copy').val(ui.item.departmentName);
	    			$('#deptID-copy').val(ui.item.ID);
	    			return false;
	    		},
	    	}).autocomplete("instance")._renderItem = function(ul, data) {
	    		return $("<li>").append("<a>" + data.departmentName+ "</a>").appendTo(ul);
	    	};
		}
		
		var districtNameCopy = $('#districtName-copy');
		var districtIDCopy = $('#districtID-copy');
		var upazilaNameCopy = $('#upazilaName-copy');
		var upazilaIDCopy = $('#upazilaID-copy').val('');
		var unionNameCopy = $('#unionName-copy');
		var unionIDCopy = $('#unionID-copy');
		var departmentNameCopy = $('#deptName-copy');
		var departmentIDCopy = $('#deptID-copy');
		
		
		districtNameCopy.change(function(){
			if(	$.trim(	districtNameCopy.val()	).length ==0	){
				districtOnChange();
			}
		});
		upazilaNameCopy.change(function(){
			if(	$.trim(	upazilaNameCopy.val()	).length ==0	){
				upazilaOnChange();
			}
		});
		unionNameCopy.change(function(){
			if(	$.trim(	unionNameCopy.val()	).length ==0	){
				unionOnChange();
			}
		});
		function districtOnChange(){
			districtIDCopy.val('');
			upazilaNameCopy.val('');
			upazilaOnChange();
		};
		
		function upazilaOnChange(){
			upazilaIDCopy.val('');
			unionNameCopy.val('');
			unionOnChange();
		}
		function unionOnChange(){
			unionIDCopy.val('');
			departmentNameCopy.val('');
			departmentOnChange();
		}
		function departmentOnChange(){
			departmentIDCopy.val('');
		}
		
		function fireAutocompleteRegion(){
			districtNameCopy.autocomplete({
				source : function(request, response) {
					districtOnChange();
					var url = '../../CrmDesignation/department/autoComplete.do';
					var formData={};
					formData.name = request.term;
					formData.categoryType = 1;
					processList(url,formData,response,"POST");
				},
				minLength : 1,
				select : function(e, ui) {
					districtNameCopy.val(ui.item.name);
					districtIDCopy.val(ui.item.ID);
					return false;
				},
			}).autocomplete("instance")._renderItem = function(ul, data) {
				return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
			};
			
			upazilaNameCopy.autocomplete({
				source : function(request, response) {
					upazilaOnChange();
					var url = '../../CrmDesignation/department/autoComplete.do';
					var formData={};
					formData.name = request.term;
					formData.categoryType=2;
					formData.parentID = $('#districtID-copy').val();
					processList(url,formData,response,"POST");
				},
				minLength : 1,
				select : function(e, ui) {
					upazilaNameCopy.val(ui.item.name);
					upazilaIDCopy.val(ui.item.ID);
					return false;
				},
			}).autocomplete("instance")._renderItem = function(ul, data) {
				return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
			};
			
			unionNameCopy.autocomplete({
				source : function(request, response) {
					unionOnChange();
					var url = '../../CrmDesignation/department/autoComplete.do';
					var formData={};
					formData.name = request.term;
					formData.categoryType=3;
					formData.parentID = $('#upazilaID-copy').val();
					processList(url,formData,response,"POST");
				},
				minLength : 1,
				select : function(e, ui) {
					unionNameCopy.val(ui.item.name);
					unionIDCopy.val(ui.item.ID);
					return false;
				},
			}).autocomplete("instance")._renderItem = function(ul, data) {
				return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
			};
			
		}
		$("input:radio[name=treeOption]").change(function(){
			var self = $(this).val();
			if(self == 1){
				createNewDesignationTree();
			}else if(self == 2){
				$('#wrapper-copy-dept').show();
				fireAutocompleteRegion();
				autocompleteDept();
			}
		});
		
				
		
		
		var isAdmin = "<%=loginDTO.getIsAdmin() %>";
		var context = "<%=request.getContextPath() %>";
		var isNOC = "<%=loginDTO.isNOC() %>";
		var isCrmAdmin = <%=(loginDTO.getMenuPermission(PermissionConstants.CRM_EMPLOYEE) != -1)%>;
<%-- 		var crmDesignationID = "<%=loginDTO.getCrmDesignationID() %>"; --%>
		
		if( !isCrmAdmin ){
			
			$(".treeAddBtn").hide();
		}
		
		var builder = new TreeBuilder();
		
		builder.setContainerID( "con" );
		builder.setContainerDesignationDiv( "desCon" );
		builder.setDepartmentID(<%=request.getParameter("departmentID")%>);
		builder.setIsAdmin( isAdmin );
		
		builder.setAllEmployeeRootURL( context + "/CrmEmployee/getAllEmployeeRoots.do?departmentID=<%=request.getParameter("departmentID")%>");
		builder.setAllDesignationRootURL( context + "/CrmDesignation/getDesignations.do?departmentID=<%=request.getParameter("departmentID")%>");
		
		builder.setEmployeeModal( "employeeAddModal" );
		builder.setEmployeeForm( "employeeAddForm" );
		
		builder.setDesignationModal( "designationAddModal" );
		builder.setDesignationForm( "designationAddForm" );
		
		builder.setContext( context );
		builder.buildEmployeeTrees();
		builder.buildDesignationTrees();
		builder.groupTree();
		
		$( document ).on( "submit", ".employeeAddForm", function( event ){
			
			event.preventDefault();
			builder.addEmployee();
		});
		
		$( document ).on( "submit", ".designationAddForm", function( event ){
			
			event.preventDefault();
			builder.addDesignation();
		});
		
		$( document ).on( "click", ".employee-remove", function( event ){
			
			builder.removeEmployee( event, $(this) );
		});
		
		$( document ).on( "click", ".designation-remove", function( event ){
			
			builder.removeDesignation( event, $(this) );
		});
		
		/************Ajaira styling, rotate remove icon - Alam. Can delete if you want***********/
		$( document ).on( "mouseenter", ".designation-remove,.designation-add, .employee-remove, .employee-add", function( event ){
			
			$(this).addClass( "fa-spin" );
		});
		
		$( document ).on( "mouseleave", ".designation-remove,.designation-add, .employee-remove, .employee-add", function( event ){
			
			$(this).removeClass( "fa-spin" );
		});
		/************Ajaira styling end***********/
		
		$( document ).on( "click", "#newEmployeeTree", function( event ){
			
			$("#employeeAddForm")[0].reset();
			
			var checkboxes = $("#employeeAddForm").find('input:checkbox');
		    
		    for( var i =0; i<checkboxes.length; i++ ){
		    	
		    	checkboxes[i].checked = false;
				var span = $(checkboxes[i]).parent("span")[0];
				$(span).removeClass( 'checked' );
		    }
		    
			builder.createNewEmployeeTree();
		});
		function createNewDesignationTree(){
			$("#designationAddForm")[0].reset();
			
			var checkboxes = $("#designationAddForm").find('input:checkbox');
		    
		    for( var i =0; i<checkboxes.length; i++ ){
		    	
		    	checkboxes[i].checked = false;
				var span = $(checkboxes[i]).parent("span")[0];
				$(span).removeClass( 'checked' );
		    }
			
			builder.createNewDesignationTree();
		}
		$( document ).on( "click", "#newDesignationTree", function( event ){
			
			
		});
		
		
		$( "#selectAll_des" ).on( "change",  function( e ){
			
			var checked = this.checked;
			
			var checkboxes = $("#permission-container-designation").find('input:checkbox');
		    
		    for( var i =0; i<checkboxes.length; i++ ){
		    	
		    	checkboxes[i].checked = checked;
		    }
		});
		
		$( "#selectAll" ).on( "change",  function( e ){
			
			var checked = this.checked;
			
			var checkboxes = $("#permission-container-employee").find('input:checkbox');
		    
		    for( var i =0; i<checkboxes.length; i++ ){
		    	
		    	checkboxes[i].checked = checked;
		    }
		});
		
		$("#editBtn").click(function(){
			$("#departmentAddModal").modal('show');
			return false;
		});
	});
	
</script>