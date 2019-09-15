<!DOCTYPE html>
<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="sessionmanager.SessionConstants" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page contentType="text/html;charset=utf-8" %>

<%
	/* String context = request.getContextPath() + "/"; */
	String context = "../../.."  + request.getContextPath() + "/";
	String pluginsContext = context +"assets/global/plugins/";
    LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
    request.setAttribute("context", context);
    request.setAttribute("pluginsContext",pluginsContext);
    request.setAttribute("loginDTO",loginDTO);
//	System.out.println("************************************************BODY" +request.getParameter("body"));
    
%>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
<html:base />
<meta charset="utf-8" />
<link rel="shortcut icon" type="image/x-icon" href="${context}favicon.ico" />
<title>BTCL | <%=request.getParameter("title") %></title>
<%@ include file="../skeleton_btcl/head.jsp"%>
<%
String[] cssStr = request.getParameterValues("css");
for(int i = 0; ArrayUtils.isNotEmpty(cssStr) &&i < cssStr.length;i++)
{
	%>
<link href="${context}<%=cssStr[i]%>" rel="stylesheet" type="text/css" />
	<%
}
%>
<style>
	[v-cloak] {
		display: none;
	}
</style>
<script type="text/javascript">
 var context = '${context}';
 var pluginsContext = '${pluginsContext}';
</script>
</head>
<!-- END HEAD -->
<%
String  fullMenu="'"+  request.getAttribute("menu")+"','"
		+ request.getAttribute("subMenu1")+"','"
		+ request.getAttribute("subMenu2");
		
if(request.getAttribute("subMenu3")!=null){
	fullMenu+="','"+request.getAttribute("subMenu3")+"'";
}else{
	fullMenu+="'";
}

%>

<body class="page-container-bg-solid page-header-fixed page-sidebar-closed-hide-logo"  onload="activateMenu(<%=fullMenu%>)">
	<!-- BEGIN HEADER -->
	<div id="fakeLoader"></div>
	
	<div class="page-header navbar navbar-fixed-top">
		<!-- BEGIN HEADER INNER -->
		<%@ include file="../skeleton_btcl/header.jsp"%>
		<!-- END HEADER INNER -->
	</div>
	<!-- END HEADER -->
	<!-- BEGIN HEADER & CONTENT DIVIDER -->
	<div class="clearfix"></div>
	<!-- END HEADER & CONTENT DIVIDER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<%if(loginDTO.getUserID() > 0){%>
			<%@ include file="../skeleton_btcl/menu.jsp"%>
		<%}else{%>
			<%@ include file="../skeleton_btcl/menu_client.jsp"%>
		<%}%>

		<!-- END SIDEBAR -->
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<div class="page-content">
				<!-- BEGIN PAGE BREADCRUMB -->
<!-- 				<ul class="page-breadcrumb breadcrumb"> -->
<!-- 					<li><a href="index.html">Home</a> <i class="fa fa-circle"></i></li> -->
<%-- 					<li><span class="active"><%=request.getParameter("title") %></span></li> --%>
<!-- 				</ul> -->
				<!-- END PAGE BREADCRUMB -->
				<!-- BEGIN PAGE BASE CONTENT -->
				<%-- <%@ include file="../skeleton/form.jsp"%> --%>
				<%--<jsp:include page='flushActionStatus.jsp' />--%>

				<jsp:include page='<%=request.getParameter("body")%>' />
				<!-- END PAGE BASE CONTENT -->
			</div>
			<!-- END CONTENT BODY -->
		</div>
		<!-- END CONTENT -->
	</div>

	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<%@ include file="../skeleton_btcl/footer.jsp"%>
	<!-- END FOOTER -->
	<%@ include file="../skeleton_btcl/includes.jsp"%>
	<%
	String[] helpers = request.getParameterValues("helpers");
	for(int i = 0; ArrayUtils.isNotEmpty(helpers)&& i < helpers.length;i++)
	{
	%>
		<jsp:include page="<%=helpers[i] %>" flush="true">
			<jsp:param name="helper" value="<%=i %>" />
		</jsp:include>
	<% } %>
<%
String[] jsStr = request.getParameterValues("js");
for(int i = 0; ArrayUtils.isNotEmpty(jsStr)&& i < jsStr.length;i++)
{
%>
<script src="${context}<%=jsStr[i]%>" type="text/javascript"></script>
<%
}
%>
</body>
</html>

<!--
                   ,   __, ,
   _.._         )\/(,-' (-' `.__
  /_   `-.      )'_      ` _  (_    _.---._
 // \     `-. ,'   `-.    _\`.  `.,'   ,--.\
// -.\       `        `.  \`.   `/   ,'   ||
|| _ `\_         ___    )  )     \  /,-'  ||
||  `---\      ,'__ \   `,' ,--.  \/---. //
 \\  .---`.   / /  | |      |,-.\ |`-._ //
  `..___.'|   \ |,-| |      |_  )||\___//
    `.____/    \\\O| |      \o)// |____/
         /      `---/        \-'  \
         |        ,'|,--._.--')    \
         \       /   `n     n'\    /
          `.   `<   .::`-,-'::.) ,'
            `.   \-.____,^.   /,'
              `. ;`.,-V-.-.`v'
                \| \     ` \|\
                 ;  `-^---^-'/                BOOOOH..........................
                  `-.______,'
	-->
