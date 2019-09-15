<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="../../../includes/checkLogin.jsp"%>
<%@page import="java.util.*, costConfig.*, common.*, util.*"%>
<%
	//<R@!h@n>
	int moduleID = Integer.parseInt(request.getParameter("moduleID"));	
	String[] moduleUnitList = (String[]) ModuleConstants.moduleUnitMap.get(moduleID);
	String noSuchCostChartFound = (String)request.getAttribute("NoSuchCostChartFound");
	HttpSession thisSession = request.getSession();
	String errorMsg = (String)thisSession.getAttribute("msg");
	String message = (String) thisSession.getAttribute("success_message");
	thisSession.removeAttribute("success_message");
	thisSession.removeAttribute("msg");
	TableDTO tableDTO = null;
	long tableID = -1;
	List<RowDTO> rowList = Collections.EMPTY_LIST;
	List<ColumnDTO> columnList = Collections.EMPTY_LIST;
	List<CostCellDTO> cellList = Collections.EMPTY_LIST;
	int firstUpperValue = -1;
	int lastUpperValue = -1;
	CostConfigService costConfigService = ServiceDAOFactory.getService(CostConfigService.class);
	TimeConverter timeConverter = new TimeConverter();
	int numOfCategories = -1;
	String catID = (String)request.getParameter("categoryID");
	if(noSuchCostChartFound == null){
		if(request.getAttribute("table")!=null){
			tableDTO = (TableDTO) request.getAttribute("table");
		}
		tableID = tableDTO.getTableID();
		rowList = tableDTO.getRowDTOs();
		columnList = tableDTO.getColumnDTOs();
		cellList = tableDTO.getCostCellDTOs();
		
		
		lastUpperValue = columnList.get(columnList.size() - 1).getUpperRange() + 1;
		firstUpperValue = columnList.get(0).getUpperRange() + 1;

		
		
		if(catID == null){
			catID = "";
		}
	
	}
	
	
	
	//</R@!h@n>
%>

<!-- 	edit strat -->
	<div class="portlet-body">
		<div class="row">
			<div class="col-lg-12">
						<%
							if(errorMsg != null){
						%>
						<div class="text-center">
							<div class="">
								<p style="color: #d36b7e"><%=errorMsg %></p>
								
							</div>
						</div>
						<% 	
							}
						%>
						<%
							if (message != null) {
						%>
						<div class="text-center">
							<div class="">
								<p style="color: #5cb85c"><%=message %></p>
							</div>
						</div>
						<%
							}
						%>
						<%
							if (noSuchCostChartFound != null) {
						%>
						<div class="text-center">
							<div class="">
								<p style="color: #d36b7e"><%=noSuchCostChartFound %></p>
							</div>
						</div>
						<%
							}
// 							else{
						%>
							<!-- history -->
							<%
								ArrayList<TableDTO> tableDTOs = new ArrayList<TableDTO>();
								if(tableDTO!=null){
								    tableDTOs.add(tableDTO);
								}
