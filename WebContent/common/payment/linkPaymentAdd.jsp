<%@page import="common.ModuleConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.PermissionConstants"%>
<%@ page import="org.apache.struts.action.ActionRedirect" %>
<%@ page import="common.RequestFailureException" %>
<%@ include file="../../includes/checkLogin.jsp" %>
<%

boolean hasPermission = false;
int permission;

if( loginDTO.getUserID()>0 )
{
    if(loginDTO.getMenuPermission( PermissionConstants.BILL) != -1 
    		
      &&( loginDTO.getMenuPermission( PermissionConstants.BILL_BANK_PAYMENT ) >= PermissionConstants.PERMISSION_FULL ) ){
    	
        hasPermission=true;
    }
}

if( !hasPermission && loginDTO.getUserID()>0 ){

	
	CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
//
	commonActionStatusDTO.setErrorMessage( "You don't have permission to see this page" , true, request );
//
//	request.getRequestDispatcher("/SearchBill.do?moduleID=" + request.getParameter("moduleID")).forward(request, response);
	response.sendRedirect(request.getContextPath() + "bill/search.do?moduleID=" + request.getParameter("moduleID") );
//	throw new RequestFailureException("akljsdfh");

}

%>

<%
if((ModuleConstants.Module_ID_VPN+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","vpnMenu");
	request.setAttribute("subMenu1","vpnBillAndPayment");
	request.setAttribute("subMenu2","vpnBillSearch");
}else if((ModuleConstants.Module_ID_LLI+"").equals(request.getParameter("moduleID"))){
	request.setAttribute("menu","lli");
	request.setAttribute("subMenu1","lliBillAndPayment");
	request.setAttribute("subMenu2","lliBillSearch");
}
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Add Bank Payment" /> 
	<jsp:param name="body" value="../common/payment/linkPaymentAddBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 