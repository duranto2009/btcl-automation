<%@page import="org.apache.struts.action.ActionForward"%>
<%@page import="org.apache.struts.action.ActionRedirect"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="vpn.link.VpnInternalFRDataLocation"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="vpn.client.ClientService"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
<%@page import="language.VpnLanguageConstants"%>
<%@page import="common.RequestFailureException"%>
<%@page import="net.sf.jasperreports.olap.mapping.Mapping"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="common.ModuleConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.List"%>
<%@page import="login.LoginDTO"%>
<%@page import="inventory.InventoryService"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="common.CategoryConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%
	VpnLinkService vpnLinkService = ServiceDAOFactory.getService(VpnLinkService.class);
	ClientService clientService = ServiceDAOFactory.getService(ClientService.class);
	InventoryService inventoryService = ServiceDAOFactory.getService(InventoryService.class);
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	Long vpnLinkID = 0L;
	
	if(request.getParameter("id") != null) {
		vpnLinkID = Long.parseLong(request.getParameter("id"));
	} else {
	}
	if (! (vpnLinkService.isLinkVerified(vpnLinkID) && loginDTO.getIsAdmin())) response.sendRedirect("#");
	
	InventoryAttributeName portTypeInventoryAttributeName = inventoryService.getPortTypeInventoryAttributeName();
	
	VpnLinkDTO vpnLinkDTO = vpnLinkService.getVpnLinkByVpnLinkID(vpnLinkID);
	VpnNearEndDTO vpnNearEndDTO = vpnLinkService.getNearEndByNearEndID(vpnLinkDTO.getNearEndID());
	VpnFarEndDTO vpnFarEndDTO = vpnLinkService.getFarEndByFarEndID(vpnLinkDTO.getFarEndID());
	VpnEndPointDetailsDTO nearEndDetails = LinkUtils.getEndPointDTODetails(vpnNearEndDTO);
	VpnEndPointDetailsDTO farEndDetails = LinkUtils.getEndPointDTODetails(vpnFarEndDTO);
	
	ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(vpnLinkDTO.getClientID());
	
	VpnInternalFRDataLocation vpnInternalFRDataLocation = vpnLinkService.getInternalFRDataLocationForEdit(vpnLinkDTO, vpnNearEndDTO, vpnFarEndDTO, VpnRequestTypeConstants.REQUEST_NEW_LINK.UPDATE_APPLICATION);
