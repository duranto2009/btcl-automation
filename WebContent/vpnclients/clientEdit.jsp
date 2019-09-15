<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="user.UserDTO"%>
<%@page import="user.UserRepository"%>
<%@page import="client.form.ClientForm"%>
<%@page import="login.PermissionConstants"%>
<%@page import="org.apache.struts.Globals"%>

<%@ include file="../../includes/checkLogin.jsp"%>
<%@ page language="java"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page errorPage="../../common/failure.jsp" %>
<%@ page
	import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,packages.*,regiontype.*,java.sql.*,util.DAOResult,dslm.*,exchange.*"%>


<%
	String submitCaption = "Update Client";
	String actionName = "/UpdateClient";
	

	String id = (String) request.getSession(true).getAttribute("id");
	session.setAttribute("UPDATE_CLIENT_ID", id);

	
	
	ClientForm cf = (ClientForm) request.getAttribute("clientForm");
	ClientDTO cDTO = ClientRepository.getInstance().getClient(cf.getUniqueID());
	
	
%>

<%
	String months[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };

	DAOResult daoResult = new DAOResult();
	Connection cn = null;
	PreparedStatement ps = null;

	long exp_date = 0;
	long bonus_exp_date = 0;

	String sql;
	ResultSet rs;

	//end of test
%>


