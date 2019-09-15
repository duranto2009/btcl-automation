<%@page import="util.ServiceDAOFactory"%>
<%@page import="distance.DistanceService"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.List"%>
<%@page import="vpn.link.VpnInternalFRDataLocation"%>
<%@page import="vpn.VpnDAO"%>
<%@page import="common.CategoryConstants"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="vpn.link.VpnBandWidthChangeRequestDTO"%>
<%@page import="util.SqlGenerator"%>
<%@page import="vpn.link.VpnLinkService"%>
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
<%@page import="vpn.link.VpnFRResponseExternalDTO"%>
<%@page import="common.CommonService"%>
<%@page import="vpn.link.VpnFRResponseInternalDTO"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
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
Logger lIntLogger = Logger.getLogger("linkInternalFR");
int entityID = Integer.parseInt(request.getParameter("entityID"));
System.out.println("request.getParameter(entityID) " + request.getParameter("entityID"));
System.out.println("request.getAttribute(entityID) " + request.getAttribute("entityID"));
String context = "" + request.getContextPath() + "/";

String actionName = "../../VpnLinkAction.do?entityID=" + entityID+"&getMode=edit";
String back  = "../../VpnLinkAction.do?entityID=" + entityID+"&entityTypeID="+EntityTypeConstant.VPN_LINK;

VpnNearEndDTO nearEndDTO=(VpnNearEndDTO)request.getAttribute("vpnNE");
VpnEndPointDetailsDTO nearEndDetailsDTO=LinkUtils.getEndPointDTODetails(nearEndDTO);

VpnFarEndDTO farEndDTO=(VpnFarEndDTO)request.getAttribute("vpnFE");
VpnEndPointDetailsDTO farEndDetailsDTO=LinkUtils.getEndPointDTODetails(farEndDTO);

lIntLogger.debug("nearEndDTO " + nearEndDTO);
lIntLogger.debug("farEndDTO " + farEndDTO);

request.setAttribute("nearEnd", nearEndDetailsDTO);
request.setAttribute("farEnd", farEndDetailsDTO);

VpnLinkDTO vpnLinkDTO= (VpnLinkDTO)request.getAttribute("vpnLink");
CommonService comService=new CommonService();
InventoryService inventoryService= new InventoryService();
VpnLinkService vpnLinkService = new VpnLinkService();
VpnFRResponseInternalDTO vpnFRResponseInternalDTO = new VpnFRResponseInternalDTO();

long nePopID=nearEndDTO.getPopID();
long fePopID=farEndDTO.getPopID();
Long neRouterID=null, feRouterID=null;

boolean editableSlotNearEnd=false;
boolean editableSlotFarEnd=false;
boolean editablePOPNearEnd = false;
boolean editablePOPFarEnd = false;
boolean editablePortTypeNearEnd = false;
boolean editablePortTypeFarEnd = false;

VpnNearEndDTO parentNearEndDTO=null;
VpnFRResponseExternalDTO vpnFRResponseExternalDTOForNearEnd = null;
VpnFRResponseExternalDTO vpnFRResponseExternalDTOForFarEnd = null;

int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
int upazilaCategoryId = CategoryConstants.CATEGORY_ID_UPAZILA;
int unionCategoryId = CategoryConstants.CATEGORY_ID_UNION;
int popCategoryId = CategoryConstants.CATEGORY_ID_POP;
int routerCategoryId = CategoryConstants.CATEGORY_ID_ROUTER;
int vlanCategoryId = CategoryConstants.CATEGORY_ID_VLAN;

int portCategoryId = CategoryConstants.CATEGORY_ID_PORT;
boolean previousInternalFRFound = true;
int requestTypeID = Integer.parseInt((String)request.getAttribute("requestTypeID"));


if(requestTypeID == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){
	neRouterID = nearEndDTO.getRouterID();
	feRouterID = farEndDTO.getRouterID();
}


