<%
String requestTime = (String)session.getAttribute("lastPaymentDateTo");
if(requestTime == null)requestTime="";
%>

<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Last Payment Date To</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<div class="input-group date ">
           <div class="input-group-addon">
             <i class="fa fa-calendar"></i>
           </div>
           <input type="text"  name="lastPaymentDateTo" class="form-control  datepicker" id="lastPaymentDateTo"  value ="<%=requestTime %>" autocomplete="off" >
        </div>	
	</div>
</div>

