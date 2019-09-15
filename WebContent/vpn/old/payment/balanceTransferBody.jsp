<%@page import="common.ModuleConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="util.TimeConverter"%>
<%@page import="domain.DomainNameDTO"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%
String context = "" + request.getContextPath() + "/";
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption">
			<i class="fa fa-exchange" aria-hidden="true"></i>VPN Balance Transfer
		</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<html:form styleId="fileupload" styleClass="form-horizontal" method="post"  action="/VpnBalanceTransfer">
			<div class="form-body">

				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Client </label>
					<div class="col-sm-6">
						<div class="input-icon">
                            <i class="fa fa-user"></i>
                          	<input id="clientIdStr" class="form-control ui-autocomplete-input" name="clientIdStr" autocomplete="off" type="text" placeholder="Search Client"> 
                         </div>
						<input id="clientId" class="form-control" name="clientID" type="hidden" >
					</div>
					<div class="col-sm-2 hidden">
						<a id="clientHyperLink" target="_blank" href="#">View Client</a>
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">From VPN Link </label>
					<div class="col-sm-6">
						<div class="input-icon">
                             <i class="fa fa-search"></i>
                             <select name="vpnLinkFrom" id="balanceFrom"  class="select-2 form-control balanceFrom">
                             	<option disabled>Balance Transfer From</option>
                             </select>
                         </div>
					</div>
					<div id="balanceFromDetails" class="col-md-2">
						
					</div>
				</div>
				
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">To  VPN Link </label>
					<div class="col-sm-6">
						<div class="input-icon">
                             <i class="fa fa-search"></i>
                               <select name="vpnLinkTo" id="balanceTo"  class="select-2 form-control balanceFrom">
                             	<option disabled>Balance Transfer To</option>
                             </select>
                         </div>
					</div>
					<div id="balanceToDetails" class="col-md-2">
						
					</div>
				</div>

				<div class="form-group">
					<label for="description" class="col-sm-3 control-label">Description</label>

					<div class="col-sm-6">
						<html:textarea property="description" styleClass="form-control" rows="3" ></html:textarea>
					</div>
				</div>

			</div>
			<div class="form-actions text-center">
				<input type="hidden" name="moduleID" id="moduleID" value="<%=ModuleConstants.Module_ID_VPN%>">
				<button class="btn  btn-sm btn btn-circle  grey-mint btn-outline sbold uppercase " type="reset">Reset</button>
				<button class="submit btn btn-sm  btn-circle green-meadow  sbold uppercase" name="action" value="bank" type="submit">Transfer Balance</button>
			</div>
		</html:form>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("#clientIdStr").autocomplete({
	    source: function(request, response) {
		   $("#clientId").val(-1);
		   $("#balanceFromDetails").html('');
		   $("#balanceToDetails").html('');
		   
	       var term = request.term;
	       var url = '../../AutoComplete.do?need=client&moduleID='+$("#moduleID").val();
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
           $("#clientHyperLink").closest('div').removeClass("hidden");
           $("#clientHyperLink").attr("href",context+"GetClientForView.do?moduleID="+$("#moduleID").val()+"&entityID="+ui.item.id);
	       loadVpnLinks(ui.item.id);
	       return false;
	    },
	 }).autocomplete("instance")._renderItem = function(ul, item) {
	    return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	 };
	 
	 var afterVpnLinkLoad=function(data){
	     console.log(data);
	     var options="";
	     if(data.length>0){
		     $.each(data,function(index, item){
			 	options+="<option value='"+item['data']['ID']+"'>"+"#"+item['data']['linkName']+"</option>"
		     });
	     }else{
		 	options="<option value='-1' selected disabled>No Vpn Link Found for this client</option>"
	     }
	     var optionFrom="<option value='-1' disabled selected>---select---</option>" +options;
	     var optionTo="<option value='-1' disabled selected>---select---</option>" +options;
	     $("#balanceFrom").html(optionFrom);
	     $("#balanceTo").html(optionTo);
	    
	 }
	 function loadVpnLinks(clientID){
	     var formData = {};
	     var url= context+'FetchAutoLoadAction.do?linkName=&mode=vpnLinkDetails&clientID='+clientID+'&status=active';
	     
         callAjax(url, formData, afterVpnLinkLoad, "GET");
         $("#detailsView").html("");
	 }
	
	 $('.balanceFrom').change(function(){
		if($('#balanceFrom').val()==$('#balanceTo').val()){
			$(this).val(-1);
			toastr.error("Balance from Vpn ID and Balance To VPN ID must be different");
		}
		if($(this).attr('id')=='balanceFrom'){
			var fromLink="<a target='_blank' href='"+context+"VpnLinkAction.do?entityID="+$(this).val()+"&entityTypeID="+CONFIG.get('entityType','link')+"'> View Link </a>";
			$("#balanceFromDetails").html(fromLink);
		}
		if($(this).attr('id')=='balanceTo'){
			var toLink="<a target='_blank' href='"+context+"VpnLinkAction.do?entityID="+$(this).val()+"&entityTypeID="+CONFIG.get('entityType','link')+"'> View Link </a>";
			$("#balanceToDetails").html(toLink);
		}
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
		 if($("#balanceFrom").val()<=0 ||  $("#balanceFrom").val()=='') {
			 toastr.error("Please select a valid Vpn From ID");
			 return false;
		 }
		 if($("#balanceTo").val()<=0 ||  $("#balanceTo").val()=='') {
			 toastr.error("Please select a valid Vpn From ID");
			 return false;
		 }
		 return true;
	 })
});

</script>
