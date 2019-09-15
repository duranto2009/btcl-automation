<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="help.HelpDTO"%>
<%@page import="complain.ComplainService"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%java.text.SimpleDateFormat help_date = new java.text.SimpleDateFormat ("yyyy-MM-dd"); %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page errorPage="../common/failure.jsp" %>


<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,complain.*" %>


<%
 String title = "Reply Form ";
 String submitCaption = "Send Reply";
 String actionName = "/AddHelpSolution";

%>
<%
    ArrayList helpList = (ArrayList) session.getAttribute("HelpList");
%>
<%
     String help_id=null;
     String sql = "select hshrdHelpID from adhelpsolution";

  java.util.HashMap replyMap = new java.util.HashMap();
      Connection connection = null;
      Statement stmt = null;
      connection = DatabaseManager.getInstance().getConnection();
      stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next())
      {
         help_id=rs.getString("hshrdHelpID");
         replyMap.put(help_id,help_id);
      }
      rs.close();
      stmt.close();
      DatabaseManager.getInstance().freeConnection(connection);


%>
<html><html:base />
  <head>
    <meta charset="UTF-8" />
    <title>BTCL | Complain View</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'/>
    <!-- Bootstrap 3.3.4 -->
    <link href="../assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <link href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
	
    <!-- Theme style -->
    <link href="../assets/css/layout.css" rel="stylesheet" type="text/css" />
    <link href="../assets/css/skin.css" rel="stylesheet" type="text/css" />
	<link href="../assets/css/custom.css" rel="stylesheet" type="text/css" />
	
	 <!-- REQUIRED JS SCRIPTS -->
    <script src="../assets/js/jQuery-2.1.4.min.js"></script>
    <script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="../assets/js/app.min.js" type="text/javascript"></script>
    <script src="../assets/js/util.js" type="text/javascript"></script>
	

	
	<style type="text/css">
		
		.btn {
    
    border: 0 none;
    font-weight: 700;
    letter-spacing: 1px;
    text-transform: uppercase;
    height: 40px;
}
 
.btn:focus, .btn:active:focus, .btn.active:focus {
    outline: 0 none;
}
 
.btn-primary {
    background: #47d147;
    color: #ffffff;
}
 
.btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open > .dropdown-toggle.btn-primary {
    background: #29a329;
}
 
.btn-primary:active, .btn-primary.active {
    background: #007299;
    box-shadow: none;
}

.panel {
	border: 0 none;
}
.panel-body{
border: 0 none;
}

