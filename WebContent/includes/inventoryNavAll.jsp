<%@page import="inventory.InventoryCatagoryTreeNode"%>
<%@page import="java.util.Collections"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" %>
<%@ page import="util.RecordNavigator"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="java.util.ArrayList"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="java.util.List, java.util.stream.Collectors"%>
<%@page import="inventory.repository.InventoryRepository"%>
<%
	System.out.println("Inside nav.jsp");
	String url = request.getParameter("url");
	String navigator = request.getParameter("navigator");
	String moduleID = request.getParameter("moduleID");
	String pageno = "";

	RecordNavigator rn = (RecordNavigator)session.getAttribute(navigator);
	pageno = ( rn == null ) ? "1" : "" + rn.getCurrentPageNo();

	System.out.println("rn " + rn);
	
	String action = "/" + url;
	String context = request.getContextPath() + "/";
	String link = context + url + ".do";
	String category_id = (String)session.getAttribute("categoryID");
	if(category_id!=null){
		link += "?categoryID=" + category_id;
	}
	if(moduleID != null) {
		link += "&moduleID=" + moduleID;
	}
	String searchFieldInfo[][] = rn.getSearchFieldInfo();
	String totalPage = "1";
	if(rn != null)
		totalPage = rn.getTotalPages() + "";

%>
<style>
.kay-group > input 
{
	margin:5px;
}
</style>

<%

String categoryIDStr=(String)request.getSession(true).getAttribute("categoryID");
Integer categoryID=0;
if(StringUtils.isNotBlank(categoryIDStr)){
	 categoryID=Integer.parseInt(categoryIDStr);
}


%>
<!-- search control -->
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-search-plus"></i>Search
		</div>
		<div class="tools">
			<a class="collapse" href="javascript:;" data-original-title=""
				title=""> </a>
		</div>
	</div>
	<div class="portlet-body " style="padding: 20px;">
		<!-- BEGIN FORM-->
		<div class="tabbable-custom form " >
			
				<ul class="nav nav-tabs ">
					<%
                 	List<InventoryCatagoryType> catList = InventoryRepository.getInstance().getAllInventoryCategory();
					Collections.sort(catList);
                 	for(int i = 0; i < catList.size();i++){
                 		InventoryCatagoryType catdto =	catList.get(i);
                 	%>
					<li <%if(catdto.getID().equals(categoryID)){%> class="active" <%}%>>
						<a
						href="../../inventory/search.do?categoryID=<%=catdto.getID() %>&moduleID=<%=moduleID%>">
							<%=catdto.getName()%>
					</a>
					</li>
					<%}%>

				</ul>
				<div class="tab-content" style="padding: 0px;">
					<div id="tab_1_1_<%=categoryID %>" class="tab-pane active ">
					<!--form action="#" class="form-horizontal">
						<%for(int i = 0; i < catList.size();i++){
	               	  InventoryCatagoryType catdto =catList.get(i);
	                 %>
						<%if(!catdto.getID().equals(categoryID)) {
                    			continue;
                    		}
	                    		%>
						<%
		                      		List<InventoryCatagoryType> pathList = InventoryRepository.getInstance().getInventoryCatagoryTypePathFromRoot(catdto.getID());
		                      		for(InventoryCatagoryType pathCatType : pathList)
		                      		{ 
		                      		
		                      		%>
						<div class="form-group">
							<div class="col-md-12">
								<label class="control-label col-md-2 select">Select <%=pathCatType.getName()%></label>
								<div class="col-md-4">
									<input type="text" class="category-item form-control" name="partialName" />
									<input type="hidden" class="category-id form-control" value="<%=pathCatType.getID()%>" />
								</div>
							</div>
						</div>
						<%
                      		}
		                      		
		                      		
		                      		
		                      		
		                      		
                      		%>
						<%}%>
						
						
