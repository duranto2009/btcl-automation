<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.List"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="language.VpnLanguageConstants"%>
<%@page import="vpn.link.VpnFarEndDTO"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
<%@page import="inventory.InventoryService"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.CategoryConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="file.FileTypeConstants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>

<%
Logger logger = Logger.getLogger(this.getClass());

int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
int upazilaCategoryId = CategoryConstants.CATEGORY_ID_UPAZILA;
int unionCategoryId = CategoryConstants.CATEGORY_ID_UNION;
int popCategoryId = CategoryConstants.CATEGORY_ID_POP;
int routerCategoryId = CategoryConstants.CATEGORY_ID_ROUTER;
int portCategoryId = CategoryConstants.CATEGORY_ID_PORT;

LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

boolean isAnyOtherLinkWithThisNearCurrentyBeingProcessed = false;

//d dynamically generates port type options
List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(CategoryConstants.CATEGORY_ID_PORT);
InventoryAttributeName portTypeInventoryAttributeName = new InventoryAttributeName();;
for(InventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
	if(inventoryAttributeName.getName().equals("Port Type")){
		portTypeInventoryAttributeName = inventoryAttributeName;
		break;
	}
}

%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption"><i class="fa fa-link"></i>Shift Link</div>

	</div>
	<div class="portlet-body form">
		<form  id="fileupload" class="form-horizontal" method="post" action="<%=request.getContextPath()%>/VPN/Link/Shift/Add.do">

		 	<div class="form-body">
			 	<% if (!loginDTO.getIsAdmin()) { %>
				<input id="clientID" type="hidden" class="form-control" value="<%=loginDTO.getAccountID() %>" >		
				<%}else{%>
				<div class="form-group">
					<label for="clientIDName" class="col-sm-3 control-label">Select Client</label>
					<div class="col-sm-6">
						<input id="clientIDName" type="text" class="form-control">
						<input id="clientID" type="hidden" class="form-control" value="" >
					</div>
				</div>
				<%}%>
				
				<div class="form-group">
					<label for="vpnLinkIDName" class="col-sm-3 control-label">Select Link</label>
					<div class="col-sm-6">
						<input id="vpnLinkIDName" type="text" class="form-control">
						<input id="vpnLinkID" type="hidden" class="form-control" name="vpnLinkID" value="" >
					</div>
				</div>
				
				
				
				<!-- End Points -->
				<div class="form-group">
					
					<div class="col-md-6 " id="nearFormInputs" >
						<!-- BEGIN Portlet PORTLET-->
						<div class="portlet light" style="margin-bottom: 0px; padding-bottom: 0px;">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-level-up"></i> 
										<span class="caption-subject bold font-grey-gallery uppercase"> <%=VpnLanguageConstants.LOCAL_END %> </span>
								</div>
								<div id="localAvailabilityStatus" style="text-align:right;"><p></p></div>
							</div>
							
							<div class="portlet-body">
							
								<div class="form-group">
									<label class="col-sm-3 control-label">Shift Mode</label>
	
									<div class="col-sm-8">
										<select name=neShiftMode class="form-control shiftMode">
											<option value=0>Select a Mode</option>
											<option value=1>Do not shift</option>
											<option value=2>Shift to a new office</option>
											<option value=3>Shift to an existing office</option>
										</select>
									</div>
								</div>
							
								<div id="neShiftMode1" style="display:none">
								<input type="hidden" name="neID" value="-1">
								</div>
								
								<div id="neShiftMode2" style="display:none">
								<div class="form-group">
									<label class="col-sm-3 control-label" for="neName"> <%=VpnLanguageConstants.END_NAME %></label>
	
									<div class="col-sm-8">
										<input type="text" placeholder="Near End Name" name="neName" class=" form-control endName" id="nearEnd" autocomplete=off>
										<input type="hidden" name="neID" class="form-control" id="nearEndHidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item district" placeholder="Type to select district..." value="" type="text"> 
										<input type="hidden" value="<%=districtCategoryId%>" name="districtCategoryId" class="category-id"> 
										<input name="neDistrictID" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item upazila" placeholder="Type to select upazila..." value="" type="text"> 
										<input type="hidden" value="<%=upazilaCategoryId%>" name="upazilaCategoryId" class="category-id"> 
										<input name="neUpazilaID" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item union" placeholder="Type to select union..." value="" type="text"> 
										<input type="hidden" value="<%=unionCategoryId%>" name="unionCategoryId" class="category-id"> 
										<input name="neUnionID" value="" type="hidden">
									</div>
								</div>
								
								<%if(loginDTO.getIsAdmin()){ %>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item load-ports" placeholder="Type to select pop..." value="" type="text">
										<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id"> 
										<input type="hidden" class="form-control" name="nePopID">
									</div>
								</div>
								<div class="form-group ">
									<label for="lnName" class="col-sm-3 control-label">Port	Type</label>
									<div class="col-sm-8">
										<select id="nePortType" name="nePortType" class="ne-hide form-control" data-port-parent="<%=routerCategoryId%>">
											<%	
											for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
												<option value="<%=portTypeInventoryAttributeNameType%>"><%=portTypeInventoryAttributeNameType%></option>
											<%}%>
										</select>
									</div>
								</div>
								<%} %>
								
								<div class="form-group ">
									<label for="lnName" class="col-sm-3 control-label">Office Address</label>
									<div class="col-sm-8">
										<textarea class="ne-hide form-control" rows="3" name="neAddress" placeholder="Give address..." required></textarea>
									</div>
								</div>
								
								<div class="form-group">
									<label for="neCoreType" class="col-sm-3 control-label">OFC Type</label>
									<div class="col-sm-8">
										<select class="ne-hide  form-control coreType" name="neCoreType" id="neCoreType">
											<option value="-1">-</option>
											<%for(Integer key : EndPointConstants.coreTypeMap.keySet()){%>
												<option value="<%=key%>"><%= EndPointConstants.coreTypeMap.get(key)%></option>
											<%}%>
										</select>
									</div>
								</div>
								
								<div class="form-group">
									<label for="lnName" class="col-sm-3 control-label">Local <%=VpnLanguageConstants.LOOP_PROVIDER %></label>
									<div class="col-sm-8">
										<select class="ne-hide form-control ofcProvider " name="neOfcProvider" id="neOfcProvider">
											<option value="-1" disabled="disabled" selected="selected">-</option>
											<%for (Integer key : EndPointConstants.providerOfOFC.keySet()){%>
												<option value="<%=key%>"><%= EndPointConstants.providerOfOFC.get(key)%></option>
											<%}%>
										</select>
									</div>
								</div>
								
								<div class="form-group">
									<label for="lnName" class="col-sm-3 control-label">Terminal Device Provider</label>
									<div class="col-sm-8">
										<select class="form-control ne-hide terminal-device-provider" name="neTerminalDeviceProvider" id="neTerminalDeviceProvider">
											<option value="-1">-</option>
											<%for (Integer key : EndPointConstants.LaidOFCby.keySet()){%>
												<option value="<%=key%>"><%= EndPointConstants.terminalDeviceProvider.get(key)%></option>
											<%}%>
										</select>
									</div>
								</div>
								</div>
								
								<div id="neShiftMode3" style="display:none">
								<div class="form-group">
									<label class="col-sm-3 control-label"> <%=VpnLanguageConstants.END_NAME %></label>
									<div class="col-sm-8">
										<select id=neOfficeList name=neID class=form-control>
											<option value="-1">Select an Office</option>
										</select>
									</div>
								</div>
								</div>
								
							</div>
						</div>
						<!-- END GRID PORTLET-->
					</div>

					<div class="col-md-6 " id="farFormInputs" >
						<!-- BEGIN Portlet PORTLET-->
						<div class="portlet light" style="margin-bottom: 0px; padding-bottom: 0px;">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-level-down"></i> 
									<span class="caption-subject bold font-grey-gallery uppercase"><%=VpnLanguageConstants.REMOTE_END%></span>
								</div>
								<div id="remoteAvailabilityStatus" style="text-align:right;"><p></p></div>
							</div>
							
							<div class="portlet-body">
								
								<div class="form-group">
									<label class="col-sm-3 control-label">Shift Mode</label>
	
									<div class="col-sm-8">
										<select name=feShiftMode class="form-control shiftMode">
											<option value=0>Select a Mode</option>
											<option value=1>Do not shift</option>
											<option value=2>Shift to a new office</option>
										</select>
									</div>
								</div>
							
								<div id="feShiftMode1" style="display:none">
								<input type="hidden" name="feID" value="-1">
								</div>
								
								<div id="feShiftMode2" style="display:none">
								<div class="form-group">
									<label class="col-sm-3 control-label" for="feName"><%=VpnLanguageConstants.REMOTE_END %></label>
									<div class="col-sm-8">
										<input type="text" placeholder="Far End Name" name="feName" class=" form-control endName" id="farEnd" autocomplete=off>
										<input type="hidden" name="feID" class="form-control " id="farEndHidden">
										<input type="hidden" name="feEndPointID">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label class="col-sm-3 control-label" for="lnName"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></label>
									<div class="col-sm-8">
										<input class="fe-hide form-control category-item district" name="feDistrictStr" autocomplete=off placeholder="Type to select..." value="" type="text">
										<input type="hidden" value="<%=districtCategoryId%>" name="districtCategoryId" 	class="category-id">
										<input name="feDistrictID" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></label>
									<div class="col-sm-8">
										<input class="fe-hide form-control category-item upazila" name="feUpazilaStr" placeholder="Type to select upazila..." value="" type="text"> 
										<input type="hidden" value="<%=upazilaCategoryId%>" name="upazilaCategoryId" class="category-id"> 
										<input name="feUpazilaID" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></label>
									<div class="col-sm-8">
										<input class="fe-hide form-control category-item union" name="feUnionStr" placeholder="Type to select union..." value="" type="text"> 
										<input type="hidden" value="<%=unionCategoryId%>" name="unionCategoryId" class="category-id"> 
										<input name="feUnionID" value="" type="hidden">
									</div>
								</div>
								
								<%if(loginDTO.getIsAdmin()){ %>
									<div class="form-group up-down-path">
										<label class="col-sm-3 control-label" for="lnName"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></label>
										<div class="col-sm-8">
											<input class="fe-hide form-control category-item " name="fePopIdStr" placeholder="Type to select pop..." value="" type="text"> 
											<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id"> 
											<input type="hidden" class="form-control" name="fePopID">
										</div>
									</div>
									<div class="form-group ">
										<label class="col-sm-3 control-label" for="lnName"> Port Type</label>
										<div class="col-sm-8">
											<select id="fePortType" name="fePortType" class="fe-hide  form-control  " data-port-parent="<%=routerCategoryId%>">
											<%for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
												<option value="<%=portTypeInventoryAttributeNameType%>"><%=portTypeInventoryAttributeNameType%></option>
											<%}%>
											</select>
										</div>
									</div>
								<%} %>
								
								<div class="form-group">
									<label class="col-sm-3 control-label" for="lnName">Office Address</label>
									<div class="col-sm-8">
										<textarea  placeholder="Give address..." name="feAddress" rows="3" class="fe-hide form-control" required></textarea>
									</div>
								</div>
								
								<div class="form-group">
									<label for="feCoreType" class="col-sm-3 control-label">OFC Type</label>
									<div class="col-sm-8">
										<select class="fe-hide  form-control coreType" name="feCoreType" id="feCoreType">
											<option value="-1" selected="selected">-</option>
											<%for (Integer key : EndPointConstants.coreTypeMap.keySet()){%>
												<option value="<%=key%>"><%= EndPointConstants.coreTypeMap.get(key)%></option>
											<%}%>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="lnName" class="col-sm-3 control-label">Remote <%=VpnLanguageConstants.LOOP_PROVIDER %></label>
									<div class="col-sm-8">
										<select class="fe-hide  form-control ofcProvider" name="feOfcProvider" id="feOfcProvider">
											<option value="-1" disabled="disabled" selected="selected">-</option>
											<%for (Integer key : EndPointConstants.providerOfOFC.keySet()){%>
												<option value="<%=key%>"><%= EndPointConstants.providerOfOFC.get(key)%></option>
											<%}%>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="lnName" class="col-sm-3 control-label">Terminal Device Provider</label>
									<div class="col-sm-8">
										<select class="form-control fe-hide terminal-device-provider" name="feTerminalDeviceProvider" id="feTerminalDeviceProvider">
											<option value="-1" selected="selected">-</option>
											<%for (Integer key : EndPointConstants.LaidOFCby.keySet()){%>
												<option value="<%=key%>"><%= EndPointConstants.terminalDeviceProvider.get(key)%></option>
											<%}%>
										</select>
									</div>
								</div>
								</div>
								
							</div>
						</div>
						<!-- END GRID PORTLET-->
					</div>
				</div>
			</div>
			
			<div class="form-actions text-center">
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Cancel</a>
				<button class="btn btn-reset-btcl" type="reset">Reset</button>
				<button class="btn btn-submit-btcl" type="button" id=submit>Submit</button>
			</div>
		</form>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$("input[name=layerType]").click(function(){
			if($(this).val()=='1'){
				$("#lanCounterText").html('Additional VLAN');
				$("#vlanCounter").show();
			}else{
				$("#lanCounterText").html('Additional IP Address');
				$("#vlanCounter").hide();
			}
		});
		$("input[name=layerType][checked='checked']").trigger("click");
	})
</script>

<script src="${context}assets/scripts/vpn/link/vpnLinkShiftEndPointAutocomplete.js" type="text/javascript"></script>
<script src="${context}assets/scripts/vpn/link/vpnLinkShift.js" type="text/javascript"></script>
<script src="${context}assets/scripts/inventory/inventoryAutocomplete.js" type="text/javascript"></script>
<script src="${context}assets/scripts/vpn/link/link-edit-validation.js" type="text/javascript"></script>

<%if(isAnyOtherLinkWithThisNearCurrentyBeingProcessed){%>
<script>
$(document).ready(function(){
	$("#nearFormInputs").find("input, select, textarea").prop("disabled", "true");
	$("#nearFormInputs .form-group:first").prepend("<div style='color:red;margin-bottom:10px;text-align:right' class='col-sm-11'>Information about this near end has already been verified. </br> It cannot be modified now.</div>");

	$("input[name=feName]").keyup(function(){
		$("input[name=feID]").val("");
		$("input[name=feEndPointID]").val("");
	});
});
</script>
<%}%>

<script>
$(document).ready(ajaxSubmit);
</script>