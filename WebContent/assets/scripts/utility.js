var finalEnlishToBanglaNumber = {    '0' : '০',   '1' : '১',    '2' : '২',  '3' : '৩',    '4' : '৪',  '5' : '৫',   '6' : '৬',   '7' : '৭',  '8' : '৮',    '9' : '৯' };
var finalBanglaToEnglishNumber = {     '০' : '0',  '১' : '1', '২' : '2', '৩' : '3',    '৪' : '4',    '৫' : '5',  '৬' : '6',  '৭' : '7',  '৮' : '8',   '৯' : '9' };

/*
 * window.alert = function(text) { console.log('tried to alert: ' + text);
 * return true; };
 */
String.prototype.getDigitBanglaFromEnglish = function() {
    var retStr = this;
    for ( var x in finalEnlishToBanglaNumber) {
	retStr = retStr.replace(new RegExp(x, 'g'),
		finalEnlishToBanglaNumber[x]);
    }
    return retStr;
};
String.prototype.toEng = function() {
    var retStr = this;
    for ( var x in finalBanglaToEnglishNumber) {
	retStr = retStr.replace(new RegExp(x, 'g'),
		finalBanglaToEnglishNumber[x]);
    }
    return retStr;
};
String.prototype.toBan = function() {
    var retStr = this;
    for ( var x in finalEnlishToBanglaNumber) {
	retStr = retStr.replace(new RegExp(x, 'g'),
		finalEnlishToBanglaNumber[x]);
    }
    return retStr;
};

String.prototype.toNum = function() {
    return parseInt(this, 10);
}

function getDateTime(dateVal) {
    var now = new Date(dateVal);
    if (typeof dateVal == 'undefined') {
	now = new Date();
    }

    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    var hour = now.getHours();
    var minute = now.getMinutes();
    var second = now.getSeconds();
    if (month.toString().length == 1) {
	var month = '0' + month;
    }
    if (day.toString().length == 1) {
	var day = '0' + day;
    }
    if (hour.toString().length == 1) {
	var hour = '0' + hour;
    }
    if (minute.toString().length == 1) {
	var minute = '0' + minute;
    }
    if (second.toString().length == 1) {
	var second = '0' + second;
    }
    var dateTime = year + '/' + month + '/' + day + ' ' + hour + ':' + minute
	    + ':' + second;
    return dateTime.getDigitBanglaFromEnglish();
}

var entityMap = {
    "&" : "&amp;",
    "<" : "&lt;",
    ">" : "&gt;",
    '"' : '&quot;',
    "'" : '&#39;',
    "/" : '&#x2F;'
};

function escapeHtml(string) {
    return String(string).replace(/[&<>"'\/]/g, function(s) {
	return entityMap[s];
    });
}

function callAjax(url, formData, callback, type) {
    $.ajax({
		type : typeof type != 'undefined' ? type : "POST",
		url : url,
		data : formData,
		dataType : 'JSON',
		success : function(data) {
		    callback(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
		    toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
			    + ", Message: " + errorThrown);
		},
		failure : function(response) {
		    toastr.error(response);
		},
		complete: function (data) {
		   LOG('ajax call is completed');
		   $('.ui-autocomplete-loading').removeClass('ui-autocomplete-loading');
	    }
    });
}
function callAjaxHTML(url, formData, callback) {
    $.ajax({
		type : "POST",
		url : url,
		data : formData,
		dataType : 'HTML',
		success : function(data) {
		    callback(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
		    toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
			    + ", Message: " + errorThrown);
		}
    });
};
function sortSelectSingle(selector){
	var selectedVal=selector.val();
    var my_options = selector.find('option');
    // sort alphabetically
    my_options.sort(function(a,b) {
        if (a.text > b.text) return 1;
        else if (a.text < b.text) return -1;
        else return 0
    })
   //replace with sorted my_options;
   selector.empty().append( my_options );

   // clearing any selections
   selector.val(selectedVal);
}
function sortSelectBox(selectors){
	if (selectors instanceof Array) {
	  for(var i=0;i<selectors.length;i++){
		  sortSelectSingle(selectors[i]);
	  }
	} else {
		sortSelectSingle(selectors);
	}
}
var delay = (function(){
	  var timer = 0;
	  return function(callback, ms){
	    clearTimeout (timer);
	    timer = setTimeout(callback, ms);
	    $('.ui-autocomplete-loading').removeClass('ui-autocomplete-loading');
	  };
	})();

function isEmpty(str){
	if( typeof str =='undefined' || str === null){
		return true;
	}
	return str ? str.trim().length == 0 : true;
}
function isNotEmpty(str){
	return !isEmpty(str)
}
function LOG(msg){
	console.log(msg);
}




function ajax(url, data, callback, type, loaderIdArray, emptyAllowed) {
	
	type == "POST" ? (toastr.options.timeOut = "0") : (toastr.options.timeOut = "1000"); 
    $.ajax({
		type : type,
		url : url,
		data : data,
		dataType : 'JSON',
		beforeSend : function(){
			if(loaderIdArray != undefined){
				if(loaderIdArray.length == 0){
					$("#fakeLoader").addClass("full-page-loading");
					var loadingNotice = "<div style='opacity:1;width:100%;height:100px;text-align:center;padding-top:40vh'>"
						+ "<i style='color:black' class='fa fa-circle-o-notch fa-lg fa-spin'></i>"
						+ "<p style='font-size:20px;color:black;'><b>Please Wait!</b></p>"
						+ "</div";
					$("#fakeLoader").html(loadingNotice);
				}else{
					$.each(loaderIdArray, function(index, value){
						$(value).addClass("ajaxcall-ongoing");
					});
				}
			}
		},
		statusCode: {
			302: function() {
				toastr.error("Action Not Found");
			}
		},
		success : function(data) {
			if(data.responseCode != undefined){
				data.responseCode == 2 ?
					toastr.error(data.msg)
				: (data.payload != undefined ? 
					(typeof emptyAllowed == undefined ? 
						(data.payload.length == 0 ? toastr.error("No data found!") : callback(data.payload)) : callback(data.payload)
					) : toastr.success(data.msg)
				);
			}else{
				(Array.isArray(data) && data.length == 0) ? 
					(toastr.error("No data found!"), callback(data))
				: callback(data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
		    toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus + ", Message: " + errorThrown);
		},
		failure : function(response) {
		    toastr.error(response);
		},
		complete: function (data) {
			if(loaderIdArray != undefined){
				if(loaderIdArray.length == 0){
					$("#fakeLoader").removeClass("full-page-loading");
					$("#fakeLoader").html("");
				}else{
					$(".ajaxcall-ongoing").each(function(index){
						$(this).removeClass("ajaxcall-ongoing");
					});
				}
			}
	    }
    });
    toastr.options.timeOut = "10000";
}


function ajaxSubmit(){
	$.each(arguments, function(index, value){
		value();
	});
	var form = $("body form");
	
	$("#submit").click(function(event){
		if ($(form).valid()){
			ajax($(form).attr("action"), $(form).serialize(), function(){
				window.location.href = context + data.location;
			}, "POST", []);
		}
	});
}