</form-->
						<form action="<%=link%>" method="POST" class="form-horizontal">
							<div class="form-body">
							<%
								if(searchFieldInfo != null && searchFieldInfo.length > 0)
								{
								%>
									<% for(int i = 0; i < searchFieldInfo.length;i++) { %>
										<%if(i % 2 == 0){%>
										<%}%>
										<div class="form-group">
											<div class="col-md-12">
												<label for="" class="control-label col-md-2 "><%=searchFieldInfo[i][0]%></label>
												<div class="col-md-4">
													<input type="text" class="category-item form-control" id="" placeholder="" 	name="<%=searchFieldInfo[i][1]%>"
														<%
												    String value = (String)session.getAttribute(searchFieldInfo[i][1]);
												    session.removeAttribute(searchFieldInfo[i][1]);
												    if( value != null){%>
														value="<%=value%>" <%}%>>
													<input type="hidden" class="category-id form-control" value="<%=searchFieldInfo[i][1] %>" />
												</div>
											</div>
										</div>
										<%if(i % 2 == 1){%>
										<%} %>
									<%}%>
									
									<%
									List<InventoryAttributeName> inventoryAttributeNames =  InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(categoryID);
									for(InventoryAttributeName inventoryAttributeName: inventoryAttributeNames){%>
										<div class="form-group">
										<div class="col-md-12">
											<label for="" class="control-label col-md-2 "><%=inventoryAttributeName.getName()%></label>
											<div class="col-md-4">
										<%if(inventoryAttributeName.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_TEXT){%>
											<input type="text" class="category-item form-control" name="<%=inventoryAttributeName.getID()%>">
										
										<%}else if(inventoryAttributeName.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_RADIO_BUTTON){ %>
											<div class="radio-list">
												<label class="radio-inline">
													<%for(String attributeTypeName : inventoryAttributeName.getAttributeTypeNames().split(",")){%>
				                      					<input type="radio"	name="<%=inventoryAttributeName.getID() %>" value="<%=attributeTypeName.trim()%>"/> <%=attributeTypeName.trim()%>
				                      				<%}%>
												</label> 
											</div>
										
										<%}else if(inventoryAttributeName.getAttributeType() == InventoryAttributeName.ATTRIBUTE_TYPE_DRPDOWN){ %>
											<select class="form-control" name="<%=inventoryAttributeName.getID()%>">
			                      				<option value="">Any</option>
			                      				<%for(String attributeTypeName : inventoryAttributeName.getAttributeTypeNames().split(",")){%>
			                      					<option value="<%=attributeTypeName.trim()%>"><%=attributeTypeName.trim()%></option>
			                      				<%}%>
			                      			</select>
										<%}%>
										<input type="hidden" class="category-id form-control" value="<%=inventoryAttributeName.getID()%>" />
											</div>
										</div>
									</div>
									<%}%>
									
									
									<!-- d To search isUsed -->
									<%
									List<InventoryCatagoryTreeNode> invtCatTypeChildList= InventoryRepository.getInstance().getInventoryCatagoryTreeNodeByCatagoryID(categoryID).getInventoryCatagoryDetailsChildNodes();
									boolean isUsable=(invtCatTypeChildList.size()==0)?true:false;
									
									if(isUsable){%>
										<div class="form-group">
										<div class="col-md-12">
											<label for="" class="control-label col-md-2 ">Usage</label>
											<div class="col-md-4">
												<select name="isUsed" class="form-control">
													<option value="">All</option>
													<option value="0" 
													<% if(session.getAttribute("isUsed") !=null){
														if(session.getAttribute("isUsed").equals("0") ){
															%> selected <% 
															session.removeAttribute("isUsed");
														}
													}%>
													>Not Used</option>
													<option value="1"
													<% if(session.getAttribute("isUsed") !=null){
														if(session.getAttribute("isUsed").equals("1") ){
															%>selected<% 
															session.removeAttribute("isUsed");
														}
													}%>
													>Used</option>
												</select>
											</div>
										</div>
									</div>
									<%}%>
									<!--  -->
									
								<%} %>

								<%if(searchFieldInfo.length % 2 == 0){%>
								<%}%>
								</div>
								<div class="form-actions fluid">
									<div class="row">
										<div class="col-xs-offset-0 col-sm-offset-2 col-md-offset-3 col-xs-12 col-md-9">
											<label for="" class="control-label col-xs-3 col-sm-3 col-md-3">Records Per Page</label>
											<div class="col-xs-3  col-sm-3 col-md-2">
												<input type="text" class="custom-form-control" name="RECORDS_PER_PAGE" id="" placeholder="" value="<%=rn.getPageSize()%>">
											</div>
											<div class="col-xs-6  col-sm-5  col-md-4">					
											 	<input type=hidden name="search" value="yes" />
<!-- 									          	<input type="reset" class="btn  btn-sm btn btn-circle  grey-mint btn-outline sbold uppercase" value="Reset" > -->
										        <input type="submit" class="btn  btn-sm btn btn-circle btn-sm green-meadow btn-outline sbold uppercase" value="Search" >
									        </div>
										</div>
									</div>
								</div>
								<input type="hidden" name="categoryID" value="<%=categoryID%>">
						</form>
					</div>
				<!-- END FORM-->
			</div>
		</div>
	</div>
</div>

<div class="portlet box portlet-btcl light">
	<div class="portlet-body">
		<div class="row text-center">
			<form action="<%=link%>" method="POST" class="form-inline" id = "form-page-navigation">
				<nav aria-label="Page navigation" >
				  <ul class="pagination" style="margin: 0px;">
				   <li class="page-item">
				      <a class="page-link" href="<%=link%>&id=first" aria-label="First"  title="Left">
				        <i class="fa fa-angle-double-left" aria-hidden="true"></i>
				        <span class="sr-only">First</span>
				      </a>
				    </li>
				    <li class="page-item">
				      <a class="page-link" href="<%=link%>&id=previous" aria-label="Previous" title="Previous">
				         <i class="fa fa-angle-left" aria-hidden="true"></i>
				        <span class="sr-only">Previous</span>
				      </a>
				    </li>
				
				     <li class="page-item">
				      <a class="page-link" href="<%=link%>&id=next" aria-label="Next" title="Next">
				         <i class="fa fa-angle-right" aria-hidden="true"></i>
				        <span class="sr-only">Next</span>
				      </a>
				    </li>
				    <li class="page-item">
				      <a class="page-link" href="<%=link%>&id=last" aria-label="Last"  title="Last">
				        <i class="fa fa-angle-double-right" aria-hidden="true"></i>
				        <span class="sr-only">Last</span>
				      </a>
				    </li>
				    <li>
				    	&nbsp;&nbsp;<i class="hidden-xs">Page </i><input type="text" class="custom-form-control " name="pageno" value='<%=pageno%>' size="15"> <i class="hidden-xs">of</i>
						<%=rn.getTotalPages()%>
						<input type=hidden name="go" value="yes" />
						<input type="submit" class="btn btn-circle  btn-sm green-haze btn-outline sbold uppercase" value="GO"/>
				    </li>
				  </ul>
				</nav>
			</form>
		</div>
	</div>
</div>

