<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.List"%>
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
LliLinkDTO lliLinkDTO= (LliLinkDTO)request.getAttribute("lliLink");

LliFarEndDTO farEndDTO=(LliFarEndDTO)request.getAttribute("lliFE");
LliEndPointDetailsDTO farEndDetailsDTO=LinkUtils.getEndPointDTODetails(farEndDTO);


//d make Port Type dynamic
	List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(CategoryConstants.CATEGORY_ID_PORT);
	InventoryAttributeName portTypeInventoryAttributeName = new InventoryAttributeName();
	for(InventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
		if(inventoryAttributeName.getName().equals("Port Type")){
			portTypeInventoryAttributeName = inventoryAttributeName;
			break;
		}
	}

%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption"><i class="fa fa-link"></i>Connection Edit Form</div>

	</div>
	<div class="portlet-body form">
		<html:form  styleId="fileupload" styleClass="form-horizontal" method="post" action="/LliLinkAction.do?mode=edit"  enctype="multipart/form-data">
			<input type="hidden" name="entityID" value = "${lliLink.ID}" />
			<input type="hidden" name="linkID" value = "${lliLink.ID}" />
			<input type="hidden" name="nearEndPointID" value = "${lliNE.lliEndPointID}" />
			<input type="hidden" name="farEndPointID" value = "${lliFE.lliEndPointID}" />
			<input type="hidden" name="entityTypeID" value = "<%=entityTypeID %>" />
			<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
		 	
			<div class="form-body">
				<% if (loginDTO.getIsAdmin()) { %>
					<div class="form-group">
						<label for="cnName" class="col-sm-3 control-label">Client Name</label>
						<div class="col-sm-6">
							<input id="clientIdStr" type="text" class="form-control" name="clientIdStr" value="<%= AllClientRepository.getInstance().getClientByClientID(clientID).getLoginName()%>"  />
							<input id="clientId" type="hidden" class="form-control"  name="clientID" value="${clientID}" >
						</div>
					</div>
					<div class="form-group">
						<label for="cnName" class="col-sm-3 control-label">Service Purpose</label>
						<div class="col-sm-6">
							<div class="radio-list">
								<label class="radio-inline"> 
									<span>
										<input type="radio" name="servicePurpose" value="0" <%if(lliLinkDTO.getServicePurpose()==0){ %> checked <%} %> />
									</span>  NO
								</label> 
								<label class="radio-inline"> 
									<span> 
										<input type="radio" name="servicePurpose" value="1" <%if(lliLinkDTO.getServicePurpose()==1){ %> checked <%} %> />
									</span> YES 
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="cnName" class="col-sm-3 control-label">Is Migrated</label>
						<div class="col-sm-6">
							<div class="radio-list">
								<label class="radio-inline"> 
									<span>
										<input type="radio" name="isMigrated" value="0" <%if(lliLinkDTO.getIsMigrated()==0){ %> checked <%} %> />
									</span>  NO
								</label> 
								<label class="radio-inline"> 
									<span> 
										<input type="radio" name="isMigrated" value="1" <%if(lliLinkDTO.getIsMigrated()==1){ %> checked <%} %> />
									</span> YES 
								</label>
							</div>
						</div>
					</div>
				<% }else{ %>
					 <input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID() %>" >		
				 <%}%>
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Connection Name</label>

					<div class="col-sm-6">
						<html:text styleClass="form-control simple-input regi" property="linkName" />
					</div>
				</div>
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Bandwidth</label>
					<div class="col-sm-4">
							<html:text styleClass="form-control simple-input regi" property="linkBandwidth" />
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
				
				
				
				<input type="hidden" name="layerType" value="1" />
							
				
				
				<input type="hidden" class="form-control " name="lanCounter" value="0"/>
					
				
				
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
						<textarea class="form-control simple-input regi" rows="3" name="linkDescription" >${lliLink.linkDescription}</textarea>
					</div>
				</div>
				<div class="form-group">

					<div id="farFormInputs" >
						<!-- BEGIN Portlet PORTLET-->
						<div class="portlet light" style="margin-bottom: 0px; padding-bottom: 0px;">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-level-down"></i> 
									<span class="caption-subject bold font-grey-gallery uppercase">  <%=LliLanguageConstants.REMOTE_END %> </span>
								</div>

							</div>
							<div class="portlet-body">
								<div class="form-group"  style="display:none">
									<label class="col-sm-3 control-label" for="feName"><%=LliLanguageConstants.END_NAME %></label>
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
								
								
								<div class="form-group">
									<label class="col-sm-3 control-label" for="lnName">Office Address</label>

									<div class="col-sm-8">
										<textarea  placeholder="Give address..." name="feAddress"
											rows="3" class="fe-hide form-control"></textarea>
									</div>
								</div>
								<%if(loginDTO.getIsAdmin()){ %>
									<div class="form-group ">
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
									<label for="feCoreType" class="col-sm-3 control-label"> OFC Type</label>
									<div class="col-sm-8">
										<select class="fe-hide  form-control coreType" name="feCoreType" id="feCoreType">
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
									<label for="lnName" class="col-sm-3 control-label"> <%=LliLanguageConstants.LOOP_PROVIDER %></label>

									<div class="col-sm-8">
										<select class="fe-hide  form-control ofcProvider" name="feOfcProvider" id="feOfcProvider">
											<option value="" disabled="disabled" selected="selected">-</option>
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
								<div class="form-group hidden" id="feProviderServiceType">
									<label for="lnName" class="col-sm-3 control-label">Provider Service Type</label>

									<div class="col-sm-8">
										<div class="radio-list">
											<label class="radio-inline"> <span>
												<input type="radio" name="feProviderServiceType" value="1" checked></span>Buy
											</label> 
											<!--label class="radio-inline"> 
												<span>
												<input type="radio" name="feProviderServiceType" value="2" ></span>Rent
											</label-->
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="lnName" class="col-sm-3 control-label">Terminal Device Provider</label>

									<div class="col-sm-8">
										<select class="form-control fe-hide " name="feTerminalDeviceProvider" id="feTerminalDeviceProvider">
											<option value="">-</option>
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
								<input class="jFile" id="nid" type="file" name="<%=FileTypeConstants.LLI_LINK_ADD.LLI_LINK_ADD_FORWARDING_LETTER %>" > 
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
						<jsp:include page="../../common/fileListHelperEdit.jsp" flush="true">
							<jsp:param name="entityTypeID" value="<%=EntityTypeConstant.LLI_LINK%>" />	
							<jsp:param name="entityID" value="<%=entityID%>" />	
						</jsp:include>	
					</div>
			 		<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
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
		
		//$("#farFormInputs").find(".fe-hide").attr("disabled", true);
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
		})
	})
</script>
<script src="${context}assets/scripts/lli/link/lliLink.js" type="text/javascript"></script>

<script src="${context}/assets/scripts/lli/link/link-edit-validation.js" type="text/javascript"></script>