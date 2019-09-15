<%@page import="java.util.List"%>
<%@page import="role.RoleService"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="permission.PermissionRepository"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="databasemanager.DatabaseManager"%>
<%@page import="role.RoleDTO"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%



	Logger logger = Logger.getLogger("roleListAjax");
   	response.setContentType("application/json");
   	String name = request.getParameter("name");
   	login.LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
   	List<RoleDTO> roles = ServiceDAOFactory.getService(RoleService.class).getDescendentRolesByPartialMatch(loginDTO, name);
    String searchList = new Gson().toJson(roles);
    response.getWriter().write(searchList);
				
%>