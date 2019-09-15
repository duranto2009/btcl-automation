<%@page import="util.ServiceDAOFactory"%>
<%@page import="distance.form.DistrictDistanceDTO"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.List"%>
<%@page import="distance.*"%>


<%
	
	DistanceService distanceService = (DistanceService)ServiceDAOFactory.getService(DistanceService.class);
	
	List<DistrictDistanceDTO> districts = distanceService.getAllDistricts();
	DistrictDistanceDTO defaultDistrict = null;
	String defaultDistName = null;
	Integer defaultDistId = null;
	String originatingDistrictStr = (String) request.getSession(true).getAttribute("originatingDistrictId");
	if (StringUtils.isNotBlank(originatingDistrictStr)) {
		for (int i = 0; i < districts.size(); i++) {
			if (districts.get(i) != null && (districts.get(i).getId() + "").equals(originatingDistrictStr)) {
				defaultDistrict = districts.get(i);
			}
		}
		defaultDistName = defaultDistrict.getName();
		defaultDistId = defaultDistrict.getId();

	} else if (districts != null && districts.size() > 0) {
		defaultDistrict = districts.get(0);
		defaultDistName = defaultDistrict.getName();
		defaultDistId = defaultDistrict.getId();
	}
%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-map-marker "></i>Update distance between Districts
		</div>
	</div>
	<div class="portlet-body form">
		<form id="districtUpdateForm" action="../Distance/UpdateDistrictDistance.do" method="post"
			class="form-horizontal">
			<div class="form-body">
				<div class="row">
					<label class="col-md-3 control-label" for="originatingDistrict"
						style="font-size: 22px; padding: 0px;">From</label>
					<div class="col-md-6">
						<input type="text" id="originatingDistrict"
							name="originatingDistrictStr" class=" form-control"
							value="<%=defaultDistName%>" /> <input type="hidden"
							id="originatingDistrictId" name="originatingDistrictId"
							class=" form-control" value="<%=defaultDistId%>" />
					</div>
				</div>
			</div>
			<div class="form-body">
				<div class="row">
					<%
						if (districts != null)
							for (int i = 0; i < districts.size(); i++) {
					%>
					<div class="col-xs-6 col-sm-4 col-md-4 col-lg-3 pull-left"
						style="padding: 5px">
						<div class="col-md-6">
							<label class="control-label col-md-12"><%=districts.get(i).getName()%>
							</label>
						</div>
						<div class="col-md-6">
							<input id="<%=districts.get(i).getId()%>"
								class="form-control distanceALL  col-md-12" type="text"
								name="distance_from_<%=districts.get(i).getId()%>"
								placeholder="km">
						</div>
					</div>
					<%
						}
					%>
				</div>
			</div>
			<div class="form-actions fluid">
				<div class="row">
					<div class="col-md-offset-4 col-md-8">
						<a class="btn btn-cancel-btcl" type="button"
							href="<%=request.getHeader("referer")%>">Back</a>
						<button class="btn btn-submit-btcl" type="submit" name="B2">Update</button>
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
	}
}	

$(document).ready(function() {
	$("#originatingDistrict").autocomplete({
	    source : function(request, response) {
			$('.distanceALL').attr('disabled', false);
			var url = context + 'Distance/PopulateDistrictList.do';
			var formData={};
			formData.districtValue=request.term;
			formData.locationType='district';
			//callAjax(url, formData, response);
			processList(url, formData, response, 'GET');
	    },
	    minLength : 1,
	    select : function(e, ui) {
			$("#originatingDistrict").val(ui.item.name);
			$("#originatingDistrictId").val(ui.item.id);
			loadDistance(ui.item.id);
			return false;
	    },

	}).autocomplete("instance")._renderItem = function(ul, item) {
	    console.log(item);
	    return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
	};
	
	function processDistrictDistance(data){
	 	$('.distanceALL').val(0);
	 	var array=data.payload;
		console.log(array);
	 	
	    $.each(array, function(i, item) {
			$('#' + item['destinationDistrictId']).val(item['distance']);
    	})
    	
	}
	function loadDistance(sourceDistrictID) {
	    $('#' + sourceDistrictID).attr('disabled', true);
	    
	    var url=context+'Distance/PopulateDistrictToDistrictDistance.do';
	    //var url=context+'PopulateDistance.do?id=' + id;
		var formData={};
		formData.sourceDistrictID=sourceDistrictID;
	    formData['mode']='district';
		callAjax(url, formData, processDistrictDistance, "GET");
	}
	
	loadDistance( <%=defaultDistId%>   );
	
	$("#districtUpdateForm").on("submit", function(event) {
		event.preventDefault();
		var url = $(this).attr('action');
		var param = $("#districtUpdateForm").serialize();

		callAjax(url, param, ResponseCallback, "POST");
	});

});
</script>









