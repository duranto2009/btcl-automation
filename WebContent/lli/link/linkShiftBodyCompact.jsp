<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.List"%>
<%@page import="lli.constants.LliRequestTypeConstants"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="language.LliLanguageConstants"%>
<%@page import="lli.link.LliFarEndDTO"%>
<%@page import="lli.link.LinkUtils"%>
<%@page import="lli.link.LliEndPointDetailsDTO"%>
<%@page import="inventory.InventoryService"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="login.LoginDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.CategoryConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="lli.constants.EndPointConstants"%>
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
		<div class="caption"><i class="fa fa-link"></i>Shift Connection</div>

	</div>
	<div class="portlet-body form">
	
		<form  id="fileupload" class="form-horizontal" method="post" action="<%=request.getContextPath()%>/LLI/Link/Shift/Add.do">

		 	<div class="form-body">
			 	<% if (!loginDTO.getIsAdmin()) { %>
				<input id="clientID" type="hidden" class="form-control" value="<%=loginDTO.getAccountID() %>" >		
				<%}else{%>
				<div class="form-group">
					<label for="clientIDName" class="col-sm-3 control-label">Select Client</label>
					<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
						<input id="clientIDName" type="text" class="form-control">
						<input id="clientID" type="hidden" class="form-control" value="" >
					</div>
					<div class="col-xxs-12 col-xs-12 col-sm-2 col-md-2 col-lg-3">			
						<a id="clientHyperLink" target="_blank" href="<%=request.getContextPath() %>/GetClientForView.do?moduleID=6&entityID=" required> View Client Details </a>
					</div>
				</div>
				<%}%>
				
				<div class="form-group">
					<label for="lliLinkIDName" class="col-sm-3 control-label">Select Connection</label>
					<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
						<input id="lliLinkIDName" type="text" class="form-control">
						<input id="lliLinkID" type="hidden" class="form-control" name="lliLinkID" value="" >
					</div>
					<div class="col-xxs-12 col-xs-12 col-sm-2 col-md-2 col-lg-3">
						<a id="linkHyperlink" target="_blank" href="<%=request.getContextPath() %>/LliLinkAction.do?&entityTypeID=<%=EntityTypeConstant.LLI_LINK %>&getMode=details&entityID=">View Connection Details</a>
					</div>
				</div>
				
				
				
				<!-- End Points -->
				<div class="form-group">
					

					<div id="farFormInputs" >
						<!-- BEGIN Portlet PORTLET-->
						<div class="portlet light" style="margin-bottom: 0px; padding-bottom: 0px;">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-level-down"></i> 
									<span class="caption-subject bold font-grey-gallery uppercase"><%=LliLanguageConstants.REMOTE_END%></span>
								</div>
								<div id="remoteAvailabilityStatus" style="text-align:right;"><p></p></div>
							</div>
							
							<div class="portlet-body">
							
								<div id="feShiftMode1" style="display:none">
									<input type="hidden" name="feID" value="-1">
								</div>
								
								<div id="feShiftMode2">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="feName"><%=LliLanguageConstants.REMOTE_END %></label>
										<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
											<input type="text" placeholder="Far End Name" name="feName" class=" form-control" id="farEnd" autocomplete=off>
											<input type="hidden" name="feID" class="form-control " id="farEndHidden">
											<input type="hidden" name="feEndPointID">
										</div>
									</div>
									<div class="form-group up-down-path">
										<label class="col-sm-3 control-label" for="lnName"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></label>
										<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
											<input class="fe-hide form-control category-item district" name="feDistrictStr" autocomplete=off placeholder="Type to select..." value="" type="text">
											<input type="hidden" value="<%=districtCategoryId%>" name="districtCategoryId" 	class="category-id">
											<input name="feDistrictID" value="" type="hidden">
										</div>
									</div>
									<div class="form-group up-down-path">
										<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></label>
										<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
											<input class="fe-hide form-control category-item upazila" name="feUpazilaStr" placeholder="Type to select upazila..." value="" type="text"> 
											<input type="hidden" value="<%=upazilaCategoryId%>" name="upazilaCategoryId" class="category-id"> 
											<input name="feUpazilaID" value="" type="hidden">
										</div>
									</div>
									<div class="form-group up-down-path">
										<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></label>
										<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
											<input class="fe-hide form-control category-item union" name="feUnionStr" placeholder="Type to select union..." value="" type="text"> 
											<input type="hidden" value="<%=unionCategoryId%>" name="unionCategoryId" class="category-id"> 
											<input name="feUnionID" value="" type="hidden">
										</div>
									</div>
									
									<%if(loginDTO.getIsAdmin()){ %>
										<div class="form-group up-down-path">
											<label class="col-sm-3 control-label" for="lnName"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></label>
											<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
												<input class="fe-hide form-control category-item " name="fePopIdStr" placeholder="Type to select pop..." value="" type="text"> 
												<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id"> 
												<input type="hidden" class="form-control" name="fePopID">
											</div>
										</div>
										<div class="form-group ">
											<label class="col-sm-3 control-label" for="lnName"> Port Type</label>
											<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
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
										<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
											<textarea  placeholder="Give address..." name="feAddress" rows="3" class="fe-hide form-control" required></textarea>
										</div>
									</div>
									
									<div class="form-group">
										<label for="feCoreType" class="col-sm-3 control-label">OFC Type</label>
										<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
											<select class="fe-hide  form-control coreType" name="feCoreType" id="feCoreType">
												<option value="-1" selected="selected">-</option>
												<%for (Integer key : EndPointConstants.coreTypeMap.keySet()){%>
													<option value="<%=key%>"><%= EndPointConstants.coreTypeMap.get(key)%></option>
												<%}%>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label for="lnName" class="col-sm-3 control-label">Remote <%=LliLanguageConstants.LOOP_PROVIDER %></label>
										<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
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
										<div class="col-xxs-12 col-xs-12 col-sm-6 col-md-6 col-lg-4">
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
				<button class="btn btn-submit-btcl" type="submit">Submit</button>
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

<script src="${context}assets/scripts/lli/link/lliLinkShiftEndPointAutocomplete.js" type="text/javascript"></script>
<script src="${context}assets/scripts/lli/link/lliLinkShift.js" type="text/javascript"></script>
<script src="${context}assets/scripts/inventory/inventoryAutocomplete.js" type="text/javascript"></script>
<script src="${context}assets/scripts/lli/link/link-edit-validation.js" type="text/javascript"></script>

<script>
	$(document).ready(function(){
		
		$("#linkHyperlink").hide();
		$("#clientHyperLink").hide();
		
		$("input[name=feName]").keyup(function(){
			$("input[name=feID]").val("");
			$("input[name=feEndPointID]").val("");
		});
	});
</script>