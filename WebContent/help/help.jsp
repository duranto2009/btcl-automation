<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="client.ClientRepository"%>
<%@page import="complain.ComplainService"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@page errorPage="../common/failure.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,
         sessionmanager.SessionConstants,
         help.*" %>
<html><html:base/>
  <head>
    <meta charset="UTF-8"/>
    <title>BTCL | Help Desk</title>
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

  </head>

  <body onload="activateMenu('help_parent','help','help_search')" class="layout-skin sidebar-mini">
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
            Help Desk
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
<%
	String url = "ViewHelp";
	String navigator = SessionConstants.NAV_HELP;
%>
<jsp:include page="../includes/nav.jsp" flush="true">
	<jsp:param name="url" value="<%=url %>"/>
	<jsp:param name="navigator" value="<%=navigator %>" />
</jsp:include>

		
		<!-- Search Result Table -->
		<div class="col-md-12 search-table-div">	
			<div class="table-responsive">			
				<html:form action="/DropHelp.do" method="POST">
				
<!-- 					<input type="hidden" value="" name="actionType"/>
					<input type="hidden" value="" name="type"/> -->
					
					<table id="search_table" class="table table-bordered">
						<%-- <caption class="text-center">
						<%if(loginDTO.getUserID() > 0){%>						
								<input type="submit" class="btn btn-success" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_APPROVED%>" value="Approve" name="Approve">								
								<input type="submit" class="btn btn-danger" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_REJECT%>" value="Reject" name="Reject">
								<input type="submit" class="btn btn-warning" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_BLOCK %>" value="Block" name="Block">
								<input type="submit" class="btn btn-success" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_ACTIVE%>" value="Activate" name="Activate">								
						<%}else{%>
								<input type="submit" class="btn btn-success" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_PAID%>" value="Pay" name="Pay">								
								<input type="submit" class="btn btn-danger" onclick="document.forms[3].actionType.value=<%=HostingConstants.STATUS_CANCEL%>" value="Cancel" name="Cancel">						
						<%}%>
								<input type="button" class="btn btn-primary" value="Download" name="Download">
						</caption> --%> 
						<thead>
							 
							 
                    <tr>
                      <td class="td_viewheader"  align="center"   >Token Code</td>
                      <td class="td_viewheader"  align="center"   >Subject</td>
                      <td class="td_viewheader"  align="center"   >Detail 
                      </td>
                      <!--Need menu permission		------->
                    </tr>							 
							 
						</thead>
						<tbody>
					<%                           
                      	ArrayList data = (ArrayList)session.getAttribute(SessionConstants.VIEW_HELP);

						if( data!= null)
						{
							int size = data.size();
				
							for(int i= 0 ; i < size; i++){
				
								HelpDTO row = (HelpDTO)data.get(i);
				
						%>
				                    <tr>
				                      <td class="td_viewdata1"  align="center" width="143" ><%=row.getTokenCode() %>&nbsp;</td>
				                      <td class="td_viewdata2 " align="center" width="333" ><%=row.getHelpSub() %>&nbsp;</td>
				                      <td class="td_viewdata1"  align="center" width="55" ><a href="../GetHelp.do?id=<%=row.getTokenCode() %>" >View</a></td>
				                      <!---need permision---------->
				                    </tr>
				                    <%
				            }
				          }
				          %>
	  

						</tbody>
					</table>
				</html:form>	
			</div>
		</div><!-- /.Search Result Table -->

		</div>
        </section>
		
      </div><!-- /.content-wrapper -->
	  
     <!--4. ADD FOOTER FILE-->
      <%@ include file="../includes/responsive/footer.html" %>

    </div><!-- ./wrapper -->

  </body>
</html>