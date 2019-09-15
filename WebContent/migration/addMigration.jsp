<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="migration.form.MigrationForm"%>
<%@page import="regiontype.RegionConstants"%>
<%@page import="regiontype.RegionDTO"%>
<%@page import="regiontype.RegionService"%>
<%@page import="client.ClientRepository"%>
<%@ include file="../includes/checkLogin.jsp" %>

<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page errorPage="failure.jsp" %>


<%@ page import="java.util.ArrayList,
sessionmanager.SessionConstants,

java.sql.*,
databasemanager.*,migration.*,client.*,packages.*" %>				 
				 

<%
 String title = "Migrate";
 String submitCaption2 = "Add";
 String actionName = "/AddMigration";
 ClientDTO temp = (ClientDTO)ClientRepository.getInstance().getClientList().get(0);
 MigrationForm sform = (MigrationForm)request.getAttribute("migrationForm");
	if(sform == null)
	{
		sform = new MigrationForm();
	}

%>

<html>
<head>
<html:base/>
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title><%=title %></title>
<link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
<script type="text/javascript" src="../scripts/util.js"></script>

<script type="text/javascript">

if (!String.prototype.trim) {
	 String.prototype.trim = function() {
	  return this.replace(/^\s+|\s+$/g,'');
	 }
}

function replaceInReadOnly(tb, content){
	
	var temp = document.getElementsByName('temp')[0];
    
    temp.innerHTML = content;
    //element.parentNode.replaceChild(newNode.firstChild.firstChild, element.firstChild);   
    tb.parentNode.replaceChild(temp.firstChild.firstChild, tb);
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
	
	
	function changeHandle()
	{
		w = document.getElementsByName("areaCode")[0].options[document.getElementsByName("areaCode")[0].selectedIndex].value;
     	v = document.getElementsByName("adslPhone")[0].value;
     	changeClient(v,w);
	}
	
	
	function changeClient(v,w)
	{
		  
		  xmlHttp = GetXmlHttpObject();
		  if(xmlHttp == null)
		  {
			  alert("Browser is not supported.");
			  return false;
		  }    	  	
		  
		  v = v.trim(v);
		
		  	var url="../recharge/GetClient.do?adslPhone="+v+"&areaCode="+w;
		  //	alert(url);
		  	xmlHttp.onreadystatechange = ClientChanged;
		  	xmlHttp.open("GET",url,false);    	  	
		  	xmlHttp.send(null);    	  	
		  	return true;
	}

	function ClientChanged() 
	{ 
	   //alert("12:"+xmlHttp.responseText);
	   if (xmlHttp.readyState==4)
	   { 
	    // alert("11:"+document.getElementById("cl").innerHTML);
	     if(xmlHttp.responseText.charAt(1) == 'i')
	     document.getElementById("cl").innerHTML=xmlHttp.responseText;
	     changePackageName(document.getElementsByName("clientID")[0].value);
		  
	   }
	   
	   else
	 	 {
	 	 	//alert("Request failed.");
	 	 }
	}
	
	
	function changePackageName(v)
    {
		
	  if(v == "")
	  {
	  	return true;
	  }
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
      	 //alert("2:"+xmlHttp.responseText);
          //alert("1: "+document.getElementById("previous").innerHTML);	
         document.getElementById("previous").innerHTML="<table  width=\"402\" class=\"form1\" style=\"border-collapse: collapse\" bordercolor=\"#111111\" cellpadding=\"0\" cellspacing=\"0\"><tbody>"+xmlHttp.responseText+"</tbody></tbody>";
        // replaceInReadOnly(document.getElementById("previous"),"<tbody id = \"previous\">"+xmlHttp.responseText+"</tbody>")
         
         //setTBodyInnerHTML( document.getElementById("previous"),xmlHttp.responseText);
    	  // removeAll();
       }
       
       else
     	 {
     	 	//alert("Request failed.");
     	 }
    }

    function checkRadio()
    {
  	  document.getElementsByName("paymentType")[1].checked = true;
  	  return true;
  	  
    }


</script>

</head>

<body class="body_center_align" onload="checkRadio();">


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

