<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String dnsDomainExpiryDateTo = (String)session.getAttribute("dnsDomainExpiryDateTo");
if(dnsDomainExpiryDateTo == null)dnsDomainExpiryDateTo="";
System.out.println("DNS Domain Expiry Date From " + dnsDomainExpiryDateTo);
%>

<input type="hidden" name="mode" value="search">
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Expire Date To</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<input type="text"  name="dnsDomainExpiryDateTo" class="form-control datepicker" id="dnsDomainExpiryDateTo"  value =<%=dnsDomainExpiryDateTo %> >
	</div>
</div>
