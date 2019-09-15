/*
 * Report Generation
 * Contributor : Dhrubo & Kawser
 */
/*
 * START : Table generation using ajax data and Jquery dataTable 
 * By: Kawser
 */
//document.addEventListener('contextmenu', event => event.preventDefault());
let currentPageNo = 1;
let totalRecord = 0;
let totalPageNo = 0;
let recordPerPage = 0;

let reportTable = $('#reportTable');
let reportForm =  $("#ReportForm");
//init DataTable first time without any data
let reportDataTable = reportTable.DataTable({
    'searching': false,
    'destroy': true,
    'ordering': false,
    'scrollX': true,
    'scrollY': true,
    'paging': false
});

let flashOldReport = function(){
	currentPageNo = 1;
	totalRecord = 0;
	totalPageNo = 0;
	recordPerPage = 0;
	
	$(".navigator").hide();
	reportDataTable.destroy();
    reportTable.empty();
    let createdHTML = "<thead><tr><th></th></tr></thead><tbody></tody>";
    reportTable.append(createdHTML);
    reportDataTable = reportTable.DataTable({
        'searching': false,
        'destroy': true,
        'ordering': false,
        'scrollX': true,
        'scrollY': true,
        'paging': false
    });
};
	
$(document).ready(function () {
	
    let columnDataFromater = {
        exportOptions: {
            format: {
                body: function (data, row, column, node) {
                	/*data.replace( /[$,.]/g, '' )*/ 
                    return data.replace(/(&nbsp;|<([^>]+)>)/ig, "");/*remove html tag from data when exporting*/
                }
            }
        }
    };
    
    let createTable = function (data) {
        let createdHTML = "";
        let isBodyCreatedStart = false;
        let isHeadCreated = false;
        $.each(data, function (index, list) {
            if (index != 0) {
                if (!isBodyCreatedStart) {
                    createdHTML += "<tbody>";
                    isBodyCreatedStart = true;
                }
                createdHTML += "<tr>";
                $.each(list, function (innerIndex, listItem) {
                    createdHTML += "<td class='text-center'>" + listItem + "</td>"
                })
                createdHTML += "</tr>";
            } else {
                if (!isHeadCreated) {
                    createdHTML += "<thead><tr>";
                }
                $.each(list, function (innerIndex, listItem) {
                    createdHTML += "<th class='text-center'>" + listItem + "</th>"
                })
                if (!isHeadCreated) {
                    createdHTML += "</thead></tr>";
                    isHeadCreated = true;
                }
            }
        });
        createdHTML += "</tbody>";
        reportDataTable.destroy();
        reportTable.empty();
        reportTable.attr("width", "100%");
        reportTable.append(createdHTML);
        reportDataTable = reportTable.DataTable({
            'searching': false,
            'destroy': true,
            'ordering': false,
            'scrollX': true,
            'scrollY': true,
            'paging': false,
            'pagingType': 'simple',
            'dom': 'Bfrtip',
            'buttons': [
                $.extend(true, {}, columnDataFromater, {
                    extend: 'copyHtml5'
                }),
                $.extend(true, {}, columnDataFromater, {
                    extend: 'excelHtml5'
                }),
                $.extend(true, {}, columnDataFromater, {
                    extend: 'csvHtml5'
                })
            ]
            
        });
        $("#report-div").show();
        $('html, body').animate({
            scrollTop: parseInt($(".custom-form-action").offset().top-20)
        }, 400);
        
        $("#reportTable_info").html("Total "+totalRecord+" entries");
    }

    function createTableCallback(data) {
    	if (data['responseCode'] == 1) {
    	    createTable(data.payload);
    	}else {
    		toastr.error(data['msg']);
    	}
    }
    
    function isAnyDisplayItemChecked(){
    	let isFoundDisplay =false;
    	let reportFormDataArray = reportForm.serializeArray();
    	$.each(reportFormDataArray,function(index,data){
    		if(data.name.indexOf("display") !== -1){
    			isFoundDisplay = true;			
    		}
    	});
    	return isFoundDisplay;
    }
    
    let updateRecordInfo = function(totalRecord,recordPerPage){
    	totalPageNo = Math.ceil(parseFloat(totalRecord)/parseFloat(recordPerPage));
    	$("input[name=tatalPageNo]").val(totalPageNo);
    	$("input[name=totalRecord]").val(totalRecord);
    	
    };
    
    let getTotalDataCountCallBack = function(data){
    	recordPerPage = $("input[name=RECORDS_PER_PAGE]").val();
    	totalRecord = data.payload;
    	$("#reportTable_info").html("Total "+totalRecord+" entries");
    	updateRecordInfo(totalRecord,recordPerPage);  	
    };
    
    
    
    let submit = function(){
        if(isAnyDisplayItemChecked()){ 
        	$(".navigator").show();
        	
        	callAjax(reportForm.attr('action'), reportForm.serialize(), createTableCallback, "GET");
        }else{
        	flashOldReport();
        	alert("Please select one/more display item to generate a report.");
        }
    }
    
    $("#defaultLoad").on("click",function(event){
    	$("input[name=pageno]").val(currentPageNo);
    	event.preventDefault();
    	callAjax(context+$("#countURL").val(),reportForm.serialize(),getTotalDataCountCallBack,"GET");
    	submit();
    });
    
    $("#forceLoad").on("click",function(event){
    	event.preventDefault();
    	updateRecordInfo(totalRecord,$("input[name=RECORDS_PER_PAGE]").val());
    	submit();
    });
    
    $("#firstLoad").on("click",function(event){
    	currentPageNo = 1;
    	$("input[name=pageno]").val(currentPageNo);
    	event.preventDefault();
    	submit();
    });
    
    $("#nextLoad").on("click",function(event){
    	if(currentPageNo < totalPageNo){
    		currentPageNo++;
    	}
    	$("input[name=pageno]").val(currentPageNo);
    	event.preventDefault();
    	submit();
    });
    
    $("#previousLoad").on("click",function(event){
    	if(currentPageNo >= 2 ){
    		currentPageNo--;
    	}
    	$("input[name=pageno]").val(currentPageNo);
    	event.preventDefault();
    	submit();
    });
    
    $("#lastLoad").on("click",function(event){
    	$("input[name=pageno]").val(totalPageNo);
    	event.preventDefault();
    	submit();
    });
   
});
/*
 * END : Table generation using ajax data
 */
