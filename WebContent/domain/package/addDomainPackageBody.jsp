<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackageDetails"%>
<%@page import="util.TimeConverter"%>
<%@page import="domain.DomainPackage"%>
<%@page import="java.util.ArrayList"%>
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
ArrayList<DomainPackage> domainNextPackages = domainService.getNextDomainPackages();
ArrayList<DomainPackage> domainRunningPackages = domainService.getRunningDomainPackages();
List<DomainPackageTypeDTO> docuDomainPackageTypeDTOs = domainService.getDomainPackageTypes(); 
String context = "../../.." + request.getContextPath() + "/";
Integer msgType = (Integer)request.getAttribute("msg");
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
		<div class="caption">Domain Package Add</div>
	</div>
	<div class="portlet-body" style="min-height: 400px;">
		<div class="row">
			<div class="col-md-12">
				<div class="table-responsive">
					<form method="post" action="../../DomainAction.do?mode=addDomainPackage">
						<table id="packageAddTable" class="table table-checkable dataTable1 no-footer " >
							<thead>
							</thead>
							<tbody>
								<tr>
									<td colspan="1">Package Name</td>
									<td colspan="3"><input type="text"
										class="form-control form-filter" name="packageName"  autocomplete="off" placeholder="Write desire Package Name" required="required">
									</td>
									<td></td>
								</tr>
								<tr>
									<td width="15%" style="text-align:center;">Package Type</td>
									<td  width="35%"><select class="form-control" name="packageTypeID">
											<%try{for(DomainPackageTypeDTO domainPackageTypeDTO : docuDomainPackageTypeDTOs){ %>
										 		<option value="<%=domainPackageTypeDTO.getID() %>"><%=domainPackageTypeDTO.getPackageName() %></option>
										 	<%}}catch(Exception ex){
										 		
										 	}%>
										</select>
									</td>
									<td  width="15%" style="text-align:center;">Activation Date</td>
									<td width="35%">
										<div class="input-group date ">
											<div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											</div>
											<input type="text" id="activationDate"
												name="activationDate" class="form-control pull-right"
												value="" autocomplete="off" placeholder="dd/mm/yyyy" required />
										</div>
									</td>
									<td></td>
								</tr>
								<tr>
									<td style="text-align:center;">Year</td>
									<td><input type="text" class="form-control pull-right" name="year" value="" autocomplete="off" placeholder="Number of year" required /></td>
									<td style="text-align:center;">Cost</td>
									<td><input type="text" class="form-control pull-right" name="cost" value="" autocomplete="off" placeholder="Cost in BDT" required/></td>
									<td><a href="javascript:;"
										class="btn btn-icon-only red-pink year-remove"> <i class="fa fa-times"></i>
									</a></td>
								</tr>
							</tbody>
						</table>
						<table class="table table-checkable dataTable1 no-footer extra-table">
							<thead></thead>
							<tbody>
								<tr>
									<td width="10%" style="text-align: left"><a id="addMore" class="class="btn btn-icon-only green"><i class="fa fa-plus"></i>&nbsp;More Year</a></td>
									<td width="90%" style="text-align: right;">
										<div>
											<button
												class="btn green-jungle btn-sm btn-success filter-submit margin-bottom package-form-submit">Add
												Package</button>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<div class="col-md-12">
				<h4><strong>Running Packages</strong></h4>
				<%
				for(DomainPackage domainPackage : domainRunningPackages){
				%>
					<div class="table-responsive">
						<table class="table table-bordered dataTable no-footer" >
							<thead>
								
							</thead>
							<tbody>
								<tr style="background-color:  #dde4e4 ;">
									<td class="text-center">Package Name</td>
									<td class="text-center"><%= domainPackage.getPackageName() %></td>
								</tr>
								<tr >
									<td class="text-center">Package Type</td>
									<td class="text-center"><%= domainService.getDomainPackageTypeByID(domainPackage.getPackageTypeID()).getPackageName() %></td>
								</tr>
								<tr >
									<td class="text-center">Activation Date</td>
									<td class="text-center"><%= TimeConverter.getTimeStringFromLong(domainPackage.getActivationDate()) %></td>
								</tr>
								<tr>
									<td class="text-center"><strong>Year</strong></td>
									<td class="text-center"><strong>Cost</strong></td>
								</tr>
								<% for(DomainPackageDetails domainPackageDetails : domainPackage.getDomainPackageDetailsList()){ %>
									<tr>
										
										<td class="text-center"><%= domainPackageDetails.getYear() %></td>
										<td class="text-center"><%= domainPackageDetails.getCost() %></td>
									</tr>
								<%} %>
								<tr>
									<td colspan="2" style="text-align: right;">
									 <a class="btn btn-sm blue" href="<%=context %>domain/package/updateDomainPackage.jsp?id=<%=domainPackage.getID() %>">Edit</a>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				<%} %>
				<h4><strong>Upcoming Packages</strong></h4>
				<%
				for(DomainPackage domainPackage : domainNextPackages){
				%>
					<div class="table-responsive">
						<table
							class="table table-bordered dataTable no-footer" style="border: 2px solid #e7ecf1;">
							<thead>
								
							</thead>
							<tbody>
								<tr style="background-color:  #98b1b4;">
									<td  width="50%"><strong style="color:  #5e738b ;">Package Name</strong></td>
									<td style="text-align: center;"><%= domainPackage.getPackageName() %></td>
								</tr>
								<tr style="background-color:  #c5cece;">
									<td ><strong style="color:  #5e738b ;">Package Type</strong></td>
									<td style="text-align: center;"><%= domainService.getDomainPackageTypeByID(domainPackage.getPackageTypeID()).getPackageName() %></td>
								</tr>
								<tr style="background-color:  #dde4e4 ;">
									<td ><strong style="color:  #5e738b ;">Activation Date</strong></td>
									<td style="text-align: center;"><%= TimeConverter.getTimeStringFromLong(domainPackage.getActivationDate()) %></td>
								</tr>
								<tr>
								
									<td style="text-align: center;"><strong>Year</strong></td>
									<td style="text-align: center;"><strong>Cost</strong></td>
								</tr>
								<% for(DomainPackageDetails domainPackageDetails : domainPackage.getDomainPackageDetailsList()){ %>
									<tr>
										
										<td style="text-align: center;"><%= domainPackageDetails.getYear() %></td>
										<td style="text-align: center;"><%= domainPackageDetails.getCost() %></td>
									</tr>
								<%} %>
								<tr>
									<td colspan="2" style="text-align: right;">
									 <a class="btn btn-sm blue" href="<%=context %>domain/package/updateDomainPackage.jsp?id=<%=domainPackage.getID() %>">Edit</a>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				<%} %>
			</div>
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
			var row='<tr><td style="text-align:center;">Year</td><td><input type="text" class="form-control pull-right" name="year" value="" autocomplete="off" placeholder="Number of year" required/></td><td style="text-align:center;">Cost</td><td><input type="text" class="form-control pull-right" name="cost" value="" autocomplete="off" placeholder="Cost in BDT" required/></td><td><a href="javascript:;" class="btn btn-icon-only red-pink year-remove"> <i class="fa fa-times"></i></a></td></tr>';
			$(row).appendTo("#packageAddTable tbody");
		});
		
		/*$('.year-remove').click(function(){
			$(this).closest ('tr').remove ();
		});*/
		var table = $('#packageAddTable');
		table.on('click', '.year-remove', function(e) {
			e.preventDefault();
			if(table.find('tbody tr').length != 3){
				var nRow = $(this).closest('tr');
				nRow.remove();
	        }else{
	        	toastr.error("Add at least one cost");
	        }	
		});
		
		/*$('.extra-table').on('click', '.package-form-submit', function(e) {
			e.preventDefault();
	        toastr.error("Add at least one cost");
	
		});*/
		
		/*$('#removeLast').click(function(){
			if($("#packageBody tr").length != 3){
	            $("#packageBody tr:last-child").remove();
	        }
	      	else
	        {
	            alert("You cannot delete first row");
	         }
		});*/
	});
</script>