<%@page import="crm.CrmDepartmentDTO"%>

<%
CrmDepartmentDTO crmDepartmentDTO = (CrmDepartmentDTO)request.getAttribute("department");
%>


<div class="modal fade" id="previewModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Preview</h4>
			</div>
			<div class="modal-body" id="parentPreview">
				<div id ="previewModalDiv">
					
				</div>			
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="copy-btn">Copy</button>
			</div>
		</div>
	</div>
</div>
<script>

	$('#copy-btn').click(function(){
		var formData = {};
		formData.desDeptID = <%=crmDepartmentDTO.getID()%>;
		formData.srcDeptID =  $('#deptID-copy').val();
		
		var url = context+"CrmDesignation/department/copyOrganogram.do";
		callAjax(url, formData, function(data){
			if(data.responseCode == 1){
				$('#previewModal').modal('hide');
				toastr.success(data.msg);
				setTimeout(function(){
					location.reload();	
				}, 1000);
			}else {
				toastr.error(data.msg);
			}
		}, "POST");
		return false;
	});
</script>