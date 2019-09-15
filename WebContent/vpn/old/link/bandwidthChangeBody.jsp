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
    Logger logger = Logger.getLogger(this.getClass());
	int districtCategoryId = CategoryConstants.CATEGORY_ID_DISTRICT;
	int popCategoryId = CategoryConstants.CATEGORY_ID_POP;
	int routerCategoryId = CategoryConstants.CATEGORY_ID_ROUTER;
	int portCategoryId = CategoryConstants.CATEGORY_ID_PORT;
	
	VpnLinkService vpnLinkService = new VpnLinkService();
	InventoryService inventoryService= new InventoryService();

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
	
	try{
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Bandwidth Upgrade/Downgrade</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form class="form-horizontal" method="post" action="../../VpnBandwidthChange/submit.do">
			<div class="form-body">
				<%if (loginDTO.getIsAdmin()) {%>
					<div class="form-group">
						<label for="clientIDName" class="col-sm-3 control-label">Client Name</label>
						<div class="col-sm-6">
					     	<input id="clientIDName"  placeholder="Select Client" type="text" class="form-control" required>
						 	<input type="hidden" id="clientID" name="clientID" required>
						</div>
						<div class="col-sm-2 hidden">
							<a id="clientHyperLink" target="_blank" href="#">View Client Details</a>
						</div>
					</div>
				<% }else{ %>
					 <input type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID() %>" >		
				<%}%>
				
				<div class="form-group">
					<label for="vpnLinkIDName" class="col-sm-3 control-label">Select Link</label>
					<div class="col-sm-6">
						<input type="text"  placeholder="Link  Name" class="form-control" id="vpnLinkIDName" required>
						<input type="hidden" name="linkID" required>
					</div>
					<div class="col-sm-2 hidden">
						<a id="linkHyperlink" target="_blank" href="">View Link Details</a>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Current Bandwidth</label>
					<div class="col-sm-4">
						<p class="form-control" id="currentBandwidth" style="border:none;"></p>
					</div>
					<div class="col-sm-4"></div>
				</div>			
				
				<div class="form-group">
					<label for="lnName" class="col-sm-3 control-label">New Bandwidth</label>
					<div class="col-sm-4">
						<input type="number" placeholder="Bandwidth " class="form-control" name="newBandwidth"  id="newBandwidth"step="any" min="0.1" required>
					</div>
					<div class="col-sm-4">
						<div class="radio-list">
							<label class="radio-inline"> 
								<span> <input type="radio" name="newBandwidthType" value="1" checked> </span>Mb
							</label>
							<label class="radio-inline"> 
								<span> <input type="radio" name="newBandwidthType" value="2" ></span>Gb
							</label>
						</div>
					</div>
				</div>				
			</div>
			<div class="form-actions text-center">
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_VPN %>" />
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Back</a>
				<button class="btn btn-reset-btcl" type="reset" >Cancel</button>
				<button id="updateBtn" class="btn btn-submit-btcl"type="submit">Update</button>
			</div>
		</form>
	</div>
</div>

<%} catch (Exception ex) {
	logger.debug("General Error " + ex);
}
%>
<script src="${context}assets/scripts/vpn/link/linkBandwidthVsClientAutoComplete.js" type="text/javascript"></script>
<script src="${context}assets/scripts/lli/link/link-bandwidth-change-validation.js" type="text/javascript"></script>
