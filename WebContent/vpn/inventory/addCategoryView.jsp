<script type="text/javascript">
	$(document).ready(function(){
	    var counter = 1;
	    var wrapper = $("#accordion");
	
		 $("#addButton").on("click", function(e){ 
	    	e.preventDefault();
	    	var catgName = prompt("Please Add your category name");
			if(catgName == ''){
				catgName = 'Catg#'+counter;
			}
			if(catgName != null){
				var ariaExpanded = false;
				var expandedClass = '';
				var collapsedClass = 'collapsed';
				if(counter==1){
					  ariaExpanded = true;
					  expandedClass = 'in';
					  collapsedClass = '';
				}
				  $(wrapper).append('<div class="col-sm-12" style="margin-bottom: 0;"><div class="panel panel-default" id="panel'+ counter +'">' + 
				     '<div class="panel-heading" role="tab" id="heading'+ counter +'"><h4 class="panel-title">' +
					 '<a class="'+collapsedClass+'" id="panel-lebel'+ counter +'" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse'+ counter +'" ' +
					 'aria-expanded="'+ariaExpanded+'" aria-controls="collapse'+ counter +'"> '+catgName+' </a><div class="actions_div" style="position: relative; top: -26px;">' +
					 '<a href="#" accesskey="'+ counter +'" class="remove_ctg_panel exit-btn pull-right"><span class="glyphicon glyphicon-remove"></span></a>' +
					 '<a href="#" accesskey="'+ counter +'" class="edit_ctg_label pull-right"><span class="glyphicon glyphicon-edit "></span>&nbsp;Edit</a>' +
					 '<a href="#" accesskey="'+ counter +'" class="pull-right" id="addButton2"> <span class="glyphicon glyphicon-plus"></span>&nbsp; Add child category</a></div></h4></div>' +
					 '<div id="collapse'+ counter +'" class="panel-collapse collapse '+expandedClass+'"role="tabpanel" aria-labelledby="heading'+ counter +'">'+
					 '<div class="panel-body"><div id="TextBoxDiv'+ counter +'"></div><a class="btn btn-xs btn-primary" accesskey="'+ counter +'" id="addButton3" ><span class="glyphicon glyphicon-plus"></span> Add New Attributes</a>' +
					 '<a class="btn btn-xs btn-success" accesskey="'+ counter +'" id="ajax_submit_button" >Done</a></div></div></div></div>');
				counter++;
			}
			
	     });
		 
		var x = 1; 
	     $(wrapper).on("click","#addButton2", function(e){
	         e.preventDefault();
			 var parentId = $(this).attr('accesskey');
			 var parentPanel = '#panel'+ parentId;
			 var catgName = prompt("Please Add your category name");
			 if(catgName == ''){
				catgName = ' P#'+parentId+' Catg#'+counter;
			 }
			if(catgName != null){
				var ariaExpanded = false;
				var expandedClass = '';
				var collapsedClass = 'collapsed';
			
				  $(wrapper).find(parentPanel).append('<div class="col-sm-12" style="margin-bottom: 0;"><div class="panel panel-default" id="panel'+counter+'">' + 
				     '<div class="panel-heading" role="tab" id="heading'+counter+'"><h4 class="panel-title">' +
					 '<a class="'+collapsedClass+'" id="panel-lebel'+ counter +'" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse'+ counter+'" ' +
					 'aria-expanded="'+ariaExpanded+'" aria-controls="collapse'+ counter+'"> '+catgName+' </a><div class="actions_div" style="position: relative; top: -26px;">' +
					 '<a href="#" accesskey="'+counter +'" class="remove_ctg_panel exit-btn pull-right"><span class="glyphicon glyphicon-remove"></span></a>' +
					 '<a href="#" accesskey="'+ counter +'" class="edit_ctg_label pull-right"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</a>' +
					 '<a href="#" accesskey="'+ counter +'" class="pull-right" id="addButton2"> <span class="glyphicon glyphicon-plus"></span>&nbsp; Add child category</a></h4></div>' +
					 '<div id="collapse'+ counter+'" class="panel-collapse collapse '+expandedClass+'"role="tabpanel" aria-labelledby="heading'+counter+'">'+
					 '<div class="panel-body"><div id="TextBoxDiv'+ counter +'"></div><a class="btn btn-xs btn-primary" accesskey="'+ counter +'" id="addButton3" ><span class="glyphicon glyphicon-plus"></span> Add New Attributes</a>' +
					 '<a class="btn btn-xs btn-success" accesskey="'+ counter +'" id="ajax_submit_button" >Done</a></div></div></div></div>');
				
				x++;
				counter++;
			}
			
	     });
		 
	     $(wrapper).on("click",".remove_ctg_panel", function(e){ 
				 e.preventDefault(); 
				 var accesskey = $(this).attr('accesskey');
		        $('#panel'+accesskey).remove();
				counter--;
				x--;
	     });
	     
		 
		 
		 
	     var y = 1; 
	     $(wrapper).on("click","#addButton3", function(e){
	         e.preventDefault();
			 var accesskey = $(this).attr('accesskey');
			 y++; 
			 $('#panel'+accesskey).find('#TextBoxDiv'+accesskey).append('<div class="col-md-12 form-group"><input type="text" name="ctgtext[]" class="form-control" style="width: 40%;float: left;"/><a href="#" class="remove_field exit-btn"><span class="glyphicon glyphicon-remove"></a></div>');
	        
	     });
	     
	     $(wrapper).on("click",".remove_field", function(e){
	         e.preventDefault(); 
	     	$(this).parent('div').remove();y--;
	     })
	  	
	     $(wrapper).on("click",".edit_ctg_label", function(e){ 
	    	 var panelId = $(this).attr('accesskey');
			 var catgName = prompt("Please Change your category name");
			 if(catgName == ''){
				   return false;
			 }
			 if(catgName != null){
				 $('#panel'+panelId).find("#panel-lebel"+panelId).html('').html(catgName);
			 }
				
			
     });
  });

