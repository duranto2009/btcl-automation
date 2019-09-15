<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@ page import="util.RecordNavigator" %>
<%@ page import="login.LoginDTO" %>
<%@ page import="common.RequestFailureException" %>
<style>
	.kay-group > input{
		margin:5px;
	}
</style>
<%
	Logger logger= Logger.getLogger("inside nav");
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	String url = request.getParameter("url");
	String navigator = request.getParameter("navigator");
	String pageno = "";
	RecordNavigator rn=null;
	String moduleID=request.getParameter("moduleID");
	try{
	 	rn= (RecordNavigator)session.getAttribute(navigator);
		pageno = ( rn == null ) ? "1" : "" + rn.getCurrentPageNo();
	}catch(Exception e){
		logger.fatal("exception e",e);
	}
	System.out.println("rn " + rn);


	String action;

	String context = "../../.."  + request.getContextPath() + "/";
	String link;
	String linkFirst;
	String linkLast;
	String linkNext;
	String linkPrevious;
    if( moduleID !=null && !moduleID.equalsIgnoreCase("null")) {
        action = "/" + url+"?moduleID="+moduleID;
        link = context + url +".do?moduleID="+moduleID;
        linkFirst = link +"&id=first";
        linkLast = link+"&id=last";
        linkNext = link+"&id=next";
        linkPrevious = link+"&id=previous";

    }else {
        action = "/" + url;
        link = context + url + ".do";
        linkFirst = link +"?id=first";
        linkLast = link+"?id=last";
        linkNext = link+"?id=next";
        linkPrevious = link+"?id=previous";
    }
	String[][] searchFieldInfo;
	if(rn == null) {
		throw new RequestFailureException("Record Navigator is NULL");
	}else {
		searchFieldInfo = rn.getSearchFieldInfo();
	}
	String title = request.getParameter("title");
	if(title== null){  
		title = "Search";
	}

	String goCheck = request.getParameter(SessionConstants.GO_CHECK_FIELD);
	String searchCheck = request.getParameter(SessionConstants.SEARCH_CHECK_FIELD);
	String htmlSearchCheck = request.getParameter(SessionConstants.HTML_SEARCH_CHECK_FIELD);
	String id = request.getParameter(SessionConstants.NAVIGATION_LINK);


	if(goCheck == null && searchCheck == null && htmlSearchCheck == null && id == null) {

		if(searchFieldInfo != null && searchFieldInfo.length > 0){
			for (String[] strings : searchFieldInfo) {
				if (strings[0].equalsIgnoreCase("keyValuePairDropdown")) {
					int limit = strings[1].indexOf("_");
					String key = strings[1].substring(0, limit);

					session.removeAttribute(key);
				} else {
					session.removeAttribute(strings[1]);
				}
			}
		}
	}

%>



