<%@page import="common.UniversalDTOService"%>
<%@page import="scheduler.scheduler.CommonSchedulerProperty"%>
<%@page import="scheduler.scheduler.CommonSchedulerService"%>
<%@page import="scheduler.scheduler.SchedulerConfiguration"%>

<%@page import="util.ServiceDAOFactory"%>
<%@ page import="util.TimeConverter" %>
<%@ page import="java.util.List" %>
<%
	List<SchedulerConfiguration> schedulers = (List)request.getAttribute("schedulers");
	long currentTime = System.currentTimeMillis();
	UniversalDTOService universalDTOService = ServiceDAOFactory.getService(UniversalDTOService.class);
	CommonSchedulerProperty commonSchedulerProperty = universalDTOService.get(CommonSchedulerProperty.class);
	boolean isRunning = (currentTime - commonSchedulerProperty.getLastRunningTime() -10000 ) < CommonSchedulerService.COMMON_SCHEDULER_SLEEP_TIME;
%>
<div class="portlet box portlet-btcl">
	<div class="row" id="timeBox">
		<div class="col-sm-6">
			<h4>Server Time</h4>
			<p><%=TimeConverter.getTimeStringByDateFormat(currentTime, "dd MMMM yyyy - hh:mm a") %></p>
		
		</div>
		<div class="col-sm-6 text-right">
			<h4>Last Running Time</h4>
			<p><%=TimeConverter.getTimeStringByDateFormat(commonSchedulerProperty.getLastRunningTime(), "dd MMMM yyyy - hh:mm a")%></p>
		</div>
		<div class="text-center row">
			<%
			if(isRunning){
		%>
				<p style="color:green;font-size:24px">Scheduler is Running </p>
		<%} else {%>
			<p style="color:red;font-size:24px">Scheduler is not Running </p>
		<%} %>
		</div>
	</div>
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-clock-o"></i><%=request.getParameter("title") %>
		</div>
	</div>
	<div class="portlet-body">
		<div class="table-responsive">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th>Scheduler Name</th>
						<th>Last Run</th>
						<th>Next Run</th>
						<th>Result</th>
						<th>Allow Execution</th>
						<th>Summary</th>
						<th>Summary Update Time</th>
					</tr>
				</thead>
				<tbody>
				<%for(SchedulerConfiguration scheduler : schedulers) {%>
					<tr>
						<td>
						<a href="${context}scheduler/viewScheduler.do?schedulerID=<%=scheduler.getID()%>" target="_blank">
							<%=scheduler.getSchedulerName() %>
						</a>
						</td>
						<td><%=TimeConverter.getMeridiemTime(scheduler.getLastRunningTime()) %></td>
						<td><%=TimeConverter.getMeridiemTime(scheduler.getNextRunningTime()) %></td>
						<td><%=scheduler.getResult() %></td>
						<td><%=scheduler.isAllowExecution()==true?"Yes":"No" %></td>
						<td><%=scheduler.getSummary() %> </td>
						<td><%=TimeConverter.getMeridiemTime(scheduler.getSummaryUpdateTime())%> </td>
					</tr>
				<%} %>
				</tbody>
			</table>
		</div>
		
	</div>
</div>

