<%@page import="util.ServiceDAOFactory"%>
<%@page import="crm.service.CrmEmployeeService"%>
<%@page import="crm.repository.CrmDepartmentRepository"%>
<%@page import="crm.CrmDepartmentDTO"%>
<%@page import="crm.CrmDesignationDTO"%>
<%@page import="crm.repository.CrmAllDesignationRepository"%>
<%@page import="crm.repository.CrmAllEmployeeRepository"%>
<%@page import="crm.CrmEmployeeDTO"%>
<%@page import="crm.inventory.CRMInventoryItem"%>
<%@page import="inventory.*"%>
<%@page import="java.util.Map"%>

<%
CrmEmployeeDTO crmEmployeeDTO= (CrmEmployeeDTO) request.getAttribute("employee");
CrmDesignationDTO crmDesignation = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(crmEmployeeDTO.getInventoryCatagoryTypeID());
CrmDesignationDTO rootCrmDesignation = CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignation.getDesignationID());
CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootCrmDesignation.getDesignationID());

CrmEmployeeService crmEmployeeService = (CrmEmployeeService)ServiceDAOFactory.getService(CrmEmployeeService.class);
Map<Integer , Map<Long, InventoryItem> > resultMap = crmEmployeeService.getMapOfInventoryItemToInventoryItemIDToCategoryTypeID();
String districtName = (crmDepartmentDTO.getDistrictID()!=null?  resultMap.get(InventoryConstants.CATEGORY_ID_DISTRICT).get(crmDepartmentDTO.getDistrictID()).getName():"N/A");
String upazilaName = (crmDepartmentDTO.getUpazilaID() != null?  resultMap.get(InventoryConstants.CATEGORY_ID_UPAZILA).get(crmDepartmentDTO.getUpazilaID()).getName():"N/A");
String unionName = (crmDepartmentDTO.getUnionID() !=null     ?  resultMap.get(InventoryConstants.CATEGORY_ID_UNION).get(crmDepartmentDTO.getUnionID()).getName():"N/A");


%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-user"></i>CRM Employee View
		</div>
		
	</div>
	
	<div class="portlet-body">
		<div class="table-responsive">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<th scope="row">Name</th>
						<td><%=crmEmployeeDTO.getName() %></td>
					</tr>
					<tr>
						<th scope="row">Designation</th>
						<td><%=crmDesignation.getName()%></td>
					</tr>
					
					<tr>
						<th scope="row">District</th>
						<td><%=districtName%></td>
					</tr>
					<tr>
						<th scope="row">Upazila</th>
						<td><%=upazilaName %></td>
					</tr>
					<tr>
						<th scope="row">Union</th>
						<td><%=unionName %></td>
					</tr>
					
					<tr>
						<th scope="row">Department</th>
						<td><%=crmDepartmentDTO.getDepartmentName()%></td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</div>
</div>