<%@page import="java.util.*, costConfig.*, common.*, util.*"%>

<div class="form-horizontal"  id="edit-cost-chart-<%=tableID%>" style="display:none">
	<hr>
	<form method="post" action="../../CostChart/edit.do" id="costing-form-<%=tableID %>">
		<input type=hidden name="tableID" value="<%=tableID%>" />
		<input type=hidden name="moduleID" value="<%=moduleID %>" />
		<input type="hidden" name="categoryID" value="<%=categoryID %>">
		
		<div class="dynamic-hidden-field-row-<%=tableID %>">
		<%for (int rowIdx = 0; rowIdx < rowList.size(); rowIdx++) {%>
			<input type=hidden name="rowIDs" value='<%=rowList.get(rowIdx).getID()%>' />
			<input type=hidden name="rowIndex" data-lower=<%=rowList.get(rowIdx).getLowerRange() %> data-upper=<%=rowList.get(rowIdx).getUpperRange() %> value='<%=rowList.get(rowIdx).getIndex()%>' /> 
		<%}%>
		</div>
		<div class="dynamic-hidden-field-col-<%=tableID %>">
		<%for (int columnIdx = 0; columnIdx< columnList.size(); columnIdx++) {%>
			<input type=hidden name="lowerRangeKm" value='<%=columnList.get(columnIdx).getLowerRange()%>' />
			<input type=hidden name="upperRangeKm" value='<%=columnList.get(columnIdx).getUpperRange()%>' />
		<% }%>
		</div>
		<div class="table-responsive" id='div-render-'<%=tableID %>>
			<button class="btn btn-default pull-left cost-chart-restore-btn" data-table-id="<%=tableID%>" type=button>Restore</button>
			<button class="btn btn-default col-remove pull-right" type="button" data-table-id="<%=tableID%>" 
				style="margin-bottom: 15px"> <i class="fa fa-times color-error" aria-hidden="true"></i> 
			</button>
			<button class="btn btn-search-btcl col-add pull-right"  data-table-id="<%=tableID%>" type="button" style="margin-right: 5px; margin-bottom: 15px"> Column Add&nbsp;
				<i style="color: #fff" class="fa fa-arrow-right" aria-hidden="true"></i>
			</button>
			<table id="costingTable-<%=tableID%>" class="table table-hover table-bordered table-striped table-edit" style="margin-top: 30px; text-align: center">
				<thead>
					<tr>
						<td class="td_viewheader">Sl</td>
						<td class="align-center td_viewheader"><%=moduleUnitList[0]+"(" +moduleUnitList[1]+ ")" %></td>
					<%
					for (int columnIdx = 0; columnIdx< columnList.size(); columnIdx++) {
						if (columnList.get(columnIdx).getUpperRange() != Integer.MAX_VALUE) {
					%>
						<td class="align-center td_viewheader">
						<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="<%=columnList.get(columnIdx).getLowerRange() %>" 
							data-upper="<%=columnList.get(columnIdx).getUpperRange()%>" accesskey="<%=columnIdx%>" data-table-id='<%=tableID%>' 
							style='display:none'> <i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit
						</a>
							<%=moduleUnitList[2]+" " + (columnIdx + 1)%>
							<br>
							<%="(" + columnList.get(columnIdx).getLowerRange() + "-" + columnList.get(columnIdx).getUpperRange() + " "+moduleUnitList[3]+") "+moduleUnitList[4]%>
						</td>
						<%} else {%>
						<td class="align-center td_viewheader">
							<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="<%=columnList.get(columnIdx).getLowerRange() %>" 
								data-upper="<%=columnList.get(columnIdx).getUpperRange()%>" accesskey="<%=columnIdx%>" data-table-id='<%=tableID%>' 
								style='display:none'> <i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit
							</a>
							<%=moduleUnitList[2]+" "+ (columnIdx + 1)%>
							<br>
							<%="(" + columnList.get(columnIdx).getLowerRange() + "+ " +moduleUnitList[3] + ") " + moduleUnitList[4]%>
						</td>
						<%
							}
						}
						%>
					</tr>
				</thead>
				<tbody>
				<%
					cellIdx = 0;
					for (int rowIdx= 0; rowIdx< rowList.size(); rowIdx++) {
						int upperRange = rowList.get(rowIdx).getUpperRange();
				%>
					<tr>
						<td><%=(rowIdx + 1) + "."%></td>
						<td>
							<input type="text" class="form-control lower-range-mb" id="lowerRangeMb<%=rowIdx%>" style="width: 35%; float: left" 
								name="lowerRangeMb" value="<%=rowList.get(rowIdx).getLowerRange()%>" />
							<span>-</span>
							<input type="text" class="form-control upper-range-mb" id="upperRangeMb<%=rowIdx%>" style="width: 35%; float: right" 
								name="upperRangeMb" value=<%=upperRange!=Integer.MAX_VALUE?upperRange:"~"%> />
						</td>
						<%
						for (int columnIdx = 0; columnIdx< columnList.size(); columnIdx++) {
						%>
						<td>
							<input type="text" class="form-control" name="cost" value='<%=cellList.get(cellIdx++).getValue()%>' />
							<input type="hidden" name="cellIDs" value='<%=cellList.get(cellIdx- 1).getID()%>' />
						</td>
						<%}%>
					</tr>
					<%} %>
				</tbody>
			</table>
		</div>
		<!-- table responsive div end -->
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
		<button class="btn btn-search-btcl row-add" data-table-id='<%=tableID%>' type="button">
			<i class="fa fa-arrow-down" style="color: #fff"
				aria-hidden="true"></i>&nbsp;Row Add
		</button>
		<button class="btn btn-default row-remove" data-table-id='<%=tableID%>' type="button">
			<i class="fa fa-times" style="color: #d36b7e"
				aria-hidden="true"></i>
		</button>			
		<div class="text-center">
			<button type="button" class="btn btn-default btn-reset-btcl" >Reset</button>
			<button type="button" class="btn btn-success submit-btn btn-submit-btcl" data-table-id=<%=tableID %> >Submit</button>
		</div>
	</form>
	<!-- form end -->
</div>
<br><br>