<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="login.PermissionConstants"%>
<%@include file="../includes/checkLogin.jsp"%>
<%@page language="java"%>
<%@taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page
	import="java.util.ArrayList,sessionmanager.SessionConstants,vpn.client.*"%>

<%@ page errorPage="../common/failure.jsp" %>
<html>
<head>
<html:base />
<title>VPN Client</title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>
<script language="JavaScript">

function CheckAllBlackList()
{
    var form = document.forms[2];
    if  (form.Check_All_BlackList.checked == true)
    {
       
           for(i=0; i<form.blackListedIDs.length; i++)
            if(form.blackListedIDs[i].disabled == false)
                form.blackListedIDs[i].checked = true;
    }
    else
    {
        for(i=0; i<form.blackListedIDs.length; i++)
            if(form.blackListedIDs[i].disabled == false)
                form.blackListedIDs[i].checked = false;
    }
    return true;
}

            function CheckAll()
            {
                var form = document.forms[2];
                if  (form.Check_All.checked == true)
                {
                    form.Check_All_App.checked = false;	  
                    form.Check_All_Den.checked = false;
                    CheckAllApp();
                    CheckAllDen();
	  
                    for(i=0; i<form.deletedIDs.length; i++)
                        if(form.deletedIDs[i].disabled == false)
                            form.deletedIDs[i].checked = true;
                }
                else
                {
                    for(i=0; i<form.deletedIDs.length; i++)
                        if(form.deletedIDs[i].disabled == false)
                            form.deletedIDs[i].checked = false;
                }
                return true;
            }
            function CheckAllApp()
            {
                var form = document.forms[2];
                if  (form.Check_All_App.checked == true)
                {
                    form.Check_All.checked = false;	  
                    form.Check_All_Den.checked = false;
                    CheckAll();
                    CheckAllDen();
                    for(i=0; i<form.approvedIDs.length; i++)
                        if(form.approvedIDs[i].disabled == false)
                            form.approvedIDs[i].checked = true;
                }
                else
                {
                    for(i=0; i<form.approvedIDs.length; i++)
                        if(form.approvedIDs[i].disabled == false)
                            form.approvedIDs[i].checked = false;
                }
                return true;
            }
            function CheckAllDen()
            {
                var form = document.forms[2];
                if  (form.Check_All_Den.checked == true)
                {
                    if(form.Check_All_App != null)
                    {
                        form.Check_All.checked = false;
                        form.Check_All_App.checked = false;
                        CheckAll();
                        CheckAllApp();
                    }
	  

                    else
                    {
                        form.Check_All_Ver.checked == false;
                        CheckAllVer();
                    }
	 
	 
                    for(i=0; i<form.deniedIDs.length; i++)
                        if(form.deniedIDs[i].disabled == false)
                            form.deniedIDs[i].checked = true;
                }
                else
                {
                    for(i=0; i<form.deniedIDs.length; i++)
                        if(form.deniedIDs[i].disabled == false)
                            form.deniedIDs[i].checked = false;
                }
                return true;
            }
            function CheckAllVer()
            {
                var form = document.forms[2];
	
                if  (form.Check_All_Ver.checked == true)
                {
                    form.Check_All_Den.checked == false;
                    CheckAllDen();
                    for(i=0; i<form.verifiedIDs.length; i++)
                        if(form.verifiedIDs[i].disabled == false)
                            form.verifiedIDs[i].checked = true;
                }
                else
                {
                    for(i=0; i<form.verifiedIDs.length; i++)
                        if(form.verifiedIDs[i].disabled == false)
                            form.verifiedIDs[i].checked = false;
                }
                return true;
            }

            function handleCheckBoxes(u,v)
            {
                var form = document.forms[2];
                if(v == 1)
                {
                    form.Check_All_App.checked = false;
                    if (document.getElementById("approved"+u).checked)
                    {
                        document.getElementById("denied"+u).checked = false;
                        document.getElementById("deleted"+u).checked = false;
                    }
                }
	
                else if(v == 2)
                {
                    form.Check_All_Den.checked = false;
                    if (document.getElementById("denied"+u).checked)
                    {
                        document.getElementById("approved"+u).checked = false;
                        document.getElementById("deleted"+u).checked = false;
                    }
                }
	
                else if(v == 3)
                {
                    form.Check_All_Ver.checked = false;
                    if (document.getElementById("verified"+u).checked)
                    {			
                        document.getElementById("denied"+u).checked = false;
                    }
                }
	
                else if(v == 4)
                {
                    form.Check_All.checked = false;
                    if (document.getElementById("deleted"+u).checked)
                    {
                        document.getElementById("approved"+u).checked = false;
                        document.getElementById("denied"+u).checked = false;
                    }
	
                }
	
                else if(v == 5)
                {
                    form.Check_All_Den.checked = false;
                    if (document.getElementById("denied"+u).checked)
                    {			
                        document.getElementById("verified"+u).checked = false;
                    }
                }
	
	
                return true;
	
            }
            function alertCalled()
            {
            	//alert("Alert");
            }
        </script>
