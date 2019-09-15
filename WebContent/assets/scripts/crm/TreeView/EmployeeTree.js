function EmployeeTree() {
	
	var divID;
	var modalID;
	var formID;
	var context;
	var treeData = [];
	var treeBuilder;
	var isAdmin;
	var thisRef = this;
	
	this.setIsAdmin = function( admin ){
		
		isAdmin = admin;
	}
	
	this.setTreeBuilder = function( tb ){
		
		treeBuilder = tb;
	}
	
	this.setContext = function( c ){
		
		context = c;
	}
	
	this.getDiv = function(){
		
		return divID;
	}
	
	this.init = function( dID, mID, fID ){
		
		divID = dID;
		modalID = mID;
		formID = fID;
	}
	
	this.log = function(){
		
	}
	
	this.getTreeData = function(){
		
		return treeData;
	}
	
	this.populateData = function( root ){
		
		dfsEmployee( root );
	}
	
	this.draw = function(){
		
		$( '#' + divID ).jstree({ 
			"plugins" : [ "wholerow" ],
			'core' : {
				'data' : treeData
			} 
		})
		.bind("loaded.jstree", function(event, data){
			$(this).jstree("open_all");
		});
		
		$('#' + divID ).on("ready.jstree", function( event,data ){
			
			if( isAdmin == "false" ){
				$(".employee-add").hide();
				$(".employee-remove").hide();
			}
		});
	}
	
	
	this.employeeAddHandler = function( ev, departmentID ){
		
		var checkboxes = $("#employeeAddForm").find('input:checkbox');
	    
	    for( var i =0; i<checkboxes.length; i++ ){
	    	
	    	checkboxes[i].checked = false;
			var span = $(checkboxes[i]).parent("span")[0];
			$(span).removeClass( 'checked' );
	    }
	    
		$("#employeeAddForm")[0].reset();
		
		$("#parentID").val( ev.target.getAttribute('data-id') );
		$("#parentDesignationCategoryTypeID").val( ev.target.getAttribute( 'data-designation-category-id' ) );
		$("#divID").val( divID );
		
		$("#editForm").val("false");
		
		var url = context + "/CrmDesignation/get_child_designation_list.do";
		var param = {};
		param.departmentID = departmentID;
		param['parentDesignationCategoryTypeID'] = $("#parentDesignationCategoryTypeID").val();
		
		callAjax( url, param, populateDesignationForEmployeeModal, "GET" );
	}
	
	this.employeeEditHandler = function( ev ){
		$("#employeeAddForm")[0].reset();
		
		if( ev.target.getAttribute('data-parent-id') == '#' )
			$("#parentID").val( "" );
		else
			$("#parentID").val( ev.target.getAttribute('data-parent-id') );
		
		$("#parentDesignationCategoryTypeID").val( ev.target.getAttribute( 'data-designation-category-id' ) );
		$("#divID").val( divID );
		
		var url = context + "/CrmEmployee/getEmployeeByID.do"
		
		var param = {};
		param['crmEmployeeID'] = ev.target.getAttribute('data-id');
		
		callAjax( url, param, employeeGetCallBack, "GET" );
		
		$( "#"+modalID ).modal("show");
	}
	
	employeeGetCallBack = function( data ){
		if( data['responseCode'] == 1 ){
			
			var url = context + "/CrmDesignation/getDesignation.do";
			var param = {};
			param['crmInventoryCategoryTypeID'] = data['payload']['inventoryCatagoryTypeID'];
			
			thisRef.callAjaxNotAsync( url, param, thisRef.drawEmployeePermissionFromDesignation, "GET" );
			
			populateModalFromEmployeeDTO( data['payload'] );
		}
		else{
			
			toastr.error( data['msg'] );
		}
	}
	
	this.drawEmployeePermissionFromDesignation = function( data ){
		if( data['responseCode'] == 1 ){
			
			var responseKeys = Object.keys( data['payload'] );
			
			var permissionMap = {};
			
			for( var i = 0; i<responseKeys.length; i++ ){
				
				if( responseKeys[i].startsWith( "hasPermissionTo" ) && data['payload'][ responseKeys[i] ] == true && permissions.indexOf( responseKeys[i] ) != -1 ){
					
					permissionMap[ responseKeys[i] ] = data['payload'][ responseKeys[i] ];
				}
			}
			
			//This method is called from Tree Builder
			drawPermission( permissionMap, "permission-container-employee" );
			
		}
		else if( data['responseCode'] == 2 ){
			
			toastr.error( data['msg'] );
		}
	}
	
	usernameGetCallback = function( data ){
		
		if( data['responseCode'] == 1 ){
			
			$("#username").val( data['payload'] );
		}
		else{
			
			toastr.error("Username not found");
		}
	}
	
	populateModalFromEmployeeDTO = function( data ){
		
		$("#designationTypeID").html( "" );
		
		var designationText = treeBuilder.getDesignationName( data['inventoryCatagoryTypeID'] );
		
		var optionStr = "<option value='" + data['inventoryCatagoryTypeID'] + "'>" + designationText + "</option>";
		$("#designationTypeID").append( optionStr );
		
		$("#userName").val( data['name'] );
		$("#crmEmployeeID").val( data['crmEmployeeID'] );
		$("#ID").val( data['ID'] );
		$("#userID").val( data['userID'] );
		
		var url = context + "/CrmEmployee/GetUsernameFromID.do"
		var param = {};
		param['id'] = data['userID'];
		
		callAjax( url, param, usernameGetCallback, "GET" );
		
		var keys = Object.keys( data );
		
		for( var i = 0; i<keys.length; i++ ){
			
			if( keys[i].startsWith( "hasPermissionTo") && data[ keys[i] ] == true ){
				
				document.getElementById( keys[i] ).checked = true;
				
				var span = $( keys[i] + "_des" ).parent("span")[0];
				$(span).addClass( 'checked' );
			}
		}
	}

	this.refresh = function( data ){
		
		if( data['responseCode'] == 1 ){
		        
	        var temp = {
	        	"id" : data['payload']['ID'],
	        	"parent": data['payload']['parentID'],
	        	"text" : data['payload']['name']  + ' <i class="fa fa-plus-circle fa-lg employee-add" data-id="' + data['payload']['ID'] 
	        			+ '" data-designation-category-id="' + data['payload']['inventoryCatagoryTypeID'] + '"></i>'
	        			+ ' <i class="fa fa-remove fa-lg employee-remove" data-div-id="' + divID + '" data-id="' + data['payload']['crmEmployeeID'] + '"></i>'
						+ ' <i class="fa fa-pencil-square-o fa-lg  employee-edit" data-parent-id="' + data['payload']['parentID'] + '" data-id="' + data['payload']['crmEmployeeID']
						+ '" data-designation-category-id="' + data['payload']['inventoryCatagoryTypeID'] + '"></i>',
	        	"employee-id": data['payload']['crmEmployeeID']
	        }
	        treeData.push( temp );
	        
	        $( "#" + divID ).jstree( true ).settings.core.data = treeData;
	        
	        $( "#" + divID ).jstree( true ).refresh();
	        
	        $( "#" + modalID ).modal( "hide" );
	        
	        toastr.success( data['payload']['name'] + " added successfully" );
		}
		else if( data['responseCode'] == 2 ){
			
			$("#employeeAddModal").modal("hide");
			toastr.error( data['msg'] );
		}
	}
	
	this.refreshRemove = function( data ){
		
		if( data['responseCode'] == 1 ){
			
			for(var i = 0; i < treeData.length; i++){
				
				if( treeData[i]['employee-id'] == data['payload'] ){
					
					treeData.splice(i,1);
					break;
				}
			}
			
			$( "#" + divID ).jstree( true ).settings.core.data = treeData;
	        
	        $( "#" + divID ).jstree( true ).refresh();	
	        
	        
	        
	        
	        
	        if( treeData.length == 0 ){ 
				$("#" + divID ).parent( "div" ).remove();
				setTimeout(function(){
					location.reload(true);
				}, 1000);
	        }
	        toastr.success( "Employee removed successfully" );
		}
		else{
			
			toastr.error( data['msg'] );
		}
	}
	
	this.refreshUpdate = function( data ){
		
		if( data['responseCode'] == 1 ){
			
			$("#" + modalID ).modal("hide");
			
			for(var i = 0; i<treeData.length; i++){
				
				if( treeData[i]['employee-id'] == data['payload']['crmEmployeeID'] ){
					
					var index = treeData[i]['text'].indexOf( "<i " );
					var substring = treeData[i]['text'].substring( index, treeData[i]['text'].length );
					
					treeData[i]['text'] = data['payload']['name'] + " " + substring;
					break;
				}
			}
			
			$( "#" + divID ).jstree( true ).settings.core.data = treeData;
			$( "#" + divID ).jstree( true ).refresh();
			
			toastr.success( data['payload']['name'] + " updated successfully" );
		}
		else{
			
			toastr.error( data['msg'] );
			$( "#" + modalID ).modal("hide");
		}
	}
	
	populateDesignationForEmployeeModal = function( data ){
		
		if( data['responseCode'] == 1 ){
		
			$("#designationTypeID").html( "" );
			
			$("#designationTypeID").append( "<option value=''> ---Select Designation--- </option>" );
			
			for( var i=0; i < data['payload'].length; i++ ){
				
				var optionStr = "<option value='" + data['payload'][i]['ID'] + "'>" + data['payload'][i]['name'] + "</option>";
				$("#designationTypeID").append( optionStr );
			}
			
			if( data['payload'].length == 0 ){
				
				toastr.error( "No Designation exists under this employee" );
			}
			else{
				
				drawPermission( {}, "permission-container-employee" );
				$( "#" + modalID ).modal("show");
			}
		}
		else{
			
			toastr.error( data['msg'] );
		}
	}
	
	dfsEmployee = function( root ){
		
		if( !root['parentID'] )
			root['parentID'] = "#";
		
		for(var i = 0; i < root['childEmployeeList'].length; i++ ){
			
			dfsEmployee( root['childEmployeeList'][i] );
		}
		
		if( root ){
			
			var temp = {
				"id" : root['ID'],
				"parent" : root['parentID'],
				"text" : root['name'] + ' <i class="fa fa-plus-circle fa-lg employee-add" data-id="' + root['ID'] 
						+ '" data-designation-category-id="' + root['inventoryCatagoryTypeID'] + '" ></i>'
						+ ' <i class="fa fa-remove fa-lg employee-remove" data-div-id="' + thisRef.getDiv() + '" data-id="' + root['crmEmployeeID'] + '"></i>'
						+ ' <i class="fa fa-pencil-square-o fa-lg  employee-edit" data-parent-id="' + root['parentID'] + '" data-id="' + root['crmEmployeeID']
						+ '" data-designation-category-id="' + root['inventoryCatagoryTypeID'] + '"></i>',
				"employee-id": root['crmEmployeeID']
			}
			
			treeData.push( temp );
		}
	}
	
	callAjax = function(url,param,callback,reqType){
		
		$.ajax({
			type : reqType,
			url : url,
			data : param,
			dataType : 'JSON',
			success : function(data) {
			    callback(data);
			},
			error : function(jqXHR, textStatus, errorThrown) {
			    /*toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
				    + ", Message: " + errorThrown);*/
				console.log("Error");
			},
			failure : function(response) {
			    /*toastr.error(response);*/
				console.log("failure");
			}
		});
	}
	
	this.callAjaxNotAsync = function(url,param,callback,reqType){
		
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
			    /*toastr.error("Error Code: " + jqXHR.status + ", Type:" + textStatus
				    + ", Message: " + errorThrown);*/
				console.log("Error");
			},
			failure : function(response) {
			    /*toastr.error(response);*/
				console.log("failure");
			}
		});
	}
}