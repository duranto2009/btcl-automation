<div class="portlet box purple">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i>Rate Plan Format Add
		</div>
	</div>
	<div class="portlet-body form">
		<!-- BEGIN FORM-->
		<form onsubmit="return validate();" class="form-horizontal" 	action="/ILCR/AddDRPFormat.do" method="POST" name="drpFormatForm">
			<div class="form-body">
				<div class="well well-xs">
					<p>Star(*) marked fields must be present in the format. For
						fields which are absent from the format, write 0 or keep the
						position box empty</p>
				</div>
				<div class="form-group">
					<label class="col-md-4 control-label">Template Name</label>
					<div class="col-md-4">
						<input type="text" class="form-control" value=""
							name="DRPFormatName">
					</div>
				</div>

				<div class="form-actions fluid">
					<div class="row">
						<div class="col-md-offset-4 col-md-8">
							<button name="B2" type="submit" class="btn green">Submit</button>
							<button name="B1" value="Reset" type="button" class="btn default">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</form>
		<!-- END FORM-->
	</div>
</div>