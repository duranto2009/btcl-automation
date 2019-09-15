<!-- Horizontal Form -->
<%@page import="java.util.Map"%>
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
String currentItemTab=(String)request.getSession().getAttribute("currentItemTab");
InventoryItem inventoryItem = (InventoryItem)request.getSession().getAttribute("inventoryItem");
InventoryService inventoryService=new InventoryService();
InventoryItem tempItemDTO=null;

request.getSession().removeAttribute("inventoryItem");
request.getSession().removeAttribute("currentItemTab");
Integer moduleID = (Integer)request.getAttribute( "moduleID" );

Map<Integer, InventoryItem> fromRootToItemPathMap=null;
%>

<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-plus-circle"></i>New Inventory Item Add
		</div>
	</div>
	<div class="portlet-body form">
			<div class="form-body">
			<div class="tabbable-custom ">
                  <ul class="nav nav-tabs ">
                  	<%
                  	List<InventoryCatagoryType> catList = InventoryRepository.getInstance().getAllInventoryCategory();
                	Collections.sort(catList);
                	boolean isSubmitDisabled=true;
                  	for(int i = 0; i < catList.size();i++){
                  		InventoryCatagoryType catdto =	catList.get(i);
                  		isSubmitDisabled=true;
                  	%>
                      <li <%if(i==0){%>class="active"<%}%>>
                          <a href="#tab_1_1_<%=i%>" data-toggle="tab" id="y_1_1_<%=i%>"> <%=catdto.getName()%> </a>
                      </li>
                   	<%}%>
                      
                  </ul>
                  <div class="tab-content" style="padding: 0px;">
                  <%for(int i = 0; i < catList.size();i++){
                	  try{
                	  InventoryCatagoryType catdto =catList.get(i);
                  %>
                   <div class="tab-pane <%if(i==0){%>active<%}%>" id="tab_1_1_<%=i%>">
                  		<form class="form-horizontal inventory-add-form" method="post" action="../../AddInventory.do">
                  			
                  			<%if( request.getAttribute( "moduleID" ) != null ){ %>
                  				<input type="hidden" name="moduleID" value="<%=moduleID %>" />
                  			<%} %>
                  			
	                     	<input type ="hidden" name="inventoryCategoryID" value="<%=catdto.getID()%>" />
                    		<div class="form-body">
	                      		<%
	                      		
		                      		List<InventoryCatagoryType> pathList = InventoryRepository.getInstance().getInventoryCatagoryTypePathFromRoot(catdto.getID());
		                      		pathList.remove(pathList.size() - 1);
		                      		if(inventoryItem!=null){
		                      			fromRootToItemPathMap= inventoryService.getInventoryParentItemPathMapUptoRootByItemID(inventoryItem.getID());
		                      		}
                      				for(InventoryCatagoryType pathCatType : pathList)
		                      		{ 
		                      			if(inventoryItem!=null){
		                      				if((inventoryItem.getInventoryCatagoryTypeID().equals(catdto.getID()))
		                      						&&(pathCatType.getID().intValue()<=inventoryItem.getInventoryCatagoryTypeID().intValue())){
	                      						tempItemDTO=fromRootToItemPathMap.get(pathCatType.getID());//inventoryService.getInventoryItemByCategoryTypeIDAndItemID(pathCatType.getID(), inventoryItem.getID());
	                      						isSubmitDisabled=false;
		                      				}else{
		                      					tempItemDTO=null;
		                      					isSubmitDisabled=true;
		                      				}
	                      				}
                      			%>
		                      			<div class="form-group">
			                      			<div class="col-md-12">
				                      			<label class="control-label col-md-2 select">Select <%=pathCatType.getName()%></label>
				                      			<div class="col-md-4">                      			
					                      			<input type="text"class="category-item form-control" name="partialName"  <%if(tempItemDTO!=null){ %> data-category-item-id="<%=tempItemDTO.getID() %>" value="<%=tempItemDTO.getName()%>" <%} %>    />
					                      			<input type="hidden" class="category-id form-control" value="<%=pathCatType.getID()%>" />
				                      			</div>
				                      			<div class="col-md-4 error-div">
				                      				
				                      			</div>
			                      			</div>
		                      			</div>                      		
                      			<% 
                     			 	} 
	                      			if(inventoryItem==null ){
                  						isSubmitDisabled=true;
                  					}
                      				if(pathList.size()==0 ){
                      					isSubmitDisabled=false;
                      				}
	                      		%>
	                      		<%
	                      			List<InventoryAttributeName> attrList = InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(catdto.getID());
	                      			logger.debug("attrList " + attrList);
	                      			%>
	                      			<div class="form-group">
		                      			<div class="col-md-12">
			                      			<label class="control-label col-md-2"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(catdto.getID()).getName() %></label>
			                      			<div class="col-md-4">                      			
				                      			<input type="text" class="form-control" name="itemName" required/>
			                      			</div>
			                      			<div class="col-md-4 error-div">
			                      			</div>
		                      			</div>
	                      			</div>
	                      			<%                      			
	                      			for(InventoryAttributeName name: attrList)
	                      			{ %>
	                      			
	                      			<div class="form-group">
		                      			<div class="col-md-12">
			                      			<label class="control-label col-md-2"><%=name.getName()%></label>
			                      			<div class="col-md-4">
			                      			
			                      			<!-- d Inventory Attribute Name Distinct Values -->
			                      			
			                      				<%if(name.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_TEXT){%>
				                      			<input type="text" class="form-control" name="attributeValues"/>
				                      			
				                      			<%}else if(name.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_RADIO_BUTTON){%>
				                      			<div class="radio-list">
													<label class="radio-inline">
														<%for(String attributeTypeName : name.getAttributeTypeNames().split(",")){%>
					                      					<input type="radio"	name="attributeValues" value="<%=attributeTypeName.trim()%>"/> <%=attributeTypeName.trim()%>
					                      				<%}%>
													</label> 
												</div>
				                      			
				                      			<%}else if(name.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_DRPDOWN){%>
				                      			<select class="form-control" name="attributeValues">
				                      				<%for(String attributeTypeName : name.getAttributeTypeNames().split(",")){%>
				                      					<option value="<%=attributeTypeName.trim()%>"><%=attributeTypeName.trim()%></option>
				                      				<%}%>
				                      			</select>
				                      			<%}%>
				                      			<input type="hidden" name="attributeNameIDs" value="<%=name.getID()%>"/>
			                      			</div>
		                      			</div>
	                      			</div>
                      				<%} %>
	                      	</div>
	                      	
			                <div class="form-actions text-center">
			                	<div class="row">
									<div class="col-md-8">
										<button name="B1" value="Reset" type="button" class="btn  btn-sm btn btn-circle  grey-mint btn-outline sbold uppercase ">Reset</button>
										<button name="B2" type="submit" class="submit btn btn-sm  btn-circle green-meadow  sbold uppercase" <%if(isSubmitDisabled){ %>disabled <%} %>>Submit</button>
									</div>
								</div>
								<div class="col-md-offset-2 col-md-10">
									<input type="hidden" name="parentcategoryID" class="parentcategoryID"  value="<%=catdto.getParentCatagoryTypeID()%>" />
									<input type="hidden" name="categoryID"  value="<%=catdto.getID()%>"/>
	                      			<input type="hidden" name ="parentItemID" class="parentItemID" <%if(inventoryItem!=null){%> value="<%=inventoryItem.getParentID() %>" <%} %>>
	                      			<input type="hidden" name ="currentItemTab" value="<%=currentItemTab %>" >
								</div>
							</div>	
                      	</form>
               	      </div>
               	      <%}catch(Exception ex){
               	      ex.printStackTrace();
               	      %>
               	      <%}%>
                  <%}%>  
                  </div>
             </div>
		</div>
	</div>
