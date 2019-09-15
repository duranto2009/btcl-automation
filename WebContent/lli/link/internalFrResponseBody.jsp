<%@page import="ipaddress.IpAddressService"%>
<%@page import="ipaddress.IpBlock"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="lli.constants.LliRequestTypeConstants.REQUEST_DOWNGRADE"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.List"%>
<%@page import="lli.LliDAO"%>
<%@page import="lli.link.LliInternalFRDataLocation"%>
<%@page import="common.CategoryConstants"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="lli.link.LliBandWidthChangeRequestDTO"%>
<%@page import="util.SqlGenerator"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="permission.PermissionService"%>
<%@page import="user.UserService"%>
<%@page import="user.UserDTO"%>
<%@page import="inventory.InventoryService"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="lli.link.LliFRResponseExternalDTO"%>
<%@page import="common.CommonService"%>
<%@page import="lli.link.LliFRResponseInternalDTO"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="lli.constants.EndPointConstants"%>
<%@page import="lli.link.LliFarEndDTO"%>
<%@page import="lli.link.LinkUtils"%>
<%@page import="lli.link.LliEndPointDetailsDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="file.FileDTO"%>
<%@page import="util.SOP"%>
<%@page import="file.FileDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="common.ModuleConstants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
Logger logger = Logger.getLogger("linkInternalFR");
int entityID = Integer.parseInt(request.getParameter("entityID"));
logger.debug("request.getParameter(entityID) " + request.getParameter("entityID"));
logger.debug("request.getAttribute(entityID) " + request.getAttribute("entityID"));
String context = "../../.." + request.getContextPath() + "/";

String actionName = "../../LliLinkAction.do?entityID=" + entityID+"&getMode=edit";
String back  = "../../LliLinkAction.do?entityID=" + entityID+"&entityTypeID="+EntityTypeConstant.LLI_LINK;

LliFarEndDTO farEndDTO=(LliFarEndDTO)request.getAttribute("lliFE");
LliEndPointDetailsDTO farEndDetailsDTO=LinkUtils.getEndPointDTODetails(farEndDTO);

request.setAttribute("farEnd", farEndDetailsDTO);

LliLinkDTO lliLinkDTO= (LliLinkDTO)request.getAttribute("lliLink");
	CommonService comService=ServiceDAOFactory.getService(CommonService.class);
	InventoryService inventoryService= ServiceDAOFactory.getService(InventoryService.class);
	LliLinkService lliLinkService = ServiceDAOFactory.getService(LliLinkService.class);
LliFRResponseInternalDTO lliFRResponseInternalDTO = new LliFRResponseInternalDTO();

long fePopID=farEndDTO.getPopID();
Long feRouterID=null;
boolean editableSlotFarEnd=false;
boolean editablePOPFarEnd = false;
boolean editablePortTypeFarEnd = false;

//LliFRResponseExternalDTO lliFRResponseExternalDTOForFarEnd = null;

int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
int upazilaCategoryId = CategoryConstants.CATEGORY_ID_UPAZILA;
int unionCategoryId = CategoryConstants.CATEGORY_ID_UNION;
int popCategoryId = CategoryConstants.CATEGORY_ID_POP;
int routerCategoryId = CategoryConstants.CATEGORY_ID_ROUTER;
int vlanCategoryId = CategoryConstants.CATEGORY_ID_VLAN;

