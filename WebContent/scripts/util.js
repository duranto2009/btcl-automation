 function validateRequired(text)
{
	if(eval(text.length) == 0)
    {
		return false;
    }
    return true;
}

function validateMaxLength(text,len)
{
	if(eval(text.length) >  eval(len))
    {
		return false;
	}
	return true;
}

function validateMinLength(text,len)
{
	if(eval(text.length) <  eval(len))
    {
		return false;
	}
	return true;
}

function validateEmail(text)
{
	var email = text;
    var splitted = email.match("^(.+)@(.+)$");
    if(splitted == null) return false;
    if(splitted[1] != null )
    {
      var regexp_user=/^\"?[\w-_\.]*\"?$/;
      if(splitted[1].match(regexp_user) == null) return false;
    }
    if(splitted[2] != null)
    {
      var regexp_domain=/^[\w-\.]*\.[A-Za-z]{2,4}$/;
      if(splitted[2].match(regexp_domain) == null)
      {
	    var regexp_ip =/^\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\]$/;
	    if(splitted[2].match(regexp_ip) == null) return false;
      }// if
      return true;
    }
	return false;
}


function validateGT(text,value)
{
	if(isNaN(text))
    {
         return false;
    }

    if(eval(text) <=  eval(value))
    {
		return false;
	}
	return true;
}

function validateLT(text,value)
{
	if(isNaN(text))
    {
         return false;
    }

    if(eval(text) >=  eval(value))
    {
		return false;
	}
	return true;
}

function validateInteger(text)
{
	var charpos = text.search("[^0-9]");
	if( eval(text.length) > 0 &&  charpos >= 0 )
	{
		return false;
	}

	return true;
}

function validateDecimal(text)
{
	
	if(!text.match("^([0-9]+)(\.[0-9]*)?$"))
	{
		alert(text);
		return false;
	}
	return true;
}

function validateAlpha(text)
{
	var charpos = text.search("[^A-Za-z]");
	if( eval(text.length) > 0 &&  charpos >= 0 )
	{
		return false;
	}
	return true;
}

function validateAlphaNumeric(text)
{
	var charpos = text.search("[^A-Za-z0-9]");
	if( eval(text.length) > 0 &&  charpos >= 0 )
	{
		return false;
	}
	return true;
}


