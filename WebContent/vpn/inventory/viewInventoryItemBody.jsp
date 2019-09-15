<%@page import="common.CommonService"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.PermissionConstants"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.CommonDAO"%>
<%@page import="common.EntityDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.CategoryConstants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="inventory.InventoryItemDetails"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="java.util.List"%>
<%@page import="inventory.InventoryService"%>
<%@page import="inventory.InventoryItem"%>
<%@ page import="common.repository.AllClientRepository" %>
<%
InventoryItem inventoryItem = (InventoryItem)request.getAttribute("inventoryItem");
InventoryService inventoryService=new InventoryService();
InventoryItem tempItemDTO=null;
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption"><i class="fa fa-user"></i>Info</div>
		<%LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
		if(loginDTO.getMenuPermission(PermissionConstants.VPN_INVENTORY_ADD) >= PermissionConstants.PERMISSION_FULL){%>
			<div class="actions">
		 		<a href="../../InventoryItemAction.do?itemID=<%=inventoryItem.getID()%>&mode=editItem" class="btn btn-edit-btcl" style="padding-left: 15px; padding-right: 15px; "> Edit </a>
		 	</div>
		 <%}%>
	</div>
	<div class="portlet-body " id="printContent">
		<div class="table-responsive">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th class="text-center" colspan="2"><h3><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(inventoryItem.getInventoryCatagoryTypeID()).getName() + ": "%><b><%=inventoryItem.getName()%></b></h3></th>
					</tr>
				</thead>
					<tbody>
					           		<%
           		List<InventoryCatagoryType> pathList = InventoryRepository.getInstance().getInventoryCatagoryTypePathFromRoot(inventoryItem.getInventoryCatagoryTypeID());
           		pathList.remove(pathList.size() - 1);
           		
           		for(InventoryCatagoryType pathCatType : pathList){ 
           			tempItemDTO=inventoryService.getInventoryItemByCategoryTypeIDAndItemID(pathCatType.getID(), inventoryItem.getID());%>
           			<tr>
						<th scope="row"><%=pathCatType.getName()%></th>
						<td><%=tempItemDTO.getName()%></td>
					</tr>
       			<%}
           		
    			List<InventoryAttributeName> attrList = InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(inventoryItem.getInventoryCatagoryTypeID());
    			InventoryItemDetails inventoryItemDetailsDTO = inventoryService.getInventoryItemDetailsByItemID(inventoryItem.getID());%>
        		
        			<tr>
						<th scope="row"><%=InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(inventoryItemDetailsDTO.getInventoryItem().getInventoryCatagoryTypeID()).getName() %></th>
						<td><%=inventoryItem.getName()%></td>
					</tr>
          			
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
	   				
	   				<tr>
						<th scope="row"><%=name.getName()%></th>
						<td><%=attributeValue%></td>
					</tr>
           		<%}%>
           				
           		
           		<!-- Use Port by Another Port -->
           		<%if(inventoryItem.getInventoryCatagoryTypeID() == CategoryConstants.CATEGORY_ID_PORT){
           			if(inventoryItem.getIsUsed()){
           				if(inventoryItem.getOccupierEntityTypeID()/EntityTypeConstant.MULTIPLIER2 != 99){
           					
           					EntityDTO entityWhichIsNotAnInventoryItem = new CommonService().getEntityDTOByEntityIDAndEntityTypeID(inventoryItem.getOccupierEntityTypeID(), inventoryItem.getOccupierEntityID() );%>
           					<tr>
	    						<th scope="row">Used In</th>
	    						<td><%=(entityWhichIsNotAnInventoryItem!=null)?entityWhichIsNotAnInventoryItem.getName():"-"%></td>
    						</tr>
           				<%}	else{
           					InventoryItem inventoryItemUsingPort = inventoryService.getInventoryItemByItemID(inventoryItem.getOccupierEntityID());
							String parentRouterOfConnectingPortName = inventoryService.getInventoryItemByItemID(inventoryItemUsingPort.getParentID()).getName();%>
							<tr>
	    						<th scope="row">Used In</th>
	    						<td><%=parentRouterOfConnectingPortName + " > " + inventoryItemUsingPort.getName()%></td>
    						</tr>
           				<%}
           			}
           		}
           		%>	

					</tbody>
			</table>
		</div>
	</div>
</div>
<div id="used-in">
	<btcl-portlet title="Used In" subtitle="Inventory Item" v-if="isApplicable">
		<li v-for ="item in usedPlaces">
			{{item.moduleId}} {{item.count}}
		</li>
	</btcl-portlet>
</div>

<script>
	new Vue({
		el:'#used-in',
		data: {
			usedPlaces : [],
			itemId: <%=inventoryItem.getID()%>,
			isApplicable :<%=inventoryItem.getInventoryCatagoryTypeID() != 1 && inventoryItem.getInventoryCatagoryTypeID() != 2 && inventoryItem.getInventoryCatagoryTypeID() != 3 %>,
			clients : [],
			clientMap : {}

		},
		mounted() {
			axios.get(context + 'lli/inventory/get-usage.do?itemId=' + this.itemId)
					.then(result=>{
						if(result.data.responseCode === 1){
							this.usedPlaces = result.data.payload;
							this.usedPlaces.forEach((item)=>{
								this.clients.push({
									key : item.clientId,
									value : item.moduleId
								});
							});
							axios.post(context + 'Client/get-all-by-id-module.do', {
								list : this.clients
							}).then(res=>{
								this.clientMap = res.data.responseCode === 1? result.data.payload: errorMessage(res.data.msg);
							}).catch(err=>console.log(err));
						}else {
							errorMessage(result.data.msg);
						}
					}).catch(err=> console.log(err))

		},
	});
</script>
