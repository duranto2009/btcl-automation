<!DOCTYPE html>

<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
	<%@page contentType="text/html;charset=utf-8" %>

<%
	String context = request.getContextPath() + "/";
	String pluginsContext = context +"assets/global/plugins/";
    LoginDTO loginDTO = (LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
    
    request.setAttribute("context", context);
    request.setAttribute("pluginsContext",pluginsContext);
    request.setAttribute("loginDTO",loginDTO);
    request.setAttribute("isCDGM", loginDTO.getRoleID() == 22021);
    
    boolean nonAdmin = !loginDTO.getIsAdmin();
    long id = (nonAdmin ? loginDTO.getAccountID() : loginDTO.getUserID());
    String name = loginDTO.getUsername();
    int regisTrantType = -1;
    if(loginDTO.getUserID()<0){ // In this case user is a client -_-
        //set the registrant type for lli client
         regisTrantType = AllClientRepository.getInstance().getRegistrantTypeByClientId(id);
    }
//    String registrantType = loginDTO.getUserID();
%>
<html lang="en">
<head>
<meta charset="utf-8" />
<link rel="shortcut icon" type="image/x-icon" href="${context}favicon.ico" />
<title>BTCL | <%=request.getParameter("title") %></title>

<%@ include file="../skeleton_btcl/head-2018.jsp"%>


<%
String[] cssStr = request.getParameterValues("css");
for(int i = 0; ArrayUtils.isNotEmpty(cssStr) &&i < cssStr.length;i++){%>
<link href="${context}<%=cssStr[i]%>" rel="stylesheet" type="text/css" />
<%}%>
<style>
  [v-cloak] {
    display: none;
  }
.spinner-loading::before{
	content: "\f110";
	display: flex;
	flex-direction: column;
	justify-content: center;
	width: 100%;
	text-align: center;
	padding: 20%;
	font-family: FontAwesome;
	font-style: normal;
	font-size: 5em;
	font-weight: normal;
	text-decoration: inherit;
	-webkit-animation: fa-spin  infinite;
	-moz-animation: fa-spin infinite;
	animation: fa-spin 2s infinite linear;
	}
</style>
<script type="text/javascript">
	var context = '${context}';
 	var pluginsContext = '${pluginsContext}';
 	var isClientLoggedIn = "true" == "<%=nonAdmin%>";
 	var loggedInAccount = {
 			ID : '<%=id%>',
 			label : '<%=name%>',
			registrantType: <%=regisTrantType%>,

 	};
    //start: added by forhad
	function errorMessage(msg) {
		toastr.options.timeOut = 3000;
		toastr.error(msg, "Failure");
		return;
	}

	function successMessage(msg) {
		toastr.options.timeOut = 3000;
		toastr.success(msg, "Success");
	}
</script>
</head>

<%
List<String > menuList = new ArrayList<String>();
if(request.getAttribute("menu")!=null){
    menuList.add("'"+request.getAttribute("menu")+"'");
}
if(request.getAttribute("subMenu1")!=null){
    menuList.add("'"+request.getAttribute("subMenu1")+"'");
}
if(request.getAttribute("subMenu2")!=null){
    menuList.add("'"+request.getAttribute("subMenu2")+"'");
}
if(request.getAttribute("subMenu3")!=null){
    menuList.add("'"+request.getAttribute("subMenu3")+"'");
}
if(request.getAttribute("subMenu4")!=null){
    menuList.add("'"+request.getAttribute("subMenu4")+"'");
}
String fullMenu = String.join(",",menuList);

%>

<body class="page-container-bg-solid page-header-fixed page-sidebar-closed-hide-logo"  onload="activateMenu(<%=fullMenu%>)">
	<div id="fakeLoader"></div>
	
	<div class="page-header navbar navbar-fixed-top">
		<%@ include file="../skeleton_btcl/header.jsp"%>
	</div>

	<div class="clearfix"></div>

	<div class="page-container">
		<%if(loginDTO.getUserID() > 0){%>
			<%@ include file="../skeleton_btcl/menu.jsp" %>
		<%}else{%>
			<%@ include file="../skeleton_btcl/menu_client.jsp"%>
		<%}%>

		<div class="page-content-wrapper">
			<div class="page-content">
				<%-- <jsp:include page='../common/flushActionStatus.jsp' /> --%>
				<jsp:include page='<%=request.getParameter("body")%>' />
			</div>
		</div>
	</div>

	<%@ include file="../skeleton_btcl/footer.jsp"%>

	<%@ include file="../skeleton_btcl/includes.jsp"%>

	<%
	String[] helpers = request.getParameterValues("helpers");
	for(int i = 0; ArrayUtils.isNotEmpty(helpers)&& i < helpers.length;i++){%>
		<jsp:include page="<%=helpers[i] %>" flush="true">
			<jsp:param name="helper" value="<%=i %>" />
		</jsp:include>
	<%}%>
	
	<%
	String[] jsStr = request.getParameterValues("js");
	for(int i = 0; ArrayUtils.isNotEmpty(jsStr)&& i < jsStr.length;i++){%>
		<script src="${context}<%=jsStr[i]%>" type="text/javascript"></script>
	<%}%>
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
