
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="java.util.*, costConfig.*, common.*, util.*"%>
<%
	//<R@!h@n>
	int moduleID = Integer.parseInt(request.getParameter("moduleID"));	
	String[] moduleUnitList = (String[]) ModuleConstants.moduleUnitMap.get(moduleID);
	String noSuchCostChartFound = (String)request.getAttribute("NoSuchCostChartFound");
// 	HttpSession thisSession = request.getSession();
// 	String errorMsg = (String)thisSession.getAttribute("msg");
// 	String message=null;
	TableDTO tableDTO = null;
	long tableID = -1;
	List<RowDTO> rowList = Collections.EMPTY_LIST;
	List<ColumnDTO> columnList = Collections.EMPTY_LIST;
	List<CostCellDTO> cellList = Collections.EMPTY_LIST;
	
	int firstUpperValueRow = -1;
	int lastUpperValueRow  = -1;
	int firstUpperValue = -1;
	int lastUpperValue = -1;
	int numOfCategories = -1;
	
	CostConfigService costConfigService = null;
	TimeConverter timeConverter = null;
	
	if(noSuchCostChartFound == null){
		tableDTO = (TableDTO) request.getAttribute("table");
		tableID = tableDTO.getTableID();
		
		rowList = tableDTO.getRowDTOs();
		columnList = tableDTO.getColumnDTOs();
		cellList = tableDTO.getCostCellDTOs();
// 		message = (String) thisSession.getAttribute("success_message");

// 		thisSession.removeAttribute("success_message");
		lastUpperValue = columnList.get(columnList.size() - 1).getUpperRange() + 1;
		lastUpperValueRow = rowList.get(rowList.size() - 1).getUpperRange() + 1;
		firstUpperValueRow = rowList.get(0).getUpperRange() + 1;
		firstUpperValue = columnList.get(0).getUpperRange() + 1;
		costConfigService = new CostConfigService();
		timeConverter = new TimeConverter();	
	}
	//</R@!h@n>
%>

<html:base />
<!-- start main code -->
<div class="portlet box portlet-btcl ">
	<div class="portlet-title" style="background: #5cb85c">
		<div class="caption"><i class="fa fa-money"></i>Cost Configuration</div>
	</div>
	
	<%
		if(moduleID == ModuleConstants.Module_ID_LLI){
			numOfCategories = (Integer)request.getAttribute("totalCategory");
	%>
<!-- 				nav tab starts     -->
					<jsp:include page="../../../lli/lliTabPanel.jsp" flush="true"></jsp:include>
<!-- 				nav tab ends       -->

<!-- 				nav tab content starts -->
					<div class="tab-content clearfix">
					<%
// 						for(int i=0;i<numOfCategories;i++){
// 							String divID = "" + i;
// 							if(i==0){
					%>
<%-- 								<div class="tab-pane active" id="<%=divID%>"> --%>
									<jsp:include page="commonCostChartPortion.jsp" flush="true"></jsp:include>
<!-- 								</div> -->
					<%
// 							}else{
					%>
<%-- 								<div class="tab-pane" id="<%=divID%>"> --%>
<%-- 									<jsp:include page="commonCostChartPortion.jsp" flush="true"></jsp:include> --%>
<!-- 								</div> -->
					<%	
// 							}
// 						}
					%>
					</div>
			<!-- 				nav tab content ends -->
	<%
		}else{
	%>
<!-- 	for category less modules -->
	<jsp:include page="commonCostChartPortion.jsp" flush="true"/>
	<%
		}
	%>

	
<!-- edit start -->
<!-- edit end -->

</div>
<!-- End Main code -->

<div class="modal fade costing-modal" id="costingModalCol" tabindex="-1"
	role="dialog" aria-labelledby="costingModalCol">
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
				<button type="button" class="btn btn-xs btn-default"
					data-dismiss="modal" style="height: 25px;">Close</button>
				<button type="button" class="btn btn-xs btn-primary range-btn-col"
					style="height: 25px;">ok</button>
			</div>
		</div>
	</div>
</div>


<!-- R@!h@n -->
<div class="modal fade costing-modal" id="costingModalRow" tabindex="-1"
	role="dialog" aria-labelledby="costingModalRow">
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
							<input name="upperRange" value="" id="upperRangeRow" min=""
								class="form-control border-radius bill" type="number"
								autocomplete="off" required="required">
						</div>
					</div>
					<br>

				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-xs btn-default"
					data-dismiss="modal" style="height: 25px;">Close</button>
				<button type="button" class="btn btn-xs btn-primary range-btn-row"
					style="height: 25px;">ok</button>
			</div>
		</div>
	</div>
</div>


<!-- /R@!h@n -->





<div class="modal fade costing-modal-edit" id="costingModalEditCol"
	tabindex="-1" role="dialog" aria-labelledby="costingModalCol">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
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
							<input name="upperRange" value="" id="EditupperRange" min=""
								class="form-control border-radius bill" type="number"
								autocomplete="off" required="required">
						</div>
					</div>
					<br>

				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-xs btn-default"
					data-dismiss="modal" style="height: 25px;">Close</button>
				<button type="button" class="btn btn-xs btn-primary col-edit-btn"
					style="height: 25px;">ok</button>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		var divToRemove;
		
		$('#activationDate').datepicker({
	        
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
		
		
		function afterDeleteTable(data) {
			if (data['responseCode'] == 1) {
				toastr.success(data['msg']);
				divToRemove.remove();
			} else {
				toastr.error(data['msg']);
				
			}
			
		}
		$('.costChartDltBtn').click(function(e){
			e.preventDefault();
			var tableId = $(this).parent().children().eq(1).attr("id"); 	
// 			alert(tableId);
			var urlString = "../../CostChart/deleteTable.do";
			divToRemove = $(this).parent().parent();
			var formData={tableID: tableId};
			
			callAjax(urlString, formData, afterDeleteTable);
		});
		
	});
</script>
<%@include file="vpnCostConfigPageScript.jsp"%>