// 							    switch case needed here
								if(moduleID == ModuleConstants.Module_ID_LLI){
									
									System.err.println("LLI from jsp");
									tableDTOs.addAll(costConfigService.getRecentAndUpcomingTableIDs(moduleID, Integer.parseInt(catID)));
								}else if(moduleID == ModuleConstants.Module_ID_VPN){
									System.err.println("VPN from jsp");
									tableDTOs.addAll(costConfigService.getRecentAndUpcomingTableIDs(moduleID));
								}
							    int ix = 0;
								for (TableDTO table : tableDTOs) {
									long tableId = table.getTableID();
									List<RowDTO> rowListIN =  table.getRowDTOs();// costConfigService.getRowDTOByTableID(tableId);
									List<ColumnDTO> columnListIN =  table.getColumnDTOs();// costConfigService.getAllColumnDTOsByTableID(tableId);
									List<CostCellDTO> cellListIN = table.getCostCellDTOs();//costConfigService.getCellDTOsbyTableID(tableId);
// 									System.out.println("tableDTO : "+table +"\n rowList : "+rowListIN+"\n columnListIN : "+columnListIN+"\n cellListIN : " +cellListIN);
							%>
							
								<div>
									<%
										if(ix == 0){ 
											ix++;
									%>
										<h3 class=" text-center">Activation Date : <%=timeConverter.getTimeStringFromLong( table.getActivationDate()) %></h3>
									<%}else{ %>
										<h3 class=" text-center">Activation Date : <%=timeConverter.getTimeStringFromLong( table.getActivationDate()) %></h3>
									<%} %>
									
									
									<div class="table-responsive">
										
										<button class="btn btn-default col-remove pull-right costChartDltBtn" type="button">
										<i class="fa fa-times" style="color: #d36b7e" aria-hidden="true"></i>
										</button>
										<table class="table table-bordered table-striped" id="<%=table.getTableID()%>"style="margin-top: 30px; text-align: center;">
											<thead>
												<tr>
													<td>Sl</td>
													<td colspan="3">
													<%=moduleUnitList[0]+"("+moduleUnitList[1]+")" %></td>
													<%
														for (int columnIndexer = 0; columnIndexer < columnListIN.size(); columnIndexer++) {
																if (columnListIN.get(columnIndexer).getUpperRange() != Integer.MAX_VALUE) {
													%>
													<td >
													<%=moduleUnitList[2]+" "+ (columnIndexer + 1)
													%>
													<br>
													<%="(" + columnListIN.get(columnIndexer).getLowerRange() 
															+ "-"
															+ columnListIN.get(columnIndexer).getUpperRange() 
															+" "+moduleUnitList[3]+ ") "+moduleUnitList[4] 
													%>
													</td>
													<%
														} else {
													%>
													<td>
														<%=moduleUnitList[2]+" " + (columnIndexer + 1)%>
														<br>
														<%="(" + columnListIN.get(columnIndexer).getLowerRange() + "+ " 
														+ moduleUnitList[3] + ") " + moduleUnitList[4]
														%>
														
													</td>
													<%
														}
													}
													%>
												</tr>
											</thead>
											<tbody>
												<%
														int cellIndexIN = 0;
														for (int rowIndexer = 0; rowIndexer < rowListIN.size(); rowIndexer++) {
												%>
												<tr>
	
													<td>
														<%="" + (rowIndexer + 1) + "."%>
													</td>
													
													<%
														if (rowListIN.get(rowIndexer).getUpperRange() != Integer.MAX_VALUE) {
													%>
													<td style=" border-right:none;">
														<%=rowListIN.get(rowIndexer).getLowerRange()%>
													</td>
													
													<td style=" border-left:none;border-right:none;">
														-
													</td>
													<td style="border-left:none;">
														<%=rowListIN.get(rowIndexer).getUpperRange()%>
													</td>
	
													<%
														} else {
													%>
													<td style="border-right:none;">
														<%=rowListIN.get(rowIndexer).getLowerRange()%>
													</td>
													<td style=" border-left:none;border-right:none;">
														-
													</td>
													<td style=" border-left:none;">
														~
													</td>
													<%
														}
													%>
													<%
														for (int columnIndexer = 0; columnIndexer < columnListIN.size(); columnIndexer++) {
													%>
	
													<td>
														<%=cellListIN.get(cellIndexIN++).getValue()%>
													</td>
													<%
														}
													%>
												</tr>
												<%
													}
												%>
	
											</tbody>
										</table>
									</div>
								
								</div>
							<!-- /history -->
							<% 
//  								}
 							}
 							%> 
							
						<div style="width:100%; text-align: center;">
						<button type="button" class="btn btn-submit-btcl" id="btnAddNewCostConfig">Configure New Cost</button>
						</div>
						
						
						<!-- add more cost based on current active cost -->
						<div class="form-horizontal" id="addNewCostConfig" style="display:none">
						<hr>
							<div class="alert alert-danger error-msg"
								style="display: none; text-align: center;"></div>
