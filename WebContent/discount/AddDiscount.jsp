<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.text.SimpleDateFormat"%>


<%@ include file="../includes/checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,discount.*,packages.*,regiontype.*"%>


<%
String submitCaption = "Add Discount";
String actionName = "/AddDiscount";
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>


<html>
<head>
<html:base/>
    <title>Add New Discount</title>
    <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
     <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
    <script  src="../scripts/util.js" type="text/javascript"></script>
    
  
    
    <script type="text/javascript">

      
      
      function selectAll()
      {    
    	//  alert("kundu");
    	  for (var i = 0; i< document.getElementsByName("packageIDList")[0].options.length; i++)
    	  {
    		  document.getElementsByName("packageIDList")[0].options[i].selected = true;
    	  }
    	  
    	 
    	  return true;
     	 
      }
      
      function validate()
      {
    	  selectAll();
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
      
     
      
      function init()
      {	
    	  document.getElementsByName("discountAmountType")[0].checked = true;
    	  return true;
    	  
      }
      
      

      function handleTypes()
      {
    	  if(document.getElementsByName("discountAmountType")[1].checked)
   		  {
   		  	document.getElementById("unit").innerHTML = "%";
   		   
   		  }
    	  
    	  else
   		  {
    		  document.getElementById("unit").innerHTML = "Tk";
   		  }
    	  
    	  return true;
      }
      
      function onClickReset()
      {
    	  document.getElementsByName("discountForm")[0].reset();    	 
    	  document.getElementsByName("discountAmountType")[0].checked = true;
    	  document.getElementById("unit").innerHTML = "Tk";
	  	  document.getElementById("amount").innerHTML = "Discount";
    	  return true;
      }
      
      
      function add()
      {
    	 for (var i = 0; i< document.getElementsByName("packageIDList")[0].length; i++)
   		 {
   		 	if(document.getElementById("package").options[document.getElementById("package").selectedIndex].value ==  document.getElementsByName("packageIDList")[0].options[i].value )
		 	{
   		 		alert("The discount has already been applied to this package.");
		 		return false;
		 	}
   		 }
    	 
    	 var option=document.createElement("option");
    	 option.text = document.getElementById("package").options[document.getElementById("package").selectedIndex].text;
    	 option.value = document.getElementById("package").options[document.getElementById("package").selectedIndex].value;
    	 try
    	 {
    	  // for IE earlier than version 8
    	  	document.getElementsByName("packageIDList")[0].add(option,document.getElementsByName("packageIDList")[0].options[null]);
    	 }
    	 catch (e)
    	 {
    		document.getElementsByName("packageIDList")[0].add(option,null);
    	 }
      }
      
      function remove()
      {
    	  document.getElementsByName("packageIDList")[0].options[document.getElementsByName("packageIDList")[0].selectedIndex] = null;
    	  return true;
      }
      
      function addAll()
      {
    	  document.getElementsByName("packageIDList")[0].options.length = 0;
    	  for (var i = 0; i< document.getElementById("package").options.length; i++)
    	  {
    		  var option=document.createElement("option");
   	     	  option.text = document.getElementById("package").options[i].text;
    	      option.value = document.getElementById("package").options[i].value;
    	      try
    	      {
    	     	  // for IE earlier than version 8
    	     	 document.getElementsByName("packageIDList")[0].add(option,document.getElementsByName("packageIDList")[0].options[null]);
    	      }
    	      catch (e)
    	      {
    	     	document.getElementsByName("packageIDList")[0].add(option,null);
    	      }
    	  }
    	  
    	  return true;
     	 
     	 
      }
      
      function removeAll()
      {
    	  document.getElementsByName("packageIDList")[0].options.length = 0;
    	  return true;
      }
      
      
      
      </script>
    </head>

    <body class="body_center_align" onload="init();">
      <table border="0" cellpadding="0" cellspacing="0"  width="780" id="AutoNumber1">
        <tr><td width="100%"><%@ include file="../includes/header.jsp"  %></td></tr>
        <tr><td width="100%"><table border="0" cellpadding="0" cellspacing="0"  width="780" id="AutoNumber2">
          <tr>
          	
         
            <td width="<%=("780")%>" valign="top" class="td_main" align="center">
                  <table border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tr><td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                      <div class="div_title">Create New Discount</div>
            </td></tr>
<tr><td width="100%" align="center" style="padding-left: 160px"><br/>
  <html:form  action="<%=actionName %>" method="POST" onsubmit="return validate();" >

                  <table width="500" border="0" cellpadding="0" cellspacing="0" class="form1">
                    <%
    String msg = null;
    if( (msg = (String)session.getAttribute(util.ConformationMessage.DISCOUNT_ADD))!= null)
    {
      session.removeAttribute(util.ConformationMessage.DISCOUNT_ADD);
      %>
                    <tr>
                      <td colspan="2" align="center" valign="top" height="50"><b><%=msg%></b></td>
                    </tr>
                    <%}%>
                    
                    <!-- <tr>
                      <td width="195" height="39" style="font-weight: bold">
                        Account Information
                        Connection Information</td>
                      <td width="292" height="39">&nbsp;</td>
                    </tr> -->
                    
                    <tr>
                      <td width="200" height="25" align="left">Discount Name</td>
                      <td width="300" height="25" align="left"><html:text property="discountName" size="20" /><label style="color:red; vertical-align: middle;" >*</label></td>
                    </tr>
                    
                    
                     <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="discountName" /></td>
												</tr>
                   
                    <tr>
                      <td width="200" height="40" align="left">Discount Description</td>
                      <td width="300" height="40" align="left"><html:textarea property="discountDescription" /></td>
                      <td></td>
                    </tr>
                    <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="discountDescription" /></td>
												</tr>
                    
                    
                    <tr>
                      <td style="padding-right: 0" height="25" align="left">Discount Category</td>
                      <td height="25" align="left"> <select name="discountCategoryId">                          
                          <% 
					          for(int i=0;i<DiscountConstants.DISCOUNT_CATEGORY_NAMES.length;i++)
					          {
					            
					       %>
                          		<option value="<%=DiscountConstants.DISCOUNT_CATEGORY_VALUES[i]%>"><%=DiscountConstants.DISCOUNT_CATEGORY_NAMES[i]%></option>
                          <% } %>
                        </select></td>
                    </tr>
                    
                    
                     <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="discountCategory" /></td>
												</tr>
                    
                 
                    
                    
                    
                    
                    <tr>
                            <td   align="left" height="25">
                            Activation Date
                            </td>
                            <td  align="left" height="25">
                              <html:text property = "discountActivationDate" size="21" readonly="readonly"
                              value="<%=new String(format.format(new Date())) %>"/>
                              <script type="text/javascript" >
                              new tcal ({
                                // form name
                                'formname': 'discountForm',
                                // input name
                                'controlname': 'discountActivationDate'
                              });
                              </script>
                            </td>
                          </tr>
                      

 <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="discountActivationDate" /></td>
												</tr>

				<tr>
                            <td  align="left" height="25">
                            Expiration Date
                            </td>
                            <td  align="left" height="25">
                              <html:text property = "discountExpirationDate" size="21" readonly="readonly"
                              value="<%=new String(format.format(new Date())) %>"/>
                              <script type="text/javascript" >
                              new tcal ({
                                // form name
                                'formname': 'discountForm',
                                // input name
                                'controlname': 'discountExpirationDate'
                              });
                              </script>
                            </td>
                          </tr>
                    
                     <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="discountExpirationDate" /></td>
												</tr>
                    
                   
                    
                    <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="discountDate" /></td>
												</tr>
                    
                    <tr>
                      <td width="200" height="25" align="left">Amount Type</td>
                      <td width="300" height="25" align="left">
                      
                      <%for(int i = 0; i<DiscountConstants.DISCOUNT_AMOUNTTYPE_VALUES.length;i++) { %>
                      <html:radio property="discountAmountType" value = "<%= Integer.toString(DiscountConstants.DISCOUNT_AMOUNTTYPE_VALUES[i]) %>" onclick="handleTypes();"/><%=DiscountConstants.DISCOUNT_AMOUNTTYPE_NAMES[i]%>
                      <%} %>
<label style="color:red; vertical-align: middle;" >*</label>
					 </td>
                    </tr>
                    
                     <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="discountAmountType" /></td>
												</tr>
                    
                    
                    <tr>
                      <td width="200" height="25" align="left"><label id = "amount" >Discount</label></td>
                      <td width="292" height="25" align="left"> <html:text property="discountAmount"></html:text><label id ="unit">Tk</label><label style="color:red; vertical-align: middle;" >*</label></td>
                    </tr>                 
                     
                     
                      <tr>
													<td width="150"></td>
													<td width="430"><html:errors property="discountAmount" /></td>
												</tr>
                                  
                                   
                    <tr>
                      <td width="195" height="59" align="left">Add to Package</td>
                      <td width="100" height="59" align="left"> <div  id = "packageDiv"><select id="package"  size="1">                        
                        <%
						  PackageService packageService = new PackageService();
						  ArrayList list = (ArrayList)packageService.getDTOs(packageService.getIDs(loginDTO));
						  for(int i = 0; i<list.size();i++)
						  {
							PackageDTO packageDTO = (PackageDTO)list.get(i);	
							if(packageDTO.getPackageType() == PackageConstants.PACKAGE_TYPE_POSTPAID)
							{
							
							%>
                        		<option value="<%=packageDTO.getPackageID()%>"><%=packageDTO.getPackageName()%></option>
	                    <%
							}
						}
					    %>
                        </select></div> </td>
                        </tr>
                        <tr>
                        <td width="254" rowspan="1" align="left">
                        <table >
                        <tr><input class="cmd"  type="button" value="Add" name="AddPackage" onClick = "add();">&nbsp;</tr>
                        <tr><input class="cmd"  type="button" value="Remove" name="RemovePackage" onClick = "remove();">&nbsp;</tr>
                        <tr><input class="cmd"  type="button" value="Add All" name="AddAll" onClick = "addAll();">&nbsp;</tr>
                        <tr><input class="cmd"  type="button" value="Remove All" name="RemoveAll" onClick = "removeAll()">&nbsp;</tr>
                        </table>
                        </td>
                        
                        <td width = "254" align="left">
                        <html:select property="packageIDList" multiple = "true" >
                        
                        </html:select>
                        
                        </td>
                    </tr>      
                    
                    
                  </table>
    <br/>

    <table border="0" cellpadding="0" cellspacing="0" class="form1" width="487">
      <tr>

        <td width="319" height="25" align="left" style="padding-left: 60px">
          <input class="cmd"  type="button" value="Reset" name="B1" onClick = "return onClickReset();">&nbsp;
          <input class="cmd" type="submit" value="<%=submitCaption%>" name="B2">
        </td>
      </tr>
    </table>

  </html:form>
  <!-- end of the form -->
  <br/>
</td></tr></table></td></tr></table></td></tr>
<tr><td width="100%"><%@ include file="../includes/footer.jsp"%></td></tr>
</table></body></html>
