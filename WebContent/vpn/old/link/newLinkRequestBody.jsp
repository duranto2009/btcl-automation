<%@page import="config.GlobalConfigConstants"%>
<%@page import="config.GlobalConfigurationRepository"%>
<%@page import="inventory.InventoryAttributeValue"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnLinkDTO"%>
<%@page import="language.VpnLanguageConstants"%>
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
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="inventory.InventoryService"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="vpn.link.VpnNearEndDTO"%>
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
	
	VpnLinkService vpnLinkService = new VpnLinkService();
	InventoryService inventoryService= new InventoryService();

	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	VpnNearEndDTO nearEndDTO = null;
	VpnEndPointDetailsDTO nearEndDetailsDTO=null;
	
	Long nearEndID = (Long) request.getSession(true).getAttribute("oldNearEndID");
	if (nearEndID == null) {
		nearEndID = new Long(0);//20001
	} else {
		request.getSession(true).removeAttribute("oldNearEndID");
		request.getSession(true).removeAttribute("vpnLinkID");
		request.getSession(true).removeAttribute("addmore");
		logger.debug(nearEndDTO);
		
		nearEndDTO = vpnLinkService.getNearEndByNearEndID(nearEndID);
		nearEndID=nearEndDTO.getID();
		nearEndDetailsDTO=LinkUtils.getEndPointDTODetails(nearEndDTO);
	
	}
	logger.debug(nearEndID+" nearEndID" +" link id: "+ (Long) request.getSession(true).getAttribute("vpnLinkID"));
	logger.debug("addmore :"+ request.getSession(true).getAttribute("addmore"));
	
	
	
	//d make Port Type dynamic
	List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(CategoryConstants.CATEGORY_ID_PORT);
	InventoryAttributeName portTypeInventoryAttributeName = new InventoryAttributeName();;
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
		New Link Form</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form id="fileupload" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../VpnLinkAction.do?mode=add">
			<div class="form-body">
				<%
					if (loginDTO.getIsAdmin()) {
				%>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Client
						Name<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<%if(nearEndDTO==null){ %>
					     	<input id="clientIdStr" type="text" class="form-control" name="clientIdStr" >
						 	<input id="clientId" type="hidden" class="form-control" name="clientID" >
						 <%}else{ %>
						 	<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=nearEndDTO.getClientID()%>">
						 	<input id="clientIdStr" type="text" class="form-control" name="clientIdStr"   
						 	value="<%=AllClientRepository.getInstance().getClientByClientID(nearEndDTO.getClientID()).getLoginName() %>"  disabled="disabled">
						 <%} %>
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
			
				<%if(GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MIGRATION_ENABLED).getValue() == 1){%>
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
				<%}%>
				<% }else{ %>
					 <input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID() %>" >		
				<%}%>
				
				
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Bandwidth<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-4">
						<input type="number"  min="1" max="9999" maxlength="4" class="form-control" name="linkBandwidth"  required>
					</div>
					<div class="col-sm-4">
						<div class="radio-list">
							<label class="radio-inline"> 
								<span> <input type="radio" name="linkBandwidthType" value="<%=EntityTypeConstant.BANDWIDTH_TYPE_MB %>" checked> </span>
								<%=EntityTypeConstant.linkBandwidthTypeMap.get(EntityTypeConstant.BANDWIDTH_TYPE_MB) %>
							</label>
							<label class="radio-inline"> 
								<span> <input type="radio" name="linkBandwidthType" value="<%=EntityTypeConstant.BANDWIDTH_TYPE_GB %>" ></span>
								<%=EntityTypeConstant.linkBandwidthTypeMap.get(EntityTypeConstant.BANDWIDTH_TYPE_GB) %>
							</label>
						</div>
					</div>
				</div>		
						
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">VPN 	Layer Type<span class="required" aria-required="true"> * </span></label>
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
				
				<%--
				<div class="form-group" id="vlanCounter">
					<label for="lnName" class="col-sm-3 control-label"><span id="lanCounterText">Additional VLan </span> Count<span class="required" aria-required="true"> * </span></label>
					<div class="col-sm-6">
						<input type="number"  min="0" max="9999" maxlength="4" class="form-control" name="lanCounter" required>
					</div>
				</div>
				--%>
				
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
				<div class="form-group">
					<label for="description" class="col-sm-3 control-label">Comment</label>
					<div class="col-sm-6">
						<textarea class="form-control" rows="3" name="linkDescription"
							placeholder="Describe something about the link"></textarea>
					</div>
				</div>
			
				<div class="form-group">
					<div class="col-md-6 " id="nearFormInputs" >
						<!-- BEGIN Portlet PORTLET-->
						<div class="portlet light" style="margin-bottom: 0px; padding-bottom: 0px;">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-level-up"></i> <span
										class="caption-subject bold font-grey-gallery uppercase"> <%=VpnLanguageConstants.LOCAL_END %> </span>
								</div>
							</div>
						
							<div class="portlet-body">
								<div class="form-group">
									<label class="col-sm-4 control-label" for="neName"><%=VpnLanguageConstants.END_NAME %><span class="required" aria-required="true"> * </span></label>
	
									<div class="col-sm-8">
										<input type="text" placeholder="Near End Name" name="neName" class="form-control endName" id="nearEnd">
										<input type="hidden" name="neID" class="form-control " value="<%=nearEndID%>" id="nearEndHidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-4 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %><span class="required" aria-required="true"> * </span></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item district" name="neDistrictStr" placeholder="Type to select district..." value="" type="text"> 
										<input type="hidden" value="<%=districtCategoryId%>" name="districtCategoryId" class="category-id"> 
										<input name="neDistrictID" class="item-id" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-4 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %><span class="required" aria-required="true"> * </span></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item upazila" name="neUpazilaStr" placeholder="Type to select upazila..." value="" type="text"> 
										<input type="hidden" value="<%=upazilaCategoryId%>" name="upazilaCategoryId" class="category-id"> 
										<input name="neUpazilaID" value="" class="item-id" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-4 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item union" name="neUnionStr" placeholder="Type to select union..." value="" type="text"> 
										<input type="hidden" value="<%=unionCategoryId%>" name="upazilaCategoryId" class="category-id"> 
										<input name="neUnionID" value="" class='item-id' type="hidden">
									</div>
								</div>
								<%if(loginDTO.getIsAdmin()){ %>
								<div class="form-group up-down-path1">
									<label for="lnName" class="col-sm-4 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item " name="nePopIdStr" placeholder="Type to select pop..." value="" type="text">
										<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id"> 
										<input type="hidden" class="form-control" name="nePopID">
									</div>
								</div>
								
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-4 control-label">Port Type</label>
									<div class="col-sm-8">
										<select id="nePortType" name=" nePortType" class="ne-hide form-control" data-port-parent="<%=routerCategoryId%>">
											<option value="-1" disabled="disabled" selected="selected">---Select---</option>
											<%	
											for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
												<option value="<%=portTypeInventoryAttributeNameType%>"><%=portTypeInventoryAttributeNameType%></option>
											<%}%>
										</select>
									</div>
								</div>
								<%} %>
								<div class="form-group ">
									<label for="lnName" class="col-sm-4 control-label">Office Address<span class="required" aria-required="true"> * </span></label>

									<div class="col-sm-8">
										<textarea class="ne-hide form-control office-address" rows="3" name="neAddress"	placeholder="Give address..." required></textarea>
									</div>
								</div>
								
								<div class="form-group">
									<label for="neCoreType" class="col-sm-4 control-label"> OFC Type<span class="required" aria-required="true"> * </span></label>
									<div class="col-sm-8">
										<select class="ne-hide  form-control coreType" name="neCoreType" id="neCoreType">
											<option value="-1" disabled="disabled" selected="selected">---Select---</option>
											<%
											 for (Integer key : EndPointConstants.coreTypeMap.keySet()) {
											 %>
												 	<option value="<%=key%>"><%= EndPointConstants.coreTypeMap.get(key)%></option>
											<%}%>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="lnName" class="col-sm-4 control-label">Local <%=VpnLanguageConstants.LOOP_PROVIDER %><span class="required" aria-required="true"> * </span></label>

									<div class="col-sm-8">
										<select class="ne-hide form-control ofcProvider " name="neOfcProvider" id="neOfcProvider">
											<option value="-1" disabled="disabled" selected="selected">---Select---</option>
											<%
											 for (Integer key : EndPointConstants.providerOfOFC.keySet()) {
											 %>
												 	<option value="<%=key%>"><%= EndPointConstants.providerOfOFC.get(key)%></option>
											<% } %>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="lnName" class="col-sm-4 control-label">Terminal Device Provider<span class="required" aria-required="true"> * </span></label>

									<div class="col-sm-8">
										<select class="form-control ne-hide terminal-device-provider" name="neTerminalDeviceProvider" id="neTerminalDeviceProvider">
											<option value="-1" disabled="disabled" selected="selected">---Select---</option>
											<%
											 for (Integer key : EndPointConstants.LaidOFCby.keySet()) {
											 %>
												 	<option value="<%=key%>"><%= EndPointConstants.terminalDeviceProvider.get(key)%></option>
											<% } %>
										</select>
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
									<span class="caption-subject bold font-grey-gallery uppercase"> <%=VpnLanguageConstants.REMOTE_END %> </span>
								</div>
								<div id="remoteAvailabilityStatus" style="text-align:right;"><p></p></div>
							</div>
							<div class="portlet-body">
								
								<div class="form-group">
									<label class="col-sm-4 control-label" for="feName"><%=VpnLanguageConstants.END_NAME %><span class="required" aria-required="true"> * </span></label>
									<div class="col-sm-8">
										<input type="text" placeholder="Far End Name" name="feName" class="fe-hide form-control endName" id="farEnd">
										<input type="hidden" name="feID" class="form-control " id="farEndHidden">
									</div>
									<div id="officeNameAvailability" style="display:none"></div>
								</div>
								<div class="form-group up-down-path">
									<label class="col-sm-4 control-label" for="lnName"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %><span class="required" aria-required="true"> * </span></label>
									<div class="col-sm-8">
										<input class="fe-hide form-control category-item district" name="feDistrictStr" placeholder="Type to select..." value="" type="text">
										<input type="hidden" value="<%=districtCategoryId%>" name="districtCategoryId" 	class="category-id">
										<input name="feDistrictID" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-4 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %><span class="required" aria-required="true"> * </span></label>
									<div class="col-sm-8">
										<input class="fe-hide form-control category-item upazila" name="feUpazilaStr" placeholder="Type to select upazila..." value="" type="text"> 
										<input type="hidden" value="<%=upazilaCategoryId%>" name="upazilaCategoryId" class="category-id"> 
										<input name="feUpazilaID" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-4 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></label>
									<div class="col-sm-8">
										<input class="fe-hide form-control category-item union" name="feUnionStr" placeholder="Type to select union..." value="" type="text"> 
										<input type="hidden" value="<%=unionCategoryId%>" name="unionCategoryId" class="category-id"> 
										<input name="feUnionID" value="" type="hidden">
									</div>
								</div>
								
							
								<%if(loginDTO.getIsAdmin()){ %>
									<div class="form-group  up-down-path1">
										<label class="col-sm-4 control-label" for="lnName"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></label>
	
										<div class="col-sm-8">
											<input class="fe-hide form-control category-item" name="fePopIdStr" placeholder="Type to select pop..." value="" type="text"> 
											<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id"> 
											<input type="hidden" class="form-control" name="fePopID">
										</div>
									</div>
									<div class="form-group up-down-path">
										<label class="col-sm-4 control-label" for="lnName"> Port Type</label>
	
										<div class="col-sm-8">
											<select id="fePortType" name="fePortType" class="fe-hide  form-control  " data-port-parent="<%=routerCategoryId%>">
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
									<label class="col-sm-4 control-label" for="lnName">Office Address<span class="required" aria-required="true"> * </span></label>

									<div class="col-sm-8">
										<textarea  placeholder="Give address..." name="feAddress"
											rows="3" class="fe-hide form-control" required></textarea>
									</div>
								</div>
								<div class="form-group">
									<label for="feCoreType" class="col-sm-4 control-label"> OFC Type<span class="required" aria-required="true"> * </span></label>
									<div class="col-sm-8">
										<select class="fe-hide  form-control coreType" name="feCoreType" id="feCoreType">
											<option value="-1" disabled="disabled" selected="selected">---Select---</option>
											<%for(Integer key : EndPointConstants.coreTypeMap.keySet()) { %>
											 	<option value="<%=key%>"><%= EndPointConstants.coreTypeMap.get(key)%></option>
											<%}%>
										</select>
										<input type='hidden' name='feCoreType'>
									</div>
								</div>
								<div class="form-group">
									<label for="lnName" class="col-sm-4 control-label">Local  <%=VpnLanguageConstants.LOOP_PROVIDER %><span class="required" aria-required="true"> * </span></label>

									<div class="col-sm-8">
										<select class="fe-hide  form-control ofcProvider" name="feOfcProvider" id="feOfcProvider">
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
								<div class="form-group">
									<label for="lnName" class="col-sm-4 control-label">Terminal Device Provider<span class="required" aria-required="true"> * </span></label>

									<div class="col-sm-8">
										<select class="form-control fe-hide terminal-device-provider" name="feTerminalDeviceProvider" id="feTerminalDeviceProvider">
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
						</div>
						<!-- END GRID PORTLET-->
					</div>
				</div>
				<div class="col-md-12"><hr></div>
				<div class="form-group">
					<div class="col-md-12">
					 	<div class="col-md-3" style="padding: 0px;">
							<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
							<span class="btn btn-warning-btcl  fileinput-button">
								<i class="fa fa-upload"></i>
								<span> Add <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.VPN_LINK_ADD.VPN_LINK_ADD_GENERAL_DOCUMENT)%> </span>
								<input class="jFile" id="nid" type="file" name="<%=FileTypeConstants.VPN_LINK_ADD.VPN_LINK_ADD_GENERAL_DOCUMENT %>" > 
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
			 		<jsp:include page="../../../common/ajaxfileUploadTemplate.jsp" />
			 	</div>
			</div>
			<div class="form-actions text-center">
				<!--<input type="hidden" name="parentItemID" class="parentItemID"> -->
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_VPN %>" />
				<input type="hidden" name="linkConnectionID" id="linkConnectionID" />
				<button class="btn btn-reset-btcl" type="reset">Reset</button>
				<button id="btn-link-add" class="btn btn-submit-btcl" name="action" value="goView" type="submit">Add</button>
				<button class="btn btn-submit2-btcl" name="action" value="addMore" 	type="submit">Add Another Connection</button>
			</div>
		</form>
	</div>