<!-- 							category id div starts -->
							<div id="category<%=catID %>">	
							<form method="post" action="../../UpdateCostConfig.do"
								id="costingUpdateForm">
								
								
								
								<input type=hidden name="tableID" value="<%=tableID%>" />
								<input type=hidden name="moduleID" value="<%=moduleID %>" />
								<%
									if(moduleID==ModuleConstants.Module_ID_LLI){
								%>
								<input type="hidden" name="categoryID" value="<%=catID%>">
								<%
									}
								%>
								<div class="dynamic-hidden-field-row">
								<%
									for (int rowIndexer = 0; rowIndexer < rowList.size(); rowIndexer++) {
								%>
								<input
									type=hidden name="rowIDs"
									value='<%=rowList.get(rowIndexer).getID()%>' />
								<input type=hidden name="rowIndex"
									value='<%=rowList.get(rowIndexer).getIndex()%>' /> 
								<%
									}
								%>
								</div>
								<div class="dynamic-hidden-field">
									<%
										for (int columnIndexer = 0; columnIndexer < columnList.size(); columnIndexer++) {
									%>
									
									<input type=hidden name="lowerRangeKm"
										value='<%=columnList.get(columnIndexer).getLowerRange()%>' />
									<input type=hidden name="upperRangeKm"
										value='<%=columnList.get(columnIndexer).getUpperRange()%>' />

									<%
										}
									%>
								</div>
								<div class="table-responsive">
									<table id="costingTable"
										class="table table-bordered table-striped"
										style="margin-top: 30px; text-align: center">
										<button class="btn btn-default col-remove pull-right"
											type="button" style="margin-bottom: 15px">
											<i class="fa fa-times" style="color: #d36b7e"
												aria-hidden="true"></i>
										</button>
										<button id="addCol" class="btn btn-search-btcl pull-right"
											type="button" style="margin-right: 5px; margin-bottom: 15px">
											Column Add&nbsp;<i style="color: #fff"
												class="fa fa-arrow-right" aria-hidden="true"></i>
										</button>
										<thead>
											<tr>
												<td class="td_viewheader">Sl</td>
												<td class="align-center td_viewheader">
												<%=moduleUnitList[0]+"(" +moduleUnitList[1]+ ")" %>
												
												</td>
												<%
													for (int columnIndexer = 0; columnIndexer < columnList.size(); columnIndexer++) {
														if (columnList.get(columnIndexer).getUpperRange() != Integer.MAX_VALUE) {
												%>
												<td class="align-center td_viewheader">
												
												<%=moduleUnitList[2]+" " + (columnIndexer + 1)%><br>
												<%="(" + columnList.get(columnIndexer).getLowerRange() + "-"
														+ columnList.get(columnIndexer).getUpperRange() + 
														" "+moduleUnitList[3]+") "+moduleUnitList[4]%>
												</td>
												<%
													} else {
												%>
												<td class="align-center td_viewheader"><%=moduleUnitList[2]+" "+ (columnIndexer + 1)%><br>
												<%="(" + columnList.get(columnIndexer).getLowerRange() + "+ " +moduleUnitList[3] + ") " + moduleUnitList[4]%></td>
												<%
													}
												}
												%>
											</tr>
										</thead>
										<tbody>
											<%
												int cellIndex = 0;
												for (int rowIndexer = 0; rowIndexer < rowList.size(); rowIndexer++) {
											%>
											<tr>

												<td><%="" + (rowIndexer + 1) + "."%></td>
												<%
													if (rowList.get(rowIndexer).getUpperRange() != Integer.MAX_VALUE) {
												%>
												<td>
												<input type="text"
													class="form-control lower-range-mb"
													id="lowerRangeMb<%=rowIndexer%>"
													
													style="width: 35%; float: left" name="lowerRangeMb"
													value="<%=rowList.get(rowIndexer).getLowerRange()%>" />
													
													
													<span>-</span>
												<input
													type="text" class="form-control upper-range-mb"
													id="upperRangeMb<%=rowIndexer%>"
													style="width: 35%; float: right" name="upperRangeMb"
													value="<%=rowList.get(rowIndexer).getUpperRange()%>" />
													
													</td>

												<%
													} else {
												%>
												<td><input type="text"
													class="form-control lower-range-mb"
													style="width: 35%; float: left" name="lowerRangeMb"
													id="lowerRangeMb<%=rowIndexer%>"
													value="<%=rowList.get(rowIndexer).getLowerRange()%>" /><span>-</span><input
													type="text" class="form-control upper-range-mb"
													id="upperRangeMb<%=rowIndexer%>"
													style="width: 35%; float: right" name="upperRangeMb"
													value="~" /></td>

												<%
													}
												%>
												<%
													for (int columnIndexer = 0; columnIndexer < columnList.size(); columnIndexer++) {
												%>

												<td><input type="text" class="form-control" name="cost"
													value='<%=cellList.get(cellIndex++).getValue()%>' /></td>
												<input type="hidden" name="cellIDs"
													value='<%=cellList.get(cellIndex - 1).getID()%>' />
												<%
													}
												%>
											</tr>
											<%
												}
											%>

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

											<input type="text" id="activationDate" name="activationDate"
												class="form-control pull-right" value="" autocomplete="off" />
										</div>
									</div>
								</div>
									<button id="addRow" class="btn btn-search-btcl" type="button">
										<i class="fa fa-arrow-down" style="color: #fff"
											aria-hidden="true"></i>&nbsp;Row Add
									</button>
									<button class="btn btn-default row-remove" type="button">
										<i class="fa fa-times" style="color: #d36b7e"
											aria-hidden="true"></i>
									</button>
								<div class="text-center">
									<input type="reset" value="Reset" class="btn btn-default btn-reset-btcl" />
									<input type="button" value="Submit"
										class="btn btn-success submit-btn btn-submit-btcl" />
								</div>
							</form>
							</div>
<!-- 							category id div finishes -->
						</div>
						<!-- /add more cost based on current active cost -->
			</div>
		</div>
	</div>