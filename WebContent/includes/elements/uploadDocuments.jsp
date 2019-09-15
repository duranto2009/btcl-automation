<%@page import="file.FileTypeConstants"%>
<%
String fileRequestID=(String)request.getAttribute("fileRequestID");
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
	<div class="row">
		<div class="col-md-12">
		 	<div  class="col-md-3" style="padding: 0px;">
				<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
				<span class="btn btn-warning-btcl  fileinput-button">
					<i class="fa fa-upload"></i>
					<span> Add Documents </span> 
					<input class="jFile" type="file" name="<%=fileRequestID %>" >
				</span>
			</div>
			<div class="col-md-9">
				 <!-- The global file processing state -->
				 <span class="fileupload-process"></span>
		   		  <!-- The global progress state -->
		          <div class="col-lg-12 fileupload-progress fade">
		              <!-- The global progress bar -->
		              <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
		                  <div class="progress-bar progress-bar-success" style="width:0%;"></div>
		              </div>
		              <!-- The extended global progress state -->
		              <div class="progress-extended">&nbsp;</div>
		          </div>
			</div>
			<!-- The table listing the files available for upload/download -->
			<table role="presentation" class="table table-striped margin-top-10">
				<tbody class="files"></tbody>
			</table>
		</div>
		
</div>
<!-- /.box-body -->
<jsp:include page="../../common/ajaxfileUploadTemplate.jsp" />
<jsp:include page="../../common/fileUploadHelper.jsp" />
<script type="text/javascript">

$(document).ready(function(){
    var initFileupload<%=fileRequestID%> = function () {
		var parentWrapper='#fileupload<%=fileRequestID%>';
    	var $fileupload=$('#fileupload<%=fileRequestID%>');
    	 // Upload server status check for browsers with CORS support:
        if ($.support.cors) {
            $.ajax({
                url: baseUrl+'JqueryFileUpload',
                type: 'HEAD'
            }).fail(function () {
                $('<span class="alert alert-error"/>')
                    .text('Upload server currently unavailable - ' +
                    new Date())
                    .appendTo(parentWrapper);
            });
        };
        
    	$fileupload.fileupload({
            // Uncomment the following to send cross-domain cookies:
            // xhrFields: {withCredentials: true},
            url: baseUrl+'JqueryFileUpload',
            autoUpload: true,
            previewMaxWidth: 150,
            previewMaxHeight: 150
        }).bind('fileuploaddone', function(e, data) {
            console.log('fileuploaddone'+ data.result);
            $(parentWrapper+' .files').find('tr.template-upload').addClass('in');
            $.each(data.result, function (index, file) {
                console.log(file[0]);
                $('<input id="'+file[0]["modified_name"]+'" type="hidden" name="documents" value="'+file[0]["modified_name"]+'"/>').appendTo(parentWrapper);
            });
          }).bind('fileuploadfail', function(e, data) {
              console.log("fileuploadfail");
              $(parentWrapper+' .files').find('tr.template-upload').addClass('in');
          }).bind('fileuploadcompleted', function (e, data) {
            	console.log("fileuploadcompleted");
            	$(parentWrapper+' .files').find('tr.template-upload').addClass('in');
          })
          .bind('fileuploadfailed', function (e, data) {
              console.log("fileuploadfailed");
              $(parentWrapper+' .files').find('tr.template-upload').addClass('in');
          }).bind('fileuploadfinished', function (e, data) {
              $(parentWrapper+ ' .files').find('tr.template-upload').addClass('in');;
              console.log("fileuploadfinished");})
          .bind('fileuploadstarted', function (e) {console.log("fileuploadstarted");})
          .bind('fileuploadstopped', function (e) {console.log("fileuploadstopped");});
       
        $('.files').on('click', 'td button.delete, td span.delete-from-list', function (e) {
      	  e.preventDefault();
      	  var $link = $(this);
      	  // alert('deleted');
      	  console.log("ID to remove: "+$link.data('id'));
      	  $('input[id="'+$link.data('id')+'"]').remove();
      	  $link.closest('tr').remove();
      	 
      });
    }
    
    initFileupload<%=fileRequestID%>();
})
</script>


						