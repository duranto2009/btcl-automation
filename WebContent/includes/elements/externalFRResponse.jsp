<%@page import="user.UserRepository"%>
<%@page import="user.UserDTO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="vpn.constants.VpnRequestTypeConstants"%>
<%@page import="common.constants.RequestTypeConstants"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="common.CategoryConstants"%>
<%@page import="vpn.link.LinkUtils"%>
<%@page import="vpn.link.VpnEndPointDetailsDTO"%>
<%@page import="vpn.link.VpnLinkService"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="vpn.link.VpnEndPointDTO"%>
<%@page import="request.CommonRequestDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
CommonRequestDTO externamComReq = (CommonRequestDTO) request.getAttribute("commonRequestDTO");
VpnLinkService vpnLinkService= new VpnLinkService();
VpnEndPointDetailsDTO endPointDTO=null;
String divClassName="";
String divID="";
if(externamComReq.getEntityTypeID()==EntityTypeConstant.VPN_LINK_NEAR_END){
	endPointDTO=LinkUtils.getEndPointDTODetails(vpnLinkService.getNearEndByNearEndID(externamComReq.getEntityID()));
	divClassName="near-end-div";
	divID=EntityTypeConstant.VPN_LINK_NEAR_END+"";
}else if(externamComReq.getEntityTypeID()==EntityTypeConstant.VPN_LINK_FAR_END){
	endPointDTO=LinkUtils.getEndPointDTODetails(vpnLinkService.getFarEndByFarEndID(externamComReq.getEntityID()));
	divClassName="far-end-div";
	divID=EntityTypeConstant.VPN_LINK_FAR_END+"";
}

String readOnly = "";
String disabled = "";

