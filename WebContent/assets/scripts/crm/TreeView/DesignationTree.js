function DesignationTree() {
	
	var divID;
	var modalID;
	var formID;
	var context;
	var treeData = [];
	var isAdmin;
	var thisRef = this;
	
	this.setIsAdmin = function( admin ){
		
		isAdmin = admin;
	}

	this.init = function( dID, mID, fID ){
		
		divID = dID;
		modalID = mID;
		formID = fID;
	}
	
	this.setContext = function( c ){
		
		context = c;
	}
	
	this.getDiv = function(){
		
		return divID;
	}
	
	this.getTreeData = function(){
		
		return treeData;
	}

	this.populateData = function( root ){
		
		dfsDesignation( root );
	}
	this.populateDataExceptGlyphicon = function(root){
		dfsDesignationExceptGlyphicon(root);
	}

	//Draws the Designation tree on specified div set by setter method
	this.draw = function(){
		
		$( "#" + divID ).jstree({
			'plugins': ['wholerow'],
			'core' : {
				'data' : treeData
			}
		
		
		})
		
		.bind("loaded.jstree", function(event, data){
			$(this).jstree("open_all");
		});
		
		$( '#' + divID ).on("ready.jstree", function( event,data ){
			
			if( isAdmin == "false" ){
				$(".designation-add").hide();
				$(".designation-remove").hide();
			}
		});
	}
	dfsDesignationExceptGlyphicon = function (root){
		if( !root['parentCatagoryTypeID'] )
			root['parentCatagoryTypeID'] = "#";
		
		for( var i = 0; i < root['childEmployeeList'].length; i++ ){
			
			dfsDesignationExceptGlyphicon( root['childEmployeeList'][i] );
		}
		
		if( root ){
			
			var temp = {
					"id" : root['ID'],
					"parent" : root['parentCatagoryTypeID'],
					"text": root['name'],
					"designation-id" : root['designationTypeID']
			}
			
			treeData.push( temp );
		}
	}
	//DFS and make separate node of given nested Trees from API
	dfsDesignation = function( root ){
		if( !root['parentCatagoryTypeID'] )
			root['parentCatagoryTypeID'] = "#";
		
		for( var i = 0; i < root['childEmployeeList'].length; i++ ){
			
			dfsDesignation( root['childEmployeeList'][i] );
		}
		
		if( root ){
			
			var temp = {
					"id" : root['ID'],
					"parent" : root['parentCatagoryTypeID'],
					"text": root['name'] + ' <i class="fa fa-plus-circle fa-lg designation-add" data-id="' + root['ID'] + '"></i>'
					 		+' <i class="fa fa-remove fa-lg designation-remove" data-div-id="' + divID + '" data-designation-type-id="' + root['designationTypeID'] + '"></i>'
					 		+' <i class="fa fa-pencil-square-o fa-lg designation-edit" data-id="' + root['ID'] + '" data-designation-type-id="' + root['designationTypeID'] + '"></i>',
					 "designation-id" : root['designationTypeID']
			}
			
			treeData.push( temp );
		}
	}
	
	this.log = function(){
		
		console.log( "Designation Tree: " + divID + " " + modalID + " " + formID );
	}
	
	//This method handles addition of a new designation under another designation
	this.designationAddHandler = function( ev, deptID ){
		
		var checkboxes = $("#designationAddForm").find('input:checkbox');
	    
		//First 
	    for( var i =0; i<checkboxes.length; i++ ){
	    	
	    	checkboxes[i].checked = false;
			var span = $(checkboxes[i]).parent("span")[0];
			$(span).removeClass( 'checked' );
	    }
	    
		$("#designationAddForm")[0].reset();
		
		$("#parentCatagoryTypeID").val( ev.target.getAttribute('data-id') );
		$("#editForm_des").val( "false" );
		$("#divID_des").val( divID );
		
		var url = context + "/CrmDesignation/getDesignation.do";
		var param = {};
		param.departmentID = deptID;
		param['crmInventoryCategoryTypeID'] = $("#parentCatagoryTypeID").val();
		
		thisRef.callAjaxNotAsync( url, param, drawPermissionFromParentDesignation, "GET" );
			
		$( "#" + modalID ).modal("show");
	}
	
	this.designationEditHandler = function( ev ){
		$("#designationAddForm")[0].reset();
		$("#divID_des").val( divID );
		
		var url = context + "/CrmDesignation/getDesignation.do";
		var param = {};
		param['crmInventoryCategoryTypeID'] = ev.target.getAttribute( "data-id" );
		
		thisRef.callAjax( url, param, thisRef.getDesignationCallback, "GET" );
	}
	
	this.getDesignationCallback = function( data ){
		if( data['responseCode'] == 1 ){
			
			var url = context + "/CrmDesignation/getDesignation.do";
			var param = {};
			param['crmInventoryCategoryTypeID'] = data['payload']['parentCatagoryTypeID'];
			
			thisRef.callAjaxNotAsync( url, param, drawPermissionFromParentDesignation, "GET" );
			
			populateModalFromDesignationDTO( data['payload'] );
			
			$("#designationAddModal").modal("show");
		}
		else{
			
			toastr.error( data['msg'] );
		}
	}
	
	drawPermissionFromParentDesignation = function( data ){
		
		if( data['payload'] ){
			
			var responseKeys = Object.keys( data['payload'] );
			
			var permissionMap = {};
			
			for( var i = 0; i<responseKeys.length; i++ ){
				
				if( responseKeys[i].startsWith( "hasPermissionTo" ) && data['payload'][ responseKeys[i] ] == true && permissions.indexOf( responseKeys[i] ) != -1  ){
					
					permissionMap[ responseKeys[i] ] = data['payload'][ responseKeys[i] ];
				}
			}
			
			//This method is called from Tree Builder
			drawPermission( permissionMap, "permission-container-designation" );
			
		}
		else{
			
			//This method is called from Tree Builder
			drawAllPermissionForDesignationModal( "permission-container-designation" );
		}
		
	}
	
	this.refresh = function( data ){
		
		if( data['responseCode'] == 1 ){
			LOG("here");
			var temp = {
				"id" : data['payload']['ID'],
				"parent" : data['payload']['parentCatagoryTypeID'],
				"text": data['payload']['name'] + ' <i class="fa fa-plus-circle fa-lg designation-add" data-id="' + data['payload']['ID'] + '"></i>'
				 		+' <i class="fa fa-remove fa-lg designation-remove" data-div-id="' + divID + '" data-designation-type-id="' + data['payload']['designationTypeID'] + '"></i>'
				 		+' <i class="fa fa-pencil-square-o fa-lg designation-edit"  data-id="' + data['payload']['ID'] + '" data-designation-type-id="' + data['payload']['designationTypeID'] + '"></i>',
				"designation-id" : data['payload']['designationTypeID']
			}
			
			treeData.push( temp );
			
			$( "#" + divID ).jstree( true ).settings.core.data = treeData;
			
			$( "#" + divID ).jstree( true ).refresh();
			
			$( "#" + modalID ).modal( "hide" );
			
			toastr.success( "Designation added successfully" );
		}
		else if( data['responseCode'] == 2 ){
			
			$( "#" + modalID ).modal("hide");
			toastr.error( data['msg'] );
		}
	}
	
	this.refreshUpdate = function( data ){
		
		if( data['responseCode'] == 1 ){
			
			$( "#" + modalID ).modal( "hide" );
			
			for( var i =0; i<treeData.length; i++ ){
				
				if( treeData[i]['id'] == data['payload']['ID'] ){
					
					var index = treeData[i]['text'].indexOf( "<i " );
					var substring = treeData[i]['text'].substring( index, treeData[i]['text'].length );
					
					treeData[i]['text'] = data['payload']['name'] + " " + substring;
					
					break;
				}
			}
			
			$( "#" + divID ).jstree( true ).settings.core.data = treeData;
			$( "#" + divID ).jstree( true ).refresh();
			toastr.success( "Designation Updated successfully" );
		}
		else{
			
			toastr.error( data['msg'] );
		}
	}
	
	this.refreshRemove = function( data ){
		
		console.log("hey");
		if( data['responseCode'] == 1 ){
			
			for( var i =0; i<treeData.length; i++ ){
				
				if( treeData[i]['designation-id'] == data['payload'] ){
					
					treeData.splice(i,1);
					break;
				}
			}
			
			$( "#" + divID ).jstree( true ).settings.core.data = treeData;
			
			$( "#" + divID ).jstree( true ).refresh();
			toastr.success( data.msg);
			if( treeData.length == 0 ) {
				$("#" + divID ).parent( "div" ).remove();
				setTimeout(function(){
					location.reload(true);
				}, 1000);
			}
			
		}
		else{

			toastr.error( data['msg'] );
		}
	}
	
	
	populateModalFromDesignationDTO = function( data ){
		
		$("#designationTypeID_des").val( data['designationTypeID']);
		$("#parentCatagoryTypeID").val( data['ID']);
		$("#name").val( data['name'] );
		
		var keys = Object.keys( data );
		
		for( var i = 0; i<keys.length; i++ ){
			
			if( keys[i].startsWith( "hasPermissionTo") && data[ keys[i] ] == true && permissions.indexOf( keys[i] ) != -1 ){
				
				document.getElementById( keys[i] + "_des" ).checked = true;
				
				var span = $( keys[i] + "_des" ).parent("span")[0];
				$(span).addClass( 'checked' );
			}
		}
	}
	
	this.callAjax = function(url,param,callback,reqType){
		$("#loading").show();
		
		$.ajax({
			type : reqType,
			url : url,
			data : param,
			dataType : 'JSON',
			success : function(data) {
			    callback(data);
			},
			error : function(jqXHR, textStatus, errorThrown) {
			    toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
				    + ", Message: " + errorThrown);
			},
			failure : function(response) {
			    toastr.error(response);
			},
			complete: function(){
				
				$("#loading").hide();
			}
		});
	}
	
	this.callAjaxNotAsync = function(url,param,callback,reqType){
		
		$("#loading").show();
		
		$.ajax({
			type : reqType,
			url : url,
			data : param,
			dataType : 'JSON',
			async: false,
			success : function(data) {
			    callback(data);
			},
			error : function(jqXHR, textStatus, errorThrown) {
			    toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
				    + ", Message: " + errorThrown);
			},
			failure : function(response) {
			    toastr.error(response);
			},
			complete: function(){
				
				$("#loading").hide();
			}
		});
	}
}