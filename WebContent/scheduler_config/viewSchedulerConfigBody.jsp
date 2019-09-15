
<%@page import="common.StringUtils"%>
<%@page import="util.TimeConverter"%>
<%@ page import="scheduler.scheduler.SchedulerConfiguration" %>
<link href="../assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css"/>
<link href="../assets/global/css/plugins.min.css" rel="stylesheet" type="text/css"/>
<link href="../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet"
	  type="text/css"/>
<link href="../assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet"
	  type="text/css"/>
<link href="../assets/global/plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css" rel="stylesheet"
	  type="text/css"/>
<link href="../assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet"
	  type="text/css"/>
<link href="../assets/global/plugins/clockface/css/clockface.css" rel="stylesheet" type="text/css"/>

<style>
	ul {
		list-style-type: none;
	}

	.column {
		float: left;
	}
</style>
<%
	SchedulerConfiguration scheduler = (SchedulerConfiguration)request.getAttribute("scheduler");
	String daysInAWeekString[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	boolean isMonthly = scheduler.getIntervalType() == SchedulerConfiguration.INTERVAL_TYPE_MONTHLY;
	boolean isWeekly = scheduler.getIntervalType() == SchedulerConfiguration.INTERVAL_TYPE_WEEKLY;
	int schedulingHour = scheduler.getSchedulingHour();
	int schedulingMinute = scheduler.getSchedulingMinute();
	String []intervalOrder = (StringUtils.isBlank(scheduler.getIntervalOrder())?new String[0]:StringUtils.trim(scheduler.getIntervalOrder()).split(",", -1));
	
	//10000 ms is added for precaution. margin of error or whatever.
%>

<div class="portlet box portlet-btcl">
	
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
		<div class="actions">
			<button class="btn btn-default" id="edit">Edit</button>
		</div>
	</div>
	<div class="portlet-body form">
	<!-- method="POST" action="../Scheduler/update.do" -->
		<form class="form-horizontal" id="tableForm" >
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">Scheduler Name</label>
					<div class="col-sm-6">
						<input type="text" class="form-control"
						name="schedulerName" id="schedulerName" value="<%=scheduler.getSchedulerName() %>" required>
						<input type="hidden" id="ID" name="ID" value="<%=scheduler.getID()%>">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Last Running Time</label>
					<div class="col-sm-6">
							
						<input type="text" class="form-control" id="lastRunningTime"
						name="lastRunningTime" value="<%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(scheduler.getLastRunningTime(),"dd MMMM yyyy - hh:mm a")%>">						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Next Running Time</label>
					<div class="col-sm-6">
						<div class="input-group date form_meridian_datetime toShow" style="display:none">
							<input type="text" class="form-control" name="nextRunningTime" value="<%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(scheduler.getNextRunningTime(),"dd MMMM yyyy - hh:mm a")%>">
							<span class="input-group-btn">
								<button class="btn default date-set" type="button">
									<i class="fa fa-calendar"></i>
								</button>
							</span>
						</div>
						<input type="text" class="form-control toRemove"
						name="nextRunningTime" value="<%=TimeConverter.getDateTimeStringByMillisecAndDateFormat(scheduler.getNextRunningTime(),"dd MMMM yyyy - hh:mm a")%>">	
										
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Class Name</label>
					<div class="col-sm-6">
						<input type="text" class="form-control"
						name="className" value="<%=scheduler.getClassName() %>" >						
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Method Name</label>
					<div class="col-sm-6">
						<input type="text" class="form-control"
						name="methodName" value="<%=scheduler.getMethodName()%>" >						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Result</label>
					<div class="col-sm-6">
						<input type="text" class="form-control"
						name="result" value="<%=scheduler.getResult()%>">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Mails</label>
					<div class="col-sm-6">
						<input type="text" class="form-control"
						name="mails" value="<%=scheduler.getMails() %>" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Executable</label>
					<div class="col-sm-6">
						<input type="text" class="form-control toRemove"
						name="allowExecution" value='<%=scheduler.isAllowExecution()?"Yes":"No" %>' >
						<div class="radio-list toShow" style="display: none">
							<label class="radio-inline">
								<input type="radio" value="1" name="allowExecution" <%=scheduler.isAllowExecution()?"checked":"" %>>Yes
							</label>
							<label class="radio-inline">
								<input type="radio" value="0" name="allowExecution" <%=scheduler.isAllowExecution()?"":"checked" %>>No
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Interval Type</label>
					<div class="col-sm-6">
						
						<select name="intervalType" class="form-control select" id="intervalType">
							<option value="<%=SchedulerConfiguration.INTERVAL_TYPE_MONTHLY%>" 
							<%=isMonthly?"selected":"" %>>Monthly</option>
							<option value="<%=SchedulerConfiguration.INTERVAL_TYPE_WEEKLY%>" 
							<%=isWeekly?"selected":"" %>>Weekly</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Details</label>
					<div class="col-sm-6" id="monthly" 
					<%
						if(isWeekly){
					%>
					style="display:none"
					<%} %>
					>
					<%
					int columnCount = 6;
					int dayCount = 31;
					for(int i=1;i<=columnCount;i++){
					%>
						<div class="column">
							<ul>
							<%
							int j;
							for(j=1;j<dayCount;j++){
								if(j%columnCount==i || (i==columnCount && j%columnCount==0)){
							%>	
								<li><input name="intervalOrderMonthly" id="m_<%=j %>" class="checkbox" type="checkbox" value="<%=j%>"><%=j %></li>
								<%}
							} %>
							<%if(i==1 && j==dayCount){ %>
								<li><input name="intervalOrderMonthly" id="m_<%=j %>" class="checkbox" type="checkbox" value="<%=j%>"><%=j %></li>
							<%} %>
							</ul>
						</div>	
					<%}%>
					</div> 
					<div class="col-sm-6" id="weekly" 
					<%
						if(isMonthly){					
					%>
					style="display:none"
					<%} %>
					>
					<%
					int columnCountForWeekly = 3;
					int dayCountForWeekly = 7;
					for(int i=1;i<=columnCountForWeekly;i++){
					%>
						<div class="column">
							<ul>
							<%
							int j;
							for(j=1;j<=dayCountForWeekly;j++){
								if(j%columnCountForWeekly==i || (i==columnCountForWeekly && j%columnCountForWeekly==0)){
							%>	
								<li><input name="intervalOrderWeekly" id="w_<%=j %>" class="checkbox" type="checkbox" value="<%=j%>"><%=daysInAWeekString[j-1] %></li>
								<%}
							} %>
							<%if(i==1 && j==dayCountForWeekly){ %>
								<li><input name="intervalOrderWeekly" id="w_<%=j %>" class="checkbox" type="checkbox" value="<%=j%>"><%=daysInAWeekString[j-1] %></li>
							<%} %>
							</ul>
						</div>	
					<%}%>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Scheduling Hour</label>
					<div class="col-sm-2">
						<select name="schedulingHour" class="form-control select" id="hour">
							<%
								for(int i=0;i<25;i++){
							%>
								<option value="<%=i %>" <%=schedulingHour==i ?"selected":""%>>
									<%
										if(i<10){
									%>
										0<%=i%>
									<%}else {%>
										<%=i%>
									<%}%>
								</option>
							<%} %>
						</select>
					</div>
					<label class="col-sm-2 control-label">Minute</label>
					<div class="col-sm-2">
						<select name="schedulingMinute" class="form-control select" id="minute">
							<%
								for(int i=0;i<61;i++){
							%>
								<option value="<%=i%>" <%=schedulingMinute==i ? "selected":""%> >
									<%
										if(i<10){
									%>
										0<%=i%>
									<%}else {%>
										<%=i%>
									<%}%>
								
								</option>
							<%} %>
						</select>
					</div> 
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Summary</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="summary"
						 name="summary" value="<%=scheduler.getSummary()%>">
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-3 control-label">Summary Update Time</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="summaryUpdateTime"
						 name="summaryUpdateTime" value="<%=TimeConverter.getMeridiemTime(scheduler.getSummaryUpdateTime())%>">
					</div>
				</div>
			</div>
			<div class="form-actions text-center toShow" style="display:none">
				<button class="btn btn-reset-btcl" type="reset" id="resetbtn">Reset</button>
				<button class="btn btn-submit-btcl" type="submit" id="submitbtn">Submit</button>
			</div>
			
		</form>
	</div>
</div>

<script>
	$(document).ready(function(){
		$('input').prop('readonly', true);
		$('.select').prop('disabled', true);
		checkTheCheckboxes();
		$('.checkbox').attr('disabled', true);
		$('#edit').click(function(){
			$('input:not("#schedulerName, #summaryUpdateTime, #summary, #lastRunningTime")').prop('readonly', false);
			$('.toShow').show();
			$('.toRemove').remove();
			$('.select').prop('disabled', false);
			$('.checkbox').attr('disabled', false);
			
			return false;
		});
		$('#resetbtn').click(function(){
			$('#tableForm').trigger('reset');
		});
		$('#intervalType').change(function(){
			LOG($('#intervalType').val());
			if($('#intervalType').val() == 1){
				$('#weekly').hide();
				$('#monthly').show();
			}
			else if($('#intervalType').val() == 2){
				$('#monthly').hide();
				$('#weekly').show();
				
			}
		});
		function checkTheCheckboxes(){
			<%
				for(String s: intervalOrder){
			%>
				var id = <%=s%>;
				if(<%=isMonthly%>){
					id = "m_" + id;
				}else if(<%=isWeekly%>){
					id="w_" + id;
				}
				var selector = "#"+id+"";
				$(selector).prop('checked', true);
			<%}%>
		}
	});
	
	$('#submitbtn').click(function(){
		var url = context + "scheduler/update.do";
		var formData = $('#tableForm').serializeArray();
		var intervalType = $('#intervalType').val();
		var intervalOrder = "";
		var queryString = "";
		formData.forEach(function (entry){
			if(entry.name === "intervalOrderMonthly" && intervalType==1){
				intervalOrder += entry.value + ',';
			}else if(entry.name==="intervalOrderWeekly" && intervalType == 2){
				intervalOrder += entry.value + ',';
			}else if(entry.name !== "intervalOrderMonthly" && entry.name !== "intervalOrderWeekly"){
				queryString += entry.name + "=" + entry.value + "&";
			}
		});
		queryString += "intervalOrder=" + intervalOrder.slice(0, -1);
		callAjax(url, queryString, function(data){
			if(data.responseCode==1){
				toastr.success(data.msg);
				setTimeout(function(){
					location.href = context + "scheduler/viewScheduler.do?schedulerID=<%=scheduler.getID()%>";
				}, 1500);
			}
		}, "POST");
		return false;
	});
	
</script>
<script src="../assets/global/plugins/moment.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<script src="../assets/global/plugins/clockface/js/clockface.js" type="text/javascript"></script>
<script src="../assets/global/scripts/app.min.js" type="text/javascript"></script>
<script src="../assets/pages/scripts/components-date-time-pickers.min.js" type="text/javascript"></script>