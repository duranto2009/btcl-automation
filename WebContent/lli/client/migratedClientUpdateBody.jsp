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
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	try{
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Update Migrated Client LLI</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form class="form-horizontal" method="post"  action="<%=request.getContextPath() %>/MigratedClientUpdate.do">
			<input type="hidden" name="method" value="migrateLliClient" />
			<div class="form-body">
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Client Name</label>
					<div class="col-sm-6">
				     	<input id="clientIdStr" placeholder="Client Name" type="text" class="form-control" required>
					 	<input id="clientId" type="hidden" class="form-control" name="lliClientID" value="-1" required>
					</div>
				</div>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Activation Date</label>
					<div class="col-sm-6">
						<input type="text"  placeholder="Set Activation Date" class="datepicker form-control" name="activationDate" required>
					</div>
				</div>
			</div>
			
			<div class="form-actions text-center">
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
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
<script>
$(document).ready(function(){
	$("#clientIdStr").autocomplete({
		source : function(request, response) {
			$("#clientId").val(-1);
			var term = request.term;
			var url = context + 'AutoComplete.do?need=moduleClient';
			var formData = {};
			formData['name'] = term;
			formData['moduleID'] = <%=ModuleConstants.Module_ID_LLI%>;
			if (term.length >= 3) {
				callAjax(url, formData, response, "GET");
			} else {
				delay(function() {
					toastr.info("Your search name should be at lest 3 characters");
				}, systemConfig.getTypingDelay());
			}
		},
		minLength : 1,
		select : function(e, ui) {
			$('#clientId').val(ui.item.id);
			$('#clientIdStr').val(ui.item.data);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);
	};
});

</script>
