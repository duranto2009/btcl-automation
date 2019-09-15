$(document).ready(function() {
	
	var totalBtclAmount = 0.0;
	var totalVat = 0.0;
	var totalPayable = 0.0; 
	
	
	var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
	                  "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
	                ];
	
	$("#sample_editable_1_info").hide();
	$("#payDue").hide(); 
	$("#distributeReset").hide();
	$("#totalAmountDiv").hide();
	$("#totalAmount").text( "0" );
	
	Date.prototype.addMonths = function (m) {
		
	    var d = new Date(this);
	    var years = Math.floor(m / 12);
	    var months = m - (years * 12);
	    if (years) d.setFullYear(d.getFullYear() + years);
	    if (months) d.setMonth(d.getMonth() + months);
	    return d;
	}
	
	$("#distributeEvenly").on( "change", function(){
		
		if( $(this).is(":checked") ){
			
			var noOfMonth = $("#noOfMonths").val();
			var installment = 0.0;
			
			if( totalPayable != 0 ){
				
				installment = totalPayable / noOfMonth;
			}
			
			$(".duePaymentAmount").each(function(){
				
				$(this).val( installment );
			});
			
			renderTotalAmount();
		}
	});
	
	$( document ).on( "change keyup input", ".duePaymentAmount" , function(){

		renderTotalAmount();
	});
	
	function renderTotalAmount(){
		
		var sum = 0.0;
		
		$(".duePaymentAmount").each(function(){
			
			if( $(this).val() )
				sum += parseFloat( $(this).val() );
		});
		
		$("#totalAmount").text( sum );
	}
	
	$("#payDue").on( "click", function(){
		
		var sum = 0;
		
		$(".duePaymentAmount").each(function(){
			
			sum += parseFloat( $(this).val() );
			console.log( $(this).val() );
		});
		
		if( sum != totalPayable ){
			
			toastr.error( " Sum of amount paid per month is not equal to the due bill " );
		}
		else{
			
			var data = new Array();
			
			$(".duePaymentAmount").each(function(){
				
				var temp = {};
				temp['installment'] = $(this).val();
				temp['month'] = $(this).attr("month");
				temp['year'] = $(this).attr("year");
				
				data.push( temp );
			});
			
			var param = {};
			
			var bills = new Array();
			
			$("#billTableBody").find('tr').each(function () {
					
				var id = parseInt( $(this).find( "a" ).text() );
				
				if( !isNaN(id) ){
					bills.push( id );
				}
			});
			
			param['billID'] = JSON.stringify( bills );
			param['data'] = JSON.stringify( data );
			param['method'] = "mergeBills";
			
			var url = context + "/SplitBill.do";
			callAjax( url, param, renderMergedBill, "POST" );
		}
	});
	
	$("#distributeReset").on( "click", function(){
		
		$(".duePaymentAmount").each(function(){
			
			$(this).val( "0" );
		});
		$("#totalAmount").val("0");
	});
	
	$('.datepicker').datepicker({
        	orientation: "top",
         autoclose: true,
         format: 'dd/mm/yyyy',
         todayBtn: 'linked',
         todayHighlight:	true
    });
	
	$("#noOfMonths").on("keyup",function(){
		if( $("#payBillFrom").val() != "" )
			renderDuePaymentDate();
	});
	
	$("#payBillFrom").on( "change", renderDuePaymentDate );
	
	function renderMergedBill( data ){
		
		console.log( data );
		
		if( data['responseCode'] == 2 ){
			
			toastr.error( data['msg'] );
		}
		else{
			
			toastr.success( data['msg'] );
			resetPage();
		}
	}
	
	function resetPage(){
		
		$("#clientIdStr").val("");
		$("#clientId").val("");
		$("#billTableBody").empty();
		$("#noOfMonths").val("");
		$("#payBillFrom").val("");
		$("#duePaymentContainer").empty();
		$("#totalAmountDiv").hide();
		$("#totalAmount").val("");
		$("#payDue").hide();
		$("#distributeReset").hide();
	}
	
	function renderDuePaymentDate(){
		
		var payFrom = $("#payBillFrom").val();
		var fromDate = new Date( payFrom );
		
		if( $("#noOfMonths").val() == "" ){
			
			toastr.error( "First fill in how many months the due will be paid" );
		}
		
		var noOfMonth = $("#noOfMonths").val();

		var container = $("#duePaymentContainer");
		$(container).empty();
		
		for( var i = 0; i < noOfMonth; i++ ){
			
			var div = $("<div/>");
			var label = $("<label/>");
			var div2 = $("<div/>");
			var input = $("<input/>");
			
			$(label).addClass( "col-sm-3 control-label text-right" );
			$(label).text( monthNames[fromDate.getMonth()] + ", " + fromDate.getFullYear() + " installation" );
			
			$(div).addClass( "form-group" );
			$(div2).addClass( "col-sm-6" );
			$(input).addClass( "duePaymentAmount form-control" );
			$(input).attr( "type", "number" );
			$(input).attr( "step", "any" );
			$(input).attr( "month", fromDate.getMonth() );
			$(input).attr( "year", fromDate.getFullYear() );
			
			$(div2).append( input );
			$(div).append( label );
			$(div).append( div2 );
			$(container).append( div );
			$(container).append( $("<br/>") );
			$(container).append( $("<br/>") );
			
			fromDate = fromDate.addMonths( 1 );
		}
		
		if( !noOfMonth || noOfMonth == 0 ){
			
			$("#payDue").hide(); 
			$("#distributeReset").hide();
			$("#totalAmountDiv").hide();
			$("#totalAmount").text( "0" );
		}
		else{
			
			$("#payDue").show();
			$("#distributeReset").show();
			$("#totalAmountDiv").show();
		}
		
	}
	
	$("#clientIdStr").autocomplete({
	    source: function(request, response) {
		  $("#clientId").val(-1);
	       var term = request.term;
	       var url = '../../AutoComplete.do?need=client&moduleID='+$("#moduleID").val()+"&status=active";
	       var formData = {};
	       formData['name']=term;
	       callAjax(url, formData, response, "GET");
	    },
	    minLength: 1,
	    select: function(e, ui) {
	       
	    	$('#clientIdStr').val(ui.item.data);
	       	$('#clientId').val(ui.item.id);
	       
	       	totalBtclAmount = 0.0;
	       	totalVat = 0.0;
	       	totalPayable = 0.0;
	       	
	       	showBills( ui.item.id );
	       	return false;
	    },
	 }).autocomplete("instance")._renderItem = function(ul, item) {
	    return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	};
	
	
	function showBills( clientID ){
		
		var moduleID = $("#moduleID").val();
		
		var url = context + "/SplitBill.do";
		var param= {};
		param['method'] = "getUnpaidBills";
		param['clientID'] = clientID;
		param['moduleID'] = moduleID;
		
		callAjax( url, param, renderBill, "GET" );
		
		$("#duePaymentContainer").empty();
		$("#noOfMonths").val("");
		$("#payBillFrom").val("");
		$("#payDue").hide(); 
		$("#distributeReset").hide();
	}
	
	function makeBillIDColumn( data ){
		
		var td = $("<td/>");
		var a = $("<a/>");
		
		$(a).attr( "href", context + "/GetPdfBill.do?method=getPdf&id=" + data['ID'] );
		$(a).attr( "target", "_blank" );
		$(a).text( data['ID'] );
		
		$(td).append( a );
		
		return td;
	}
	
	function makeBillMonthColumn( data ){
		
		var td = $("<td/>");
		
		if( data['activationTimeFrom'] > 0 ){
			var date = new Date( data['activationTimeFrom'] );
			$(td).text( date.getDate() + " " + monthNames[date.getMonth()] + ", " + date.getFullYear() );
		}
		else
			$(td).text( "N/A" );
		
		return td;
	}
	
	function makeEntityNameColumn( data ){
		
		var td = $("<td/>");
		$(td).text( data['entityName'] );
		
		return td;
	}
	
	function makeTotalPayableColumn( data ){
		
		var td = $("<td/>");
		$(td).text( Math.round( data['totalPayable'] * 100 )/100 );
		
		totalBtclAmount += data['totalPayable'];
		return td;
	}
	
	function makeVatColumn( data ){
		
		var td = $("<td/>");
		$(td).text( Math.round( data['VAT'] * 100 )/100 );
		
		totalVat += data['VAT'];
		return td;
	}
	
	function makePayableAmount( data ){
		
		var td = $("<td/>");
		$(td).text( Math.round( data['VAT'] + data['totalPayable'] * 100 )/100 );
		
		totalPayable += data['VAT'] + data['totalPayable'];
		return td;
	}
	
	function renderBill( data ){
		
		if( data['responseCode'] == 1 ){
		
			$("#billTableBody").empty();
			
			for( var i = 0; i < data['payload'].length; i++ ){
				
				var tr = $("<tr/>");
				
				var td = makeBillIDColumn( data['payload'][i] );
				$(tr).append( td );
				
				td = makeBillMonthColumn( data['payload'][i] );
				$(tr).append( td );
				
				td = makeEntityNameColumn( data['payload'][i] );
				$(tr).append( td );
				
				td = makeTotalPayableColumn( data['payload'][i] );
				$(tr).append( td );
				
				td = makeVatColumn( data['payload'][i] );
				$(tr).append( td );
				
				td = makePayableAmount( data['payload'][i] );
				$(tr).append( td );
				
				$("#billTableBody").append( tr );
			}
			
			if( data['payload'].length == 0 ){
				
				var td = $("<td/>");
				var tr = $("<tr/>");
				
				$(td).attr( "colspan", "center" );
				$(td).attr( "colspan", 6 );
				$(td).html( "<h3>No pending bill found</h3>");
				
				$(tr).append( td );
				$("#billTableBody").append( tr );
				
			}
			else{
				
				totalBtclAmount = Math.round( totalBtclAmount );
				totalVat = Math.round( totalVat );
				totalPayable = Math.round( totalPayable );
				
				var td = $("<td/>");
				var tr = $("<tr/>");
				
				$(td).html( "<b>Total</b>" );
				$(td).attr( "colspan", 3 );
				$(tr).append( td );
				
				td = $("<td/>");
				$(td).html( "<b>" + totalBtclAmount + "</b>" );
				$(tr).append( td );
				
				td = $("<td/>");
				$(td).html( "<b>" + totalVat + "</b>" );
				$(tr).append( td );
				
				td = $("<td/>");
				$(td).html( "<b>" + totalPayable + "</b>" );
				$(tr).append( td );
				
				$("#billTableBody").append( tr );	
			}
			
			$("#payDue").hide();
			$("#distributeReset").hide();
			$("#totalAmountDiv").hide();
			$("#totalAmount").text( "0" );
			
		}
		else if( data['responseCode'] == 2 ){
			toastr.error( data['msg'] );
		}
	}
	
});
	