</script>

<style>
#accordion .panel-heading {
	padding: 0;
}

#accordion .panel-title>a {
	display: block;
	padding: 0.4em 0.6em;
	outline: none;
	font-weight: bold;
	text-decoration: none;
}

#accordion .panel-title>a.accordion-toggle::before, #accordion a[data-toggle="collapse"]::before
	{
	content: "\e113";
	float: left;
	font-family: 'Glyphicons Halflings';
	margin-right: 1em;
}

#accordion .panel-title>a.accordion-toggle.collapsed::before, #accordion a.collapsed[data-toggle="collapse"]::before
	{
	content: "\e114";
}
#accordion.panel-group .panel{
	margin-top: 5px;
}
#accordion .actions_div > a {
     font-size: 14px;
    margin-right: 15px;
    padding: 1px 0 2px;
}
#accordion .actions_div > a.exit-btn {
 color: #d36b7e;
}
#accordion .remove_field.exit-btn{
 color: #d36b7e;
    float: left;
    font-size: 15px;
    margin-left: 7px;
    margin-top: 9px;
}
#ajax_submit_button{
 margin-left: 10px;
}
</style>

<div class="col-md-12">
	<!-- Horizontal Form -->
	<div class="box box-success">
		<div class="box-header with-border">
			<h2 class="box-title">Add Category</h2>
		</div>
		<!-- /.box-header -->
		<!-- form start -->
		<form class="form-horizontal">
			<div class="box-body">
				<div class="container">
					
						<div class="panel-group" id="accordion" role="tablist"
							aria-multiselectable="true">
							
							
						</div>

					
					<div class="col-md-12 text-center" style="margin-top:15px;">
						<button class="btn btn-primary" id="addButton" value=""><span class="glyphicon glyphicon-plus"></span> Add New Category</button>
					</div>
				</div>
				
			</div>
			<!-- /.box-body -->
<!-- 			<div class="box-footer">
				<div class="col-sm-offset-3 col-sm-6 text-center">
					<input type="submit" class="btn btn-success" value="Submit" style="padding:6px 40px;">
				</div>

			</div> -->
			<!-- /.box-footer -->
		</form>
	</div>
</div>