/*
 *START : Generate dynamic code from DisplayDiv
 *BY: Dhrubo
 */

function drawCriteriaAndOrderFromDisplay() {
    let criteriaHtml = '<div class="portlet light">' +
        '<div class="portlet-title"><div class="caption"><i class="fa fa-list"></i>Criteria</div></div>' +
        '<div class="portlet-body form" style="height: 30vh; overflow-x: hidden; overflow-y:  scroll;"><div class="form-body"></div></div>' +
        '</div>'
    $("#criteria").html(criteriaHtml);

    let orderByHtml = '<div class="portlet light">' +
        '<div class="portlet-title"><div class="caption"><i class="fa fa-reorder"></i>Order By</div></div>' +
        '<div class="portlet-body form" style="height: 30vh; overflow-x: hidden; overflow-y:  scroll;"><div class="form-body"></div></div>' +
        '</div>'
    $("#orderby").html(orderByHtml);

    $("#display .form-group .display-input")
        .each(
            function (index) {
                let thisName = $(this).attr('name');
                let newValue = thisName.substring(8);

                let newFormGroupCriteria = '<div class="form-group"><div class="col-md-9">' +
                    '<label class="checkbox"><span><input type="checkbox" class="input-checkbox-criteria" value="' +
                    $(this).parent().prev().find("input")
                    .val() +
                    '"></span>' +
                    $(this).val() +
                    '</label></div>' +
                    '<div class="col-md-3" style="position: relative;">' +
                    '<span class="up-down-link" style="position: absolute; top: 50%"><a class="up-link"><span><i class="fa fa-arrow-circle-up"></i></span></a><a class="down-link"><span><i class="fa fa-arrow-circle-down"></i></span></a></span>' +
                    '</div></div>';
                $("#criteria .form-body").append(
                    newFormGroupCriteria);

                let newFormGroupOrderBy = '<div class="form-group">' +
                    '<div class="col-md-9">' +
                    '<label class="checkbox"><span><input type="checkbox" class="input-checkbox-orderby" value="' +
                    newValue +
                    '" name="orderByColumns" data-sequenceno="' +
                    $(this).parent().prev().find("input")
                    .val() +
                    '"></span>' +
                    $(this).val() +
                    '</label>' +
                    '</div>' +
                    '<div class="col-md-3">' +
                    '<span class="up-down-link" style="position: absolute; top: 50%"><a class="up-link"><span><i class="fa fa-arrow-circle-up"></i></span></a><a class="down-link"><span><i class="fa fa-arrow-circle-down"></i></span></a></span>' +
                    '</div></div>';
                $("#orderby .form-body")
                    .append(newFormGroupOrderBy);
            });
}
drawCriteriaAndOrderFromDisplay();
/*
 *END : Generate dynamic code from DisplayDiv
 */

