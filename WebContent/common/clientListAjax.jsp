<%@page import="common.ClientDTO"%>
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

	Logger logger = Logger.getLogger("clientListAjax");
   	response.setContentType("application/json");
   	
	String sql;
	Connection cn=null;
	ResultSet rs = null;
	Statement stmt=null;
	ArrayList<ClientDTO> data = new ArrayList<ClientDTO>();
	String name = request.getParameter("name");    	
   	login.LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	try
	{
	    sql = "select * from at_client where clLoginName like '" + name + "%' limit 10";
	    cn = DatabaseManager.getInstance().getConnection();
	    stmt = cn.createStatement();
	    rs = stmt.executeQuery(sql);
	    while(rs.next()) 
	    {
	    	ClientDTO dto = new ClientDTO();
	    	dto.setClientID(rs.getLong("clID"));
	    	dto.setLoginName(rs.getString("clLoginName"));
	    	data.add(dto);
	    }	        	        
	}
	catch(Exception ex)
	{
	    logger.fatal("role.RoleDAO: ", ex);
	}
	finally
	{
	  try{ if (stmt != null) {stmt.close();}} catch (Exception e){}
	  try{ if (cn != null){ DatabaseManager.getInstance().freeConnection(cn); } } catch (Exception e){ }
	}
          String searchList = new Gson().toJson(data);
          response.getWriter().write(searchList);
				
%>