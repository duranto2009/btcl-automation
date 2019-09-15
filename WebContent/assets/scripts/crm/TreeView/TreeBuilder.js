var TreeType = { "Employee":1, "Designation":2 };

function TreeBuilder() {
	var divID; //for manual purpose
	var EmployeeTrees = [];
	var DesignationTrees = [];
	var containerDiv;
	var containerDesignationDiv;
	var urlEmployee;
	var urlDesignation;
	var employeeModalID;
	var employyeFormID;
	var designationModalID;
	var designationFormID;
	var context;
	var tempDivID;
	var isAdmin;
	var departmentID;
	var thisRef = this;
	
	this.setDivID = function (id){
		divID = id;
	}
	this.getDivID = function(){
		return divID;
	}
	this.setDepartmentID = function (deptID){
		
		departmentID = deptID;
	}
	this.setIsAdmin = function( admin ){
		
		isAdmin = admin;
	}
	this.setContext = function( c ){
		context = c;
	}
	this.setDesignationModal = function( id ){
		
		designationModalID = id;
	}
	this.setDesignationForm = function( id ){
		
		designationFormID = id;
	}
	this.setEmployeeModal = function( id ){
		employeeModalID = id;
	}
	this.setEmployeeForm = function( id ){
		
		employeeFormID = id;
	}
	this.setContainerID = function( id ){
		containerDiv = id;
	}
	this.setContainerDesignationDiv = function( id ){
		containerDesignationDiv = id;
	}
	this.setAllEmployeeRootURL = function( url ){
		urlEmployee = url;
	}
	this.setAllDesignationRootURL = function( url ){
		urlDesignation = url;
	}
	this.init = function( treeType, divID, modalID, formID ){
		
		if( treeType == TreeType.Employee ){
			
			var employeeTree = new EmployeeTree();
			
			employeeTree.init( divID, modalID, formID );
			employeeTree.setContext( context );
			employeeTree.setTreeBuilder( thisRef );
			employeeTree.setIsAdmin( isAdmin );
			
			EmployeeTrees.push( employeeTree );
			
			return employeeTree;
		}
		else if( treeType == TreeType.Designation ){
			
			var designationTree = new DesignationTree();
			
			designationTree.init( divID, modalID, formID );
			designationTree.setContext( context );
			designationTree.setIsAdmin( isAdmin );
			
			DesignationTrees.push( designationTree );
			return designationTree;
		}
	}
	
	this.groupTree = function(){
		
		console.log("");
		var treeDivs = $(".jstree");
		
		for( var i = 0; i<treeDivs.length; i++ ){
			
			var designationDivID = treeDivs[i].id;
			
			if( designationDivID.indexOf( "designation" ) != -1 ){
				
				var designationID = designationDivID.split( "_" )[1];
				
				
				
				var designationContainerdiv = $("<div/>");
				
				designationContainerdiv[0].id = "designationContainer_" + designationID; //DesignationTrees.length;
				designationContainerdiv[0].style= "padding:10px;border:1px solid black; overflow:hidden; margin-bottom:15px; margin-right: 20px";
				
				var designationHeading = $("<h4/>");
				
				$(designationHeading).html( "Designation Tree" );
				$(treeDivs[i]).prepend( designationHeading );
				
				designationContainerdiv.append( treeDivs[i] );
				
				$( "#" + containerDiv ).append( designationContainerdiv );
				
				
				console.log( designationID );
				
				for( var j = 0; j<treeDivs.length; j++ ){
					
					var employeeDivID = treeDivs[j].id;
					
					if( employeeDivID.indexOf( "employee" ) != -1 ){
						
						var employeeDesignationID = employeeDivID.split( "_" )[2];
						
						if( employeeDesignationID == designationID ){
							
							var employeeContainerDiv = $("<div/>");
							
							
							var employeeHeading = $("<h4/>");
							
							employeeContainerDiv[0].style = "float:left; margin-right:20px; padding: 20px";
							
							$(employeeHeading).html( "Employee Tree" );
							$(employeeContainerDiv).append( employeeHeading );
							$(employeeContainerDiv).append( treeDivs[j] );
							
							designationContainerdiv.append( employeeContainerDiv );
						}
						//console.log( desgnaionID + "_" + treeDivs[j].id );
					}
				}
			}
		}
	}
	this.getDepartmentID = function(){
		return departmentID;
	}
	this.getEmployeeTrees = function(){
		
		return EmployeeTrees;
	}
	
	this.getDesignationTrees = function(){
		
		return DesignationTrees;
	}
	this.buildPreviewTree = function(){
		
		var formData = {};
		formData.departmentID = departmentID;
		var callback = initDesignationPreviewTree;
		var method = "GET";
		callAjax(urlDesignation, formData, callback, method);
	}
	initDesignationPreviewTree = function(data){
		var desTree = thisRef.init( TreeType.Designation, divID);
		desTree.populateDataExceptGlyphicon( data['payload'][0] );
		desTree.draw();
				
	}
	this.buildDesignationTrees = function(){
		
		var formData = {};
		var callback = initDesignationTrees;
		var method = "GET";
		
		callAjax( urlDesignation, formData, callback, method );
	}
	
	initDesignationTrees = function( data ){
		
		for( var i = 0; i<data['payload'].length; i++ ){
			
			var dID = createDesignationDiv( data['payload'][i]['ID'],data['payload'][i]['departmentName'] );
			var desTree = thisRef.init( TreeType.Designation, dID, designationModalID, designationFormID );
			desTree.populateData( data['payload'][i] );
			desTree.draw();
		}
	}
	
	this.buildEmployeeTrees = function(){
		console.log(departmentID);
		var formData = {};
		var callback = initEmployeeTrees;
		var method = "GET";
		callAjax( urlEmployee, formData, callback, method );
	}
	
	initEmployeeTrees = function( data ){
		
		for( var i=0; i<data['payload'].length; i++ ){
			
			var dID = createDiv( data['payload'][i]['ID'] + "_" + data['payload'][i]['inventoryCatagoryTypeID'], data['payload'][i]['departmentName'] );
			var empTree = thisRef.init( TreeType.Employee, dID, employeeModalID, employeeFormID );
			empTree.populateData( data['payload'][i] );
			empTree.draw();
		}
	}
	
	this.createNewEmployeeTree = function(){
		
		$("#employeeAddForm")[0].reset();
		$("#parentID").val("");
		
		$("#editForm").val("false");
		
		var url = context + "/CrmDesignation/get_child_designation_list.do";
		var param = {};
		param.departmentID = departmentID;
	
		thisRef.callAjax( url, param, thisRef.populateDesignationForEmployeeModal, "GET" );

		drawPermission( {}, "permission-container-employee" );
		
		$( "#employeeAddModal" ).modal("show");
	}
	
	drawPermissionFromDesignation = function( data ){
		
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
	}

	$(document).on( "change", "#designationTypeID", function( e ){
		
		var url = context + "/CrmDesignation/getDesignation.do";
		var param = {};
		param['crmInventoryCategoryTypeID'] = $(e.target).val();
		
		callAjax( url, param, drawPermissionFromDesignation, "GET" );
	});
	
	this.populateDesignationForEmployeeModal = function( data ){
		
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
				
				$( "#" + modalID ).modal("show");
			}
		}
		else{
			
			toastr.error( data['msg'] );
		}
	}
	
	drawPermission = function( permissionMap, permissionContainer ){
		
		$( "#" + permissionContainer ).html("");
		
		var permissionKeys = Object.keys( permissionMap );
		
		for( var i = 0; i < permissionKeys.length; i++ ){
			
			var form_group = $("<div/>");
			
			$(form_group[0]).addClass( "form-group permission");
			
			var label = $("<label/>");
			
			$(label[0]).addClass( "col-sm-5 control-label" );
			$(label[0]).attr('for', permissionKeys[i] );
			
			$(label[0]).html( permissionLabel[ permissionKeys[i] ] );
			
			var inputDiv = $("<div/>");
			
			$(inputDiv[0]).addClass( "col-sm-6" );
			
			var input = $("<input/>");
			
			$(input[0]).addClass( "simple-input regi" );
			$(input[0]).attr( "type", "checkbox" );
			$(input[0]).attr( "name", permissionKeys[i] );
			
			if( permissionContainer.indexOf( "designation") != -1 )
				$(input[0]).attr( "id", permissionKeys[i] + "_des" );
			else
				$(input[0]).attr( "id", permissionKeys[i] );
			
			var clearfix = $("<div/>")
			
			$(clearfix[0]).addClass( "clearfix" );
			
			$(form_group[0]).append( label[0] );
			
			$(inputDiv[0]).append( input[0] );
			
			$(form_group[0]).append( inputDiv[0] );
			
			$( "#" + permissionContainer ).append( form_group[0] );
			$( "#" + permissionContainer ).append( clearfix[0] );
			
		}
	}
	
	drawAllPermissionForDesignationModal = function( permissionContainer ){
		
		var permissionMap = {};
		for( var i = 0; i<permissions.length; i++ ){
			
			permissionMap[permissions[i]] = false;
		}
		
		drawPermission( permissionMap, permissionContainer );
	}
	
	this.createNewDesignationTree = function(){
		
		$("#designationAddForm")[0].reset();
		
		$("#editForm_des").val( "false" );
		
		$("#dept_name_block").show();
		
		//$("#dept_code_block").show();
		
		$("#des_noc_block").show();
		
		drawAllPermissionForDesignationModal( "permission-container-designation" );
		
		$("#designationAddModal").modal( "show" );
	}
	
	$( document ).on( "click",  ".designation-add", function( ev ){
		
		var id = ev.target.getAttribute( "data-id" );
		
		$("#editForm_des").val("false");
		
		$("#dept_name_block").hide();
		
		$("#dept_code_block").hide();
		
		$("#des_noc_block").hide();
		
		for( var i = 0; i<DesignationTrees.length; i++ ){
			
			var treeData = DesignationTrees[i].getTreeData();
			
			for( var j = 0; j<treeData.length; j++ ){
				
				if( treeData[j]['id'] == id ){
					
					DesignationTrees[i].designationAddHandler( ev , departmentID);
					break;
				}
			}
		}
	});
	
	$(document).on( "click", ".designation-edit", function( ev ){
		
		var id = ev.target.getAttribute( 'data-designation-type-id' );
		$("#editForm_des").val( "true" );
		$("#dept_name_block").hide();
		
		for( var i = 0; i<DesignationTrees.length; i++ ){
			
			var treeData = DesignationTrees[i].getTreeData();
			
			for( var j = 0; j<treeData.length; j++ ){
				
				if( treeData[j]['designation-id'] == id ){
					
					DesignationTrees[i].designationEditHandler( ev );
					break;
				}
			}
		}
		
	});

	$(document).on( "click", ".employee-add", function( ev ){
		
		var id = ev.target.getAttribute('data-id');
		
		$("#editForm").val("false");
		
		$("#employeeModalTitle").html( "Add Employee" );
		
		for( var i = 0; i<EmployeeTrees.length; i++ ){
			
			var treeData = EmployeeTrees[i].getTreeData();
			
			for( var j = 0; j<treeData.length; j++){
				
				if( treeData[j]['id'] == id ){
					
					EmployeeTrees[i].employeeAddHandler( ev, departmentID );
					break;
				}
			}
		}
	});
	
	
	$(document).on( "click", ".employee-edit", function( ev ){
		
		var id = ev.target.getAttribute('data-id');
		
		$("#editForm").val("true");

		$("#employeeModalTitle").html( "Edit Employee" );
		
		for( var i = 0; i<EmployeeTrees.length; i++ ){
			
			var treeData = EmployeeTrees[i].getTreeData();
			
			for( var j = 0; j<treeData.length; j++){
				
				if( treeData[j]['employee-id'] == id ){
					
					EmployeeTrees[i].employeeEditHandler( ev );
					break;
				}
			}
		}
	});
	
	createDiv = function( employeeID, departmentName ){
		
		var div = $("<div/>");
		div[0].id = "employeeDiv_" + employeeID; //EmployeeTrees.length;
		div[0].style= "float:left; border:rgb(96,191,80) 1px solid;border-radius:5px; margin:15px 15px 15px 0px;padding:10px;";
		
		var treeWrapper = $("<div/>");
		treeWrapper[0].style="float:left";
		
		var deptName = $("<h4/>");
		$(deptName[0]).html( departmentName );
		deptName[0].style = "border-left:rgb(96, 191, 80) 3px solid; padding-left: 5px; pading-right: 5px;";
		
		$(treeWrapper[0]).append( deptName[0] );
		$(treeWrapper[0]).append( div[0] );
		
		if( containerDiv ){
			
			$( "#" + containerDiv ).append( treeWrapper[0] );
			return div[0].id;
		}
		else{
			console.error( "No parent container ID specified " );
		}
	}
	
	createDesignationDiv = function( designationID, departmentName ){
		
		var div = $("<div/>");
		div[0].id = "designationDiv_" + designationID; //DesignationTrees.length;
		div[0].style= "float:left; border:rgb(96,191,80) 1px solid; margin:15px 15px 15px 0px;padding:10px;";
		
		
		var treeWrapper = $("<div/>");
		treeWrapper[0].style="float:left";
		
		var deptName = $("<h4/>");
		$(deptName[0]).html( departmentName );
		deptName[0].style = "border-left:rgb(96, 191, 80) 3px solid; padding-left: 5px; padding-right: 5px;";
		
		$(treeWrapper[0]).append( deptName[0] );
		$(treeWrapper[0]).append( div[0] );
		
		if( containerDiv ){
			
			$( "#" + containerDesignationDiv ).append( treeWrapper[0] );
			return div[0].id;
		}
		else{
			console.error( "No parent container ID specified " );
		}
	}

	this.addEmployee = function(){
		
		console.log( "Submitted" );
		
		if( $("#userName").val().trim() == "" ){
			
			toastr.error( "Employee Name can not be empty" );
			return;
		}
		
		var url = $("#employeeAddForm").attr( 'action' );
		var param = {};
		param.departmentID = departmentID;
		var callback = employeeAddCallback;

	    if( $("#editForm").val() == "true" ){
	    	
	    	url = context +"/CrmEmployee/updateEmployee.do";
	    	
	    	param['crmEmployeeID'] = $("#crmEmployeeID").val();
	    	param['ID'] = $("#ID").val();
	    	callback = employeeUpdateCallback;
	    }
	    
	    var checkboxes = $("#employeeAddForm").find('input:checkbox:checked');
	    
	    for( var i =0; i<checkboxes.length; i++ ){
	    	
	    	param[ checkboxes[i]['name'] ] = "on";
	    }
	    
	    if( $("#parentID").val().length != 0 )
	    	param['parentID'] = $("#parentID").val();
	    
	    param['inventoryCatagoryTypeID'] = $("#designationTypeID").val();
	    param['name'] = $("#userName").val();
	    param['userID'] = $("#userID").val();
	    
	    thisRef.tempDivID = $("#divID").val();
	    
		callAjax( url,param,callback,"POST" );
	}
	
	this.addDesignation = function(){
		
		var url = $("#designationAddForm").attr( 'action' );
		var param = {};
		param.departmentID = departmentID;
		var callback = designationAddCallback;

	    if( $("#editForm_des").val() == "true" ){
	    	
	    	url = context +"/CrmDesignation/update_designation.do";
	    	
	    	param['designationTypeID'] = $("#designationTypeID_des").val();
	    	param['ID'] = $("#parentCatagoryTypeID").val();
	    	param.departmentID = departmentID;
	    	callback = designationUpdateCallback;
	    }
	    
	    var checkboxes = $("#designationAddForm").find('input:checkbox:checked');
	    
	    for( var i =0; i<checkboxes.length; i++ ){
	    	
	    	param[ checkboxes[i]['name'] ] = "on";
	    }
	    if( $("#parentCatagoryTypeID").val().length != 0 )
	    	param['parentCatagoryTypeID'] = $("#parentCatagoryTypeID").val();
	    
	    param['name'] = $("#name").val();
	    param['departmentName'] = $("#dept_name").val();
	    
	    thisRef.tempDivID = $("#divID_des").val();
	    
		thisRef.callAjax( url,param,callback,"POST" );
	}
	
	designationAddCallback= function( data ){
		
		var i = 0;
		$( "#" + designationModalID ).modal("hide");
		console.log(data);
		for( i = 0; i<DesignationTrees.length; i++ ){
			
			if( DesignationTrees[i].getDiv() == thisRef.tempDivID ){
				
				DesignationTrees[i].refresh( data );
				break;
			}
		}
		if( i == DesignationTrees.length ){
			
			location.reload(true);
		}

			
	}
	
	designationUpdateCallback = function( data ){
		
		for( var i = 0; i<DesignationTrees.length; i++ ){
			
			if( DesignationTrees[i].getDiv() == thisRef.tempDivID ){
				
				DesignationTrees[i].refreshUpdate( data );
				break;
			}
		}
	}

	employeeAddCallback = function( data ){
		var i = 0;
		for(  i = 0; i<EmployeeTrees.length; i++ ){
			
			if( EmployeeTrees[i].getDiv() == thisRef.tempDivID ){
				
				EmployeeTrees[i].refresh( data );
				break;
			}
		}
		
		if( i == EmployeeTrees.length ) 
			location.reload( true );
		
//		genericCallback(data);
	}
	
	
	employeeUpdateCallback = function( data ){
		
		for( var i = 0; i<EmployeeTrees.length; i++ ){
			
			if( EmployeeTrees[i].getDiv() == thisRef.tempDivID ){
				
				EmployeeTrees[i].refreshUpdate( data );
				break;
			}
		}
	}

	this.getDesignationName = function( id ){
		
		for( var i = 0; i<DesignationTrees.length; i++ ){
			
			var treeData = DesignationTrees[i].getTreeData();
			
			for( var j = 0; j<treeData.length; j++ ){
				
				if( treeData[j]['id'] == id ){
					
					var index = treeData[j]['text'].indexOf( "<i " );
					var designationName = treeData[j]['text'].substring( 0, index );
					return designationName;
				}
			}
		}
		return "";
	}
	
	this.removeEmployee = function( event, removeButton ){
		var employeeName = $(removeButton).closest("a")[0].innerText;
		
		if( confirm( "Are you sure that you want remove '" + employeeName + "'?" ) ){
			
			var url = context + "/CrmEmployee/deleteEmployee.do";
			var param = {};
			param['employeeID'] = event.target.getAttribute( 'data-id' );
			
			thisRef.tempDivID = event.target.getAttribute( 'data-div-id' );
			
			callAjax( url, param, employeeDeleteCallback, "POST" );
		}
	}
	
	this.removeDesignation = function( event, removeButton ){
		var designationName = $(removeButton).closest( "a" )[0].innerText;
		var id = event.target.getAttribute( 'data-designation-type-id' );
		
		if( confirm( "Are you sure that you want to remove '" + designationName + "'?" )){
			
			var url = context + "/CrmDesignation/deleteDesignation.do";
			var param = {};
			param['designationID'] = id;
			
			thisRef.tempDivID = event.target.getAttribute( 'data-div-id' );
			
			callAjax( url, param, designationDeleteCallback, "POST" );
		}
	}
	
	designationDeleteCallback = function( data ){
		
		
		for( var i = 0; i<DesignationTrees.length; i++ ){
			
			if( DesignationTrees[i].getDiv() == thisRef.tempDivID ){
				
				DesignationTrees[i].refreshRemove( data );
				break;
			}
		}
		
//		genericCallback(data);
	}
	
	employeeDeleteCallback = function( data ){
		
		for( var i = 0; i<EmployeeTrees.length; i++ ){
			
			if( EmployeeTrees[i].getDiv() == thisRef.tempDivID ){
				
				EmployeeTrees[i].refreshRemove( data );
				break;
			}
		}
		
//		genericCallback(data);
	}

	this.callAjax = function(url,param,callback,reqType){
		
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
}