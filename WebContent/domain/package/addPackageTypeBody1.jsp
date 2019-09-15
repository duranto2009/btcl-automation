<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackageDetails"%>
<%@page import="domain.DomainService"%>
<%
	DomainService domainService = new DomainService();
	DomainPackageTypeDTO domainPackageDTO;
	List<DomainPackageTypeDTO> docuDomainPackageTypeDTOs = domainService.getDomainPackageTypes();
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/datatables/datatables.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css"
	rel="stylesheet" type="text/css" />
<!-- END PAGE LEVEL PLUGINS -->
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">Domain Package Type</div>
	</div>
	<div class="portlet-body">
		<!-- New Package Type Design -->
		<div class="row">
			<!-- BEGIN EXAMPLE TABLE PORTLET-->
			<div class="col-md-12">
				<div class="portlet light portlet-fit bordered">
					<div class="portlet-body">
						<div class="table-toolbar">
							<div class="row">
								<div class="col-md-6">
									<div class="btn-group">
										<button id="sample_editable_1_new" class="btn green">
											Add New <i class="fa fa-plus"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
						<table class="table table-striped table-bordered"
							id="sample_editable_1">
							<thead>
								<tr>
									<th>Name</th>
									<th>TLD</th>
									<th>Document Required</th>
									<th>Edit</th>
									<th>Delete</th>
								</tr>
							</thead>
							<tbody>
								<%
								for (DomainPackageTypeDTO domainPackageTypeDTO : docuDomainPackageTypeDTOs) {
								%>

								<tr>
									<td><input type="hidden" name="ID"
										value="<%=domainPackageTypeDTO.getID()%>"><input
										type="text" name="packageName" class="form-control"
										value="<%=domainPackageTypeDTO.getPackageName()%>"
										autocomplete="off" disabled="disabled"></td>
									<td><select class="form-control" name="tld"
										disabled="disabled">
											<option value="1"
												<%if (domainPackageTypeDTO.getTld() == 1) {%> selected <%}%>>.bd</option>
											<option value="2"
												<%if (domainPackageTypeDTO.getTld() == 2) {%> selected <%}%>>.বাংলা</option>
									</select>
									<td>
										<%
										if (domainPackageTypeDTO.isDocumentRequired()) {
										%> 
										<input type="checkbox" name="documentRequired" value="1"
											checked="checked" disabled="disabled"> 
										<%
										 } else {
										 %> 
										 <input type="checkbox" name="documentRequired" value="0" disabled="disabled"> 
										 <%
										 }
										 %>
									</td>
									<td><a class="edit" href=""> Edit </a></td>
									<td><a class="delete" href=""> Delete </a></td>
								</tr>
								<%
								}
								%>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- END EXAMPLE TABLE PORTLET-->
		</div>
		<!-- /New Package Type Design -->
	</div>
</div>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script
	src="<%=request.getContextPath()%>/assets/global/scripts/datatable.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/assets/global/plugins/datatables/datatables.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js"
	type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- Custom Js -->
<script src="packageType.js" type="text/javascript"></script>
<!-- /Custom Js -->