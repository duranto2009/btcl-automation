<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="../../includes/checkLogin.jsp"%>
<%@page import="java.util.*, costConfig.*, common.*, util.*"%>
<style>
	th{
		text-align: center !important;
	}
	.no-right-border {
		border-right: none !important;
	}
	.no-border {
		border-right: none !important;
		border-left: none !important;
	}
	.no-left-border{
		border-left: none !important;
	}
	.color-success{
		color: #5cb85c;
	}
	.color-error{
		color: #d36b7e;
	}
	.table-view{
		margin-top: 30px; 
		text-align: center;
	}
</style>

<div class="row">
	<div class="col-lg-12">
		<div id=msgDiv style='text-align:center'>
			<h4 class=color-error ><%=errorMsg==null?"":errorMsg %></h4>
			<h4 class=color-success><%=successMsg==null?"":successMsg%></h4>
			<h4 class=color-error ><%=tableDTO!=null?"":"No activated cost chart found for LLI category "+categoryID %></h4>
		</div>
	
	<!-- history -->
<%
	Map<Long, TableDTO> mapOfTablesToTableID = new HashMap<Long, TableDTO>();
	for (int tableIdx=0;tableIdx<tableDTOs.size();tableIdx++) {
		TableDTO table = tableDTOs.get(tableIdx);
		long tableID = table.getTableID();
		mapOfTablesToTableID.put(tableID, table);
		
		List<RowDTO> rowList = table.getRowDTOs();// costConfigService.getRowDTOByTableID(tableID);
		List<ColumnDTO> columnList = table.getColumnDTOs();// costConfigService.getAllColumnDTOsByTableID(tableID);
		List<CostCellDTO> cellList = table.getCostCellDTOs();//.getCellDTOsbyTableID(tableID);
%>
							
		<div class=history>
			<h3 class=" text-center">
				Activation Date : <%=TimeConverter.getTimeStringFromLong( table.getActivationDate()) %>
			</h3>
			<div class="table-responsive" id="view-cost-chart-<%=tableID%>" >
				<button class="btn btn-default col-remove pull-right costChartDltBtn" data-table-id=<%=tableID%> type="button">
					<i class="fa fa-times color-error" aria-hidden="true"></i>
				</button>
				<%if(tableIdx>0){ //not visible for first table as it is activated it can not be edited%>
				<button class="btn btn-default pull-left cost-chart-edit-btn" data-table-id="<%=tableID%>" type=button>
					<i class="fa fa-pencil-square-o"></i>Edit
				</button>
				<%} %>
				<table class="table table-striped table-hover table-bordered" id="<%=tableID%>"
				 style="margin-top: 30px; text-align: center;">
					<thead>
						<tr>
							<th>Sl</th>
							<th colspan="3"> <%=moduleUnitList[0]+"("+moduleUnitList[1]+")" %></th>
							<%
							for (int columnIdx=0; columnIdx<columnList.size();columnIdx++) {
								if (columnList.get(columnIdx).getUpperRange() != Integer.MAX_VALUE) {
							%>
								<th>
									<%=moduleUnitList[2]+" "+ (columnIdx+ 1)%>
									<br>
									<%="(" + columnList.get(columnIdx).getLowerRange() 
											+ "-"
											+ columnList.get(columnIdx).getUpperRange() 
											+" "+moduleUnitList[3]+ ") "+moduleUnitList[4] 
									%>
								</th>
								<%} else {%>
								<th>
									<%=moduleUnitList[2]+" " + (columnIdx+ 1)%>
									<br>
									<%="(" + columnList.get(columnIdx).getLowerRange() + "+ " 
									+ moduleUnitList[3] + ") " + moduleUnitList[4]
									%>
								</th>
							<%
								}
							}
							%>
						</tr>
					</thead>
					<tbody>
					<%
						int cellIdx= 0;
						for (int rowIdx = 0; rowIdx<rowList.size(); rowIdx++) {
							int currentUpperRange = rowList.get(rowIdx).getUpperRange();
					%>
						<tr>
							<td><%=(rowIdx + 1)%></td>
							<td class=no-right-border><%=rowList.get(rowIdx).getLowerRange()%></td>
							<td class=no-border>-</td>
							<td class=no-left-border>
								<%=currentUpperRange!=Integer.MAX_VALUE?currentUpperRange:"~"%>
							</td>
							<%for (int columnIdx=0;columnIdx<columnList.size();columnIdx++) { %>
								<td><%=cellList.get(cellIdx++).getValue()%></td>
							<%}%>
						</tr>
						<%}%>
					</tbody>
				</table>
			</div>
			<%@include file="costEdit.jsp" %>
		</div>
		<!-- /history -->
	<%}%> 
	<!-- end for -->
	</div>
	<!-- end column-->
