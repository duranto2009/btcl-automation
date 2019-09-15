<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="user.UserDTO"%>
<%@page import="user.UserRepository"%>
<%@page import="user.UserService"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="role.RoleDTO"%>
<%@page import="role.RoleService"%>
<%@page import="packages.PackageService"%>
<%@page import="report.ReportConfigurationDTO"%>
<%@page import="report.ReportOptions"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<html>
    <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
        <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
            <script  src="../scripts/util.js" type="text/javascript"></script>
            
            <script language="JavaScript">
                function generateReport()
                {
                    var option=document.createElement("option");
                    option.text = "UserName";
                    option.value = "UserName";
                    try
                    {
                        // for IE earlier than version 8
                        document.getElementsByName("columnsSelected")[0].add(option,document.getElementsByName("columnsSelected")[0].options[null]);
                    }
                    catch (e)
                    {
                        document.getElementsByName("columnsSelected")[0].add(option,null);
                    }
                    var option2=document.createElement("option");
                    option2.text = "Role";
                    option2.value = "Role";
                    try
                    {
                        // for IE earlier than version 8
                        document.getElementsByName("columnsSelected")[0].add(option2,document.getElementsByName("columnsSelected")[0].options[null]);
                    }
                    catch (e)
                    {
                        document.getElementsByName("columnsSelected")[0].add(option2,null);
                    }
                    var option3=document.createElement("option");
                    option3.text = "Full Name";
                    option3.value = "Full Name";
                    try
                    {
                        // for IE earlier than version 8
                        document.getElementsByName("columnsSelected")[0].add(option3,document.getElementsByName("columnsSelected")[0].options[null]);
                    }
                    catch (e)
                    {
                        document.getElementsByName("columnsSelected")[0].add(option3,null);
                    }
                    var option4=document.createElement("option");
                    option4.text = "Designation";
                    option4.value = "Designation";
                    try
                    {
                        // for IE earlier than version 8
                        document.getElementsByName("columnsSelected")[0].add(option4,document.getElementsByName("columnsSelected")[0].options[null]);
                    }
                    catch (e)
                    {
                        document.getElementsByName("columnsSelected")[0].add(option4,null);
                    }
                    var option5=document.createElement("option");
                    option5.text = "Email";
                    option5.value = "Email";
                    try
                    {
                        // for IE earlier than version 8
                        document.getElementsByName("columnsSelected")[0].add(option5,document.getElementsByName("columnsSelected")[0].options[null]);
                    }
                    catch (e)
                    {
                        document.getElementsByName("columnsSelected")[0].add(option5,null);
                    }
                    var option6=document.createElement("option");
                    option6.text = "Phone";
                    option6.value = "Phone";
                    try
                    {
                        // for IE earlier than version 8
                        document.getElementsByName("columnsSelected")[0].add(option6,document.getElementsByName("columnsSelected")[0].options[null]);
                    }
                    catch (e)
                    {
                        document.getElementsByName("columnsSelected")[0].add(option6,null);
                    }
                    for (var i = 0; i< document.getElementsByName("columnsSelected")[0].options.length; i++)
                    {
                        document.getElementsByName("columnsSelected")[0].options[i].selected = true;
                    }
                    /*document.getElementsByName("adminName")[0].checked = true;
                   document.getElementsByName("role")[0].checked = true;*/
                }
                function validate()
                {
                    var reportTitle = document.getElementById("reportTitle");  
                    var startDate = document.getElementById("bwdtFromSelect");
                    var endDate = document.getElementById("bwdtToSelect");
                    if(!validateRequired(reportTitle.value))
                    {
                        alert("Please Enter Report Title");
                        reportTitle.value = "";
                        reportTitle.focus();
                        return false;
                    }
                    if(!validateRequired(startDate.value))
                    {
                        alert("Please Enter Start Date");
                    }
                    if(!validateRequired(endDate.value))
                    {
                        alert("Please Enter End Date");
                    }
                    return true;
                }
                function updateUser()
                {
//                    alert("updateUser");
                    var xmlhttp;
                    if (window.XMLHttpRequest)
                    {// code for IE7+, Firefox, Chrome, Opera, Safari
                        xmlhttp=new XMLHttpRequest();
                    }
                    else
                    {// code for IE6, IE5
                        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                    xmlhttp.onreadystatechange = function() {
                        console.log(xmlhttp.readyState + " " + xmlhttp.status);
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            xmlDoc = xmlhttp.responseText;
                            //alert(xmlDoc);
                            //alert(document.getElementById("userList").innerHTML);
                            document.getElementById("userList").innerHTML =  xmlDoc;
                            console.log(xmlDoc);
                        }
                    }
                    //xmlhttp.open("GET", "home/signup.jsp?type=country", true);
                    xmlhttp.open("GET", "../GetUserList.do?role=" + document.getElementsByName("roleSelect")[0].options[document.getElementsByName("roleSelect")[0].options.selectedIndex].value, true);
                    xmlhttp.send();
                }
            </script>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                    <title>System User Report</title>
                    <%SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");%>
            </head>
            <body  class="body_center_align" onload="updateUser()">
                <center>
                    <table border="0" cellpadding="0" cellspacing="0"  width="1024" id="AutoNumber1">
                        <tr>
                            <td width="100%"><%@ include file="../includes/header.jsp"  %></td>
                        </tr>
                        <tr>
                            <td width="100%">
                                <table border="0" cellpadding="0" cellspacing="0"  width="1024" id="AutoNumber2">
                                    <tr>

                                        <td width="1024" valign="top" class="td_main" align="center">
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                <tr>
                                                    <td width="1024" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                                                        <div class="div_title">Create Report on System User </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td width="100%" align="center" style="padding-left: 180px"><br/>
                                                        <html:form action="/SingleReport" method="POST" onsubmit = "return validate();">
                                                            <html:select property = "columnsSelected" size="10" multiple = "true" style="display:none;width: 100%">
                                                            </html:select> 

                                                            <table width="500" border="0" cellpadding="0" cellspacing="0" id = "AutoNumber3">
                                                                <tr>
                                                                    <td align="left" height="25"><h4>Report Title  </h4></td>

                                                                    <td align="left" height="25">
                                                                        <input type= "text" name ="reportTitle" id="reportTitle"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>

                                                                    <td align="left" height="25"><h4>Report Type </h4></td>
                                                                    <td align="left" height="25">
                                                                        <input type = "text" name="infoSectionSelected" style="display:none" value="System Admin"/> 
            									 		System User
                                                                    </td>
                                                                </tr>

                                                                <tr>
                                                                    <td align="left" height="25"><h4>Filters</h4></td>
                                                                    <td></td>
                                                                </tr>

                                                                <tr>
                                                                    <td align="left" height="25">
                                                                        <input type= "checkbox" name = "role" />
                    							Role 
                                                                    </td>
                                                                    <td align = "left" height="25">
                                                                        <html:select property="roleSelect" size="1" onchange="updateUser();">
                                                                            <html:option value="-1" >All</html:option>
                                                                            <%
                                                                                RoleService roleService = util.ServiceDAOFactory.getService(RoleService.class);
                                                                                ArrayList<RoleDTO> roles = (ArrayList<RoleDTO>) roleService.getDTOs(roleService.getIDs(loginDTO));
                                                                                for (int i = 0; i < roles.size(); i++) {
                                                                            %>
                                                                            <html:option value="<%=Long.toString(roles.get(i).getRoleID())%>" ><%=roles.get(i).getRoleName()%></html:option>
                                                                            
                                                                            <%}%>
                                                                        </html:select>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="left" height="25">
                                                                        <input type= "checkbox" name = "adminName"/>
                    							User Name
                                                                    </td>
                                                                    <td align = "left" height="25">
                                                                        <div id ="userList">
                                                                            <html:select property="adminNameSelect" size="1" >
                                                                                <html:option value="All" >All</html:option>
                                                                                <%
                                                                                    ArrayList<UserDTO> userList = UserRepository.getInstance().getUserList();

                                                                                    for (int i = 0; i < userList.size(); i++) {
                                                                                %>
                                                                                <html:option value="<%=Long.toString(userList.get(i).getUserID())%>" ><%=userList.get(i).getUsername()%></html:option>
                                                                                <%}%>
                                                                            </html:select>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="left" height="25" colspan="2" style="padding-left: 80px">
                                                                        <input type="submit" name="submit" value = "Generate Report >" onclick = "generateReport()"/>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </html:form>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td width="100%"><%@ include file="../includes/footer.jsp"%></td>
                        </tr>
                    </table>
                </center>
            </body>
            </html>
