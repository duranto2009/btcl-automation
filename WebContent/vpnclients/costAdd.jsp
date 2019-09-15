<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="user.UserDTO"%>
<%@page import="vpn.client.form.VPNClientForm"%>
<%@page import="exchange.ExchangeDTO"%>
<%@page import="exchange.ExchangeRepository"%>
<%@page import="user.UserRepository"%>
<%@page import="dslm.DslmRepository"%>
<%@page import="vpn.packages.*"%>
<%@page errorPage="../common/failure.jsp" %>
<%@ include file="../includes/checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*,packages.*,regiontype.*,dslm.*,exchange.*" %>

<%
    String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",};
    GregorianCalendar calendar = new java.util.GregorianCalendar();

    int year = calendar.get(GregorianCalendar.YEAR);
    int month = calendar.get(GregorianCalendar.MONTH);
    int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);

%>




<%
    String submitCaption = "Add Cost";

    String actionName = "/AddVPNCost";
    RegionService rService = new RegionService();

%>


<html>


    <head>


        <html:base/>
        <title>Add VPN Client</title>
        <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
        <script  type = "text/javascript" src="../scripts/util.js"></script>
     
     
    <link class="jsbin" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
	<script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
	<script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
	
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
      
            
            function submitForm()
            {
            	document.forms[0].submit();
            }
            
            function validate()
            {
            	document.getElementsByName("webuserName")[1].value = document.getElementsByName("webUserName")[0].value;
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
                ob = f.dslmName;
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
            	document.forms[0].reset();
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
      
      
            function checkRadio()
            {
                //document.getElementsByName("paymentType")[1].checked = true;
                return true;
    	  
            }

            function checkBalance(balance)
            {
    	  
    	

                if( !validateDecimal(balance.value))
                {
                    if(balance.value.length>1)
                        balance.value = balance.value.substring(0,balance.value.length-1);
                    else
                        balance.value = "";
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
                var url="../bonus/GetPackages.do?packageType="+arg+"&requester=client";
                //  alert(url);
                xmlHttp.onreadystatechange = PackageChanged;
                xmlHttp.open("GET",url,false);    	  	
                xmlHttp.send(null);    	  	
                return true;
            }
      		function vpnTypeChanged(v)
      		{
      			if(v == 0)
      			{
      				document.getElementById("multipleDestDslm").style.display = "none";
      				document.getElementById("mulitpleDSLMBox").style.display = "none";
      				document.getElementById("singleDestDslm").style.display = "";
      			}
      			else
      			{
      				document.getElementById("multipleDestDslm").style.display = "";
      				document.getElementById("mulitpleDSLMBox").style.display = "";
      				document.getElementById("singleDestDslm").style.display = "none";
      			}
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
                //w = document.getElementsByName("phoneNo")[0].value;
                //changeExchange(v,w);
            }
      
            function changeHandleTo()
            {
                v = document.getElementsByName("areaCodeTo")[0].options[document.getElementsByName("areaCodeTo")[0].selectedIndex].value;
                //w = document.getElementsByName("phoneNo")[0].value;
                //changeExchange(v,w);
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
                var url="../dslm/GetExchange.do?areaCode="+v+"&adslPhone="+w;
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
                    //alert(xmlHttp.responseText.substr(0,6));
          
          
          	 
                    if(xmlHttp.responseText.substr(0,31) == "<select name = 'dslmExchangeNo'")
                        document.getElementById("exchange").innerHTML=xmlHttp.responseText;
                    if(document.getElementsByName("dslmExchangeNo")[0].options.length>0)
                        changeDslm(document.getElementsByName("dslmExchangeNo")[0].options[0].value);
      	  
                }
         
                else
                {
                    //alert("Request failed.");
                }
            }
      
      
            function changeDslm(v)
            {
    	 
                // alert("I am Called" +v);
                xmlHttp = GetXmlHttpObject();
                if(xmlHttp == null)
                {
                    alert("Browser is not supported.");
                    return false;
                } 
                var url="../dslm/GetDslms.do?exCode="+v;
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
      
      
        </script>
    </head>

    <body class="body_center_align" onload="return checkRadio();">
        <table border="0" cellpadding="0" cellspacing="0"  width="780" id="AutoNumber1">
            <tr><td width="100%"><%@ include file="../includes/header.jsp"  %></td></tr>
            <tr><td width="100%"><table border="0" cellpadding="0" cellspacing="0"  width="780" id="AutoNumber2">
                        <tr>
                            <td width="<%=("600")%>" valign="top" class="td_main" align="center">
                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                    <tr><td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                                            <div class="div_title">Add Cost</div>
                                        </td></tr>
                                    <tr><td width="100%" align="center"><br/>
                                            <html:form  action="<%=actionName%>" enctype="multipart/form-data" method="POST" onsubmit="return validate();"  >



                                                <table border="0" cellpadding="0" cellspacing="0" class="form1"  width="500">

                                                    <%
                                                        String msg = null;
                                                        if ((msg = (String) session.getAttribute(util.ConformationMessage.VPN_CLIENT_ADD)) != null) {
                                                            session.removeAttribute(util.ConformationMessage.VPN_CLIENT_ADD);
                                                    %>
                                                    <tr>
                                                        <td colspan="2" align="center" valign="top" height="50"><b><%=msg%></b></td>
                                                    </tr>
                                                    <%}%>

                                                    <tr>
                                                        <td height="25" colspan="2">&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td style="padding-right: 0; font-weight:bold" height="16" colspan="2" align = "left">                        
                                                            <!--Contact Information-->
                                                            Cost Information                      
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td height="6" colspan="2" align = "left"></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="194" style="padding-right: 0" height="11" align = "left">
                                                            <!--Name-->
                                                            Distance</td>
                                                        <td width="292" height="25" align = "left"> <html:text property="distance" size="21" /><label style="color:red; vertical-align: middle;" >*</label>
                                                        </td>
                                                    </tr>
                                                    
                                                     <tr>
                                                        <td width="194" align = "left">
                                                         </td>
                                                        <td width="292" align = "left"> <html:errors property="distance" />
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="194" style="padding-right: 0" height="11" align = "left">
                                                            <!--Name-->
                                                            Fixed Cost</td>
                                                        <td width="292" height="25" align = "left"> <html:text property="fixedCost" size="21" />
                                                        </td>
                                                    </tr>
													<tr>
                                                        <td width="194" align = "left">
                                                         </td>
                                                        <td width="292" align = "left"> <html:errors property="fixedCost"/>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="200" height="25" align = "left">Variable cost per unit</td>
                                                        <td width="292" height="25" align = "left">  <html:text property="variableCost" size="21"/> <label style="color:red; vertical-align: middle;" >*</label></td>
                                                    </tr>

													<tr>
                                                        <td width="194" align = "left">
                                                         </td>
                                                        <td width="292" align = "left"> <html:errors property="variableCost"/>
                                                        </td>
                                                    </tr>                                                    
                                                </table> 

                                                <br/>

                                                <table border="0" cellpadding="0" cellspacing="0" class="form1" width="487">
                                                    <tr>
                                                        <td width="168" height="19" align = "left">&nbsp;</td>
                                                        <td width="319" height="19" align = "left">
                                                        <html:reset onclick = "return onClickReset();"> Reset </html:reset><html:submit onclick="submitForm();"><%=submitCaption%></html:submit> 
                                                           <%--  <input class="cmd"  type="reset" value="Reset" name="B1" onclick= "return onClickReset();">&nbsp;
                                                           
                                                           <input class="cmd" type="submit" value="<%=submitCaption%>" name="B2">  --%>
                                                        </td>
                                                    </tr>
                                                </table>

                                            </html:form>
                                            <!-- end of the form -->
                                            <br/>
                                        </td></tr></table></td></tr></table></td></tr>
            <tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr>
        </table></body></html>
