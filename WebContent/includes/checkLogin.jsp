<%@ page import="sessionmanager.SessionConstants" %><%
	String context = "../../.."  + request.getContextPath() + "/";
    login.LoginDTO loginDTO = (login.LoginDTO)request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
//   if(loginDTO == null)
//   {
//   	 response.sendRedirect(context + "home/login.jsp");
//          return;
//   }
//   login.LoginService common = new login.LoginService();

//   loginDTO = common.validateUser(loginDTO);
//   if(loginDTO == null)
//   {
//   	 //response.sendRedirect("../");
//   	 response.sendRedirect(context + "home/login.jsp");
//          return;
//   }
//   request.getSession(true).setAttribute(SessionConstants.USER_LOGIN,loginDTO);
  %>
