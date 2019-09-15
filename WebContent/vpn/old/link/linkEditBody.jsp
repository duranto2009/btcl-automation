<%@page import="config.GlobalConfigConstants"%>
<%@page import="config.GlobalConfigurationRepository"%>
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
long entityID= Long.parseLong(request.getAttribute("entityID")+"");
long entityTypeID= Long.parseLong(request.getAttribute("entityTypeID")+"");
int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
int upazilaCategoryId = CategoryConstants.CATEGORY_ID_UPAZILA;
int unionCategoryId = CategoryConstants.CATEGORY_ID_UNION;
int popCategoryId = CategoryConstants.CATEGORY_ID_POP;
int routerCategoryId = CategoryConstants.CATEGORY_ID_ROUTER;
int portCategoryId = CategoryConstants.CATEGORY_ID_PORT;

LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
Long clientID=Long.parseLong(request.getAttribute("clientID")+"");
VpnLinkDTO vpnLinkDTO= (VpnLinkDTO)request.getAttribute("vpnLink");

VpnNearEndDTO nearEndDTO=(VpnNearEndDTO)request.getAttribute("vpnNE");
VpnEndPointDetailsDTO nearEndDetailsDTO=LinkUtils.getEndPointDTODetails(nearEndDTO);

VpnFarEndDTO farEndDTO=(VpnFarEndDTO)request.getAttribute("vpnFE");
VpnEndPointDetailsDTO farEndDetailsDTO=LinkUtils.getEndPointDTODetails(farEndDTO);

boolean isAnyOtherLinkWithThisNearEndAlreadyVerified = new VpnLinkService().isAnyOtherLinkWithThisNearEndAlreadyVerified(vpnLinkDTO.getNearEndID());

//d make Port Type dynamic
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
		<div class="caption"><i class="fa fa-link"></i>Link Edit Form</div>

	</div>
	<div class="portlet-body form">
		<html:form  styleId="fileupload" styleClass="form-horizontal" method="post" action="/VpnLinkAction.do?mode=edit"  enctype="multipart/form-data">
			<input type="hidden" name="entityID" value = "${vpnLink.ID}" />
			<input type="hidden" name="linkID" value = "${vpnLink.ID}" />
			<input type="hidden" name="nearEndPointID" value = "${vpnNE.vpnEndPointID}" />
			<input type="hidden" name="farEndPointID" value = "${vpnFE.vpnEndPointID}" />
			<input type="hidden" name="entityTypeID" value = "<%=entityTypeID %>" />
			<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_VPN %>" />
		 	<input type="hidden" name="vpnConnectionID" value = "${vpnLink.vpnConnectionID}" />
		 	
		 	<% if (!loginDTO.getIsAdmin()) { %>
				<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID() %>" >		
			<%}else{%>
				<input id="clientId" type="hidden" class="form-control"  name="clientID" value="${clientID}" >
			<%}%>
		 	
			<div class="form-body">
			
				<% if (loginDTO.getIsAdmin()) { %>
					<div class="form-group">
						<label for="cnName" class="col-sm-3 control-label">Client Name</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" value="<%= AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName()%>"  readonly/>
						</div>
					</div>
					<div class="form-group">
						<label for="cnName" class="col-sm-3 control-label">Service Purpose</label>
						<div class="col-sm-6">
							<div class="radio-list">
								<label class="radio-inline"> 
									<span>
										<input type="radio" name="servicePurpose" value="0" <%if(vpnLinkDTO.getServicePurpose()==0){ %> checked <%} %> />
									</span>  NO
								</label> 
								<label class="radio-inline"> 
									<span> 
										<input type="radio" name="servicePurpose" value="1" <%if(vpnLinkDTO.getServicePurpose()==1){ %> checked <%} %> />
									</span> YES 
								</label>
							</div>
						</div>
					</div>
					<%if(GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(GlobalConfigConstants.MIGRATION_ENABLED).getValue() == 1){%>
					<div class="form-group">
						<label for="cnName" class="col-sm-3 control-label">Is Migrated</label>
						<div class="col-sm-6">
							<div class="radio-list">
								<label class="radio-inline"> 
									<span>
										<input type="radio" name="isMigrated" value="0" <%if(vpnLinkDTO.getIsMigrated()==0){ %> checked <%} %> />
									</span>  NO
								</label> 
								<label class="radio-inline"> 
									<span> 
										<input type="radio" name="isMigrated" value="1" <%if(vpnLinkDTO.getIsMigrated()==1){ %> checked <%} %> />
									</span> YES 
								</label>
							</div>
						</div>
					</div>
					<%}%>
				<% }%>
				
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Bandwidth</label>
					<div class="col-sm-4">
						<html:text styleClass="form-control simple-input regi" property="linkBandwidth"/>
					</div>
					<div class="col-sm-4">
						<div class="radio-list">
							<label class="radio-inline"> 
								<span> <html:radio property="linkBandwidthType" value="1" /> </span>Mb
							</label>
							<label class="radio-inline"> 
								<span> <html:radio property="linkBandwidthType" value="2" /></span>Gb
							</label>
						</div>
					</div>
				</div>
				
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">VPN 	Layer Type</label>
					<div class="col-sm-6">
						<div class="radio-list">
							<label class="radio-inline"> <span>
								<html:radio property="layerType" value="1" /></span><%=EndPointConstants.layerTypeMap.get(1) %>
							</label> 
							<label class="radio-inline"> <span>
								<html:radio property="layerType" value="2" /></span><%=EndPointConstants.layerTypeMap.get(2) %>
							</label>
						</div>
					</div>
				</div>
				
