
<style>
	#left-panel {
		border-right: 2px solid;
	}
	#right-panel {
		border-left : 2px solid;
	}
	h1 {
		text-align: center;
	}
	text-area.form-control {
		min-width: 90%;
	}
	.no-border {
		border: none;
	}
</style>
<div id="btcl-application">
	<btcl-body title="LLI" subtitle="Demand Note">
		<div class=row>
			<div class="col-md-6" id=left-panel>
				<h1> Edit Demand Note</h1>
				<hr>
				<div class="form-horizontal">
					<div class="form-body">
						<div class=form-group>
							<textarea v-model="header" class="form-control" placeholder="Header" ></textarea>
						</div>
						<div class=form-group>
							<label class="control-label col-md-3">Client Name</label>
							<div class=col-md-9>
								<input type="text" class="form-control" id="client-name" name="client-name" required>
							</div>
						</div>
						<div class=form-group>
							<label class="control-label col-md-3">Client Name</label>
							<div class=col-md-9>
								<input type="text" class="form-control" id="connection" name="connection" required>
							</div>
						</div>
						<div class=form-group>
							<textarea v-model="footer" class="form-control" placeholder="Footer" ></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6" id=right-panel> 
				<h1> Preview</h1>
				<hr>
				<div class="form-horizontal">
					<div class="form-body">
						<div class="form-group">
							<textarea class="form-control no-border" style="overflow-x: auto">{{header}}</textarea>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">Client Name</label>
							<div class=col-md-9>
								<input type="text" class="form-control no-border" id="client-name"  name="client-name">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">Client Name</label>
							<div class=col-md-9>
								<input type="text" class="form-control no-border" id="connection"  name="connection">
							</div>
						</div>
						<div class="form-group">
							<textarea class="form-control no-border" >{{footer}}</textarea>
						</div>
					</div>
				</div>
			</div>
		</div>
	</btcl-body>
</div>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script>
	var globalData = {
			header:"",
			footer:"",
			'client-name':"",
			connection:"",
	};
	var pageData = {};
	axios.get(context + 'lli/preview/dn.do')
	.then (function (response) {
		pageData.firstParameter = response.data.payload;
	})
	.catch (function (error) {
		console.log(error);
	});
	
	axios.get(context + 'lli/preview/an.do', {
		params :{
			id : 1
		}
	})
	.then(function (response){
		pageData.domainDTO = response.data.payload;
	})
	.catch(function(error){
		console.log(error);
		
	});
	
	console.log(pageData);
	
</script>