<html:form  action="<%=actionName %>" method="POST" >
                  <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                    <%
  String msg = null;
  if( (msg = (String)session.getAttribute(util.ConformationMessage.MIGRATION_ADD))!= null)
  {
    session.removeAttribute(util.ConformationMessage.MIGRATION_ADD);
    %>
                    <tr> 
                      <td colspan="3" align="center" valign="top" height="50" ><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                    
                    
                    	<tr>
                        <td width="200" style="padding-right: 0" height="21" align="left">Area Code</td>
                        <td width="430" height="22" align="left" style="padding-left: 50px"><div id = "area" ><select name="areaCode" onchange ="changeHandle();">                           
                            <%
					          RegionService rService = new RegionService();
					          ArrayList regionList = rService.getAllRegion();
					          for(int i=0;i<regionList.size();i++)
					        	
					          {
					            RegionDTO regionDTO = (RegionDTO)regionList.get(i);
					            if(regionDTO.getStatus() == RegionConstants.REGION_STATUS_ACTIVE)
					            {
					            %>
                            <option value="<%=regionDTO.getRegionDesc()%>"><%=regionDTO.getRegionName()%></option>
                            <%} } %>
                          </select></div> </td>
                      </tr>
                    
                    
                    
                    <tr>
												<td width="200" style="padding-right: 0;padding-left: 0; font-size: 8pt" align="left">ADSL Phone Number</td>
												<td width="430" height="22" align="left" style="padding-left: 50px"><html:text property="adslPhone" size="10" onchange="changeHandle()"/><label style="color:red; vertical-align: middle;" >*</label></td>
											</tr>			
										<tr>
										
										
										
													<td width="200" align="left"></td>
													<td width="430" align="left" ><html:errors property="migrationClientID" /></td>
												</tr>	
											 <tr>
												<td width="200 style="padding-right: 0" align="left">Client Id</td>
												<td width="430" height="22" align="left" style="padding-left: 50px"><div id = "cl"><html:hidden property="clientID"/><label id ="client"><%=(sform.getClientUserName() == null ||sform .getClientUserName().equals(""))?"":sform.getClientUserName()%></label></div>
												</td>
											</tr>
                    
                    
                    
					 <%--< TR> 
                      <TD valign="top" height="22">Client ID</TD>
                      <TD height="22" colspan="2">
                      <html:select property="clientID" onchange="changePackageName(this.value)" >
                      <%ArrayList clientList = ClientRepository.getInstance().getClientList();
                      for(int i = 0; i<clientList.size();i++)
                      {
                    	  ClientDTO row = (ClientDTO)clientList.get(i);
                    	  if(row.getAccountStatus() == ClientConstants.CLIENTSTATUS_VALUE[0])
                          {
                      
                      %>
                      <html:option value = "<%=Long.toString(row.getUniqueID())%>"><%=row.getUserName()%></html:option>
                      <%}} %>
                      </html:select>
                      </TD>
                    </TR> --%>
                    </table>
                    <div id = "previous">
                    <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                   <tbody >
                   <tr><td width="200" height="19" align="left">Previous Package Type</td><td width="430" height="19" align = "left" style="padding-left: 30px"><label><%=(sform.getPackageType() == null ||sform .getPackageType().equals(""))?"":sform.getPackageType()%></label></td></tr>
                    <tr>
                    <td width="200" height="19" align="left">Current Package</td>
                    <td width="430" height="19" align="left" style="padding-left: 30px"><html:hidden property = "previousPackage"/><label ><%=(sform.getPackageName() == null ||sform .getPackageName().equals(""))?"":sform.getPackageName()%></label></td>
                    </tr>
                   </tbody>
                   </table>
                   </div>
                   <table  width="402" class="form1" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
                   <tr>
                      <td width="200" style="padding-right: 0" height="22" align="left">Package Type</td>
                      <td height="22" align="left"> 
                          
                          <%					          
					        
					          for(int i=0;i<ClientConstants.PACKAGETYPE_VALUE.length;i++)
					          {
					            
					            %>
                          <input type = "radio" value="<%=Integer.toString(ClientConstants.PACKAGETYPE_VALUE[i])%>" name = "paymentType"  onclick="changePackageList(this.value)"/><%=ClientConstants.PACKAGETYPE_NAME[i]%>
                          <% } %>
                       </td>
                    </tr> 
                  
                    
                   
					 <tr>
                      <td width="200" height="19" align="left">Migration Package</td>
                      <td width="430" height="19" align="left"><div id = "packageDiv">  <html:select property="migratedPackage"  size="1">
                        
                        
                        <%
						  
						  ArrayList list = (ArrayList) PackageRepository.getInstance().getPackageList();
						  for(int i = 0; i<list.size();i++)
						  {
							PackageRepositoryDTO packageDTO = (PackageRepositoryDTO)list.get(i);
							if(packageDTO.getPackageType() ==PackageConstants.PACKAGE_TYPE_POSTPAID && packageDTO.getIsDeleted() ==0)
							{
							%>
                        		<html:option value="<%=Long.toString(packageDTO.getPackageID())%>"><%=packageDTO.getPackageName()%></html:option>
	                    <%
							}}
					    %>
                        </html:select></div>    </td>
                    </tr>                
                    
                      <tr>
													<td width="200" align="left"></td>
													<td width="430" align="left"><html:errors property="migratedPackage" /></td>
												</tr>	
                     <tr>
                        <td width="194" style="padding-right: 0" height="21" align="left">Migration Charge</td>
                        <td width="292" height="22" align="left"> <html:text property="migrationCharge"/><label style="color:red; vertical-align: middle;" >*</label></td>
                      </tr>
                      
                        <tr>
													<td width="200" align="left"></td>
													<td width="430" align="left"><html:errors property="migrationCharge" /></td>
												</tr>	
                      
                       <tr>
                        <td width="194" style="padding-right: 0" height="21" align="left">Migration Invoice No</td>
                        <td width="292" height="22" align="left"> <html:text property="migrationInvoiceNo"/><label style="color:red; vertical-align: middle;" >*</label></td>
                      </tr>
                     <tr>
													<td width="200" align="left"></td>
													<td width="430" align="left"><html:errors property="migrationInvoiceNo" /></td>
												</tr>	
                    
                     <tr>
                        <td width="194" style="padding-right: 0" height="21" align="left">Description</td>
                        <td width="292" height="22" align="left"> <html:textarea property="description"/></td>
                      </tr>
                       <tr>
													<td width="200" align="left"></td>
													<td width="430" align="left"><html:errors property="migrationDescription" /></td>
												</tr>	
                    <TR> 
                      <TD valign="top" height="15" align="left"></TD>
                      <TD height="15" colspan="2" align="left">&nbsp;</TD>
                    </TR>
                 
                  <TD valign="top" height="15" align="left" style="padding-left:70px"><html:reset> Reset </html:reset></TD>
                  <TD valign="top" height="15" align="left"><html:submit><%=submitCaption2 %></html:submit> </TD>
                   </table>
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