<!-- 				<div class="form-group" id="vlanCounter" style="display:none"> -->
<!-- 					<label for="lnName" class="col-sm-3 control-label"><span id="lanCounterText">Additional VLan </span> Count</label> -->
<!-- 					<div class="col-sm-6"> -->
<%-- 						<html:text styleClass="form-control " property="lanCounter"/> --%>
<!-- 					</div> -->
<!-- 				</div> -->
				
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Connection Type</label>

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
							 	 	<html:radio  property="connectionType" value="<%=key.toString() %>"/> <%=EndPointConstants.connectionType.get(key) %>
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
						<textarea class="form-control simple-input regi" rows="3" name="linkDescription" >${vpnLink.linkDescription}</textarea>
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
							</div>
							
							<div class="portlet-body">
								<div class="form-group">
									<label class="col-sm-3 control-label" for="neName"> <%=VpnLanguageConstants.END_NAME %></label>
	
									<div class="col-sm-8">
										<input type="text" placeholder="Near End Name" name="neName" class=" form-control endName" id="nearEnd">
										<input type="hidden" name="neID" class="form-control " id="nearEndHidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item district" name="neDistrictStr" placeholder="Type to select district..." value="" type="text"> 
										<input type="hidden" value="<%=districtCategoryId%>" name="districtCategoryId" class="category-id"> 
										<input name="neDistrictID" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item upazila" name="neUpazilaStr" placeholder="Type to select upazila..." value="" type="text"> 
										<input type="hidden" value="<%=upazilaCategoryId%>" name="upazilaCategoryId" class="category-id"> 
										<input name="neUpazilaID" value="" type="hidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item union" name="neUnionStr" placeholder="Type to select union..." value="" type="text"> 
										<input type="hidden" value="<%=unionCategoryId%>" name="upazilaCategoryId" class="category-id"> 
										<input name="neUnionID" value="" type="hidden">
									</div>
								</div>
								
								<%if(loginDTO.getIsAdmin()){ %>
								<div class="form-group up-down-path">
									<label for="lnName" class="col-sm-3 control-label"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></label>
									<div class="col-sm-8">
										<input class="ne-hide form-control category-item load-ports" name="nePopIdStr" placeholder="Type to select pop..." value="" type="text">
										<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id"> 
										<input type="hidden" class="form-control" name="nePopID">
									</div>
								</div>
								
								<div class="form-group ">
									<label for="lnName" class="col-sm-3 control-label">Port
										Type</label>
									<div class="col-sm-8">
										<select id="nePortType" name=" nePortType" class="ne-hide form-control  " data-port-parent="<%=routerCategoryId%>">
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
										<textarea class="ne-hide form-control" rows="3" name="neAddress"
											placeholder="Give address..." required></textarea>
									</div>
								</div>
								
								<div class="form-group">
									<label for="neCoreType" class="col-sm-3 control-label"> OFC Type</label>
									<div class="col-sm-8">
										<select class="ne-hide  form-control coreType" name="neCoreType" id="neCoreType">
											<option value="-1">-</option>
											<%
											 for (Integer key : EndPointConstants.coreTypeMap.keySet()) {
											 %>
												 	<option value="<%=key%>"><%= EndPointConstants.coreTypeMap.get(key)%></option>
											<% 
												}
											%>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="lnName" class="col-sm-3 control-label">Local <%=VpnLanguageConstants.LOOP_PROVIDER %></label>

									<div class="col-sm-8">
										<select class="ne-hide form-control ofcProvider " name="neOfcProvider" id="neOfcProvider">
											<option value="-1" disabled="disabled" selected="selected">-</option>
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
									<label for="lnName" class="col-sm-3 control-label">Terminal Device Provider</label>

									<div class="col-sm-8">
										<select class="form-control ne-hide terminal-device-provider" name="neTerminalDeviceProvider" id="neTerminalDeviceProvider">
											<option value="-1">-</option>
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

					<div class="col-md-6 " id="farFormInputs" >
						<!-- BEGIN Portlet PORTLET-->
						<div class="portlet light" style="margin-bottom: 0px; padding-bottom: 0px;">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-level-down"></i> 
									<span class="caption-subject bold font-grey-gallery uppercase">  <%=VpnLanguageConstants.REMOTE_END %> </span>
								</div>

							</div>
							<div class="portlet-body">
								<div class="form-group">
									<label class="col-sm-3 control-label" for="feName"><%=VpnLanguageConstants.LOCAL_END %></label>
									<div class="col-sm-8">
										<input type="text" placeholder="Far End Name" name="feName" class=" form-control endName" id="farEnd">
										<input type="hidden" name="feID" class="form-control " id="farEndHidden">
									</div>
								</div>
								<div class="form-group up-down-path">
									<label class="col-sm-3 control-label" for="lnName"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></label>
									<div class="col-sm-8">
										<input class="fe-hide form-control category-item district" name="feDistrictStr" placeholder="Type to select..." value="" type="text">
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
										<input type="hidden" value="<%=unionCategoryId%>" name="upazilaCategoryId" class="category-id"> 
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
											<%	
											for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
												<option value="<%=portTypeInventoryAttributeNameType%>"><%=portTypeInventoryAttributeNameType%></option>
											<%}%>
											</select>
										</div>
									</div>
								<%} %>
								
								<div class="form-group">
									<label class="col-sm-3 control-label" for="lnName">Office Address</label>

									<div class="col-sm-8">
										<textarea  placeholder="Give address..." name="feAddress"
											rows="3" class="fe-hide form-control" required></textarea>
									</div>
								</div>
								
								<div class="form-group">
									<label for="feCoreType" class="col-sm-3 control-label"> OFC Type</label>
									<div class="col-sm-8">
										<select class="fe-hide  form-control coreType" name="feCoreType" id="feCoreType">
											<option value="-1" selected="selected">-</option>
											<%
											 for (Integer key : EndPointConstants.coreTypeMap.keySet()) {
											 %>
												 	<option value="<%=key%>"><%= EndPointConstants.coreTypeMap.get(key)%></option>
											<% 
												}
											%>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="lnName" class="col-sm-3 control-label">Remote <%=VpnLanguageConstants.LOOP_PROVIDER %></label>

									<div class="col-sm-8">
										<select class="fe-hide  form-control ofcProvider" name="feOfcProvider" id="feOfcProvider">
											<option value="-1" disabled="disabled" selected="selected">-</option>
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
									<label for="lnName" class="col-sm-3 control-label">Terminal Device Provider</label>

									<div class="col-sm-8">
										<select class="form-control fe-hide terminal-device-provider" name="feTerminalDeviceProvider" id="feTerminalDeviceProvider">
											<option value="-1" selected="selected">-</option>
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
								<span> Add Documents </span> 
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
						<jsp:include page="../../../common/fileListHelperEdit.jsp" flush="true">
							<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.VPN_LINK%>" />	
							<jsp:param name="entityID" value="<%=entityID%>" />	
						</jsp:include>	
					</div>
			 		<jsp:include page="../../../common/ajaxfileUploadTemplate.jsp" />
			 	</div>
			</div>
			<div class="form-actions text-center">
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Cancel</a>
				<button class="btn btn-reset-btcl" type="reset">Reset</button>
				<button class="btn btn-submit-btcl" type="submit">Submit</button>
			</div>
		</html:form>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#nearEndHidden").val(<%=nearEndDTO.getID() %>);
		$("#nearEnd").val('<%=nearEndDTO.getName() %>');
	 	$('input[name=neDistrictID]').val('<%=nearEndDetailsDTO.getDistrictId()%>');
        $("input[name=neDistrictStr]").val('<%=nearEndDetailsDTO.getDistrictName()%>');
        $("input[name=neUpazilaID]").val('<%=nearEndDetailsDTO.getUpazilaId()%>');
        $("input[name=neUpazilaStr]").val('<%=nearEndDetailsDTO.getUpazilaName()%>');
        $("input[name=neUnionID]").val('<%=nearEndDetailsDTO.getUnionId()%>');
        $("input[name=neUnionStr]").val('<%=nearEndDetailsDTO.getUnionName()%>');
        $("input[name=nePopID]").val('<%=nearEndDetailsDTO.getPopID()%>');
        $("input[name=nePopIdStr]").val('<%=nearEndDetailsDTO.getPopName()%>');
        $("textarea[name=neAddress]").val('<%=nearEndDetailsDTO.getAddress()%>');
		$("#nePortType").val('<%=nearEndDetailsDTO.getPortCategoryType()%>');
		$("#neCoreType").val('<%=nearEndDetailsDTO.getCoreType() %>');
		$("#neOfcProvider").val('<%=nearEndDetailsDTO.getOfcProviderTypeID() %>');
		$("#neTerminalDeviceProvider").val('<%=nearEndDetailsDTO.getTerminalDeviceProvider() %>');
		
		$("#farEndHidden").val('<%=farEndDTO.getID() %>');
		$("#farEnd").val('<%=farEndDTO.getName() %>');
		$('input[name=feDistrictID]').val('<%=farEndDetailsDTO.getDistrictId()%>');
        $("input[name=feDistrictStr]").val('<%=farEndDetailsDTO.getDistrictName()%>');
        $("input[name=feUpazilaID]").val('<%=farEndDetailsDTO.getUpazilaId()%>');
        $("input[name=feUpazilaStr]").val('<%=farEndDetailsDTO.getUpazilaName()%>');
        $("input[name=feUnionID]").val('<%=farEndDetailsDTO.getUnionId()%>');
        $("input[name=feUnionStr]").val('<%=farEndDetailsDTO.getUnionName()%>');
        $("input[name=fePopID]").val('<%=farEndDetailsDTO.getPopID()%>');
        $("input[name=fePopIdStr]").val('<%=farEndDetailsDTO.getPopName()%>');
        $("textarea[name=feAddress]").val('<%=farEndDetailsDTO.getAddress() %>');
		$("#fePortType").val('<%=farEndDetailsDTO.getPortCategoryType()%>');
		$("#feCoreType").val('<%=farEndDetailsDTO.getCoreType() %>');
		$("#feOfcProvider").val('<%=farEndDetailsDTO.getOfcProviderTypeID() %>');
		$("#feTerminalDeviceProvider").val('<%=farEndDetailsDTO.getTerminalDeviceProvider() %>');
	});
</script>
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
<script src="${context}assets/scripts/vpn/link/vpnLinkEdit.js" type="text/javascript"></script>

<script src="${context}/assets/scripts/vpn/link/link-edit-validation.js" type="text/javascript"></script>

<% if(isAnyOtherLinkWithThisNearEndAlreadyVerified){%>
<script>
	$(document).ready(function(){
		$("#nearFormInputs").find("input, select, textarea").prop("disabled", "true");
		$("#nearFormInputs .form-group:first").prepend("<div style='color:red;margin-bottom:10px;text-align:right' class='col-sm-11'>Information about this near end has already been verified. </br> It cannot be modified now.</div>");
	});
</script>
<%}%>

