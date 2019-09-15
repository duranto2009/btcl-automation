<input type="hidden" name="secondaryPackageID" value="<%=secondaryDomainPackage.getID() %>">
<div class="row" id="secondaryDomainPackage" style="display: none;">
	<div class="col-md-12">
		<h4>Package Details</h4>
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
					<%							
					for (DomainPackageDetails domainPackageDetail : secondaryDomainPackageList) {
					%>
					<tr role="row" class="filter">
						<td colspan="1" style="text-align: center; padding-top: 13px;"><%=domainPackageDetail.getYear()%></td>
						<td colspan="1" style="text-align: center; padding-top: 13px;"><%=domainPackageDetail.getCost()%></td>
						<td style="text-align: center;">

							<div class="margin-bottom-5">
								<button type="submit" value="<%=domainPackageDetail.getYear()%>"
									class="btn green-jungle btn-sm"
									id="button_<%=domainPackageDetail.getID()%>">Buy Now</button>
							</div>
						</td>
					</tr>
					<%
					}
					%>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
</div>