<html>
<head>
<html:base />
<title>Edit Client</title>
<link rel="stylesheet" type="text/css" href="../../stylesheets/styles.css">
	<script type="text/javascript" src="../../scripts/util.js"></script>




	<link class="jsbin"
		href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css"
		rel="stylesheet" type="text/css" />
	<script class="jsbin"
		src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
	<script class="jsbin"
		src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>

	<!--[if IE]>
      <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->


	<script type="text/javascript">
            function readURL(input) {
            	
            	//alert("I get a call:"+input.value);
                if (input.files && input.files[0]) {
                    var reader = new FileReader();

                    reader.onload = function (e) {
                        $('#blah')
                            .attr('src', e.target.result)
                            .width(225)
                            .height(275);
                    };

                    reader.readAsDataURL(input.files[0]);
                }
            }
            

            if (!String.prototype.trim) {
                String.prototype.trim = function() {
                    return this.replace(/^\s+|\s+$/g,'');
                }
            }

            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }	
            function validate()
            {
                document.getElementsByName("webloginPassword")[1].value = document.getElementsByName("webloginPassword")[0].value;
  	  
  	  
                var f = document.forms[0];
      
      
                var ob = f.userName;
      
                if(!validateRequired(ob.value))
                {
                    alert("Please Enter User Name");
                    ob.value = "";
                    ob.focus();
                    return false;
                }
      
                if(!document.getElementsByName("sameUserName")[0].checked)
                {
                    ob = f.webuserName[0];
                    if( !validateRequired(ob.value))
                    {
                        alert("Please Enter Web User Name");
                        ob.value = "";
                        ob.focus();
                        return false;
                    }
                }
      
                ob = f.loginPassword;
     
                if( !validateRequired(ob.value))
                {
                    alert("Please enter Login Password");
                    ob.value = "";
                    ob.focus();
                    return false;
                }
                ob = f.confirmLoginPassword;
                if( !validateRequired(ob.value))
                {
                    alert("Please re-enter Login Password");
                    ob.value = "";
                    ob.focus();
                    return false;
                }
                //match both password
                if( f.confirmLoginPassword.value != f.loginPassword.value)
                {
                    alert("Two Login Passwords do not match!!");
                    f.confirmLoginPassword.focus();
                    return false;
                }      
		
                if(!document.getElementsByName("samePassword")[0].checked)
                {
                    ob = f.webloginPassword[0];
                    if( !validateRequired(ob.value))
                    {
                        alert("Please enter Web Password");
                        ob.value = "";
                        ob.focus();
                        return false;
                    }
                    ob = f.confirmWebLoginPassword;
                    if( !validateRequired(ob.value))
                    {
                        alert("Please re-enter Web Password");
                        ob.value = "";
                        ob.focus();
                        return false;
                    }
			
                    //match both password
                    if( f.confirmWebLoginPassword.value != f.webloginPassword[0].value)
                    {
                        alert("Two Web Passwords do not match!!");
                        f.confirmWebLoginPassword.focus();
                        return false;
                    }
    
                }
                ob = f.dslmNo;
                if( !validateRequired(ob.value))
                {
                    alert("Please Enter DSLM Name");
                    ob.focus();
                    return false;
                }
      
                ob = f.portNo;
                if( !validateRequired(ob.value) || !isNum(ob.value))
                {
                    alert("Please Enter a valid Port Number");
                    ob.focus();
                    return false;
                }
     
                ob = f.referenceNo;
                if( !validateRequired(ob.value))
                {
                    alert("Please Enter a valid Reference Number");
                    ob.focus();
                    return false;
                }
      
		

                ob = f.balance;
                if( !validateRequired(ob.value))
                {
                    alert("Please Enter Balance");
                    ob.value = "";
                    ob.focus();
                    return false;
                }
     
                if( ob.value.length > 20)
                {
                    alert("Balance Must be less than 999999999999999999.99");
                    ob.value = "";
                    ob.focus();
                    return false;
                }
      
                ob = f.customerName;
                if( !validateRequired(ob.value))
                {
                    alert("Please Enter a valid Customer Name");
                    ob.focus();
                    return false;
                }
      
                ob = f.customerPhone;
                if( !validateRequired(ob.value))
                {
                    alert("Please Enter a valid Customer Phone No");
                    ob.focus();
                    return false;
                }
      
      
                ob = f.customerEmail;
                if( !validateRequired(ob.value))
                {
                    alert("Please Enter a valid Email Address");
                    ob.focus();
                    return false;
                }

                if(!validateEmail(ob.value))
                {
 	   
                    alert("The Email address provided is invalid.Please Enter a valid Email Address");
                    ob.focus();
                    return false;
                }
      
                ob = f.customerAddress;
                if( !validateRequired(ob.value))
                {
                    alert("Please Enter a valid Customer Address");
                    ob.focus();
                    return false;
                }

     
                return true;
            }
    
    
            function onlyNumbers(evt)
            {
                var e = event || evt; 
                var charCode = e.which || e.keyCode;

                if (charCode > 31 && (charCode < 48 || charCode > 57))
                    return false;

                return true;

            }
    
            function onclickSameUserName()
            {    	  
                if(document.getElementsByName("sameUserName")[0].checked)
                {
                    document.getElementsByName("webuserName")[0].value = document.getElementsByName("userName")[0].value;
                    document.getElementsByName("webuserName")[0].disabled = true;
                }
  	  
                else
                {
                    document.getElementsByName("webuserName")[0].disabled = false;
                }
  	  
                return true;
            }
    
            function onclickSamePassword()
            {    	  
                if(document.getElementsByName("samePassword")[0].checked)
                {
                    document.getElementsByName("webloginPassword")[0].value = document.getElementsByName("loginPassword")[0].value;
                    document.getElementsByName("confirmWebLoginPassword")[0].value = document.getElementsByName("confirmLoginPassword")[0].value;
                    document.getElementsByName("webloginPassword")[0].disabled = true;
                    document.getElementsByName("confirmWebLoginPassword")[0].disabled = true;
                    document.getElementById('webstrength').innerHTML = '<span></span';
                }
  	  
                else
                {
                    document.getElementsByName("webloginPassword")[0].disabled = false;
                    document.getElementsByName("confirmWebLoginPassword")[0].disabled = false;
                    passwordChanged('webloginPassword','webstrength') ;
                }
  	  
                return true;
            }
    
    
            function passwordChanged(name,place) 
            {
                if(document.getElementsByName("samePassword")[0].checked)
                {
                    setWebPassword();
                }
                var strength = document.getElementById(place);
                var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
                var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
                var enoughRegex = new RegExp("(?=.{6,}).*", "g");
                var pwd = document.getElementsByName(name)[0];
                if (pwd.value.length==0)
                {
                    strength.innerHTML = '<span style="color:red">&nbsp&nbsp&nbspPassword must not be empty</span>';
                } 
  	  
                else if (false == enoughRegex.test(pwd.value)) 
                {
                    strength.innerHTML = '<span style="color:red">&nbsp&nbsp&nbsp Very Weak!!</span>';
                } 
  	  
                else if (strongRegex.test(pwd.value)) 
                {
                    strength.innerHTML = '<span style="color:green">&nbsp&nbsp&nbspStrong!</span>';
                } 
  	  
                else if (mediumRegex.test(pwd.value)) 
                {
                    strength.innerHTML = '<span style="color:orange">&nbsp&nbsp&nbspMedium!</span>';
                }
  	  
                else
                {
                    strength.innerHTML = '<span style="color:red">&nbsp&nbsp&nbspWeak!</span>';
                }
            }
    
    
    
            function onClickReset()
            {
    	
                var strength = document.getElementById("strength");
                strength.innerHTML = '<span></span';
                strength = document.getElementById("webstrength");
                strength.innerHTML = '<span></span';
  	 
                return true;
            }
    
    
            function setWebUserName()
            {
                if(document.getElementsByName("sameUserName")[0].checked)
                {
                    document.getElementsByName("webuserName")[0].value = document.getElementsByName("userName")[0].value;
                }
  	  
                return true;
            }
    
            function setWebPassword()
            {
                if(document.getElementsByName("samePassword")[0].checked)
                {
                    document.getElementsByName("webloginPassword")[0].value = document.getElementsByName("loginPassword")[0].value;    		 
                }
  	  
                return true;
            }
    
            function setConfirmWebPassword()
            {
                if(document.getElementsByName("samePassword")[0].checked)
                {
                    document.getElementsByName("confirmWebLoginPassword")[0].value = document.getElementsByName("confirmLoginPassword")[0].value;    		 
                }
  	  
                return true;
            }
    
    
            function changePackageList(v)
            {
                var arg = v;
                // alert("I am Called" +arg);
                xmlHttp = GetXmlHttpObject();
                if(xmlHttp == null)
                {
                    alert("Browser is not supported.");
                    return false;
                } 
                var url="../../bonus/GetPackages.do?packageType="+arg+"&requester=client";
                //  alert(url);
                xmlHttp.onreadystatechange = PackageChanged;
                xmlHttp.open("GET",url,false);    	  	
                xmlHttp.send(null);    	  	
                return true;
            }
    
            function PackageChanged() 
            { 
  	 
                if (xmlHttp.readyState==4)
                { 
                    // alert(xmlHttp.responseText);
                    // alert(document.getElementById("packageDiv"));	
                    document.getElementById("packageDiv").innerHTML=xmlHttp.responseText;
                    // removeAll();
                }
       
                else
                {
                    //alert("Request failed.");
                }
            }
      
    
    
            function changeHandle()
            {
                v = document.getElementsByName("areaCode")[0].options[document.getElementsByName("areaCode")[0].selectedIndex].value;
                w = document.getElementsByName("phoneNo")[0].value;
                changeExchange(v,w);
            }
    
    
    
            function changeExchange(v,w)
            {
  	 
                // alert("I am Called" +v);
                if(v==null || w==null)
                {
                    return false;
                }
                xmlHttp = GetXmlHttpObject();
                if(xmlHttp == null)
                {
                    alert("Browser is not supported.");
                    return false;
                } 
                var url="../../dslm/GetExchange.do?areaCode="+v+"&adslPhone="+w;
                //  alert(url);
                xmlHttp.onreadystatechange = ExchangeChanged;
                xmlHttp.open("GET",url,false);    	  	
                xmlHttp.send(null);    	  	
                return true;
            }
    
            function ExchangeChanged() 
            { 
  	 
                if (xmlHttp.readyState==4)
                { 
                    //alert(xmlHttp.responseText);
                    //	 alert(document.getElementById("packageDiv"));
        
        	 
                    if(xmlHttp.responseText.substr(0,31) == "<select name = 'dslmExchangeNo'")
                        document.getElementById("exchange").innerHTML=xmlHttp.responseText;
                    if(document.getElementsByName("dslmExchangeNo")[0].options.length>0)
                        changeDslm(document.getElementsByName("dslmExchangeNo")[0].options[0].value);
                    // removeAll();
                }
       
                else
                {
                    //alert("Request failed.");
                }
            }
    
    
            function changeDslm(v)
            {
  	 
                //  alert("I am Called" +v);
                xmlHttp = GetXmlHttpObject();
                if(xmlHttp == null)
                {
                    alert("Browser is not supported.");
                    return false;
                } 
                var url="../../dslm/GetDslms.do?exCode="+v;
                // alert(url);
                xmlHttp.onreadystatechange = DslmChanged;
                xmlHttp.open("GET",url,false);    	  	
                xmlHttp.send(null);    	  	
                return true;
            }
    
            function DslmChanged() 
            { 
  	 
                if (xmlHttp.readyState==4)
                { 
                    // alert(xmlHttp.responseText);
                    // alert(document.getElementById("packageDiv"));	
        	 
                    document.getElementById("dslm").innerHTML=xmlHttp.responseText;
                    // removeAll();
                }
       
                else
                {
                    //alert("Request failed.");
                }
            }

            
            function goBack()
            {
            	window.history.back()
            }


        </script>
