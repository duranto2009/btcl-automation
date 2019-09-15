<%@page import="inventory.InventoryConstants"%>
<%@page import="ipaddress.IpBlock"%>
<%@page import="ipaddress.IpAddressService"%>
<%@page import="user.UserRepository"%>
<%@page import="common.StringUtils"%>
<%@page import="org.apache.struts.action.ActionForward"%>
<%@page import="org.apache.struts.action.ActionRedirect"%>
<%@page import="lli.constants.EndPointConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="lli.link.LliFarEndDTO"%>
<%@page import="lli.link.LliInternalFRDataLocation"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="lli.link.LinkUtils"%>
<%@page import="lli.link.LliEndPointDetailsDTO"%>
<%@page import="language.LliLanguageConstants"%>
<%@page import="common.RequestFailureException"%>
<%@page import="net.sf.jasperreports.olap.mapping.Mapping"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="common.ModuleConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.List"%>
<%@page import="login.LoginDTO"%>
<%@page import="inventory.InventoryService"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="common.CategoryConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
	ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
	InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	Long lliLinkID = 0L;
	
	if(request.getParameter("id") != null) {
		lliLinkID = Long.parseLong(request.getParameter("id"));
	} else {
	}
	if (! (lliLinkService.isLinkVerified(lliLinkID) && loginDTO.getIsAdmin())) response.sendRedirect("#");
	
	InventoryAttributeName portTypeInventoryAttributeName = inventoryService.getPortTypeInventoryAttributeName();
	
	LliLinkDTO lliLinkDTO = lliLinkService.getLliLinkByLliLinkID(lliLinkID);
	LliFarEndDTO lliFarEndDTO = lliLinkService.getFarEndByFarEndID(lliLinkDTO.getFarEndID());
	LliEndPointDetailsDTO farEndDetails = LinkUtils.getEndPointDTODetails(lliFarEndDTO);
	
	ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(lliLinkDTO.getClientID());
	
	LliInternalFRDataLocation lliInternalFRDataLocation = lliLinkService.getInternalFRDataLocationForEdit(lliLinkDTO, lliFarEndDTO, LliRequestTypeConstants.REQUEST_NEW_LINK.UPDATE_APPLICATION);
	request.setAttribute("lliLink", lliLinkDTO);
