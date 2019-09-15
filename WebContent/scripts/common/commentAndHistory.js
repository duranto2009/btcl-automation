var dataOfList = {};
$("#actionList option").each(function(i, el) {
    dataOfList[$(el).data("value")] = $(el).val();
});
var loadComment = function() {

    var entityID = $("#entityID").val();
    var entityTypeID = $("#entityTypeID").val();
    $.ajax({
	type : 'get',
	url : context + 'Comment.do',
	data : {
	    entityTypeID : entityTypeID,
	    entityID : entityID,
	    start : 0
	},
	success : function(response) {
	    $("#heading").val("");
	    $("#description").val("");
	    createHtmlForComment(response);
	}
    });
};

var createHTMLForCommentWithFile = function(val) {

    $("#load-comment")
	    .append(
		    "<div class='note'>"
			    + "<span class='block font-lg'>"
			    + val.username
			    + " </span>"
			    + "<span class=' font-xs'>"
			    + " at "
			    + val.time
			    + "</span><br>"
			    + "<span class='font-purple-soft'>"
			    + val.subject
			    + "</span><br><p><span class='font-dark'>"
			    + val.description
			    + "</span></p>"
			    + "<span class='font-blue-ebonyclay'>Attachment </span>: <a href='../../common/downloadFile.jsp?et="
			    + val.et + "&e=" + val.e + "'>" + val.filename
			    + "</a></div>");

};

var createHTMLForCommentWithoutFile = function(val) {

    $("#load-comment").append(
	    "<div class='note'>" + "<span class='block font-lg'>"
		    + val.username + " </span>" + "<span class=' font-xs'>"
		    + " at " + val.time + "</span><br>"
		    + "<span class='font-purple-soft'>" + val.subject
		    + "</span><br><p><span class='font-dark'>"
		    + val.description + "</span></p> </div>");

};

var createHtmlForComment = function(json) {
    $("#load-comment").empty();
    $(json).each(function(i, val) {
	if (val.filename === undefined) {
	    createHTMLForCommentWithoutFile(val);
	} else {
	    createHTMLForCommentWithFile(val);
	}

    });
};
// End : Load old + new comment, when user submit a new comment. //

// Comment submit to action for update//
var commentForm = $("#commentForm");
commentForm.submit(function(ev) {

    var formData = new FormData($(this)[0]);
    $.ajax({
	type : commentForm.attr('method'),
	url : context + 'Comment.do?mode=add',
	data : formData,
	async : false,
	cache : false,
	contentType : false,
	processData : false,
	success : function(data) {
	    loadComment();
	}
    });
    ev.preventDefault();
});
// End : Comment submit to action for update //

// Start : Histoy Search //

var createHtmlForHistorySearch = function(json) {
    $("#load-history").empty();
    $(json).each(
	    function(i, val) {
		$("#load-history").append(
			"<div class='note note-info'>"
				+ "<span class='block font-lg'>" + val.username
				+ " </span>" + "<span class=' font-xs'>"
				+ " at " + val.time
				+ "</span><p> <span class='font-dark'>"
				+ val.description + "</p></div>");

	    });
};
var historySearchForm = $("#historySearchForm");
historySearchForm.submit(function(ev) {

    var pieces = window.location.href.split("/");
    var name = pieces[pieces.length - 1];
    var check = "GetRequestForView";
    var req = false;
    if (name.indexOf(check) != -1) {
	req = true;
    }
    var entityID = $("#entityID").val();
    var entityTypeID = $("#entityTypeID").val();
    var username = $("#username").val();
    var innerContent = $("#innerContent").val();
    var fromDate = $("#datepicker-from").val();
    var toDate = $("#datepicker-to").val();

    // var value = $('#actionTypeID').val();
    // var actionTypeID = $('#actionList [value="' + value +
    // '"]').data('value');//$("#actionTypeID option:selected").val();
    var actionTypeID = $("#actionTypeID option:selected").val();
    $.ajax({
	type : 'get',
	url : context + 'FetchAutoLoadAction.do?mode=search',
	data : {
	    entityTypeID : entityTypeID,
	    entityID : entityID,
	    username : username,
	    innerContent : innerContent,
	    fromDate : fromDate,
	    toDate : toDate,
	    actionTypeID : actionTypeID,
	    req : req
	},
	success : function(data) {
	    createHtmlForHistorySearch(data);
	}
    });
    ev.preventDefault();
});

// End : History Search //

$(document)
	.ready(
		function() {
		    // This code :load comment when page load and 'load
		    // more'button is clicked //
		    $("#load-more-comment-btn").click(function() {
			loadMoreCommentIN();
		    });

		    var loadMoreCommentIN = function() {
			var start = $("#next-count-comment").val();
			var entityID = $("#entityID").val();
			var entityTypeID = $("#entityTypeID").val();
			$
				.ajax({
				    type : 'get',
				    url : context + 'Comment.do',
				    data : {
					entityTypeID : entityTypeID,
					entityID : entityID,
					start : start
				    },
				    success : function(response) {
					createHtmlForCommentIN(response);
					$('#next-count-comment').val(
						Number(start) + 1);
				    }
				});
		    };

		    var createHtmlForCommentIN = function(json) {
			$(json).each(function(i, val) {

			    if (val.filename === undefined) {
				createHTMLForCommentWithoutFile(val);
			    } else {
				createHTMLForCommentWithFile(val);
			    }

			});
		    };

		    // End : This code :load comment when page load and 'load
		    // more'button is clicked //

		    // History//

		    $("#load-more-history-btn").click(function() {
			loadMoreHistory();
		    });
		    var loadMoreHistory = function() {
			var pieces = window.location.href.split("/");
			var name = pieces[pieces.length - 1];
			var check = "GetRequestForView";
			var req = false;
			if (name.indexOf(check) != -1) {
			    req = true;
			}
			var val = $("#next-count-history").val();
			var entityTypeID = $("#entityTypeID").val();
			var entityID = $("#entityID").val();
			$.ajax({
			    type : 'post',
			    url : context + 'FetchAutoLoadAction.do',
			    data : {
				mode : "loadHistory",
				entityTypeID : entityTypeID,
				entityID : entityID,
				start : val,
				req : req
			    },
			    success : function(response) {
				createHtmlForHistory(response);
				$('#next-count-history').val(Number(val) + 1);
			    }
			});
		    };

		    var createHtmlForHistory = function(json) {
			$(json)
				.each(
					function(i, val) {
					    $("#load-history")
						    .append(
							    "<div class='note'>"
								    + "<span class='block font-lg'>"
								    + val.username
								    + " </span>"
								    + "<span class=' font-xs'>"
								    + " at "
								    + val.time
								    + "</span><p> <span class='font-dark'>"
								    + val.description
								    + "</p></div>");

					});
		    };
		    // End: load history //

		    // Start : Histoy Search //

		    // End : History Search //

		    loadMoreHistory();
		    loadMoreCommentIN();
		});
