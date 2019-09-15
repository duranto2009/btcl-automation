function init()
{ 
if( document.forms[0].orgAccountName!=null)
{
	document.forms[0].orgAccountName.focus();
  document.forms[0].orgAccountName.setAttribute("autocomplete","off");
}
}

function LTrim(value)
{
	var re = /\s*((\S+\s*)*)/;
  	return value.replace(re, "$1");
}

// Removes ending whitespaces
function RTrim(value)
{
	var re = /((\s*\S+)*)\s*/;
  	return value.replace(re, "$1");
}

// Removes leading and ending whitespaces
function trim(value)
{
	return LTrim(RTrim(value));
}

var xmlhttp = new getXMLObject();	//xmlhttp holds the ajax object
function isPositiveInteger( val)
{
	if( isEmpty(val))
		return false;

	for( i = 0; i < val.length ; i++)
  	{
    	if(val.charAt(i)<'0' || val.charAt(i)>'9')
    	{
      		return false;
    	}
  	}
  	return true;
}

function isSignedInteger(str)
{
	if(str.length == 0)
  		return false;

	var c = 0;
  	digit = str.substring(c, c+1);

	if( digit == "-")
  	c = c + 1;

	for(; c < str.length; c++)
  	{
    	digit = str.substring(c, c+1);
    	if(digit !="." && (digit < "0" || digit > "9"))
			return false;
  	}
  	return true;
}

function isInteger(value)
{
  	var strChar;
  	var result = true;
	var strValidChars = "0123456789";

	for (i = 0; i < value.length && result == true; i++)
  	{
    	strChar = value.charAt(i);
    	if(strValidChars.indexOf(strChar) == -1)
    	{
      		return false;
    	}
  	}
	return true;
}

function validate()
{
  	var form = document.forms[0];

  	if(form.orgAccountID.value == "-1")
  	{
    	alert("please select an originator");
    	form.orgAccountName.focus();
    	return false;
  	}
  	
  	if(form.rechargeAmount.value.length <= 0)
  	{
    	alert("please type recharge amount");
    	form.rechargeAmount.focus();
    	return false;
  	}
  	else if(form.baseRechargeAmount.value.length == 0)
  	{
    	alert("invalid recharge number");
    	return false;
  	}
  	else if(form.mobileNumber.value.length<=0 || isInteger(trim(form.mobileNumber.value))==false)
  	{
  		alert("invalid mobile number");
  		form.mobileNumber.focus();
    	return false;
  	}
	
	var confirmation = "Total " + form.baseRechargeAmount.value + " " + form.baseCurrencyCode.value 
	                 + " will be duducted from your account for this recharge. Do you agree?";
	var result = confirm(confirmation);
	if (result == true)
	{
		return true;
	}
	else
  		return false;		
}



function getCountryList()
{
	     //var xmlhttp=GetXmlHttpObject();
	    if (xmlhttp==null)
	    {
	       alert ("Your browser does not support AJAX!");
	       return false;
	    }
	    
	    var url="../recharge/addRecharge.jsp";
	    url=url+"?selectedAccount="+document.forms[0].orgAccountID.value;

		if(xmlhttp) 
		{
	    	xmlhttp.open("GET", url, true); //gettime will be the servlet name
	    	xmlhttp.onreadystatechange  = accountChanged;
	    	xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    	xmlhttp.send(null);
	  	}
	 	return true;
}

function accountChanged()
{
	if(xmlhttp.readyState==4){
		if(xmlhttp.status == 200) 
		{
			document.getElementById('countryDiv').innerHTML=xmlhttp.responseText;
			
			if(document.forms[0].countryID.value!=null)
				getOperator(document.forms[0].countryID.value);
		}
	}
}



function setOperator()
{
	if(xmlhttp.readyState==4)
	{
		document.getElementById('operatorDiv').innerHTML=xmlhttp.responseText;
		
		getServiceType();
	}
}

function getOperator(text)
{
	if(text==null || text=="")
		return false;
	
	 if (xmlhttp==null)
	    {
	       alert ("Your browser does not support AJAX!");
	       return false;
	    }
	    
	    var url="../recharge/addRecharge.jsp?countryID="+text+"&selectedAccount="+document.forms[0].orgAccountID.value;

		if(xmlhttp) 
		{
	    	xmlhttp.open("GET", url, true); //gettime will be the servlet name
	    	xmlhttp.onreadystatechange  = setOperator;
	    	xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    	xmlhttp.send(null);
	  	}
	 	return true;
}

function setServiceType()
{
	if(xmlhttp.readyState==4)
	{
		document.getElementById('serviceTypeRow').innerHTML=xmlhttp.responseText;
		
		if(document.rechargeForm.rechargeAmount.value != "")
			calculateCharge();
	}
}

function getServiceType()
{
	 if (xmlhttp==null)
	    {
	       alert ("Your browser does not support AJAX!");
	       return false;
	    }
	    
	    var url="../recharge/addRecharge.jsp?operatorID="+document.forms[0].operatorID.value+"&selectedAccount="+document.forms[0].orgAccountID.value;

		if(xmlhttp) 
		{
	    	xmlhttp.open("GET", url, true); //gettime will be the servlet name
	    	xmlhttp.onreadystatechange  = setServiceType;
	    	xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    	xmlhttp.send(null);
	  	}
	 	return true;
}

