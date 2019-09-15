<%@page import="org.apache.log4j.Logger"%>
<%@page import="domain.DomainDTO"%>
<%@page import="domain.DomainContants"%>
<%@page import="domain.DomainPackageDetails"%>
<%@page import="java.util.List"%>
<%@page import="domain.DomainPackage"%>
<%@page import="domain.DomainNameDTO"%>
<%@page import="domain.DomainService"%>
<%@page import="common.payment.constants.PaymentConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="user.UserDTO, java.util.ArrayList"%>    
<%
DomainService domainService = new DomainService();
request.setAttribute("domainExt", "" + DomainNameDTO.BANGLA_EXT);
DomainDTO domainDTO = (DomainDTO)request.getAttribute("domainDTO");
if(domainDTO.getDomainAddress().endsWith(".bd"))
{
	request.setAttribute("domainExt", "" + DomainNameDTO.BD_EXT);
}
Logger loggerYearList = Logger.getLogger("yearlist");
loggerYearList.debug("domainDTO " + domainDTO);
//if(loginDTO.getUserID() > 0)
{
	request.setAttribute("domainClientID", ""+domainDTO.getDomainClientID());
}
domainDTO = domainService.domainQuery(null, null, request, response,true);
if(domainDTO!=null){
	DomainPackage domainPackage = domainService.getDomainPackageByID(domainDTO.getPackageID());
%>
<div class="form-group">
	<label for="year" class="control-label col-md-3">Year</label>
	<div class="col-md-4"> 
	<select class="form-control select2" name="year" style="width: 100%">
		<%
		List<DomainPackageDetails> domainPackageList = domainPackage.getDomainPackageDetailsList();
		%>
			<%for( DomainPackageDetails domainPackageDetails : domainPackageList) {%>
				<option value='<%=domainPackageDetails.getYear()%>'><%=domainPackageDetails.getYear()%>&nbsp;year - <%=domainPackageDetails.getCost()%> tk</option>
			<%} %>
		</select>
	</div>
</div>
<%}else{%>
<div class="form-group">
	<div class="alert alert-danger"> <strong>Error!</strong> Invalid Domain. </div>
</div>
<%}%>