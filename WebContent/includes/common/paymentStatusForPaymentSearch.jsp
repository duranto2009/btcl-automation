<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@page import="common.payment.constants.PaymentConstants"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("paymentStatus");
	/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Payment
		Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="paymentStatus" ><option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option><option
				value="<%=PaymentConstants.PAYMENT_STATUS_VERIFIED%>" <%if (requested.equals("1")) {%>
				selected <%}%>>Verify</option>
			<option
				value="<%=PaymentConstants.PAYMENT_STATUS_APPROVED%>" <%if (requested.equals("2")) {%>
				selected <%}%>>Approved</option>
			</select>
	</div>
</div>
