<%@page import="java.util.ArrayList"%>
<%@page import="costConfig.CategoryDTO"%>
<%@page import="java.util.List"%>
<%@page import="common.*" %>
<%
	int numOfTabs = (Integer)request.getAttribute("totalCategory");
	List<CategoryDTO> catgoryList = (List<CategoryDTO>)request.getAttribute("categoryList");
%>	
<ul class="nav nav-pills" style="margin-bottom:0px">
<%
	for(int i=0;i<catgoryList.size();i++){ 
		long catId = catgoryList.get(i).getId();
		String uriString = "../../GetCostConfig.do?moduleID=" + ModuleConstants.Module_ID_LLI +"&categoryID="+catId;
		String className = "";
		if(catId == categoryID){
			className = "active";
		}
%>
	<li class=<%=className %> >
<%-- 		<a href= <%=uriString %>>Category <%=i+1 %></a>
 --%>		<a href= <%=uriString %>> <%=catgoryList.get(i).getCategoryName() %></a>
		
	</li>
<%}%>
</ul>

		
			
			
		
