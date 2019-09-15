

<div id="btcl-application" v-cloak="true">
	<btcl-body title="Change IP PORT" subtitle="Application" :loader="loading">

		<btcl-portlet>

			<btcl-field title="Client">
				<lli-client-search :client.sync="application.client"
								   :callback="clientSelectionCallback">
				</lli-client-search>
			</btcl-field>

			<btcl-field title="Connection">
				<multiselect v-model="connection"
							 :options="connectionOptionListByClientID"
							 track-by=ID label=label
							 :allow-empty="false"
							 :searchable=true
							 open-direction="bottom">
				</multiselect>
			</btcl-field>



			<div style="padding-top:30px;">
				<button type="submit" class="btn green-haze btn-block" @click="getConnectionInfo">Submit</button>
			</div>

		</btcl-portlet>
		<div v-if ="isConnectionInfo == true">
				<div class="portlet-title">
					<div class="caption" align="center"
						 style="background: #bfbfbf;padding: 10px;">IP and Port Information
					</div>
					<div class="tools">
						<a href="javascript:;" data-original-title="" title="" class="collapse"></a>
					</div>
				</div>
				<div class="form-body">

					<btcl-portlet>

						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="col-md-6 col-sm-6 col-xs-6">
								<h1 style="font-size:14px;">Existing Ports, Switches, Vlans for Connection {{application.connectionInfo.name}}</h1>
							</div>
						</div>

						<div v-for="(office,index) in application.connectionInfo.offices">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="col-md-6 col-sm-6 col-xs-6">
									<h1 style="font-size:14px;">Information for {{office.officeName}}</h1>
								</div>
							</div>
							<table class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th>Pop Name</th>
									<th>Switch</th>
									<th>Port</th>
									<th>Vlan</th>
									<th>Edit</th>
								</tr>
								</thead>
								<tbody>
								<tr v-for="row in office.loops">
									<td>{{row.popName}}</td>
									<td>{{row.switchName}}</td>
									<td>{{row.portName}}
										({{application.connectionInfo.portTypeMap[row.portID]}})
										({{application.connectionInfo.usedNotUsedMap[row.portID]}})

									</td>
									<td>{{row.vlanName}}
										({{application.connectionInfo.vlanTypeMap[row.VLANID]}})
										({{application.connectionInfo.usedNotUsedMap[row.VLANID]}})

									</td>
									<td><a @click="showLoopEditModal(row)">Edit</a></td>

								</tr>
								</tbody>
							</table>
						</div>


					</btcl-portlet>

					<jsp:include page="lli-port-assign-modal.jsp"/>


					<div v-if="application.connectionInfo.ipByConId.length>0"
						 style= "background:white;" >
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="col-md-6 col-sm-6 col-xs-6">
								<h1 style="font-size:14px;">Existing IP Information</h1>
							</div>
						</div>
						<div id="existingIpInfo">

							<table id="thirdTable"
								   class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th>Purpose</th>
									<th>Version</th>
									<th>Usage Type</th>
									<th>From IP</th>
									<th>To IP</th>
									<th>Edit</th>
								</tr>
								</thead>
								<tbody>
								<tr v-for="row in application.connectionInfo.ipByConId">
									<td style="max-width:250px; word-wrap: break-word">{{row.purpose}}</td>
									<td>{{row.version}}</td>
									<td>{{row.usageType}}</td>
									<td>{{row.fromIP}}</td>
									<td>{{row.toIP}}</td>
									<td><a @click="showAssignModal(row)">Edit</a></td>
								</tr>
								</tbody>
							</table>
						</div>
					</div>

					<jsp:include page="lli-ip-assign-modal.jsp"/>

				</div>
			</div>
	</btcl-body>
</div>
<script src="../change-ip-port/lli-change-ip-port.js"></script>


