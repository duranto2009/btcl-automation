<div class="portlet box green">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-history"></i>History
		</div>
	</div>
	<div class="portlet-body form">

		<form method="POST" class="form-horizontal" id="historySearchForm">
			<div class="form-body">
				<div class="input-group history-search">
					<span class="input-group-addon"><i class="fa fa-user"></i></span><input class="form-control spinner" id="username"
						type="text" placeholder="Name">
				</div>
				<div class="input-group history-search">
					<span class="input-group-addon"><i class="fa fa-file-o"></i></span><input class="form-control spinner"
						id="innerContent" type="text" placeholder="Inner Content">
				</div>
				<div class="input-group history-search">
					<span class="input-group-addon"><i class="fa fa-calendar"></i></span><input class="form-control spinner"
						type="text" id="datepicker-from" placeholder="From">
				</div>
				<div class="input-group history-search">
					<span class="input-group-addon"><i class="fa fa-calendar"></i></span><input class="form-control spinner"
						type="text" id="datepicker-to" placeholder="To">
				</div>
				<button type="submit" class="btn btn-column btn-outline  btn-block sbold uppercase" id="search-history">
					<b>Search</b>
				</button>
			</div>
		</form>
	</div>
	<div class="portlet-body">
		<div class="timeline">
			<!-- TIMELINE ITEM -->
			<div class="timeline-item" id="load-history">
				<input type="hidden" name="vpnLinkID" id="vpnLinkID" value="${vpnLink.ID}" />
				<!-- load history here -->
			</div>
			<input type="hidden" id="next-count-history" value="0">

			<hr>
			<a class="btn btn-column-light btn-block" id="load-more-history-btn"><b>Loading More<!-- History --></b></a>
			<!-- END TIMELINE ITEM -->
		</div>
	</div>
</div>