/*
 * START: Store sequence of criteria,display, order by
 * BY: Dhrubo
 */
let criteriaSequenceArray = [];
$("#criteria .input-checkbox-criteria").each(function (index) {
    criteriaSequenceArray.push($(this).val());
});

$(".input-checkbox-criteria")
    .click(
        function () {
        	flashOldReport();
            if (!($(this).parent().hasClass("checked"))) {
                let elementFromDisplay = $("#display .input-checkbox-display[value='" +
                    $(this).val() + "']");
                let elementWithInput = elementFromDisplay.parent()
                    .parent().parent().parent().parent().next()
                    .children().first();
                let newName = elementWithInput.attr("name");
                newName = newName.substring(8);
                if (!("datepicker" == elementWithInput.attr("data-comment"))) {
	                newName = "criteria." + newName + "." + elementWithInput.attr("data-operator");
                }
                if ("select" == elementWithInput
                    .attr("data-comment")) {

                    let newFormGroupSearchCriteria = '<div id="' +
                        $(this).val() +
                        '" class="search-criteria-div"><div class="form-group"><label class="col-sm-3 control-label">' +
                        $(this).parent().parent().parent()
                        .parent().text() +
                        '</label>' +
                        '<div class="col-sm-6"><select class="form-control" name="' + newName + '"><option value="">Select</option></select>' +
                        '</div></div></div>';
                    $("#searchCriteria").append(
                        newFormGroupSearchCriteria);

                    let valueArray = elementWithInput.attr(
                        "data-values").split(",");
                    $.each(valueArray, function (index, value) {
                        let nameValuePair = value.split(":");
                        $(
                            "#searchCriteria select[name='" +
                            newName + "']").append(
                            "<option value=" + nameValuePair[1] + ">" +
                            nameValuePair[0] +
                            "</option>");
                    });
                }
                else {
                    if ("datepicker" == elementWithInput.attr("data-comment")) {
                    	
                    	let newFormGroupSearchCriteria = '<div id="' + $(this).val() + '" class="search-criteria-div">'
                    	
                    	+'<div class="form-group"><label class="col-sm-3 control-label">' +
                        $(this).parent().parent().parent().parent().text() +' From </label>' +
                        '<div class="col-sm-6"><input type="text" class="form-control" name="criteria.' + newName + '.geq" value="">' +
                        '</div></div>'
                        
                        +'<div class="form-group"><label class="col-sm-3 control-label">' +
                        $(this).parent().parent().parent().parent().text() +' To </label>' +
                        '<div class="col-sm-6"><input type="text" class="form-control" name="criteria.' + newName + '.leq" value="">' +
                        '</div></div>'

                        +'</div>';
                    	$("#searchCriteria").append(newFormGroupSearchCriteria);
                    	
                        $("#searchCriteria input[name='criteria." + newName + ".leq']").addClass("datepicker");
                        $("#searchCriteria input[name='criteria." + newName + ".geq']").addClass("datepicker");
                        $('.datepicker').datepicker({
                            orientation: "top",
                            autoclose: true,
                            format: 'dd/mm/yyyy',
                            todayBtn: 'linked',
                            todayHighlight: true
                        });
                    } else{
                    	let newFormGroupSearchCriteria = '<div id="' + $(this).val() + '" class="search-criteria-div"><div class="form-group"><label class="col-sm-3 control-label">' +
                        $(this).parent().parent().parent().parent().text() +'</label>' +
                        '<div class="col-sm-6"><input type="text" class="form-control" name="' + newName + '" value="">' +
                        '</div></div></div>';
                    	$("#searchCriteria").append(newFormGroupSearchCriteria);
                    }
                }
            }
            else {
                if ($("#searchCriteria").has("#" + $(this).val())) {
                    $("#" + $(this).val()).remove();
                }
            }
            sortSearchCriteria();
        });