</div>
<%
	if(nearEndDTO != null){
%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#nearEndHidden").val(<%=nearEndDTO.getID() %>);
			$("#nearEnd").val('<%=nearEndDTO.getName() %>');
			$("#linkConnectionID").val(<%=nearEndDTO.getVpnConnectionID() %>);
		 	$('input[name=neDistrictID]').val('<%=nearEndDTO.getDistrictID()%>');
            $("input[name=neDistrictStr]").val('<%=nearEndDetailsDTO.getDistrictName()%>');
            $("input[name=neUpazilaID]").val('<%=nearEndDetailsDTO.getUpazilaId()%>');
            $("input[name=neUpazilaStr]").val('<%=nearEndDetailsDTO.getUpazilaName()%>');
            $("input[name=neUnionID]").val('<%=nearEndDetailsDTO.getUnionId()%>');
            $("input[name=neUnionStr]").val('<%=nearEndDetailsDTO.getUnionName()%>');
            $("input[name=nePopID]").val('<%=nearEndDetailsDTO.getPopID()%>');
            $("input[name=nePopIdStr]").val('<%=nearEndDetailsDTO.getPopName()%>');
			$("textarea[name=neAddress]").val('<%=nearEndDTO.getAddress() %>');
			$("#nePortType").val('<%=nearEndDTO.getPortType() %>');
			$("#neCoreType").val('<%=nearEndDTO.getCoreType() %>');
			$("#feCoreType").val('<%=nearEndDTO.getCoreType() %>');
            $("#feCoreType").attr("disabled", true);
            $('input[type=hidden][name=feCoreType]').val($('#neCoreType').val());
			$("#neOfcProvider").val('<%=nearEndDTO.getOfcProviderTypeID() %>');
			$("#neTerminalDeviceProvider").val(<%=nearEndDTO.getTerminalDeviceProvider() %>);
			
			$("#nearFormInputs").find(".ne-hide").attr("readonly", true);
			$("#nearEndHidden").attr("disabled", false);
		});
	</script>
