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
                                                String url = "ViewReply";
                                                String navigator = SessionConstants.NAV_REPLY;
                                            %>
                                            <jsp:include page="../includes/nav.jsp" flush="true">
                                                <jsp:param name="url" value="<%=url%>"/>
                                                <jsp:param name="navigator" value="<%=navigator%>" />
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
                                                        <td width="111" height="25"  align="center" class="td_viewheader" >Token Code</td>
                                                        <td class="td_viewheader"  align="center" >Client ID </td>
                                                        <td class="td_viewheader"  align="center" >Phone No. </td>
                                                        <td class="td_viewheader"  align="center" >Subject</td>
                                                        <td class="td_viewheader"  align="center" >Category</td>
                                                        <td class="td_viewheader"  align="center" >View</td>
                                                        <td class="td_viewheader"  align="center" ><input type="submit"  value="Close" > </td>
                                                        <td class="td_viewheader"  align="center" >Status</td>  					   

                                                        <!--Need menu permission		------->
                                                    </tr>							 
							 
						</thead>
						<tbody>
							                                                 
                                                     <%
                                                        ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_REPLY);
                                                        if (data != null) {
                                                            ComplainService complainService = new ComplainService();
                                                            int size = data.size();
                                                            for (int i = 0; i < size; i++) {
                                                                HelpDTO row = (HelpDTO) data.get(i);
                                                                String st = "" + HelpConstants.HELP_STATUS_VALUE_CLOSED;


                                                    %>
                                                    <tr> 
                                                        <td width="111" align="center" class="td_viewdata1" ><%=row.getTokenCode()%></td>
                                                        <td width="84" align="center" class="td_viewdata2" ><%=row.getCustomerID()%></td>
                                                        <td width="84" align="center" class="td_viewdata2" ><%=ClientRepository.getInstance().getClient(row.getAccountID()).getPhoneNo()%></td>
                                                        <td width="161" align="center" class="td_viewdata1 " ><%=row.getHelpSub()%></td>
                                                        <td width="161" align="center" class="td_viewdata1 " ><%=(complainService.getComplain("" + row.getCategoryID())).getComplainType()%></td>
                                                        <td class="td_viewdata2"  align="center" width="50" ><a href="../GetReply.do?id=<%=row.getTokenCode()%>" >View</a></td>


                                                        <%
                                                            if (row.getHelpStatus().equals("" + HelpConstants.HELP_STATUS_VALUE_CLOSED)) {
                                                        %>
                                                        <td class="td_viewdata1"  align="center" width="50" ><input type="checkbox" name="deleteIDs" value="<%=row.getTokenCode()%>" disabled = "true"></td>
                                                        <td class="td_viewdata2" align="left"width="46"><div align="center"><font size="-2">Closed</font></div></td>

                                                        <%
                                                            } else {
                                                        %>
                                                        <td class="td_viewdata1"  align="center" width="50" ><input type="checkbox" name="deleteIDs" value="<%=row.getTokenCode()%>" ></td>
                                                        <td class="td_viewdata2" align="left"width="46"><div align="center"><font size="-2">Open</font></div>
                                                        </td>
                                                        <% }%>
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