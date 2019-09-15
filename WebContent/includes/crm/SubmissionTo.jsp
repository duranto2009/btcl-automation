<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>

<input type="hidden" name="mode" value="search">
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Submission To</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<div class="input-group date ">
			<div class="input-group-addon">
	            <i class="fa fa-calendar"></i>
	          </div>
			<input type="text" name="submissionTo" class="form-control datepicker" id="submissionTo"/>
		</div>
	</div>
</div>