<%}
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
			$("#remoteAvailabilityStatus").css("color", "green");
			$("#remoteAvailabilityStatus").find("p").html("Available");
		}else{
			$("#remoteAvailabilityStatus").css("color", "red");
			$("#remoteAvailabilityStatus").find("p").html("Not Available");
		}
	}
	$(document).ready(function(){
		$("input[name=feName]").keyup(function(){
			$("#remoteAvailabilityStatus").find("p").html("");
			if($(this).val().trim().length>0){
				data = {};
				data.clientID = $("#clientId").val();
				data.officeName = $(this).val();
				data.mode="officeNameAvailability";
				if(data.clientID > 0){
					$(this).addClass("ui-autocomplete-loading");
					ajax(context+"VpnAjax.do?", data, responseCallback, "GET", [$("#farEnd")]);
				}else{
					toastr.error("Please Select a Client");
				}
			}else{
				$(this).val($(this).val().trim());
				$("#officeNameAvailability").html("<p></p>");
				$("#remoteAvailabilityStatus").find("p").html("");
			}
			
		});
		$('#neCoreType').on('change', function(){
			
			$('#feCoreType').val($('#neCoreType').val());
			$('input[type=hidden][name=feCoreType]').val($('#neCoreType').val());
			$('#feCoreType').attr('disabled', true);
		});
	})
</script>

<script src="${context}assets/scripts/vpn/link/vpnLink.js" type="text/javascript"></script>
<script src="${context}/assets/scripts/vpn/link/link-add-validation.js" type="text/javascript"></script>
