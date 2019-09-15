<div id="btcl-application" v-cloak="true">
	<btcl-body title="Shift Bandwidth" subtitle="Application" :loader="loading">
		<btcl-form :action="contextPath + 'lli/application/shift-bandwidth.do'"
				   :name="['application']"
				   :form-data="[application]"
				   :redirect="goView"
				   v-bind:is-json="true"

		>
        	<btcl-portlet>
           		<btcl-field title="Client" :required=true>
           			<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback"></lli-client-search>
           		</btcl-field>
				<!-- && application.connectionType.ID!=2" -->
				<btcl-field title="Shifting Bandwidth Type" :required=true>
					<multiselect v-model="application.type" :options="[{ID:1,label:'Bandwidth Transfer'},{ID:2,label:'Bandwidth Transfer and Create Connection'}]"
								 track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
					</multiselect>
				</btcl-field>
				<template v-if="application.client">
					<btcl-field v-if="application.client.registrantType==1 && application.type.ID==2"
								title="Want's to Pay DN(Connection Charge)"
								:required=true>
						<multiselect v-model="application.skipPayment"
									 :options="[{ID:1,label:'First Month Bill'},{ID:0,label:'Before Connection'}]"
									 track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
						</multiselect>
					</btcl-field>
				</template>



           		<btcl-field title="Source Connection" :required=true>
           			<multiselect v-model="application.sourceConnection" :options="connectionOptionListByClientID"
			        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
           		</btcl-field>
				<btcl-info v-if="application.hasOwnProperty('sourceConnection')" title="Source Existing Bandwidth (Mbps)" :text.sync="application.sourceConnection.object"></btcl-info>

				<btcl-field title="Destination Connection" v-if="application.type.ID==1" :required=true>
           			<multiselect v-model="application.destinationConnection" :options="dstconnectionOptionListByClientID"
			        	track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
			        </multiselect>
           		</btcl-field>
				<btcl-info v-if="application.hasOwnProperty('destinationConnection') && application.type.ID==1" title="Destination Existing Bandwidth (Mbps)" :text.sync="application.destinationConnection.object"></btcl-info>

				<btcl-input  :required=true title="Shifted Bandwidth (Mbps)" :text.sync="application.bandwidth"></btcl-input>



				<btcl-field title="Connection Type" v-if="application.type.ID==2" :required=true>
					<select class="form-control" v-model="application.connectionType">
						<option v-if="type.ID!=2" v-for="type in application.connectionTypes" :value="type" >
							{{type.label}}
						</option>
					</select>
				</btcl-field>


				<%--<btcl-field title="Connection Type" >--%>
					<%--<connection-type :data.sync=application.connectionType :client="application.client"></connection-type>--%>
				<%--</btcl-field>--%>
				<%--<btcl-field v-if="application.client.registrantType==1" title="Want's to Pay DN(Connection Charge)">--%>
					<%--<multiselect v-model="application.skipPayment" :options="[{ID:0,label:'First Month Bill'},{ID:1,label:'Instantly'}]"--%>
								 <%--:track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">--%>
					<%--</multiselect>--%>
				<%--</btcl-field>--%>
				<btcl-field title="Zone" v-if="application.type.ID==2"  :required=true>
					<lli-zone-search :client.sync="application.zone"></lli-zone-search>
				</btcl-field>
				<btcl-field title="Loop Provider" v-if="application.type.ID==2"  :required=true>
					<multiselect v-model="application.loopProvider" :options="[{ID:1,label:'BTCL'},{ID:2,label:'Client'}]"
								 track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
					</multiselect>
				</btcl-field>
				<%--<btcl-field v-if="application.connectionType && application.connectionType.ID==2" title="Duration (Days)">--%>
					<%--<input type=number class="form-control" v-model=application.duration>--%>
				<%--</btcl-field>--%>





				<btcl-input title="Description" :text.sync="application.description"></btcl-input>
				<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>

           		<btcl-field title="Suggested Date" :required=true><btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker></btcl-field>

				<btcl-bounded v-if="application.type.ID==2" v-for="(office,officeIndex) in application.officeList" :title="'Office '+(officeIndex+1)"
							  :key="officeIndex">
					<btcl-input title="Connection Office Name" :text.sync="office.officeName"  :required=true></btcl-input>
					<btcl-input title="Connection Office Address" :text.sync="office.officeAddress"  :required=true></btcl-input>

<%--					commented by bony--%>
<%--					<div align=right v-if="officeIndex!=0">--%>
<%--						<button type=button--%>
<%--						&lt;%&ndash;@click="deleteLocalLoop(content, officeIndex, officeIndex, $event)"&ndash;%&gt;--%>
<%--								@click="deleteOffice(application,officeIndex, $event)"--%>
<%--								class="btn red btn-outline">--%>
<%--							Remove this Connection Office--%>
<%--						</button>--%>
<%--					</div>--%>
				</btcl-bounded>
				<div align=right v-if="application.type.ID==2">
<%--					commented by bony--%>
<%--					<button type=button--%>
<%--					&lt;%&ndash;@click="addLocalLoop(office,content.bandwidth, $event)" &ndash;%&gt;--%>
<%--							@click="addOffice(application,  $event)"--%>
<%--							class="btn green-haze btn-outline">--%>
<%--						Add More Connection Office--%>
<%--					</button>--%>
				</div>

           		
           	</btcl-portlet>
           	
		</btcl-form>
	</btcl-body>
</div>

<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=lli-application-submission.js></script>


