<%@page import="inventory.InventoryAttributeName"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="lli.link.LliEndPointDetailsDTO"%>
<%@page import="lli.link.LinkUtils"%>
<%@page import="lli.link.LliLinkDTO"%>
<%@page import="language.LliLanguageConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="java.util.List"%>
<%@page import="common.ModuleConstants"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="util.SOP"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryItemDetails"%>
<%@page import="inventory.InventoryItem"%>
<%@page import="file.FileTypeConstants"%>
<%@page import="java.util.HashMap, java.util.Map"%>
<%@page import="lli.constants.EndPointConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="inventory.InventoryService"%>
<%@page import="lli.link.LliLinkService"%>
<%@page import="common.CategoryConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="login.LoginDTO"%>

<%
    Logger logger = Logger.getLogger(this.getClass());
	int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
	int upazilaCategoryId = CategoryConstants.CATEGORY_ID_UPAZILA;
	int unionCategoryId = CategoryConstants.CATEGORY_ID_UNION;
	int popCategoryId = CategoryConstants.CATEGORY_ID_POP;
	int routerCategoryId = CategoryConstants.CATEGORY_ID_ROUTER;
	int portCategoryId = CategoryConstants.CATEGORY_ID_PORT;
	
	LliLinkService lliLinkService = new LliLinkService();
	InventoryService inventoryService= new InventoryService();

	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	
	//d make Port Type dynamic
	List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(CategoryConstants.CATEGORY_ID_PORT);
	InventoryAttributeName portTypeInventoryAttributeName = new InventoryAttributeName();
	for(InventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
		if(inventoryAttributeName.getName().equals("Port Type")){
			portTypeInventoryAttributeName = inventoryAttributeName;
			break;
		}
	}
	
