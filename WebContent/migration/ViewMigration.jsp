<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="client.ClientRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>


<%@ page import="java.util.ArrayList,java.text.SimpleDateFormat,
sessionmanager.SessionConstants,

databasemanager.*,migration.*,client.*,packages.*" %>				 
				 

<%
 String title = "Migrate";
 String submitCaption2 = "Back";
 String actionName = "/ViewMigration";
 PackageService pservice = new PackageService();  
 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
 
 String idForDownloadMigration = request.getParameter("id");
 session.setAttribute("idForDownloadMigrationDetails", idForDownloadMigration);

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
	
	
	 function changePackageList(v)
     {
   	  var arg = v-1;
   	 // alert("I am Called" +arg);
   	  xmlHttp = GetXmlHttpObject();
   	  if(xmlHttp == null)
   	  {
   		  alert("Browser is not supported.");
   		  return false;
   	  } 
   	  var url="../bonus/GetPackages.do?packageType="+arg+"&requester=migration";
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


	function validate()
      {
        var f = document.forms[0];

        var ob = f.dslmName;
        if( isEmpty(ob.value))
        {
          alert("Please enter Migration Name");
          ob.value = "";
          ob.focus();
          return false;
        }

       
        return true;
      }
	
	
	function changePackageName(v)
    {
  	  var arg = v;
  	 // alert("I am Called" +arg);
  	  xmlHttp = GetXmlHttpObject();
  	  if(xmlHttp == null)
  	  {
  		  alert("Browser is not supported.");
  		  return false;
  	  } 
  	  var url="../client/GetPackages.do?client="+v;
  	//  alert(url);
  	  xmlHttp.onreadystatechange = PackageNameChanged;
  	  xmlHttp.open("GET",url,false);    	  	
  	  xmlHttp.send(null);    	  	
  	  return true;
    }
    
    function PackageNameChanged() 
    { 
  	 
       if (xmlHttp.readyState==4)
       { 
      	// alert(xmlHttp.responseText);
        	// alert(document.getElementById("packageDiv"));	
         document.getElementById("previous").innerHTML=xmlHttp.responseText;
    	  // removeAll();
       }
       
       else
     	 {
     	 	//alert("Request failed.");
     	 }
    }



</script>

</head>

<body class="body_center_align" >


<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="780" id="AutoNumber1">

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/header.jsp"  %>
    </td>
  </tr>

  <!--center-->
  <tr>
    <td width="100%">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="780" id="AutoNumber2">
      <tr>
	    <!--left menu-->
       
	    <!--main -->
        <td width="780" valign="top" class="td_main" align="center">

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber3">
          <tr>
            <td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
            <div class="div_title"><%=title %></div>
			</td>
          </tr>
		 
            <td width="100%" align="center" style="padding-left:120px">

<br><br>
<!-- start of the form  -->

<html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();">
                  <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    
                    
                    <bean:define id="client" scope="request"> <bean:write name="migrationForm" property = "clientID"/></bean:define>  
                    <bean:define id="prevpackage" scope="request"> <bean:write name="migrationForm" property = "previousPackage"/></bean:define>
                    <bean:define id="migratePackage" scope="request"> <bean:write name="migrationForm" property = "migratedPackage"/></bean:define>                    
                    <bean:define id="time" scope="request"> <bean:write name="migrationForm" property = "migrationTime"/></bean:define>
                     <bean:define id="charge" scope="request"> <bean:write name="migrationForm" property = "migrationCharge"/></bean:define>
                    
                    <TR> 
                      <TD width="118" height="14"></TD>
                      <TD colspan="2"><html:errors property="dslmName"  /></TD>
                    </TR>
					 <TR> 
                      <TD valign="top" height="22" align="left">ADSL Phone No</TD>
                      <TD height="22" colspan="2" align="left">
                      <%=ClientRepository.getInstance().getClient(Long.parseLong(client)).getPhoneNo() %>
                      </TD>
                    </TR>
                   
                    <tr>
                    <td width="195" height="19" align="left">Previous Package</td>                   
                    <td width="292" height="19" align="left"><%=pservice.getPackageName(prevpackage) %></td>
                    </tr>
                   
                   
                    
                    
					 <tr>
                      <td width="195" height="19" align="left">Migrated Package</td>
                     <td width="292" height="19" align="left"><%=pservice.getPackageName(migratePackage) %></td>   
                    </tr>                
                    
                    <tr>
                        <td width="194" style="padding-right: 0" height="21" align="left">Migration Charge</td>
                        <td width="292" height="22" align="left"> <%=charge %></td>
                      </tr>
                    
                    
                      <tr>
                        <td width="194" style="padding-right: 0" height="21" align="left">Migration Invoice No</td>
                        <td width="292" height="22" align="left"><bean:write name = "migrationForm" property = "migrationInvoiceNo"/></td>
                      </tr>
                    
                     <tr>
                        <td width="194" style="padding-right: 0" height="21" align="left">Description</td>
                        <td width="292" height="22" align="left"><bean:write name = "migrationForm" property = "description"/></td>
                      </tr>
                      
                      <tr>
                        <td width="194" style="padding-right: 0" height="21" align="left">Migration Time</td>
                        <td width="292" height="22" align="left"> <%=format.format(new Date(Long.parseLong(time))) %></td>
                      </tr>
                    <TR> 
                      <TD valign="top" height="15" align="left"></TD>
                      <TD height="15" colspan="2" align="left">&nbsp;</TD>
                    </TR>
                  
                  <TR> 
                      <TD valign="top" height="15" align="center" style="padding-left:100px">
                 <html:submit><%=submitCaption2 %></html:submit> </TD>
                  </html:form> 
                  <html:form action="/Download" method="POST">
                  	<TD valign="top" height="15" align="center" style="padding-right:130px"><input type="submit" name="downloadMigrationDetails" value="Download"></TD>
                  </html:form>
                  </TR>
                  </table>
                  <!-- end of the form -->
                  <br>

            <br>
            </td>
          </tr>
        </table>

        </td>
      </tr>
    </table>
    </td>
  </tr>

  <!--header-->
  <tr>
    <td width="100%">
	<%@ include file="../includes/footer.jsp"  %>
    </td>
  </tr>

</table>
</body>
</html>
