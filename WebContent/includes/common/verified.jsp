<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@page import="common.payment.PaymentDTO"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("isVerified");
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Payment Verification Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="isVerified" >
		<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
		<option value="1" <%if (requested.equals("1")) {%> selected <%}%>>Verified</option>
		<option value="0" <%if (requested.equals("0")) {%> selected <%}%>>Not verified</option></select>
	</div>
</div>