%>
<div class="portlet box portlet-btcl">

	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i>Update Connection</div>
	</div>

	<div class="portlet-body form">
		<form  class="form-horizontal" method="post" action="../../LLI/Link/Update/Link.do">

			<div class="form-body row">
				<div class="col-md-12">
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<thead><tr><th colspan="3" class="text-center"><h1> Connection Details </h1></th></tr></thead>
							<tbody>
								<tr>
									<th scope="row">Client Name</th>
									<td colspan="2"><%=clientDTO.getLoginName()%></td>
								</tr>
								<tr>
									<th scope="row">Connection Name</th>
									<td colspan="2"><%=lliLinkDTO.getName()%>
										<input type=hidden name=lliLinkID value=<%=lliLinkDTO.getID()%>>
									</td>
								</tr>
								<%if(loginDTO.getIsAdmin()){ %>
								<tr>
									<th scope="row">Service Purpose</th>
									<td colspan="2"><%if(lliLinkDTO.getServicePurpose()==1){ %> YES <%}else{ %>NO <%} %> </td>
								</tr>
							<%} %>
							<tr>
								<th scope="row">Bandwidth</th>
								<td colspan="2"><%=lliLinkDTO.getLliBandwidth() %>&nbsp; <%=EntityTypeConstant.linkBandwidthTypeMap.get(lliLinkDTO.getLliBandwidthType()) %>&nbsp; </td>
							</tr>
							<tr>
								<th scope="row">Connection Type</th>
								<td colspan="2"><%=EndPointConstants.connectionType.get(lliLinkDTO.getConnectionType()) %></td>
							</tr> 
							<%if( lliLinkDTO.getConnectionType() == EndPointConstants.CONNECTION_TYPE_FIVE_YEARS_ ){ %>
								<tr>
									<th scope="row">Five Year Bandwidth</th>
									<td colspan="2"><%=lliLinkDTO.getFiveYearBandwidth() %> <%=EntityTypeConstant.linkBandwidthTypeMap.get( lliLinkDTO.getFiveYearBandwidthType() ) %></td>
								</tr>
							<%} %>
							
							<%
							 List<IpBlock> ipList = ServiceDAOFactory.getService(IpAddressService.class).getIPAddressByEntityID(lliLinkDTO.getID());
							String ipStringMandatory = "";
							String ipStringAdditional = "";
							if(ipList != null)
							{								
								int ipCount = 0;
								for(IpBlock ipBlock: ipList)
								{
									if(ipBlock.getUsageType() == InventoryConstants.USAGE_ESSENTIAL)
									{
										ipStringMandatory += IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()) + " - " + IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress());
									}
									else
									{
										ipStringAdditional += IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getStartingIpAddress()) + " - " + IpAddressService.getIp4AddressStringRepresentationByIpAddressLong(ipBlock.getEndingIpAddress());
									}
									if(++ipCount == ipList.size())
									{
										break;
									}
									ipStringMandatory += ",";
								}
							}
							%>
							<tr>							
							<th scope="row">Mandatory IP Address</th>
							<td colspan="2"><%=ipStringMandatory%></td>
							</tr>
							<tr>							
							<th scope="row">Additional IP Address</th>
							<td colspan="2"><%=ipStringAdditional%></td>
							</tr>
							<tr>
								<th scope="row">Connection Balance </th>
								<td colspan="2"><%=Math.round(lliLinkDTO.getBalance()) %> BDT</td>
							</tr>
							<tr>
								<th scope="row">Description</th>
								<td colspan="2">${lliLink.linkDescription}</td>
							</tr> 
								
							</tbody>
						</table>
					</div>	
					
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<thead><tr><th colspan="3" class="text-center"><h3> End Point Details </h3></th></tr></thead>
					
							<tbody>
								<tr>
									<th scope="row">Connection Name</th>
									<td class="text-muted"><%=farEndDetails.getVepName()%></td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_DISTRICT).getName() %></th>
									<td class="text-muted"><%=farEndDetails.getDistrictName()%></td>
								</tr>
							
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_UPAZILA).getName() %></th>
									<td class="text-muted"><%=farEndDetails.getUpazilaName()%></td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_UNION).getName() %></th>
									<td class="text-muted"><%=farEndDetails.getUnionName()%></td>
								</tr>
								
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_POP).getName() %>
										<%=(lliInternalFRDataLocation.farEndPopInFRResponse) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
									</th>
									
									<%if(lliInternalFRDataLocation.farEndPopInFRResponse){%>
									<td>
										<div class="form-group up-down-path">
											<div class="col-sm-10">
												<input class="fe-hide form-control category-item" type="text" value="<%=farEndDetails.getPopName()%>"> 
												<input type="hidden" value="<%=CategoryConstants.CATEGORY_ID_POP%>" class="category-id">
												<input type="hidden" id="fePopID" value="<%=farEndDetails.getPopID()%>">
											</div>
										</div>
									</td>
									<%}else{%>			
									<td class="text-muted"><%=farEndDetails.getPopName()%>
										<input type="hidden" id="fePopID" value="<%=farEndDetails.getPopID()%>">
									</td>
									<%}%>
								</tr>
						
							<tr>
								<th scope="row">Router
									<%=( lliInternalFRDataLocation.farEndSlotInFRResponse) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
								</th>

								<%if(lliInternalFRDataLocation.farEndSlotInFRResponse){ %>
									<td>
										<div class="form-group up-down-path">
											<div class="col-sm-10">
												<input class="fe-hide form-control category-item router" id="feRouterStr" type="text" value="<%=farEndDetails.getRouterName()%>"> 
												<input type="hidden" value="<%=CategoryConstants.CATEGORY_ID_ROUTER%>" id="routerCategoryId" class="category-id"> 
												<input type="hidden" id="feRouterID" value="<%=farEndDetails.getRouterID()%>">
											</div>
										</div>
									</td>
								<%}else{ %>
									<td class="text-muted"><%=farEndDetails.getRouterName()%>
										<input type="hidden" id="feRouterID" value="<%=farEndDetails.getRouterID()%>">
									</td>
									<%} %>
							</tr>
							<tr>	
								<th scope="row">Port Type
									<%=(lliInternalFRDataLocation.farEndPortInFRResponse) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
								</th>
								
								<td class="text-muted">
									<%if(lliInternalFRDataLocation.farEndPortInFRResponse){ %>
										<div class="form-group ">
											<div class="col-md-10">
												<select id="fePortType" class="form-control">
												<%for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
													<option value="<%=portTypeInventoryAttributeNameType%>" <%=portTypeInventoryAttributeNameType.equals(farEndDetails.getPortCategoryType()) ? "selected" : ""%>><%=portTypeInventoryAttributeNameType%></option>
												<%}%>
												</select>
											</div>
										</div>
									<%}else{ %>
										<%=farEndDetails.getPortCategoryType()%>
									<%} %>
								</td>
								</tr>
								<tr>
									<th scope="row">Port
										<%=( lliInternalFRDataLocation.farEndPortInFRResponse) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
									</th>
									<td class="text-muted">
										<%if(lliInternalFRDataLocation.farEndPortInFRResponse){ %>
											<div class="form-group ">
												<div id="fePortItemID" class="col-md-10">
													<select  name="fePortID" class="form-control portItem" required>
														<option value="<%=farEndDetails.getPortID()%>"><%=farEndDetails.getPortName()%></option>
													</select>
												</div>
											</div>
										<%}else{ %>
										<%=farEndDetails.getPortName()%>
									<%} %>
									</td>
									
								</tr>
								<tr>
									<th scope="row">Address</th>
									<td class="text-muted"><%=farEndDetails.getAddress()%></td>
								</tr>
								<tr>
									<th scope="row">OFC Type</th>
									<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(lliFarEndDTO.getCoreType()) %></td>
								</tr>
								
								<tr>
										<th scope="row"><%=LliLanguageConstants.LOOP_PROVIDER %></th>
										<td class="text-muted"> <%=EndPointConstants.providerOfOFC.get(lliFarEndDTO.getOfcProvider()) %></td>
									</tr>									
									<tr>
										<th scope="row">Terminal Device Provider</th>
										<td class="text-muted"> <%=EndPointConstants.terminalDeviceProvider.get(lliFarEndDTO.getTerminalDeviceProvider()) %></td>
									</tr>
									<%if( lliFarEndDTO.isOFCProvidedByBTCL() ){ %>
									<tr>
										<th scope="row">Outsourcing Company Name </th>
										<td class="text-muted"> <%=UserRepository.getInstance().getUserDTOByUserID( lliFarEndDTO.getOfcProviderID() ).getUsername() %></td>
									</tr>
									<%} %>
							</tbody>
						</table>
					</div>
					<div class="row">
						<jsp:include page="../../common/fileListHelper.jsp" flush="true">
							<jsp:param name="entityTypeID"
								value="<%=EntityTypeConstant.LLI_LINK%>" />
							<jsp:param name="entityID" value="<%=lliLinkDTO.getID()%>" />
						</jsp:include>
					</div>
				</div>
				
			</div>
			
			
			
			<div class="form-actions text-center">
					<button class="btn btn-reset-btcl" type="reset">Cancel</button>
					<input class="btn btn-submit-btcl" type="submit" value="Submit">
			</div>
		</form>
	</div>
