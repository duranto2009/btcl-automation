<%@page import="distance.form.UpazilaDistanceDTO"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>

<%
	boolean isLoadedAfterUpdate = false; 
	String districtName="";
	String districtID="-1";
	
	if((String) request.getSession().getAttribute("isLoadedAfterUpdate")=="true"){
		isLoadedAfterUpdate = true;
	}
	if(isLoadedAfterUpdate){
		districtName=(String) request.getSession().getAttribute("districtName");
		districtID=(String) request.getSession().getAttribute("districtID");
		request.getSession().removeAttribute("isLoadedAfterUpdate");
	}
%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-map-marker "></i>Update Distance from District to Upazila
		</div>
	</div>
	<div class="portlet-body form">
		<form  id="upazilaUpdateForm" action="../Distance/UpdateUpazilaDistance.do" method="post"
			class="form-horizontal">
			<div class="form-body">
				<div class="row">
					<label class="col-md-3 control-label" style="font-size: 22px; padding: 0px;">From</label>
					<div class="col-md-6">
						<input type="text" id="districtName" name="districtName" class=" form-control" value="<%=districtName%>"/> 
						<input type="hidden" id="districtId" name="districtID" class=" form-control" value="<%=districtID%>"/>
					</div>
				</div>
				<!-- 			Dynamic Part Begins-->
				<div class="row" id="dynamicUpazila">
<!-- ......................................... -->
				</div>
			</div>
			

			
			
			<div class="form-actions fluid">
				<div class="row">
					<div class="col-md-offset-4 col-md-8">
						<a class="btn btn-cancel-btcl" type="button"
							href="<%=request.getHeader("referer")%>">Back</a>
						<button class="btn btn-submit-btcl" type="submit">Update</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<script type="text/javascript">

function processList(url, formData, callback, type) {
    $.ajax({
        type: typeof type != 'undefined' ? type : "POST",
        url: url,
        data: formData,
        dataType: 'JSON',
        success: function (data) {
            callback(data.payload);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
                + ", Message: " + errorThrown);
        },
        failure: function (response) {
            toastr.error(response);
        }
    });
}

function ResponseCallback(data) {

	console.log(data);

	if (data['responseCode'] == 1) {
		toastr.success(data['msg']);
	} else {
		toastr.error(data['msg']);
		refreshAfterUpdate();
	}
}	

function generateForm(data){
	$("#dynamicUpazila").html("");
	$("#dynamicUpazila").append("<br/>");

	var array=data.payload;
	console.log(array);
	
	$.each(array, function(index, value){
		var newUpazila = "";			
		newUpazila+="<div class='col-xs-6 col-sm-4 col-md-4 col-lg-3 pull-left' style='padding: 5px'>";			
		newUpazila+="<div class='col-md-6'> <label class='control-label col-md-12'>"+value.upazilaName+"</label></div>";
		newUpazila+="<div class='col-md-6'> <input class='form-control distanceALL col-md-12' type='text' name='"+value.upazilaId+"' placeholder='distance in km' value='"+value.distance+"' required></div>"
		newUpazila+="</div>";
		
		$("#dynamicUpazila").append(newUpazila);
	});

}


function populateAllUpazilas(districtID){
	var url=context+'Distance/PopulateDistrictToUpazilaDistance.do';
	var formData={};
	formData.districtID=districtID;
	formData.mode='upazila';
	
	callAjax(url,formData,generateForm,"GET");
}

function refreshAfterUpdate(){
	 if(($("#districtId")).val()!="-1"){
		 populateAllUpazilas($("#districtId").val());
	 }
}

$(document).ready(function() {

	$("#upazilaUpdateForm").on("submit", function(event) {
		event.preventDefault();
		var upazilaForm = $("#upazilaUpdateForm");
		var url = $(this).attr('action');
		var param = $("#upazilaUpdateForm").serialize();

		callAjax(url, param, ResponseCallback, "POST");
	});

	$("#districtName").autocomplete({
		source : function(request, response) {
			$('.distanceALL').attr('disabled', false);
			var url = context + 'Distance/PopulateDistrictList.do';
			var formData = {};
			formData.locationType = 'district';
			formData.districtValue = request.term;
			//callAjax(url, formData, response);
			processList(url, formData, response, 'GET');
		},
		minLength : 1,
		select : function(e, ui) {
			$("#districtName").val(ui.item.name);
			$("#districtId").val(ui.item.id);
			populateAllUpazilas($("#districtId").val());
			return false;
		},

	}).autocomplete("instance")._renderItem = function(ul,
			item) {
		console.log(item);
		return $("<li>").append("<a>" + item.name + "</a>")
				.appendTo(ul);
	};

});
</script>









