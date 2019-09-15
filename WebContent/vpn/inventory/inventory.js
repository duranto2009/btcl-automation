	$(document).ready(function(){
	    var counter = -100000;
	    var wrapper = $("#accordion");
	    /*
	     * Adds a root new category 
	     * 
	     */	
		 $("#addButton").on("click", function(e){ 
	    	e.preventDefault();
	    	var catgName = prompt("Please Add your category name");
			/*if(catgName == ''){
				catgName = 'Catg#'+counter;
			}*/
	    	
			if(catgName != null){
				AddCategory(-1, catgName);
				
				/*var ariaExpanded = false;
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
				  
				counter++;*/
			}
			
	     });
		 /*
		  * Adds a child new category
		  * 
		  */
		var x = 1; 
	     $(wrapper).on("click","#addButton2", function(e){
	         e.preventDefault();
			 var parentId = $(this).attr('accesskey');
			 var parentPanel = '#panel'+ parentId;
			 var catgName = prompt("Please Add your category name");
			 /*if(catgName == ''){
				catgName = ' P#'+parentId+' Catg#'+counter;
			 }*/
			 
			if(catgName != null){
				AddCategory(parentId, catgName); 
				/*var ariaExpanded = false;
				var expandedClass = '';
				var collapsedClass = 'collapsed';*/
			
				  
				
				  
				//x++;
				//counter++;
			}
			
	     });
		 
	     $(wrapper).on("click",".remove_ctg_panel", function(e){ 
				 e.preventDefault(); 
				 var accesskey = $(this).attr('accesskey');
				removeCategory(accesskey);
		        //$('#panel'+accesskey).remove();		        
				//counter--;
				//x--;
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
	     
	 	function initCallback(response){
			 document.getElementById("accordion").innerHTML=response;	       
		}
		var url = "innerContent.jsp";
		callAjaxHTML(url,{},initCallback);
  });

	function commonResponse(){
		toastr.success("Successful");
	}
	function updateAttributes(elem)
	{
		//alert("#TextBoxDiv"+elem.accessKey);
		
		/* if(elem.parentElement.parentElement.id.equals('TextBoxDiv' + elem.accessKey)
		{
			alert('found');
		} */
		//elem.parent().parent("#TextBoxDiv"+elem.accessKey);
		//alert($(this).find("#TextBoxDiv"+elem.accessKey).id);
		var parentElem = document.getElementById('TextBoxDiv'+elem.accessKey);
		var attributeList=[];
		if(typeof parentElem != 'undefined')
		{
			//alert(parentElem.id);
			/* $('#TextBoxDiv'+elem.accessKey+' > input[name=]' ).each(function(){
				
			}); */
			var childrenArray = $(parentElem).children();
			for(var i = 1; i < childrenArray.size();i++)
			{
				//alert($(childrenArray[i]).children()[0].value);
				attributeList.push($(childrenArray[i]).children()[0].value);
			}
			//var chld = $(parentElem).find('input[name="ctgtext[]"]');
			//alert(chld.className);
		}
		updateAttributesAjax(elem.accessKey, attributeList);
		
	}
	
	/*window.onload = function fetchInnerHtml()
	{
		  xmlHttp=GetXmlHttpObject();
			  if (xmlHttp==null)
			  {
			     alert ("Your browser does not support AJAX!");
			     return false;
			  }      
			var url = "innerContent.jsp";
			//url=url+"?destinationCode="+text+"&count="+getdropDownVisibleItemsNo();
			xmlHttp.onreadystatechange = contentLoaded;
			xmlHttp.open("GET", url, true);
			xmlHttp.send(null);
			//document.getElementById("response_code").innerHTML="";
			return true;
	}

	function contentLoaded()
	{
		   if (xmlHttp.readyState==4)
		   {
			  // alert(xmlHttp.responseText);
		       document.getElementById("accordion").innerHTML=xmlHttp.responseText;	       
		     
		    }
	}*/
	
	function updateAttributesAjax(catagoryID, attributeArray)
	{
		$.ajax({
	        type : 'post',
	        url : 'updateInventoryConfig.jsp',
	        data : {
	        	type: 2,
	        	catagoryID : catagoryID,
	         attributeList : attributeArray         
	        },
	        success : function(response) {
	        	commonResponse();
	        	console.log(response);
        	}
       });
	}
	
	function AddCategory(parentID, childCatName)
	{
		$.ajax({
	        type : 'post',
	        url : 'updateInventoryConfig.jsp',
	        data : {
	        	type: 1,
	        	parentID : parentID,
	        	childCatName : childCatName         
	        },
	        success : function(response) {
	        	commonResponse();
	        	showAddedCategory(response, parentID, childCatName)
	    
	        }
       });
	}
	
	function removeCategory(catID)
	{
		$.ajax({
	        type : 'post',
	        url : 'updateInventoryConfig.jsp',
	        data : {
	        	type: 10,
	        	catID : catID	        	        
	        },
	        success : function(response) {	        
	        	commonResponse();
	        	showDeletedCategory(response)	    
	        }
       });
	}
	
	function showAddedCategory(response, parentID, childCatName)
	{
		console.log('response ' + response);
		var counter = response;
		var catgName = childCatName;
		
		var parentPanel = '#panel'+ parentID;
		var wrapper = $("#accordion");
		var collapsedClass = 'collapsed';
		var ariaExpanded = false;
		var expandedClass = '';
		/*if(counter==1){
			  ariaExpanded = true;
			  expandedClass = 'in';
			  collapsedClass = '';
		}*/
		if(parentID <= 0)
		{
			ariaExpanded = true;
			expandedClass = 'in';
			collapsedClass = '';			
		}
		var canvas;
		if(parentID > 0) 
			canvas = $(wrapper).find(parentPanel);
		else
			canvas = $(wrapper);
		
		canvas.append('<div class="col-sm-12" style="margin-bottom: 0;"><div class="panel panel-default" id="panel'+counter+'">' + 
			     '<div class="panel-heading" role="tab" id="heading'+counter+'"><h4 class="panel-title">' +
				 '<a class="'+collapsedClass+'" id="panel-lebel'+ counter +'" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse'+ counter+'" ' +
				 'aria-expanded="'+ariaExpanded+'" aria-controls="collapse'+ counter+'"> '+catgName+' </a><div class="actions_div" style="position: relative; top: -26px;">' +
				 '<a href="#" accesskey="'+counter +'" class="remove_ctg_panel exit-btn pull-right"><span class="glyphicon glyphicon-remove"></span></a>' +
				 '<a href="#" accesskey="'+ counter +'" class="edit_ctg_label pull-right"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</a>' +
				 '<a href="#" accesskey="'+ counter +'" class="pull-right" id="addButton2"> <span class="glyphicon glyphicon-plus"></span>&nbsp; Add child category</a></h4></div>' +
				 '<div id="collapse'+ counter+'" class="panel-collapse collapse '+expandedClass+'"role="tabpanel" aria-labelledby="heading'+counter+'">'+
				 '<div class="panel-body"><div id="TextBoxDiv'+ counter +'"></div><a class="btn btn-xs btn-primary" accesskey="'+ counter +'" id="addButton3" ><span class="glyphicon glyphicon-plus"></span> Add New Attributes</a>' +
				 '<a class="btn btn-xs btn-success" accesskey="'+ counter +'" id="ajax_submit_button" >Done</a></div></div></div></div>');
	}
	
	function showDeletedCategory(response)
	{
		console.log('response ' + response);
		$('#panel'+response).remove();
	}
	