<div class="row ">
	<!-- Domain Search Result and Buy -->
	<div class="col-md-12">
		<%
			if (domainPackageList != null) {
		%>
		<div class="row">
			<div class="col-md-12">
				<div class="portlet box" style="border: 1px solid #e6e9ec !important; padding: 15px !important;">
					<!-- Domain Buy Form -->
					<!--form method="post" action="../../DomainAction.do?mode=buyDomain"
						id="fileupload" enctype="multipart/form-data"-->
						<input type="hidden" name="domainAddress" value="<%=domainDTO.getDomainAddress()%>">

						<!-- Domain Buy : General Information -->
						<%
							if ((isFileRequired != null && isFileRequired)) {
						%>
							 <%@ include file="domain-buy-file-upload.jsp"%> 
						<%
						}
						%>
						<!-- /Domain Buy : General Information -->

						<!-- Domain Buy : Server Information -->
						<%@ include file="domain-server-info.jsp"%>
						<!-- /Domain Buy : Server Information -->

						<!-- Domain Buy : Package Details -->
						<%@ include file="primary-domain-package.jsp"%>
						<!-- /Domain Buy :Package Details -->
					<!-- /form-->
					<!-- /Domain Buy Form -->
				</div>
			</div>
		</div>
		<%}%>
	</div>
</div>