<!-- Horizontal Form -->
<%@page import="common.CommonService"%>
<%@page import="inventory.InventoryConstants"%>
<%@page import="common.EntityDTO"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="inventory.InventoryAttributeValuePair"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="inventory.InventoryAttributeValue"%>
<%@page import="inventory.InventoryItemDetails"%>
<%@page import="inventory.InventoryService"%>
<%@page import="inventory.InventoryItem"%>
<%@page import="java.util.Collections"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.ArrayList"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="java.util.List"%>
<%@page import="inventory.repository.InventoryRepository, common.CategoryConstants"%>
<%

Logger logger = Logger.getLogger(getClass());
InventoryItem inventoryItemDTO = (InventoryItem)request.getAttribute("inventoryItemDTO");
InventoryService inventoryService=new InventoryService();
InventoryItem tempItemDTO=null;
CommonService commonService = new CommonService();

//Needed to shift inventory under another parent
// InventoryItem parentItem=null;
// if(inventoryItemDTO.getParentID() != null){
// 	parentItem = inventoryService.getInventoryItemByItemID(inventoryItemDTO.getParentID());
// }


%>
<%try{ %>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-plus-circle"></i>Edit <%=" " + InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(inventoryItemDTO.getInventoryCatagoryTypeID()).getName() + ": "+inventoryItemDTO.getName()%>
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal inventory-add-form" method="post" action="${context}InventoryItemAction.do">
			
			<div class="form-body">
     	   		<input type ="hidden" name="inventoryCatagoryTypeID" value="<%=inventoryItemDTO.getInventoryCatagoryTypeID()%>" />
        		<input type="hidden" name="mode" value="updateItem">
         		<input type="hidden" name="itemID" value="<%=inventoryItemDTO.getID()%>">
           		<%
           		List<InventoryCatagoryType> pathList = InventoryRepository.getInstance().getInventoryCatagoryTypePathFromRoot(inventoryItemDTO.getInventoryCatagoryTypeID());
           		pathList.remove(pathList.size() - 1);
           		
           		List<InventoryItem> inventoryItemDetailsFromRoot = inventoryService.getInventoryItemDetailsFromRootByItemID(inventoryItemDTO.getID());
           		for(int i=0;i<pathList.size();i++){
           		//for(InventoryCatagoryType pathCatType : pathList){ 
           			InventoryCatagoryType pathCatType = pathList.get(i);
           			tempItemDTO = inventoryItemDetailsFromRoot.get(i); //inventoryService.getInventoryItemByCategoryTypeIDAndItemID(pathCatType.getID(), inventoryItemDTO.getID());%>
       			<div class="form-group">
           			<label class="control-label col-md-3 select"> <%=pathCatType.getName()%></label>
           			<div class="col-md-6">                      			
            			<input type="text"class="category-item form-control" name="itemName"  <%if(tempItemDTO!=null){ %> data-category-item-id="<%=tempItemDTO.getID() %>" value="<%=tempItemDTO.getName()%>" <%} %>   disabled />
            			<input type="hidden" class="category-id form-control" value="<%=pathCatType.getID()%>" />
           			</div>
           			<div class="col-md-3 error-div"></div>
       			</div>                      		
       			<%}
           		
    			List<InventoryAttributeName> attrList = InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(inventoryItemDTO.getInventoryCatagoryTypeID());
    			logger.debug("attrList " + attrList);
    			InventoryItemDetails inventoryItemDetailsDTO = inventoryService.getInventoryItemDetailsByItemID(inventoryItemDTO.getID());%>
        		
       			<div class="form-group">
          			<label class="control-label col-md-3"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(inventoryItemDTO.getInventoryCatagoryTypeID()).getName()%></label>
          			<div class="col-md-6">                      			
           				<input type="text" class="form-control" name="itemName" value="<%=inventoryItemDTO.getName() %>" required/>
          			</div>
          			<div class="col-md-3 error-div"></div>
       			</div>
          			
          		<%for(InventoryAttributeName name: attrList){ 
	   				String attributeValue;
	   				long attributeValueID;
	   				try{
	   					attributeValue=inventoryItemDetailsDTO.getValueByAttributeName(name.getName());
	   					attributeValue=StringUtils.trimToEmpty(attributeValue);
	   					attributeValueID=inventoryItemDetailsDTO.getInventoryAttributeValueByAttributeID(name.getID()).getID();
	   				}catch(Exception ex){
	   					attributeValue="";
	   					attributeValueID=-1; // will be handled latter
	   				}%>
            			
       			<div class="form-group">
         			<label class="control-label col-md-3"><%=name.getName()%></label>
					<div class="col-md-6">
              			
              		<!-- d Inventory Attribute Name Distinct Values -->
               		<%if(name.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_TEXT){%>
               			<input type="text" class="form-control" name="attributeValues" value="<%=attributeValue%>" <%if(attributeValueID==-1){ %>readonly <%} %>/>
					<%}else if(name.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_RADIO_BUTTON){%>
               			<div class="radio-list">
							<label class="radio-inline">
							<%for(String attributeTypeName : name.getAttributeTypeNames().split(",")){%>
                				<input type="radio"	name="attributeValues" value="<%=attributeTypeName.trim()%>" 
                				<%=(inventoryItemDetailsDTO.getValueByAttributeName(name.getName()).trim().equals(attributeTypeName.trim())) ? " checked " : ""%> /> <%=attributeTypeName.trim()%>
                				<%}%>
							</label> 
						</div>
						<%if(attributeValueID==-1){ %>
						<script>$('input[name="attributeValues"]:not(:checked)').prop('disabled', true);</script>
               			<%} %>

               		<%}else if(name.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_DRPDOWN){%>
               			<select class="form-control" name="attributeValues" <%if(attributeValueID==-1){ %> readonly <%} %>>
               			<%for(String attributeTypeName : name.getAttributeTypeNames().split(",")){%>
               				<option value="<%=attributeTypeName.trim()%>" 
               				<%=(inventoryItemDetailsDTO.getValueByAttributeName(name.getName()).trim().equals(attributeTypeName.trim())) ? " selected " : ""%>>
				            <%=attributeTypeName.trim()%></option>
               			<%}%>
               			</select>
               			<%if(attributeValueID==-1){ %>
               			<script>$('select[name=attributeValues] option:not(:selected)').prop('disabled', true);</script>
               			<%} %>
               			
               		<%}%>
               		               			
               			<input type="hidden" name="attributeNameIDs" value="<%=attributeValueID%>" />
              		</div>
            	</div>
           		<%}%>
           				
           		
           		<!-- Use Port by Another Port -->
           		<%if(inventoryItemDTO.getInventoryCatagoryTypeID() == CategoryConstants.CATEGORY_ID_PORT){%>	
       				<% 	boolean isPortUsableByInventory = true;
   						String otherPortThatUsesThisPortName = "";
   						String otherPortThatUsesThisPortID = "";
   						String otherPortThatUsesThisPortType = "";
   						String parentRouterOfConnectingPortName = "";
   						String parentRouterOfConnectingPortID = "";
   						String isUsed = "";
       				 	if(inventoryItemDTO.getIsUsed()){
							if(inventoryItemDTO.getOccupierEntityTypeID()/EntityTypeConstant.MULTIPLIER2 != 99){
								isPortUsableByInventory = false;
								EntityDTO entityWhichIsNotAnInventoryItem = commonService.getEntityDTOByEntityIDAndEntityTypeID(inventoryItemDTO.getOccupierEntityTypeID(), inventoryItemDTO.getOccupierEntityID());
	       				 		logger.debug("entityWhichIsNotAnInventoryItem: " + entityWhichIsNotAnInventoryItem);
	       				 		%>
								<div class="form-group">
									<label class="control-label col-md-3">Used In</label>
				          			<div class="col-md-6">                      			
				           				<input type="text" class="form-control" value="<%=(entityWhichIsNotAnInventoryItem!=null)?entityWhichIsNotAnInventoryItem.getName():"-"%>" readonly/>
				          			</div>
				          			<div class="col-md-3 error-div"></div>
			          			</div>
	       				 		<%
							}else{
								InventoryItem inventoryItemUsingPort = inventoryService.getInventoryItemByItemID(inventoryItemDTO.getOccupierEntityID());
								InventoryItemDetails inventoryItemDetailsUsingPort = inventoryService.getInventoryItemDetailsByItemID(inventoryItemUsingPort.getID());
								otherPortThatUsesThisPortName = inventoryItemUsingPort.getName();
								otherPortThatUsesThisPortID = ""+inventoryItemUsingPort.getID();
								otherPortThatUsesThisPortType = inventoryItemDetailsUsingPort.getValueByAttributeName("Port Type");
								isUsed = "1";
								
								parentRouterOfConnectingPortName = inventoryService.getInventoryItemByItemID(inventoryItemUsingPort.getParentID()).getName();
								parentRouterOfConnectingPortID = "" + inventoryService.getInventoryItemByItemID(inventoryItemUsingPort.getParentID()).getID();
								
							}
       				 	}
       				 %>
       				 <%	if(isPortUsableByInventory){
	       					List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(CategoryConstants.CATEGORY_ID_PORT);
	       					InventoryAttributeName portTypeInventoryAttributeName = new InventoryAttributeName();;
	       					for(InventoryAttributeName inventoryAttributeName : inventoryAttributeNames){
	       						if(inventoryAttributeName.getName().equals("Port Type")){
	       							portTypeInventoryAttributeName = inventoryAttributeName;
	       							break;
	       						}
	       					}
       				 %>
						<div class="row" style="padding-bottom: 10px;padding-top: 25px">
							<div class="control-label col-md-3" style="font-weight: bolder;font-size: medium;">Use Port</div><hr>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">Select Router</label>
		          			<div class="col-md-6">                      			
		           				<input type="text" id="connectingRouterIDName" class="form-control" value="<%=parentRouterOfConnectingPortName%>"/>
	          					<input type="hidden" id="connectingRouterID" value="<%=parentRouterOfConnectingPortID%>"/>
		          			</div>
		          			<div class="col-md-3 error-div"></div>
	          			</div>
	          			<div class="form-group">
							<label class="control-label col-md-3">Select Port Type</label>
		          			<div class="col-md-6">                      			
		           				<select id=portAttributeValue class="form-control">
									<%	
									for(String portTypeInventoryAttributeNameType : portTypeInventoryAttributeName.getAttributeTypeNames().split(",")){%>
										<option value="<%=portTypeInventoryAttributeNameType%>" <%=(otherPortThatUsesThisPortType.equals(portTypeInventoryAttributeNameType)) ? "selected" : "" %>>
											<%=portTypeInventoryAttributeNameType%>
										</option>
									<%}%>
								</select>
		          			</div>
		          			<div class="col-md-3 error-div"></div>
	          			</div>
       					<div class="form-group">
       						<label class="control-label col-md-3">Select Port</label>
		          			<div class="col-md-6">                      			
		           				<input type="text" id="connectingPortIDName" class="form-control" value="<%=otherPortThatUsesThisPortName%>"/>
		          				<input type="hidden" id="connectingPortID" value="<%=otherPortThatUsesThisPortID%>" name="occupierEntityID"/>
		          				<input type="hidden" value="<%=isUsed%>" name="isUsed"/>
		           				<input type="hidden" value="<%=EntityTypeConstant.INVENTORY_PORT%>" name="occupierEntityTypeID"/>
		          			</div>
	          				<div class="col-md-3 error-div"></div>
	           			</div>
          			<%}%>
       			<%}%>
       			<!-- Use Port Ends -->
       			
       			
<!--        			//Needed to shift inventory under another parent -->
<!--        			Shift Port Begins -->
<%--        			<%if(inventoryItemDTO.getParentID() != null){ --%>
<!--         				InventoryCatagoryType parentCategoryType = InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(parentItem.getInventoryCatagoryTypeID());  -->

<%--        			%> --%>
<!--        				<div class="row" style="padding-bottom: 10px;padding-top: 25px"> -->
<%-- 						<div class="control-label col-md-3" style="font-weight: bolder;font-size: medium;">Shift <%=" "+InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(inventoryItemDTO.getInventoryCatagoryTypeID()).getName() %></div><hr> --%>
<!-- 					</div> -->
<!-- 					<div class="form-group"> -->
<%-- 						<label class="control-label col-md-3">Select <%=" "+parentCategoryType.getName() %></label> --%>
<!-- 	          			<div class="col-md-6">                      			 -->
<%-- 	           				<input type="text" id="parentName" class="form-control" value="<%=parentItem.getName()%>" required/> --%>
<%--           					<input type="hidden" name="parentItemID" value="<%=parentItem.getID()%>" pattern="[0-9]*"/> --%>
<!-- 	          			</div> -->
<!-- 	          			<div class="col-md-3 error-div"></div> -->
<!--           			</div> -->
<%--        			<%}%> --%>
<!--        			Shift Port Ends -->
       			
       			
       			
       			
            </div>
           	
           	<div class="form-actions text-center">
               	<div class="row">
					<div class="col-md-10">
						<button name="B1" value="Reset" type="button" class="btn  btn-sm btn btn-circle  grey-mint btn-outline sbold uppercase ">Reset</button>
						<button name="B2" type="submit" class="submit btn btn-sm  btn-circle green-meadow  sbold uppercase" >Update</button>
					</div>
				</div>
			</div>	
		</form>  
	</div>
</div>
<%}catch(Exception ex){ %>
	<%=ex.toString() %>
<%} %>
<script src="${context}assets/scripts/inventory/inventory-add.js" type="text/javascript"></script>

