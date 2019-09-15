<%@page import="common.LanguageConstants"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<%
	LoginDTO localLoginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	if(localLoginDTO!=null){
		String username =localLoginDTO.getUsername();
		request.setAttribute("username", username);
	}
%>

<div class="page-header-inner ">

	<div class="page-logo">
       	<a href= "${context}dashboard.do"><img src="${context}assets/images/BTCL-small-Logo.png" alt="logo" class="logo-default" height=42 style="margin: 12px 10px 0 !important"/> </a>
       	<div class="menu-toggler sidebar-toggler"></div>
   	</div>
   	<a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse"> </a>
	
	
	<div class="page-top">


		<div class="top-menu">
			<ul class="nav navbar-nav pull-right">
				<li class="separator hide"> </li>
				<%@ include file="./notification.jsp"%>
				<li class="separator hide"> </li>

			</ul>
		</div>

		<div class="top-menu">
			<ul class="nav navbar-nav pull-right">
				<li class="separator hide"></li>
				<li class="dropdown dropdown-user dropdown-light">
					<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
						<span class="username username-hide-on-mobile">${username}</span>
						<img alt="" class="img-circle" src="<%=request.getContextPath() %>/ClientProfileInfo.do?type=showPicture" />
					</a>
					<ul class="dropdown-menu dropdown-menu-default">
						<%if(localLoginDTO.getIsAdmin()){ %>
						<li><a href="${context}profile/changePasswordAdmin.jsp"> <i class="icon-users"></i>Client password</a></li>
						<%}%>
						<li><a href="${context}ClientProfileInfo.do"> <i class="fa fa-key"></i><%=LanguageConstants.UPDATE %> password</a></li>
						<li class="divider"> </li>
						<li><a href="${context}Logout.do"> <i class="fa fa-arrow-right"></i>Log Out</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>

