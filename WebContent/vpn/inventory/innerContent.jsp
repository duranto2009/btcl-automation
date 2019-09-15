
<%@page import="inventory.repository.InventoryRepository"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.List"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="inventory.InventoryCatagoryDetails"%>
<%@page import="inventory.InventoryCatagoryTreeNode"%>
<%!
Logger logger = Logger.getLogger("contentLoader_jsp");
public void generateTree(java.io.PrintWriter out, List<InventoryCatagoryTreeNode> rootNodeIDList)
{	
	try
	{
	//System.out.println("rootNodeIDList " + rootNodeIDList);
	if(rootNodeIDList ==  null || rootNodeIDList.size() == 0)
	{
		return;
	}
	for(int i = 0; i < rootNodeIDList.size(); i++)
	{	
		//System.out.println("Hello");
		InventoryCatagoryTreeNode node = rootNodeIDList.get(i);
		
		//logger.debug("rootNodeIDList.get("+i+") " + rootNodeIDList.get(i));
		
		InventoryCatagoryDetails categoryDetails = node.getInventoryCatagoryDetailsRootNode();	
		InventoryCatagoryType categoryType = categoryDetails.getInventoryCatagoryType();
		//logger.debug("name"+categoryType.getName());
		//logger.debug("catagoryID"+categoryType.getID());
		
		//out.println("<button>"+categoryType.getName()+"</button>");
		//String escaped = escapeHtml4(abc);
		//out.println("<div class=\"panel panel-default\" id=\"panel1\"><div class=\"panel-heading\" role=\"tab\" id=\"heading1\"><h4 class=\"panel-title\"><a class=\"\" id=\"panel-lebel1\" role=\"button\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse1\" aria-expanded=\"true\" aria-controls=\"collapse1\"> abc <\/a><div class=\"actions_div\" style=\"position: relative; top: -26px;\"><a href=\"#\" accesskey=\"1\" class=\"remove_ctg_panel exit-btn pull-right\"><span class=\"glyphicon glyphicon-remove\"><\/span><\/a><a href=\"#\" accesskey=\"1\" class=\"edit_ctg_label pull-right\"><span class=\"glyphicon glyphicon-edit \"><\/span>&nbsp;Edit<\/a><a href=\"#\" accesskey=\"1\" class=\"pull-right\" id=\"addButton2\"> <span class=\"glyphicon glyphicon-plus\"><\/span>&nbsp; Add child category<\/a><\/div><\/h4><\/div><div id=\"collapse1\" class=\"panel-collapse collapse in\" role=\"tabpanel\" aria-labelledby=\"heading1\"><div class=\"panel-body\"><div id=\"TextBoxDiv1\"><\/div><a class=\"btn btn-xs btn-primary\" accesskey=\"1\" id=\"addButton3\"><span class=\"glyphicon glyphicon-plus\"><\/span> Add New Attributes<\/a><a class=\"btn btn-xs btn-success\" accesskey=\"1\" id=\"ajax_submit_button\">Done<\/a><\/div><\/div><\/div>");
		//out.println("&lt;div class=&quot;panel panel-default&quot; id=&quot;panel1&quot;&gt;&lt;div class=&quot;panel-heading&quot; role=&quot;tab&quot; id=&quot;heading1&quot;&gt;&lt;h4 class=&quot;panel-title&quot;&gt;&lt;a class=&quot;&quot; id=&quot;panel-lebel1&quot; role=&quot;button&quot; data-toggle=&quot;collapse&quot; data-parent=&quot;#accordion&quot; href=&quot;#collapse1&quot; aria-expanded=&quot;true&quot; aria-controls=&quot;collapse1&quot;&gt; abc &lt;/a&gt;&lt;div class=&quot;actions_div&quot; style=&quot;position: relative; top: -26px;&quot;&gt;&lt;a href=&quot;#&quot; accesskey=&quot;1&quot; class=&quot;remove_ctg_panel exit-btn pull-right&quot;&gt;&lt;span class=&quot;glyphicon glyphicon-remove&quot;&gt;&lt;/span&gt;&lt;/a&gt;&lt;a href=&quot;#&quot; accesskey=&quot;1&quot; class=&quot;edit_ctg_label pull-right&quot;&gt;&lt;span class=&quot;glyphicon glyphicon-edit &quot;&gt;&lt;/span&gt;&amp;nbsp;Edit&lt;/a&gt;&lt;a href=&quot;#&quot; accesskey=&quot;1&quot; class=&quot;pull-right&quot; id=&quot;addButton2&quot;&gt; &lt;span class=&quot;glyphicon glyphicon-plus&quot;&gt;&lt;/span&gt;&amp;nbsp; Add child category&lt;/a&gt;&lt;/div&gt;&lt;/h4&gt;&lt;/div&gt;&lt;div id=&quot;collapse1&quot; class=&quot;panel-collapse collapse in&quot; role=&quot;tabpanel&quot; aria-labelledby=&quot;heading1&quot;&gt;&lt;div class=&quot;panel-body&quot;&gt;&lt;div id=&quot;TextBoxDiv1&quot;&gt;&lt;/div&gt;&lt;a class=&quot;btn btn-xs btn-primary&quot; accesskey=&quot;1&quot; id=&quot;addButton3&quot;&gt;&lt;span class=&quot;glyphicon glyphicon-plus&quot;&gt;&lt;/span&gt; Add New Attributes&lt;/a&gt;&lt;a class=&quot;btn btn-xs btn-success&quot; accesskey=&quot;1&quot; id=&quot;ajax_submit_button&quot;&gt;Done&lt;/a&gt;&lt;/div&gt;&lt;/div&gt;&lt;/div&gt;");
		
		out.println("<div class=\"col-sm-12\" style=\"margin-bottom: 0;\"><div class=\"panel panel-default\" id=\"panel"+categoryType.getID()+"\"><div class=\"panel-heading\" role=\"tab\" id=\"heading"+categoryType.getID()+"\"><h4 class=\"panel-title\"><a class=\"\" id=\"panel-lebel"+categoryType.getID()+"\" role=\"button\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse"+categoryType.getID()+"\" aria-expanded=\"true\" aria-controls=\"collapse"+categoryType.getID()+"\"> "+categoryType.getName()+" </a><div class=\"actions_div\" style=\"position: relative; top: -26px;\"><a href=\"#\" accesskey=\""+categoryType.getID()+"\" class=\"remove_ctg_panel exit-btn pull-right\"><span class=\"glyphicon glyphicon-remove\"></span></a><a href=\"#\" accesskey=\""+categoryType.getID()+"\" class=\"edit_ctg_label pull-right\"><span class=\"glyphicon glyphicon-edit \"></span>&nbsp;Edit</a><a href=\"#\" accesskey=\""+categoryType.getID()+"\" class=\"pull-right\" id=\"addButton2\"> <span class=\"glyphicon glyphicon-plus\"></span>&nbsp; Add child category</a></div></h4></div><div id=\"collapse"+categoryType.getID()+"\" class=\"panel-collapse collapse in\" role=\"tabpanel\" aria-labelledby=\"heading"+categoryType.getID()+"\"><div class=\"panel-body\"><div id=\"TextBoxDiv"+categoryType.getID()+"\">");
		categoryType.getParentCatagoryTypeID();
		List<InventoryAttributeName> attributeNameList = categoryDetails.getInventoryAttributeNameList();
		//logger.debug("attributeNameList " + attributeNameList);
		out.println("<div class=\"col-md-12 form-group\"><input type=\"text\" value=\"Name\" " + " readonly " + " class=\"form-control\" style=\"width: 40%;float: left;\"/></div>");
		if(attributeNameList != null)
		{
			
			for(int j = 0; j < attributeNameList.size(); j++)
			{
				InventoryAttributeName name = attributeNameList.get(j);
				out.println("<div class=\"col-md-12 form-group\"><input type=\"text\" value=\""+name.getName()+"\" name=\"ctgtext[]\" class=\"form-control\" style=\"width: 40%;float: left;\"/><a href=\"#\" class=\"remove_field exit-btn\"><span class=\"glyphicon glyphicon-remove\"></a></div>");
			}
		}
		out.println("</div>");
		out.println("<div class=\"col-md-12 paddingleft\"> <a class=\"btn btn-xs btn-primary\" accesskey=\""+categoryType.getID()+"\" id=\"addButton3\"><span class=\"glyphicon glyphicon-plus\"></span> Add New Attributes</a><a class=\"btn btn-xs btn-success\" accesskey=\""+categoryType.getID()+"\" id=\"ajax_submit_button\" onclick=\"updateAttributes(this);\">Done</a></div>");//add_attribute_button_full_div
		out.println("</div></div></div>");//a category enclosing its attribute names
		//logger.debug("number of child nodes "+(node.getInventoryCatagoryDetailsChildNodes()!=null?node.getInventoryCatagoryDetailsChildNodes().size():0));
		generateTree(out, node.getInventoryCatagoryDetailsChildNodes());
		out.println("</div>");//a category enclosing its child categories
	}
	}catch(Exception e)
	{
		logger.debug("Exception", e);
	}
}
%>


						<%
						try{
						List<InventoryCatagoryTreeNode> rootNodeIDList =  InventoryRepository.getInstance().getInventoryCategoryTreeRootNodes();
						logger.debug("------node " + rootNodeIDList.get(0));
						PrintWriter myOut = response.getWriter();
						generateTree(myOut, rootNodeIDList);
						}
						catch(Exception e)
						{
							logger.debug("Exception", e);
						}
						%>