if( externamComReq.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_FAR_END
	|| externamComReq.getRequestTypeID() == VpnRequestTypeConstants.REQUEST_NEW_LINK.SYSTEM_RESPONSE_EXACT_EXTERNAL_FR_OF_NEAR_END ){
	readOnly = "readonly";
	disabled = "disabled";
}
String currentActionTypeID = (String)request.getAttribute("fileRequestID");
%>
<div id="<%=divID %>" class="end-div <%=divClassName %>">
	<div class="form-group">
		<input name="categoryID" value="" type="hidden">
		<label for="cusDistance" class="control-label col-md-3">Select <%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_DISTRICT).getName() %></label>
		<div class="col-md-4">
			<input class="category-item form-control col-md-6"   placeholder="Type District Name"  autocomplete="off"  type="text"
			value="<%=endPointDTO.getDistrictName()%>" 
			data-category-item-id="<%=endPointDTO.getDistrictId() %>" readonly>
			<input class="category-id form-control" value="<%=CategoryConstants.CATEGORY_ID_DISTRICT %>"  type="hidden">
			<input class="form-control" placeholder="" name="districtID" value="<%=endPointDTO.getDistrictId() %>" type="hidden">
		</div>
	</div>
	<div class="form-group">
		<label for="cusDistance" class="control-label col-md-3">Select <%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UPAZILA).getName() %></label>
		<div class="col-md-4">
			<input class="category-item form-control " id="" placeholder="Type Upazila Name" autocomplete="off" type="text"
			value="<%=endPointDTO.getUpazilaName()%>" <%=readOnly%> >
			<input class="category-id form-control" value="<%=CategoryConstants.CATEGORY_ID_UPAZILA %>" type="hidden">
			<input class="item-id form-control" placeholder="" name="upazilaID" value="<%=endPointDTO.getUpazilaId() %>" type="hidden">	
		</div>
	</div>
	<div class="form-group">
		<label for="cusDistance" class="control-label col-md-3">Select <%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_UNION).getName() %></label>
		<div class="col-md-4">
			<input class="category-item form-control " id="" placeholder="Type Union Name" autocomplete="off" type="text"
			value="<%=endPointDTO.getUnionName()%>" <%=readOnly%> >
			<input class="category-id form-control" value="<%=CategoryConstants.CATEGORY_ID_UNION %>" type="hidden">
			<input class="item-id form-control" placeholder="" name="unionID" value="<%=endPointDTO.getUnionId() %>" type="hidden">	
		</div>
	</div>
	<div class="form-group">
		<label for="cusDistance" class="control-label col-md-3">Select <%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(InventoryConstants.CATEGORY_ID_POP).getName() %></label>
		<div class="col-md-4">
			<input class="category-item form-control " id="" placeholder="Type POP Name" autocomplete="off" type="text"
			value="<%=endPointDTO.getPopName()%>" <%=readOnly%> >
			<input class="category-id form-control" value="<%=CategoryConstants.CATEGORY_ID_POP %>" type="hidden">
			<input class="item-id form-control"  placeholder="" name="popID" value="<%=endPointDTO.getPopID() %>" type="hidden">	
		</div>
	</div>
	
		
	
	
	<div class="form-group">
		<label for="froptions" class="control-label col-md-3">Select Option</label>
		<div class="col-md-4">
			<%
			
			UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
			if(userDTO.isBTCLPersonnel()){%>
			
				<label>
					<input type="radio" name="froptions" class="froptions " value="btcl" required selected> BTCL Only 
				</label> 
				&nbsp
			<%}else{ %>
			<label><input type="radio" name="froptions" class="froptions " value="oc" required selected> O/C Only </label> &nbsp
			<label><input type="radio" name="froptions" class="froptions " value="btclandoc" required 
				<%if(endPointDTO.getOfcProviderTypeID() != EndPointConstants.OFC_BTCL){ %> <%=disabled%> <%} %> > BTCL and O/C </label>
			<%} %>
		</div>
	</div>
	
	
	<div class="form-group btclDist" style="display:none" >
		<label for="btclDistance" class="control-label col-md-3">Distance Covered By BTCL (meter)</label> 
		<div class="col-md-4">
			<input id="btclDistance<%=divID %>" class="form-control  distance"  placeholder="Distance in meter" name="btclDistance" value="0" type="number" min="0">
		</div>
	</div>
	
	<div class="form-group ocDist" style="display:none" >
		<label for="ocDistance" class="control-label col-md-3" >Distance Covered By O/C (meter)</label>
		<div class="col-md-4">
			<input id="ocDistance<%=divID %>" class="form-control  distance"  placeholder="Distance in meter" name="ocDistance" value="0" type="number" min="0">
		</div>
	</div>
	
	<div class="form-group btclAndOCDist" style="display:none; margin-bottom: 20px;">
		<label for="cusDistance" class="control-label col-md-2" >Distance Covered By Customer  (meter)</label>
		<div class="col-md-4">
			<input id="cusDistance<%=divID %>" class="form-control  distance"  placeholder="Distance in meter" name="cusDistance" value="0" type="number" min="0">
		</div>
	</div>
	
	<div class="form-group">
		<label for="totalDistance" class="control-label col-md-3">Total Local loop distance (meter)</label> 
		<div class="col-md-4">
			<input id="totalDistance<%=divID %>" class="form-control distance "  placeholder="Total Local loop distance in meter " name="totalDistance" value="<%=endPointDTO.getDistanceFromNearestPopInMeter() %>" type="number" min="0">
		</div>
	</div>
</div>

<script type="text/javascript">