try{
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i>
		New LLI Connection Form</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form id="fileupload" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../LliLinkAction.do?mode=add">
			<div class="form-body">
				<%
					if (loginDTO.getIsAdmin()) {
				%>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">
						Client Name
						<span class="required" aria-required="true"> * </span>
					</label>
					<div class="col-sm-6">
				     	<input id="clientIdStr" type="text" class="form-control" name="clientIdStr" >
					 	<input id="clientId" type="hidden" class="form-control" name="clientID" >
					</div>
				</div>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Service Purpose<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<div class="radio-list">
							<label class="radio-inline"> <span>
								<input type="radio" name="servicePurpose" value="0" checked></span>  NO
							</label> 
							<label class="radio-inline"> <span>
								<input type="radio" name="servicePurpose" value="1"></span> YES 
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Is Migrated<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<div class="radio-list">
							<label class="radio-inline"> <span>
								<input type="radio" name="isMigrated" value="0" checked></span>  NO
							</label> 
							<label class="radio-inline"> <span>
								<input type="radio" name="isMigrated" value="1"></span> YES 
							</label>
						</div>
					</div>
				</div>
				<% }else{ %>
					 <input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID() %>" >		
				<%}%>
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Bandwidth<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-4">
						<input type="number"  min="1" max="9999" maxlength="4" class="form-control" name="linkBandwidth" id="linkBandwidth"  required>
					</div>
					<div class="col-sm-4">
						<div class="radio-list">
							<label class="radio-inline"> 
								<span> <input type="radio" name="linkBandwidthType" value="<%=EntityTypeConstant.BANDWIDTH_TYPE_MB %>" checked=""> </span>
								<%=EntityTypeConstant.linkBandwidthTypeMap.get(EntityTypeConstant.BANDWIDTH_TYPE_MB) %>
							</label>
							<label class="radio-inline"> 
								<span> <input type="radio" name="linkBandwidthType" value="<%=EntityTypeConstant.BANDWIDTH_TYPE_GB %>" ></span>
								<%=EntityTypeConstant.linkBandwidthTypeMap.get(EntityTypeConstant.BANDWIDTH_TYPE_GB) %>
							</label>
						</div>
					</div>
				</div>		
						
				<div class="form-group" style="display:none">
					<label for="lnName" class="col-sm-3 control-label">LLI 	Layer Type<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<div class="radio-list">
							<label class="radio-inline"> <span>
								<input type="radio" name="layerType" value="1"  checked></span> <%=EndPointConstants.layerTypeMap.get(1) %>
							</label> 
							<label class="radio-inline"> <span>
								<input type="radio" name="layerType" value="2" ></span>  <%=EndPointConstants.layerTypeMap.get(2) %>
							</label>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Connection Type<span class="required" aria-required="true"> * </span></label>

					<div class="col-sm-8">
						<div class="radio-list">
							<%
							 int i=0;
							 String checkedStr;
							 for (Integer key : EndPointConstants.connectionType.keySet()) {
								 if(i==0){
									 checkedStr="checked";
								 }else{
									 checkedStr="";
								 }
							 %>
							 	<label class="radio-inline">
							 	 	<input type="radio" name="connectionType" value="<%=key %>" <%=checkedStr %>><%=EndPointConstants.connectionType.get(key) %>
							 	</label>
							<% 
							i++;
							 }
							%>
						</div>
					</div>
				</div>
				
				<div class="form-group" style="display:none" id="temporaryDateRange">
					<label class="col-sm-3 control-label">Temporary Connection<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-1 control-label">From</div>
					<div class="col-sm-2"><input type="text" class="datepicker form-control" name="temporaryConnectionFromDate"/></div>
					<div class="col-sm-2 control-label">For (days)</div>
					<div class="col-sm-1"><input type="number" class="form-control" max=31 name="temporaryConnectionRange"/></div>
				</div>
				
				<div class="form-group" style="display:none" id="fiveYearBandwidthBlock">
					<label for="lnName" class="col-sm-3 control-label">Five Year Bandwidth<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-4">
						<input type="number" step="1" min="0" max="9999" maxlength="4" class="form-control" value="0" name="fiveYearBandwidth" id="fiveYearBandwidth"  required />
					</div>
					<div class="col-sm-4">
						<div class="radio-list">
							<label class="radio-inline"> 
								<span> <input type="radio" name="fiveYearBandwidthType" value="<%=EntityTypeConstant.BANDWIDTH_TYPE_MB %>" checked=""> </span>
								<%=EntityTypeConstant.linkBandwidthTypeMap.get(EntityTypeConstant.BANDWIDTH_TYPE_MB) %>
							</label>
							<label class="radio-inline"> 
								<span> <input type="radio" name="fiveYearBandwidthType" value="<%=EntityTypeConstant.BANDWIDTH_TYPE_GB %>" ></span>
								<%=EntityTypeConstant.linkBandwidthTypeMap.get(EntityTypeConstant.BANDWIDTH_TYPE_GB) %>
							</label>
						</div>
					</div>
				</div>	
				
				<div class="form-group">
					<label for="description" class="col-sm-3 control-label">Comment</label>
					<div class="col-sm-6">
						<textarea class="form-control" rows="3" name="linkDescription"
							placeholder="Describe something about the link"></textarea>
					</div>
				</div>
			
			
				<!-- Remote End Begins -->
				<div id="farFormInputs">
				<div class="form-group">
					<div class="portlet light" style="margin-bottom: 0px; padding-bottom: 0px;">
						<div class="portlet-title">
							<div class="col-md-offset-1 caption">
								<i class="fa fa-level-down"></i> 
								<span class="caption-subject bold font-grey-gallery uppercase"> <%=LliLanguageConstants.REMOTE_END %> </span>
							</div>

						</div>
					</div>
				</div>
			
				<div class="form-group">
				
					<label class="col-sm-3 control-label" for="feName">
						<%=LliLanguageConstants.END_NAME %>
						<span class="required" aria-required="true"> * </span>
					</label>
					
					<div class="col-sm-6">
						<input type="text" placeholder="Connection Name" name="feName" class="fe-hide form-control endName" id="farEnd" required autocomplete="off">
						<input type="hidden" name="feID" class="form-control " id="farEndHidden">
					</div>
					<div class="col-sm-3 unavailable" id="officeNameAvailability">
					</div>
					
				</div>
				<div class="form-group up-down-path">
					<label class="col-sm-3 control-label" for="lnName"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %><span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<input class="fe-hide form-control category-item district" name="feDistrictStr" placeholder="Type to select..." value="" type="text">
						<input type="hidden" value="<%=districtCategoryId%>" name="districtCategoryId" 	class="category-id">
						<input name="feDistrictID" value="" class="item-id" type="hidden">
					</div>
				</div>
				<div class="form-group up-down-path">
					<label for="lnName" class="col-sm-3 control-label">
						<%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %>
						<span class="required" aria-required="true"> * </span>
					</label>
					<div class="col-sm-6">
						<input class="fe-hide form-control category-item upazila" name="feUpazilaStr" placeholder="Type to select upazila..." value="" type="text"> 
						<input type="hidden" value="<%=upazilaCategoryId%>" name="upazilaCategoryId" class="category-id"> 
						<input name="feUpazilaID" value="" type="hidden" class="item-id">
					</div>
				</div>
				<div class="form-group up-down-path">
					<label for="lnName" class="col-sm-3 control-label">
						<%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %>
						<span class="required" aria-required="true"> * </span>
					</label>
					<div class="col-sm-6">
						<input class="fe-hide form-control category-item union" name="feUnionStr" placeholder="Type to select union..." value="" type="text"> 
						<input type="hidden" value="<%=unionCategoryId%>" name="unionCategoryId" class="category-id"> 
						<input name="feUnionID" value="" class="item-id" type="hidden">
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-3 control-label" for="lnName">Connection Address<span class="required" aria-required="true"> * </span></label>

					<div class="col-sm-6">
						<textarea  placeholder="Give address..." name="feAddress"
							rows="3" class="fe-hide form-control" required></textarea>
					</div>
				</div>
				<%if(loginDTO.getIsAdmin()){ %>
					<div class="form-group ">
						<label class="col-sm-3 control-label" for="lnName">
							<%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %>
							<span class="required" aria-required="true"> * </span>
						</label>

						<div class="col-sm-6">
							<input class="fe-hide form-control category-item" name="fePopIdStr" placeholder="Type to select pop..." value="" type="text"> 
							<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id" required> 
							<input type="hidden" class="form-control item-id " name="fePopID">
						</div>
					</div>
					<div class="form-group up-down-path">
						<label for="lnName" class="col-sm-3 control-label">
							Port Type<span class="required" aria-required="true"> * </span>
						</label>
						<div class="col-sm-6">
							<select id="fePortType" name=" fePortType" class="fe-hide form-control" data-port-parent="<%=routerCategoryId%>" required>
								<option value="-1" disabled="disabled" selected="selected">---Select---</option>
								<%	
								for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
									<option value="<%=portTypeInventoryAttributeNameType%>"><%=portTypeInventoryAttributeNameType%></option>
								<%}%>
							</select>
						</div>
					</div>
				<%} %>
				<div class="form-group">
					<label for="feCoreType" class="col-sm-3 control-label"> OFC Type <span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<select class="fe-hide  form-control coreType" name="feCoreType" id="feCoreType" required>
							<option value="-1" disabled="disabled" selected="selected">---Select---</option>
							<%for(Integer key : EndPointConstants.coreTypeMap.keySet()) { %>
							 	<option value="<%=key%>"><%= EndPointConstants.coreTypeMap.get(key)%></option>
							<%}%>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Local  <%=LliLanguageConstants.LOOP_PROVIDER %> <span class="required" aria-required="true"> * </span></label>

					<div class="col-sm-6">
						<select class="fe-hide  form-control ofcProvider" name="feOfcProvider" id="feOfcProvider" required>
							<option value="-1" disabled="disabled" selected="selected">---Select---</option>
							<%
							 for (Integer key : EndPointConstants.providerOfOFC.keySet()) {
							 %>
								 	<option value="<%=key%>"><%= EndPointConstants.providerOfOFC.get(key)%></option>
							<% 
								}
							%>
						</select>
					</div>
				</div>

				<!-- <input type="hidden" name="feProviderServiceType" value="1"> -->
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Terminal Device Provider <span class="required" aria-required="true"> * </span></label>

					<div class="col-sm-6">
						<select class="form-control fe-hide " name="feTerminalDeviceProvider" id="feTerminalDeviceProvider" required>
							<option value="-1" disabled="disabled" selected="selected">---Select---</option>
							<%
							 for (Integer key : EndPointConstants.LaidOFCby.keySet()) {
							 %>
								 	<option value="<%=key%>"><%= EndPointConstants.terminalDeviceProvider.get(key)%></option>
							<% 
								}
							%>
						</select>
					</div>
				</div>
				
				
				</div>
				<!-- Remote End Ends -->
				<h6><b>Valid File Extension: jpg/png/pdf</b></h6>
				<div class="col-md-12"><hr></div>
				<div class="form-group">
					<div class="col-md-12">
					 	<div class="col-md-3" style="padding: 0px;">
							<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
							<span class="btn btn-warning-btcl  fileinput-button">
								<i class="fa fa-upload"></i>
								<span> Add <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.LLI_LINK_ADD.LLI_LINK_GENERAL_DOCUMENT)%> </span>
								<input class="jFile" id="nid" type="file" name="<%=FileTypeConstants.LLI_LINK_ADD.LLI_LINK_GENERAL_DOCUMENT %>" > 
							</span>
						</div>
						<div class="col-md-9">
							 <!-- The global file processing state -->
							 <span class="fileupload-process"></span>
					   		  <!-- The global progress state -->
					          <div class="col-lg-12 fileupload-progress fade">
					              <!-- The global progress bar -->
					              <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
					                  <div class="progress-bar progress-bar-success" style="width:0%;"></div>
					              </div>
					              <!-- The extended global progress state -->
					              <div class="progress-extended">&nbsp;</div>
					          </div>
						</div>
						<!-- The table listing the files available for upload/download -->
						<table role="presentation" class="table table-striped margin-top-10">
							<tbody class="files"></tbody>
						</table>
					</div>
			 		<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
			 	</div>
			</div>
			<div class="form-actions text-center">
				<!--<input type="hidden" name="parentItemID" class="parentItemID"> -->
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
				<input type="hidden" name="linkConnectionID" id="linkConnectionID" />
				<button class="btn btn-reset-btcl" type="reset">Reset</button>
				<button id='btn-link-add' class="btn btn-submit-btcl" name="action" value="goView" type="submit">Add</button>
			</div>
		</form>
	</div>