</div>
<!-- end row -->
	<hr><hr>
<%
	long newTableID = -1;
	List<RowDTO> newCostChartRows = Collections.emptyList();
	List<ColumnDTO> newCostChartColumns = Collections.emptyList();
	List<CostCellDTO> newCostChartCells= Collections.emptyList();
	if(tableDTO != null){
		newTableID = tableDTO.getTableID();
		newCostChartRows = tableDTO.getRowDTOs();
		newCostChartColumns = tableDTO.getColumnDTOs();
		newCostChartCells = tableDTO.getCostCellDTOs();
	}
	
%>
<div class=row>
	<div class="col-lg-12">				
		<div style="text-align: center">
			<button type="button" class="btn btn-submit-btcl" id="btnAddNewCostConfig">
				Configure New Cost
			</button>
		</div>
		<!-- add more cost based on current active cost -->
		<div class="form-horizontal" id ="addNewCostConfig" style="display:none">
			<hr>
			<div id="category<%=categoryID %>">	
				<form method="post" action="../../UpdateCostConfig.do" id="costing-form-new">
					<input type=hidden name="tableID" value="<%=newTableID%>" />
					<input type=hidden name="moduleID" value="<%=moduleID %>" />
					<input type="hidden" name="categoryID" value="<%=categoryID%>">
					<div class="dynamic-hidden-field-row-new">
					<%for (int rowIdx= 0; rowIdx< newCostChartRows.size(); rowIdx++) {%>
						<input type=hidden name="rowIDs" value='<%=newCostChartRows.get(rowIdx).getID()%>' />
						<input type=hidden name="rowIndex" data-lower=<%=newCostChartRows.get(rowIdx).getLowerRange() %> 
						data-upper=<%=newCostChartRows.get(rowIdx).getUpperRange() %> 
						value='<%=newCostChartRows.get(rowIdx).getIndex()%>' /> 
					<%}%>
					</div>
					<div class="dynamic-hidden-field-col-new">
					<%for (int columnIdx= 0; columnIdx< newCostChartColumns.size(); columnIdx++) {%>
						<input type=hidden name="lowerRangeKm" value='<%=newCostChartColumns.get(columnIdx).getLowerRange()%>' />
						<input type=hidden name="upperRangeKm" value='<%=newCostChartColumns.get(columnIdx).getUpperRange()%>' />
					<% }%>
					</div>
					<div class="table-responsive" id='div-render-new'>
						<button class="btn btn-default col-remove pull-right" type="button" data-table-id="new" style="margin-bottom: 15px">
							<i class="fa fa-times color-error" aria-hidden="true"></i>
						</button>
						<button class="btn btn-search-btcl col-add pull-right" type="button" data-table-id="new" 
						style="margin-right: 5px; margin-bottom: 15px"> 
							Column Add&nbsp;
							<i style="color: #fff" class="fa fa-arrow-right" aria-hidden="true"></i>
						</button>
						<table id="costingTable-new" class="table table-hover table-bordered table-striped table-edit" 
						style="margin-top: 30px; text-align: center">
							<thead>
								<tr>
									<td class="td_viewheader">Sl</td>
									<td class="align-center td_viewheader"><%=moduleUnitList[0]+"(" +moduleUnitList[1]+ ")" %></td>
									<%
									for (int columnIdx= 0; columnIdx< newCostChartColumns.size(); columnIdx++) {
										if(newCostChartColumns.get(columnIdx).getUpperRange() != Integer.MAX_VALUE){
									%>
									<td class="align-center td_viewheader">
										<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="<%=newCostChartColumns.get(columnIdx).getLowerRange() %>" 
											data-upper="<%=newCostChartColumns.get(columnIdx).getUpperRange()%>" accesskey="<%=columnIdx%>"  data-table-id='new' 
											style='display:none'> <i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit
										</a>
										<%=moduleUnitList[2]+" " + (columnIdx + 1)%><br>
										<%="(" + newCostChartColumns.get(columnIdx).getLowerRange() + "-"
												+ newCostChartColumns.get(columnIdx).getUpperRange() + 
												" "+moduleUnitList[3]+") "+moduleUnitList[4]%>
									</td>
									<%} else {%>
									<td class="align-center td_viewheader">
										<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="<%=newCostChartColumns.get(columnIdx).getLowerRange() %>"
											data-upper="<%=newCostChartColumns.get(columnIdx).getUpperRange()%>" accesskey="<%=columnIdx%>" data-table-id='new'
											style='display:none'> <i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit
										</a>
										<%=moduleUnitList[2]+" "+ (columnIdx+ 1)%><br>
										<%="(" + newCostChartColumns.get(columnIdx).getLowerRange() + "+ " +moduleUnitList[3] + ") " + moduleUnitList[4]%>
									</td>
									<% } 
									}%>
								</tr>
							</thead>
							<tbody>
							<%
								int cellIdx = 0;
								for (int rowIdx= 0; rowIdx< newCostChartRows.size(); rowIdx++) {
									int upperRange = newCostChartRows.get(rowIdx).getUpperRange();
							%>
								<tr>
									<td><%=(rowIdx + 1) + "."%></td>
									<td>
										<input type="text" class="form-control lower-range-mb" id="lowerRangeMb<%=rowIdx%>" 
										style="width: 35%; float: left" name="lowerRangeMb" value="<%=newCostChartRows.get(rowIdx).getLowerRange()%>" />
										<span>-</span>
										<input type="text" class="form-control upper-range-mb" id="upperRangeMb<%=rowIdx%>" 
										style="width: 35%; float: right" name="upperRangeMb" value=<%=upperRange!=Integer.MAX_VALUE?upperRange:"~"%> />
									</td>
									
									<%for (int columnIdx= 0; columnIdx< newCostChartColumns.size(); columnIdx++) {%>
											
									<td>
										<input type="text" class="form-control" name="cost" value='<%=newCostChartCells.get(cellIdx++).getValue()%>' />
										<input type="hidden" name="cellIDs" value='<%=newCostChartCells.get(cellIdx - 1).getID()%>' />
									</td>
									<%}%>
								</tr>
							<%} %>
							</tbody>
						</table>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label">Activation Date</label>
						<div class="col-md-4">
							<div class="input-group date ">
								<div class="input-group-addon tohide">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" name="activationDate"
									class="form-control activationDate" value="" autocomplete="off" />
							</div>
						</div>
					</div>
					<button class="btn btn-search-btcl row-add" data-table-id='new' type="button">
						<i class="fa fa-arrow-down" style="color: #fff"
							aria-hidden="true"></i>&nbsp;Row Add
					</button>
					<button class="btn btn-default row-remove" data-table-id='new' type="button">
						<i class="fa fa-times" style="color: #d36b7e"
							aria-hidden="true"></i>
					</button>			
					<div class="text-center">
						<button type="button" class="btn btn-default btn-reset-btcl">Reset</button>
						<button type="button" data-table-id=new class="btn btn-success submit-btn btn-submit-btcl" >Submit</button>
					</div>
				</form>
			</div>
			<!-- 	category id div finishes -->
		</div>
		<!-- /add more cost based on current active cost -->
	</div>
	<!-- end column -->
</div>
<!-- row finish -->
