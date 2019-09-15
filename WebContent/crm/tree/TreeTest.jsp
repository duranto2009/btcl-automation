<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<% String context = request.getContextPath(); %>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
		<style>
			
			#con div:FIRST-CHILD , #desCon div:FIRST-CHILD {
				
				border-right: 1px dotted blue;
				padding-right: 15px;
			}
		</style>
	</head>
	
	<body>
		<jsp:include page="employeeAddModal.jsp"></jsp:include>
		<jsp:include page="designationAddModal.jsp"></jsp:include>
		
		<h4> Employee Trees </h4>
		<div id="con">
			&nbsp <button class="btn btn-xs btn-primary" id="newEmployeeTree" > Add New Employee Tree  <i class="fa fa-plus"></i></button>
		</div>
		
		<div class="clearfix"></div>
		<br/><br/>
		
		<h4> Designation Trees </h4>
		<div id="desCon">
			&nbsp <button class="btn btn-xs btn-primary" id="newDesignationTree" > Add New Designation Tree  <i class="fa fa-plus"></i></button>
		</div>
		
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.1/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
		<script src="https://use.fontawesome.com/70de7f4894.js"></script>
		<script src="EmployeeTree.js"></script>
		<script src="DesignationTree.js"></script>
		<script src="TreeBuilder.js"></script>
		<script>
			
			$(document).ready( function(){
				
				var context = "<%=request.getContextPath() %>";
				
				var builder = new TreeBuilder();
				builder.setContainerID( "con" );
				builder.setContainerDesignationDiv( "desCon" );
				
				builder.setAllEmployeeRootURL( context + "/CrmEmployee/getAllEmployeeRoots.do" );
				builder.setAllDesignationRootURL( context + "/CrmDesignation/getDesignations.do" );
				
				builder.setEmployeeModal( "employeeAddModal" );
				builder.setEmployeeForm( "employeeAddForm" );
				
				builder.setDesignationModal( "designationAddModal" );
				builder.setDesignationForm( "designationAddForm" );
				
				builder.setContext( context );
				builder.buildEmployeeTrees();
				builder.buildDesignationTrees();
				
				$( document ).on( "submit", ".employeeAddForm", function( event ){
					
					event.preventDefault();
					builder.addEmployee();
				});
				
				$( document ).on( "submit", ".designationAddForm", function( event ){
					
					event.preventDefault();
					builder.addDesignation();
				});
				
				$( document ).on( "click", ".employee-remove", function( event ){
					
					builder.removeEmployee( event, $(this) );
				});
				
				$( document ).on( "click", ".designation-remove", function( event ){
					
					builder.removeDesignation( event, $(this) );
				});
				
				$( document ).on( "click", "#newEmployeeTree", function( event ){
					
					builder.createNewEmployeeTree();
				});
				
				$( document ).on( "click", "#newDesignationTree", function( event ){
					
					builder.createNewDesignationTree();
				});
			});
			
		</script>
	</body>
</html>