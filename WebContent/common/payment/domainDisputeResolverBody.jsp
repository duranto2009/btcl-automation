<%@page import="common.EntityTypeConstant"%>
<%@page import="util.TimeConverter"%>
<%@page import="domain.DomainNameDTO"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%
String context = "../../.." + request.getContextPath() + "/";
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title portlet-title-btcl">
		<div class="caption">
			<i class="fa fa-link" aria-hidden="true"></i> Dispute Resolver
		</div>
	</div>
	<div class="portlet-body portlet-body-btcl form">
		<html:form styleId="fileupload" styleClass="form-horizontal" method="post" enctype="multipart/form-data" action="/DomainDisputeResolver">
			<div class="form-body">

				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Select Client </label>
					<div class="col-sm-6">
						<input id="clientIdStr" class="form-control ui-autocomplete-input" name="clientIdStr" autocomplete="off" type="text"> 
						<input id="clientId" class="form-control" name="clientID" type="hidden" >
					</div>
				</div>
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label">Select 	Bill</label>
					<div class="col-sm-6">
						<html:select styleId="clientBill" styleClass="form-control pull-right" property="billID" >
							<option value="0" selected="">Select Bill</option>
						</html:select>
					</div>
					<div id="detailsView" class="col-md-2">
					</div>
				</div>
			</div>
			<div class="form-actions text-center">
				<input type="hidden" name="moduleID" id="moduleID" value="<%=request.getParameter("moduleID")%>">
				<button class="btn btn-submit-btcl" name="action" value="bank" type="submit">Resolve</button>
			</div>
		</html:form>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("#clientIdStr").autocomplete({
	    source: function(request, response) {
		 $("#clientId").val(-1);
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
	       loadBills(ui.item.id);
	       return false;
	    },
	 }).autocomplete("instance")._renderItem = function(ul, item) {
	    /* console.log(item); */
	    return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	 };
	 
	 var afterBillLoad=function(data){
	     console.log(data);
	     var options="";
	     if(data.length>0){
		 	options="<option value='-1' disabled selected>Select a bill</option>"
		     $.each(data,function(index, item){
			 	options+="<option value='"+item['ID']+"'>"+"#"+item['ID']+", "+item['billName']+"("+item['entityName']+")"+"</option>"
		     });
	     }else{
		 	options="<option value='-1' selected disabled>No Bill Found</option>"
	     }
	     $("#clientBill").html(options);
	    
	 }
	 function loadBills(clientID){
	     var formData = {};
	     var url= '../../AutoComplete.do?need=bill&moduleID='+$("#moduleID").val()+"&clientID="+clientID;
	     
         callAjax(url, formData, afterBillLoad, "GET");
         $("#detailsView").html("");
         $('#paymentStatus').remove();
	 }
	 $("#clientBill").change(function(){
	     $("#detailsView").html("<a target='_blank' href='../bill/billView.jsp?id="+$(this).val()+"' >View</a>");
	     var str='<div id="paymentStatus" class="form-group">'
				+'<label for="paymentStatus" class="col-sm-3 control-label">Payment Status </label>'
				+'	<div class="col-sm-6">'
				+'		<label for="paymentStatus" class="label label-info">Paid </label>'
				+'	</div>';
				+'</div>';
			$('.form-body').append(str);
			
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