function sortSearchCriteria() {
    let $searchCriteriaDivs = $('#searchCriteria').children(
        '.search-criteria-div');

    $searchCriteriaDivs
        .sort(function (div1, div2) {
            let index1 = criteriaSequenceArray.indexOf(div1
                .getAttribute('id'));
            let index2 = criteriaSequenceArray.indexOf(div2
                .getAttribute('id'));

            if (index1 > index2) {
                return 1;
            }
            if (index1 < index2) {
                return -1;
            }
            return 0;
        });

    $searchCriteriaDivs.detach().appendTo('#searchCriteria');
}

$(function () {
    $('.up-link')
        .on(
            'click',
            function (e) {
            	flashOldReport();
                let thisRow = $(this).closest('.form-group');
                let hook = thisRow.prev('.form-group');
                if (hook.length) {
                    let elementToMove = thisRow.detach();
                    hook.before(elementToMove);

                    let thisVal = thisRow.find(
                        ".input-checkbox-criteria").val();
                    let thisValIndex = criteriaSequenceArray
                        .indexOf(thisVal);
                    criteriaSequenceArray[thisValIndex] = criteriaSequenceArray[thisValIndex - 1];
                    criteriaSequenceArray[thisValIndex - 1] = thisVal;

                    sortSearchCriteria();
                }
                return false;
            });
    $('.down-link')
        .on(
            'click',
            function () {
            	flashOldReport();
                let thisRow = $(this).closest('.form-group');
                let hook = thisRow.next('.form-group');
                if (hook.length) {
                    let elementToMove = thisRow.detach();
                    hook.after(elementToMove);

                    let thisVal = thisRow.find(
                        ".input-checkbox-criteria").val();
                    let thisValIndex = criteriaSequenceArray
                        .indexOf(thisVal);
                    criteriaSequenceArray[thisValIndex] = criteriaSequenceArray[thisValIndex + 1];
                    criteriaSequenceArray[thisValIndex + 1] = thisVal;

                    sortSearchCriteria();
                }
                return false;
            });
});
/*
 * END: Store sequence of criteria,display, order by
 */

/*
 * START : Display Check-box on click handler
 * BY: Dhrubo
 */
$(".input-checkbox-display").click(
	    function() {
	    	flashOldReport();
	        if ($(this).parent().hasClass("checked")) {
	            $(this).parent().parent().parent().parent().parent().next()
	                .find("input").prop('disabled', true);
	        }
	        else {
	            $(this).parent().parent().parent().parent().parent().next()
	                .find("input").prop('disabled', false);
	        }
});
/*
 * END : Display Check-box on click handler
 */
/*
 * START : Load template List
 * BY: Dhrubo
 */
$(document).ready(function () {
	let urlLoadTemplateList = context + "ReportTemplate.do";
	let loadTemplateListData = {};
	loadTemplateListData.mode = "loadTemplateList";

	function loadTemplateListCallback(data) {
		$.each(data, function (index, item) {
			$(".load-template").append($('<option>', {
				value: item.id,
				text: item.name
			}));
		});
	}

	function loadTemplateList() {
		$(".load-template").html("<option value='-1'>Load a Template</option>");
		callAjax(urlLoadTemplateList, loadTemplateListData, loadTemplateListCallback, "GET");
	}
	loadTemplateList();
	//save template
	$(".save-template").on('keyup', function () {
		if ($(this).val().length > 0) {
			$(this).parent().next().find("button").prop('disabled', false);
		}
		else {
			$(this).parent().next().find("button").prop('disabled', true);
		}
	});

	function saveTemplateCallback(data) {
		if (data.success == "true") {
			toastr.success("Template is saved successfully.");
			loadTemplateList();
		}
	}
	$("#save-template-button").click(function () {
		let serializedFormData = $("#reportForm").serialize();
		if (serializedFormData.length > 0) {
			let urlSaveTemplate = context + "ReportTemplate.do";
			serializedFormData += "&mode=saveTemplate";
			serializedFormData += "&reportTemplateName=" + $(this).parent().prev().find("input").val();
			callAjax(urlSaveTemplate, serializedFormData, saveTemplateCallback, "POST");
		}
		else {
			toastr.error("Nothing is selected.");
		}
	});
});
/*
 * END : Load template List
 */

/*
 * START : Redraw all criteria 
 * BY: Dhrubo
 **/