</div>


<script type="text/javascript">
	var routerCategoryID=<%=CategoryConstants.CATEGORY_ID_ROUTER%>;
	var popCategoryID = <%=CategoryConstants.CATEGORY_ID_POP%>;
	
	$(document).ready(function() {
		
		$("#nePortType").change(function(){
			loadPortItems($("#neRouterID").val(), $(this).val(),"#nePortItemID");
		});
		
		$("#fePortType").change(function(){
			loadPortItems($("#feRouterID").val(), $(this).val(), "#fePortItemID");
		});
		
		
		function loadPortItems(parentItemID, portType, portContainer) {
			var url= '../../AutoInventoryItem.do';
	        var data= {
	            'categoryID': <%=CategoryConstants.CATEGORY_ID_PORT%>,
	            'parentItemID': parentItemID ,
	            'attributeName' : "Port Type",
	            'attributeValue': portType ,
	            'lliLinkID' : <%=lliLinkDTO.getID()%>
	        };
			callAjax(url, data, function(data){
		  		console.log(data);
            	var ports = '<option  selected disabled>---Select Port---</option>';
		        if(data.length > 0) {
	               $.each(data, function(i, port) {
	            	   ports += "<option value='" + port.ID + "'>" + port.name+"( #"+port.ID +" )" + "</option>";
	               })
	            } else {
	            	ports = '<option  selected disabled>---No Port Is Available---</option>';
	            }
		        $(portContainer).find('.portItem').html(ports);
			  });
		   }

		var currentAutoComplete;
		var categoryObj;
		var parentItemObj;
		var index;
		$(".category-item").each(function() {
			$(this).autocomplete({
				source: function(request, response) {
					currentAutoComplete = this.element;
      
					// d Clear Values
					currentAutoComplete.attr('data-category-item-id', "");
					currentAutoComplete.next().next().val("");
					//
      
					categoryObj = this.element.next(".category-id");
					index=$(this.element).closest('td').index();
					parentItemObj = $(this.element).closest("tr").prev("tr").find('td').eq(index-1).find(".category-item");;
					//var tdObj=$(this.element).closest("tr").prev("tr").find('td').eq(index-1);
    
					var map = {};
					if(categoryObj.val()==routerCategoryID){// start from router
						map['parentItemID'] = $('#fePopID').val();
					}else if(categoryObj.val()==popCategoryID){// start from router
						map['isParentNeeded'] = "false";
					}else{
						var tempVal=parentItemObj.next().next().val();
						map['parentItemID'] = tempVal; 
					}
      
					map['categoryID'] = categoryObj.val();
					var url = '../../AutoInventoryItem.do?partialName=' + request.term;
					ajax(url, map, response, "GET", [$(this)])
				},
				minLength: 1,
				select: function(e, ui) {
					currentAutoComplete.attr('data-category-item-id', ui.item.ID);
					currentAutoComplete.val(ui.item.name);
					currentAutoComplete.next().next().val(ui.item.ID);

					toastr.success( ui.item.name + "  is selected");
					currentAutoComplete.closest("tr").nextAll("tr").find('td').eq(index-1).find(".category-item").val("");
					currentAutoComplete.closest("tr").nextAll("tr").find('td').eq(index-1).find(".category-item").find(".category-item").removeAttr('data-category-item-id');
					return false;
				},
			}).autocomplete("instance")._renderItem = function(ul, item) {
				console.log(item);
				return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
			}
		});	
	   
		//$("select[name=nePortType]").trigger("change");
		//$("select[name=fePortType]").trigger("change");
		
	})
</script>