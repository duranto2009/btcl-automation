<%@page import="language.LliLanguageConstants"%>
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
	int popCategoryId = CategoryConstants.CATEGORY_ID_POP;
	int routerCategoryId = CategoryConstants.CATEGORY_ID_ROUTER;
	int portCategoryId = CategoryConstants.CATEGORY_ID_PORT;
	
	LliLinkService lliLinkService = new LliLinkService();
	InventoryService inventoryService= new InventoryService();

	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	
	
	try{
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Connection Shifting</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form id="fileupload" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../LliLinkAction.do?mode=shiftLink">
			<div class="form-body">
				<%
					if (loginDTO.getIsAdmin()) {
				%>
					<div class="form-group">
						<label for="cnName" class="col-sm-3 control-label">Client Name</label>
						<div class="col-sm-6">
					     	<input id="clientIdStr" placeholder="Client Name" type="text" class="form-control" name="clientIdStr"  required>
						 	<input id="clientId" type="hidden" class="form-control" name="clientID"  required>
						</div>
						<div class="col-sm-2 hidden">
							<a id="clientHyperLink" target="_blank" href="#">View Client Details</a>
						</div>
					</div>
				<% }else{ %>
					 <input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID() %>" >		
				<%}%>
				<div class="portlet-body">
					<div class="form-group">
						<label class="col-sm-3 control-label" for="feName">End Point Name</label>
						<div class="col-sm-6">
							<input type="hidden" name="neID" class="form-control " id="endPointID" required>
							<input type="text" placeholder="End Point Name" name="endName" class="form-control endName" id="farEnd">
						</div>
						<div class="col-sm-2 hidden">
						<a id="endPointHyperlink" target="_blank" href="">View End Point Details</a>
						</div>
			 	</div>
				<div class="form-group up-down-path">
					<label class="col-sm-3 control-label" for="lnName">District</label>
					<div class="col-sm-6">
						<input class="form-control category-item district" name="districtStr" placeholder="Type to select district..." value="" type="text">
						<input type="hidden" value="<%=CategoryConstants.CATEGORY_ID_DISTRICT%>" name="districtCategoryId" 	class="category-id">
						<input name="district" value="" type="hidden">
					</div>
				</div>
				<div class="form-group up-down-path">
					<label class="col-sm-3 control-label" for="lnName">Upazila</label>
					<div class="col-sm-6">
						<input class="form-control category-item upazila" name="upazilaStr" placeholder="Type to select upazila..." value="" type="text">
						<input type="hidden" value="<%=CategoryConstants.CATEGORY_ID_UPAZILA%>" name="upazilaCategoryId" 	class="category-id">
						<input name="upazila" value="" type="hidden">
					</div>
				</div>
				<div class="form-group up-down-path">
					<label class="col-sm-3 control-label" for="lnName">Union</label>
					<div class="col-sm-6">
						<input class="form-control category-item union" name="unionStr" placeholder="Type to select uinion..." value="" type="text">
						<input type="hidden" value="<%=CategoryConstants.CATEGORY_ID_UNION%>" name="unionCategoryId" 	class="category-id">
						<input name="union" value="" type="hidden">
					</div>
				</div>
				<div class="form-group up-down-path">
					<label class="col-sm-3 control-label" for="lnName">Pop</label>

					<div class="col-sm-6">
						<input class="form-control category-item load-ports" name="popIdStr" placeholder="Type to select..." value="" type="text"> 
						<input type="hidden" value="<%=popCategoryId%>" name="popCategoryId" class="category-id"> 
						<input type="hidden" class="form-control" name="popID">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label" for="lnName">Address</label>
					<div class="col-sm-6">
						<textarea  placeholder="Give address..." name="address"
							rows="3" class="form-control"></textarea>
					</div>
				</div>
			
				<div class="form-group up-down-path">
					<label class="col-sm-3 control-label" for="lnName"> Port Type</label>

					<div class="col-sm-6">
						<select id="portType" name="portType" class="form-control ports" data-port-parent="<%=routerCategoryId%>">
							<option value="0" disabled="disabled" selected="selected">-</option>
							<%
							 for (Integer key : EndPointConstants.portCategoryMap.keySet()) {
							 %>
								 	<option value="<%=key%>"><%= EndPointConstants.portCategoryMap.get(key)%></option>
							<% 
								}
							%>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="coreType" class="col-sm-3 control-label"> OFC Type</label>
					<div class="col-sm-6">
						<select class="form-control coreType" name="coreType" id="coreType">
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

					<div class="col-sm-6">
						<select class="form-control ofcProvider" name="ofcProvider" id="ofcProvider">
							<option value="0" disabled="disabled" selected="selected">-</option>
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
				
				<div class="form-group hidden" id="ofcProviderServiceType">
					<label for="lnName" class="col-sm-3 control-label">Provider Service Type</label>
					<div class="col-sm-6">
						<div class="radio-list">
							<label class="radio-inline"> <span>
								<input type="radio" name="feProviderServiceType" value="1" checked></span>Buy
							</label> 
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">Terminal Device Provider</label>
					<div class="col-sm-6">
						<select class="form-control" name="terminalDeviceProvider" id="terminalDeviceProvider">
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
			<div class="form-actions ">
				<div class="text-center">
					<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
					<a class="btn btn-md btn-circle yellow btn-outline sbold uppercase" type="button" href="<%=request.getHeader("referer")%>">Back</a>
					<button class="btn btn-md btn-circle grey-mint btn-outline sbold uppercase" type="reset" >Cancel</button>
					<button id="updateBtn" class="btn  btn-md btn-circle green-meadow btn-outline sbold uppercase" type="submit" disabled="disabled">Update</button>
				</div>
			</div>
		</form>
	</div>
</div>

<%} catch (Exception ex) {
	logger.debug("General Error " + ex);
}
%>
<script src="${context}assets/scripts/lli/link/lliLinkShifting.js" type="text/javascript"></script>