</head>
<body class="body_center_align" onload="alertCalled();">
	<table border="0" cellpadding="0" cellspacing="0" width="780"
		id="AutoNumber1">
		<tr>
			<td width="100%"><%@include file="../includes/header.jsp"%>
			</td>
		</tr>
		<tr>
			<td width="100%">
				<table border="0" cellpadding="0" cellspacing="0" width="780"
					id="AutoNumber2">
					<tr>
						
						<td width="600" valign="top" class="td_main" align="center">
							<table border="0" cellpadding="0" cellspacing="0" width="100%"
								id="AutoNumber3">
								<tr>
									<td width="100%" align="right"
										style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
										<div class="div_title">
											<!--Client-->
											VPN Client
										</div>
									</td>
								</tr>
								<%
								System.out.println("here");
                                        String msg = null;
                                        if ((msg = (String) session.getAttribute(util.ConformationMessage.CLIENT_UPDATE)) != null) {
                                            session.removeAttribute(util.ConformationMessage.CLIENT_UPDATE);                                            
                                    %>
								<tr>
									<td width="100%" align="center" valign="top" height="25">
										<b><%=msg%> </b>
									</td>
								</tr>
								<%  }System.out.println("here2");%>
								<tr>
									<td width="100%" align="center"><br /> <!-- search and navigation control-->
										<%
                                                String url = "ViewVPNClient";
                                                String navigator = SessionConstants.NAV_VPNCLIENT;
                                                System.out.println("here3");
                                            %> <jsp:include page="../includes/nav.jsp" flush="true">
											<jsp:param name="url" value="<%=url%>" />
											<jsp:param name="navigator" value="<%=navigator%>" />
										</jsp:include> <!-- search and navigation control --> <!-- start of the form -->
										<%System.out.println("here4"); %>
										<html:form action="/DropClient" method="POST">
											<table width="595" class="table_view">
												<tr>
													<td class="td_viewheader" align="center" valign="top"
														width="110" height="25">
														<!--Customer ID--> ADSL Phone Number
													</td>



													<td class="td_viewheader" align="center" valign="top"
														width="70" height="25">
														<!--Payment Type--> Client ID
													</td>																										



													<td class="td_viewheader" align="center" valign="top"
														width="70" height="25">
														<!--Account Status--> Account Status
													</td>
													
													

													<td class="td_viewheader" align="center" valign="top"
														width="50" height="25">
														<!--Edit--> View/Edit &amp; Summary
													</td>
                            						<%
                            						int permission = loginDTO.getMenuPermission(login.PermissionConstants.MANAGE_CLIENTS_SEARCH);
                            						System.out.println("permission " + permission);
                            						System.out.println("loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE" + loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE));
                            						if (permission == 3 && loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) {%>
													<td class="td_viewheader" align="center" valign="top"
														width="55" height="25"><input type="submit"
														value="Delete"><input type="checkbox"
														name="Check_All" onClick="CheckAll();" value="CheckAll">
													</td>
													<%}%>					
													
												</tr>
												<%
                                                        ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_VPNCLIENT);
														if(data == null)System.out.println("data is null");
														else System.out.println("data is not null");
                                                        if (data != null) {
                                                            int size = data.size();
                                                            System.out.println("size:" + size);

                                                            for (int i = 0; i < size; i++) {
                                                                ClientDetailsDTO row = (ClientDetailsDTO) data.get(i);
                                                    %>
												<tr>
													<td class="td_viewdata1" align="center" width="110"
														height="15"><%=VPNClientRepository.getInstance().getClient(row.getUniqueID()).getPhoneNo()%></td>
													<td class="td_viewdata1" align="center" width="70"
														height="15"><%=VPNClientRepository.getInstance().getClient(row.getUniqueID()).getUserName()%>
													</td>

													<td class="td_viewdata1" align="center" width="70"
														height="15"><%=ClientConstants.CLIENTSTATUS_NAME[VPNClientRepository.getInstance().getClient(row.getUniqueID()).getAccountStatus() - 1]+ "("+(row.getBlackListed()==1?"Blacklisted":"Safe")+")"%>
													</td>
													
													


													<td class="td_viewdata1" align="center" width="50"
														height="15"><a
														href="../GetClient.do?id=<%=row.getUniqueID()%>"> <!--Edit-->
															<%=row.getAccountStatus() == 5 ? "View" : "Edit"%></a> &nbsp;<a target="_blank"
														href="../showClientSummary.do?id=<%=row.getUniqueID()%>">/Summary</a></td>

													
													<%--  </td>
                                                         <td class="td_viewdata2" align="center" width="50" height="15">
                                                           <a target="_blank" href="../clients/printableViewClientDetail.jsp?id=<%=row.getUniqueID() %>">View</a>
                                                         </td> --%>
													<%if (permission == 3 && loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) {%>

													<%if (row.getAccountStatus() == ClientConstants.CLIENTSTATUS_VALUE[4] ||row.getAdminDeleted()== 1) {%>
													<td class="td_viewdata1" align="center" width="55"
														height="15"><input type="checkbox" name="deletedIDs"
														id="<%="deleted" + row.getUniqueID()%>"
														value="<%=row.getUniqueID()%>"
														onclick="handleCheckBoxes(<%=row.getUniqueID()%>,4);"
														disabled="true"></td>
													<%} else {%>
													<td class="td_viewdata1" align="center" width="55"
														height="15"><input type="checkbox" name="deletedIDs"
														id="<%="deleted" + row.getUniqueID()%>"
														value="<%=row.getUniqueID()%>"
														onclick="handleCheckBoxes(<%=row.getUniqueID()%>,4);">
													</td>
													
													<%} %>													
													<%} %>
												</tr>
												<%
                                                            }
                                                        }
                                                    %>
											</table>
										</html:form> <br /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="100%"><%@include file="../includes/footer.jsp"%>
			</td>
		</tr>
	</table>
</body>
</html>
