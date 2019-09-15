<!-- 
Need Package Type?
Need isNeedDocument?
 -->
 <%@page import="util.TimeConverter"%>
<%@page import="domain.DomainPackage"%>
<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackageDetails"%>
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
	String id = (String)request.getParameter("id");
	long ID = 0;
	if(id != null){
		ID = Long.parseLong(id);
		List<DomainPackageTypeDTO> domainPackageTypeDTOs = domainService.getDomainPackageTypes();
		DomainPackage domainPackage = domainService.getDomainPackageByID(ID);
		String sMsg = request.getParameter("msg");
		Integer msgType = 0;
		if(sMsg != null){
			msgType = Integer.parseInt(request.getParameter("msg"));
		}
%>
<%if(msgType != null && msgType == -1){ %>
<div class="alert alert-block alert-danger fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong >Error!</strong> Failed to add package.
</div><%}else if(msgType != null && msgType == 1){ %>
<div class="alert alert-block alert-success fade in">
     <button type="button" class="close" data-dismiss="alert"></button>
      <strong >Success!</strong> Package add successfully.
</div>
<%} %>
<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">Update Domain Package</div>
	</div>
	<div class="portlet-body" style="min-height: 450px;">
		
		<div class="row">
			<!-- Used for update -->
			<div class="col-md-12 ">
				<div class="portlet light bordered">
					<div class="portlet-body form">
								<form method="post" action="../../DomainAction.do?mode=updateDomainPackage">
									<table class="table table-striped table-bordered table-checkable dataTable no-footer">
										<thead>
										</thead>
										<tbody id="packageBody">
											<tr>
												<td colspan="1">Package Name</td>
												<td colspan="3"><input type="text"
													class="form-control form-filter input-sm" name="packageName" value="<%=domainPackage.getPackageName() %>" required>
												</td>
											</tr>
											<tr>
												<td>Package Type</td>
												<td><select class="form-control" name="packageTypeID" required>
														<%for(DomainPackageTypeDTO domainPackageTypeDTO : domainPackageTypeDTOs){ %>
													 		<option value="<%=domainPackageTypeDTO.getID() %>" <% if(domainPackageTypeDTO.getID() == domainPackage.getPackageTypeID()){%>selected<%} %>><%=domainPackageTypeDTO.getPackageName() %></option>
													 	<%} %>
													</select>
												</td>
												<td colspan="1">Activation Date</td>
												<td colspan="3"><input type="text" id="activationDate" value="<%= TimeConverter.getTimeStringFromLong(domainPackage.getActivationDate()) %>"
													name="activationDate" class="form-control pull-right"
													value="" autocomplete="off" required/></td>
												
											</tr>
											<% for(DomainPackageDetails domainPackageDetails : domainPackage.getDomainPackageDetailsList()){ %>
											<tr>
												<td>Year</td>
												<td><input type="text" class="form-control pull-right" name="year" autocomplete="off" value="<%=domainPackageDetails.getYear() %>"/></td>
												<td>Cost</td>
												<td><input type="text" class="form-control pull-right" name="cost" autocomplete="off" value="<%=domainPackageDetails.getCost() %>"/></td>
											</tr>
											<%} %>
										</tbody>
									</table>
								
									<table class="table table-striped table-bordered table-checkable dataTable no-footer">
										<thead></thead>
										<tbody>
											<tr>
												<td width="50%" ><a id="addMore"
													class="btn green-haze btn-sm btn-success filter-submit margin-bottom">Add More</a></td>
												<td width="50%" style="text-align: center;"><a id="removeLast"
													class="btn red-pink btn-sm btn-danger filter-submit margin-bottom">Remove
														Last</a></td>
											</tr>
											<tr>
												<td colspan="2">
													<div>
														<input type="hidden" name="ID" value="<%=domainPackage.getID() %>">
														<button
															class="btn green-jungle btn-sm btn-success filter-submit margin-bottom">Update
															Package</button>
													</div>
												</td>
											</tr>
										</tbody>
									</table>
								</form>
					</div>
				</div>
			</div>
			<!-- ./Package Update -->
			<%} %>
		</div>
		
	</div>
</div>
<script>
	$(document).ready(function() {
		
		$('#activationDate').datepicker({
			dateFormat : 'dd/mm/yy',
			autoclose : true
		});

		$('#addMore').click(function() {
			var row='<tr><td>Year</td><td><input type="text" class="form-control pull-right" name="year" value="" autocomplete="off" required/></td><td>Cost</td><td><input type="text" class="form-control pull-right" name="cost" value="" autocomplete="off" required/></td></tr>';
			$(row).appendTo("#packageBody");
		});
		
		$('#removeLast').click(function(){
			if($("#packageBody tr").length != 3){
	            $("#packageBody tr:last-child").remove();
	        }
	      	else
	        {
	            alert("You cannot delete first row");
	         }
		});
	});
</script>