<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
	String title = request.getParameter("title");
	String fromTime = (String)session.getAttribute(title);
	if(fromTime == null ) fromTime=new SimpleDateFormat( "yyyy-MM" ).format(new Date());
%>
<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4"><%=title %></label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<div class="input-group date">
			<div class=input-group-addon>
				<i class="fa fa-calendar"></i>
			</div>
			<input type=text  name="<%=title %>" class="form-control datepicker" data-format="yyyy-mm" value ="<%=fromTime%>" 
			autocomplete=off>	
		</div>
	</div>
</div>


