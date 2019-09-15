<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="util.RecordNavigator"%>
<%
	System.out.println("Inside nav.jsp");
	String url = request.getParameter("url");
	String navigator = request.getParameter("navigator");
	String pageno = "";

	RecordNavigator rn = (RecordNavigator)session.getAttribute(navigator);
	pageno = ( rn == null ) ? "1" : "" + rn.getCurrentPageNo();

	System.out.println("rn " + rn);
	
	String action = "/" + url;
	String context = "../../.."  + request.getContextPath() + "/";
	String link = context + url + ".do";
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


<!-- search control -->
<div class="portlet box purple">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-search-plus"></i>Search
		</div>
		 <div class="tools">
            <a class="collapse" href="javascript:;" data-original-title="" title=""> </a>
        </div>
	</div>
	<div class="portlet-body form">
		<!-- BEGIN FORM-->
		<html:form action="<%=action%>" method="POST" styleClass="form-horizontal">
			<div class="form-body">
			<%
	if(searchFieldInfo != null && searchFieldInfo.length > 0)
	{
%>
			<% for(int i = 0; i < searchFieldInfo.length;i++) { %>
					<%if(i % 2 == 0){%>
						<div class="row">
						<%}%>
							<div class="col-md-6 col-sm-6">
					  <%
						  if (searchFieldInfo[i][0].endsWith(".jsp") )
						  {%>
						  <jsp:include page="<%=searchFieldInfo[i][0]%>" flush="true"/>
						  <%}else{ %>
						<div class="form-group">
							<label for="" class="control-label col-md-4 col-sm-4 col-xs-4"><%=searchFieldInfo[i][0]%></label>
							<div class="col-md-8 col-sm-8 col-xs-8">
							    <input type="text" class="form-control" id="" placeholder="" name="<%=searchFieldInfo[i][1]%>"
					    <%
					    String value = (String)session.getAttribute(searchFieldInfo[i][1]);
					    session.removeAttribute(searchFieldInfo[i][1]);
					    if( value != null){%>value = "<%=value%>"<%}%>>
								
							</div>
						</div>
						<%}%>
						</div>

						<%if(i % 2 == 1){%>
						</div>
						<%} %>

						<%}%>						
				<%} %>

						<%if(searchFieldInfo.length % 2 == 0){%>
						<div class="row">
						<%}%>
						<%-- <div class="col-md-6 col-sm-6">
						<div class="form-group">
							<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Records Per Page</label>
							<div class="col-md-8 col-sm-8 col-xs-8">
								<input type="text" class="form-control" name="RECORDS_PER_PAGE" id="" placeholder="" value="<%=rn.getPageSize()%>">
							</div>
						</div>
						</div> --%>
						
						</div>
				
			</div>
			<div class="form-actions fluid">
				<div class="row">
					<div class="col-md-offset-2 col-md-10">
							<label for="" class="control-label col-md-4 col-sm-4 col-xs-12">Records Per Page</label>
							<div class="col-md-2 col-sm-8 col-xs-12">
								<input type="text" class="form-control" name="RECORDS_PER_PAGE" id="" placeholder="" value="<%=rn.getPageSize()%>">
							</div>
							<div class="col-md-4 col-sm-8 col-xs-12">					
						 	<html:hidden property="search" value="yes" />
					        <input type="submit" class="btn green" value="Search" >
					        <input type="reset" class="btn " value="Reset" >
					        </div>
					</div>
				</div>
			</div>
			<input type="hidden" name="parentItemID" value="14013">
			<input type="hidden" name="categoryID" value="3">
		</html:form>
		<!-- END FORM-->
	</div>
</div>


<div class="portlet box">
	<div class="portlet-body">
		<div class="row">
			<html:form action="<%=action%>" method="POST"
				styleClass="form-inline">
				<div class="col-md-offset-4 col-xs-12 col-md-2 text-center">
					<a href="<%=link%>?id=first"> <i
						class="icon-control-rewind icons "></i>
					</a> <a href="<%=link%>?id=previous"> <i
						class="icon-arrow-left icons "></i></a> <a href="<%=link%>?id=next">
						<i class="icon-arrow-right icons"> </i>
					</a> <a href="<%=link%>?id=last"> <i
						class="icon-control-forward icons"></i></a>
				</div>
				<div class=" col-xs-12 col-md-4 pull-left">
					<div class="form-inline">
						Page <input type="text" class="custom-form-control" name="pageno" value='<%=pageno%>' size="15"> of
						<%=rn.getTotalPages()%>
						<html:hidden property="go" value="yes" />
						<input type="submit" class="btn btn-sm green " value=" Go ">
					</div>
				</div>
			</html:form>
		</div>
	</div>
</div>
