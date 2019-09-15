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
	InventoryService inventoryService = new InventoryService();

	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	try {
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption">
			<i class="fa fa-link" aria-hidden="true"></i> Bandwidth
			Upgrade/Downgrade
		</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form class="form-horizontal" method="post" action="../../LliBandwidthChange/submit.do">
			<div class="form-body">
				<%
					if (loginDTO.getIsAdmin()) {
				%>
				<div class="form-group">
					<label for="clientIDName" class="col-sm-3 control-label">
						Client Name
					</label>
					<div class="col-sm-6">
						<input id="clientIDName" placeholder="Client Name" type="text" class="form-control" name="clientIDName" required /> 
						<input id="clientID" type="hidden" class="form-control" name="clientID" value="-1" required />
					</div>
					<div class="col-sm-2 hidden">
						<a id="clientHyperLink" target="_blank" href="#" required>
							View Client Details
						</a>
					</div>
				</div>
				<%} else {%>
					<input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID()%>">
				<%}%>
				
				<div class="form-group">
					
					<label for="lliLinkIDName" class="col-sm-3 control-label">
						Connection Name
					</label>
					
					<div class="col-sm-6">
						<input type="text" placeholder="Connection  Name" class="linkName form-control" id="lliLinkIDName" name="lliLinkIDName" required />
						<input type="hidden" class="form-control" name="linkID" value="-1" required />
					</div>
					<div class="col-sm-2 hidden">
						<a id="linkHyperlink" target="_blank" href="">
							View Connection Details
						</a>
					</div>
				</div>
				
				<div class="form-group">
					
					<label for="lnName" class="col-sm-3 control-label">
						<b>Current Bandwidth</b>
					</label>
					<div class="col-sm-4">
						<input type="text" placeholder="Bandwidth " class="form-control" id="currentBandwidth" name="currentBandwidth" disabled="disabled"/>
					</div>
					
				</div>
				
				<div class="form-group">
					
					<label for="lnName" class="col-sm-3 control-label">
						New Bandwidth
					</label>
					
					<div class="col-sm-4">
						<input type="number" placeholder="Bandwidth " class="form-control" name="newBandwidth" id="newBandwidth" step="any" min="0.1" required>
					</div>
					
					<div class="col-sm-4">
					
						<div class="radio-list">
							<label class="radio-inline"> 
								<span> 
									<input type="radio" name="newBandwidthType" value="1" checked="">
								</span>
								Mb
							</label> 
							
							<label class="radio-inline"> 
								<span> 
									<input type="radio" name="newBandwidthType" value="2">
								</span>
								Gb
							</label>
						</div>
						
					</div>
				</div>
				
				<%-- <div class="form-group up-down-path">
					
					<label class="col-sm-3 control-label" for="lnName">
						<b>Current Port Type</b>
					</label>
					
					<div class="col-sm-6">
					
						<select name="portTypeCurrent" id="portTypeCurrent"
							class=" form-control  ports" ata-port-parent="<%=routerCategoryId%>" disabled >
							<option value="0" selected>-</option>
							<option value="8002">Port Type FE</option>
							<option value="8003">Port Type GE</option>
							<option value="8004">Port Type 10GE</option>
						</select>
					</div>
				</div> --%>
				
				<!-- <div class="form-group up-down-path">
					
					<label class="col-sm-3 control-label" for="lnName">
						New Port Type
					</label>
					
					<div class="col-sm-6">
						<select name="fePortType" class=" form-control  ports">
							<option value="0" selected>-</option>
							<option value="8002">Port Type FE</option>
							<option value="8003">Port Type GE</option>
							<option value="8004">Port Type 10GE</option>
						</select>
					</div>
					
				</div> -->
				
			</div>
			
			<div class="form-actions text-center">
			
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI%>" />
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">
					Back
				</a>
				
				<button class="btn btn-reset-btcl" type="reset">Cancel</button>
				
				<button id="updateBtn" class="btn btn-submit-btcl" type="submit" disabled="disabled">
					Update
				</button>
			</div>
		</form>
	</div>
</div>

<%
	} catch (Exception ex) {
		logger.debug("General Error " + ex);
	}
%>

<script src="${context}assets/scripts/lli/link/linkBandwidthVsClientAutoComplete.js" type="text/javascript"></script>
<script src="${context}/assets/scripts/lli/link/link-bandwidth-change-validation.js" type="text/javascript"></script>


