<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="col-md-6 pull-right">
	<button type="submit" class="btn btn-info" id="previewButton">Preview and Download</button>
</div>

<script type="text/javascript">
	$(document).ready(function(){
		$("#previewButton").click(function(){
			 var url = context+'/common/note/externalReferPreview.jsp'; 
			 var formData = {};
			 formData=$('.action-list-form').serialize();
			 callAjaxHTML(url,formData, showFrInNewTab);
	         return false;
		})
		function showFrInNewTab(data){
			var newWindow = window.open('', '_blank', 'toolbar=0,location=0,menubar=0');//window.open();
			 //window.open('http://www.stackoverflow.com', '_blank', 'toolbar=yes, location=yes, status=yes, menubar=yes, scrollbars=yes');
			 
       	 	newWindow.document.write(data);
		}
	})
</script>