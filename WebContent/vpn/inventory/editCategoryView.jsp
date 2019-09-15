<%@page import="java.io.PrintWriter"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="inventory.InventoryCatagoryType"%>
<%@page import="inventory.InventoryCatagoryDetails"%>
<%@page import="inventory.InventoryAttributeName"%>
<%@page import="inventory.InventoryCatagoryTreeNode"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>



<link href="inventory.css" rel="stylesheet" type="text/css" />

<div class="col-md-12">
	<!-- Horizontal Form -->
	<div class="portlet box portlet-btcl">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-gear"></i>Inventory Category Configuration
			</div>
		</div>
		<!-- /.box-header -->
		<!-- form start -->
		
		<div class="portlet-body form">
			<div class="form-body">
				<div class="panel-group row" id="accordion" role="tablist" aria-multiselectable="true">
					
				</div>
				<div class="text-center" style="margin-top:15px;">
					<a type="button" class="btn btn-default btn-cancel-btcl" href="<%=request.getHeader("referer")%>">Cancel</a>
					<button class="btn btn-submit-btcl" id="addButton" value="">Add New Category</button>
				</div>
			</div>	
		</div>
		
	</div>
</div>


<script type="text/javascript" src="../../../scripts/util.js"></script>
<script type="text/javascript" src="inventory.js"></script>

