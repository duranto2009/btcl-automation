<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="common.ModuleConstants, costConfig.CostConfigService, costConfig.TableDTO, org.apache.log4j.Logger"%>
<%@page import="util.ServiceDAOFactory"%>

<%
	Logger logger = Logger.getLogger(getClass());

	int moduleID = Integer.parseInt(request.getParameter("moduleID"));	
	int categoryID = Integer.parseInt(request.getParameter("categoryID"));
	int numOfCategories = (Integer)request.getAttribute("totalCategory");
	TableDTO tableDTO = (TableDTO) request.getAttribute("table");
	String[] moduleUnitList = (String[]) ModuleConstants.moduleUnitMap.get(moduleID);
	CostConfigService costConfigService = ServiceDAOFactory.getService(CostConfigService.class);

	
// 	int firstUpperValueRow = -1;
// 	int lastUpperValueRow  = -1;
// 	int firstUpperValue = -1;
// 	int lastUpperValue = -1;
	
	
	List<TableDTO> tableDTOs = new ArrayList<TableDTO>();
	if(tableDTO != null) {
		tableDTOs.add(tableDTO);
// 		firstUpperValueRow = tableDTO.getRowDTOs().get(0).getUpperRange() + 1;
// 		lastUpperValueRow = tableDTO.getRowDTOs().get(tableDTO.getRowDTOs().size() - 1).
// 							getUpperRange() + 1;
		
// 		firstUpperValue = tableDTO.getColumnDTOs().get(0).getUpperRange() + 1;
// 		lastUpperValue = tableDTO.getColumnDTOs().get(tableDTO.getColumnDTOs().size() - 1).
// 							getUpperRange() + 1;
		
	}
	tableDTOs.addAll(costConfigService.getRecentAndUpcomingTableIDs(moduleID, categoryID));
	
	String errorMsg = (String) request.getSession().getAttribute("msg");
	String successMsg = (String) request.getSession().getAttribute("success_message");
	request.getSession().removeAttribute("success_message");
	request.getSession().removeAttribute("msg");
	
%>

<!-- start main code -->
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption"><i class="fa fa-money"></i>Cost Configuration</div>
	</div>
	<div class="portlet-body">
		<%@include file="../../lli/lliTabPanel.jsp"%>
		<div class="tab-content clearfix">
			<%@include file="commonCostChartPortion.jsp" %>
		</div>
	</div>
</div>
<!-- End Main code -->

<div class="modal fade costing-modal" id="col-add-modal" tabindex="-1"
	role="dialog" aria-labelledby="col-add-modal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Set Your Range</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="alert alert-danger error-msg" style="display: none;"></div>
					<br>
					<div class="form-group">
						<label class="col-md-3  control-label">Upper Range</label>
						<div class="col-md-8 col-xs-12">
							<input name="upperRange" value="" id="upperRangeCol" min=""
								class="form-control border-radius bill" type="number"
								autocomplete="off" required="required">
						</div>
					</div>
					<br>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-xs btn-default" data-dismiss="modal" style="height: 25px;">Close</button>
				<button type="button" class="btn btn-xs btn-primary" id=btn-create-column style="height: 25px;">ok</button>
			</div>
		</div>
	</div>
</div>


<!-- R@!h@n -->
<div class="modal fade costing-modal" id="row-add-modal" tabindex="-1"
	role="dialog" aria-labelledby="costingModalRow">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"> 
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Set Your Range</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="alert alert-danger error-msg" style="display: none;"></div>
					<br>
					<div class="form-group">
						<label class="col-md-3  control-label">Upper Range</label>
						<div class="col-md-8 col-xs-12">
							<input name="upperRange" value="" id="upperRangeRow" min="" class="form-control border-radius bill" 
							type="number" autocomplete="off" required="required">
						</div>
					</div>
					<br>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-xs btn-default" data-dismiss="modal" style="height: 25px;">Close</button>
				<button type="button" class="btn btn-xs btn-primary" id=btn-create-row style="height: 25px;">ok</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade costing-modal-edit" id="costingModalEditCol" tabindex="-1" role="dialog" aria-labelledby="costingModalEditCol">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Edit Your Range</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="alert alert-danger error-msg" style="display: none;"></div>
					<br>
					<div class="form-group">
						<label class="col-md-3  control-label">Edit Upper Range</label>
						<div class="col-md-8 col-xs-12">
							<input name="upperRange" value="" id="edit-upper-range" min=""
								class="form-control border-radius bill" type="number"
								autocomplete="off" required="required">
						</div>
					</div>
					<br>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-xs btn-default" data-dismiss="modal" style="height: 25px;">Close</button>
				<button type="button" class="btn btn-xs btn-primary" id='col-edit-btn' style="height: 25px;">ok</button>
			</div>
		</div>
	</div>
</div>
<script>
	var rowHeading = '<%=moduleUnitList[0]%>';
	var rowUnit = '<%=moduleUnitList[1]%>';
	var colHeading = '<%=moduleUnitList[2]%>';
	var colInnerUnit = '<%=moduleUnitList[3]%>';
	var colOuterUnit = '<%=moduleUnitList[4]%>';
	$(document).ready(function() {
		
		$('.activationDate').datepicker({
			
			dateFormat : 'dd/mm/yy',
			autoclose : true,
			beforeShowDay: function (date) {
		        //getDate() returns the day (0-31)
		        if (date.getDate() == 1) {
		            return [true, ''];
		        }
		        return [false, ''];
			}
// 			dateFormat: 'dd/mm/yy',
// 	        onClose: function(dateText, inst) { 
// 	            $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
// 	        }
		});
		$('#btnAddNewCostConfig').click(function(){
			$('#addNewCostConfig').toggle();
		});
		
		$('.costChartDltBtn').click(function(){
			var tableId = $(this).attr('data-table-id');
			var urlString = "../../CostChart/deleteTable.do";
			var divToRemove = $(this).parent().parent();
			LOG(divToRemove);
			bootbox.confirm("Are you sure you want to delete the cost chart?", 
				function(result) {
	        		if (result) {
	        			var formData={tableID: tableId};
	        			ajax(urlString, formData, function(data){
	        				if (data.value) {
								$(divToRemove).remove();
								toastr.success("Cost chart is successfully deleted", "Success", {closeButton: true, timeOut: 0});
	        				}else {
	        					toastr.error()
	        				}
	        			}, "POST", []);
	             	}
	         	}
			);
			return false;
		});
		$('.cost-chart-edit-btn , .cost-chart-restore-btn').click(function(){
			var editCostChartDiv = '#edit-cost-chart-' + $(this).data('table-id');
			var viewCostChartDiv = '#view-cost-chart-' + $(this).data('table-id');
			$(editCostChartDiv).toggle();
			$(viewCostChartDiv).toggle();
			return false;
		});
	});
	
</script>
<script src="../../assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<%@include file="lliCostConfigPageScript.jsp"%>
<%@include file="columnFunctionality.jsp" %>
<%@include file="rowFunctionality.jsp" %>
