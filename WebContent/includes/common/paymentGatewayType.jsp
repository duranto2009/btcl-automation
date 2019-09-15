<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@page import="common.payment.constants.PaymentConstants"%>
<%@page import="common.payment.PaymentDTO"%>
<%@page import="common.bill.BillDTO"%>
<%@ page language="java"%><%@ taglib uri="../../WEB-INF/struts-bean.tld"
	prefix="bean"%><%@ taglib uri="../../WEB-INF/struts-html.tld"
	prefix="html"%><%@ taglib uri="../../WEB-INF/struts-logic.tld"
	prefix="logic"%>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("paymentGatewayType");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Payment Gateway Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="paymentGatewayType" >
			<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
			<%for(Integer key: PaymentConstants.paymentGatewayIDNameMap.keySet()){ %>
				<option value="<%=key %>" <%if (requested.equals(key+"")) {%> selected <%}%>><%=PaymentConstants.paymentGatewayIDNameMap.get(key) %></option>
			<%} %>
		</select>
	</div>
</div>