function redrawAllCriterias() {
	let $displayDivs = $('#display .form-body').children('.form-group');
	$displayDivs.sort(function (div1, div2) {
		let index1 = div1.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.getAttribute("value");
		let index2 = div2.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.getAttribute("value");
		console.log("index1 " + index1);
		if (index1 > index2) {
			return 1;
		}
		if (index1 < index2) {
			return -1;
		}
		return 0;
	});
	$displayDivs.detach().appendTo('#display .form-body');
	$("#display .form-group .input-checkbox-display").each(function (index) {
		if (($(this).parent().hasClass("checked"))) {
			$(this).trigger("click");
		}
	});
	let $criteriaDivs = $('#criteria .form-body').children('.form-group');
	$criteriaDivs.sort(function (div1, div2) {
		let index1 = div1.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.getAttribute("value");
		let index2 = div2.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.getAttribute("value");
		if (index1 > index2) {
			return 1;
		}
		if (index1 < index2) {
			return -1;
		}
		return 0;
	});
	$criteriaDivs.detach().appendTo('#criteria .form-body');
	$("#criteria .form-group .input-checkbox-criteria").each(function (index) {
		if (($(this).parent().hasClass("checked"))) {
			$(this).trigger("click");
		}
	});
	let $orderDivs = $('#orderby .form-body').children('.form-group');
	$orderDivs.sort(function (div1, div2) {
		let index1 = div1.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.getAttribute("data-sequenceno");
		let index2 = div2.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.firstElementChild.getAttribute("data-sequenceno");
		if (index1 > index2) {
			return 1;
		}
		if (index1 < index2) {
			return -1;
		}
		return 0;
	});
	$orderDivs.detach().appendTo('#orderby .form-body');
	$("#orderby .form-group .input-checkbox-orderby").each(function (index) {
		if (($(this).parent().hasClass("checked"))) {
			$(this).trigger("click");
		}
	});
}
/*
 * END : Redraw all criteria
 */

/*
 * START : Load template
 * BY: Dhrubo
 */
$(".load-template").on('change', function () {
	if ($(this).val() > 0) {
		$(this).parent().next().find("button").prop('disabled', false);
	}
	else {
		$(this).parent().next().find("button").prop('disabled', true);
	}
});
let urlLoadTemplate = context + "ReportTemplate.do";
let loadTemplateData = {};
loadTemplateData.mode = "loadTemplate";

function loadTemplateCallback(data) {
	let criteriaKeys = data.reportCriteria.split(',');
	let displayKeyValuePairs = data.reportDisplay.split(',');
	let orderValues = data.reportOrder.split(',');
	redrawAllCriterias();
	$.each(displayKeyValuePairs.reverse(), function (index, item) {
		let displayName = item.split("=")[0];
		let displayValue = item.split("=")[1];
		$("input[name='" + displayName + "']").val(displayValue);
		let displayItem = $("input[name='" + displayName + "']").parent().parent();
		displayItem.detach().prependTo("#display .form-body");
		$("input[name='" + displayName + "']").parent().prev().find("input").trigger("click");
	});
	$.each(criteriaKeys.reverse(), function (index, item) {
		let nameOfItemSplitted = item.split(".");
		nameOfItemSplitted[0] = "display";
		nameOfItemSplitted.pop();
		let displayNameOfCriteria = nameOfItemSplitted.join(".");
		let sequenceNoOfItem = $("input[name='" + displayNameOfCriteria + "']").parent().prev().find(".input-checkbox-display").val();
		let criteriaItem = $("#criteria input[value='" + sequenceNoOfItem + "']").parent().parent().parent().parent().parent().parent();
		criteriaItem.detach().prependTo("#criteria .form-body");
		let indexOfSequenceNoInCriteriaSequenceArray = criteriaSequenceArray.indexOf(sequenceNoOfItem);
		criteriaSequenceArray.splice(indexOfSequenceNoInCriteriaSequenceArray, 1);
		criteriaSequenceArray.unshift(sequenceNoOfItem);
		$("#criteria input[value='" + sequenceNoOfItem + "']").trigger("click");
	});
	$.each(orderValues.reverse(), function (index, item) {
		let orderItem = $("#orderby input[value='" + item + "']").parent().parent().parent().parent().parent().parent();
		orderItem.detach().prependTo("#orderby .form-body");
		$("#orderby input[value='" + item + "']").trigger("click");
	});
	toastr.success("Template Loaded Successfully");
}
$("#load-template-button").click(function () {
	loadTemplateData.reportTemplateID = $(this).parent().prev().find("select").val();
	callAjax(urlLoadTemplate, loadTemplateData, loadTemplateCallback, "GET");
});
/*
* END :Load template
*/

