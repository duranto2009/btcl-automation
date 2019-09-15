<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="inventory.InventoryCatagoryTreeNode"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.ArrayList"%>
<%@page import="inventory.InventoryService"%>

<%
Logger logger = Logger.getLogger("update_inventory_config_jsp");
int reqType =  (Integer.parseInt(request.getParameter("type")));
if(reqType == 2)
{
	int category_id = (Integer.parseInt(request.getParameter("catagoryID")));
	String[] attribute_names = (String[]) request.getParameterValues("attributeList[]");
	
	ArrayList<String> list = new ArrayList<String>();
	for(int i = 0; i < attribute_names.length;i++)
	{
		//logger.debug("names " + attribute_names[i]);
		list.add(attribute_names[i]);
		
	}
	logger.debug("category_id " + category_id);
	logger.debug("list " + list);
	new InventoryService().updateInventoryAttributeName(category_id, list);
}
else if(reqType == 1)//addchild category
{
	int parent_id = (Integer.parseInt(request.getParameter("parentID")));	
	String childCatName = request.getParameter("childCatName");
	InventoryCatagoryType catType = new InventoryCatagoryType();
	if(parent_id > 0){
		catType.setParentCatagoryTypeID(parent_id);
	}
	catType.setName(childCatName);
	catType.setLastModificationTime(System.currentTimeMillis());
	new InventoryService().addCatagory(catType);
	response.setContentType("application/json");
	String result = new Gson().toJson(catType.getID());
	response.getWriter().write(result);
}
else if(reqType == 10)//addchild category (not root)
{
	int cat_id = (Integer.parseInt(request.getParameter("catID")));	
	new InventoryService().deleteTreeNodeInCasecadeModeByCatagoryID(cat_id);
	response.setContentType("application/json");
	String result = new Gson().toJson(cat_id);
	response.getWriter().write(result);
}
%>