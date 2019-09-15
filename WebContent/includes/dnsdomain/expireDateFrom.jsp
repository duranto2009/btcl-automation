<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String dnsDomainExpiryDateFrom = (String)session.getAttribute("dnsDomainExpiryDateFrom");
if(dnsDomainExpiryDateFrom == null)dnsDomainExpiryDateFrom="";
System.out.println("DNS Domain Expiry Date From " + dnsDomainExpiryDateFrom);
%>

<input type="hidden" name="mode" value="search">
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Expire Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<input type="text"  name="dnsDomainExpiryDateFrom" class="form-control datepicker" id="dnsDomainExpiryDateFrom"  value =<%=dnsDomainExpiryDateFrom %> >
	</div>
</div>
