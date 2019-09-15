
$(document).ready(function(){

 
    var initFileupload = function () {
    	var $fileupload=$('#fileupload');
    	 // Upload server status check for browsers with CORS support:
        if ($.support.cors) {
            $.ajax({
                url: baseUrl+'JqueryFileUpload',
                type: 'HEAD'
            }).fail(function () {
                $('<span class="alert alert-error"/>')
                    .text('Upload server currently unavailable - ' +
                    new Date())
                    .appendTo('#fileupload');
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
            $('.files').find('tr.template-upload').addClass('in');
            $.each(data.result, function (index, file) {
                console.log(file[0]);
                $('<input id="'+file[0]["modified_name"]+'" type="hidden" name="documents" value="'+file[0]["modified_name"]+'" data-documentType='+ file[0]["modified_name"].split("_")[0] +' />').appendTo('#fileupload');
            });
          }).bind('fileuploadfail', function(e, data) {
              console.log("fileuploadfail");
              $('.files').find('tr.template-upload').addClass('in');
          }).bind('fileuploadcompleted', function (e, data) {
            	console.log("fileuploadcompleted");
            	$('.files').find('tr.template-upload').addClass('in');
          })
          .bind('fileuploadfailed', function (e, data) {
              console.log("fileuploadfailed");
              $('.files').find('tr.template-upload').addClass('in');
          }).bind('fileuploadfinished', function (e, data) {
              $('.files').find('tr.template-upload').addClass('in');;
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
    
    initFileupload();
})

