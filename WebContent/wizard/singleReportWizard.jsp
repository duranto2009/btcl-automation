<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="exchange.ExchangeRepository"%>
<%@page import="operator.OperatorDAO"%>
<%@page import="dslm.DslmService"%>
<%@page import="role.RoleDTO"%>
<%@page import="role.RoleService"%>
<%@page import="user.UserRepository"%>
<%@page import="user.UserDTO"%>
<%@page import="regiontype.RegionDTO"%>
<%@page import="regiontype.RegionService"%>
<%@page import="report.ClientDAO"%>
<%@page import="packages.PackageService"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="report.ReportOptions"%>
<%@ include file="../includes/checkLogin.jsp" %>
<%@ page language="java" %>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="../common/failure.jsp" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>

<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>
<html>
 <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css">
     <link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">
    <script  src="../scripts/util.js" type="text/javascript"></script>
    
<script language="JavaScript">
var add = 1;
var addColumns = 1;
var idForSelect = "";
var idForSelected = "";
var idForCheckBox = "";
function refreshFields()
{
	var index = document.getElementsByName("infoSectionSelected")[0].selectedIndex ;
	var val = document.getElementsByName("infoSectionSelected")[0].options[index].value;
	var table = document.getElementById("filter");
	 var rowCount = table.rows.length;
	 var row;
	switch(val)
		{
		case "Customer Details":
			getFilters("customerDetailsDiv");
			document.getElementById("customerDetails").style.display = 'block';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'block';

			clearFields();
			
			
			 var option=document.createElement("option");
	    	 option.text = "User Name";
	    	 option.value = "User Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 
	/*     	 var option111=document.createElement("option");
	    	 option111.text = "Login Password";
	    	 option111.value = "Login Password";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option111,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option111,null);
	    	 } */
	    	 
	    	 var option1=document.createElement("option");
	    	 option1.text = "ADSL Phone";
	    	 option1.value = "ADSL Phone";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option1,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option1,null);
	    	 }
	    	 var option251=document.createElement("option");
	    	 option251.text = "Full Name";
	    	 option251.value = "Full Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option251,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option251,null);
	    	 }
	    	 var option351=document.createElement("option");
	    	 option351.text = "Address";
	    	 option351.value = "Address";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option351,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option351,null);
	    	 }
	    	
	    	 var option3=document.createElement("option");
	    	 option3.text = "Web Username";
	    	 option3.value = "Web Username";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 
	    /* 	 var option112=document.createElement("option");
	    	 option112.text = "Web Password";
	    	 option112.value = "Web Password";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option112,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option112,null);
	    	 } */
	    	
	    	 var option4=document.createElement("option");
	    	 option4.text = "Type Of Connection";
	    	 option4.value = "Type Of Connection";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "Payment Type";
	    	 option5.value = "Payment Type";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Application Status";
	    	 option6.value = "Application Status";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
	    	 var option7=document.createElement("option");
	    	 option7.text = "Account Status";
	    	 option7.value = "Account Status";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option7,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option7,null);
	    	 }
	    	 var option71=document.createElement("option");
	    	 option71.text = "Connection Status";
	    	 option71.value = "Connection Status";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option71,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option71,null);
	    	 }
	    	 var option8=document.createElement("option");
	    	 option8.text = "Package";
	    	 option8.value = "Package";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option8,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option8,null);
	    	 }
	    	 var option9=document.createElement("option");
	    	 option9.text = "Region";
	    	 option9.value = "Region";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option9,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option9,null);
	    	 }
	    	 
	    	 var option11=document.createElement("option");
	    	 option11.text = "DSLAM";
	    	 option11.value = "DSLAM";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option11,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option11,null);
	    	 }
	    	 var option12=document.createElement("option");
	    	 option12.text = "Port";
	    	 option12.value = "Port";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option12,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option12,null);
	    	 }
	    	 var option13=document.createElement("option");
	    	 option13.text = "Added On";
	    	 option13.value = "Added On";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option13,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option13,null);
	    	 }
	    	 var option14=document.createElement("option");
	    	 option14.text = "Verified On";
	    	 option14.value = "Verified On";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option14,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option14,null);
	    	 }
	    	 var option15=document.createElement("option");
	    	 option15.text = "Approved On";
	    	 option15.value = "Approved On";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option15,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option15,null);
	    	 }
	    	 var option16=document.createElement("option");
	    	 option16.text = "Exchange Name";
	    	 option16.value = "Exchange Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option16,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option16,null);
	    	 }
	    	 var option17=document.createElement("option");
	    	 option17.text = "Operator";
	    	 option17.value = "Operator";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option17,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option17,null);
	    	 }
	    	 var option18=document.createElement("option");
	    	 option18.text = "Blacklist Status";
	    	 option18.value = "Blacklist Status";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option18,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option18,null);
	    	 }
	    	 /*var option19=document.createElement("option");
	    	 option19.text = "Registration";
	    	 option19.value = "Registration";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option19,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option19,null);
	    	 }*/
	    	 var option20=document.createElement("option");
	    	 option20.text = "Bonus";
	    	 option20.value = "Bonus";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option20,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option20,null);
	    	 }
	    	 var option22=document.createElement("option");
	    	 option22.text = "Bonus Category";
	    	 option22.value = "Bonus Category";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option22,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option22,null);
	    	 }
	    	 var option21=document.createElement("option");
	    	 option21.text = "Discount";
	    	 option21.value = "Discount";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option21,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option21,null);
	    	 }
	    	 var option23=document.createElement("option");
	    	 option23.text = "Discount Category";
	    	 option23.value = "Discount Category";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option23,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option23,null);
	    	 }
			removeFilter();
			
			break;
		case "System Admin":
			getFilters("systemAdmin");
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'block';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';
			
			clearFields();
			
			var option=document.createElement("option");
	    	 option.text = "UserName";
	    	 option.value = "UserName";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Role";
	    	 option2.value = "Role";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option3=document.createElement("option");
	    	 option3.text = "Full Name";
	    	 option3.value = "Full Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "Designation";
	    	 option4.value = "Designation";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "Email";
	    	 option5.value = "Email";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Phone";
	    	 option6.value = "Phone";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
			
			removeFilter();
			break;
		case "Region Wise":
			getFilters("regionWise");
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'block';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';

			clearFields();
			var option=document.createElement("option");
	    	 option.text = "Region Name";
	    	 option.value = "Region Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Region Code";
	    	 option2.value = "Region Code";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option3=document.createElement("option");
	    	 option3.text = "No. Of Customers";
	    	 option3.value = "No. Of Customers";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "Parent Region";
	    	 option4.value = "Parent Region";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
			
			removeFilter();
			break;
		case "Package Wise Customer Details":
			getFilters("packageWiseCustomerDetailsDiv");
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'block';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'block';
			document.getElementById("customerDetailsDiv").style.display = 'none';

			clearFields();
			var option=document.createElement("option");
	    	 option.text = "Package Name";
	    	 option.value = "Package Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "No. Of Customers";
	    	 option2.value = "No. Of Customers";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option8=document.createElement("option");
	    	 option8.text = "New Customer";
	    	 option8.value = "New Customer";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option8,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option8,null);
	    	 }
	    	 var option7=document.createElement("option");
	    	 option7.text = "Bonus Granted";
	    	 option7.value = "Bonus Granted";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option7,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option7,null);
	    	 }
	    	 var option9=document.createElement("option");
	    	 option9.text = "Special Discount Offered";
	    	 option9.value = "Special Discount Offered";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option9,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option9,null);
	    	 }
	    	 var option10=document.createElement("option");
	    	 option10.text = "Festival Discount Offered";
	    	 option10.value = "Festival Discount Offered";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option10,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option10,null);
	    	 }
	    	 /*var option3=document.createElement("option");
	    	 option3.text = "Online Registered";
	    	 option3.value = "Online Registered";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "System Registered";
	    	 option4.value = "System Registered";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }*/
	    	 /*var option5=document.createElement("option");
	    	 option5.text = "Renewed With Discount";
	    	 option5.value = "Renewed With Discount";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Renewed Without Discount";
	    	 option6.value = "Renewed Without Discount";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
	    	 var option7=document.createElement("option");
	    	 option7.text = "Bonus Granted";
	    	 option7.value = "Bonus Granted";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option7,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option7,null);
	    	 }*/
	    	 
	    	/*var option9=document.createElement("option");
	    	 option9.text = "Renewed Customer";
	    	 option9.value = "Renewed Customer";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option9,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option9,null);
	    	 }
			*/
			removeFilter();
			
			break;
		case "Billing Packages":
			getFilters("billingPackages");
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'block';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';

			clearFields();
			 var option=document.createElement("option");
	    	 option.text = "Package Name";
	    	 option.value = "Package Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Package Type";
	    	 option2.value = "Package Type";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option3=document.createElement("option");
	    	 option3.text = "Validity Period";
	    	 option3.value = "Validity Period";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "Is Unlimited";
	    	 option4.value = "Is Unlimited";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "Total Volume Limit";
	    	 option5.value = "Total Volume Limit";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Data Transfer Rate";
	    	 option6.value = "Data Transfer Rate";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
	    	 var option7=document.createElement("option");
	    	 option7.text = "Unit Type";
	    	 option7.value = "Unit Type";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option7,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option7,null);
	    	 }
	    	 var option8=document.createElement("option");
	    	 option8.text = "Vat";
	    	 option8.value = "Vat";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option8,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option8,null);
	    	 }
	    	 var option9=document.createElement("option");
	    	 option9.text = "Tariff Type";
	    	 option9.value = "Tariff Type";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option9,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option9,null);
	    	 }
	    	 var option10=document.createElement("option");
	    	 option10.text = "Costing Type";
	    	 option10.value = "Costing Type";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option10,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option10,null);
	    	 }
	    	 var option11=document.createElement("option");
	    	 option11.text = "Configuration Charge";
	    	 option11.value = "Configuration Charge";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option11,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option11,null);
	    	 }
	    	 var option12=document.createElement("option");
	    	 option12.text = "Registration Charge";
	    	 option12.value = "Registration Charge";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option12,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option12,null);
	    	 }
	    	 var option13=document.createElement("option");
	    	 option13.text = "Shifting Charge";
	    	 option13.value = "Shifting Charge";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option13,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option13,null);
	    	 }
	    	
			removeFilter();
			
			break;
		case "Traffic Details":
			getFilters("trafficDetailsDiv");
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'block';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'block';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';

			clearFields();
			var option=document.createElement("option");
	    	 option.text = "Customer ID";
	    	 option.value = "Customer ID";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Caller ID";
	    	 option2.value = "Caller ID";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option3=document.createElement("option");
	    	 option3.text = "From(Date)";
	    	 option3.value = "From(Date)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "To(Date)";
	    	 option4.value = "To(Date)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "No. Of Login";
	    	 option5.value = "No. Of Login";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Total Upload(MB)";
	    	 option6.value = "Total Upload(MB)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
	    	 var option7=document.createElement("option");
	    	 option7.text = "Total Download(MB)";
	    	 option7.value = "Total Download(MB)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option7,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option7,null);
	    	 }
	    	 var option8=document.createElement("option");
	    	 option8.text = "Average Upload(MB)";
	    	 option8.value = "Average Upload(MB)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option8,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option8,null);
	    	 }
	    	 var option9=document.createElement("option");
	    	 option9.text = "Average Download(MB)";
	    	 option9.value = "Average Download(MB)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option9,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option9,null);
	    	 }
	    	 var option10=document.createElement("option");
	    	 option10.text = "Total Data Transfer";
	    	 option10.value = "Total Data Transfer";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option10,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option10,null);
	    	 }
	    	 var option11=document.createElement("option");
	    	 option11.text = "Average Data Transfer(Per Login)";
	    	 option11.value = "Average Data Transfer(Per Login)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option11,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option11,null);
	    	 }
	    	 var option16=document.createElement("option");
	    	 option16.text = "Exchange Name";
	    	 option16.value = "Exchange Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option16,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option16,null);
	    	 }
			
			removeFilter();
			break;
		case "Tariff Details":
			getFilters("tariffDetailsDiv");
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'block';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'block';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';
			

			clearFields();
			 var option=document.createElement("option");
	    	 option.text = "Tariff ID";
	    	 option.value = "Tariff ID";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Package Name";
	    	 option2.value = "Package Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option3=document.createElement("option");
	    	 option3.text = "Activation Date";
	    	 option3.value = "Activation Date";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "Expiry Date";
	    	 option4.value = "Expiry Date";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "Package Value";
	    	 option5.value = "Package Value";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Unit Rate";
	    	 option6.value = "Unit Rate";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
	    	 var option7=document.createElement("option");
	    	 option7.text = "Additional Usage Charge";
	    	 option7.value = "Additional Usage Charge";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option7,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option7,null);
	    	 }
	    	
	    	

			removeFilter();
			
			break;
		case "Usage Details":
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'block';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';
			
			clearFields();
			var option=document.createElement("option");
	    	 option.text = "Connect Time";
	    	 option.value = "Connect Time";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Disconnect Time";
	    	 option2.value = "Disconnect Time";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option3=document.createElement("option");
	    	 option3.text = "Upload(MB)";
	    	 option3.value = "Upload(MB)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "Download(MB)";
	    	 option4.value = "Download(MB)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "Total Data Transfer";
	    	 option5.value = "Total Data Transfer";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
			
			removeFilter();
			break;
		case "Bandwidth Consumption and Data Transfer":
			getFilters("bwAndDataTransferDiv");
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'block';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'block';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';
			
			clearFields();
			 var option=document.createElement("option");
	    	 option.text = "Client ID";
	    	 option.value = "Client ID";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Username";
	    	 option2.value = "Username";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option3=document.createElement("option");
	    	 option3.text = "Caller ID";
	    	 option3.value = "Caller ID";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "From(Date)";
	    	 option4.value = "From(Date)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "To(Date)";
	    	 option5.value = "To(Date)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Upload(MB)";
	    	 option6.value = "Upload(MB)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
	    	 var option7=document.createElement("option");
	    	 option7.text = "Download(MB)";
	    	 option7.value = "Download(MB)";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option7,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option7,null);
	    	 }
	    	 var option8=document.createElement("option");
	    	 option8.text = "Total Data Transfer";
	    	 option8.value = "Total Data Transfer";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option8,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option8,null);
	    	 }
	    	 var option16=document.createElement("option");
	    	 option16.text = "Exchange Name";
	    	 option16.value = "Exchange Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option16,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option16,null);
	    	 }
			
			removeFilter();
			break;
		case "Type of Connectivity Services":
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'block';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';
			clearFields();
			
			var option=document.createElement("option");
	    	 option.text = "Type Of Connection";
	    	 option.value = "Type Of Connection";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "No. Of Customers";
	    	 option2.value = "No. Of Customers";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	/* var option3=document.createElement("option");
	    	 option3.text = "Online Registered";
	    	 option3.value = "Online Registered";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "System Registered";
	    	 option4.value = "System Registered";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "Renewed With Discount";
	    	 option5.value = "Renewed With Discount";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Renewed Without Discount";
	    	 option6.value = "Renewed Without Discount";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
	    	 var option7=document.createElement("option");
	    	 option7.text = "Bonus Granted";
	    	 option7.value = "Bonus Granted";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option7,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option7,null);
	    	 }*/
	    	 var option8=document.createElement("option");
	    	 option8.text = "New Customer";
	    	 option8.value = "New Customer";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option8,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option8,null);
	    	 }
	    	/* var option9=document.createElement("option");
	    	 option9.text = "Renewed Customer";
	    	 option9.value = "Renewed Customer";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option9,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option9,null);
	    	 }
			*/
			removeFilter();
			break;
		case "Login Status":
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'block';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';
			
			clearFields();
			var option=document.createElement("option");
	    	 option.text = "Login Time";
	    	 option.value = "Login Time";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Login Status";
	    	 option2.value = "Login Status";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
			
			removeFilter();
			break;
		case "Client Status":
			getFilters("clientStatusDiv");
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'block';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'none';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'block';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';
			clearFields();
			var option=document.createElement("option");
	    	 option.text = "Client ID";
	    	 option.value = "Client ID";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option,null);
	    	 }
	    	 var option2=document.createElement("option");
	    	 option2.text = "Client Name";
	    	 option2.value = "Client Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option2,null);
	    	 }
	    	 var option21=document.createElement("option");
	    	 option21.text = "Username";
	    	 option21.value = "Username";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option21,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option21,null);
	    	 }
	    	 var option22=document.createElement("option");
	    	 option22.text = "ADSL Phone";
	    	 option22.value = "ADSL Phone";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option22,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option22,null);
	    	 }
	    	 var option3=document.createElement("option");
	    	 option3.text = "Previous Status";
	    	 option3.value = "Previous Status";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option3,null);
	    	 }
	    	 var option4=document.createElement("option");
	    	 option4.text = "Current Status";
	    	 option4.value = "Current Status";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option4,null);
	    	 }
	    	 var option5=document.createElement("option");
	    	 option5.text = "Changing Date";
	    	 option5.value = "Changing Date";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option5,null);
	    	 }
	    	 var option6=document.createElement("option");
	    	 option6.text = "Cause Of Change";
	    	 option6.value = "Cause Of Change";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option6,null);
	    	 }
	    	 var option16=document.createElement("option");
	    	 option16.text = "Exchange Name";
	    	 option16.value = "Exchange Name";
	    	 try
	    	 {
	    	  // for IE earlier than version 8
	    	  	document.getElementsByName("columns")[0].add(option16,document.getElementsByName("columns")[0].options[null]);
	    	 }
	    	 catch (e)
	    	 {
	    		document.getElementsByName("columns")[0].add(option16,null);
	    	 }
			removeFilter();
			break;
		case "Password Change":
			document.getElementById("customerDetails").style.display = 'none';
			document.getElementById("tariffDetails").style.display = 'none';
			document.getElementById("bwAndDataTransfer").style.display = 'none';
			document.getElementById("usageDetailsReport").style.display = 'none';
			document.getElementById("packageWiseCustomerDetails").style.display = 'none';
			document.getElementById("typeOfConnectivity").style.display = 'none';
			document.getElementById("trafficDetails").style.display = 'none';
			document.getElementById("loginStatus").style.display = 'none';
			document.getElementById("clientStatus").style.display = 'none';
			document.getElementById("systemAdmin").style.display = 'none';
			document.getElementById("billingPackages").style.display = 'none';
			document.getElementById("regionWise").style.display = 'none';
			document.getElementById("passwordChange").style.display = 'block';
			document.getElementById("bwAndDataTransferDiv").style.display = 'none';
			document.getElementById("tariffDetailsDiv").style.display = 'none';
			document.getElementById("clientStatusDiv").style.display = 'none';
			document.getElementById("trafficDetailsDiv").style.display = 'none';
			document.getElementById("packageWiseCustomerDetailsDiv").style.display = 'none';
			document.getElementById("customerDetailsDiv").style.display = 'none';
			clearFields();
			
			 var option1=document.createElement("option");
			 option1.text = "ADSL Phone";
			 option1.value = "ADSL Phone";
			 try
			 {
			  // for IE earlier than version 8
			  	document.getElementsByName("columns")[0].add(option1,document.getElementsByName("columns")[0].options[null]);
			 }
			 catch (e)
			 {
				document.getElementsByName("columns")[0].add(option1,null);
			 }
			 var option2=document.createElement("option");
			 option2.text = "Name";
			 option2.value = "Name";
			 try
			 {
			  // for IE earlier than version 8
			  	document.getElementsByName("columns")[0].add(option2,document.getElementsByName("columns")[0].options[null]);
			 }
			 catch (e)
			 {
				document.getElementsByName("columns")[0].add(option2,null);
			 }
			 var option3=document.createElement("option");
			 option3.text = "Old Password";
			 option3.value = "Old Password";
			 try
			 {
			  // for IE earlier than version 8
			  	document.getElementsByName("columns")[0].add(option3,document.getElementsByName("columns")[0].options[null]);
			 }
			 catch (e)
			 {
				document.getElementsByName("columns")[0].add(option3,null);
			 }
			 var option4=document.createElement("option");
			 option4.text = "Current Password";
			 option4.value = "Current Password";
			 try
			 {
			  // for IE earlier than version 8
			  	document.getElementsByName("columns")[0].add(option4,document.getElementsByName("columns")[0].options[null]);
			 }
			 catch (e)
			 {
				document.getElementsByName("columns")[0].add(option4,null);
			 }
			 var option5=document.createElement("option");
			 option5.text = "Change Date";
			 option5.value = "Change Date";
			 try
			 {
			  // for IE earlier than version 8
			  	document.getElementsByName("columns")[0].add(option5,document.getElementsByName("columns")[0].options[null]);
			 }
			 catch (e)
			 {
				document.getElementsByName("columns")[0].add(option5,null);
			 }
			 var option6=document.createElement("option");
			 option6.text = "Password Type";
			 option6.value = "Password Type";
			 try
			 {
			  // for IE earlier than version 8
			  	document.getElementsByName("columns")[0].add(option6,document.getElementsByName("columns")[0].options[null]);
			 }
			 catch (e)
			 {
				document.getElementsByName("columns")[0].add(option6,null);
			 }
			removeFilter();
			break;
		
	}
	//alert("inside refresh fields" + val);
}
function addOneColumn()
{
    var index1 = document.getElementsByName("columns")[0].selectedIndex;
    var index2 = document.getElementsByName("columnsSelected")[0].selectedIndex;
    var matched = false;
    var val = document.getElementsByName("columns")[0].options[index1].value;
    var inner = document.getElementsByName("columns")[0].options[index1].text;
    var matchedIndex = -1;
    if(addColumns == 1)
	 {
    	for(var i=0;i<document.getElementsByName("columnsSelected")[0].options.length;i++)
	    	 {
	    		 if(document.getElementsByName("columnsSelected")[0].options[i].value == val)
	    		{
	    			 matched = true;
	    			 matchedIndex = i;
	    			 break;
	    		}
	    	 }
	       	 if(matched == false)
	         {
	       		var option = document.createElement("option");
	      		option.value = inner;
	      		option.text = inner;
	      		 try
		    	 {
		    	  // for IE earlier than version 8
		    	  	document.getElementsByName("columnsSelected")[0].add(option,document.getElementsByName("columnsSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("columnsSelected")[0].add(option,null);
		    	 }
	       		 var lastIndex = document.getElementsByName("columnsSelected")[0].options.length -1;
	       		document.getElementsByName("columnsSelected")[0].options[lastIndex].value = val;
	       		 index2 = lastIndex;
	       		 index1 = 0;
	       		 //addFilter();
	         }
	       	 else
	       	 {
	       		document.getElementsByName("columnsSelected")[0].selectedIndex = matchedIndex;
	       	 }     
    }
    else
    {
	   	
    	document.getElementsByName("columnsSelected")[0].options.remove(index2);
	   	 	//removeFilter();
    }
}
function addAllColumn()
{
	var matched;
	 if(addColumns == 1)
	 {
		for(var i=0;i<document.getElementsByName("columns")[0].options.length;i++)
	    {
			matched = false;
	      	var val = document.getElementsByName("columns")[0].options[i].value;
	      	var inner = document.getElementsByName("columns")[0].options[i].text;
	      	for(var j = 0;j<document.getElementsByName("columnsSelected")[0].options.length;j++)
	      	{
	      		if(document.getElementsByName("columnsSelected")[0].options[j].value == val)
	      		{
	      			matched = true;
	      		}
	      	}
	      	if(matched == false)
	      	{
	      		var option = document.createElement("option");
	      		option.value = inner;
	      		option.text = inner;
	      		 try
		    	 {
		    	  // for IE earlier than version 8
		    	  	document.getElementsByName("columnsSelected")[0].add(option,document.getElementsByName("columnsSelected")[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName("columnsSelected")[0].add(option,null);
		    	 }
	      		document.getElementsByName("columnsSelected")[0].options[j].value = val;
	      		document.getElementsByName("columns")[0].selectedIndex = 0;
	      	}
	    }
	 }
     else
     {
    	 var len = document.getElementsByName("columnsSelected")[0].options.length - 1;
    	 for(var i = len; i>=0;i--)
    	 {
    		 document.getElementsByName("columnsSelected")[0].options.remove(i);
    	 	//removeFilter();
    	 }
     }
}
function changeButtonName(name)
{
	if(name == "columns")
	{
		document.getElementsByName("addColumnOne")[0].value = ">";
		document.getElementsByName("addColumnAll")[0].value = ">>";
		addColumns = 1;
	}
	else
	{
		document.getElementsByName("addColumnOne")[0].value = "<";
		document.getElementsByName("addColumnAll")[0].value = "<<";
		addColumns = 0;
	}
}

function generateReport()
{
	for (var i = 0; i< document.getElementsByName("columnsSelected")[0].options.length; i++)
	{
		document.getElementsByName("columnsSelected")[0].options[i].selected = true;
	}
	for (var j = 0; j< document.getElementsByName(idForSelected)[0].options.length; j++)
	{
		document.getElementsByName(idForSelected)[0].options[j].selected = true;
	}
	var val = document.getElementsByName("infoSectionSelected")[0].options[index].value;
	
}

function clearFields()
{
	if(document.getElementsByName("columns")[0].options.length>0)
	{
		var len = document.getElementsByName("columns")[0].options.length - 1;
		for(var i = len; i>=0;i--)
		{
			document.getElementsByName("columns")[0].options.remove(i);
		}
	}
	if(document.getElementsByName("columnsSelected")[0].options.length>0)
	{
		var len = document.getElementsByName("columnsSelected")[0].length - 1;
		for(var i = len; i>=0;i--)
		{
			document.getElementsByName("columnsSelected")[0].options.remove(i);
		}
	}
}


function removeFilter()
{
	var table = document.getElementById("filter");
    var rowCount = table.rows.length;

    for(var i = rowCount-1; i>=0;i--) 
    {
        table.deleteRow(i);
 	}
}
function upOptionValue()
{
    var index = document.getElementsByName("columnsSelected")[0].selectedIndex;
    
    if(index>0)
    {
    	var val = document.getElementsByName("columnsSelected")[0].options[index].value;
    	var inner = document.getElementsByName("columnsSelected")[0].options[index].text;
    	
    	document.getElementsByName("columnsSelected")[0].options[index].value = document.getElementsByName("columnsSelected")[0].options[index-1].value;
    	document.getElementsByName("columnsSelected")[0].options[index].text = document.getElementsByName("columnsSelected")[0].options[index-1].text;
  		
    	document.getElementsByName("columnsSelected")[0].options[index-1].value = val;
    	document.getElementsByName("columnsSelected")[0].options[index-1].text = inner;
    	document.getElementsByName("columnsSelected")[0].selectedIndex = index-1;
  		//form.columnsSelected[index-1].selected = true;
    }
}
function downOptionValue()
{
    var index = document.getElementsByName("columnsSelected")[0].selectedIndex;
    
    if(index<document.getElementsByName("columnsSelected")[0].options.length)
    {
    	var val = document.getElementsByName("columnsSelected")[0].options[index].value;
    	var inner = document.getElementsByName("columnsSelected")[0].options[index].text;
    	
    	document.getElementsByName("columnsSelected")[0].options[index].value = document.getElementsByName("columnsSelected")[0].options[index+1].value;
    	document.getElementsByName("columnsSelected")[0].options[index].text = document.getElementsByName("columnsSelected")[0].options[index+1].text;
  		
    	document.getElementsByName("columnsSelected")[0].options[index+1].value = val;
    	document.getElementsByName("columnsSelected")[0].options[index+1].text = inner;
    	document.getElementsByName("columnsSelected")[0].selectedIndex = index+1;
  		//form.columnsSelected[index+1].selected = true;
    }
}
function checkPair(id1,id2)
{
	if(id1.checked == true)
	{
		id2.checked = true;	
	}
	else
	{
		id2.checked = false;
	}
}
function validate()
{
	var form = document.forms[0];
	var reportTitle = document.getElementById("reportTitle");  
    
    if(!validateRequired(reportTitle.value))
    {
      alert("Please Enter Report Title");
      reportTitle.value = "";
      reportTitle.focus();
      return false;
    }
    if(document.getElementsByName("infoSectionSelected")[0].options.length == 0)
	{
		alert("please select a report type");
		return false;
	}
	if(document.getElementsByName("columnsSelected")[0].options.length == 0)
	{
		alert("Please select some columns to generate report");		
		return false;
	}
	
	if(document.getElementsByName(idForCheckBox)[0].checked == true && document.getElementsByName(idForSelected)[0].options.length <= 0)
		{
		
		alert("Please add exchange");		
		return false;
		}
	return true;
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
function getFilters(type)
{
	 xmlHttp = GetXmlHttpObject();
	  if(xmlHttp == null)
	  {
		  alert("Browser is not supported.");
		  return false;
	  }
		var url="../GetFilters.do?type="+type;
		//alert(url);
	  	xmlHttp.onreadystatechange = function(){typeChanged(xmlHttp, type);};
	  	xmlHttp.open("GET",url,false);    	  	
	  	xmlHttp.send(null);    	  	
	  	return true;
}

function typeChanged(xmlHttp, type) 
	{ 
	   //alert("responsetext is "+xmlHttp.responseText);
	   if (xmlHttp.readyState==4)
	   { 
	    // alert(document.getElementById("systemAdmin").innerHTML);
	     //alert("responsetext is "+xmlHttp.responseText);
	     document.getElementById(type).innerHTML = xmlHttp.responseText;
	     //alert("innerhtml is :"+document.getElementById("systemAdmin").innerHTML);
	  	 xmlHttp.onreadystatechange = empty;
		  
	   }
	   
	   else
	 	 {
	 	 	//alert("Request failed.");
	 	 }
	}
function addOneExchange(idSelect, idSelected, idCheckBox)
{
	idForCheckBox = idCheckBox;
    idForSelect = idSelect;
    idForSelected = idSelected;
   //alert("ids are : "+idSelect +";"+idCheckBox);
    var index1 = document.getElementsByName(idSelect)[0].selectedIndex;
//    alert("index1 is :"+index1);
    var index2 = document.getElementsByName(idSelected)[0].selectedIndex;

    var matched = false;
    var val = document.getElementsByName(idSelect)[0].options[index1].value;
//    alert("val is : "+val);
    var inner = document.getElementsByName(idSelect)[0].options[index1].text;
//    alert("inner " + inner);
    var matchedIndex = -1;
    for(var i=0;i<document.getElementsByName(idSelected)[0].options.length;i++)
    {
        if(document.getElementsByName(idSelected)[0].options[i].value == val)
        {
            matched = true;
            matchedIndex = i;
            break;
        }
    }
    if(matched == false)
    {
        var option = document.createElement("option");
        option.value = val;
        option.text = inner;
        try
        {
            // for IE earlier than version 8
            document.getElementsByName(idSelected)[0].add(option,document.getElementsByName(idSelected)[0].options[null]);
        }
        catch (e)
        {
            document.getElementsByName(idSelected)[0].add(option,null);
        }
        var lastIndex = document.getElementsByName(idSelected)[0].options.length -1;
        document.getElementsByName(idSelected)[0].options[lastIndex].value = val;
        index2 = lastIndex;
        index1 = 0;
        //addFilter();
    }
    else
    {
        document.getElementsByName(idSelected)[0].selectedIndex = matchedIndex;
    }     
}
function removeOneExchange(idSelect, idSelected, idCheckbox)
{
	idForCheckBox = idCheckbox;
	idForSelect = idSelect;
	idForSelected = idSelected;
	var index2 = document.getElementsByName(idSelected)[0].selectedIndex;
	document.getElementsByName(idSelected)[0].options.remove(index2);
}
function addAllExchange(idSelect, idSelected, idCheckbox)
{
	idForCheckBox = idCheckbox;
	idForSelect = idSelect;
	idForSelected = idSelected;
	var matched;
	for(var i=0;i<document.getElementsByName(idSelect)[0].options.length;i++)
	    {
			matched = false;
	      	var val = document.getElementsByName(idSelect)[0].options[i].value;
	      	var inner = document.getElementsByName(idSelect)[0].options[i].text;
	      	for(var j = 0;j<document.getElementsByName(idSelected)[0].options.length;j++)
	      	{
	      		if(document.getElementsByName(idSelected)[0].options[j].value == val)
	      		{
	      			matched = true;
	      		}
	      	}
	      	if(matched == false)
	      	{
	      		var option = document.createElement("option");
	      		option.value = val;
	      		option.text = inner;
	      		 try
		    	 {
		    	  // for IE earlier than version 8
		    	  	document.getElementsByName(idSelected)[0].add(option,document.getElementsByName(idSelected)[0].options[null]);
		    	 }
		    	 catch (e)
		    	 {
		    		document.getElementsByName(idSelected)[0].add(option,null);
		    	 }
	      		document.getElementsByName(idSelected)[0].options[j].value = val;
	      		document.getElementsByName(idSelect)[0].selectedIndex = 0;
	      	}
	    }
}
function removeAllExchange(idSelect, idSelected, idCheckbox)
{
	idForCheckBox = idCheckbox;
	idForSelect = idSelect;
	idForSelected = idSelected;
	var len = document.getElementsByName(idSelected)[0].options.length - 1;
	 for(var i = len; i>=0;i--)
	 {
		 document.getElementsByName(idSelected)[0].options.remove(i);
	 	//removeFilter();
	 }
}
function setIdForSelected(idSelected, idCheckBox)
{
	idForSelected = idSelected;	
	idForCheckBox = idCheckBox;
}
function changeHandle()
{
    var v = document.getElementsByName("regionNameSelect")[0].options[document.getElementsByName("regionNameSelect")[0].selectedIndex].value;
    var w = document.getElementsByName("clientPhoneNoSelect")[0].value;
   // alert("v and w is "+v+"  "+w);
    changeExchange(v,w);
}


function changeExchange(v,w)
{
	var xmlHttp = null;
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
    var url="../GetExchanges.do?areaCode="+v+"&adslPhone="+w;
    //  alert(url);
    xmlHttp.onreadystatechange = function(){ExchangeChanged(xmlHttp);};
    xmlHttp.open("GET",url,false);    	  	
    xmlHttp.send(null);    	  	
    return true;
}

function ExchangeChanged(xmlHttp) 
{ 

    if (xmlHttp.readyState==4)
    { 
       // alert(xmlHttp.responseText);
        //alert(xmlHttp.responseText.substr(0,6));

	//alert(xmlHttp.responsetext);
	 //alert( document.getElementById("exchange").innerHTML);
        //if(xmlHttp.responseText.substr(0,31) == "<select name = \'exchangeNoSelect\'")
            document.getElementById("exchange").innerHTML=xmlHttp.responseText;
    }

    else
    {
        //alert("Request failed.");
    }
}
function updateUser()
{
//    alert("updateUser");
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
           // alert(xmlDoc);
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

<title>Report Wizard</title>
<%


%>
</head>

<body  class="body_center_align" onLoad="init();">

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
                    				<td width="100%" align="right" style="border-left-width: 1; border-right-width: 1; border-top-width: 1; border-bottom: 1px solid #C0C0C0">
                      					<div class="div_title">Create Report</div>
            						</td>
            					</tr>
								<tr>
									<td width="100%" align="center"><br/>
										<html:form action ="/SingleReport" method = "POST" onsubmit = "return validate();">
											<table width="500" border="0" cellpadding="0" cellspacing="0" class="form1">
								
												<tr>
													<td width="250" height="22" align="left" height="25">Title of Report</td>
                									<td width="250" height="22" align="left" height="25"><input type = "text" name ="reportTitle" id = "reportTitle"/></td>
            									</tr>
            									<tr>
													<td width="100" height="22" align="left" height="25">Select Report Type</td>
            									 	<td width="400" height="22" align="left" height="25"> 
            									 		<html:select property="infoSectionSelected" size="1" onchange = "refreshFields()" >
            									 			<html:option value="Select" >Select</html:option>
                        								<%
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_NEW_REPORT) !=-1)
            									 		{
                        									//<!--  html:option value="New Report" >New Report</html:option-->
                        								}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_TYPE_OF_CONNECTIVITY_SERVICES) !=-1)
            									 		{%>
            									 			<html:option value="Type of Connectivity Services" >Type of Connectivity Services</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_BANDWIDTH_CONSUMPTION_AND_DATA_TRANSFER) !=-1)
            									 		{%>
                        									<html:option value="Bandwidth Consumption and Data Transfer" >Bandwidth Consumption and Data Transfer</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_BILLING_PACKAGES) !=-1)
            									 		{%>
                        									<html:option value="Billing Packages" >Billing Packages</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_REGION_WISE) !=-1)
            									 		{%>
                        									<html:option value="Region Wise" >Region Wise</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_CUSTOMER_DETAILS) !=-1)
            									 		{%>
                        									<html:option value="Customer Details" >Customer Details</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_SYSTEM_ADMIN) !=-1)
            									 		{%>
                        									<html:option value="System Admin" >System Admin</html:option>
                        								<%}
            									 		
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_TARIFF_DETAILS) !=-1)
            									 		{%>
                        									<html:option value="Tariff Details" >Tariff Details</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_TRAFFIC_DETAILS) !=-1)
            									 		{%>
                        									<html:option value="Traffic Details" >Traffic Details</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_USAGE_DETAIL) !=-1)
            									 		{%>
                        									<html:option value="Usage Details" >Usage Details</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_PACKAGE_WISE_CUSTOMER_REPORT) !=-1)
            									 		{%>
                        									<html:option value="Package Wise Customer Details" >Package Wise Customer Details</html:option>
                        								<%} 
            									 		
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_CLIENT_STATUS) !=-1)
            									 		{%>
                        									<html:option value="Client Status" >Client Status</html:option>
                        								<%}
            									 		if( loginDTO.getMenuPermission(login.PermissionConstants.REPORT_PASSWORD_CHANGE) !=-1)
            									 		{%>
                        									<html:option value="Password Change" >Password Change</html:option>
                        								<%}
            									 		
                        								%>
                        								</html:select> 
			                        				</td>

			                        			</tr>
			                        			<tr>
			                        				<td width="100" height="22" align="left">Select Fields</td>
	                        						<td width ="400" align="left">
	                        							<table width="400" >
	                        								<tr>
			                        							<td width="160" height="22" align="center"> 
			            									 		<html:select property = "columns" size="10" style="width: 100%" onfocus = "changeButtonName('columns')" multiple = "true">
													
			                        								</html:select> 
			                        							</td>
			                        							<td width="40" height="22" align="center">
			                        								<table>
			                        									<tr>
			                        										<td align="center"><input type="button" name="addColumnOne" value = ">" onclick = "addOneColumn()"/></td>
			                        									</tr>
			                        									<tr>
			                        										<td align="center"><input type="button" name="addColumnAll" value = ">>" onclick = "addAllColumn()"/></td>
			                        									</tr>
			                        								</table>
			                        							</td>
			                        							<td width="160" height="22" align="center"> 
			            									 		<html:select property = "columnsSelected" size="10" style="width: 100%" onfocus = "changeButtonName('columnsSelected')" multiple = "true" onchange="addFilter()">
			                        								</html:select> 
			                        							</td>
			                        							<td width="40" height= "22" align = "center">
			                        								<table>
			                        									<tr>
			                        										<td align="center"><input type="button" name="up" value = "Up" onclick = "upOptionValue()"/></td>
			                        									</tr>
			                        									<tr>
			                        										<td align="center"><input type="button" name="down" value = "Down" onclick = "downOptionValue()"/></td>
			                        									</tr>
			                        								</table>
			                        							</td>
			                        						</tr>
			                        					</table>
	                        						</td>
                    							</tr>
                    							</table>
