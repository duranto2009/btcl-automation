<script type="text/javascript">

$(document).ready(function() {

	$('#backToLogin').click(function(){
		window.location=context+'domain/domainQueryForBuy/domainQueryForBuy.jsp';
	});
	$("form #checkTermsAndContinue").click(function(){
	    if($("#checkTerms:checked").val()){
	    	$('#checkedBuy').val(true);
			$('#termsModal').modal('hide');
			$("#submit_for_check").submit();
	    }else{
			toastr.error("Please read all the terms and conditions");
	    }
	    return false;
	})

	$('#termsModal').on('shown', function() {
        $(".modal-header").focus();
    })

});
</script>