<script>
$(document).ready(function(){
	
	
	//Use Port by Router
	$("#connectingRouterIDName").autocomplete({
		source: function(request, response) {
			callAjax('../../AutoInventoryItem.do?categoryID=5&partialName='+request.term, {isParentNeeded : "false"}, response, "GET");
			$("#connectingPortIDName").val("");
			$("#connectingPortID").val("");
			$("input[name='isUsed']").val(0);
	    },
	    minLength: 1,
	    select: function(e, ui) {
	    	$("#connectingRouterIDName").val(ui.item.name);
	    	$("#connectingRouterID").val(ui.item.ID);
	       return false;
	    },
	 }).autocomplete("instance")._renderItem = function(ul, item) {
	    return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
	 }
	
	$("#connectingPortIDName").autocomplete({
	    source: function(request, response) {
	    	if(parseInt($("#connectingRouterID").val()) > 0){
	    		callAjax('../../AutoInventoryItem.do?categoryID=99&partialName='+request.term+'&parentItemID='+$("#connectingRouterID").val(), 
	    				{attributeName : "Port Type", attributeValue : $("#portAttributeValue").val()}, response, "GET")
	    	}else{
	    		toastr.warning("Select Router First");
	    	}
	    },
	    minLength: 1,
	    select: function(e, ui) {
	    	$("#connectingPortIDName").val(ui.item.name);
	    	$("#connectingPortID").val(ui.item.ID);
	    	$("input[name='isUsed']").val(1);
	       return false;
	    },
	 }).autocomplete("instance")._renderItem = function(ul, item) {
	    return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
	 }
	
	function handleWritingOnUserInventoryName(){
		if($(this).attr("id") == "connectingRouterIDName"){
			if($("#connectingRouterIDName").val().length == 0){
				$("#connectingRouterID").val("");
				$("#connectingPortIDName").val("");
				$("#connectingPortID").val("");
				$("input[name='isUsed']").val(0);
			}
		}else{
			if($("#connectingPortIDName").val().length == 0){
				$("#connectingPortID").val("");
				$("input[name='isUsed']").val(0);
			}
		}
		
	}
	
	$("#connectingRouterIDName, #connectingPortIDName").blur(handleWritingOnUserInventoryName).change(handleWritingOnUserInventoryName).keyup(handleWritingOnUserInventoryName).keydown(handleWritingOnUserInventoryName);
	
	
	//Needed to shift inventory under another parent
// 	//Shift Inventory
<%-- 	<%if(inventoryItemDTO.getParentID() != null){%> --%>
// 	$("#parentName").autocomplete({
// 		source: function(request, response) {
<%-- 			callAjax('../../AutoInventoryItem.do?categoryID=<%=parentItem.getInventoryCatagoryTypeID()%>&partialName='+request.term, {isParentNeeded : "false"}, response, "GET"); --%>
// 			$("input[name=parentItemID]").val("");
// 	    },
// 	    minLength: 1,
// 	    select: function(e, ui) {
// 	    	$("#parentName").val(ui.item.name);
// 	    	$("input[name=parentItemID]").val(ui.item.ID);
// 	       return false;
// 	    },
// 	 }).autocomplete("instance")._renderItem = function(ul, item) {
// 	    return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
// 	 }
<%-- 	<%}%> --%>
	
});
</script>
