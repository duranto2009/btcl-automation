
<%@page import="common.HierarchicalEntityService"%>
<%@page import="common.CategoryConstants"%>
<%@page import="common.EntityDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="common.CommonService"%>
<%@page import="common.CommonDAO"%>
<%@page import="com.ctc.wstx.ent.IntEntity"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.ModuleConstants"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants"%>
<%@ page import="inventory.*, java.util.List"%>
<%
long categoryID=1;
String moduleID = request.getParameter("moduleID");
request.setAttribute("moduleID", moduleID);
if(StringUtils.isNotBlank(request.getParameter("categoryID"))){
	categoryID=Long.parseLong(request.getParameter("categoryID"));
}
Logger searchInventoryLogger = Logger.getLogger("searchInventory_jsp");
InventoryService inventoryService = new InventoryService();
HierarchicalEntityService hierarchicalEntityService = new HierarchicalEntityService();
%>

<%
	String url = "inventory/search";
	String navigator = SessionConstants.NAV_VPN_INVENTORY;
	String context = "" + request.getContextPath() + "/";
%>
<jsp:include page="/includes/inventoryNavAll.jsp" flush="true">
	<jsp:param name="url" value="<%=url%>" />
	<jsp:param name="navigator" value="<%=navigator%>" />
	<jsp:param name ="moduleID" value="<%=moduleID %>"/>
</jsp:include>


<div class="portlet box portlet-btcl light">
	<div class="portlet-body">
		<html:form action="InventoryItemAction" method="POST" styleId="tableForm">
			<input type="hidden" value="<%=categoryID %>" name="categoryID">
			<input type="hidden" name="mode" value="deleteItem"  />
			<div class="table-responsive">
				<table id="sample_1" class="table table-bordered table-striped">
					<thead>
						<tr>
							
							<%
							 	String categoryIdStr=(String)request.getSession(true).getAttribute("categoryID");
							 	Integer categoryId=Integer.parseInt(categoryIdStr);
								List<InventoryCatagoryType>  parentCategoryList= InventoryRepository.getInstance().getInventoryCatagoryTypePathFromRoot(categoryId);
								InventoryCatagoryType invtCatType= InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(categoryId);
								List<InventoryCatagoryTreeNode> invtCatTypeChildList= InventoryRepository.getInstance().getInventoryCatagoryTreeNodeByCatagoryID(categoryId).getInventoryCatagoryDetailsChildNodes();
								boolean isInventoryUsable=(invtCatTypeChildList.size()==0)?true:false;
								List<InventoryAttributeName> attrList = InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(categoryId);
	                     		
							%>
							<th><%=invtCatType.getName()%></th>
							<%
								List<InventoryAttributeName> inventoryAttributeNames =InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(categoryId);
								for(InventoryAttributeName invAttrName: inventoryAttributeNames){ %>
								<th>
									<%=invAttrName.getName() %>
								</th>
							<% } %>
							<%if(parentCategoryList.size()>1){ %>
								<th>
									<%=parentCategoryList.get(parentCategoryList.size()-2).getName() %>
								</th>
							<%} %>
							
							<%if(isInventoryUsable){ %>
								<th>
									Client
								</th>
								<th>
									Used In
								</th>
							<%} %>
								<!-- <th>Edit</th> -->
								<th  class="text-center no-sort">
					        		<input type="checkbox" name="Check_All" value="CheckAll" class="group-checkable pull-left" />
					        		<input class="btn btn-xs btn-danger" type="submit"  value="Delete"/>
					        	</th>
							</tr>
						</thead>
					<%
						ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_VPN_INVENTORY);
						String actionNameForViewingEntity = "";
						String entityName = "";
						
						if (data != null) {
							int size = data.size();
							%>
						<tbody>
							<% 
							for (int i = 0; i < size; i++) {
								InventoryItemDetails inventoryItemDetailsDTO = (InventoryItemDetails) data.get(i);
								entityName = "";
								/*if(inventoryItemDetailsDTO.getInventoryItem().getIsUsed() != null){
									if(inventoryItemDetailsDTO.getInventoryItem().getIsUsed()){
										actionNameForViewingEntity = EntityTypeConstant.entityStrutsActionMap.get(inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityTypeID());
										searchInventoryLogger.debug("actionNameForViewingEntity " + actionNameForViewingEntity);
										searchInventoryLogger.debug("inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityTypeID() " + inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityTypeID());
										searchInventoryLogger.debug("inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityID() " + inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityID());
										if(actionNameForViewingEntity != null && inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityTypeID() > 0 && inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityID() > 0)
										{
											EntityDTO entityDTO = new CommonService().getEntityDTOByEntityIDAndEntityTypeID( inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityTypeID(), inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityID() );
											searchInventoryLogger.debug("entityDTO " + entityDTO);
											if(entityDTO != null)
												entityName = entityDTO.getName();
										}
									}
								}*/
								
							%>
								<tr>
									<td>
										<a href="<%=context%>ViewInventory.do?id=<%=inventoryItemDetailsDTO.getInventoryItem().getID()%>"><%=inventoryItemDetailsDTO.getInventoryItem().getName()%></a>
									</td>
									<%                      			
	                     			for(InventoryAttributeName name: attrList)
	                     			{ 
	                     				String value;
	                     				try{
	                     					value=inventoryItemDetailsDTO.getValueByAttributeName(name.getName());
	                     				}catch(Exception ex){
	                     					value="";
	                     				}
                     				%>
									<td><%= value %></td>
									<%} %>
									
									<%if(parentCategoryList.size()>1){ %>
									<td>
										<a href="<%=context%>ViewInventory.do?id=<%=inventoryItemDetailsDTO.getInventoryItem().getParentID()%>"><%=inventoryItemDetailsDTO.getParentItemName()%></a>
									</td>
									<%} %>
									
									<%if(isInventoryUsable){ 
										boolean itemUsed=inventoryItemDetailsDTO.getInventoryItem().getIsUsed()?true:false;%>
										<td>
										<%if(itemUsed){%>
											<%if(inventoryItemDetailsDTO.getInventoryItem().getOccupierClientID() != null){ %>
											<a href="<%=context%>GetClientForView.do?moduleID=<%=inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityTypeID()/EntityTypeConstant.MULTIPLIER2%>&entityID=<%=inventoryItemDetailsDTO.getInventoryItem().getOccupierClientID()%>">
												<%=AllClientRepository.getInstance().getClientByClientID(inventoryItemDetailsDTO.getInventoryItem().getOccupierClientID()).getLoginName() %>
											</a>
											<%}%>
										<%}%>
										</td>
										<td>
										<%if(itemUsed){%>
											<% if(inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityTypeID()/EntityTypeConstant.MULTIPLIER2 == 99){%>
											<a href="<%=context%>ViewInventory.do?id=<%=inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityID()%>">
												<%=inventoryService.getInventoryItemByItemID(inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityID()).getName() 
												+ " (" + inventoryService.getInventoryItemByItemID(inventoryService.getInventoryItemByItemID(inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityID()).getParentID()).getName() + ")"%>
											</a>
											<%} else{%>
											<a href="<%=context%><%=hierarchicalEntityService.getViewPageByEntityIDAndEntityTypeID(inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityID(), inventoryItemDetailsDTO.getInventoryItem().getOccupierEntityTypeID())%>">
												<%=entityName%>
											</a>
											<%}%>
										<%}else{
											if(inventoryItemDetailsDTO.getInventoryItem().getInventoryCatagoryTypeID() == CategoryConstants.CATEGORY_ID_PORT){%>
												<a href="../../InventoryItemAction.do?itemID=<%=inventoryItemDetailsDTO.getInventoryItem().getID()%>&mode=editItem"> <b>Use Now</b></a>
											<%}
										}%>
										</td>
										<%}%>
										<%-- <td class="text-center"> <a href="${context}InventoryItemAction.do?itemID=<%=inventoryItemDetailsDTO.getInventoryItem().getID() %>&mode=editItem">Edit</a> </td> --%>
									 <td class="text-center">
										 <input  class="checkboxes" type="checkbox" name="deleteIDs" value="<%=inventoryItemDetailsDTO.getInventoryItem().getID() %>" > 
									 </td>
								</tr>
								<% } %>
						<% } %>
						</tbody>
				</table>
			</div>
		</html:form>
	</div>
