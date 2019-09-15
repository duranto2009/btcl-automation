<%@page import="sessionmanager.SessionConstants"%>
<%@page import="common.CommonActionStatusDTO"%>
<%@page import="login.LoginDTO"%>
<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);

	if( loginDTO.getUserID() <= 0 ){
		
		new CommonActionStatusDTO().setErrorMessage( "You don't have permission to see this page" , false, request );
		response.sendRedirect("../");
		return;
	}
%>
<jsp:include page="../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Profile" /> 
	<jsp:param name="body" value="../profile/changePasswordAdminBody.jsp" />
	<jsp:param name="css" value="assets/pages/css/profile.min.css" />
	<jsp:param name="js" value="assets/pages/scripts/profile.min.js" />
	<jsp:param name="js" value="assets/global/plugins/jquery.sparkline.min.js" />
	<jsp:param name="js" value="assets/global/plugins/pwstrength.js" />
</jsp:include> 