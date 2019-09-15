<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainService"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
DomainService domainService = new DomainService();
List<DomainPackageTypeDTO> domainPackageTypeDTOs = domainService.getDomainPackageTypes();
Integer msgType = (Integer)request.getAttribute("msg");
%>
<%if(msgType != null && msgType == -1){ %>
<div class="alert alert-block alert-danger fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong >Error!</strong>
</div><%}else if(msgType != null && msgType == 1){ %>
<div class="alert alert-block alert-success fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong >Success!</strong>
</div>
<%} %>

<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">Add Domain</div>
	</div>
	<div class="portlet-body" style="min-height: 400px;">
		<div class="row">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-6">
						<div class="portlet light bordered">
							<div class="portlet-title">
								<div class="caption caption-md">
									<i class="icon-bar-chart theme-font hide"></i> <span
										class="caption-subject font-blue-madison bold uppercase">Using
										CSV File </span>
								</div>
							</div>
							<div class="portlet-body">
								<div class="table-responsive">
									<form method="post" enctype="multipart/form-data"
										action="../../AddDomain.do?mode=addDomainUsingFile">
										<table
											class="table table-striped table-bordered table-checkable dataTable no-footer"
											id="package">
											<thead>
											</thead>
											<tbody>
												<tr>
													<td>File(CSV)</td>
													<td>
														<div class="fileinput fileinput-new"
															data-provides="fileinput" style="width: 100%;">
															<div class="input-group">
																<div class="form-control uneditable-input input-fixed"
																	data-trigger="fileinput">
																	<i class="fa fa-file fileinput-exists"></i>&nbsp; <span
																		class="fileinput-filename"> </span>
																</div>
																<span class="input-group-addon btn default btn-file">
																	<span class="fileinput-new"> + </span> <span
																	class="fileinput-exists"> + </span> <input type="file"
																	name="csvDocument" accept="*" value="" id="attachment" required>
																</span> <a href="javascript:;"
																	class="input-group-addon btn red fileinput-exists"
																	data-dismiss="fileinput"> x </a>
															</div>
														</div>
													</td>
												</tr>
												<tr>
													<td>Type</td>
													<td><select class="form-control" name="type">
													<%for(DomainPackageTypeDTO domainPackageTypeDTO : domainPackageTypeDTOs){ %>
													 		<option value="<%=domainPackageTypeDTO.getID() %>"><%=domainPackageTypeDTO.getPackageName() %></option>
													 <%} %>
													</select></td>
												</tr>
												<tr>
													<td colspan="2">
														<div>
															<button
																class="btn btn-sm btn-success filter-submit margin-bottom">Add
																Domain</button>
														</div>
													</td>
												</tr>
											</tbody>
										</table>
									</form>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="portlet light bordered">
							<div class="portlet-title">
								<div class="caption caption-md">
									<i class="icon-bar-chart theme-font hide"></i> <span
										class="caption-subject font-blue-madison bold uppercase">Single
										Domain Add </span>
								</div>
							</div>
							<div class="portlet-body">
								<div class="table-responsive">
									<form method="post"
										action="../../AddDomain.do?mode=addDomain">
										<table
											class="table table-striped table-bordered table-checkable dataTable no-footer"
											id="package">
											<thead>
											</thead>
											<tbody>
												<tr>
													<td>Domain Name</td>
													<td><input type="text"
														class="form-control form-filter input-sm"
														name="domainName" required></td>
												</tr>
												<tr>
													<td>Type</td>
													<td><select class="form-control" name="type">
													<%for(DomainPackageTypeDTO domainPackageTypeDTO : domainPackageTypeDTOs){ %>
													 		<option value="<%=domainPackageTypeDTO.getID() %>"><%=domainPackageTypeDTO.getPackageName() %></option>
													 <%} %>
													</select></td>
												</tr>
												<tr>
													<td colspan="2">
														<div>
															<button
																class="btn btn-sm btn-success filter-submit margin-bottom">Add
																Domain</button>
														</div>
													</td>
												</tr>
											</tbody>
										</table>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>