$(document).ready(function() {
    
	var currentAutoComplete;
    $(".category-item").each(function(){
		$(this).autocomplete({
			source : function(request, response) {
			    var map = {};
			    currentAutoComplete=this.element;
			    var categoryObj= $( this.element).next(".category-id");
			    var parentItemObj = $( this.element).closest(".form-group").prev(".form-group").find(".category-item");
			    
			    
			    map['parentItemID'] = parentItemObj.next().next().val(); 
	            map['categoryID'] = categoryObj.val();
	            
	            if( map['categoryID'] == <%=InventoryConstants.CATEGORY_ID_POP %> || map['categoryID'] == <%=InventoryConstants.CATEGORY_ID_DISTRICT %>){
	            	map['isParentNeeded'] = "false";
	            }
			    
	         	var url =  '../../AutoInventoryItem.do?partialName='+request.term;
	         	callAjax(url, map, response, "GET");
	         	currentAutoComplete.closest(".form-group").nextAll(".form-group").find(".category-item").val("");
	         	currentAutoComplete.closest(".form-group").find(".item-id").val("");
			    currentAutoComplete.closest(".form-group").nextAll(".form-group").find(".item-id").val("");
			},
			minLength : 1,
			select : function(e, ui) {
			    currentAutoComplete.val(ui.item.name);
	            currentAutoComplete.next().next().val(ui.item.ID);
			    toastr.success( ui.item.name + "  is selected");
			    
				return false;
			},
	
		}).autocomplete("instance")._renderItem = function(ul, item) {
	    	console.log(item);
	        return $("<li>") .append("<a>" + item.name + "</a>") .appendTo(ul);
		};
    });
    
    var currentID;
	//LOG("currentID: "+currentID);
	
    $(".end-div").on( "click", ".froptions", function(){
		var val = $(this).val(); 
		currentID=$(this).closest('.end-div').attr('id');
		$('.distance').val(0);
		
		if( val == "btcl" ){
			//$(this).next('.acontainer').toggle(400);
			$(this).closest('.end-div').find(".ocDist").hide(); 
			$(this).closest('.end-div').find(".ocDistance").val("0"); 
			$(this).closest('.end-div').find(".btclDist").show();
		}
		else if( val == 'oc' ){
			
			$(this).closest('.end-div').find(".btclDist").hide(); 
			$(this).closest('.end-div').find(".btclDistance").val("0"); 
			$(this).closest('.end-div').find(".ocDist").show();
		}
		else if( val == "btclandoc" ){
			
			$(this).closest('.end-div').find(".btclDist").show();
			$(this).closest('.end-div').find(".ocDist").show();
		}
		
	})
	
	$(".distance").on("change",function(){
		var total=parseFloat($('#btclDistance'+currentID).val())+parseFloat( $('#ocDistance'+currentID).val())+parseFloat($('#cusDistance'+currentID).val());
		$('#totalDistance'+currentID).val(total);
	});
    function validateInventory(inventory){
    	var flag, closestFormGroup;
    	flag = true;
    	closestFormGroup = $(inventory).closest('.form-group');
    	if( $(inventory).val() < 0  || $(inventory).val() === "" )  {
    		$(inventory).closest('.form-group').addClass('has-error');
			flag = false;
    	}else {
    		if($(closestFormGroup).hasClass('has-error')){
    			$(closestFormGroup).removeClass('has-error');
    		}
    		flag = true;
    	}
    	return flag;
    }
    function validateDistance(distance){
    	var flag, closestFormGroup;
    	flag = true;
    	closestFormGroup = $(distance).closest('.form-group');
    	
    	if($(closestFormGroup).css('display') !== 'none' && $(distance).val() <= 0) {
    		$(closestFormGroup).addClass('has-error');
    		flag = false;
    	}else {
    		if($(closestFormGroup).hasClass('has-error')){
    			$(closestFormGroup).removeClass('has-error');
    		}
    		flag = true;
    	}
    	return flag;
    }
    function validateFileCount(){
    	if($('.files tr').length === 0 ){
    		toastr.error('Documents must be given');
    		return false;
    	}
    	return true;
    }
    
    function validateForm() {
    	var flag1, flag2, flag3, flag4, flag5, flag6, upazilaID, unionID, popID, ocDistance, btclDistance;
    	flag = true;
    	upazilaID = $('input[name=upazilaID]');
    	unionID = $('input[name=unionID]');
    	popID = $('input[name=popID]');
    	ocDistance = $('input[name=ocDistance]');
    	btclDistance = $('input[name=btclDistance]');
		flag1 = validateInventory(upazilaID);
		flag2 = validateInventory(unionID);
		flag3 = validateInventory(popID);
		flag4 = validateDistance(ocDistance);
		flag5 = validateDistance(btclDistance);
		flag6 = validateFileCount();
    	flag = flag1 && flag2 && flag3 && flag4 && flag5 && flag6;
    	return flag;
    }
    var form = $('#fileupload<%=currentActionTypeID%>');
	
	
	$(form).on('submit', function(){
		return validateForm();
	});
    
});
</script>
