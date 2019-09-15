var baseUrl = context;
var systemConfig = (function(){
    var ajax_timeout = 5000000; // ms
    // var websocketurl="ws://"+"127.0.0.1:8080/icpnothi/"+"ChatServer";
    var poll_request_timeout = 5000000 //
    var call_after_page_load = 5000;// ms
    var call_after_page_load_short = 2000;// ms
    var typing_delay = 500;// ms
    var username_reg_expr=/^([A-Za-z0-9]+[_\.\-]*[A-Za-z0-9]+)+$/;
    var mobile_number_reg_expr=/^[\+]?[(]?[0-9\-]{3}[)]?[-\s\.]?[0-9\-]{3}[-\s\.]?[0-9\-]{1,15}$/;
    var url_reg_expr= /^(http(s)?:\/\/)?(www\.)?[a-zA-Z0-9]+([\-\.]{1}[a-zA-Z0-9]+)*\.[a-zA-Z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;
    var dns_name_expr= /^([a-zA-Z0-9][a-zA-Z0-9-_]{0,61}[a-zA-Z0-9]{0,1}\.)+([a-zA-Z]{1,6}|[a-zA-Z0-9-]{1,30}\.[a-zA-Z]{2,20})$/;
    var email_reg_expr = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    


    return {
		getBaseUrl : function() {
		    return baseUrl;
		},
		getAjaxTimeOut : function() {
		    return ajax_timeout;
		},
		getPollTimeOut : function() {
		    return poll_request_timeout;
		},
		getCallAfterPageLoadTimer : function() {
		    return call_after_page_load;
		},
		getCallAfterPageLoadShortTimer : function() {
		    return call_after_page_load_short;
		},
		getMobileNumRegExpr : function(){
			return mobile_number_reg_expr;
		},
		getUrlRegExpr : function(){
			return url_reg_expr;
		},
		getUsernameRegExpr : function(){
			return username_reg_expr;
		},
		getDnsNameRegExpr : function(){
			return dns_name_expr;
		},
		getEmailRegExpr : function(){
			return email_reg_expr;
		},
		getTypingDelay : function(){
			return typing_delay;
		},
    };
})();
