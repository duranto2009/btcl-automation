<%@page import="coLocation.*, java.util.*"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String titleForm = request.getParameter("title")+" Form";
%>


<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=titleForm %>
		</div>
	</div>
	
	
	<div class="portlet-body form">
		<form class="form-horizontal" id="tableForm">
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">Mark As NOC</label>
					<label class="col-sm-6 control-label" style="text-align: left">
						<input  type="checkbox" name="isNOC" id="isNOC"/>
					</label>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">District</label>
					<div class="col-sm-6">
						<input type="text" class="form-control"
						name="districtName" id="districtName">
						<input type="hidden" id="districtID" name="districtID" value="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Upazila</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="upazilaName"
						name="upazilaName">
						<input type="hidden" id= "upazilaID" name="upazilaID" value="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Union</label>
					<div class="col-sm-6">
						<input type="text" class="form-control"
						name="unionName" id="unionName">
						<input type="hidden" id="unionID" name="unionID" value="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Department<span class="required" aria-required="true">*</span></label>
					<div class="col-sm-6">
						<input type="text" class="form-control"
						name="departmentName" required>
					</div>
				</div>
				
				
			</div>
			<div class="form-actions text-center">
				<button class="btn btn-reset-btcl" type="reset" id="resetBtn">Reset</button>
				<button class="btn btn-submit-btcl" type="submit" id="submitBtn">Add</button>
			</div>
			
		</form>
	</div>
</div>
<script src="../../assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../../scripts/util.js"></script>
<script>
$(document).ready(function(){
	$("#submitBtn").click(function(){
		var formData = $("#tableForm").serialize();
		if($('input[name=departmentName]').val().length == 0){
			$('input[name=departmentName]').closest('.form-group').addClass('has-error');
		}else {
			var url = "../../CrmDesignation/department/addDepartment.do";
			callAjax(url,formData, function(data){
				if(data['responseCode'] == 1 ){
					toastr.success(data['msg']);
					var redirectURL = "${context}CrmEmployee/getDepartmentView.do?departmentID=" + data.payload;
				    var redirectTime = 1000;
			        setTimeout(function (){
			            location.href = redirectURL
			        }, redirectTime);
				}else {
					toastr.error(data['msg']);
				}
			}, "POST");
				
		}
		
		return false;
	});
	
	$("#resetBtn").click(reset);
	function reset(){
		$("#tableForm").trigger("reset");
	}
	
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
	
	var districtName = $('#districtName');
	var upazilaName = $('#upazilaName');
	var unionName = $('#unionName');
	var districtID = $('#districtID');
	var unionID = $('#unionID');
	var upazilaID = $('#upazilaID');
	
	districtName.change(function(){
		if(	$.trim(	districtName.val()	).length ==0	){
			districtOnChange();
		}
	});
	upazilaName.change(function(){
		if(	$.trim(	upazilaName.val()	).length ==0	){
			upazilaOnChange();
		}
	});
	unionName.change(function(){
		if(	$.trim(	unionName.val()	).length ==0	){
			unionOnChange();
		}
	});
	function districtOnChange(){
		districtID.val('');
		upazilaName.val('');
		upazilaOnChange();
	};
	
	function upazilaOnChange(){
		upazilaID.val('');
		unionName.val('');
		unionOnChange();
	}
	function unionOnChange(){
		unionID.val('');
	}
	
	
	
	districtName.autocomplete({
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
			districtName.val(ui.item.name);
			districtID.val(ui.item.ID);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, data) {
		return $("<li>").append("<a>" + data.name+ "</a>").appendTo(ul);
	};
	
	upazilaName.autocomplete({
		source : function(request, response) {
			upazilaOnChange();
			var url = '../../CrmDesignation/department/autoComplete.do';
			var formData={};
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
			unionOnChange();
			var url = '../../CrmDesignation/department/autoComplete.do';
			var formData={};
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