.panel-primary > .panel-heading {
    background-color: #337ab7;
    border-color: #aeeaae;
    color: #fff;
}
		</style>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body onload="activateMenu('webhosting_parent','webhosting','package')" class="layout-skin sidebar-mini">
    <div class="wrapper">

      <!-- 1. ADD Main Header (top header) -->
      <!-- 2. ADD Left side column. contains the logo and sidebar -->
      <!-- 3. Content Wrapper. Contains page content -->
      <%@ include file="../includes/responsive/top-header.jsp" %>
      <%if(loginDTO.getUserID() > 0){%>      
      <%@ include file="../includes/responsive/left-menu.html" %>
      <%}else{%>
      <%@ include file="../includes/responsive/left-menu_client.html" %>
      <%}%>      
      <div class="content-wrapper">
        <section class="content-header">
          <h1>
            Complain View
            <!-- <small>Optional description</small> -->
          </h1>
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
            <li class="active">Here</li>
          </ol>
        </section>

	
		
		
		
        <!-- Main content -->
        
        <section class="content">
			<div class="row">
			<div class="col-lg-offset-3 col-lg-6">
				<div class="panel panel-success">
					
						<div class="panel-heading" style="text-align: center; font-size: 14pt; background-color: #aeeaae;"><div class="div_title">Complain View</div></div>
					
					<div class="panel-body">
						
							<html:form styleClass="form-horizontal"  action="<%=actionName%>" method="POST" onsubmit="return validate();">
							
                    <%
						String token = (String)request.getSession(true).getAttribute("id");
						String perticularID = (String)request.getSession(true).getAttribute("perticularID");
						
						String complain=null;							
						 Connection connection1 = DatabaseManager.getInstance().getConnection();
						 Statement stmt1 = connection1.createStatement();
						 ResultSet resultSet1 = stmt1.executeQuery("select hrdHelpDesc from adhelprequestdetail where hrdHelpID=" + perticularID);
						 while(resultSet1.next())
						 {
						   complain=resultSet1.getString("hrdHelpDesc");
						 }
						 resultSet1.close();
						 stmt1.close();
						 DatabaseManager.getInstance().freeConnection(connection1);

					%>							
							<input name="helpID" type ="hidden" value="<%=perticularID %>"  /><%-- <%=token %> --%>
							<input  name="complain"  type ="hidden" value="<%=complain %>" /><%-- <%=complain %> --%>
							<input name="userID" type ="hidden"   value="<%=loginDTO.getUserID()%>"/></TD>
				 		<%						
				  String msg = null;
				  if( (msg = (String)session.getAttribute(util.ConformationMessage.HELP_ADD))!= null)
				  {
				    session.removeAttribute(util.ConformationMessage.HELP_ADD);
				    %>
						
                                <div style="margin: 0 0 2px;" class="row">
                                    <b class="green_text"><%=msg%></b>
                                </div>
                            <%}%>


	  					                            	
                            	<div class="panel panel-info">
                            	<div class="panel-heading"> Summary 
                            	</div>
                            	<div class="panel-body">
                            	Token Code: <%=token%><br/>
                            	<input type="hidden" name="tokenCode" value="<%=token%>"/>
                      <%
                    	String subject = (String)request.getSession(true).getAttribute("subject");
                    	%>
                    	Complain description: <%=complain%>
                            	</div></div>	  					                            	
	  					                  
	  					                  
	  					          <div class="table-responsive">                  	
	  					         <table id="search_table" class="table table-bordered">                   	

                          <%
						    for( int i=0 ; i < helpList.size();i++)
						    {
						      HelpDTO dto = (HelpDTO) helpList.get(i);
						
							  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
							  String date = formatter.format(new java.util.Date(dto.getRequestTime()));
							  
							 					 
							  %>
							  
                          <tr> 
                            <td class="td_viewdata1"  align="left"width="73">Date:<%=date%> 
                              <input name="helpID2" type ="hidden" value="<%=dto.m_helpID %>" /> 
                            </td>
                          </tr>
                          <tr> 
                            <td class="td_viewdata2"  align="left"width="73">Complain</td>
                            <td  align="left" class="td_viewdata2"><%=dto.m_helpDesc%></td>
                            <td  align="left" >&nbsp;</td>
                          </tr>
                          <tr> 
                            <td class="td_viewdata2"  align="left"width="73">Reply</td>
                            <td class="td_viewdata2"  align="left"width="333"><%=dto.m_replyDesc%></td>
                            <%if(replyMap.get(dto.m_helpID) == null)
							  {
							  %>
                            <td align="left"width="66"><a href="../GetReplyPerticular.do?id=<%=dto.m_helpID%>" ><font size="-2">Send 
                              Reply</font></a></td>
                            <%}%>
                          </tr>
                          <tr> 
                            <td   align="left"width="73"></td>
                            <td colspan="2"   align="left"></td>
                          </tr>
                          <%  }
                          
                          

%>
                        </table>
                        </div>
						<div class="panel palen-info">
						<div class="panel-heading">Reply</div>
						<div class="panel-body">
						<!-- <textarea style="width:100%" rows="6"  name="helpDesc"></textarea> -->
						<textarea cols="50" rows="6"  name="replyDesc" /></textarea>
						<input name="replyTime" type ="hidden" value="<%=help_date.format(new java.util.Date())%>"/>						
						</div>
						</div>
	  					                            	                          	
                            	

								
								
								<div class="form-group">
									<div
										class="col-md-offset-4 col-sm-offset-4 col-xs-offset-4 col-md-8 col-sm-8 col-xs-8">
										<div class="input-group">
											
										    <input class="btn btn-default"  type="reset" value="Reset">
										    <input class="btn btn-success"  type="submit" value="Send Reply">
										    <%-- <a class="btn btn-success"  type="submit" href="../GetHelpYes.do?id=<%=token%>"> Submit </a> --%>
										</div>
									</div>
								</div>
								

							</html:form>
						
					</div>
				</div>
			</div>





			</div>
        </section>
		
      </div><!-- /.content-wrapper -->
	  
     <%@ include file="../includes/responsive/footer.html"%>
      

    </div><!-- ./wrapper -->

  </body>
</html>