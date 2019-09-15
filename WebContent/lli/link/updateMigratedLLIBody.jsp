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
<%@	taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@	taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@	taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@	taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="login.LoginDTO"%>

<%
    Logger logger = Logger.getLogger(this.getClass());
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute( SessionConstants.USER_LOGIN );
	
	try{
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Update Migrated Connection</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form id="fileupload" class="form-horizontal" method="post"  action="../../LliLinkAction.do?mode=migratedLliUpdate">
			<div class="form-body">
				<% if (loginDTO.getIsAdmin()) { %>
					<div class="form-group">
						<label for="clientIDName" class="col-sm-3 control-label">Client Name</label>
						<div class="col-sm-6">
					     	<input id="clientIDName"  placeholder="Client Name" type="text" class="form-control" name="clientIDName"  required>
						 	<input id="clientID" type="hidden" class="form-control" name="clientID" value="-1" required>
						</div>
						<div class="col-sm-2 hidden">
							<a id="clientHyperLink" target="_blank" href="#" required>View Client Details</a>
						</div>
					</div>
				<% }else{ %>
					 <input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID() %>" >		
				<%}%>
				<div class="form-group">
					<label for="lliLinkIDName" class="col-sm-3 control-label">Connection Name</label>
					<div class="col-sm-6">
						<input type="text"  placeholder="Link  Name" class="linkName form-control" name="lliLinkIDName" id="lliLinkIDName" required>
						<input type="hidden" class="form-control" name="linkID" value="-1" required>
					</div>
					<div class="col-sm-2 hidden">
						<a id="linkHyperlink" target="_blank" href="">View Link Details</a>
					</div>
				</div>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Activation Date</label>
					<div class="col-sm-6">
						<input type="text"  placeholder="Set Activation Date" class="datepicker form-control" name="activationDate" required>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label"> Loop Distance</label>
					<div class="col-sm-6">
						<input type="text"  placeholder="Loop Distance" class=" form-control" name="feLoopDistance" required>
					</div>
				</div>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Security Money</label>
					<div class="col-sm-6">
						<input type="text"  placeholder="Security Money" class=" form-control" name="securityMoney" required>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Balance</label>
					<div class="col-sm-6">
						<input type="text"  placeholder="Balance" class=" form-control" name="balance" required>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Remove Demand Note</label>
					<div class="col-sm-6" style="padding-top:7px">
						<input type="checkbox"  placeholder="" class=" form-control" name="removeDemandNote" >
					</div>
				</div>
				
			</div>
			<div class="form-actions text-center">
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Back</a>
				<button class="btn btn-reset-btcl" type="reset" >Cancel</button>
				<button id="updateBtn" class="btn btn-submit-btcl"type="submit" disabled="disabled">Update</button>
			</div>
		</form>
	</div>
</div>

<%} catch (Exception ex) {
	logger.debug("General Error " + ex);
}
%>
<script src="${context}assets/scripts/lli/link/linkBandwidthVsClientAutoComplete.js" type="text/javascript"></script>