</head>
<%
	if (loginDTO.getUserID() > 0) {
%><body class="body_center_align"
	>
	<%
		} else {
	%><body class="body_center_align">
		<%
			}
		%>
		<table border="0" cellpadding="0" cellspacing="0" width="780"
			id="AutoNumber1">
			
			<tr>
				<td width="100%"><%@ include file="../../includes/header.jsp"%></td>
			</tr>
			
			<tr>
				<td width="100%"><table border="0" cellpadding="0"
						cellspacing="0" width="780">
						<tr>

							<td width="600" valign="top" class="td_main" align="center">
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
									<tr>
										<td width="100%" align="right"
											style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
											<div class="div_title">Edit Client</div>
										</td>
									</tr>
									<tr>
										<td width="100%" align="center"><br /> <html:form
												action="<%=actionName%>" enctype="multipart/form-data"
												method="POST" >
												
												<%
													
												    
														String acstatus = cDTO.getAccountStatus() + "";
														String pid = cDTO.getRateID() + "";
														String ptype = cDTO.getPaymentType() + "";
														String aps = cDTO.getApplicationStatus() + "";
														String bl = cDTO.getBalance() + "";
														String toc = cDTO.getTypeofConnection() + "";
														String arc = cDTO.getAreaCode() + "";
														String ds = cDTO.getDslmNo() + "";
														String ccstatus = cDTO.getConnected() + "";
														int black = cDTO.getBlackListed();
														cf.setAccountStatus(cDTO.getAccountStatus());
														cf.setApplicationStatus(cDTO.getApplicationStatus());
														cf.setBalance(cDTO.getBalance());
														cf.setBlackListed(cDTO.getBlackListed());
														cf.setConnected(cDTO.getConnected());
														cf.setReferenceNo(cDTO.getReferenceNo());
														if(cf.getApplicationStatus()!=ClientDTO.APPLICATION_STATUS_SUBMITTED)
														{
															cf.setPaymentType(cDTO.getPaymentType());
															cf.setRateID(cDTO.getRateID());
															cf.setAreaCode(cDTO.getAreaCode());
															cf.setPhoneNo(cDTO.getPhoneNo());
														}
												%>

												<table class="form1">
													<tr>
														<td colspan="2"><img id="blah"
															src="../../getCilentImage.do?id=<bean:write name="clientForm" property = "uniqueID" />"
															height="250px" width="225px" /></td>
													</tr>
													
													<%
													if (!acstatus
																.equals("" + ClientConstants.CLIENTSTATUS_VALUE[4])) {
												%>
													
													<tr>
														<td style="padding-right: 0; font-weight: bold"
															height="16" align="left">Upload New Image</td>
														<td align="left"><html:file property="myImage"
																onchange="readURL(this);" /></td>
													</tr>
													<%} %>
												</table>
												
												
												<%
													if (!acstatus
																.equals("" + ClientConstants.CLIENTSTATUS_VALUE[4])) {
												%>

												<table border="0" cellpadding="0" cellspacing="0"
													class="form1" width="500">
													<tr>
														<td height="22" colspan="2" align="left">&nbsp;</td>
													</tr>
													<tr>
														<td
															style="padding-left: 50px; padding-right: 0; font-weight: bold"
															height="16" colspan="2" align="left">
															<!--Contact Information--> Contact Information
														</td>
													</tr>

													<tr>
														<td height="6" colspan="2" align="left"
															style="padding-left: 50px"></td>
													</tr>

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200"
															style="padding-left: 50px; padding-right: 0" height="22"
															align="left">
															<!--Name--> Name
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><bean:write
																name="clientForm" property="customerName" /></td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200"
															style="padding-left: 50px; padding-right: 0" height="11"
															align="left">
															<!--Name--> Name
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><html:text
																property="customerName" size="21" /><label style="color:red; vertical-align: middle;" >*</label></td>
													</tr>

													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientName" />
														</td>
													</tr>


													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200"
															style="padding-left: 50px; padding-right: 0" height="22"
															align="left">
															<!--Name--> Father/Guardian/Spouse Name
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><bean:write
																name="clientForm" property="customerGurdian" /></td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200"
															style="padding-left: 50px; padding-right: 0" height="22"
															align="left">
															<!--Name--> Father/Gurdian/Spouse Name
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><html:text
																property="customerGurdian" size="21" /></td>
													</tr>

													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientFGSName" />
														</td>
													</tr>

													<%
														if (!(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_SUBMITTED || cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_DENIED)) {
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">ADSL Phone No</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><bean:write
																name="clientForm" property="phoneNo" /></td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">ADSL Phone No</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><html:text
																property="phoneNo" onkeypress="return onlyNumbers();"
																onchange="changeHandle();"></html:text><label style="color:red; vertical-align: middle;" >*</label></td>
													</tr>
													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientAdslPhone" />
														</td>
													</tr>

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">
															<!--Designation--> Moblie No
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><bean:write
																name="clientForm" property="customerMobile" /></td>
													</tr>
													<%
														} else {
													%>
													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">
															<!--Designation--> Moblie No
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><html:text
																property="customerMobile" size="21" /><label style="color:red; vertical-align: middle;" >*</label></td>
													</tr>
													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientMobileNo" />
														</td>
													</tr>


													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">
															<!--Email--> Email
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><bean:write
																name="clientForm" property="customerEmail" /></td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">
															<!--Email--> Email
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><html:text
																property="customerEmail" size="21" /><label style="color:red; vertical-align: middle;" >*</label></td>
													</tr>
													<%
														}
													%>

													 <tr>
                                                        <td width="194" align = "left">
                                                         </td>
                                                        <td width="292" align = "left"> <html:errors property="clientEmailAddress"/>
                                                        </td>
                                                    </tr>


													<%
														if (!(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_SUBMITTED || cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_DENIED)) {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Area</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><%=RegionRepository.getInstance()
								.getRegionID(Long.parseLong(arc))
								.getRegionName()%>
														</td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Area</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><html:select
																property="areaCode" onchange="changeHandle();">

																<%
																	RegionService rService = new RegionService();
																					ArrayList regionList = rService.getAllRegion();
																					for (int i = 0; i < regionList.size(); i++) {
																						RegionDTO regionDTO = (RegionDTO) regionList
																								.get(i);
																						if (regionDTO.getStatus() == RegionConstants.REGION_STATUS_ACTIVE) {
																%>
																<html:option
																	value="<%=Long.toString((regionDTO
												.getRegionDesc()))%>"><%=regionDTO.getRegionName()%></html:option>
																<%
																	}
																					}
																%>
															</html:select></td>
													</tr>

													<%
														}
													%>

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
															<!--Address--> Address
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 30px"><bean:write
																name="clientForm" property="customerAddress" /></td>
													</tr>
													<%
														} else {
													%>

													<td width="200" style="padding-left: 50px" height="22"
														align="left">
														<!--Address--> Address
													</td>
													<td width="300" height="22" align="left"
														style="padding-left: 30px"><html:textarea
															property="customerAddress" /><label style="color:red; vertical-align: middle;" >*</label></td>
													</tr>
													<%
														}
													%>
													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientAddress" />
														</td>
													</tr>
												</table>
												<table width="500" border="0" cellpadding="0"
													cellspacing="0" class="form1">
													<tr>
														<td width="200" height="39"
															style="padding-left: 50px; font-weight: bold"
															align="left">
															<!--Account Information--> Connection Information
														</td>
														<td width="300" height="39" align="left"
															style="padding-left: 50px">&nbsp;</td>
													</tr>

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" height="21"
															align="left">Type of Connection</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=ClientConstants.TYPEOFCONNECTION_NAME[Integer
								.parseInt(toc) - 1]%></td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" height="21"
															align="left">Type of Connection</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:select
																property="typeofConnection" size="1">

																<%
																	for (int i = 0; i < ClientConstants.TYPEOFCONNECTION_VALUE.length; i++) {
																%>
																<html:option
																	value="<%=Integer
											.toString(ClientConstants.TYPEOFCONNECTION_VALUE[i])%>"><%=ClientConstants.TYPEOFCONNECTION_NAME[i]%></html:option>
																<%
																	}
																%>
															</html:select></td>
													</tr>
													<%
														}
													%>

													<%
														if (!(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_SUBMITTED ||cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_DENIED)) {
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Exchange Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=ExchangeRepository
								.getInstance()
								.getExchange(
										DslmRepository.getInstance()
												.getDslm(Long.parseLong(ds))
												.getDslmExchangeNo())
								.getExName()%></td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Exchange Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><div id="exchange">
																<html:select property="dslmExchangeNo"
																	onchange="changeDslm(this.value)">
																	

																	<html:option
																		value="<%=Long.toString(DslmRepository.getInstance().getDslm(Long.parseLong(ds)).getDslmExchangeNo())%>"><%=ExchangeRepository
																				.getInstance().getExchange(	DslmRepository.getInstance().getDslm(Long.parseLong(ds)).getDslmExchangeNo()).getExName()%></html:option>
																	<%
																		
																	%>
																</html:select>
															</div></td>
													</tr>

													<%
														}
													%>



													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">DSLM Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=DslmRepository
								.getInstance()
								.getDslm(
										((ClientForm) request
												.getAttribute("clientForm"))
												.getDslmNo()).getDslmName()%></td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">DSLM Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><div id="dslm">
																<html:select property="dslmNo">
																	<%
																		for (int i = 0; i < DslmRepository.getInstance()
																								.getDslmList().size(); i++) {
																							DslmDTO row = (DslmDTO) DslmRepository
																									.getInstance().getDslmList().get(i);
																							if (row.getDslmStatus() == DslmConstants.DSLM_ACTIVE
																									&& DslmRepository.getInstance()
																											.getDslm(Long.parseLong(ds))
																											.getDslmExchangeNo() == row
																											.getDslmExchangeNo()) {
																	%>

																	<html:option
																		value="<%=Long.toString(row.getDslmID())%>"><%=row.getDslmName()%></html:option>
																	<%
																		}
																						}
																	%>
																</html:select>
															</div></td>
													</tr>
													
													
													 <tr>
                                                        <td width="194" align = "left">
                                                         </td>
                                                        <td width="292" align = "left"> <html:errors property="dslmNo"/>
                                                        </td>
                                                    </tr>
                                                    

													<%
														}
													%>

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED)
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Port No</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="portNo" /></td>
													</tr>

													<%
														} else {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Port No</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:text
																property="portNo" onkeypress="return onlyNumbers();"></html:text><label style="color:red; vertical-align: middle;" >*</label></td>
													</tr>

													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientPortNo" />
														</td>
													</tr>


													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Mac Address</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="macAddress" /></td>
													</tr>

													<%
														} else {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Mac Address</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:text
																property="macAddress"></html:text></td>
													</tr>

													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientMacAddress" />
														</td>
													</tr>


													<tr>
														<%
															if (UserRepository.getInstance()
																			.getUser(cDTO.getAddedBy()).getRoleID() == 1
																			|| UserRepository.getInstance()
																					.getUser(cDTO.getAddedBy())
																					.getUsername().equals("btcl")) {
														%>

														<td width="200" height="22" align="left"
															style="padding-left: 50px">Invoice No</td>

														<%
															} else {
														%>

														<td width="200" height="22" align="left"
															style="padding-left: 50px">Invoice No</td>

														<%
															}
														%>

														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="referenceNo" /></td>
													</tr>

<%if((UserRepository.getInstance()
															.getUser(cDTO.getAddedBy()).getRoleID() != UserDTO.ROLE_DATA_ENTRY)
															|| UserRepository.getInstance()
																	.getUser(cDTO.getAddedBy())
																	.getUserID() == UserDTO.BTCL){ %>
                                                    
                                                    <tr>
                                                      <td width="200" height="25" align = "left" style="padding-left: 50px">Advice Note No</td>
                                                      <td width="300" height="22" align="left"
															style="padding-left: 50px">  <bean:write name="clientForm" property="adviceNote" /></td>
                                                  </tr>
                                                  
                                                  
                                                  
                                                  <%}%>



													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Bind to Port</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:checkbox
																property="bindToPort" disabled="true"></html:checkbox></td>
													</tr>
													<%
														} else {
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Bind to Port</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:checkbox
																property="bindToPort"></html:checkbox></td>
													</tr>
													<%
														}
													%>

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Bind to Mac</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:checkbox
																property="bindToMac" disabled="true"></html:checkbox></td>
													</tr>
													<%
														} else {
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Bind to Mac</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:checkbox
																property="bindToMac"></html:checkbox></td>
													</tr>
													<%
														}
													%>
												</table>

												<table border="0" cellpadding="0" cellspacing="0"
													class="form1" width="500"">
													<tr>
														<td colspan="2" height="35" align="left"
															style="padding-left: 50px">&nbsp;</td>
													</tr>


													<tr>
														<td width="200" height="19"
															style="padding-left: 50px; font-weight: bold"
															align="left">
															<!--Account Information--> Package Information
														</td>
														<td width="300" height="19" align="left"
															style="padding-left: 50px">&nbsp;</td>
													</tr>

													<tr>
														<td colspan="2" height="7" align="left"
															style="padding-left: 50px">&nbsp;</td>
													</tr>


													<%
														if (!(aps.equals("1")|| aps.equals("3")) ) {
													%>
													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Package Type</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=ClientConstants.PACKAGETYPE_NAME[Integer
								.parseInt(ptype)==2?0:1]%>
														</td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Package Type</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px">
															<%
																for (int i = 0; i < ClientConstants.PACKAGETYPE_VALUE.length; i++) {
															%> <html:radio
																value="<%=Integer
									.toString(ClientConstants.PACKAGETYPE_VALUE[i])%>"
																property="paymentType"
																onclick="changePackageList(this.value)" /><%=ClientConstants.PACKAGETYPE_NAME[i]%>
															<%
																}
															%>
														</td>
													</tr>


													<%
														}
													%>

													<%
														if (!(aps.equals("1")||aps.equals("3"))) {
													%>
													<tr>
														<td height="10" colspan="2" align="left"
															style="padding-left: 50px"></td>
													</tr>

													<%--<bean:define id="pid" scope="request"> <bean:write name="clientForm" property = "rateID"/></bean:define>--%>
													<%
														PackageService packageService = new PackageService();
																	pid = packageService.getPackageName(pid);
													%>


													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Package</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><label><%=pid%></label></td>
													</tr>

													<%
														} else {
													%>


													<%
														{
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Package</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><div id="packageDiv">
																<html:select property="rateID" size="1">


																	<%
																		ArrayList list = (ArrayList) PackageRepository
																									.getInstance().getPackageList();
																							for (int i = 0; i < list.size(); i++) {
																								PackageRepositoryDTO packageDTO = (PackageRepositoryDTO) list
																										.get(i);
																								if (packageDTO.getPackageType() == Integer
																										.parseInt(ptype)
																										&& packageDTO.getIsDeleted() == 0) {
																	%>
																	<html:option
																		value="<%=Long.toString(packageDTO
													.getPackageID())%>"><%=packageDTO
													.getPackageName()%></html:option>
																	<%
																		}
																							}
																	%>
																</html:select>
															</div></td>
													</tr>
													<%
														}
													%>

													<%
														}
													%>


												</table>

												<table width="500" border="0" cellpadding="0"
													cellspacing="0" class="form1">
													<tr>
														<td width="200" height="39"
															style="padding-left: 50px; font-weight: bold"
															align="left">
															<!--Account Information--> Account Information
														</td>
														<td width="300" height="39" align="left"
															style="padding-left: 50px">&nbsp;</td>
													</tr>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Unique ID</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:text
																property="uniqueID" disabled="true" />
															<html:hidden property="uniqueID" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientUniqueID" />
														</td>
													</tr>
													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">User Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="userName" /></td>
													</tr>
													<%
														} else {
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">User Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:text
																property="userName" size="20"
																onkeyup="return setWebUserName();"></html:text><label style="color:red; vertical-align: middle;" >*</label></td>
													</tr>
													<%
														}
													%>
													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientUserName" />
														</td>
													</tr>
													
													
													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Login Password</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:password
																property="loginPassword" size="20" disabled="true" /><span
															id="strength"></span></td>
														<td></td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Login Password</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:password
																property="loginPassword" size="20"
																onkeyup="return passwordChanged('loginPassword','strength');" /><label style="color:red; vertical-align: middle;" >*</label><span
															id="strength"></span></td>
														<td></td>
													</tr>
													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientLoginPassword" />
														</td>
													</tr>

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/!((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED)
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Confirm Login Password</td>
															<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:password
																property="confirmLoginPassword"
																onkeyup="return setConfirmWebPassword();" /></td>
														
													</tr>
													<%
														}
													%>
													
													
													
													
													
													
													
													
													
													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Web User Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="webuserName" /></td>
													</tr>
													<%
														} else {
													%>


													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Web User Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:text
																property="webuserName" size="20" /><label style="color:red; vertical-align: middle;" >*</label>
															<html:hidden property="webuserName" />
															<html:checkbox
															property="sameUserName" 
															onclick="onclickSameUserName();"/><%="Same as Username"%></td>
													</tr>
													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientWebUserName" />
														</td>
													</tr>

													<tr>
														<td width="200" align="left" style="padding-left: 50px"></td>
														<td width="300" align="left" style="padding-left: 50px"><html:errors
																property="customerID" /></td>
													</tr>
													


																										<!--      </tbody> -->

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Web Login Password</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:password
																property="webloginPassword" size="20" disabled="true" /><label style="color:red; vertical-align: middle;" >*</label>
														</td>
													</tr>
													<%
														} else {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Web Login Password</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:password
																property="webloginPassword" size="20"
																onkeyup="return passwordChanged('webloginPassword','webstrength');" /><label style="color:red; vertical-align: middle;" >*</label>
															<html:hidden property="webloginPassword" /> <html:checkbox
															property="samePassword" 
															onclick="onclickSamePassword();"/>Same as Login
																Password<span id="webstrength"></span></td>
													</tr>

													<%
														}
													%>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientWebLoginPassword" />
														</td>
													</tr>

													<%
														if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/!((cDTO
																		.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																		|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																		&& !loginDTO
																				.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																			.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Confirm Web Login
															Password</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:password
																property="confirmWebLoginPassword" /><label style="color:red; vertical-align: middle;" >*</label></td>
													</tr>
													<%
														}
													%>


													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Application Status</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=ClientConstants.APPLICATIONSTATUS_NAME[Integer
							.parseInt(aps) - 1]%>
														</td>
													</tr>



													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Connection Status</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=Integer.parseInt(ccstatus) == 1 ? "Connected"
							: "Not Connected"%>
														</td>
													</tr>



													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Account Status</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=ClientConstants.CLIENTSTATUS_NAME[Integer
							.parseInt(acstatus) - 1]%>
														</td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Blacklisted</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=black == 1 ? "Yes" : "No"%>
														</td>
													</tr>



													<%--<bean:define id="bl" scope="request"> <bean:write name="clientForm" property = "balance"/></bean:define>--%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Balance</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=bl%></td>
													</tr>
													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientBalance" />
														</td>
													</tr>
													
													
													
													<%
														if(loginDTO.getRoleID()!= UserDTO.ROLE_ADMIN && loginDTO.getRoleID()!= UserDTO.ROLE_OPERATOR)
														{
													%>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Comments</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="comments" /></td>
													</tr>
													<%
														} else {
													%>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Comments</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:textarea
																property="comments" ></html:textarea></td>
													</tr>
													<%
														}
													%>
													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="comments" />
														</td>
													</tr>
													
													
													
													
													
													
													
													
												</table>
												<br />
												<table border="0" cellpadding="0" cellspacing="0"
													class="form1" width="500">
													<tr>
														<td width="168" height="19" align="left"
															style="padding-left: 50px">&nbsp;</td>
														<td width="319" height="19" align="left">
															<%
																if (/*(cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED && !loginDTO.getColumnPermission(PermissionConstants.COLUMN_APPROVE)) ||*/((cDTO
																				.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_APPROVED
																				|| cDTO.getApplicationStatus() == ClientDTO.APPLICATION_STATUS_VERIFIED )
																				&& !loginDTO
																						.getColumnPermission(PermissionConstants.COLUMN_VERIFY) && !loginDTO
																					.getColumnPermission(PermissionConstants.COLUMN_APPROVE))) {
															%>
															<input type="button"
															value="Back" onclick="goBack();"/> <%
 	} else {
 %> <input
																class="cmd" type="reset" value="Reset" name="B1"
																onclick="onClickReset();changePackageList(<%=ptype%>);"/>&nbsp;
																	<input class="cmd" type="submit" name="submitType"
																	value="Update" name="B2"/> <%
 	}
 %>
														</td>
													</tr>
												</table>
												<%
													} else {
												%>
												<table border="1" cellpadding="0"
													style="border-style: solid;" cellspacing="0" class="form2"
													width="500">

													<tr>
														<td style="padding-left: 50px; font-weight: bold"
															height="16" colspan="2" align="left">
															<!--Contact Information--> Contact Information
														</td>
													</tr>

													<tr>
														<td height="6" colspan="2" align="left"
															style="padding-left: 50px"></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" height="11"
															align="left">
															<!--Name--> Name
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="customerName" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientName" />
														</td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" height="11"
															align="left">
															<!--Name--> Father/Gurdian/Spouse Name
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="customerGurdian" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientFGSName" />
														</td>
													</tr>


													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">ADSL Phone No</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="phoneNo" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientAdslPhone" />
														</td>
													</tr>


													<tr>
														<td width="200" style="padding-left: 50px" height="11"
															align="left">
															<!--Mobile Number--> Mobile No
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="customerMobile" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientMobileNo" />
														</td>
													</tr>


													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">
															<!--Email--> Email
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="customerEmail" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientEmail" />
														</td>
													</tr>


													<tr>
														<td width="200" style="padding-left: 50px" height="21"
															align="left">Area</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=RegionRepository.getInstance()
							.getRegionID(Long.parseLong(arc)).getRegionName()%></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">
															<!--Address--> Address
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="customerAddress" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientAddress" />
														</td>
													</tr>
												</table>
												<br />
												<table border="1" style="border-style: solid;"
													cellpadding="0" cellspacing="0" class="form2" width="500">

													<tr>
														<td width="200" height="39"
															style="padding-left: 50px; font-weight: bold"
															align="left">
															<!--Account Information--> Connection Information
														</td>
														<td width="300" height="39" align="left"
															style="padding-left: 50px">&nbsp;</td>
													</tr>

													<tr>
														<td height="6" colspan="2" align="left"
															style="padding-left: 50px"></td>
													</tr>
													<tr>
														<td width="200" style="padding-left: 50px" height="21"
															align="left">Type of Connection</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=ClientConstants.TYPEOFCONNECTION_NAME[Integer
							.parseInt(toc) - 1]%></td>
													</tr>

													<tr>
														<td width="200" height="22" align="left"style="padding-left: 50px">Exchange Name</td>
														<td width="300" height="22" align="left"style="padding-left: 50px"><%=ExchangeRepository.getInstance().getExchange(DslmRepository.getInstance().getDslm(Long.parseLong(ds)).getDslmExchangeNo()).getExName()%>
														</td>
													</tr>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">DSLM Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=DslmRepository.getInstance().getDslm(((ClientForm) request.getAttribute("clientForm")).getDslmNo()).getDslmName()%>
														</td>
													</tr>
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Port No</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="portNo" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientPortNo" />
														</td>
													</tr>


													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Mac Address</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="macAddress" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientMacAddress" />
														</td>
													</tr>


													<tr>
														<%
															if (UserRepository.getInstance()
																			.getUser(cDTO.getAddedBy()).getRoleID() != UserDTO.ROLE_DATA_ENTRY
																			|| UserRepository.getInstance()
																					.getUser(cDTO.getAddedBy())
																					.getUserID() == UserDTO.BTCL) {
														%>

														<td width="200" height="22" align="left"
															style="padding-left: 50px">Advice Note No</td>
															
															<td width="300" height="22" align="left"
															style="padding-left: 50px">  <bean:write name="clientForm" property="adviceNote" /></td>

														<%
															} 
														%>

														<td width="200" height="22" align="left"
															style="padding-left: 50px">Invoice No</td>

														<%
															
														%>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="referenceNo" /></td>
													</tr>



													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Bind to Port</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:checkbox
																property="bindToPort" disabled="true"></html:checkbox></td>
													</tr>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Bind to Mac</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><html:checkbox
																property="bindToMac" disabled="true"></html:checkbox></td>
													</tr>

												</table>

												<br />

												<table border="1" style="border-style: solid;"
													cellpadding="0" cellspacing="0" class="form2" width="500"">
													<tr>
														<td width="200" height="22"
															style="padding-left: 50px; font-weight: bold"
															align="left">
															<!--Package Information--> Package Information
														</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px">&nbsp;</td>
													</tr>

													<tr>
														<td height="6" colspan="2" align="left"
															style="padding-left: 50px"></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Package Type</td>
														<td width="300" height="22" align="left"
															style="padding-left: 70px"><%=ClientConstants.PACKAGETYPE_NAME[Integer
							.parseInt(ptype)==2?0:1]%>
														</td>
													</tr>

													<%
														PackageService packageService = new PackageService();
																pid = packageService.getPackageName(pid);
													%>


													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Package</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><label><%=pid%></label></td>
													</tr>

												</table>
												<br />
												<table border="1" style="border-style: solid;"
													cellpadding="0" cellspacing="0" class="form2" width="500">
													<tr>
														<td width="200" height="39"
															style="padding-left: 50px; font-weight: bold"
															align="left">
															<!--Account Information--> Account Information
														</td>
														<td width="300" height="39" align="left"
															style="padding-left: 50px">&nbsp;</td>
													</tr>

													<tr>
														<td height="6" colspan="2" align="left"
															style="padding-left: 50px"></td>
													</tr>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Unique ID</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="uniqueID" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientUniqueID" />
														</td>
													</tr>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">User Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="userName" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientUserName" />
														</td>
													</tr>
													
													
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Login Password</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="loginPassword" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientLoginPassword" />
														</td>
													</tr>


													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Web User Name</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="webuserName" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientWebUserName" />
														</td>
													</tr>


													

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Web Login Password</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="webloginPassword" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientWebLoginPassword" />
														</td>
													</tr>
													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Application Status</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=ClientConstants.APPLICATIONSTATUS_NAME[Integer.parseInt(aps) - 1]%>

														</td>
													</tr>
													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Connection Status</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=Integer.parseInt(ccstatus) == 1 ? "Connected"
							: "Not Connected"%>
														</td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Account Status</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=ClientConstants.CLIENTSTATUS_NAME[Integer
							.parseInt(acstatus) - 1]%>
														</td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" height="22"
															align="left">Blacklisted</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=black == 1 ? "Yes" : "No"%>
														</td>
													</tr>

													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Balance</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><%=bl%></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="clientBalance" />
														</td>
													</tr>
													
													
													<tr>
														<td width="200" height="22" align="left"
															style="padding-left: 50px">Comments</td>
														<td width="300" height="22" align="left"
															style="padding-left: 50px"><bean:write
																name="clientForm" property="comments" /></td>
													</tr>

													<tr>
														<td width="200" style="padding-left: 50px" align="left">
														</td>
														<td width="300" align="left" style="padding-left: 30px">
															<html:errors property="comments" />
														</td>
													</tr>
													
													
												</table>

												<br />

												<table border="0" cellpadding="0" cellspacing="0"
													class="form1" width="500">
													<tr>
														<td width="168" height="19" align="left"
															style="padding-left: 50px">&nbsp;</td>
														<td width="319" height="19" align="left"
															style="padding-left: 50px"><input type="button"
															value="Back" onclick="goBack();"/></td>
													</tr>
												</table>
												<%
													}
												%>

											</html:form> <!-- end of the form --> <br /></td>
									</tr>
								</table>
							</td>
						</tr>
					</table></td>
			</tr>
			<tr>
				<td width="100%"><%@ include file="../../includes/footer.jsp"%>
				</td>
			</tr>
		</table>
	</body>
</html>
