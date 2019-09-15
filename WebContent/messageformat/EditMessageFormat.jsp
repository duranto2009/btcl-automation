<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>


<%@page errorPage="../common/failure.jsp" %>

<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,messageformat.*,regiontype.*" %>				 
				 

<%
 String title = "Edit Message Formats";
 String submitCaption2 = "Update";
 String actionName = "/UpdateMessageFormat";
 MessageService fservice = new MessageService();
%>

<html>
<head>
<html:base/>
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script language="JavaScript" src="../scripts/util.js"></script>

<script language="JavaScript">
	 
	 
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
	
	
	


	function changeLook(v)
	{
		  
		  xmlHttp = GetXmlHttpObject();
		  if(xmlHttp == null)
		  {
			  alert("Browser is not supported.");
			  return false;
		  }    	  	
		  
		  v = v.trim(v);
		
		  	var url="../messageformat/GetFormat.do?id="+v+"&type="+document.getElementsByName("msgFormatType")[0].options[document.getElementsByName("msgFormatType")[0].selectedIndex].value;
		  	//alert(url);
		  	xmlHttp.onreadystatechange = LookChanged;
		  	xmlHttp.open("GET",url,false);    	  	
		  	xmlHttp.send(null);    	  	
		  	return true;
	}

	function LookChanged() 
	{ 
	  // alert(xmlHttp.responseText);
	   if (xmlHttp.readyState==4)
	   { 
	    // alert(document.getElementById("category").innerHTML);
	     document.getElementById("category").innerHTML=xmlHttp.responseText;
		  
	   }
	   
	   else
	 	 {
	 	 //	alert("Request failed.");
	 	 }
	}
	
	
    function changeType(v)
    {
    	 xmlHttp = GetXmlHttpObject();
		  if(xmlHttp == null)
		  {
			  alert("Browser is not supported.");
			  return false;
		  }    	  	
		  
		  v = v.trim(v);
		
		  	var url="../messageformat/GetCategories.do?type="+v;
		  //	alert(url);
		  	xmlHttp.onreadystatechange = TypeChanged;
		  	xmlHttp.open("GET",url,false);    	  	
		  	xmlHttp.send(null);    	  	
		  	return true;
    }
    
    function TypeChanged() 
	{ 
	   //alert(xmlHttp.responseText);
	   if (xmlHttp.readyState==4)
	   { 
	     //alert(document.getElementById("type").innerHTML);
	     document.getElementById("type").innerHTML=xmlHttp.responseText;
	     //chageLook(document.getElementsByName("msgFormatCategory")[0].options[0].value);
		  
	   }
	   
	   else
	 	 {
	 	 	//alert("Request failed.");
	 	 }
	}

	 

</script>

</head>

<body class="body_center_align" >


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="1024" id="AutoNumber2">
      <tr>
	    <!--left menu-->
        
	    <!--main -->
        <td width="1024" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
		 
            
            </table>

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="500" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                   
                   
                    <bean:define id="mtype" scope="request"> <bean:write name="messageFormatForm" property = "msgFormatType"/></bean:define>
                     
                     
                      <tr>
                        <td width="194" height="25" align="left" style="padding-left: 50px">Message Type</td>
                        <td width="292" height="25" align="left"><html:select property="msgFormatType" size = "1" onchange = "changeType(this.value)">
                        
                        <html:option value="<%=Integer.toString(MessageConstants.MESSAGE_TYPE_MAIL) %>" ><%="Mail"%></html:option>
                        <html:option value="<%=Integer.toString(MessageConstants.MESSAGE_TYPE_SMS) %>" ><%="SMS"%></html:option>
                        
                        </html:select>



</td>
                       
                      </tr>
                    </table>
                    
                     <div id = "type">
                      <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                 
					 <TR> 
                      <TD height="25" width = "194" align="left">Message Category</TD>
                      <TD height="25" width = "292" align="left">
                      <html:select property="msgFormatCategory" size = "1" onchange = "changeLook(this.value)">
                      <%
                      ArrayList<MessageDTO> list = (ArrayList<MessageDTO>)fservice.getMessageFormats(Integer.parseInt(mtype));
                      
                      for(int i = 0; i<list.size();i++){ %>
                      
                      <html:option value="<%=Long.toString(list.get(i).getMsgFormatID()) %>" ><%=MessageConstants.MESSAGE_TYPE_NAME[list.get(i).getMsgFormatCategory()-1] %></html:option>
                      <%} %>
                      </html:select>
                      
                      </TD>
                    </TR>
                    
                     <tr>
                        <td width="194" align="left"></td>
                        <td width="292" align="left"><html:errors property = "msgFormatCategory"/></td>
                       
                      </tr>
                     
                    
                      </table>
                      
                        
                     
                    
                      <div id = "category">
                      <%if(Integer.parseInt(mtype) == MessageConstants.MESSAGE_TYPE_MAIL) {%>
                     <table width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                     <tr>
                        <td width="194" style="padding-right: 0" height="40" align="left">Message Header</td>
                        <td width="292" height="40" align="left"><html:textarea property = "msgFormatHeader"></html:textarea></td>
                       
                      </tr>
                      
                      <tr>
                        <td width="194" align="left"></td>
                        <td width="292" align="left"><html:errors property = "msgFormatHeader"/></td>
                       
                      </tr>
                    
                    
                   
					<tr>
                        <td width="194" style="padding-right: 0" height="40" align="left">Message Footer</td>
                        <td width="292" height="40" align="left"><html:textarea property = "msgFormatFooter"></html:textarea></td>
                       
                      </tr>  
                       <tr>
                        <td width="194" align="left"></td>
                        <td width="292" align="left"><html:errors property = "msgFormatFooter"/></td>
                       
                      </tr>                
                    
                    <tr>
                        <td width="194" style="padding-right: 0" height="40" align="left">Message Subject</td>
                        <td width="292" height="40" align="left"><html:textarea property = "msgFormatSubject"></html:textarea></td>
                       
                      </tr>
                      
                      <tr>
                        <td width="194" align="left"></td>
                        <td width="292" align="left"><html:errors property = "msgFormatSubject"/></td>
                       
                      </tr>   
                      </table>
                      
                     
                      <%} %>
                      
                      <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                                    
                  <tr>
                        <td width="194" style="padding-right: 0" height="40" align="left">Message Body</td>
                        <td width="292" height="40" align="left"><html:textarea property = "msgFormatBody"></html:textarea></td>
                       
                      </tr>
                      
                       <tr>
                        <td width="194" align="left"></td>
                        <td width="292" align="left"><html:errors property = "msgFormatBody"/></td>
                       
                      </tr>   
                      
                   </table>
                   </div>
                   </div>
                    
                    <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <TR> 
                      <TD valign="top" height="15"></TD>
                      <TD height="15" colspan="2">&nbsp;</TD>
                    </TR>
                  </table>
                  
                  <html:reset> Reset </html:reset><html:submit><%=submitCaption2 %></html:submit> 
                  </html:form> 
                  <!-- end of the form -->
                  <br>

            <br>
            </td>
          </tr>
        </table>

        </td>
      </tr>
    </table>
   <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/footer.jsp"  %>
    </td>
  </tr>

</table>
</body>
</html>