function calculateCharge()
{
	var form = document.forms[0];
  	var accountID = form.orgAccountID.value;
  	var currencyCode = form.currencyCode.value;
  	var rechargeAmount = form.rechargeAmount.value;

	if(rechargeAmount.value == "0")
  	{
    	form.baseRechargeAmount.value = "";
    	return false;
  	}
	if(rechargeAmount.length == 0)
  	{
    	form.baseRechargeAmount.value = "";
    	return false;
  	}
	/*if(form.mobileNumber.value.length == 0)
  	{
    	alert("Please give mobile number");
    	form.baseRechargeAmount.value = "";
    	return false;
  	}*/
	if(rechargeAmount.length == 7)
  	{
    	alert("recharge amount can't greater then 10000");
    	form.rechargeAmount.value = "";
    	form.baseRechargeAmount.value = "";
    	return false;
  	}
	if(!isInteger(rechargeAmount))
  	{
    	alert("please put a valid amount as recharge amount");
    	form.rechargeAmount.value = "";
    	form.baseRechargeAmount.value = "";
    	return false;
	}	
	
	if (xmlhttp==null)
  	{
    	alert ("Your browser does not support AJAX!");
    	return false;
  	}

	var rechargeType=-2;
	
	try
	{
		rechargeType=form.rechargeType.value;
	}
	catch(e)
	{
		
	}
	var url="../recharge/addRecharge.jsp?selectedAccount="+accountID+"&operatorID="+form.operatorID.value+"&rechargeAmount="+rechargeAmount;

	xmlhttp.open("GET", url, true); //gettime will be the servlet name
	xmlhttp.onreadystatechange  = handleServerResponse;
	xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xmlhttp.send(null);
 	return true;
}

function getXMLObject()  //XML OBJECT
{
	
  var xmlHttp = false;
  try {
    xmlHttp = new ActiveXObject("Msxml2.XMLHTTP")  // For Old Microsoft Browsers
  }
  catch (e) {
    try {
      xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")  // For Microsoft IE 6.0+
    }
    catch (ex) {
      xmlHttp = false;   // No Browser accepts the XMLHTTP Object then false
    }
  }
  if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
    xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
  }
  return xmlHttp;  // Mandatory Statement returning the ajax object created
}

function handleServerResponse() 
{
	if (xmlhttp.readyState == 4) 
  	{
		if(xmlhttp.status == 200) 
		{
      		var result = trim(xmlhttp.responseText);
			if(result == -3 || result == -5)
      		{
        		alert("rate not found for this destination");
        		document.rechargeForm.rechargeAmount.value = "";
        		document.rechargeForm.baseRechargeAmount.value = "";
      		}
      		else if(result == -2)
      		{
        		alert("Insufficient balance in your account.");
        		document.rechargeForm.rechargeAmount.value = "";
       			document.rechargeForm.baseRechargeAmount.value = "";
      		}
      		else if(result == -4)
      		{
        		alert("Insufficient balance in reseller account. Please contact with your reseller.");
        		document.rechargeForm.rechargeAmount.value = "";
       			document.rechargeForm.baseRechargeAmount.value = "";
      		}
      		else
      		{
        		document.rechargeForm.baseRechargeAmount.value = result;
      		}
    	}
  	}
}

function showOrgClients(text) {
	if(text==""){
		document.getElementById("response_orgClient").innerHTML="";
		document.getElementById("response_orgClient").style.visibility = "hidden";
				
		return false;
	}
	else{
		document.getElementById("response_orgClient").style.visibility = "visible";		
	}
	
	  xmlHttp=getXMLObject();
	  if (xmlHttp==null)
	  {
	     alert ("Your browser does not support AJAX!");
	     return false;
	  }      
		var url = "../recharge/addRecharge.jsp";
		url=url+"?orgAccountID="+text;
		xmlHttp.onreadystatechange = orgClientChanged;
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);
		return true;
	}

function orgClientChanged() 
{ 
   if (xmlHttp.readyState==4)
   {
       document.getElementById("response_orgClient").innerHTML=xmlHttp.responseText;
       
       var orgClientResponse = document.getElementById('response_orgClient');
       
       if(document.getElementById("response_orgClient").innerHTML == "")
       {
    	   orgClientResponse.style.display="none";     		
       }
       else
       {
    	   orgClientResponse.style.display="block";
    	   orgClientResponse.style.width=document.forms[0].orgAccountName.style.width;
    	   orgClientResponse.style.left=findPosX(document.getElementById("orgAccountName"));
    	   orgClientResponse.style.top=findPosY(document.getElementById("orgAccountName"))+document.getElementById("orgAccountName").offsetHeight; 

       }
     
    }
}

function setOrgClient(val, name)
{
	document.forms[0].orgAccountID.value = val;
	document.forms[0].orgAccountName.value = name;

	document.getElementById("response_orgClient").style.display="none";
	
	
	getCountryList();
	//calculateCharge();

}

function ShowPopUp(rechargeID,mobile)
{
	var topUpWin=window.open('TopUpStatus.jsp?id='+rechargeID,'TopUpStatus'+rechargeID,'width=600,height=275,resizable=0,scrollbars=0,toolbar=0,status=0,menubar=0');
}

