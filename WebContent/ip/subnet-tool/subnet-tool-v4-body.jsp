
<div id=btcl-application>
	<btcl-body title="Subnet Tool" subtitle="IP Management">
		<btcl-portlet>
			<btcl-input title="IP Address (Host or Network)" :text.sync="address" :required=true></btcl-input>
			<btcl-input title="Netmask (i.e. 24)" :text.sync="netmask" :required=true></btcl-input>
			<btcl-input title="Netmask for subnet (Optional)" :text.sync="subnet"></btcl-input>
			<button type="button" class="btn btn-block green-haze" @click="showSubnetList">Show Subnet</button>
		</btcl-portlet>
		<div class=row>
			<div class=col-sm-6>
				<btcl-portlet v-cloak>
					<btcl-info title="Netmask" :text="subnetMask"></btcl-info>
					<btcl-info title="Network Address" :text="networkAddress"></btcl-info>
					<btcl-info title="Broadcast Address" :text="broadcastAddress"></btcl-info>
					<btcl-info title="Hosts/Net" :text="numberOfHosts"></btcl-info>
					<btcl-info title="Type" :text="type"></btcl-info>
					<btcl-info title="Class" :text="ipClass"></btcl-info>
				</btcl-portlet>
			</div>
			<div class=col-sm-6 >
				<btcl-portlet v-cloak>
					<ul style="overflow-y:scroll;height: 300px;list-style:none">
						<li v-for="item in subnetList" >{{ item }}</li>
						<li v-if="subnetList.length">Showing Total Number of Subnets: {{subnetList.length - 2*subnet.split(",").length}}</li>	
					</ul>
<!-- 					<btcl-info title="Network Address" :text="networkAddress"></btcl-info> -->
<!-- 					<btcl-info title="Broadcast Address" :text="broadcastAddress"></btcl-info> -->
<!-- 					<btcl-info title="Hosts/Net" :text="numberOfHosts"></btcl-info> -->
<!-- 					<btcl-info title="Type" :text="type"></btcl-info> -->
<!-- 					<btcl-info title="Class" :text="ipClass"></btcl-info> -->
				</btcl-portlet>
			</div>
		</div>
	</btcl-body>
</div>
<script src="../subnet-tool/subnet-tool-v4.js"></script>
