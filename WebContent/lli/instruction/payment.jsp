<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","common");
  request.setAttribute("subMenu2","lliInstructionPayment"); 
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Payment Instruction" /> 
	<jsp:param name="body" value="../lli/instruction/paymentBody.jsp" />
</jsp:include>