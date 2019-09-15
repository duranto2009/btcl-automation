$(document).ready(
	function() {
	    $(".dashboardItem.general").each(function() {
		var setPlaceID = $(this).attr("id")
		var moduleID = $(this).data("module");
		$.ajax({
		    type : 'get',
		    url : context + 'FetchAutoLoadAction.do',
		    data : {
			mode : "countRequest",
			moduleID : moduleID,
		    },
		    success : function(response) {
			console.log(setPlaceID);
			$('#' + setPlaceID).html(response[0]['totalCount']);
		    }
		});
	    });

	    function loadServices() {
		$.ajax({
		    type : 'get',
		    url : context + 'FetchAutoLoadAction.do',
		    data : {
			mode : "serviceCount",
		    },
		    success : function(response) {
			// $('#' + setPlaceID).html(response[0]['totalCount']);
			console.log(response);
			$.each(response, function(index, serviceItem) {
			    console.log(serviceItem);
			    $("#clientService_" + serviceItem['serviceTypeID'])
				    .html(serviceItem['serviceCount']);
			})
		    }
		});
	    }
	    loadServices();
	});
