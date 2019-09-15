<%@page import="common.ModuleConstants"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="domain.DomainDTO"%>
<%@page import="domain.DomainService"%>
<%@page import="domain.constants.DomainRequestTypeConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="util.TimeConverter"%>
<%@page import="domain.DomainNameDTO"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%
DomainService domianService= new DomainService();
String context = "../../.." + request.getContextPath() + "/";
String entityID= request.getParameter("entityID");
ClientDTO clientDTO= null;
DomainDTO domainDTO=null;

if(entityID!=null){
	domainDTO=domianService.getDomainByID(Long.parseLong(entityID));
	long clientID= domainDTO.getDomainClientID();
	clientDTO=AllClientRepository.getInstance().getClientByClientID(clientID);
}
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption">
			<i class="fa fa-link" aria-hidden="true"></i> Manual Demand Note Generation
		</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<form id="fileupload" class="form-horizontal" method="post" enctype="multipart/form-data" action="../../CommonAction.do?mode=genDemandNote">
			<input type='hidden' name='requestTypeID' value="<%=DomainRequestTypeConstants.REQUEST_RENEW.CLIENT_APPLY%>"> 
			<input type="hidden" name="actionName" value="/domain/NIXDemandNote/manualDemandNote.jsp" class="actionName">
			<input type='hidden' name='entityTypeID' value="<%=EntityTypeConstant.DOMAIN%>">
			<input type='hidden' name='requestToAccountID' value="">
			<input type='hidden' name='forceDemandNote' value="true">
			
			<div class="form-body">
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Select Client </label>
					<div class="col-sm-6">
						
						<%if(entityID!=null){ %>
							<input id="clientId" class="form-control" name="clientID" type="hidden" value="<%=clientDTO.getClientID()%>"/>
							<input id="clientIdStr" class="form-control ui-autocomplete-input" name="clientIdStr" autocomplete="off" type="text" value="<%=clientDTO.getLoginName()%>"> 
						<%}else{ %>
							<input id="clientIdStr" class="form-control ui-autocomplete-input" name="clientIdStr" autocomplete="off" type="text"> 
							<input id="clientId" class="form-control" name="clientID" type="hidden">
						<%} %>
						
					</div>
				</div>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Select Domain</label>
					<div class="col-sm-6">
						<select id="clientDomainList" class="form-control pull-right" name="entityID" required>
							<%if(entityID==null){ %>
								<option value="0" selected>Select </option>
							<%}else{ %>
								<option value="<%=entityID %>" selected><%=domainDTO.getDomainAddress() %> </option>
							<%} %>
						</select>
					</div>
					<div id="detailsView" class="col-md-2">
						
					</div>
				</div>

				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Year</label>
					<div class="col-sm-6">
						<div class="">
							<input  type="number" name="year"  class="form-control" min="0" max="9999" required/>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Cost </label>
					<div class="col-sm-6">
						<div class="">
							<input type="number" name="cost"  class="form-control" min="1" required/>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label"> Late Fee </label>
					<div class="col-sm-6">
						<div class="">
							<input type="number" name="lateFee"  class="form-control" value="0"/>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label for="description" class="col-sm-3 control-label">Description</label>

					<div class="col-sm-6">
						<textarea name="description" class="form-control" rows="3"></textarea>
					</div>
				</div>

			</div>
			<div class="form-actions text-center">
				<input type="hidden" name="moduleID" id="moduleID" value="<%=request.getParameter("moduleID")%>">
				<button class="btn btn-reset-btcl" type="reset">Reset</button>
				<button class="btn btn-submit-btcl"  type="submit">Generate Demand Note</button>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("#clientIdStr").autocomplete({
	    source: function(request, response) {
		 $("#clientId").val(-1);
	       var term = request.term;
	       var url = '../../AutoComplete.do?need=client&moduleID=<%=ModuleConstants.Module_ID_DOMAIN%>';
	       var formData = {};
	       formData['name']=term;
	       if (term.length >= 3) {
				callAjax(url, formData, response, "GET");
			} else {
				delay(function() {
					toastr.info("Your search name should be at lest 3 characters");
				}, systemConfig.getTypingDelay());
			}
	    },
	    minLength: 1,
	    select: function(e, ui) {
	       $('#clientIdStr').val(ui.item.data);
	       $('#clientId').val(ui.item.id);
	       $('input[name=requestToAccountID]').val(ui.item.id);
	       loadDomains(ui.item.id);
	       return false;
	    },
	 }).autocomplete("instance")._renderItem = function(ul, item) {
	    /* console.log(item); */
	    return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	 };
	 
	 var afterDomainLoad=function(data){
	     console.log(data);
	     var options="";
	     if(data.length>0){
		 	options="<option value='-1' disabled selected>Select a Domain</option>"
		     $.each(data,function(index, item){
			 	options+="<option value='"+item['ID']+"'>"+item['domainAddress']+"</option>";
		     });
	     }else{
		 	options="<option value='-1' selected disabled>No Domain Found</option>"
	     }
	     $("#clientDomainList").html(options);
	    
	 }
	 function loadDomains(clientID){
	     var formData = {};
	     var url= '../../AutoComplete.do?need=domainList&moduleID=<%=ModuleConstants.Module_ID_DOMAIN%>'+'&clientID='+clientID;
	     
         callAjax(url, formData, afterDomainLoad, "GET");
         $("#detailsView").html("");
	 }
	 
	 $("#clientDomainList").change(function(){
	     $("#detailsView").html("<a target='_blank' href='../../ViewDomain.do?entityID="+$(this).val()+"&entityTypeID=101'>View</a>");
	 })
	 
	 $('#fileupload').submit(function(){
		 if($("#clientId").val()==''){
			 toastr.error("Please search and select a client");
			 return false;
		 }
		 if($("#clientBill").val()<=0 ||  $("#clientBill").val()=='') {
			 toastr.error("Please select a valid bill");
			 return false;
		 }
		 return true;
	 })
});

</script>