%>
<div class="portlet box portlet-btcl">

	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i>Update Link</div>
	</div>

	<div class="portlet-body form">
		<form  class="form-horizontal" method="post" action="../../VPN/Link/Update/Link.do">

			<div class="form-body row">
				<div class="col-md-12">
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<thead><tr><th colspan="3" class="text-center"><h1> Link Details </h1></th></tr></thead>
							<tbody>
								<tr>
									<th scope="row">Client Name</th>
									<td colspan="2"><%=clientDTO.getLoginName()%></td>
								</tr>
								<tr>
									<th scope="row">Link Name</th>
									<td colspan="2"><%=vpnLinkDTO.getName()%>
										<input type=hidden name=vpnLinkID value=<%=vpnLinkDTO.getID()%>>
									</td>
								</tr>
								<tr>
									<th scope="row">Bandwidth</th>
									<td colspan="2"><%=vpnLinkDTO.getVpnBandwidth()%> <%=EntityTypeConstant.linkBandwidthTypeMap.get(vpnLinkDTO.getVpnBandwidthType()) %></td>
								</tr>
								<tr>
									<th scope="row">Description</th>
									<td colspan="2"><%=vpnLinkDTO.getLinkDescription()%></td>
								</tr> 
								
							</tbody>
						</table>
					</div>	
					
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<thead><tr><th colspan="3" class="text-center"><h3> End Point Details </h3></th></tr></thead>
					
							<tbody>
								<tr>
									<th scope="row">Name</th>
									<td class="text-muted"><%=nearEndDetails.getVepName()%></td>
									<td class="text-muted"><%=farEndDetails.getVepName()%></td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_DISTRICT).getName() %></th>
									<td class="text-muted"><%=nearEndDetails.getDistrictName()%></td>
									<td class="text-muted"><%=farEndDetails.getDistrictName()%></td>
								</tr>
							
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_UPAZILA).getName() %></th>
									<td class="text-muted"><%=nearEndDetails.getUpazilaName()%></td>
									<td class="text-muted"><%=farEndDetails.getUpazilaName()%></td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_UNION).getName() %></th>
									<td class="text-muted"><%=nearEndDetails.getUnionName()%></td>
									<td class="text-muted"><%=farEndDetails.getUnionName()%></td>
								</tr>
								
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(CategoryConstants.CATEGORY_ID_POP).getName() %>
										<%=(vpnInternalFRDataLocation.nearEndPopInFRResponse || vpnInternalFRDataLocation.farEndPopInFRResponse) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
									</th>
									<%if(vpnInternalFRDataLocation.nearEndPopInFRResponse){%>
									<td>
										<div class="form-group up-down-path">
											<div class="col-sm-10">
												<input class="ne-hide form-control category-item " type="text" value="<%=nearEndDetails.getPopName()%>">
												<input type="hidden" value="<%=CategoryConstants.CATEGORY_ID_POP%>" class="category-id"> 
												<input type="hidden" id="nePopID" value="<%=nearEndDetails.getPopID()%>">
											</div>
										</div>
									</td>
									<%}else{%>
									<td class="text-muted"><%=nearEndDetails.getPopName()%>
										<input type="hidden" id="nePopID" value="<%=nearEndDetails.getPopID()%>">
									</td>					
									<%}%>
									<%if(vpnInternalFRDataLocation.farEndPopInFRResponse){%>
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
									<%=(vpnInternalFRDataLocation.nearEndSlotInFRResponse || vpnInternalFRDataLocation.farEndSlotInFRResponse) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
								</th>
								
								<%if(vpnInternalFRDataLocation.nearEndSlotInFRResponse){ %>
									<td>
										<div class="form-group up-down-path">
											<div class="col-sm-10">
												<input class="ne-hide form-control category-item router" id="neRouterStr" type="text" value="<%=nearEndDetails.getRouterName()%>">  
												<input type="hidden" value="<%=CategoryConstants.CATEGORY_ID_ROUTER%>" id="routerCategoryId" class="category-id"> 
												<input type="hidden" id="neRouterID" value="<%=nearEndDetails.getRouterID()%>">
											</div>
										</div>
									</td>
								<%}else{ %>
									<td class="text-muted"><%=nearEndDetails.getRouterName()%>
										<input type="hidden" id="neRouterID" value="<%=nearEndDetails.getRouterID()%>">
									</td>
								<%} %>
								
								
								<%if(vpnInternalFRDataLocation.farEndSlotInFRResponse){ %>
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
									<%=(vpnInternalFRDataLocation.nearEndPortInFRResponse || vpnInternalFRDataLocation.farEndPortInFRResponse) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
								</th>
								<td class="text-muted">
									<%if(vpnInternalFRDataLocation.nearEndPortInFRResponse){ %>
										<div class="form-group up-down-path">
											<div class="col-md-10">
												<select id="nePortType" class="form-control" >
												<%for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
													<option value="<%=portTypeInventoryAttributeNameType%>" <%=portTypeInventoryAttributeNameType.equals(nearEndDetails.getPortCategoryType()) ? "selected" : ""%> ><%=portTypeInventoryAttributeNameType%></option>
												<%}%>
												</select>
											</div>
										</div>
									<%}else{ %>
										<%=nearEndDetails.getPortCategoryType()%>
									<%} %>
								</td>
								<td class="text-muted">
									<%if(vpnInternalFRDataLocation.farEndPortInFRResponse){ %>
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
										<%=(vpnInternalFRDataLocation.nearEndPortInFRResponse || vpnInternalFRDataLocation.farEndPortInFRResponse) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
									</th>
									
									<td class="text-muted">
									<%if(vpnInternalFRDataLocation.nearEndPortInFRResponse){ %>
										<div class="form-group ">
											<div id="nePortItemID" class="col-md-10">
												<select class="form-control portItem" name="nePortID" required>
													<option value="<%=nearEndDetails.getPortID()%>"><%=nearEndDetails.getPortName()%></option>
												</select>
											</div>
										</div>
									</td>
									<%}else{ %>
										<%=nearEndDetails.getPortName() + " (" + nearEndDetails.getPortID() + ")"%>
									<%} %>
									<td class="text-muted">
										<%if(vpnInternalFRDataLocation.farEndPortInFRResponse){ %>
											<div class="form-group ">
												<div id="fePortItemID" class="col-md-10">
													<select  name="fePortID" class="form-control portItem" required>
														<option value="<%=farEndDetails.getPortID()%>"><%=farEndDetails.getPortName()%></option>
													</select>
												</div>
											</div>
										<%}else{ %>
										<%=farEndDetails.getPortName() + " (" + farEndDetails.getPortID() + ")"%>
									<%} %>
									</td>
								</tr>
								<tr>
									<th scope="row">Address</th>
									<td class="text-muted"><%=nearEndDetails.getAddress()%></td>
									<td class="text-muted"><%=farEndDetails.getAddress()%></td>
								</tr>
								<tr>
									<th scope="row">OFC Type</th>
									<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(vpnNearEndDTO.getCoreType()) %></td>
									<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(vpnFarEndDTO.getCoreType()) %></td>
								</tr>
								<tr>
									<th scope="row">Loop Provider</th>
									<td class="text-muted"><%=EndPointConstants.providerOfOFC.get(vpnNearEndDTO.getOfcProviderTypeID())%></td>
									<td class="text-muted"> <%=EndPointConstants.providerOfOFC.get(vpnFarEndDTO.getOfcProviderTypeID())%></td>
								</tr>
							</tbody>
						</table>
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
	            'vpnLinkID' : <%=vpnLinkDTO.getID()%>
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
						if(index==1){
							map['parentItemID'] = $('#nePopID').val();
						}else if (index==2){
							map['parentItemID'] = $('#fePopID').val();
						}
					}else if(categoryObj.val()==popCategoryID){// start from router
						map['isParentNeeded'] = "false";
					}else{
						var tempVal=parentItemObj.next().next().val();
						map['parentItemID'] = tempVal; 
					}
      
					map['categoryID'] = categoryObj.val();
					var url = '../../AutoInventoryItem.do?partialName=' + request.term;
					callAjax(url, map, response, "GET")
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