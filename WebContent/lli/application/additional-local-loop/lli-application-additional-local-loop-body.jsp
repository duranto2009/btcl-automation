<div id="btcl-application" v-cloak="true">

		<btcl-body title="Additional Port/Local Loop" subtitle="Application" :loader="loading">
			<%--<btcl-form :action="contextPath + 'lli/application/additional-local-loop.do'" :name="['application']" :form-data="[application]" :redirect="goView">--%>
				<btcl-portlet>

					<btcl-field title="Client" required="true">
						<lli-client-search :client.sync="application.client" :callback="clientSelectionCallback">Client</lli-client-search>
					</btcl-field>
					<btcl-field v-if="application.client.registrantType==1" title="Want's to Pay DN(Connection Charge)" required="true">
						<multiselect v-model="application.skipPayment" :options="[{ID:1,label:'First Month Bill'},{ID:0,label:'Before Connection'}]"
									 track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
						</multiselect>
					</btcl-field>


					<btcl-field title="Loop Provider" required="true">
						<multiselect v-model="application.loopProvider" :options="loopProviderOptionList"
									 track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom">
						</multiselect>
					</btcl-field>

					<btcl-field title="Connection" required="true">
						<multiselect v-model="application.connection" :options="connectionOptionListByClientID"
							track-by=ID label=label :allow-empty="false" :searchable=true open-direction="bottom"
							@select="connectionSelectionCallback">
						</multiselect>
					</btcl-field>

					<btcl-field title="Category" required="true" >
						<multiselect v-model="application.selectOld" :options="[{ID:1,label:'From Existing Office'},{ID:2,label:'Add new Office'}]"
									 label=label :allow-empty="false" :searchable=true open-direction="bottom">
						</multiselect>
					</btcl-field>

					<btcl-field title="Port Count" required="true">
						<input type=number class="form-control" v-model=application.portCount>
					</btcl-field>

					<btcl-field v-if  ="isOld" title="Existing Connection Office">
						<multiselect v-model="application.selectedOfficeIndex" :options="officeList"
									 label=label :allow-empty="false" :searchable=true open-direction="bottom"
									 >
						</multiselect>
					</btcl-field>
					<div v-else align=right>
						<button type=button
								@click="addOffice()"
								class="btn green-haze btn-outline">
							Add Connection Office For Local Loop
						</button>
					</div>


					<btcl-field v-if="isOld==true" title="Loop By">
						<multiselect v-model="application.isReuse" :options="[{ID:1,label:'Reuse Existing Pop'},{ID:2,label:'Add new Pop'},{ID:3,label:'Replace Existing Pop'}]"
									 label=label :allow-empty="false" :searchable=true open-direction="bottom">
						</multiselect>
					</btcl-field>

					<btcl-bounded v-if="isOld == true && isReuse!=2" v-for="(localloop,localloopIndex) in localloops" :title="'Loop '+(localloopIndex+1)"
								  :key="localloopIndex">
						<span><input type="checkbox" :value="localloopIndex" v-model ="application.selectedloopIndex" /></span>
						<btcl-input title="Loop Name" :text.sync="localloop.loopName" :readonly = true></btcl-input>
					</btcl-bounded>
					<btcl-bounded v-for="(addedOffice,addedOfficeIndex) in application.addedOfficeList" :title="'New Connection Office '+(addedOfficeIndex+1)+ ' for Local Loop'"
								  :key="addedOfficeIndex">
						<btcl-input title="Connection Office Name" :text.sync="addedOffice.officeName"></btcl-input>
						<btcl-input title="Connection Office Address" :text.sync="addedOffice.officeAddress"></btcl-input>
						<div align=right v-if="officeIndex!=0">
							<button type=button
									@click="deleteOffice(addedOfficeIndex)"
									class="btn red btn-outline">
								Remove this Connection Office
							</button>
						</div>
					</btcl-bounded>


				<%--	<div v-if="!isOld" align=right>
						<button type=button
								@click="addOffice()"
								class="btn green-haze btn-outline">
							Add Office For Local Loop
						</button>
					</div>--%>

					<btcl-input title="Description" :text.sync="application.description"></btcl-input>
					<btcl-input title="Comment" :text.sync="application.comment"></btcl-input>

					<btcl-field title="Suggested Date" required="true">
						<btcl-datepicker :date.sync="application.suggestedDate"></btcl-datepicker>
					</btcl-field>

					<div style="padding-top:30px;">
						<button type="submit" class="btn green-haze btn-block" @click="submitData">Submit</button>
					</div>


				</btcl-portlet>

			<%--</btcl-form>--%>
		</btcl-body>

</div>


<script src="../connection/lli-connection-components.js"></script>
<script src=lli-application-components.js></script>
<script src=additional-local-loop/lli-application-additional-local-loop.js></script>


