<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="domain.DomainContants"%>
<%@page import="domain.DomainPackageDetails"%>
<%
List<DomainPackageDetails> packageList=domainPackageList;
Collections.sort(packageList);
%>
<div class="row" id="primaryDomainPackage">
	<div class="col-md-12">
		<h4>Package Details</h4>
		<input type="hidden" name="numberOfYear" id="numberOfYear">
		<input type="hidden" name="checkedBuy" value="false" id="checkedBuy">
		<div class="table-responsive">
			<table
				class="table table-striped table-bordered table-hover table-checkable dataTable no-footer"
				aria-describedby="datatable_orders_info" role="grid">
				<thead>
					<tr role="row">
						<th style="color: black; text-align: center;">Year</th>
						<th style="color: black; text-align: center;">Price(BDT)</th>
						<th style="color: black; text-align: center;">Buy&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<%							
					for (DomainPackageDetails domainPackageDetails : packageList) {
						if(domainPackageDetails.getYear()<DomainContants.MIN_DOMAIN_BUY_YEAR){
							continue;
						}
					%>
					<tr role="row" class="filter">
						<td colspan="1" style="text-align: center; padding-top: 13px;"><%=domainPackageDetails.getYear()%></td>
						<td colspan="1" style="text-align: center; padding-top: 13px;"><%=domainPackageDetails.getCost()%></td>
						<td style="text-align: center;">
							<div class="margin-bottom-5">
								<button  name="numberOfYear" value="<%=domainPackageDetails.getYear()%>" class="btn btn-submit-btcl uppercase bold  my-group-button buyRequest"  type="submit" >
									Buy Now
								</button>
							</div>
						</td>
					</tr>
					<% } %>
				</tbody>
			</table>
		</div>
	</div>
</div>