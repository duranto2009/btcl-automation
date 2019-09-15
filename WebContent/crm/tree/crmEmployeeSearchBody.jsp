<%@page import="crm.service.CrmEmployeeService"%>
<%@page import="crm.service.CrmDepartmentService"%>
<%@page import="crm.repository.CrmAllEmployeeRepository"%>
<%@page import="crm.CrmDepartmentDTO"%>
<%@page import="crm.repository.CrmDepartmentRepository"%>
<%@page import="crm.repository.CrmDesignationRepository"%>
<%@page import="crm.CrmDesignationDTO"%>
<%@page import="crm.inventory.CRMInventoryItem"%>
<%@page import="crm.repository.CrmAllDesignationRepository"%>
<%@page import="crm.CrmEmployeeDTO"%>
<%@ page import="login.*, util.*, common.*, common.repository.*, java.util.*, sessionmanager.*, inventory.*"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>

<style>
	th,td{
		text-align: center;
	}
</style>
<%
	String msg = (String)request.getAttribute("msg");
	String url = "CrmEmployee/searchEmployee";
	String navigator = SessionConstants.NAV_CRM_EMPLOYEE;
	String context = "../.." + request.getContextPath();
	System.out.println(context);
	LoginDTO localLoginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<jsp:include page="../../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
</jsp:include>



<div class="portlet box">
	<div class="portlet-body">
	<% if(msg != null){%>
		<div class="text-center" id="msgDiv">
			<p style="color: #5cb85c"><%=msg %></p>
		</div>
	<%} %>
		<div class="table-responsive">
			<table id="tableData" class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th>Name</th>
						<th>Designation</th>
						<th>District</th>
						<th>Upazila</th>
						<th>Union</th>
						<th>Department</th>
					</tr>
				</thead>
				<tbody>
				<%
				ArrayList<CRMInventoryItem> data = (ArrayList<CRMInventoryItem>) session.getAttribute(SessionConstants.VIEW_CRM_EMPLOYEE);
				CrmDepartmentService crmDepartmentService= (CrmDepartmentService)ServiceDAOFactory.getService(CrmDepartmentService.class);
				CrmEmployeeService crmEmployeeService = (CrmEmployeeService)ServiceDAOFactory.getService(CrmEmployeeService.class);
				
				Map<Integer, Map<Long, InventoryItem>> resultMap = crmEmployeeService.getMapOfInventoryItemToInventoryItemIDToCategoryTypeID();
				
				
				if(data != null){
					for(CRMInventoryItem crmInventoryItem: data){
						
						
						Integer inventoryCategoryID = crmInventoryItem.getInventoryCatagoryTypeID();
						CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(inventoryCategoryID);
						CrmDesignationDTO rootCrmDesignationDTO = CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
						CrmDepartmentDTO rootCrmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootCrmDesignationDTO.getDesignationID());
						String districtName = (rootCrmDepartmentDTO.getDistrictID()!=null?  resultMap.get(InventoryConstants.CATEGORY_ID_DISTRICT).get(rootCrmDepartmentDTO.getDistrictID()).getName():"All");
						String upazilaName = (rootCrmDepartmentDTO.getUpazilaID() != null?  resultMap.get(InventoryConstants.CATEGORY_ID_UPAZILA).get(rootCrmDepartmentDTO.getUpazilaID()).getName():"All");
						String unionName = (rootCrmDepartmentDTO.getUnionID() !=null     ?  resultMap.get(InventoryConstants.CATEGORY_ID_UNION).get(rootCrmDepartmentDTO.getUnionID()).getName():"All");
						CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByCRMInventoryItemID(crmInventoryItem.getID());
						
						 
				%>
							
							<tr>
								
								<td>
								<a href="${context }CrmEmployee/getEmployeeView.do?employeeID=<%=crmEmployeeDTO.getCrmEmployeeID()%>" target="_blank">
									<%=crmInventoryItem.getName()%>
								
								</a>
								</td>
								<td><%=crmDesignationDTO.getName() %></td>
								<td><%=districtName%></td>
								<td><%=upazilaName%></td>
								<td><%=unionName%></td>
								<td><a href="${context }CrmEmployee/getDepartmentView.do?departmentID=<%=rootCrmDepartmentDTO.getID()%>" target="_blank" ><%=rootCrmDepartmentDTO.getDepartmentName()%></a></td>
							</tr>
						<%			
					}
						
					
					%>
					</tbody>
				<%	
				}
				%>
				
			</table>
		</div>
	</div>
</div>

