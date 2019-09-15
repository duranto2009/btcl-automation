<%@page import="util.TimeFormat"%>

<link href="${context}assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet" type="text/css" />
<link href="${context}assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
<script src="${context}assets/global/plugins/moment.min.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var targetFormat = $('.datepicker').attr('data-format');
		var targetEndDate = $('.datepicker').attr('data-min-month');
		if (typeof targetFormat !== typeof undefined && targetFormat !== false) {
			$('.datepicker').datepicker({
	           	orientation: "top",
	            autoclose: true,
	            format: targetFormat,
// 	            startView : "months",
				viewMode:'months',
	            minViewMode : "months",
	            endDate : (typeof targetEndDate !== typeof undefined && targetEndDate !== false) ? targetEndDate : "+0m"
	        });    
		}else {
			$('.datepicker').datepicker({
	           	orientation: "top",
	            autoclose: true,
	            format: 'dd/mm/yyyy',
	            todayBtn: 'linked',
	            todayHighlight:	true
	        });
		}
	    
	})
</script>