int portCategoryId = CategoryConstants.CATEGORY_ID_PORT;
boolean previousInternalFRFound = true;
int requestTypeID = Integer.parseInt((String)request.getAttribute("requestTypeID"));
int divisionID = 0;
if(requestTypeID == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){
	feRouterID = farEndDTO.getRouterID();
}
try {
	
	LliInternalFRDataLocation lliInternalFRDataLocation =  new LliDAO().getInternalFRDataLocation(lliLinkDTO,  farEndDTO, requestTypeID);
	
	if((requestTypeID / EntityTypeConstant.MULTIPLIER2) % 100 == 9)//shifting
	{
		{
			lliInternalFRDataLocation.farEndPopInEndpointDTO = true;
			lliInternalFRDataLocation.farEndPortInEndpointDTO = true;
			lliInternalFRDataLocation.farEndSlotInEndpointDTO = true;
			
			lliInternalFRDataLocation.farEndPopInFRResponse = false;
			lliInternalFRDataLocation.farEndPortInFRResponse = false;
			lliInternalFRDataLocation.farEndSlotInFRResponse = false;
		}
		
	}
	String UpozillaFarEnd = "-";
	String UnionFarEnd = "-";
	String PopFarEnd = "-";
	String PortTypeFarEnd = "-";
	String PortIDFarEnd = "-";
	String RouterFarEnd = "-";
	String MandatoryVLanIDFarEnd = "-";
	String AdditionalVLanIDFarEnd = "-";
	
	UpozillaFarEnd = "" + inventoryService.getInventoryItemByItemID(farEndDTO.getUpazilaID()).getName();
	UnionFarEnd = "" + inventoryService.getInventoryItemByItemID(farEndDTO.getUnionID()).getName();
	
	if(lliInternalFRDataLocation.farEndPopInEndpointDTO){
		PopFarEnd = "" + inventoryService.getInventoryItemByItemID(farEndDTO.getPopID()).getName();
	}
	if(lliInternalFRDataLocation.farEndSlotInEndpointDTO){
		RouterFarEnd = "" + inventoryService.getInventoryItemByItemID(farEndDTO.getRouterID()).getName();
		
		MandatoryVLanIDFarEnd = "" + LinkUtils.getAdditionalVLanNames(farEndDTO.getMandatoryVLanID());
		AdditionalVLanIDFarEnd = "" + LinkUtils.getAdditionalVLanNames(farEndDTO.getAdditionalVLanID());
	}
	if(lliInternalFRDataLocation.farEndPortInEndpointDTO){
		PortTypeFarEnd = farEndDTO.getPortType();
		PortIDFarEnd = "" + inventoryService.getInventoryItemByItemID(farEndDTO.getPortID()).getName();
	}
	editablePOPFarEnd = lliInternalFRDataLocation.farEndPopInFRResponse;
	editableSlotFarEnd = lliInternalFRDataLocation.farEndSlotInFRResponse;
	editablePortTypeFarEnd = lliInternalFRDataLocation.farEndPortInFRResponse;


	//logger.debug("request.getAttribute(entityTypeIDReplace)" + request.getAttribute("entityTypeIDReplace"));
	if(request.getAttribute("entityTypeIDReplace") != null){
		request.setAttribute("entityTypeID", request.getAttribute("entityTypeIDReplace"));
	}
	if(request.getAttribute("entityIDReplace") != null){
		request.setAttribute("entityID", request.getAttribute("entityIDReplace"));
	}
	//editableSlotFarEnd=true;  //this line is added hardcoded. please check and delete this line 
	
	boolean ipEditable = lliInternalFRDataLocation.mandatoryIPInFRResponse;
	/* 
	if(farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_BTCL) {
		boolean farEndExternalFRDone =  (farEndDTO.getLatestStatus() == LliRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_ACCEPT_EXTERNAL_FR_OF_FAR_END);
		ipEditable = !editablePOPFarEnd && farEndExternalFRDone;
	}
	else if(farEndDTO.getOfcProviderTypeID() == EndPointConstants.OFC_CUSTOMER) {
		ipEditable = true;
	}
	*/
	if(ipEditable && fePopID != 0) {
		Long districtIDOfSuggestedPop = inventoryService.getInventoryParentItemPathMapByCategoryTypeIDAndItemID(CategoryConstants.CATEGORY_ID_DISTRICT, fePopID).get(CategoryConstants.CATEGORY_ID_DISTRICT).getID(); 
		divisionID = InventoryConstants.districtToDivisionMap.get(districtIDOfSuggestedPop);
	} 
	
	String ipStringMandatory = "";
	String ipStringAdditional = "";
	
	if(lliInternalFRDataLocation.mandatoryIPInEndpointDTO)
	{
		List<IpBlock> ipList = ServiceDAOFactory.getService(IpAddressService.class).getIPAddressByEntityID(lliLinkDTO.getID());
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
	}
	//d make Port Type dynamic
	List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(CategoryConstants.CATEGORY_ID_PORT);
	InventoryAttributeName portTypeInventoryAttributeName = new InventoryAttributeName();;
	for(InventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
		if(inventoryAttributeName.getName().equals("Port Type")){
			portTypeInventoryAttributeName = inventoryAttributeName;
			break;
		}
	}
	LliBandWidthChangeRequestDTO lliBandWidthChangeRequestDTO = lliLinkService.getLatestBandwidthChangeRequestByEntityAndEntityTypeID( entityID, EntityTypeConstant.LLI_LINK );
%>


<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Connection Internal FR Response Details
		</div>
		<div class="actions">
			<a  href="<%=back%>" class="btn btn-back-btcl" style="padding-left: 15px; padding-right: 15px;"> Back </a>
		</div>
	</div>
	
	<div class="portlet-body" id="printContent">
		<form  id="fileupload" class="form-horizontal" method="post" action="../../CommonAction.do?mode=internalFrResponse" >
			<%-- <input type="hidden" name="entityID" value = "${lliLink.ID}" />
			<input type="hidden" name="linkID" value = "${lliLink.ID}" /> --%>
			<input type='hidden' name='requestTypeID' value="<%=request.getAttribute("requestTypeID")%>">
			 
			<input type='hidden' name='entityTypeID' value="<%=request.getAttribute("entityTypeID")%>">
			<input type='hidden' name='entityID' value="<%=request.getAttribute("entityID")%>">
			<input type='hidden' name='rootEntityTypeID' value="<%=request.getAttribute("entityTypeID")%>">
			<input type='hidden' name='rootEntityID' value="<%=request.getAttribute("entityID")%>">
			
			<input type="hidden" name="clientID" value="${clientID}" >
			<input type='hidden' name='requestToAccountID' value="<%=request.getParameter("requestTo")%>">
			<input name="actionName" value="LliLinkAction.do?entityID=${lliLink.ID}&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>" class="actionName" type="hidden">
		
	
			<div class="row">
				<div class="col-md-12">
					<div class="table-responsive">
						<table class="table table-bordered  table-striped">
							<thead>
								<tr>
									<th colspan="2" class="text-center">
										<h1> Connection Details </h1>
									</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<th scope="row">Client Name</th>
									<td>${clientName}</td>
								</tr>
								<tr>
									<th scope="row">Connection Name</th>
									<td>${lliLink.linkName}</td>
								</tr>
								<tr>
									<th scope="row">Bandwidth</th>
									<td>${lliLink.lliBandwidth} <%=EntityTypeConstant.linkBandwidthTypeMap.get(lliLinkDTO.getLliBandwidthType()) %></td>
								</tr>
								<%
									if(requestTypeID == LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR 
									|| requestTypeID == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){
								%>
								<tr>
									<th scope="row">Requested Bandwidth</th>
									<td><%=lliBandWidthChangeRequestDTO.getNewBandwidth() + " " 
									+ EntityTypeConstant.linkBandwidthTypeMap.get(lliBandWidthChangeRequestDTO.getNewBandwidthType()) %>
									</td>
								</tr>	
								<%} %>
								<tr>
									<th scope="row">Bandwidth Available (?)</th>
									<td>
										<div class="radio-list" style="display: inline-block;">
											<label class="radio-inline"> 
												<span> <input type="radio" name="bandwidthAvailablity" value="0" > </span>  Not Available
											</label>
											<label class="radio-inline"> 
												<span> <input type="radio" name="bandwidthAvailablity" value="1"  checked  ></span>  Available
											</label>
										</div>
									</td>
								</tr>
								<tr id="bandWidthComment" style="display:none">
									<th scope="row">Bandwidth Non-Availability Reason<span style="color:red" aria-required="true"> * </span></th>
									<td>
										<div class=row>
											<div class="col-sm-6">
												<textarea class="form-control" rows="3" name="bandWidthComment"
													placeholder="Comments..."></textarea>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<th scope="row">Mandatory IP Address Count</th>
									<td>4</td>
								</tr>
								<tr>
									<th scope="row">Available Mandatory IP Address Count<span style="color:red" aria-required="true"> * </span></th>
									<td>
										<%if(ipEditable){%>
										<div class=row>
											<div class=col-sm-6>
<!-- 												<select class="form-control inside-table" name="mandatoryIpAvailableCount"> -->
<!-- 													<option value="0">0</option> -->
<!-- 													<option value="2">2</option> -->
<!-- 													<option value="4">4</option> -->
<!-- 													<option value="8">8</option> -->
<!-- 													<option value="16">16</option> -->
<!-- 													<option value="32">32</option> -->
<!-- 												</select>	 -->
												<input type="number" name="mandatoryIpAvailableCount" value="0" class="form-control inside-table" min="0" max="256" step="1">
											</div>
										</div>
										
										<%}else{%>
										-
										<%} %>
									</td>
								</tr>
								
								<tr id="mandatoryIpUnavailabilityReason">
									<th scope="row">Mandatory IP Address Unavailability Reason<span style="color:red" aria-required="true"> * </span></th>
									<td>
										<%if(ipEditable){%>
										<div class=row>
											<div class="col-sm-6">
												<textarea class="form-control inside-table" rows="3" name="mandatoryIpUnavailabilityReason"
												placeholder="Comments..."></textarea>
											</div>
										</div>
										<%}else{%>
										-
										<%} %>
									</td>
								</tr>
								<tr class="mandatoryIpAvailableTR" style=display:none>
									<th scope=row>Type Mandatory IPs</th>
									<td>
									<%if(ipEditable){ %>
										<div class=row>
											<div class=col-sm-6>
												<table class='table table-striped' style='text-align:center'>
												
													<tbody>
														<tr>
															<td><input class=form-control name=starting-ip-essential></td>
															<td style=text-align:center><span> to </span></td>
															<td><input class=form-control name=ending-ip-essential ></td>
														</tr>
														<tr>
															<td style='text-align:left'>
																<button class='btn btn-primary btn-ip-add' data-id=essential>Add</button>
																<button class='btn btn-danger btn-ip-remove' data-id=essential>Remove</button>
															</td>
														</tr>
													</tbody>
												</table>	
											</div>
										</div>
										
									<%} else {%>
										-
									<%}%>
									</td>
								</tr>
								<tr id="mandatoryIpAvailableBlockID" style="display:none">
									<th scope="row">Selected Mandatory IP Blocks<span style="color:red" aria-required="true"> * </span></th>
									<td>
										<%if(ipEditable){%>
										<div class=row>
											<div class=col-sm-6>
												<select  style="width:100%;border:1px solid #c2cad8;padding: 6px 12px;" class="selectIP" name="mandatoryIpAvailableBlockID" multiple>
											
												</select>
											</div>
										</div>
										<%}else{%>
										-
										<%} %>
									</td>
								</tr>
								
<!-- 								<tr> -->
<!-- 									<th scope="row">Additional IP Address Count</th> -->
<%-- 									<td>${lliLink.additionalIPCount}</td> --%>
<!-- 								</tr> -->
<!-- 								<tr id="additionalIpAvailableCount" style="display:none"> -->
<!-- 									<th scope="row">Available Additional IP Address Count<span style="color:red" aria-required="true"> * </span></th> -->
<!-- 									<td> -->
<%-- 										<%if(ipEditable){%> --%>
<!-- 										<div class=row> -->
<!-- 											<div class=col-sm-6> -->
<!-- 												<select class="form-control inside-table" name="additionalIpAvailableCount"> -->
<!-- 													<option value="0">0</option> -->
<!-- 													<option value="2">2</option> -->
<!-- 													<option value="4">4</option> -->
<!-- 													<option value="8">8</option> -->
<!-- 													<option value="16">16</option> -->
<!-- 													<option value="32">32</option> -->
<!-- 												</select>	 -->
<!-- 											</div> -->
<!-- 										</div> -->
										
<%-- 										<%}else{%> --%>
<!-- 										<input type='hidden' name='additionalIpAvailableCount' value="0"> -->
<%-- 										<%} %> --%>
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 								<tr id="additionalIpUnavailabilityReason" style="display:none"> -->
<!-- 									<th scope="row">Additional IP Address Unavailability Reason<span style="color:red" aria-required="true"> * </span></th> -->
<!-- 									<td> -->
<%-- 										<%if(ipEditable){%> --%>
<!-- 										<div class=row> -->
<!-- 											<div class="col-sm-6"> -->
<!-- 												<textarea class="form-control inside-table" rows="3" name="additionalIpUnavailabilityReason" -->
<!-- 												placeholder="Comments..."></textarea> -->
<!-- 											</div> -->
<!-- 										</div> -->
<%-- 										<%}else{%> --%>
<!-- 										- -->
<%-- 										<%} %> --%>
<!-- 									</td> -->
<!-- 								</tr> -->
								
								<tr class=additionalIpAvailableTR style=display:none>
									<th scope="row">Available Additional IP Blocks<span style="color:red" aria-required="true"> * </span></th>
									<td>
									<%if(ipEditable){%>
									<div class=row>
										<div class=col-sm-6>
											<table class='table table-striped' style='text-align:center'>
												
												<tbody>
													<tr>
														<td><input class=form-control name=starting-ip-additional></td>
														<td style=text-align:center><span>to</span>
														<td><input class=form-control name=ending-ip-additional></td>
													</tr>
													<tr>
														<td style=text-align:left>
															<button class='btn btn-primary btn-ip-add' data-id=additional>Add</button>
															<button class='btn btn-danger btn-ip-remove' data-id=additional>Remove</button>
														</td>
													</tr>
												</tbody>
											</table>
										
										</div>
									</div>
										
									<%} else {%>
									-
									<%}%>
									</td>
								</tr>
								<tr id="additionalIpAvailableBlockID" style="display:none">
									<th scope="row">Available Additional IP Blocks<span style="color:red" aria-required="true"> * </span></th>
									<td>
									<%if(ipEditable){%>
									<div class=row>
										<div class=col-sm-6>
											<select  style="width:100%;border:1px solid #c2cad8;padding: 6px 12px;" class=selectIP name="additionalIpAvailableBlockID" multiple>
											
											</select>
										</div>
									</div>
										
										<%}else{%>
										-
										<%} %>
									</td>
								</tr>
								
								<tr>
									<th scope="row">Description</th>
									<td>${lliLink.linkDescription}</td>
								</tr> 
								
							</tbody>
						</table>
					</div>	
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th colspan="2" class="text-center">
										<h3> End Point Details </h3>
									</th >
								</tr>
							</thead>
					
							<tbody>
								<tr>
									<th scope="row">Name</th>
									<td class="text-muted">${farEnd.vepName }</td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></th>
									<td class="text-muted">${farEnd.districtName }</td>
								</tr>
							
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></th>
									<td class="text-muted"><%=UpozillaFarEnd%></td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></th>
									<td class="text-muted"><%=UnionFarEnd%></td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %>
										<%=(editablePOPFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
									</th>
									
									<td>
										<%if(editablePOPFarEnd){ %>
											<div class="form-group up-down-path">
												<div class="col-sm-6">
												<% if( requestTypeID == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR ||	requestTypeID == LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR) {%>
													<input class="fe-hide form-control category-item" name="fePopIdStr" value="<%=farEndDetailsDTO.getPopName()%>" type="text"> 
													<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id">
													<input type="hidden" name="fePopID" class="item-id" value="<%=farEndDTO.getPopID()%>">
												<% } else { %>
													<input class="fe-hide form-control category-item" name="fePopIdStr" placeholder="Type to select pop..." value="" type="text"> 
													<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id">
													<input type="hidden" class="form-control item-id"  name="fePopID">
												<% } %>
												</div>
											</div>
										<%}else{ %>
											<%=PopFarEnd%>
											<input type="hidden" name="fePopID" value="<%=fePopID%>"/>
										<%} %>
									</td>
								</tr>
						
								<tr>
									<th scope="row">Router/Switch
									<%=(editableSlotFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%></th>
									<td>
										<%if(editableSlotFarEnd){ %>
											<div class="form-group up-down-path">
												<div class="col-sm-6">
												<% if( requestTypeID == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR ||	requestTypeID == LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR) {%>
													<input class="ne-hide form-control category-item router" name="neRouterStr" value="<%=farEndDetailsDTO.getRouterName() %>" type="text"> 
													<input type="hidden" value="<%=routerCategoryId%>" name="routerCategoryId" class="category-id"> 
													<input name="feRouterID" class="item-id" value="<%=farEndDTO.getRouterID()%>" type="hidden">
												<% } else { %>
													<input class="ne-hide form-control category-item router" name="neRouterStr" placeholder="Type to select router..." value="" type="text"> 
													<input type="hidden" value="<%=routerCategoryId%>" name="routerCategoryId" class="category-id"> 
													<input name="feRouterID" value="" class="item-id" type="hidden">
												<% } %>
												</div>
											</div>
										<%}else{ %>
											<%=RouterFarEnd%>
										<%} %>
									</td>
								</tr>
								
								
								<tr>	
									<th scope="row">Port Type<%=(editablePortTypeFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%></th>
									<td class="text-muted">
										<%if(editablePortTypeFarEnd){ %>
											<div class="form-group up-down-path">
												<div class="col-md-6">
												<% if( requestTypeID == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR ||	requestTypeID == LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR) {%>
													<select  name="fePortType" class="form-control" >
														<option value="0">Not Available</option>
													<%for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
														<option value="<%=portTypeInventoryAttributeNameType%>" <%=(portTypeInventoryAttributeNameType.equals(farEndDTO.getPortCategoryType())) ? "selected" : ""%>><%=portTypeInventoryAttributeNameType%></option>
													<%}%>
													</select>
												<% } else { %>
													<select  name="fePortType" class="form-control" >
														<option value="0">Not Available</option>
													<%for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
														<option value="<%=portTypeInventoryAttributeNameType%>"><%=portTypeInventoryAttributeNameType%></option>
													<%}%>
													</select>
													<div class="form-group ">
														<div class="col-md-6" style="margin: 30px 5px;"  id="fePortTypeComment">	
															<textarea  class="form-control" rows="3" name="fePortTypeComment" placeholder="Comments..."></textarea>														
														</div>
													</div>
												<% } %>
												</div>
											</div>
										<%}else{ %>
											<%=PortTypeFarEnd%>
										<%} %>
									</td>
								</tr>
								<tr>
									<th scope="row">Port<%=(editablePortTypeFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%></th>
									<td>
										<%if(editablePortTypeFarEnd){ %>
											<div class="form-group up-down-path">
												<div id="fePortItemID" class="col-md-6">
													<% if( requestTypeID == LliRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR ||	requestTypeID == LliRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR) {%>
														<select  name="fePortID" class="form-control portItem">
															<option value="<%=farEndDTO.getPortID()%>"><%=farEndDetailsDTO.getPortName()%></option>
														</select>
													<% } else { %>
														<select  name="fePortID" class="form-control portItem">
															<option value="0">Not Available</option>
														</select>
													<% } %>
												</div>
											</div>
										<%}else{ %>
										<%=PortIDFarEnd%>
									<%} %>
									</td>
								</tr>
								<%--
								<tr>	
									<th scope="row">Mandatory VLAN<%=(editableSlotFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%></th>
									<td class="text-muted">
										<%if(editableSlotFarEnd){ %>
											<div class="form-group ">
												<div class="col-md-10">
													<input id="feMandatoryVlanIdText" class="form-control vlanAutocomplete mandatoryVlan fe-end "  placeholder="Mandatory VLAN" type="text">
													<input type="hidden" value="<%=vlanCategoryId%>" name="vlanCategoryId" class="category-id"> 
													<input id="feMandatoryVlanID" name="feMandatoryVlanID" value="" type="hidden">
												</div>
											</div>
											<div class="form-group ">
												<div class="col-md-10" style="margin: 30px 5px;"  >	
													<textarea  class="form-control" rows="3" name="feMandatoryVLANComment"  id="feMandatoryVLANComment"
															placeholder="Comments..."></textarea>
												</div>
											</div>
										<%}else{ %>
											<%=MandatoryVLanIDFarEnd %>
										<%} %>
									</td>
								</tr>
								
								<tr>	
									<th scope="row">Additional VLAN</th>
									
									<td class="text-muted">
									<%if(editableSlotFarEnd){ %>
										<div class="form-group ">
											<div class="col-md-10">
												<input class="form-control additionalVlan vlanAutocomplete fe-end" name="feAdditionalVLAN" placeholder="Additional VLAN" type="text">
											</div>
										</div>
										<div class="form-group ">
											<div class="col-md-8" >	
												<select id="feAdditionalVlanIDsSelect" name="feAdditionalVlanIDs" class="form-control" style="min-height: 100px;" multiple>
												</select>
												<input name="feAdditionalVlanIDs" type="hidden" value="" />
											</div>
											<div class="col-md-2">
												<a class="btn btn-xs btn-danger" href="#" id="feRemoveAdditionalVlan">Remove</a>	
											</div>
										</div>
										<div class="form-group ">
											<div class="col-md-10" style="margin: 30px 5px;"  >	
												<textarea  class="form-control" rows="3" name="feAdditionalVLANComment" id="feAdditionalVLANComment"
														placeholder="Comments..."><%=StringUtils.trimToEmpty(lliFRResponseInternalDTO.getFePortTypeComment()) %></textarea>
											</div>
										</div>
										<%}else{ %>
										<%=AdditionalVLanIDFarEnd %>
										<%} %>
									</td>
								</tr>
								--%>
								<tr>
									<th scope="row">Address</th>
									<td class="text-muted">${farEnd.address }</td>
								</tr>
								<tr>
									<th scope="row">OFC Type</th>
									<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(farEndDTO.getCoreType()) %></td>
								</tr>
								<tr>
									<th scope="row">Loop Provider</th>
									<td class="text-muted"> <%=EndPointConstants.providerOfOFC.get(farEndDTO.getOfcProviderTypeID()) %></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="flags" style="display:none">
				<input id="isAdditionalEmpty" type="hidden">
			</div>
			<div class="form-actions text-center">
					<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
					<input type="hidden" name="divisionID" value="<%=divisionID%>" />
					<button class="btn btn-reset-btcl" type="reset">Cancel</button>
					<input id="updateBtn" class="btn btn-submit-btcl" name="action" type="button" value="Submit"/>
			</div>
		</form>
	</div>
</div>



<%}catch(Exception ex){ 
logger.debug("ex: ", ex);
}%>

<script src="<%=context %>assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<%@include file="internalFRResponseJS.jsp" %>
<%@include file="ipAddressSelection.jsp" %>
<script src="${context}assets/scripts/lli/link/internal-fr-validation.js" type="text/javascript"></script>


