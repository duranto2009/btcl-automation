<%@page import="crm.CrmComplainDTO"%>
<div class="row">
	<div class="col-md-12">
		<div class="portlet box portlet-btcl">

			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-search-plus"></i> Add Complain
				</div>
				<div class="tools">
					<a class="collapse" href="javascript:;" data-original-title=""
						title=""> </a>
				</div>
			</div>

			<div class="portlet-body form">
				<form class="form-horizontal" id="crmComplainAddForm"
					action="<%=request.getContextPath()%>/CrmComplain/CreateComplain.do"
					method="post">
					<div class="form-body">
						<div class="form-group">
							<label for="cmDepartment" class="col-sm-2 control-label">Description</label>
							<div class="col-sm-10">
								<textarea class="form-control border-radius textarea"
									id="currentDescription" name="currentDescription"></textarea>
							</div>
						</div>
						<div class="form-group">
								<label class="col-sm-2 control-label">Priority</label>
								<div class="col-sm-10">
									<select class="form-control pull-right" name="priority">
										<option value="<%=CrmComplainDTO.NORMAL%>">Normal</option>
										<option value="<%=CrmComplainDTO.LOW%>">Low</option>
										<option value="<%=CrmComplainDTO.HIGH%>">High</option>
									</select>
								</div>
							</div>
						<div class="form-group">
							<label for="cmDepartment" class="col-sm-2 control-label">Complain Resolver</label>
							<div class="col-sm-10">
								<input id="complainResolverName" type="text" class="form-control"
									placeholder="Type to add" name="complainResolverName"> <input
									id="complainResolverID" type="hidden" class="form-control" name="complainResolverID">
							</div>
						</div>
						<div class='form-group'>
							<label class='col-sm-2 control-label'>Tag</label>
							<div class='col-sm-10'>
								<input class='form-control' placeholder='' name='tag' type='text'>
							</div>
						</div>
					</div>
					<div class="form-actions right">
						<button class="btn btn-reset-btcl" type="reset">Reset</button>
						<button class="btn btn-submit-btcl" type="submit">Submit</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$("#crmComplainAddForm").on( "submit", function( event ){
    event.preventDefault();

    var url = $(this).attr( 'action' );
    var param = {};

    param['currentDescription'] = $("#currentDescription").val();
    param['complainResolverID'] = $("#complainResolverID").val();

    console.log( url );
    console.log( param );

    callAjax( url,param,complainAddCallback,"POST" );
});

function complainAddCallback( data ){

    console.log( data );

    if( data['responseCode'] == 1 ){

        toastr.success(data['msg']);
    }
    else{

        toastr.error( data['msg'] );
    }
}
$(document).ready(function() {
	function processList(url, formData, callback, type) {
	    $.ajax({
			type : typeof type != 'undefined' ? type : "POST",
			url : url,
			data : formData,
			dataType : 'JSON',
			success : function(data) {
			    callback(data.payload);
			},
			error : function(jqXHR, textStatus, errorThrown) {
			    toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
				    + ", Message: " + errorThrown);
			},
			failure : function(response) {
			    toastr.error(response);
			}
	    });
	}
    $("#complainResolverName").autocomplete({
        source: function (request, response) {
            $("#complainResolverID").val(-1);
            var term = request.term;
            var url = context + 'CrmEmployee/GetDescendantEmployeesByPartialName.do?';
            var param = {};
            param['complainResolverName'] = $("#complainResolverName").val();
            
            processList(url, param, response, "GET");
            
        },
        minLength: 1,
        select: function (e, ui) {
            $('#complainResolverName').val(ui.item.employeeName);
            $('#complainResolverID').val(ui.item.employeeID);
            return false;
        },
    }).autocomplete("instance")._renderItem = function (ul, data) {
        	return $("<li>").append("<a>" + data.employeeName + "</a>").appendTo(ul);
    };
})
</script>


