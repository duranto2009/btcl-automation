<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>

<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-comments-o"></i>Comments
		</div>
	</div>
	<div class="portlet-body">
		<form method="post" action="../../Comment.do?mode=add" id="commentForm" enctype="multipart/form-data">
			<div class="form-body">
				<input type="hidden" name="moduleID" value="<%=ModuleConstants.Module_ID_VPN %>" id="moduleID"> <input
					type="hidden" name="entityTypeID" value="<%=EntityTypeConstant.VPN_CLIENT %>" id="entityTypeID"> <input
					type="hidden" name="entityID" value="${clientID}" id="entityID">
				<div class="form-group">
					<input class="form-control spinner" type="text" id="heading" name="heading" placeholder="Subject">
				</div>
				<div class="form-group">
					<textarea class="form-control spinner" rows="3" id="description" name="description" placeholder="Comment"></textarea>
				</div>
				<div class="form-group">
					<div class="row">
						<div class="col-md-8">
							<input id="lefile" type="file" name="document" style="display: none">
							<div class="input-append">
								<input id="attachment" class="form-control" type="text" placeholder="Browse file to upload" disabled>
							</div>
						</div>
						<div class="col-md-4">
							<a class="btn btn-default btn-md" onclick="$('input[id=lefile]').click();">Browse</a>
						</div>
					</div>
					<span style="padding-left: 5px; padding-top: 5px; color: #f36a5a; font-size: 11px;">If multiple file then
						zip them</span>
					<script type="text/javascript">
			$('input[id=lefile]').change(function() {
			    $('#attachment').val($(this).val());
			});
		    </script>
				</div>
				<div class="form-group">
					<input type="submit" class="btn btn-column  btn-block sbold uppercase" id="submit-comment">
				</div>

			</div>
		</form>
		<hr>
		<div class="timeline">
			<!-- TIMELINE ITEM -->
			<div class="timeline-item" id="load-comment">
				<!-- load comment here -->
			</div>
			<input type="hidden" id="next-count-comment" value="0">
			<hr>
			<a class="btn btn-column-light btn-block" id="load-more-comment-btn"><b>Loading More<!-- comment --></b></a>
			<!-- END TIMELINE ITEM -->
		</div>
	</div>
</div>