<!-- search control -->
<div class="portlet light bordered">
	<div class="portlet-title">
		<div class="caption font-green-sharp">
			<%--<i class="fa fa-search-plus"></i>--%>
			<span class="caption-subject bold uppercase">Search </span>
			<%--<span class="caption-subject bold uppercase"><%=title %></span>--%>
			<span class="caption-helper"><%=title%> | Criteria</span>
		</div>
		<div class="tools"><a class="collapse" href="javascript:;" data-original-title="" title=""> </a></div>
	</div>
	<div class="portlet-body form">
		<!-- BEGIN FORM-->
		<html:form action="<%=action%>" method="POST" styleClass="form-horizontal">
			<div class="form-body">
			<%if(searchFieldInfo != null && searchFieldInfo.length > 0){%>
			<%for(int i = 0; i < searchFieldInfo.length;i++){
				boolean doNotPrint = false;
 				if(i % 2 == 0){%>
 				<div class="row">
 				<%}%>
					<div class="col-md-6 col-sm-6">
						<%if(searchFieldInfo[i][0].equalsIgnoreCase("keyValuePairDropdown")){
							int limit = searchFieldInfo[i][1].indexOf("_");
							String key = searchFieldInfo[i][1].substring(0, limit);
							String value = searchFieldInfo[i][1].substring(limit+1);
							request.setAttribute("dropdownList", SessionConstants.getDropdownListByKey(value + "_" + key.toLowerCase()));
//							request.getSession(true).removeAttribute("dropdownList");

						%>
							<jsp:include page="/includes/common/generic-dropdown-list.jsp" flush="true">
								<jsp:param name="title" value="<%=key%>"/>


							</jsp:include>
						<%}else if (searchFieldInfo[i][0].endsWith(".jsp")){%>
							<jsp:include page="<%=searchFieldInfo[i][0]%>" flush="true">
								<jsp:param name="title" value="<%=searchFieldInfo[i][1] %>" />
							</jsp:include>
						<%}else{

							if(loginDTO.getUserID()<0 && searchFieldInfo[i][0].toLowerCase().contains("client")) {
								doNotPrint = true;
							}

							String value = (String)session.getAttribute(searchFieldInfo[i][1]);
							String name = searchFieldInfo[i][1];
							String label = searchFieldInfo[i][0];
						%>
						<%if(!doNotPrint){%>
						<div class="form-group">
							<label for="" class="control-label col-md-4 col-sm-4 col-xs-4"><%=label%></label>
							<div class="col-md-8 col-sm-8 col-xs-8">
								<input type="text" class="form-control" name="<%=name%>" value="<%=value!=null?value:""%>">
							</div>
						</div>
						<%}%>
					<%}%>
					</div>

				<%if(i % 2 == 1){%>
				</div>
				<%}%>

			<%}						
			}%>

				<%if(searchFieldInfo != null && searchFieldInfo.length % 2 == 0){%>
				<div class="row">
				<%}%>
				</div>
				
			<div class=clearfix></div>
			</div>
			<div class="form-actions fluid">
				<div class="row">
					<div class="col-xs-offset-0 col-sm-offset-2 col-md-offset-3 col-xs-12 col-md-9">
						<label for="" class="control-label col-xs-3 col-sm-3 col-md-3">Records Per Page</label>
						<div class="col-xs-3  col-sm-3 col-md-2">
							<input type="text" class="custom-form-control" name="RECORDS_PER_PAGE" id="" placeholder="" value="<%=rn.getPageSize()%>">
						</div>
						<div class="col-xs-6  col-sm-5  col-md-4">					
						 	<html:hidden property="search" value="yes" />
							<input type="hidden" name="moduleID" value="<%=moduleID%>">
<!-- 				          	<input type="reset" class="btn  btn-sm btn btn-circle  grey-mint btn-outline sbold uppercase" value="Reset" > -->
					        <input type="submit" class="btn  btn-sm btn btn-circle btn-sm green-meadow btn-outline sbold uppercase" value="Search" >
				        </div>
					</div>
				</div>
			</div>
		</html:form>
		<!-- END FORM-->
	</div>
</div>


<div class="portlet box portlet-btcl light">
	<div class="portlet-body">
		<div class="row text-center">
			<html:form action="<%=action%>" method="POST" styleClass="form-inline">
				<nav aria-label="Page navigation" >
			  	<ul class="pagination" style="margin: 0;">
					<li class="page-item">
					  <a class="page-link" href="<%=linkFirst%>" aria-label="First"  title="Left">
						<i class="fa fa-angle-double-left" aria-hidden="true"></i>
						<span class="sr-only">First</span>
					  </a>
					</li>
					<li class="page-item">
					  <a class="page-link" href="<%=linkPrevious%>" aria-label="Previous" title="Previous">
						 <i class="fa fa-angle-left" aria-hidden="true"></i>
						<span class="sr-only">Previous</span>
					  </a>
					</li>
					<li class="page-item">
					  <a class="page-link" href="<%=linkNext%>" aria-label="Next" title="Next">
						 <i class="fa fa-angle-right" aria-hidden="true"></i>
						<span class="sr-only">Next</span>
					  </a>
					</li>
					<li class="page-item">
					  <a class="page-link" href="<%=linkLast%>" aria-label="Last"  title="Last">
						<i class="fa fa-angle-double-right" aria-hidden="true"></i>
						<span class="sr-only">Last</span>
					  </a>
					</li>
					<li>
						&nbsp;&nbsp;<i class="hidden-xs">Page </i><input type="text" class="custom-form-control " name="pageno" value='<%=pageno%>' size="15"> <i class="hidden-xs">of</i>
						<%=rn.getTotalPages()%>
						<html:hidden property="go" value="yes" />
						<html:hidden property="mode" value="search" />
						<input type="hidden" name="moduleID" value="<%=moduleID%>">
						<input type="submit" class="btn btn-circle  btn-sm green-haze btn-outline sbold uppercase" id="goButton" value="GO"/>
					</li>
			  	</ul>
				</nav>
			</html:form>
		</div>
	</div>
</div>