<table id = "passwordChange" style="display:none" width="400" align="center">
											<tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    								<td align="left"><input type="checkbox" name="clientPhoneNoForPassChange" />
                    								</td>
                    								<td align = "left" height="25">
                    								Client ADSL Phone No 
                    								</td>
                    								<td align = "left" height="25">
                    								<input type = "text" name="clientPhoneNoForPassChangeSelect" value = ""/>
                    								</td>
                    								<td>
                    								&nbsp;
                    								</td>
                    							</tr>
                    							<tr>
                    								<td align="left"><input type="checkbox" name="clientUserIDPassChange" />
                    								</td>
                    								<td align = "left" height="25">
                    								Client Username 
                    								</td>
                    								<td align = "left" height="25">
                    								<input type = "text" name="clientUserIDPassChangeSelect" value = ""/>
                    								</td>
                    								<td>
                    								&nbsp;
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "changedDateFrom" onclick = "checkPair(changedDateFrom,changedDateTo)"/>
                    							</td>
                    							<td align="left" height="25">
                    							From 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="changedDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'changedDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "changedDateTo" onclick = "checkPair(changedDateTo,changedDateFrom)"/>
                    							</td>
                    							<td align="left" height="25">
                    							To 
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="changedDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'changedDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						
                    							</table>
  <table id = "clientStatus" style="display: none" width="400" align="center">
  <tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    								<td align="left" height="25" width="50">
                    								<input type="checkbox" name="clientID"/>
                    								</td>
                    								<td align = "left" height="25" width="175">
                    								Client ADSL Phone No 
                    								</td>
                    								<td align = "left" height="25" width="175">
                    								<input type = "text" name="clientIDSelect" value = ""/>
                    								</td>
                    								<td>
                    								&nbsp;
                    								</td>
                    							</tr>
                    							<tr>
                    								<td align="left" height="25" width="50">
                    								<input type="checkbox" name="clientUserID"/>
                    								</td>
                    								<td align = "left" height="25" width="175">
                    								Client Username
                    								</td>
                    								<td align = "left" height="25" width="175">
                    								<input type = "text" name="clientUserIDSelect" value = ""/>
                    								</td>
                    								<td>
                    								&nbsp;
                    								</td>
                    							</tr>
                    							
                    							<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "clientStatusFrom" onclick = "checkPair(clientStatusFrom,clientStatusTo)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Start From 
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="clientStatusFromSelect" value="<%=format.format(new Date()) %>"/>
                    							<script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'clientStatusFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "clientStatusTo" onclick = "checkPair(clientStatusTo,clientStatusFrom)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							To 
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="clientStatusToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'clientStatusToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    							</table>
                    							<div id = "clientStatusDiv" align="center">
                    							</div>
 <table id = "loginStatus" style="display: none" width="400" align="center">

                    							</table>
  <table id = "trafficDetails" style="display: none" width="400" align="center">
  <tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
  												
                    							<tr>
                    								<td align="left" height="25" width="50">
	                    							<input type= "checkbox" name = "trafficDetailsFrom" onclick = "checkPair(trafficDetailsFrom,trafficDetailsTo)"/>
	                    							</td>
	                    							<td align="left" height="25" width="175">
	                    							From
	                    							</td>
	                    							<td align="left" height="25" width="175">
	                    							<input type = "text" name="trafficDetailsFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
	                    								'formname' : 'singleReportForm',
	                    								'controlname' : 'trafficDetailsFromSelect'
	                    							});</script>
	                    							</td>
	                    						</tr>
	                    						<tr>
	                    							<td align="left" height="25" width="50">
	                    							<input type= "checkbox" name = "trafficDetailsTo" onclick = "checkPair(trafficDetailsTo,trafficDetailsFrom)"/>
	                    							</td>
	                    							<td align="left" height="25" width="175">
	                    							To
	                    							</td>
	                    							<td align="left" height="25" width="175">
	                    							<input type = "text" name="trafficDetailsToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
	                    								'formname' : 'singleReportForm',
	                    								'controlname' : 'trafficDetailsToSelect'
	                    							});</script>
	                    							</td>
	                    						</tr>
                    							</table>
                    							<div id = "trafficDetailsDiv" align="center">
                    							</div>
   <table id = "typeOfConnectivity" style="display:none" width="400" align="center">
   <tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "typesOfConnection"/>
                    							</td>
                    								<td align = "left" width = "100" height="25">
                    								Type Of Connection
                    								</td>
                    								<td align = "left" height="25">
                    								<html:select property = "typesOfConnectionSelect" size="1">
                    								<%for(int i = 0;i<ClientConstants.TYPEOFCONNECTION_VALUE.length;i++)
                    									{%>
                    									<html:option value="<%=Long.toString(ClientConstants.TYPEOFCONNECTION_VALUE[i]) %>" ><%=ClientConstants.TYPEOFCONNECTION_NAME[i] %></html:option>
			                        				<%} %>
			                        				</html:select> 
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "typesOfConnectionFrom" onclick = "checkPair(typesOfConnectionFrom,typesOfConnectionTo)"/>
                    							</td>
                    							<td align="left" height="25">
                    							From
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="typesOfConnectionFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'typesOfConnectionFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "typesOfConnectionTo" onclick = "checkPair(typesOfConnectionTo,typesOfConnectionFrom)"/>
                    							</td>
                    							<td align="left" height="25">
                    							To
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="typesOfConnectionToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'typesOfConnectionToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						</table>
   <table id = "packageWiseCustomerDetails" style="display:none" width="400" align="center">
   <tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							
                    							<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "pwcdFrom" onclick = "checkPair(pwcdFrom,pwcdTo)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							From
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="pwcdFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'pwcdFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "pwcdTo" onclick = "checkPair(pwcdTo,pwcdFrom)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							To
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="pwcdToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'pwcdToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						</table>
                    						<div id="packageWiseCustomerDetailsDiv" align="center">
                    						</div>
 <table id = "usageDetailsReport" style="display:none" width="400" align="center">
 <tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    							<tr>
                    							<td>
                    							</td>
                    								<td align = "left" height="25">
                    								Client ADSL Phone No
                    								</td>
                    								<td align = "left" height="25">
                    								<input type = "text" name="clientIdSelected" value = ""/>
                    								</td>
                    							</tr>
                    							<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "usageDetailsFrom" onclick = "checkPair(usageDetailsFrom,usageDetailsTo)"/>
                    							</td>
                    							<td align="left" height="25">
                    							Start From
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="usageDetailsFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'usageDetailsFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "usageDetailsTo" onclick = "checkPair(usageDetailsTo,usageDetailsFrom)"/>
                    							</td>
                    							<td align="left" height="25">
                    							To
                    							</td>
                    							<td align="left" height="25">
                    							<input type = "text" name="usageDetailsToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'usageDetailsToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						</table>
                    						
 <table width = "400" id = "bwAndDataTransfer" style="display:none" align="center">
 <tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
 											
                    							
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "bwdtFrom" onclick = "checkPair(bwdtFrom,bwdtTo)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Start From
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="bwdtFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'bwdtFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "bwdtTo" onclick = "checkPair(bwdtTo,bwdtFrom)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							To
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="bwdtToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'bwdtToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						
                    						</table>
                    						<div id = "bwAndDataTransferDiv" align="center">
 											</div>
                    						
  <table width = "400" id = "tariffDetails" style="display:none" align="center">
  <tr>
                    							<td align="left" height="25"><h4>Filters</h4></td>
                    							<td></td>
                    							</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "activationDateFrom" onclick = "checkPair(activationDateFrom,activationDateTo)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Activation date From
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="activationDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'activationDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "activationDateTo" onclick = "checkPair(activationDateTo,activationDateFrom)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Activation date To
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="activationDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'activationDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "expirationDateFrom" onclick = "checkPair(expirationDateFrom,expirationDateTo)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Expiration date From
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="expirationDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'expirationDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "expirationDateTo" onclick = "checkPair(expirationDateTo,expirationDateFrom)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Expiration date To
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="expirationDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'expirationDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						
                    						</table>
                    						<div id = "tariffDetailsDiv" align="center">
                    						</div>
                    						<div id="customerDetailsTable" align="center">
                    						
 <table width = "400" id = "customerDetails" style="display:none" align="center">
 <tr>
                    							<td align="left" height="25" width="50"><h4>Filters</h4></td>
                    							<td width="175">&nbsp;</td>
                    							<td width="175">&nbsp;</td>
                    							</tr>
 											<tr>
                    								<td align="left" height="25" width="50"><input type="checkbox" name="clientPhoneNo"/>
                    								</td>
                    								<td align = "left" height="25" width="175">
                    								Client ADSL Phone No 
                    								</td>
                    								<td align = "left" height="25" width="175">
                    								<input type = "text" name="clientPhoneNoSelect" value = ""/>
                    								</td>
                    								<td>
                    								&nbsp;
                    								</td>
                    							</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "addedDateFrom" onclick = "checkPair(addedDateFrom,addedDateTo)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Added date From
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="addedDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'addedDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "addedDateTo" onclick = "checkPair(addedDateTo,addedDateFrom)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Added date To
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="addedDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'addedDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "verifiedDateFrom" onclick = "checkPair(verifiedDateFrom,verifiedDateTo)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Verified date From
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="verifiedDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'verifiedDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "verifiedDateTo" onclick = "checkPair(verifiedDateTo,verifiedDateFrom)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Verified date To
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="verifiedDateToSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'verifiedDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "approvedDateFrom" onclick = "checkPair(approvedDateFrom,approvedDateTo)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Approved date From
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="approvedDateFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'approvedDateFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "approvedDateTo" onclick = "checkPair(approvedDateTo,approvedDateFrom)"/>
                    							</td>
                    							<td align="left" height="25" width="175">
                    							Approved date To
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="approvedDateToSelect" value = "<%=format.format(new Date(System.currentTimeMillis())) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'approvedDateToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						
                    							
                    							<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "bonusGrantedFrom" onclick = "checkPair(bonusGrantedFrom,bonusGrantedTo)"/>
                    							</td>
                    							<td align = "left" height="25" width="175">
                    							Bonus Granted From 
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="bonusGrantedFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'bonusGrantedFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "bonusGrantedTo" onclick = "checkPair(bonusGrantedTo,bonusGrantedFrom)"/>
                    							</td>
                    							<td align = "left" height="25" width="175">
                    							Bonus Granted To 
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="bonusGrantedToSelect" value = "<%=format.format(new Date(System.currentTimeMillis())) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'bonusGrantedToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "discountOfferedFrom" onclick = "checkPair(discountOfferedFrom,discountOfferedTo)"/>
                    							</td>
                    							<td align = "left" height="25" width="175">
                    							Discount Offered From 
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="discountOfferedFromSelect" value="<%=format.format(new Date()) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'discountOfferedFromSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25" width="50">
                    							<input type= "checkbox" name = "discountOfferedTo" onclick = "checkPair(discountOfferedTo,discountOfferedFrom)"/>
                    							</td>
                    							<td align = "left" height="25" width="175">
                    							Discount Offered To 
                    							</td>
                    							<td align="left" height="25" width="175">
                    							<input type = "text" name="discountOfferedToSelect" value = "<%=format.format(new Date(System.currentTimeMillis())) %>"/><script type="text/javascript">new tcal({
                    								'formname' : 'singleReportForm',
                    								'controlname' : 'discountOfferedToSelect'
                    							});</script>
                    							</td>
                    						</tr>
                    						<tr>
                    							<td align="left" height="25">
                    							<input type= "checkbox" name = "connection" />
                    							</td>
                    							<td align = "left" height="25" width="175">
                    							Connection Status 
                    							</td>
                    								<td align = "left" height="25">
                    								<html:select property="connectionSelect" size="1">
                        									<html:option value="All" >All</html:option>
                        									<html:option value="Not Connected" >Not Connected</html:option>
                        									<html:option value="Connected" >Connected</html:option>
                        									
                        								</html:select>
                    								</td>
                    							</tr>
                    						
                    							
                    						</table>
                    						</div>
                    						
                    						<div id = "customerDetailsDiv" align="center">
                    						</div>
                    						
          <div id = "systemAdmin" align="center">

                    						</div>
 
 <div id = "regionWise" align="center">
 </div>
 
   <div id = "billingPackages" align="center">
   </div>
 
                    							<table width = "500" id = "filter" align="center">

                    						</table>
                    						<table width = "500">
                    							<tr>
                									<td align = "center" height="25"><input type="submit" name="submit" value = "Generate Report >" onclick = "generateReport()"/></td>
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
</body>
</html>