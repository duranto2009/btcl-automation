var dropDownSelectedIndex=-1;
var dropDownSize=10;
var dropDownVisibleItems=dropDownSize;

// Shantanu Begin
function stickyFooter(){
	
	var footerHeight = 46;
	var wrapperDiv = "style2Wrapper";

	var windowHeight=0;

	try
	{
		if (typeof window.innerWidth != 'undefined'){
		    windowHeight = window.innerHeight - 18;
		    //alert(1);
		}
		// IE6 in standards compliant mode (i.e. with a valid doctype as the first 
		// line in the document)
		else if (typeof document.documentElement != 'undefined'
		        && typeof document.documentElement.clientWidth != 'undefined' 
		        && document.documentElement.clientWidth != 0)
		{
		    windowHeight = document.documentElement.clientHeight;
		    //alert(2);
		}
		// older versions of IE
		else
		{
		    windowHeight = document.getElementsByTagName('body')[0].clientHeight;
		    //alert(3);
		}

		windowHeight -= footerHeight  ;
	} catch(ee){
		//alert(4);
	}
	//Ximran change begin
	
	//document.getElementById( wrapperDiv ).style.height = windowHeight + "px";
	
	//Ximran change end
	
	//document.getElementById("loginWrapper").style.width = "100%";
}

// Shantanu End


  function getdropDownVisibleItemsNo()
  {
	  return dropDownVisibleItems;
  }

  function setOnMouseHover(field)
  {
	  var items = document.getElementsByName(field.name);
	  
	  if(items==undefined)
			return false;
	  
	  
	  if(dropDownSelectedIndex>=0 && dropDownSelectedIndex<items.length)
		  	items[dropDownSelectedIndex].blur();
	  
	  for(var i=0; i<items.length;++i)
	  {
		  if(items[i]==field)
		  {
			  dropDownSelectedIndex=i;
			  items[dropDownSelectedIndex].focus();
			  break;
		  }
	  }

	  return false;
  }
  function setSelectedIndex(fieldName)	
  {
	  var items = document.getElementsByName(fieldName);
	  
	  if(items==undefined)
			return;
	  

	  if(dropDownSelectedIndex>=0 && dropDownSelectedIndex<items.length)
	  {
	  	items[dropDownSelectedIndex].focus();
		  
	  }
	  
	  dropDownVisibleItems=dropDownVisibleItems+dropDownSize;
  }
  
  function selectOnKeyPress(e,fieldName)
  {
  	if(e!=undefined && e.keyCode)
  	{		
  		if(e.keyCode=="40" || e.keyCode=="38" || e.keyCode=="9")
  		{
  			var items = document.getElementsByName(fieldName);
  			
  			if(items==undefined)
  				return true;
  			
  		  if(dropDownSelectedIndex>=0 && dropDownSelectedIndex<items.length)
  		  	items[dropDownSelectedIndex].blur();
  		  
  			if(items.length==undefined)
  			{
  				items.focus();
  			}
  			else
  			{
  				var length=items.length;
  				if(e.keyCode=="40" || e.keyCode=="9")
  					dropDownSelectedIndex=++dropDownSelectedIndex%length;
  				else
  				{
  					if(dropDownSelectedIndex<=0)
  						dropDownSelectedIndex=length-1;
  					else
  						dropDownSelectedIndex=--dropDownSelectedIndex%length;
  				}

  				items[dropDownSelectedIndex].focus();
  			}
  			return false;
  			
  		}
  		else if(e.keyCode!="13" )
  		{
  			dropDownVisibleItems=dropDownSize;
  			dropDownSelectedIndex=-1;
  		}
  	}

  	return true;
  }
function findPosX(obj)
{
  var curleft = 0;
  if(obj.offsetParent)
      while(1) 
      {
        curleft += obj.offsetLeft;
        if(!obj.offsetParent)
          break;
        obj = obj.offsetParent;
      }
  else if(obj.x)
      curleft += obj.x;
  return curleft;
}

function findPosY(obj)
{
  var curtop = 0;
  if(obj.offsetParent)
      while(1)
      {
        curtop += obj.offsetTop;
        if(!obj.offsetParent)
          break;
        obj = obj.offsetParent;
      }
  else if(obj.y)
      curtop += obj.y;
  return curtop;
}
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
	if(!text.match("^([0-9]*)(.[0-9]+)?$")) 
	{
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

  function checkFromToDateForValidation(yy,mm,dd,hh,min,sec,
	                         yyTo,mmTo,ddTo,hhTo,minTo,secTo)
  {

	//var s = yy + " " + mm + " " + dd + " " + hh + " "+min + " " + sec;  
	//s += "\n";		
	//s += yyTo + " " + mmTo + " " + ddTo + " " + hhTo + " "+minTo + " " + secTo;  
    //alert(s);

	var yy = 23; //;,mm,dd,hh,min,sec,
	//yyTo,mmTo,ddTo,hhTo,minTo,secTo	
	alert(yy);
	
	var flag = false;
    if(!isYearMonthDateValid(yy, mm, dd)
       ||    !isYearMonthDateValid(yyTo, mmTo,ddTo)) {
       //alert("month check ");
       return false;
    }
      
	


    if( yy < yyTo )
      return true;
    else if( yy == yyTo )
    {
	  //alert("year equel");
      
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

          if( hh < hhTo)
            return true;
          else if(hh== hhTo)
          {
            if(min < minTo)
              return true;
            else if(min == minTo)
            {
              if( sec <= secTo)
                return true;
            }
          }
        }
      }
    }
    return false;
  }

  function isYearMonthDateValid(year,month,date)
  {
    var endOfMonth = -1;
    //alert("M="+month);
	
    switch(month)
    {
      case "0"://January
      case "2"://March
      case "4"://May
      case "6"://July
      case "7"://August
      case "9"://October
      case "11"://December
        endOfMonth = 31;
        break;

      case "1"://February
        endOfMonth = isLeapYear(year) ? 29 : 28;
        break;

      case "3"://April
      case "5"://June
      case "8"://September
      case "10"://November
        endOfMonth = 30;
        break;
    }
    //alert("EOM ="+endOfMonth);
    return !( date < 1 || date > endOfMonth);
  }
  
  function isLeapYear(year)
  {
    return (year % 4 == 0 && year % 100 != 0 ) || year % 400 ==0;
  }
  
  function getXMLObject()  //XML OBJECT
  {
  	
  	var xmlHttp=null;
  	   try
  	   {
  	      // Firefox, Opera 8.0+, Safari
  	      xmlHttp=new XMLHttpRequest();
  	   }
  	   catch (e)
  	   {
  	      // Internet Explorer
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
  
  
  $(document).ready(function(){
  	function detectIE() {
          var ua = window.navigator.userAgent;

          var msie = ua.indexOf('MSIE ');
          if (msie > 0) {
              // IE 10 or older => return version number
              return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
          }

          var trident = ua.indexOf('Trident/');
          if (trident > 0) {
              // IE 11 => return version number
              var rv = ua.indexOf('rv:');
              return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
          }

          var edge = ua.indexOf('Edge/');
          if (edge > 0) {
             // Edge (IE 12+) => return version number
             return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
          }

          // other browser
          return false;
      }
      if(!(detectIE()==false)){
    	  
      	$(".table-responsive").css( "padding-bottom" , "20px"  );
      }
  });