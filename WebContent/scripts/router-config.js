// Write Button Click Action Here
$(document).ready(function() {
	
	var clickExecuteRouter = new ClickAction();
	
	//(onClick Events)
	
	//////////////////////// Domain ////////////////////////
	// Domain > Apply for Domain Service > 
	$('#applyDomain').on('click', function() {
		clickExecuteRouter.showMainDivForApplyDomain();
		clickExecuteRouter.ChangeUrl('Apply Domain','apply_domain');
	});

	// Domain > My Services > 
	$('#viewDomain').on('click', function() {
		clickExecuteRouter.showMainDivForViewDomain();
		clickExecuteRouter.ChangeUrl('View Domain','view_domain');
	});
	
	////////////////////////Web Hosting //////////////////
	
	//////////////////////////////////////////////////////
	

});


var ClickAction = function(){
	
	var _private = {};
	var _public = {};
	
	//(onClick Events Implementation)
	
	_public.ChangeUrl = function(page, url) {
        if (typeof (history.pushState) != "undefined") {
            var obj = { Page: page, Url: url };
            history.pushState(obj, obj.Page, obj.Url);
        } else {
            alert("Browser does not support HTML5.");
        }
    }
	
	/////////////////// Domain ////////////////// 
	
	// Domain > Apply for Domain Service > 
	_public.showMainDivForApplyDomain = function(){
		$("#innerMainDiv").load('../hosting/buydomain.jsp');
	};
	
	// Domain > My Services > 
	_public.showMainDivForViewDomain = function(){
		$.ajax({
			type : "GET",
			url : "../ViewHostingdomain.do",
			data : "",
			success : function(response) {
				$("#innerMainDiv").load('../hostingdomain/hostingdomain.jsp');
			}
		
		});
	};
	
	
	
	//////////////////// Web Hosting ///////////////////////
	return _public;
	
};