/*
case "alnumhyphen":
 search("[^A-Za-z0-9\-_]")
*/

   function isEmpty(s)
   {
     for(var i=0;i<s.length;i++)
         if(s.charAt(i)!=' ')break;

     if(i==s.length)
         return true;
     else
         return false;
   }

   function isNum(s)
   {
     for(var i=0;i<s.length;i++)
     {
         if(s.charAt(i)=='1'||s.charAt(i)=='2'||
		s.charAt(i)=='3'||s.charAt(i)=='4'||
		s.charAt(i)=='5'||s.charAt(i)=='6'||
		s.charAt(i)=='7'||s.charAt(i)=='8'||
		s.charAt(i)=='9'||s.charAt(i)=='0'||
		s.charAt(i)=='.')
           continue;
         else
           break;
     }
     if(i==s.length)
         return true;
     else
         return false;
   }

  function checkFromToDateForValidation2(stDate, enDate)
  {
	//var stDate = document.getElementById(startDate).value;
	//var enDate = document.getElementById(endDate).value;
	//alert(stDate);
	//alert(enDate);

	if (stDate.length == 0)
	{
		alert("Please enter start date.");
		return false;
	}
	else if (stDate.length != 10)
	{
		alert("Error in start date.");
		 return false;
	}
	if (enDate.length == 0)
	{
		alert("Please enter end date.");
		return false;
	}
	else if (enDate.length != 10)
	{
		alert("Error in end date.");
		 return false;
	}
	//alert("dd"+(stDate.substring(0,2)));
	var dd   = parseInt(stDate.substring(0,2), 10);
	var mm   = parseInt(stDate.substring(3,5), 10)-1;//alert("start month"+mm);
	var yy   = parseInt(stDate.substring(6,10));
	var ddTo   = parseInt(enDate.substring(0,2), 10);
	var mmTo   = parseInt(enDate.substring(3,5), 10)-1;//alert("end month"+mmTo);
	var yyTo   = parseInt(enDate.substring(6,10));

	var flag = false;
    if(!isYearMonthDateValid2(yy, mm, dd))
	{
       alert("Invalid Start Date");
	   return false;
    }
	else if(!isYearMonthDateValid2(yyTo, mmTo,ddTo))
	{
       alert("Invalid End Date");
       return false;
	}
    if( yy < yyTo )
      return true;
    else if( yy == yyTo )
    {
	 // alert("year equel");

      if( mm < mmTo )
        return true;
      else if( mm == mmTo)
      {
	    // alert("month equel");

        if( dd < ddTo )
          return true;
        else if( dd == ddTo )
        {
	     //alert("day equal");
		return true;
        }
      }
    }
	alert("Start date is smaller than end date.");
    return false;
  }
  
  
  function checkFromToDateForValidation2_alternative(stDate, enDate)
  {
	//var stDate = document.getElementById(startDate).value;
	//var enDate = document.getElementById(endDate).value;
	//alert(stDate);
	//alert(enDate);

	if (stDate.length == 0)
	{
		alert("Please enter start date.");
		return false;
	}
	else if (stDate.length != 10)
	{
		alert("Error in start date.");
		 return false;
	}
	if (enDate.length == 0)
	{
		alert("Please enter end date.");
		return false;
	}
	else if (enDate.length != 10)
	{
		alert("Error in end date.");
		 return false;
	}
	//alert("dd"+(stDate.substring(0,2)));
	var dd   = parseInt(stDate.substring(0,2), 10);
	var mm   = parseInt(stDate.substring(3,5), 10)-1;//alert("start month"+mm);
	var yy   = parseInt(stDate.substring(6,10));
	var ddTo   = parseInt(enDate.substring(0,2), 10);
	var mmTo   = parseInt(enDate.substring(3,5), 10)-1;//alert("end month"+mmTo);
	var yyTo   = parseInt(enDate.substring(6,10));

	var flag = false;
    if(!isYearMonthDateValid2(yy, mm, dd))
	{
       alert("Invalid Start Date");
	   return false;
    }
	else if(!isYearMonthDateValid2(yyTo, mmTo,ddTo))
	{
       alert("Invalid End Date");
       return false;
	}
    
    return true;
  }

  function isYearMonthDateValid2(year,month,date)
  {//alert("date"+date);
    var endOfMonth = -1;
   // alert("M="+month);

    switch(month)
    {
      case 0://January
      case 2://March
      case 4://May
      case 6://July
      case 7://August
      case 9://October
      case 11://December
        endOfMonth = 31;
        break;

      case 1://February
        endOfMonth = isLeapYear(year) ? 29 : 28;
        break;

      case 3://April
      case 5://June
      case 8://September
      case 10://November
        endOfMonth = 30;
        break;
    }
    //alert("day ="+date+( date < 1 )+( date > endOfMonth)+endOfMonth );

    return !( date < 1 || date > endOfMonth);
  }

  function checkDate(yyFrom,mmFrom,ddFrom,yyTo,mmTo,ddTo)
  {
    if( parseInt(yyFrom, 10) < parseInt(yyTo, 10))
      return true;
    else if(parseInt(yyFrom) == parseInt(yyTo))
    {
      if(parseInt(mmFrom, 10) < parseInt(mmTo, 10))
        return true;
      else if(parseInt(mmFrom, 10) == parseInt(mmTo, 10))
      {
        if(parseInt(ddFrom, 10) < parseInt(ddTo, 10))
          return true;
        else if(parseInt(ddFrom, 10) == parseInt(ddTo, 10))
        {
          return false;
        }
      }
    }
    return false;
  }

  function checkDate2(stDate)
  {
	//var stDate = document.getElementById(startDate).value;
	if (stDate.length == 0)
	{
		alert("Please enter date.");
		return false;
	}
	else if (stDate.length != 10)
	{
		alert("Error in date.");
		 return false;
	}
	var dd   = parseInt(stDate.substring(0,2), 10);
	var mm   = parseInt(stDate.substring(3,5), 10)-1;//alert("start month"+mm);
	var yy   = parseInt(stDate.substring(6,10));
	var flag = false;
    if(!isYearMonthDateValid2(yy, mm, dd))
	{
       alert("Invalid Date");
	return false;
    }
    return true;
  }

  function isLeapYear(year)
  {
    return (year % 4 == 0 && year % 100 != 0 ) || year % 400 ==0;
  }
  
  function getElementsByClassName(node,classname) {
	  if (node.getElementsByClassName) { // use native implementation if available
	    return node.getElementsByClassName(classname);
	  } else {
	    return (function getElementsByClass(searchClass,node) {
	        if ( node == null )
	          node = document;
	        var classElements = [],
	            els = node.getElementsByTagName("*"),
	            elsLen = els.length,
	            pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)"), i, j;

	        for (i = 0, j = 0; i < elsLen; i++) {
	          if ( pattern.test(els[i].className) ) {
	              classElements[j] = els[i];
	              j++;
	          }
	        }
	        return classElements;
	    })(classname, node);
	  }
	}

  
  function GetXmlHttpObject() {
		var xmlHttp = null;
		try {
			// Firefox, Opera 8.0+, Safari
			xmlHttp = new XMLHttpRequest();
		} catch (e) {
			// Internet Explorer
			try {
				xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
		}
		return xmlHttp;
	}