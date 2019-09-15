<%@page import="common.RegistrantTypeConstants"%>
<%@page import="domain.constants.DomainRegistrantsConstants"%>
<%@page import="domain.DomainContants"%>
<%@page import="util.MultipleTypeUtils"%>
<%@page import="vpn.client.ClientForm"%>
<%@page import="java.util.Set"%>
<%@page import="vpn.constants.VpnRegistrantsConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="domain.DomainPackageTypeDTO"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackageDetails"%>
<%@page import="domain.DomainService"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	DomainService domainService = new DomainService();
	DomainPackageTypeDTO domainPackageDTO;
	//List<DomainPackageTypeDTO> docuDomainPackageTypeDTOs = domainService.getDomainPackageTypes();
	
	String url = "SearchPackageType";
	String navigator = SessionConstants.NAV_DOMAIN_PACKAGE_TYPE;
	String context = "../../.."  + request.getContextPath() + "/";
	
	ArrayList<DomainPackageTypeDTO> docuDomainPackageTypeDTOs = (ArrayList<DomainPackageTypeDTO>) session.getAttribute(SessionConstants.VIEW_DOMAIN_PACKAGE_TYPE);
	
%>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<link href="<%=context%>/assets/global/plugins/datatables/datatables.min.css" rel="stylesheet" type="text/css" />
<link href="<%=context%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	button.dt-button, div.dt-button, a.dt-button{
		color: white !important;
		border-color: white !important;
	}
</style>
<!-- END PAGE LEVEL PLUGINS -->
<div class="portlet box portlet-btcl">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-globe"></i>Package Type Search </div>
        <div class="tools"> </div>
    </div>
    <div class="portlet-body">
      <html:form action="DomainPackageType" method="POST" styleId="tableForm">
		<table class="table table-striped table-bordered" id="sample_1">
			<thead>
				<tr>
					<th>Name</th>
					<th>TLD</th>
					<th>Document</th>
					<th>Client Type</th>
					<th>Company Type</th>
					<th scope="row">Category</th>
					<th scope="row">Second Level Dom.</th>
					<th>Edit</th>
					<th  class="text-center no-sort">
						<input type="hidden" name="mode" value="removePackageType"  />
		        		<input type="checkbox" name="Check_All" value="CheckAll" class="group-checkable pull-left" />
		        		<input class="btn btn-xs btn-danger" type="submit"  value="Delete"/>
		        	</th>
				</tr>
			</thead>
			<tbody>
				<%
				if(docuDomainPackageTypeDTOs!=null){
					for (DomainPackageTypeDTO domainPackageTypeDTO : docuDomainPackageTypeDTOs) {
						Integer registrantCategory = (int)clientForm.getClientDetailsDTO().getRegistrantCategory();
						Set<Integer> companyTypes =MultipleTypeUtils.getArrayToUniqueSet(domainPackageTypeDTO.getRegTypes());
						Set<Integer> clientTypes =MultipleTypeUtils.getArrayToUniqueSet(domainPackageTypeDTO.getClientTypes());
						Set<Integer> secondLevelDomains =MultipleTypeUtils.getArrayToUniqueSet(domainPackageTypeDTO.getSecondLevelDomains());
					%>
					<tr>
						<td>
							<%=domainPackageTypeDTO.getPackageName()%>
						</td>
						<td>
						 	<%if (domainPackageTypeDTO.getTld() == 1) {%>.bd <%} %>
							<%if (domainPackageTypeDTO.getTld() == 2) { %> .বাংলা <%} %>
						<td>
							<%if (domainPackageTypeDTO.isDocumentRequired()) {%>
								Yes
							<%}else{%>
								No
							<%} %>
						</td>
						<td>
							<%
								HashMap<Integer, String> clientTypeMap = ClientForm.CLIENT_TYPE_STR;
								for (Integer availableItem : clientTypeMap.keySet()) {
									if(clientTypes.contains(availableItem)){ %>
										<span class=" badge badge-warning"> <%=clientTypeMap.get(availableItem)%> </span>
								<%}
							}%>
							</td>
						<td>
							<%
								HashMap<Integer, String> companyTypeMap = RegistrantTypeConstants.DOMAIN_REGISTRANT_TYPE;
								for (Integer availableItem : companyTypeMap.keySet()) {
									if(companyTypes.contains(availableItem)){ %>
										<span class=" badge badge-info"> <%=companyTypeMap.get(availableItem)%> </span>
								<%}
							}%>
						</td>
						<td>  
							<%
								HashMap<Integer, String> regMap = DomainRegistrantsConstants.domainRegTypeMap;
								for (Integer availableItem : regMap.keySet()) {
									if(registrantCategory == availableItem){ %>
										<span class=" badge badge-primary"> <%=regMap.get(availableItem)%> </span>
								<%}
							}%>
						</td>
						<td>  
							<%
								HashMap<Integer, String> secondLevelDomainsMap = DomainContants.secondLevelDomainsMap;
								for (Integer availableItem : secondLevelDomainsMap.keySet()) {
									if(secondLevelDomains.contains(availableItem)){ %>
										<span class=" badge badge-danger"> .<%=secondLevelDomainsMap.get(availableItem)%> </span>
								<%}
							}%>
						</td>
						<td>
							<a  href="<%=context %>DomainPackageType.do?ID=<%=domainPackageTypeDTO.getID()%>&mode=getPackageType">Edit</a>
						</td>
						<td>
							 <input  class="checkboxes" type="checkbox" name="deleteIDs" value="<%=domainPackageTypeDTO.getID() %>" > 
						</td>
					</tr>
					<%
					}
					%>
				<%} else {%>
				<tr ><td class="text-center" colspan="4">No Package type found</td></tr>
				<%} %>
			</tbody>
		</table>
		</html:form>
	</div>
</div>
			
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="<%=context %>/assets/global/scripts/datatable.js" type="text/javascript"></script>
<script src="<%=context%>/assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
<script src="<%=context%>/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js" 	type="text/javascript"></script>
<script src="<%=context%>/assets/pages/scripts/table-datatables-buttons.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<script src="<%=context%>/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript">
   $(document).ready(function(){
       var table=$("#sample_1");
       $('.group-checkable', table).change(function() {
           var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
           var checked = $(this).prop("checked");
           $(set).each(function() {
               $(this).prop("checked", checked);
           });
           $.uniform.update(set);
       });
       $('#tableForm').submit(function(e) {
           var currentForm = this;
           var selected=false;
           e.preventDefault();
           var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
           $(set).each(function() {
           	if($(this).prop('checked')){
           		selected=true;
           	}
           });
           if(!selected){
           	 bootbox.alert("Select Domain Package Type to delete!", function() { });
           }else{
           	 bootbox.confirm("Are you sure  to delete the Domain Package Type (s)?", function(result) {
                    if (result) {
                        currentForm.submit();
                    }
                });
           }
       });
    })
    
    </script>
