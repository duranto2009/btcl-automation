<%@page import="common.ClientDTO"%>
<%@page import="common.repository.AllClientRepository"%>
<%@page import="login.LoginDTO"%>

<%@page import="request.CommonRequestDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="request.RequestStatus"%>
<%@page import="request.RequestUtilService"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@ page import="user.UserDTO"%>
<%@page import="user.UserRepository"%>
<%@page import="util.RecordNavigator"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%
LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	System.out.println("Inside nav.jsp" + request.getParameter("entityID"));
	String url = request.getParameter("url");
	String navigator = request.getParameter("navigator");
	String pageno = "";

	RecordNavigator rn = (RecordNavigator)session.getAttribute(navigator);
	pageno = ( rn == null ) ? "1" : "" + rn.getCurrentPageNo();

	System.out.println("rn " + rn);
	
	String actionParameter="moduleID="+request.getParameter("moduleID")+"&currentTab="+request.getParameter("currentTab")+"&entityID="+request.getParameter("entityID")+"&entityTypeID="+request.getParameter("entityTypeID");
	
	String action = "/" + url+"?"+actionParameter;
	String context = "../../.."  + request.getContextPath() + "/";
	String link = context + url + ".do?"+actionParameter;
	String searchFieldInfo[][] = rn.getSearchFieldInfo();
	String totalPage = "1";
	if(rn != null)
		totalPage = rn.getTotalPages() + "";

%>

<!-- search control -->
<div class="portlet" style="margin-bottom: 5px;padding-bottom: 0px;">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-search-plus"></i>History 
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
					<div class="col-md-6 col-sm-6">
						<div class="form-group">
							<label class="control-label col-md-4 col-sm-4 col-xs-4" for="">Records Per Page</label>
							<div class="col-md-8 col-sm-8 col-xs-8">
								<input type="text" class="form-control" name="RECORDS_PER_PAGE" id="" placeholder="" value="<%=rn.getPageSize()%>">
							</div>
						</div>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-md-12 text-center">
						<input type="hidden" name="currentTab" value="3"/>
						<input type="hidden" value="yes" name="search">
						<input type="reset" class="btn btn-circle btn-md grey " value="Reset" >
			        	<input type="submit" class="btn btn-circle btn-md green-meadow btn-outline sbold uppercase" value="Search" >
					</div>
				</div>
			</div>
		</html:form>
		<!-- END FORM-->
	</div>
</div>

<div class="row">
    <div class="col-md-12 col-sm-12">
        <div class="portlet light bordered">
            <div class="portlet-title" style="border-bottom: none !important">
                <div class="row"> 
                	<html:form action="<%=action%>" method="POST" styleClass="form-inline">
						<div class="col-md-offset-3 col-xs-12 col-md-6 text-center">
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
							    	&nbsp;&nbsp;<i class="hidden-xs">Page </i><input type="text" class="custom-form-control " name="pageno" value='<%=pageno%>' size="15"> <i class="hidden-xs">of &nbsp;</i><%=rn.getTotalPages()%>
									<html:hidden property="go" value="yes" />
									<input type="submit" class="btn btn-circle btn-sm green-meadow btn-outline sbold uppercase" value="GO"/>
							    </li>
							  </ul>
							</nav>
						</div>
					</html:form>
                 </div>
		    </div>
		    <div class="portlet-body">
		        <div class="row">
		            <ul class="feeds">
		            <%
					ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_COMMON_REQUEST);
					
					if (data != null) {
						int size = data.size();
						for (int i = 0; i < size; i++) {
							CommonRequestDTO row = (CommonRequestDTO) data.get(i);
							ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getClientID());
							if (clientDTO == null)
								continue;
							String clientName = clientDTO.getLoginName();
							Date requestDate = new Date(row.getRequestTime());
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
							String requestDateString = df.format(requestDate).toString();
							int status = row.getCompletionStatus();
							if(loginDTO.getAccountID() > 0){
								if(status == RequestStatus.SEMI_PROCESSED)
									status = RequestStatus.PENDING;
							
							} 
							UserDTO userdto = null;
							String requestedBy = "";
							if(row.getRequestByAccountID() > 0)
							{
								clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getRequestByAccountID());
								if (clientDTO != null)
								{
									requestedBy = clientDTO.getLoginName();
								}
							    
							}
							else
							{
								userdto = UserRepository.getInstance().getUserDTOByUserID(Math.abs(row.getRequestByAccountID()));
								if(userdto != null)
								{
									if(loginDTO.getAccountID() > 0)
									{
										requestedBy = "System";
									}
									else
									{
										requestedBy =  userdto.getUsername() +" ("+userdto.getFullName()+")";
									}
								}
							}
							String requestedTo = "";
							if(row.getRequestToAccountID() != null)
							{
							if(row.getRequestToAccountID() > 0)
							{
								clientDTO = AllClientRepository.getInstance().getClientByClientID(row.getRequestToAccountID());
								if (clientDTO != null)
								{
									requestedTo = clientDTO.getLoginName();
								}
							    
							}
							else if(row.getRequestToAccountID() < 0)
							{
								userdto = UserRepository.getInstance().getUserDTOByUserID(Math.abs(row.getRequestToAccountID()));
								if(userdto != null)
								{
									if(loginDTO.getAccountID() > 0)
									{
										requestedTo = "System";
									}
									else
									{
										requestedTo = userdto.getUsername() +" ("+userdto.getFullName()+")";
									}
								}
							}	
							}						
							%>
						<li>
                          <div  <%if(row.isDeleted()){%>class="note note-normal"<%}else{%>class="note note-info"<%} %>>
                              <%-- <h4 class="block"><%=requestDateString%></h4> --%>
                              <p><strong>Time:</strong> <%=requestDateString%></p>
                              <%if(row.getIP() != null && row.getIP().length() > 0 && loginDTO.getUserID() > 0){ %>
                              <p><strong>IP:</strong> <%=row.getIP()%></p>
                              <%} %>
                              <p><strong>From:</strong> <%=requestedBy%></p>
                              <%if(requestedTo.length() > 0){%>
                              <p><strong>To:</strong> <%=requestedTo%></p>
                              <%} %>
                              <p><strong>Action:</strong> <%=RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(row.getRequestTypeID()).getDescription()%></p>
                              <p>
                              
                              	<strong>Description:</strong>
                              	<br>
                              	<%=row.getDescription()%>
                              	
                              	<%
    								String noteLink = RequestUtilService.getNoteLink( request.getContextPath(), row.getRequestTypeID(), row.getReqID(), row.getEntityTypeID(), row.getEntityID() );
        						%>
        						
        						<% if( noteLink != null ){%><%=noteLink %><%} %>
                              </p>
                          </div>						
                          
		                </li>
						<%
						}
						%>
						<%
						}else{
						%>
							<li class="note note-info">
                                <h1 class="text-center"> No Result </h1>
                            </li>
						<%} %>
                	</ul>
            	</div>
            </div>
        </div>
    </div>
</div>
