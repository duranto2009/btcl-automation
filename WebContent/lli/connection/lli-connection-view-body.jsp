<div id="btcl-application" v-cloak="true">
	
	<btcl-body :title="'LLI : '+connection.name" :loader="loading">
		<%--<div class="row" id="loading" style="text-align: center">
			<i class="fa fa-spinner fa-spin fa-5x"></i>
		</div>--%>
		<div class=row>
			<div class=col-md-8>
				<btcl-portlet title=Connection>
					<btcl-info title="Client" :text=connection.client.label></btcl-info>
					<btcl-info title="Connection Name" :text=connection.name></btcl-info>
		       		<btcl-info v-if="connection.status.ID == 1 || connection.status.ID == 2" title="Bandwidth (Mbps)" :text="connection.bandwidth"></btcl-info>
		       		<btcl-info title="Connection Type" :text="connection.connectionType.label"></btcl-info>
		            <btcl-info title="Active From" :text="connection.activeFrom" v-bind:is-date="true"></btcl-info>
		            <btcl-info title="Status" :text="connection.status.label"></btcl-info>
		   		</btcl-portlet>

		       	<btcl-portlet :title="'Office '+(officeIndex+1)" v-for="(office,officeIndex) in connection.officeList">
		       		<btcl-info title="Office Name" :text=office.name></btcl-info>
		       		<btcl-info title="Address" :text="office.address"></btcl-info>

		         	<btcl-bounded v-for="(localLoop, localLoopIndex) in office.localLoopList" :title="'Local Loop '+(localLoopIndex+1)">
	       				<inventory-autocomplete :itemid=localLoop.portID
                                                :vlanid=localLoop.vlanID
                                                readonly>
                        </inventory-autocomplete>
	       				<btcl-info title="Loop Provider" :text="parseFloat(localLoop.btclDistance) + parseFloat(localLoop.OCDistance) > 0 ? 'BTCL' : 'Client'"></btcl-info>
	       				<btcl-info title="Client Distance" :text=localLoop.clientDistance></btcl-info>
	       				<btcl-info title="BTCL Distance" :text=localLoop.btclDistance></btcl-info>
	       				<btcl-info title="O/C Distance" :text=localLoop.OCDistance></btcl-info>
	       				<btcl-info title="OFC Type" :text=localLoop.ofcType.label></btcl-info>
					</btcl-bounded>
		       	</btcl-portlet>
				<%--<btcl-portlet title="IP Information">--%>
					<%--<btcl-bounded v-for="(ip, index) in ipList" title="">--%>
						<%--<btcl-info title="From IP" :text="ip.fromIP"></btcl-info>--%>
						<%--<btcl-info title="To IP" :text="ip.toIP"></btcl-info>--%>
						<%--<btcl-info title="Active From" :text="ip.activeFrom"  date="true" ></btcl-info>--%>
						<%--<btcl-info title="Type" :text="ip.type"></btcl-info>--%>
						<%--<btcl-info title="Status" :text="ip.status"></btcl-info>--%>
						<%--<btcl-info title="Usage Type" :text="ip.usageType"></btcl-info>--%>
						<%--<btcl-info title="Version" :text="ip.version"></btcl-info>--%>
					<%--</btcl-bounded>--%>
				<%--</btcl-portlet>--%>
				<btcl-portlet title="IP Information" v-if="ipList.length>0">
					<btcl-bounded title="">
						<div class="form-body">
							<div class="form-group"
								 style="background: rgb(220, 220, 220);margin-left: 1px;margin-top: 7px;border-radius: 9px;padding:5px;margin-right:2px">
								<div class="col-md-3"><p style="text-align: center;">From IP</p></div>
								<div class="col-md-3"><p style="text-align: center;">To IP</p></div>
								<div class="col-md-3"><p style="text-align: center;">Active From</p></div>
								<%--<div class="col-md-2"><p style="text-align: center;">Type</p></div>--%>
								<%--<div class="col-md-2"><p style="text-align: center;">Status</p></div>--%>
								<%--<div class="col-md-2"><p style="text-align: center;">Version</p></div>--%>
								<div class="col-md-3"><p style="text-align: center;">Usage Type</p></div>
							</div>
							<div class="form-group" v-for="(ip, index) in ipList">
								<div class="col-md-3">
									<p style="text-align: center;border: 1px solid;">
										{{ip.fromIP}}</p>
								</div>
								<div class="col-md-3"><p style="text-align: center;border: 1px solid;">{{ip.toIP}}</p></div>

								<div class="col-md-3"><p style="text-align: center;border: 1px solid;">
									{{new Date(parseInt(ip.activeFrom)).toLocaleDateString("ca-ES")}}
									</p></div>
								<%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;">{{ip.type}}</p></div>--%>
								<%--<div class="col-md-2"><p style="text-align: center;border: 1px solid;">{{ip.version}}</p></div>--%>
								<div class="col-md-3"><p style="text-align: center;border: 1px solid;">{{ip.usageType}}</p></div>

							</div>
						</div>
					</btcl-bounded>
				</btcl-portlet>

			</div>
			<div class=col-md-4>
				<btcl-portlet title=History>
					<connection-history></connection-history>
		   		</btcl-portlet>
			</div>
		</div>
	</btcl-body>
	
</div>

<script>
	/*todo loading icon might here implement through vue componant*/
	var tid = setInterval( function () {
		if ( document.readyState !== 'complete' ) return;
		clearInterval( tid );
		$("#loading").hide();
	}, 500 );
	var requestID = "<%=request.getParameter("id")%>";
	if(requestID =="null") {window.location.href=context;}
</script>
<script src=../connection/lli-connection-components.js></script>
<script src=lli-connection-view.js></script>



