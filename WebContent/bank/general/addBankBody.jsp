<div class="row">
	<div class="col-md-12">
		<div class="portlet box portlet-btcl">

			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-search-plus"></i> Add Bank
				</div>
				<div class="tools">
					<a class="collapse" href="javascript:;" data-original-title=""
						title=""> </a>
				</div>
			</div>

			<div class="portlet-body form">
				<form class="form-horizontal" id=""
					action="<%=request.getContextPath()%>/Bank/AddNewBank.do"
					method="post">
					<div class="form-body">
						<div class="form-group">
							<label for="cmDepartment" class="col-sm-2 control-label">Bank Name<span class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<input id="bankName" type="text" class="form-control"
									placeholder="Sonali Bank Ltd." name="bankName" required> 
							</div>
						</div>
						<div class="form-group">
							<label for="cmDepartment" class="col-sm-2 control-label">Bank Code<span class="required" aria-required="true"> * </span></label>
							<div class="col-sm-10">
								<input id="bankCode" type="text" class="form-control"
									placeholder="SonaliBank" name="paclBankCode" required> 
							</div>
						</div>
						<div class="form-group">
							<label for="cmDepartment" class="col-sm-2 control-label">Username</label>
							<div class="col-sm-10">
								<input id="bankUsername" type="text" class="form-control"
									placeholder="" name="paclUserName"> 
							</div>
						</div>
						<div class="form-group">
							<label for="cmDepartment" class="col-sm-2 control-label">Password</label>
							<div class="col-sm-10">
								<input id="password" type="text" class="form-control"
									placeholder="" name="paclPassword"> 
							</div>
						</div>
					</div>
					<div class="form-actions right">
						<button class="btn btn-reset-btcl" type="reset">Reset</button>
						<button class="btn btn-submit-btcl" type="submit">Submit</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>