</div>
<script src="${context}assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
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
			    map['parentItemID'] = parentItemObj.attr('data-category-item-id');
			    map['categoryID'] = categoryObj.val();
			    var url= '../../AutoInventoryItem.do?partialName='+request.term;
			    ajax(url, map, response, "GET", [$(this)]);
				
			},
			minLength : 1,
			select : function(e, ui) {
			    currentAutoComplete.attr('data-category-item-id',ui.item.ID);
			    currentAutoComplete.val(ui.item.name);
			    $(".parentItemID").val(ui.item.ID);
			    $("input[name=parentItemID]").val(ui.item.ID);
			    currentAutoComplete.closest(".form-group").nextAll(".form-group").find(".category-item").val("");
			    currentAutoComplete.closest(".form-group").nextAll(".form-group").find(".category-item").removeAttr('data-category-item-id');
				return false;
			},
	
		}).autocomplete("instance")._renderItem = function(ul, item) {
	    	console.log(item);
	        return $("<li>")
	            .append("<a>" + item.name + "</a>")
	            .appendTo(ul);
		};
    })
    
    var table=$("#sample_1");
    $('.group-checkable', table).change(function() {
        var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
        var checked = $(this).prop("checked");
        $(set).each(function() {
            $(this).prop("checked", checked);
        });
        $.uniform.update(set);
    });
    $('#tableForm').submit(function(e) {
        var currentForm = this;
        var selected=false;
        e.preventDefault();
        var set = table.find('tbody > tr > td:last-child input[type="checkbox"]');
        $(set).each(function() {
        	if($(this).prop('checked')){
        		selected=true;
        	}
        });
        if(!selected){
        	 bootbox.alert("Select Domain Package Type to delete!", function() { });
        }else{
        	 bootbox.confirm("Are you sure  to delete the Inventory Item(s)?", function(result) {
                 if (result) {
                     currentForm.submit();
                 }
             });
        }
    });
    
    $("#form-page-navigation").submit(function(e){
    	var input = $("<input>").attr("type", "hidden").attr("name", "categoryID").val("<%=categoryID%>");
        $(this).append($(input));
        $(this).submit();
    });
	
});
</script>
