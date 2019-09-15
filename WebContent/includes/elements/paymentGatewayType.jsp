<%@page import="common.payment.constants.PaymentConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="user.UserDTO, java.util.ArrayList"%>    
<%

%> 
<div class="form-group">
	<label>Payment By</label>
	<select class="form-control select2" name="paymentGatewayList" style="width: 100%">
		<%for( Integer pgID: PaymentConstants.paymentGatewayIDNameMap.keySet()) {%>
			<option value='<%=pgID%>'><%=PaymentConstants.paymentGatewayIDNameMap.get(pgID)%></option>
		<%} %>
	</select>
</div>