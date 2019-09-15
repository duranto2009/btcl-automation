<script>

function isInt(value){
	
	return !isNaN(value) && ( 
  			function(x) { 
				return (x | 0) === x; 
			}
  		)(parseFloat(value));	
}
function validateRawData(data){
	if(!isInt(data)){
		toastr.error("Please insert an integer number");
		return false;
	}
	var x = parseInt(data);
	if(x < 0 || x > 256){
		toastr.error("Please insert an integer ranging from 0-256");
		return false;
	}
	return true;
}
$(document).ready(function(){
	$("#fileupload").on('submit', function(){
		selectAllOptions();	
	});
	function selectAllOptions() {
		var form = document.forms[0]; 
		var select1 = form[name="mandatoryIpAvailableBlockID"];
		if(select1)
		{
	  	var len1 = select1.length;
	   	for(i = 0; i < len1; i++) {
	      	select1.options[i].selected = true;
	   	}
		}
	  	var select2 = form[name="additionalIpAvailableBlockID"];
	  	if(select2)
	  	{
	  	var len2 = select2.length;
	  	for(i = 0; i < len2; i++) {
	     	select2.options[i].selected = true;
	   	}
	  	}
	   	return true;
	}
	$('.btn-ip-add').on('click', function () {
		var select,selectCount, startingInput, endingInput, startingIP, endingIP, ipFromTo, dataID;
		dataID = $(this).attr('data-id');
		
		
		startingInput = $('input[name=starting-ip-'+dataID+ ']');
		endingInput = $('input[name=ending-ip-' + dataID+']');
		startingIP = $(startingInput).val();
		endingIP = $(endingInput).val();
		
		ipFromTo = startingIP + "-" + endingIP;
		
		
		if(dataID === 'essential'){
			select = 'mandatoryIpAvailableBlockID';
			selectCount = 'mandatoryIpAvailableCount';
		}else if(dataID === 'additional'){
			select = 'additionalIpAvailableBlockID';	
			selectCount = 'additionalIpAvailableCount';
		}
		$('select[name="'+select+'"]').append($('<option>', {
		    value: ipFromTo,
			text: ipFromTo
			
		}) );
		
			
		
		$(startingInput).val('');
		$(endingInput).val('');	
		
		
		return false;
	});
	
	$('.btn-ip-remove').on('click', function (){
		var select, dataID, selectedIdx, form;
		dataID = $(this).attr('data-id');
		form = document.forms[0];
		if(dataID === 'essential') {
			select = 'mandatoryIpAvailableBlockID';
		}else if(dataID === 'additional'){
			select = 'additionalIpAvailableBlockID';
		}
		selectedIdx = form[name=select].selectedIndex;
		if(selectedIdx != -1 ) {
			form[name=select].options.remove(selectedIdx);
		}
		return false;
	});
	
	$("input[name=mandatoryIpAvailableCount]").on('focusout',function(){
		var rawData = $("input[name=mandatoryIpAvailableCount]").val();
		if(validateRawData(rawData)){
			if($("input[name=mandatoryIpAvailableCount]").val()=='0'){
				$("#mandatoryIpUnavailabilityReason").show();
				$("#mandatoryIpAvailableBlockID").hide();
				$('.mandatoryIpAvailableTR').hide();
			}else{
				$("#mandatoryIpUnavailabilityReason").hide();
				$("#mandatoryIpAvailableBlockID").show();
				$('.mandatoryIpAvailableTR').show();
			}	
		}
		
		
	});	
	if ('${lliLink.additionalIPCount}' != '0'){
		$("#additionalIpAvailableCount").val(0);
		$("#additionalIpAvailableCount").show();
		$("#additionalIpUnavailabilityReason").show();
		

		$("select[name=additionalIpAvailableCount]").change(function(){
			if($("select[name=additionalIpAvailableCount]").val()=='0'){
				$("#additionalIpUnavailabilityReason").show();
				$("#additionalIpAvailableBlockID").hide();
				$(".additionalIpAvailableTR").hide();
			}else{
				if($("select[name='mandatoryIpAvailableBlockID'] option").length === 0){
					toastr.error("Provide Mandatory IP Blocks First");
					$(this).val('0');
				}else{
					$("#additionalIpUnavailabilityReason").hide();
					$("#additionalIpAvailableBlockID").show();
					$(".additionalIpAvailableTR").show();
				}
			}
		});
		
		$("#mandatoryIpAvailableBlockID td select").change(function(){
			$("select[name=additionalIpAvailableCount]").trigger("change");
		});
	}else{
		$("#isAdditionalEmpty").val(0);
	}
	
    <%if(lliFRResponseInternalDTO != null && lliFRResponseInternalDTO.isBandWidthIsAvailable()){%>
		$('#bandWidthComment').hide();
	<%}%>
});
</script>