</div>
<script src="${context}assets/scripts/inventory/inventory-add.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
    var rootCategory=<%=CategoryConstants.CATEGORY_ID_DISTRICT%>;
    var currentAutoComplete;
    $(".category-item").each(function(){
		$(this).autocomplete({
			source : function(request, response) {
			    var map = {};
			    currentAutoComplete=this.element;
			    var categoryObj= currentAutoComplete.next(".category-id");
			    var parentItemObj = currentAutoComplete.closest(".form-group").prev(".form-group").find(".category-item");
			    map['parentItemID'] = parentItemObj.attr('data-category-item-id');
			    map['categoryID'] = categoryObj.val();
			    if(map['categoryID']!= rootCategory && typeof map['parentItemID'] == 'undefined'){
					this.element.val("");
					toastr.error("Please select parent item");
					return;
			    }
			    var url='../../AutoInventoryItem.do?partialName='+request.term;
			    callAjax(url, map, response, "GET" );
			},
			minLength : 1,
			select : function(e, ui) {
			    currentAutoComplete.attr('data-category-item-id',ui.item.ID);
			    currentAutoComplete.val(ui.item.name);
			    currentAutoComplete.closest(".form-group").nextAll(".form-group").find(".category-item").val("");
			    currentAutoComplete.closest(".form-group").nextAll(".form-group").find(".category-item").removeAttr('data-category-item-id');
			    var lastParent= currentAutoComplete.next('input.category-id');
			    var aLastParent=currentAutoComplete.closest('form').find('.parentcategoryID');
			    if(lastParent.val()==aLastParent.val()){
					currentAutoComplete.closest('form').find(".parentItemID").val(ui.item.ID);
					currentAutoComplete.closest('form').find('.submit').prop('disabled', false); 
			    }else{
					currentAutoComplete.closest('form').find('.submit').prop('disabled',true);
			    }
				return false;
			},
	
		}).autocomplete("instance")._renderItem = function(ul, item) {
	    	console.log(item);
	        return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
		};
    })
    
    var currentItemTab="<%=currentItemTab%>";
    $('.nav-tabs a').click(function(){
    	$('input[name=currentItemTab]').val($(this).attr('href'));
    })
    $('.nav-tabs a[href="' + currentItemTab + '"]').tab('show');
    
});
</script>