try{
	
	VpnInternalFRDataLocation vpnInternalFRDataLocation =  new VpnDAO().getInternalFRDataLocation(vpnLinkDTO, nearEndDTO, farEndDTO, requestTypeID);
	if((requestTypeID / EntityTypeConstant.MULTIPLIER2) % 100 == 9)//shifting
	{
		if(!new VpnLinkService().isVpLinkNearEndEligibleToShift(vpnLinkDTO.getID())) {
			vpnInternalFRDataLocation.nearEndPopInEndpointDTO = true;
			vpnInternalFRDataLocation.nearEndPortInEndpointDTO = true;
			vpnInternalFRDataLocation.nearEndSlotInEndpointDTO = true;
			
			vpnInternalFRDataLocation.nearEndPopInFRResponse = false;
			vpnInternalFRDataLocation.nearEndPortInFRResponse = false;
			vpnInternalFRDataLocation.nearEndSlotInFRResponse = false;
		}
		if(!new VpnLinkService().isVpnLinkFarEndEligibleToShift(vpnLinkDTO.getID())) {
			vpnInternalFRDataLocation.farEndPopInEndpointDTO = true;
			vpnInternalFRDataLocation.farEndPortInEndpointDTO = true;
			vpnInternalFRDataLocation.farEndSlotInEndpointDTO = true;
			
			vpnInternalFRDataLocation.farEndPopInFRResponse = false;
			vpnInternalFRDataLocation.farEndPortInFRResponse = false;
			vpnInternalFRDataLocation.farEndSlotInFRResponse = false;
		}
		
	}
	String upazilaNameNear = inventoryService.getInventoryItemByItemID(nearEndDTO.getUpazilaID()).getName();
	String UpozillaNearEnd = (upazilaNameNear == null?"-":upazilaNameNear);
	
	String unionNameNear = inventoryService.getInventoryItemByItemID(nearEndDTO.getUnionID()).getName();
	String UnionNearEnd = (unionNameNear == null?"-":unionNameNear);
	
	String PopNearEnd = "";
	String PortTypeNearEnd = "";
	String PortIDNearEnd = "";
	String RouterNearEnd = "";
	String MandatoryVLanIDNearEnd = "-";
	String AdditionalVLanIDNearEnd = "-";
	
	String upazilaNameFar = inventoryService.getInventoryItemByItemID(farEndDTO.getUpazilaID()).getName();
	String UpozillaFarEnd = (upazilaNameFar == null ? "-" : upazilaNameFar);
	
	String unionNameFar = inventoryService.getInventoryItemByItemID(farEndDTO.getUnionID()).getName();
	String UnionFarEnd = (unionNameFar == null ? "-" : unionNameFar);
	
	
	String PopFarEnd = "";
	String PortTypeFarEnd = "";
	String PortIDFarEnd = "";
	String RouterFarEnd = "";
	String MandatoryVLanIDFarEnd = "-";
	String AdditionalVLanIDFarEnd = "-";
	
	
	
	
	if(vpnInternalFRDataLocation.farEndPopInEndpointDTO){
		String farEndName = inventoryService.getInventoryItemByItemID(farEndDTO.getPopID()).getName();
		PopFarEnd = (farEndName==null?"-":farEndName);
	}
	if(vpnInternalFRDataLocation.farEndSlotInEndpointDTO){
		
		String farEndRouterName = inventoryService.getInventoryItemByItemID(farEndDTO.getRouterID()).getName();
		RouterFarEnd = (farEndRouterName == null ? "-" : farEndRouterName);
		/*
		MandatoryVLanIDFarEnd = "" + LinkUtils.getAdditionalVLanNames(farEndDTO.getMandatoryVLanID());
		AdditionalVLanIDFarEnd = "" + LinkUtils.getAdditionalVLanNames(farEndDTO.getAdditionalVLanID());
		*/
	}
	if(vpnInternalFRDataLocation.farEndPortInEndpointDTO){
		PortTypeFarEnd = farEndDTO.getPortType();
		String portIDFarName = inventoryService.getInventoryItemByItemID(farEndDTO.getPortID()).getName();
		PortIDFarEnd = (portIDFarName == null ? "-" : portIDFarName); 
	}
	editablePOPFarEnd = vpnInternalFRDataLocation.farEndPopInFRResponse;
	editableSlotFarEnd = vpnInternalFRDataLocation.farEndSlotInFRResponse;
	editablePortTypeFarEnd = vpnInternalFRDataLocation.farEndPortInFRResponse;
	
	/**** Near end *****/
	
	if(vpnInternalFRDataLocation.nearEndPopInEndpointDTO){
		String popNearName = inventoryService.getInventoryItemByItemID(nearEndDTO.getPopID()).getName();
		PopNearEnd = (popNearName == null ? "-" : popNearName);
		
	}
	if(vpnInternalFRDataLocation.nearEndSlotInEndpointDTO){
		String routerNearName = inventoryService.getInventoryItemByItemID(nearEndDTO.getRouterID()).getName();
		RouterNearEnd = (routerNearName == null ? "-" : routerNearName);
		/*
		MandatoryVLanIDNearEnd = "" + LinkUtils.getAdditionalVLanNames(nearEndDTO.getMandatoryVLanID());
		AdditionalVLanIDNearEnd = "" + LinkUtils.getAdditionalVLanNames(nearEndDTO.getAdditionalVLanID());
		*/
	}
	if(vpnInternalFRDataLocation.nearEndPortInEndpointDTO){
		PortTypeNearEnd = nearEndDTO.getPortType();
		String portNearName = inventoryService.getInventoryItemByItemID(nearEndDTO.getPortID()).getName();
		PortIDNearEnd = (portNearName == null ? "-" : portNearName);
	}
	editablePOPNearEnd = vpnInternalFRDataLocation.nearEndPopInFRResponse;
	editableSlotNearEnd = vpnInternalFRDataLocation.nearEndSlotInFRResponse;
	editablePortTypeNearEnd = vpnInternalFRDataLocation.nearEndPortInFRResponse;	

	lIntLogger.debug("editableSlotNearEnd " + editableSlotNearEnd + " editablePOPNearEnd " + editablePOPNearEnd + " editablePortTypeNearEnd " + editablePortTypeNearEnd);
	


if(request.getAttribute("entityTypeIDReplace") != null){
	request.setAttribute("entityTypeID", request.getAttribute("entityTypeIDReplace"));
}
if(request.getAttribute("entityIDReplace") != null){
	request.setAttribute("entityID", request.getAttribute("entityIDReplace"));
}
//editableSlotNearEnd=true; //this line is added hardcoded. please check and delete this line 
//editableSlotFarEnd=true;  //this line is added hardcoded. please check and delete this line 

//d make Port Type dynamic
List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(CategoryConstants.CATEGORY_ID_PORT);
InventoryAttributeName portTypeInventoryAttributeName = new InventoryAttributeName();;
for(InventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
	if(inventoryAttributeName.getName().equals("Port Type")){
		portTypeInventoryAttributeName = inventoryAttributeName;
		break;
	}
}

DistanceService distanceService = ServiceDAOFactory.getService(DistanceService.class);
%>


<div class="portlet box portlet-btcl ">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-link"></i>Link Internal FR Response Details
		</div>
		<div class="actions">
			<a  href="<%=back%>" class="btn btn-back-btcl" style="padding-left: 15px; padding-right: 15px;"> Back </a>
		</div>
	</div>
	
	<div class="portlet-body" id="printContent">
		<form  id="fileupload"class="form-horizontal" method="post" action="../../CommonAction.do?mode=internalFrResponse"  >
			<%-- <input type="hidden" name="entityID" value = "${vpnLink.ID}" />
			<input type="hidden" name="linkID" value = "${vpnLink.ID}" /> --%>
			<input type='hidden' name='requestTypeID' value="<%=request.getAttribute("requestTypeID")%>"> 
			
			<input type='hidden' name='entityTypeID' value="<%=request.getAttribute("entityTypeID")%>">
			<input type='hidden' name='entityID' value="<%=request.getAttribute("entityID")%>">
			<input type='hidden' name='rootEntityTypeID' value="<%=request.getAttribute("entityTypeID")%>">
			<input type='hidden' name='rootEntityID' value="<%=request.getAttribute("entityID")%>">
			
			<input type="hidden" name="clientID" value="${clientID}" >
			<%-- <input type='hidden' name='sourceRequestID' value="<%=request.getAttribute("sourceRequestID")%>"> --%>
			<input type='hidden' name='requestToAccountID' value="<%=request.getParameter("requestTo")%>">
			<input name="actionName" value="VpnLinkAction.do?entityID=${vpnLink.ID}&entityTypeID=<%=EntityTypeConstant.VPN_LINK %>" class="actionName" type="hidden">
		
	
			<div class="row">
				<div class="col-md-12">
					<div class="table-responsive">
						<table class="table table-bordered  table-striped">
							<thead><tr><th colspan="3" class="text-center"><h1> Link Details </h1></th></tr></thead>
							<tbody>
								<tr>
									<th scope="row">Client Name</th>
									<td colspan="2">${clientName}</td>
								</tr>
								<tr>
									<th scope="row">Link Name</th>
									<td colspan="2">${vpnLink.linkName}</td>
								</tr>
								<tr>
									<th scope="row">Bandwidth</th>
									<td colspan="2">${vpnLink.vpnBandwidth} <%=EntityTypeConstant.linkBandwidthTypeMap.get(vpnLinkDTO.getVpnBandwidthType()) %></td>
								</tr>
								<tr>
									<th scope="row">Bandwidth Available (?)</th>
									<td colspan="2">
										<div class="radio-list" style="display: inline-block;">
											<label class="radio-inline"> 
												<span> <input type="radio" name="bandwidthAvailablity" value="0" > </span>  Not Available
											</label>
											<label class="radio-inline"> 
												<span> <input type="radio" name="bandwidthAvailablity" value="1" checked ></span>  Available
											</label>
										</div>
									</td>
								</tr>
								<tr id="bandWidthComment" style="display:none">
									<th scope="row">Bandwidth Non-Availability Reason<span style="color:red" aria-required="true"> * </span></th>
									<td colspan="2">
										<div class="col-sm-12">
											<textarea class="form-control inside-table" rows="3" name="bandWidthComment"
												placeholder="Comments..."></textarea>
										</div>
									</td>
								</tr>
								<%if(requestTypeID == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == VpnRequestTypeConstants.REQUEST_POP_CHANGE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){ 
									double suggestedDistace = distanceService.getDistanceBetweenTwoLocation(nearEndDTO.getUnionID(), farEndDTO.getUnionID());
								%>
								<tr>
									<th scope="row">Pop-Pop Distance</th>
									<td colspan="2">
										<div class=col-md-12>
											<div class=col-md-5>
												<input type=number class=form-control name=vpnLoopDistance value='<%=(vpnLinkDTO.getVpnLoopDistance() > 0 ? vpnLinkDTO.getVpnLoopDistance() : suggestedDistace) %>'>
											</div>
											<div class=col-md-6>
												<p class=form-control>Suggested Distance: <%=suggestedDistace%> KM</p>
											</div>
										</div>
									</td>
								</tr>
								<%}%>
								<tr>
									<th scope="row">Description</th>
									<td colspan="2">${vpnLink.linkDescription}</td>
								</tr> 
								
							</tbody>
						</table>
					</div>	
					
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th colspan="3" class="text-center">
										<h3> End Point Details </h3>
									</th >
								</tr>
							</thead>
					
							<tbody>
								<tr>
									<th scope="row"></th>
									<td class="text-muted"><h4><b>LOCAL END</b></h4></td>
									<td class="text-muted"><h4><b>REMOTE END</b></h4></td>
								</tr>
								<tr>
									<th scope="row">Name</th>
									<td class="text-muted">${nearEnd.vepName}</td>
									<td class="text-muted">${farEnd.vepName }</td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></th>
									<td class="text-muted">${nearEnd.districtName }</td>
									<td class="text-muted">${farEnd.districtName }</td>
								</tr>
							
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></th>
									<td class="text-muted"><%=UpozillaNearEnd%></td>
									<td class="text-muted"><%=UpozillaFarEnd%></td>
								</tr>
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></th>
									<td class="text-muted"><%=UnionNearEnd%></td>
									<td class="text-muted"><%=UnionFarEnd%></td>
								</tr>
								
								<tr>
									<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %>
										<%=(editablePOPNearEnd || editablePOPFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
									</th>
									<%if(editablePOPNearEnd){%>
									<td>
										<div class="form-group up-down-path">
											<div class="col-sm-12">
												<input class="ne-hide form-control category-item " name="nePopIdStr" placeholder="Type to select pop..." value="<%=nearEndDetailsDTO.getPopName()%>" type="text"> 
												<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId"	class="category-id"> 
												<input type="hidden" class='item-id' name="nePopID" value="<%=nearEndDetailsDTO.getPopID()%>">
											</div>
										</div>
									</td>
									<%}else{%>
									<td class="text-muted"><%=PopNearEnd%>							
										<input type="hidden" name="nePopID" value="<%=nePopID%>"/>
									</td>					
									<%}%>
									<%if(editablePOPFarEnd){%>
									<td>
										<div class="form-group up-down-path">
											<div class="col-sm-12">
												<input class="fe-hide form-control category-item" name="fePopIdStr" placeholder="Type to select pop..." value="<%=farEndDetailsDTO.getPopName()%>" type="text">
												<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id">
												<input type="hidden" class='item-id' name="fePopID" value="<%=farEndDetailsDTO.getPopID()%>">
											</div>
										</div>
									</td>
									<%}else{%>			
									<td class="text-muted">									
										<%=PopFarEnd%>
										<input type="hidden" name="fePopID" value="<%=fePopID%>"/>
									</td>
									<%}%>
								</tr>
						
							<tr>
								<th scope="row">Router/Switch
									<%=(editableSlotNearEnd || editableSlotFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
								</th>
								
								<%if(editableSlotNearEnd){ %>
<%-- 									<% if(requestTypeID == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){ %> --%>
									<td>
										<div class="form-group up-down-path">
											<div class="col-sm-12">
												<input class="ne-hide form-control category-item router" name="neRouterStr" value="<%=nearEndDetailsDTO.getRouterName()%>" type="text"> 
												<input type="hidden" value="<%=routerCategoryId%>" name="routerCategoryId" class="category-id"> 
												<input name="neRouterID"  class='item-id' value="<%=nearEndDTO.getRouterID()%>" type="hidden" id="neRouterID">
											</div>
										</div>
									</td>
<%-- 									<% } else { %> --%>
<!-- 									<td> -->
<!-- 										<div class="form-group up-down-path"> -->
<!-- 											<div class="col-sm-10"> -->
<!-- 												<input class="ne-hide form-control category-item router" name="neRouterStr" placeholder="Type to select router..." value="" type="text">  -->
<%-- 												<input type="hidden" value="<%=routerCategoryId%>" name="routerCategoryId" class="category-id">  --%>
<!-- 												<input name="neRouterID" value="" type="hidden" id="neRouterID"> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</td> -->
<%-- 									<% } %> --%>
								<%}else{ %>
									<td class="text-muted"><%=RouterNearEnd%></td>
								<%} %>
								
								
								<%if(editableSlotFarEnd){ %>
<%-- 									<% if(requestTypeID == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){ %> --%>
									<td>
										<div class="form-group up-down-path">
											<div class="col-sm-12">
												<input class="fe-hide form-control category-item router" name="feRouterStr" value="<%=farEndDetailsDTO.getRouterName()%>" type="text"> 
												<input type="hidden" value="<%=routerCategoryId%>" name="routerCategoryId" class="category-id"> 
												<input name="feRouterID" value="<%=farEndDTO.getRouterID()%>" type="hidden" id="feRouterID">
											</div>
										</div>
									</td>
<%-- 									<% } else { %> --%>
<!-- 									<td> -->
<!-- 										<div class="form-group up-down-path"> -->
<!-- 											<div class="col-sm-10"> -->
<!-- 												<input class="fe-hide form-control category-item router" name="feRouterStr" placeholder="Type to select router..." value="" type="text">  -->
<%-- 												<input type="hidden" value="<%=routerCategoryId%>" name="routerCategoryId" class="category-id">  --%>
<!-- 												<input name="feRouterID" value="" type="hidden" id="feRouterID"> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</td> -->
<%-- 									<% } %> --%>
								<%}else{ %>
									<td class="text-muted"><%=RouterFarEnd%></td>
								<%} %>
							</tr>
							<tr>	
								<th scope="row">Port Type
									<%=(editablePortTypeNearEnd || editablePortTypeFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
								</th>
								<td class="text-muted">
									<%if(editablePortTypeNearEnd){ %>
<%-- 										<% if(requestTypeID == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){ %> --%>
										<div class="form-group up-down-path">
											<div class="col-md-12">
												<select  name="nePortType" class="form-control" >
													<option value="0">--- Select ---</option>
												<%for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
													<option value="<%=portTypeInventoryAttributeNameType%>" <%=(portTypeInventoryAttributeNameType.equals(nearEndDTO.getPortCategoryType())) ? "selected" : "" %>><%=portTypeInventoryAttributeNameType%></option>
												<%}%>
												</select>
											</div>
										</div>

										<div class="form-group ">
											<div class="col-md-12" style= "margin: 30px 5px;" id="nePortTypeComment">	
													<textarea  class="form-control" rows="3" name="nePortTypeComment"
														placeholder="Comments..."></textarea>
											</div>
										</div>
									<%}else{ %>
										<%=PortTypeNearEnd%>
									<%} %>
								</td>
								<td class="text-muted">
									<%if(editablePortTypeFarEnd){ %>
<%-- 										<% if(requestTypeID == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){ %> --%>
										<div class="form-group up-down-path">
											<div class="col-md-12">
												<select  name="fePortType" class="form-control" >
												<option value="0">--- Select ---</option>
												<%for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
													<option value="<%=portTypeInventoryAttributeNameType%>" <%=(portTypeInventoryAttributeNameType.equals(farEndDTO.getPortCategoryType())) ? "selected" : ""%>><%=portTypeInventoryAttributeNameType%></option>
												<%}%>
												</select>
											</div>
										</div>
										<div class="form-group ">
											<div class="col-md-12" style="margin: 30px 5px;"  id="fePortTypeComment">	
												<textarea  class="form-control" rows="3" name="fePortTypeComment"
														placeholder="Comments..."></textarea>														
											</div>
										</div>
									<%}else{ %>
										<%=PortTypeFarEnd%>
									<%} %>
								</td>
								</tr>
								<tr>
									<th scope="row">Port
										<%=(editablePortTypeNearEnd || editablePortTypeFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
									</th>
									
									<td>
									<%if(editablePortTypeNearEnd){ %>
										<div class="form-group ">
											<div id="nePortItemID" class="col-md-12">
											
												<select  name="nePortID" class="form-control portItem" required>
													<option value="<%=nearEndDTO.getPortID()%>"><%=nearEndDetailsDTO.getPortName() %></option>
												</select>
											</div>
										</div>
									</td>
									<%}else{ %>
										<%=PortIDNearEnd%>
									<%} %>
									<td>
									<%if(editablePortTypeFarEnd){ %>
<%-- 										<% if(requestTypeID == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR || requestTypeID == VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR){ %> --%>
										<div class="form-group ">
											<div id="fePortItemID" class="col-md-12">
												<select name="fePortID" class="form-control portItem" required>
													<option value="<%=farEndDTO.getPortID()%>"><%=farEndDetailsDTO.getPortName() %></option>
												</select>
											</div>
										</div>										
									<%}else{ %>
										<%=PortIDFarEnd%>
									<%} %>
									</td>
								</tr>
								
								<%--
								<tr>	
									<th scope="row">Mandatory VLAN
										<%=(editableSlotNearEnd || editableSlotFarEnd) ? "<span style='color:red' aria-required='true'> * </span>" : ""%>
									</th>
									<td class="text-muted" id="neMandatoryVlanTd">
										<%if(editableSlotNearEnd){ %>
											<div class="form-group up-down-path">
												<div class="col-md-12">
													<input id="neMandatoryVlanIdText" class="form-control vlanAutocomplete mandatoryVlan ne-end neMandatoryVlan" value="" placeholder="Mandatory VLAN" type="text">
													<input id="neMandatoryVlanID" name="neMandatoryVlanID" value="" type="hidden">
												</div>
											</div>
												<div class="form-group ">
													<div class="col-md-12" style= "margin: 30px 5px;" >	
														<textarea  class="form-control" rows="3" name="neMandatoryVLANComment" id="neMandatoryVLANComment"
																placeholder="Comments..."></textarea>																
													</div>
											</div>
										<%}else{ %>
										<%=MandatoryVLanIDNearEnd %>
										<%} %>
									</td>
									
									<td class="text-muted" id="feMandatoryVlanTd">
									<%if(editableSlotFarEnd){ %>
											<div class="form-group ">
												<div class="col-md-12">
													<input id="feMandatoryVlanIdText" class="form-control vlanAutocomplete mandatoryVlan fe-end feMandatoryVlan" value="" placeholder="Mandatory VLAN" type="text">
													<input id="feMandatoryVlanID" name="feMandatoryVlanID" value="" type="hidden">
												</div>
											</div>
											<div class="form-group ">
												<div class="col-md-12" style="margin: 30px 5px;"  >	
													<textarea  class="form-control" rows="3" name="feMandatoryVLANComment"  id="feMandatoryVLANComment"
															placeholder="Comments..."><%=StringUtils.trimToEmpty(vpnFRResponseInternalDTO.getFePortTypeComment()) %></textarea>
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
										<%if(editableSlotNearEnd){ %>
										<div class="form-group up-down-path">
											<div class="col-md-12">
												<input class="form-control additionalVlan vlanAutocomplete ne-end" name="neAdditionalVLAN" placeholder="Additional VLAN" type="text">
											</div>
											</div>
											<div class="form-group ">
												<div class="col-md-8" >	
													<select id="neAdditionalVlanIDsSelect" name="neAdditionalVlanIDs" class="form-control" style="min-height: 100px;" multiple>
													</select>
													<input name="neAdditionalVlanIDs" type="hidden" value="" />
												</div>
												<div class="col-md-2">
													<a class="btn btn-xs btn-danger" href="#" id="neRemoveAdditionalVlan">Remove</a>	
												</div>
											</div>
											<div class="form-group ">
												<div class="col-md-12" style= "margin: 30px 5px;" >	
													<textarea  class="form-control" rows="3" name="neAdditionalVLANComment" id="neAdditionalVLANComment"
															placeholder="Comments..."><%=StringUtils.trimToEmpty(vpnFRResponseInternalDTO.getNePortTypeComment()) %></textarea>
												</div>
											</div>
										<%}else{ %>
										<%=AdditionalVLanIDNearEnd %>
										<%} %>
									</td>
									
									<td class="text-muted">
									<%if(editableSlotFarEnd){ %>
										<div class="form-group ">
											<div class="col-md-12">
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
											<div class="col-md-12" style="margin: 30px 5px;"  >	
												<textarea  class="form-control" rows="3" name="feAdditionalVLANComment" id="feAdditionalVLANComment"
														placeholder="Comments..."><%=StringUtils.trimToEmpty(vpnFRResponseInternalDTO.getFePortTypeComment()) %></textarea>
											</div>
										</div>
										<%}else{ %>
										<%=AdditionalVLanIDNearEnd %>
										<%} %>
									</td>
								</tr>
								 --%>
								<tr>
									<th scope="row">Address</th>
									<td class="text-muted">${nearEnd.address }</td>
									<td class="text-muted">${farEnd.address }</td>
								</tr>
								<tr>
									<th scope="row">OFC Type</th>
									<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(nearEndDTO.getCoreType()) %></td>
									<td class="text-muted"><%=EndPointConstants.coreTypeMap.get(farEndDTO.getCoreType()) %></td>
								</tr>
								<tr>
									<th scope="row">Loop Provider</th>
									<td class="text-muted"><%=EndPointConstants.providerOfOFC.get(nearEndDTO.getOfcProviderTypeID()) %></td>
									<td class="text-muted"> <%=EndPointConstants.providerOfOFC.get(farEndDTO.getOfcProviderTypeID()) %></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			
			<div class="form-actions text-center">
					<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_VPN %>" />
					<button class="btn btn-reset-btcl" type="reset">Cancel</button>
					<input id="updateBtn" class="btn btn-submit-btcl" name="action" type="button" value="Submit"/>
			</div>
		</form>
	</div>
</div>
<%}catch(Exception ex){ 
lIntLogger.debug("ex: ", ex);
}%>
<script src="<%=context %>assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript">
	var vlanCategoryID=<%=CategoryConstants.CATEGORY_ID_VLAN%>;
	var routerCategoryID=<%=CategoryConstants.CATEGORY_ID_ROUTER%>;
	var popCategoryID = <%=CategoryConstants.CATEGORY_ID_POP%>;
	
	$(document).ready(function() {
		$("input[name=bandwidthAvailablity]").click(function(){
			if($(this).val()=='0'){
				$('#bandWidthComment').show();
			}else{
				$('#bandWidthComment').hide();
			}
		})
		
		$("select[name=nePortType]").change(function(){
			if($(this).val()=='0'){
				$('#nePortTypeComment').show();
				$('#nePortItemID').hide();
			}else{
				$('#nePortTypeComment').hide();
				$('#nePortItemID').show();
				loadPortItems($("#neRouterID").val(), $(this).val(),"#nePortItemID");
			}
		})
		
		$("select[name=fePortType]").change(function(){
			if($(this).val()=='0'){
				$('#fePortTypeComment').show();
				$('#fePortItemID').hide();
			}else{
				$('#fePortTypeComment').hide();
				$('#fePortItemID').show();
				loadPortItems($("#feRouterID").val(), $(this).val(), "#fePortItemID");
			}
		})
		
		$("#neRemoveAdditionalVlan").click(function(){
			$('#neAdditionalVlanIDsSelect option:selected').each(function() {
				$(this).remove();
			});
			$("input[name=neAdditionalVlanIDs]").val("");
			return false;
		})
		
		$("#feRemoveAdditionalVlan").click(function(){
			$('#feAdditionalVlanIDsSelect option:selected').each(function() {
				$(this).remove();
			});
			$("input[name=feAdditionalVlanIDs]").val("");
			return false;
		})
		
		$('#fileupload #updateBtn').click(function(e) {
			bootbox.confirm("Are you sure to Submit? After submission it can not be modified.", function(result) {
                   if (result) {
                	   var neAdditionIDs = [];
                	   $("#neAdditionalVlanIDsSelect > option").each(function(){
                		   neAdditionIDs.push(this.value);
                	   });
                	   
                	   var feAdditionIDs = [];
                	   $("#feAdditionalVlanIDsSelect > option").each(function(){
                		   feAdditionIDs.push(this.value);
                	   });
                	   
                	   $("input[name=neAdditionalVlanIDs]").val(neAdditionIDs.join(", "));
                	   $("input[name=feAdditionalVlanIDs]").val(feAdditionIDs.join(", "));
                	   
                       $("#fileupload").submit();
                   }
               });
		 	return false;
       	});
		function loadPortItems(parentItemID, portType, portContainer) {
			var url= '../../AutoInventoryItem.do';
	        var data= {
	            'categoryID': <%=CategoryConstants.CATEGORY_ID_PORT%>,
	            'parentItemID': parentItemID ,
	            'attributeName' : "Port Type",
	            'attributeValue': portType ,
	            <%if(requestTypeID == VpnRequestTypeConstants.REQUEST_UPGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR 
	            || requestTypeID == VpnRequestTypeConstants.REQUEST_DOWNGRADE.SYSTEM_RESPONSE_WITH_INTERNAL_FR
	            || requestTypeID == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_WITH_INTERNAL_FR){ %>
	            'vpnLinkID' : <%=vpnLinkDTO.getID()%>
	            <%}%>
	        };
			ajax(url, data, function(data){ 
            	var ports = '<option value=0 selected>---Select Port---</option>';
		        if(data.length > 0) {
	               $.each(data, function(i, port) {
	            	   ports += "<option value='" + port.ID + "'>" + port.name + "</option>";
	               })
	            } else {
	            	ports = '<option value=0 selected>---No Port Is Available---</option>';
	            }
		        $(portContainer).find('.portItem').html(ports);
			  }, "GET", [$(portContainer)]);
		   }
			
		/***load mandatory vlan***/
		
		var vlanAutocomplete;
		$(".vlanAutocomplete").each(function() {
		      $(this).autocomplete({
		         source: function(request, response) {
		        	vlanAutocomplete = this.element;
		        	if(vlanAutocomplete.hasClass('mandatoryVlan')){
		        		if(vlanAutocomplete.hasClass('ne-end')){
		        			$("#neMandatoryVlanID").val("");
		        		}else{
		        			$("#feMandatoryVlanID").val("");
		        		}
		        	}else{
		        		if(vlanAutocomplete.hasClass('ne-end')){
		        			if($("#neMandatoryVlanID").val() == undefined || $("#neMandatoryVlanID").val() == ""){
		        				 toastr.error("Select Mandatory VLAN First");
		        			 }
		        		}else{
		        			if($("#feMandatoryVlanID").val() == undefined || $("#feMandatoryVlanID").val() == ""){
		        				 toastr.error("Select Mandatory VLAN First");
		        			 }
		        		}
		        	}
		        	
		            var data={};
		            if(this.element.hasClass('ne-end')){
		            	data['parentItemID'] = $('input[name=neRouterID]').val();
		            }else{
		            	data['parentItemID'] =  $('input[name=feRouterID]').val();
		            }
		            if(null == data.parentItemID){
		            	toastr.error("Select Router First");
		            	response({});
		            }else{
		            	data['categoryID'] = vlanCategoryID;  // for mandatory vlan
			            var url = '../../AutoInventoryItem.do?partialName=' + request.term;
			            callAjax(url, data, response, "GET")
		            }
		         },
		         minLength: 1,
		         select: function(e, ui) {
		        	 if(vlanAutocomplete.hasClass('ne-end')){
		        		 if(vlanAutocomplete.hasClass('mandatoryVlan')){
		        			 
		        			var neAdditionalVlanValues = [];
		        			var isAlreadyUsedInAdditional = false;
	        	    		$('select[name=neAdditionalVlanIDs] option').each(function(){
	        	    			neAdditionalVlanValues.push($(this).val());
	        	    		});
	        				$.each(neAdditionalVlanValues, function(index, neAdditionalVlanID){
	        					if(neAdditionalVlanID == ui.item.ID){
	        						isAlreadyUsedInAdditional = true;
	        					}
	        				});
	        				if(isAlreadyUsedInAdditional){
	        					toastr.error("Already used as an Additional VLAN");
	        				}else{
	        					$("#neMandatoryVlanIdText").val(ui.item.name);
			        			 $("#neMandatoryVlanID").val(ui.item.ID);
	        				}
		        		 }else{
		        			 if($("#neMandatoryVlanID").val() != undefined && $("#neMandatoryVlanID").val() != ""){
		        				 if($("#neMandatoryVlanID").val() != ui.item.ID){
		        					 var option="<option value='"+ui.item.ID+"''>"+ui.item.name+"</option>";		        			 
				        			 $("#neAdditionalVlanIDsSelect").append(option);
				        			 vlanAutocomplete.val("");
		        				 }else{
		        					 toastr.error("Already used as the Mandatory VLAN");
		        				 }
		        			 }else{
		        				 toastr.error("Select Mandatory VLAN First");
		        			 }
		        		 }
		        	 }else{
		        		 if(vlanAutocomplete.hasClass('mandatoryVlan')){
		        			var feAdditionalVlanValues = [];
		        			var isAlreadyUsedInAdditional = false;
	        	    		$('select[name=feAdditionalVlanIDs] option').each(function(){
	        	    			feAdditionalVlanValues.push($(this).val());
	        	    		});
	        				$.each(feAdditionalVlanValues, function(index, feAdditionalVlanID){
	        					if(feAdditionalVlanID == ui.item.ID){
	        						isAlreadyUsedInAdditional = true;
	        					}
	        				});
	        				if(isAlreadyUsedInAdditional){
	        					toastr.error("Already used as an Additional VLAN");
	        				}else{
	        					$("#feMandatoryVlanIdText").val(ui.item.name);
			        			 $("#feMandatoryVlanID").val(ui.item.ID);
	        				}
		        		 }else{
		        			 if($("#feMandatoryVlanID").val() != undefined  && $("#feMandatoryVlanID").val() != ""){
		        				 if($("#feMandatoryVlanID").val() != ui.item.ID){
		        					 var option="<option value='"+ui.item.ID+"''>"+ui.item.name+"</option>";		        			 
				        			 $("#feAdditionalVlanIDsSelect").append(option);
				        			 vlanAutocomplete.val("");
		        				 }else{
		        					 toastr.error("Already used as the Mandatory VLAN");
		        				 }
		        			 }else{
		        				 toastr.error("Select Mandatory VLAN First");
		        			 }
		        		 }
		        	 }
		        	 
		        	return false;
		         },
		      }).autocomplete("instance")._renderItem = function(ul, item) {
// 		         console.log(item);
		         return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
		      }
		   });
		
		
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
		            		map['parentItemID'] = $('input[name=nePopID]').val();
		            	}else if (index==2){
		            		map['parentItemID'] = $('input[name=fePopID]').val();
		            	}
		            }else if(categoryObj.val()==popCategoryID){// start from router
		            	map['isParentNeeded'] = "false";
		            }else{
		            	var tempVal=parentItemObj.next().next().val();
		            	map['parentItemID'] = tempVal; 
		            }
		            
		            map['categoryID'] = categoryObj.val();
		            var url = '../../AutoInventoryItem.do?partialName=' + request.term;
		            ajax(url, map, response, "GET", [$(this)]);
		            var whichTD = (index - 1)%2 == 0 ? 'even': 'odd';
		            currentAutoComplete.closest("tr").nextAll("tr").find('td:'+whichTD).find(".category-item").val("");
		            currentAutoComplete.closest("tr").nextAll("tr").find('td:'+whichTD).find(".item-id").val("");
		            currentAutoComplete.closest("tr").nextAll("tr").find('td:'+whichTD).find("select").val("0");
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
// 		         console.log(item);
		         return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
		      }
		   });
		   
		   $("#neMandatoryVlanIdText").change(function(){
			   $("#feMandatoryVlanIdText").trigger("keyup");
		   });
		   $("#feMandatoryVlanIdText").change(function(){
			   $("#neMandatoryVlanIdText").trigger("keyup");
		   });
		   
		   
	    <%if(vpnFRResponseInternalDTO != null && vpnFRResponseInternalDTO.isBandWidthIsAvailable()){%>
			$('#bandWidthComment').hide();
		<%}%>
	})
</script>


<script src="${context}assets/scripts/vpn/link/internal-fr-validation.js" type="text/javascript"></script>