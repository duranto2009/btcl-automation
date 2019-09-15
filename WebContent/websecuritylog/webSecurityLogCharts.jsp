<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.RequestFailureException"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.LoginDTO"%>
<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);


	if(!loginDTO.getIsAdmin())
	{
		new CommonActionStatusDTO().setErrorMessage( "You don't have permission to see this page" , false, request );
		throw new RequestFailureException( "You don't have permission to see this page" );
	}
%>

<%
  request.setAttribute("menu","userAndRoleManagement"); 
  request.setAttribute("subMenu1","webSecurityLog");
  request.setAttribute("subMenu2","webSecurityLogCharts");
%>
<jsp:include page="../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Web Security Log Charts" /> 
	<jsp:param name="body" value="../websecuritylog/webSecurityLogChartsBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 