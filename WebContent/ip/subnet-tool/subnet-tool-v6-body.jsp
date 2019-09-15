<div id=btcl-application>
	<btcl-body title="Subnet Tool" subtitle="IP Management">
		<div class=row>
				<btcl-portlet>
					<h3 style="text-align:center">Input an IPv6 address and the subnet size in slash notation</h3>
					<btcl-input title="IPv6 Address(slash)" :text.sync="ipv6address"></btcl-input>
					<button type="button" class="btn btn-block green-haze" @click="ipv6calc">Calculate</button>
					<hr>
					<btcl-portlet v-cloak>
						<btcl-info title="Compressed Address" :text.sync="ipv6compressedAddress"></btcl-info>
						<btcl-info title="Expanded Address" :text.sync="ipv6expandedAddress"></btcl-info>
						<btcl-info title="Prefix" :text.sync="prefix"></btcl-info>
						<btcl-info title="Range" :text.sync="range"></btcl-info>
						<btcl-info title="Number of /64s" :text.sync="slash64"></btcl-info>
					</btcl-portlet>
				</btcl-portlet>
		</div>
		<div class=row>	
			<btcl-portlet>
					<h3 style="text-align:center">Select a number of subnets or a subnet size to divide the above IPv6 address into</h3>
				<div class="row">
					<div class="col-sm-6">
						<btcl-input title="Number of Subnets" :text.sync="requiredSubnets"></btcl-input>
						<button type="button" class="btn btn-block green-haze" @click="subnet_subnet">Calculate</button>
						<hr>
						<btcl-portlet v-cloak>
							<div style="height: 20px;"><p>{{subnetted}}</p><hr></div>
							
							<ul style="overflow-y:scroll;height: 300px;list-style:none">
								<li v-for="item in subnettedList">{{item}}</li>
							</ul>
						</btcl-portlet>		
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label class="control-label col-sm-4">Subnet Size</label>
							<div class="col-sm-6">
								<select v-model="requiredSlashes" v-cloak class="form-control pull-right">
									<option value="0">Select Subnet Size</option>
									<option :value="i+7" v-for="i in 121">/{{i+7}}</option>
								</select>
							</div>
						</div>
						<button type="button" class="btn btn-block green-haze" @click="subnet_slashes">Calculate</button>
						<hr>
						<btcl-portlet v-cloak>
							<div style="height: 20px;"><p>{{slashed}}</p><hr></div>
							<ul style="overflow-y:scroll;height: 300px;list-style:none">
								<li v-for="item in slashedList">{{item}}</li>
							</ul>
						</btcl-portlet>
					</div>
				</div>
			</btcl-portlet>
		</div>
	</btcl-body>
</div>
<script src="../subnet-tool/BigInteger.min.js"></script>
<script src="../subnet-tool/subnet-tool-v6.js"></script>
