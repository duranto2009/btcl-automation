<%@page import="common.ModuleConstants"%>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	Integer moduleID = Integer.parseInt(request.getParameter("moduleID"));
%>
<html:base />
<form class="form-horizontal" id="ReportForm"
			action="<%=request.getContextPath()%>/report/GetClientReportTableData.do">
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-cubes"></i>Report Generator
		</div>
	</div>
	<div class="portlet-body form">
		
			<input type="hidden" name="moduleID" value="<%=moduleID%>">
			<input type="hidden" id="countURL" name="countURL" value="report/GetClientTotalResultCount.do">
			<div class="form-body">
				<div class="row">
				</div>
				<div class="row">
					<hr>
				</div>
				<div class="row">
					<div class="col-md-3" id="criteria"></div>

					<div class="col-md-6" id="display"><%@include
							file="reportClientDisplayDiv.jsp"%>
					</div>
					<div class="col-md-3" id="orderby"></div>
				</div>
				<div id="searchCriteria"></div>


				<div class="custom-form-action">
					<div>
						<div class="text-center">
							<button type="reset" class="btn blue-hoki">Reset</button>
							<button id="defaultLoad" type="submit" class="btn blue">Submit</button>
						</div>
					</div>
				</div>
			</div>
	</div>
</div>

<div class="portlet box portlet-btcl light navigator">
	<div class="portlet-body" style="display:none">	
		<div class="row text-center">
				<nav aria-label="Page navigation">
					<ul class="pagination" style="margin: 0px;">
						<li style="float:left;"><i class="hidden-xs">Record Per Page</i>&nbsp;&nbsp;
							<input type="text" class="custom-form-control" name="RECORDS_PER_PAGE" style="height: 34px;" placeholder="" value="100000000" />&nbsp;&nbsp;&nbsp;&nbsp;
						</li>
						<li class="page-item"><a class="page-link" href="" id="firstLoad"
							aria-label="First" title="Left"> <i
								class="fa fa-angle-double-left" aria-hidden="true"></i> <span
								class="sr-only">First</span>
						</a></li>
						<li class="page-item"><a class="page-link" id="previousLoad"
							href="" aria-label="Previous" title="Previous"> <i
								class="fa fa-angle-left" aria-hidden="true"></i> <span
								class="sr-only">Previous</span>
						</a></li>

						<li class="page-item"><a class="page-link" href="" id="nextLoad"
							aria-label="Next" title="Next"> <i class="fa fa-angle-right"
								aria-hidden="true"></i> <span class="sr-only">Next</span>
						</a></li>
						<li class="page-item"><a class="page-link" href="" id="lastLoad"
							aria-label="Last" title="Last"> <i
								class="fa fa-angle-double-right" aria-hidden="true"></i> <span
								class="sr-only">Last</span>
						</a></li>
						<li>&nbsp;&nbsp;<i class="hidden-xs">Page </i><input
							type="text" class="custom-form-control " name="pageno" value='' style="height: 34px;"
							size="15"> <i class="hidden-xs">of</i>&nbsp;&nbsp;<input
							type="text" class="custom-form-control " name="tatalPageNo" value='' style="height: 34px;"
							size="15" disabled> <input type="hidden" name="totalRecord"><input type="submit" id="forceLoad"
							class="btn btn-circle  btn-sm green-haze btn-outline sbold uppercase"
							value="GO" />
						</li>
					</ul>
				</nav>
		</div>
	</div>
</div>
</form>
<div class="portlet box portlet-btcl light">
	<div class="portlet-body">
		<!-- Dynamically loaded report table -->
		<div class="row" id="report-div">
			<div class="col-md-12">
				<div class="portlet light bordered">
					<div class="portlet-title">
						<div class="caption font-dark">
							<i class="icon-settings font-dark"></i> <span
								class="caption-subject bold uppercase">Report</span>
						</div>
						<div class="tools"></div>
					</div>
					<div class="portlet-body">
						<div class="table">
							<table class="table table-striped " id="reportTable"
								style="width: 100%">

								<thead>
									<tr>
										<th></th>
									</tr>
								</thead>

								<tbody>
								</tbody>

							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Dynamically loaded report table -->
	</div>
</div>