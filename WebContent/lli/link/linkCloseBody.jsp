<%@page import="util.TimeConverter"%>
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
		<div class="caption"><i class="fa fa-link" aria-hidden="true"></i> Connection Close Request</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form id="fileupload" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../LliLinkAction.do?mode=linkClose">
			<div class="form-body">
					<%

					if (loginDTO.getIsAdmin()) {
				%>
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
				<%-- <default-form-group data-title="Client Name" data-width=3-6>
					<client-autocomplete data-need=client data-module=6 data-status=active></client-autocomplete>
				</default-form-group> --%>
					
				<% }else{ %>
					 <input id="clientId" type="hidden" class="form-control" name="clientID" value="<%=loginDTO.getAccountID() %>" >		
				<%}%>
				<div class="form-group">
					<label for="lliLinkIDName" class="col-sm-3 control-label">Select Connection</label>
					<div class="col-sm-6">
						<input type="text"  placeholder="Link  Name" class="form-control" id="lliLinkIDName" required>
						<input type="hidden" name="linkID" required>
					</div>
					<div class="col-sm-2 hidden">
						<a id="linkHyperlink" target="_blank" href="">View Connection Details</a>
					</div>
				</div>
				
				<div class="form-group">
					<label for="tdDate" class="col-sm-3 control-label">Expected Closing Date</label>
					<div class="col-sm-6">
						<input type="text"  class="form-control datepicker"  name="closeDate"  value="<%=TimeConverter.getToDay()%>"/>
					</div>
				</div>
				<div class="form-group">
					<label for="description" class="col-sm-3 control-label">Comments</label>
					<div class="col-sm-6">
						<textarea class="form-control" rows="3" name="linkDescription"
							placeholder="Comments..."></textarea>
					</div>
				</div>		
				<div class="form-group">
					<div class="col-md-12">
					 	<div class="col-md-offset-3 col-md-3" style="padding: 0px;">
							<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
							<span class="btn btn-warning-btcl  fileinput-button">
								<i class="fa fa-upload"></i>
								<span> Add <%=FileTypeConstants.TYPE_ID_NAME.get(FileTypeConstants.LLI_LINK_ADD.LLI_LINK_CLOSE)%> </span>
								<input class="jFile"  type="file" name="<%=FileTypeConstants.LLI_LINK_ADD.LLI_LINK_CLOSE %>" > 
							</span>
						</div>
						<div class="col-md-6">
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
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_LLI %>" />
				<a class="btn btn-cancel-btcl" type="button" href="<%=request.getHeader("referer")%>">Back</a>
				<button class="btn btn-reset-btcl" type="reset" >Cancel</button>
				<button id="updateBtn" class="btn btn-submit-btcl"type="submit" >Submit</button>
			</div>
		</form>
	</div>
</div>

<%} catch (Exception ex) {
	logger.debug("General Error " + ex);
}
%>

<%-- <script src="${context}assets/scripts/lli/link/linkVsClientAutoComplete.js" type="text/javascript"></script> --%>
<script src="${context}assets/scripts/lli/link/linkBandwidthVsClientAutoComplete.js" type="text/javascript"></script>
<script src="${context}assets/scripts/vpn/link/link-bandwidth-change-validation.js" type="text/javascript"></script>
<%-- <script src="${context}assets/scripts/lli/link/link-close-validation.js" type="text/javascript"></script> --%>
