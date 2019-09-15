<!-- 
Need Package Type?
Need isNeedDocument?
 -->
 <%@page import="domain.DomainNameDTO"%>
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
		DomainNameDTO domainNameDTO = domainService.getDomainNameDtoByID(ID);
%>

<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">Update Domain</div>
	</div>
	<div class="portlet-body" style="min-height: 450px;">
		
		<div class="row">
			<!-- Used for update -->
			<div class="col-md-12 ">
				<div class="portlet light bordered">
					<div class="portlet-body form">
								<form method="post" action="../../DomainAction.do?mode=updateDomain">
									<table class="table table-striped table-bordered table-checkable dataTable no-footer">
										<thead>
										</thead>
										<tbody id="packageBody">
											<tr>
												<td>Domain Name</td>
												<td><input type="text"
													class="form-control form-filter input-sm" name="domainName" value="<%=domainNameDTO.getDomainName() %>">
												</td>
											</tr>
											<tr>
												<td>Domain Type</td>
												<td><select class="form-control" name="domainPackageType">
														<option value="-1" <% if(domainNameDTO.getDomainNameType() == -1){%>selected<%} %>>Partial Match Forbidden</option>
														<option value="0" <% if(domainNameDTO.getDomainNameType() == 0){%>selected<%} %>>Forbidden</option>
														<%for(DomainPackageTypeDTO domainPackageTypeDTO : domainPackageTypeDTOs){ %>
													 		<option value="<%=domainPackageTypeDTO.getID() %>" <% if(domainPackageTypeDTO.getID() == domainNameDTO.getDomainNameType()){%>selected<%} %>><%=domainPackageTypeDTO.getPackageName() %></option>
													 	<%} %>
													</select>
												</td>	
											</tr>
											<tr>
											<td colspan="2">
													<div>
														<input type="hidden" name="ID" value="<%=domainNameDTO.getID() %>">
														<button
															class="btn green-jungle btn-sm btn-success filter-submit margin-bottom">Update
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
			<!-- ./Package Update -->
			<%} %>
		</div>
		
	</div>
</div>
<script>
</script>