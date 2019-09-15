<div class="form-group">
	<label for="colocationClientIDName" class="control-label col-md-4 col-sm-4 col-xs-4">Client Name</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<input type="text" class="form-control"	id="colocationClientIDName"> 
		<input type="hidden" class="form-control" name="colocationClientID">
	</div>
</div>

<script>
$(document).ready(function(){
	$("#colocationClientIDName").autocomplete({
	      source: function(request, response) {
		 		$("input[name='colocationClientID']").val(-1);
	         	var term = request.term;
	         	var url = '../../AutoComplete.do?need=client&moduleID='+CONFIG.get('module','colocation')+"&status=active";
	         	var formData = {};
	         	formData['name']=term;
	         	callAjax(url, formData, response, "GET");
	      },
	      minLength: 1,
	      select: function(e, ui) {
	         $('#colocationClientIDName').removeAttr("value");
	         $("input[name='colocationClientID']").val(ui.item.id);
	         return false;
	      },
	   }).autocomplete("instance")._renderItem = function(ul, item) {
	      return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	   };
});
</script>