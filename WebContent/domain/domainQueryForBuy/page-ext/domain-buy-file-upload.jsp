<%@page import="file.FileTypeConstants"%>
<div class="row">
	<div class="col-md-12">
		<h4>General Information</h4>
		<div class="table">
			<table
				class="table table-striped table-bordered table-checkable dataTable no-footer">
				<tbody>
					<%if(isFileRequired != null && isFileRequired) { %>
					<tr>
						<td colspan="1" width="25%"><label class="control-label">Document<span
								class="required" aria-required="true"> * </span></label></td>
						<td colspan="1" width="75%">
							<!-- File upload -->
							<div class="row">
								<div class="col-md-10">
									<hr>
								</div>
								<div class="col-md-10">

									<div class="col-md-3" style="padding: 0px;">
										<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
										<span class="btn btn-warning-btcl  fileinput-button">
											 <i class="fa fa-upload"></i> <span> Add Document </span> 
											 <input class="jFile" id="doc" type="file" name="<%=FileTypeConstants.DOMAIN_BUY.DOMAIN_BUY_DOC %>">
										</span>
									</div>
									<div class="col-md-9 has-error" id="fileRequired"></div>

									<div class="col-md-9">
										<!-- The global file processing state -->
										<span class="fileupload-process"></span>
										<!-- The global progress state -->
										<div class="col-lg-12 fileupload-progress fade">
											<!-- The global progress bar -->
											<div class="progress progress-striped active"
												role="progressbar" aria-valuemin="0" aria-valuemax="100">
												<div class="progress-bar progress-bar-success"
													style="width: 0%;"></div>
											</div>
											<!-- The extended global progress state -->
											<div class="progress-extended">&nbsp;</div>
										</div>
									</div>
									<!-- The table listing the files available for upload/download -->
									<table role="presentation"
										class="table table-striped margin-top-10">
										<tbody class="files"></tbody>
									</table>
								</div>
								<jsp:include page="../../../common/ajaxfileUploadTemplate.jsp" />
							</div> <!-- /File upload -->
						</td>
					</tr>
					<%} %>

				</tbody>
			</table>
		</div>
	</div>
</div>