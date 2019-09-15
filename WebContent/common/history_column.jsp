<%@page import="common.repository.AllClientRepository"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.CommonService"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="login.LoginDTO"%>
<%@page import="request.RequestStatus"%>
<%@page import="common.ClientDTO"%>
<%@page import="common.ClientRepository"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="vpn.client.ClientDetailsDTO"%>
<%@page import="java.util.*,java.text.*"%>
<script src="${context}/scripts/bootstrap-datepicker.js"></script>
<%
	CommonService commonService = new CommonService();
	int entityTypeID = (Integer) request.getAttribute("entityTypeID");
	request.setAttribute("actionList", commonService.getActionDTOs(entityTypeID, false,
			(login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN)));
	
	String tabClasses="";
	String tabButton="";
	tabClasses="col-md-6 col-md-offset-3";
	tabButton="col-md-offset-4 col-md-4";
	
%>

<%=request.getRequestURI() %>
<div class="portlet light">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-history"></i>History
		</div>
	</div>
	<div class="portlet-body  form" style="margin-bottom: 25px;">
		<form method="get" class="" id="historySearchForm1" action="../../VpnLinkAction.do">
			<input type="hidden" name="historySearch" value="yes"/>
			<input type="hidden" name="id" value="<%= request.getParameter("id")%>"/>
			<input type="hidden" name="currentTab" value="3"/>
			
			<div class="form-body <%=tabClasses%>">
				<div class="form-group">	
					<div class="input-group history-search">
						<span class="input-group-addon">
							<i class="fa fa-user"></i>
						</span>
						<input name="arClientId" class="form-control spinner" id="username" type="text" autocomplete="off" placeholder="Name">
					</div>
				</div>
				<div class="form-group">
					<div class="input-group history-search">
						<span class="input-group-addon">
							<i class="fa fa-file-o"></i>
						</span>
						<input name="arDescription" class="form-control spinner" id="innerContent" type="text" autocomplete="off" placeholder="Inner Content">
					</div>
				</div>
				<div class="form-group">	
					<div class="input-group history-search">
						<span class="input-group-addon">
							<i class="fa fa-wrench"></i>
						</span>
						<select name="actionTypeID" id="actionTypeID" class="form-control select2">
							<option value="0" selected>Select Action Type</option>
							<c:forEach var="action" items="${actionList}">
							<c:set var="rootActionID" value="${action.rootActionTypeID}"></c:set>
								<option value='${action.actionTypeID}'>${action.description}(<%=RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID((Integer) pageContext.getAttribute("rootActionID")).getDescription()%> )</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="input-group history-search">
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
						<input name="arReqTimeFrom" class="form-control spinner" type="text" id="datepicker-from" autocomplete="off" placeholder="From">
					</div>
				</div>
				<div class="form-group">
					<div class="input-group history-search">
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
						<input name="arReqTimeTo" class="form-control spinner" type="text" id="datepicker-to" autocomplete="off" placeholder="To">
					</div>
				</div>
				<div class="form-group">        
			      	<div class="pull-right">
			       	 	<button class="btn btn-default uppercase" type="reset" >Reset</button>
			        	<button class="btn btn-search-btcl uppercase" type="submit" id="search-history1">Search</button>
			      	</div>
			    </div>
			    
			</div>
		</form>
		<div class="col-md-12">
			<div class="table-responsive">
						<table id="example1" class="table table-bordered table-striped">
							<thead>
								<tr>

<!-- 									<th>Request Name</th> -->
									<th>Entity</th>
									<th>Description</th>
									<th>Client Name</th>
									<th>Request Date</th>
									<th>Status</th>					
								</tr>
							</thead>
							<tbody>
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
												DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
												String requestDateString = df.format(requestDate).toString();
								%>
								<tr>
									<%
									
									/*RequestActionStateRepository actionRepo = RequestActionStateRepository.getInstance();
									String rootRequestName = "";
									if(row.getRootReqID() == null)
									{
										
									}*/
									%>
<%-- 									<td><a href="<%=context%>GetRequestForView.do?id=<%=row.getReqID()%>"><%//RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(row.getRequestTypeID()).getDescription()%></a></td> --%>
									<td><%=EntityTypeConstant.entityNameMap.get(row.getEntityTypeID())%></td>	
									<td><%=row.getDescription()%></td>																	
									<td><%=clientName%></td>
									<td><%=requestDateString%></td>
									<%
									int status = row.getCompletionStatus();
									/*if(loginDTO.getAccountID() > 0){
										if(status == RequestStatus.SEMI_PROCESSED)
											status = RequestStatus.PENDING;*/
									%>
									<%//}%>
									<td><%//RequestStatus.reqStatusMap.get(status)%></td>
								</tr>

								<%
									}
								%>
							</tbody>
							<%
								}
							%>

						</table>
					</div>
		</div>
	</div>
	
	
</div>

<div class="row">
	<div class="col-md-12">
		<!-- TIMELINE ITEM -->
		<div class="timeline-item" id="load-history">
			<!-- load history here -->
		</div>
		<input type="hidden" id="next-count-history" value="0">
		<!-- END TIMELINE ITEM -->
	</div>
</div>
<script>
    $(document).ready(function() {

	$('#datepicker-from').datepicker({
	    dateFormat : 'dd/mm/yy',
	    autoclose : true
	});
	$('#datepicker-to').datepicker({
	    dateFormat : 'dd/mm/yy',
	    autoclose : true
	});
    });
</script>