</div>
<%
} catch (Exception ex) {
	logger.debug("General Error " + ex);
}
%>

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
		})
	})
</script>

<script>
	function responseCallback(data){
		if(data==true){
			$("#officeNameAvailability").html("<p style='color:green'>Available</p>");
			$("#officeNameAvailability").removeClass("unavailable");
		}else{
			$("#officeNameAvailability").html("<p style='color:red'>Not Available</p>");
			$("#officeNameAvailability").addClass("unavailable");
		}
	}
	$(document).ready(function(){
		$("input[name=feName]").keyup(function(){
			if($(this).val().length>0){
				data = {};
				data.officeName = $(this).val();
				callAjax(context+"LliAutocomplete.do?officeName="+$(this).val(), data, responseCallback, "GET");
			}else{
				$("#officeNameAvailability").html("<p></p>");
			}
			
		})
	})
</script>

<script>
	var temporaryDateRange = "<%=EndPointConstants.CONNECTION_TYPE_TEMPORARY_%>";
	var fiveYearConnection = "<%=EndPointConstants.CONNECTION_TYPE_FIVE_YEARS_ %>";
	var bwMB = "<%=EntityTypeConstant.BANDWIDTH_TYPE_MB%>";
	var bwGB = "<%=EntityTypeConstant.BANDWIDTH_TYPE_GB%>";
	
</script>
<script src="${context}assets/scripts/lli/link/lliLink.js" type="text/javascript"></script>

<script src="${context}/assets/scripts/lli/link/link-add-validation.js" type="text/javascript"></script>
